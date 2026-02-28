package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户图片推荐信息列表
 * 对应实体: UserImageRecommendation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserImageRecommendation_list_njj extends EntityList<UserImageRecommendation_njj> {

    // 内存存储
    private static final Map<Long, UserImageRecommendation_njj> userImageRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserImageRecommendation_njj>> userRecommendationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserImageRecommendation_njj>> imageRecommendationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserImageRecommendation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserImageRecommendation_njj> allUserImageRecommendation) {
        try {
            System.out.println("开始直接加载用户图片推荐数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserImageRecommendation == null || allUserImageRecommendation.isEmpty()) {
                System.out.println("用户图片推荐数据为空，清空缓存并设置状态");
                totalUserImageRecommendation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户图片推荐数据直接加载完成！总数: " + totalUserImageRecommendation);
                return;
            }

            // 存储到内存缓存
            for (UserImageRecommendation_njj recommendation : allUserImageRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户图片推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userImageRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按图片内容ID分组存储
                    imageRecommendationCache.computeIfAbsent(recommendation.getImageContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserImageRecommendation = userImageRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户图片推荐数据直接加载完成！总数: " + totalUserImageRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户图片推荐数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserImageRecommendation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户图片推荐数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserImageRecommendation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户图片推荐数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户图片推荐数据
            System.out.println("正在执行数据库查询...");
            List<UserImageRecommendation_njj> allUserImageRecommendation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserImageRecommendation.size() + " 条记录");

            // 存储到内存缓存
            for (UserImageRecommendation_njj recommendation : allUserImageRecommendation) {
                if (recommendation != null && recommendation.getId() != null) {
                    System.out.println(
                            "处理用户图片推荐数据: ID=" + recommendation.getId() + ", 用户ID=" + recommendation.getUserId());
                    userImageRecommendationCache.put(recommendation.getId(), recommendation);

                    // 按用户ID分组存储
                    userRecommendationCache.computeIfAbsent(recommendation.getUserId(), k -> new ArrayList<>())
                            .add(recommendation);

                    // 按图片内容ID分组存储
                    imageRecommendationCache.computeIfAbsent(recommendation.getImageContentId(), k -> new ArrayList<>())
                            .add(recommendation);
                }
            }

            // 更新统计信息
            totalUserImageRecommendation = userImageRecommendationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户图片推荐数据加载完成！总数: " + totalUserImageRecommendation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户图片推荐数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户图片推荐数据（从内存）
     */
    public static List<UserImageRecommendation_njj> getAllUserImageRecommendation() {
        return new ArrayList<>(userImageRecommendationCache.values());
    }

    /**
     * 根据ID获取用户图片推荐数据
     */
    public static UserImageRecommendation_njj getUserImageRecommendationById(Long id) {
        return userImageRecommendationCache.get(id);
    }

    /**
     * 根据用户ID获取用户图片推荐数据
     */
    public static List<UserImageRecommendation_njj> getUserImageRecommendationByUserId(Long userId) {
        return userRecommendationCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据图片内容ID获取用户图片推荐数据
     */
    public static List<UserImageRecommendation_njj> getUserImageRecommendationByImageContentId(Long imageContentId) {
        return imageRecommendationCache.getOrDefault(imageContentId, new ArrayList<>());
    }

    /**
     * 根据评分范围获取用户图片推荐数据
     */
    public static List<UserImageRecommendation_njj> getUserImageRecommendationByScoreRange(Float minScore,
            Float maxScore) {
        List<UserImageRecommendation_njj> result = new ArrayList<>();
        for (UserImageRecommendation_njj recommendation : userImageRecommendationCache.values()) {
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
     * 根据排序获取用户图片推荐数据（按评分降序）
     */
    public static List<UserImageRecommendation_njj> getUserImageRecommendationSortedByScore() {
        List<UserImageRecommendation_njj> result = new ArrayList<>(userImageRecommendationCache.values());
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
        stats.put("totalUserImageRecommendation", totalUserImageRecommendation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userImageRecommendationCache.clear();
        userRecommendationCache.clear();
        imageRecommendationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserImageRecommendation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
