package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 成就记录信息列表
 * 对应实体: AchievementRecord_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AchievementRecord_list_njj extends EntityList<AchievementRecord_njj> {

    // 内存存储
    private static final Map<Long, AchievementRecord_njj> achievementRecordCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<AchievementRecord_njj>> userAchievementCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAchievementRecord = 0;

    /**
     * 从数据库加载所有成就记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AchievementRecord_njj> mapper) {
        try {
            System.out.println("开始从数据库加载成就记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有成就记录数据
            List<AchievementRecord_njj> allAchievementRecord = mapper.selectList(null);

            // 存储到内存缓存
            for (AchievementRecord_njj record : allAchievementRecord) {
                achievementRecordCache.put(record.getRecordId(), record);

                // 按用户ID分组存储
                userAchievementCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
            }

            // 更新统计信息
            totalAchievementRecord = achievementRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("成就记录数据加载完成！总数: " + totalAchievementRecord);

        } catch (Exception e) {
            System.err.println("加载成就记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有成就记录数据（从内存）
     */
    public static List<AchievementRecord_njj> getAllAchievementRecord() {
        return new ArrayList<>(achievementRecordCache.values());
    }

    /**
     * 根据记录ID获取成就记录数据
     */
    public static AchievementRecord_njj getAchievementRecordById(Long recordId) {
        return achievementRecordCache.get(recordId);
    }

    /**
     * 根据用户ID获取成就记录
     */
    public static List<AchievementRecord_njj> getAchievementRecordByUserId(Long userId) {
        return userAchievementCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据成就ID获取成就记录
     */
    public static List<AchievementRecord_njj> getAchievementRecordByAchievementId(Long achievementId) {
        return achievementRecordCache.values().stream()
                .filter(record -> record.getAchievementId().equals(achievementId))
                .collect(Collectors.toList());
    }

    /**
     * 根据完成状态获取成就记录
     */
    public static List<AchievementRecord_njj> getAchievementRecordByStatus(Byte isCompleted) {
        return achievementRecordCache.values().stream()
                .filter(record -> record.getIsCompleted().equals(isCompleted))
                .collect(Collectors.toList());
    }

    /**
     * 获取用户已完成的成就记录
     */
    public static List<AchievementRecord_njj> getCompletedAchievementRecordByUserId(Long userId) {
        return getAchievementRecordByUserId(userId).stream()
                .filter(record -> record.getIsCompleted() == 1)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户未完成的成就记录
     */
    public static List<AchievementRecord_njj> getIncompleteAchievementRecordByUserId(Long userId) {
        return getAchievementRecordByUserId(userId).stream()
                .filter(record -> record.getIsCompleted() == 0)
                .collect(Collectors.toList());
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAchievementRecord", totalAchievementRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));

        // 完成状态统计
        long completedCount = achievementRecordCache.values().stream()
                .mapToLong(record -> record.getIsCompleted() == 1 ? 1 : 0)
                .sum();
        stats.put("completedCount", completedCount);
        stats.put("incompleteCount", totalAchievementRecord - completedCount);

        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        achievementRecordCache.clear();
        userAchievementCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AchievementRecord_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
