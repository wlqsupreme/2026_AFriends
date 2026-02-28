package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内容点踩关系信息列表
 * 对应实体: ContentDislikeRelation_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ContentDislikeRelation_list_wlq extends EntityList<ContentDislikeRelation_wlq> {

    // 内存存储
    private static final Map<Long, ContentDislikeRelation_wlq> contentDislikeRelationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalContentDislikeRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ContentDislikeRelation_wlq> allContentDislikeRelation) {
        try {
            System.out.println("开始直接加载内容点踩关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ContentDislikeRelation_wlq relation : allContentDislikeRelation) {
                System.out.println("处理点踩关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentDislikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentDislikeRelation = contentDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("内容点踩关系数据直接加载完成！总数: " + totalContentDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载内容点踩关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有内容点踩关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ContentDislikeRelation_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载内容点踩关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有内容点踩关系数据
            System.out.println("正在执行数据库查询...");
            List<ContentDislikeRelation_wlq> allContentDislikeRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allContentDislikeRelation.size() + " 条记录");

            // 存储到内存缓存
            for (ContentDislikeRelation_wlq relation : allContentDislikeRelation) {
                System.out.println("处理点踩关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentDislikeRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentDislikeRelation = contentDislikeRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("内容点踩关系数据加载完成！总数: " + totalContentDislikeRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载内容点踩关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有内容点踩关系数据（从内存）
     */
    public static List<ContentDislikeRelation_wlq> getAllContentDislikeRelation() {
        return new ArrayList<>(contentDislikeRelationCache.values());
    }

    /**
     * 根据关系ID获取内容点踩关系数据
     */
    public static ContentDislikeRelation_wlq getContentDislikeRelationById(Long id) {
        return contentDislikeRelationCache.get(id);
    }

    /**
     * 根据用户ID搜索内容点踩关系数据
     */
    public static List<ContentDislikeRelation_wlq> searchContentDislikeRelationByUserId(Long userId) {
        List<ContentDislikeRelation_wlq> result = new ArrayList<>();
        for (ContentDislikeRelation_wlq relation : contentDislikeRelationCache.values()) {
            if (relation.getUserId() != null && relation.getUserId().equals(userId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据内容ID搜索内容点踩关系数据
     */
    public static List<ContentDislikeRelation_wlq> searchContentDislikeRelationByContentId(Long contentId) {
        List<ContentDislikeRelation_wlq> result = new ArrayList<>();
        for (ContentDislikeRelation_wlq relation : contentDislikeRelationCache.values()) {
            if (relation.getContentId() != null && relation.getContentId().equals(contentId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据内容类型搜索内容点踩关系数据
     */
    public static List<ContentDislikeRelation_wlq> searchContentDislikeRelationByContentType(Byte contentType) {
        List<ContentDislikeRelation_wlq> result = new ArrayList<>();
        for (ContentDislikeRelation_wlq relation : contentDislikeRelationCache.values()) {
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
        stats.put("totalContentDislikeRelation", totalContentDislikeRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        contentDislikeRelationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ContentDislikeRelation_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

