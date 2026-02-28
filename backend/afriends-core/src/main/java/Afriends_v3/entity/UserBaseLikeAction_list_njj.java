package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础点赞行为信息列表
 * 对应实体: UserBaseLikeAction_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseLikeAction_list_njj extends EntityList<UserBaseLikeAction_njj> {

    // 内存存储
    private static final Map<Long, UserBaseLikeAction_njj> userBaseLikeActionCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseLikeAction_njj>> userLikeCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseLikeAction_njj>> targetLikeCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseLikeAction = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseLikeAction_njj> allUserBaseLikeAction) {
        try {
            System.out.println("开始直接加载用户基础点赞行为数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseLikeAction == null || allUserBaseLikeAction.isEmpty()) {
                System.out.println("用户基础点赞行为数据为空，清空缓存并设置状态");
                totalUserBaseLikeAction = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户基础点赞行为数据直接加载完成！总数: " + totalUserBaseLikeAction);
                return;
            }

            // 存储到内存缓存
            for (UserBaseLikeAction_njj action : allUserBaseLikeAction) {
                if (action != null && action.getLikeId() != null) {
                    System.out.println("处理用户基础点赞行为数据: ID=" + action.getLikeId() + ", 用户ID=" + action.getUserId());
                    userBaseLikeActionCache.put(action.getLikeId(), action);

                    // 按用户ID分组存储
                    userLikeCache.computeIfAbsent(action.getUserId(), k -> new ArrayList<>()).add(action);

                    // 按目标ID分组存储
                    targetLikeCache.computeIfAbsent(action.getTargetId(), k -> new ArrayList<>()).add(action);
                }
            }

            // 更新统计信息
            totalUserBaseLikeAction = userBaseLikeActionCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础点赞行为数据直接加载完成！总数: " + totalUserBaseLikeAction);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础点赞行为数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseLikeAction = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户基础点赞行为数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseLikeAction_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础点赞行为数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础点赞行为数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseLikeAction_njj> allUserBaseLikeAction = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseLikeAction.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseLikeAction_njj action : allUserBaseLikeAction) {
                if (action != null && action.getLikeId() != null) {
                    System.out.println("处理用户基础点赞行为数据: ID=" + action.getLikeId() + ", 用户ID=" + action.getUserId());
                    userBaseLikeActionCache.put(action.getLikeId(), action);

                    // 按用户ID分组存储
                    userLikeCache.computeIfAbsent(action.getUserId(), k -> new ArrayList<>()).add(action);

                    // 按目标ID分组存储
                    targetLikeCache.computeIfAbsent(action.getTargetId(), k -> new ArrayList<>()).add(action);
                }
            }

            // 更新统计信息
            totalUserBaseLikeAction = userBaseLikeActionCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础点赞行为数据加载完成！总数: " + totalUserBaseLikeAction);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础点赞行为数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户基础点赞行为数据（从内存）
     */
    public static List<UserBaseLikeAction_njj> getAllUserBaseLikeAction() {
        return new ArrayList<>(userBaseLikeActionCache.values());
    }

    /**
     * 根据点赞ID获取用户基础点赞行为数据
     */
    public static UserBaseLikeAction_njj getUserBaseLikeActionById(Long likeId) {
        return userBaseLikeActionCache.get(likeId);
    }

    /**
     * 根据用户ID获取用户基础点赞行为数据
     */
    public static List<UserBaseLikeAction_njj> getUserBaseLikeActionByUserId(Long userId) {
        return userLikeCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据目标ID获取用户基础点赞行为数据
     */
    public static List<UserBaseLikeAction_njj> getUserBaseLikeActionByTargetId(Long targetId) {
        return targetLikeCache.getOrDefault(targetId, new ArrayList<>());
    }

    /**
     * 根据目标类型获取用户基础点赞行为数据
     */
    public static List<UserBaseLikeAction_njj> getUserBaseLikeActionByTargetType(Integer targetType) {
        List<UserBaseLikeAction_njj> result = new ArrayList<>();
        for (UserBaseLikeAction_njj action : userBaseLikeActionCache.values()) {
            if (action.getTargetType() != null && action.getTargetType().equals(targetType)) {
                result.add(action);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserBaseLikeAction", totalUserBaseLikeAction);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseLikeActionCache.clear();
        userLikeCache.clear();
        targetLikeCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseLikeAction_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
