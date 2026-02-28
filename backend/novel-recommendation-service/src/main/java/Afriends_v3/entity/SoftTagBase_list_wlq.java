package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 软标签基础信息列表
 * 对应实体: SoftTagBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class SoftTagBase_list_wlq extends EntityList<SoftTagBase_wlq> {

    // 内存存储
    private static final Map<Long, SoftTagBase_wlq> softTagBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalSoftTagBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<SoftTagBase_wlq> allSoftTagBase) {
        try {
            System.out.println("开始直接加载软标签基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (SoftTagBase_wlq tag : allSoftTagBase) {
                System.out.println("处理软标签数据: ID=" + tag.getSoftTagId() + ", 内容=" + tag.getSoftTagContent());
                softTagBaseCache.put(tag.getSoftTagId(), tag);
            }

            // 更新统计信息
            totalSoftTagBase = softTagBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("软标签基础数据直接加载完成！总数: " + totalSoftTagBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载软标签基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有软标签基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<SoftTagBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载软标签基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有软标签基础数据
            System.out.println("正在执行数据库查询...");
            List<SoftTagBase_wlq> allSoftTagBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allSoftTagBase.size() + " 条记录");

            // 存储到内存缓存
            for (SoftTagBase_wlq tag : allSoftTagBase) {
                System.out.println("处理软标签数据: ID=" + tag.getSoftTagId() + ", 内容=" + tag.getSoftTagContent());
                softTagBaseCache.put(tag.getSoftTagId(), tag);
            }

            // 更新统计信息
            totalSoftTagBase = softTagBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("软标签基础数据加载完成！总数: " + totalSoftTagBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载软标签基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有软标签基础数据（从内存）
     */
    public static List<SoftTagBase_wlq> getAllSoftTagBase() {
        return new ArrayList<>(softTagBaseCache.values());
    }

    /**
     * 根据软标签ID获取软标签基础数据
     */
    public static SoftTagBase_wlq getSoftTagBaseById(Long softTagId) {
        return softTagBaseCache.get(softTagId);
    }

    /**
     * 根据软标签内容搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseBySoftTagContent(String softTagContent) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getSoftTagContent() != null && tag.getSoftTagContent().contains(softTagContent)) {
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据分类ID搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByCategoryId(Long categoryId) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getCategoryId() != null && tag.getCategoryId().equals(categoryId)) {
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据浏览量范围搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByViewCountRange(Integer minViewCount, Integer maxViewCount) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getViewCount() != null) {
                if (minViewCount != null && tag.getViewCount() < minViewCount) {
                    continue;
                }
                if (maxViewCount != null && tag.getViewCount() > maxViewCount) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getLikeCount() != null) {
                if (minLikeCount != null && tag.getLikeCount() < minLikeCount) {
                    continue;
                }
                if (maxLikeCount != null && tag.getLikeCount() > maxLikeCount) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据收藏数范围搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByCollectCountRange(Integer minCollectCount, Integer maxCollectCount) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getCollectCount() != null) {
                if (minCollectCount != null && tag.getCollectCount() < minCollectCount) {
                    continue;
                }
                if (maxCollectCount != null && tag.getCollectCount() > maxCollectCount) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据评论数范围搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByCommentCountRange(Integer minCommentCount, Integer maxCommentCount) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getCommentCount() != null) {
                if (minCommentCount != null && tag.getCommentCount() < minCommentCount) {
                    continue;
                }
                if (maxCommentCount != null && tag.getCommentCount() > maxCommentCount) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据关联度范围搜索软标签基础数据
     */
    public static List<SoftTagBase_wlq> searchSoftTagBaseByRelevanceValueRange(java.math.BigDecimal minRelevanceValue, java.math.BigDecimal maxRelevanceValue) {
        List<SoftTagBase_wlq> result = new ArrayList<>();
        for (SoftTagBase_wlq tag : softTagBaseCache.values()) {
            if (tag.getRelevanceValue() != null) {
                if (minRelevanceValue != null && tag.getRelevanceValue().compareTo(minRelevanceValue) < 0) {
                    continue;
                }
                if (maxRelevanceValue != null && tag.getRelevanceValue().compareTo(maxRelevanceValue) > 0) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSoftTagBase", totalSoftTagBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        softTagBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<SoftTagBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
