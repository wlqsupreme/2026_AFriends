package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模型等级规则列表
 * 对应实体: AimodelLevelRule_njj
 * 功能：从数据库读取数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AimodelLevelRule_list_njj extends EntityList<AimodelLevelRule_njj> {

    // 内存存储
    private static final Map<Long, AimodelLevelRule_njj> levelRuleCache = new ConcurrentHashMap<>();
    private static final Map<Integer, AimodelLevelRule_njj> levelRuleByLevelCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalLevelRules = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AimodelLevelRule_njj> allLevelRules) {
        try {
            System.out.println("开始直接加载AI模型等级规则数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AimodelLevelRule_njj rule : allLevelRules) {
                System.out.println("处理等级规则数据: levelId=" + rule.getLevelId() +
                        ", level=" + rule.getLevel() +
                        ", expRequirement=" + rule.getExpRequirement());
                levelRuleCache.put(rule.getLevelId(), rule);
                levelRuleByLevelCache.put(rule.getLevel(), rule);
            }

            // 更新统计信息
            totalLevelRules = levelRuleCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型等级规则数据直接加载完成！总数: " + totalLevelRules);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI模型等级规则数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI模型等级规则数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AimodelLevelRule_njj> mapper) {
        try {
            System.out.println("开始从数据库加载AI模型等级规则数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI模型等级规则数据
            System.out.println("正在执行数据库查询...");
            List<AimodelLevelRule_njj> allLevelRules = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allLevelRules.size() + " 条记录");

            // 存储到内存缓存
            for (AimodelLevelRule_njj rule : allLevelRules) {
                System.out.println("处理等级规则数据: levelId=" + rule.getLevelId() +
                        ", level=" + rule.getLevel() +
                        ", expRequirement=" + rule.getExpRequirement());
                levelRuleCache.put(rule.getLevelId(), rule);
                levelRuleByLevelCache.put(rule.getLevel(), rule);
            }

            // 更新统计信息
            totalLevelRules = levelRuleCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模型等级规则数据加载完成！总数: " + totalLevelRules);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI模型等级规则数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI模型等级规则数据（从内存）
     */
    public static List<AimodelLevelRule_njj> getAllLevelRules() {
        return new ArrayList<>(levelRuleCache.values());
    }

    /**
     * 根据等级ID获取AI模型等级规则数据
     */
    public static AimodelLevelRule_njj getLevelRuleById(Long levelId) {
        return levelRuleCache.get(levelId);
    }

    /**
     * 根据等级获取AI模型等级规则数据
     */
    public static AimodelLevelRule_njj getLevelRuleByLevel(Integer level) {
        return levelRuleByLevelCache.get(level);
    }

    /**
     * 获取下一级等级规则
     */
    public static AimodelLevelRule_njj getNextLevelRule(Integer currentLevel) {
        return levelRuleByLevelCache.get(currentLevel + 1);
    }

    /**
     * 获取所有等级规则（按等级排序）
     */
    public static List<AimodelLevelRule_njj> getAllLevelRulesOrderByLevel() {
        return levelRuleByLevelCache.values().stream()
                .sorted(Comparator.comparing(AimodelLevelRule_njj::getLevel))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLevelRules", totalLevelRules);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        levelRuleCache.clear();
        levelRuleByLevelCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AimodelLevelRule_njj> mapper) {
        loadFromDatabase(mapper);
    }
}