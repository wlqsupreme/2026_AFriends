package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.*;
import Afriends_v3.mapper.UserBaseMapper;
import Afriends_v3.util.PrivacySettingsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * U开头实体类控制器
 * 提供所有U开头实体类数据的API接口
 */
@RestController
@RequestMapping("/api/u-entities")
@CrossOrigin(origins = "*")
public class UserEntityController {

    @Autowired
    private UserHardTagRelationService userHardTagRelationService;

    @Autowired
    private UserLikeRelationService userLikeRelationService;

    @Autowired
    private UserTextRecommendationService userTextRecommendationService;

    @Autowired
    private UserBaseEntityService userBaseEntityService;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private AccountDeletionService accountDeletionService;

    @Autowired
    private UserBaseService userBaseService;

    // UserHardTagRelation 相关接口
    @PostMapping("/user-hard-tag-relation/load")
    public ResponseEntity<Map<String, String>> loadUserHardTagRelationToMemory() {
        try {
            System.out.println("开始加载用户硬标签关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userHardTagRelationService.loadUserHardTagRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户硬标签关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户硬标签关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户硬标签关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户硬标签关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-hard-tag-relation/all")
    public ResponseEntity<List<UserHardTagRelation_njj>> getAllUserHardTagRelation() {
        try {
            List<UserHardTagRelation_njj> relations = userHardTagRelationService.getAllUserHardTagRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-hard-tag-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserHardTagRelationStatistics() {
        try {
            Map<String, Object> stats = userHardTagRelationService.getUserHardTagRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserLikeRelation 相关接口
    @PostMapping("/user-like-relation/load")
    public ResponseEntity<Map<String, String>> loadUserLikeRelationToMemory() {
        try {
            System.out.println("开始加载用户喜欢关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userLikeRelationService.loadUserLikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户喜欢关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户喜欢关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户喜欢关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-like-relation/all")
    public ResponseEntity<List<UserLikeRelation_njj>> getAllUserLikeRelation() {
        try {
            List<UserLikeRelation_njj> relations = userLikeRelationService.getAllUserLikeRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-like-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserLikeRelationStatistics() {
        try {
            Map<String, Object> stats = userLikeRelationService.getUserLikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserTextRecommendation 相关接口
    @PostMapping("/user-text-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserTextRecommendationToMemory() {
        try {
            System.out.println("开始加载用户文本推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userTextRecommendationService.loadUserTextRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户文本推荐数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户文本推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户文本推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户文本推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-text-recommendation/all")
    public ResponseEntity<List<UserTextRecommendation_njj>> getAllUserTextRecommendation() {
        try {
            List<UserTextRecommendation_njj> recommendations = userTextRecommendationService
                    .getAllUserTextRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-text-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserTextRecommendationStatistics() {
        try {
            Map<String, Object> stats = userTextRecommendationService.getUserTextRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseLikeAction 相关接口
    @PostMapping("/user-base-like-action/load")
    public ResponseEntity<Map<String, String>> loadUserBaseLikeActionToMemory() {
        try {
            System.out.println("开始加载用户基础点赞行为数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseLikeActionToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础点赞行为数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础点赞行为数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础点赞行为数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-like-action/all")
    public ResponseEntity<List<UserBaseLikeAction_njj>> getAllUserBaseLikeAction() {
        try {
            List<UserBaseLikeAction_njj> actions = userBaseEntityService.getAllUserBaseLikeActionFromMemory();
            return ResponseEntity.ok(actions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-like-action/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseLikeActionStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseLikeActionStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBasePicComment 相关接口
    @PostMapping("/user-base-pic-comment/load")
    public ResponseEntity<Map<String, String>> loadUserBasePicCommentToMemory() {
        try {
            System.out.println("开始加载用户基础图片评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBasePicCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础图片评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础图片评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础图片评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-pic-comment/all")
    public ResponseEntity<List<UserBasePicComment_njj>> getAllUserBasePicComment() {
        try {
            List<UserBasePicComment_njj> comments = userBaseEntityService.getAllUserBasePicCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-pic-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserBasePicCommentStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBasePicCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseSystemMessage 相关接口
    @PostMapping("/user-base-system-message/load")
    public ResponseEntity<Map<String, String>> loadUserBaseSystemMessageToMemory() {
        try {
            System.out.println("开始加载用户基础系统消息数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseSystemMessageToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础系统消息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础系统消息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础系统消息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-system-message/all")
    public ResponseEntity<List<UserBaseSystemMessage_njj>> getAllUserBaseSystemMessage() {
        try {
            List<UserBaseSystemMessage_njj> messages = userBaseEntityService.getAllUserBaseSystemMessageFromMemory();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-system-message/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseSystemMessageStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseSystemMessageStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseTextComment 相关接口
    @PostMapping("/user-base-text-comment/load")
    public ResponseEntity<Map<String, String>> loadUserBaseTextCommentToMemory() {
        try {
            System.out.println("开始加载用户基础文本评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseTextCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础文本评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础文本评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础文本评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-text-comment/all")
    public ResponseEntity<List<UserBaseTextComment_njj>> getAllUserBaseTextComment() {
        try {
            List<UserBaseTextComment_njj> comments = userBaseEntityService.getAllUserBaseTextCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-text-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseTextCommentStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseTextCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBaseUserCollectioin 相关接口
    @PostMapping("/user-base-user-collection/load")
    public ResponseEntity<Map<String, String>> loadUserBaseUserCollectioinToMemory() {
        try {
            System.out.println("开始加载用户基础用户收藏数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseEntityService.loadUserBaseUserCollectioinToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础用户收藏数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础用户收藏数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础用户收藏数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base-user-collection/all")
    public ResponseEntity<List<UserBaseUserCollectioin_njj>> getAllUserBaseUserCollectioin() {
        try {
            List<UserBaseUserCollectioin_njj> collections = userBaseEntityService
                    .getAllUserBaseUserCollectioinFromMemory();
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base-user-collection/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseUserCollectioinStatistics() {
        try {
            Map<String, Object> stats = userBaseEntityService.getUserBaseUserCollectioinStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserCertRecord 相关接口
    @PostMapping("/user-cert-record/load")
    public ResponseEntity<Map<String, String>> loadUserCertRecordToMemory() {
        try {
            System.out.println("开始加载用户认证记录数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserCertRecordToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户认证记录数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户认证记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户认证记录数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-cert-record/all")
    public ResponseEntity<List<UserCertRecord_njj>> getAllUserCertRecord() {
        try {
            List<UserCertRecord_njj> records = userEntityService.getAllUserCertRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-cert-record/user/{userId}")
    public ResponseEntity<List<UserCertRecord_njj>> getUserCertRecordByUserId(@PathVariable Long userId) {
        try {
            List<UserCertRecord_njj> records = userEntityService.getUserCertRecordByUserIdFromMemory(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-cert-record/statistics")
    public ResponseEntity<Map<String, Object>> getUserCertRecordStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserCertRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-cert-record/save")
    public ResponseEntity<Map<String, Object>> saveUserCertRecord(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = userEntityService.saveUserCertRecord(requestData);

            // 如果返回结果中 success 为 false，根据消息判断状态码
            if (result.containsKey("success") && !Boolean.TRUE.equals(result.get("success"))) {
                String message = result.get("message").toString();
                if (message.contains("用户不存在")) {
                    return ResponseEntity.status(404).body(result);
                }
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存认证记录失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // UserChatDetail 相关接口
    @PostMapping("/user-chat-detail/load")
    public ResponseEntity<Map<String, String>> loadUserChatDetailToMemory() {
        try {
            System.out.println("开始加载用户聊天详情数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserChatDetailToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-chat-detail/all")
    public ResponseEntity<List<UserChatDetail_njj>> getAllUserChatDetail() {
        try {
            List<UserChatDetail_njj> details = userEntityService.getAllUserChatDetailFromMemory();
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-detail/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatDetailStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserChatDetailStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserChatList 相关接口
    @PostMapping("/user-chat-list/load")
    public ResponseEntity<Map<String, String>> loadUserChatListToMemory() {
        try {
            System.out.println("开始加载用户聊天列表数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserChatListToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户聊天列表数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天列表数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-chat-list/all")
    public ResponseEntity<List<UserChatList_njj>> getAllUserChatList() {
        try {
            List<UserChatList_njj> lists = userEntityService.getAllUserChatListFromMemory();
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-list/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatListStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserChatListStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-chat-list/user/{userId}")
    public ResponseEntity<List<UserChatList_njj>> getUserChatListByUserId(@PathVariable Long userId) {
        try {
            List<UserChatList_njj> lists = userEntityService.getUserChatListByUserIdFromMemory(userId);
            return ResponseEntity.ok(lists);
        } catch (Exception e) {
            System.err.println("获取用户聊天列表失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-chat-list/clear-unread/{chatId}")
    public ResponseEntity<Map<String, String>> clearUnreadCount(@PathVariable Long chatId) {
        try {
            boolean success = userEntityService.clearUnreadCount(chatId);
            if (success) {
                Map<String, String> result = Map.of(
                        "message", "未读消息数已清零",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> result = Map.of(
                        "message", "清零未读消息数失败",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("清零未读消息数失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> result = Map.of(
                    "message", "清零未读消息数失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @PostMapping("/user-chat-list/session/create-or-get")
    public ResponseEntity<Map<String, Object>> createOrGetChatSession(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long friendId = Long.valueOf(requestData.get("friendId").toString());

            Map<String, Object> data = userEntityService.createOrGetChatSession(userId, friendId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取会话成功");
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("创建或获取会话失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建或获取会话失败: " + e.getMessage());
            response.put("data", new HashMap<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    // UserContentViewLog 相关接口
    @PostMapping("/user-content-view-log/load")
    public ResponseEntity<Map<String, String>> loadUserContentViewLogToMemory() {
        try {
            System.out.println("开始加载用户内容查看日志数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserContentViewLogToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户内容查看日志数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户内容查看日志数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户内容查看日志数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-content-view-log/all")
    public ResponseEntity<List<UserContentViewLog_njj>> getAllUserContentViewLog() {
        try {
            List<UserContentViewLog_njj> logs = userEntityService.getAllUserContentViewLogFromMemory();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-content-view-log/statistics")
    public ResponseEntity<Map<String, Object>> getUserContentViewLogStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserContentViewLogStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserDevice 相关接口
    @PostMapping("/user-device/load")
    public ResponseEntity<Map<String, String>> loadUserDeviceToMemory() {
        try {
            System.out.println("开始加载用户设备数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserDeviceToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户设备数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户设备数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户设备数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-device/all")
    public ResponseEntity<List<UserDevice_njj>> getAllUserDevice() {
        try {
            List<UserDevice_njj> devices = userEntityService.getAllUserDeviceFromMemory();
            return ResponseEntity.ok(devices);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-device/statistics")
    public ResponseEntity<Map<String, Object>> getUserDeviceStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserDeviceStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据用户ID获取设备列表
     */
    @GetMapping("/user-device/list/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDevicesByUserId(@PathVariable Long userId) {
        try {
            List<UserDevice_njj> devices = userEntityService.getUserDevicesByUserId(userId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", devices);
            result.put("count", devices.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取用户设备列表失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "获取设备列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/user-device/{deviceId}")
    public ResponseEntity<Map<String, Object>> deleteUserDevice(
            @PathVariable Long deviceId,
            @RequestParam Long userId) {
        try {
            boolean success = userEntityService.deleteUserDevice(userId, deviceId);

            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("success", true);
                result.put("message", "设备删除成功");
            } else {
                result.put("success", false);
                result.put("message", "设备删除失败，可能是当前设备或设备不存在");
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("删除设备失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "删除设备失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 批量删除设备
     */
    @PostMapping("/user-device/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteUserDevices(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            @SuppressWarnings("unchecked")
            List<Long> deviceIds = (List<Long>) requestData.get("deviceIds");

            boolean success = userEntityService.batchDeleteUserDevices(userId, deviceIds);

            Map<String, Object> result = new HashMap<>();
            if (success) {
                result.put("success", true);
                result.put("message", "批量删除成功");
            } else {
                result.put("success", false);
                result.put("message", "批量删除失败");
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("批量删除设备失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 记录登录设备信息
     */
    @PostMapping("/user-device/record-login")
    public ResponseEntity<Map<String, Object>> recordLoginDevice(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String deviceName = (String) requestData.get("deviceName");
            String deviceType = (String) requestData.get("deviceType");
            String deviceModel = (String) requestData.get("deviceModel");
            String deviceIdentifier = (String) requestData.get("deviceIdentifier");
            String loginLocation = (String) requestData.get("loginLocation");
            String loginIp = (String) requestData.get("loginIp");

            UserDevice_njj device = userEntityService.recordLoginDevice(
                    userId, deviceName, deviceType, deviceModel,
                    deviceIdentifier, loginLocation, loginIp);

            Map<String, Object> result = new HashMap<>();
            if (device != null) {
                result.put("success", true);
                result.put("data", device);
                result.put("message", "设备信息记录成功");
            } else {
                result.put("success", false);
                result.put("message", "设备信息记录失败");
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("记录登录设备信息失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "记录设备信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 检查设备是否为信任设备
     */
    @GetMapping("/user-device/check-trusted")
    public ResponseEntity<Map<String, Object>> checkDeviceTrusted(
            @RequestParam Long userId,
            @RequestParam String deviceIdentifier) {
        try {
            boolean isTrusted = userEntityService.isDeviceTrusted(userId, deviceIdentifier);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("isTrusted", isTrusted);
            result.put("message", isTrusted ? "设备是信任设备" : "设备不是信任设备");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("检查设备信任状态失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "检查设备信任状态失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // UserDislikeRelation 相关接口
    @PostMapping("/user-dislike-relation/load")
    public ResponseEntity<Map<String, String>> loadUserDislikeRelationToMemory() {
        try {
            System.out.println("开始加载用户不喜欢关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserDislikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户不喜欢关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户不喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户不喜欢关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-dislike-relation/all")
    public ResponseEntity<List<UserDislikeRelation_njj>> getAllUserDislikeRelation() {
        try {
            List<UserDislikeRelation_njj> relations = userEntityService.getAllUserDislikeRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-dislike-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserDislikeRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserDislikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserFriendsRelationship 相关接口
    @PostMapping("/user-friends-relationship/load")
    public ResponseEntity<Map<String, String>> loadUserFriendsRelationshipToMemory() {
        try {
            System.out.println("开始加载用户好友关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserFriendsRelationshipToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户好友关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户好友关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户好友关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-friends-relationship/all")
    public ResponseEntity<List<UserFriendsRelationship_njj>> getAllUserFriendsRelationship() {
        try {
            List<UserFriendsRelationship_njj> relationships = userEntityService
                    .getAllUserFriendsRelationshipFromMemory();
            return ResponseEntity.ok(relationships);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-friends-relationship/user/{userId}")
    public ResponseEntity<List<UserInfo_njj>> getMentionableFriends(@PathVariable Long userId) {
        try {
            // 获取用户好友关系列表
            List<UserFriendsRelationship_njj> friendships = userEntityService
                    .getUserFriendsRelationshipByUserIdFromMemory(userId);

            // 转换为用户信息列表
            List<UserInfo_njj> friends = new ArrayList<>();
            for (UserFriendsRelationship_njj relation : friendships) {
                if (relation.getFunctionId() == null || !relation.getFunctionId().equals(20000002L)) {
                    continue;
                }
                UserInfo_njj friend = UserInfo_list_njj.getUserById(relation.getFriendsId());
                if (friend != null) {
                    // 检查隐私设置（是否允许被@）
                    if (PrivacySettingsUtil.canFriendAt(friend.getUserId(), userId)) {
                        friends.add(friend);
                    }
                }
            }

            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            System.err.println("获取可提及好友列表失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-friends-relationship/user/{userId}/search")
    public ResponseEntity<List<UserInfo_njj>> searchMentionableFriends(
            @PathVariable Long userId,
            @RequestParam String keyword) {
        try {
            // 获取可提及的好友列表
            List<UserInfo_njj> allFriends = getMentionableFriends(userId).getBody();

            // 进行模糊匹配
            List<UserInfo_njj> matchedFriends = new ArrayList<>();
            if (allFriends != null) {
                for (UserInfo_njj friend : allFriends) {
                    if (friend.getUsername() != null &&
                            friend.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                        matchedFriends.add(friend);
                    }
                }
            }

            return ResponseEntity.ok(matchedFriends);
        } catch (Exception e) {
            System.err.println("搜索可提及好友列表失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base/phones")
    public ResponseEntity<List<Map<String, Object>>> listUserPhones() {
        List<UserBase_wlq> users = userBaseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq>()
                        .isNotNull("login_tel_account")
                        .ne("login_tel_account", "")
                        .last("LIMIT 100"));
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserBase_wlq u : users) {
            Map<String, Object> item = new HashMap<>();
            item.put("userId", u.getUserId());
            item.put("phone", u.getLoginTelAccount());
            result.add(item);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 搜索所有用户用于@提及功能
     * 
     * @param userId  当前用户ID
     * @param keyword 搜索关键词
     * @return 匹配的用户列表
     */
    @GetMapping("/user-friends-relationship/user/{userId}/search-all")
    public ResponseEntity<List<UserInfo_njj>> searchAllUsersForMention(
            @PathVariable Long userId,
            @RequestParam String keyword) {
        try {
            // 获取所有用户并进行模糊匹配
            List<UserInfo_njj> allUsers = UserInfo_list_njj.getAllUsers();

            // 进行模糊匹配
            List<UserInfo_njj> matchedUsers = new ArrayList<>();
            if (allUsers != null) {
                for (UserInfo_njj user : allUsers) {
                    // 排除自己
                    if (user.getUserId().equals(userId)) {
                        continue;
                    }

                    // 检查被搜索的用户是否允许当前用户@自己
                    if (!PrivacySettingsUtil.canFriendAt(user.getUserId(), userId)) {
                        continue;
                    }

                    if (user.getUsername() != null &&
                            user.getUsername().toLowerCase().contains(keyword.toLowerCase())) {
                        matchedUsers.add(user);
                    }
                }
            }

            return ResponseEntity.ok(matchedUsers);
        } catch (Exception e) {
            System.err.println("搜索所有用户失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-friends-relationship/statistics")
    public ResponseEntity<Map<String, Object>> getUserFriendsRelationshipStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserFriendsRelationshipStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-friends-relationship/save-settings")
    public ResponseEntity<Map<String, Object>> saveUserFriendsSettings(@RequestBody Map<String, Object> settingsData) {
        try {
            Map<String, Object> result = userEntityService.saveUserFriendsSettings(settingsData);
            
            // 检查服务层返回的结果，如果是错误响应，返回相应的HTTP状态码
            if (result.containsKey("success") && Boolean.FALSE.equals(result.get("success"))) {
                String message = result.get("message") != null ? result.get("message").toString() : "";
                // 无效的设置ID或设置值 -> 400
                if (message.contains("无效的设置ID") || message.contains("设置值不能为空") || message.contains("设置值格式无效")) {
                    return ResponseEntity.badRequest().body(result);
                }
                // 其他错误 -> 400
                return ResponseEntity.badRequest().body(result);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("保存用户好友设置失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "保存设置失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/user-friends-relationship/settings/{userId}/{friendId}")
    public ResponseEntity<Map<String, Object>> getUserFriendsSettings(@PathVariable Long userId,
            @PathVariable Long friendId) {
        try {
            Map<String, Object> settings = userEntityService.getUserFriendsSettings(userId, friendId);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            System.err.println("获取用户好友设置失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/friend/search")
    public ResponseEntity<Map<String, Object>> searchUsersForAddFriend(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam(required = false) String source) {
        try {
            List<UserInfo_njj> users = userEntityService.searchUsersByUsernameForAddFriend(userId, keyword, source);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "搜索成功");
            response.put("data", users);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("搜索用户用于添加好友失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/friend/recommend/{userId}")
    public ResponseEntity<Map<String, Object>> recommendFriends(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer limit) {
        try {
            boolean enabled = userEntityService.getUserSettingBoolean(userId, "privacy.recommendFriends", true);
            if (!enabled) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "推荐已关闭");
                response.put("disabled", true);
                response.put("data", new ArrayList<>());
                response.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(response);
            }

            List<Map<String, Object>> data = userEntityService.getFriendRecommendations(userId, limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取推荐成功");
            response.put("disabled", false);
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取好友推荐失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取推荐失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/friend/remove")
    public ResponseEntity<Map<String, Object>> removeFriend(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long friendId = Long.valueOf(requestData.get("friendId").toString());
            String message = userEntityService.removeFriend(userId, friendId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("删除好友失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除好友失败: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/friend/list/{userId}")
    public ResponseEntity<Map<String, Object>> getFriendList(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> data = userEntityService.getFriendListByUserIdFromMemory(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取好友列表成功");
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取好友列表失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取好友列表失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/friend/request")
    public ResponseEntity<Map<String, Object>> requestAddFriend(@RequestBody Map<String, Object> requestData) {
        try {
            Long fromUserId = Long.valueOf(requestData.get("fromUserId").toString());
            Long toUserId = Long.valueOf(requestData.get("toUserId").toString());
            String message = requestData.get("message") != null ? requestData.get("message").toString() : "";
            String source = requestData.get("source") != null ? requestData.get("source").toString() : "PHONE_SEARCH";

            Map<String, Object> data = userEntityService.requestAddFriend(fromUserId, toUserId, message, source);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.valueOf(data.getOrDefault("message", "操作成功")));
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("发起好友申请失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "发起好友申请失败: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/friend/requests/inbox/{userId}")
    public ResponseEntity<Map<String, Object>> getFriendRequestsInbox(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> data = userEntityService.getFriendRequestsInbox(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取收到的好友申请失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/friend/requests/outbox/{userId}")
    public ResponseEntity<Map<String, Object>> getFriendRequestsOutbox(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> data = userEntityService.getFriendRequestsOutbox(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取成功");
            response.put("data", data);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取发出的好友申请失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
            response.put("data", new ArrayList<>());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/friend/request/accept")
    public ResponseEntity<Map<String, Object>> acceptFriendRequest(@RequestBody Map<String, Object> requestData) {
        try {
            Long requestId = Long.valueOf(requestData.get("requestId").toString());
            String message = userEntityService.acceptFriendRequest(requestId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("同意好友申请失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "同意失败: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/friend/request/reject")
    public ResponseEntity<Map<String, Object>> rejectFriendRequest(@RequestBody Map<String, Object> requestData) {
        try {
            Long requestId = Long.valueOf(requestData.get("requestId").toString());
            String message = userEntityService.rejectFriendRequest(requestId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", message);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("拒绝好友申请失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "拒绝失败: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }

    // UserImageRecommendation 相关接口
    @PostMapping("/user-image-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserImageRecommendationToMemory() {
        try {
            System.out.println("开始加载用户图片推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserImageRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户图片推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户图片推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户图片推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-image-recommendation/all")
    public ResponseEntity<List<UserImageRecommendation_njj>> getAllUserImageRecommendation() {
        try {
            List<UserImageRecommendation_njj> recommendations = userEntityService
                    .getAllUserImageRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-image-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserImageRecommendationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserImageRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserInfoFeatureVector 相关接口
    @PostMapping("/user-info-feature-vector/load")
    public ResponseEntity<Map<String, String>> loadUserInfoFeatureVectorToMemory() {
        try {
            System.out.println("开始加载用户信息特征向量数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserInfoFeatureVectorToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户信息特征向量数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户信息特征向量数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户信息特征向量数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-info-feature-vector/all")
    public ResponseEntity<List<UserInfoFeatureVector_njj>> getAllUserInfoFeatureVector() {
        try {
            List<UserInfoFeatureVector_njj> vectors = userEntityService.getAllUserInfoFeatureVectorFromMemory();
            return ResponseEntity.ok(vectors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-info-feature-vector/statistics")
    public ResponseEntity<Map<String, Object>> getUserInfoFeatureVectorStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserInfoFeatureVectorStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserInfoQuestion 相关接口
    @PostMapping("/user-info-question/load")
    public ResponseEntity<Map<String, String>> loadUserInfoQuestionToMemory() {
        try {
            System.out.println("开始加载用户信息问题数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserInfoQuestionToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户信息问题数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户信息问题数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户信息问题数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-info-question/all")
    public ResponseEntity<List<UserInfoQuestion_njj>> getAllUserInfoQuestion() {
        try {
            List<UserInfoQuestion_njj> questions = userEntityService.getAllUserInfoQuestionFromMemory();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-info-question/statistics")
    public ResponseEntity<Map<String, Object>> getUserInfoQuestionStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserInfoQuestionStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserNovelRecommendation 相关接口
    @PostMapping("/user-novel-recommendation/load")
    public ResponseEntity<Map<String, String>> loadUserNovelRecommendationToMemory() {
        try {
            System.out.println("开始加载用户小说推荐数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserNovelRecommendationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户小说推荐数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户小说推荐数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户小说推荐数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-novel-recommendation/all")
    public ResponseEntity<List<UserNovelRecommendation_njj>> getAllUserNovelRecommendation() {
        try {
            List<UserNovelRecommendation_njj> recommendations = userEntityService
                    .getAllUserNovelRecommendationFromMemory();
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-novel-recommendation/statistics")
    public ResponseEntity<Map<String, Object>> getUserNovelRecommendationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserNovelRecommendationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserNovelRelation 相关接口
    @PostMapping("/user-novel-relation/load")
    public ResponseEntity<Map<String, String>> loadUserNovelRelationToMemory() {
        try {
            System.out.println("开始加载用户小说关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserNovelRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户小说关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户小说关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户小说关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-novel-relation/all")
    public ResponseEntity<List<UserNovelRelation_njj>> getAllUserNovelRelation() {
        try {
            List<UserNovelRelation_njj> relations = userEntityService.getAllUserNovelRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-novel-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserNovelRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserNovelRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserReviewBase 相关接口
    @PostMapping("/user-review-base/load")
    public ResponseEntity<Map<String, String>> loadUserReviewBaseToMemory() {
        try {
            System.out.println("开始加载用户评论基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserReviewBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户评论基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户评论基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户评论基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-review-base/all")
    public ResponseEntity<List<UserReviewBase_njj>> getAllUserReviewBase() {
        try {
            List<UserReviewBase_njj> reviews = userEntityService.getAllUserReviewBaseFromMemory();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-review-base/statistics")
    public ResponseEntity<Map<String, Object>> getUserReviewBaseStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserReviewBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSettingRelation 相关接口
    @PostMapping("/user-setting-relation/load")
    public ResponseEntity<Map<String, String>> loadUserSettingRelationToMemory() {
        try {
            System.out.println("开始加载用户设置关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSettingRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户设置关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户设置关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户设置关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-setting-relation/all")
    public ResponseEntity<List<UserSettingRelation_njj>> getAllUserSettingRelation() {
        try {
            List<UserSettingRelation_njj> relations = userEntityService.getAllUserSettingRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-setting-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserSettingRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSettingRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSoftTagRelation 相关接口
    @PostMapping("/user-soft-tag-relation/load")
    public ResponseEntity<Map<String, String>> loadUserSoftTagRelationToMemory() {
        try {
            System.out.println("开始加载用户软标签关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSoftTagRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户软标签关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户软标签关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户软标签关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-soft-tag-relation/all")
    public ResponseEntity<List<UserSoftTagRelation_njj>> getAllUserSoftTagRelation() {
        try {
            List<UserSoftTagRelation_njj> relations = userEntityService.getAllUserSoftTagRelationFromMemory();
            return ResponseEntity.ok(relations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-soft-tag-relation/statistics")
    public ResponseEntity<Map<String, Object>> getUserSoftTagRelationStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSoftTagRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserSystemMessage 相关接口
    @PostMapping("/user-system-message/load")
    public ResponseEntity<Map<String, String>> loadUserSystemMessageToMemory() {
        try {
            System.out.println("开始加载用户系统消息数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserSystemMessageToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户系统消息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户系统消息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户系统消息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-system-message/all")
    public ResponseEntity<List<UserSystemMessage_njj>> getAllUserSystemMessage() {
        try {
            List<UserSystemMessage_njj> messages = userEntityService.getAllUserSystemMessageFromMemory();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-system-message/statistics")
    public ResponseEntity<Map<String, Object>> getUserSystemMessageStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserSystemMessageStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserTaskRelationship 相关接口
    @PostMapping("/user-task-relationship/load")
    public ResponseEntity<Map<String, String>> loadUserTaskRelationshipToMemory() {
        try {
            System.out.println("开始加载用户任务关系数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserTaskRelationshipToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户任务关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户任务关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户任务关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-task-relationship/all")
    public ResponseEntity<List<UserTaskRelationship_njj>> getAllUserTaskRelationship() {
        try {
            List<UserTaskRelationship_njj> relationships = userEntityService.getAllUserTaskRelationshipFromMemory();
            return ResponseEntity.ok(relationships);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-task-relationship/statistics")
    public ResponseEntity<Map<String, Object>> getUserTaskRelationshipStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserTaskRelationshipStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserAiComment 相关接口
    @PostMapping("/user-ai-comment/load")
    public ResponseEntity<Map<String, String>> loadUserAiCommentToMemory() {
        try {
            System.out.println("开始加载用户AI评论数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserAiCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-comment/all")
    public ResponseEntity<List<UserAiComment_njj>> getAllUserAiComment() {
        try {
            List<UserAiComment_njj> comments = userEntityService.getAllUserAiCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-comment/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiCommentStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserAiCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-comment/aimodel/{aimodelId}")
    public ResponseEntity<List<UserAiComment_njj>> getUserAiCommentByAimodelId(@PathVariable Long aimodelId) {
        try {
            System.out.println("UserEntityController: 查询AI模型评论，aimodelId: " + aimodelId);
            List<UserAiComment_njj> comments = userEntityService.getUserAiCommentByAimodelIdFromMemory(aimodelId);
            System.out.println("UserEntityController: 查询到 " + (comments != null ? comments.size() : 0) + " 条评论");
            
            // 如果结果为空，尝试检查缓存是否已加载
            if (comments == null || comments.isEmpty()) {
                System.out.println("UserEntityController: 警告 - 未找到 aimodelId=" + aimodelId + " 的评论，可能原因：");
                System.out.println("  1. 数据未加载到内存，请先调用 POST /api/u-entities/user-ai-comment/load");
                System.out.println("  2. 数据库中不存在该 aimodelId 的评论数据");
                
                // 返回空列表而不是 null
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            System.err.println("UserEntityController: 查询AI模型评论失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @PostMapping("/user-ai-comment/save")
    public ResponseEntity<Map<String, Object>> saveUserAiComment(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = userEntityService.saveUserAiComment(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存AI评价失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // UserAiModel 相关接口
    @PostMapping("/user-ai-model/load")
    public ResponseEntity<Map<String, String>> loadUserAiModelToMemory() {
        try {
            System.out.println("开始加载用户AI模型数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService.loadUserAiModelToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-model/all")
    public ResponseEntity<List<UserAiModel_njj>> getAllUserAiModel() {
        try {
            List<UserAiModel_njj> models = userEntityService.getAllUserAiModelFromMemory();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-model/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiModelStatistics() {
        try {
            Map<String, Object> stats = userEntityService.getUserAiModelStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/user-ai-model/purchase")
    public ResponseEntity<Map<String, Object>> purchaseAiModel(@RequestBody Map<String, Object> requestData) {
        try {
            // 获取请求参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long parentModelId = Long.valueOf(requestData.get("parentModelId").toString());
            String modelName = requestData.get("modelName").toString();
            String modelDesc = requestData.get("modelDesc").toString();
            String modelImageUrl = requestData.get("modelImageUrl").toString();
            Double price = Double.valueOf(requestData.get("price").toString());

            // 调用服务层购买方法
            Long userAiId = userEntityService.purchaseAiModel(
                    userId,
                    parentModelId,
                    modelName,
                    modelDesc,
                    modelImageUrl,
                    java.math.BigDecimal.valueOf(price));

            Map<String, Object> result = new HashMap<>();
            if (userAiId != null) {
                result.put("success", true);
                result.put("userAiId", userAiId);
                result.put("message", "AI模型购买成功");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "AI模型购买失败");
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("购买AI模型失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "购买失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // FriendsProfile 相关接口
    @GetMapping("/friends-profile/{userId}/{friendId}")
    public ResponseEntity<Map<String, Object>> getFriendsProfile(@PathVariable Long userId,
            @PathVariable Long friendId) {
        try {
            Map<String, Object> profile = userEntityService.getFriendsProfile(userId, friendId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            System.err.println("获取朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/friends-profile/save")
    public ResponseEntity<Map<String, Object>> saveFriendsProfile(@RequestBody Map<String, Object> profileData) {
        try {
            Map<String, Object> result = userEntityService.saveFriendsProfile(profileData);
            
            // 检查服务层返回的结果，如果是错误响应，返回相应的HTTP状态码
            if (result.containsKey("success") && Boolean.FALSE.equals(result.get("success"))) {
                String message = result.get("message") != null ? result.get("message").toString() : "";
                // 用户不存在或好友不存在 -> 404
                if (message.contains("用户不存在") || message.contains("好友不存在")) {
                    return ResponseEntity.status(404).body(result);
                }
                // 其他错误 -> 400
                return ResponseEntity.badRequest().body(result);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("保存朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存朋友资料失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // UserBase 相关接口
    /**
     * 根据用户ID获取用户基础信息（包括手机号）
     */
    @GetMapping("/user-base/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBaseInfo(@PathVariable Long userId) {
        try {
            System.out.println("开始获取用户基础信息，用户ID: " + userId);

            // 从数据库查询用户基础信息
            UserBase_wlq userBase = userBaseMapper.selectById(userId);

            if (userBase == null) {
                System.out.println("未找到用户ID为 " + userId + " 的用户基础信息");
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "用户不存在");
                return ResponseEntity.status(404).body(errorResult);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("userId", userBase.getUserId());
            result.put("loginTelAccount", userBase.getLoginTelAccount());
            result.put("hasPhoneBound",
                    userBase.getLoginTelAccount() != null && !userBase.getLoginTelAccount().isEmpty());
            result.put("hasPassword", userBase.getPasswordHash() != null && !userBase.getPasswordHash().isEmpty());

            System.out.println("成功获取用户基础信息，用户ID: " + userId + ", 手机号: " + userBase.getLoginTelAccount());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取用户基础信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "获取用户基础信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 绑定或更换手机号
     */
    @PutMapping("/user-base/phone")
    public ResponseEntity<Map<String, Object>> updateUserPhone(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String phoneNumber = requestData.get("phoneNumber").toString();
            String operationType = requestData.get("operationType") != null
                    ? requestData.get("operationType").toString()
                    : "bind";

            System.out.println("开始" + (operationType.equals("bind") ? "绑定" : "更换") + "手机号，用户ID: " + userId + ", 手机号: "
                    + phoneNumber);

            // 验证手机号格式
            if (phoneNumber == null || !phoneNumber.matches("^1[3-9]\\d{9}$")) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "手机号格式不正确");
                return ResponseEntity.status(400).body(errorResult);
            }

            // 检查手机号是否已被其他用户绑定
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserBase_wlq> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("login_tel_account", phoneNumber);
            UserBase_wlq existingUser = userBaseMapper.selectOne(queryWrapper);

            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "该手机号已被其他账号绑定");
                return ResponseEntity.status(400).body(errorResult);
            }

            // 更新手机号
            UpdateWrapper<UserBase_wlq> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId).set("login_tel_account", phoneNumber);
            int updateCount = userBaseMapper.update(null, updateWrapper);

            if (updateCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", operationType.equals("bind") ? "手机号绑定成功" : "手机号更换成功");
                result.put("phoneNumber", phoneNumber);
                System.out.println("成功" + (operationType.equals("bind") ? "绑定" : "更换") + "手机号，用户ID: " + userId);
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "更新失败，用户不存在");
                return ResponseEntity.status(404).body(errorResult);
            }
        } catch (Exception e) {
            System.err.println("绑定/更换手机号失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "绑定/更换手机号失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 解绑手机号
     */
    @PutMapping("/user-base/phone/unbind")
    public ResponseEntity<Map<String, Object>> unbindUserPhone(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());

            System.out.println("开始解绑手机号，用户ID: " + userId);

            // 更新手机号为空
            UpdateWrapper<UserBase_wlq> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId).set("login_tel_account", null);
            int updateCount = userBaseMapper.update(null, updateWrapper);

            if (updateCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "手机号解绑成功");
                System.out.println("成功解绑手机号，用户ID: " + userId);
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "解绑失败，用户不存在");
                return ResponseEntity.status(404).body(errorResult);
            }
        } catch (Exception e) {
            System.err.println("解绑手机号失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "解绑手机号失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 修改密码
     * 注意：此方法暂时不对密码进行加密处理，仅做明文存储，实际生产环境需要添加加密逻辑
     */
    @PutMapping("/user-base/password")
    public ResponseEntity<Map<String, Object>> updateUserPassword(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String newPassword = requestData.get("newPassword").toString();

            System.out.println("开始修改密码，用户ID: " + userId);

            // 验证密码格式（至少6位）
            if (newPassword == null || newPassword.length() < 6) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "密码长度至少为6位");
                return ResponseEntity.status(400).body(errorResult);
            }

            // TODO: 实际生产环境中，应该对密码进行加密处理
            // 例如使用 BCrypt、SHA-256 等加密算法
            // String encryptedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            // 或者使用其他加密方式

            // 暂时直接存储明文密码（仅用于开发测试）
            String passwordHash = newPassword;

            // 更新密码
            UpdateWrapper<UserBase_wlq> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", userId).set("password_hash", passwordHash);
            int updateCount = userBaseMapper.update(null, updateWrapper);

            if (updateCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "密码修改成功");
                System.out.println("成功修改密码，用户ID: " + userId);
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "修改失败，用户不存在");
                return ResponseEntity.status(404).body(errorResult);
            }
        } catch (Exception e) {
            System.err.println("修改密码失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "修改密码失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    /**
     * 注销账户
     * 对用户所有相关数据进行逻辑删除
     */
    @PostMapping("/user-base/delete-account")
    public ResponseEntity<Map<String, Object>> deleteAccount(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());

            System.out.println("收到注销账户请求，用户ID: " + userId);

            // 调用账户注销服务
            Map<String, Object> result = accountDeletionService.deleteAccount(userId);

            if (result.get("success").equals(true)) {
                System.out.println("账户注销成功，用户ID: " + userId);
                return ResponseEntity.ok(result);
            } else {
                System.err.println("账户注销失败，用户ID: " + userId + ", 原因: " + result.get("message"));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("注销账户异常: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "注销账户失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // ==================== 隐私设置相关接口 ====================

    /**
     * 获取用户隐私设置
     * 
     * @param userId 用户ID
     * @return 隐私设置Map
     */
    @GetMapping("/privacy-settings/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPrivacySettings(@PathVariable Long userId) {
        try {
            System.out.println("UserEntityController: 收到获取用户隐私设置请求，userId=" + userId);

            Map<String, Boolean> settings = userEntityService.getUserPrivacySettings(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取隐私设置成功");
            response.put("data", settings);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("UserEntityController: 获取用户隐私设置失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取隐私设置失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 保存用户隐私设置
     * 
     * @param requestData 请求数据，包含userId, settingKey, value
     * @return 保存结果
     */
    @PostMapping("/privacy-settings/save")
    public ResponseEntity<Map<String, Object>> saveUserPrivacySetting(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("UserEntityController: 收到保存用户隐私设置请求: " + requestData);

            Long userId = Long.valueOf(requestData.get("userId").toString());
            String settingKey = requestData.get("settingKey").toString();
            Boolean value = Boolean.valueOf(requestData.get("value").toString());

            String result = userEntityService.saveUserPrivacySetting(userId, settingKey, value);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("UserEntityController: 保存用户隐私设置失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "保存隐私设置失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/mode-settings/minor/save")
    public ResponseEntity<Map<String, Object>> saveUserMinorModeSetting(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("UserEntityController: 收到保存未成年人模式设置请求: " + requestData);

            Long userId = Long.valueOf(requestData.get("userId").toString());
            Boolean enabled = Boolean.valueOf(requestData.get("enabled").toString());

            String result = userEntityService.saveUserMinorModeEnabled(userId, enabled);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result);
            response.put("userId", userId);
            response.put("enabled", enabled);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("UserEntityController: 保存未成年人模式设置失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "保存未成年人模式设置失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 用户登出接口
     * 前端调用：POST /api/u-entities/logout
     * 支持请求体（JSON）或URL参数两种方式
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody(required = false) Map<String, Object> requestBody,
            @RequestParam(required = false) String token) {
        try {
            // 支持两种传参方式：请求体（JSON）或URL参数
            String tokenValue = null;
            if (requestBody != null && requestBody.get("token") != null) {
                // 从请求体获取
                tokenValue = requestBody.get("token").toString();
            } else if (token != null) {
                // 从URL参数获取（向后兼容）
                tokenValue = token;
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "token 不能为空");
                return ResponseEntity.badRequest().body(errorResult);
            }

            System.out.println("收到登出请求，token: " + tokenValue);

            // 调用登出服务
            boolean logoutResult = userBaseService.logout(tokenValue);

            if (logoutResult) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "登出成功");
                result.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("success", false);
                errorResult.put("message", "登出失败，token无效或已过期");
                return ResponseEntity.status(401).body(errorResult);
            }
        } catch (Exception e) {
            System.err.println("登出异常: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "登出失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}
