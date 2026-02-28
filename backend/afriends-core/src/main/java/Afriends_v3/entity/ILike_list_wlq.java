package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我喜欢内容基础信息列表
 * 对应实体: ILike_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ILike_list_wlq extends EntityList<ILike_wlq> {

    // 内存存储
    private static final Map<Long, ILike_wlq> iLikeCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalILike = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ILike_wlq> allILike) {
        try {
            System.out.println("开始直接加载我喜欢内容基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ILike_wlq like : allILike) {
                System.out.println("处理喜欢内容数据: ID=" + like.getLikeId() + ", 用户ID=" + like.getUserId());
                iLikeCache.put(like.getLikeId(), like);
            }

            // 更新统计信息
            totalILike = iLikeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我喜欢内容基础数据直接加载完成！总数: " + totalILike);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我喜欢内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我喜欢内容基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ILike_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我喜欢内容基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我喜欢内容基础数据
            System.out.println("正在执行数据库查询...");
            List<ILike_wlq> allILike = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allILike.size() + " 条记录");

            // 存储到内存缓存
            for (ILike_wlq like : allILike) {
                System.out.println("处理喜欢内容数据: ID=" + like.getLikeId() + ", 用户ID=" + like.getUserId());
                iLikeCache.put(like.getLikeId(), like);
            }

            // 更新统计信息
            totalILike = iLikeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我喜欢内容基础数据加载完成！总数: " + totalILike);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我喜欢内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我喜欢内容基础数据（从内存）
     */
    public static List<ILike_wlq> getAllILike() {
        return new ArrayList<>(iLikeCache.values());
    }

    /**
     * 根据喜欢内容ID获取我喜欢内容基础数据
     */
    public static ILike_wlq getILikeById(Long likeId) {
        return iLikeCache.get(likeId);
    }

    /**
     * 根据用户ID搜索我喜欢内容基础数据
     */
    public static List<ILike_wlq> searchILikeByUserId(Long userId) {
        List<ILike_wlq> result = new ArrayList<>();
        for (ILike_wlq like : iLikeCache.values()) {
            if (like.getUserId() != null && like.getUserId().equals(userId)) {
                result.add(like);
            }
        }
        return result;
    }

    /**
     * 根据喜欢类型搜索我喜欢内容基础数据
     */
    public static List<ILike_wlq> searchILikeByLikeType(String likeType) {
        List<ILike_wlq> result = new ArrayList<>();
        for (ILike_wlq like : iLikeCache.values()) {
            if (like.getLikeType() != null && like.getLikeType().contains(likeType)) {
                result.add(like);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索我喜欢内容基础数据
     */
    public static List<ILike_wlq> searchILikeByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<ILike_wlq> result = new ArrayList<>();
        for (ILike_wlq like : iLikeCache.values()) {
            if (like.getHeatScore() != null) {
                if (minHeatScore != null && like.getHeatScore() < minHeatScore) {
                    continue;
                }
                if (maxHeatScore != null && like.getHeatScore() > maxHeatScore) {
                    continue;
                }
                result.add(like);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalILike", totalILike);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iLikeCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ILike_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
