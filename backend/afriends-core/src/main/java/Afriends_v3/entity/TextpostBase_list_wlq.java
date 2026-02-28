package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文字帖子基础信息列表
 * 对应实体: TextpostBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class TextpostBase_list_wlq extends EntityList<TextpostBase_wlq> {

    // 内存存储
    private static final Map<Long, TextpostBase_wlq> textpostBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalTextpostBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<TextpostBase_wlq> allTextpostBase) {
        try {
            System.out.println("开始直接加载文字帖子基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (TextpostBase_wlq post : allTextpostBase) {
                System.out.println("处理文字帖子数据: ID=" + post.getPostId() + ", 用户ID=" + post.getUserId());
                textpostBaseCache.put(post.getPostId(), post);
            }

            // 更新统计信息
            totalTextpostBase = textpostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("文字帖子基础数据直接加载完成！总数: " + totalTextpostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载文字帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有文字帖子基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<TextpostBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载文字帖子基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有文字帖子基础数据
            System.out.println("正在执行数据库查询...");
            List<TextpostBase_wlq> allTextpostBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allTextpostBase.size() + " 条记录");

            // 存储到内存缓存
            for (TextpostBase_wlq post : allTextpostBase) {
                System.out.println("处理文字帖子数据: ID=" + post.getPostId() + ", 用户ID=" + post.getUserId());
                textpostBaseCache.put(post.getPostId(), post);
            }

            // 更新统计信息
            totalTextpostBase = textpostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("文字帖子基础数据加载完成！总数: " + totalTextpostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载文字帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有文字帖子基础数据（从内存）
     */
    public static List<TextpostBase_wlq> getAllTextpostBase() {
        return new ArrayList<>(textpostBaseCache.values());
    }

    /**
     * 根据帖子ID获取文字帖子基础数据
     */
    public static TextpostBase_wlq getTextpostBaseById(Long postId) {
        return textpostBaseCache.get(postId);
    }

    /**
     * 根据用户ID搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByUserId(Long userId) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getUserId() != null && post.getUserId().equals(userId)) {
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据内容文本搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByContentText(String contentText) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getContentText() != null && post.getContentText().contains(contentText)) {
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getLikeCount() != null) {
                if (minLikeCount != null && post.getLikeCount() < minLikeCount) {
                    continue;
                }
                if (maxLikeCount != null && post.getLikeCount() > maxLikeCount) {
                    continue;
                }
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据评论数范围搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByCommentCountRange(Integer minCommentCount, Integer maxCommentCount) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getCommentCount() != null) {
                if (minCommentCount != null && post.getCommentCount() < minCommentCount) {
                    continue;
                }
                if (maxCommentCount != null && post.getCommentCount() > maxCommentCount) {
                    continue;
                }
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据收藏数范围搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByCollectCountRange(Integer minCollectCount, Integer maxCollectCount) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getCollectCount() != null) {
                if (minCollectCount != null && post.getCollectCount() < minCollectCount) {
                    continue;
                }
                if (maxCollectCount != null && post.getCollectCount() > maxCollectCount) {
                    continue;
                }
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据浏览量范围搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByViewCountRange(Integer minViewCount, Integer maxViewCount) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getViewCount() != null) {
                if (minViewCount != null && post.getViewCount() < minViewCount) {
                    continue;
                }
                if (maxViewCount != null && post.getViewCount() > maxViewCount) {
                    continue;
                }
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getHeatScore() != null) {
                if (minHeatScore != null && post.getHeatScore() < minHeatScore) {
                    continue;
                }
                if (maxHeatScore != null && post.getHeatScore() > maxHeatScore) {
                    continue;
                }
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据是否可见搜索文字帖子基础数据
     */
    public static List<TextpostBase_wlq> searchTextpostBaseByIsView(Byte isView) {
        List<TextpostBase_wlq> result = new ArrayList<>();
        for (TextpostBase_wlq post : textpostBaseCache.values()) {
            if (post.getIsView() != null && post.getIsView().equals(isView)) {
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTextpostBase", totalTextpostBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        textpostBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<TextpostBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
