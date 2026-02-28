package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户内容浏览日志信息列表
 * 对应实体: UserContentViewLog_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserContentViewLog_list_njj extends EntityList<UserContentViewLog_njj> {

    // 内存存储
    private static final Map<Long, UserContentViewLog_njj> userContentViewLogCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserContentViewLog_njj>> userViewLogCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserContentViewLog_njj>> contentViewLogCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserContentViewLog = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserContentViewLog_njj> allUserContentViewLog) {
        try {
            System.out.println("开始直接加载用户内容浏览日志数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserContentViewLog == null || allUserContentViewLog.isEmpty()) {
                System.out.println("用户内容浏览日志数据为空，清空缓存并设置状态");
                totalUserContentViewLog = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户内容浏览日志数据直接加载完成！总数: " + totalUserContentViewLog);
                return;
            }

            // 存储到内存缓存
            for (UserContentViewLog_njj log : allUserContentViewLog) {
                if (log != null && log.getId() != null) {
                    System.out.println("处理用户内容浏览日志数据: ID=" + log.getId() + ", 用户ID=" + log.getUserId());
                    userContentViewLogCache.put(log.getId(), log);

                    // 按用户ID分组存储
                    userViewLogCache.computeIfAbsent(log.getUserId(), k -> new ArrayList<>()).add(log);

                    // 按内容ID分组存储
                    contentViewLogCache.computeIfAbsent(log.getContentId(), k -> new ArrayList<>()).add(log);
                }
            }

            // 更新统计信息
            totalUserContentViewLog = userContentViewLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户内容浏览日志数据直接加载完成！总数: " + totalUserContentViewLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户内容浏览日志数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserContentViewLog = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户内容浏览日志数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserContentViewLog_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户内容浏览日志数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户内容浏览日志数据
            System.out.println("正在执行数据库查询...");
            List<UserContentViewLog_njj> allUserContentViewLog = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserContentViewLog.size() + " 条记录");

            // 存储到内存缓存
            for (UserContentViewLog_njj log : allUserContentViewLog) {
                if (log != null && log.getId() != null) {
                    System.out.println("处理用户内容浏览日志数据: ID=" + log.getId() + ", 用户ID=" + log.getUserId());
                    userContentViewLogCache.put(log.getId(), log);

                    // 按用户ID分组存储
                    userViewLogCache.computeIfAbsent(log.getUserId(), k -> new ArrayList<>()).add(log);

                    // 按内容ID分组存储
                    contentViewLogCache.computeIfAbsent(log.getContentId(), k -> new ArrayList<>()).add(log);
                }
            }

            // 更新统计信息
            totalUserContentViewLog = userContentViewLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户内容浏览日志数据加载完成！总数: " + totalUserContentViewLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户内容浏览日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户内容浏览日志数据（从内存）
     */
    public static List<UserContentViewLog_njj> getAllUserContentViewLog() {
        return new ArrayList<>(userContentViewLogCache.values());
    }

    /**
     * 根据ID获取用户内容浏览日志数据
     */
    public static UserContentViewLog_njj getUserContentViewLogById(Long id) {
        return userContentViewLogCache.get(id);
    }

    /**
     * 根据用户ID获取用户内容浏览日志数据
     */
    public static List<UserContentViewLog_njj> getUserContentViewLogByUserId(Long userId) {
        return userViewLogCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据内容ID获取用户内容浏览日志数据
     */
    public static List<UserContentViewLog_njj> getUserContentViewLogByContentId(Long contentId) {
        return contentViewLogCache.getOrDefault(contentId, new ArrayList<>());
    }

    /**
     * 根据内容类型获取用户内容浏览日志数据
     */
    public static List<UserContentViewLog_njj> getUserContentViewLogByContentType(Byte contentType) {
        List<UserContentViewLog_njj> result = new ArrayList<>();
        for (UserContentViewLog_njj log : userContentViewLogCache.values()) {
            if (log.getContentType() != null && log.getContentType().equals(contentType)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 根据浏览类型获取用户内容浏览日志数据
     */
    public static List<UserContentViewLog_njj> getUserContentViewLogByViewType(Byte viewType) {
        List<UserContentViewLog_njj> result = new ArrayList<>();
        for (UserContentViewLog_njj log : userContentViewLogCache.values()) {
            if (log.getViewType() != null && log.getViewType().equals(viewType)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserContentViewLog", totalUserContentViewLog);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userContentViewLogCache.clear();
        userViewLogCache.clear();
        contentViewLogCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserContentViewLog_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
