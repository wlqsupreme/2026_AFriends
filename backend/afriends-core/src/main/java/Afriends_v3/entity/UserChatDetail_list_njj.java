package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户聊天详情列表
 * 对应实体: UserChatDetail_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserChatDetail_list_njj extends EntityList<UserChatDetail_njj> {

    // 内存存储
    private static final Map<Long, UserChatDetail_njj> userChatDetailCache = new ConcurrentHashMap<>();
    private static final Map<BigInteger, List<UserChatDetail_njj>> sessionCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserChatDetail = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserChatDetail_njj> allUserChatDetail) {
        try {
            System.out.println("开始直接加载用户聊天详情数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserChatDetail == null || allUserChatDetail.isEmpty()) {
                System.out.println("用户聊天详情数据为空，清空缓存并设置状态");
                totalUserChatDetail = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户聊天详情数据直接加载完成！总数: " + totalUserChatDetail);
                return;
            }

            // 存储到内存缓存
            for (UserChatDetail_njj detail : allUserChatDetail) {
                if (detail != null && detail.getId() != null) {
                    System.out.println("处理用户聊天详情数据: ID=" + detail.getId() + ", 会话ID=" + detail.getSessionId());
                    userChatDetailCache.put(detail.getId(), detail);

                    // 按会话ID分组存储
                    if (detail.getSessionId() != null) {
                        sessionCache.computeIfAbsent(detail.getSessionId(), k -> new ArrayList<>()).add(detail);
                    }
                }
            }

            // 更新统计信息
            totalUserChatDetail = userChatDetailCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户聊天详情数据直接加载完成！总数: " + totalUserChatDetail);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserChatDetail = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户聊天详情数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserChatDetail_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户聊天详情数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户聊天详情数据
            System.out.println("正在执行数据库查询...");
            List<UserChatDetail_njj> allUserChatDetail = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserChatDetail.size() + " 条记录");

            // 存储到内存缓存
            for (UserChatDetail_njj detail : allUserChatDetail) {
                if (detail != null && detail.getId() != null) {
                    System.out.println("处理用户聊天详情数据: ID=" + detail.getId() + ", 会话ID=" + detail.getSessionId());
                    userChatDetailCache.put(detail.getId(), detail);

                    // 按会话ID分组存储
                    if (detail.getSessionId() != null) {
                        sessionCache.computeIfAbsent(detail.getSessionId(), k -> new ArrayList<>()).add(detail);
                    }
                }
            }

            // 更新统计信息
            totalUserChatDetail = userChatDetailCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户聊天详情数据加载完成！总数: " + totalUserChatDetail);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户聊天详情数据（从内存）
     */
    public static List<UserChatDetail_njj> getAllUserChatDetail() {
        return new ArrayList<>(userChatDetailCache.values());
    }

    /**
     * 根据ID获取用户聊天详情数据
     */
    public static UserChatDetail_njj getUserChatDetailById(Long id) {
        return userChatDetailCache.get(id);
    }

    /**
     * 根据会话ID获取用户聊天详情数据
     */
    public static List<UserChatDetail_njj> getUserChatDetailBySessionId(java.math.BigInteger sessionId) {
        return sessionCache.getOrDefault(sessionId, new ArrayList<>());
    }

    /**
     * 根据发送者类型获取用户聊天详情数据
     */
    public static List<UserChatDetail_njj> getUserChatDetailBySenderType(String senderType) {
        List<UserChatDetail_njj> result = new ArrayList<>();
        for (UserChatDetail_njj detail : userChatDetailCache.values()) {
            if (detail.getSenderType() != null && detail.getSenderType().equals(senderType)) {
                result.add(detail);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserChatDetail", totalUserChatDetail);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userChatDetailCache.clear();
        sessionCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserChatDetail_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加聊天详情到缓存
     */
    public static void addToCache(UserChatDetail_njj chatDetail) {
        if (chatDetail != null && chatDetail.getId() != null) {
            userChatDetailCache.put(chatDetail.getId(), chatDetail);

            // 按会话ID分组存储
            if (chatDetail.getSessionId() != null) {
                sessionCache.computeIfAbsent(chatDetail.getSessionId(), k -> new ArrayList<>()).add(chatDetail);
            }

            // 更新统计信息
            totalUserChatDetail = userChatDetailCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("聊天详情已添加到缓存: id=" + chatDetail.getId() + ", sessionId=" + chatDetail.getSessionId());
        }
    }
}