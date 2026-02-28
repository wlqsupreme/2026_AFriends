package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户小说关系信息列表
 * 对应实体: UserNovelRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserNovelRelation_list_njj extends EntityList<UserNovelRelation_njj> {

    // 内存存储
    private static final Map<Long, UserNovelRelation_njj> userNovelRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserNovelRelation_njj>> userNovelCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserNovelRelation_njj>> novelUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserNovelRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserNovelRelation_njj> allUserNovelRelation) {
        try {
            System.out.println("开始直接加载用户小说关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserNovelRelation == null || allUserNovelRelation.isEmpty()) {
                System.out.println("用户小说关系数据为空，清空缓存并设置状态");
                totalUserNovelRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户小说关系数据直接加载完成！总数: " + totalUserNovelRelation);
                return;
            }

            // 存储到内存缓存
            for (UserNovelRelation_njj relation : allUserNovelRelation) {
                if (relation != null && relation.getId() != null) {
                    System.out.println("处理用户小说关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId());
                    userNovelRelationCache.put(relation.getId(), relation);

                    // 按用户ID分组存储
                    userNovelCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按小说ID分组存储
                    novelUserCache.computeIfAbsent(relation.getNovelId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserNovelRelation = userNovelRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户小说关系数据直接加载完成！总数: " + totalUserNovelRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户小说关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserNovelRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户小说关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserNovelRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户小说关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户小说关系数据
            System.out.println("正在执行数据库查询...");
            List<UserNovelRelation_njj> allUserNovelRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserNovelRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserNovelRelation_njj relation : allUserNovelRelation) {
                if (relation != null && relation.getId() != null) {
                    System.out.println("处理用户小说关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId());
                    userNovelRelationCache.put(relation.getId(), relation);

                    // 按用户ID分组存储
                    userNovelCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按小说ID分组存储
                    novelUserCache.computeIfAbsent(relation.getNovelId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserNovelRelation = userNovelRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户小说关系数据加载完成！总数: " + totalUserNovelRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户小说关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户小说关系数据（从内存）
     */
    public static List<UserNovelRelation_njj> getAllUserNovelRelation() {
        return new ArrayList<>(userNovelRelationCache.values());
    }

    /**
     * 根据ID获取用户小说关系数据
     */
    public static UserNovelRelation_njj getUserNovelRelationById(Long id) {
        return userNovelRelationCache.get(id);
    }

    /**
     * 根据用户ID获取用户小说关系数据
     */
    public static List<UserNovelRelation_njj> getUserNovelRelationByUserId(Long userId) {
        return userNovelCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据小说ID获取用户小说关系数据
     */
    public static List<UserNovelRelation_njj> getUserNovelRelationByNovelId(Long novelId) {
        return novelUserCache.getOrDefault(novelId, new ArrayList<>());
    }

    /**
     * 根据可见性获取用户小说关系数据
     */
    public static List<UserNovelRelation_njj> getUserNovelRelationByIsView(Byte isView) {
        List<UserNovelRelation_njj> result = new ArrayList<>();
        for (UserNovelRelation_njj relation : userNovelRelationCache.values()) {
            if (relation.getIsView() != null && relation.getIsView().equals(isView)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围获取用户小说关系数据
     */
    public static List<UserNovelRelation_njj> getUserNovelRelationByLikeCountRange(Integer minLikeCount,
            Integer maxLikeCount) {
        List<UserNovelRelation_njj> result = new ArrayList<>();
        for (UserNovelRelation_njj relation : userNovelRelationCache.values()) {
            if (relation.getLikeCount() != null) {
                if (minLikeCount != null && relation.getLikeCount() < minLikeCount)
                    continue;
                if (maxLikeCount != null && relation.getLikeCount() > maxLikeCount)
                    continue;
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
        stats.put("totalUserNovelRelation", totalUserNovelRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userNovelRelationCache.clear();
        userNovelCache.clear();
        novelUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserNovelRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
