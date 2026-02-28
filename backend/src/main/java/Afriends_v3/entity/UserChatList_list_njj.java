package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户聊天列表信息列表
 * 对应实体: UserChatList_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserChatList_list_njj extends EntityList<UserChatList_njj> {

    // 内存存储
    private static final Map<Long, UserChatList_njj> userChatListCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserChatList_njj>> userChatCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserChatList_njj>> sessionChatCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserChatList = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserChatList_njj> allUserChatList) {
        try {
            System.out.println("开始直接加载用户聊天列表数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserChatList == null || allUserChatList.isEmpty()) {
                System.out.println("用户聊天列表数据为空，清空缓存并设置状态");
                totalUserChatList = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户聊天列表数据直接加载完成！总数: " + totalUserChatList);
                return;
            }

            // 存储到内存缓存
            for (UserChatList_njj chat : allUserChatList) {
                if (chat != null && chat.getId() != null) {
                    System.out.println("处理用户聊天列表数据: ID=" + chat.getId() + ", 好友ID=" + chat.getFriendId());
                    userChatListCache.put(chat.getId(), chat);

                    // 按用户ID分组存储
                    if (chat.getUserId() != null) {
                        userChatCache.computeIfAbsent(chat.getUserId(), k -> new ArrayList<>()).add(chat);
                    }

                    // 按会话ID分组存储
                    if (chat.getSessionId() != null) {
                        sessionChatCache.computeIfAbsent(chat.getSessionId(), k -> new ArrayList<>()).add(chat);
                    }
                }
            }

            // 更新统计信息
            totalUserChatList = userChatListCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户聊天列表数据直接加载完成！总数: " + totalUserChatList);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserChatList = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户聊天列表数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserChatList_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户聊天列表数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户聊天列表数据
            System.out.println("正在执行数据库查询...");
            List<UserChatList_njj> allUserChatList = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserChatList.size() + " 条记录");

            // 存储到内存缓存
            for (UserChatList_njj chat : allUserChatList) {
                if (chat != null && chat.getId() != null) {
                    System.out.println("处理用户聊天列表数据: ID=" + chat.getId() + ", 用户ID=" + chat.getUserId() + ", 好友ID=" + chat.getFriendId());
                    userChatListCache.put(chat.getId(), chat);

                    // 按用户ID分组存储
                    if (chat.getUserId() != null) {
                        userChatCache.computeIfAbsent(chat.getUserId(), k -> new ArrayList<>()).add(chat);
                    }

                    // 按会话ID分组存储
                    if (chat.getSessionId() != null) {
                        sessionChatCache.computeIfAbsent(chat.getSessionId(), k -> new ArrayList<>()).add(chat);
                    }
                }
            }

            // 更新统计信息
            totalUserChatList = userChatListCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户聊天列表数据加载完成！总数: " + totalUserChatList);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户聊天列表数据（从内存）
     */
    public static List<UserChatList_njj> getAllUserChatList() {
        return new ArrayList<>(userChatListCache.values());
    }

    /**
     * 根据ID获取用户聊天列表数据
     */
    public static UserChatList_njj getUserChatListById(Long id) {
        return userChatListCache.get(id);
    }

    /**
     * 根据用户ID获取用户聊天列表数据
     */
    public static List<UserChatList_njj> getUserChatListByUserId(Long userId) {
        return userChatCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据好友ID获取用户聊天列表数据
     */
    public static List<UserChatList_njj> getUserChatListByFriendId(Long friendId) {
        List<UserChatList_njj> result = new ArrayList<>();
        for (UserChatList_njj chat : userChatListCache.values()) {
            if (chat.getFriendId() != null && chat.getFriendId().equals(friendId)) {
                result.add(chat);
            }
        }
        return result;
    }

    /**
     * 根据会话ID获取用户聊天列表数据
     */
    public static List<UserChatList_njj> getUserChatListBySessionId(Long sessionId) {
        return sessionChatCache.getOrDefault(sessionId, new ArrayList<>());
    }

    /**
     * 根据状态获取用户聊天列表数据
     */
    public static List<UserChatList_njj> getUserChatListByStatus(String status) {
        List<UserChatList_njj> result = new ArrayList<>();
        for (UserChatList_njj chat : userChatListCache.values()) {
            if (chat.getStatus() != null && chat.getStatus().equals(status)) {
                result.add(chat);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserChatList", totalUserChatList);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userChatListCache.clear();
        userChatCache.clear();
        sessionChatCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserChatList_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加聊天列表到缓存
     */
    public static void addToCache(UserChatList_njj chatList) {
        if (chatList != null && chatList.getId() != null) {
            userChatListCache.put(chatList.getId(), chatList);

            // 按用户ID分组存储
            if (chatList.getUserId() != null) {
                userChatCache.computeIfAbsent(chatList.getUserId(), k -> new ArrayList<>()).add(chatList);
            }

            // 按会话ID分组存储
            if (chatList.getSessionId() != null) {
                sessionChatCache.computeIfAbsent(chatList.getSessionId(), k -> new ArrayList<>()).add(chatList);
            }

            // 更新统计信息
            totalUserChatList = userChatListCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("聊天列表已添加到缓存: id=" + chatList.getId() + ", userId=" + chatList.getUserId() + ", sessionId=" + chatList.getSessionId());
        }
    }

    public static void removeFromCacheById(Long id) {
        if (id == null) {
            return;
        }

        UserChatList_njj removed = userChatListCache.remove(id);
        if (removed == null) {
            return;
        }

        if (removed.getUserId() != null) {
            List<UserChatList_njj> userList = userChatCache.get(removed.getUserId());
            if (userList != null) {
                userList.removeIf(r -> r != null && id.equals(r.getId()));
            }
        }

        if (removed.getSessionId() != null) {
            List<UserChatList_njj> sessionList = sessionChatCache.get(removed.getSessionId());
            if (sessionList != null) {
                sessionList.removeIf(r -> r != null && id.equals(r.getId()));
            }
        }

        totalUserChatList = userChatListCache.size();
        lastUpdateTime = System.currentTimeMillis();
    }
}