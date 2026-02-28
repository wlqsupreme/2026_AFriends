package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户AI评论列表
 * 对应实体: UserAiComment_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserAiComment_list_njj extends EntityList<UserAiComment_njj> {

    // 内存存储
    private static final Map<Long, UserAiComment_njj> userAiCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserAiComment_njj>> userCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserAiComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserAiComment_njj> allUserAiComment) {
        try {
            System.out.println("开始直接加载用户AI评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserAiComment == null || allUserAiComment.isEmpty()) {
                System.out.println("用户AI评论数据为空，清空缓存并设置状态");
                totalUserAiComment = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户AI评论数据直接加载完成！总数: " + totalUserAiComment);
                return;
            }

            // 存储到内存缓存
            for (UserAiComment_njj comment : allUserAiComment) {
                if (comment != null && comment.getUserAiCommentId() != null) {
                    System.out.println(
                            "处理用户AI评论数据: ID=" + comment.getUserAiCommentId() + ", 用户ID=" + comment.getUserId());
                    userAiCommentCache.put(comment.getUserAiCommentId(), comment);

                    // 按用户ID分组存储
                    userCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);
                }
            }

            // 更新统计信息
            totalUserAiComment = userAiCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI评论数据直接加载完成！总数: " + totalUserAiComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户AI评论数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserAiComment = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户AI评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserAiComment_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户AI评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户AI评论数据
            System.out.println("正在执行数据库查询...");
            List<UserAiComment_njj> allUserAiComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserAiComment.size() + " 条记录");

            // 存储到内存缓存
            for (UserAiComment_njj comment : allUserAiComment) {
                if (comment != null && comment.getUserAiCommentId() != null) {
                    System.out.println(
                            "处理用户AI评论数据: ID=" + comment.getUserAiCommentId() + ", 用户ID=" + comment.getUserId());
                    userAiCommentCache.put(comment.getUserAiCommentId(), comment);

                    // 按用户ID分组存储
                    userCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);
                }
            }

            // 更新统计信息
            totalUserAiComment = userAiCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI评论数据加载完成！总数: " + totalUserAiComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户AI评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI评论数据（从内存）
     */
    public static List<UserAiComment_njj> getAllUserAiComment() {
        return new ArrayList<>(userAiCommentCache.values());
    }

    /**
     * 根据评论ID获取用户AI评论数据
     */
    public static UserAiComment_njj getUserAiCommentById(Long userAiCommentId) {
        return userAiCommentCache.get(userAiCommentId);
    }

    /**
     * 根据用户ID获取用户AI评论数据
     */
    public static List<UserAiComment_njj> getUserAiCommentByUserId(Long userId) {
        return userCommentCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据AI模型ID获取用户AI评论数据
     */
    public static List<UserAiComment_njj> getUserAiCommentByAimodelId(Long aimodelId) {
        List<UserAiComment_njj> result = new ArrayList<>();
        for (UserAiComment_njj comment : userAiCommentCache.values()) {
            if (comment.getAimodelId() != null && comment.getAimodelId().equals(aimodelId)) {
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
        stats.put("totalUserAiComment", totalUserAiComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userAiCommentCache.clear();
        userCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserAiComment_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加新的用户AI评论到缓存
     */
    public static void addToCache(UserAiComment_njj userAiComment) {
        if (userAiComment != null && userAiComment.getUserAiCommentId() != null) {
            // 添加到主缓存
            userAiCommentCache.put(userAiComment.getUserAiCommentId(), userAiComment);

            // 添加到用户分组缓存
            userCommentCache.computeIfAbsent(userAiComment.getUserId(), k -> new ArrayList<>()).add(userAiComment);

            // 更新统计信息
            totalUserAiComment = userAiCommentCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI评论已添加到缓存，commentId: " + userAiComment.getUserAiCommentId());
        }
    }
}