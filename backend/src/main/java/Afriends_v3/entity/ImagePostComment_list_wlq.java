package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片帖子评论信息列表
 * 对应实体: ImagePostComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ImagePostComment_list_wlq extends EntityList<ImagePostComment_wlq> {

    // 内存存储
    private static final Map<Long, ImagePostComment_wlq> imagePostCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalImagePostComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ImagePostComment_wlq> allImagePostComment) {
        try {
            System.out.println("开始直接加载图片帖子评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ImagePostComment_wlq comment : allImagePostComment) {
                System.out.println("处理评论数据: ID=" + comment.getImagepostCommentId() + ", 用户ID=" + comment.getUserId());
                imagePostCommentCache.put(comment.getImagepostCommentId(), comment);
            }

            // 更新统计信息
            totalImagePostComment = imagePostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片帖子评论数据直接加载完成！总数: " + totalImagePostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载图片帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有图片帖子评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ImagePostComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载图片帖子评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有图片帖子评论数据
            System.out.println("正在执行数据库查询...");
            List<ImagePostComment_wlq> allImagePostComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allImagePostComment.size() + " 条记录");

            // 存储到内存缓存
            for (ImagePostComment_wlq comment : allImagePostComment) {
                System.out.println("处理评论数据: ID=" + comment.getImagepostCommentId() + ", 用户ID=" + comment.getUserId());
                imagePostCommentCache.put(comment.getImagepostCommentId(), comment);
            }

            // 更新统计信息
            totalImagePostComment = imagePostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片帖子评论数据加载完成！总数: " + totalImagePostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载图片帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有图片帖子评论数据（从内存）
     */
    public static List<ImagePostComment_wlq> getAllImagePostComment() {
        return new ArrayList<>(imagePostCommentCache.values());
    }

    /**
     * 根据评论ID获取图片帖子评论数据
     */
    public static ImagePostComment_wlq getImagePostCommentById(Long imagepostCommentId) {
        return imagePostCommentCache.get(imagepostCommentId);
    }

    /**
     * 根据帖子ID搜索图片帖子评论数据
     */
    public static List<ImagePostComment_wlq> searchImagePostCommentByPostId(Long postId) {
        List<ImagePostComment_wlq> result = new ArrayList<>();
        for (ImagePostComment_wlq comment : imagePostCommentCache.values()) {
            if (comment.getPostId() != null && comment.getPostId().equals(postId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据用户ID搜索图片帖子评论数据
     */
    public static List<ImagePostComment_wlq> searchImagePostCommentByUserId(Long userId) {
        List<ImagePostComment_wlq> result = new ArrayList<>();
        for (ImagePostComment_wlq comment : imagePostCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索图片帖子评论数据
     */
    public static List<ImagePostComment_wlq> searchImagePostCommentByParentCommentId(Long parentCommentId) {
        List<ImagePostComment_wlq> result = new ArrayList<>();
        for (ImagePostComment_wlq comment : imagePostCommentCache.values()) {
            if (comment.getParentCommentId() != null && comment.getParentCommentId().equals(parentCommentId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据目标图片ID搜索图片帖子评论数据
     */
    public static List<ImagePostComment_wlq> searchImagePostCommentByTargetImageId(Long targetImageId) {
        List<ImagePostComment_wlq> result = new ArrayList<>();
        for (ImagePostComment_wlq comment : imagePostCommentCache.values()) {
            if (comment.getTargetImageId() != null && comment.getTargetImageId().equals(targetImageId)) {
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
        stats.put("totalImagePostComment", totalImagePostComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        imagePostCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ImagePostComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
