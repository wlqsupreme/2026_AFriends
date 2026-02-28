package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.util.PrivacySettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 帖子详情页面服务类
 * 负责处理图文和文字帖子详情数据的查询和操作
 */
@Service
public class PostDetailService {

    // 帖子基础Mapper
    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    // 评论Mapper
    @Autowired
    private TextpostCommentMapper textpostCommentMapper;

    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;

    // 内容互动关系Mapper
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;

    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;

    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 评论点赞关系Mapper
    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;

    // 系统消息Mapper
    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;

    // 通知服务
    @Autowired
    private NotificationService notificationService;

    /**
     * 获取帖子详情数据
     * @param postId 帖子ID
     * @param postType 帖子类型：text, image
     * @param userId 当前用户ID
     * @return 帖子详情数据
     */
    public Map<String, Object> getPostDetailData(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 开始获取帖子详情数据 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        long startTime = System.currentTimeMillis();

        try {
            Map<String, Object> postDetail = new HashMap<>();

            if ("动态".equals(postType)) {
                // 获取文字帖子详情
                postDetail = getTextPostDetail(postId, userId);
            } else if ("图片".equals(postType)) {
                // 获取图片帖子详情
                postDetail = getImagePostDetail(postId, userId);
            } else {
                throw new IllegalArgumentException("不支持的帖子类型: " + postType);
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("PostDetailService: 获取帖子详情数据完成，耗时: " + duration + "ms");

            return postDetail;

        } catch (Exception e) {
            System.err.println("PostDetailService: 获取帖子详情数据失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取文字帖子详情
     */
    private Map<String, Object> getTextPostDetail(Long postId, Long userId) {
        System.out.println("PostDetailService: 获取文字帖子详情 - 帖子ID: " + postId);
        UserInfo_njj user = UserInfo_list_njj.getUserById(userId);
        if (user == null) {
            try {
                user = userInfoMapper.selectById(userId);
                if (user != null) {
                    UserInfo_list_njj.updateUserInfoInCache(user);
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 从数据库查询用户信息失败: " + e.getMessage());
            }
        }
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : "未知用户";

        // 查询文字帖子基础信息
        TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
        if (textPost == null) {
            throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
        }

        // 构建帖子详情数据
        Map<String, Object> postDetail = new HashMap<>();
        postDetail.put("id", textPost.getPostId());
        postDetail.put("type", "text");
        postDetail.put("reviewer", username);
        postDetail.put("avatar", "/static/avatar1.png");
        postDetail.put("rating", 5); // 文字帖子默认5星
        postDetail.put("content", textPost.getContentText());
        postDetail.put("time", formatTimeAgo(textPost.getCreatedAt()));
        postDetail.put("likes", textPost.getLikeCount() != null ? textPost.getLikeCount() : 0);
        postDetail.put("dislikes", textPost.getDislikeCount() != null ? textPost.getDislikeCount() : 0);
        postDetail.put("favorites", textPost.getCollectCount() != null ? textPost.getCollectCount() : 0);
        postDetail.put("isLiked", checkUserLikeStatus(userId, postId, (byte) 1));
        postDetail.put("isDisliked", checkUserDislikeStatus(userId, postId, (byte) 1));
        postDetail.put("isFavorited", checkUserFavoriteStatus(userId, postId, (byte) 1));
        postDetail.put("isFollowed", false); // 默认未关注
        postDetail.put("comments", getTextPostComments(postId));

        System.out.println("PostDetailService: 文字帖子详情构建完成");
        return postDetail;
    }

    /**
     * 获取图片帖子详情
     */
    private Map<String, Object> getImagePostDetail(Long postId, Long userId) {
        System.out.println("PostDetailService: 获取图片帖子详情 - 帖子ID: " + postId);
        UserInfo_njj user = UserInfo_list_njj.getUserById(userId);
        if (user == null) {
            try {
                user = userInfoMapper.selectById(userId);
                if (user != null) {
                    UserInfo_list_njj.updateUserInfoInCache(user);
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 从数据库查询用户信息失败: " + e.getMessage());
            }
        }
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : "未知用户";

        // 查询图片帖子基础信息
        ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
        if (imagePost == null) {
            throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
        }

        // 构建帖子详情数据
        Map<String, Object> postDetail = new HashMap<>();
        postDetail.put("id", imagePost.getPostId());
        postDetail.put("type", "image");
        postDetail.put("reviewer", username); // 这里应该查询用户信息
        postDetail.put("avatar", "/static/avatar1.png");
        postDetail.put("rating", 5); // 图片帖子默认5星
        postDetail.put("content", imagePost.getContentText());
        postDetail.put("time", formatTimeAgo(imagePost.getCreatedAt()));
        postDetail.put("likes", imagePost.getLikeCount() != null ? imagePost.getLikeCount() : 0);
        postDetail.put("dislikes", imagePost.getDislikeCount() != null ? imagePost.getDislikeCount() : 0);
        postDetail.put("favorites", imagePost.getCollectCount() != null ? imagePost.getCollectCount() : 0);
        postDetail.put("isLiked", checkUserLikeStatus(userId, postId, (byte) 2));
        postDetail.put("isDisliked", checkUserDislikeStatus(userId, postId, (byte) 2));
        postDetail.put("isFavorited", checkUserFavoriteStatus(userId, postId, (byte) 2));
        postDetail.put("isFollowed", false); // 默认未关注
        postDetail.put("comments", getImagePostComments(postId));

        // 处理图片URLs
        List<String> images = new ArrayList<>();
        if (imagePost.getImageUrls() != null && !imagePost.getImageUrls().isEmpty()) {
            String[] imageArray = imagePost.getImageUrls().split(",");
            for (String imageUrl : imageArray) {
                if (imageUrl.trim().length() > 0) {
                    images.add(imageUrl.trim());
                }
            }
        }
        if (images.isEmpty()) {
            images.add("/static/default-image.png");
        }
        postDetail.put("images", images);

        System.out.println("PostDetailService: 图片帖子详情构建完成");
        return postDetail;
    }

    /**
     * 获取文字帖子评论
     */
    private List<Map<String, Object>> getTextPostComments(Long postId) {
        System.out.println("PostDetailService: 获取文字帖子评论 - 帖子ID: " + postId);


        // 查询评论数据
        List<TextpostComment_wlq> comments = textpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TextpostComment_wlq>()
                        .eq("text_content_id", postId)
                        .eq("is_deleted", 0)
                        .orderByDesc("created_at")
        );

        List<Map<String, Object>> commentList = new ArrayList<>();
        for (TextpostComment_wlq comment : comments) {
            if(comment.getParentCommentId() != null) {
                continue;
            }
            UserInfo_njj user = UserInfo_list_njj.getUserById(comment.getUserId());
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", user.getUsername());
            commentData.put("commentId", comment.getTextpostCommentId());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", getTextPostReplys(postId, comment.getTextpostCommentId()));
            commentList.add(commentData);
        }

        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条文字帖子评论");
        return commentList;
    }

    /**
     * 获取文字帖子评论回复
     */
    private List<Map<String, Object>> getTextPostReplys(Long postId, Long parentCommentId) {
        System.out.println("PostDetailService: 获取文字帖子评论回复 - 帖子ID: " + postId);

        // 查询评论数据
        List<TextpostComment_wlq> comments = textpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TextpostComment_wlq>()
                        .eq("text_content_id", postId)
                        .eq("parent_comment_id", parentCommentId)
                        .eq("is_deleted", 0)
                        .orderByDesc("created_at")
        );

        List<Map<String, Object>> commentList = new ArrayList<>();
        for (TextpostComment_wlq comment : comments) {
            UserInfo_njj user = UserInfo_list_njj.getUserById(comment.getUserId());
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", user.getUsername());
            commentData.put("commentId", comment.getTextpostCommentId());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", getTextPostReplys(postId, comment.getTextpostCommentId()));
            commentList.add(commentData);
        }

        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条文字帖子评论回复");
        return commentList;
    }

    /**
     * 获取图片帖子评论
     */
    private List<Map<String, Object>> getImagePostComments(Long postId) {
        System.out.println("PostDetailService: 获取图片帖子评论 - 帖子ID: " + postId);

        // 查询评论数据
        List<ImagePostComment_wlq> comments = imagePostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImagePostComment_wlq>()
                        .eq("post_id", postId)
                        .eq("is_deleted", 0)
                        .orderByDesc("created_at")
        );

        List<Map<String, Object>> commentList = new ArrayList<>();
        for (ImagePostComment_wlq comment : comments) {
            if(comment.getParentCommentId() != null) {
                continue;
            }
            UserInfo_njj user = UserInfo_list_njj.getUserById(comment.getUserId());
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", user.getUsername());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", getImagePostReply(postId, comment.getParentCommentId())); // 暂时没有回复功能
            commentList.add(commentData);
        }

        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条图片帖子评论");
        return commentList;
    }

    /**
     * 获取图片帖子评论回复
     */
    private List<Map<String, Object>> getImagePostReply(Long postId, Long parentCommentId) {
        System.out.println("PostDetailService: 获取图片帖子评论回复 - 帖子ID: " + postId);

        // 查询评论数据
        List<ImagePostComment_wlq> comments = imagePostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImagePostComment_wlq>()
                        .eq("post_id", postId)
                        .eq("parent_comment_id", parentCommentId)
                        .eq("is_deleted", 0)
                        .orderByDesc("created_at")
        );

        List<Map<String, Object>> commentList = new ArrayList<>();
        for (ImagePostComment_wlq comment : comments) {
            UserInfo_njj user = UserInfo_list_njj.getUserById(comment.getUserId());
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("username", user.getUsername());
            commentData.put("userAvatar", "/static/avatar2.png");
            commentData.put("time", formatTimeAgo(comment.getCreatedAt()));
            commentData.put("content", comment.getCommentText());
            commentData.put("likes", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
            commentData.put("isLiked", false);
            commentData.put("replies", getImagePostReply(postId, comment.getParentCommentId()));
            commentList.add(commentData);
        }

        System.out.println("PostDetailService: 获取到 " + commentList.size() + " 条图片帖子评论回复");
        return commentList;
    }

    /**
     * 检查用户点赞状态
     */
    private boolean checkUserLikeStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentLikeRelation_wlq likeRelation = contentLikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
                            .eq("content_type", contentType)
                            .eq("is_active", 1)
            );
            return likeRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户点赞状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查用户点踩状态
     */
    private boolean checkUserDislikeStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentDislikeRelation_wlq dislikeRelation = contentDislikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
                            .eq("content_type", contentType)
                            .eq("is_active", 1)
            );
            return dislikeRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户点踩状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 检查用户收藏状态
     */
    private boolean checkUserFavoriteStatus(Long userId, Long contentId, Byte contentType) {
        try {
            ContentFavouriteRelation_wlq favoriteRelation = contentFavouriteRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
                            .eq("content_type", contentType)
                            .eq("is_active", 1)
            );
            return favoriteRelation != null;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户收藏状态失败: " + e.getMessage());
            return false;
        }
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
     * 处理点赞操作
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Map<String, Object> handleLike(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理点赞操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);

    try {
        Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
        boolean isLiked;
        int newLikeCount = 0;

        // 关键：查询时加行锁（FOR UPDATE），防止并发插入
        // 使用XML mapper执行带锁的查询，MyBatis-Plus的QueryWrapper不直接支持加锁
        ContentLikeRelation_wlq existingLike = contentLikeRelationMapper.selectWithLock(userId, postId, contentType);

        if (existingLike != null) {
            // 已存在记录，切换状态
            isLiked = existingLike.getIsActive() == 0;
            existingLike.setIsActive(isLiked ? (byte) 1 : (byte) 0);
            existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            contentLikeRelationMapper.updateById(existingLike);
            newLikeCount = updatePostLikeCount(postId, postType, isLiked ? 1 : -1);
        } else {
            // 不存在记录，插入新点赞
            // 先检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
            ContentLikeRelation_wlq existingRecord = contentLikeRelationMapper.selectByUserIdAndContentId(userId, postId);
            
            if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
                // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
                // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入可能会失败
                // 这种情况下，我们仍然尝试插入，如果失败会在 catch 块中处理
            }
            
            ContentLikeRelation_wlq newLike = new ContentLikeRelation_wlq();
            Long newId = generateNewId(contentLikeRelationMapper);
            newLike.setId(newId);
            newLike.setUserId(userId);
            newLike.setContentId(postId);
            newLike.setIsActive((byte) 1);
            newLike.setContentType(contentType);
            newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            contentLikeRelationMapper.insert(newLike);

            isLiked = true;
            newLikeCount = updatePostLikeCount(postId, postType, 1);
        }

        // 构建响应...
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", isLiked ? "点赞成功" : "取消点赞成功");
        response.put("isLiked", isLiked);
        response.put("likes", newLikeCount);
        response.put("timestamp", System.currentTimeMillis());
        return response;

    } catch (DuplicateKeyException e) {
        // 处理唯一索引冲突：检查是否是不同 contentType 导致的
        System.err.println("点赞插入冲突：" + e.getMessage());
        
        // 重新定义 contentType（在 catch 块中）
        Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
        
        // 重新查询，检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
        ContentLikeRelation_wlq existingRecord = contentLikeRelationMapper.selectByUserIdAndContentId(userId, postId);
        
        if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
            // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
            // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入失败
            // 重新查询包含 contentType 的记录，确认是否真的不存在
            ContentLikeRelation_wlq sameTypeRecord = contentLikeRelationMapper.selectWithLock(userId, postId, contentType);
            if (sameTypeRecord == null) {
                // 确实不存在相同 contentType 的记录，说明是唯一索引设计问题
                // 返回错误，提示需要修改数据库唯一索引包含 contentType
                System.err.println("检测到不同 contentType 的记录，但唯一索引冲突。userId=" + userId + ", contentId=" + postId + ", 现有 contentType=" + existingRecord.getContentType() + ", 新 contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "数据库唯一索引限制：已存在相同内容ID但不同内容类型的记录，无法插入新记录。请修改数据库唯一索引包含 content_type 字段。");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            } else {
                // 存在相同 contentType 的记录，说明查询逻辑有问题，重新处理
                boolean isLiked = sameTypeRecord.getIsActive() == 0;
                sameTypeRecord.setIsActive(isLiked ? (byte) 1 : (byte) 0);
                sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentLikeRelationMapper.updateById(sameTypeRecord);
                int newLikeCount = updatePostLikeCount(postId, postType, isLiked ? 1 : -1);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", isLiked ? "点赞成功" : "取消点赞成功");
                response.put("isLiked", isLiked);
                response.put("likes", newLikeCount);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            }
        } else {
            // 相同 contentType 的记录或查询不到记录，说明是真正的重复点赞或并发插入
            // 重新查询包含 contentType 的记录
            ContentLikeRelation_wlq sameTypeRecord = contentLikeRelationMapper.selectWithLock(userId, postId, contentType);
            if (sameTypeRecord != null) {
                // 存在记录，重新处理
                boolean isLiked = sameTypeRecord.getIsActive() == 0;
                sameTypeRecord.setIsActive(isLiked ? (byte) 1 : (byte) 0);
                sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentLikeRelationMapper.updateById(sameTypeRecord);
                int newLikeCount = updatePostLikeCount(postId, postType, isLiked ? 1 : -1);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", isLiked ? "点赞成功" : "取消点赞成功");
                response.put("isLiked", isLiked);
                response.put("likes", newLikeCount);
                response.put("timestamp", System.currentTimeMillis());
                return response;
            } else {
                // 确实不存在记录，返回错误
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "您已点赞过该内容");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }
        }
    } catch (Exception e) {
        // 其他异常处理...
        System.err.println("处理点赞操作失败: " + e.getMessage());
        e.printStackTrace();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "点赞操作失败: " + e.getMessage());
        errorResponse.put("timestamp", System.currentTimeMillis());
        return errorResponse;
    }
}
    /**
     * 更新帖子的点赞数
     * @param postId 帖子ID
     * @param postType 帖子类型（text/image）
     * @param change 变化量（+1 或 -1）
     * @return 更新后的点赞数
     */
    private int updatePostLikeCount(Long postId, String postType, int change) {
        if ("text".equals(postType)) {
            // 更新文字帖子的点赞数
            TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
            if (textPost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
            }
            int newLikeCount = (textPost.getLikeCount() == null ? 0 : textPost.getLikeCount()) + change;
            textPost.setLikeCount(newLikeCount);
            textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis())); // 更新时间戳
            textpostBaseMapper.updateById(textPost);
            return newLikeCount;
        } else if ("image".equals(postType)) {
            // 更新图片帖子的点赞数
            ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
            if (imagePost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
            }
            int newLikeCount = (imagePost.getLikeCount() == null ? 0 : imagePost.getLikeCount()) + change;
            imagePost.setLikeCount(newLikeCount);
            imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis())); // 更新时间戳
            imagePostBaseMapper.updateById(imagePost);
            return newLikeCount;
        } else {
            throw new IllegalArgumentException("不支持的帖子类型: " + postType);
        }
    }

    /**
     * 处理点踩操作（新增点踩数更新逻辑）
     */
    public Map<String, Object> handleDislike(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理点踩操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);

        try {
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            boolean isDisliked;
            int newDislikeCount = 0; // 新增：记录更新后的点踩数

            // 查询是否已有点踩记录
            ContentDislikeRelation_wlq existingDislike = contentDislikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", postId)
                            .eq("content_type", contentType)
            );

            if (existingDislike != null) {
                // 已存在记录，切换状态
                isDisliked = existingDislike.getIsActive() == 0; // 原状态为0（未点踩）则切换为点踩
                existingDislike.setIsActive(isDisliked ? (byte) 1 : (byte) 0);
                existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentDislikeRelationMapper.updateById(existingDislike);

                // 根据状态更新帖子的点踩数（+1 或 -1）
                newDislikeCount = updatePostDislikeCount(postId, postType, isDisliked ? 1 : -1);
            } else {
                // 创建新的点踩记录（首次点踩）
                // 先检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
                ContentDislikeRelation_wlq existingRecord = contentDislikeRelationMapper.selectByUserIdAndContentIdWithoutType(userId, postId);
                
                if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
                    // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
                    // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入可能会失败
                    // 这种情况下，我们仍然尝试插入，如果失败会在 catch 块中处理
                }
                
                ContentDislikeRelation_wlq newDislike = new ContentDislikeRelation_wlq();
                Long newId = generateNewId(contentDislikeRelationMapper);
                newDislike.setId(newId);
                newDislike.setUserId(userId);
                newDislike.setContentId(postId);
                newDislike.setIsActive((byte) 1);
                newDislike.setContentType(contentType);
                newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                contentDislikeRelationMapper.insert(newDislike);
                isDisliked = true;
                // 点踩数 +1
                newDislikeCount = updatePostDislikeCount(postId, postType, 1);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
            response.put("isDisliked", isDisliked);
            response.put("dislikes", newDislikeCount); // 新增：返回最新点踩数
            response.put("timestamp", System.currentTimeMillis());

            return response;

        } catch (DuplicateKeyException e) {
            // 处理唯一索引冲突：检查是否是不同 contentType 导致的
            System.err.println("点踩插入冲突：" + e.getMessage());
            
            // 重新定义 contentType（在 catch 块中）
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            
            // 重新查询，检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
            ContentDislikeRelation_wlq existingRecord = contentDislikeRelationMapper.selectByUserIdAndContentIdWithoutType(userId, postId);
            
            if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
                // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
                // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入失败
                // 重新查询包含 contentType 的记录，确认是否真的不存在
                ContentDislikeRelation_wlq sameTypeRecord = contentDislikeRelationMapper.selectByUserIdAndContentId(userId, postId, contentType);
                if (sameTypeRecord == null) {
                    // 确实不存在相同 contentType 的记录，说明是唯一索引设计问题
                    System.err.println("检测到不同 contentType 的记录，但唯一索引冲突。userId=" + userId + ", contentId=" + postId + ", 现有 contentType=" + existingRecord.getContentType() + ", 新 contentType=" + contentType);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "数据库唯一索引限制：已存在相同内容ID但不同内容类型的记录，无法插入新记录。请修改数据库唯一索引包含 content_type 字段。");
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    return errorResponse;
                } else {
                    // 存在相同 contentType 的记录，重新处理
                    boolean isDisliked = sameTypeRecord.getIsActive() == 0;
                    sameTypeRecord.setIsActive(isDisliked ? (byte) 1 : (byte) 0);
                    sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    contentDislikeRelationMapper.updateById(sameTypeRecord);
                    int newDislikeCount = updatePostDislikeCount(postId, postType, isDisliked ? 1 : -1);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
                    response.put("isDisliked", isDisliked);
                    response.put("dislikes", newDislikeCount);
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                }
            } else {
                // 相同 contentType 的记录或查询不到记录，重新查询包含 contentType 的记录
                ContentDislikeRelation_wlq sameTypeRecord = contentDislikeRelationMapper.selectByUserIdAndContentId(userId, postId, contentType);
                if (sameTypeRecord != null) {
                    // 存在记录，重新处理
                    boolean isDisliked = sameTypeRecord.getIsActive() == 0;
                    sameTypeRecord.setIsActive(isDisliked ? (byte) 1 : (byte) 0);
                    sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    contentDislikeRelationMapper.updateById(sameTypeRecord);
                    int newDislikeCount = updatePostDislikeCount(postId, postType, isDisliked ? 1 : -1);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
                    response.put("isDisliked", isDisliked);
                    response.put("dislikes", newDislikeCount);
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                } else {
                    // 确实不存在记录，返回错误
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "您已点踩过该内容");
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    return errorResponse;
                }
            }
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理点踩操作失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "点踩操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return errorResponse;
        }
    }

    /**
     * 更新帖子的点踩数
     */
    private int updatePostDislikeCount(Long postId, String postType, int change) {
        if ("text".equals(postType)) {
            TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
            if (textPost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
            }
            int newDislikeCount = (textPost.getDislikeCount() == null ? 0 : textPost.getDislikeCount()) + change;
            textPost.setDislikeCount(newDislikeCount);
            textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            textpostBaseMapper.updateById(textPost);
            return newDislikeCount;
        } else if ("image".equals(postType)) {
            ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
            if (imagePost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
            }
            int newDislikeCount = (imagePost.getDislikeCount() == null ? 0 : imagePost.getDislikeCount()) + change;
            imagePost.setDislikeCount(newDislikeCount);
            imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            imagePostBaseMapper.updateById(imagePost);
            return newDislikeCount;
        } else {
            throw new IllegalArgumentException("不支持的帖子类型: " + postType);
        }
    }

    /**
     * 处理收藏操作（完善版本）
     */
    public Map<String, Object> handleFavorite(Long postId, String postType, Long userId) {
        System.out.println("PostDetailService: 处理收藏操作 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);

        try {
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            boolean isFavorited;
            int newFavoriteCount = 0; // 记录更新后的收藏数

            // 查询是否已有收藏记录
            ContentFavouriteRelation_wlq existingFavorite = contentFavouriteRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", postId)
                            .eq("content_type", contentType)
            );

            if (existingFavorite != null) {
                // 已存在记录，切换状态
                isFavorited = existingFavorite.getIsActive() == 0; // 原状态为0（未收藏）则切换为收藏
                existingFavorite.setIsActive(isFavorited ? (byte) 1 : (byte) 0);
                existingFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentFavouriteRelationMapper.updateById(existingFavorite);

                // 根据状态更新帖子的收藏数（+1 或 -1）
                newFavoriteCount = updatePostFavoriteCount(postId, postType, isFavorited ? 1 : -1);
            } else {
                // 创建新的收藏记录
                // 先检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
                ContentFavouriteRelation_wlq existingRecord = contentFavouriteRelationMapper.selectByUserIdAndContentIdWithoutType(userId, postId);
                
                if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
                    // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
                    // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入可能会失败
                    // 这种情况下，我们仍然尝试插入，如果失败会在 catch 块中处理
                }
                
                ContentFavouriteRelation_wlq newFavorite = new ContentFavouriteRelation_wlq();
                Long newId = generateNewId(contentFavouriteRelationMapper);
                newFavorite.setId(newId);
                newFavorite.setUserId(userId);
                newFavorite.setContentId(postId);
                newFavorite.setFolderName("默认收藏夹");
                newFavorite.setIsActive((byte) 1);
                newFavorite.setContentType(contentType);
                newFavorite.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                contentFavouriteRelationMapper.insert(newFavorite);
                isFavorited = true;
                // 收藏数 +1
                newFavoriteCount = updatePostFavoriteCount(postId, postType, 1);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", isFavorited ? "收藏成功" : "取消收藏成功");
            response.put("isFavorited", isFavorited);
            response.put("favorites", newFavoriteCount); // 返回最新收藏数
            response.put("timestamp", System.currentTimeMillis());

            return response;

        } catch (DuplicateKeyException e) {
            // 处理唯一索引冲突：检查是否是不同 contentType 导致的
            System.err.println("收藏插入冲突：" + e.getMessage());
            
            // 重新定义 contentType（在 catch 块中）
            Byte contentType = "text".equals(postType) ? (byte) 1 : (byte) 2;
            
            // 重新查询，检查是否存在相同 userId 和 contentId 但不同 contentType 的记录
            ContentFavouriteRelation_wlq existingRecord = contentFavouriteRelationMapper.selectByUserIdAndContentIdWithoutType(userId, postId);
            
            if (existingRecord != null && !existingRecord.getContentType().equals(contentType)) {
                // 存在不同 contentType 的记录，说明是不同内容类型，应该允许插入
                // 但由于数据库唯一索引可能只包含 userId 和 contentId，插入失败
                // 重新查询包含 contentType 的记录，确认是否真的不存在
                ContentFavouriteRelation_wlq sameTypeRecord = contentFavouriteRelationMapper.selectByUserIdAndContentId(userId, postId, contentType);
                if (sameTypeRecord == null) {
                    // 确实不存在相同 contentType 的记录，说明是唯一索引设计问题
                    System.err.println("检测到不同 contentType 的记录，但唯一索引冲突。userId=" + userId + ", contentId=" + postId + ", 现有 contentType=" + existingRecord.getContentType() + ", 新 contentType=" + contentType);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "数据库唯一索引限制：已存在相同内容ID但不同内容类型的记录，无法插入新记录。请修改数据库唯一索引包含 content_type 字段。");
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    return errorResponse;
                } else {
                    // 存在相同 contentType 的记录，重新处理
                    boolean isFavorited = sameTypeRecord.getIsActive() == 0;
                    sameTypeRecord.setIsActive(isFavorited ? (byte) 1 : (byte) 0);
                    sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    contentFavouriteRelationMapper.updateById(sameTypeRecord);
                    int newFavoriteCount = updatePostFavoriteCount(postId, postType, isFavorited ? 1 : -1);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", isFavorited ? "收藏成功" : "取消收藏成功");
                    response.put("isFavorited", isFavorited);
                    response.put("favorites", newFavoriteCount);
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                }
            } else {
                // 相同 contentType 的记录或查询不到记录，重新查询包含 contentType 的记录
                ContentFavouriteRelation_wlq sameTypeRecord = contentFavouriteRelationMapper.selectByUserIdAndContentId(userId, postId, contentType);
                if (sameTypeRecord != null) {
                    // 存在记录，重新处理
                    boolean isFavorited = sameTypeRecord.getIsActive() == 0;
                    sameTypeRecord.setIsActive(isFavorited ? (byte) 1 : (byte) 0);
                    sameTypeRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                    contentFavouriteRelationMapper.updateById(sameTypeRecord);
                    int newFavoriteCount = updatePostFavoriteCount(postId, postType, isFavorited ? 1 : -1);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", isFavorited ? "收藏成功" : "取消收藏成功");
                    response.put("isFavorited", isFavorited);
                    response.put("favorites", newFavoriteCount);
                    response.put("timestamp", System.currentTimeMillis());
                    return response;
                } else {
                    // 确实不存在记录，返回错误
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "您已收藏过该内容");
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    return errorResponse;
                }
            }
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理收藏操作失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "收藏操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return errorResponse;
        }
    }

    /**
     * 更新帖子的收藏数
     */
    private int updatePostFavoriteCount(Long postId, String postType, int change) {
        if ("text".equals(postType)) {
            TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
            if (textPost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
            }
            int newFavoriteCount = (textPost.getCollectCount() == null ? 0 : textPost.getCollectCount()) + change;
            textPost.setCollectCount(newFavoriteCount);
            textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            textpostBaseMapper.updateById(textPost);
            return newFavoriteCount;
        } else if ("image".equals(postType)) {
            ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
            if (imagePost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
            }
            int newFavoriteCount = (imagePost.getCollectCount() == null ? 0 : imagePost.getCollectCount()) + change;
            imagePost.setCollectCount(newFavoriteCount);
            imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            imagePostBaseMapper.updateById(imagePost);
            return newFavoriteCount;
        } else {
            throw new IllegalArgumentException("不支持的帖子类型: " + postType);
        }
    }

    /**
     * 提交评论
     * @param postId 帖子ID
     * @param postType 帖子类型：text, image
     * @param userId 用户ID
     * @param commentText 评论内容
     * @param parentCommentId 父评论ID，如果为null则表示是评论，否则是回复
     * @return 操作结果
     */
    public Map<String, Object> submitComment(Long postId, String postType, Long userId, String commentText, Long parentCommentId) {
        System.out.println("PostDetailService: 提交评论 - 帖子ID: " + postId + ", 帖子类型: " + postType +
                ", 用户ID: " + userId + ", 评论内容: " + commentText + ", 父评论ID: " + parentCommentId);

        // 验证评论内容
        if (commentText == null || commentText.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论内容不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return errorResponse;
        }

        // 限制评论长度
        if (commentText.length() > 500) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论内容不能超过500个字符");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return errorResponse;
        }

        try {
            // 提交评论并获取新评论ID
            Long newCommentId = null;
            // 获取帖子作者ID
            Long postAuthorId = null;
            if ("text".equals(postType)) {
                TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
                if (textPost != null) {
                    postAuthorId = textPost.getUserId();
                }
            } else if ("image".equals(postType)) {
                ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
                if (imagePost != null) {
                    postAuthorId = imagePost.getUserId();
                }
            }

            // 检查隐私设置：是否允许好友评论
            if (postAuthorId != null && !postAuthorId.equals(userId)) {
                boolean canComment = PrivacySettingsUtil.canFriendComment(postAuthorId, userId);
                if (!canComment) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "该用户不允许非好友评论");
                    errorResponse.put("errorCode", "PRIVACY_COMMENT_DISABLED");
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    System.out.println("PostDetailService: 评论被隐私设置阻止 - 帖子作者ID: " + postAuthorId + ", 评论者ID: " + userId);
                    return errorResponse;
                }
            }

            // 如果 parentCommentId 不为 null，验证父评论是否存在
            if (parentCommentId != null) {
                boolean parentCommentExists = false;
                if ("text".equals(postType)) {
                    // 验证文字帖子的父评论是否存在
                    TextpostComment_wlq parentComment = textpostCommentMapper.selectById(parentCommentId);
                    if (parentComment != null && parentComment.getTextContentId().equals(postId)) {
                        // 父评论存在且属于当前帖子
                        parentCommentExists = true;
                    }
                } else if ("image".equals(postType)) {
                    // 验证图片帖子的父评论是否存在
                    ImagePostComment_wlq parentComment = imagePostCommentMapper.selectById(parentCommentId);
                    if (parentComment != null && parentComment.getPostId().equals(postId)) {
                        // 父评论存在且属于当前帖子
                        parentCommentExists = true;
                    }
                }
                
                if (!parentCommentExists) {
                    System.err.println("PostDetailService: 父评论不存在，parentCommentId=" + parentCommentId + ", postId=" + postId + ", postType=" + postType);
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "父评论不存在或不属于当前帖子");
                    errorResponse.put("parentCommentId", parentCommentId);
                    errorResponse.put("timestamp", System.currentTimeMillis());
                    return errorResponse;
                }
            }

            // 解析评论中的@用户名并发送通知
            processMentions(commentText, userId, postId, postType);

            if ("text".equals(postType)) {
                // 提交文字帖子评论
                TextpostComment_wlq comment = new TextpostComment_wlq();
                newCommentId = generateNewId(textpostCommentMapper);
                comment.setTextpostCommentId(newCommentId);
                comment.setUserId(userId);
                comment.setParentCommentId(parentCommentId);
                comment.setCommentText(commentText);
                comment.setLikeCount(0);
                comment.setIsVisible((byte) 1);
                comment.setStatus((byte) 1);
                comment.setIsDeleted((byte) 0);
                comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setTextContentId(postId);

                textpostCommentMapper.insert(comment);

                // 更新帖子评论数
                updatePostCommentCount(postId, postType, 1);
            } else if ("image".equals(postType)) {
                // 提交图片帖子评论
                ImagePostComment_wlq comment = new ImagePostComment_wlq();
                newCommentId = generateNewId(imagePostCommentMapper);
                comment.setImagepostCommentId(newCommentId);
                comment.setPostId(postId);
                comment.setUserId(userId);
                comment.setParentCommentId(parentCommentId);
                comment.setCommentText(commentText);
                comment.setLikeCount(0);
                comment.setIsVisible((byte) 1);
                comment.setStatus((byte) 1);
                comment.setIsDeleted((byte) 0);
                comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                imagePostCommentMapper.insert(comment);

                // 更新帖子评论数
                updatePostCommentCount(postId, postType, 1);
            }

            // 获取最新评论列表
            List<Map<String, Object>> updatedComments = "text".equals(postType)
                    ? getTextPostComments(postId)
                    : getImagePostComments(postId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", parentCommentId == null ? "评论成功" : "回复成功");
            response.put("commentId", newCommentId);
            response.put("comments", updatedComments); // 返回更新后的评论列表
            response.put("timestamp", System.currentTimeMillis());

            return response;

        } catch (Exception e) {
            System.err.println("PostDetailService: 提交评论失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return errorResponse;
        }
    }

    /**
     * 更新帖子的评论数
     */
    private int updatePostCommentCount(Long postId, String postType, int change) {
        if ("text".equals(postType)) {
            TextpostBase_wlq textPost = textpostBaseMapper.selectById(postId);
            if (textPost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的文字帖子");
            }
            // 假设TextpostBase_wlq有commentCount字段
            int newCommentCount = (textPost.getCommentCount() == null ? 0 : textPost.getCommentCount()) + change;
            textPost.setCommentCount(newCommentCount);
            textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            textpostBaseMapper.updateById(textPost);
            return newCommentCount;
        } else if ("image".equals(postType)) {
            ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(postId);
            if (imagePost == null) {
                throw new RuntimeException("未找到ID为 " + postId + " 的图片帖子");
            }
            // 假设ImagePostBase_wlq有commentCount字段
            int newCommentCount = (imagePost.getCommentCount() == null ? 0 : imagePost.getCommentCount()) + change;
            imagePost.setCommentCount(newCommentCount);
            imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            imagePostBaseMapper.updateById(imagePost);
            return newCommentCount;
        } else {
            throw new IllegalArgumentException("不支持的帖子类型: " + postType);
        }
    }

    /**
     * 解析评论中的@用户名并发送通知
     * @param commentText 评论内容
     * @param authorId 评论作者ID
     * @param postId 帖子ID
     * @param postType 帖子类型
     */
    private void processMentions(String commentText, Long authorId, Long postId, String postType) {
        try {
            // 匹配@用户名模式
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("@([\\w\\u4e00-\\u9fa5]+)");
            java.util.regex.Matcher matcher = pattern.matcher(commentText);

            // 存储被@的用户ID
            java.util.Set<Long> mentionedUserIds = new java.util.HashSet<>();

            while (matcher.find()) {
                String username = matcher.group(1);
                // 根据用户名查找用户
                UserInfo_njj user = UserInfo_list_njj.getUserByUsername(username);
                if (user != null) {
                    // 检查是否是好友且允许被@
                    if (PrivacySettingsUtil.canFriendAt(user.getUserId(), authorId)) {
                        mentionedUserIds.add(user.getUserId());
                    }
                }
            }

            // 为每个被@的用户发送系统消息通知
            for (Long mentionedUserId : mentionedUserIds) {
                sendMentionNotification(mentionedUserId, authorId, postId, postType);
            }

            System.out.println("PostDetailService: 处理了 " + mentionedUserIds.size() + " 个@提及");
        } catch (Exception e) {
            System.err.println("PostDetailService: 处理@提及失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送@提醒通知
     * @param mentionedUserId 被@的用户ID
     * @param authorId 评论作者ID
     * @param postId 帖子ID
     * @param postType 帖子类型
     */
    private void sendMentionNotification(Long mentionedUserId, Long authorId, Long postId, String postType) {
        try {
            // 获取作者信息
            UserInfo_njj author = UserInfo_list_njj.getUserById(authorId);
            String authorName = author != null ? author.getUsername() : "用户" + authorId;

            // 使用通知服务发送通知，确保遵守用户通知设置
            String messageContent = "用户 " + authorName + " 在" + ("text".equals(postType) ? "文字" : "图片") + "帖子中提到了您";
            notificationService.sendNotification(
                    mentionedUserId,
                    authorId,
                    "mention",
                    messageContent,
                    postType,
                    postId
            );

            System.out.println("PostDetailService: 发送@提醒通知给用户 " + mentionedUserId);
        } catch (Exception e) {
            System.err.println("PostDetailService: 发送@提醒通知失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理评论点赞操作
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 操作结果
     */
    public Map<String, Object> handleCommentLike(Long userId, Long commentId) {
        System.out.println("PostDetailService: 处理评论点赞 - 用户ID: " + userId + ", 评论ID: " + commentId);

        try {
            // 检查评论是否存在
            TextpostComment_wlq comment = textpostCommentMapper.selectById(commentId);
            if (comment == null) {
                throw new RuntimeException("未找到ID为 " + commentId + " 的评论");
            }

            // 检查是否已经点赞
            CommentLikeRelation_wlq existingLike = commentLikeRelationMapper.selectByUserIdAndCommentId(userId, commentId);

            if (existingLike != null) {
                // 已存在点赞记录，切换状态
                existingLike.setIsActive(existingLike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                commentLikeRelationMapper.updateById(existingLike);

                System.out.println("PostDetailService: 更新点赞状态 - " + (existingLike.getIsActive() == 1 ? "点赞" : "取消点赞"));
            } else {
                // 创建新的点赞记录
                CommentLikeRelation_wlq newLike = new CommentLikeRelation_wlq();
                newLike.setUserId(userId);
                newLike.setCommentId(commentId);
                newLike.setIsActive((byte) 1);
                newLike.setType((byte) 0); // 0-文字帖子评论类型
                newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                commentLikeRelationMapper.insert(newLike);
                System.out.println("PostDetailService: 创建新点赞记录");
            }

            // 更新评论的点赞数
            updateCommentLikeCount(commentId);

            // 获取更新后的点赞状态
            boolean isLiked = checkUserLikedComment(userId, commentId);
            int likeCount = getCommentLikeCount(commentId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isLiked", isLiked);
            result.put("likeCount", likeCount);
            result.put("message", isLiked ? "点赞成功" : "取消点赞成功");
            result.put("timestamp", System.currentTimeMillis());

            return result;

        } catch (Exception e) {
            System.err.println("PostDetailService: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            return result;
        }
    }

    /**
     * 检查用户是否点赞了评论
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    private boolean checkUserLikedComment(Long userId, Long commentId) {
        try {
            int count = commentLikeRelationMapper.checkUserLikedComment(userId, commentId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("PostDetailService: 检查用户点赞状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取评论的点赞数
     * @param commentId 评论ID
     * @return 点赞数
     */
    private int getCommentLikeCount(Long commentId) {
        try {
            return commentLikeRelationMapper.countActiveLikesByCommentId(commentId);
        } catch (Exception e) {
            System.err.println("PostDetailService: 获取评论点赞数失败: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 更新评论的点赞数
     * @param commentId 评论ID
     */
    private void updateCommentLikeCount(Long commentId) {
        try {
            int likeCount = getCommentLikeCount(commentId);

            // 更新评论表中的点赞数
            TextpostComment_wlq comment = textpostCommentMapper.selectById(commentId);
            if (comment != null) {
                comment.setLikeCount(likeCount);
                comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                textpostCommentMapper.updateById(comment);

                System.out.println("PostDetailService: 更新评论点赞数 - 评论ID: " + commentId + ", 点赞数: " + likeCount);
            }
        } catch (Exception e) {
            System.err.println("PostDetailService: 更新评论点赞数失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 生成从10000010开始的自增ID
     */
    private Long generateNewId(Object mapper) {
        try {
            Long maxId = 10000009L; // 起始值减1

            if (mapper instanceof ContentLikeRelationMapper) {
                List<ContentLikeRelation_wlq> allLikes = contentLikeRelationMapper.selectList(null);
                for (ContentLikeRelation_wlq like : allLikes) {
                    if (like.getId() != null && like.getId() >= 10000010L && like.getId() > maxId) {
                        maxId = like.getId();
                    }
                }
            } else if (mapper instanceof ContentDislikeRelationMapper) {
                List<ContentDislikeRelation_wlq> allDislikes = contentDislikeRelationMapper.selectList(null);
                for (ContentDislikeRelation_wlq dislike : allDislikes) {
                    if (dislike.getId() != null && dislike.getId() >= 10000010L && dislike.getId() > maxId) {
                        maxId = dislike.getId();
                    }
                }
            } else if (mapper instanceof ContentFavouriteRelationMapper) {
                List<ContentFavouriteRelation_wlq> allFavorites = contentFavouriteRelationMapper.selectList(null);
                for (ContentFavouriteRelation_wlq favorite : allFavorites) {
                    if (favorite.getId() != null && favorite.getId() >= 10000010L && favorite.getId() > maxId) {
                        maxId = favorite.getId();
                    }
                }
            } else if (mapper instanceof TextpostCommentMapper) {
                List<TextpostComment_wlq> allComments = textpostCommentMapper.selectList(null);
                for (TextpostComment_wlq comment : allComments) {
                    if (comment.getTextpostCommentId() != null && comment.getTextpostCommentId() >= 10000010L && comment.getTextpostCommentId() > maxId) {
                        maxId = comment.getTextpostCommentId();
                    }
                }
            } else if (mapper instanceof ImagePostCommentMapper) {
                List<ImagePostComment_wlq> allComments = imagePostCommentMapper.selectList(null);
                for (ImagePostComment_wlq comment : allComments) {
                    if (comment.getImagepostCommentId() != null && comment.getImagepostCommentId() >= 10000010L && comment.getImagepostCommentId() > maxId) {
                        maxId = comment.getImagepostCommentId();
                    }
                }
            }

            Long newId = maxId + 1;
            System.out.println("PostDetailService: 生成新ID: " + newId);
            return newId;

        } catch (Exception e) {
            System.err.println("PostDetailService: 生成ID失败，使用默认值: " + e.getMessage());
            return 10000010L;
        }
    }
}
