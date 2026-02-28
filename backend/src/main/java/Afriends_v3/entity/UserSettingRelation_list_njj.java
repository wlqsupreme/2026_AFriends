package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户设置关系信息列表
 * 对应实体: UserSettingRelation_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserSettingRelation_list_njj extends EntityList<UserSettingRelation_njj> {

    // 内存存储
    private static final Map<Long, UserSettingRelation_njj> userSettingRelationCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSettingRelation_njj>> userSettingCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSettingRelation_njj>> settingUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserSettingRelation = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserSettingRelation_njj> allUserSettingRelation) {
        try {
            System.out.println("开始直接加载用户设置关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserSettingRelation == null || allUserSettingRelation.isEmpty()) {
                System.out.println("用户设置关系数据为空，清空缓存并设置状态");
                totalUserSettingRelation = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户设置关系数据直接加载完成！总数: " + totalUserSettingRelation);
                return;
            }

            // 存储到内存缓存
            for (UserSettingRelation_njj relation : allUserSettingRelation) {
                if (relation != null && relation.getId() != null) {
                    System.out.println("处理用户设置关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId());
                    userSettingRelationCache.put(relation.getId(), relation);

                    // 按用户ID分组存储
                    userSettingCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按设置ID分组存储
                    settingUserCache.computeIfAbsent(relation.getSettingId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserSettingRelation = userSettingRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户设置关系数据直接加载完成！总数: " + totalUserSettingRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户设置关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserSettingRelation = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户设置关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserSettingRelation_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户设置关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户设置关系数据
            System.out.println("正在执行数据库查询...");
            List<UserSettingRelation_njj> allUserSettingRelation = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserSettingRelation.size() + " 条记录");

            // 存储到内存缓存
            for (UserSettingRelation_njj relation : allUserSettingRelation) {
                if (relation != null && relation.getId() != null) {
                    System.out.println("处理用户设置关系数据: ID=" + relation.getId() + ", 用户ID=" + relation.getUserId());
                    userSettingRelationCache.put(relation.getId(), relation);

                    // 按用户ID分组存储
                    userSettingCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按设置ID分组存储
                    settingUserCache.computeIfAbsent(relation.getSettingId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserSettingRelation = userSettingRelationCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户设置关系数据加载完成！总数: " + totalUserSettingRelation);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户设置关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户设置关系数据（从内存）
     */
    public static List<UserSettingRelation_njj> getAllUserSettingRelation() {
        return new ArrayList<>(userSettingRelationCache.values());
    }

    /**
     * 根据ID获取用户设置关系数据
     */
    public static UserSettingRelation_njj getUserSettingRelationById(Long id) {
        return userSettingRelationCache.get(id);
    }

    /**
     * 根据用户ID获取用户设置关系数据
     */
    public static List<UserSettingRelation_njj> getUserSettingRelationByUserId(Long userId) {
        return userSettingCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据设置ID获取用户设置关系数据
     */
    public static List<UserSettingRelation_njj> getUserSettingRelationBySettingId(Long settingId) {
        return settingUserCache.getOrDefault(settingId, new ArrayList<>());
    }

    /**
     * 根据设置值搜索用户设置关系数据
     */
    public static List<UserSettingRelation_njj> searchUserSettingRelationByValueText(String valueText) {
        List<UserSettingRelation_njj> result = new ArrayList<>();
        for (UserSettingRelation_njj relation : userSettingRelationCache.values()) {
            if (relation.getValueText() != null && relation.getValueText().contains(valueText)) {
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
        stats.put("totalUserSettingRelation", totalUserSettingRelation);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userSettingRelationCache.clear();
        userSettingCache.clear();
        settingUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserSettingRelation_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 更新缓存中的记录
     */
    public static void updateInCache(UserSettingRelation_njj relation) {
        if (relation != null && relation.getId() != null) {
            userSettingRelationCache.put(relation.getId(), relation);
            
            // 更新用户设置缓存
            Long userId = relation.getUserId();
            if (userId != null) {
                List<UserSettingRelation_njj> userSettings = userSettingCache.getOrDefault(userId, new ArrayList<>());
                boolean found = false;
                for (int i = 0; i < userSettings.size(); i++) {
                    if (userSettings.get(i).getId().equals(relation.getId())) {
                        userSettings.set(i, relation);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    userSettings.add(relation);
                }
                userSettingCache.put(userId, userSettings);
            }
            
            // 更新设置用户缓存
            Long settingId = relation.getSettingId();
            if (settingId != null) {
                List<UserSettingRelation_njj> settingUsers = settingUserCache.getOrDefault(settingId, new ArrayList<>());
                boolean found = false;
                for (int i = 0; i < settingUsers.size(); i++) {
                    if (settingUsers.get(i).getId().equals(relation.getId())) {
                        settingUsers.set(i, relation);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    settingUsers.add(relation);
                }
                settingUserCache.put(settingId, settingUsers);
            }
            
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    /**
     * 添加新记录到缓存
     */
    public static void addToCache(UserSettingRelation_njj relation) {
        if (relation != null && relation.getId() != null) {
            userSettingRelationCache.put(relation.getId(), relation);
            
            // 按用户ID分组存储
            Long userId = relation.getUserId();
            if (userId != null) {
                userSettingCache.computeIfAbsent(userId, k -> new ArrayList<>()).add(relation);
            }
            
            // 按设置ID分组存储
            Long settingId = relation.getSettingId();
            if (settingId != null) {
                settingUserCache.computeIfAbsent(settingId, k -> new ArrayList<>()).add(relation);
            }
            
            totalUserSettingRelation = userSettingRelationCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }
}
