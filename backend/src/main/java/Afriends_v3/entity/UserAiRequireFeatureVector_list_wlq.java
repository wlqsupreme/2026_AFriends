package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户AI需求特征向量信息列表
 * 对应实体: UserAiRequireFeatureVector_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserAiRequireFeatureVector_list_wlq extends EntityList<UserAiRequireFeatureVector_wlq> {

    // 内存存储
    private static final Map<Long, UserAiRequireFeatureVector_wlq> userAiRequireFeatureVectorCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserAiRequireFeatureVector = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserAiRequireFeatureVector_wlq> allUserAiRequireFeatureVector) {
        try {
            System.out.println("开始直接加载用户AI需求特征向量数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserAiRequireFeatureVector_wlq vector : allUserAiRequireFeatureVector) {
                System.out.println("处理特征向量数据: 帖子ID=" + vector.getPostId());
                userAiRequireFeatureVectorCache.put(vector.getPostId(), vector);
            }

            // 更新统计信息
            totalUserAiRequireFeatureVector = userAiRequireFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI需求特征向量数据直接加载完成！总数: " + totalUserAiRequireFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户AI需求特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户AI需求特征向量数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserAiRequireFeatureVector_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户AI需求特征向量数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户AI需求特征向量数据
            System.out.println("正在执行数据库查询...");
            List<UserAiRequireFeatureVector_wlq> allUserAiRequireFeatureVector = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserAiRequireFeatureVector.size() + " 条记录");

            // 存储到内存缓存
            for (UserAiRequireFeatureVector_wlq vector : allUserAiRequireFeatureVector) {
                System.out.println("处理特征向量数据: 帖子ID=" + vector.getPostId());
                userAiRequireFeatureVectorCache.put(vector.getPostId(), vector);
            }

            // 更新统计信息
            totalUserAiRequireFeatureVector = userAiRequireFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI需求特征向量数据加载完成！总数: " + totalUserAiRequireFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户AI需求特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI需求特征向量数据（从内存）
     */
    public static List<UserAiRequireFeatureVector_wlq> getAllUserAiRequireFeatureVector() {
        return new ArrayList<>(userAiRequireFeatureVectorCache.values());
    }

    /**
     * 根据帖子ID获取用户AI需求特征向量数据
     */
    public static UserAiRequireFeatureVector_wlq getUserAiRequireFeatureVectorById(Long postId) {
        return userAiRequireFeatureVectorCache.get(postId);
    }

    /**
     * 根据帖子ID范围搜索用户AI需求特征向量数据
     */
    public static List<UserAiRequireFeatureVector_wlq> searchUserAiRequireFeatureVectorByPostIdRange(Long minPostId, Long maxPostId) {
        List<UserAiRequireFeatureVector_wlq> result = new ArrayList<>();
        for (UserAiRequireFeatureVector_wlq vector : userAiRequireFeatureVectorCache.values()) {
            if (vector.getPostId() != null) {
                if (minPostId != null && vector.getPostId() < minPostId) {
                    continue;
                }
                if (maxPostId != null && vector.getPostId() > maxPostId) {
                    continue;
                }
                result.add(vector);
            }
        }
        return result;
    }

    /**
     * 根据特征向量内容搜索用户AI需求特征向量数据
     */
    public static List<UserAiRequireFeatureVector_wlq> searchUserAiRequireFeatureVectorByFeatureVector(String featureVector) {
        List<UserAiRequireFeatureVector_wlq> result = new ArrayList<>();
        for (UserAiRequireFeatureVector_wlq vector : userAiRequireFeatureVectorCache.values()) {
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
        stats.put("totalUserAiRequireFeatureVector", totalUserAiRequireFeatureVector);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userAiRequireFeatureVectorCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserAiRequireFeatureVector_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
