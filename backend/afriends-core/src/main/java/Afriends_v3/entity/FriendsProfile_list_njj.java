package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 好友资料列表
 * 对应实体: FriendsProfile_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class FriendsProfile_list_njj extends EntityList<FriendsProfile_njj> {

    // 内存存储
    private static final Map<Long, FriendsProfile_njj> friendsProfileCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<FriendsProfile_njj>> userFriendsCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalFriendsProfile = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<FriendsProfile_njj> allFriendsProfile) {
        try {
            System.out.println("开始直接加载好友资料数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (FriendsProfile_njj profile : allFriendsProfile) {
                System.out.println("处理好友资料数据: ID=" + profile.getFriendsProfileId() + ", 用户ID=" + profile.getUserId()
                        + ", 好友ID=" + profile.getFriendId());
                friendsProfileCache.put(profile.getFriendsProfileId(), profile);

                // 按用户ID分组存储
                userFriendsCache.computeIfAbsent(profile.getUserId(), k -> new ArrayList<>()).add(profile);
            }

            // 更新统计信息
            totalFriendsProfile = friendsProfileCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("好友资料数据直接加载完成！总数: " + totalFriendsProfile);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载好友资料数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有好友资料数据到内存
     */
    public static void loadFromDatabase(BaseMapper<FriendsProfile_njj> mapper) {
        try {
            System.out.println("开始从数据库加载好友资料数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有好友资料数据
            System.out.println("正在执行数据库查询...");
            List<FriendsProfile_njj> allFriendsProfile = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allFriendsProfile.size() + " 条记录");

            // 存储到内存缓存
            for (FriendsProfile_njj profile : allFriendsProfile) {
                System.out.println("处理好友资料数据: ID=" + profile.getFriendsProfileId() + ", 用户ID=" + profile.getUserId()
                        + ", 好友ID=" + profile.getFriendId());
                friendsProfileCache.put(profile.getFriendsProfileId(), profile);

                // 按用户ID分组存储
                userFriendsCache.computeIfAbsent(profile.getUserId(), k -> new ArrayList<>()).add(profile);
            }

            // 更新统计信息
            totalFriendsProfile = friendsProfileCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("好友资料数据加载完成！总数: " + totalFriendsProfile);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载好友资料数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有好友资料数据（从内存）
     */
    public static List<FriendsProfile_njj> getAllFriendsProfile() {
        return new ArrayList<>(friendsProfileCache.values());
    }

    /**
     * 根据好友资料ID获取好友资料数据
     */
    public static FriendsProfile_njj getFriendsProfileById(Long friendsProfileId) {
        return friendsProfileCache.get(friendsProfileId);
    }

    /**
     * 根据用户ID获取好友资料数据
     */
    public static List<FriendsProfile_njj> getFriendsProfileByUserId(Long userId) {
        return userFriendsCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据好友ID获取好友资料数据
     */
    public static List<FriendsProfile_njj> getFriendsProfileByFriendId(Long friendId) {
        List<FriendsProfile_njj> result = new ArrayList<>();
        for (FriendsProfile_njj profile : friendsProfileCache.values()) {
            if (profile.getFriendId().equals(friendId)) {
                result.add(profile);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFriendsProfile", totalFriendsProfile);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        friendsProfileCache.clear();
        userFriendsCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<FriendsProfile_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加单个好友资料到缓存
     */
    public static void addToCache(FriendsProfile_njj profile) {
        if (profile != null) {
            friendsProfileCache.put(profile.getFriendsProfileId(), profile);

            // 按用户ID分组存储
            userFriendsCache.computeIfAbsent(profile.getUserId(), k -> new ArrayList<>()).add(profile);

            // 更新统计信息
            totalFriendsProfile = friendsProfileCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    public static void removeFromCacheById(Long friendsProfileId) {
        if (friendsProfileId == null) {
            return;
        }

        FriendsProfile_njj removed = friendsProfileCache.remove(friendsProfileId);
        if (removed == null) {
            return;
        }

        if (removed.getUserId() != null) {
            List<FriendsProfile_njj> list = userFriendsCache.get(removed.getUserId());
            if (list != null) {
                list.removeIf(p -> p != null && friendsProfileId.equals(p.getFriendsProfileId()));
            }
        }

        totalFriendsProfile = friendsProfileCache.size();
        lastUpdateTime = System.currentTimeMillis();
    }
}