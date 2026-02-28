package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户点赞关系信息列表
 * 对应实体: UserLikeRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserLikeRelation_list_njj extends EntityList<UserLikeRelation_njj> {

    // 内存存储
    private static final Map<Long, UserLikeRelation_njj> userLikeRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserLikeRelation_njj>> userLikeCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserLikeRelation_njj>> likedUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserLikeRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserLikeRelation_njj> allUserLikeRelation) {
        try {
            System.out.println("开始直接加载用户点赞关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserLikeRelation == null || allUserLikeRelation.isEmpty()) {
                System.out.println("用户点赞关系数据为空，清空缓存并设置状态");
                totalUserLikeRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户点赞关系数据直接加载完成！总数: " + totalUserLikeRelation);
                return;
            }

            // 存储到内存缓存
            for (UserLikeRelation_njj relation : allUserLikeRelation) {
                if (relation != null && relation.getLikeId() != null) {
                    System.out
                            .println("处理用户点赞关系数据: ID=" + relation.getLikeId() + ", 点赞者ID=" + relation.getLikerUserId());
                    userLikeRelationCache.put(relation.getLikeId(), relation);

                    // 按点赞者用户ID分组存储
                    userLikeCache.computeIfAbsent(relation.getLikerUserId(), k -> new ArrayList<>()).add(relation);

                    // 按被点赞者用户ID分组存储
                    likedUserCache.computeIfAbsent(relation.getLikedUserId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserLikeRelation = userLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户点赞关系数据直接加载完成！总数: " + totalUserLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserLikeRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户点赞关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserLikeRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户点赞关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户点赞关系数据
            System.out.println("正在执行数据库查询...");
            List<UserLikeRelation_njj> allUserLikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserLikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserLikeRelation_njj relation : allUserLikeRelation) {
                if (relation != null && relation.getLikeId() != null) {
                    System.out
                            .println("处理用户点赞关系数据: ID=" + relation.getLikeId() + ", 点赞者ID=" + relation.getLikerUserId());
                    userLikeRelationCache.put(relation.getLikeId(), relation);

                    // 按点赞者用户ID分组存储
                    userLikeCache.computeIfAbsent(relation.getLikerUserId(), k -> new ArrayList<>()).add(relation);

                    // 按被点赞者用户ID分组存储
                    likedUserCache.computeIfAbsent(relation.getLikedUserId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserLikeRelation = userLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户点赞关系数据加载完成！总数: " + totalUserLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户点赞关系数据（从内存）
     */
    public static List<UserLikeRelation_njj> getAllUserLikeRelation() {
        return new ArrayList<>(userLikeRelationCache.values());
    }

    /**
     * 根据点赞ID获取用户点赞关系数据
     */
    public static UserLikeRelation_njj getUserLikeRelationById(Long likeId) {
        return userLikeRelationCache.get(likeId);
    }

    /**
     * 根据点赞者用户ID获取用户点赞关系数据
     */
    public static List<UserLikeRelation_njj> getUserLikeRelationByLikerUserId(Long likerUserId) {
        return userLikeCache.getOrDefault(likerUserId, new ArrayList<>());
    }

    /**
     * 根据被点赞者用户ID获取用户点赞关系数据
     */
    public static List<UserLikeRelation_njj> getUserLikeRelationByLikedUserId(Long likedUserId) {
        return likedUserCache.getOrDefault(likedUserId, new ArrayList<>());
    }

    /**
     * 根据活跃状态获取用户点赞关系数据
     */
    public static List<UserLikeRelation_njj> getUserLikeRelationByIsActive(Byte isActive) {
        List<UserLikeRelation_njj> result = new ArrayList<>();
        for (UserLikeRelation_njj relation : userLikeRelationCache.values()) {
            if (relation.getIsActive() != null && relation.getIsActive().equals(isActive)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserLikeRelation", totalUserLikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userLikeRelationCache.clear();
        userLikeCache.clear();
        likedUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserLikeRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
