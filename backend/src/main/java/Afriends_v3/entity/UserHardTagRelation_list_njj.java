package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户硬标签关系信息列表
 * 对应实体: UserHardTagRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserHardTagRelation_list_njj extends EntityList<UserHardTagRelation_njj> {

    // 内存存储
    private static final Map<Long, UserHardTagRelation_njj> userHardTagRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserHardTagRelation_njj>> userTagCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserHardTagRelation_njj>> tagUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserHardTagRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserHardTagRelation_njj> allUserHardTagRelation) {
        try {
            System.out.println("开始直接加载用户硬标签关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserHardTagRelation == null || allUserHardTagRelation.isEmpty()) {
                System.out.println("用户硬标签关系数据为空，清空缓存并设置状态");
                totalUserHardTagRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户硬标签关系数据直接加载完成！总数: " + totalUserHardTagRelation);
                return;
            }

            // 存储到内存缓存
            for (UserHardTagRelation_njj relation : allUserHardTagRelation) {
                if (relation != null && relation.getHardRelationId() != null) {
                    System.out.println(
                            "处理用户硬标签关系数据: ID=" + relation.getHardRelationId() + ", 用户ID=" + relation.getUserId());
                    userHardTagRelationCache.put(relation.getHardRelationId(), relation);

                    // 按用户ID分组存储
                    userTagCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按标签ID分组存储
                    tagUserCache.computeIfAbsent(relation.getHardTagId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserHardTagRelation = userHardTagRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户硬标签关系数据直接加载完成！总数: " + totalUserHardTagRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户硬标签关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserHardTagRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户硬标签关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserHardTagRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户硬标签关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户硬标签关系数据
            System.out.println("正在执行数据库查询...");
            List<UserHardTagRelation_njj> allUserHardTagRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserHardTagRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserHardTagRelation_njj relation : allUserHardTagRelation) {
                if (relation != null && relation.getHardRelationId() != null) {
                    System.out.println(
                            "处理用户硬标签关系数据: ID=" + relation.getHardRelationId() + ", 用户ID=" + relation.getUserId());
                    userHardTagRelationCache.put(relation.getHardRelationId(), relation);

                    // 按用户ID分组存储
                    userTagCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按标签ID分组存储
                    tagUserCache.computeIfAbsent(relation.getHardTagId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserHardTagRelation = userHardTagRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户硬标签关系数据加载完成！总数: " + totalUserHardTagRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户硬标签关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户硬标签关系数据（从内存）
     */
    public static List<UserHardTagRelation_njj> getAllUserHardTagRelation() {
        return new ArrayList<>(userHardTagRelationCache.values());
    }

    /**
     * 根据关系ID获取用户硬标签关系数据
     */
    public static UserHardTagRelation_njj getUserHardTagRelationById(Long hardRelationId) {
        return userHardTagRelationCache.get(hardRelationId);
    }

    /**
     * 根据用户ID获取用户硬标签关系数据
     */
    public static List<UserHardTagRelation_njj> getUserHardTagRelationByUserId(Long userId) {
        return userTagCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据硬标签ID获取用户硬标签关系数据
     */
    public static List<UserHardTagRelation_njj> getUserHardTagRelationByHardTagId(Long hardTagId) {
        return tagUserCache.getOrDefault(hardTagId, new ArrayList<>());
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserHardTagRelation", totalUserHardTagRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userHardTagRelationCache.clear();
        userTagCache.clear();
        tagUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserHardTagRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
