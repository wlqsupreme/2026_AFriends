package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片帖子特征向量信息列表
 * 对应实体: ImagePostFeatureVector_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ImagePostFeatureVector_list_wlq extends EntityList<ImagePostFeatureVector_wlq> {

    // 内存存储
    private static final Map<Long, ImagePostFeatureVector_wlq> imagePostFeatureVectorCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalImagePostFeatureVector = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ImagePostFeatureVector_wlq> allImagePostFeatureVector) {
        try {
            System.out.println("开始直接加载图片帖子特征向量数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ImagePostFeatureVector_wlq vector : allImagePostFeatureVector) {
                System.out.println("处理特征向量数据: ID=" + vector.getPostId());
                imagePostFeatureVectorCache.put(vector.getPostId(), vector);
            }

            // 更新统计信息
            totalImagePostFeatureVector = imagePostFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片帖子特征向量数据直接加载完成！总数: " + totalImagePostFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载图片帖子特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有图片帖子特征向量数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ImagePostFeatureVector_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载图片帖子特征向量数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有图片帖子特征向量数据
            System.out.println("正在执行数据库查询...");
            List<ImagePostFeatureVector_wlq> allImagePostFeatureVector = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allImagePostFeatureVector.size() + " 条记录");

            // 存储到内存缓存
            for (ImagePostFeatureVector_wlq vector : allImagePostFeatureVector) {
                System.out.println("处理特征向量数据: ID=" + vector.getPostId());
                imagePostFeatureVectorCache.put(vector.getPostId(), vector);
            }

            // 更新统计信息
            totalImagePostFeatureVector = imagePostFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片帖子特征向量数据加载完成！总数: " + totalImagePostFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载图片帖子特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有图片帖子特征向量数据（从内存）
     */
    public static List<ImagePostFeatureVector_wlq> getAllImagePostFeatureVector() {
        return new ArrayList<>(imagePostFeatureVectorCache.values());
    }

    /**
     * 根据帖子ID获取图片帖子特征向量数据
     */
    public static ImagePostFeatureVector_wlq getImagePostFeatureVectorById(Long postId) {
        return imagePostFeatureVectorCache.get(postId);
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalImagePostFeatureVector", totalImagePostFeatureVector);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        imagePostFeatureVectorCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ImagePostFeatureVector_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
