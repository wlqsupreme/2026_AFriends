package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户评论历史服务类
 * 负责处理用户评论历史的查询操作
 */
@Service
public class UserCommentsService {

    @Autowired
    private TextpostCommentMapper textpostCommentMapper;

    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;

    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取用户的评论历史列表
     *
     * @param userId 用户ID
     * @return 评论历史列表
     */
    public List<Map<String, Object>> getUserComments(Long userId) {
        System.out.println("UserCommentsService: 开始获取用户 " + userId + " 的评论历史");

        List<Map<String, Object>> allComments = new ArrayList<>();

        try {
            // 1. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                System.err.println("UserCommentsService: 用户不存在，userId=" + userId);
                throw new IllegalArgumentException("用户不存在");
            }

            // 2. 查询用户发出的文字评论
            QueryWrapper<TextpostComment_wlq> textCommentWrapper = new QueryWrapper<>();
            textCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<TextpostComment_wlq> textComments = textpostCommentMapper.selectList(textCommentWrapper);
            System.out.println("UserCommentsService: 查询到 " + textComments.size() + " 条文字评论");
            allComments.addAll(convertTextCommentsToMap(textComments));

            // 3. 查询用户发出的图片评论
            QueryWrapper<ImagePostComment_wlq> imageCommentWrapper = new QueryWrapper<>();
            imageCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<ImagePostComment_wlq> imageComments = imagePostCommentMapper.selectList(imageCommentWrapper);
            System.out.println("UserCommentsService: 查询到 " + imageComments.size() + " 条图片评论");
            allComments.addAll(convertImageCommentsToMap(imageComments));

            // 4. 查询用户发出的小说评论
            QueryWrapper<NovelpostComment_wlq> novelCommentWrapper = new QueryWrapper<>();
            novelCommentWrapper.eq("user_id", userId)
                    .eq("is_deleted", 0)
                    .orderByDesc("created_at");
            List<NovelpostComment_wlq> novelComments = novelpostCommentMapper.selectList(novelCommentWrapper);
            System.out.println("UserCommentsService: 查询到 " + novelComments.size() + " 条小说评论");
            allComments.addAll(convertNovelCommentsToMap(novelComments));

            // 5. 按时间降序排序（最新的在前）
            allComments.sort((a, b) -> {
                Timestamp timeA = (Timestamp) a.get("timestamp");
                Timestamp timeB = (Timestamp) b.get("timestamp");
                if (timeA == null || timeB == null) {
                    return 0;
                }
                return timeB.compareTo(timeA);
            });

            System.out.println("UserCommentsService: 成功获取用户 " + userId + " 的评论历史，共 " + allComments.size() + " 条");
            return allComments;

        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出参数错误，由控制器处理
        } catch (Exception e) {
            System.err.println("UserCommentsService: 获取评论历史失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取评论历史失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将文字评论转换为前端需要的 Map 格式
     */
    private List<Map<String, Object>> convertTextCommentsToMap(List<TextpostComment_wlq> comments) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (TextpostComment_wlq comment : comments) {
            try {
                Map<String, Object> commentMap = new HashMap<>();
                
                // 基本信息
                commentMap.put("commentId", comment.getTextpostCommentId());
                commentMap.put("contentType", 1); // 1=文字动态
                commentMap.put("commentText", comment.getCommentText());
                commentMap.put("likeCount", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
                
                // 时间信息
                Timestamp createdAt = comment.getCreatedAt();
                commentMap.put("timestamp", createdAt);
                commentMap.put("timeAgo", formatTimeAgo(createdAt));
                commentMap.put("createdAt", DATE_FORMAT.format(createdAt));

                // 获取用户信息（优先从缓存获取）
                UserInfo_njj userInfo = UserInfo_list_njj.getUserById(comment.getUserId());
                if (userInfo == null) {
                    userInfo = userInfoMapper.selectById(comment.getUserId());
                    if (userInfo != null) {
                        UserInfo_list_njj.updateUserInfoInCache(userInfo);
                    }
                }
                commentMap.put("username", userInfo != null && userInfo.getUsername() != null ? userInfo.getUsername() : "用户" + comment.getUserId());
                commentMap.put("userAvatar", userInfo != null && userInfo.getProfilePicUrl() != null ? userInfo.getProfilePicUrl() : "");

                // 获取内容信息（被评论的文字动态）
                TextpostBase_wlq textPost = textpostBaseMapper.selectById(comment.getTextContentId());
                if (textPost != null) {
                    commentMap.put("contentId", textPost.getPostId());
                    String contentText = textPost.getContentText();
                    if (contentText != null && contentText.length() > 50) {
                        contentText = contentText.substring(0, 50) + "...";
                    }
                    commentMap.put("contentTitle", contentText != null ? contentText : "文字动态");
                    commentMap.put("contentThumbnail", "");
                } else {
                    commentMap.put("contentId", comment.getTextContentId());
                    commentMap.put("contentTitle", "内容已删除");
                    commentMap.put("contentThumbnail", "");
                }

                result.add(commentMap);
            } catch (Exception e) {
                System.err.println("UserCommentsService: 转换文字评论失败，commentId=" + comment.getTextpostCommentId() + ": " + e.getMessage());
                // 继续处理下一条记录
            }
        }

        return result;
    }

    /**
     * 将图片评论转换为前端需要的 Map 格式
     */
    private List<Map<String, Object>> convertImageCommentsToMap(List<ImagePostComment_wlq> comments) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (ImagePostComment_wlq comment : comments) {
            try {
                Map<String, Object> commentMap = new HashMap<>();
                
                // 基本信息
                commentMap.put("commentId", comment.getImagepostCommentId());
                commentMap.put("contentType", 2); // 2=图片动态
                commentMap.put("commentText", comment.getCommentText());
                commentMap.put("likeCount", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
                
                // 时间信息
                Timestamp createdAt = comment.getCreatedAt();
                commentMap.put("timestamp", createdAt);
                commentMap.put("timeAgo", formatTimeAgo(createdAt));
                commentMap.put("createdAt", DATE_FORMAT.format(createdAt));

                // 获取用户信息（优先从缓存获取）
                UserInfo_njj userInfo = UserInfo_list_njj.getUserById(comment.getUserId());
                if (userInfo == null) {
                    userInfo = userInfoMapper.selectById(comment.getUserId());
                    if (userInfo != null) {
                        UserInfo_list_njj.updateUserInfoInCache(userInfo);
                    }
                }
                commentMap.put("username", userInfo != null && userInfo.getUsername() != null ? userInfo.getUsername() : "用户" + comment.getUserId());
                commentMap.put("userAvatar", userInfo != null && userInfo.getProfilePicUrl() != null ? userInfo.getProfilePicUrl() : "");

                // 获取内容信息（被评论的图片动态）
                ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(comment.getPostId());
                if (imagePost != null) {
                    commentMap.put("contentId", imagePost.getPostId());
                    String contentText = imagePost.getContentText();
                    if (contentText != null && contentText.length() > 50) {
                        contentText = contentText.substring(0, 50) + "...";
                    }
                    commentMap.put("contentTitle", contentText != null ? contentText : "图文动态");
                    
                    // 获取第一张图片作为缩略图
                    String imageUrls = imagePost.getImageUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        String[] urls = imageUrls.split(",");
                        commentMap.put("contentThumbnail", urls[0].trim());
                    } else {
                        commentMap.put("contentThumbnail", "");
                    }
                } else {
                    commentMap.put("contentId", comment.getPostId());
                    commentMap.put("contentTitle", "内容已删除");
                    commentMap.put("contentThumbnail", "");
                }

                result.add(commentMap);
            } catch (Exception e) {
                System.err.println("UserCommentsService: 转换图片评论失败，commentId=" + comment.getImagepostCommentId() + ": " + e.getMessage());
                // 继续处理下一条记录
            }
        }

        return result;
    }

    /**
     * 将小说评论转换为前端需要的 Map 格式
     */
    private List<Map<String, Object>> convertNovelCommentsToMap(List<NovelpostComment_wlq> comments) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (NovelpostComment_wlq comment : comments) {
            try {
                Map<String, Object> commentMap = new HashMap<>();
                
                // 基本信息
                commentMap.put("commentId", comment.getNovelpostCommentId());
                commentMap.put("contentType", 3); // 3=小说
                commentMap.put("commentText", comment.getCommentText());
                commentMap.put("likeCount", comment.getLikeCount() != null ? comment.getLikeCount() : 0);
                
                // 时间信息
                Timestamp createdAt = comment.getCreatedAt();
                commentMap.put("timestamp", createdAt);
                commentMap.put("timeAgo", formatTimeAgo(createdAt));
                commentMap.put("createdAt", DATE_FORMAT.format(createdAt));

                // 获取用户信息（优先从缓存获取）
                UserInfo_njj userInfo = UserInfo_list_njj.getUserById(comment.getUserId());
                if (userInfo == null) {
                    userInfo = userInfoMapper.selectById(comment.getUserId());
                    if (userInfo != null) {
                        UserInfo_list_njj.updateUserInfoInCache(userInfo);
                    }
                }
                commentMap.put("username", userInfo != null && userInfo.getUsername() != null ? userInfo.getUsername() : "用户" + comment.getUserId());
                commentMap.put("userAvatar", userInfo != null && userInfo.getProfilePicUrl() != null ? userInfo.getProfilePicUrl() : "");

                // 获取内容信息（被评论的小说）
                NovelpostBase_wlq novelPost = novelpostBaseMapper.selectById(comment.getNovelId());
                if (novelPost != null) {
                    commentMap.put("contentId", novelPost.getNovelId());
                    commentMap.put("contentTitle", novelPost.getNovelTitle() != null ? novelPost.getNovelTitle() : "小说");
                    commentMap.put("contentThumbnail", novelPost.getNovelCoverUrl() != null ? novelPost.getNovelCoverUrl() : "");
                } else {
                    commentMap.put("contentId", comment.getNovelId());
                    commentMap.put("contentTitle", "内容已删除");
                    commentMap.put("contentThumbnail", "");
                }

                result.add(commentMap);
            } catch (Exception e) {
                System.err.println("UserCommentsService: 转换小说评论失败，commentId=" + comment.getNovelpostCommentId() + ": " + e.getMessage());
                // 继续处理下一条记录
            }
        }

        return result;
    }

    /**
     * 格式化时间为"刚刚"、"X分钟前"等格式
     */
    private String formatTimeAgo(Timestamp timestamp) {
        if (timestamp == null) {
            return "未知时间";
        }

        long now = System.currentTimeMillis();
        long time = timestamp.getTime();
        long diff = now - time;

        if (diff < 60000) { // 1分钟内
            return "刚刚";
        } else if (diff < 3600000) { // 1小时内
            return (diff / 60000) + "分钟前";
        } else if (diff < 86400000) { // 24小时内
            return (diff / 3600000) + "小时前";
        } else if (diff < 604800000) { // 7天内
            return (diff / 86400000) + "天前";
        } else {
            return DATE_FORMAT.format(timestamp);
        }
    }
}
