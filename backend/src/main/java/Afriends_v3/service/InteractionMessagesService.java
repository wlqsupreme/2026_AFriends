package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 互动消息页面服务类
 * 负责处理各种互动消息数据的查询和整合
 */
@Service
public class InteractionMessagesService {

    // 内容互动关系Mapper
    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;
    
    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;
    
    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;
    
    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;
    
    // 各种评论Mapper
    @Autowired
    private TextpostCommentMapper textpostCommentMapper;
    
    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;
    
    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;
    
    @Autowired
    private ILikeCommentMapper iLikeCommentMapper;
    
    @Autowired
    private IDislikeCommentMapper iDislikeCommentMapper;
    
    @Autowired
    private IHaveCommentMapper iHaveCommentMapper;
    
    @Autowired
    private INeedCommentMapper iNeedCommentMapper;
    
    // 内容基础Mapper
    @Autowired
    private TextpostBaseMapper textpostBaseMapper;
    
    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;
    
    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;
    
    // 用户信息Mapper
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;

    private static final String INTERACTION_MESSAGES_READ_MARKER_TYPE = "INTERACTION_MESSAGES_READ_MARKER";

    /**
     * 获取用户的互动消息数据
     * @param userId 用户ID
     * @param filterType 筛选类型：all, likes, received-comments, sent-comments
     * @return 整合后的互动消息数据列表
     */
    public List<Map<String, Object>> getInteractionMessagesData(Long userId, String filterType) {
        System.out.println("InteractionMessagesService: 开始获取用户 " + userId + " 的互动消息数据，筛选类型: " + filterType);
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> messagesData = new ArrayList<>();
        
        try {
            long interactionMessagesLastReadAtMillis = getOrInitInteractionMessagesLastReadAtMillis(userId);

            // 检查Mapper是否注入成功
            System.out.println("=== InteractionMessagesService: 检查Mapper注入状态 ===");
            System.out.println("contentLikeRelationMapper: " + (contentLikeRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("contentDislikeRelationMapper: " + (contentDislikeRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("contentFavouriteRelationMapper: " + (contentFavouriteRelationMapper != null ? "已注入" : "未注入"));
            System.out.println("commentLikeRelationMapper: " + (commentLikeRelationMapper != null ? "已注入" : "未注入"));
            
            // 根据筛选类型获取不同的数据
            switch (filterType) {
                case "all":
                    // 获取所有类型的互动消息
                    messagesData.addAll(getLikesAndFavorites(userId, interactionMessagesLastReadAtMillis));
                    messagesData.addAll(getReceivedComments(userId, interactionMessagesLastReadAtMillis));
                    messagesData.addAll(getSentComments(userId, interactionMessagesLastReadAtMillis));
                    break;
                case "likes":
                    // 只获取赞与收藏
                    messagesData.addAll(getLikesAndFavorites(userId, interactionMessagesLastReadAtMillis));
                    break;
                case "received-comments":
                    // 只获取收到的评论
                    messagesData.addAll(getReceivedComments(userId, interactionMessagesLastReadAtMillis));
                    break;
                case "sent-comments":
                    // 只获取发出的评论
                    messagesData.addAll(getSentComments(userId, interactionMessagesLastReadAtMillis));
                    break;
                default:
                    // 默认获取所有数据
                    messagesData.addAll(getLikesAndFavorites(userId, interactionMessagesLastReadAtMillis));
                    messagesData.addAll(getReceivedComments(userId, interactionMessagesLastReadAtMillis));
                    messagesData.addAll(getSentComments(userId, interactionMessagesLastReadAtMillis));
                    break;
            }
            
            // 按时间排序（最新的在前）
            messagesData.sort((a, b) -> {
                Date timeA = (Date) a.get("createdAt");
                Date timeB = (Date) b.get("createdAt");
                if (timeA == null || timeB == null) return 0;
                return timeB.compareTo(timeA);
            });
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("InteractionMessagesService: 用户互动消息数据获取完成！总共 " + messagesData.size() + " 条数据，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取互动消息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messagesData;
    }
    
    private long getOrInitInteractionMessagesLastReadAtMillis(Long userId) {
        long now = System.currentTimeMillis();
        if (userId == null) {
            return now;
        }

        try {
            QueryWrapper<UserSystemMessage_njj> wrapper = new QueryWrapper<>();
            wrapper.eq("recipient_user_id", userId)
                    .eq("related_entity_type", INTERACTION_MESSAGES_READ_MARKER_TYPE)
                    .orderByDesc("created_at")
                    .last("LIMIT 1");

            List<UserSystemMessage_njj> markers = userSystemMessageMapper.selectList(wrapper);
            if (markers != null && !markers.isEmpty()) {
                UserSystemMessage_njj marker = markers.get(0);
                if (marker.getCreatedAt() != null) {
                    return marker.getCreatedAt().getTime();
                }
            }

            createInteractionMessagesReadMarker(userId, now);
            return now;
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取/初始化互动消息已读水位线失败: " + e.getMessage());
            e.printStackTrace();
            return now;
        }
    }

    private void createInteractionMessagesReadMarker(Long userId, long timestampMillis) {
        UserSystemMessage_njj marker = new UserSystemMessage_njj();
        long base = System.currentTimeMillis();
        marker.setMessageId(base * 1000 + java.util.concurrent.ThreadLocalRandom.current().nextInt(1000));
        marker.setRecipientUserId(userId);
        marker.setSenderUserId(null);
        marker.setMessageContent(INTERACTION_MESSAGES_READ_MARKER_TYPE);
        marker.setIsRead((byte) 1);
        marker.setRelatedEntityType(INTERACTION_MESSAGES_READ_MARKER_TYPE);
        marker.setRelatedEntityId(timestampMillis);
        marker.setCreatedAt(new Timestamp(timestampMillis));
        userSystemMessageMapper.insert(marker);
    }

    private boolean isReadByMarker(Timestamp createdAt, long lastReadAtMillis) {
        if (createdAt == null) {
            return true;
        }
        return createdAt.getTime() <= lastReadAtMillis;
    }
    
    /**
     * 获取点赞和收藏消息
     * 查询用户发布的内容被点赞和收藏的记录
     */
    private List<Map<String, Object>> getLikesAndFavorites(Long userId, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            System.out.println("InteractionMessagesService: 开始查询用户 " + userId + " 的点赞和收藏消息");
            
            // 1. 查询用户发布的内容被点赞的记录
            // 先查询用户发布的所有内容ID（按类型分组）
            List<Long> textPostIds = getTextPostIdsByUserId(userId);
            List<Long> imagePostIds = getImagePostIdsByUserId(userId);
            List<Long> novelPostIds = getNovelPostIdsByUserId(userId);
            
            // 查询这些内容被点赞的记录
            if (!textPostIds.isEmpty()) {
                QueryWrapper<ContentLikeRelation_wlq> textLikeWrapper = new QueryWrapper<>();
                textLikeWrapper.in("content_id", textPostIds)
                        .eq("content_type", 1) // 1=文字
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentLikeRelation_wlq> textLikes = contentLikeRelationMapper.selectList(textLikeWrapper);
                messages.addAll(convertLikesToMessages(textLikes, 1, userId, interactionMessagesLastReadAtMillis));
            }
            
            if (!imagePostIds.isEmpty()) {
                QueryWrapper<ContentLikeRelation_wlq> imageLikeWrapper = new QueryWrapper<>();
                imageLikeWrapper.in("content_id", imagePostIds)
                        .eq("content_type", 2) // 2=图片
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentLikeRelation_wlq> imageLikes = contentLikeRelationMapper.selectList(imageLikeWrapper);
                messages.addAll(convertLikesToMessages(imageLikes, 2, userId, interactionMessagesLastReadAtMillis));
            }
            
            if (!novelPostIds.isEmpty()) {
                QueryWrapper<ContentLikeRelation_wlq> novelLikeWrapper = new QueryWrapper<>();
                novelLikeWrapper.in("content_id", novelPostIds)
                        .eq("content_type", 3) // 3=小说
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentLikeRelation_wlq> novelLikes = contentLikeRelationMapper.selectList(novelLikeWrapper);
                messages.addAll(convertLikesToMessages(novelLikes, 3, userId, interactionMessagesLastReadAtMillis));
            }
            
            // 2. 查询用户发布的内容被收藏的记录
            if (!textPostIds.isEmpty()) {
                QueryWrapper<ContentFavouriteRelation_wlq> textFavWrapper = new QueryWrapper<>();
                textFavWrapper.in("content_id", textPostIds)
                        .eq("content_type", 1)
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentFavouriteRelation_wlq> textFavs = contentFavouriteRelationMapper.selectList(textFavWrapper);
                messages.addAll(convertFavoritesToMessages(textFavs, 1, userId, interactionMessagesLastReadAtMillis));
            }
            
            if (!imagePostIds.isEmpty()) {
                QueryWrapper<ContentFavouriteRelation_wlq> imageFavWrapper = new QueryWrapper<>();
                imageFavWrapper.in("content_id", imagePostIds)
                        .eq("content_type", 2)
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentFavouriteRelation_wlq> imageFavs = contentFavouriteRelationMapper.selectList(imageFavWrapper);
                messages.addAll(convertFavoritesToMessages(imageFavs, 2, userId, interactionMessagesLastReadAtMillis));
            }
            
            if (!novelPostIds.isEmpty()) {
                QueryWrapper<ContentFavouriteRelation_wlq> novelFavWrapper = new QueryWrapper<>();
                novelFavWrapper.in("content_id", novelPostIds)
                        .eq("content_type", 3)
                        .eq("is_active", 1)
                        .orderByDesc("created_at");
                List<ContentFavouriteRelation_wlq> novelFavs = contentFavouriteRelationMapper.selectList(novelFavWrapper);
                messages.addAll(convertFavoritesToMessages(novelFavs, 3, userId, interactionMessagesLastReadAtMillis));
            }
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条点赞和收藏消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取点赞和收藏消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 将点赞记录转换为消息格式
     */
    private List<Map<String, Object>> convertLikesToMessages(List<ContentLikeRelation_wlq> likes, int contentType, Long contentOwnerId, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (ContentLikeRelation_wlq like : likes) {
            try {
                // 优先从缓存获取用户信息
                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(like.getUserId());
                if (fromUser == null) {
                    // 缓存未命中，从数据库查询
                    fromUser = userInfoMapper.selectById(like.getUserId());
                    if (fromUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(fromUser);
                    }
                }
                if (fromUser == null) continue;
                
                // 获取内容信息
                Map<String, Object> contentInfo = getContentInfo(like.getContentId(), contentType);
                if (contentInfo == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", like.getId());
                message.put("type", "like");
                message.put("actionType", "like");
                message.put("fromUserId", like.getUserId());
                message.put("fromUsername", fromUser.getUsername() != null ? fromUser.getUsername() : "用户" + like.getUserId());
                message.put("fromUserAvatar", fromUser.getProfilePicUrl() != null ? fromUser.getProfilePicUrl() : "");
                
                // 根据内容类型设置
                String contentTypeStr = contentType == 1 ? "text" : (contentType == 2 ? "image" : "novel");
                message.put("contentType", contentTypeStr);
                message.put("contentId", like.getContentId());
                message.put("contentTitle", contentInfo.get("title"));
                message.put("contentThumbnail", contentInfo.get("thumbnail"));
                
                // 生成消息文本
                String messageText = generateLikeMessageText(contentTypeStr, fromUser.getUsername());
                message.put("messageText", messageText);
                
                // 时间信息
                Timestamp createdAt = like.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换点赞消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 将收藏记录转换为消息格式
     */
    private List<Map<String, Object>> convertFavoritesToMessages(List<ContentFavouriteRelation_wlq> favorites, int contentType, Long contentOwnerId, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (ContentFavouriteRelation_wlq favorite : favorites) {
            try {
                // 优先从缓存获取用户信息
                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(favorite.getUserId());
                if (fromUser == null) {
                    // 缓存未命中，从数据库查询
                    fromUser = userInfoMapper.selectById(favorite.getUserId());
                    if (fromUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(fromUser);
                    }
                }
                if (fromUser == null) continue;
                
                // 获取内容信息
                Map<String, Object> contentInfo = getContentInfo(favorite.getContentId(), contentType);
                if (contentInfo == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", favorite.getId());
                message.put("type", "favorite");
                message.put("actionType", "favorite");
                message.put("fromUserId", favorite.getUserId());
                message.put("fromUsername", fromUser.getUsername() != null ? fromUser.getUsername() : "用户" + favorite.getUserId());
                message.put("fromUserAvatar", fromUser.getProfilePicUrl() != null ? fromUser.getProfilePicUrl() : "");
                
                // 根据内容类型设置
                String contentTypeStr = contentType == 1 ? "text" : (contentType == 2 ? "image" : "novel");
                message.put("contentType", contentTypeStr);
                message.put("contentId", favorite.getContentId());
                message.put("contentTitle", contentInfo.get("title"));
                message.put("contentThumbnail", contentInfo.get("thumbnail"));
                
                // 生成消息文本
                String messageText = generateFavoriteMessageText(contentTypeStr, fromUser.getUsername());
                message.put("messageText", messageText);
                
                // 时间信息
                Timestamp createdAt = favorite.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换收藏消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 获取收到的评论消息
     * 查询用户发布的内容收到的评论
     */
    private List<Map<String, Object>> getReceivedComments(Long userId, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            System.out.println("InteractionMessagesService: 开始查询用户 " + userId + " 收到的评论消息");
            
            // 1. 查询用户发布的文字帖子收到的评论
            List<Long> textPostIds = getTextPostIdsByUserId(userId);
            if (!textPostIds.isEmpty()) {
                QueryWrapper<TextpostComment_wlq> textCommentWrapper = new QueryWrapper<>();
                textCommentWrapper.in("text_content_id", textPostIds)
                        .eq("is_deleted", 0)
                        .ne("user_id", userId) // 排除自己评论自己的
                        .orderByDesc("created_at");
                List<TextpostComment_wlq> textComments = textpostCommentMapper.selectList(textCommentWrapper);
                messages.addAll(convertReceivedCommentsToMessages(textComments, 1, interactionMessagesLastReadAtMillis));
            }
            
            // 2. 查询用户发布的图片帖子收到的评论
            List<Long> imagePostIds = getImagePostIdsByUserId(userId);
            if (!imagePostIds.isEmpty()) {
                QueryWrapper<ImagePostComment_wlq> imageCommentWrapper = new QueryWrapper<>();
                imageCommentWrapper.in("post_id", imagePostIds)
                        .eq("is_deleted", 0)
                        .ne("user_id", userId)
                        .orderByDesc("created_at");
                List<ImagePostComment_wlq> imageComments = imagePostCommentMapper.selectList(imageCommentWrapper);
                messages.addAll(convertReceivedImageCommentsToMessages(imageComments, 2, interactionMessagesLastReadAtMillis));
            }
            
            // 3. 查询用户发布的小说帖子收到的评论
            List<Long> novelPostIds = getNovelPostIdsByUserId(userId);
            if (!novelPostIds.isEmpty()) {
                QueryWrapper<NovelpostComment_wlq> novelCommentWrapper = new QueryWrapper<>();
                novelCommentWrapper.in("novel_id", novelPostIds)
                        .eq("is_deleted", 0)
                        .ne("user_id", userId)
                        .orderByDesc("created_at");
                List<NovelpostComment_wlq> novelComments = novelpostCommentMapper.selectList(novelCommentWrapper);
                messages.addAll(convertReceivedNovelCommentsToMessages(novelComments, 3, interactionMessagesLastReadAtMillis));
            }
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条收到的评论消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取收到的评论消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 将文字评论转换为消息格式
     */
    private List<Map<String, Object>> convertReceivedCommentsToMessages(List<TextpostComment_wlq> comments, int contentType, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (TextpostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取用户信息
                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(comment.getUserId());
                if (fromUser == null) {
                    // 缓存未命中，从数据库查询
                    fromUser = userInfoMapper.selectById(comment.getUserId());
                    if (fromUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(fromUser);
                    }
                }
                if (fromUser == null) continue;
                
                // 获取内容信息
                Map<String, Object> contentInfo = getContentInfo(comment.getTextContentId(), contentType);
                if (contentInfo == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getTextpostCommentId());
                message.put("type", "comment");
                message.put("actionType", "received-comment");
                message.put("fromUserId", comment.getUserId());
                message.put("fromUsername", fromUser.getUsername() != null ? fromUser.getUsername() : "用户" + comment.getUserId());
                message.put("fromUserAvatar", fromUser.getProfilePicUrl() != null ? fromUser.getProfilePicUrl() : "");
                
                message.put("contentType", "text");
                message.put("contentId", comment.getTextContentId());
                message.put("contentTitle", contentInfo.get("title"));
                message.put("contentThumbnail", contentInfo.get("thumbnail"));
                
                message.put("messageText", fromUser.getUsername() + "评论了你的文字");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换文字评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 将图片评论转换为消息格式
     */
    private List<Map<String, Object>> convertReceivedImageCommentsToMessages(List<ImagePostComment_wlq> comments, int contentType, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (ImagePostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取用户信息
                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(comment.getUserId());
                if (fromUser == null) {
                    // 缓存未命中，从数据库查询
                    fromUser = userInfoMapper.selectById(comment.getUserId());
                    if (fromUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(fromUser);
                    }
                }
                if (fromUser == null) continue;
                
                // 获取内容信息
                Map<String, Object> contentInfo = getContentInfo(comment.getPostId(), contentType);
                if (contentInfo == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getImagepostCommentId());
                message.put("type", "comment");
                message.put("actionType", "received-comment");
                message.put("fromUserId", comment.getUserId());
                message.put("fromUsername", fromUser.getUsername() != null ? fromUser.getUsername() : "用户" + comment.getUserId());
                message.put("fromUserAvatar", fromUser.getProfilePicUrl() != null ? fromUser.getProfilePicUrl() : "");
                
                message.put("contentType", "image");
                message.put("contentId", comment.getPostId());
                message.put("contentTitle", contentInfo.get("title"));
                message.put("contentThumbnail", contentInfo.get("thumbnail"));
                
                message.put("messageText", fromUser.getUsername() + "评论了你的图文");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换图片评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 将小说评论转换为消息格式
     */
    private List<Map<String, Object>> convertReceivedNovelCommentsToMessages(List<NovelpostComment_wlq> comments, int contentType, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (NovelpostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取用户信息
                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(comment.getUserId());
                if (fromUser == null) {
                    // 缓存未命中，从数据库查询
                    fromUser = userInfoMapper.selectById(comment.getUserId());
                    if (fromUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(fromUser);
                    }
                }
                if (fromUser == null) continue;
                
                // 获取内容信息
                Map<String, Object> contentInfo = getContentInfo(comment.getNovelId(), contentType);
                if (contentInfo == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getNovelpostCommentId());
                message.put("type", "comment");
                message.put("actionType", "received-comment");
                message.put("fromUserId", comment.getUserId());
                message.put("fromUsername", fromUser.getUsername() != null ? fromUser.getUsername() : "用户" + comment.getUserId());
                message.put("fromUserAvatar", fromUser.getProfilePicUrl() != null ? fromUser.getProfilePicUrl() : "");
                
                message.put("contentType", "novel");
                message.put("contentId", comment.getNovelId());
                message.put("contentTitle", contentInfo.get("title"));
                message.put("contentThumbnail", contentInfo.get("thumbnail"));
                
                message.put("messageText", fromUser.getUsername() + "评论了你分享的小说");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换小说评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 获取发出的评论消息
     * 查询用户发出的评论
     */
    private List<Map<String, Object>> getSentComments(Long userId, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        try {
            System.out.println("InteractionMessagesService: 开始查询用户 " + userId + " 发出的评论消息");
            
            // 1. 查询用户发出的文字评论
            QueryWrapper<TextpostComment_wlq> textCommentWrapper = new QueryWrapper<>();
            textCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<TextpostComment_wlq> textComments = textpostCommentMapper.selectList(textCommentWrapper);
            messages.addAll(convertSentTextCommentsToMessages(textComments, interactionMessagesLastReadAtMillis));
            
            // 2. 查询用户发出的图片评论
            QueryWrapper<ImagePostComment_wlq> imageCommentWrapper = new QueryWrapper<>();
            imageCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<ImagePostComment_wlq> imageComments = imagePostCommentMapper.selectList(imageCommentWrapper);
            messages.addAll(convertSentImageCommentsToMessages(imageComments, interactionMessagesLastReadAtMillis));
            
            // 3. 查询用户发出的小说评论
            QueryWrapper<NovelpostComment_wlq> novelCommentWrapper = new QueryWrapper<>();
            novelCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<NovelpostComment_wlq> novelComments = novelpostCommentMapper.selectList(novelCommentWrapper);
            messages.addAll(convertSentNovelCommentsToMessages(novelComments, interactionMessagesLastReadAtMillis));
            
            System.out.println("InteractionMessagesService: 获取到 " + messages.size() + " 条发出的评论消息");
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取发出的评论消息失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * 将发出的文字评论转换为消息格式
     */
    private List<Map<String, Object>> convertSentTextCommentsToMessages(List<TextpostComment_wlq> comments, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (TextpostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取内容信息
                TextpostBase_wlq textPost = TextpostBase_list_wlq.getTextpostBaseById(comment.getTextContentId());
                if (textPost == null) {
                    // 缓存未命中，从数据库查询
                    textPost = textpostBaseMapper.selectById(comment.getTextContentId());
                }
                if (textPost == null) continue;
                
                // 优先从缓存获取内容作者信息
                UserInfo_njj toUser = UserInfo_list_njj.getUserById(textPost.getUserId());
                if (toUser == null) {
                    // 缓存未命中，从数据库查询
                    toUser = userInfoMapper.selectById(textPost.getUserId());
                    if (toUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(toUser);
                    }
                }
                if (toUser == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getTextpostCommentId());
                message.put("type", "comment");
                message.put("actionType", "sent-comment");
                message.put("toUserId", textPost.getUserId());
                message.put("toUsername", toUser.getUsername() != null ? toUser.getUsername() : "用户" + textPost.getUserId());
                message.put("toUserAvatar", toUser.getProfilePicUrl() != null ? toUser.getProfilePicUrl() : "");
                
                message.put("contentType", "text");
                message.put("contentId", comment.getTextContentId());
                
                String title = textPost.getContentText();
                if (title != null && title.length() > 30) {
                    title = title.substring(0, 30) + "...";
                }
                message.put("contentTitle", title != null ? title : "文字动态");
                message.put("contentThumbnail", "");
                
                message.put("messageText", "你评论了" + toUser.getUsername() + "的文字");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换发出的文字评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 将发出的图片评论转换为消息格式
     */
    private List<Map<String, Object>> convertSentImageCommentsToMessages(List<ImagePostComment_wlq> comments, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (ImagePostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取内容信息
                ImagePostBase_wlq imagePost = ImagePostBase_list_wlq.getImagePostBaseById(comment.getPostId());
                if (imagePost == null) {
                    // 缓存未命中，从数据库查询
                    imagePost = imagePostBaseMapper.selectById(comment.getPostId());
                }
                if (imagePost == null) continue;
                
                // 优先从缓存获取内容作者信息
                UserInfo_njj toUser = UserInfo_list_njj.getUserById(imagePost.getUserId());
                if (toUser == null) {
                    // 缓存未命中，从数据库查询
                    toUser = userInfoMapper.selectById(imagePost.getUserId());
                    if (toUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(toUser);
                    }
                }
                if (toUser == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getImagepostCommentId());
                message.put("type", "comment");
                message.put("actionType", "sent-comment");
                message.put("toUserId", imagePost.getUserId());
                message.put("toUsername", toUser.getUsername() != null ? toUser.getUsername() : "用户" + imagePost.getUserId());
                message.put("toUserAvatar", toUser.getProfilePicUrl() != null ? toUser.getProfilePicUrl() : "");
                
                message.put("contentType", "image");
                message.put("contentId", comment.getPostId());
                
                String title = imagePost.getContentText();
                if (title != null && title.length() > 30) {
                    title = title.substring(0, 30) + "...";
                }
                message.put("contentTitle", title != null ? title : "图文动态");
                
                // 获取第一张图片作为缩略图
                String imageUrls = imagePost.getImageUrls();
                if (imageUrls != null && !imageUrls.isEmpty()) {
                    String[] urls = imageUrls.split(",");
                    message.put("contentThumbnail", urls[0].trim());
                } else {
                    message.put("contentThumbnail", "");
                }
                
                message.put("messageText", "你评论了" + toUser.getUsername() + "的图文");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换发出的图片评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }
    
    /**
     * 将发出的小说评论转换为消息格式
     */
    private List<Map<String, Object>> convertSentNovelCommentsToMessages(List<NovelpostComment_wlq> comments, long interactionMessagesLastReadAtMillis) {
        List<Map<String, Object>> messages = new ArrayList<>();
        
        for (NovelpostComment_wlq comment : comments) {
            try {
                // 优先从缓存获取内容信息
                NovelpostBase_wlq novelPost = NovelpostBase_list_wlq.getNovelpostBaseById(comment.getNovelId());
                if (novelPost == null) {
                    // 缓存未命中，从数据库查询
                    novelPost = novelpostBaseMapper.selectById(comment.getNovelId());
                }
                if (novelPost == null) continue;
                
                // 优先从缓存获取内容作者信息
                UserInfo_njj toUser = UserInfo_list_njj.getUserById(novelPost.getUserId());
                if (toUser == null) {
                    // 缓存未命中，从数据库查询
                    toUser = userInfoMapper.selectById(novelPost.getUserId());
                    if (toUser != null) {
                        // 更新缓存
                        UserInfo_list_njj.updateUserInfoInCache(toUser);
                    }
                }
                if (toUser == null) continue;
                
                Map<String, Object> message = new HashMap<>();
                message.put("id", comment.getNovelpostCommentId());
                message.put("type", "comment");
                message.put("actionType", "sent-comment");
                message.put("toUserId", novelPost.getUserId());
                message.put("toUsername", toUser.getUsername() != null ? toUser.getUsername() : "用户" + novelPost.getUserId());
                message.put("toUserAvatar", toUser.getProfilePicUrl() != null ? toUser.getProfilePicUrl() : "");
                
                message.put("contentType", "novel");
                message.put("contentId", comment.getNovelId());
                message.put("contentTitle", novelPost.getNovelTitle() != null ? novelPost.getNovelTitle() : "小说");
                message.put("contentThumbnail", novelPost.getNovelCoverUrl() != null ? novelPost.getNovelCoverUrl() : "");
                
                message.put("messageText", "你评论了" + toUser.getUsername() + "分享的小说");
                message.put("commentText", comment.getCommentText());
                
                Timestamp createdAt = comment.getCreatedAt();
                message.put("createdAt", createdAt != null ? new Date(createdAt.getTime()) : new Date());
                message.put("timeAgo", formatTimeAgo(createdAt));
                
                message.put("isRead", isReadByMarker(createdAt, interactionMessagesLastReadAtMillis));
                
                messages.add(message);
            } catch (Exception e) {
                System.err.println("InteractionMessagesService: 转换发出的小说评论消息失败: " + e.getMessage());
            }
        }
        
        return messages;
    }

    private List<Long> getTextPostIdsByUserId(Long userId) {
        try {
            List<TextpostBase_wlq> posts = TextpostBase_list_wlq.searchTextpostBaseByUserId(userId);
            if (posts != null && !posts.isEmpty()) {
                return posts.stream().map(TextpostBase_wlq::getPostId).collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.out.println("InteractionMessagesService: 从缓存获取文字帖子失败，使用数据库查询: " + e.getMessage());
        }

        QueryWrapper<TextpostBase_wlq> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).select("post_id");
        List<TextpostBase_wlq> posts = textpostBaseMapper.selectList(wrapper);
        return posts.stream().map(TextpostBase_wlq::getPostId).collect(Collectors.toList());
    }

    private List<Long> getImagePostIdsByUserId(Long userId) {
        try {
            List<ImagePostBase_wlq> posts = ImagePostBase_list_wlq.searchImagePostBaseByUserId(userId);
            if (posts != null && !posts.isEmpty()) {
                return posts.stream().map(ImagePostBase_wlq::getPostId).collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.out.println("InteractionMessagesService: 从缓存获取图片帖子失败，使用数据库查询: " + e.getMessage());
        }

        QueryWrapper<ImagePostBase_wlq> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).select("post_id");
        List<ImagePostBase_wlq> posts = imagePostBaseMapper.selectList(wrapper);
        return posts.stream().map(ImagePostBase_wlq::getPostId).collect(Collectors.toList());
    }

    private List<Long> getNovelPostIdsByUserId(Long userId) {
        try {
            List<NovelpostBase_wlq> posts = NovelpostBase_list_wlq.searchNovelpostBaseByUserId(userId);
            if (posts != null && !posts.isEmpty()) {
                return posts.stream().map(NovelpostBase_wlq::getNovelId).collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.out.println("InteractionMessagesService: 从缓存获取小说帖子失败，使用数据库查询: " + e.getMessage());
        }

        QueryWrapper<NovelpostBase_wlq> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).select("novel_id");
        List<NovelpostBase_wlq> posts = novelpostBaseMapper.selectList(wrapper);
        return posts.stream().map(NovelpostBase_wlq::getNovelId).collect(Collectors.toList());
    }

    private Map<String, Object> getContentInfo(Long contentId, int contentType) {
        Map<String, Object> contentInfo = new HashMap<>();

        try {
            if (contentType == 1) {
                TextpostBase_wlq textPost = TextpostBase_list_wlq.getTextpostBaseById(contentId);
                if (textPost == null) {
                    textPost = textpostBaseMapper.selectById(contentId);
                }
                if (textPost != null) {
                    String title = textPost.getContentText();
                    if (title != null && title.length() > 30) {
                        title = title.substring(0, 30) + "...";
                    }
                    contentInfo.put("title", title != null ? title : "文字动态");
                    contentInfo.put("thumbnail", "");
                }
            } else if (contentType == 2) {
                ImagePostBase_wlq imagePost = ImagePostBase_list_wlq.getImagePostBaseById(contentId);
                if (imagePost == null) {
                    imagePost = imagePostBaseMapper.selectById(contentId);
                }
                if (imagePost != null) {
                    String title = imagePost.getContentText();
                    if (title != null && title.length() > 30) {
                        title = title.substring(0, 30) + "...";
                    }
                    contentInfo.put("title", title != null ? title : "图文动态");

                    String imageUrls = imagePost.getImageUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        String[] urls = imageUrls.split(",");
                        contentInfo.put("thumbnail", urls[0].trim());
                    } else {
                        contentInfo.put("thumbnail", "");
                    }
                }
            } else if (contentType == 3) {
                NovelpostBase_wlq novelPost = NovelpostBase_list_wlq.getNovelpostBaseById(contentId);
                if (novelPost == null) {
                    novelPost = novelpostBaseMapper.selectById(contentId);
                }
                if (novelPost != null) {
                    contentInfo.put("title", novelPost.getNovelTitle() != null ? novelPost.getNovelTitle() : "小说");
                    contentInfo.put("thumbnail", novelPost.getNovelCoverUrl() != null ? novelPost.getNovelCoverUrl() : "");
                }
            }
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 获取内容信息失败: " + e.getMessage());
        }

        return contentInfo.isEmpty() ? null : contentInfo;
    }

    private String generateLikeMessageText(String contentType, String username) {
        switch (contentType) {
            case "text":
                return username + "赞了你的文字";
            case "image":
                return username + "赞了你的图文";
            case "novel":
                return username + "赞了你分享的小说";
            default:
                return username + "赞了你的内容";
        }
    }

    private String generateFavoriteMessageText(String contentType, String username) {
        switch (contentType) {
            case "text":
                return username + "收藏了你的文字";
            case "image":
                return username + "收藏了你的图文";
            case "novel":
                return username + "收藏了你分享的小说";
            default:
                return username + "收藏了你的内容";
        }
    }

    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) return "刚刚";

        long now = System.currentTimeMillis();
        long time = timestamp.getTime();
        long diff = now - time;

        if (diff < 60000) {
            return "刚刚";
        } else if (diff < 3600000) {
            return (diff / 60000) + "分钟前";
        } else if (diff < 86400000) {
            return (diff / 3600000) + "小时前";
        } else if (diff < 604800000) {
            return (diff / 86400000) + "天前";
        } else if (diff < 2592000000L) {
            return (diff / 604800000) + "周前";
        } else if (diff < 31536000000L) {
            return (diff / 2592000000L) + "个月前";
        } else {
            return (diff / 31536000000L) + "年前";
        }
    }
    
    /**
     * 标记所有消息为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> markAllAsRead(Long userId) {
        System.out.println("InteractionMessagesService: 标记用户 " + userId + " 的所有消息为已读");
        
        try {
            long now = System.currentTimeMillis();
            createInteractionMessagesReadMarker(userId, now);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "已全部标记为已读");
            response.put("userId", userId);
            response.put("lastReadAt", now);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("InteractionMessagesService: 标记所有消息为已读成功");
            
            return response;
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesService: 标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "标记所有消息为已读失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
}
