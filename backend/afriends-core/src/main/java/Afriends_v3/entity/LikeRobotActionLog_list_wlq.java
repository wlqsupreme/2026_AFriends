package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 点赞机器人操作日志信息列表
 * 对应实体: LikeRobotActionLog_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class LikeRobotActionLog_list_wlq extends EntityList<LikeRobotActionLog_wlq> {

    // 内存存储
    private static final Map<Long, LikeRobotActionLog_wlq> likeRobotActionLogCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalLikeRobotActionLog = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<LikeRobotActionLog_wlq> allLikeRobotActionLog) {
        try {
            System.out.println("开始直接加载点赞机器人操作日志数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (LikeRobotActionLog_wlq log : allLikeRobotActionLog) {
                System.out.println("处理机器人操作日志数据: ID=" + log.getLogId() + ", 用户ID=" + log.getUserId());
                likeRobotActionLogCache.put(log.getLogId(), log);
            }

            // 更新统计信息
            totalLikeRobotActionLog = likeRobotActionLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("点赞机器人操作日志数据直接加载完成！总数: " + totalLikeRobotActionLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载点赞机器人操作日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有点赞机器人操作日志数据到内存
     */
    public static void loadFromDatabase(BaseMapper<LikeRobotActionLog_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载点赞机器人操作日志数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有点赞机器人操作日志数据
            System.out.println("正在执行数据库查询...");
            List<LikeRobotActionLog_wlq> allLikeRobotActionLog = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allLikeRobotActionLog.size() + " 条记录");

            // 存储到内存缓存
            for (LikeRobotActionLog_wlq log : allLikeRobotActionLog) {
                System.out.println("处理机器人操作日志数据: ID=" + log.getLogId() + ", 用户ID=" + log.getUserId());
                likeRobotActionLogCache.put(log.getLogId(), log);
            }

            // 更新统计信息
            totalLikeRobotActionLog = likeRobotActionLogCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("点赞机器人操作日志数据加载完成！总数: " + totalLikeRobotActionLog);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载点赞机器人操作日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有点赞机器人操作日志数据（从内存）
     */
    public static List<LikeRobotActionLog_wlq> getAllLikeRobotActionLog() {
        return new ArrayList<>(likeRobotActionLogCache.values());
    }

    /**
     * 根据日志ID获取点赞机器人操作日志数据
     */
    public static LikeRobotActionLog_wlq getLikeRobotActionLogById(Long logId) {
        return likeRobotActionLogCache.get(logId);
    }

    /**
     * 根据用户ID搜索点赞机器人操作日志数据
     */
    public static List<LikeRobotActionLog_wlq> searchLikeRobotActionLogByUserId(Long userId) {
        List<LikeRobotActionLog_wlq> result = new ArrayList<>();
        for (LikeRobotActionLog_wlq log : likeRobotActionLogCache.values()) {
            if (log.getUserId() != null && log.getUserId().equals(userId)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 根据帖子ID搜索点赞机器人操作日志数据
     */
    public static List<LikeRobotActionLog_wlq> searchLikeRobotActionLogByPostId(Long postId) {
        List<LikeRobotActionLog_wlq> result = new ArrayList<>();
        for (LikeRobotActionLog_wlq log : likeRobotActionLogCache.values()) {
            if (log.getPostId() != null && log.getPostId().equals(postId)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 根据动作类型搜索点赞机器人操作日志数据
     */
    public static List<LikeRobotActionLog_wlq> searchLikeRobotActionLogByActionType(String actionType) {
        List<LikeRobotActionLog_wlq> result = new ArrayList<>();
        for (LikeRobotActionLog_wlq log : likeRobotActionLogCache.values()) {
            if (log.getActionType() != null && log.getActionType().contains(actionType)) {
                result.add(log);
            }
        }
        return result;
    }

    /**
     * 根据分数范围搜索点赞机器人操作日志数据
     */
    public static List<LikeRobotActionLog_wlq> searchLikeRobotActionLogByScoreRange(Integer minScore, Integer maxScore) {
        List<LikeRobotActionLog_wlq> result = new ArrayList<>();
        for (LikeRobotActionLog_wlq log : likeRobotActionLogCache.values()) {
            if (log.getScore() != null) {
                if (minScore != null && log.getScore() < minScore) {
                    continue;
                }
                if (maxScore != null && log.getScore() > maxScore) {
                    continue;
                }
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
        stats.put("totalLikeRobotActionLog", totalLikeRobotActionLog);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        likeRobotActionLogCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<LikeRobotActionLog_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
