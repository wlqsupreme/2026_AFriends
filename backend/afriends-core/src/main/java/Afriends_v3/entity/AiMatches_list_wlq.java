package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI对话匹配结果列表
 * 对应实体: AiMatches_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiMatches_list_wlq extends EntityList<AiMatches_wlq> {

    // 内存存储
    private static final Map<Long, AiMatches_wlq> aiMatchesCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAiMatches = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AiMatches_wlq> allAiMatches) {
        try {
            System.out.println("开始直接加载AI对话匹配结果数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiMatches_wlq aiMatch : allAiMatches) {
                System.out.println("处理AI匹配数据: ID=" + aiMatch.getAimatchesId() + ", 相似度=" + aiMatch.getSimilarity());
                aiMatchesCache.put(aiMatch.getAimatchesId(), aiMatch);
            }

            // 更新统计信息
            totalAiMatches = aiMatchesCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI对话匹配结果数据直接加载完成！总数: " + totalAiMatches);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI对话匹配结果数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI对话匹配结果数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiMatches_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载AI对话匹配结果数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI对话匹配结果数据
            System.out.println("正在执行数据库查询...");
            List<AiMatches_wlq> allAiMatches = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAiMatches.size() + " 条记录");

            // 存储到内存缓存
            for (AiMatches_wlq aiMatch : allAiMatches) {
                System.out.println("处理AI匹配数据: ID=" + aiMatch.getAimatchesId() + ", 相似度=" + aiMatch.getSimilarity());
                aiMatchesCache.put(aiMatch.getAimatchesId(), aiMatch);
            }

            // 更新统计信息
            totalAiMatches = aiMatchesCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI对话匹配结果数据加载完成！总数: " + totalAiMatches);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI对话匹配结果数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI对话匹配结果数据（从内存）
     */
    public static List<AiMatches_wlq> getAllAiMatches() {
        return new ArrayList<>(aiMatchesCache.values());
    }

    /**
     * 根据匹配ID获取AI对话匹配结果数据
     */
    public static AiMatches_wlq getAiMatchesById(Long aimatchesId) {
        return aiMatchesCache.get(aimatchesId);
    }

    /**
     * 根据要求用户ID搜索AI对话匹配结果数据
     */
    public static List<AiMatches_wlq> searchAiMatchesByRequireUserId(Long requireUserId) {
        List<AiMatches_wlq> result = new ArrayList<>();
        for (AiMatches_wlq aiMatch : aiMatchesCache.values()) {
            if (aiMatch.getRequireUserId() != null && aiMatch.getRequireUserId().equals(requireUserId)) {
                result.add(aiMatch);
            }
        }
        return result;
    }

    /**
     * 根据响应用户ID搜索AI对话匹配结果数据
     */
    public static List<AiMatches_wlq> searchAiMatchesByRespondUserId(Long respondUserId) {
        List<AiMatches_wlq> result = new ArrayList<>();
        for (AiMatches_wlq aiMatch : aiMatchesCache.values()) {
            if (aiMatch.getRespondUserId() != null && aiMatch.getRespondUserId().equals(respondUserId)) {
                result.add(aiMatch);
            }
        }
        return result;
    }

    /**
     * 根据相似度范围搜索AI对话匹配结果数据
     */
    public static List<AiMatches_wlq> searchAiMatchesBySimilarityRange(Double minSimilarity, Double maxSimilarity) {
        List<AiMatches_wlq> result = new ArrayList<>();
        for (AiMatches_wlq aiMatch : aiMatchesCache.values()) {
            if (aiMatch.getSimilarity() != null) {
                if (minSimilarity == null || aiMatch.getSimilarity() >= minSimilarity) {
                    if (maxSimilarity == null || aiMatch.getSimilarity() <= maxSimilarity) {
                        result.add(aiMatch);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAiMatches", totalAiMatches);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aiMatchesCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiMatches_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
