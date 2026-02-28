package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户信息问答信息列表
 * 对应实体: UserInfoQuestion_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserInfoQuestion_list_njj extends EntityList<UserInfoQuestion_njj> {

    // 内存存储
    private static final Map<Long, UserInfoQuestion_njj> userInfoQuestionCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserInfoQuestion = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserInfoQuestion_njj> allUserInfoQuestion) {
        try {
            System.out.println("开始直接加载用户信息问答数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserInfoQuestion == null || allUserInfoQuestion.isEmpty()) {
                System.out.println("用户信息问答数据为空，清空缓存并设置状态");
                totalUserInfoQuestion = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户信息问答数据直接加载完成！总数: " + totalUserInfoQuestion);
                return;
            }

            // 存储到内存缓存
            for (UserInfoQuestion_njj question : allUserInfoQuestion) {
                if (question != null && question.getId() != null) {
                    System.out.println("处理用户信息问答数据: ID=" + question.getId() + ", 问题=" + question.getQuestion());
                    userInfoQuestionCache.put(question.getId(), question);
                }
            }

            // 更新统计信息
            totalUserInfoQuestion = userInfoQuestionCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户信息问答数据直接加载完成！总数: " + totalUserInfoQuestion);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户信息问答数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserInfoQuestion = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户信息问答数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserInfoQuestion_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户信息问答数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户信息问答数据
            System.out.println("正在执行数据库查询...");
            List<UserInfoQuestion_njj> allUserInfoQuestion = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserInfoQuestion.size() + " 条记录");

            // 存储到内存缓存
            for (UserInfoQuestion_njj question : allUserInfoQuestion) {
                if (question != null && question.getId() != null) {
                    System.out.println("处理用户信息问答数据: ID=" + question.getId() + ", 问题=" + question.getQuestion());
                    userInfoQuestionCache.put(question.getId(), question);
                }
            }

            // 更新统计信息
            totalUserInfoQuestion = userInfoQuestionCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户信息问答数据加载完成！总数: " + totalUserInfoQuestion);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户信息问答数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户信息问答数据（从内存）
     */
    public static List<UserInfoQuestion_njj> getAllUserInfoQuestion() {
        return new ArrayList<>(userInfoQuestionCache.values());
    }

    /**
     * 根据ID获取用户信息问答数据
     */
    public static UserInfoQuestion_njj getUserInfoQuestionById(Long id) {
        return userInfoQuestionCache.get(id);
    }

    /**
     * 根据问题内容搜索用户信息问答数据
     */
    public static List<UserInfoQuestion_njj> searchUserInfoQuestionByQuestion(String keyword) {
        List<UserInfoQuestion_njj> result = new ArrayList<>();
        for (UserInfoQuestion_njj question : userInfoQuestionCache.values()) {
            if (question.getQuestion() != null && question.getQuestion().contains(keyword)) {
                result.add(question);
            }
        }
        return result;
    }

    /**
     * 根据软标签搜索用户信息问答数据
     */
    public static List<UserInfoQuestion_njj> searchUserInfoQuestionBySoftTags(String softTag) {
        List<UserInfoQuestion_njj> result = new ArrayList<>();
        for (UserInfoQuestion_njj question : userInfoQuestionCache.values()) {
            if (question.getSoftTags() != null && question.getSoftTags().contains(softTag)) {
                result.add(question);
            }
        }
        return result;
    }

    /**
     * 根据硬标签搜索用户信息问答数据
     */
    public static List<UserInfoQuestion_njj> searchUserInfoQuestionByHardTags(String hardTag) {
        List<UserInfoQuestion_njj> result = new ArrayList<>();
        for (UserInfoQuestion_njj question : userInfoQuestionCache.values()) {
            if (question.getHardTags() != null && question.getHardTags().contains(hardTag)) {
                result.add(question);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserInfoQuestion", totalUserInfoQuestion);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userInfoQuestionCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserInfoQuestion_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
