package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 举报记录列表
 * 对应实体: ReportRecord_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ReportRecord_list_njj extends EntityList<ReportRecord_njj> {

    // 内存存储
    private static final Map<Long, ReportRecord_njj> reportRecordCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<ReportRecord_njj>> userReportCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalReportRecord = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ReportRecord_njj> allReportRecord) {
        try {
            System.out.println("开始直接加载举报记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ReportRecord_njj record : allReportRecord) {
                System.out.println("处理举报记录数据: ID=" + record.getReportId() + ", 用户ID=" + record.getUserId()
                        + ", 被举报用户ID=" + record.getReportedUserId());
                reportRecordCache.put(record.getReportId(), record);

                // 按用户ID分组存储
                userReportCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalReportRecord = reportRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("举报记录数据直接加载完成！总数: " + totalReportRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载举报记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有举报记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ReportRecord_njj> mapper) {
        try {
            System.out.println("开始从数据库加载举报记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有举报记录数据
            System.out.println("正在执行数据库查询...");
            List<ReportRecord_njj> allReportRecord = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allReportRecord.size() + " 条记录");

            // 存储到内存缓存
            for (ReportRecord_njj record : allReportRecord) {
                System.out.println("处理举报记录数据: ID=" + record.getReportId() + ", 用户ID=" + record.getUserId()
                        + ", 被举报用户ID=" + record.getReportedUserId());
                reportRecordCache.put(record.getReportId(), record);

                // 按用户ID分组存储
                userReportCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalReportRecord = reportRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("举报记录数据加载完成！总数: " + totalReportRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载举报记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有举报记录数据（从内存）
     */
    public static List<ReportRecord_njj> getAllReportRecord() {
        return new ArrayList<>(reportRecordCache.values());
    }

    /**
     * 根据举报ID获取举报记录数据
     */
    public static ReportRecord_njj getReportRecordById(Long reportId) {
        return reportRecordCache.get(reportId);
    }

    /**
     * 根据用户ID获取举报记录数据
     */
    public static List<ReportRecord_njj> getReportRecordByUserId(Long userId) {
        return userReportCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据被举报用户ID获取举报记录数据
     */
    public static List<ReportRecord_njj> getReportRecordByReportedUserId(Long reportedUserId) {
        List<ReportRecord_njj> result = new ArrayList<>();
        for (ReportRecord_njj record : reportRecordCache.values()) {
            if (record.getReportedUserId().equals(reportedUserId)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 根据状态获取举报记录数据
     */
    public static List<ReportRecord_njj> getReportRecordByStatus(Byte status) {
        List<ReportRecord_njj> result = new ArrayList<>();
        for (ReportRecord_njj record : reportRecordCache.values()) {
            if (record.getStatus() != null && record.getStatus().equals(status)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReportRecord", totalReportRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        reportRecordCache.clear();
        userReportCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ReportRecord_njj> mapper) {
        loadFromDatabase(mapper);
    }
}