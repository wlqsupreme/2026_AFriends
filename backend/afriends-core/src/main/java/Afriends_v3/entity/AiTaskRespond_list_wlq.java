package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;

/**
 * AI任务回应信息列表
 * 对应实体: AiTaskRespond_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiTaskRespond_list_wlq extends EntityList<AiTaskRespond_wlq> {

    // 内存存储
    private static final Map<Long, AiTaskRespond_wlq> aiTaskRespondCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAiTaskRespond = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AiTaskRespond_wlq> allAiTaskRespond) {
        try {
            System.out.println("开始直接加载AI任务回应数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiTaskRespond_wlq task : allAiTaskRespond) {
                System.out.println("处理任务回应数据: ID=" + task.getTaskId() + ", 名称=" + task.getRespondName());
                aiTaskRespondCache.put(task.getTaskId(), task);
            }

            // 更新统计信息
            totalAiTaskRespond = aiTaskRespondCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务回应数据直接加载完成！总数: " + totalAiTaskRespond);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI任务回应数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI任务回应数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiTaskRespond_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载AI任务回应数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI任务回应数据
            System.out.println("正在执行数据库查询...");
            List<AiTaskRespond_wlq> allAiTaskRespond = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAiTaskRespond.size() + " 条记录");

            // 存储到内存缓存
            for (AiTaskRespond_wlq task : allAiTaskRespond) {
                System.out.println("处理任务回应数据: ID=" + task.getTaskId() + ", 名称=" + task.getRespondName());
                aiTaskRespondCache.put(task.getTaskId(), task);
            }

            // 更新统计信息
            totalAiTaskRespond = aiTaskRespondCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务回应数据加载完成！总数: " + totalAiTaskRespond);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI任务回应数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI任务回应数据（从内存）
     */
    public static List<AiTaskRespond_wlq> getAllAiTaskRespond() {
        return new ArrayList<>(aiTaskRespondCache.values());
    }

    /**
     * 根据任务ID获取AI任务回应数据
     */
    public static AiTaskRespond_wlq getAiTaskRespondById(Long taskId) {
        return aiTaskRespondCache.get(taskId);
    }

    /**
     * 根据回应名称搜索AI任务回应数据
     */
    public static List<AiTaskRespond_wlq> searchAiTaskRespondByName(String name) {
        List<AiTaskRespond_wlq> result = new ArrayList<>();
        for (AiTaskRespond_wlq task : aiTaskRespondCache.values()) {
            if (task.getRespondName() != null && task.getRespondName().contains(name)) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 根据礼物价格范围搜索AI任务回应数据
     */
    public static List<AiTaskRespond_wlq> searchAiTaskRespondByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<AiTaskRespond_wlq> result = new ArrayList<>();
        for (AiTaskRespond_wlq task : aiTaskRespondCache.values()) {
            if (task.getRequiredGiftPrice() != null) {
                if (minPrice != null && task.getRequiredGiftPrice().compareTo(minPrice) < 0) {
                    continue;
                }
                if (maxPrice != null && task.getRequiredGiftPrice().compareTo(maxPrice) > 0) {
                    continue;
                }
                result.add(task);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAiTaskRespond", totalAiTaskRespond);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aiTaskRespondCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiTaskRespond_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

