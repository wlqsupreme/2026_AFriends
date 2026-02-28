package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户AI响应信息列表
 * 对应实体: UserAiRespond_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserAiRespond_list_wlq extends EntityList<UserAiRespond_wlq> {

    // 内存存储
    private static final Map<Long, UserAiRespond_wlq> userAiRespondCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserAiRespond = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserAiRespond_wlq> allUserAiRespond) {
        try {
            System.out.println("开始直接加载用户AI响应数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (UserAiRespond_wlq respond : allUserAiRespond) {
                System.out.println("处理用户AI响应数据: ID=" + respond.getId() + ", 用户ID=" + respond.getUserId());
                userAiRespondCache.put(respond.getId(), respond);
            }

            // 更新统计信息
            totalUserAiRespond = userAiRespondCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI响应数据直接加载完成！总数: " + totalUserAiRespond);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户AI响应数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有用户AI响应数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserAiRespond_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载用户AI响应数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户AI响应数据
            System.out.println("正在执行数据库查询...");
            List<UserAiRespond_wlq> allUserAiRespond = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserAiRespond.size() + " 条记录");

            // 存储到内存缓存
            for (UserAiRespond_wlq respond : allUserAiRespond) {
                System.out.println("处理用户AI响应数据: ID=" + respond.getId() + ", 用户ID=" + respond.getUserId());
                userAiRespondCache.put(respond.getId(), respond);
            }

            // 更新统计信息
            totalUserAiRespond = userAiRespondCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI响应数据加载完成！总数: " + totalUserAiRespond);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户AI响应数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI响应数据（从内存）
     */
    public static List<UserAiRespond_wlq> getAllUserAiRespond() {
        return new ArrayList<>(userAiRespondCache.values());
    }

    /**
     * 根据ID获取用户AI响应数据
     */
    public static UserAiRespond_wlq getUserAiRespondById(Long id) {
        return userAiRespondCache.get(id);
    }

    /**
     * 根据用户ID搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondByUserId(Long userId) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getUserId() != null && respond.getUserId().equals(userId)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 根据任务ID搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondByTaskId(Long taskId) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getTaskId() != null && respond.getTaskId().equals(taskId)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 根据响应文本搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondByRespondText(String respondText) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getRespondText() != null && respond.getRespondText().contains(respondText)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 根据软标签搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondBySoftTags(String softTags) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getSoftTags() != null && respond.getSoftTags().contains(softTags)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 根据硬标签搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondByHardTags(String hardTags) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getHardTags() != null && respond.getHardTags().contains(hardTags)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 根据特征向量搜索用户AI响应数据
     */
    public static List<UserAiRespond_wlq> searchUserAiRespondByFeatureVector(String featureVector) {
        List<UserAiRespond_wlq> result = new ArrayList<>();
        for (UserAiRespond_wlq respond : userAiRespondCache.values()) {
            if (respond.getFeatureVector() != null && respond.getFeatureVector().contains(featureVector)) {
                result.add(respond);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserAiRespond", totalUserAiRespond);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userAiRespondCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserAiRespond_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
