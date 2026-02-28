package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户小说推荐信息列表
 * 对应实体: UserNovelRecommendation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserNovelRecommendation_list_njj extends EntityList<UserNovelRecommendation_njj> {

    // 内存存储
    private static final Map<Long, UserNovelRecommendation_njj> userNovelRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserNovelRecommendation_njj>> userRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserNovelRecommendation_njj>> novelRecommendationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserNovelRecommendation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserNovelRecommendation_njj> allUserNovelRecommendation) {
        try {
            System.out.println("开始直接加载用户小说推荐数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserNovelRecommendation == null || allUserNovelRecommendation.isEmpty()) {
                System.out.println("用户小说推荐数据为空，清空缓存并设置状态");
                totalUserNovelRecommendation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户小说推荐数据直接加载完成！总数: " + totalUserNovelRecommendation);
                return;
            }

            // 存储到内存缓存
            for (UserNovelRecommendation_njj recommendation : allUserNovelRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户小说推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userNovelRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按小说内容ID分组存储
                    novelRecommendationCache.computeIfAbsent(recommendation.getNovelContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserNovelRecommendation = userNovelRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户小说推荐数据直接加载完成！总数: " + totalUserNovelRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户小说推荐数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserNovelRecommendation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户小说推荐数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserNovelRecommendation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户小说推荐数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户小说推荐数据
            System.out.println("正在执行数据库查询...");
            List<UserNovelRecommendation_njj> allUserNovelRecommendation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserNovelRecommendation.size() + " 条记录");

            // 存储到内存缓存
            for (UserNovelRecommendation_njj recommendation : allUserNovelRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户小说推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userNovelRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按小说内容ID分组存储
                    novelRecommendationCache.computeIfAbsent(recommendation.getNovelContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserNovelRecommendation = userNovelRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户小说推荐数据加载完成！总数: " + totalUserNovelRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户小说推荐数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户小说推荐数据（从内存）
     */
    public static List<UserNovelRecommendation_njj> getAllUserNovelRecommendation() {
        return new ArrayList<>(userNovelRecommendationCache.values());
    }

    /**
     * 根据ID获取用户小说推荐数据
     */
    public static UserNovelRecommendation_njj getUserNovelRecommendationById(Long id) {
        return userNovelRecommendationCache.get(id);
    }

    /**
     * 根据用户ID获取用户小说推荐数据
     */
    public static List<UserNovelRecommendation_njj> getUserNovelRecommendationByUserId(Long userId) {
        return userRecommendationCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据小说内容ID获取用户小说推荐数据
     */
    public static List<UserNovelRecommendation_njj> getUserNovelRecommendationByNovelContentId(Long novelContentId) {
        return novelRecommendationCache.getOrDefault(novelContentId, new ArrayList<>());
    }

    /**
     * 根据评分范围获取用户小说推荐数据
     */
    public static List<UserNovelRecommendation_njj> getUserNovelRecommendationByScoreRange(Float minScore,
            Float maxScore) {
        List<UserNovelRecommendation_njj> result = new ArrayList<>();
        for (UserNovelRecommendation_njj recommendation : userNovelRecommendationCache.values()) {
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
     * 根据排序获取用户小说推荐数据（按评分降序）
     */
    public static List<UserNovelRecommendation_njj> getUserNovelRecommendationSortedByScore() {
        List<UserNovelRecommendation_njj> result = new ArrayList<>(userNovelRecommendationCache.values());
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
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserNovelRecommendation", totalUserNovelRecommendation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userNovelRecommendationCache.clear();
        userRecommendationCache.clear();
        novelRecommendationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserNovelRecommendation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
