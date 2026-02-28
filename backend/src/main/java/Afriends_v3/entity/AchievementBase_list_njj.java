package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 成就基础信息列表
 * 对应实体: AchievementBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AchievementBase_list_njj extends EntityList<AchievementBase_njj> {

    // 内存存储
    private static final Map<Long, AchievementBase_njj> achievementBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAchievementBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AchievementBase_njj> allAchievementBase) {
        try {
            System.out.println("开始直接加载成就基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AchievementBase_njj achievement : allAchievementBase) {
                System.out.println("处理成就数据: ID=" + achievement.getAchievementId() + ", 名称=" + achievement.getName());
                achievementBaseCache.put(achievement.getAchievementId(), achievement);
            }

            // 更新统计信息
            totalAchievementBase = achievementBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("成就基础数据直接加载完成！总数: " + totalAchievementBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载成就基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有成就基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AchievementBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载成就基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有成就基础数据
            System.out.println("正在执行数据库查询...");
            List<AchievementBase_njj> allAchievementBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAchievementBase.size() + " 条记录");

            // 存储到内存缓存
            for (AchievementBase_njj achievement : allAchievementBase) {
                System.out.println("处理成就数据: ID=" + achievement.getAchievementId() + ", 名称=" + achievement.getName());
                achievementBaseCache.put(achievement.getAchievementId(), achievement);
            }

            // 更新统计信息
            totalAchievementBase = achievementBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("成就基础数据加载完成！总数: " + totalAchievementBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载成就基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有成就基础数据（从内存）
     */
    public static List<AchievementBase_njj> getAllAchievementBase() {
        return new ArrayList<>(achievementBaseCache.values());
    }

    /**
     * 根据成就ID获取成就基础数据
     */
    public static AchievementBase_njj getAchievementBaseById(Long achievementId) {
        return achievementBaseCache.get(achievementId);
    }

    /**
     * 根据名称搜索成就基础数据
     */
    public static List<AchievementBase_njj> searchAchievementBaseByName(String name) {
        List<AchievementBase_njj> result = new ArrayList<>();
        for (AchievementBase_njj achievement : achievementBaseCache.values()) {
            if (achievement.getName() != null && achievement.getName().contains(name)) {
                result.add(achievement);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAchievementBase", totalAchievementBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        achievementBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AchievementBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}