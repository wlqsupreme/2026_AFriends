package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表情基础信息列表
 * 对应实体: EmojiBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class EmojiBase_list_njj extends EntityList<EmojiBase_njj> {

    // 内存存储
    private static final Map<Long, EmojiBase_njj> emojiBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalEmojiBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<EmojiBase_njj> allEmojiBase) {
        try {
            System.out.println("开始直接加载表情基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allEmojiBase == null || allEmojiBase.isEmpty()) {
                System.out.println("表情基础信息数据为空，清空缓存并设置状态");
                totalEmojiBase = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("表情基础信息数据直接加载完成！总数: " + totalEmojiBase);
                return;
            }

            // 存储到内存缓存
            for (EmojiBase_njj emoji : allEmojiBase) {
                if (emoji != null && emoji.getEmojiId() != null) {
                    System.out.println("处理表情数据: ID=" + emoji.getEmojiId() + ", 名称=" + emoji.getEmojiName());
                    emojiBaseCache.put(emoji.getEmojiId(), emoji);
                }
            }

            // 更新统计信息
            totalEmojiBase = emojiBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("表情基础信息数据直接加载完成！总数: " + totalEmojiBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载表情基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalEmojiBase = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有表情基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<EmojiBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载表情基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有表情基础信息数据
            System.out.println("正在执行数据库查询...");
            List<EmojiBase_njj> allEmojiBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allEmojiBase.size() + " 条记录");

            // 存储到内存缓存
            for (EmojiBase_njj emoji : allEmojiBase) {
                System.out.println("处理表情数据: ID=" + emoji.getEmojiId() + ", 名称=" + emoji.getEmojiName());
                emojiBaseCache.put(emoji.getEmojiId(), emoji);
            }

            // 更新统计信息
            totalEmojiBase = emojiBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("表情基础信息数据加载完成！总数: " + totalEmojiBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载表情基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有表情基础信息数据（从内存）
     */
    public static List<EmojiBase_njj> getAllEmojiBase() {
        return new ArrayList<>(emojiBaseCache.values());
    }

    /**
     * 根据表情ID获取表情基础信息数据
     */
    public static EmojiBase_njj getEmojiBaseById(Long emojiId) {
        return emojiBaseCache.get(emojiId);
    }

    /**
     * 根据表情名称搜索表情基础信息数据
     */
    public static List<EmojiBase_njj> searchEmojiBaseByName(String name) {
        List<EmojiBase_njj> result = new ArrayList<>();
        for (EmojiBase_njj emoji : emojiBaseCache.values()) {
            if (emoji.getEmojiName() != null && emoji.getEmojiName().contains(name)) {
                result.add(emoji);
            }
        }
        return result;
    }

    /**
     * 根据用户ID获取表情基础信息数据
     */
    public static List<EmojiBase_njj> getEmojiBaseByUserId(Long userId) {
        List<EmojiBase_njj> result = new ArrayList<>();
        for (EmojiBase_njj emoji : emojiBaseCache.values()) {
            if (emoji.getUserId() != null && emoji.getUserId().equals(userId)) {
                result.add(emoji);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmojiBase", totalEmojiBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        emojiBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<EmojiBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}