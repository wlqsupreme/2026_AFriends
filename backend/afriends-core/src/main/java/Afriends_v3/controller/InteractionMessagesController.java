package Afriends_v3.controller;

import Afriends_v3.entity.UserBase_wlq;
import Afriends_v3.mapper.UserBaseMapper;
import Afriends_v3.service.InteractionMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 互动消息页面控制器
 * 提供互动消息数据的API接口
 */
@RestController
@RequestMapping("/api/interaction-messages")
@CrossOrigin(origins = "*")
public class InteractionMessagesController {

    @Autowired
    private InteractionMessagesService interactionMessagesService;

    @Autowired
    private UserBaseMapper userBaseMapper;

    // 支持的筛选类型
    private static final Set<String> VALID_FILTER_TYPES = Set.of("all", "likes", "received-comments", "sent-comments");
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 互动消息测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "互动消息后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户的互动消息数据
     * @param userId 用户ID，默认为1000100
     * @param filterType 筛选类型：all, likes, received-comments, sent-comments
     * @return 互动消息数据列表
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getInteractionMessagesData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId,
            @RequestParam(value = "filterType", defaultValue = "all") String filterType) {
        
        System.out.println("=== InteractionMessagesController: 收到获取互动消息数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("筛选类型: " + filterType);
        
        try {
            // 1. 参数校验 - 用户ID
            if (userId == null || userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 参数校验 - 筛选类型
            if (filterType == null || filterType.trim().isEmpty()) {
                filterType = "all"; // 默认值
            }
            filterType = filterType.trim().toLowerCase();
            if (!VALID_FILTER_TYPES.contains(filterType)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "无效的筛选类型，支持的筛选类型: " + String.join(", ", VALID_FILTER_TYPES));
                errorResponse.put("filterType", filterType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 3. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("data", new Object[0]);
                errorResponse.put("count", 0);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }

            // 4. 获取互动消息数据
            long startTime = System.currentTimeMillis();
            List<Map<String, Object>> messagesData = interactionMessagesService.getInteractionMessagesData(userId, filterType);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // 5. 构建成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取互动消息数据成功");
            response.put("data", messagesData != null ? messagesData : new Object[0]);
            response.put("count", messagesData != null ? messagesData.size() : 0);
            response.put("userId", userId);
            response.put("filterType", filterType);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("InteractionMessagesController: 返回互动消息数据成功，共 " + (messagesData != null ? messagesData.size() : 0) + " 条数据，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            // 参数错误
            System.err.println("InteractionMessagesController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("userId", userId);
            errorResponse.put("filterType", filterType);
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            // 系统错误
            System.err.println("InteractionMessagesController: 获取互动消息数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取互动消息数据失败，请稍后重试");
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("filterType", filterType);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 标记所有消息为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/mark-all-read")
    public ResponseEntity<Map<String, Object>> markAllAsRead(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("InteractionMessagesController: 收到标记所有消息为已读请求，用户ID: " + userId);
        
        try {
            // 1. 参数校验 - 用户ID
            if (userId == null || userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }

            // 3. 执行标记为已读操作
            Map<String, Object> result = interactionMessagesService.markAllAsRead(userId);
            
            // 4. 检查服务层返回的结果
            if (Boolean.FALSE.equals(result.get("success"))) {
                // 服务层返回失败，返回业务错误
                return ResponseEntity.badRequest().body(result);
            }
            
            System.out.println("InteractionMessagesController: 标记所有消息为已读成功，用户ID: " + userId);
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            // 参数错误
            System.err.println("InteractionMessagesController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            // 系统错误
            System.err.println("InteractionMessagesController: 标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "标记所有消息为已读失败，请稍后重试");
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
