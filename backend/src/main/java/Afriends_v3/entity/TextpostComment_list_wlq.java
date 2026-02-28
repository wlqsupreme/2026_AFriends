package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字帖子评论信息列表
 * 对应实体: TextpostComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class TextpostComment_list_wlq extends EntityList<TextpostComment_wlq> {

    // 内存存储
    private static final Map<Long, TextpostComment_wlq> textpostCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalTextpostComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<TextpostComment_wlq> allTextpostComment) {
        try {
            System.out.println("开始直接加载文字帖子评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (TextpostComment_wlq comment : allTextpostComment) {
                System.out.println("处理评论数据: ID=" + comment.getTextpostCommentId() + ", 用户ID=" + comment.getUserId());
                textpostCommentCache.put(comment.getTextpostCommentId(), comment);
            }

            // 更新统计信息
            totalTextpostComment = textpostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("文字帖子评论数据直接加载完成！总数: " + totalTextpostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载文字帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有文字帖子评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<TextpostComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载文字帖子评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有文字帖子评论数据
            System.out.println("正在执行数据库查询...");
            List<TextpostComment_wlq> allTextpostComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allTextpostComment.size() + " 条记录");

            // 存储到内存缓存
            for (TextpostComment_wlq comment : allTextpostComment) {
                System.out.println("处理评论数据: ID=" + comment.getTextpostCommentId() + ", 用户ID=" + comment.getUserId());
                textpostCommentCache.put(comment.getTextpostCommentId(), comment);
            }

            // 更新统计信息
            totalTextpostComment = textpostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("文字帖子评论数据加载完成！总数: " + totalTextpostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载文字帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有文字帖子评论数据（从内存）
     */
    public static List<TextpostComment_wlq> getAllTextpostComment() {
        return new ArrayList<>(textpostCommentCache.values());
    }

    /**
     * 根据评论ID获取文字帖子评论数据
     */
    public static TextpostComment_wlq getTextpostCommentById(Long textpostCommentId) {
        return textpostCommentCache.get(textpostCommentId);
    }

    /**
     * 根据用户ID搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByUserId(Long userId) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByParentCommentId(Long parentCommentId) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getParentCommentId() != null && comment.getParentCommentId().equals(parentCommentId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据评论内容搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByCommentText(String commentText) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getCommentText() != null && comment.getCommentText().contains(commentText)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据文字内容ID搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByTextContentId(Long textContentId) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getTextContentId() != null && comment.getTextContentId().equals(textContentId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getLikeCount() != null) {
                if (minLikeCount != null && comment.getLikeCount() < minLikeCount) {
                    continue;
                }
                if (maxLikeCount != null && comment.getLikeCount() > maxLikeCount) {
                    continue;
                }
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据是否可见搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByIsVisible(Byte isVisible) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getIsVisible() != null && comment.getIsVisible().equals(isVisible)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据状态搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByStatus(Byte status) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
            if (comment.getStatus() != null && comment.getStatus().equals(status)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据是否删除搜索文字帖子评论数据
     */
    public static List<TextpostComment_wlq> searchTextpostCommentByIsDeleted(Byte isDeleted) {
        List<TextpostComment_wlq> result = new ArrayList<>();
        for (TextpostComment_wlq comment : textpostCommentCache.values()) {
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
        stats.put("totalTextpostComment", totalTextpostComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        textpostCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<TextpostComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
