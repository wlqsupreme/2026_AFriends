package Afriends_v3.service;

import Afriends_v3.entity.ILike_wlq;
import Afriends_v3.entity.IDislike_wlq;
import Afriends_v3.mapper.ILikeMapper;
import Afriends_v3.mapper.IDislikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户喜欢讨厌页面服务类
 * 负责处理用户喜欢讨厌数据的查询和操作
 */
@Service
public class LikesDislikesService {

    @Autowired
    private ILikeMapper iLikeMapper;
    
    @Autowired
    private IDislikeMapper iDislikeMapper;

    /**
     * 获取用户的喜欢讨厌数据
     * @param userId 用户ID
     * @return 整合后的喜欢讨厌数据
     */
    public Map<String, Object> getLikesDislikesData(Long userId) {
        System.out.println("LikesDislikesService: 开始获取用户 " + userId + " 的喜欢讨厌数据...");
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查Mapper是否注入成功
            System.out.println("=== LikesDislikesService: 检查Mapper注入状态 ===");
            System.out.println("iLikeMapper: " + (iLikeMapper != null ? "已注入" : "未注入"));
            System.out.println("iDislikeMapper: " + (iDislikeMapper != null ? "已注入" : "未注入"));
            
            // 测试数据库连接
            System.out.println("=== 测试数据库连接 ===");
            try {
                long totalLikes = iLikeMapper.selectCount(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ILike_wlq>());
                long totalDislikes = iDislikeMapper.selectCount(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IDislike_wlq>());
                System.out.println("数据库连接正常，喜欢表总记录数: " + totalLikes + ", 讨厌表总记录数: " + totalDislikes);
            } catch (Exception e) {
                System.err.println("数据库连接测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 1. 获取用户喜欢数据
            System.out.println("LikesDislikesService: 查询用户喜欢数据...");
            System.out.println("LikesDislikesService: 执行SQL: SELECT * FROM v2_i_like WHERE user_id = " + userId + " AND deleted_at IS NULL ORDER BY created_at DESC");
            List<ILike_wlq> likes = iLikeMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ILike_wlq>()
                    .eq("user_id", userId)
                    .isNull("deleted_at")
                    .orderByDesc("created_at")
            );
            System.out.println("LikesDislikesService: 查询到 " + likes.size() + " 条喜欢数据");
            
            // 处理喜欢数据
            List<Map<String, Object>> likesList = new ArrayList<>();
            for (ILike_wlq like : likes) {
                Map<String, Object> likeItem = createLikeItem(like);
                likesList.add(likeItem);
                System.out.println("LikesDislikesService: 处理喜欢数据 - ID: " + like.getLikeId() + ", 内容: " + like.getLikeText());
            }
            
            // 2. 获取用户讨厌数据
            System.out.println("LikesDislikesService: 查询用户讨厌数据...");
            System.out.println("LikesDislikesService: 执行SQL: SELECT * FROM v2_i_dislike WHERE user_id = " + userId + " AND deleted_at IS NULL ORDER BY created_at DESC");
            List<IDislike_wlq> dislikes = iDislikeMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IDislike_wlq>()
                    .eq("user_id", userId)
                    .isNull("deleted_at")
                    .orderByDesc("created_at")
            );
            System.out.println("LikesDislikesService: 查询到 " + dislikes.size() + " 条讨厌数据");
            
            // 处理讨厌数据
            List<Map<String, Object>> dislikesList = new ArrayList<>();
            for (IDislike_wlq dislike : dislikes) {
                Map<String, Object> dislikeItem = createDislikeItem(dislike);
                dislikesList.add(dislikeItem);
                System.out.println("LikesDislikesService: 处理讨厌数据 - ID: " + dislike.getDislikeId() + ", 内容: " + dislike.getDislikeText());
            }
            
            // 构建返回数据
            result.put("likes", likesList);
            result.put("dislikes", dislikesList);
            result.put("likesCount", likesList.size());
            result.put("dislikesCount", dislikesList.size());
            result.put("totalCount", likesList.size() + dislikesList.size());
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("LikesDislikesService: 用户喜欢讨厌数据获取完成！喜欢 " + likesList.size() + " 条，讨厌 " + dislikesList.size() + " 条，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 获取喜欢讨厌数据失败: " + e.getMessage());
            e.printStackTrace();
            
            // 返回空数据
            result.put("likes", new ArrayList<>());
            result.put("dislikes", new ArrayList<>());
            result.put("likesCount", 0);
            result.put("dislikesCount", 0);
            result.put("totalCount", 0);
        }
        
        return result;
    }
    
    /**
     * 创建喜欢项目数据
     */
    private Map<String, Object> createLikeItem(ILike_wlq like) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", like.getLikeId());
        item.put("text", like.getLikeText());
        item.put("type", like.getLikeType());
        item.put("imageUrls", like.getImageUrls());
        item.put("softTags", like.getSoftTags());
        item.put("hardTags", like.getHardTags());
        item.put("itemType", like.getItemType());
        item.put("itemStatus", like.getItemStatus());
        item.put("likeCount", like.getLikeCount() != null ? like.getLikeCount() : 0);
        item.put("commentCount", like.getCommentCount() != null ? like.getCommentCount() : 0);
        item.put("dislikeCount", like.getDislikeCount() != null ? like.getDislikeCount() : 0);
        item.put("heatScore", like.getHeatScore() != null ? like.getHeatScore() : 0.0);
        item.put("viewCount", like.getViewCount() != null ? like.getViewCount() : 0);
        item.put("createdAt", like.getCreatedAt());
        item.put("updatedAt", like.getUpdatedAt());
        item.put("timeAgo", formatTimeAgo(like.getCreatedAt()));
        return item;
    }
    
    /**
     * 创建讨厌项目数据
     */
    private Map<String, Object> createDislikeItem(IDislike_wlq dislike) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", dislike.getDislikeId());
        item.put("text", dislike.getDislikeText());
        item.put("type", dislike.getDislikeType());
        item.put("imageUrls", dislike.getImageUrls());
        item.put("softTags", dislike.getSoftTags());
        item.put("hardTags", dislike.getHardTags());
        item.put("itemType", dislike.getItemType());
        item.put("itemStatus", dislike.getItemStatus());
        item.put("likeCount", dislike.getLikeCount() != null ? dislike.getLikeCount() : 0);
        item.put("commentCount", dislike.getCommentCount() != null ? dislike.getCommentCount() : 0);
        item.put("dislikeCount", dislike.getDislikeCount() != null ? dislike.getDislikeCount() : 0);
        item.put("heatScore", dislike.getHeatScore() != null ? dislike.getHeatScore() : 0.0);
        item.put("viewCount", dislike.getViewCount() != null ? dislike.getViewCount() : 0);
        item.put("createdAt", dislike.getCreatedAt());
        item.put("updatedAt", dislike.getUpdatedAt());
        item.put("timeAgo", formatTimeAgo(dislike.getCreatedAt()));
        return item;
    }
    
    /**
     * 格式化时间显示
     */
    private String formatTimeAgo(Date date) {
        if (date == null) return "刚刚";
        
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;
        
        long minutes = diff / (1000 * 60);
        long hours = diff / (1000 * 60 * 60);
        long days = diff / (1000 * 60 * 60 * 24);
        
        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        if (hours < 24) return hours + "小时前";
        if (days < 7) return days + "天前";
        return "一周前";
    }
    
    /**
     * 添加喜欢项目
     * @param userId 用户ID
     * @param likeText 喜欢内容
     * @param likeType 喜欢类型
     * @return 操作结果
     */
    public Map<String, Object> addLike(Long userId, String likeText, String likeType) {
        System.out.println("LikesDislikesService: 添加喜欢项目 - 用户ID: " + userId + ", 内容: " + likeText);
        
        try {
            // 创建新的喜欢记录
            ILike_wlq newLike = new ILike_wlq();
            // 生成从10000010开始的自增ID
            Long newId = generateNewLikeId();
            newLike.setLikeId(newId);
            newLike.setUserId(userId);
            newLike.setLikeText(likeText);
            newLike.setLikeType(likeType);
            newLike.setItemType("user_preference");
            newLike.setItemStatus("active");
            newLike.setLikeCount(0);
            newLike.setCommentCount(0);
            newLike.setDislikeCount(0);
            newLike.setHeatScore(0.0);
            newLike.setViewCount(0);
            newLike.setIsView(1);
            newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            
            iLikeMapper.insert(newLike);
            System.out.println("LikesDislikesService: 创建新喜欢记录成功，ID: " + newId);
            return createResponse(true, "添加喜欢成功", newId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 添加喜欢失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "添加喜欢失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 添加讨厌项目
     * @param userId 用户ID
     * @param dislikeText 讨厌内容
     * @param dislikeType 讨厌类型
     * @return 操作结果
     */
    public Map<String, Object> addDislike(Long userId, String dislikeText, String dislikeType) {
        System.out.println("LikesDislikesService: 添加讨厌项目 - 用户ID: " + userId + ", 内容: " + dislikeText);
        
        try {
            // 创建新的讨厌记录
            IDislike_wlq newDislike = new IDislike_wlq();
            // 生成从10000010开始的自增ID
            Long newId = generateNewDislikeId();
            newDislike.setDislikeId(newId);
            newDislike.setUserId(userId);
            newDislike.setDislikeText(dislikeText);
            newDislike.setDislikeType(dislikeType);
            newDislike.setItemType("user_preference");
            newDislike.setItemStatus("active");
            newDislike.setLikeCount(0);
            newDislike.setCommentCount(0);
            newDislike.setDislikeCount(0);
            newDislike.setHeatScore(0.0);
            newDislike.setViewCount(0);
            newDislike.setIsView(1);
            newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            
            iDislikeMapper.insert(newDislike);
            System.out.println("LikesDislikesService: 创建新讨厌记录成功，ID: " + newId);
            return createResponse(true, "添加讨厌成功", newId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 添加讨厌失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "添加讨厌失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 删除喜欢项目
     * @param userId 用户ID
     * @param likeId 喜欢ID
     * @return 操作结果
     */
    public Map<String, Object> deleteLike(Long userId, Long likeId) {
        System.out.println("LikesDislikesService: 删除喜欢项目 - 用户ID: " + userId + ", 喜欢ID: " + likeId);
        
        try {
            // 查询记录是否存在且属于该用户
            ILike_wlq existingLike = iLikeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ILike_wlq>()
                    .eq("like_id", likeId)
                    .eq("user_id", userId)
            );
            
            if (existingLike == null) {
                return createResponse(false, "喜欢项目不存在或无权限删除", null);
            }
            
            // 软删除：设置deleted_at字段
            existingLike.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            iLikeMapper.updateById(existingLike);
            
            System.out.println("LikesDislikesService: 删除喜欢记录成功，ID: " + likeId);
            return createResponse(true, "删除喜欢成功", likeId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 删除喜欢失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "删除喜欢失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 删除讨厌项目
     * @param userId 用户ID
     * @param dislikeId 讨厌ID
     * @return 操作结果
     */
    public Map<String, Object> deleteDislike(Long userId, Long dislikeId) {
        System.out.println("LikesDislikesService: 删除讨厌项目 - 用户ID: " + userId + ", 讨厌ID: " + dislikeId);
        
        try {
            // 查询记录是否存在且属于该用户
            IDislike_wlq existingDislike = iDislikeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IDislike_wlq>()
                    .eq("dislike_id", dislikeId)
                    .eq("user_id", userId)
            );
            
            if (existingDislike == null) {
                return createResponse(false, "讨厌项目不存在或无权限删除", null);
            }
            
            // 软删除：设置deleted_at字段
            existingDislike.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            iDislikeMapper.updateById(existingDislike);
            
            System.out.println("LikesDislikesService: 删除讨厌记录成功，ID: " + dislikeId);
            return createResponse(true, "删除讨厌成功", dislikeId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 删除讨厌失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "删除讨厌失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 编辑喜欢项目
     * @param userId 用户ID
     * @param likeId 喜欢ID
     * @param likeText 新的喜欢内容
     * @param likeType 新的喜欢类型
     * @return 操作结果
     */
    public Map<String, Object> editLike(Long userId, Long likeId, String likeText, String likeType) {
        System.out.println("LikesDislikesService: 编辑喜欢项目 - 用户ID: " + userId + ", 喜欢ID: " + likeId + ", 内容: " + likeText);
        
        try {
            // 查询记录是否存在且属于该用户
            ILike_wlq existingLike = iLikeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ILike_wlq>()
                    .eq("like_id", likeId)
                    .eq("user_id", userId)
            );
            
            if (existingLike == null) {
                return createResponse(false, "喜欢项目不存在或无权限编辑", null);
            }
            
            // 更新内容
            existingLike.setLikeText(likeText);
            existingLike.setLikeType(likeType);
            existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            iLikeMapper.updateById(existingLike);
            
            System.out.println("LikesDislikesService: 编辑喜欢记录成功，ID: " + likeId);
            return createResponse(true, "编辑喜欢成功", likeId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 编辑喜欢失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "编辑喜欢失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 编辑讨厌项目
     * @param userId 用户ID
     * @param dislikeId 讨厌ID
     * @param dislikeText 新的讨厌内容
     * @param dislikeType 新的讨厌类型
     * @return 操作结果
     */
    public Map<String, Object> editDislike(Long userId, Long dislikeId, String dislikeText, String dislikeType) {
        System.out.println("LikesDislikesService: 编辑讨厌项目 - 用户ID: " + userId + ", 讨厌ID: " + dislikeId + ", 内容: " + dislikeText);
        
        try {
            // 查询记录是否存在且属于该用户
            IDislike_wlq existingDislike = iDislikeMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IDislike_wlq>()
                    .eq("dislike_id", dislikeId)
                    .eq("user_id", userId)
            );
            
            if (existingDislike == null) {
                return createResponse(false, "讨厌项目不存在或无权限编辑", null);
            }
            
            // 更新内容
            existingDislike.setDislikeText(dislikeText);
            existingDislike.setDislikeType(dislikeType);
            existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            iDislikeMapper.updateById(existingDislike);
            
            System.out.println("LikesDislikesService: 编辑讨厌记录成功，ID: " + dislikeId);
            return createResponse(true, "编辑讨厌成功", dislikeId);
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 编辑讨厌失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "编辑讨厌失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 创建统一响应格式
     */
    private Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    /**
     * 生成从10000010开始的自增ID（喜欢表）
     */
    private Long generateNewLikeId() {
        try {
            Long maxId = 10000009L; // 起始值减1
            
            // 查询喜欢表的最大ID，只考虑从10000010开始的ID
            List<ILike_wlq> allLikes = iLikeMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ILike_wlq>());
            for (ILike_wlq like : allLikes) {
                if (like.getLikeId() != null && like.getLikeId() >= 10000010L && like.getLikeId() > maxId) {
                    maxId = like.getLikeId();
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("LikesDislikesService: 生成新喜欢ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 生成喜欢ID失败，使用默认值: " + e.getMessage());
            return 10000010L; // 如果出错，返回默认起始值
        }
    }
    
    /**
     * 生成从10000010开始的自增ID（讨厌表）
     */
    private Long generateNewDislikeId() {
        try {
            Long maxId = 10000009L; // 起始值减1
            
            // 查询讨厌表的最大ID，只考虑从10000010开始的ID
            List<IDislike_wlq> allDislikes = iDislikeMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IDislike_wlq>());
            for (IDislike_wlq dislike : allDislikes) {
                if (dislike.getDislikeId() != null && dislike.getDislikeId() >= 10000010L && dislike.getDislikeId() > maxId) {
                    maxId = dislike.getDislikeId();
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("LikesDislikesService: 生成新讨厌ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("LikesDislikesService: 生成讨厌ID失败，使用默认值: " + e.getMessage());
            return 10000010L; // 如果出错，返回默认起始值
        }
    }
}
