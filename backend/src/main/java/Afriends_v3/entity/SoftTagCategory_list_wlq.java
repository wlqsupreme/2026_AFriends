package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 软标签分类信息列表
 * 对应实体: SoftTagCategory_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class SoftTagCategory_list_wlq extends EntityList<SoftTagCategory_wlq> {

    // 内存存储
    private static final Map<Long, SoftTagCategory_wlq> softTagCategoryCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalSoftTagCategory = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<SoftTagCategory_wlq> allSoftTagCategory) {
        try {
            System.out.println("开始直接加载软标签分类数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (SoftTagCategory_wlq category : allSoftTagCategory) {
                System.out.println("处理软标签分类数据: ID=" + category.getSoftCategoryId() + ", 内容=" + category.getSoftCategoryContent());
                softTagCategoryCache.put(category.getSoftCategoryId(), category);
            }

            // 更新统计信息
            totalSoftTagCategory = softTagCategoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("软标签分类数据直接加载完成！总数: " + totalSoftTagCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载软标签分类数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有软标签分类数据到内存
     */
    public static void loadFromDatabase(BaseMapper<SoftTagCategory_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载软标签分类数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有软标签分类数据
            System.out.println("正在执行数据库查询...");
            List<SoftTagCategory_wlq> allSoftTagCategory = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allSoftTagCategory.size() + " 条记录");

            // 存储到内存缓存
            for (SoftTagCategory_wlq category : allSoftTagCategory) {
                System.out.println("处理软标签分类数据: ID=" + category.getSoftCategoryId() + ", 内容=" + category.getSoftCategoryContent());
                softTagCategoryCache.put(category.getSoftCategoryId(), category);
            }

            // 更新统计信息
            totalSoftTagCategory = softTagCategoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("软标签分类数据加载完成！总数: " + totalSoftTagCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载软标签分类数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有软标签分类数据（从内存）
     */
    public static List<SoftTagCategory_wlq> getAllSoftTagCategory() {
        return new ArrayList<>(softTagCategoryCache.values());
    }

    /**
     * 根据软标签分类ID获取软标签分类数据
     */
    public static SoftTagCategory_wlq getSoftTagCategoryById(Long softCategoryId) {
        return softTagCategoryCache.get(softCategoryId);
    }

    /**
     * 根据软标签分类内容搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryBySoftCategoryContent(String softCategoryContent) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getSoftCategoryContent() != null && category.getSoftCategoryContent().contains(softCategoryContent)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据父级软标签分类ID搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByParentSoftCategoryId(Long parentSoftCategoryId) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getParentSoftCategoryId() != null && category.getParentSoftCategoryId().equals(parentSoftCategoryId)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据层级搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByLevel(Integer level) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getLevel() != null && category.getLevel().equals(level)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据层级范围搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByLevelRange(Integer minLevel, Integer maxLevel) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getLevel() != null) {
                if (minLevel != null && category.getLevel() < minLevel) {
                    continue;
                }
                if (maxLevel != null && category.getLevel() > maxLevel) {
                    continue;
                }
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据备份标签搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByBackup(String backup) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getBackup() != null && category.getBackup().contains(backup)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据键值对标签搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByKeyvalue(String keyvalue) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getKeyvalue() != null && category.getKeyvalue().contains(keyvalue)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据特征向量搜索软标签分类数据
     */
    public static List<SoftTagCategory_wlq> searchSoftTagCategoryByFeatureVector(String featureVector) {
        List<SoftTagCategory_wlq> result = new ArrayList<>();
        for (SoftTagCategory_wlq category : softTagCategoryCache.values()) {
            if (category.getFeatureVector() != null && category.getFeatureVector().contains(featureVector)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSoftTagCategory", totalSoftTagCategory);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        softTagCategoryCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<SoftTagCategory_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
