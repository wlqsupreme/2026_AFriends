package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小说帖子基础信息列表
 * 对应实体: NovelpostBase_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class NovelpostBase_list_wlq extends EntityList<NovelpostBase_wlq> {

    // 内存存储
    private static final Map<Long, NovelpostBase_wlq> novelpostBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalNovelpostBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<NovelpostBase_wlq> allNovelpostBase) {
        try {
            System.out.println("开始直接加载小说帖子基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (NovelpostBase_wlq novel : allNovelpostBase) {
                System.out.println("处理小说帖子数据: ID=" + novel.getNovelId() + ", 标题=" + novel.getNovelTitle());
                novelpostBaseCache.put(novel.getNovelId(), novel);
            }

            // 更新统计信息
            totalNovelpostBase = novelpostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子基础数据直接加载完成！总数: " + totalNovelpostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载小说帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有小说帖子基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<NovelpostBase_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载小说帖子基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有小说帖子基础数据
            System.out.println("正在执行数据库查询...");
            List<NovelpostBase_wlq> allNovelpostBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allNovelpostBase.size() + " 条记录");

            // 存储到内存缓存
            for (NovelpostBase_wlq novel : allNovelpostBase) {
                System.out.println("处理小说帖子数据: ID=" + novel.getNovelId() + ", 标题=" + novel.getNovelTitle());
                novelpostBaseCache.put(novel.getNovelId(), novel);
            }

            // 更新统计信息
            totalNovelpostBase = novelpostBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说帖子基础数据加载完成！总数: " + totalNovelpostBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载小说帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有小说帖子基础数据（从内存）
     */
    public static List<NovelpostBase_wlq> getAllNovelpostBase() {
        return new ArrayList<>(novelpostBaseCache.values());
    }

    /**
     * 根据小说ID获取小说帖子基础数据
     */
    public static NovelpostBase_wlq getNovelpostBaseById(Long novelId) {
        return novelpostBaseCache.get(novelId);
    }

    /**
     * 根据用户ID搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByUserId(Long userId) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
            if (novel.getUserId() != null && novel.getUserId().equals(userId)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据小说标题搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByNovelTitle(String novelTitle) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
            if (novel.getNovelTitle() != null && novel.getNovelTitle().contains(novelTitle)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据作者ID搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByAuthorId(Long authorId) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
            if (novel.getAuthorId() != null && novel.getAuthorId().equals(authorId)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据作者名称搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByAuthorName(String authorName) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
            if (novel.getAuthorName() != null && novel.getAuthorName().contains(authorName)) {
                result.add(novel);
            }
        }
        return result;
    }

    /**
     * 根据点赞数范围搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByLikeCountRange(Integer minLikeCount, Integer maxLikeCount) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
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
     * 根据浏览量范围搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByViewCountRange(Integer minViewCount, Integer maxViewCount) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
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
     * 根据小说状态搜索小说帖子基础数据
     */
    public static List<NovelpostBase_wlq> searchNovelpostBaseByNovelStatus(Byte novelStatus) {
        List<NovelpostBase_wlq> result = new ArrayList<>();
        for (NovelpostBase_wlq novel : novelpostBaseCache.values()) {
            if (novel.getNovelStatus() != null && novel.getNovelStatus().equals(novelStatus)) {
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
        stats.put("totalNovelpostBase", totalNovelpostBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        novelpostBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<NovelpostBase_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
