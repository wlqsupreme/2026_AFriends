package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小说内容基础信息列表
 * 对应实体: NovelContentBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class NovelContentBase_list_wlq extends EntityList<NovelContentBase_wlq> {

    // 内存存储
    private static final Map<Long, NovelContentBase_wlq> novelContentBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalNovelContentBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<NovelContentBase_wlq> allNovelContentBase) {
        try {
            System.out.println("开始直接加载小说内容基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (NovelContentBase_wlq novel : allNovelContentBase) {
                System.out.println("处理小说数据: ID=" + novel.getNovelContentId() + ", 标题=" + novel.getNovelTitle());
                novelContentBaseCache.put(novel.getNovelContentId(), novel);
            }

            // 更新统计信息
            totalNovelContentBase = novelContentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说内容基础数据直接加载完成！总数: " + totalNovelContentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载小说内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有小说内容基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<NovelContentBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载小说内容基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有小说内容基础数据
            System.out.println("正在执行数据库查询...");
            List<NovelContentBase_wlq> allNovelContentBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allNovelContentBase.size() + " 条记录");

            // 存储到内存缓存
            for (NovelContentBase_wlq novel : allNovelContentBase) {
                System.out.println("处理小说数据: ID=" + novel.getNovelContentId() + ", 标题=" + novel.getNovelTitle());
                novelContentBaseCache.put(novel.getNovelContentId(), novel);
            }

            // 更新统计信息
            totalNovelContentBase = novelContentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说内容基础数据加载完成！总数: " + totalNovelContentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载小说内容基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有小说内容基础数据（从内存）
     */
    public static List<NovelContentBase_wlq> getAllNovelContentBase() {
        return new ArrayList<>(novelContentBaseCache.values());
    }

    /**
     * 根据小说内容ID获取小说内容基础数据
     */
    public static NovelContentBase_wlq getNovelContentBaseById(Long novelContentId) {
        return novelContentBaseCache.get(novelContentId);
    }

    /**
     * 根据用户ID搜索小说内容基础数据
     */
    public static List<NovelContentBase_wlq> searchNovelContentBaseByUserId(Long userId) {
        List<NovelContentBase_wlq> result = new ArrayList<>();
        for (NovelContentBase_wlq novel : novelContentBaseCache.values()) {
            if (novel.getUserId() != null && novel.getUserId().equals(userId)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据小说标题搜索小说内容基础数据
     */
    public static List<NovelContentBase_wlq> searchNovelContentBaseByNovelTitle(String novelTitle) {
        List<NovelContentBase_wlq> result = new ArrayList<>();
        for (NovelContentBase_wlq novel : novelContentBaseCache.values()) {
            if (novel.getNovelTitle() != null && novel.getNovelTitle().contains(novelTitle)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索小说内容基础数据
     */
    public static List<NovelContentBase_wlq> searchNovelContentBaseByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<NovelContentBase_wlq> result = new ArrayList<>();
        for (NovelContentBase_wlq novel : novelContentBaseCache.values()) {
            if (novel.getLikeCount() != null) {
                if (minLikeCount != null && novel.getLikeCount() < minLikeCount) {
                    continue;
                }
                if (maxLikeCount != null && novel.getLikeCount() > maxLikeCount) {
                    continue;
                }
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据浏览量范围搜索小说内容基础数据
     */
    public static List<NovelContentBase_wlq> searchNovelContentBaseByViewCountRange(Integer minViewCount, Integer maxViewCount) {
        List<NovelContentBase_wlq> result = new ArrayList<>();
        for (NovelContentBase_wlq novel : novelContentBaseCache.values()) {
            if (novel.getViewCount() != null) {
                if (minViewCount != null && novel.getViewCount() < minViewCount) {
                    continue;
                }
                if (maxViewCount != null && novel.getViewCount() > maxViewCount) {
                    continue;
                }
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNovelContentBase", totalNovelContentBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        novelContentBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<NovelContentBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
