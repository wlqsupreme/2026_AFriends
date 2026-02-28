package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模型币日志列表
 * 对应实体: AimodelCoinLog_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AimodelCoinLog_list_njj extends EntityList<AimodelCoinLog_njj> {

    // 内存存储
    private static final Map<Long, AimodelCoinLog_njj> aimodelCoinLogCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<AimodelCoinLog_njj>> userCoinLogCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAimodelCoinLog = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AimodelCoinLog_njj> allAimodelCoinLog) {
        try {
            System.out.println("开始直接加载AI模型币日志数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AimodelCoinLog_njj log : allAimodelCoinLog) {
                System.out.println(
                        "处理币日志数据: ID=" + log.getLogId() + ", 用户ID=" + log.getUserId() + ", 金额=" + log.getAmount());
                aimodelCoinLogCache.put(log.getLogId(), log);

                // 按用户ID分组存储
                userCoinLogCache.computeIfAbsent(log.getUserId(), k -> new ArrayList<>()).add(log);
            }

            // 更新统计信息
            totalAimodelCoinLog = aimodelCoinLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型币日志数据直接加载完成！总数: " + totalAimodelCoinLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI模型币日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI模型币日志数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AimodelCoinLog_njj> mapper) {
        try {
            System.out.println("开始从数据库加载AI模型币日志数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI模型币日志数据
            System.out.println("正在执行数据库查询...");
            List<AimodelCoinLog_njj> allAimodelCoinLog = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAimodelCoinLog.size() + " 条记录");

            // 存储到内存缓存
            for (AimodelCoinLog_njj log : allAimodelCoinLog) {
                System.out.println(
                        "处理币日志数据: ID=" + log.getLogId() + ", 用户ID=" + log.getUserId() + ", 金额=" + log.getAmount());
                aimodelCoinLogCache.put(log.getLogId(), log);

                // 按用户ID分组存储
                userCoinLogCache.computeIfAbsent(log.getUserId(), k -> new ArrayList<>()).add(log);
            }

            // 更新统计信息
            totalAimodelCoinLog = aimodelCoinLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型币日志数据加载完成！总数: " + totalAimodelCoinLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI模型币日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI模型币日志数据（从内存）
     */
    public static List<AimodelCoinLog_njj> getAllAimodelCoinLog() {
        return new ArrayList<>(aimodelCoinLogCache.values());
    }

    /**
     * 根据日志ID获取AI模型币日志数据
     */
    public static AimodelCoinLog_njj getAimodelCoinLogById(Long logId) {
        return aimodelCoinLogCache.get(logId);
    }

    /**
     * 根据用户ID获取AI模型币日志数据
     */
    public static List<AimodelCoinLog_njj> getAimodelCoinLogByUserId(Long userId) {
        return userCoinLogCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAimodelCoinLog", totalAimodelCoinLog);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aimodelCoinLogCache.clear();
        userCoinLogCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AimodelCoinLog_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加金币日志到缓存
     */
    public static void addToCache(AimodelCoinLog_njj coinLog) {
        if (coinLog != null && coinLog.getLogId() != null) {
            aimodelCoinLogCache.put(coinLog.getLogId(), coinLog);
            userCoinLogCache.computeIfAbsent(coinLog.getUserId(), k -> new ArrayList<>()).add(coinLog);
            totalAimodelCoinLog = aimodelCoinLogCache.size();
            lastUpdateTime = System.currentTimeMillis();
            System.out.println("AI模型金币日志已添加到缓存，logId: " + coinLog.getLogId());
        }
    }
}