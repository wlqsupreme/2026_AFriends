package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 我不喜欢内容特征向量信息列表
 * 对应实体: IDislikeFeatureVector_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IDislikeFeatureVector_list_wlq extends EntityList<IDislikeFeatureVector_wlq> {

    // 内存存储
    private static final Map<Long, IDislikeFeatureVector_wlq> iDislikeFeatureVectorCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIDislikeFeatureVector = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IDislikeFeatureVector_wlq> allIDislikeFeatureVector) {
        try {
            System.out.println("开始直接加载我不喜欢内容特征向量数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IDislikeFeatureVector_wlq vector : allIDislikeFeatureVector) {
                System.out.println("处理特征向量数据: ID=" + vector.getDislikeId());
                iDislikeFeatureVectorCache.put(vector.getDislikeId(), vector);
            }

            // 更新统计信息
            totalIDislikeFeatureVector = iDislikeFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容特征向量数据直接加载完成！总数: " + totalIDislikeFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载我不喜欢内容特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有我不喜欢内容特征向量数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IDislikeFeatureVector_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载我不喜欢内容特征向量数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有我不喜欢内容特征向量数据
            System.out.println("正在执行数据库查询...");
            List<IDislikeFeatureVector_wlq> allIDislikeFeatureVector = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIDislikeFeatureVector.size() + " 条记录");

            // 存储到内存缓存
            for (IDislikeFeatureVector_wlq vector : allIDislikeFeatureVector) {
                System.out.println("处理特征向量数据: ID=" + vector.getDislikeId());
                iDislikeFeatureVectorCache.put(vector.getDislikeId(), vector);
            }

            // 更新统计信息
            totalIDislikeFeatureVector = iDislikeFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("我不喜欢内容特征向量数据加载完成！总数: " + totalIDislikeFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载我不喜欢内容特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有我不喜欢内容特征向量数据（从内存）
     */
    public static List<IDislikeFeatureVector_wlq> getAllIDislikeFeatureVector() {
        return new ArrayList<>(iDislikeFeatureVectorCache.values());
    }

    /**
     * 根据不喜欢内容ID获取我不喜欢内容特征向量数据
     */
    public static IDislikeFeatureVector_wlq getIDislikeFeatureVectorById(Long dislikeId) {
        return iDislikeFeatureVectorCache.get(dislikeId);
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIDislikeFeatureVector", totalIDislikeFeatureVector);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        iDislikeFeatureVectorCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IDislikeFeatureVector_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

