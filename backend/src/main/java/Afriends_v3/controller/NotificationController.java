package Afriends_v3.controller;

import Afriends_v3.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知控制器
 * 提供通知相关的API接口，支持轮询查询
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * 获取用户未读消息数量
     * 用于轮询检查是否有新通知
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Object>> getUnreadCount(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long unreadCount = notificationService.getUnreadCount(userId);
            response.put("success", true);
            response.put("unreadCount", unreadCount);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取未读消息数量失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取未读消息数量失败: " + e.getMessage());
            response.put("unreadCount", 0);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 获取用户通知列表（分页）
     * @param userId 用户ID
     * @param page 页码（从1开始，默认1）
     * @param size 每页数量（默认20）
     * @return 通知列表和分页信息
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getNotificationList(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> result = notificationService.getNotificationList(userId, page, size);
            response.put("success", true);
            response.putAll(result);
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取通知列表失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取通知列表失败: " + e.getMessage());
            response.put("notifications", java.util.List.of());
            response.put("total", 0);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 标记消息为已读
     * @param params 包含messageId和userId的Map
     * @return 操作结果
     */
    @PostMapping("/mark-read")
    public ResponseEntity<Map<String, Object>> markAsRead(@RequestBody Map<String, Long> params) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long messageId = params.get("messageId");
            Long userId = params.get("userId");
            
            if (messageId == null || userId == null) {
                response.put("success", false);
                response.put("message", "参数不完整：需要messageId和userId");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean success = notificationService.markAsRead(messageId, userId);
            response.put("success", success);
            response.put("message", success ? "标记成功" : "标记失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("标记消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "标记失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/mark-all-read")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (userId == null) {
                response.put("success", false);
                response.put("message", "参数不完整：需要userId");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean success = notificationService.markAllAsRead(userId);
            response.put("success", success);
            response.put("message", success ? "全部标记成功" : "标记失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "标记失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

