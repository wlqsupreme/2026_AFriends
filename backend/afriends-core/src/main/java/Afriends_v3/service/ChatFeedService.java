package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 聊天动态页面服务类
 * 负责处理小说、图片、文字推荐数据的查询和整合
 * 包含缓存机制，提高查询性能
 */
@Service
public class ChatFeedService {

    @Autowired
    private UserNovelRecommendationMapper userNovelRecommendationMapper;

    @Autowired
    private UserImageRecommendationMapper userImageRecommendationMapper;

    @Autowired
    private UserTextRecommendationMapper userTextRecommendationMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;

    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;

    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    // 缓存相关
    private Map<Long, List<UserNovelRecommendation_njj>> novelRecommendationCache = new ConcurrentHashMap<>();
    private Map<Long, List<UserImageRecommendation_njj>> imageRecommendationCache = new ConcurrentHashMap<>();
    private Map<Long, List<UserTextRecommendation_njj>> textRecommendationCache = new ConcurrentHashMap<>();

    private Map<Long, NovelpostBase_wlq> novelPostCache = new ConcurrentHashMap<>();
    private Map<Long, ImagePostBase_wlq> imagePostCache = new ConcurrentHashMap<>();
    private Map<Long, TextpostBase_wlq> textPostCache = new ConcurrentHashMap<>();

    private Map<Long, UserInfo_njj> userInfoCache = new ConcurrentHashMap<>();

    // 用户互动状态缓存 (userId_contentId -> status)
    private Map<String, Map<String, Boolean>> userInteractionCache = new ConcurrentHashMap<>();

    // 缓存最后更新时间
    private long lastCacheUpdateTime = 0;
    private static final long CACHE_REFRESH_INTERVAL = 5 * 60 * 1000; // 5分钟刷新一次缓存

    /**
     * 初始化缓存 - 异步执行,避免阻塞应用启动
     */
    @PostConstruct
    public void initCache() {
        System.out.println("ChatFeedService: 启动异步缓存初始化...");
        CompletableFuture.runAsync(() -> {
            try {
                refreshAllCache();
                System.out.println("ChatFeedService: 异步缓存初始化完成");
            } catch (Exception e) {
                System.err.println("ChatFeedService: 缓存初始化失败 - " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * 刷新所有缓存
     */
    public void refreshAllCache() {
        System.out.println("ChatFeedService: 开始刷新缓存...");
        long startTime = System.currentTimeMillis();

        try {
            // 刷新推荐数据缓存
            refreshRecommendationCache();

            // 刷新帖子基础数据缓存
            refreshPostBaseCache();

            // 刷新用户信息缓存
            refreshUserInfoCache();

            // 清空用户互动状态缓存（每次刷新都重新查询）
            userInteractionCache.clear();

            lastCacheUpdateTime = System.currentTimeMillis();
            long duration = lastCacheUpdateTime - startTime;
            System.out.println("ChatFeedService: 缓存刷新完成，耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("ChatFeedService: 刷新缓存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 刷新推荐数据缓存
     */
    private void refreshRecommendationCache() {
        try {
            // 清空现有缓存
            novelRecommendationCache.clear();
            imageRecommendationCache.clear();
            textRecommendationCache.clear();

            // 加载所有推荐数据
            List<UserNovelRecommendation_njj> allNovelRecs = userNovelRecommendationMapper.selectList(null);
            List<UserImageRecommendation_njj> allImageRecs = userImageRecommendationMapper.selectList(null);
            List<UserTextRecommendation_njj> allTextRecs = userTextRecommendationMapper.selectList(null);

            // 按用户ID分组缓存
            novelRecommendationCache = allNovelRecs.stream()
                    .filter(rec -> rec.getUserId() != null)
                    .collect(Collectors.groupingBy(
                            UserNovelRecommendation_njj::getUserId,
                            Collectors.toList()
                    ));

            imageRecommendationCache = allImageRecs.stream()
                    .filter(rec -> rec.getUserId() != null)
                    .collect(Collectors.groupingBy(
                            UserImageRecommendation_njj::getUserId,
                            Collectors.toList()
                    ));

            textRecommendationCache = allTextRecs.stream()
                    .filter(rec -> rec.getUserId() != null)
                    .collect(Collectors.groupingBy(
                            UserTextRecommendation_njj::getUserId,
                            Collectors.toList()
                    ));

            System.out.println("ChatFeedService: 推荐数据缓存刷新完成 - 小说: " + allNovelRecs.size() +
                    ", 图片: " + allImageRecs.size() + ", 文字: " + allTextRecs.size());

        } catch (Exception e) {
            System.err.println("ChatFeedService: 刷新推荐数据缓存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 刷新帖子基础数据缓存
     * 优化：使用更大的分页大小，减少查询次数，避免连接泄漏
     */
    private void refreshPostBaseCache() {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 清空现有缓存（仅在分页查询成功后清空，避免缓存丢失）
            List<NovelpostBase_wlq> allNovels = new ArrayList<>();
            List<ImagePostBase_wlq> allImages = new ArrayList<>();
            List<TextpostBase_wlq> allTexts = new ArrayList<>();

            // 分页配置：每页1000条（增大页面大小，减少查询次数和连接持有时间）
            int pageSize = 1000;
            int currentPage = 1;
            int maxPages = 100; // 限制最大页数，避免查询过多数据（最多10万条）

            // -------------------------- 分页查询小说帖子 --------------------------
            IPage<NovelpostBase_wlq> novelPage;
            currentPage = 1;
            do {
                try {
                    novelPage = novelpostBaseMapper.selectPage(
                            new Page<>(currentPage, pageSize),
                            new QueryWrapper<NovelpostBase_wlq>()
                                    .orderByDesc("created_at") // 按创建时间排序，便于后续缓存时效管理
                                    .last("LIMIT " + pageSize) // 显式限制查询数量
                    );
                    if (novelPage.getRecords() != null && !novelPage.getRecords().isEmpty()) {
                        allNovels.addAll(novelPage.getRecords());
                    }
                    currentPage++;
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 分页查询小说帖子失败（第" + currentPage + "页）: " + e.getMessage());
                    break; // 遇到错误时跳出循环，避免无限重试
                }
            } while (currentPage <= novelPage.getPages() && currentPage <= maxPages);

            // -------------------------- 分页查询图片帖子 --------------------------
            currentPage = 1;
            IPage<ImagePostBase_wlq> imagePage;
            do {
                try {
                    imagePage = imagePostBaseMapper.selectPage(
                            new Page<>(currentPage, pageSize),
                            new QueryWrapper<ImagePostBase_wlq>()
                                    .orderByDesc("created_at")
                                    .last("LIMIT " + pageSize) // 显式限制查询数量
                    );
                    if (imagePage.getRecords() != null && !imagePage.getRecords().isEmpty()) {
                        allImages.addAll(imagePage.getRecords());
                    }
                    currentPage++;
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 分页查询图片帖子失败（第" + currentPage + "页）: " + e.getMessage());
                    break; // 遇到错误时跳出循环，避免无限重试
                }
            } while (currentPage <= imagePage.getPages() && currentPage <= maxPages);

            // -------------------------- 分页查询文字帖子（数据量最大，重点优化） --------------------------
            currentPage = 1;
            IPage<TextpostBase_wlq> textPage;
            do {
                try {
                    textPage = textpostBaseMapper.selectPage(
                            new Page<>(currentPage, pageSize),
                            new QueryWrapper<TextpostBase_wlq>()
                                    .orderByDesc("created_at")
                                    .last("LIMIT " + pageSize) // 显式限制查询数量
                    );
                    if (textPage.getRecords() != null && !textPage.getRecords().isEmpty()) {
                        allTexts.addAll(textPage.getRecords());
                    }
                    currentPage++;
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 分页查询文字帖子失败（第" + currentPage + "页）: " + e.getMessage());
                    break; // 遇到错误时跳出循环，避免无限重试
                }
            } while (currentPage <= textPage.getPages() && currentPage <= maxPages);

            // 2. 所有分页查询成功后，更新缓存（避免部分数据加载导致缓存不一致）
            novelPostCache.clear();
            allNovels.forEach(novel -> novelPostCache.put(novel.getNovelId(), novel));

            imagePostCache.clear();
            allImages.forEach(image -> imagePostCache.put(image.getPostId(), image));

            textPostCache.clear();
            allTexts.forEach(text -> textPostCache.put(text.getPostId(), text));

            // 3. 打印优化后的耗时日志（验证效果）
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("ChatFeedService: 帖子基础数据缓存刷新完成（分页查询）- " +
                    "小说: " + allNovels.size() + "条, " +
                    "图片: " + allImages.size() + "条, " +
                    "文字: " + allTexts.size() + "条, " +
                    "耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("ChatFeedService: 刷新帖子基础数据缓存失败（总耗时: " + (System.currentTimeMillis() - startTime) + "ms）: " + e.getMessage());
            e.printStackTrace();
            // 异常时不更新缓存，保留旧数据，避免影响用户使用
        }
    }

    /**
     * 刷新用户信息缓存
     */
    private void refreshUserInfoCache() {
        try {
            // 清空现有缓存
            userInfoCache.clear();

            // 加载所有用户信息
            List<UserInfo_njj> allUsers = userInfoMapper.selectList(null);

            // 按用户ID缓存
            userInfoCache = allUsers.stream()
                    .collect(Collectors.toMap(
                            UserInfo_njj::getUserId,
                            user -> user,
                            (existing, replacement) -> existing
                    ));

            System.out.println("ChatFeedService: 用户信息缓存刷新完成 - 用户数: " + allUsers.size());

        } catch (Exception e) {
            System.err.println("ChatFeedService: 刷新用户信息缓存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 检查并刷新缓存（如果需要）
     */
    private void checkAndRefreshCache() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdateTime > CACHE_REFRESH_INTERVAL) {
            System.out.println("ChatFeedService: 缓存过期，开始刷新...");
            refreshAllCache();
        }
    }

    /**
     * 获取用户的聊天动态数据
     * @param userId 用户ID，默认为1000001
     * @return 整合后的动态数据列表
     * @throws IllegalArgumentException 如果用户不存在
     */
    public List<Map<String, Object>> getChatFeedData(Long userId) {
        System.out.println("ChatFeedService: 开始获取用户 " + userId + " 的聊天动态数据...");
        long startTime = System.currentTimeMillis();

        // 1. 验证用户是否存在
        UserBase_wlq user = userBaseMapper.selectById(userId);
        if (user == null) {
            System.err.println("ChatFeedService: 用户不存在，userId=" + userId);
            throw new IllegalArgumentException("用户不存在");
        }

        // 2. 检查并刷新缓存
        checkAndRefreshCache();

        List<Map<String, Object>> feedData = new ArrayList<>();

        try {
            // 1. 从缓存获取小说推荐数据
            System.out.println("ChatFeedService: 从缓存获取用户小说推荐数据...");
            List<UserNovelRecommendation_njj> novelRecommendations = novelRecommendationCache.getOrDefault(userId, new ArrayList<>())
                    .stream()
                    .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                    .limit(10)
                    .collect(Collectors.toList());

            System.out.println("ChatFeedService: 从缓存获取到 " + novelRecommendations.size() + " 条小说推荐数据");

            // 处理小说推荐数据
            for (UserNovelRecommendation_njj recommendation : novelRecommendations) {
                try {
                    Long contentId = recommendation.getNovelContentId();
                    NovelpostBase_wlq novelContent = novelPostCache.get(contentId);

                    if (novelContent != null) {
                        System.out.println("ChatFeedService: 从缓存找到小说内容 - 标题: " + novelContent.getNovelTitle());
                        Map<String, Object> novelPost = createNovelPost(novelContent, recommendation, userId);
                        feedData.add(novelPost);
                    } else {
                        System.out.println("ChatFeedService: 缓存中未找到小说内容ID为 " + contentId + " 的小说");
                    }
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 处理小说推荐数据失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 2. 从缓存获取图片推荐数据
            System.out.println("ChatFeedService: 从缓存获取用户图片推荐数据...");
            List<UserImageRecommendation_njj> imageRecommendations = imageRecommendationCache.getOrDefault(userId, new ArrayList<>())
                    .stream()
                    .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                    .limit(5)
                    .collect(Collectors.toList());

            System.out.println("ChatFeedService: 从缓存获取到 " + imageRecommendations.size() + " 条图片推荐数据");

            // 处理图片推荐数据
            for (UserImageRecommendation_njj recommendation : imageRecommendations) {
                try {
                    Long contentId = recommendation.getImageContentId();
                    ImagePostBase_wlq imageContent = imagePostCache.get(contentId);

                    if (imageContent != null) {
                        System.out.println("ChatFeedService: 从缓存找到图片内容 - 内容: " + imageContent.getContentText());
                        Map<String, Object> imagePost = createImagePost(imageContent, recommendation, userId);
                        feedData.add(imagePost);
                    } else {
                        System.out.println("ChatFeedService: 缓存中未找到图片内容ID为 " + contentId + " 的图片");
                        // 创建默认图片帖子
                        Map<String, Object> defaultImagePost = createDefaultImagePost(recommendation);
                        feedData.add(defaultImagePost);
                    }
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 处理图片推荐数据失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 3. 从缓存获取文字推荐数据
            System.out.println("ChatFeedService: 从缓存获取用户文字推荐数据...");
            List<UserTextRecommendation_njj> textRecommendations = textRecommendationCache.getOrDefault(userId, new ArrayList<>())
                    .stream()
                    .sorted((a, b) -> Float.compare(b.getScore(), a.getScore()))
                    .limit(5)
                    .collect(Collectors.toList());

            System.out.println("ChatFeedService: 从缓存获取到 " + textRecommendations.size() + " 条文字推荐数据");

            // 处理文字推荐数据
            for (UserTextRecommendation_njj recommendation : textRecommendations) {
                try {
                    Long contentId = recommendation.getTextContentId();
                    TextpostBase_wlq textContent = textPostCache.get(contentId);

                    if (textContent != null) {
                        System.out.println("ChatFeedService: 从缓存找到文字内容 - 内容: " + textContent.getContentText());
                        Map<String, Object> textPost = createTextPost(textContent, recommendation, userId);
                        feedData.add(textPost);
                    } else {
                        System.out.println("ChatFeedService: 缓存中未找到文字内容ID为 " + contentId + " 的文字");
                    }
                } catch (Exception e) {
                    System.err.println("ChatFeedService: 处理文字推荐数据失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 按时间排序
            feedData.sort((a, b) -> {
                Date timeA = (Date) a.get("createdAt");
                Date timeB = (Date) b.get("createdAt");
                if (timeA == null || timeB == null) return 0;
                return timeB.compareTo(timeA);
            });

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("ChatFeedService: 用户聊天动态数据获取完成！总共 " + feedData.size() + " 条数据，耗时: " + duration + "ms");

        } catch (Exception e) {
            System.err.println("ChatFeedService: 获取聊天动态数据失败: " + e.getMessage());
            e.printStackTrace();
        }

        return feedData;
    }

    /**
     * 创建小说帖子数据
     */
    private Map<String, Object> createNovelPost(NovelpostBase_wlq novelContent, UserNovelRecommendation_njj recommendation, Long currentUserId) {
        Map<String, Object> post = new HashMap<>();
        post.put("id", novelContent.getNovelId());
        post.put("type", "novel");

        // 从缓存获取作者信息
        Map<String, Object> authorInfo = getAuthorInfoFromCache(novelContent.getUserId());
        post.put("username", authorInfo.get("username"));
        post.put("avatar", authorInfo.get("avatar"));
        post.put("isAI", authorInfo.get("isAI"));
        post.put("timeAgo", formatTimeAgo(novelContent.getCreatedAt()));

        // 小说信息
        Map<String, Object> novelInfo = new HashMap<>();
        novelInfo.put("title", novelContent.getNovelTitle());
        novelInfo.put("description", novelContent.getNovelDescription() != null ?
                novelContent.getNovelDescription().substring(0, Math.min(100, novelContent.getNovelDescription().length())) + "..." : "暂无描述");
        novelInfo.put("rating", novelContent.getNovelScore() != null ? novelContent.getNovelScore().toString() : "4.5");
        post.put("novelInfo", novelInfo);

        post.put("novelCover", novelContent.getNovelCoverUrl());
        post.put("author", novelContent.getAuthorName() != null ? novelContent.getAuthorName() : "未知作者");
        post.put("rating", novelContent.getNovelScore() != null ? novelContent.getNovelScore().toString() : "4.5");
        post.put("reviewCount", "1.2万人点评");
        post.put("readerCount", novelContent.getReadingCount() != null ? novelContent.getReadingCount().toString() + "人" : "5.6万人");
        post.put("wordCount", "12.3万字");
        post.put("updateDays", "连续更新30天");

        // 标签处理
        List<String> tags = new ArrayList<>();
        if (novelContent.getSoftTags() != null && !novelContent.getSoftTags().isEmpty()) {
            String[] tagArray = novelContent.getSoftTags().split(",");
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

        post.put("synopsis", novelContent.getNovelDescription() != null ?
                novelContent.getNovelDescription().substring(0, Math.min(200, novelContent.getNovelDescription().length())) + "..." : "暂无简介");

        // 从基础表获取互动数据
        post.put("likes", novelContent.getLikeCount() != null ? novelContent.getLikeCount() : 0);
        post.put("dislikes", novelContent.getDislikeCount() != null ? novelContent.getDislikeCount() : 0);
        post.put("favorites", novelContent.getFavoriteCount() != null ? novelContent.getFavoriteCount() : 0);
        post.put("comments", novelContent.getCommentCount() != null ? novelContent.getCommentCount() : 0);

        // 获取用户互动状态（从关系表查询）
        Map<String, Object> userInteractionStatus = getUserInteractionStatusFromCache(currentUserId, novelContent.getNovelId(), (byte) 3);
        post.put("isLiked", userInteractionStatus.get("isLiked"));
        post.put("isDisliked", userInteractionStatus.get("isDisliked"));
        post.put("isFavorited", userInteractionStatus.get("isFavorited"));

        post.put("createdAt", novelContent.getCreatedAt());

        return post;
    }

    /**
     * 创建图片帖子数据
     */
    private Map<String, Object> createImagePost(ImagePostBase_wlq imageContent, UserImageRecommendation_njj recommendation, Long currentUserId) {
        Map<String, Object> post = new HashMap<>();
        post.put("id", imageContent.getPostId());
        post.put("type", "image");

        // 从缓存获取作者信息
        Map<String, Object> authorInfo = getAuthorInfoFromCache(imageContent.getUserId());
        post.put("username", authorInfo.get("username"));
        post.put("avatar", authorInfo.get("avatar"));
        post.put("isAI", authorInfo.get("isAI"));
        post.put("timeAgo", formatTimeAgo(imageContent.getCreatedAt()));

        post.put("textContent", imageContent.getContentText());

        // 处理图片URLs
        List<String> images = new ArrayList<>();
        if (imageContent.getImageUrls() != null && !imageContent.getImageUrls().isEmpty()) {
            String[] imageArray = imageContent.getImageUrls().split(",");
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

        // 从基础表获取互动数据
        post.put("likes", imageContent.getLikeCount() != null ? imageContent.getLikeCount() : 0);
        post.put("dislikes", imageContent.getDislikeCount() != null ? imageContent.getDislikeCount() : 0);
        post.put("favorites", imageContent.getCollectCount() != null ? imageContent.getCollectCount() : 0);
        post.put("comments", imageContent.getCommentCount() != null ? imageContent.getCommentCount() : 0);

        // 获取用户互动状态（从关系表查询）
        Map<String, Object> userInteractionStatus = getUserInteractionStatusFromCache(currentUserId, imageContent.getPostId(), (byte) 2);
        post.put("isLiked", userInteractionStatus.get("isLiked"));
        post.put("isDisliked", userInteractionStatus.get("isDisliked"));
        post.put("isFavorited", userInteractionStatus.get("isFavorited"));

        post.put("createdAt", imageContent.getCreatedAt());

        return post;
    }

    /**
     * 创建默认图片帖子数据（当图片内容查询失败时使用）
     */
    private Map<String, Object> createDefaultImagePost(UserImageRecommendation_njj recommendation) {
        Map<String, Object> post = new HashMap<>();
        post.put("id", recommendation.getImageContentId());
        post.put("type", "image");
        post.put("isAI", true);
        post.put("username", "AI推荐");
        post.put("avatar", "/static/avatar-ai.png");
        post.put("timeAgo", "刚刚");

        // 默认文字内容
        post.put("textContent", "这是一条图片推荐，但图片内容暂时无法加载。");

        // 预留图片位置，但不显示图片
        List<String> images = new ArrayList<>();
        post.put("images", images);
        post.put("hasImages", false); // 标记没有图片

        // 默认互动数据
        post.put("likes", 0);
        post.put("dislikes", 0);
        post.put("favorites", 0);
        post.put("comments", 0);
        post.put("isLiked", false);
        post.put("isDisliked", false);
        post.put("isFavorited", false);

        post.put("createdAt", new java.sql.Timestamp(System.currentTimeMillis()));

        return post;
    }

    /**
     * 创建文字帖子数据
     */
    private Map<String, Object> createTextPost(TextpostBase_wlq textContent, UserTextRecommendation_njj recommendation, Long currentUserId) {
        Map<String, Object> post = new HashMap<>();
        post.put("id", textContent.getPostId());
        post.put("type", "text");

        // 从缓存获取作者信息
        Map<String, Object> authorInfo = getAuthorInfoFromCache(textContent.getUserId());
        post.put("username", authorInfo.get("username"));
        post.put("avatar", authorInfo.get("avatar"));
        post.put("isAI", authorInfo.get("isAI"));
        post.put("timeAgo", formatTimeAgo(textContent.getCreatedAt()));

        post.put("textContent", textContent.getContentText());

        // 从基础表获取互动数据
        post.put("likes", textContent.getLikeCount() != null ? textContent.getLikeCount() : 0);
        post.put("dislikes", textContent.getDislikeCount() != null ? textContent.getDislikeCount() : 0);
        post.put("favorites", textContent.getCollectCount() != null ? textContent.getCollectCount() : 0);
        post.put("comments", textContent.getCommentCount() != null ? textContent.getCommentCount() : 0);

        // 获取用户互动状态（从关系表查询）
        Map<String, Object> userInteractionStatus = getUserInteractionStatusFromCache(currentUserId, textContent.getPostId(), (byte) 1);
        post.put("isLiked", userInteractionStatus.get("isLiked"));
        post.put("isDisliked", userInteractionStatus.get("isDisliked"));
        post.put("isFavorited", userInteractionStatus.get("isFavorited"));

        post.put("createdAt", textContent.getCreatedAt());

        return post;
    }

    /**
     * 从缓存获取作者信息
     * @param userId 用户ID
     * @return 作者信息Map
     */
    private Map<String, Object> getAuthorInfoFromCache(Long userId) {
        Map<String, Object> authorInfo = new HashMap<>();

        try {
            // 从缓存查询用户信息
            UserInfo_njj userInfo = userInfoCache.get(userId);

            if (userInfo != null) {
                // 设置用户名
                String username = userInfo.getUsername();
                if (username == null || username.trim().isEmpty()) {
                    username = "用户" + userId;
                }
                authorInfo.put("username", username);

                // 设置头像
                String avatar = userInfo.getProfilePicUrl();
                if (avatar == null || avatar.trim().isEmpty()) {
                    avatar = "/static/default-avatar.png";
                }
                authorInfo.put("avatar", avatar);

                // 根据user_kind字段判断是否为AI（演员）
                boolean isAI = false;
                if (userInfo.getUserKind() != null) {
                    // 检查是否为"演员"
                    isAI = "演员".equals(userInfo.getUserKind());
                }
                authorInfo.put("isAI", isAI);

                System.out.println("ChatFeedService: 从缓存获取作者信息成功 - 用户ID: " + userId +
                        ", 用户名: " + username + ", 是否AI: " + isAI);
            } else {
                // 用户不存在，使用默认信息
                authorInfo.put("username", "用户" + userId);
                authorInfo.put("avatar", "/static/default-avatar.png");
                authorInfo.put("isAI", false);

                System.out.println("ChatFeedService: 缓存中用户ID " + userId + " 不存在，使用默认信息");
            }
        } catch (Exception e) {
            System.err.println("ChatFeedService: 从缓存获取作者信息失败 - 用户ID: " + userId + ", 错误: " + e.getMessage());
            e.printStackTrace();

            // 异常时使用默认信息
            authorInfo.put("username", "用户" + userId);
            authorInfo.put("avatar", "/static/default-avatar.png");
            authorInfo.put("isAI", false);
        }

        return authorInfo;
    }

    /**
     * 从缓存获取用户互动状态
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @return 包含isLiked, isDisliked, isFavorited的状态Map
     */
    private Map<String, Object> getUserInteractionStatusFromCache(Long userId, Long contentId, Byte contentType) {
        String cacheKey = userId + "_" + contentId;
        Map<String, Object> status = new HashMap<>();
        status.put("isLiked", false);
        status.put("isDisliked", false);
        status.put("isFavorited", false);

        try {
            // 检查缓存中是否已有该用户的互动状态
            Map<String, Boolean> userStatus = userInteractionCache.get(cacheKey);
            if (userStatus != null) {
                status.put("isLiked", userStatus.getOrDefault("isLiked", false));
                status.put("isDisliked", userStatus.getOrDefault("isDisliked", false));
                status.put("isFavorited", userStatus.getOrDefault("isFavorited", false));
                return status;
            }

            // 缓存中没有，从数据库查询
            Map<String, Boolean> interactionStatus = new HashMap<>();

            // 查询点赞状态
            ContentLikeRelation_wlq likeRelation = contentLikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );
            boolean isLiked = likeRelation != null && likeRelation.getIsActive() == 1;
            interactionStatus.put("isLiked", isLiked);

            // 查询点踩状态
            ContentDislikeRelation_wlq dislikeRelation = contentDislikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );
            boolean isDisliked = dislikeRelation != null && dislikeRelation.getIsActive() == 1;
            interactionStatus.put("isDisliked", isDisliked);

            // 查询收藏状态
            ContentFavouriteRelation_wlq favoriteRelation = contentFavouriteRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );
            boolean isFavorited = favoriteRelation != null && favoriteRelation.getIsActive() == 1;
            interactionStatus.put("isFavorited", isFavorited);

            // 缓存查询结果
            userInteractionCache.put(cacheKey, interactionStatus);

            status.put("isLiked", isLiked);
            status.put("isDisliked", isDisliked);
            status.put("isFavorited", isFavorited);

            System.out.println("ChatFeedService: 查询用户互动状态 - 用户ID: " + userId +
                    ", 内容ID: " + contentId + ", 点赞: " + isLiked + ", 点踩: " + isDisliked + ", 收藏: " + isFavorited);

        } catch (Exception e) {
            System.err.println("ChatFeedService: 查询用户互动状态失败 - 用户ID: " + userId +
                    ", 内容ID: " + contentId + ", 错误: " + e.getMessage());
            e.printStackTrace();
        }

        return status;
    }

    /**
     * 更新缓存中帖子表的点赞数
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @param isLiked 是否点赞（true=点赞，false=取消点赞）
     */
    private void updatePostLikeCountInCache(Long contentId, Byte contentType, boolean isLiked) {
        try {
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textPostCache.get(contentId);
                if (textPost != null) {
                    Integer currentLikes = textPost.getLikeCount() != null ? textPost.getLikeCount() : 0;
                    textPost.setLikeCount(isLiked ? currentLikes + 1 : Math.max(0, currentLikes - 1));
                    textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    textpostBaseMapper.updateById(textPost);

                    System.out.println("ChatFeedService: 更新文字帖子点赞数 - 帖子ID: " + contentId + ", 新点赞数: " + textPost.getLikeCount());
                }
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostCache.get(contentId);
                if (imagePost != null) {
                    Integer currentLikes = imagePost.getLikeCount() != null ? imagePost.getLikeCount() : 0;
                    imagePost.setLikeCount(isLiked ? currentLikes + 1 : Math.max(0, currentLikes - 1));
                    imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    imagePostBaseMapper.updateById(imagePost);

                    System.out.println("ChatFeedService: 更新图片帖子点赞数 - 帖子ID: " + contentId + ", 新点赞数: " + imagePost.getLikeCount());
                }
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelPostCache.get(contentId);
                if (novelPost != null) {
                    Integer currentLikes = novelPost.getLikeCount() != null ? novelPost.getLikeCount() : 0;
                    novelPost.setLikeCount(isLiked ? currentLikes + 1 : Math.max(0, currentLikes - 1));
                    novelPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    novelpostBaseMapper.updateById(novelPost);

                    System.out.println("ChatFeedService: 更新小说帖子点赞数 - 帖子ID: " + contentId + ", 新点赞数: " + novelPost.getLikeCount());
                }
            }
        } catch (Exception e) {
            System.err.println("ChatFeedService: 更新缓存中帖子点赞数失败 - 内容ID: " + contentId + ", 内容类型: " + contentType + ", 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新缓存中帖子表的点踩数
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @param isDisliked 是否点踩（true=点踩，false=取消点踩）
     */
    private void updatePostDislikeCountInCache(Long contentId, Byte contentType, boolean isDisliked) {
        try {
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textPostCache.get(contentId);
                if (textPost != null) {
                    Integer currentDislikes = textPost.getDislikeCount() != null ? textPost.getDislikeCount() : 0;
                    textPost.setDislikeCount(isDisliked ? currentDislikes + 1 : Math.max(0, currentDislikes - 1));
                    textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    textpostBaseMapper.updateById(textPost);

                    System.out.println("ChatFeedService: 更新文字帖子点踩数 - 帖子ID: " + contentId + ", 新点踩数: " + textPost.getDislikeCount());
                }
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostCache.get(contentId);
                if (imagePost != null) {
                    Integer currentDislikes = imagePost.getDislikeCount() != null ? imagePost.getDislikeCount() : 0;
                    imagePost.setDislikeCount(isDisliked ? currentDislikes + 1 : Math.max(0, currentDislikes - 1));
                    imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    imagePostBaseMapper.updateById(imagePost);

                    System.out.println("ChatFeedService: 更新图片帖子点踩数 - 帖子ID: " + contentId + ", 新点踩数: " + imagePost.getDislikeCount());
                }
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelPostCache.get(contentId);
                if (novelPost != null) {
                    Integer currentDislikes = novelPost.getDislikeCount() != null ? novelPost.getDislikeCount() : 0;
                    novelPost.setDislikeCount(isDisliked ? currentDislikes + 1 : Math.max(0, currentDislikes - 1));
                    novelPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    novelpostBaseMapper.updateById(novelPost);

                    System.out.println("ChatFeedService: 更新小说帖子点踩数 - 帖子ID: " + contentId + ", 新点踩数: " + novelPost.getDislikeCount());
                }
            }
        } catch (Exception e) {
            System.err.println("ChatFeedService: 更新缓存中帖子点踩数失败 - 内容ID: " + contentId + ", 内容类型: " + contentType + ", 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新缓存中帖子表的收藏数
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @param isFavorited 是否收藏（true=收藏，false=取消收藏）
     */
    private void updatePostFavoriteCountInCache(Long contentId, Byte contentType, boolean isFavorited) {
        try {
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textPostCache.get(contentId);
                if (textPost != null) {
                    Integer currentFavorites = textPost.getCollectCount() != null ? textPost.getCollectCount() : 0;
                    textPost.setCollectCount(isFavorited ? currentFavorites + 1 : Math.max(0, currentFavorites - 1));
                    textPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    textpostBaseMapper.updateById(textPost);

                    System.out.println("ChatFeedService: 更新文字帖子收藏数 - 帖子ID: " + contentId + ", 新收藏数: " + textPost.getCollectCount());
                }
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostCache.get(contentId);
                if (imagePost != null) {
                    Integer currentFavorites = imagePost.getCollectCount() != null ? imagePost.getCollectCount() : 0;
                    imagePost.setCollectCount(isFavorited ? currentFavorites + 1 : Math.max(0, currentFavorites - 1));
                    imagePost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    imagePostBaseMapper.updateById(imagePost);

                    System.out.println("ChatFeedService: 更新图片帖子收藏数 - 帖子ID: " + contentId + ", 新收藏数: " + imagePost.getCollectCount());
                }
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelPostCache.get(contentId);
                if (novelPost != null) {
                    Integer currentFavorites = novelPost.getFavoriteCount() != null ? novelPost.getFavoriteCount() : 0;
                    novelPost.setFavoriteCount(isFavorited ? currentFavorites + 1 : Math.max(0, currentFavorites - 1));
                    novelPost.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                    // 同时更新数据库
                    novelpostBaseMapper.updateById(novelPost);

                    System.out.println("ChatFeedService: 更新小说帖子收藏数 - 帖子ID: " + contentId + ", 新收藏数: " + novelPost.getFavoriteCount());
                }
            }
        } catch (Exception e) {
            System.err.println("ChatFeedService: 更新缓存中帖子收藏数失败 - 内容ID: " + contentId + ", 内容类型: " + contentType + ", 错误: " + e.getMessage());
            e.printStackTrace();
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
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @return 操作结果
     */
    public Map<String, Object> handleLike(Long userId, Long contentId, Byte contentType) {
        System.out.println("ChatFeedService: 处理点赞操作 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);

        try {
            // 0. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                System.err.println("ChatFeedService: 用户不存在，userId=" + userId);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 1. 验证内容类型是否有效（1=文字, 2=图片, 3=小说）
            if (contentType == null || (contentType != 1 && contentType != 2 && contentType != 3)) {
                System.err.println("ChatFeedService: 无效的内容类型，contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "无效的内容类型，有效值为：1（文字）、2（图片）、3（小说）");
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 2. 验证内容是否存在
            boolean contentExists = false;
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textpostBaseMapper.selectById(contentId);
                contentExists = (textPost != null);
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(contentId);
                contentExists = (imagePost != null);
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelpostBaseMapper.selectById(contentId);
                contentExists = (novelPost != null);
            }

            if (!contentExists) {
                System.err.println("ChatFeedService: 内容不存在，contentId=" + contentId + ", contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不存在");
                errorResponse.put("contentId", contentId);
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 3. 立即执行数据库操作
            ContentLikeRelation_wlq existingLike = contentLikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );

            boolean isLiked;
            if (existingLike != null) {
                // 已存在记录，切换状态
                existingLike.setIsActive(existingLike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentLikeRelationMapper.updateById(existingLike);
                isLiked = existingLike.getIsActive() == 1;

                System.out.println("ChatFeedService: 更新点赞状态 - " + (isLiked ? "点赞" : "取消点赞"));
            } else {
                // 创建新的点赞记录
                ContentLikeRelation_wlq newLike = new ContentLikeRelation_wlq();
                Long newId = generateNewId(contentLikeRelationMapper);
                newLike.setId(newId);
                newLike.setUserId(userId);
                newLike.setContentId(contentId);
                newLike.setIsActive((byte) 1);
                newLike.setContentType(contentType);
                newLike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newLike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                contentLikeRelationMapper.insert(newLike);
                isLiked = true;

                System.out.println("ChatFeedService: 创建新点赞记录成功，ID: " + newId);
            }

            // 第二步：更新缓存中的数据
            updatePostLikeCountInCache(contentId, contentType, isLiked);

            // 第三步：更新用户互动状态缓存
            String cacheKey = userId + "_" + contentId;
            Map<String, Boolean> userStatus = userInteractionCache.getOrDefault(cacheKey, new HashMap<>());
            userStatus.put("isLiked", isLiked);
            userInteractionCache.put(cacheKey, userStatus);

            return createResponse(true, isLiked ? "点赞成功" : "取消点赞成功", isLiked);
        } catch (Exception e) {
            System.err.println("ChatFeedService: 处理点赞操作失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "点赞操作失败: " + e.getMessage(), false);
        }
    }

    /**
     * 处理点踩操作
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @return 操作结果
     */
    public Map<String, Object> handleDislike(Long userId, Long contentId, Byte contentType) {
        System.out.println("ChatFeedService: 处理点踩操作 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);

        try {
            // 0. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                System.err.println("ChatFeedService: 用户不存在，userId=" + userId);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 1. 验证内容类型是否有效（1=文字, 2=图片, 3=小说）
            if (contentType == null || (contentType != 1 && contentType != 2 && contentType != 3)) {
                System.err.println("ChatFeedService: 无效的内容类型，contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "无效的内容类型，有效值为：1（文字）、2（图片）、3（小说）");
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 2. 验证内容是否存在
            boolean contentExists = false;
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textpostBaseMapper.selectById(contentId);
                contentExists = (textPost != null);
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(contentId);
                contentExists = (imagePost != null);
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelpostBaseMapper.selectById(contentId);
                contentExists = (novelPost != null);
            }

            if (!contentExists) {
                System.err.println("ChatFeedService: 内容不存在，contentId=" + contentId + ", contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不存在");
                errorResponse.put("contentId", contentId);
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 3. 立即执行数据库操作
            ContentDislikeRelation_wlq existingDislike = contentDislikeRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );

            boolean isDisliked;
            if (existingDislike != null) {
                // 已存在记录，切换状态
                existingDislike.setIsActive(existingDislike.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentDislikeRelationMapper.updateById(existingDislike);
                isDisliked = existingDislike.getIsActive() == 1;

                System.out.println("ChatFeedService: 更新点踩状态 - " + (isDisliked ? "点踩" : "取消点踩"));
            } else {
                // 创建新的点踩记录
                ContentDislikeRelation_wlq newDislike = new ContentDislikeRelation_wlq();
                Long newId = generateNewId(contentDislikeRelationMapper);
                newDislike.setId(newId);
                newDislike.setUserId(userId);
                newDislike.setContentId(contentId);
                newDislike.setIsActive((byte) 1);
                newDislike.setContentType(contentType);
                newDislike.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newDislike.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                contentDislikeRelationMapper.insert(newDislike);
                isDisliked = true;

                System.out.println("ChatFeedService: 创建新点踩记录成功，ID: " + newId);
            }

            // 第二步：更新缓存中的数据
            updatePostDislikeCountInCache(contentId, contentType, isDisliked);

            // 第三步：更新用户互动状态缓存
            String cacheKey = userId + "_" + contentId;
            Map<String, Boolean> userStatus = userInteractionCache.getOrDefault(cacheKey, new HashMap<>());
            userStatus.put("isDisliked", isDisliked);
            userInteractionCache.put(cacheKey, userStatus);

            return createResponse(true, isDisliked ? "点踩成功" : "取消点踩成功", isDisliked);
        } catch (Exception e) {
            System.err.println("ChatFeedService: 处理点踩操作失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "点踩操作失败: " + e.getMessage(), false);
        }
    }

    /**
     * 处理收藏操作
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型：1=文字, 2=图片, 3=小说
     * @return 操作结果
     */
    public Map<String, Object> handleFavorite(Long userId, Long contentId, Byte contentType) {
        System.out.println("ChatFeedService: 处理收藏操作 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);

        try {
            // 0. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                System.err.println("ChatFeedService: 用户不存在，userId=" + userId);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 1. 验证内容类型是否有效（1=文字, 2=图片, 3=小说）
            if (contentType == null || (contentType != 1 && contentType != 2 && contentType != 3)) {
                System.err.println("ChatFeedService: 无效的内容类型，contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "无效的内容类型，有效值为：1（文字）、2（图片）、3（小说）");
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 2. 验证内容是否存在
            boolean contentExists = false;
            if (contentType == 1) {
                // 文字帖子
                TextpostBase_wlq textPost = textpostBaseMapper.selectById(contentId);
                contentExists = (textPost != null);
            } else if (contentType == 2) {
                // 图片帖子
                ImagePostBase_wlq imagePost = imagePostBaseMapper.selectById(contentId);
                contentExists = (imagePost != null);
            } else if (contentType == 3) {
                // 小说帖子
                NovelpostBase_wlq novelPost = novelpostBaseMapper.selectById(contentId);
                contentExists = (novelPost != null);
            }

            if (!contentExists) {
                System.err.println("ChatFeedService: 内容不存在，contentId=" + contentId + ", contentType=" + contentType);
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "内容不存在");
                errorResponse.put("contentId", contentId);
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return errorResponse;
            }

            // 3. 立即执行数据库操作
            ContentFavouriteRelation_wlq existingFavorite = contentFavouriteRelationMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>()
                            .eq("user_id", userId)
                            .eq("content_id", contentId)
            );

            boolean isFavorited;
            if (existingFavorite != null) {
                // 已存在记录，切换状态
                existingFavorite.setIsActive(existingFavorite.getIsActive() == 1 ? (byte) 0 : (byte) 1);
                existingFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contentFavouriteRelationMapper.updateById(existingFavorite);
                isFavorited = existingFavorite.getIsActive() == 1;

                System.out.println("ChatFeedService: 更新收藏状态 - " + (isFavorited ? "收藏" : "取消收藏"));
            } else {
                // 创建新的收藏记录
                ContentFavouriteRelation_wlq newFavorite = new ContentFavouriteRelation_wlq();
                Long newId = generateNewId(contentFavouriteRelationMapper);
                newFavorite.setId(newId);
                newFavorite.setUserId(userId);
                newFavorite.setContentId(contentId);
                newFavorite.setFolderName("默认收藏夹"); // 默认收藏夹
                newFavorite.setIsActive((byte) 1);
                newFavorite.setContentType(contentType);
                newFavorite.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newFavorite.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                contentFavouriteRelationMapper.insert(newFavorite);
                isFavorited = true;

                System.out.println("ChatFeedService: 创建新收藏记录成功，ID: " + newId);
            }

            // 第二步：更新缓存中的数据
            updatePostFavoriteCountInCache(contentId, contentType, isFavorited);

            // 第三步：更新用户互动状态缓存
            String cacheKey = userId + "_" + contentId;
            Map<String, Boolean> userStatus = userInteractionCache.getOrDefault(cacheKey, new HashMap<>());
            userStatus.put("isFavorited", isFavorited);
            userInteractionCache.put(cacheKey, userStatus);

            return createResponse(true, isFavorited ? "收藏成功" : "取消收藏成功", isFavorited);
        } catch (Exception e) {
            System.err.println("ChatFeedService: 处理收藏操作失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "收藏操作失败: " + e.getMessage(), false);
        }
    }

    /**
     * 创建统一响应格式
     */
    private Map<String, Object> createResponse(boolean success, String message, boolean isActive) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("isActive", isActive);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * 生成从10000010开始的自增ID
     * @param mapper 对应的Mapper接口
     * @return 新的ID
     */
    private Long generateNewId(Object mapper) {
        try {
            Long maxId = 10000009L; // 起始值减1

            if (mapper instanceof ContentLikeRelationMapper) {
                // 查询点赞表的最大ID，只考虑从10000010开始的ID
                List<ContentLikeRelation_wlq> allLikes = contentLikeRelationMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentLikeRelation_wlq>());
                for (ContentLikeRelation_wlq like : allLikes) {
                    if (like.getId() != null && like.getId() >= 10000010L && like.getId() > maxId) {
                        maxId = like.getId();
                    }
                }
            } else if (mapper instanceof ContentDislikeRelationMapper) {
                // 查询点踩表的最大ID，只考虑从10000010开始的ID
                List<ContentDislikeRelation_wlq> allDislikes = contentDislikeRelationMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentDislikeRelation_wlq>());
                for (ContentDislikeRelation_wlq dislike : allDislikes) {
                    if (dislike.getId() != null && dislike.getId() >= 10000010L && dislike.getId() > maxId) {
                        maxId = dislike.getId();
                    }
                }
            } else if (mapper instanceof ContentFavouriteRelationMapper) {
                // 查询收藏表的最大ID，只考虑从10000010开始的ID
                List<ContentFavouriteRelation_wlq> allFavorites = contentFavouriteRelationMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ContentFavouriteRelation_wlq>());
                for (ContentFavouriteRelation_wlq favorite : allFavorites) {
                    if (favorite.getId() != null && favorite.getId() >= 10000010L && favorite.getId() > maxId) {
                        maxId = favorite.getId();
                    }
                }
            }

            Long newId = maxId + 1;
            System.out.println("ChatFeedService: 生成新ID: " + newId + " (忽略时间戳ID，只考虑>=10000010的ID)");
            return newId;

        } catch (Exception e) {
            System.err.println("ChatFeedService: 生成ID失败，使用默认值: " + e.getMessage());
            return 10000010L; // 如果出错，返回默认起始值
        }
    }

}
