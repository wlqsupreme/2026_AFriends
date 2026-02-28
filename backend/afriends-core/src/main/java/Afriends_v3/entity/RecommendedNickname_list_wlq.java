package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推荐昵称信息列表
 * 对应实体: RecommendedNickname_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class RecommendedNickname_list_wlq extends EntityList<RecommendedNickname_wlq> {

    // 内存存储
    private static final Map<Long, RecommendedNickname_wlq> recommendedNicknameCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalRecommendedNickname = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<RecommendedNickname_wlq> allRecommendedNickname) {
        try {
            System.out.println("开始直接加载推荐昵称数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (RecommendedNickname_wlq nickname : allRecommendedNickname) {
                System.out.println("处理推荐昵称数据: ID=" + nickname.getId() + ", 昵称=" + nickname.getNickname());
                recommendedNicknameCache.put(nickname.getId(), nickname);
            }

            // 更新统计信息
            totalRecommendedNickname = recommendedNicknameCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐昵称数据直接加载完成！总数: " + totalRecommendedNickname);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载推荐昵称数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有推荐昵称数据到内存
     */
    public static void loadFromDatabase(BaseMapper<RecommendedNickname_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载推荐昵称数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有推荐昵称数据
            System.out.println("正在执行数据库查询...");
            List<RecommendedNickname_wlq> allRecommendedNickname = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allRecommendedNickname.size() + " 条记录");

            // 存储到内存缓存
            for (RecommendedNickname_wlq nickname : allRecommendedNickname) {
                System.out.println("处理推荐昵称数据: ID=" + nickname.getId() + ", 昵称=" + nickname.getNickname());
                recommendedNicknameCache.put(nickname.getId(), nickname);
            }

            // 更新统计信息
            totalRecommendedNickname = recommendedNicknameCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("推荐昵称数据加载完成！总数: " + totalRecommendedNickname);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载推荐昵称数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有推荐昵称数据（从内存）
     */
    public static List<RecommendedNickname_wlq> getAllRecommendedNickname() {
        return new ArrayList<>(recommendedNicknameCache.values());
    }

    /**
     * 根据ID获取推荐昵称数据
     */
    public static RecommendedNickname_wlq getRecommendedNicknameById(Long id) {
        return recommendedNicknameCache.get(id);
    }

    /**
     * 根据昵称搜索推荐昵称数据
     */
    public static List<RecommendedNickname_wlq> searchRecommendedNicknameByNickname(String nickname) {
        List<RecommendedNickname_wlq> result = new ArrayList<>();
        for (RecommendedNickname_wlq nick : recommendedNicknameCache.values()) {
            if (nick.getNickname() != null && nick.getNickname().contains(nickname)) {
                result.add(nick);
            }
        }
        return result;
    }

    /**
     * 根据ID范围搜索推荐昵称数据
     */
    public static List<RecommendedNickname_wlq> searchRecommendedNicknameByIdRange(Long minId, Long maxId) {
        List<RecommendedNickname_wlq> result = new ArrayList<>();
        for (RecommendedNickname_wlq nickname : recommendedNicknameCache.values()) {
            if (nickname.getId() != null) {
                if (minId != null && nickname.getId() < minId) {
                    continue;
                }
                if (maxId != null && nickname.getId() > maxId) {
                    continue;
                }
                result.add(nickname);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecommendedNickname", totalRecommendedNickname);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        recommendedNicknameCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<RecommendedNickname_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
