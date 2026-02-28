package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小说章节信息列表
 * 对应实体: NovelChapterInfo_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class NovelChapterInfo_list_wlq extends EntityList<NovelChapterInfo_wlq> {

    // 内存存储
    private static final Map<Long, NovelChapterInfo_wlq> novelChapterInfoCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalNovelChapterInfo = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<NovelChapterInfo_wlq> allNovelChapterInfo) {
        try {
            System.out.println("开始直接加载小说章节信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (NovelChapterInfo_wlq chapter : allNovelChapterInfo) {
                System.out.println("处理章节数据: ID=" + chapter.getChapterId() + ", 小说ID=" + chapter.getNovelId());
                novelChapterInfoCache.put(chapter.getChapterId(), chapter);
            }

            // 更新统计信息
            totalNovelChapterInfo = novelChapterInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说章节信息数据直接加载完成！总数: " + totalNovelChapterInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载小说章节信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有小说章节信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<NovelChapterInfo_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载小说章节信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有小说章节信息数据
            System.out.println("正在执行数据库查询...");
            List<NovelChapterInfo_wlq> allNovelChapterInfo = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allNovelChapterInfo.size() + " 条记录");

            // 存储到内存缓存
            for (NovelChapterInfo_wlq chapter : allNovelChapterInfo) {
                System.out.println("处理章节数据: ID=" + chapter.getChapterId() + ", 小说ID=" + chapter.getNovelId());
                novelChapterInfoCache.put(chapter.getChapterId(), chapter);
            }

            // 更新统计信息
            totalNovelChapterInfo = novelChapterInfoCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("小说章节信息数据加载完成！总数: " + totalNovelChapterInfo);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载小说章节信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有小说章节信息数据（从内存）
     */
    public static List<NovelChapterInfo_wlq> getAllNovelChapterInfo() {
        return new ArrayList<>(novelChapterInfoCache.values());
    }

    /**
     * 根据章节ID获取小说章节信息数据
     */
    public static NovelChapterInfo_wlq getNovelChapterInfoById(Long chapterId) {
        return novelChapterInfoCache.get(chapterId);
    }

    /**
     * 根据小说ID搜索小说章节信息数据
     */
    public static List<NovelChapterInfo_wlq> searchNovelChapterInfoByNovelId(Long novelId) {
        List<NovelChapterInfo_wlq> result = new ArrayList<>();
        for (NovelChapterInfo_wlq chapter : novelChapterInfoCache.values()) {
            if (chapter.getNovelId() != null && chapter.getNovelId().equals(novelId)) {
                result.add(chapter);
            }
        }
        return result;
    }

    /**
     * 根据章节标题搜索小说章节信息数据
     */
    public static List<NovelChapterInfo_wlq> searchNovelChapterInfoByChapterTitle(String chapterTitle) {
        List<NovelChapterInfo_wlq> result = new ArrayList<>();
        for (NovelChapterInfo_wlq chapter : novelChapterInfoCache.values()) {
            if (chapter.getChapterTitle() != null && chapter.getChapterTitle().contains(chapterTitle)) {
                result.add(chapter);
            }
        }
        return result;
    }

    /**
     * 根据章节索引范围搜索小说章节信息数据
     */
    public static List<NovelChapterInfo_wlq> searchNovelChapterInfoByChapterIndexRange(Long minIndex, Long maxIndex) {
        List<NovelChapterInfo_wlq> result = new ArrayList<>();
        for (NovelChapterInfo_wlq chapter : novelChapterInfoCache.values()) {
            if (chapter.getChapterIndex() != null) {
                if (minIndex != null && chapter.getChapterIndex() < minIndex) {
                    continue;
                }
                if (maxIndex != null && chapter.getChapterIndex() > maxIndex) {
                    continue;
                }
                result.add(chapter);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNovelChapterInfo", totalNovelChapterInfo);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        novelChapterInfoCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<NovelChapterInfo_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
