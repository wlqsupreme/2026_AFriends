package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI任务日志列表
 * 对应实体: AiTaskLog_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiTaskLog_list_njj extends EntityList<AiTaskLog_njj> {

    // 内存存储
    private static final Map<Long, AiTaskLog_njj> aiTaskLogCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAiTaskLog = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AiTaskLog_njj> allAiTaskLog) {
        try {
            System.out.println("开始直接加载AI任务日志数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiTaskLog_njj log : allAiTaskLog) {
                System.out.println("处理任务日志数据: ID=" + log.getAiTaskLogId() + ", 任务名称=" + log.getAiTaskName());
                aiTaskLogCache.put(log.getAiTaskLogId(), log);
            }

            // 更新统计信息
            totalAiTaskLog = aiTaskLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务日志数据直接加载完成！总数: " + totalAiTaskLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI任务日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI任务日志数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiTaskLog_njj> mapper) {
        try {
            System.out.println("开始从数据库加载AI任务日志数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI任务日志数据
            System.out.println("正在执行数据库查询...");
            List<AiTaskLog_njj> allAiTaskLog = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAiTaskLog.size() + " 条记录");

            // 存储到内存缓存
            for (AiTaskLog_njj log : allAiTaskLog) {
                System.out.println("处理任务日志数据: ID=" + log.getAiTaskLogId() + ", 任务名称=" + log.getAiTaskName());
                aiTaskLogCache.put(log.getAiTaskLogId(), log);
            }

            // 更新统计信息
            totalAiTaskLog = aiTaskLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务日志数据加载完成！总数: " + totalAiTaskLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI任务日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI任务日志数据（从内存）
     */
    public static List<AiTaskLog_njj> getAllAiTaskLog() {
        return new ArrayList<>(aiTaskLogCache.values());
    }

    /**
     * 根据任务日志ID获取AI任务日志数据
     */
    public static AiTaskLog_njj getAiTaskLogById(Long aiTaskLogId) {
        return aiTaskLogCache.get(aiTaskLogId);
    }

    /**
     * 根据任务名称搜索AI任务日志数据
     */
    public static List<AiTaskLog_njj> searchAiTaskLogByName(String name) {
        List<AiTaskLog_njj> result = new ArrayList<>();
        for (AiTaskLog_njj log : aiTaskLogCache.values()) {
            if (log.getAiTaskName() != null && log.getAiTaskName().contains(name)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 根据状态获取AI任务日志数据
     */
    public static List<AiTaskLog_njj> getAiTaskLogByStatus(Byte status) {
        List<AiTaskLog_njj> result = new ArrayList<>();
        for (AiTaskLog_njj log : aiTaskLogCache.values()) {
            if (log.getStatus() != null && log.getStatus().equals(status)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAiTaskLog", totalAiTaskLog);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aiTaskLogCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiTaskLog_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 更新缓存中的AI任务日志信息
     */
    public static void updateAiTaskLogInCache(AiTaskLog_njj aiTaskLog) {
        if (aiTaskLog != null && aiTaskLog.getAiTaskLogId() != null) {
            aiTaskLogCache.put(aiTaskLog.getAiTaskLogId(), aiTaskLog);
            lastUpdateTime = System.currentTimeMillis();
            System.out.println("缓存中的AI任务日志信息已更新，aiTaskLogId: " + aiTaskLog.getAiTaskLogId());
        }
    }
}