package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.util.PrivacySettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户个人主页服务类
 * 负责处理用户个人主页数据的查询和整合
 */
@Service
public class UserProfileService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;

    /**
     * 获取用户个人主页数据
     * 
     * @param userId 用户ID
     * @return 整合后的用户主页数据
     */
    public Map<String, Object> getUserProfileData(Long userId) {
        System.out.println("UserProfileService: 开始获取用户 " + userId + " 的个人主页数据...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();

        try {
            // 检查Mapper是否注入成功
            System.out.println("=== UserProfileService: 检查Mapper注入状态 ===");
            System.out.println("userInfoMapper: " + (userInfoMapper != null ? "已注入" : "未注入"));
            System.out.println("novelpostBaseMapper: " + (novelpostBaseMapper != null ? "已注入" : "未注入"));
            System.out.println("imagePostBaseMapper: " + (imagePostBaseMapper != null ? "已注入" : "未注入"));
            System.out.println("textpostBaseMapper: " + (textpostBaseMapper != null ? "已注入" : "未注入"));
            System.out.println(
                    "contentFavouriteRelationMapper: " + (contentFavouriteRelationMapper != null ? "已注入" : "未注入"));

            // 测试数据库连接
            System.out.println("=== 测试数据库连接 ===");
            try {
                long totalUsers = userInfoMapper.selectCount(null);
                System.out.println("数据库连接正常，用户信息表总记录数: " + totalUsers);
            } catch (Exception e) {
                System.err.println("数据库连接测试失败: " + e.getMessage());
                e.printStackTrace();
            }

            // 1. 获取用户基本信息
            System.out.println("UserProfileService: 查询用户基本信息...");
            System.out.println("UserProfileService: 执行SQL: SELECT * FROM v2_user_info WHERE user_id = " + userId);
            UserInfo_njj userInfo = null;
            try {
                userInfo = userInfoMapper.selectById(userId);
                if (userInfo == null) {
                    System.out.println("UserProfileService: 未找到用户ID为 " + userId + " 的用户信息");
                    // 创建默认用户信息
                    userInfo = createDefaultUserInfo(userId);
                } else {
                    System.out.println("UserProfileService: 找到用户信息 - 用户名: " + userInfo.getUsername() +
                            ", 位置: " + userInfo.getLocation() + ", 等级: " + userInfo.getLevel() +
                            ", 金币: " + userInfo.getGold() + ", 钻石: " + userInfo.getDiamond());
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 查询用户信息失败: " + e.getMessage());
                e.printStackTrace();
                System.out.println("UserProfileService: 使用默认用户信息");
                userInfo = createDefaultUserInfo(userId);
            }

            // 处理用户基本信息
            Map<String, Object> userBasicInfo = createUserBasicInfo(userInfo);
            result.put("userInfo", userBasicInfo);

            // 2. 获取用户动态数据统计
            Map<String, Object> dynamicStats = getUserDynamicStats(userId);
            result.put("dynamicStats", dynamicStats);

            // 3. 获取用户收藏数据统计
            Map<String, Object> collectionStats = getUserCollectionStats(userId);
            result.put("collectionStats", collectionStats);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserProfileService: 用户个人主页数据获取完成！耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("UserProfileService: 获取用户个人主页数据失败: " + e.getMessage());
            e.printStackTrace();

            // 返回默认数据
            result.put("userInfo", createDefaultUserBasicInfo(userId));
            result.put("dynamicStats", createDefaultDynamicStats());
            result.put("collectionStats", createDefaultCollectionStats());
        }

        return result;
    }

    /**
     * 获取用户动态数据
     * 
     * @param userId 用户ID
     * @return 用户动态数据
     */
    public Map<String, Object> getUserDynamicData(Long userId) {
        System.out.println("UserProfileService: 开始获取用户 " + userId + " 的动态数据...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 获取用户发布的小说
            System.out.println("UserProfileService: 查询用户发布的小说...");
            List<NovelpostBase_wlq> novels = novelpostBaseMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_deleted", 0)
                            .orderByDesc("created_at")
                            .last("LIMIT 20"));
            System.out.println("UserProfileService: 查询到 " + novels.size() + " 部小说");

            List<Map<String, Object>> novelPosts = new ArrayList<>();
            for (NovelpostBase_wlq novel : novels) {
                Map<String, Object> novelPost = createNovelPost(novel);
                novelPosts.add(novelPost);
            }

            // 2. 获取用户发布的图片
            System.out.println("UserProfileService: 查询用户发布的图片...");
            List<ImagePostBase_wlq> images = imagePostBaseMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImagePostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_view", 1)
                            .orderByDesc("created_at")
                            .last("LIMIT 20"));
            System.out.println("UserProfileService: 查询到 " + images.size() + " 张图片");

            List<Map<String, Object>> imagePosts = new ArrayList<>();
            for (ImagePostBase_wlq image : images) {
                Map<String, Object> imagePost = createImagePost(image);
                imagePosts.add(imagePost);
            }

            // 3. 获取用户发布的文字
            System.out.println("UserProfileService: 查询用户发布的文字...");
            System.out.println("UserProfileService: 执行SQL: SELECT * FROM v2_textpost_base WHERE user_id = " + userId
                    + " AND is_view = 1 ORDER BY created_at DESC LIMIT 20");
            List<TextpostBase_wlq> texts = textpostBaseMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TextpostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_view", 1)
                            .orderByDesc("created_at")
                            .last("LIMIT 20"));
            System.out.println("UserProfileService: 查询到 " + texts.size() + " 条文字");
            for (TextpostBase_wlq text : texts) {
                System.out.println(
                        "UserProfileService: 文字内容 - ID: " + text.getPostId() + ", 内容: " + text.getContentText() +
                                ", 创建时间: " + text.getCreatedAt());
            }

            List<Map<String, Object>> textPosts = new ArrayList<>();
            for (TextpostBase_wlq text : texts) {
                Map<String, Object> textPost = createTextPost(text);
                textPosts.add(textPost);
            }

            // 合并所有动态数据并按时间排序
            List<Map<String, Object>> allPosts = new ArrayList<>();
            allPosts.addAll(novelPosts);
            allPosts.addAll(imagePosts);
            allPosts.addAll(textPosts);

            // 按时间排序
            allPosts.sort((a, b) -> {
                Date timeA = (Date) a.get("createdAt");
                Date timeB = (Date) b.get("createdAt");
                if (timeA == null || timeB == null)
                    return 0;
                return timeB.compareTo(timeA);
            });

            result.put("posts", allPosts);
            result.put("totalCount", allPosts.size());
            result.put("novelCount", novelPosts.size());
            result.put("imageCount", imagePosts.size());
            result.put("textCount", textPosts.size());

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserProfileService: 用户动态数据获取完成！总共 " + allPosts.size() + " 条动态，耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("UserProfileService: 获取用户动态数据失败: " + e.getMessage());
            e.printStackTrace();

            result.put("posts", new ArrayList<>());
            result.put("totalCount", 0);
            result.put("novelCount", 0);
            result.put("imageCount", 0);
            result.put("textCount", 0);
        }

        return result;
    }

    /**
     * 获取用户收藏数据
     * 
     * @param userId 收藏所有者用户ID
     * @param viewerId 查看者用户ID（用于隐私设置检查）
     * @return 用户收藏数据
     */
    public Map<String, Object> getUserCollectionsData(Long userId, Long viewerId) {
        System.out.println("UserProfileService: 开始获取用户 " + userId + " 的收藏数据，查看者ID: " + viewerId);
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();

        try {
            // 隐私设置检查：如果不是本人查看，检查是否允许公开收藏
            if (viewerId != null && !userId.equals(viewerId)) {
                boolean canView = Afriends_v3.util.PrivacySettingsUtil.canViewCollections(userId, viewerId);
                if (!canView) {
                    System.out.println("UserProfileService: 收藏查看被隐私设置阻止 - 所有者ID: " + userId + ", 查看者ID: " + viewerId);
                    result.put("collectedPosts", new ArrayList<>());
                    result.put("totalCount", 0);
                    result.put("privacyRestricted", true);
                    result.put("message", "该用户未公开收藏");
                    return result;
                }
            }
            // 获取用户收藏关系
            System.out.println("UserProfileService: 查询用户收藏关系...");
            List<ContentFavouriteRelation_wlq> favourites = contentFavouriteRelationMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("is_active", 1)
                            .orderByDesc("created_at")
                            .last("LIMIT 50"));
            System.out.println("UserProfileService: 查询到 " + favourites.size() + " 条收藏记录");

            List<Map<String, Object>> collectedPosts = new ArrayList<>();
            for (ContentFavouriteRelation_wlq favourite : favourites) {
                try {
                    Map<String, Object> collectedPost = getCollectedPostContent(favourite);
                    if (collectedPost != null) {
                        collectedPosts.add(collectedPost);
                    }
                } catch (Exception e) {
                    System.err.println("UserProfileService: 处理收藏内容失败: " + e.getMessage());
                }
            }

            result.put("collectedPosts", collectedPosts);
            result.put("totalCount", collectedPosts.size());

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println(
                    "UserProfileService: 用户收藏数据获取完成！总共 " + collectedPosts.size() + " 条收藏，耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("UserProfileService: 获取用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();

            result.put("collectedPosts", new ArrayList<>());
            result.put("totalCount", 0);
        }

        return result;
    }

    /**
     * 创建用户基本信息
     */
    private Map<String, Object> createUserBasicInfo(UserInfo_njj userInfo) {
        Map<String, Object> info = new HashMap<>();
        info.put("userId", userInfo.getUserId());
        info.put("username", userInfo.getUsername() != null ? userInfo.getUsername() : "猫又屋之主");
        info.put("realName", userInfo.getRealName());
        info.put("age", userInfo.getAge());
        info.put("gender", userInfo.getGender() != null ? userInfo.getGender().toString() : "UNKNOWN");
        info.put("location", userInfo.getLocation() != null ? userInfo.getLocation() : "中国-南京");
        info.put("bio", userInfo.getBio() != null ? userInfo.getBio() : "这个人很懒,什么都没有留下");
        info.put("profilePicUrl", userInfo.getProfilePicUrl());
        info.put("homepageBgUrl", userInfo.getHomepageBgUrl());
        info.put("level", userInfo.getLevel() != null ? userInfo.getLevel() : 10);
        info.put("gold", userInfo.getGold() != null ? userInfo.getGold() : 0);
        info.put("diamond", userInfo.getDiamond() != null ? userInfo.getDiamond() : 0);
        info.put("realNameVerified", userInfo.getRealNameVerified() != null ? userInfo.getRealNameVerified() : 0);
        info.put("jobVerified", userInfo.getJobVerified() != null ? userInfo.getJobVerified() : 0);
        info.put("studentVerified", userInfo.getStudentVerified() != null ? userInfo.getStudentVerified() : 0);
        info.put("userKind", userInfo.getUserKind()); // 直接使用String类型
        info.put("createdAt", userInfo.getCreatedAt());
        info.put("updatedAt", userInfo.getUpdatedAt());

        System.out.println("UserProfileService: 处理用户信息 - 用户名: " + userInfo.getUsername() +
                ", 位置: " + userInfo.getLocation() + ", 用户类型: " + userInfo.getUserKind());

        return info;
    }

    /**
     * 创建默认用户信息
     */
    private UserInfo_njj createDefaultUserInfo(Long userId) {
        UserInfo_njj userInfo = new UserInfo_njj();
        userInfo.setUserId(userId);
        userInfo.setUsername("猫又屋之主");
        userInfo.setLocation("中国-南京");
        userInfo.setBio("这个人很懒,什么都没有留下");
        userInfo.setLevel(10L);
        userInfo.setGold(0L);
        userInfo.setDiamond(0L);
        userInfo.setRealNameVerified((byte) 0);
        userInfo.setJobVerified((byte) 0);
        userInfo.setStudentVerified((byte) 0);
        userInfo.setUserKind(UserInfo_njj.UserKind.REAL); // 设置默认用户类型
        userInfo.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        userInfo.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return userInfo;
    }

    /**
     * 创建默认用户基本信息
     */
    private Map<String, Object> createDefaultUserBasicInfo(Long userId) {
        Map<String, Object> info = new HashMap<>();
        info.put("userId", userId);
        info.put("username", "猫又屋之主");
        info.put("location", "中国-南京");
        info.put("bio", "这个人很懒,什么都没有留下");
        info.put("level", 10);
        info.put("gold", 0);
        info.put("diamond", 0);
        info.put("realNameVerified", 0);
        info.put("jobVerified", 0);
        info.put("studentVerified", 0);
        return info;
    }

    /**
     * 获取用户动态数据统计
     */
    private Map<String, Object> getUserDynamicStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        try {
            long novelCount = novelpostBaseMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<NovelpostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_deleted", 0));
            long imageCount = imagePostBaseMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ImagePostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_view", 1));
            long textCount = textpostBaseMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TextpostBase_wlq>()
                            .eq("user_id", userId)
                            .eq("is_view", 1));

            stats.put("novelCount", novelCount);
            stats.put("imageCount", imageCount);
            stats.put("textCount", textCount);
            stats.put("totalCount", novelCount + imageCount + textCount);
        } catch (Exception e) {
            System.err.println("UserProfileService: 获取动态统计失败: " + e.getMessage());
            stats.put("novelCount", 0);
            stats.put("imageCount", 0);
            stats.put("textCount", 0);
            stats.put("totalCount", 0);
        }
        return stats;
    }

    /**
     * 获取用户收藏数据统计
     */
    private Map<String, Object> getUserCollectionStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        try {
            long totalCount = contentFavouriteRelationMapper.selectCount(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("is_active", 1));
            stats.put("totalCount", totalCount);
        } catch (Exception e) {
            System.err.println("UserProfileService: 获取收藏统计失败: " + e.getMessage());
            stats.put("totalCount", 0);
        }
        return stats;
    }

    /**
     * 创建默认动态统计
     */
    private Map<String, Object> createDefaultDynamicStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("novelCount", 0);
        stats.put("imageCount", 0);
        stats.put("textCount", 0);
        stats.put("totalCount", 0);
        return stats;
    }

    /**
     * 创建默认收藏统计
     */
    private Map<String, Object> createDefaultCollectionStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", 0);
        return stats;
    }

    /**
     * 创建小说帖子数据
     */
    private Map<String, Object> createNovelPost(NovelpostBase_wlq novel) {
        // 先从缓存获取用户信息，如果缓存中没有则从数据库查询
        UserInfo_njj user = UserInfo_list_njj.getUserById(novel.getUserId());
        if (user == null) {
            // 缓存中没有，从数据库查询
            try {
                user = userInfoMapper.selectById(novel.getUserId());
                if (user != null) {
                    // 将查询到的用户信息更新到缓存
                    UserInfo_list_njj.updateUserInfoInCache(user);
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 从数据库查询用户信息失败: " + e.getMessage());
            }
        }
        // 如果仍然为null，使用默认值
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : "未知用户";

        Map<String, Object> post = new HashMap<>();
        post.put("id", novel.getNovelId());
        post.put("type", "novel");
        post.put("contentType", "小说");
        post.put("username", username);
        post.put("timeAgo", formatTimeAgo(novel.getCreatedAt()));

        // 小说信息
        Map<String, Object> novelInfo = new HashMap<>();
        novelInfo.put("title", novel.getNovelTitle());
        novelInfo.put("description",
                novel.getNovelDescription() != null
                        ? novel.getNovelDescription().substring(0, Math.min(100, novel.getNovelDescription().length()))
                                + "..."
                        : "暂无描述");
        novelInfo.put("rating", novel.getNovelScore() != null ? novel.getNovelScore().toString() : "4.5");
        post.put("novelInfo", novelInfo);

        post.put("novelCover", novel.getNovelCoverUrl());
        post.put("author", novel.getAuthorName() != null ? novel.getAuthorName() : "未知作者");
        post.put("rating", novel.getNovelScore() != null ? novel.getNovelScore().toString() : "4.5");
        post.put("reviewCount", "1.2万人点评");
        post.put("readerCount", novel.getReadingCount() != null ? novel.getReadingCount().toString() + "人" : "5.6万人");
        post.put("wordCount", "12.3万字");
        post.put("updateDays", "连续更新30天");

        // 标签处理
        List<String> tags = new ArrayList<>();
        if (novel.getSoftTags() != null && !novel.getSoftTags().isEmpty()) {
            String[] tagArray = novel.getSoftTags().split(",");
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
        post.put("tags", tags);

        post.put("synopsis",
                novel.getNovelDescription() != null
                        ? novel.getNovelDescription().substring(0, Math.min(200, novel.getNovelDescription().length()))
                                + "..."
                        : "暂无简介");

        // 互动数据
        post.put("likes", novel.getLikeCount() != null ? novel.getLikeCount() : 0);
        post.put("dislikes", novel.getDislikeCount() != null ? novel.getDislikeCount() : 0);
        post.put("favorites", novel.getFavoriteCount() != null ? novel.getFavoriteCount() : 0);
        post.put("comments", novel.getCommentCount() != null ? novel.getCommentCount() : 0);
        post.put("isLiked", false);
        post.put("isDisliked", false);
        post.put("isFavorited", false);

        post.put("createdAt", novel.getCreatedAt());

        return post;
    }

    /**
     * 创建图片帖子数据
     */
    private Map<String, Object> createImagePost(ImagePostBase_wlq image) {
        // 先从缓存获取用户信息，如果缓存中没有则从数据库查询
        UserInfo_njj user = UserInfo_list_njj.getUserById(image.getUserId());
        if (user == null) {
            // 缓存中没有，从数据库查询
            try {
                user = userInfoMapper.selectById(image.getUserId());
                if (user != null) {
                    // 将查询到的用户信息更新到缓存
                    UserInfo_list_njj.updateUserInfoInCache(user);
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 从数据库查询用户信息失败: " + e.getMessage());
            }
        }
        // 如果仍然为null，使用默认值
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : "未知用户";

        Map<String, Object> post = new HashMap<>();
        post.put("id", image.getPostId());
        post.put("type", "image");
        post.put("contentType", "图片");
        post.put("username", username);
        post.put("timeAgo", formatTimeAgo(image.getCreatedAt()));

        post.put("textContent", image.getContentText());

        // 处理图片URLs
        List<String> images = new ArrayList<>();
        if (image.getImageUrls() != null && !image.getImageUrls().isEmpty()) {
            String[] imageArray = image.getImageUrls().split(",");
            for (String imageUrl : imageArray) {
                if (imageUrl.trim().length() > 0) {
                    images.add(imageUrl.trim());
                }
            }
        }
        if (images.isEmpty()) {
            images.add("/static/default-image.png");
        }
        post.put("images", images);

        // 互动数据
        post.put("likes", image.getLikeCount() != null ? image.getLikeCount() : 0);
        post.put("dislikes", image.getDislikeCount() != null ? image.getDislikeCount() : 0);
        post.put("favorites", image.getCollectCount() != null ? image.getCollectCount() : 0);
        post.put("comments", image.getCommentCount() != null ? image.getCommentCount() : 0);
        post.put("isLiked", false);
        post.put("isDisliked", false);
        post.put("isFavorited", false);

        post.put("createdAt", image.getCreatedAt());

        return post;
    }

    /**
     * 创建文字帖子数据
     */
    private Map<String, Object> createTextPost(TextpostBase_wlq text) {
        // 先从缓存获取用户信息，如果缓存中没有则从数据库查询
        UserInfo_njj user = UserInfo_list_njj.getUserById(text.getUserId());
        if (user == null) {
            // 缓存中没有，从数据库查询
            try {
                user = userInfoMapper.selectById(text.getUserId());
                if (user != null) {
                    // 将查询到的用户信息更新到缓存
                    UserInfo_list_njj.updateUserInfoInCache(user);
                }
            } catch (Exception e) {
                System.err.println("UserProfileService: 从数据库查询用户信息失败: " + e.getMessage());
            }
        }
        // 如果仍然为null，使用默认值
        String username = (user != null && user.getUsername() != null) ? user.getUsername() : "未知用户";

        Map<String, Object> post = new HashMap<>();
        post.put("id", text.getPostId());
        post.put("type", "text");
        post.put("contentType", "动态");
        post.put("username", username);
        post.put("timeAgo", formatTimeAgo(text.getCreatedAt()));

        post.put("textContent", text.getContentText());

        // 互动数据
        post.put("likes", text.getLikeCount() != null ? text.getLikeCount() : 0);
        post.put("dislikes", text.getDislikeCount() != null ? text.getDislikeCount() : 0);
        post.put("favorites", text.getCollectCount() != null ? text.getCollectCount() : 0);
        post.put("comments", text.getCommentCount() != null ? text.getCommentCount() : 0);
        post.put("isLiked", false);
        post.put("isDisliked", false);
        post.put("isFavorited", false);

        post.put("createdAt", text.getCreatedAt());

        return post;
    }

    /**
     * 获取收藏的内容详情
     */
    private Map<String, Object> getCollectedPostContent(ContentFavouriteRelation_wlq favourite) {
        try {
            Byte contentType = favourite.getContentType();
            Long contentId = favourite.getContentId();

            if (contentType == 3) { // 小说
                NovelpostBase_wlq novel = novelpostBaseMapper.selectById(contentId);
                if (novel != null) {
                    return createNovelPost(novel);
                }
            } else if (contentType == 2) { // 图片
                ImagePostBase_wlq image = imagePostBaseMapper.selectById(contentId);
                if (image != null) {
                    return createImagePost(image);
                }
            } else if (contentType == 1) { // 文字
                TextpostBase_wlq text = textpostBaseMapper.selectById(contentId);
                if (text != null) {
                    return createTextPost(text);
                }
            }
        } catch (Exception e) {
            System.err.println("UserProfileService: 获取收藏内容详情失败: " + e.getMessage());
        }
        return null;
    }

    /**
     * 格式化时间显示
     */
    private String formatTimeAgo(Date date) {
        if (date == null)
            return "刚刚";

        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;

        long minutes = diff / (1000 * 60);
        long hours = diff / (1000 * 60 * 60);
        long days = diff / (1000 * 60 * 60 * 24);

        if (minutes < 1)
            return "刚刚";
        if (minutes < 60)
            return minutes + "分钟前";
        if (hours < 24)
            return hours + "小时前";
        if (days < 7)
            return days + "天前";
        return "一周前";
    }
    
    /**
     * 更新用户个人资料
     * 
     * @param userInfo 用户信息
     * @return 是否更新成功
     */
    public boolean updateUserProfile(UserInfo_njj userInfo) {
        try {
            System.out.println("UserProfileService: 开始更新用户资料，用户ID: " + userInfo.getUserId());
            
            // 设置更新时间
            userInfo.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            
            // 更新数据库
            int result = userInfoMapper.updateById(userInfo);
            System.out.println("UserProfileService: 数据库更新结果: " + result);
            
            return result > 0;
        } catch (Exception e) {
            System.err.println("UserProfileService: 更新用户资料失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取用户个人资料详情（用于编辑资料页面）
     * 
     * @param userId 用户ID
     * @return 用户信息实体
     */
    public UserInfo_njj getUserProfileDetail(Long userId) {
        try {
            System.out.println("UserProfileService: 开始获取用户个人资料详情，用户ID: " + userId);
            
            // 从数据库获取完整的用户信息
            UserInfo_njj userInfo = userInfoMapper.selectById(userId);
            
            if (userInfo == null) {
                System.out.println("UserProfileService: 未找到用户个人资料详情，用户ID: " + userId);
                return null;
            }
            
            System.out.println("UserProfileService: 成功获取用户个人资料详情，用户ID: " + userId);
            return userInfo;
        } catch (Exception e) {
            System.err.println("UserProfileService: 获取用户个人资料详情失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
