package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册初始标签信息列表
 * 对应实体: IntiniallabelChen_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class IntiniallabelChen_list_wlq extends EntityList<IntiniallabelChen_wlq> {

    // 内存存储
    private static final Map<Long, IntiniallabelChen_wlq> intiniallabelChenCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalIntiniallabelChen = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<IntiniallabelChen_wlq> allIntiniallabelChen) {
        try {
            System.out.println("开始直接加载注册初始标签数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (IntiniallabelChen_wlq label : allIntiniallabelChen) {
                System.out.println("处理初始标签数据: ID=" + label.getV2IntiniallabelChenId() + ", 用户ID=" + label.getUserId());
                intiniallabelChenCache.put(label.getV2IntiniallabelChenId(), label);
            }

            // 更新统计信息
            totalIntiniallabelChen = intiniallabelChenCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("注册初始标签数据直接加载完成！总数: " + totalIntiniallabelChen);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载注册初始标签数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有注册初始标签数据到内存
     */
    public static void loadFromDatabase(BaseMapper<IntiniallabelChen_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载注册初始标签数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有注册初始标签数据
            System.out.println("正在执行数据库查询...");
            List<IntiniallabelChen_wlq> allIntiniallabelChen = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allIntiniallabelChen.size() + " 条记录");

            // 存储到内存缓存
            for (IntiniallabelChen_wlq label : allIntiniallabelChen) {
                System.out.println("处理初始标签数据: ID=" + label.getV2IntiniallabelChenId() + ", 用户ID=" + label.getUserId());
                intiniallabelChenCache.put(label.getV2IntiniallabelChenId(), label);
            }

            // 更新统计信息
            totalIntiniallabelChen = intiniallabelChenCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("注册初始标签数据加载完成！总数: " + totalIntiniallabelChen);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载注册初始标签数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有注册初始标签数据（从内存）
     */
    public static List<IntiniallabelChen_wlq> getAllIntiniallabelChen() {
        return new ArrayList<>(intiniallabelChenCache.values());
    }

    /**
     * 根据初始标签ID获取注册初始标签数据
     */
    public static IntiniallabelChen_wlq getIntiniallabelChenById(Long v2IntiniallabelChenId) {
        return intiniallabelChenCache.get(v2IntiniallabelChenId);
    }

    /**
     * 根据用户ID搜索注册初始标签数据
     */
    public static List<IntiniallabelChen_wlq> searchIntiniallabelChenByUserId(Long userId) {
        List<IntiniallabelChen_wlq> result = new ArrayList<>();
        for (IntiniallabelChen_wlq label : intiniallabelChenCache.values()) {
            if (label.getUserId() != null && label.getUserId().equals(userId)) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * 根据标签ID搜索注册初始标签数据
     */
    public static List<IntiniallabelChen_wlq> searchIntiniallabelChenByLabelId(Long labelId) {
        List<IntiniallabelChen_wlq> result = new ArrayList<>();
        for (IntiniallabelChen_wlq label : intiniallabelChenCache.values()) {
            if (label.getLabelId() != null && label.getLabelId().equals(labelId)) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIntiniallabelChen", totalIntiniallabelChen);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        intiniallabelChenCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<IntiniallabelChen_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
