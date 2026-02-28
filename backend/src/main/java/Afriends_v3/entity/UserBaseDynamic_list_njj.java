package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户动态基础信息列表
 * 对应实体: UserBaseDynamic_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseDynamic_list_njj extends EntityList<UserBaseDynamic_njj> {

    // 内存存储
    private static final Map<Long, UserBaseDynamic_njj> userBaseDynamicCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseDynamic_njj>> userDynamicCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseDynamic = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseDynamic_njj> allUserBaseDynamic) {
        try {
            System.out.println("开始直接加载用户动态基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseDynamic == null || allUserBaseDynamic.isEmpty()) {
                System.out.println("用户动态基础信息数据为空，清空缓存并设置状态");
                totalUserBaseDynamic = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户动态基础信息数据直接加载完成！总数: " + totalUserBaseDynamic);
                return;
            }

            // 存储到内存缓存
            for (UserBaseDynamic_njj dynamic : allUserBaseDynamic) {
                if (dynamic != null && dynamic.getDynamicId() != null) {
                    System.out.println("处理用户动态数据: ID=" + dynamic.getDynamicId() + ", 用户ID=" + dynamic.getUserId());
                    userBaseDynamicCache.put(dynamic.getDynamicId(), dynamic);

                    // 按用户ID分组存储
                    userDynamicCache.computeIfAbsent(dynamic.getUserId(), k -> new ArrayList<>()).add(dynamic);
                }
            }

            // 更新统计信息
            totalUserBaseDynamic = userBaseDynamicCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户动态基础信息数据直接加载完成！总数: " + totalUserBaseDynamic);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户动态基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseDynamic = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户动态基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseDynamic_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户动态基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户动态基础信息数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseDynamic_njj> allUserBaseDynamic = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseDynamic.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseDynamic_njj dynamic : allUserBaseDynamic) {
                if (dynamic != null && dynamic.getDynamicId() != null) {
                    System.out.println("处理用户动态数据: ID=" + dynamic.getDynamicId() + ", 用户ID=" + dynamic.getUserId());
                    userBaseDynamicCache.put(dynamic.getDynamicId(), dynamic);

                    // 按用户ID分组存储
                    userDynamicCache.computeIfAbsent(dynamic.getUserId(), k -> new ArrayList<>()).add(dynamic);
                }
            }

            // 更新统计信息
            totalUserBaseDynamic = userBaseDynamicCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户动态基础信息数据加载完成！总数: " + totalUserBaseDynamic);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户动态基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户动态基础信息数据（从内存）
     */
    public static List<UserBaseDynamic_njj> getAllUserBaseDynamic() {
        return new ArrayList<>(userBaseDynamicCache.values());
    }

    /**
     * 根据动态ID获取用户动态基础信息数据
     */
    public static UserBaseDynamic_njj getUserBaseDynamicById(Long dynamicId) {
        return userBaseDynamicCache.get(dynamicId);
    }

    /**
     * 根据用户ID获取用户动态基础信息数据
     */
    public static List<UserBaseDynamic_njj> getUserBaseDynamicByUserId(Long userId) {
        return userDynamicCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据内容类型获取用户动态基础信息数据
     */
    public static List<UserBaseDynamic_njj> getUserBaseDynamicByContentType(Byte contentType) {
        List<UserBaseDynamic_njj> result = new ArrayList<>();
        for (UserBaseDynamic_njj dynamic : userBaseDynamicCache.values()) {
            if (dynamic.getContentType() != null && dynamic.getContentType().equals(contentType)) {
                result.add(dynamic);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserBaseDynamic", totalUserBaseDynamic);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseDynamicCache.clear();
        userDynamicCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseDynamic_njj> mapper) {
        loadFromDatabase(mapper);
    }
}