package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户信息列表
 * 对应实体: UserInfo_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserInfo_list_njj extends EntityList<UserInfo_njj> {

    // 内存存储
    private static final Map<Long, UserInfo_njj> userCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUsers = 0;

    /**
     * 从数据库加载所有用户数据到内存
     */
    public static void loadFromDatabase(IService<UserInfo_njj> userService) {
        loadFromDatabaseWithLimit(userService, Integer.MAX_VALUE);
    }

    /**
     * 从数据库查询结果直接加载数据到内存缓存
     * @param allUser 从数据库查询的所有用户列表
     */
    public static void loadFromDatabaseDirectly(List<UserInfo_njj> allUser) {
        // 清空旧缓存
        userCache.clear();

        // 将用户数据存入Map（key=userId，便于快速查询）
        for (UserInfo_njj user : allUser) {
            if (user != null && user.getUserId() != null) {  // 过滤无效数据
                userCache.put(user.getUserId(), user);
            }
        }

        // 更新缓存状态
        isCacheLoaded = true;
        lastUpdateTime = System.currentTimeMillis();
        System.out.println("UserInfo_list_njj: 内存缓存加载完成，共 " + userCache.size() + " 个用户");
    }

    /**
     * 从数据库加载指定数量的用户数据到内存
     */
    public static void loadFromDatabaseWithLimit(IService<UserInfo_njj> userService, int limit) {
        try {
            System.out.println("开始从数据库加载用户数据（限制" + limit + "条）...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询指定数量的用户，按用户ID升序排列
            // 明确排除有问题的user_kind字段，避免中文枚举问题
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserInfo_njj> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.select("user_id", "real_name", "age", "gender", "username", "profile_pic_url",
                            "appearance", "identity", "unit", "personality", "interests", "location",
                            "created_at", "updated_at", "gold", "diamond", "level", "feature_vector",
                            "soft_tags", "hard_tags", "real_name_verified", "job_verified", "student_verified",
                            "bio", "homepage_bg_url")
                    .orderByAsc("user_id")
                    .last("LIMIT " + limit);

            List<UserInfo_njj> limitedUsers = userService.list(queryWrapper);
            System.out.println("查询到的用户数量: " + (limitedUsers != null ? limitedUsers.size() : 0));

            // 存储到内存缓存
            if (limitedUsers != null && !limitedUsers.isEmpty()) {
                for (UserInfo_njj user : limitedUsers) {
                    if (user != null && user.getUserId() != null) {
                        userCache.put(user.getUserId(), user);
                        System.out.println("存储用户: " + user.getUserId() + " - " + user.getUsername());
                    }
                }
                System.out.println("缓存中的用户数量: " + userCache.size());
            } else {
                System.out.println("没有查询到用户数据");
            }

            // 更新统计信息
            totalUsers = userCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户数据加载完成！实际加载: " + totalUsers + " 条");
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", totalUsers=" + totalUsers);

        } catch (Exception e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户（从内存）
     */
    public static List<UserInfo_njj> getAllUsers() {
        return new ArrayList<>(userCache.values());
    }

    /**
     * 根据用户ID获取用户
     */
    public static UserInfo_njj getUserById(Long userId) {
        System.out.println("hq记录: ID=" + userId);
        return userCache.get(userId);
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        // 直接从缓存获取实时数据，而不是依赖静态变量
        stats.put("totalUsers", userCache.size());
        // 如果缓存中有数据，则认为已加载
        stats.put("cacheLoaded", !userCache.isEmpty() || isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(IService<UserInfo_njj> userService) {
        loadFromDatabase(userService);
    }

    /**
     * 更新缓存中的用户信息
     */
    public static void updateUserInfoInCache(UserInfo_njj userInfo) {
        if (userInfo != null && userInfo.getUserId() != null) {
            userCache.put(userInfo.getUserId(), userInfo);
            lastUpdateTime = System.currentTimeMillis();
            System.out.println("缓存中的用户信息已更新，userId: " + userInfo.getUserId());
        }
    }

    /**
     * 根据用户名获取用户
     */
    public static UserInfo_njj getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        // 遍历缓存查找匹配用户名的用户
        for (UserInfo_njj user : userCache.values()) {
            if (user != null && username.equals(user.getUsername())) {
                return user;
            }
        }

        return null;
    }
}