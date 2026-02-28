package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户需求评论信息列表
 * 对应实体: INeedComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class INeedComment_list_wlq extends EntityList<INeedComment_wlq> {

    // 内存存储
    private static final Map<Long, INeedComment_wlq> iNeedCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalINeedComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<INeedComment_wlq> allINeedComment) {
        try {
            System.out.println("开始直接加载用户需求评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (INeedComment_wlq comment : allINeedComment) {
                System.out.println("处理评论数据: ID=" + comment.getIneedCommentId() + ", 用户ID=" + comment.getUserId());
                iNeedCommentCache.put(comment.getIneedCommentId(), comment);
            }

            // 更新统计信息
            totalINeedComment = iNeedCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户需求评论数据直接加载完成！总数: " + totalINeedComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户需求评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户需求评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<INeedComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户需求评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户需求评论数据
            System.out.println("正在执行数据库查询...");
            List<INeedComment_wlq> allINeedComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allINeedComment.size() + " 条记录");

            // 存储到内存缓存
            for (INeedComment_wlq comment : allINeedComment) {
                System.out.println("处理评论数据: ID=" + comment.getIneedCommentId() + ", 用户ID=" + comment.getUserId());
                iNeedCommentCache.put(comment.getIneedCommentId(), comment);
            }

            // 更新统计信息
            totalINeedComment = iNeedCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户需求评论数据加载完成！总数: " + totalINeedComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户需求评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户需求评论数据（从内存）
     */
    public static List<INeedComment_wlq> getAllINeedComment() {
        return new ArrayList<>(iNeedCommentCache.values());
    }

    /**
     * 根据评论ID获取用户需求评论数据
     */
    public static INeedComment_wlq getINeedCommentById(Long ineedCommentId) {
        return iNeedCommentCache.get(ineedCommentId);
    }

    /**
     * 根据需求ID搜索用户需求评论数据
     */
    public static List<INeedComment_wlq> searchINeedCommentByNeedId(Long needId) {
        List<INeedComment_wlq> result = new ArrayList<>();
        for (INeedComment_wlq comment : iNeedCommentCache.values()) {
            if (comment.getNeedId() != null && comment.getNeedId().equals(needId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据用户ID搜索用户需求评论数据
     */
    public static List<INeedComment_wlq> searchINeedCommentByUserId(Long userId) {
        List<INeedComment_wlq> result = new ArrayList<>();
        for (INeedComment_wlq comment : iNeedCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索用户需求评论数据
     */
    public static List<INeedComment_wlq> searchINeedCommentByParentCommentId(Long parentCommentId) {
        List<INeedComment_wlq> result = new ArrayList<>();
        for (INeedComment_wlq comment : iNeedCommentCache.values()) {
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
        stats.put("totalINeedComment", totalINeedComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iNeedCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<INeedComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
