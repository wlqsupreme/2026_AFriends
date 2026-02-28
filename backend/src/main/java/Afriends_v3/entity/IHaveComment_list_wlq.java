package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我拥有物品评论信息列表
 * 对应实体: IHaveComment_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IHaveComment_list_wlq extends EntityList<IHaveComment_wlq> {

    // 内存存储
    private static final Map<Long, IHaveComment_wlq> iHaveCommentCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIHaveComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IHaveComment_wlq> allIHaveComment) {
        try {
            System.out.println("开始直接加载我拥有物品评论数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IHaveComment_wlq comment : allIHaveComment) {
                System.out.println("处理评论数据: ID=" + comment.getIhaveCommentId() + ", 用户ID=" + comment.getUserId());
                iHaveCommentCache.put(comment.getIhaveCommentId(), comment);
            }

            // 更新统计信息
            totalIHaveComment = iHaveCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我拥有物品评论数据直接加载完成！总数: " + totalIHaveComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我拥有物品评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我拥有物品评论数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IHaveComment_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我拥有物品评论数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我拥有物品评论数据
            System.out.println("正在执行数据库查询...");
            List<IHaveComment_wlq> allIHaveComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIHaveComment.size() + " 条记录");

            // 存储到内存缓存
            for (IHaveComment_wlq comment : allIHaveComment) {
                System.out.println("处理评论数据: ID=" + comment.getIhaveCommentId() + ", 用户ID=" + comment.getUserId());
                iHaveCommentCache.put(comment.getIhaveCommentId(), comment);
            }

            // 更新统计信息
            totalIHaveComment = iHaveCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我拥有物品评论数据加载完成！总数: " + totalIHaveComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我拥有物品评论数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我拥有物品评论数据（从内存）
     */
    public static List<IHaveComment_wlq> getAllIHaveComment() {
        return new ArrayList<>(iHaveCommentCache.values());
    }

    /**
     * 根据评论ID获取我拥有物品评论数据
     */
    public static IHaveComment_wlq getIHaveCommentById(Long ihaveCommentId) {
        return iHaveCommentCache.get(ihaveCommentId);
    }

    /**
     * 根据拥有物品ID搜索我拥有物品评论数据
     */
    public static List<IHaveComment_wlq> searchIHaveCommentByHaveId(Long haveId) {
        List<IHaveComment_wlq> result = new ArrayList<>();
        for (IHaveComment_wlq comment : iHaveCommentCache.values()) {
            if (comment.getHaveId() != null && comment.getHaveId().equals(haveId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据用户ID搜索我拥有物品评论数据
     */
    public static List<IHaveComment_wlq> searchIHaveCommentByUserId(Long userId) {
        List<IHaveComment_wlq> result = new ArrayList<>();
        for (IHaveComment_wlq comment : iHaveCommentCache.values()) {
            if (comment.getUserId() != null && comment.getUserId().equals(userId)) {
                result.add(comment);
            }
        }
        return result;
    }

    /**
     * 根据父评论ID搜索我拥有物品评论数据
     */
    public static List<IHaveComment_wlq> searchIHaveCommentByParentCommentId(Long parentCommentId) {
        List<IHaveComment_wlq> result = new ArrayList<>();
        for (IHaveComment_wlq comment : iHaveCommentCache.values()) {
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
        stats.put("totalIHaveComment", totalIHaveComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iHaveCommentCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IHaveComment_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
