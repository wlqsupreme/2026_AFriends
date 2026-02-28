package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推荐头像信息列表
 * 对应实体: RecommendedAvatar_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class RecommendedAvatar_list_wlq extends EntityList<RecommendedAvatar_wlq> {

    // 内存存储
    private static final Map<Long, RecommendedAvatar_wlq> recommendedAvatarCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalRecommendedAvatar = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<RecommendedAvatar_wlq> allRecommendedAvatar) {
        try {
            System.out.println("开始直接加载推荐头像数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (RecommendedAvatar_wlq avatar : allRecommendedAvatar) {
                System.out.println("处理推荐头像数据: ID=" + avatar.getId() + ", URL=" + avatar.getUrl());
                recommendedAvatarCache.put(avatar.getId(), avatar);
            }

            // 更新统计信息
            totalRecommendedAvatar = recommendedAvatarCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐头像数据直接加载完成！总数: " + totalRecommendedAvatar);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载推荐头像数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有推荐头像数据到内存
     */
    public static void loadFromDatabase(BaseMapper<RecommendedAvatar_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载推荐头像数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有推荐头像数据
            System.out.println("正在执行数据库查询...");
            List<RecommendedAvatar_wlq> allRecommendedAvatar = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allRecommendedAvatar.size() + " 条记录");

            // 存储到内存缓存
            for (RecommendedAvatar_wlq avatar : allRecommendedAvatar) {
                System.out.println("处理推荐头像数据: ID=" + avatar.getId() + ", URL=" + avatar.getUrl());
                recommendedAvatarCache.put(avatar.getId(), avatar);
            }

            // 更新统计信息
            totalRecommendedAvatar = recommendedAvatarCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐头像数据加载完成！总数: " + totalRecommendedAvatar);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载推荐头像数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有推荐头像数据（从内存）
     */
    public static List<RecommendedAvatar_wlq> getAllRecommendedAvatar() {
        return new ArrayList<>(recommendedAvatarCache.values());
    }

    /**
     * 根据ID获取推荐头像数据
     */
    public static RecommendedAvatar_wlq getRecommendedAvatarById(Long id) {
        return recommendedAvatarCache.get(id);
    }

    /**
     * 根据URL搜索推荐头像数据
     */
    public static List<RecommendedAvatar_wlq> searchRecommendedAvatarByUrl(String url) {
        List<RecommendedAvatar_wlq> result = new ArrayList<>();
        for (RecommendedAvatar_wlq avatar : recommendedAvatarCache.values()) {
            if (avatar.getUrl() != null && avatar.getUrl().contains(url)) {
                result.add(avatar);
            }
        }
        return result;
    }

    /**
     * 根据ID范围搜索推荐头像数据
     */
    public static List<RecommendedAvatar_wlq> searchRecommendedAvatarByIdRange(Long minId, Long maxId) {
        List<RecommendedAvatar_wlq> result = new ArrayList<>();
        for (RecommendedAvatar_wlq avatar : recommendedAvatarCache.values()) {
            if (avatar.getId() != null) {
                if (minId != null && avatar.getId() < minId) {
                    continue;
                }
                if (maxId != null && avatar.getId() > maxId) {
                    continue;
                }
                result.add(avatar);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecommendedAvatar", totalRecommendedAvatar);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        recommendedAvatarCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<RecommendedAvatar_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
