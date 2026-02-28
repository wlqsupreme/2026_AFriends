package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用户通知设置列表缓存类
 * 用于在内存中缓存用户通知设置数据，提高访问性能
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserNotificationSettings_list_njj extends EntityList<UserNotificationSettings_njj> {
    
    // 内存存储
    private static final Map<Long, UserNotificationSettings_njj> userNotificationSettingsCache = new ConcurrentHashMap<>();
    private static final List<UserNotificationSettings_njj> userNotificationSettingsList = new CopyOnWriteArrayList<>();
    
    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;
    
    // 统计信息
    private static int totalUserNotificationSettings = 0;
    
    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserNotificationSettings_njj> allUserNotificationSettings) {
        try {
            System.out.println("开始直接加载用户通知设置数据到内存...");
            
            // 清空现有缓存
            clearCache();
            
            // 检查数据是否为空
            if (allUserNotificationSettings == null || allUserNotificationSettings.isEmpty()) {
                System.out.println("用户通知设置数据为空，清空缓存并设置状态");
                totalUserNotificationSettings = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户通知设置数据直接加载完成！总数: " + totalUserNotificationSettings);
                return;
            }
            
            // 存储到内存缓存
            for (UserNotificationSettings_njj settings : allUserNotificationSettings) {
                if (settings != null && settings.getUserId() != null) {
                    System.out.println("处理用户通知设置数据: ID=" + settings.getId() + ", 用户ID=" + settings.getUserId());
                    userNotificationSettingsCache.put(settings.getUserId(), settings);
                    userNotificationSettingsList.add(settings);
                }
            }
            
            // 更新统计信息
            totalUserNotificationSettings = userNotificationSettingsCache.size();
            
            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();
            
            System.out.println("用户通知设置数据直接加载完成！总数: " + totalUserNotificationSettings);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);
            
        } catch (Exception e) {
            System.err.println("直接加载用户通知设置数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserNotificationSettings = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }
    
    /**
     * 从数据库加载所有用户通知设置数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserNotificationSettings_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户通知设置数据...");
            
            // 清空现有缓存
            clearCache();
            
            // 从数据库查询所有用户通知设置数据
            System.out.println("正在执行数据库查询...");
            List<UserNotificationSettings_njj> allUserNotificationSettings = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserNotificationSettings.size() + " 条记录");
            
            // 存储到内存缓存
            for (UserNotificationSettings_njj settings : allUserNotificationSettings) {
                if (settings != null && settings.getUserId() != null) {
                    System.out.println("处理用户通知设置数据: ID=" + settings.getId() + ", 用户ID=" + settings.getUserId());
                    userNotificationSettingsCache.put(settings.getUserId(), settings);
                    userNotificationSettingsList.add(settings);
                }
            }
            
            // 更新统计信息
            totalUserNotificationSettings = userNotificationSettingsCache.size();
            
            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();
            
            System.out.println("用户通知设置数据加载完成！总数: " + totalUserNotificationSettings);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);
            
        } catch (Exception e) {
            System.err.println("加载用户通知设置数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 根据用户ID获取通知设置
     *
     * @param userId 用户ID
     * @return 用户通知设置对象，如果不存在则返回null
     */
    public static UserNotificationSettings_njj getByUserId(Long userId) {
        return userNotificationSettingsCache.get(userId);
    }
    
    /**
     * 添加或更新用户通知设置
     *
     * @param settings 用户通知设置对象
     */
    public static void put(UserNotificationSettings_njj settings) {
        if (settings == null || settings.getUserId() == null) {
            return;
        }
        
        // 更新缓存映射
        userNotificationSettingsCache.put(settings.getUserId(), settings);
        
        // 更新列表
        removeByUserId(settings.getUserId());
        userNotificationSettingsList.add(settings);
        
        // 更新统计信息
        totalUserNotificationSettings = userNotificationSettingsCache.size();
        lastUpdateTime = System.currentTimeMillis();
    }
    
    /**
     * 根据用户ID移除通知设置
     *
     * @param userId 用户ID
     */
    public static void removeByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        
        UserNotificationSettings_njj removed = userNotificationSettingsCache.remove(userId);
        if (removed != null) {
            userNotificationSettingsList.remove(removed);
            totalUserNotificationSettings = userNotificationSettingsCache.size();
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    
    /**
     * 获取所有用户通知设置
     *
     * @return 所有用户通知设置的列表副本
     */
    public static List<UserNotificationSettings_njj> getAll() {
        return new ArrayList<>(userNotificationSettingsList);
    }
    
    /**
     * 清空所有缓存数据
     */
    public static void clear() {
        userNotificationSettingsCache.clear();
        userNotificationSettingsList.clear();
        totalUserNotificationSettings = 0;
        isCacheLoaded = false;
        lastUpdateTime = System.currentTimeMillis();
    }
    
    /**
     * 获取缓存中的用户通知设置数量
     *
     * @return 缓存中的用户通知设置数量
     */
    public static int size() {
        return userNotificationSettingsCache.size();
    }
    
    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserNotificationSettings", totalUserNotificationSettings);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }
    
    /**
     * 清空缓存
     */
    private static void clearCache() {
        userNotificationSettingsCache.clear();
        userNotificationSettingsList.clear();
        isCacheLoaded = false;
    }
    
    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserNotificationSettings_njj> mapper) {
        loadFromDatabase(mapper);
    }
}