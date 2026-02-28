package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推荐标签信息列表
 * 对应实体: RecommendedLabel_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class RecommendedLabel_list_wlq extends EntityList<RecommendedLabel_wlq> {

    // 内存存储
    private static final Map<Long, RecommendedLabel_wlq> recommendedLabelCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalRecommendedLabel = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<RecommendedLabel_wlq> allRecommendedLabel) {
        try {
            System.out.println("开始直接加载推荐标签数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (RecommendedLabel_wlq label : allRecommendedLabel) {
                System.out.println("处理推荐标签数据: ID=" + label.getId() + ", 标签名=" + label.getLabelName());
                recommendedLabelCache.put(label.getId(), label);
            }

            // 更新统计信息
            totalRecommendedLabel = recommendedLabelCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐标签数据直接加载完成！总数: " + totalRecommendedLabel);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载推荐标签数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有推荐标签数据到内存
     */
    public static void loadFromDatabase(BaseMapper<RecommendedLabel_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载推荐标签数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有推荐标签数据
            System.out.println("正在执行数据库查询...");
            List<RecommendedLabel_wlq> allRecommendedLabel = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allRecommendedLabel.size() + " 条记录");

            // 存储到内存缓存
            for (RecommendedLabel_wlq label : allRecommendedLabel) {
                System.out.println("处理推荐标签数据: ID=" + label.getId() + ", 标签名=" + label.getLabelName());
                recommendedLabelCache.put(label.getId(), label);
            }

            // 更新统计信息
            totalRecommendedLabel = recommendedLabelCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐标签数据加载完成！总数: " + totalRecommendedLabel);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载推荐标签数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有推荐标签数据（从内存）
     */
    public static List<RecommendedLabel_wlq> getAllRecommendedLabel() {
        return new ArrayList<>(recommendedLabelCache.values());
    }

    /**
     * 根据ID获取推荐标签数据
     */
    public static RecommendedLabel_wlq getRecommendedLabelById(Long id) {
        return recommendedLabelCache.get(id);
    }

    /**
     * 根据标签名称搜索推荐标签数据
     */
    public static List<RecommendedLabel_wlq> searchRecommendedLabelByLabelName(String labelName) {
        List<RecommendedLabel_wlq> result = new ArrayList<>();
        for (RecommendedLabel_wlq label : recommendedLabelCache.values()) {
            if (label.getLabelName() != null && label.getLabelName().contains(labelName)) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * 根据标签描述搜索推荐标签数据
     */
    public static List<RecommendedLabel_wlq> searchRecommendedLabelByDescription(String description) {
        List<RecommendedLabel_wlq> result = new ArrayList<>();
        for (RecommendedLabel_wlq label : recommendedLabelCache.values()) {
            if (label.getDescription() != null && label.getDescription().contains(description)) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * 根据ID范围搜索推荐标签数据
     */
    public static List<RecommendedLabel_wlq> searchRecommendedLabelByIdRange(Long minId, Long maxId) {
        List<RecommendedLabel_wlq> result = new ArrayList<>();
        for (RecommendedLabel_wlq label : recommendedLabelCache.values()) {
            if (label.getId() != null) {
                if (minId != null && label.getId() < minId) {
                    continue;
                }
                if (maxId != null && label.getId() > maxId) {
                    continue;
                }
                result.add(label);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecommendedLabel", totalRecommendedLabel);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        recommendedLabelCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<RecommendedLabel_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
