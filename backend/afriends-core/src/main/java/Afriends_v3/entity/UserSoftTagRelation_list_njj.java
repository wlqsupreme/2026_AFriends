package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户软标签关系信息列表
 * 对应实体: UserSoftTagRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserSoftTagRelation_list_njj extends EntityList<UserSoftTagRelation_njj> {

    // 内存存储
    private static final Map<Long, UserSoftTagRelation_njj> userSoftTagRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSoftTagRelation_njj>> userSoftTagCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSoftTagRelation_njj>> tagUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserSoftTagRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserSoftTagRelation_njj> allUserSoftTagRelation) {
        try {
            System.out.println("开始直接加载用户软标签关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserSoftTagRelation == null || allUserSoftTagRelation.isEmpty()) {
                System.out.println("用户软标签关系数据为空，清空缓存并设置状态");
                totalUserSoftTagRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户软标签关系数据直接加载完成！总数: " + totalUserSoftTagRelation);
                return;
            }

            // 存储到内存缓存
            for (UserSoftTagRelation_njj relation : allUserSoftTagRelation) {
                if (relation != null && relation.getSoftRelationId() != null) {
                    System.out.println(
                            "处理用户软标签关系数据: ID=" + relation.getSoftRelationId() + ", 用户ID=" + relation.getUserId());
                    userSoftTagRelationCache.put(relation.getSoftRelationId(), relation);

                    // 按用户ID分组存储
                    userSoftTagCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按软标签ID分组存储
                    tagUserCache.computeIfAbsent(relation.getSoftTagId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserSoftTagRelation = userSoftTagRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户软标签关系数据直接加载完成！总数: " + totalUserSoftTagRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户软标签关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserSoftTagRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户软标签关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserSoftTagRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户软标签关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户软标签关系数据
            System.out.println("正在执行数据库查询...");
            List<UserSoftTagRelation_njj> allUserSoftTagRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserSoftTagRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserSoftTagRelation_njj relation : allUserSoftTagRelation) {
                if (relation != null && relation.getSoftRelationId() != null) {
                    System.out.println(
                            "处理用户软标签关系数据: ID=" + relation.getSoftRelationId() + ", 用户ID=" + relation.getUserId());
                    userSoftTagRelationCache.put(relation.getSoftRelationId(), relation);

                    // 按用户ID分组存储
                    userSoftTagCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按软标签ID分组存储
                    tagUserCache.computeIfAbsent(relation.getSoftTagId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserSoftTagRelation = userSoftTagRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户软标签关系数据加载完成！总数: " + totalUserSoftTagRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户软标签关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户软标签关系数据（从内存）
     */
    public static List<UserSoftTagRelation_njj> getAllUserSoftTagRelation() {
        return new ArrayList<>(userSoftTagRelationCache.values());
    }

    /**
     * 根据关系ID获取用户软标签关系数据
     */
    public static UserSoftTagRelation_njj getUserSoftTagRelationById(Long softRelationId) {
        return userSoftTagRelationCache.get(softRelationId);
    }

    /**
     * 根据用户ID获取用户软标签关系数据
     */
    public static List<UserSoftTagRelation_njj> getUserSoftTagRelationByUserId(Long userId) {
        return userSoftTagCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据软标签ID获取用户软标签关系数据
     */
    public static List<UserSoftTagRelation_njj> getUserSoftTagRelationBySoftTagId(Long softTagId) {
        return tagUserCache.getOrDefault(softTagId, new ArrayList<>());
    }

    /**
     * 根据相关性评分范围获取用户软标签关系数据
     */
    public static List<UserSoftTagRelation_njj> getUserSoftTagRelationByRelevanceScoreRange(
            java.math.BigDecimal minScore, java.math.BigDecimal maxScore) {
        List<UserSoftTagRelation_njj> result = new ArrayList<>();
        for (UserSoftTagRelation_njj relation : userSoftTagRelationCache.values()) {
            if (relation.getSoftRelevanceScore() != null) {
                if (minScore != null && relation.getSoftRelevanceScore().compareTo(minScore) < 0)
                    continue;
                if (maxScore != null && relation.getSoftRelevanceScore().compareTo(maxScore) > 0)
                    continue;
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据相关性评分排序获取用户软标签关系数据
     */
    public static List<UserSoftTagRelation_njj> getUserSoftTagRelationSortedByRelevanceScore() {
        List<UserSoftTagRelation_njj> result = new ArrayList<>(userSoftTagRelationCache.values());
        result.sort((a, b) -> {
            if (a.getSoftRelevanceScore() == null && b.getSoftRelevanceScore() == null)
                return 0;
            if (a.getSoftRelevanceScore() == null)
                return 1;
            if (b.getSoftRelevanceScore() == null)
                return -1;
            return b.getSoftRelevanceScore().compareTo(a.getSoftRelevanceScore()); // 降序
        });
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserSoftTagRelation", totalUserSoftTagRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userSoftTagRelationCache.clear();
        userSoftTagCache.clear();
        tagUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserSoftTagRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
