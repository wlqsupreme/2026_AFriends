package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户不喜欢内容关系信息列表
 * 对应实体: ContentFeedbackRelation_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ContentFeedbackRelation_list_wlq extends EntityList<ContentFeedbackRelation_wlq> {

    // 内存存储
    private static final Map<Long, ContentFeedbackRelation_wlq> contentFeedbackRelationCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalContentFeedbackRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ContentFeedbackRelation_wlq> allContentFeedbackRelation) {
        try {
            System.out.println("开始直接加载用户不喜欢内容关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ContentFeedbackRelation_wlq relation : allContentFeedbackRelation) {
                System.out.println("处理反馈关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentFeedbackRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentFeedbackRelation = contentFeedbackRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户不喜欢内容关系数据直接加载完成！总数: " + totalContentFeedbackRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户不喜欢内容关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户不喜欢内容关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ContentFeedbackRelation_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户不喜欢内容关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户不喜欢内容关系数据
            System.out.println("正在执行数据库查询...");
            List<ContentFeedbackRelation_wlq> allContentFeedbackRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allContentFeedbackRelation.size() + " 条记录");

            // 存储到内存缓存
            for (ContentFeedbackRelation_wlq relation : allContentFeedbackRelation) {
                System.out.println("处理反馈关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId() + ", 内容ID=" + relation.getContentId());
                contentFeedbackRelationCache.put(relation.getId(), relation);
            }

            // 更新统计信息
            totalContentFeedbackRelation = contentFeedbackRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户不喜欢内容关系数据加载完成！总数: " + totalContentFeedbackRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户不喜欢内容关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户不喜欢内容关系数据（从内存）
     */
    public static List<ContentFeedbackRelation_wlq> getAllContentFeedbackRelation() {
        return new ArrayList<>(contentFeedbackRelationCache.values());
    }

    /**
     * 根据关系ID获取用户不喜欢内容关系数据
     */
    public static ContentFeedbackRelation_wlq getContentFeedbackRelationById(Long id) {
        return contentFeedbackRelationCache.get(id);
    }

    /**
     * 根据用户ID搜索用户不喜欢内容关系数据
     */
    public static List<ContentFeedbackRelation_wlq> searchContentFeedbackRelationByUserId(Long userId) {
        List<ContentFeedbackRelation_wlq> result = new ArrayList<>();
        for (ContentFeedbackRelation_wlq relation : contentFeedbackRelationCache.values()) {
            if (relation.getUserId() != null && relation.getUserId().equals(userId)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据内容ID搜索用户不喜欢内容关系数据
     */
    public static List<ContentFeedbackRelation_wlq> searchContentFeedbackRelationByContentId(Long contentId) {
        List<ContentFeedbackRelation_wlq> result = new ArrayList<>();
        for (ContentFeedbackRelation_wlq relation : contentFeedbackRelationCache.values()) {
            if (relation.getContentId() != null && relation.getContentId().equals(contentId)) {
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
        stats.put("totalContentFeedbackRelation", totalContentFeedbackRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        contentFeedbackRelationCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ContentFeedbackRelation_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

