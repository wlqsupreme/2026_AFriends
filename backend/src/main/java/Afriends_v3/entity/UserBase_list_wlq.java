package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础信息列表
 * 对应实体: UserBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBase_list_wlq extends EntityList<UserBase_wlq> {

    // 内存存储
    private static final Map<Long, UserBase_wlq> userBaseCache = new ConcurrentHashMap<>();

    // 对外提供缓存加载状态（关键修改：将private改为public）
    // 缓存状态
    @Getter
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBase_wlq> allUserBase) {
        try {
            System.out.println("开始直接加载用户基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserBase_wlq user : allUserBase) {
                System.out.println("处理用户数据: ID=" + user.getUserId() + ", 登录账号=" + user.getLoginAccount());
                userBaseCache.put(user.getUserId(), user);
            }

            // 更新统计信息
            totalUserBase = userBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础数据直接加载完成！总数: " + totalUserBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础数据
            System.out.println("正在执行数据库查询...");
            List<UserBase_wlq> allUserBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBase.size() + " 条记录");

            // 存储到内存缓存
            for (UserBase_wlq user : allUserBase) {
                System.out.println("处理用户数据: ID=" + user.getUserId() + ", 登录账号=" + user.getLoginAccount());
                userBaseCache.put(user.getUserId(), user);
            }

            // 更新统计信息
            totalUserBase = userBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础数据加载完成！总数: " + totalUserBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户基础数据（从内存）
     */
    public static List<UserBase_wlq> getAllUserBase() {
        return new ArrayList<>(userBaseCache.values());
    }

    /**
     * 根据用户ID获取用户基础数据
     */
    public static UserBase_wlq getUserBaseById(Long userId) {
        return userBaseCache.get(userId);
    }

    /**
     * 根据登录账号搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByLoginAccount(String loginAccount) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getLoginAccount() != null && user.getLoginAccount().contains(loginAccount)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据登录电话账号搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByLoginTelAccount(String loginTelAccount) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getLoginTelAccount() != null && user.getLoginTelAccount().contains(loginTelAccount)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据登录微信账号搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByLoginWechatAccount(String loginWechatAccount) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getLoginWechatAccount() != null && user.getLoginWechatAccount().contains(loginWechatAccount)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据登录QQ账号搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByLoginQqAccount(String loginQqAccount) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getLoginQqAccount() != null && user.getLoginQqAccount().contains(loginQqAccount)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据绑定抖音账号搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByLoginDouyinAccount(String boundDouyinAccount) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getBoundDouyinAccount() != null && user.getBoundDouyinAccount().contains(boundDouyinAccount)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据用户类型搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByUserKind(String userKind) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getUserKind() != null && user.getUserKind().contains(userKind)) {
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 根据用户ID范围搜索用户基础数据
     */
    public static List<UserBase_wlq> searchUserBaseByUserIdRange(Long minUserId, Long maxUserId) {
        List<UserBase_wlq> result = new ArrayList<>();
        for (UserBase_wlq user : userBaseCache.values()) {
            if (user.getUserId() != null) {
                if (minUserId != null && user.getUserId() < minUserId) {
                    continue;
                }
                if (maxUserId != null && user.getUserId() > maxUserId) {
                    continue;
                }
                result.add(user);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserBase", totalUserBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }

    // 新增：添加用户到缓存
    public static void addUserBase(UserBase_wlq user) {
        if (user != null && user.getUserId() != null) {
            userBaseCache.put(user.getUserId(), user);
        }
    }

    // 新增：更新缓存中的用户信息
    public static void updateUserBase(UserBase_wlq user) {
        if (user != null && user.getUserId() != null) {
            userBaseCache.put(user.getUserId(), user);
        }
    }
}