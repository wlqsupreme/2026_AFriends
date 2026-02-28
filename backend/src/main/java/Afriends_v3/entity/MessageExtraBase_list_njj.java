package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息扩展基础信息列表
 * 对应实体: MessageExtraBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MessageExtraBase_list_njj extends EntityList<MessageExtraBase_njj> {

    // 内存存储
    private static final Map<Long, MessageExtraBase_njj> messageExtraBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalMessageExtraBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<MessageExtraBase_njj> allMessageExtraBase) {
        try {
            System.out.println("开始直接加载消息扩展基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (MessageExtraBase_njj message : allMessageExtraBase) {
                System.out.println(
                        "处理消息扩展数据: ID=" + message.getExtraMessageId() + ", 名称=" + message.getExtraMessageName());
                messageExtraBaseCache.put(message.getExtraMessageId(), message);
            }

            // 更新统计信息
            totalMessageExtraBase = messageExtraBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("消息扩展基础信息数据直接加载完成！总数: " + totalMessageExtraBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载消息扩展基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有消息扩展基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<MessageExtraBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载消息扩展基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有消息扩展基础信息数据
            System.out.println("正在执行数据库查询...");
            List<MessageExtraBase_njj> allMessageExtraBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allMessageExtraBase.size() + " 条记录");

            // 存储到内存缓存
            for (MessageExtraBase_njj message : allMessageExtraBase) {
                System.out.println(
                        "处理消息扩展数据: ID=" + message.getExtraMessageId() + ", 名称=" + message.getExtraMessageName());
                messageExtraBaseCache.put(message.getExtraMessageId(), message);
            }

            // 更新统计信息
            totalMessageExtraBase = messageExtraBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("消息扩展基础信息数据加载完成！总数: " + totalMessageExtraBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载消息扩展基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有消息扩展基础信息数据（从内存）
     */
    public static List<MessageExtraBase_njj> getAllMessageExtraBase() {
        return new ArrayList<>(messageExtraBaseCache.values());
    }

    /**
     * 根据消息扩展ID获取消息扩展基础信息数据
     */
    public static MessageExtraBase_njj getMessageExtraBaseById(Long extraMessageId) {
        return messageExtraBaseCache.get(extraMessageId);
    }

    /**
     * 根据消息名称搜索消息扩展基础信息数据
     */
    public static List<MessageExtraBase_njj> searchMessageExtraBaseByName(String name) {
        List<MessageExtraBase_njj> result = new ArrayList<>();
        for (MessageExtraBase_njj message : messageExtraBaseCache.values()) {
            if (message.getExtraMessageName() != null && message.getExtraMessageName().contains(name)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * 根据状态获取消息扩展基础信息数据
     */
    public static List<MessageExtraBase_njj> getMessageExtraBaseByStatus(Byte status) {
        List<MessageExtraBase_njj> result = new ArrayList<>();
        for (MessageExtraBase_njj message : messageExtraBaseCache.values()) {
            if (message.getStatus() != null && message.getStatus().equals(status)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMessageExtraBase", totalMessageExtraBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        messageExtraBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<MessageExtraBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}