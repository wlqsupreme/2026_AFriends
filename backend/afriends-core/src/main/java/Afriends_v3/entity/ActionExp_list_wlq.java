package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动作经验值信息列表
 * 对应实体: ActionExp_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ActionExp_list_wlq extends EntityList<ActionExp_wlq> {

    // 内存存储
    private static final Map<Integer, ActionExp_wlq> actionExpCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalActionExp = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ActionExp_wlq> allActionExp) {
        try {
            System.out.println("开始直接加载动作经验值数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ActionExp_wlq action : allActionExp) {
                System.out.println("处理动作数据: ID=" + action.getActionId() + ", 名称=" + action.getActionName());
                actionExpCache.put(action.getActionId(), action);
            }

            // 更新统计信息
            totalActionExp = actionExpCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("动作经验值数据直接加载完成！总数: " + totalActionExp);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载动作经验值数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有动作经验值数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ActionExp_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载动作经验值数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有动作经验值数据
            System.out.println("正在执行数据库查询...");
            List<ActionExp_wlq> allActionExp = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allActionExp.size() + " 条记录");

            // 存储到内存缓存
            for (ActionExp_wlq action : allActionExp) {
                System.out.println("处理动作数据: ID=" + action.getActionId() + ", 名称=" + action.getActionName());
                actionExpCache.put(action.getActionId(), action);
            }

            // 更新统计信息
            totalActionExp = actionExpCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("动作经验值数据加载完成！总数: " + totalActionExp);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载动作经验值数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有动作经验值数据（从内存）
     */
    public static List<ActionExp_wlq> getAllActionExp() {
        return new ArrayList<>(actionExpCache.values());
    }

    /**
     * 根据动作ID获取动作经验值数据
     */
    public static ActionExp_wlq getActionExpById(Integer actionId) {
        return actionExpCache.get(actionId);
    }

    /**
     * 根据动作名称搜索动作经验值数据
     */
    public static List<ActionExp_wlq> searchActionExpByName(String name) {
        List<ActionExp_wlq> result = new ArrayList<>();
        for (ActionExp_wlq action : actionExpCache.values()) {
            if (action.getActionName() != null && action.getActionName().contains(name)) {
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
        stats.put("totalActionExp", totalActionExp);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        actionExpCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ActionExp_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

