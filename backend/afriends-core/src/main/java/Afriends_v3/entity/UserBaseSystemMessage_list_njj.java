package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户基础系统消息信息列表
 * 对应实体: UserBaseSystemMessage_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBaseSystemMessage_list_njj extends EntityList<UserBaseSystemMessage_njj> {

    // 内存存储
    private static final Map<Long, UserBaseSystemMessage_njj> userBaseSystemMessageCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBaseSystemMessage_njj>> userSystemMessageCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBaseSystemMessage = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBaseSystemMessage_njj> allUserBaseSystemMessage) {
        try {
            System.out.println("开始直接加载用户基础系统消息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBaseSystemMessage == null || allUserBaseSystemMessage.isEmpty()) {
                System.out.println("用户基础系统消息数据为空，清空缓存并设置状态");
                totalUserBaseSystemMessage = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户基础系统消息数据直接加载完成！总数: " + totalUserBaseSystemMessage);
                return;
            }

            // 存储到内存缓存
            for (UserBaseSystemMessage_njj message : allUserBaseSystemMessage) {
                if (message != null && message.getMsgId() != null) {
                    System.out.println("处理用户基础系统消息数据: ID=" + message.getMsgId() + ", 用户ID=" + message.getUserId());
                    userBaseSystemMessageCache.put(message.getMsgId(), message);

                    // 按用户ID分组存储
                    userSystemMessageCache.computeIfAbsent(message.getUserId(), k -> new ArrayList<>()).add(message);
                }
            }

            // 更新统计信息
            totalUserBaseSystemMessage = userBaseSystemMessageCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础系统消息数据直接加载完成！总数: " + totalUserBaseSystemMessage);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户基础系统消息数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBaseSystemMessage = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户基础系统消息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBaseSystemMessage_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户基础系统消息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户基础系统消息数据
            System.out.println("正在执行数据库查询...");
            List<UserBaseSystemMessage_njj> allUserBaseSystemMessage = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBaseSystemMessage.size() + " 条记录");

            // 存储到内存缓存
            for (UserBaseSystemMessage_njj message : allUserBaseSystemMessage) {
                if (message != null && message.getMsgId() != null) {
                    System.out.println("处理用户基础系统消息数据: ID=" + message.getMsgId() + ", 用户ID=" + message.getUserId());
                    userBaseSystemMessageCache.put(message.getMsgId(), message);

                    // 按用户ID分组存储
                    userSystemMessageCache.computeIfAbsent(message.getUserId(), k -> new ArrayList<>()).add(message);
                }
            }

            // 更新统计信息
            totalUserBaseSystemMessage = userBaseSystemMessageCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户基础系统消息数据加载完成！总数: " + totalUserBaseSystemMessage);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户基础系统消息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户基础系统消息数据（从内存）
     */
    public static List<UserBaseSystemMessage_njj> getAllUserBaseSystemMessage() {
        return new ArrayList<>(userBaseSystemMessageCache.values());
    }

    /**
     * 根据消息ID获取用户基础系统消息数据
     */
    public static UserBaseSystemMessage_njj getUserBaseSystemMessageById(Long msgId) {
        return userBaseSystemMessageCache.get(msgId);
    }

    /**
     * 根据用户ID获取用户基础系统消息数据
     */
    public static List<UserBaseSystemMessage_njj> getUserBaseSystemMessageByUserId(Long userId) {
        return userSystemMessageCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据消息类型获取用户基础系统消息数据
     */
    public static List<UserBaseSystemMessage_njj> getUserBaseSystemMessageByMsgType(Byte msgType) {
        List<UserBaseSystemMessage_njj> result = new ArrayList<>();
        for (UserBaseSystemMessage_njj message : userBaseSystemMessageCache.values()) {
            if (message.getMsgType() != null && message.getMsgType().equals(msgType)) {
                result.add(message);
            }
        }
        return result;
    }

    /**
     * 根据阅读状态获取用户基础系统消息数据
     */
    public static List<UserBaseSystemMessage_njj> getUserBaseSystemMessageByIsRead(Byte isRead) {
        List<UserBaseSystemMessage_njj> result = new ArrayList<>();
        for (UserBaseSystemMessage_njj message : userBaseSystemMessageCache.values()) {
            if (message.getIsRead() != null && message.getIsRead().equals(isRead)) {
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
        stats.put("totalUserBaseSystemMessage", totalUserBaseSystemMessage);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBaseSystemMessageCache.clear();
        userSystemMessageCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBaseSystemMessage_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
