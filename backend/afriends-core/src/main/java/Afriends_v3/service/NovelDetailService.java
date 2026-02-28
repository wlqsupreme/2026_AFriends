package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.util.PrivacySettingsUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * 小说详情页面服务类
 * 负责处理小说详情数据的查询和整合
 */
@Service
public class NovelDetailService {

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;
    
    @Autowired
    private NovelChapterInfoMapper novelChapterInfoMapper;
    
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;
    
    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;
    
    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;
    
    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;
    
    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;

    @Autowired
    private CommentDislikeRelationMapper commentDislikeRelationMapper;

    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;

    // 通知服务
    @Autowired
    private NotificationService notificationService;


    /**
     * 获取小说详情数据
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 小说详情数据
     */
    public Map<String, Object> getNovelDetail(Long novelId, Long userId) {
        System.out.println("NovelDetailService: 开始获取小说详情 - 小说ID: " + novelId + ", 用户ID: " + userId);
        long startTime = System.currentTimeMillis();
        
        try {
            // 1. 查询小说基本信息
            System.out.println("NovelDetailService: 查询小说基本信息...");
            NovelpostBase_wlq novelBase = novelpostBaseMapper.selectById(novelId);
            
            if (novelBase == null) {
                System.out.println("NovelDetailService: 未找到小说ID为 " + novelId + " 的小说");
                throw new RuntimeException("小说不存在");
            }
            
            System.out.println("NovelDetailService: 找到小说 - 标题: " + novelBase.getNovelTitle() + 
                ", 作者: " + novelBase.getAuthorName() + ", 创建时间: " + novelBase.getCreatedAt());
            
            // 2. 查询用户对该小说的互动状态
            Map<String, Object> userInteraction = getUserInteractionStatus(userId, novelId, (byte) 3); // 3=小说类型
            
            // 3. 构建小说详情数据
            Map<String, Object> novelDetail = buildNovelDetailData(novelBase, userInteraction);
            
            // 4. 获取章节列表（只获取前几章用于预览）
            List<Map<String, Object>> chapters = getNovelChaptersPreview(novelId);
            novelDetail.put("chapters", chapters);
            
            // 5. 获取热门书评（前3条）
            List<Map<String, Object>> reviews = getNovelReviewsPreview(novelId, userId);
            novelDetail.put("reviews", reviews);
            System.out.println("NovelDetailService: 获取到书评数量: " + reviews.size());
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("NovelDetailService: 小说详情获取完成！耗时: " + duration + "ms");
            
            return novelDetail;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说详情失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取小说章节列表
     * @param novelId 小说ID
     * @return 章节列表
     */
    public List<Map<String, Object>> getNovelChapters(Long novelId) {
        System.out.println("NovelDetailService: 开始获取小说章节列表 - 小说ID: " + novelId);
        
        try {
            // 查询所有章节，按章节索引排序
            List<NovelChapterInfo_wlq> chapters = novelChapterInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelChapterInfo_wlq>()
                    .eq("novel_id", novelId)
                    .eq("is_deleted", 0)
                    .orderByAsc("chapter_index")
            );
            
            System.out.println("NovelDetailService: 查询到 " + chapters.size() + " 个章节");
            
            List<Map<String, Object>> chapterList = new ArrayList<>();
            for (NovelChapterInfo_wlq chapter : chapters) {
                Map<String, Object> chapterData = new HashMap<>();
                chapterData.put("chapterId", chapter.getChapterId());
                chapterData.put("chapterIndex", chapter.getChapterIndex());
                chapterData.put("chapterTitle", chapter.getChapterTitle());
                chapterData.put("chapterContent", chapter.getChapterContent());
                chapterData.put("createdAt", chapter.getCreatedAt());
                chapterData.put("updatedAt", chapter.getUpdatedAt());
                
                chapterList.add(chapterData);
            }
            
            return chapterList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说章节列表失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取指定章节内容
     * @param chapterId 章节ID
     * @return 章节内容
     */
    public Map<String, Object> getChapterContent(Long chapterId) {
        System.out.println("NovelDetailService: 开始获取章节内容 - 章节ID: " + chapterId);
        
        try {
            NovelChapterInfo_wlq chapter = novelChapterInfoMapper.selectById(chapterId);
            
            if (chapter == null) {
                System.out.println("NovelDetailService: 未找到章节ID为 " + chapterId + " 的章节");
                throw new RuntimeException("章节不存在");
            }
            
            Map<String, Object> chapterData = new HashMap<>();
            chapterData.put("chapterId", chapter.getChapterId());
            chapterData.put("novelId", chapter.getNovelId());
            chapterData.put("chapterIndex", chapter.getChapterIndex());
            chapterData.put("chapterTitle", chapter.getChapterTitle());
            chapterData.put("chapterContent", chapter.getChapterContent());
            chapterData.put("createdAt", chapter.getCreatedAt());
            chapterData.put("updatedAt", chapter.getUpdatedAt());
            
            System.out.println("NovelDetailService: 成功获取章节内容 - " + chapter.getChapterTitle());
            
            return chapterData;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取章节内容失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取小说章节预览（前几章）
     * @param novelId 小说ID
     * @return 章节预览列表
     */
    private List<Map<String, Object>> getNovelChaptersPreview(Long novelId) {
        try {
            // 只获取前3章用于预览
            List<NovelChapterInfo_wlq> chapters = novelChapterInfoMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelChapterInfo_wlq>()
                    .eq("novel_id", novelId)
                    .eq("is_deleted", 0)
                    .orderByAsc("chapter_index")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> chapterList = new ArrayList<>();
            for (NovelChapterInfo_wlq chapter : chapters) {
                Map<String, Object> chapterData = new HashMap<>();
                chapterData.put("chapterId", chapter.getChapterId());
                chapterData.put("chapterIndex", chapter.getChapterIndex());
                chapterData.put("chapterTitle", chapter.getChapterTitle());
                // 预览时只显示内容的前200个字符
                String content = chapter.getChapterContent();
                if (content != null && content.length() > 200) {
                    content = content.substring(0, 200) + "...";
                }
                chapterData.put("chapterContent", content);
                chapterData.put("createdAt", chapter.getCreatedAt());
                
                chapterList.add(chapterData);
            }
            
            return chapterList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取章节预览失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取用户互动状态
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型
     * @return 互动状态
     */
    private Map<String, Object> getUserInteractionStatus(Long userId, Long contentId, Byte contentType) {
        Map<String, Object> interaction = new HashMap<>();
        
        try {
            // 查询点赞状态
            ContentLikeRelation_wlq likeRelation = contentLikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isLiked", likeRelation != null);
            
            // 查询点踩状态
            ContentDislikeRelation_wlq dislikeRelation = contentDislikeRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isDisliked", dislikeRelation != null);
            
            // 查询收藏状态
            ContentFavouriteRelation_wlq favoriteRelation = contentFavouriteRelationMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                    .eq("user_id", userId)
                    .eq("content_id", contentId)
                    .eq("is_active", 1)
            );
            interaction.put("isFavorited", favoriteRelation != null);
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取用户互动状态失败: " + e.getMessage());
            interaction.put("isLiked", false);
            interaction.put("isDisliked", false);
            interaction.put("isFavorited", false);
        }
        
        return interaction;
    }
    
    /**
     * 构建小说详情数据
     * @param novelBase 小说基础信息
     * @param userInteraction 用户互动状态
     * @return 小说详情数据
     */
    private Map<String, Object> buildNovelDetailData(NovelpostBase_wlq novelBase, Map<String, Object> userInteraction) {
        Map<String, Object> novelDetail = new HashMap<>();
        
        // 基本信息
        novelDetail.put("novelId", novelBase.getNovelId());
        novelDetail.put("title", novelBase.getNovelTitle());
        novelDetail.put("author", novelBase.getAuthorName() != null ? novelBase.getAuthorName() : "未知作者");
        novelDetail.put("authorId", novelBase.getAuthorId());
        novelDetail.put("description", novelBase.getNovelDescription());
        novelDetail.put("coverImage", novelBase.getNovelCoverUrl());
        novelDetail.put("rating", novelBase.getNovelScore() != null ? novelBase.getNovelScore().toString() : "4.5");
        novelDetail.put("status", novelBase.getNovelStatus());
        novelDetail.put("isVisible", novelBase.getIsVisible());
        
        // 统计数据
        novelDetail.put("readingCount", novelBase.getReadingCount() != null ? novelBase.getReadingCount() : 0);
        novelDetail.put("likeCount", novelBase.getLikeCount() != null ? novelBase.getLikeCount() : 0);
        novelDetail.put("dislikeCount", novelBase.getDislikeCount() != null ? novelBase.getDislikeCount() : 0);
        novelDetail.put("commentCount", novelBase.getCommentCount() != null ? novelBase.getCommentCount() : 0);
        novelDetail.put("favoriteCount", novelBase.getFavoriteCount() != null ? novelBase.getFavoriteCount() : 0);
        novelDetail.put("viewCount", novelBase.getViewCount() != null ? novelBase.getViewCount() : 0);
        
        // 格式化显示数据
        novelDetail.put("reviewCount", formatCount(novelBase.getCommentCount() != null ? novelBase.getCommentCount() : 0) + "人点评");
        novelDetail.put("readerCount", formatCount(novelBase.getReadingCount() != null ? novelBase.getReadingCount() : 0) + "人");
        novelDetail.put("wordCount", "12.3万字"); // 可以从章节内容计算，这里先用固定值
        novelDetail.put("updateDays", "连续更新30天"); // 可以根据最后更新时间计算
        
        // 标签处理
        List<String> tags = new ArrayList<>();
        if (novelBase.getSoftTags() != null && !novelBase.getSoftTags().isEmpty()) {
            String[] tagArray = novelBase.getSoftTags().split(",");
            for (String tag : tagArray) {
                if (tag.trim().length() > 0) {
                    tags.add("#" + tag.trim());
                }
            }
        }
        if (tags.isEmpty()) {
            tags.add("#原创小说");
            tags.add("#推荐");
        }
        novelDetail.put("tags", tags);
        
        // 简介处理
        String synopsis = novelBase.getNovelDescription();
        if (synopsis == null || synopsis.isEmpty()) {
            synopsis = "暂无简介";
        } else if (synopsis.length() > 200) {
            synopsis = synopsis.substring(0, 200) + "...";
        }
        novelDetail.put("synopsis", synopsis);
        
        // 用户互动状态
        novelDetail.put("isLiked", userInteraction.get("isLiked"));
        novelDetail.put("isDisliked", userInteraction.get("isDisliked"));
        novelDetail.put("isFavorited", userInteraction.get("isFavorited"));
        
        // 时间信息
        novelDetail.put("createdAt", novelBase.getCreatedAt());
        novelDetail.put("updatedAt", novelBase.getUpdatedAt());
        novelDetail.put("timeAgo", formatTimeAgo(novelBase.getCreatedAt()));
        
        return novelDetail;
    }
    
    /**
     * 格式化数字显示
     */
    private String formatCount(int count) {
        if (count >= 10000) {
            return (count / 10000.0) + "万";
        }
        return String.valueOf(count);
    }
    
    /**
     * 获取小说书评预览（前3条）
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评预览列表
     */
    private List<Map<String, Object>> getNovelReviewsPreview(Long novelId, Long userId) {
        try {
            // 获取前3条顶级评论
            List<NovelpostComment_wlq> comments = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("novel_id", novelId)
                    .isNull("parent_comment_id")
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (NovelpostComment_wlq comment : comments) {
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("reviewer", "书友" + comment.getUserId());
                reviewData.put("avatar", "/static/avatar-default.png");
                reviewData.put("rating", 5); // 默认5星
                reviewData.put("content", comment.getCommentText());
                reviewData.put("time", formatTimeAgo(comment.getCreatedAt()));
                
                // 获取真实的点赞数和点赞状态
                int likeCount = getCommentLikeCount(comment.getNovelpostCommentId());
                boolean isLiked = userId != null ? checkUserLikedComment(userId, comment.getNovelpostCommentId()) : false;

                reviewData.put("likes", likeCount);
                reviewData.put("dislikes", 0);
                reviewData.put("comments", 0);
                reviewData.put("isLiked", isLiked);
                reviewData.put("commentId", comment.getNovelpostCommentId());
                
                // 获取回复
                List<Map<String, Object>> replies = getCommentReplies(comment.getNovelpostCommentId());
                reviewData.put("replies", replies);
                
                reviewList.add(reviewData);
            }
            
            return reviewList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取书评预览失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取评论的回复
     * @param parentCommentId 父评论ID
     * @return 回复列表
     */
    private List<Map<String, Object>> getCommentReplies(Long parentCommentId) {
        try {
            List<NovelpostComment_wlq> replies = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("parent_comment_id", parentCommentId)
                    .eq("is_deleted", 0)
                    .orderByAsc("created_at")
                    .last("LIMIT 3")
            );
            
            List<Map<String, Object>> replyList = new ArrayList<>();
            for (NovelpostComment_wlq reply : replies) {
                Map<String, Object> replyData = new HashMap<>();
                replyData.put("username", "书友" + reply.getUserId());
                replyData.put("userAvatar", "/static/avatar-default.png");
                replyData.put("time", formatTimeAgo(reply.getCreatedAt()));
                replyData.put("content", reply.getCommentText());
                replyData.put("likes", reply.getLikeCount() != null ? reply.getLikeCount() : 0);
                replyData.put("isLiked", false);
                replyData.put("replyTo", "书友" + reply.getUserId()); // 简化处理
                
                replyList.add(replyData);
            }
            
            return replyList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取评论回复失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取单条书评详情
     * @param reviewId 书评ID（即评论ID）
     * @param userId 用户ID
     * @return 书评详情数据
     */
    public Map<String, Object> getReviewDetail(Long reviewId, Long userId) {
        System.out.println("NovelDetailService: 开始获取书评详情 - 书评ID: " + reviewId + ", 用户ID: " + userId);
        
        try {
            // 1. 查询书评基本信息
            NovelpostComment_wlq review = novelpostCommentMapper.selectById(reviewId);
            
            if (review == null || review.getParentCommentId() != null) {
                System.out.println("NovelDetailService: 未找到书评ID为 " + reviewId + " 的书评，或该评论是回复而非顶级书评");
                throw new RuntimeException("书评不存在");
            }
            
            // 2. 获取用户信息
            UserInfo_njj userInfo = UserInfo_list_njj.getUserById(review.getUserId());
            String authorName = userInfo != null ? userInfo.getUsername() : "书友" + review.getUserId();
            String authorAvatar = userInfo != null && userInfo.getProfilePicUrl() != null ? 
                userInfo.getProfilePicUrl() : "/static/avatar-default.png";
            
            // 3. 获取书评的互动状态
            int likeCount = getCommentLikeCount(reviewId);
            boolean isLiked = userId != null ? checkUserLikedComment(userId, reviewId) : false;
            
            // 获取点踩和收藏状态
            int dislikeCount = getContentDislikeCount(reviewId, (byte) 3);
            boolean isDisliked = userId != null ? checkUserDislikedContent(userId, reviewId, (byte) 3) : false;
            
            int favoriteCount = getContentFavoriteCount(reviewId, (byte) 3);
            boolean isFavorited = userId != null ? checkUserFavoritedContent(userId, reviewId, (byte) 3) : false;
            
            // 4. 获取书评的所有评论（回复）
            List<Map<String, Object>> comments = getReviewComments(reviewId, userId);
            
            // 5. 构建书评详情数据
            Map<String, Object> reviewDetail = new HashMap<>();
            reviewDetail.put("id", review.getNovelpostCommentId());
            reviewDetail.put("novelId", review.getNovelId());
            reviewDetail.put("authorId", review.getUserId());
            reviewDetail.put("authorName", authorName);
            reviewDetail.put("authorAvatar", authorAvatar);
            reviewDetail.put("content", review.getCommentText());
            reviewDetail.put("publishTime", formatTimeAgo(review.getCreatedAt()));
            reviewDetail.put("time", formatTimeAgo(review.getCreatedAt()));
            reviewDetail.put("createdAt", review.getCreatedAt());
            reviewDetail.put("rating", 5); // 默认5星，如果有评分字段可以替换
            
            // 互动数据
            reviewDetail.put("likes", likeCount);
            reviewDetail.put("dislikes", dislikeCount);
            reviewDetail.put("favorites", favoriteCount);
            reviewDetail.put("isLiked", isLiked);
            reviewDetail.put("isDisliked", isDisliked);
            reviewDetail.put("isFavorited", isFavorited);
            reviewDetail.put("isFollowed", false); // 可以后续添加关注功能
            
            // 评论列表
            reviewDetail.put("comments", comments);
            
            System.out.println("NovelDetailService: 书评详情获取成功，共 " + comments.size() + " 条评论");
            
            return reviewDetail;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取书评详情失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 获取书评的所有评论（回复）
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 评论列表
     */
    private List<Map<String, Object>> getReviewComments(Long reviewId, Long userId) {
        try {
            List<NovelpostComment_wlq> replies = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("parent_comment_id", reviewId)
                    .eq("is_deleted", 0)
                    .orderByAsc("created_at")
            );
            
            List<Map<String, Object>> commentList = new ArrayList<>();
            for (NovelpostComment_wlq reply : replies) {
                UserInfo_njj userInfo = UserInfo_list_njj.getUserById(reply.getUserId());
                String username = userInfo != null ? userInfo.getUsername() : "书友" + reply.getUserId();
                String userAvatar = userInfo != null && userInfo.getProfilePicUrl() != null ? 
                    userInfo.getProfilePicUrl() : "/static/avatar-default.png";
                
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("commentId", reply.getNovelpostCommentId());
                commentData.put("username", username);
                commentData.put("userAvatar", userAvatar);
                commentData.put("time", formatTimeAgo(reply.getCreatedAt()));
                commentData.put("content", reply.getCommentText());
                commentData.put("likes", reply.getLikeCount() != null ? reply.getLikeCount() : 0);
                commentData.put("isLiked", userId != null ? checkUserLikedComment(userId, reply.getNovelpostCommentId()) : false);
                commentData.put("replies", new ArrayList<>()); // 暂不支持二级回复
                
                commentList.add(commentData);
            }
            
            return commentList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取书评评论失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 处理书评点赞/取消点赞
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> handleReviewLike(Long reviewId, Long userId) {
        System.out.println("NovelDetailService: 处理书评点赞 - 书评ID: " + reviewId + ", 用户ID: " + userId);
        
        try {
            // 书评实际上是评论，所以使用评论点赞的逻辑
            return handleCommentLike(userId, reviewId);
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理书评点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 处理书评点踩/取消点踩
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> handleReviewDislike(Long reviewId, Long userId) {
        System.out.println("NovelDetailService: 处理书评点踩 - 书评ID: " + reviewId + ", 用户ID: " + userId);
        
        try {
            // 检查是否已经点踩
            ContentDislikeRelation_wlq existingDislike = contentDislikeRelationMapper.selectByUserIdAndContentId(userId, reviewId, (byte) 3);
            
            if (existingDislike != null) {
                // 已存在点踩记录，切换状态
                existingDislike.setIsActive(existingDislike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentDislikeRelationMapper.updateById(existingDislike);
                
                System.out.println("NovelDetailService: 更新点踩状态 - " + (existingDislike.getIsActive() == 1 ? "点踩" : "取消点踩"));
            } else {
                // 创建新的点踩记录
                ContentDislikeRelation_wlq newDislike = new ContentDislikeRelation_wlq();
                newDislike.setId(generateNewId());
                newDislike.setUserId(userId);
                newDislike.setContentId(reviewId);
                newDislike.setContentType((byte) 3); // 3-小说书评
                newDislike.setIsActive((byte) 1);
                newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                contentDislikeRelationMapper.insert(newDislike);
                System.out.println("NovelDetailService: 创建新点踩记录");
            }
            
            // 获取更新后的点踩状态和数量
            boolean isDisliked = checkUserDislikedContent(userId, reviewId, (byte) 3);
            int dislikeCount = getContentDislikeCount(reviewId, (byte) 3);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isDisliked", isDisliked);
            result.put("dislikeCount", dislikeCount);
            result.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理书评点踩失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 处理书评收藏/取消收藏
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> handleReviewFavorite(Long reviewId, Long userId) {
        System.out.println("NovelDetailService: 处理书评收藏 - 书评ID: " + reviewId + ", 用户ID: " + userId);
        
        try {
            // 检查是否已经收藏
            ContentFavouriteRelation_wlq existingFavorite = contentFavouriteRelationMapper.selectByUserIdAndContentId(userId, reviewId, (byte) 3);
            
            if (existingFavorite != null) {
                // 已存在收藏记录，切换状态
                existingFavorite.setIsActive(existingFavorite.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentFavouriteRelationMapper.updateById(existingFavorite);
                
                System.out.println("NovelDetailService: 更新收藏状态 - " + (existingFavorite.getIsActive() == 1 ? "收藏" : "取消收藏"));
            } else {
                // 创建新的收藏记录
                ContentFavouriteRelation_wlq newFavorite = new ContentFavouriteRelation_wlq();
                newFavorite.setId(generateNewId());
                newFavorite.setUserId(userId);
                newFavorite.setContentId(reviewId);
                newFavorite.setContentType((byte) 3); // 3-小说书评
                newFavorite.setFolderName("我的收藏");
                newFavorite.setIsActive((byte) 1);
                newFavorite.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                contentFavouriteRelationMapper.insert(newFavorite);
                System.out.println("NovelDetailService: 创建新收藏记录");
            }
            
            // 获取更新后的收藏状态和数量
            boolean isFavorited = checkUserFavoritedContent(userId, reviewId, (byte) 3);
            int favoriteCount = getContentFavoriteCount(reviewId, (byte) 3);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isFavorited", isFavorited);
            result.put("favoriteCount", favoriteCount);
            result.put("message", isFavorited ? "收藏成功" : "取消收藏成功");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理书评收藏失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 获取小说所有书评
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评列表
     */
    public List<Map<String, Object>> getNovelReviews(Long novelId, Long userId) {
        System.out.println("NovelDetailService: 开始获取小说书评 - 小说ID: " + novelId);
        
        try {
            // 1. 先验证小说是否存在
            NovelpostBase_wlq novelBase = novelpostBaseMapper.selectById(novelId);
            if (novelBase == null) {
                System.out.println("NovelDetailService: 未找到小说ID为 " + novelId + " 的小说，返回错误");
                throw new IllegalArgumentException("小说不存在");
            }
            
            // 2. 获取所有顶级评论
            List<NovelpostComment_wlq> comments = novelpostCommentMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostComment_wlq>()
                    .eq("novel_id", novelId)
                    .isNull("parent_comment_id")
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at")
            );
            
            System.out.println("NovelDetailService: 查询到 " + comments.size() + " 条书评");
            
            List<Map<String, Object>> reviewList = new ArrayList<>();
            for (NovelpostComment_wlq comment : comments) {
                Map<String, Object> reviewData = new HashMap<>();
                reviewData.put("reviewer", "书友" + comment.getUserId());
                reviewData.put("avatar", "/static/avatar-default.png");
                reviewData.put("rating", 5); // 默认5星
                reviewData.put("content", comment.getCommentText());
                reviewData.put("time", formatTimeAgo(comment.getCreatedAt()));
                
                // 获取真实的点赞数和点赞状态
                int likeCount = getCommentLikeCount(comment.getNovelpostCommentId());
                boolean isLiked = userId != null ? checkUserLikedComment(userId, comment.getNovelpostCommentId()) : false;
                
                reviewData.put("likes", likeCount);
                reviewData.put("dislikes", 0);
                reviewData.put("comments", 0);
                reviewData.put("isLiked", isLiked);
                reviewData.put("isDisliked", false);
                reviewData.put("commentId", comment.getNovelpostCommentId());
                
                // 获取所有回复
                List<Map<String, Object>> replies = getCommentReplies(comment.getNovelpostCommentId());
                reviewData.put("replies", replies);
                
                reviewList.add(reviewData);
            }
            
            return reviewList;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取小说书评失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 处理评论点赞/取消点赞
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class) // 确保事务一致性
    public Map<String, Object> handleCommentLike(Long userId, Long commentId) {
        System.out.println("处理评论点赞 - 用户ID: " + userId + ", 评论ID: " + commentId);

        //参数校验（防止空值导致SQL错误）
        if (userId == null || commentId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "用户ID和评论ID不能为空");
            return error;
        }

        try {
            //查询已有点赞记录（包含所有状态，不仅是激活的）
            CommentLikeRelation_wlq existingLike = commentLikeRelationMapper.selectByUserIdAndCommentId(userId, commentId);
            boolean isLiked;

            if (existingLike != null) {
                // 3. 切换点赞状态（1=点赞，0=取消）
                isLiked = existingLike.getIsActive() == 0; // 取反当前状态
                existingLike.setIsActive((byte) (isLiked ? 1 : 0));
                existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                // 强制更新所有字段（防止MyBatisPlus自动判断导致更新失败）
                int updateRows = commentLikeRelationMapper.updateById(existingLike);
                if (updateRows <= 0) {
                    throw new RuntimeException("数据库更新点赞记录失败");
                }
            } else {
                // 创建新的点赞记录
                CommentLikeRelation_wlq newLike = new CommentLikeRelation_wlq();
                newLike.setId(generateNewId()); // 手动生成ID
                newLike.setUserId(userId);
                newLike.setCommentId(commentId);
                newLike.setIsActive((byte) 1); // 初始为点赞状态
                newLike.setType((byte) 2); // 2=小说类型（与业务一致）
                newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                int insertRows = commentLikeRelationMapper.insert(newLike);
                if (insertRows <= 0) {
                    throw new RuntimeException("数据库插入点赞记录失败");
                }
                isLiked = true;
            }

            // 5. 刷新点赞数并更新评论表
            int likeCount = commentLikeRelationMapper.countActiveLikesByCommentId(commentId);
            updateCommentLikeCount(commentId, likeCount); // 单独传参避免二次查询

            // 6. 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isLiked", isLiked);
            result.put("likeCount", likeCount);
            result.put("message", isLiked ? "点赞成功" : "取消点赞成功");
            
            return result;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return result;
        }
    }

    // 优化评论点赞数更新方法
    private void updateCommentLikeCount(Long commentId, int likeCount) {
        if (commentId == null) return;

        try {
            NovelpostComment_wlq comment = novelpostCommentMapper.selectById(commentId);
            if (comment == null) {
                throw new RuntimeException("评论不存在，ID: " + commentId);
            }
            comment.setLikeCount(likeCount);
            novelpostCommentMapper.updateById(comment);
            System.out.println("更新评论点赞数 - ID: " + commentId + ", 新点赞数: " + likeCount);
        } catch (Exception e) {
            throw new RuntimeException("更新评论点赞数失败", e); // 抛出异常触发事务回滚
        }
    }

    /**
     * 检查用户是否点赞了评论
     * @param userId 用户ID
     * @param commentId 评论ID
     * @return 是否点赞
     */
    public boolean checkUserLikedComment(Long userId, Long commentId) {
        try {
            int count = commentLikeRelationMapper.checkUserLikedComment(userId, commentId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 检查用户点赞状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取评论的点赞数
     * @param commentId 评论ID
     * @return 点赞数
     */
    public int getCommentLikeCount(Long commentId) {
        try {
            return commentLikeRelationMapper.countActiveLikesByCommentId(commentId);
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取评论点赞数失败: " + e.getMessage());
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
            NovelpostComment_wlq comment = novelpostCommentMapper.selectById(commentId);
            if (comment != null) {
                comment.setLikeCount(likeCount);
                novelpostCommentMapper.updateById(comment);
                
                System.out.println("NovelDetailService: 更新评论点赞数 - 评论ID: " + commentId + ", 点赞数: " + likeCount);
            }
        } catch (Exception e) {
            System.err.println("NovelDetailService: 更新评论点赞数失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户对书评的点赞状态
     * @param userId 用户ID
     * @param reviews 书评列表
     * @return 更新后的书评列表
     */
    public List<Map<String, Object>> updateReviewLikeStatus(Long userId, List<Map<String, Object>> reviews) {
        try {
            for (Map<String, Object> review : reviews) {
                Long commentId = (Long) review.get("commentId");
                if (commentId != null) {
                    boolean isLiked = checkUserLikedComment(userId, commentId);
                    int likeCount = getCommentLikeCount(commentId);
                    
                    review.put("isLiked", isLiked);
                    review.put("likes", likeCount);
                }
            }
            return reviews;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 更新书评点赞状态失败: " + e.getMessage());
            return reviews;
        }
    }

    /**
     * 处理评论点踩/取消点踩
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> handleCommentDislike(Long userId, Long commentId) {
        System.out.println("处理评论点踩 - 用户ID: " + userId + ", 评论ID: " + commentId);

        // 参数校验
        if (userId == null || commentId == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "用户ID和评论ID不能为空");
            return error;
        }

        try {
            // 查询已有点踩记录
            CommentDislikeRelation_zjx existingDislike = commentDislikeRelationMapper
                    .selectByUserIdAndCommentId(userId, commentId);
            boolean isDisliked;

            if (existingDislike != null) {
                // 切换点踩状态
                isDisliked = existingDislike.getIsActive() == 0;
                existingDislike.setIsActive((byte) (isDisliked ? 1 : 0));
                existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                int updateRows = commentDislikeRelationMapper.updateById(existingDislike);
                if (updateRows <= 0) {
                    throw new RuntimeException("数据库更新点踩记录失败");
                }
            } else {
                // 新增点踩记录
                CommentDislikeRelation_zjx newDislike = new CommentDislikeRelation_zjx();
                // 手动生成id（雪花算法，确保唯一）
                newDislike.setId(IdWorker.getId()); // 关键：为id字段赋值

                newDislike.setUserId(userId);
                newDislike.setCommentId(commentId);
                newDislike.setIsActive((byte) 1);
                newDislike.setType((byte) 2); // 小说类型
                newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                int insertRows = commentDislikeRelationMapper.insert(newDislike);
                if (insertRows <= 0) {
                    throw new RuntimeException("数据库插入点踩记录失败");
                }
                isDisliked = true;
            }

            // 更新点踩数
            int dislikeCount = commentDislikeRelationMapper.countActiveDislikesByCommentId(commentId);
            updateCommentDislikeCount(commentId, dislikeCount);

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isDisliked", isDisliked);
            result.put("dislikeCount", dislikeCount);
            result.put("message", isDisliked ? "点踩成功" : "取消点踩成功");
            return result;

        } catch (Exception e) {
            System.err.println("处理评论点踩失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "操作失败: " + e.getMessage());
            return error;
        }
    }

    // 新增点踩数更新方法
    private void updateCommentDislikeCount(Long commentId, int dislikeCount) {
        if (commentId == null) return;

        try {
            NovelpostComment_wlq comment = novelpostCommentMapper.selectById(commentId);
            if (comment == null) {
                throw new RuntimeException("评论不存在，ID: " + commentId);
            }
            comment.setDislikeCount(dislikeCount); // 假设评论表有dislike_count字段
            novelpostCommentMapper.updateById(comment);
            System.out.println("更新评论点踩数 - ID: " + commentId + ", 新点踩数: " + dislikeCount);
        } catch (Exception e) {
            throw new RuntimeException("更新评论点踩数失败", e);
        }
    }

    /**
     * 检查用户是否点踩了评论
     */
    public boolean checkUserDislikedComment(Long userId, Long commentId) {
        try {
            int count = commentDislikeRelationMapper.checkUserDislikedComment(userId, commentId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("检查用户点踩状态失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取评论的点踩数
     */
    public int getCommentDislikeCount(Long commentId) {
        try {
            return commentDislikeRelationMapper.countActiveDislikesByCommentId(commentId);
        } catch (Exception e) {
            System.err.println("获取评论点踩数失败: " + e.getMessage());
            return 0;
        }
    }

    /**
     * 提交小说评论
     * @param novelId 小说ID
     * @param userId 用户ID
     * @param commentText 评论内容
     * @param parentCommentId 父评论ID，如果为null则表示是评论，否则是回复
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitComment(Long novelId, Long userId, String commentText, Long parentCommentId) {
        System.out.println("NovelDetailService: 提交小说评论 - 小说ID: " + novelId + 
            ", 用户ID: " + userId + ", 评论内容: " + commentText + ", 父评论ID: " + parentCommentId);

        if (novelId == null || userId == null || commentText == null || commentText.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "小说ID、用户ID和评论内容不能为空");
            return error;
        }

        try {
            //验证小说是否存在
            NovelpostBase_wlq novel = novelpostBaseMapper.selectById(novelId);
            if (novel == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "小说不存在");
                return error;
            }

            //验证父评论是否存在（如果是回复）
            if (parentCommentId != null) {
                NovelpostComment_wlq parentComment = novelpostCommentMapper.selectById(parentCommentId);
                if (parentComment == null) {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "回复的评论不存在");
                    return error;
                }
            }
            // 解析评论中的@用户名并发送通知
            processMentions(commentText, userId, novelId, "novel");
            
            // 创建小说评论对象
            NovelpostComment_wlq comment = new NovelpostComment_wlq();

            // 手动生成唯一ID（使用雪花算法，确保分布式环境唯一）
            Long newId = IdWorker.getId();
            comment.setNovelpostCommentId(newId); // 关键：为novelpost_comment_id赋值

            comment.setNovelId(novelId);
            comment.setUserId(userId);
            comment.setCommentText(commentText.trim());
            comment.setParentCommentId(parentCommentId);
            comment.setLikeCount(0);
            comment.setIsDeleted((byte) 0);
            comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            
            // 保存到数据库
            int result = novelpostCommentMapper.insert(comment);
            
            if (result > 0) {
                // 同时更新内存缓存
                NovelpostComment_list_wlq.addToCache(comment);

                //更新小说的评论总数
                novel.setCommentCount(novel.getCommentCount() != null ? novel.getCommentCount() + 1 : 1);
                novelpostBaseMapper.updateById(novel);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "评论提交成功");
                response.put("commentId", newId);
                System.out.println("NovelDetailService: 小说评论保存成功: commentId=" + newId + 
                        ", novelId=" + novelId + ", userId=" + userId);
                return response;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "评论提交失败");
                return response;
            }
        } catch (Exception e) {
            System.err.println("NovelDetailService: 提交小说评论失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论提交失败: " + e.getMessage());
            return errorResponse;
        }
    }
    
    /**
     * 解析评论中的@用户名并发送通知
     * @param commentText 评论内容
     * @param authorId 评论作者ID
     * @param novelId 小说ID
     * @param postType 帖子类型
     */
    private void processMentions(String commentText, Long authorId, Long novelId, String postType) {
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
                sendMentionNotification(mentionedUserId, authorId, novelId, postType);
            }
            
            System.out.println("NovelDetailService: 处理了 " + mentionedUserIds.size() + " 个@提及");
        } catch (Exception e) {
            System.err.println("NovelDetailService: 处理@提及失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 发送@提醒通知
     * @param mentionedUserId 被@的用户ID
     * @param authorId 评论作者ID
     * @param novelId 小说ID
     * @param postType 帖子类型
     */
    private void sendMentionNotification(Long mentionedUserId, Long authorId, Long novelId, String postType) {
        try {
            // 获取作者信息
            UserInfo_njj author = UserInfo_list_njj.getUserById(authorId);
            String authorName = author != null ? author.getUsername() : "用户" + authorId;
            
            // 使用通知服务发送通知，确保遵守用户通知设置
            String messageContent = "用户 " + authorName + " 在小说评论中提到了您";
            notificationService.sendNotification(
                mentionedUserId,
                authorId,
                "mention",
                messageContent,
                postType,
                novelId
            );
            
            System.out.println("NovelDetailService: 发送@提醒通知给用户 " + mentionedUserId);
        } catch (Exception e) {
            System.err.println("NovelDetailService: 发送@提醒通知失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 生成从10000010开始的自增ID
     */
    private Long generateNewId() {
        try {
            Long maxId = 10000009L; // 起始值减1
            
            // 查询所有小说评论，找出最大ID
            List<NovelpostComment_wlq> allComments = novelpostCommentMapper.selectList(null);
            for (NovelpostComment_wlq comment : allComments) {
                if (comment.getNovelpostCommentId() != null && 
                    comment.getNovelpostCommentId() >= 10000010L && 
                    comment.getNovelpostCommentId() > maxId) {
                    maxId = comment.getNovelpostCommentId();
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("NovelDetailService: 生成新ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("NovelDetailService: 生成ID失败，使用默认值: " + e.getMessage());
            return 10000010L;
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
     * 检查用户是否点踩了内容
     */
    private boolean checkUserDislikedContent(Long userId, Long contentId, Byte contentType) {
        try {
            ContentDislikeRelation_wlq relation = contentDislikeRelationMapper.selectByUserIdAndContentId(userId, contentId, contentType);
            return relation != null && relation.getIsActive() == 1;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 检查点踩状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取内容的点踩数
     */
    private int getContentDislikeCount(Long contentId, Byte contentType) {
        try {
            return contentDislikeRelationMapper.countActiveDislikesByContentId(contentId, contentType);
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取点踩数失败: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * 检查用户是否收藏了内容
     */
    private boolean checkUserFavoritedContent(Long userId, Long contentId, Byte contentType) {
        try {
            ContentFavouriteRelation_wlq relation = contentFavouriteRelationMapper.selectByUserIdAndContentId(userId, contentId, contentType);
            return relation != null && relation.getIsActive() == 1;
        } catch (Exception e) {
            System.err.println("NovelDetailService: 检查收藏状态失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取内容的收藏数
     */
    private int getContentFavoriteCount(Long contentId, Byte contentType) {
        try {
            return contentFavouriteRelationMapper.countActiveFavoritesByContentId(contentId, contentType);
        } catch (Exception e) {
            System.err.println("NovelDetailService: 获取收藏数失败: " + e.getMessage());
            return 0;
        }
    }
}
