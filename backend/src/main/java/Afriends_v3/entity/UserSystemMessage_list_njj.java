package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户系统消息信息列表
 * 对应实体: UserSystemMessage_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserSystemMessage_list_njj extends EntityList<UserSystemMessage_njj> {

    // 内存存储
    private static final Map<Long, UserSystemMessage_njj> userSystemMessageCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSystemMessage_njj>> recipientCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserSystemMessage_njj>> senderCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserSystemMessage = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserSystemMessage_njj> allUserSystemMessage) {
        try {
            System.out.println("开始直接加载用户系统消息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserSystemMessage == null || allUserSystemMessage.isEmpty()) {
                System.out.println("用户系统消息数据为空，清空缓存并设置状态");
                totalUserSystemMessage = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户系统消息数据直接加载完成！总数: " + totalUserSystemMessage);
                return;
            }

            // 存储到内存缓存
            for (UserSystemMessage_njj message : allUserSystemMessage) {
                if (message != null && message.getMessageId() != null) {
                    System.out.println(
                            "处理用户系统消息数据: ID=" + message.getMessageId() + ", 接收者ID=" + message.getRecipientUserId());
                    userSystemMessageCache.put(message.getMessageId(), message);

                    // 按接收者用户ID分组存储
                    recipientCache.computeIfAbsent(message.getRecipientUserId(), k -> new ArrayList<>()).add(message);

                    // 按发送者用户ID分组存储
                    if (message.getSenderUserId() != null) {
                        senderCache.computeIfAbsent(message.getSenderUserId(), k -> new ArrayList<>()).add(message);
                    }
                }
            }

            // 更新统计信息
            totalUserSystemMessage = userSystemMessageCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户系统消息数据直接加载完成！总数: " + totalUserSystemMessage);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户系统消息数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserSystemMessage = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户系统消息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserSystemMessage_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户系统消息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户系统消息数据
            System.out.println("正在执行数据库查询...");
            List<UserSystemMessage_njj> allUserSystemMessage = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserSystemMessage.size() + " 条记录");

            // 存储到内存缓存
            for (UserSystemMessage_njj message : allUserSystemMessage) {
                if (message != null && message.getMessageId() != null) {
                    System.out.println(
                            "处理用户系统消息数据: ID=" + message.getMessageId() + ", 接收者ID=" + message.getRecipientUserId());
                    userSystemMessageCache.put(message.getMessageId(), message);

                    // 按接收者用户ID分组存储
                    recipientCache.computeIfAbsent(message.getRecipientUserId(), k -> new ArrayList<>()).add(message);

                    // 按发送者用户ID分组存储
                    if (message.getSenderUserId() != null) {
                        senderCache.computeIfAbsent(message.getSenderUserId(), k -> new ArrayList<>()).add(message);
                    }
                }
            }

            // 更新统计信息
            totalUserSystemMessage = userSystemMessageCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户系统消息数据加载完成！总数: " + totalUserSystemMessage);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户系统消息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户系统消息数据（从内存）
     */
    public static List<UserSystemMessage_njj> getAllUserSystemMessage() {
        return new ArrayList<>(userSystemMessageCache.values());
    }

    /**
     * 根据消息ID获取用户系统消息数据
     */
    public static UserSystemMessage_njj getUserSystemMessageById(Long messageId) {
        return userSystemMessageCache.get(messageId);
    }

    /**
     * 根据接收者用户ID获取用户系统消息数据
     */
    public static List<UserSystemMessage_njj> getUserSystemMessageByRecipientUserId(Long recipientUserId) {
        return recipientCache.getOrDefault(recipientUserId, new ArrayList<>());
    }

    /**
     * 根据发送者用户ID获取用户系统消息数据
     */
    public static List<UserSystemMessage_njj> getUserSystemMessageBySenderUserId(Long senderUserId) {
        return senderCache.getOrDefault(senderUserId, new ArrayList<>());
    }

    /**
     * 根据阅读状态获取用户系统消息数据
     */
    public static List<UserSystemMessage_njj> getUserSystemMessageByIsRead(Byte isRead) {
        List<UserSystemMessage_njj> result = new ArrayList<>();
        for (UserSystemMessage_njj message : userSystemMessageCache.values()) {
            if (message.getIsRead() != null && message.getIsRead().equals(isRead)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * 根据相关实体类型获取用户系统消息数据
     */
    public static List<UserSystemMessage_njj> getUserSystemMessageByRelatedEntityType(String relatedEntityType) {
        List<UserSystemMessage_njj> result = new ArrayList<>();
        for (UserSystemMessage_njj message : userSystemMessageCache.values()) {
            if (message.getRelatedEntityType() != null && message.getRelatedEntityType().equals(relatedEntityType)) {
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
        stats.put("totalUserSystemMessage", totalUserSystemMessage);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userSystemMessageCache.clear();
        recipientCache.clear();
        senderCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserSystemMessage_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加消息到缓存
     */
    public static void addToCache(UserSystemMessage_njj message) {
        if (message != null && message.getMessageId() != null) {
            // 添加到主缓存
            userSystemMessageCache.put(message.getMessageId(), message);
            
            // 按接收者用户ID分组存储
            recipientCache.computeIfAbsent(message.getRecipientUserId(), k -> new ArrayList<>()).add(message);
            
            // 按发送者用户ID分组存储
            if (message.getSenderUserId() != null) {
                senderCache.computeIfAbsent(message.getSenderUserId(), k -> new ArrayList<>()).add(message);
            }
            
            // 更新统计信息
            totalUserSystemMessage = userSystemMessageCache.size();
            
            // 更新缓存状态
            lastUpdateTime = System.currentTimeMillis();
            
            System.out.println("用户系统消息已添加到缓存: ID=" + message.getMessageId() + 
                    ", 接收者ID=" + message.getRecipientUserId() + 
                    ", 发送者ID=" + message.getSenderUserId());
        }
    }
}
