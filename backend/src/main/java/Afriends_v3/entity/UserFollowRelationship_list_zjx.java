package Afriends_v3.entity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户关注关系信息列表
 * 对应实体: UserFollowRelationship_zjx
 * 功能：从数据库读取关注关系数据并存储到内存中，提供快速查询能力
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserFollowRelationship_list_zjx extends EntityList<UserFollowRelationship_zjx> {

    // 内存存储：关注关系ID -> 关注关系实体
    private static final Map<Long, UserFollowRelationship_zjx> followRelationshipCache = new ConcurrentHashMap<>();
    // 内存存储：用户ID -> 该用户关注的所有关系（我关注了谁）
    private static final Map<Long, List<UserFollowRelationship_zjx>> userFollowCache = new ConcurrentHashMap<>();
    // 内存存储：被关注用户ID -> 关注该用户的所有关系（谁关注了我）
    private static final Map<Long, List<UserFollowRelationship_zjx>> followedUserCache = new ConcurrentHashMap<>();

    // 缓存状态标识
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalFollowRelationship = 0;

    /**
     * 直接从内存列表加载数据到缓存（适用于已通过原生SQL查询的场景）
     */
    public static void loadFromDatabaseDirectly(List<UserFollowRelationship_zjx> allFollowRelationships) {
        try {
            System.out.println("开始直接加载用户关注关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 处理空数据场景
            if (allFollowRelationships == null || allFollowRelationships.isEmpty()) {
                System.out.println("用户关注关系数据为空，已清空缓存");
                totalFollowRelationship = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户关注关系数据加载完成！总数: " + totalFollowRelationship);
                return;
            }

            // 存储到内存缓存
            for (UserFollowRelationship_zjx relationship : allFollowRelationships) {
                if (relationship != null && relationship.getFollowId() != null) {
                    System.out.println("处理关注关系数据: ID=" + relationship.getFollowId() +
                            ", 用户ID=" + relationship.getUserId() +
                            ", 被关注用户ID=" + relationship.getFollowedUserId());

                    // 主键映射缓存
                    followRelationshipCache.put(relationship.getFollowId(), relationship);

                    // 按"关注者"分组（我关注了谁）
                    userFollowCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>())
                            .add(relationship);

                    // 按"被关注者"分组（谁关注了我）
                    followedUserCache.computeIfAbsent(relationship.getFollowedUserId(), k -> new ArrayList<>())
                            .add(relationship);
                }
            }

            // 更新统计信息
            totalFollowRelationship = followRelationshipCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户关注关系数据直接加载完成！总数: " + totalFollowRelationship);
            System.out.println("缓存状态: 已加载=" + isCacheLoaded + ", 最后更新时间=" + new Date(lastUpdateTime));

        } catch (Exception e) {
            System.err.println("直接加载用户关注关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 异常时重置状态
            totalFollowRelationship = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 通过MyBatis-Plus Mapper从数据库加载所有关注关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserFollowRelationship_zjx> mapper) {
        try {
            System.out.println("开始从数据库加载用户关注关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有数据
            System.out.println("执行数据库查询...");
            List<UserFollowRelationship_zjx> allFollowRelationships = mapper.selectList(null);
            System.out.println("数据库查询完成，共获取 " + allFollowRelationships.size() + " 条记录");

            // 存储到内存缓存
            for (UserFollowRelationship_zjx relationship : allFollowRelationships) {
                if (relationship != null && relationship.getFollowId() != null) {
                    System.out.println("处理关注关系数据: ID=" + relationship.getFollowId() +
                            ", 用户ID=" + relationship.getUserId() +
                            ", 被关注用户ID=" + relationship.getFollowedUserId());

                    followRelationshipCache.put(relationship.getFollowId(), relationship);
                    userFollowCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>())
                            .add(relationship);
                    followedUserCache.computeIfAbsent(relationship.getFollowedUserId(), k -> new ArrayList<>())
                            .add(relationship);
                }
            }

            // 更新统计信息
            totalFollowRelationship = followRelationshipCache.size();
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户关注关系数据加载完成！总数: " + totalFollowRelationship);
            System.out.println("缓存状态: 已加载=" + isCacheLoaded + ", 最后更新时间=" + new Date(lastUpdateTime));

        } catch (Exception e) {
            System.err.println("从数据库加载用户关注关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有关注关系数据（从内存缓存）
     */
    public static List<UserFollowRelationship_zjx> getAllFollowRelationships() {
        return new ArrayList<>(followRelationshipCache.values());
    }

    /**
     * 根据关注关系ID获取单个关注关系
     */
    public static UserFollowRelationship_zjx getFollowRelationshipById(Long followId) {
        return followRelationshipCache.get(followId);
    }

    /**
     * 根据用户ID获取该用户的所有关注关系（我关注了谁）
     */
    public static List<UserFollowRelationship_zjx> getFollowRelationshipsByUserId(Long userId) {
        return userFollowCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据被关注用户ID获取所有关注者关系（谁关注了我）
     */
    public static List<UserFollowRelationship_zjx> getFollowRelationshipsByFollowedUserId(Long followedUserId) {
        return followedUserCache.getOrDefault(followedUserId, new ArrayList<>());
    }

    /**
     * 检查用户是否已关注目标用户（用于前端判断"已关注"状态）
     * @param userId 关注者ID
     * @param followedUserId 被关注者ID
     * @return true=已关注，false=未关注
     */
    public static boolean isFollowing(Long userId, Long followedUserId) {
        List<UserFollowRelationship_zjx> userFollows = userFollowCache.get(userId);
        if (userFollows == null || userFollows.isEmpty()) {
            return false;
        }
        for (UserFollowRelationship_zjx relationship : userFollows) {
            if (followedUserId.equals(relationship.getFollowedUserId()) && relationship.getIsActive() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取关注关系统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("总关注关系数", totalFollowRelationship);
        stats.put("缓存加载状态", isCacheLoaded ? "已加载" : "未加载");
        stats.put("最后更新时间", new Date(lastUpdateTime));
        stats.put("用户关注缓存数", userFollowCache.size());
        stats.put("被关注用户缓存数", followedUserCache.size());
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        followRelationshipCache.clear();
        userFollowCache.clear();
        followedUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存（重新从数据库加载）
     */
    public static void forceRefresh(BaseMapper<UserFollowRelationship_zjx> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 更新缓存中的关注关系记录（适用于修改操作）
     */
    public static void updateInCache(UserFollowRelationship_zjx relationship) {
        if (relationship != null && relationship.getFollowId() != null) {
            followRelationshipCache.put(relationship.getFollowId(), relationship);
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    /**
     * 添加新的关注关系到缓存（适用于新增操作）
     */
    public static void addToCache(UserFollowRelationship_zjx relationship) {
        if (relationship != null && relationship.getFollowId() != null) {
            followRelationshipCache.put(relationship.getFollowId(), relationship);

            // 更新用户关注列表缓存
            Long userId = relationship.getUserId();
            Long followedUserId = relationship.getFollowedUserId();

            userFollowCache.computeIfAbsent(userId, k -> new ArrayList<>()).add(relationship);
            followedUserCache.computeIfAbsent(followedUserId, k -> new ArrayList<>()).add(relationship);

            totalFollowRelationship = followRelationshipCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    /**
     * 从缓存中移除关注关系（适用于删除/取消关注操作）
     */
    public static void removeFromCache(Long followId) {
        if (followId == null) return;

        UserFollowRelationship_zjx relationship = followRelationshipCache.remove(followId);
        if (relationship != null) {
            // 从用户关注列表中移除
            List<UserFollowRelationship_zjx> userFollows = userFollowCache.get(relationship.getUserId());
            if (userFollows != null) {
                userFollows.removeIf(r -> followId.equals(r.getFollowId()));
                if (userFollows.isEmpty()) {
                    userFollowCache.remove(relationship.getUserId());
                }
            }

            // 从被关注列表中移除
            List<UserFollowRelationship_zjx> followedBy = followedUserCache.get(relationship.getFollowedUserId());
            if (followedBy != null) {
                followedBy.removeIf(r -> followId.equals(r.getFollowId()));
                if (followedBy.isEmpty()) {
                    followedUserCache.remove(relationship.getFollowedUserId());
                }
            }

            totalFollowRelationship = followRelationshipCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    /**
     * 获取缓存加载状态
     */
    public static boolean isCacheLoaded() {
        return isCacheLoaded;
    }
}