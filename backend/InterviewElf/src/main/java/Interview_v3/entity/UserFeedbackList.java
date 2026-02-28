package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用户反馈列表（内存缓存）
 * 对应实体: UserFeedback
 * 功能：从数据库读取反馈数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserFeedbackList extends EntityList<UserFeedback> {

    // 内存存储（key: feedbackId，value: 反馈实体）
    private static final Map<Long, UserFeedback> feedbackCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalFeedback = 0;
    private static int unHandleFeedback = 0; // 未处理反馈数
    private static int handlingFeedback = 0; // 处理中反馈数
    private static int handledFeedback = 0;  // 已处理反馈数

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<UserFeedback> allFeedback) {
        try {
            System.out.println("开始直接加载用户反馈数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserFeedback feedback : allFeedback) {
                System.out.println("处理反馈数据: ID=" + feedback.getFeedbackId() + ", 用户ID=" + feedback.getUserId() + ", 状态=" + feedback.getHandleStatus());
                feedbackCache.put(feedback.getFeedbackId(), feedback);
            }

            // 更新统计信息（按状态分类统计）
            updateStatistics();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户反馈数据直接加载完成！总数: " + totalFeedback);
            System.out.println("未处理: " + unHandleFeedback + ", 处理中: " + handlingFeedback + ", 已处理: " + handledFeedback);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户反馈数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有反馈数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserFeedback> mapper) {
        try {
            System.out.println("开始从数据库加载用户反馈数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有反馈数据
            System.out.println("正在执行数据库查询...");
            List<UserFeedback> allFeedback = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allFeedback.size() + " 条反馈记录");

            // 存储到内存缓存
            for (UserFeedback feedback : allFeedback) {
                System.out.println("处理反馈数据: ID=" + feedback.getFeedbackId() + ", 用户ID=" + feedback.getUserId() + ", 状态=" + feedback.getHandleStatus());
                feedbackCache.put(feedback.getFeedbackId(), feedback);
            }

            // 更新统计信息（按状态分类统计）
            updateStatistics();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户反馈数据加载完成！总数: " + totalFeedback);
            System.out.println("未处理: " + unHandleFeedback + ", 处理中: " + handlingFeedback + ", 已处理: " + handledFeedback);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户反馈数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有反馈数据（从内存，按反馈时间倒序）
     */
    public static List<UserFeedback> getAllFeedback() {
        // 转换为列表并按createTime倒序（最新反馈优先）
        List<UserFeedback> feedbackList = new ArrayList<>(feedbackCache.values());
        feedbackList.sort((f1, f2) -> f2.getCreateTime().compareTo(f1.getCreateTime()));
        return feedbackList;
    }

    /**
     * 根据反馈ID获取反馈数据
     */
    public static UserFeedback getFeedbackById(Long feedbackId) {
        return feedbackCache.get(feedbackId);
    }

    /**
     * 根据用户ID查询反馈（按反馈时间倒序）
     */
    public static List<UserFeedback> getFeedbackByUserId(Long userId) {
        return feedbackCache.values().stream()
                .filter(feedback -> userId.equals(feedback.getUserId()))
                .sorted((f1, f2) -> f2.getCreateTime().compareTo(f1.getCreateTime()))
                .collect(Collectors.toList());
    }

    /**
     * 根据处理状态查询反馈（按反馈时间倒序）
     */
    public static List<UserFeedback> getFeedbackByHandleStatus(Byte status) {
        return feedbackCache.values().stream()
                .filter(feedback -> status.equals(feedback.getHandleStatus()))
                .sorted((f1, f2) -> f2.getCreateTime().compareTo(f1.getCreateTime()))
                .collect(Collectors.toList());
    }

    /**
     * 更新统计信息（按处理状态分类）
     */
    private static void updateStatistics() {
        totalFeedback = feedbackCache.size();
        // 按状态统计
        unHandleFeedback = (int) feedbackCache.values().stream()
                .filter(f -> f.getHandleStatus() == 0)
                .count();
        handlingFeedback = (int) feedbackCache.values().stream()
                .filter(f -> f.getHandleStatus() == 1)
                .count();
        handledFeedback = (int) feedbackCache.values().stream()
                .filter(f -> f.getHandleStatus() == 2)
                .count();
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFeedback", totalFeedback);
        stats.put("unHandleFeedback", unHandleFeedback);
        stats.put("handlingFeedback", handlingFeedback);
        stats.put("handledFeedback", handledFeedback);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        feedbackCache.clear();
        // 重置统计信息
        totalFeedback = 0;
        unHandleFeedback = 0;
        handlingFeedback = 0;
        handledFeedback = 0;
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserFeedback> mapper) {
        loadFromDatabase(mapper);
    }
}