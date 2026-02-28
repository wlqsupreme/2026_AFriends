package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础文本评论信息列表
 * 对应实体: UserBaseTextComment_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseTextComment_list_njj extends EntityList<UserBaseTextComment_njj> {

    // 内存存储
    private static final Map<Long, UserBaseTextComment_njj> userBaseTextCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseTextComment_njj>> userTextCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseTextComment_njj>> commentUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseTextComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseTextComment_njj> allUserBaseTextComment) {
        try {
            System.out.println("开始直接加载用户基础文本评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseTextComment == null || allUserBaseTextComment.isEmpty()) {
                System.out.println("用户基础文本评论数据为空，清空缓存并设置状态");
                totalUserBaseTextComment = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户基础文本评论数据直接加载完成！总数: " + totalUserBaseTextComment);
                return;
            }

            // 存储到内存缓存
            for (UserBaseTextComment_njj comment : allUserBaseTextComment) {
                if (comment != null && comment.getRelId() != null) {
                    System.out.println("处理用户基础文本评论数据: ID=" + comment.getRelId() + ", 用户ID=" + comment.getUserId());
                    userBaseTextCommentCache.put(comment.getRelId(), comment);

                    // 按用户ID分组存储
                    userTextCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);

                    // 按评论ID分组存储
                    commentUserCache.computeIfAbsent(comment.getCommentId(), k -> new ArrayList<>()).add(comment);
                }
            }

            // 更新统计信息
            totalUserBaseTextComment = userBaseTextCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础文本评论数据直接加载完成！总数: " + totalUserBaseTextComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础文本评论数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseTextComment = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户基础文本评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseTextComment_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础文本评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础文本评论数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseTextComment_njj> allUserBaseTextComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseTextComment.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseTextComment_njj comment : allUserBaseTextComment) {
                if (comment != null && comment.getRelId() != null) {
                    System.out.println("处理用户基础文本评论数据: ID=" + comment.getRelId() + ", 用户ID=" + comment.getUserId());
                    userBaseTextCommentCache.put(comment.getRelId(), comment);

                    // 按用户ID分组存储
                    userTextCommentCache.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>()).add(comment);

                    // 按评论ID分组存储
                    commentUserCache.computeIfAbsent(comment.getCommentId(), k -> new ArrayList<>()).add(comment);
                }
            }

            // 更新统计信息
            totalUserBaseTextComment = userBaseTextCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础文本评论数据加载完成！总数: " + totalUserBaseTextComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础文本评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户基础文本评论数据（从内存）
     */
    public static List<UserBaseTextComment_njj> getAllUserBaseTextComment() {
        return new ArrayList<>(userBaseTextCommentCache.values());
    }

    /**
     * 根据关系ID获取用户基础文本评论数据
     */
    public static UserBaseTextComment_njj getUserBaseTextCommentById(Long relId) {
        return userBaseTextCommentCache.get(relId);
    }

    /**
     * 根据用户ID获取用户基础文本评论数据
     */
    public static List<UserBaseTextComment_njj> getUserBaseTextCommentByUserId(Long userId) {
        return userTextCommentCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据评论ID获取用户基础文本评论数据
     */
    public static List<UserBaseTextComment_njj> getUserBaseTextCommentByCommentId(Long commentId) {
        return commentUserCache.getOrDefault(commentId, new ArrayList<>());
    }

    /**
     * 根据删除状态获取用户基础文本评论数据
     */
    public static List<UserBaseTextComment_njj> getUserBaseTextCommentByIsDeleted(Integer isDeleted) {
        List<UserBaseTextComment_njj> result = new ArrayList<>();
        for (UserBaseTextComment_njj comment : userBaseTextCommentCache.values()) {
            if (comment.getIsDeleted() != null && comment.getIsDeleted().equals(isDeleted)) {
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
        stats.put("totalUserBaseTextComment", totalUserBaseTextComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseTextCommentCache.clear();
        userTextCommentCache.clear();
        commentUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseTextComment_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
