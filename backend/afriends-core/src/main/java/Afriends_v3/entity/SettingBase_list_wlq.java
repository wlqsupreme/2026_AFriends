package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设置基础信息列表
 * 对应实体: SettingBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class SettingBase_list_wlq extends EntityList<SettingBase_wlq> {

    // 内存存储
    private static final Map<Long, SettingBase_wlq> settingBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalSettingBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<SettingBase_wlq> allSettingBase) {
        try {
            System.out.println("开始直接加载设置基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (SettingBase_wlq setting : allSettingBase) {
                System.out.println("处理设置数据: ID=" + setting.getId() + ", 设置键=" + setting.getSettingKey());
                settingBaseCache.put(setting.getId(), setting);
            }

            // 更新统计信息
            totalSettingBase = settingBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("设置基础数据直接加载完成！总数: " + totalSettingBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载设置基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有设置基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<SettingBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载设置基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有设置基础数据
            System.out.println("正在执行数据库查询...");
            List<SettingBase_wlq> allSettingBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allSettingBase.size() + " 条记录");

            // 存储到内存缓存
            for (SettingBase_wlq setting : allSettingBase) {
                System.out.println("处理设置数据: ID=" + setting.getId() + ", 设置键=" + setting.getSettingKey());
                settingBaseCache.put(setting.getId(), setting);
            }

            // 更新统计信息
            totalSettingBase = settingBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("设置基础数据加载完成！总数: " + totalSettingBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载设置基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有设置基础数据（从内存）
     */
    public static List<SettingBase_wlq> getAllSettingBase() {
        return new ArrayList<>(settingBaseCache.values());
    }

    /**
     * 根据ID获取设置基础数据
     */
    public static SettingBase_wlq getSettingBaseById(Long id) {
        return settingBaseCache.get(id);
    }

    /**
     * 根据设置键搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseBySettingKey(String settingKey) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getSettingKey() != null && setting.getSettingKey().equals(settingKey)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据设置名称搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseBySettingName(String settingName) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getSettingName() != null && setting.getSettingName().contains(settingName)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据父级ID搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseByParentId(Long parentId) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getParentId() != null && setting.getParentId().equals(parentId)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据数据类型搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseByDataType(String dataType) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getDataType() != null && setting.getDataType().equals(dataType)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据是否启用搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseByIsActive(Byte isActive) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getIsActive() != null && setting.getIsActive().equals(isActive)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据是否可选择搜索设置基础数据
     */
    public static List<SettingBase_wlq> searchSettingBaseByIsSelectable(Byte isSelectable) {
        List<SettingBase_wlq> result = new ArrayList<>();
        for (SettingBase_wlq setting : settingBaseCache.values()) {
            if (setting.getIsSelectable() != null && setting.getIsSelectable().equals(isSelectable)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSettingBase", totalSettingBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        settingBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<SettingBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
    
    /**
     * 检查缓存是否已加载
     */
    public static boolean isCacheLoaded() {
        return isCacheLoaded;
    }
}