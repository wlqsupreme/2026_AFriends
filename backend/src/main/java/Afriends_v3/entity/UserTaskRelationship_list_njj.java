package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户任务关系信息列表
 * 对应实体: UserTaskRelationship_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserTaskRelationship_list_njj extends EntityList<UserTaskRelationship_njj> {

    // 内存存储
    private static final Map<Long, UserTaskRelationship_njj> userTaskRelationshipCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserTaskRelationship_njj>> userTaskCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserTaskRelationship = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserTaskRelationship_njj> allUserTaskRelationship) {
        try {
            System.out.println("开始直接加载用户任务关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserTaskRelationship == null || allUserTaskRelationship.isEmpty()) {
                System.out.println("用户任务关系数据为空，清空缓存并设置状态");
                totalUserTaskRelationship = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户任务关系数据直接加载完成！总数: " + totalUserTaskRelationship);
                return;
            }

            // 存储到内存缓存
            for (UserTaskRelationship_njj relationship : allUserTaskRelationship) {
                if (relationship != null && relationship.getUserTaskId() != null) {
                    System.out.println(
                            "处理用户任务关系数据: ID=" + relationship.getUserTaskId() + ", 用户ID=" + relationship.getUserId());
                    userTaskRelationshipCache.put(relationship.getUserTaskId(), relationship);

                    // 按用户ID分组存储
                    userTaskCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>()).add(relationship);
                }
            }

            // 更新统计信息
            totalUserTaskRelationship = userTaskRelationshipCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户任务关系数据直接加载完成！总数: " + totalUserTaskRelationship);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户任务关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserTaskRelationship = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户任务关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserTaskRelationship_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户任务关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户任务关系数据
            System.out.println("正在执行数据库查询...");
            List<UserTaskRelationship_njj> allUserTaskRelationship = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserTaskRelationship.size() + " 条记录");

            // 存储到内存缓存
            for (UserTaskRelationship_njj relationship : allUserTaskRelationship) {
                if (relationship != null && relationship.getUserTaskId() != null) {
                    System.out.println(
                            "处理用户任务关系数据: ID=" + relationship.getUserTaskId() + ", 用户ID=" + relationship.getUserId());
                    userTaskRelationshipCache.put(relationship.getUserTaskId(), relationship);

                    // 按用户ID分组存储
                    userTaskCache.computeIfAbsent(relationship.getUserId(), k -> new ArrayList<>()).add(relationship);
                }
            }

            // 更新统计信息
            totalUserTaskRelationship = userTaskRelationshipCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户任务关系数据加载完成！总数: " + totalUserTaskRelationship);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户任务关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户任务关系数据（从内存）
     */
    public static List<UserTaskRelationship_njj> getAllUserTaskRelationship() {
        return new ArrayList<>(userTaskRelationshipCache.values());
    }

    /**
     * 根据任务关系ID获取用户任务关系数据
     */
    public static UserTaskRelationship_njj getUserTaskRelationshipById(Long userTaskId) {
        return userTaskRelationshipCache.get(userTaskId);
    }

    /**
     * 根据用户ID获取用户任务关系数据
     */
    public static List<UserTaskRelationship_njj> getUserTaskRelationshipByUserId(Long userId) {
        return userTaskCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据状态获取用户任务关系数据
     */
    public static List<UserTaskRelationship_njj> getUserTaskRelationshipByStatus(Byte status) {
        List<UserTaskRelationship_njj> result = new ArrayList<>();
        for (UserTaskRelationship_njj relationship : userTaskRelationshipCache.values()) {
            if (relationship.getStatus() != null && relationship.getStatus().equals(status)) {
                result.add(relationship);
            }
        }
        return result;
    }

    /**
     * 根据任务进度范围获取用户任务关系数据
     */
    public static List<UserTaskRelationship_njj> getUserTaskRelationshipByTaskPercentRange(Byte minPercent,
            Byte maxPercent) {
        List<UserTaskRelationship_njj> result = new ArrayList<>();
        for (UserTaskRelationship_njj relationship : userTaskRelationshipCache.values()) {
            if (relationship.getTaskPercent() != null) {
                if (minPercent != null && relationship.getTaskPercent() < minPercent)
                    continue;
                if (maxPercent != null && relationship.getTaskPercent() > maxPercent)
                    continue;
                result.add(relationship);
            }
        }
        return result;
    }

    /**
     * 根据任务进度排序获取用户任务关系数据
     */
    public static List<UserTaskRelationship_njj> getUserTaskRelationshipSortedByTaskPercent() {
        List<UserTaskRelationship_njj> result = new ArrayList<>(userTaskRelationshipCache.values());
        result.sort((a, b) -> {
            if (a.getTaskPercent() == null && b.getTaskPercent() == null)
                return 0;
            if (a.getTaskPercent() == null)
                return 1;
            if (b.getTaskPercent() == null)
                return -1;
            return Byte.compare(b.getTaskPercent(), a.getTaskPercent()); // 降序
        });
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserTaskRelationship", totalUserTaskRelationship);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userTaskRelationshipCache.clear();
        userTaskCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserTaskRelationship_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 更新缓存中的用户任务关系信息
     */
    public static void updateUserTaskRelationshipInCache(UserTaskRelationship_njj userTaskRelationship) {
        if (userTaskRelationship != null && userTaskRelationship.getUserTaskId() != null) {
            userTaskRelationshipCache.put(userTaskRelationship.getUserTaskId(), userTaskRelationship);
            lastUpdateTime = System.currentTimeMillis();
            System.out.println("缓存中的用户任务关系信息已更新，userTaskId: " + userTaskRelationship.getUserTaskId());
        }
    }
}
