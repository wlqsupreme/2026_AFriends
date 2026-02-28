package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户不喜欢关系信息列表
 * 对应实体: UserDislikeRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserDislikeRelation_list_njj extends EntityList<UserDislikeRelation_njj> {

    // 内存存储
    private static final Map<Long, UserDislikeRelation_njj> userDislikeRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserDislikeRelation_njj>> userDislikeCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserDislikeRelation_njj>> dislikedUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserDislikeRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserDislikeRelation_njj> allUserDislikeRelation) {
        try {
            System.out.println("开始直接加载用户不喜欢关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserDislikeRelation == null || allUserDislikeRelation.isEmpty()) {
                System.out.println("用户不喜欢关系数据为空，清空缓存并设置状态");
                totalUserDislikeRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户不喜欢关系数据直接加载完成！总数: " + totalUserDislikeRelation);
                return;
            }

            // 存储到内存缓存
            for (UserDislikeRelation_njj relation : allUserDislikeRelation) {
                if (relation != null && relation.getDislikeId() != null) {
                    System.out.println(
                            "处理用户不喜欢关系数据: ID=" + relation.getDislikeId() + ", 不喜欢者ID=" + relation.getDislikerUserId());
                    userDislikeRelationCache.put(relation.getDislikeId(), relation);

                    // 按不喜欢者用户ID分组存储
                    userDislikeCache.computeIfAbsent(relation.getDislikerUserId(), k -> new ArrayList<>())
                            .add(relation);

                    // 按被不喜欢者用户ID分组存储
                    dislikedUserCache.computeIfAbsent(relation.getDislikedUserId(), k -> new ArrayList<>())
                            .add(relation);
                }
            }

            // 更新统计信息
            totalUserDislikeRelation = userDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户不喜欢关系数据直接加载完成！总数: " + totalUserDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户不喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserDislikeRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户不喜欢关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserDislikeRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户不喜欢关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户不喜欢关系数据
            System.out.println("正在执行数据库查询...");
            List<UserDislikeRelation_njj> allUserDislikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserDislikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserDislikeRelation_njj relation : allUserDislikeRelation) {
                if (relation != null && relation.getDislikeId() != null) {
                    System.out.println(
                            "处理用户不喜欢关系数据: ID=" + relation.getDislikeId() + ", 不喜欢者ID=" + relation.getDislikerUserId());
                    userDislikeRelationCache.put(relation.getDislikeId(), relation);

                    // 按不喜欢者用户ID分组存储
                    userDislikeCache.computeIfAbsent(relation.getDislikerUserId(), k -> new ArrayList<>())
                            .add(relation);

                    // 按被不喜欢者用户ID分组存储
                    dislikedUserCache.computeIfAbsent(relation.getDislikedUserId(), k -> new ArrayList<>())
                            .add(relation);
                }
            }

            // 更新统计信息
            totalUserDislikeRelation = userDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户不喜欢关系数据加载完成！总数: " + totalUserDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户不喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户不喜欢关系数据（从内存）
     */
    public static List<UserDislikeRelation_njj> getAllUserDislikeRelation() {
        return new ArrayList<>(userDislikeRelationCache.values());
    }

    /**
     * 根据不喜欢ID获取用户不喜欢关系数据
     */
    public static UserDislikeRelation_njj getUserDislikeRelationById(Long dislikeId) {
        return userDislikeRelationCache.get(dislikeId);
    }

    /**
     * 根据不喜欢者用户ID获取用户不喜欢关系数据
     */
    public static List<UserDislikeRelation_njj> getUserDislikeRelationByDislikerUserId(Long dislikerUserId) {
        return userDislikeCache.getOrDefault(dislikerUserId, new ArrayList<>());
    }

    /**
     * 根据被不喜欢者用户ID获取用户不喜欢关系数据
     */
    public static List<UserDislikeRelation_njj> getUserDislikeRelationByDislikedUserId(Long dislikedUserId) {
        return dislikedUserCache.getOrDefault(dislikedUserId, new ArrayList<>());
    }

    /**
     * 根据活跃状态获取用户不喜欢关系数据
     */
    public static List<UserDislikeRelation_njj> getUserDislikeRelationByIsActive(Byte isActive) {
        List<UserDislikeRelation_njj> result = new ArrayList<>();
        for (UserDislikeRelation_njj relation : userDislikeRelationCache.values()) {
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
        stats.put("totalUserDislikeRelation", totalUserDislikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userDislikeRelationCache.clear();
        userDislikeCache.clear();
        dislikedUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserDislikeRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
