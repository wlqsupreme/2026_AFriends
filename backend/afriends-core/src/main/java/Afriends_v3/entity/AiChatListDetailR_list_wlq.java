package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI聊天历史信息列表
 * 对应实体: AiChatListDetailR_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiChatListDetailR_list_wlq extends EntityList<AiChatListDetailR_wlq> {

    // 内存存储
    private static final Map<Long, AiChatListDetailR_wlq> aiChatListDetailRCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAiChatListDetailR = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AiChatListDetailR_wlq> allAiChatListDetailR) {
        try {
            System.out.println("开始直接加载AI聊天历史数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiChatListDetailR_wlq chat : allAiChatListDetailR) {
                System.out.println("处理聊天数据: ID=" + chat.getId() + ", 用户ID=" + chat.getUserId());
                aiChatListDetailRCache.put(chat.getId(), chat);
            }

            // 更新统计信息
            totalAiChatListDetailR = aiChatListDetailRCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI聊天历史数据直接加载完成！总数: " + totalAiChatListDetailR);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI聊天历史数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有AI聊天历史数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiChatListDetailR_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载AI聊天历史数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有AI聊天历史数据
            System.out.println("正在执行数据库查询...");
            List<AiChatListDetailR_wlq> allAiChatListDetailR = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAiChatListDetailR.size() + " 条记录");

            // 存储到内存缓存
            for (AiChatListDetailR_wlq chat : allAiChatListDetailR) {
                System.out.println("处理聊天数据: ID=" + chat.getId() + ", 用户ID=" + chat.getUserId());
                aiChatListDetailRCache.put(chat.getId(), chat);
            }

            // 更新统计信息
            totalAiChatListDetailR = aiChatListDetailRCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI聊天历史数据加载完成！总数: " + totalAiChatListDetailR);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI聊天历史数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI聊天历史数据（从内存）
     */
    public static List<AiChatListDetailR_wlq> getAllAiChatListDetailR() {
        return new ArrayList<>(aiChatListDetailRCache.values());
    }

    /**
     * 根据聊天ID获取AI聊天历史数据
     */
    public static AiChatListDetailR_wlq getAiChatListDetailRById(Long id) {
        return aiChatListDetailRCache.get(id);
    }

    /**
     * 根据用户ID搜索AI聊天历史数据
     */
    public static List<AiChatListDetailR_wlq> searchAiChatListDetailRByUserId(Long userId) {
        List<AiChatListDetailR_wlq> result = new ArrayList<>();
        for (AiChatListDetailR_wlq chat : aiChatListDetailRCache.values()) {
            if (chat.getUserId() != null && chat.getUserId().equals(userId)) {
                result.add(chat);
            }
        }
        return result;
    }

    /**
     * 添加单条聊天记录到缓存
     */
    public static void addToCache(AiChatListDetailR_wlq chatDetail) {
        if (chatDetail != null && chatDetail.getId() != null) {
            aiChatListDetailRCache.put(chatDetail.getId(), chatDetail);
            totalAiChatListDetailR = aiChatListDetailRCache.size();
            System.out.println("AI聊天记录已添加到缓存: ID=" + chatDetail.getId());
        }
    }

    /**
     * 检查缓存是否已加载
     */
    public static boolean isCacheLoaded() {
        return isCacheLoaded;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAiChatListDetailR", totalAiChatListDetailR);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        aiChatListDetailRCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiChatListDetailR_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

