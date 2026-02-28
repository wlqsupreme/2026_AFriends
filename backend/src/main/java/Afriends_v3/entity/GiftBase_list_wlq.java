package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.math.BigDecimal;

/**
 * 礼物基表信息列表
 * 对应实体: GiftBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class GiftBase_list_wlq extends EntityList<GiftBase_wlq> {

    // 内存存储
    private static final Map<Long, GiftBase_wlq> giftBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalGiftBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<GiftBase_wlq> allGiftBase) {
        try {
            System.out.println("开始直接加载礼物基表数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (GiftBase_wlq gift : allGiftBase) {
                System.out.println("处理礼物数据: ID=" + gift.getGiftId() + ", 价格=" + gift.getPrice());
                giftBaseCache.put(gift.getGiftId(), gift);
            }

            // 更新统计信息
            totalGiftBase = giftBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("礼物基表数据直接加载完成！总数: " + totalGiftBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载礼物基表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有礼物基表数据到内存
     */
    public static void loadFromDatabase(BaseMapper<GiftBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载礼物基表数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有礼物基表数据
            System.out.println("正在执行数据库查询...");
            List<GiftBase_wlq> allGiftBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allGiftBase.size() + " 条记录");

            // 存储到内存缓存
            for (GiftBase_wlq gift : allGiftBase) {
                System.out.println("处理礼物数据: ID=" + gift.getGiftId() + ", 价格=" + gift.getPrice());
                giftBaseCache.put(gift.getGiftId(), gift);
            }

            // 更新统计信息
            totalGiftBase = giftBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("礼物基表数据加载完成！总数: " + totalGiftBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载礼物基表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有礼物基表数据（从内存）
     */
    public static List<GiftBase_wlq> getAllGiftBase() {
        return new ArrayList<>(giftBaseCache.values());
    }

    /**
     * 根据礼物ID获取礼物基表数据
     */
    public static GiftBase_wlq getGiftBaseById(Long giftId) {
        return giftBaseCache.get(giftId);
    }

    /**
     * 根据价格范围搜索礼物基表数据
     */
    public static List<GiftBase_wlq> searchGiftBaseByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<GiftBase_wlq> result = new ArrayList<>();
        for (GiftBase_wlq gift : giftBaseCache.values()) {
            if (gift.getPrice() != null) {
                if (minPrice != null && gift.getPrice().compareTo(minPrice) < 0) {
                    continue;
                }
                if (maxPrice != null && gift.getPrice().compareTo(maxPrice) > 0) {
                    continue;
                }
                result.add(gift);
            }
        }
        return result;
    }

    /**
     * 根据描述搜索礼物基表数据
     */
    public static List<GiftBase_wlq> searchGiftBaseByDescription(String description) {
        List<GiftBase_wlq> result = new ArrayList<>();
        for (GiftBase_wlq gift : giftBaseCache.values()) {
            if (gift.getDescription() != null && gift.getDescription().contains(description)) {
                result.add(gift);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalGiftBase", totalGiftBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        giftBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<GiftBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

