package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小说帖子特征向量信息列表
 * 对应实体: NovelpostFeatureVector_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class NovelpostFeatureVector_list_wlq extends EntityList<NovelpostFeatureVector_wlq> {

    // 内存存储
    private static final Map<Long, NovelpostFeatureVector_wlq> novelpostFeatureVectorCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalNovelpostFeatureVector = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<NovelpostFeatureVector_wlq> allNovelpostFeatureVector) {
        try {
            System.out.println("开始直接加载小说帖子特征向量数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (NovelpostFeatureVector_wlq vector : allNovelpostFeatureVector) {
                System.out.println("处理特征向量数据: 小说ID=" + vector.getNovelId());
                novelpostFeatureVectorCache.put(vector.getNovelId(), vector);
            }

            // 更新统计信息
            totalNovelpostFeatureVector = novelpostFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子特征向量数据直接加载完成！总数: " + totalNovelpostFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载小说帖子特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有小说帖子特征向量数据到内存
     */
    public static void loadFromDatabase(BaseMapper<NovelpostFeatureVector_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载小说帖子特征向量数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有小说帖子特征向量数据
            System.out.println("正在执行数据库查询...");
            List<NovelpostFeatureVector_wlq> allNovelpostFeatureVector = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allNovelpostFeatureVector.size() + " 条记录");

            // 存储到内存缓存
            for (NovelpostFeatureVector_wlq vector : allNovelpostFeatureVector) {
                System.out.println("处理特征向量数据: 小说ID=" + vector.getNovelId());
                novelpostFeatureVectorCache.put(vector.getNovelId(), vector);
            }

            // 更新统计信息
            totalNovelpostFeatureVector = novelpostFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子特征向量数据加载完成！总数: " + totalNovelpostFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载小说帖子特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有小说帖子特征向量数据（从内存）
     */
    public static List<NovelpostFeatureVector_wlq> getAllNovelpostFeatureVector() {
        return new ArrayList<>(novelpostFeatureVectorCache.values());
    }

    /**
     * 根据小说ID获取小说帖子特征向量数据
     */
    public static NovelpostFeatureVector_wlq getNovelpostFeatureVectorById(Long novelId) {
        return novelpostFeatureVectorCache.get(novelId);
    }

    /**
     * 根据小说ID范围搜索小说帖子特征向量数据
     */
    public static List<NovelpostFeatureVector_wlq> searchNovelpostFeatureVectorByNovelIdRange(Long minNovelId, Long maxNovelId) {
        List<NovelpostFeatureVector_wlq> result = new ArrayList<>();
        for (NovelpostFeatureVector_wlq vector : novelpostFeatureVectorCache.values()) {
            if (vector.getNovelId() != null) {
                if (minNovelId != null && vector.getNovelId() < minNovelId) {
                    continue;
                }
                if (maxNovelId != null && vector.getNovelId() > maxNovelId) {
                    continue;
                }
                result.add(vector);
            }
        }
        return result;
    }

    /**
     * 根据特征向量内容搜索小说帖子特征向量数据
     */
    public static List<NovelpostFeatureVector_wlq> searchNovelpostFeatureVectorByFeatureVector(String featureVector) {
        List<NovelpostFeatureVector_wlq> result = new ArrayList<>();
        for (NovelpostFeatureVector_wlq vector : novelpostFeatureVectorCache.values()) {
            if (vector.getFeatureVector() != null && vector.getFeatureVector().contains(featureVector)) {
                result.add(vector);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNovelpostFeatureVector", totalNovelpostFeatureVector);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        novelpostFeatureVectorCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<NovelpostFeatureVector_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
