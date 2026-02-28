package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我拥有物品信息列表
 * 对应实体: IHave_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IHave_list_wlq extends EntityList<IHave_wlq> {

    // 内存存储
    private static final Map<Long, IHave_wlq> iHaveCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIHave = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IHave_wlq> allIHave) {
        try {
            System.out.println("开始直接加载我拥有物品数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IHave_wlq have : allIHave) {
                System.out.println("处理拥有物品数据: ID=" + have.getHaveId() + ", 标题=" + have.getTitle());
                iHaveCache.put(have.getHaveId(), have);
            }

            // 更新统计信息
            totalIHave = iHaveCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我拥有物品数据直接加载完成！总数: " + totalIHave);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我拥有物品数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我拥有物品数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IHave_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我拥有物品数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我拥有物品数据
            System.out.println("正在执行数据库查询...");
            List<IHave_wlq> allIHave = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIHave.size() + " 条记录");

            // 存储到内存缓存
            for (IHave_wlq have : allIHave) {
                System.out.println("处理拥有物品数据: ID=" + have.getHaveId() + ", 标题=" + have.getTitle());
                iHaveCache.put(have.getHaveId(), have);
            }

            // 更新统计信息
            totalIHave = iHaveCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我拥有物品数据加载完成！总数: " + totalIHave);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我拥有物品数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我拥有物品数据（从内存）
     */
    public static List<IHave_wlq> getAllIHave() {
        return new ArrayList<>(iHaveCache.values());
    }

    /**
     * 根据拥有物品ID获取我拥有物品数据
     */
    public static IHave_wlq getIHaveById(Long haveId) {
        return iHaveCache.get(haveId);
    }

    /**
     * 根据用户ID搜索我拥有物品数据
     */
    public static List<IHave_wlq> searchIHaveByUserId(Long userId) {
        List<IHave_wlq> result = new ArrayList<>();
        for (IHave_wlq have : iHaveCache.values()) {
            if (have.getUserId() != null && have.getUserId().equals(userId)) {
                result.add(have);
            }
        }
        return result;
    }

    /**
     * 根据标题搜索我拥有物品数据
     */
    public static List<IHave_wlq> searchIHaveByTitle(String title) {
        List<IHave_wlq> result = new ArrayList<>();
        for (IHave_wlq have : iHaveCache.values()) {
            if (have.getTitle() != null && have.getTitle().contains(title)) {
                result.add(have);
            }
        }
        return result;
    }

    /**
     * 根据物品类型搜索我拥有物品数据
     */
    public static List<IHave_wlq> searchIHaveByItemType(String itemType) {
        List<IHave_wlq> result = new ArrayList<>();
        for (IHave_wlq have : iHaveCache.values()) {
            if (have.getItemType() != null && have.getItemType().contains(itemType)) {
                result.add(have);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索我拥有物品数据
     */
    public static List<IHave_wlq> searchIHaveByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<IHave_wlq> result = new ArrayList<>();
        for (IHave_wlq have : iHaveCache.values()) {
            if (have.getHeatScore() != null) {
                if (minHeatScore != null && have.getHeatScore() < minHeatScore) {
                    continue;
                }
                if (maxHeatScore != null && have.getHeatScore() > maxHeatScore) {
                    continue;
                }
                result.add(have);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIHave", totalIHave);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iHaveCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IHave_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
