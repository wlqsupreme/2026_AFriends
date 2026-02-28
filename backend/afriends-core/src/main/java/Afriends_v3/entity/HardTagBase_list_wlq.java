package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;

/**
 * 标签核心元数据信息列表
 * 对应实体: HardTagBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class HardTagBase_list_wlq extends EntityList<HardTagBase_wlq> {

    // 内存存储
    private static final Map<Long, HardTagBase_wlq> hardTagBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalHardTagBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<HardTagBase_wlq> allHardTagBase) {
        try {
            System.out.println("开始直接加载标签核心元数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (HardTagBase_wlq tag : allHardTagBase) {
                System.out.println("处理标签数据: ID=" + tag.getHardTagId() + ", 关联度=" + tag.getRelevanceValue());
                hardTagBaseCache.put(tag.getHardTagId(), tag);
            }

            // 更新统计信息
            totalHardTagBase = hardTagBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("标签核心元数据直接加载完成！总数: " + totalHardTagBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载标签核心元数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有标签核心元数据到内存
     */
    public static void loadFromDatabase(BaseMapper<HardTagBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载标签核心元数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有标签核心元数据
            System.out.println("正在执行数据库查询...");
            List<HardTagBase_wlq> allHardTagBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allHardTagBase.size() + " 条记录");

            // 存储到内存缓存
            for (HardTagBase_wlq tag : allHardTagBase) {
                System.out.println("处理标签数据: ID=" + tag.getHardTagId() + ", 关联度=" + tag.getRelevanceValue());
                hardTagBaseCache.put(tag.getHardTagId(), tag);
            }

            // 更新统计信息
            totalHardTagBase = hardTagBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("标签核心元数据加载完成！总数: " + totalHardTagBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载标签核心元数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有标签核心元数据（从内存）
     */
    public static List<HardTagBase_wlq> getAllHardTagBase() {
        return new ArrayList<>(hardTagBaseCache.values());
    }

    /**
     * 根据标签ID获取标签核心元数据
     */
    public static HardTagBase_wlq getHardTagBaseById(Long hardTagId) {
        return hardTagBaseCache.get(hardTagId);
    }

    /**
     * 根据分类ID搜索标签核心元数据
     */
    public static List<HardTagBase_wlq> searchHardTagBaseByCategoryId(Long categoryId) {
        List<HardTagBase_wlq> result = new ArrayList<>();
        for (HardTagBase_wlq tag : hardTagBaseCache.values()) {
            if (tag.getCategoryId() != null && tag.getCategoryId().equals(categoryId)) {
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据关联度范围搜索标签核心元数据
     */
    public static List<HardTagBase_wlq> searchHardTagBaseByRelevanceRange(BigDecimal minRelevance, BigDecimal maxRelevance) {
        List<HardTagBase_wlq> result = new ArrayList<>();
        for (HardTagBase_wlq tag : hardTagBaseCache.values()) {
            if (tag.getRelevanceValue() != null) {
                if (minRelevance != null && tag.getRelevanceValue().compareTo(minRelevance) < 0) {
                    continue;
                }
                if (maxRelevance != null && tag.getRelevanceValue().compareTo(maxRelevance) > 0) {
                    continue;
                }
                result.add(tag);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索标签核心元数据
     */
    public static List<HardTagBase_wlq> searchHardTagBaseByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<HardTagBase_wlq> result = new ArrayList<>();
        for (HardTagBase_wlq tag : hardTagBaseCache.values()) {
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
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalHardTagBase", totalHardTagBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        hardTagBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<HardTagBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

