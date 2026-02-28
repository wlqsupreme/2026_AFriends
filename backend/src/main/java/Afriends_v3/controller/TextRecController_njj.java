package Afriends_v3.controller;

import Afriends_v3.service.TextRecService_njj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文字推荐算法控制器
 */
@RestController
@RequestMapping("/api/text-recommendation")
@CrossOrigin(origins = "*")
public class TextRecController_njj {

    private static final Logger logger = LoggerFactory.getLogger(TextRecController_njj.class);

    @Autowired
    private TextRecService_njj textRecService;

    /**
     * 获取用户标签信息
     */
    @GetMapping("/user-tags/{userId}")
    public Map<String, Object> getUserTagInfo(@PathVariable Long userId) {
        logger.info("获取用户标签信息 - 用户ID: {}", userId);
        return textRecService.getUserTagInfo(userId);
    }

    /**
     * 生成推荐内容
     */
    @PostMapping("/generate")
    public Map<String, Object> generateRecommendations(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Integer limit = request.get("limit") != null ? Integer.valueOf(request.get("limit").toString()) : 10;
            Integer softTagWeight = request.get("softTagWeight") != null
                    ? Integer.valueOf(request.get("softTagWeight").toString())
                    : 70;
            Integer hardTagWeight = request.get("hardTagWeight") != null
                    ? Integer.valueOf(request.get("hardTagWeight").toString())
                    : 25;
            Integer popularityWeight = request.get("popularityWeight") != null
                    ? Integer.valueOf(request.get("popularityWeight").toString())
                    : 10;

            logger.info("生成推荐内容 - 用户ID: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                    userId, limit, softTagWeight, hardTagWeight, popularityWeight);

            return textRecService.generateRecommendations(userId, limit, softTagWeight, hardTagWeight,
                    popularityWeight);
        } catch (Exception e) {
            logger.error("生成推荐内容失败: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new java.util.HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "生成推荐内容失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 获取用户标签信息（GET方式）
     */
    @GetMapping("/user-tags")
    public Map<String, Object> getUserTagInfoByGet(@RequestParam Long userId) {
        logger.info("获取用户标签信息（GET方式） - 用户ID: {}", userId);
        return textRecService.getUserTagInfo(userId);
    }

    /**
     * 生成推荐内容（GET方式）
     */
    @GetMapping("/generate")
    public Map<String, Object> generateRecommendationsByGet(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "70") Integer softTagWeight,
            @RequestParam(defaultValue = "25") Integer hardTagWeight,
            @RequestParam(defaultValue = "10") Integer popularityWeight) {

        logger.info("生成推荐内容（GET方式） - 用户ID: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                userId, limit, softTagWeight, hardTagWeight, popularityWeight);

        return textRecService.generateRecommendations(userId, limit, softTagWeight, hardTagWeight, popularityWeight);
    }

    /**
     * 批量生成推荐内容
     */
    @PostMapping("/batch-generate")
    public Map<String, Object> batchGenerateRecommendations(@RequestBody Map<String, Object> request) {
        try {
            // 安全地转换userIds，处理Integer到Long的类型转换
            List<Long> userIds = new ArrayList<>();
            Object userIdsObj = request.get("userIds");
            if (userIdsObj instanceof List) {
                for (Object id : (List<?>) userIdsObj) {
                    if (id instanceof Integer) {
                        userIds.add(((Integer) id).longValue());
                    } else if (id instanceof Long) {
                        userIds.add((Long) id);
                    } else if (id instanceof Number) {
                        userIds.add(((Number) id).longValue());
                    } else {
                        logger.warn("Unexpected type for userId: {}", id.getClass().getName());
                    }
                }
            }

            Integer limit = request.get("limit") != null ? Integer.valueOf(request.get("limit").toString()) : 10;
            Integer softTagWeight = request.get("softTagWeight") != null
                    ? Integer.valueOf(request.get("softTagWeight").toString())
                    : 70;
            Integer hardTagWeight = request.get("hardTagWeight") != null
                    ? Integer.valueOf(request.get("hardTagWeight").toString())
                    : 25;
            Integer popularityWeight = request.get("popularityWeight") != null
                    ? Integer.valueOf(request.get("popularityWeight").toString())
                    : 10;

            logger.info("批量生成文字推荐内容 - 用户数量: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                    userIds != null ? userIds.size() : 0, limit, softTagWeight, hardTagWeight, popularityWeight);

            return textRecService.batchGenerateRecommendations(userIds, limit, softTagWeight, hardTagWeight,
                    popularityWeight);
        } catch (Exception e) {
            logger.error("批量生成文字推荐内容失败: {}", e.getMessage(), e);
            Map<String, Object> errorResult = new java.util.HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "批量生成文字推荐内容失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("status", "ok");
        result.put("service", "TextRecService");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
