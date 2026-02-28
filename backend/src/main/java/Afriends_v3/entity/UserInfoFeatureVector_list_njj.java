package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户信息特征向量信息列表
 * 对应实体: UserInfoFeatureVector_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserInfoFeatureVector_list_njj extends EntityList<UserInfoFeatureVector_njj> {

    // 内存存储
    private static final Map<Long, UserInfoFeatureVector_njj> userInfoFeatureVectorCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserInfoFeatureVector = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserInfoFeatureVector_njj> allUserInfoFeatureVector) {
        try {
            System.out.println("开始直接加载用户信息特征向量数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserInfoFeatureVector == null || allUserInfoFeatureVector.isEmpty()) {
                System.out.println("用户信息特征向量数据为空，清空缓存并设置状态");
                totalUserInfoFeatureVector = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户信息特征向量数据直接加载完成！总数: " + totalUserInfoFeatureVector);
                return;
            }

            // 存储到内存缓存
            for (UserInfoFeatureVector_njj vector : allUserInfoFeatureVector) {
                if (vector != null && vector.getUserId() != null) {
                    System.out.println("处理用户信息特征向量数据: 用户ID=" + vector.getUserId());
                    userInfoFeatureVectorCache.put(vector.getUserId(), vector);
                }
            }

            // 更新统计信息
            totalUserInfoFeatureVector = userInfoFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户信息特征向量数据直接加载完成！总数: " + totalUserInfoFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户信息特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserInfoFeatureVector = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户信息特征向量数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserInfoFeatureVector_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户信息特征向量数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户信息特征向量数据
            System.out.println("正在执行数据库查询...");
            List<UserInfoFeatureVector_njj> allUserInfoFeatureVector = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserInfoFeatureVector.size() + " 条记录");

            // 存储到内存缓存
            for (UserInfoFeatureVector_njj vector : allUserInfoFeatureVector) {
                if (vector != null && vector.getUserId() != null) {
                    System.out.println("处理用户信息特征向量数据: 用户ID=" + vector.getUserId());
                    userInfoFeatureVectorCache.put(vector.getUserId(), vector);
                }
            }

            // 更新统计信息
            totalUserInfoFeatureVector = userInfoFeatureVectorCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户信息特征向量数据加载完成！总数: " + totalUserInfoFeatureVector);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户信息特征向量数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户信息特征向量数据（从内存）
     */
    public static List<UserInfoFeatureVector_njj> getAllUserInfoFeatureVector() {
        return new ArrayList<>(userInfoFeatureVectorCache.values());
    }

    /**
     * 根据用户ID获取用户信息特征向量数据
     */
    public static UserInfoFeatureVector_njj getUserInfoFeatureVectorByUserId(Long userId) {
        return userInfoFeatureVectorCache.get(userId);
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserInfoFeatureVector", totalUserInfoFeatureVector);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userInfoFeatureVectorCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserInfoFeatureVector_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
