package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 举报类别列表
 * 对应实体: ReportCategory_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ReportCategory_list_njj extends EntityList<ReportCategory_njj> {

    // 内存存储
    private static final Map<Long, ReportCategory_njj> reportCategoryCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalReportCategory = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ReportCategory_njj> allReportCategory) {
        try {
            System.out.println("开始直接加载举报类别数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ReportCategory_njj category : allReportCategory) {
                System.out.println("处理举报类别数据: ID=" + category.getCategoryId() + ", 名称=" + category.getCategoryName());
                reportCategoryCache.put(category.getCategoryId(), category);
            }

            // 更新统计信息
            totalReportCategory = reportCategoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("举报类别数据直接加载完成！总数: " + totalReportCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载举报类别数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有举报类别数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ReportCategory_njj> mapper) {
        try {
            System.out.println("开始从数据库加载举报类别数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有举报类别数据
            System.out.println("正在执行数据库查询...");
            List<ReportCategory_njj> allReportCategory = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allReportCategory.size() + " 条记录");

            // 存储到内存缓存
            for (ReportCategory_njj category : allReportCategory) {
                System.out.println("处理举报类别数据: ID=" + category.getCategoryId() + ", 名称=" + category.getCategoryName());
                reportCategoryCache.put(category.getCategoryId(), category);
            }

            // 更新统计信息
            totalReportCategory = reportCategoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("举报类别数据加载完成！总数: " + totalReportCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载举报类别数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有举报类别数据（从内存）
     */
    public static List<ReportCategory_njj> getAllReportCategory() {
        return new ArrayList<>(reportCategoryCache.values());
    }

    /**
     * 根据类别ID获取举报类别数据
     */
    public static ReportCategory_njj getReportCategoryById(Long categoryId) {
        return reportCategoryCache.get(categoryId);
    }

    /**
     * 根据类别名称搜索举报类别数据
     */
    public static List<ReportCategory_njj> searchReportCategoryByName(String name) {
        List<ReportCategory_njj> result = new ArrayList<>();
        for (ReportCategory_njj category : reportCategoryCache.values()) {
            if (category.getCategoryName() != null && category.getCategoryName().contains(name)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 根据启用状态获取举报类别数据
     */
    public static List<ReportCategory_njj> getReportCategoryByEnabled(Byte isEnabled) {
        List<ReportCategory_njj> result = new ArrayList<>();
        for (ReportCategory_njj category : reportCategoryCache.values()) {
            if (category.getIsEnabled() != null && category.getIsEnabled().equals(isEnabled)) {
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
        stats.put("totalReportCategory", totalReportCategory);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        reportCategoryCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ReportCategory_njj> mapper) {
        loadFromDatabase(mapper);
    }
}