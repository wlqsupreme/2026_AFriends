package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI任务需求信息列表
 * 对应实体: AiTaskRequire_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiTaskRequire_list_wlq extends EntityList<AiTaskRequire_wlq> {

    // 内存存储
    private static final Map<Long, AiTaskRequire_wlq> aiTaskRequireCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAiTaskRequire = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AiTaskRequire_wlq> allAiTaskRequire) {
        try {
            System.out.println("开始直接加载AI任务需求数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiTaskRequire_wlq task : allAiTaskRequire) {
                System.out.println("处理任务需求数据: ID=" + task.getTaskId() + ", 名称=" + task.getTaskName());
                aiTaskRequireCache.put(task.getTaskId(), task);
            }

            // 更新统计信息
            totalAiTaskRequire = aiTaskRequireCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务需求数据直接加载完成！总数: " + totalAiTaskRequire);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI任务需求数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI任务需求数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiTaskRequire_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载AI任务需求数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI任务需求数据
            System.out.println("正在执行数据库查询...");
            List<AiTaskRequire_wlq> allAiTaskRequire = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAiTaskRequire.size() + " 条记录");

            // 存储到内存缓存
            for (AiTaskRequire_wlq task : allAiTaskRequire) {
                System.out.println("处理任务需求数据: ID=" + task.getTaskId() + ", 名称=" + task.getTaskName());
                aiTaskRequireCache.put(task.getTaskId(), task);
            }

            // 更新统计信息
            totalAiTaskRequire = aiTaskRequireCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI任务需求数据加载完成！总数: " + totalAiTaskRequire);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI任务需求数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI任务需求数据（从内存）
     */
    public static List<AiTaskRequire_wlq> getAllAiTaskRequire() {
        return new ArrayList<>(aiTaskRequireCache.values());
    }

    /**
     * 根据任务ID获取AI任务需求数据
     */
    public static AiTaskRequire_wlq getAiTaskRequireById(Long taskId) {
        return aiTaskRequireCache.get(taskId);
    }

    /**
     * 根据任务名称搜索AI任务需求数据
     */
    public static List<AiTaskRequire_wlq> searchAiTaskRequireByName(String name) {
        List<AiTaskRequire_wlq> result = new ArrayList<>();
        for (AiTaskRequire_wlq task : aiTaskRequireCache.values()) {
            if (task.getTaskName() != null && task.getTaskName().contains(name)) {
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
        stats.put("totalAiTaskRequire", totalAiTaskRequire);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aiTaskRequireCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiTaskRequire_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

