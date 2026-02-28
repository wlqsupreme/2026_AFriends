package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我不喜欢内容评论信息列表
 * 对应实体: IDislikeComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IDislikeComment_list_wlq extends EntityList<IDislikeComment_wlq> {

    // 内存存储
    private static final Map<Long, IDislikeComment_wlq> iDislikeCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIDislikeComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IDislikeComment_wlq> allIDislikeComment) {
        try {
            System.out.println("开始直接加载我不喜欢内容评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IDislikeComment_wlq comment : allIDislikeComment) {
                System.out.println("处理评论数据: ID=" + comment.getIdislikeCommentId() + ", 用户ID=" + comment.getUserId());
                iDislikeCommentCache.put(comment.getIdislikeCommentId(), comment);
            }

            // 更新统计信息
            totalIDislikeComment = iDislikeCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容评论数据直接加载完成！总数: " + totalIDislikeComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我不喜欢内容评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我不喜欢内容评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IDislikeComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我不喜欢内容评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我不喜欢内容评论数据
            System.out.println("正在执行数据库查询...");
            List<IDislikeComment_wlq> allIDislikeComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIDislikeComment.size() + " 条记录");

            // 存储到内存缓存
            for (IDislikeComment_wlq comment : allIDislikeComment) {
                System.out.println("处理评论数据: ID=" + comment.getIdislikeCommentId() + ", 用户ID=" + comment.getUserId());
                iDislikeCommentCache.put(comment.getIdislikeCommentId(), comment);
            }

            // 更新统计信息
            totalIDislikeComment = iDislikeCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容评论数据加载完成！总数: " + totalIDislikeComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我不喜欢内容评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我不喜欢内容评论数据（从内存）
     */
    public static List<IDislikeComment_wlq> getAllIDislikeComment() {
        return new ArrayList<>(iDislikeCommentCache.values());
    }

    /**
     * 根据评论ID获取我不喜欢内容评论数据
     */
    public static IDislikeComment_wlq getIDislikeCommentById(Long idislikeCommentId) {
        return iDislikeCommentCache.get(idislikeCommentId);
    }

    /**
     * 根据不喜欢内容ID搜索我不喜欢内容评论数据
     */
    public static List<IDislikeComment_wlq> searchIDislikeCommentByDislikeId(Long dislikeId) {
        List<IDislikeComment_wlq> result = new ArrayList<>();
        for (IDislikeComment_wlq comment : iDislikeCommentCache.values()) {
            if (comment.getDislikeId() != null && comment.getDislikeId().equals(dislikeId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据用户ID搜索我不喜欢内容评论数据
     */
    public static List<IDislikeComment_wlq> searchIDislikeCommentByUserId(Long userId) {
        List<IDislikeComment_wlq> result = new ArrayList<>();
        for (IDislikeComment_wlq comment : iDislikeCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索我不喜欢内容评论数据
     */
    public static List<IDislikeComment_wlq> searchIDislikeCommentByParentCommentId(Long parentCommentId) {
        List<IDislikeComment_wlq> result = new ArrayList<>();
        for (IDislikeComment_wlq comment : iDislikeCommentCache.values()) {
            if (comment.getParentCommentId() != null && comment.getParentCommentId().equals(parentCommentId)) {
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
        stats.put("totalIDislikeComment", totalIDislikeComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iDislikeCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IDislikeComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

