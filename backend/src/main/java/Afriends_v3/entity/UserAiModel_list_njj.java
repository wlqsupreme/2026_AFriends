package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户AI模型列表
 * 对应实体: UserAiModel_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserAiModel_list_njj extends EntityList<UserAiModel_njj> {

    // 内存存储
    private static final Map<Long, UserAiModel_njj> userAiModelCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserAiModel_njj>> userModelCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserAiModel = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserAiModel_njj> allUserAiModel) {
        try {
            System.out.println("开始直接加载用户AI模型数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserAiModel == null || allUserAiModel.isEmpty()) {
                System.out.println("用户AI模型数据为空，清空缓存并设置状态");
                totalUserAiModel = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户AI模型数据直接加载完成！总数: " + totalUserAiModel);
                return;
            }

            // 存储到内存缓存
            for (UserAiModel_njj model : allUserAiModel) {
                if (model != null && model.getUserAiId() != null) {
                    System.out.println("处理用户AI模型数据: ID=" + model.getUserAiId() + ", 用户ID=" + model.getUserId());
                    userAiModelCache.put(model.getUserAiId(), model);

                    // 按用户ID分组存储
                    userModelCache.computeIfAbsent(model.getUserId(), k -> new ArrayList<>()).add(model);
                }
            }

            // 更新统计信息
            totalUserAiModel = userAiModelCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI模型数据直接加载完成！总数: " + totalUserAiModel);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserAiModel = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户AI模型数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserAiModel_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户AI模型数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户AI模型数据
            System.out.println("正在执行数据库查询...");
            List<UserAiModel_njj> allUserAiModel = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserAiModel.size() + " 条记录");

            // 存储到内存缓存
            for (UserAiModel_njj model : allUserAiModel) {
                if (model != null && model.getUserAiId() != null) {
                    System.out.println("处理用户AI模型数据: ID=" + model.getUserAiId() + ", 用户ID=" + model.getUserId());
                    userAiModelCache.put(model.getUserAiId(), model);

                    // 按用户ID分组存储
                    userModelCache.computeIfAbsent(model.getUserId(), k -> new ArrayList<>()).add(model);
                }
            }

            // 更新统计信息
            totalUserAiModel = userAiModelCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI模型数据加载完成！总数: " + totalUserAiModel);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI模型数据（从内存）
     */
    public static List<UserAiModel_njj> getAllUserAiModel() {
        return new ArrayList<>(userAiModelCache.values());
    }

    /**
     * 根据模型ID获取用户AI模型数据
     */
    public static UserAiModel_njj getUserAiModelById(Long userAiId) {
        return userAiModelCache.get(userAiId);
    }

    /**
     * 根据用户ID获取用户AI模型数据
     */
    public static List<UserAiModel_njj> getUserAiModelByUserId(Long userId) {
        return userModelCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据父模型ID获取用户AI模型数据
     */
    public static List<UserAiModel_njj> getUserAiModelByParentModelId(Long parentModelId) {
        List<UserAiModel_njj> result = new ArrayList<>();
        for (UserAiModel_njj model : userAiModelCache.values()) {
            if (model.getParentModelId() != null && model.getParentModelId().equals(parentModelId)) {
                result.add(model);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserAiModel", totalUserAiModel);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userAiModelCache.clear();
        userModelCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserAiModel_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加新的用户AI模型到缓存
     */
    public static void addToCache(UserAiModel_njj userAiModel) {
        if (userAiModel != null && userAiModel.getUserAiId() != null) {
            // 添加到主缓存
            userAiModelCache.put(userAiModel.getUserAiId(), userAiModel);

            // 添加到用户分组缓存
            userModelCache.computeIfAbsent(userAiModel.getUserId(), k -> new ArrayList<>()).add(userAiModel);

            // 更新统计信息
            totalUserAiModel = userAiModelCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户AI模型已添加到缓存，userAiId: " + userAiModel.getUserAiId());
        }
    }

    /**
     * 更新缓存中的AI模型名称
     */
    public static void updateModelNameInCache(Long userAiId, String newName) {
        try {
            UserAiModel_njj model = userAiModelCache.get(userAiId);
            if (model != null) {
                // 更新主缓存中的模型名称
                model.setModelName(newName);
                userAiModelCache.put(userAiId, model);

                // 更新用户分组缓存中的模型名称
                for (List<UserAiModel_njj> userModels : userModelCache.values()) {
                    for (UserAiModel_njj userModel : userModels) {
                        if (userModel.getUserAiId().equals(userAiId)) {
                            userModel.setModelName(newName);
                            break;
                        }
                    }
                }

                lastUpdateTime = System.currentTimeMillis();
                System.out.println("缓存中的AI模型名称已更新，userAiId: " + userAiId + ", newName: " + newName);
            } else {
                System.out.println("缓存中未找到对应的AI模型，userAiId: " + userAiId);
            }
        } catch (Exception e) {
            System.err.println("更新缓存中的AI模型名称失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新缓存中的AI模型等级和经验
     */
    public static void updateLevelAndExpInCache(Long userAiId, Integer level, Integer totalExp) {
        try {
            UserAiModel_njj model = userAiModelCache.get(userAiId);
            if (model != null) {
                // 更新主缓存中的等级和经验
                model.setLevel(level);
                model.setTotalExp(totalExp);
                userAiModelCache.put(userAiId, model);

                // 更新用户分组缓存中的等级和经验
                for (List<UserAiModel_njj> userModels : userModelCache.values()) {
                    for (UserAiModel_njj userModel : userModels) {
                        if (userModel.getUserAiId().equals(userAiId)) {
                            userModel.setLevel(level);
                            userModel.setTotalExp(totalExp);
                            break;
                        }
                    }
                }

                lastUpdateTime = System.currentTimeMillis();
                System.out.println(
                        "缓存中的AI模型等级和经验已更新，userAiId: " + userAiId + ", level: " + level + ", totalExp: " + totalExp);
            } else {
                System.out.println("缓存中未找到对应的AI模型，userAiId: " + userAiId);
            }
        } catch (Exception e) {
            System.err.println("更新缓存中的AI模型等级和经验失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}