package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户文本推荐信息列表
 * 对应实体: UserTextRecommendation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserTextRecommendation_list_njj extends EntityList<UserTextRecommendation_njj> {

    // 内存存储
    private static final Map<Long, UserTextRecommendation_njj> userTextRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserTextRecommendation_njj>> userRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserTextRecommendation_njj>> textRecommendationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserTextRecommendation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserTextRecommendation_njj> allUserTextRecommendation) {
        try {
            System.out.println("开始直接加载用户文本推荐数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserTextRecommendation == null || allUserTextRecommendation.isEmpty()) {
                System.out.println("用户文本推荐数据为空，清空缓存并设置状态");
                totalUserTextRecommendation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户文本推荐数据直接加载完成！总数: " + totalUserTextRecommendation);
                return;
            }

            // 存储到内存缓存
            for (UserTextRecommendation_njj recommendation : allUserTextRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户文本推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userTextRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按文本内容ID分组存储
                    textRecommendationCache.computeIfAbsent(recommendation.getTextContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserTextRecommendation = userTextRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户文本推荐数据直接加载完成！总数: " + totalUserTextRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户文本推荐数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserTextRecommendation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户文本推荐数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserTextRecommendation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户文本推荐数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户文本推荐数据
            System.out.println("正在执行数据库查询...");
            List<UserTextRecommendation_njj> allUserTextRecommendation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserTextRecommendation.size() + " 条记录");

            // 存储到内存缓存
            for (UserTextRecommendation_njj recommendation : allUserTextRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户文本推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userTextRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按文本内容ID分组存储
                    textRecommendationCache.computeIfAbsent(recommendation.getTextContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserTextRecommendation = userTextRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户文本推荐数据加载完成！总数: " + totalUserTextRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户文本推荐数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户文本推荐数据（从内存）
     */
    public static List<UserTextRecommendation_njj> getAllUserTextRecommendation() {
        return new ArrayList<>(userTextRecommendationCache.values());
    }

    /**
     * 根据ID获取用户文本推荐数据
     */
    public static UserTextRecommendation_njj getUserTextRecommendationById(Long id) {
        return userTextRecommendationCache.get(id);
    }

    /**
     * 根据用户ID获取用户文本推荐数据
     */
    public static List<UserTextRecommendation_njj> getUserTextRecommendationByUserId(Long userId) {
        return userRecommendationCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据文本内容ID获取用户文本推荐数据
     */
    public static List<UserTextRecommendation_njj> getUserTextRecommendationByTextContentId(Long textContentId) {
        return textRecommendationCache.getOrDefault(textContentId, new ArrayList<>());
    }

    /**
     * 根据评分范围获取用户文本推荐数据
     */
    public static List<UserTextRecommendation_njj> getUserTextRecommendationByScoreRange(Float minScore,
            Float maxScore) {
        List<UserTextRecommendation_njj> result = new ArrayList<>();
        for (UserTextRecommendation_njj recommendation : userTextRecommendationCache.values()) {
            if (recommendation.getScore() != null) {
                if (minScore != null && recommendation.getScore() < minScore)
                    continue;
                if (maxScore != null && recommendation.getScore() > maxScore)
                    continue;
                result.add(recommendation);
            }
        }
        return result;
    }

    /**
     * 根据排序获取用户文本推荐数据（按评分降序）
     */
    public static List<UserTextRecommendation_njj> getUserTextRecommendationSortedByScore() {
        List<UserTextRecommendation_njj> result = new ArrayList<>(userTextRecommendationCache.values());
        result.sort((a, b) -> {
            if (a.getScore() == null && b.getScore() == null)
                return 0;
            if (a.getScore() == null)
                return 1;
            if (b.getScore() == null)
                return -1;
            return Float.compare(b.getScore(), a.getScore()); // 降序
        });
        return result;
    }

    /**
     * 根据排序获取用户文本推荐数据（按排序顺序升序）
     */
    public static List<UserTextRecommendation_njj> getUserTextRecommendationSortedByRankOrder() {
        List<UserTextRecommendation_njj> result = new ArrayList<>(userTextRecommendationCache.values());
        result.sort((a, b) -> {
            if (a.getRankOrder() == null && b.getRankOrder() == null)
                return 0;
            if (a.getRankOrder() == null)
                return 1;
            if (b.getRankOrder() == null)
                return -1;
            return Integer.compare(a.getRankOrder(), b.getRankOrder()); // 升序
        });
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserTextRecommendation", totalUserTextRecommendation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userTextRecommendationCache.clear();
        userRecommendationCache.clear();
        textRecommendationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserTextRecommendation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
