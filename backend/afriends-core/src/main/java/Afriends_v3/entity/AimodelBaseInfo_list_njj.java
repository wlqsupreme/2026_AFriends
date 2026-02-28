package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模型基础信息列表
 * 对应实体: AimodelBaseInfo_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AimodelBaseInfo_list_njj extends EntityList<AimodelBaseInfo_njj> {

    // 内存存储
    private static final Map<Long, AimodelBaseInfo_njj> aimodelBaseInfoCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAimodelBaseInfo = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AimodelBaseInfo_njj> allAimodelBaseInfo) {
        try {
            System.out.println("开始直接加载AI模型基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AimodelBaseInfo_njj aimodel : allAimodelBaseInfo) {
                System.out.println("处理AI模型数据: ID=" + aimodel.getModelId() + ", 名称=" + aimodel.getModelName());
                aimodelBaseInfoCache.put(aimodel.getModelId(), aimodel);
            }

            // 更新统计信息
            totalAimodelBaseInfo = aimodelBaseInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型基础信息数据直接加载完成！总数: " + totalAimodelBaseInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI模型基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI模型基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AimodelBaseInfo_njj> mapper) {
        try {
            System.out.println("开始从数据库加载AI模型基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI模型基础信息数据
            System.out.println("正在执行数据库查询...");
            List<AimodelBaseInfo_njj> allAimodelBaseInfo = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAimodelBaseInfo.size() + " 条记录");

            // 存储到内存缓存
            for (AimodelBaseInfo_njj aimodel : allAimodelBaseInfo) {
                System.out.println("处理AI模型数据: ID=" + aimodel.getModelId() + ", 名称=" + aimodel.getModelName());
                aimodelBaseInfoCache.put(aimodel.getModelId(), aimodel);
            }

            // 更新统计信息
            totalAimodelBaseInfo = aimodelBaseInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型基础信息数据加载完成！总数: " + totalAimodelBaseInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI模型基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI模型基础信息数据（从内存）
     * 按创建时间降序，模型ID升序排列
     */
    public static List<AimodelBaseInfo_njj> getAllAimodelBaseInfo() {
        List<AimodelBaseInfo_njj> result = new ArrayList<>(aimodelBaseInfoCache.values());

        // 按创建时间降序，模型ID升序排列
        result.sort((a, b) -> {
            // 首先按创建时间降序排列
            if (a.getCreatedAt() != null && b.getCreatedAt() != null) {
                int timeCompare = b.getCreatedAt().compareTo(a.getCreatedAt());
                if (timeCompare != 0) {
                    return timeCompare;
                }
            }
            // 如果创建时间相同或为空，按模型ID升序排列
            if (a.getModelId() != null && b.getModelId() != null) {
                return a.getModelId().compareTo(b.getModelId());
            }
            return 0;
        });

        return result;
    }

    /**
     * 根据模型ID获取AI模型基础信息数据
     */
    public static AimodelBaseInfo_njj getAimodelBaseInfoById(Long modelId) {
        return aimodelBaseInfoCache.get(modelId);
    }

    /**
     * 根据模型名称搜索AI模型基础信息数据
     */
    public static List<AimodelBaseInfo_njj> searchAimodelBaseInfoByName(String name) {
        List<AimodelBaseInfo_njj> result = new ArrayList<>();
        for (AimodelBaseInfo_njj aimodel : aimodelBaseInfoCache.values()) {
            if (aimodel.getModelName() != null && aimodel.getModelName().contains(name)) {
                result.add(aimodel);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAimodelBaseInfo", totalAimodelBaseInfo);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aimodelBaseInfoCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AimodelBaseInfo_njj> mapper) {
        loadFromDatabase(mapper);
    }
}