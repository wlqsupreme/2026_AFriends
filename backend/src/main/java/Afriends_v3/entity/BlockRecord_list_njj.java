package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 屏蔽记录列表
 * 对应实体: BlockRecord_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class BlockRecord_list_njj extends EntityList<BlockRecord_njj> {

    // 内存存储
    private static final Map<Long, BlockRecord_njj> blockRecordCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<BlockRecord_njj>> userBlockCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalBlockRecord = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<BlockRecord_njj> allBlockRecord) {
        try {
            System.out.println("开始直接加载屏蔽记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (BlockRecord_njj record : allBlockRecord) {
                System.out.println("处理屏蔽记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId()
                        + ", 被屏蔽用户ID=" + record.getBlockedUserId());
                blockRecordCache.put(record.getRecordId(), record);

                // 按用户ID分组存储
                userBlockCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalBlockRecord = blockRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("屏蔽记录数据直接加载完成！总数: " + totalBlockRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载屏蔽记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有屏蔽记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<BlockRecord_njj> mapper) {
        try {
            System.out.println("开始从数据库加载屏蔽记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有屏蔽记录数据
            System.out.println("正在执行数据库查询...");
            List<BlockRecord_njj> allBlockRecord = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allBlockRecord.size() + " 条记录");

            // 存储到内存缓存
            for (BlockRecord_njj record : allBlockRecord) {
                System.out.println("处理屏蔽记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId()
                        + ", 被屏蔽用户ID=" + record.getBlockedUserId());
                blockRecordCache.put(record.getRecordId(), record);

                // 按用户ID分组存储
                userBlockCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalBlockRecord = blockRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("屏蔽记录数据加载完成！总数: " + totalBlockRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载屏蔽记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有屏蔽记录数据（从内存）
     */
    public static List<BlockRecord_njj> getAllBlockRecord() {
        return new ArrayList<>(blockRecordCache.values());
    }

    /**
     * 根据记录ID获取屏蔽记录数据
     */
    public static BlockRecord_njj getBlockRecordById(Long recordId) {
        return blockRecordCache.get(recordId);
    }

    /**
     * 根据用户ID获取屏蔽记录数据
     */
    public static List<BlockRecord_njj> getBlockRecordByUserId(Long userId) {
        return userBlockCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据被屏蔽用户ID获取屏蔽记录数据
     */
    public static List<BlockRecord_njj> getBlockRecordByBlockedUserId(Long blockedUserId) {
        List<BlockRecord_njj> result = new ArrayList<>();
        for (BlockRecord_njj record : blockRecordCache.values()) {
            if (record.getBlockedUserId().equals(blockedUserId)) {
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
        stats.put("totalBlockRecord", totalBlockRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        blockRecordCache.clear();
        userBlockCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<BlockRecord_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加拉黑记录到内存缓存
     */
    public static void addBlockRecord(BlockRecord_njj record) {
        blockRecordCache.put(record.getRecordId(), record);
        userBlockCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
        totalBlockRecord = blockRecordCache.size();
        lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * 从内存缓存中移除拉黑记录（解除拉黑）
     */
    public static void removeBlockRecord(Long userId, Long blockedUserId) {
        List<BlockRecord_njj> userBlocks = userBlockCache.get(userId);
        if (userBlocks != null) {
            userBlocks.removeIf(record -> record.getBlockedUserId().equals(blockedUserId) && record.getIsBlocked() == 1);
            blockRecordCache.values().removeIf(record -> 
                record.getUserId().equals(userId) && record.getBlockedUserId().equals(blockedUserId) && record.getIsBlocked() == 1);
            totalBlockRecord = blockRecordCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    /**
     * 检查用户是否已拉黑指定用户
     */
    public static boolean isBlocked(Long userId, Long blockedUserId) {
        List<BlockRecord_njj> userBlocks = userBlockCache.get(userId);
        if (userBlocks != null) {
            return userBlocks.stream().anyMatch(record -> 
                record.getBlockedUserId().equals(blockedUserId) && record.getIsBlocked() == 1);
        }
        return false;
    }
}