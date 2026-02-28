package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户与图片帖子关系信息列表
 * 对应实体: UserBaseImagepostBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseImagepostBase_list_njj extends EntityList<UserBaseImagepostBase_njj> {

    // 内存存储
    private static final Map<Long, UserBaseImagepostBase_njj> userBaseImagepostBaseCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseImagepostBase_njj>> userImagepostCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseImagepostBase_njj>> imagepostUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseImagepostBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseImagepostBase_njj> allUserBaseImagepostBase) {
        try {
            System.out.println("开始直接加载用户与图片帖子关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseImagepostBase == null || allUserBaseImagepostBase.isEmpty()) {
                System.out.println("用户与图片帖子关系数据为空，清空缓存并设置状态");
                totalUserBaseImagepostBase = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户与图片帖子关系数据直接加载完成！总数: " + totalUserBaseImagepostBase);
                return;
            }

            // 存储到内存缓存
            for (UserBaseImagepostBase_njj relation : allUserBaseImagepostBase) {
                if (relation != null && relation.getRelId() != null) {
                    System.out.println("处理用户与图片帖子关系数据: ID=" + relation.getRelId() + ", 用户ID=" + relation.getUserId());
                    userBaseImagepostBaseCache.put(relation.getRelId(), relation);

                    // 按用户ID分组存储
                    userImagepostCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按图片帖子ID分组存储
                    imagepostUserCache.computeIfAbsent(relation.getImagepostId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserBaseImagepostBase = userBaseImagepostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户与图片帖子关系数据直接加载完成！总数: " + totalUserBaseImagepostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户与图片帖子关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseImagepostBase = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户与图片帖子关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseImagepostBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户与图片帖子关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户与图片帖子关系数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseImagepostBase_njj> allUserBaseImagepostBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseImagepostBase.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseImagepostBase_njj relation : allUserBaseImagepostBase) {
                if (relation != null && relation.getRelId() != null) {
                    System.out.println("处理用户与图片帖子关系数据: ID=" + relation.getRelId() + ", 用户ID=" + relation.getUserId());
                    userBaseImagepostBaseCache.put(relation.getRelId(), relation);

                    // 按用户ID分组存储
                    userImagepostCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按图片帖子ID分组存储
                    imagepostUserCache.computeIfAbsent(relation.getImagepostId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserBaseImagepostBase = userBaseImagepostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户与图片帖子关系数据加载完成！总数: " + totalUserBaseImagepostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户与图片帖子关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户与图片帖子关系数据（从内存）
     */
    public static List<UserBaseImagepostBase_njj> getAllUserBaseImagepostBase() {
        return new ArrayList<>(userBaseImagepostBaseCache.values());
    }

    /**
     * 根据关系ID获取用户与图片帖子关系数据
     */
    public static UserBaseImagepostBase_njj getUserBaseImagepostBaseById(Long relId) {
        return userBaseImagepostBaseCache.get(relId);
    }

    /**
     * 根据用户ID获取用户与图片帖子关系数据
     */
    public static List<UserBaseImagepostBase_njj> getUserBaseImagepostBaseByUserId(Long userId) {
        return userImagepostCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据图片帖子ID获取用户与图片帖子关系数据
     */
    public static List<UserBaseImagepostBase_njj> getUserBaseImagepostBaseByImagepostId(Long imagepostId) {
        return imagepostUserCache.getOrDefault(imagepostId, new ArrayList<>());
    }

    /**
     * 根据行为类型获取用户与图片帖子关系数据
     */
    public static List<UserBaseImagepostBase_njj> getUserBaseImagepostBaseByBehaviorType(Byte behaviorType) {
        List<UserBaseImagepostBase_njj> result = new ArrayList<>();
        for (UserBaseImagepostBase_njj relation : userBaseImagepostBaseCache.values()) {
            if (relation.getBehaviorType() != null && relation.getBehaviorType().equals(behaviorType)) {
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
        stats.put("totalUserBaseImagepostBase", totalUserBaseImagepostBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseImagepostBaseCache.clear();
        userImagepostCache.clear();
        imagepostUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseImagepostBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
