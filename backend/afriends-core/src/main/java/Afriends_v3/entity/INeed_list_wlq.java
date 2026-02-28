package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户需求信息列表
 * 对应实体: INeed_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class INeed_list_wlq extends EntityList<INeed_wlq> {

    // 内存存储
    private static final Map<Long, INeed_wlq> iNeedCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalINeed = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<INeed_wlq> allINeed) {
        try {
            System.out.println("开始直接加载用户需求信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (INeed_wlq need : allINeed) {
                System.out.println("处理需求数据: ID=" + need.getNeedId() + ", 标题=" + need.getTitle());
                iNeedCache.put(need.getNeedId(), need);
            }

            // 更新统计信息
            totalINeed = iNeedCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户需求信息数据直接加载完成！总数: " + totalINeed);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户需求信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户需求信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<INeed_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户需求信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户需求信息数据
            System.out.println("正在执行数据库查询...");
            List<INeed_wlq> allINeed = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allINeed.size() + " 条记录");

            // 存储到内存缓存
            for (INeed_wlq need : allINeed) {
                System.out.println("处理需求数据: ID=" + need.getNeedId() + ", 标题=" + need.getTitle());
                iNeedCache.put(need.getNeedId(), need);
            }

            // 更新统计信息
            totalINeed = iNeedCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户需求信息数据加载完成！总数: " + totalINeed);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户需求信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户需求信息数据（从内存）
     */
    public static List<INeed_wlq> getAllINeed() {
        return new ArrayList<>(iNeedCache.values());
    }

    /**
     * 根据需求ID获取用户需求信息数据
     */
    public static INeed_wlq getINeedById(Long needId) {
        return iNeedCache.get(needId);
    }

    /**
     * 根据用户ID搜索用户需求信息数据
     */
    public static List<INeed_wlq> searchINeedByUserId(Long userId) {
        List<INeed_wlq> result = new ArrayList<>();
        for (INeed_wlq need : iNeedCache.values()) {
            if (need.getUserId() != null && need.getUserId().equals(userId)) {
                result.add(need);
            }
        }
        return result;
    }

    /**
     * 根据标题搜索用户需求信息数据
     */
    public static List<INeed_wlq> searchINeedByTitle(String title) {
        List<INeed_wlq> result = new ArrayList<>();
        for (INeed_wlq need : iNeedCache.values()) {
            if (need.getTitle() != null && need.getTitle().contains(title)) {
                result.add(need);
            }
        }
        return result;
    }

    /**
     * 根据内容类型搜索用户需求信息数据
     */
    public static List<INeed_wlq> searchINeedByContentType(String contentType) {
        List<INeed_wlq> result = new ArrayList<>();
        for (INeed_wlq need : iNeedCache.values()) {
            if (need.getContentType() != null && need.getContentType().contains(contentType)) {
                result.add(need);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索用户需求信息数据
     */
    public static List<INeed_wlq> searchINeedByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<INeed_wlq> result = new ArrayList<>();
        for (INeed_wlq need : iNeedCache.values()) {
            if (need.getHeatScore() != null) {
                if (minHeatScore != null && need.getHeatScore() < minHeatScore) {
                    continue;
                }
                if (maxHeatScore != null && need.getHeatScore() > maxHeatScore) {
                    continue;
                }
                result.add(need);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalINeed", totalINeed);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iNeedCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<INeed_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
