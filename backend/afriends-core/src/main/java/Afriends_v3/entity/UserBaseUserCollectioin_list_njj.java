package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础用户收藏信息列表
 * 对应实体: UserBaseUserCollectioin_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseUserCollectioin_list_njj extends EntityList<UserBaseUserCollectioin_njj> {

    // 内存存储
    private static final Map<Long, UserBaseUserCollectioin_njj> userBaseUserCollectioinCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseUserCollectioin_njj>> userCollectionCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseUserCollectioin_njj>> folderCollectionCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseUserCollectioin = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseUserCollectioin_njj> allUserBaseUserCollectioin) {
        try {
            System.out.println("开始直接加载用户基础用户收藏数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseUserCollectioin == null || allUserBaseUserCollectioin.isEmpty()) {
                System.out.println("用户基础用户收藏数据为空，清空缓存并设置状态");
                totalUserBaseUserCollectioin = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户基础用户收藏数据直接加载完成！总数: " + totalUserBaseUserCollectioin);
                return;
            }

            // 存储到内存缓存
            for (UserBaseUserCollectioin_njj collection : allUserBaseUserCollectioin) {
                if (collection != null && collection.getRelId() != null) {
                    System.out
                            .println("处理用户基础用户收藏数据: ID=" + collection.getRelId() + ", 用户ID=" + collection.getUserId());
                    userBaseUserCollectioinCache.put(collection.getRelId(), collection);

                    // 按用户ID分组存储
                    userCollectionCache.computeIfAbsent(collection.getUserId(), k -> new ArrayList<>()).add(collection);

                    // 按收藏夹ID分组存储
                    folderCollectionCache.computeIfAbsent(collection.getFolderId(), k -> new ArrayList<>())
                            .add(collection);
                }
            }

            // 更新统计信息
            totalUserBaseUserCollectioin = userBaseUserCollectioinCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础用户收藏数据直接加载完成！总数: " + totalUserBaseUserCollectioin);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseUserCollectioin = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户基础用户收藏数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseUserCollectioin_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础用户收藏数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础用户收藏数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseUserCollectioin_njj> allUserBaseUserCollectioin = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseUserCollectioin.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseUserCollectioin_njj collection : allUserBaseUserCollectioin) {
                if (collection != null && collection.getRelId() != null) {
                    System.out
                            .println("处理用户基础用户收藏数据: ID=" + collection.getRelId() + ", 用户ID=" + collection.getUserId());
                    userBaseUserCollectioinCache.put(collection.getRelId(), collection);

                    // 按用户ID分组存储
                    userCollectionCache.computeIfAbsent(collection.getUserId(), k -> new ArrayList<>()).add(collection);

                    // 按收藏夹ID分组存储
                    folderCollectionCache.computeIfAbsent(collection.getFolderId(), k -> new ArrayList<>())
                            .add(collection);
                }
            }

            // 更新统计信息
            totalUserBaseUserCollectioin = userBaseUserCollectioinCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础用户收藏数据加载完成！总数: " + totalUserBaseUserCollectioin);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户基础用户收藏数据（从内存）
     */
    public static List<UserBaseUserCollectioin_njj> getAllUserBaseUserCollectioin() {
        return new ArrayList<>(userBaseUserCollectioinCache.values());
    }

    /**
     * 根据关系ID获取用户基础用户收藏数据
     */
    public static UserBaseUserCollectioin_njj getUserBaseUserCollectioinById(Long relId) {
        return userBaseUserCollectioinCache.get(relId);
    }

    /**
     * 根据用户ID获取用户基础用户收藏数据
     */
    public static List<UserBaseUserCollectioin_njj> getUserBaseUserCollectioinByUserId(Long userId) {
        return userCollectionCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据收藏夹ID获取用户基础用户收藏数据
     */
    public static List<UserBaseUserCollectioin_njj> getUserBaseUserCollectioinByFolderId(Long folderId) {
        return folderCollectionCache.getOrDefault(folderId, new ArrayList<>());
    }

    /**
     * 根据目标类型获取用户基础用户收藏数据
     */
    public static List<UserBaseUserCollectioin_njj> getUserBaseUserCollectioinByTargetType(Integer targetType) {
        List<UserBaseUserCollectioin_njj> result = new ArrayList<>();
        for (UserBaseUserCollectioin_njj collection : userBaseUserCollectioinCache.values()) {
            if (collection.getTargetType() != null && collection.getTargetType().equals(targetType)) {
                result.add(collection);
            }
        }
        return result;
    }

    /**
     * 根据删除状态获取用户基础用户收藏数据
     */
    public static List<UserBaseUserCollectioin_njj> getUserBaseUserCollectioinByIsDeleted(Integer isDeleted) {
        List<UserBaseUserCollectioin_njj> result = new ArrayList<>();
        for (UserBaseUserCollectioin_njj collection : userBaseUserCollectioinCache.values()) {
            if (collection.getIsDeleted() != null && collection.getIsDeleted().equals(isDeleted)) {
                result.add(collection);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserBaseUserCollectioin", totalUserBaseUserCollectioin);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseUserCollectioinCache.clear();
        userCollectionCache.clear();
        folderCollectionCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseUserCollectioin_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
