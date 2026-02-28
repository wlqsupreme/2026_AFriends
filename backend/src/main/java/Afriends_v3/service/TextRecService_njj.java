package Afriends_v3.service;

import Afriends_v3.entity.TextRec_njj;
import Afriends_v3.entity.TextRec_list_njj;
import Afriends_v3.entity.UserSoftTagRelation_njj;
import Afriends_v3.entity.SoftTagBase_wlq;
import Afriends_v3.entity.SoftTagBase_list_wlq;
import Afriends_v3.entity.HardTagBase_list_wlq;
import Afriends_v3.entity.TextpostBase_wlq;
import Afriends_v3.entity.TextpostBase_list_wlq;
import Afriends_v3.entity.UserSoftTagRelation_list_njj;
import Afriends_v3.entity.UserHardTagRelation_list_njj;
import Afriends_v3.mapper.TextRecMapper_njj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文字推荐算法服务类
 * 基于用户软硬标签进行个性化推荐
 */
@Service
public class TextRecService_njj {

    private static final Logger logger = LoggerFactory.getLogger(TextRecService_njj.class);

    @Autowired
    private TextRecMapper_njj textRecMapper;

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

            // 2. 从内存获取软标签内容
            List<SoftTagBase_wlq> softTags = new ArrayList<>();
            if (userSoftTagRelations != null && !userSoftTagRelations.isEmpty()) {
                for (UserSoftTagRelation_njj relation : userSoftTagRelations) {
                    SoftTagBase_wlq tag = SoftTagBase_list_wlq.getSoftTagBaseById(relation.getSoftTagId());
                    if (tag != null) {
                        softTags.add(tag);
                    }
                }
            }

            // 3. 构建返回结果
            List<Map<String, Object>> softTagList = softTags.stream()
                    .map(tag -> {
                        Map<String, Object> tagMap = new HashMap<>();
                        tagMap.put("id", tag.getSoftTagId());
                        tagMap.put("content", tag.getSoftTagContent());
                        return tagMap;
                    })
                    .collect(Collectors.toList());

            // 硬标签暂时返回空列表，后续可以扩展
            List<Map<String, Object>> hardTagList = new ArrayList<>();

            result.put("success", true);
            result.put("userId", userId);
            result.put("softTags", softTagList);
            result.put("hardTags", hardTagList);

            logger.info("用户标签信息获取成功 - 软标签数量: {}, 硬标签数量: {}",
                    softTagList.size(), hardTagList.size());

        } catch (Exception e) {
            logger.error("获取用户标签信息失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "获取用户标签信息失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 生成推荐内容
     */
    public Map<String, Object> generateRecommendations(Long userId, Integer limit,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {
        logger.info("生成推荐内容 - 用户ID: {}, 推荐数量: {}", userId, limit);

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

            // 3. 从内存获取所有可推荐的文字内容
            List<TextpostBase_wlq> allTexts = TextpostBase_list_wlq.getAllTextpostBase();
            if (allTexts == null || allTexts.isEmpty()) {
                result.put("success", false);
                result.put("message", "没有可推荐的内容");
                return result;
            }

            // 4. 计算推荐得分
            List<RecommendationScore> scores = calculateRecommendationScores(
                    userId, allTexts, userSoftTags, softTagWeight, hardTagWeight, popularityWeight);

            // 5. 创建推荐结果
            List<TextRec_njj> recommendations = createRecommendations(userId, scores, limit);

            // 6. 保存推荐到数据库
            saveRecommendations(recommendations);

            // 7. 获取推荐内容的详细信息
            List<Map<String, Object>> detailedRecommendations = getDetailedRecommendations(recommendations);

            // 8. 计算统计信息
            Set<Long> alreadyRecommendedContentIds = getAlreadyRecommendedContentIds(userId);
            int totalCandidates = allTexts.size();
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
            // 从内存获取热门文字内容（按点赞数排序）
            List<TextpostBase_wlq> hotContents = TextpostBase_list_wlq.getAllTextpostBase();

            if (hotContents == null || hotContents.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "没有可用的文字内容");
                return result;
            }

            // 按点赞数排序，取前limit个
            hotContents.sort((a, b) -> Integer.compare(
                    b.getLikeCount() != null ? b.getLikeCount() : 0,
                    a.getLikeCount() != null ? a.getLikeCount() : 0));

            List<TextRec_njj> recommendations = new ArrayList<>();
            for (int i = 0; i < Math.min(hotContents.size(), limit); i++) {
                TextpostBase_wlq content = hotContents.get(i);
                TextRec_njj recommendation = new TextRec_njj();
                recommendation.setUserId(userId);
                recommendation.setTextContentId(content.getPostId()); // 使用postId作为内容ID
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
     * 计算推荐得分
     */
    private List<RecommendationScore> calculateRecommendationScores(Long userId,
            List<TextpostBase_wlq> allTexts,
            List<Map<String, Object>> userSoftTags,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        List<RecommendationScore> scores = new ArrayList<>();

        // 构建用户软标签字符串
        String userSoftTagString = userSoftTags.stream()
                .map(tag -> (String) tag.get("content"))
                .collect(Collectors.joining(","));

        // 获取用户已经推荐过的内容ID列表
        Set<Long> alreadyRecommendedContentIds = getAlreadyRecommendedContentIds(userId);

        for (TextpostBase_wlq text : allTexts) {
            // 跳过用户自己的内容
            if (text.getUserId().equals(userId)) {
                continue;
            }

            // 跳过已经推荐过的内容
            if (alreadyRecommendedContentIds.contains(text.getPostId())) {
                logger.debug("跳过已推荐内容: contentId={}, userId={}", text.getPostId(), userId);
                continue;
            }

            // 计算各个组件的得分
            float softScore = calculateSoftTagScore(text, userSoftTagString);
            float hardScore = calculateHardTagScore(text, null);
            float popScore = calculatePopularityScore(text);
            float timeScore = calculateTimeDecayScore(text);

            // 使用权重计算最终得分
            float softWeight = (softTagWeight != null) ? softTagWeight / 100.0f : 0.4f;
            float hardWeight = (hardTagWeight != null) ? hardTagWeight / 100.0f : 0.2f;
            float popularityWeightFloat = (popularityWeight != null) ? popularityWeight / 100.0f : 0.3f;

            float finalScore = softScore * softWeight + hardScore * hardWeight + popScore * popularityWeightFloat
                    + timeScore * 0.1f;

            scores.add(
                    new RecommendationScore(text.getPostId(), finalScore, softScore, hardScore, popScore, timeScore)); // 使用postId
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
            List<TextRec_njj> existingRecommendations = textRecMapper.selectTextRecsByUserId(userId);

            // 提取内容ID并去重
            return existingRecommendations.stream()
                    .map(TextRec_njj::getTextContentId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("获取用户已推荐内容失败: userId={}, error={}", userId, e.getMessage(), e);
            return new HashSet<>(); // 出错时返回空集合，不影响推荐流程
        }
    }

    /**
     * 计算单个内容的推荐得分
     */
    private float calculateScore(TextpostBase_wlq text, String userSoftTags,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        float score = 0.0f;

        // 调整权重参数，让得分更有差异性
        float softWeight = (softTagWeight != null) ? softTagWeight / 100.0f : 0.4f; // 降低软标签权重
        float hardWeight = (hardTagWeight != null) ? hardTagWeight / 100.0f : 0.2f; // 降低硬标签权重
        float popularityWeightFloat = (popularityWeight != null) ? popularityWeight / 100.0f : 0.3f; // 提高热度权重

        // 1. 基于软标签的相似度计算
        score += calculateSoftTagScore(text, userSoftTags) * softWeight;

        // 2. 基于硬标签的匹配度计算
        score += calculateHardTagScore(text, null) * hardWeight;

        // 3. 基于内容热度的权重
        score += calculatePopularityScore(text) * popularityWeightFloat;

        // 4. 基于时间衰减的权重
        score += calculateTimeDecayScore(text) * 0.1f;

        return score;
    }

    /**
     * 计算软标签相似度得分
     */
    private float calculateSoftTagScore(TextpostBase_wlq text, String userSoftTags) {
        if (text.getSoftTags() == null || text.getSoftTags().isEmpty()) {
            return 0.1f; // 基础分数
        }

        if (userSoftTags == null || userSoftTags.isEmpty()) {
            return 0.3f; // 提高基础软标签分数
        }

        String[] textSoftTags = text.getSoftTags().split(",");
        String[] userSelectedSoftTags = userSoftTags.split(",");

        // 计算匹配的标签数量
        int matchCount = 0;
        for (String textTag : textSoftTags) {
            for (String userTag : userSelectedSoftTags) {
                if (textTag.trim().equals(userTag.trim())) {
                    matchCount++;
                }
            }
        }

        // 改进匹配度计算
        if (matchCount == 0) {
            return 0.1f; // 无匹配时的基础分数
        }

        float matchRatio = (float) matchCount / Math.max(textSoftTags.length, userSelectedSoftTags.length);
        float baseScore = matchRatio * 0.8f; // 提高基础分数权重
        float bonusScore = Math.min(matchCount * 0.2f, 0.4f); // 提高奖励分数

        return Math.min(baseScore + bonusScore, 1.0f);
    }

    /**
     * 计算硬标签匹配度得分
     */
    private float calculateHardTagScore(TextpostBase_wlq text, String userHardTags) {
        if (text.getHardTags() == null || text.getHardTags().isEmpty()) {
            return 0.1f; // 基础分数
        }

        if (userHardTags == null || userHardTags.isEmpty()) {
            return 0.2f; // 提高基础分数
        }

        String[] textHardTags = text.getHardTags().split(",");
        String[] userSelectedHardTags = userHardTags.split(",");

        // 计算匹配的标签数量
        int matchCount = 0;
        for (String textTag : textHardTags) {
            for (String userTag : userSelectedHardTags) {
                if (textTag.trim().equals(userTag.trim())) {
                    matchCount++;
                }
            }
        }

        if (matchCount == 0) {
            return 0.1f;
        }

        float matchRatio = (float) matchCount / Math.max(textHardTags.length, userSelectedHardTags.length);
        return Math.min(matchRatio * 0.9f + matchCount * 0.1f, 1.0f);
    }

    /**
     * 计算热度得分
     */
    private float calculatePopularityScore(TextpostBase_wlq text) {
        int likeCount = text.getLikeCount() != null ? text.getLikeCount() : 0;
        int collectCount = text.getCollectCount() != null ? text.getCollectCount() : 0;
        int commentCount = text.getCommentCount() != null ? text.getCommentCount() : 0;

        // 改进热度计算：使用对数函数平滑热度差异
        float totalEngagement = likeCount * 1.0f + collectCount * 2.0f + commentCount * 3.0f;

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
    private float calculateTimeDecayScore(TextpostBase_wlq text) {
        if (text.getCreatedAt() == null) {
            return 0.1f; // 基础分数
        }

        long currentTime = System.currentTimeMillis();
        long createTime = text.getCreatedAt().getTime();
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
    private List<TextRec_njj> createRecommendations(Long userId, List<RecommendationScore> scores, Integer limit) {
        // 按得分排序
        scores.sort((a, b) -> Float.compare(b.score, a.score));

        // 创建推荐记录
        List<TextRec_njj> recommendations = new ArrayList<>();
        for (int i = 0; i < Math.min(scores.size(), limit); i++) {
            RecommendationScore score = scores.get(i);
            TextRec_njj recommendation = new TextRec_njj();
            recommendation.setUserId(userId);
            recommendation.setTextContentId(score.textContentId);
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
    private void saveRecommendations(List<TextRec_njj> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return;
        }

        // 不再删除旧的推荐记录，直接追加新推荐
        // 获取当前最大ID
        Long maxId = textRecMapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }

        // 保存新推荐
        for (TextRec_njj recommendation : recommendations) {
            // 生成递增的ID
            if (recommendation.getId() == null) {
                maxId++;
                recommendation.setId(maxId);
            }
            textRecMapper.insertTextRec(recommendation);
        }
    }

    /**
     * 获取推荐内容的详细信息
     */
    private List<Map<String, Object>> getDetailedRecommendations(List<TextRec_njj> recommendations) {
        List<Map<String, Object>> detailedRecommendations = new ArrayList<>();

        if (recommendations == null || recommendations.isEmpty()) {
            return detailedRecommendations;
        }

        // 批量获取所有推荐内容的详细信息
        List<Long> textContentIds = recommendations.stream()
                .map(TextRec_njj::getTextContentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (textContentIds.isEmpty()) {
            return detailedRecommendations;
        }

        try {
            // 创建ID到内容的映射
            Map<Long, TextpostBase_wlq> contentMap = new HashMap<>();
            for (Long contentId : textContentIds) {
                TextpostBase_wlq content = TextpostBase_list_wlq.getTextpostBaseById(contentId);
                if (content != null) {
                    contentMap.put(contentId, content);
                }
            }

            // 为每个推荐结果添加详细的内容信息
            for (TextRec_njj recommendation : recommendations) {
                Map<String, Object> detailedRecommendation = new HashMap<>();

                TextpostBase_wlq textContent = contentMap.get(recommendation.getTextContentId());

                if (textContent != null) {
                    detailedRecommendation.put("textContentId", textContent.getPostId());
                    detailedRecommendation.put("contentText", textContent.getContentText());
                    detailedRecommendation.put("softTags", textContent.getSoftTags());
                    detailedRecommendation.put("hardTags", textContent.getHardTags());
                    detailedRecommendation.put("likeCount",
                            textContent.getLikeCount() != null ? textContent.getLikeCount() : 0);
                    detailedRecommendation.put("collectCount",
                            textContent.getCollectCount() != null ? textContent.getCollectCount() : 0);
                    detailedRecommendation.put("commentCount",
                            textContent.getCommentCount() != null ? textContent.getCommentCount() : 0);
                    detailedRecommendation.put("heatScore",
                            textContent.getHeatScore() != null ? textContent.getHeatScore() : 0.0);
                    detailedRecommendation.put("score", recommendation.getScore());
                    detailedRecommendation.put("rankOrder", recommendation.getRankOrder());
                    detailedRecommendation.put("createdAt", textContent.getCreatedAt());
                    detailedRecommendation.put("updatedAt", textContent.getUpdatedAt());

                    // 添加各个组件的详细得分
                    detailedRecommendation.put("softTagScore", recommendation.getSoftTagScore());
                    detailedRecommendation.put("hardTagScore", recommendation.getHardTagScore());
                    detailedRecommendation.put("popularityScore", recommendation.getPopularityScore());
                    detailedRecommendation.put("timeDecayScore", recommendation.getTimeDecayScore());
                } else {
                    // 如果找不到内容，返回基本信息
                    detailedRecommendation.put("textContentId", recommendation.getTextContentId());
                    detailedRecommendation.put("contentText", "内容不存在或已删除");
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
     * 获取内存数据统计信息
     */
    public Map<String, Object> getMemoryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("softTagBaseCount", SoftTagBase_list_wlq.getAllSoftTagBase().size());
        stats.put("hardTagBaseCount", HardTagBase_list_wlq.getAllHardTagBase().size());
        stats.put("textContentCount", TextpostBase_list_wlq.getAllTextpostBase().size());
        stats.put("userSoftTagRelationCount", UserSoftTagRelation_list_njj.getAllUserSoftTagRelation().size());
        stats.put("userHardTagRelationCount", UserHardTagRelation_list_njj.getAllUserHardTagRelation().size());
        stats.put("lastUpdateTime", new java.util.Date());
        return stats;
    }

    /**
     * 重新加载内存数据
     */
    public void reloadMemoryData() {
        logger.info("重新加载所有内存数据...");
        efmprtEntityService.loadAllEFMPRTEntityDataToMemory();
        logger.info("内存数据重新加载完成");
    }

    /**
     * 批量生成文字推荐
     */
    public Map<String, Object> batchGenerateRecommendations(List<Long> userIds, Integer limit,
            Integer softTagWeight, Integer hardTagWeight, Integer popularityWeight) {

        logger.info("开始批量生成文字推荐 - 用户数量: {}, 推荐数量: {}", userIds != null ? userIds.size() : 0, limit);

        Map<String, Object> result = new HashMap<>();

        try {
            if (userIds == null || userIds.isEmpty()) {
                result.put("success", false);
                result.put("message", "用户ID列表不能为空");
                return result;
            }

            List<Map<String, Object>> batchResults = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;

            for (Long userId : userIds) {
                try {
                    Map<String, Object> userResult = generateRecommendations(userId, limit, softTagWeight,
                            hardTagWeight, popularityWeight);
                    userResult.put("userId", userId);
                    batchResults.add(userResult);

                    if ((Boolean) userResult.get("success")) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    logger.error("用户 {} 推荐生成失败: {}", userId, e.getMessage(), e);
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("userId", userId);
                    errorResult.put("success", false);
                    errorResult.put("message", "推荐生成失败: " + e.getMessage());
                    batchResults.add(errorResult);
                    failCount++;
                }
            }

            result.put("success", true);
            result.put("message", "批量推荐生成完成");
            result.put("totalUsers", userIds.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("batchResults", batchResults);

            logger.info("批量推荐生成完成 - 总用户数: {}, 成功: {}, 失败: {}", userIds.size(), successCount, failCount);

        } catch (Exception e) {
            logger.error("批量生成推荐失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "批量生成推荐失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 推荐得分内部类
     */
    private static class RecommendationScore {
        Long textContentId;
        float score;
        float softTagScore;
        float hardTagScore;
        float popularityScore;
        float timeDecayScore;

        RecommendationScore(Long textContentId, float score, float softTagScore, float hardTagScore,
                float popularityScore, float timeDecayScore) {
            this.textContentId = textContentId;
            this.score = score;
            this.softTagScore = softTagScore;
            this.hardTagScore = hardTagScore;
            this.popularityScore = popularityScore;
            this.timeDecayScore = timeDecayScore;
        }
    }
}
