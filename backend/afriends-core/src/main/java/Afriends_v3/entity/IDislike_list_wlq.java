package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我不喜欢内容基础信息列表
 * 对应实体: IDislike_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IDislike_list_wlq extends EntityList<IDislike_wlq> {

    // 内存存储
    private static final Map<Long, IDislike_wlq> iDislikeCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIDislike = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IDislike_wlq> allIDislike) {
        try {
            System.out.println("开始直接加载我不喜欢内容基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IDislike_wlq dislike : allIDislike) {
                System.out.println("处理不喜欢内容数据: ID=" + dislike.getDislikeId() + ", 用户ID=" + dislike.getUserId());
                iDislikeCache.put(dislike.getDislikeId(), dislike);
            }

            // 更新统计信息
            totalIDislike = iDislikeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容基础数据直接加载完成！总数: " + totalIDislike);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我不喜欢内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我不喜欢内容基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IDislike_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我不喜欢内容基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我不喜欢内容基础数据
            System.out.println("正在执行数据库查询...");
            List<IDislike_wlq> allIDislike = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIDislike.size() + " 条记录");

            // 存储到内存缓存
            for (IDislike_wlq dislike : allIDislike) {
                System.out.println("处理不喜欢内容数据: ID=" + dislike.getDislikeId() + ", 用户ID=" + dislike.getUserId());
                iDislikeCache.put(dislike.getDislikeId(), dislike);
            }

            // 更新统计信息
            totalIDislike = iDislikeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容基础数据加载完成！总数: " + totalIDislike);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我不喜欢内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我不喜欢内容基础数据（从内存）
     */
    public static List<IDislike_wlq> getAllIDislike() {
        return new ArrayList<>(iDislikeCache.values());
    }

    /**
     * 根据不喜欢内容ID获取我不喜欢内容基础数据
     */
    public static IDislike_wlq getIDislikeById(Long dislikeId) {
        return iDislikeCache.get(dislikeId);
    }

    /**
     * 根据用户ID搜索我不喜欢内容基础数据
     */
    public static List<IDislike_wlq> searchIDislikeByUserId(Long userId) {
        List<IDislike_wlq> result = new ArrayList<>();
        for (IDislike_wlq dislike : iDislikeCache.values()) {
            if (dislike.getUserId() != null && dislike.getUserId().equals(userId)) {
                result.add(dislike);
            }
        }
        return result;
    }

    /**
     * 根据不喜欢类型搜索我不喜欢内容基础数据
     */
    public static List<IDislike_wlq> searchIDislikeByDislikeType(String dislikeType) {
        List<IDislike_wlq> result = new ArrayList<>();
        for (IDislike_wlq dislike : iDislikeCache.values()) {
            if (dislike.getDislikeType() != null && dislike.getDislikeType().contains(dislikeType)) {
                result.add(dislike);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索我不喜欢内容基础数据
     */
    public static List<IDislike_wlq> searchIDislikeByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<IDislike_wlq> result = new ArrayList<>();
        for (IDislike_wlq dislike : iDislikeCache.values()) {
            if (dislike.getHeatScore() != null) {
                if (minHeatScore != null && dislike.getHeatScore() < minHeatScore) {
                    continue;
                }
                if (maxHeatScore != null && dislike.getHeatScore() > maxHeatScore) {
                    continue;
                }
                result.add(dislike);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIDislike", totalIDislike);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iDislikeCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IDislike_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

