package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 购买记录列表
 * 对应实体: PurchaseRecord_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class PurchaseRecord_list_njj extends EntityList<PurchaseRecord_njj> {

    // 内存存储
    private static final Map<Long, PurchaseRecord_njj> purchaseRecordCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<PurchaseRecord_njj>> userPurchaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalPurchaseRecord = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<PurchaseRecord_njj> allPurchaseRecord) {
        try {
            System.out.println("开始直接加载购买记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (PurchaseRecord_njj record : allPurchaseRecord) {
                System.out.println("处理购买记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId() + ", 商品名称="
                        + record.getItemName());
                purchaseRecordCache.put(record.getRecordId(), record);

                // 按用户ID分组存储
                userPurchaseCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalPurchaseRecord = purchaseRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("购买记录数据直接加载完成！总数: " + totalPurchaseRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载购买记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有购买记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<PurchaseRecord_njj> mapper) {
        try {
            System.out.println("开始从数据库加载购买记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有购买记录数据
            System.out.println("正在执行数据库查询...");
            List<PurchaseRecord_njj> allPurchaseRecord = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allPurchaseRecord.size() + " 条记录");

            // 存储到内存缓存
            for (PurchaseRecord_njj record : allPurchaseRecord) {
                System.out.println("处理购买记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId() + ", 商品名称="
                        + record.getItemName());
                purchaseRecordCache.put(record.getRecordId(), record);

                // 按用户ID分组存储
                userPurchaseCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalPurchaseRecord = purchaseRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("购买记录数据加载完成！总数: " + totalPurchaseRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载购买记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有购买记录数据（从内存）
     */
    public static List<PurchaseRecord_njj> getAllPurchaseRecord() {
        return new ArrayList<>(purchaseRecordCache.values());
    }

    /**
     * 根据记录ID获取购买记录数据
     */
    public static PurchaseRecord_njj getPurchaseRecordById(Long recordId) {
        return purchaseRecordCache.get(recordId);
    }

    /**
     * 根据用户ID获取购买记录数据
     */
    public static List<PurchaseRecord_njj> getPurchaseRecordByUserId(Long userId) {
        return userPurchaseCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据商品类型获取购买记录数据
     */
    public static List<PurchaseRecord_njj> getPurchaseRecordByItemType(String itemType) {
        List<PurchaseRecord_njj> result = new ArrayList<>();
        for (PurchaseRecord_njj record : purchaseRecordCache.values()) {
            if (record.getItemType() != null && record.getItemType().equals(itemType)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPurchaseRecord", totalPurchaseRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        purchaseRecordCache.clear();
        userPurchaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<PurchaseRecord_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加新的购买记录到缓存
     */
    public static void addToCache(PurchaseRecord_njj purchaseRecord) {
        if (purchaseRecord != null && purchaseRecord.getRecordId() != null) {
            // 添加到主缓存
            purchaseRecordCache.put(purchaseRecord.getRecordId(), purchaseRecord);

            // 添加到用户分组缓存
            userPurchaseCache.computeIfAbsent(purchaseRecord.getUserId(), k -> new ArrayList<>()).add(purchaseRecord);

            // 更新统计信息
            totalPurchaseRecord = purchaseRecordCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("购买记录已添加到缓存，recordId: " + purchaseRecord.getRecordId());
        }
    }
}