package Afriends_v3.controller;

import Afriends_v3.service.ImageRecService_njj;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest; // 缺失的导入

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片推荐算法控制器
 */
@RestController
@RequestMapping("/api/image-recommendation")
@CrossOrigin(origins = "*")
@Tag(name = "图片推荐服务", description = "用户标签查询、推荐内容生成接口") // 新增：接口文档标签
public class ImageRecController_njj {

        private static final Logger logger = LoggerFactory.getLogger(ImageRecController_njj.class);

        @Autowired
        private ImageRecService_njj imageRecService;

        // 可选：从 Nacos 配置中心读取默认参数（替代硬编码）
        @Value("${recommendation.default.limit:10}")
        private Integer defaultLimit;
        @Value("${recommendation.default.soft-tag-weight:70}")
        private Integer defaultSoftTagWeight;
        @Value("${recommendation.default.hard-tag-weight:25}")
        private Integer defaultHardTagWeight;
        @Value("${recommendation.default.popularity-weight:10}")
        private Integer defaultPopularityWeight;

//        /**
//         * 获取用户标签信息
//         */
//        @GetMapping("/user-tags/{userId}")
//        public Map<String, Object> getUserTagInfo(@PathVariable Long userId) {
//                logger.info("获取用户标签信息 - 用户ID: {}", userId);
//                return imageRecService.getUserTagInfo(userId);
//        }
        /**
         * 获取用户标签信息（路径参数方式）
         */
        @GetMapping("/user-tags/{userId}")
        @Operation(summary = "获取用户标签", description = "根据用户ID查询标签信息") // 新增：接口文档说明
        public Map<String, Object> getUserTagInfo(
                @Parameter(description = "用户ID", required = true, example = "1000001") // 新增：参数说明
                @PathVariable Long userId) {
            logger.info("获取用户标签信息 - 用户ID: {}", userId);
            return imageRecService.getUserTagInfo(userId);
        }

        /**
         * 生成推荐内容
         */
        @PostMapping("/generate")
        public Map<String, Object> generateRecommendations(@RequestBody Map<String, Object> request) {
                try {
                        Long userId = Long.valueOf(request.get("userId").toString());
                        Integer limit = request.get("limit") != null ? Integer.valueOf(request.get("limit").toString())
                                        : 10;
                        Integer softTagWeight = request.get("softTagWeight") != null
                                        ? Integer.valueOf(request.get("softTagWeight").toString())
                                        : 70;
                        Integer hardTagWeight = request.get("hardTagWeight") != null
                                        ? Integer.valueOf(request.get("hardTagWeight").toString())
                                        : 25;
                        Integer popularityWeight = request.get("popularityWeight") != null
                                        ? Integer.valueOf(request.get("popularityWeight").toString())
                                        : 10;

                        logger.info("生成图片推荐内容 - 用户ID: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                                        userId, limit, softTagWeight, hardTagWeight, popularityWeight);

                        return imageRecService.generateRecommendations(userId, limit, softTagWeight, hardTagWeight,
                                        popularityWeight);
                } catch (Exception e) {
                        logger.error("生成图片推荐内容失败: {}", e.getMessage(), e);
                        Map<String, Object> errorResult = new java.util.HashMap<>();
                        errorResult.put("success", false);
                        errorResult.put("message", "生成图片推荐内容失败: " + e.getMessage());
                        return errorResult;
                }
        }

        /**
         * 获取用户标签信息（GET方式）
         */
        @GetMapping("/user-tags")
        public Map<String, Object> getUserTagInfoByGet(@RequestParam Long userId) {
                logger.info("获取用户标签信息（GET方式） - 用户ID: {}", userId);
                return imageRecService.getUserTagInfo(userId);
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

                logger.info("生成图片推荐内容（GET方式） - 用户ID: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                                userId, limit, softTagWeight, hardTagWeight, popularityWeight);

                return imageRecService.generateRecommendations(userId, limit, softTagWeight, hardTagWeight,
                                popularityWeight);
        }

//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http.csrf(csrf -> csrf.disable())
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers("/api/image-recommendation/**").permitAll() // 放行图片推荐接口
//                            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ENDPOINT_ADMIN")
//                            .anyRequest().authenticated()
//                    )
//                    .httpBasic(httpBasic -> httpBasic.realmName("Afriends Actuator Realm"));
//            return http.build();
//        }
// 注意：securityFilterChain Bean 已在 AfriendsV3Application 中定义，此处不再重复定义
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.csrf(csrf -> csrf.disable()) // 禁用 CSRF（前后端分离场景常用）
//            .authorizeHttpRequests(auth -> auth
//                    // 1. 放行所有图片推荐接口（无需登录即可访问）
//                    .requestMatchers("/api/image-recommendation/**").permitAll()
//                    // 2. Actuator 监控端点需要 ADMIN 角色
//                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ENDPOINT_ADMIN")
//                    // 3. 其他所有请求需要登录认证
//                    .anyRequest().authenticated()
//            )
//            .httpBasic(httpBasic -> httpBasic
//                    .realmName("Afriends Actuator Realm") // 基础认证的域名称
//            );
//    return http.build();
//}

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

                        Integer limit = request.get("limit") != null ? Integer.valueOf(request.get("limit").toString())
                                        : 10;
                        Integer softTagWeight = request.get("softTagWeight") != null
                                        ? Integer.valueOf(request.get("softTagWeight").toString())
                                        : 70;
                        Integer hardTagWeight = request.get("hardTagWeight") != null
                                        ? Integer.valueOf(request.get("hardTagWeight").toString())
                                        : 25;
                        Integer popularityWeight = request.get("popularityWeight") != null
                                        ? Integer.valueOf(request.get("popularityWeight").toString())
                                        : 10;

                        logger.info("批量生成图片推荐内容 - 用户数量: {}, 推荐数量: {}, 软标签权重: {}, 硬标签权重: {}, 热度权重: {}",
                                        userIds != null ? userIds.size() : 0, limit, softTagWeight, hardTagWeight,
                                        popularityWeight);

                        return imageRecService.batchGenerateRecommendations(userIds, limit, softTagWeight,
                                        hardTagWeight,
                                        popularityWeight);
                } catch (Exception e) {
                        logger.error("批量生成图片推荐内容失败: {}", e.getMessage(), e);
                        Map<String, Object> errorResult = new java.util.HashMap<>();
                        errorResult.put("success", false);
                        errorResult.put("message", "批量生成图片推荐内容失败: " + e.getMessage());
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
                result.put("service", "ImageRecService");
                result.put("timestamp", System.currentTimeMillis());
                return result;
        }
}
