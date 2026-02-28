package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户AI需求信息列表
 * 对应实体: UserAiRequire_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserAiRequire_list_wlq extends EntityList<UserAiRequire_wlq> {

    // 内存存储
    private static final Map<Long, UserAiRequire_wlq> userAiRequireCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserAiRequire = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserAiRequire_wlq> allUserAiRequire) {
        try {
            System.out.println("开始直接加载用户AI需求数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserAiRequire_wlq require : allUserAiRequire) {
                System.out.println("处理用户AI需求数据: ID=" + require.getId() + ", 用户ID=" + require.getUserId());
                userAiRequireCache.put(require.getId(), require);
            }

            // 更新统计信息
            totalUserAiRequire = userAiRequireCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI需求数据直接加载完成！总数: " + totalUserAiRequire);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户AI需求数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户AI需求数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserAiRequire_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户AI需求数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户AI需求数据
            System.out.println("正在执行数据库查询...");
            List<UserAiRequire_wlq> allUserAiRequire = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserAiRequire.size() + " 条记录");

            // 存储到内存缓存
            for (UserAiRequire_wlq require : allUserAiRequire) {
                System.out.println("处理用户AI需求数据: ID=" + require.getId() + ", 用户ID=" + require.getUserId());
                userAiRequireCache.put(require.getId(), require);
            }

            // 更新统计信息
            totalUserAiRequire = userAiRequireCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI需求数据加载完成！总数: " + totalUserAiRequire);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户AI需求数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI需求数据（从内存）
     */
    public static List<UserAiRequire_wlq> getAllUserAiRequire() {
        return new ArrayList<>(userAiRequireCache.values());
    }

    /**
     * 根据ID获取用户AI需求数据
     */
    public static UserAiRequire_wlq getUserAiRequireById(Long id) {
        return userAiRequireCache.get(id);
    }

    /**
     * 根据用户ID搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireByUserId(Long userId) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getUserId() != null && require.getUserId().equals(userId)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 根据任务ID搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireByTaskId(Long taskId) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getTaskId() != null && require.getTaskId().equals(taskId)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 根据需求文本搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireByRequireText(String requireText) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getRequireText() != null && require.getRequireText().contains(requireText)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 根据软标签搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireBySoftTags(String softTags) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getSoftTags() != null && require.getSoftTags().contains(softTags)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 根据硬标签搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireByHardTags(String hardTags) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getHardTags() != null && require.getHardTags().contains(hardTags)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 根据特征向量搜索用户AI需求数据
     */
    public static List<UserAiRequire_wlq> searchUserAiRequireByFeatureVector(String featureVector) {
        List<UserAiRequire_wlq> result = new ArrayList<>();
        for (UserAiRequire_wlq require : userAiRequireCache.values()) {
            if (require.getFeatureVector() != null && require.getFeatureVector().contains(featureVector)) {
                result.add(require);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserAiRequire", totalUserAiRequire);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userAiRequireCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserAiRequire_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
