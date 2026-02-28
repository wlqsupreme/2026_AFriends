package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天设置基础信息列表
 * 对应实体: ChatSettingsBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ChatSettingsBase_list_njj extends EntityList<ChatSettingsBase_njj> {

    // 内存存储
    private static final Map<Long, ChatSettingsBase_njj> chatSettingsBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalChatSettingsBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<ChatSettingsBase_njj> allChatSettingsBase) {
        try {
            System.out.println("开始直接加载聊天设置基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (ChatSettingsBase_njj setting : allChatSettingsBase) {
                System.out.println(
                        "处理聊天设置数据: ID=" + setting.getChatSettingId() + ", 设置名称=" + setting.getChatSettingName());
                chatSettingsBaseCache.put(setting.getChatSettingId(), setting);
            }

            // 更新统计信息
            totalChatSettingsBase = chatSettingsBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("聊天设置基础信息数据直接加载完成！总数: " + totalChatSettingsBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载聊天设置基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有聊天设置基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<ChatSettingsBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载聊天设置基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有聊天设置基础信息数据
            System.out.println("正在执行数据库查询...");
            List<ChatSettingsBase_njj> allChatSettingsBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allChatSettingsBase.size() + " 条记录");

            // 存储到内存缓存
            for (ChatSettingsBase_njj setting : allChatSettingsBase) {
                System.out.println(
                        "处理聊天设置数据: ID=" + setting.getChatSettingId() + ", 设置名称=" + setting.getChatSettingName());
                chatSettingsBaseCache.put(setting.getChatSettingId(), setting);
            }

            // 更新统计信息
            totalChatSettingsBase = chatSettingsBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("聊天设置基础信息数据加载完成！总数: " + totalChatSettingsBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载聊天设置基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有聊天设置基础信息数据（从内存）
     */
    public static List<ChatSettingsBase_njj> getAllChatSettingsBase() {
        return new ArrayList<>(chatSettingsBaseCache.values());
    }

    /**
     * 根据设置ID获取聊天设置基础信息数据
     */
    public static ChatSettingsBase_njj getChatSettingsBaseById(Long chatSettingId) {
        return chatSettingsBaseCache.get(chatSettingId);
    }

    /**
     * 根据设置名称搜索聊天设置基础信息数据
     */
    public static List<ChatSettingsBase_njj> searchChatSettingsBaseByName(String name) {
        List<ChatSettingsBase_njj> result = new ArrayList<>();
        for (ChatSettingsBase_njj setting : chatSettingsBaseCache.values()) {
            if (setting.getChatSettingName() != null && setting.getChatSettingName().contains(name)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 根据状态获取聊天设置基础信息数据
     */
    public static List<ChatSettingsBase_njj> getChatSettingsBaseByStatus(Byte status) {
        List<ChatSettingsBase_njj> result = new ArrayList<>();
        for (ChatSettingsBase_njj setting : chatSettingsBaseCache.values()) {
            if (setting.getStatus() != null && setting.getStatus().equals(status)) {
                result.add(setting);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalChatSettingsBase", totalChatSettingsBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        chatSettingsBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<ChatSettingsBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}