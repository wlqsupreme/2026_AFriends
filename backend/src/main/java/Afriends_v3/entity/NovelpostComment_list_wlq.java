package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小说帖子评论信息列表
 * 对应实体: NovelpostComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class NovelpostComment_list_wlq extends EntityList<NovelpostComment_wlq> {

    // 内存存储
    private static final Map<Long, NovelpostComment_wlq> novelpostCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalNovelpostComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<NovelpostComment_wlq> allNovelpostComment) {
        try {
            System.out.println("开始直接加载小说帖子评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (NovelpostComment_wlq comment : allNovelpostComment) {
                System.out.println("处理评论数据: ID=" + comment.getNovelpostCommentId() + ", 小说ID=" + comment.getNovelId());
                novelpostCommentCache.put(comment.getNovelpostCommentId(), comment);
            }

            // 更新统计信息
            totalNovelpostComment = novelpostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子评论数据直接加载完成！总数: " + totalNovelpostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载小说帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有小说帖子评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<NovelpostComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载小说帖子评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有小说帖子评论数据
            System.out.println("正在执行数据库查询...");
            List<NovelpostComment_wlq> allNovelpostComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allNovelpostComment.size() + " 条记录");

            // 存储到内存缓存
            for (NovelpostComment_wlq comment : allNovelpostComment) {
                System.out.println("处理评论数据: ID=" + comment.getNovelpostCommentId() + ", 小说ID=" + comment.getNovelId());
                novelpostCommentCache.put(comment.getNovelpostCommentId(), comment);
            }

            // 更新统计信息
            totalNovelpostComment = novelpostCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子评论数据加载完成！总数: " + totalNovelpostComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载小说帖子评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有小说帖子评论数据（从内存）
     */
    public static List<NovelpostComment_wlq> getAllNovelpostComment() {
        return new ArrayList<>(novelpostCommentCache.values());
    }

    /**
     * 根据评论ID获取小说帖子评论数据
     */
    public static NovelpostComment_wlq getNovelpostCommentById(Long novelpostCommentId) {
        return novelpostCommentCache.get(novelpostCommentId);
    }

    /**
     * 根据小说ID搜索小说帖子评论数据
     */
    public static List<NovelpostComment_wlq> searchNovelpostCommentByNovelId(Long novelId) {
        List<NovelpostComment_wlq> result = new ArrayList<>();
        for (NovelpostComment_wlq comment : novelpostCommentCache.values()) {
            if (comment.getNovelId() != null && comment.getNovelId().equals(novelId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据用户ID搜索小说帖子评论数据
     */
    public static List<NovelpostComment_wlq> searchNovelpostCommentByUserId(Long userId) {
        List<NovelpostComment_wlq> result = new ArrayList<>();
        for (NovelpostComment_wlq comment : novelpostCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索小说帖子评论数据
     */
    public static List<NovelpostComment_wlq> searchNovelpostCommentByParentCommentId(Long parentCommentId) {
        List<NovelpostComment_wlq> result = new ArrayList<>();
        for (NovelpostComment_wlq comment : novelpostCommentCache.values()) {
            if (comment.getParentCommentId() != null && comment.getParentCommentId().equals(parentCommentId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据评论内容搜索小说帖子评论数据
     */
    public static List<NovelpostComment_wlq> searchNovelpostCommentByCommentText(String commentText) {
        List<NovelpostComment_wlq> result = new ArrayList<>();
        for (NovelpostComment_wlq comment : novelpostCommentCache.values()) {
            if (comment.getCommentText() != null && comment.getCommentText().contains(commentText)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索小说帖子评论数据
     */
    public static List<NovelpostComment_wlq> searchNovelpostCommentByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<NovelpostComment_wlq> result = new ArrayList<>();
        for (NovelpostComment_wlq comment : novelpostCommentCache.values()) {
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
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNovelpostComment", totalNovelpostComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        novelpostCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<NovelpostComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加评论到缓存
     */
    public static void addToCache(NovelpostComment_wlq comment) {
        if (comment != null && comment.getNovelpostCommentId() != null) {
            // 添加到主缓存
            novelpostCommentCache.put(comment.getNovelpostCommentId(), comment);
            
            // 更新统计信息
            totalNovelpostComment = novelpostCommentCache.size();
            
            // 更新缓存状态
            lastUpdateTime = System.currentTimeMillis();
            
            System.out.println("小说帖子评论已添加到缓存: ID=" + comment.getNovelpostCommentId() + 
                    ", 小说ID=" + comment.getNovelId() + ", 用户ID=" + comment.getUserId());
        }
    }
}
