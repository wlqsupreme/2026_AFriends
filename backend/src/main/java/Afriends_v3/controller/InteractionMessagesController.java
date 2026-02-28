package Afriends_v3.controller;

import Afriends_v3.service.InteractionMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> messagesData = interactionMessagesService.getInteractionMessagesData(userId, filterType);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取互动消息数据成功");
            response.put("data", messagesData);
            response.put("count", messagesData.size());
            response.put("userId", userId);
            response.put("filterType", filterType);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("InteractionMessagesController: 返回互动消息数据成功，共 " + messagesData.size() + " 条数据，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("InteractionMessagesController: 获取互动消息数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取互动消息数据失败: " + e.getMessage());
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
            Map<String, Object> result = interactionMessagesService.markAllAsRead(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("InteractionMessagesController: 标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "标记所有消息为已读失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
