package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用收藏列表（内存缓存）
 * 对应实体: Collect
 * 功能：从数据库读取收藏数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CollectList extends EntityList<Collect> {

    // 内存存储（key: collectId，value: 收藏实体）
    private static final Map<Long, Collect> collectCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCollect = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<Collect> allCollect) {
        try {
            System.out.println("开始直接加载收藏数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (Collect collect : allCollect) {
                System.out.println("处理收藏数据: ID=" + collect.getId() + ", 用户ID=" + collect.getUserId());
                collectCache.put(collect.getId(), collect);
            }

            // 更新统计信息
            totalCollect = collectCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("收藏数据直接加载完成！总数: " + totalCollect);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载收藏数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有收藏数据到内存
     */
    public static void loadFromDatabase(BaseMapper<Collect> mapper) {
        try {
            System.out.println("开始从数据库加载收藏数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有收藏数据
            System.out.println("正在执行数据库查询...");
            List<Collect> allCollect = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCollect.size() + " 条记录");

            // 存储到内存缓存
            for (Collect collect : allCollect) {
                System.out.println("处理收藏数据: ID=" + collect.getId() + ", 用户ID=" + collect.getUserId());
                collectCache.put(collect.getId(), collect);
            }

            // 更新统计信息
            totalCollect = collectCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("收藏数据加载完成！总数: " + totalCollect);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载收藏数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有收藏（从内存）
     */
    public static List<Collect> getAllCollect() {
        return new ArrayList<>(collectCache.values());
    }

    /**
     * 根据收藏ID获取收藏
     */
    public static Collect getCollectById(Long collectId) {
        return collectCache.get(collectId);
    }

    /**
     * 根据用户ID+收藏类型查询收藏
     */
    public static List<Collect> getCollectByUserIdAndType(Long userId, Byte collectType) {
        List<Collect> result = new ArrayList<>();
        for (Collect collect : collectCache.values()) {
            if (userId.equals(collect.getUserId()) && collectType.equals(collect.getCollectType())) {
                result.add(collect);
            }
        }
        return result;
    }

    /**
     * 检查用户是否收藏了某个目标
     */
    public static boolean isCollected(Long userId, Byte collectType, Long targetId) {
        for (Collect collect : collectCache.values()) {
            if (userId.equals(collect.getUserId())
                    && collectType.equals(collect.getCollectType())
                    && targetId.equals(collect.getTargetId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCollect", totalCollect);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        collectCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<Collect> mapper) {
        loadFromDatabase(mapper);
    }
}