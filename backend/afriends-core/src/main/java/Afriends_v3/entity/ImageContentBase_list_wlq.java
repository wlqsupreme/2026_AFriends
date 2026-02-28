package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片内容基础信息列表
 * 对应实体: ImageContentBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ImageContentBase_list_wlq extends EntityList<ImageContentBase_wlq> {

    // 内存存储
    private static final Map<Long, ImageContentBase_wlq> imageContentBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalImageContentBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ImageContentBase_wlq> allImageContentBase) {
        try {
            System.out.println("开始直接加载图片内容基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ImageContentBase_wlq imageContent : allImageContentBase) {
                System.out.println("处理图片内容数据: ID=" + imageContent.getImageContentId() + ", 用户ID=" + imageContent.getUserId());
                imageContentBaseCache.put(imageContent.getImageContentId(), imageContent);
            }

            // 更新统计信息
            totalImageContentBase = imageContentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片内容基础数据直接加载完成！总数: " + totalImageContentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载图片内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有图片内容基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ImageContentBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载图片内容基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有图片内容基础数据
            System.out.println("正在执行数据库查询...");
            List<ImageContentBase_wlq> allImageContentBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allImageContentBase.size() + " 条记录");

            // 存储到内存缓存
            for (ImageContentBase_wlq imageContent : allImageContentBase) {
                System.out.println("处理图片内容数据: ID=" + imageContent.getImageContentId() + ", 用户ID=" + imageContent.getUserId());
                imageContentBaseCache.put(imageContent.getImageContentId(), imageContent);
            }

            // 更新统计信息
            totalImageContentBase = imageContentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("图片内容基础数据加载完成！总数: " + totalImageContentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载图片内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有图片内容基础数据（从内存）
     */
    public static List<ImageContentBase_wlq> getAllImageContentBase() {
        return new ArrayList<>(imageContentBaseCache.values());
    }

    /**
     * 根据图片内容ID获取图片内容基础数据
     */
    public static ImageContentBase_wlq getImageContentBaseById(Long imageContentId) {
        return imageContentBaseCache.get(imageContentId);
    }

    /**
     * 根据用户ID搜索图片内容基础数据
     */
    public static List<ImageContentBase_wlq> searchImageContentBaseByUserId(Long userId) {
        List<ImageContentBase_wlq> result = new ArrayList<>();
        for (ImageContentBase_wlq imageContent : imageContentBaseCache.values()) {
            if (imageContent.getUserId() != null && imageContent.getUserId().equals(userId)) {
                result.add(imageContent);
            }
        }
        return result;
    }

    /**
     * 根据内容文本搜索图片内容基础数据
     */
    public static List<ImageContentBase_wlq> searchImageContentBaseByText(String text) {
        List<ImageContentBase_wlq> result = new ArrayList<>();
        for (ImageContentBase_wlq imageContent : imageContentBaseCache.values()) {
            if (imageContent.getContentText() != null && imageContent.getContentText().contains(text)) {
                result.add(imageContent);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalImageContentBase", totalImageContentBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        imageContentBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ImageContentBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
