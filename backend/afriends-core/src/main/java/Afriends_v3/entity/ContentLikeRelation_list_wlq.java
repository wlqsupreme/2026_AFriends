package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内容点赞关系信息列表
 * 对应实体: ContentLikeRelation_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ContentLikeRelation_list_wlq extends EntityList<ContentLikeRelation_wlq> {

    // 内存存储
    private static final Map<Long, ContentLikeRelation_wlq> contentLikeRelationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalContentLikeRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ContentLikeRelation_wlq> allContentLikeRelation) {
        try {
            System.out.println("开始直接加载内容点赞关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ContentLikeRelation_wlq relation : allContentLikeRelation) {
                System.out.println("处理点赞关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentLikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentLikeRelation = contentLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("内容点赞关系数据直接加载完成！总数: " + totalContentLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载内容点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有内容点赞关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ContentLikeRelation_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载内容点赞关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有内容点赞关系数据
            System.out.println("正在执行数据库查询...");
            List<ContentLikeRelation_wlq> allContentLikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allContentLikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (ContentLikeRelation_wlq relation : allContentLikeRelation) {
                System.out.println("处理点赞关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentLikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentLikeRelation = contentLikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("内容点赞关系数据加载完成！总数: " + totalContentLikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载内容点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有内容点赞关系数据（从内存）
     */
    public static List<ContentLikeRelation_wlq> getAllContentLikeRelation() {
        return new ArrayList<>(contentLikeRelationCache.values());
    }

    /**
     * 根据关系ID获取内容点赞关系数据
     */
    public static ContentLikeRelation_wlq getContentLikeRelationById(Long id) {
        return contentLikeRelationCache.get(id);
    }

    /**
     * 根据用户ID搜索内容点赞关系数据
     */
    public static List<ContentLikeRelation_wlq> searchContentLikeRelationByUserId(Long userId) {
        List<ContentLikeRelation_wlq> result = new ArrayList<>();
        for (ContentLikeRelation_wlq relation : contentLikeRelationCache.values()) {
            if (relation.getUserId() != null && relation.getUserId().equals(userId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据内容ID搜索内容点赞关系数据
     */
    public static List<ContentLikeRelation_wlq> searchContentLikeRelationByContentId(Long contentId) {
        List<ContentLikeRelation_wlq> result = new ArrayList<>();
        for (ContentLikeRelation_wlq relation : contentLikeRelationCache.values()) {
            if (relation.getContentId() != null && relation.getContentId().equals(contentId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据内容类型搜索内容点赞关系数据
     */
    public static List<ContentLikeRelation_wlq> searchContentLikeRelationByContentType(Byte contentType) {
        List<ContentLikeRelation_wlq> result = new ArrayList<>();
        for (ContentLikeRelation_wlq relation : contentLikeRelationCache.values()) {
            if (relation.getContentType() != null && relation.getContentType().equals(contentType)) {
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
        stats.put("totalContentLikeRelation", totalContentLikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        contentLikeRelationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ContentLikeRelation_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

