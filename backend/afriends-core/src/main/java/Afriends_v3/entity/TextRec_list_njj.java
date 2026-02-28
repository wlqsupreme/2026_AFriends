package Afriends_v3.entity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文字推荐算法内存管理类
 * 用于管理文字推荐数据的内存缓存和检索
 */
public class TextRec_list_njj {
    private static List<TextRec_njj> textRecommendations = new ArrayList<>();
    private static Map<Long, List<TextRec_njj>> userRecommendationsMap = new HashMap<>();
    private static Map<Long, TextRec_njj> idToRecommendationMap = new HashMap<>();

    // 缓存状态
    private static boolean cacheLoaded = false;
    private static long lastUpdateTime = 0;

    /**
     * 加载推荐数据到内存
     */
    public static void loadRecommendations(List<TextRec_njj> recommendations) {
        textRecommendations.clear();
        userRecommendationsMap.clear();
        idToRecommendationMap.clear();

        textRecommendations.addAll(recommendations);

        // 构建索引
        for (TextRec_njj rec : recommendations) {
            // 用户推荐映射
            userRecommendationsMap.computeIfAbsent(rec.getUserId(), k -> new ArrayList<>()).add(rec);

            // ID映射
            idToRecommendationMap.put(rec.getId(), rec);
        }

        cacheLoaded = true;
        lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * 添加推荐记录到内存
     */
    public static void addRecommendation(TextRec_njj recommendation) {
        textRecommendations.add(recommendation);

        // 更新索引
        userRecommendationsMap.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>()).add(recommendation);
        idToRecommendationMap.put(recommendation.getId(), recommendation);

        lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * 根据用户ID获取推荐列表
     */
    public static List<TextRec_njj> getRecommendationsByUserId(Long userId) {
        return userRecommendationsMap.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据用户ID获取推荐列表（按得分排序）
     */
    public static List<TextRec_njj> getRecommendationsByUserIdSorted(Long userId, boolean byScore) {
        List<TextRec_njj> recommendations = getRecommendationsByUserId(userId);
        if (byScore) {
            return recommendations.stream()
                    .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                    .collect(Collectors.toList());
        } else {
            return recommendations.stream()
                    .sorted(Comparator.comparing(TextRec_njj::getRankOrder))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 根据内容ID获取推荐记录
     */
    public static List<TextRec_njj> getRecommendationsByContentId(Long contentId) {
        return textRecommendations.stream()
                .filter(rec -> rec.getTextContentId().equals(contentId))
                .collect(Collectors.toList());
    }

    /**
     * 根据得分范围获取推荐记录
     */
    public static List<TextRec_njj> getRecommendationsByScoreRange(Float minScore, Float maxScore) {
        return textRecommendations.stream()
                .filter(rec -> rec.getScore() >= minScore && rec.getScore() <= maxScore)
                .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
    }

    /**
     * 获取高分推荐记录
     */
    public static List<TextRec_njj> getHighScoreRecommendations(Float minScore) {
        return getRecommendationsByScoreRange(minScore, 1.0f);
    }

    /**
     * 根据ID获取推荐记录
     */
    public static TextRec_njj getRecommendationById(Long id) {
        return idToRecommendationMap.get(id);
    }

    /**
     * 获取用户推荐数量
     */
    public static int getRecommendationCountByUserId(Long userId) {
        return userRecommendationsMap.getOrDefault(userId, new ArrayList<>()).size();
    }

    /**
     * 获取总推荐数量
     */
    public static int getTotalRecommendationCount() {
        return textRecommendations.size();
    }

    /**
     * 获取所有推荐记录
     */
    public static List<TextRec_njj> getAllRecommendations() {
        return new ArrayList<>(textRecommendations);
    }

    /**
     * 清空内存缓存
     */
    public static void clearCache() {
        textRecommendations.clear();
        userRecommendationsMap.clear();
        idToRecommendationMap.clear();
        cacheLoaded = false;
        lastUpdateTime = 0;
    }

    /**
     * 检查缓存是否已加载
     */
    public static boolean isCacheLoaded() {
        return cacheLoaded;
    }

    /**
     * 获取最后更新时间
     */
    public static long getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 获取缓存统计信息
     */
    public static Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecommendations", textRecommendations.size());
        stats.put("uniqueUsers", userRecommendationsMap.size());
        stats.put("cacheLoaded", cacheLoaded);
        stats.put("lastUpdateTime", lastUpdateTime);

        if (!textRecommendations.isEmpty()) {
            float avgScore = (float) textRecommendations.stream()
                    .mapToDouble(TextRec_njj::getScore)
                    .average()
                    .orElse(0.0);
            stats.put("averageScore", avgScore);
        }

        return stats;
    }
}
