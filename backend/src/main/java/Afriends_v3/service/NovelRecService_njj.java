package Afriends_v3.service;

import Afriends_v3.entity.NovelRec_njj;
import Afriends_v3.entity.NovelpostBase_wlq;
import Afriends_v3.entity.NovelpostBase_list_wlq;
import Afriends_v3.entity.UserSoftTagRelation_njj;
import Afriends_v3.entity.SoftTagBase_wlq;
import Afriends_v3.entity.SoftTagBase_list_wlq;
import Afriends_v3.entity.UserSoftTagRelation_list_njj;
import Afriends_v3.mapper.NovelRecMapper_njj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 小说推荐算法服务类
 * 基于用户软硬标签进行个性化推荐
 */
@Service
public class NovelRecService_njj {

    private static final Logger logger = LoggerFactory.getLogger(NovelRecService_njj.class);

    @Autowired
    private NovelRecMapper_njj novelRecMapper;

    @Autowired
    private EFMPRTEntityService_wlq efmprtEntityService;

    /**
     * 获取用户标签信息
     */
    public Map<String, Object> getUserTagInfo(Long userId) {
        logger.info("获取用户标签信息 - 用户ID: {}", userId);

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 从内存获取用户软标签关系
            List<UserSoftTagRelation_njj> userSoftTagRelations = UserSoftTagRelation_list_njj
                    .getUserSoftTagRelationByUserId(userId);
            logger.info("用户软标签关系: {}", userSoftTagRelations);

            // 2. 构建软标签信息列表
            List<Map<String, Object>> softTags = new ArrayList<>();
            for (UserSoftTagRelation_njj relation : userSoftTagRelations) {
                SoftTagBase_wlq softTag = SoftTagBase_list_wlq.getSoftTagBaseById(relation.getSoftTagId());
                if (softTag != null) {
                    Map<String, Object> tagInfo = new HashMap<>();
                    tagInfo.put("id", softTag.getSoftTagId());
                    tagInfo.put("content", softTag.getSoftTagContent());
                    softTags.add(tagInfo);
                }
            }

            result.put("success", true);
            result.put("userId", userId);
            result.put("softTags", softTags);
            result.put("hardTags", new ArrayList<>()); // 暂时返回空列表

            logger.info("用户 {} 的软标签数量: {}", userId, softTags.size());

        } catch (Exception e) {
            logger.error("获取用户标签信息失败: userId={}, error={}", userId, e.getMessage(), e);
            result.put("success", false);
            result.put("message", "获取用户标签信息失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 生成小说推荐
     */
    public Map<String, Object> generateRecommendations(Long userId, Integer limit,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        logger.info("开始生成小说推荐 - 用户ID: {}, 推荐数量: {}", userId, limit);

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 获取用户标签信息
            Map<String, Object> tagInfo = getUserTagInfo(userId);
            if (!(Boolean) tagInfo.get("success")) {
                result.put("success", false);
                result.put("message", "获取用户标签信息失败");
                return result;
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> userSoftTags = (List<Map<String, Object>>) tagInfo.get("softTags");

            // 2. 如果没有软标签，生成默认推荐
            if (userSoftTags == null || userSoftTags.isEmpty()) {
                logger.info("用户没有软标签，生成默认推荐");
                return generateDefaultRecommendations(userId, limit);
            }

            // 3. 从内存获取所有可推荐的小说内容
            List<NovelpostBase_wlq> allNovels = NovelpostBase_list_wlq.getAllNovelpostBase();
            if (allNovels == null || allNovels.isEmpty()) {
                result.put("success", false);
                result.put("message", "没有可推荐的内容");
                return result;
            }

            // 4. 计算推荐得分
            List<RecommendationScore> scores = calculateRecommendationScores(
                    userId, allNovels, userSoftTags, softTagWeight, hardTagWeight, popularityWeight);

            // 5. 创建推荐结果
            List<NovelRec_njj> recommendations = createRecommendations(userId, scores, limit);

            // 6. 保存推荐到数据库
            saveRecommendations(recommendations);

            // 7. 获取推荐内容的详细信息
            List<Map<String, Object>> detailedRecommendations = getDetailedRecommendations(recommendations);

            // 8. 计算统计信息
            Set<Long> alreadyRecommendedContentIds = getAlreadyRecommendedContentIds(userId);
            int totalCandidates = allNovels.size();
            int skippedCount = alreadyRecommendedContentIds.size();

            result.put("success", true);
            result.put("message", "推荐生成成功");
            result.put("recommendations", detailedRecommendations);
            result.put("count", recommendations.size());
            result.put("userSoftTags", userSoftTags);
            result.put("totalCandidates", totalCandidates);
            result.put("skippedCount", skippedCount);

            logger.info("推荐生成成功 - 生成推荐数量: {}", recommendations.size());

        } catch (Exception e) {
            logger.error("生成推荐失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "生成推荐失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 生成默认推荐（热门内容）
     */
    private Map<String, Object> generateDefaultRecommendations(Long userId, Integer limit) {
        try {
            // 从内存获取热门小说内容（按点赞数排序）
            List<NovelpostBase_wlq> hotContents = NovelpostBase_list_wlq.getAllNovelpostBase();

            if (hotContents == null || hotContents.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "没有可用的小说内容");
                return result;
            }

            // 按点赞数排序，取前limit个
            hotContents.sort((a, b) -> Integer.compare(
                    b.getLikeCount() != null ? b.getLikeCount() : 0,
                    a.getLikeCount() != null ? a.getLikeCount() : 0));

            List<NovelRec_njj> recommendations = new ArrayList<>();
            for (int i = 0; i < Math.min(hotContents.size(), limit); i++) {
                NovelpostBase_wlq content = hotContents.get(i);
                NovelRec_njj recommendation = new NovelRec_njj();
                recommendation.setUserId(userId);
                recommendation.setNovelId(content.getNovelId());
                recommendation.setScore(100.0f - i * 10.0f); // 递减分数
                recommendation.setRankOrder(i + 1);
                recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                recommendations.add(recommendation);
            }

            // 保存推荐到数据库
            saveRecommendations(recommendations);

            // 获取推荐内容的详细信息
            List<Map<String, Object>> detailedRecommendations = getDetailedRecommendations(recommendations);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "默认推荐生成成功");
            result.put("recommendations", detailedRecommendations);
            result.put("count", recommendations.size());

            return result;

        } catch (Exception e) {
            logger.error("生成默认推荐失败: {}", e.getMessage(), e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "生成默认推荐失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 批量生成小说推荐
     */
    public Map<String, Object> batchGenerateRecommendations(List<Long> userIds, Integer limit,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        logger.info("开始批量生成小说推荐 - 用户数量: {}, 推荐数量: {}", userIds != null ? userIds.size() : 0, limit);

        Map<String, Object> result = new HashMap<>();

        try {
            if (userIds == null || userIds.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户ID列表不能为空");
                return result;
            }

            // 批量处理结果
            List<Map<String, Object>> batchResults = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;
            int totalRecommendations = 0;

            // 为每个用户生成推荐
            for (Long userId : userIds) {
                try {
                    logger.info("为用户 {} 生成小说推荐", userId);
                    
                    Map<String, Object> userResult = generateRecommendations(userId, limit, 
                            softTagWeight, hardTagWeight, popularityWeight);
                    
                    Map<String, Object> batchItem = new HashMap<>();
                    batchItem.put("userId", userId);
                    
                    if ((Boolean) userResult.get("success")) {
                        batchItem.put("success", true);
                        batchItem.put("recommendationCount", userResult.get("count"));
                        batchItem.put("message", "推荐生成成功");
                        
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> recommendations = (List<Map<String, Object>>) userResult.get("recommendations");
                        if (recommendations != null) {
                            totalRecommendations += recommendations.size();
                        }
                        successCount++;
                    } else {
                        batchItem.put("success", false);
                        batchItem.put("recommendationCount", 0);
                        batchItem.put("message", userResult.get("message"));
                        failCount++;
                    }
                    
                    batchResults.add(batchItem);
                    
                } catch (Exception e) {
                    logger.error("为用户 {} 生成推荐失败: {}", userId, e.getMessage(), e);
                    
                    Map<String, Object> batchItem = new HashMap<>();
                    batchItem.put("userId", userId);
                    batchItem.put("success", false);
                    batchItem.put("recommendationCount", 0);
                    batchItem.put("message", "生成推荐失败: " + e.getMessage());
                    
                    batchResults.add(batchItem);
                    failCount++;
                }
            }

            // 构建批量处理结果
            result.put("success", true);
            result.put("message", String.format("批量推荐完成 - 成功: %d, 失败: %d, 总推荐数: %d", 
                    successCount, failCount, totalRecommendations));
            result.put("totalUsers", userIds.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("totalRecommendations", totalRecommendations);
            result.put("batchResults", batchResults);
            result.put("limit", limit);
            result.put("softTagWeight", softTagWeight);
            result.put("hardTagWeight", hardTagWeight);
            result.put("popularityWeight", popularityWeight);

            logger.info("批量推荐完成 - 总用户数: {}, 成功: {}, 失败: {}, 总推荐数: {}", 
                    userIds.size(), successCount, failCount, totalRecommendations);

        } catch (Exception e) {
            logger.error("批量生成推荐失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "批量生成推荐失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 计算推荐得分
     */
    private List<RecommendationScore> calculateRecommendationScores(Long userId,
            List<NovelpostBase_wlq> allNovels,
            List<Map<String, Object>> userSoftTags,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        List<RecommendationScore> scores = new ArrayList<>();

        // 构建用户软标签字符串
        String userSoftTagString = userSoftTags.stream()
                .map(tag -> (String) tag.get("content"))
                .collect(Collectors.joining(","));

        // 获取用户已经推荐过的内容ID列表
        Set<Long> alreadyRecommendedContentIds = getAlreadyRecommendedContentIds(userId);

        for (NovelpostBase_wlq novel : allNovels) {
            // 跳过用户自己的内容
            if (novel.getUserId().equals(userId)) {
                continue;
            }

            // 跳过已经推荐过的内容
            if (alreadyRecommendedContentIds.contains(novel.getNovelId())) {
                logger.debug("跳过已推荐内容: contentId={}, userId={}", novel.getNovelId(), userId);
                continue;
            }

            // 计算各个组件的得分
            float softScore = calculateSoftTagScore(novel, userSoftTagString);
            float hardScore = calculateHardTagScore(novel, null);
            float popScore = calculatePopularityScore(novel);
            float timeScore = calculateTimeDecayScore(novel);

            // 使用权重计算最终得分
            float softWeight = (softTagWeight != null) ? softTagWeight / 100.0f : 0.4f;
            float hardWeight = (hardTagWeight != null) ? hardTagWeight / 100.0f : 0.2f;
            float popularityWeightFloat = (popularityWeight != null) ? popularityWeight / 100.0f : 0.3f;

            float finalScore = softScore * softWeight + hardScore * hardWeight + popScore * popularityWeightFloat
                    + timeScore * 0.1f;

            scores.add(
                    new RecommendationScore(novel.getNovelId(), finalScore, softScore, hardScore, popScore, timeScore));
        }

        logger.info("用户 {} 推荐计算完成，候选内容 {} 条，已推荐 {} 条", userId, scores.size(), alreadyRecommendedContentIds.size());

        return scores;
    }

    /**
     * 获取用户已经推荐过的内容ID列表
     */
    private Set<Long> getAlreadyRecommendedContentIds(Long userId) {
        try {
            // 查询用户的所有历史推荐记录
            List<NovelRec_njj> existingRecommendations = novelRecMapper.selectNovelRecsByUserId(userId);

            // 提取内容ID并去重
            return existingRecommendations.stream()
                    .map(NovelRec_njj::getNovelId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("获取用户已推荐内容失败: userId={}, error={}", userId, e.getMessage(), e);
            return new HashSet<>(); // 出错时返回空集合，不影响推荐流程
        }
    }

    /**
     * 计算软标签相似度得分
     */
    private float calculateSoftTagScore(NovelpostBase_wlq novel, String userSoftTags) {
        if (novel.getSoftTags() == null || novel.getSoftTags().isEmpty()) {
            return 0.1f; // 基础分数
        }

        if (userSoftTags == null || userSoftTags.isEmpty()) {
            return 0.3f; // 提高基础软标签分数
        }

        String[] novelSoftTags = novel.getSoftTags().split(",");
        String[] userSelectedSoftTags = userSoftTags.split(",");

        // 计算匹配的标签数量
        int matchCount = 0;
        for (String novelTag : novelSoftTags) {
            for (String userTag : userSelectedSoftTags) {
                if (novelTag.trim().equals(userTag.trim())) {
                    matchCount++;
                }
            }
        }

        // 改进匹配度计算
        if (matchCount == 0) {
            return 0.1f; // 无匹配时的基础分数
        }

        float matchRatio = (float) matchCount / Math.max(novelSoftTags.length, userSelectedSoftTags.length);
        float baseScore = matchRatio * 0.8f; // 提高基础分数权重
        float bonusScore = Math.min(matchCount * 0.2f, 0.4f); // 提高奖励分数

        return Math.min(baseScore + bonusScore, 1.0f);
    }

    /**
     * 计算硬标签匹配度得分
     */
    private float calculateHardTagScore(NovelpostBase_wlq novel, String userHardTags) {
        if (novel.getHardTags() == null || novel.getHardTags().isEmpty()) {
            return 0.1f; // 基础分数
        }

        if (userHardTags == null || userHardTags.isEmpty()) {
            return 0.2f; // 提高基础分数
        }

        String[] novelHardTags = novel.getHardTags().split(",");
        String[] userSelectedHardTags = userHardTags.split(",");

        // 计算匹配的标签数量
        int matchCount = 0;
        for (String novelTag : novelHardTags) {
            for (String userTag : userSelectedHardTags) {
                if (novelTag.trim().equals(userTag.trim())) {
                    matchCount++;
                }
            }
        }

        if (matchCount == 0) {
            return 0.1f;
        }

        float matchRatio = (float) matchCount / Math.max(novelHardTags.length, userSelectedHardTags.length);
        return Math.min(matchRatio * 0.9f + matchCount * 0.1f, 1.0f);
    }

    /**
     * 计算热度得分
     */
    private float calculatePopularityScore(NovelpostBase_wlq novel) {
        int likeCount = novel.getLikeCount() != null ? novel.getLikeCount() : 0;
        int favoriteCount = novel.getFavoriteCount() != null ? novel.getFavoriteCount() : 0;
        int commentCount = novel.getCommentCount() != null ? novel.getCommentCount() : 0;

        // 小说特殊的热度计算：考虑收藏数
        float totalEngagement = likeCount * 1.0f + favoriteCount * 2.0f + commentCount * 3.0f;

        // 使用对数函数，让热度差异更明显
        if (totalEngagement <= 0) {
            return 0.05f; // 基础分数
        }

        float popularity = (float) Math.log10(totalEngagement + 1) / 3.0f; // 归一化到0-1
        return Math.min(popularity, 1.0f); // 最大值为1.0
    }

    /**
     * 计算时间衰减得分
     */
    private float calculateTimeDecayScore(NovelpostBase_wlq novel) {
        if (novel.getCreatedAt() == null) {
            return 0.1f; // 基础分数
        }

        long currentTime = System.currentTimeMillis();
        long createTime = novel.getCreatedAt().getTime();
        long timeDiff = currentTime - createTime;

        // 改进时间衰减：7天内新内容获得更高分数
        float daysDiff = timeDiff / (24.0f * 60 * 60 * 1000);

        if (daysDiff <= 1) {
            return 1.0f; // 1天内：满分
        } else if (daysDiff <= 7) {
            return 0.8f; // 7天内：高分
        } else if (daysDiff <= 30) {
            return 0.5f; // 30天内：中等分数
        } else {
            return 0.2f; // 30天以上：低分
        }
    }

    /**
     * 创建推荐记录
     */
    private List<NovelRec_njj> createRecommendations(Long userId, List<RecommendationScore> scores, Integer limit) {
        // 按得分排序
        scores.sort((a, b) -> Float.compare(b.score, a.score));

        // 创建推荐记录
        List<NovelRec_njj> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(scores.size(), limit); i++) {
            RecommendationScore score = scores.get(i);
            NovelRec_njj recommendation = new NovelRec_njj();
            recommendation.setUserId(userId);
            recommendation.setNovelId(score.novelId);
            recommendation.setScore(score.score);
            recommendation.setRankOrder(i + 1);
            recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 设置各个组件的详细得分
            recommendation.setSoftTagScore(score.softTagScore);
            recommendation.setHardTagScore(score.hardTagScore);
            recommendation.setPopularityScore(score.popularityScore);
            recommendation.setTimeDecayScore(score.timeDecayScore);

            recommendations.add(recommendation);
        }

        return recommendations;
    }

    /**
     * 保存推荐到数据库
     */
    private void saveRecommendations(List<NovelRec_njj> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return;
        }

        // 不再删除旧的推荐记录，直接追加新推荐
        // 获取当前最大ID
        Long maxId = novelRecMapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }

        // 保存新推荐
        for (NovelRec_njj recommendation : recommendations) {
            // 生成递增的ID
            if (recommendation.getId() == null) {
                maxId++;
                recommendation.setId(maxId);
            }
            novelRecMapper.insertNovelRec(recommendation);
        }
    }

    /**
     * 获取推荐内容的详细信息
     */
    private List<Map<String, Object>> getDetailedRecommendations(List<NovelRec_njj> recommendations) {
        List<Map<String, Object>> detailedRecommendations = new ArrayList<>();

        if (recommendations == null || recommendations.isEmpty()) {
            return detailedRecommendations;
        }

        // 批量获取所有推荐内容的详细信息
        List<Long> novelIds = recommendations.stream()
                .map(NovelRec_njj::getNovelId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (novelIds.isEmpty()) {
            return detailedRecommendations;
        }

        try {
            // 创建ID到内容的映射
            Map<Long, NovelpostBase_wlq> contentMap = new HashMap<>();
            for (Long novelId : novelIds) {
                NovelpostBase_wlq content = NovelpostBase_list_wlq.getNovelpostBaseById(novelId);
                if (content != null) {
                    contentMap.put(novelId, content);
                }
            }

            // 为每个推荐结果添加详细的内容信息
            for (NovelRec_njj recommendation : recommendations) {
                Map<String, Object> detailedRecommendation = new HashMap<>();

                NovelpostBase_wlq novelContent = contentMap.get(recommendation.getNovelId());

                if (novelContent != null) {
                    detailedRecommendation.put("novelContentId", novelContent.getNovelId());
                    detailedRecommendation.put("title", novelContent.getNovelTitle());
                    detailedRecommendation.put("softTags", novelContent.getSoftTags());
                    detailedRecommendation.put("hardTags", novelContent.getHardTags());
                    detailedRecommendation.put("likeCount",
                            novelContent.getLikeCount() != null ? novelContent.getLikeCount() : 0);
                    detailedRecommendation.put("favoriteCount",
                            novelContent.getFavoriteCount() != null ? novelContent.getFavoriteCount() : 0);
                    detailedRecommendation.put("commentCount",
                            novelContent.getCommentCount() != null ? novelContent.getCommentCount() : 0);
                    detailedRecommendation.put("readingCount",
                            novelContent.getReadingCount() != null ? novelContent.getReadingCount() : 0);
                    detailedRecommendation.put("novelScore",
                            novelContent.getNovelScore() != null ? novelContent.getNovelScore() : 0.0);
                    detailedRecommendation.put("score", recommendation.getScore());
                    detailedRecommendation.put("rankOrder", recommendation.getRankOrder());
                    detailedRecommendation.put("createdAt", novelContent.getCreatedAt());
                    detailedRecommendation.put("updatedAt", novelContent.getUpdatedAt());

                    // 添加各个组件的详细得分
                    detailedRecommendation.put("softTagScore", recommendation.getSoftTagScore());
                    detailedRecommendation.put("hardTagScore", recommendation.getHardTagScore());
                    detailedRecommendation.put("popularityScore", recommendation.getPopularityScore());
                    detailedRecommendation.put("timeDecayScore", recommendation.getTimeDecayScore());
                } else {
                    // 如果找不到内容，返回基本信息
                    detailedRecommendation.put("novelContentId", recommendation.getNovelId());
                    detailedRecommendation.put("title", "小说不存在或已删除");
                    detailedRecommendation.put("score", recommendation.getScore());
                    detailedRecommendation.put("rankOrder", recommendation.getRankOrder());
                }

                detailedRecommendations.add(detailedRecommendation);
            }

        } catch (Exception e) {
            logger.error("获取推荐内容详细信息失败: {}", e.getMessage(), e);
        }

        return detailedRecommendations;
    }

    /**
     * 推荐得分内部类
     */
    private static class RecommendationScore {
        Long novelId;
        float score;
        float softTagScore;
        float hardTagScore;
        float popularityScore;
        float timeDecayScore;

        RecommendationScore(Long novelId, float score, float softTagScore, float hardTagScore, float popularityScore,
                float timeDecayScore) {
            this.novelId = novelId;
            this.score = score;
            this.softTagScore = softTagScore;
            this.hardTagScore = hardTagScore;
            this.popularityScore = popularityScore;
            this.timeDecayScore = timeDecayScore;
        }
    }
}
