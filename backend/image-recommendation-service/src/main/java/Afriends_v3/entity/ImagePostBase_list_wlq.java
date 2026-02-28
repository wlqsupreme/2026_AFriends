package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户图片内容基础信息列表
 * 对应实体: ImagePostBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ImagePostBase_list_wlq extends EntityList<ImagePostBase_wlq> {

    // 内存存储
    private static final Map<Long, ImagePostBase_wlq> imagePostBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalImagePostBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ImagePostBase_wlq> allImagePostBase) {
        try {
            System.out.println("开始直接加载用户图片内容基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ImagePostBase_wlq post : allImagePostBase) {
                System.out.println("处理图片帖子数据: ID=" + post.getPostId() + ", 用户ID=" + post.getUserId());
                imagePostBaseCache.put(post.getPostId(), post);
            }

            // 更新统计信息
            totalImagePostBase = imagePostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户图片内容基础数据直接加载完成！总数: " + totalImagePostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户图片内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户图片内容基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ImagePostBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户图片内容基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户图片内容基础数据
            System.out.println("正在执行数据库查询...");
            List<ImagePostBase_wlq> allImagePostBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allImagePostBase.size() + " 条记录");

            // 存储到内存缓存
            for (ImagePostBase_wlq post : allImagePostBase) {
                System.out.println("处理图片帖子数据: ID=" + post.getPostId() + ", 用户ID=" + post.getUserId());
                imagePostBaseCache.put(post.getPostId(), post);
            }

            // 更新统计信息
            totalImagePostBase = imagePostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户图片内容基础数据加载完成！总数: " + totalImagePostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户图片内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户图片内容基础数据（从内存）
     */
    public static List<ImagePostBase_wlq> getAllImagePostBase() {
        return new ArrayList<>(imagePostBaseCache.values());
    }

    /**
     * 根据帖子ID获取用户图片内容基础数据
     */
    public static ImagePostBase_wlq getImagePostBaseById(Long postId) {
        return imagePostBaseCache.get(postId);
    }

    /**
     * 根据用户ID搜索用户图片内容基础数据
     */
    public static List<ImagePostBase_wlq> searchImagePostBaseByUserId(Long userId) {
        List<ImagePostBase_wlq> result = new ArrayList<>();
        for (ImagePostBase_wlq post : imagePostBaseCache.values()) {
            if (post.getUserId() != null && post.getUserId().equals(userId)) {
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据内容文本搜索用户图片内容基础数据
     */
    public static List<ImagePostBase_wlq> searchImagePostBaseByContentText(String contentText) {
        List<ImagePostBase_wlq> result = new ArrayList<>();
        for (ImagePostBase_wlq post : imagePostBaseCache.values()) {
            if (post.getContentText() != null && post.getContentText().contains(contentText)) {
                result.add(post);
            }
        }
        return result;
    }

    /**
     * 根据热度值范围搜索用户图片内容基础数据
     */
    public static List<ImagePostBase_wlq> searchImagePostBaseByHeatScoreRange(Double minHeatScore, Double maxHeatScore) {
        List<ImagePostBase_wlq> result = new ArrayList<>();
        for (ImagePostBase_wlq post : imagePostBaseCache.values()) {
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
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalImagePostBase", totalImagePostBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        imagePostBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ImagePostBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
