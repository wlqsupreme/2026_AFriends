package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评论点赞关系信息列表
 * 对应实体: CommentLikeRelation_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CommentLikeRelation_list_wlq extends EntityList<CommentLikeRelation_wlq> {

    // 内存存储
    private static final Map<Long, CommentLikeRelation_wlq> commentLikeRelationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCommentLikeRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CommentLikeRelation_wlq> allCommentLikeRelation) {
        try {
            System.out.println("开始直接加载评论点赞关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CommentLikeRelation_wlq relation : allCommentLikeRelation) {
                System.out.println("处理点赞关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 评论ID=" + relation.getCommentId());
                commentLikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalCommentLikeRelation = commentLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("评论点赞关系数据直接加载完成！总数: " + totalCommentLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载评论点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有评论点赞关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CommentLikeRelation_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载评论点赞关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有评论点赞关系数据
            System.out.println("正在执行数据库查询...");
            List<CommentLikeRelation_wlq> allCommentLikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCommentLikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (CommentLikeRelation_wlq relation : allCommentLikeRelation) {
                System.out.println("处理点赞关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 评论ID=" + relation.getCommentId());
                commentLikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalCommentLikeRelation = commentLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("评论点赞关系数据加载完成！总数: " + totalCommentLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载评论点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有评论点赞关系数据（从内存）
     */
    public static List<CommentLikeRelation_wlq> getAllCommentLikeRelation() {
        return new ArrayList<>(commentLikeRelationCache.values());
    }

    /**
     * 根据关系ID获取评论点赞关系数据
     */
    public static CommentLikeRelation_wlq getCommentLikeRelationById(Long id) {
        return commentLikeRelationCache.get(id);
    }

    /**
     * 根据用户ID搜索评论点赞关系数据
     */
    public static List<CommentLikeRelation_wlq> searchCommentLikeRelationByUserId(Long userId) {
        List<CommentLikeRelation_wlq> result = new ArrayList<>();
        for (CommentLikeRelation_wlq relation : commentLikeRelationCache.values()) {
            if (relation.getUserId() != null && relation.getUserId().equals(userId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据评论ID搜索评论点赞关系数据
     */
    public static List<CommentLikeRelation_wlq> searchCommentLikeRelationByCommentId(Long commentId) {
        List<CommentLikeRelation_wlq> result = new ArrayList<>();
        for (CommentLikeRelation_wlq relation : commentLikeRelationCache.values()) {
            if (relation.getCommentId() != null && relation.getCommentId().equals(commentId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCommentLikeRelation", totalCommentLikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        commentLikeRelationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CommentLikeRelation_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

