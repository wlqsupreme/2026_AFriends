package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户评论基础信息列表
 * 对应实体: UserReviewBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserReviewBase_list_njj extends EntityList<UserReviewBase_njj> {

    // 内存存储
    private static final Map<Long, UserReviewBase_njj> userReviewBaseCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserReviewBase_njj>> reviewerCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserReviewBase_njj>> targetUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserReviewBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserReviewBase_njj> allUserReviewBase) {
        try {
            System.out.println("开始直接加载用户评论基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserReviewBase == null || allUserReviewBase.isEmpty()) {
                System.out.println("用户评论基础数据为空，清空缓存并设置状态");
                totalUserReviewBase = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户评论基础数据直接加载完成！总数: " + totalUserReviewBase);
                return;
            }

            // 存储到内存缓存
            for (UserReviewBase_njj review : allUserReviewBase) {
                if (review != null && review.getUserReviewId() != null) {
                    System.out.println(
                            "处理用户评论基础数据: ID=" + review.getUserReviewId() + ", 评论者ID=" + review.getReviewerUserId());
                    userReviewBaseCache.put(review.getUserReviewId(), review);

                    // 按评论者用户ID分组存储
                    reviewerCache.computeIfAbsent(review.getReviewerUserId(), k -> new ArrayList<>()).add(review);

                    // 按目标用户ID分组存储
                    targetUserCache.computeIfAbsent(review.getTargetUserId(), k -> new ArrayList<>()).add(review);
                }
            }

            // 更新统计信息
            totalUserReviewBase = userReviewBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户评论基础数据直接加载完成！总数: " + totalUserReviewBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户评论基础数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserReviewBase = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户评论基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserReviewBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户评论基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户评论基础数据
            System.out.println("正在执行数据库查询...");
            List<UserReviewBase_njj> allUserReviewBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserReviewBase.size() + " 条记录");

            // 存储到内存缓存
            for (UserReviewBase_njj review : allUserReviewBase) {
                if (review != null && review.getUserReviewId() != null) {
                    System.out.println(
                            "处理用户评论基础数据: ID=" + review.getUserReviewId() + ", 评论者ID=" + review.getReviewerUserId());
                    userReviewBaseCache.put(review.getUserReviewId(), review);

                    // 按评论者用户ID分组存储
                    reviewerCache.computeIfAbsent(review.getReviewerUserId(), k -> new ArrayList<>()).add(review);

                    // 按目标用户ID分组存储
                    targetUserCache.computeIfAbsent(review.getTargetUserId(), k -> new ArrayList<>()).add(review);
                }
            }

            // 更新统计信息
            totalUserReviewBase = userReviewBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户评论基础数据加载完成！总数: " + totalUserReviewBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户评论基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户评论基础数据（从内存）
     */
    public static List<UserReviewBase_njj> getAllUserReviewBase() {
        return new ArrayList<>(userReviewBaseCache.values());
    }

    /**
     * 根据评论ID获取用户评论基础数据
     */
    public static UserReviewBase_njj getUserReviewBaseById(Long userReviewId) {
        return userReviewBaseCache.get(userReviewId);
    }

    /**
     * 根据评论者用户ID获取用户评论基础数据
     */
    public static List<UserReviewBase_njj> getUserReviewBaseByReviewerUserId(Long reviewerUserId) {
        return reviewerCache.getOrDefault(reviewerUserId, new ArrayList<>());
    }

    /**
     * 根据目标用户ID获取用户评论基础数据
     */
    public static List<UserReviewBase_njj> getUserReviewBaseByTargetUserId(Long targetUserId) {
        return targetUserCache.getOrDefault(targetUserId, new ArrayList<>());
    }

    /**
     * 根据评分获取用户评论基础数据
     */
    public static List<UserReviewBase_njj> getUserReviewBaseByRatingScore(Byte ratingScore) {
        List<UserReviewBase_njj> result = new ArrayList<>();
        for (UserReviewBase_njj review : userReviewBaseCache.values()) {
            if (review.getRatingScore() != null && review.getRatingScore().equals(ratingScore)) {
                result.add(review);
            }
        }
        return result;
    }

    /**
     * 根据状态获取用户评论基础数据
     */
    public static List<UserReviewBase_njj> getUserReviewBaseByStatus(Byte status) {
        List<UserReviewBase_njj> result = new ArrayList<>();
        for (UserReviewBase_njj review : userReviewBaseCache.values()) {
            if (review.getStatus() != null && review.getStatus().equals(status)) {
                result.add(review);
            }
        }
        return result;
    }

    /**
     * 根据评分排序获取用户评论基础数据
     */
    public static List<UserReviewBase_njj> getUserReviewBaseSortedByRatingScore() {
        List<UserReviewBase_njj> result = new ArrayList<>(userReviewBaseCache.values());
        result.sort((a, b) -> {
            if (a.getRatingScore() == null && b.getRatingScore() == null)
                return 0;
            if (a.getRatingScore() == null)
                return 1;
            if (b.getRatingScore() == null)
                return -1;
            return Byte.compare(b.getRatingScore(), a.getRatingScore()); // 降序
        });
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserReviewBase", totalUserReviewBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userReviewBaseCache.clear();
        reviewerCache.clear();
        targetUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserReviewBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
