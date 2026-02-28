package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务评论列表
 * 对应实体: TaskComment_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class TaskComment_list_njj extends EntityList<TaskComment_njj> {

    // 内存存储
    private static final Map<Long, TaskComment_njj> taskCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<TaskComment_njj>> userCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalTaskComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<TaskComment_njj> allTaskComment) {
        try {
            System.out.println("开始直接加载任务评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (TaskComment_njj comment : allTaskComment) {
                System.out.println("处理任务评论数据: ID=" + comment.getTaskCommentId() + ", 用户ID=" + comment.getUserId()
                        + ", 星级=" + comment.getStars());
                taskCommentCache.put(comment.getTaskCommentId(), comment);

                // 按用户ID分组存储
                userCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);
            }

            // 更新统计信息
            totalTaskComment = taskCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("任务评论数据直接加载完成！总数: " + totalTaskComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载任务评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有任务评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<TaskComment_njj> mapper) {
        try {
            System.out.println("开始从数据库加载任务评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有任务评论数据
            System.out.println("正在执行数据库查询...");
            List<TaskComment_njj> allTaskComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allTaskComment.size() + " 条记录");

            // 存储到内存缓存
            for (TaskComment_njj comment : allTaskComment) {
                System.out.println("处理任务评论数据: ID=" + comment.getTaskCommentId() + ", 用户ID=" + comment.getUserId()
                        + ", 星级=" + comment.getStars());
                taskCommentCache.put(comment.getTaskCommentId(), comment);

                // 按用户ID分组存储
                userCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);
            }

            // 更新统计信息
            totalTaskComment = taskCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("任务评论数据加载完成！总数: " + totalTaskComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载任务评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有任务评论数据（从内存）
     */
    public static List<TaskComment_njj> getAllTaskComment() {
        return new ArrayList<>(taskCommentCache.values());
    }

    /**
     * 根据评论ID获取任务评论数据
     */
    public static TaskComment_njj getTaskCommentById(Long taskCommentId) {
        return taskCommentCache.get(taskCommentId);
    }

    /**
     * 根据用户ID获取任务评论数据
     */
    public static List<TaskComment_njj> getTaskCommentByUserId(Long userId) {
        return userCommentCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据星级获取任务评论数据
     */
    public static List<TaskComment_njj> getTaskCommentByStars(Byte stars) {
        List<TaskComment_njj> result = new ArrayList<>();
        for (TaskComment_njj comment : taskCommentCache.values()) {
            if (comment.getStars() != null && comment.getStars().equals(stars)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据可见性获取任务评论数据
     */
    public static List<TaskComment_njj> getTaskCommentByVisible(Byte isVisible) {
        List<TaskComment_njj> result = new ArrayList<>();
        for (TaskComment_njj comment : taskCommentCache.values()) {
            if (comment.getIsVisible() != null && comment.getIsVisible().equals(isVisible)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTaskComment", totalTaskComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        taskCommentCache.clear();
        userCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 添加任务评论到缓存
     */
    public static void addToCache(TaskComment_njj comment) {
        if (comment != null && comment.getTaskCommentId() != null) {
            taskCommentCache.put(comment.getTaskCommentId(), comment);

            // 按用户ID分组存储
            userCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);

            // 更新统计信息
            totalTaskComment = taskCommentCache.size();

            System.out.println("任务评论已添加到缓存: ID=" + comment.getTaskCommentId() +
                    ", 用户ID=" + comment.getUserId() + ", 星级=" + comment.getStars());
        }
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<TaskComment_njj> mapper) {
        loadFromDatabase(mapper);
    }
}