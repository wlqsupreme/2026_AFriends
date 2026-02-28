package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户好友关系信息列表
 * 对应实体: UserFriendsRelationship_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserFriendsRelationship_list_njj extends EntityList<UserFriendsRelationship_njj> {

    // 内存存储
    private static final Map<Long, UserFriendsRelationship_njj> userFriendsRelationshipCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserFriendsRelationship_njj>> userFriendsCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserFriendsRelationship_njj>> friendUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserFriendsRelationship = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserFriendsRelationship_njj> allUserFriendsRelationship) {
        try {
            System.out.println("开始直接加载用户好友关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserFriendsRelationship == null || allUserFriendsRelationship.isEmpty()) {
                System.out.println("用户好友关系数据为空，清空缓存并设置状态");
                totalUserFriendsRelationship = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户好友关系数据直接加载完成！总数: " + totalUserFriendsRelationship);
                return;
            }

            // 存储到内存缓存
            for (UserFriendsRelationship_njj relationship : allUserFriendsRelationship) {
                if (relationship != null && relationship.getUserFriendsInfoId() != null) {
                    System.out.println("处理用户好友关系数据: ID=" + relationship.getUserFriendsInfoId() + ", 用户ID="
                            + relationship.getUserId());
                    userFriendsRelationshipCache.put(relationship.getUserFriendsInfoId(), relationship);

                    // 按用户ID分组存储
                    userFriendsCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>())
                            .add(relationship);

                    // 按好友ID分组存储
                    friendUserCache.computeIfAbsent(relationship.getFriendsId(), k -> new ArrayList<>())
                            .add(relationship);
                }
            }

            // 更新统计信息
            totalUserFriendsRelationship = userFriendsRelationshipCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户好友关系数据直接加载完成！总数: " + totalUserFriendsRelationship);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户好友关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserFriendsRelationship = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户好友关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserFriendsRelationship_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户好友关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户好友关系数据
            System.out.println("正在执行数据库查询...");
            List<UserFriendsRelationship_njj> allUserFriendsRelationship = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserFriendsRelationship.size() + " 条记录");

            // 存储到内存缓存
            for (UserFriendsRelationship_njj relationship : allUserFriendsRelationship) {
                if (relationship != null && relationship.getUserFriendsInfoId() != null) {
                    System.out.println("处理用户好友关系数据: ID=" + relationship.getUserFriendsInfoId() + ", 用户ID="
                            + relationship.getUserId());
                    userFriendsRelationshipCache.put(relationship.getUserFriendsInfoId(), relationship);

                    // 按用户ID分组存储
                    userFriendsCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>())
                            .add(relationship);

                    // 按好友ID分组存储
                    friendUserCache.computeIfAbsent(relationship.getFriendsId(), k -> new ArrayList<>())
                            .add(relationship);
                }
            }

            // 更新统计信息
            totalUserFriendsRelationship = userFriendsRelationshipCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户好友关系数据加载完成！总数: " + totalUserFriendsRelationship);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户好友关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户好友关系数据（从内存）
     */
    public static List<UserFriendsRelationship_njj> getAllUserFriendsRelationship() {
        return new ArrayList<>(userFriendsRelationshipCache.values());
    }

    /**
     * 根据好友关系ID获取用户好友关系数据
     */
    public static UserFriendsRelationship_njj getUserFriendsRelationshipById(Long userFriendsInfoId) {
        return userFriendsRelationshipCache.get(userFriendsInfoId);
    }

    /**
     * 根据用户ID获取用户好友关系数据
     */
    public static List<UserFriendsRelationship_njj> getUserFriendsRelationshipByUserId(Long userId) {
        return userFriendsCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据好友ID获取用户好友关系数据
     */
    public static List<UserFriendsRelationship_njj> getUserFriendsRelationshipByFriendsId(Long friendsId) {
        return friendUserCache.getOrDefault(friendsId, new ArrayList<>());
    }

    /**
     * 根据功能ID获取用户好友关系数据
     */
    public static List<UserFriendsRelationship_njj> getUserFriendsRelationshipByFunctionId(Long functionId) {
        List<UserFriendsRelationship_njj> result = new ArrayList<>();
        for (UserFriendsRelationship_njj relationship : userFriendsRelationshipCache.values()) {
            if (relationship.getFunctionId() != null && relationship.getFunctionId().equals(functionId)) {
                result.add(relationship);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserFriendsRelationship", totalUserFriendsRelationship);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userFriendsRelationshipCache.clear();
        userFriendsCache.clear();
        friendUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserFriendsRelationship_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 更新缓存中的记录
     */
    public static void updateInCache(UserFriendsRelationship_njj relationship) {
        if (relationship != null) {
            userFriendsRelationshipCache.put(relationship.getUserFriendsInfoId(), relationship);
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    /**
     * 添加新记录到缓存
     */
    public static void addToCache(UserFriendsRelationship_njj relationship) {
        if (relationship != null) {
            userFriendsRelationshipCache.put(relationship.getUserFriendsInfoId(), relationship);
            
            // 更新用户好友缓存
            Long userId = relationship.getUserId();
            Long friendId = relationship.getFriendsId();
            
            userFriendsCache.computeIfAbsent(userId, k -> new ArrayList<>()).add(relationship);
            friendUserCache.computeIfAbsent(friendId, k -> new ArrayList<>()).add(relationship);
            
            totalUserFriendsRelationship = userFriendsRelationshipCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    public static void removeFromCacheById(Long userFriendsInfoId) {
        if (userFriendsInfoId == null) {
            return;
        }

        UserFriendsRelationship_njj removed = userFriendsRelationshipCache.remove(userFriendsInfoId);
        if (removed == null) {
            return;
        }

        if (removed.getUserId() != null) {
            List<UserFriendsRelationship_njj> userList = userFriendsCache.get(removed.getUserId());
            if (userList != null) {
                userList.removeIf(r -> r != null && userFriendsInfoId.equals(r.getUserFriendsInfoId()));
            }
        }

        if (removed.getFriendsId() != null) {
            List<UserFriendsRelationship_njj> friendList = friendUserCache.get(removed.getFriendsId());
            if (friendList != null) {
                friendList.removeIf(r -> r != null && userFriendsInfoId.equals(r.getUserFriendsInfoId()));
            }
        }

        totalUserFriendsRelationship = userFriendsRelationshipCache.size();
        lastUpdateTime = System.currentTimeMillis();
    }
}
