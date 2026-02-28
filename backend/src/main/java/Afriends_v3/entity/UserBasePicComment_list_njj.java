package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户与图片评论关系信息列表
 * 对应实体: UserBasePicComment_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserBasePicComment_list_njj extends EntityList<UserBasePicComment_njj> {

    // 内存存储
    private static final Map<Long, UserBasePicComment_njj> userBasePicCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBasePicComment_njj>> userPicCommentCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserBasePicComment_njj>> commentUserCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserBasePicComment = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserBasePicComment_njj> allUserBasePicComment) {
        try {
            System.out.println("开始直接加载用户与图片评论关系数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserBasePicComment == null || allUserBasePicComment.isEmpty()) {
                System.out.println("用户与图片评论关系数据为空，清空缓存并设置状态");
                totalUserBasePicComment = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户与图片评论关系数据直接加载完成！总数: " + totalUserBasePicComment);
                return;
            }

            // 存储到内存缓存
            for (UserBasePicComment_njj relation : allUserBasePicComment) {
                if (relation != null && relation.getRelId() != null) {
                    System.out.println("处理用户与图片评论关系数据: ID=" + relation.getRelId() + ", 用户ID=" + relation.getUserId());
                    userBasePicCommentCache.put(relation.getRelId(), relation);

                    // 按用户ID分组存储
                    userPicCommentCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按评论ID分组存储
                    commentUserCache.computeIfAbsent(relation.getCommentId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserBasePicComment = userBasePicCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户与图片评论关系数据直接加载完成！总数: " + totalUserBasePicComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户与图片评论关系数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserBasePicComment = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户与图片评论关系数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserBasePicComment_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户与图片评论关系数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户与图片评论关系数据
            System.out.println("正在执行数据库查询...");
            List<UserBasePicComment_njj> allUserBasePicComment = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserBasePicComment.size() + " 条记录");

            // 存储到内存缓存
            for (UserBasePicComment_njj relation : allUserBasePicComment) {
                if (relation != null && relation.getRelId() != null) {
                    System.out.println("处理用户与图片评论关系数据: ID=" + relation.getRelId() + ", 用户ID=" + relation.getUserId());
                    userBasePicCommentCache.put(relation.getRelId(), relation);

                    // 按用户ID分组存储
                    userPicCommentCache.computeIfAbsent(relation.getUserId(), k -> new ArrayList<>()).add(relation);

                    // 按评论ID分组存储
                    commentUserCache.computeIfAbsent(relation.getCommentId(), k -> new ArrayList<>()).add(relation);
                }
            }

            // 更新统计信息
            totalUserBasePicComment = userBasePicCommentCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户与图片评论关系数据加载完成！总数: " + totalUserBasePicComment);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户与图片评论关系数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户与图片评论关系数据（从内存）
     */
    public static List<UserBasePicComment_njj> getAllUserBasePicComment() {
        return new ArrayList<>(userBasePicCommentCache.values());
    }

    /**
     * 根据关系ID获取用户与图片评论关系数据
     */
    public static UserBasePicComment_njj getUserBasePicCommentById(Long relId) {
        return userBasePicCommentCache.get(relId);
    }

    /**
     * 根据用户ID获取用户与图片评论关系数据
     */
    public static List<UserBasePicComment_njj> getUserBasePicCommentByUserId(Long userId) {
        return userPicCommentCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据评论ID获取用户与图片评论关系数据
     */
    public static List<UserBasePicComment_njj> getUserBasePicCommentByCommentId(Long commentId) {
        return commentUserCache.getOrDefault(commentId, new ArrayList<>());
    }

    /**
     * 根据删除状态获取用户与图片评论关系数据
     */
    public static List<UserBasePicComment_njj> getUserBasePicCommentByIsDeleted(Integer isDeleted) {
        List<UserBasePicComment_njj> result = new ArrayList<>();
        for (UserBasePicComment_njj relation : userBasePicCommentCache.values()) {
            if (relation.getIsDeleted() != null && relation.getIsDeleted().equals(isDeleted)) {
                result.add(relation);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserBasePicComment", totalUserBasePicComment);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userBasePicCommentCache.clear();
        userPicCommentCache.clear();
        commentUserCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserBasePicComment_njj> mapper) {
        loadFromDatabase(mapper);
    }
}
