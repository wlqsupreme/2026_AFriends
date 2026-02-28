package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础信息列表（内存缓存）
 * 对应实体: UserInfo
 * 功能：从数据库读取用户数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserInfoList extends EntityList<UserInfo> {

    // 内存存储（key: userId，value: 用户实体）
    private static final Map<Long, UserInfo> userInfoCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserInfo = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<UserInfo> allUserInfo) {
        try {
            System.out.println("开始直接加载用户基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserInfo user : allUserInfo) {
                System.out.println("处理用户数据: ID=" + user.getId() + ", 手机号=" + user.getPhone());
                userInfoCache.put(user.getId(), user);
            }

            // 更新统计信息
            totalUserInfo = userInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础数据直接加载完成！总数: " + totalUserInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserInfo> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础数据
            System.out.println("正在执行数据库查询...");
            List<UserInfo> allUserInfo = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserInfo.size() + " 条记录");

            // 存储到内存缓存
            for (UserInfo user : allUserInfo) {
                System.out.println("处理用户数据: ID=" + user.getId() + ", 手机号=" + user.getPhone());
                userInfoCache.put(user.getId(), user);
            }

            // 更新统计信息
            totalUserInfo = userInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础数据加载完成！总数: " + totalUserInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户（从内存）
     */
    public static List<UserInfo> getAllUserInfo() {
        return new ArrayList<>(userInfoCache.values());
    }

    /**
     * 根据用户ID获取用户信息
     */
    public static UserInfo getUserInfoById(Long userId) {
        return userInfoCache.get(userId);
    }

    /**
     * 根据手机号查询用户（登录场景）
     */
    public static UserInfo getUserInfoByPhone(String phone) {
        for (UserInfo user : userInfoCache.values()) {
            if (phone.equals(user.getPhone())) {
                return user;
            }
        }
        return null;
    }

    /**
     * 检查用户账号状态是否正常
     */
    public static boolean isUserStatusNormal(Long userId) {
        UserInfo user = userInfoCache.get(userId);
        return user != null && user.getStatus() == 1;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserInfo", totalUserInfo);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userInfoCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserInfo> mapper) {
        loadFromDatabase(mapper);
    }
}