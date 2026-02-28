package Afriends_v3.entity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评论点踩关系信息列表
 * 对应实体: CommentDislikeRelation_zjx
 * 功能：从数据库读取点踩关系数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CommentDislikeRelation_list_zjx extends EntityList<CommentDislikeRelation_zjx> {

    // 内存存储：使用ConcurrentHashMap保证线程安全
    private static final Map<Long, CommentDislikeRelation_zjx> commentDislikeRelationCache = new ConcurrentHashMap<>();

    // 缓存状态标识
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCommentDislikeRelation = 0;

    /**
     * 直接加载数据到内存（使用外部查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CommentDislikeRelation_zjx> allCommentDislikeRelation) {
        try {
            System.out.println("开始直接加载评论点踩关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CommentDislikeRelation_zjx relation : allCommentDislikeRelation) {
                System.out.println("处理点踩关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 评论ID=" + relation.getCommentId());
                commentDislikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalCommentDislikeRelation = commentDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("评论点踩关系数据直接加载完成！总数: " + totalCommentDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载评论点踩关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有评论点踩关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CommentDislikeRelation_zjx> mapper) {
        try {
            System.out.println("开始从数据库加载评论点踩关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有评论点踩关系数据
            System.out.println("正在执行数据库查询...");
            List<CommentDislikeRelation_zjx> allCommentDislikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCommentDislikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (CommentDislikeRelation_zjx relation : allCommentDislikeRelation) {
                System.out.println("处理点踩关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 评论ID=" + relation.getCommentId());
                commentDislikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalCommentDislikeRelation = commentDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("评论点踩关系数据加载完成！总数: " + totalCommentDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载评论点踩关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有评论点踩关系数据（从内存）
     */
    public static List<CommentDislikeRelation_zjx> getAllCommentDislikeRelation() {
        return new ArrayList<>(commentDislikeRelationCache.values());
    }

    /**
     * 根据关系ID获取评论点踩关系数据
     */
    public static CommentDislikeRelation_zjx getCommentDislikeRelationById(Long id) {
        return commentDislikeRelationCache.get(id);
    }

    /**
     * 根据用户ID搜索评论点踩关系数据
     */
    public static List<CommentDislikeRelation_zjx> searchCommentDislikeRelationByUserId(Long userId) {
        List<CommentDislikeRelation_zjx> result = new ArrayList<>();
        for (CommentDislikeRelation_zjx relation : commentDislikeRelationCache.values()) {
            if (relation.getUserId() != null && relation.getUserId().equals(userId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据评论ID搜索评论点踩关系数据
     */
    public static List<CommentDislikeRelation_zjx> searchCommentDislikeRelationByCommentId(Long commentId) {
        List<CommentDislikeRelation_zjx> result = new ArrayList<>();
        for (CommentDislikeRelation_zjx relation : commentDislikeRelationCache.values()) {
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
        stats.put("totalCommentDislikeRelation", totalCommentDislikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        commentDislikeRelationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CommentDislikeRelation_zjx> mapper) {
        loadFromDatabase(mapper);
    }
}