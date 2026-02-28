package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 产品基础信息列表
 * 对应实体: ProductsBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ProductsBase_list_njj extends EntityList<ProductsBase_njj> {

    // 内存存储
    private static final Map<Long, ProductsBase_njj> productsBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalProductsBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ProductsBase_njj> allProductsBase) {
        try {
            System.out.println("开始直接加载产品基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ProductsBase_njj product : allProductsBase) {
                System.out.println("处理产品数据: ID=" + product.getCoinRechargeId() + ", 币数=" + product.getCoinNum());
                productsBaseCache.put(product.getCoinRechargeId(), product);
            }

            // 更新统计信息
            totalProductsBase = productsBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("产品基础信息数据直接加载完成！总数: " + totalProductsBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载产品基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有产品基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ProductsBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载产品基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有产品基础信息数据
            System.out.println("正在执行数据库查询...");
            List<ProductsBase_njj> allProductsBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allProductsBase.size() + " 条记录");

            // 存储到内存缓存
            for (ProductsBase_njj product : allProductsBase) {
                System.out.println("处理产品数据: ID=" + product.getCoinRechargeId() + ", 币数=" + product.getCoinNum());
                productsBaseCache.put(product.getCoinRechargeId(), product);
            }

            // 更新统计信息
            totalProductsBase = productsBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("产品基础信息数据加载完成！总数: " + totalProductsBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载产品基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有产品基础信息数据（从内存）
     */
    public static List<ProductsBase_njj> getAllProductsBase() {
        return new ArrayList<>(productsBaseCache.values());
    }

    /**
     * 根据产品ID获取产品基础信息数据
     */
    public static ProductsBase_njj getProductsBaseById(Long coinRechargeId) {
        return productsBaseCache.get(coinRechargeId);
    }

    /**
     * 根据状态获取产品基础信息数据
     */
    public static List<ProductsBase_njj> getProductsBaseByStatus(Byte status) {
        List<ProductsBase_njj> result = new ArrayList<>();
        for (ProductsBase_njj product : productsBaseCache.values()) {
            if (product.getStatus() != null && product.getStatus().equals(status)) {
                result.add(product);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProductsBase", totalProductsBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        productsBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ProductsBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}