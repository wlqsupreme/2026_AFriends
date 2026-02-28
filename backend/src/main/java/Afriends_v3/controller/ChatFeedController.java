package Afriends_v3.controller;

import Afriends_v3.service.ChatFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天动态页面控制器
 * 提供聊天动态数据的API接口
 */
@RestController
@RequestMapping("/api/chat-feed")
@CrossOrigin(origins = "*")
public class ChatFeedController {

    @Autowired
    private ChatFeedService chatFeedService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户的聊天动态数据
     * @param userId 用户ID，默认为1000001
     * @return 聊天动态数据列表
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getChatFeedData(
            @RequestParam(value = "userId", defaultValue = "1000001") Long userId) {
        
        System.out.println("=== ChatFeedController: 收到获取聊天动态数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> feedData = chatFeedService.getChatFeedData(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取聊天动态数据成功");
            response.put("data", feedData);
            response.put("count", feedData.size());
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("ChatFeedController: 返回聊天动态数据成功，共 " + feedData.size() + " 条数据，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ChatFeedController: 获取聊天动态数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取聊天动态数据失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取在线人数（模拟数据）
     * @return 在线人数
     */
    @GetMapping("/online-count")
    public ResponseEntity<Map<String, Object>> getOnlineCount() {
        try {
            // 模拟在线人数，实际应该从Redis或其他缓存中获取
            int onlineCount = 12345 + (int)(Math.random() * 1000);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("onlineCount", onlineCount);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("ChatFeedController: 返回在线人数: " + onlineCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ChatFeedController: 获取在线人数失败: " + e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取在线人数失败: " + e.getMessage());
            errorResponse.put("onlineCount", 0);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 刷新聊天动态数据
     * @param userId 用户ID
     * @return 刷新结果
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshChatFeedData(
            @RequestParam(value = "userId", defaultValue = "1000001") Long userId) {
        
        System.out.println("ChatFeedController: 收到刷新聊天动态数据请求，用户ID: " + userId);
        
        try {
            // 重新获取数据
            List<Map<String, Object>> feedData = chatFeedService.getChatFeedData(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "刷新聊天动态数据成功");
            response.put("data", feedData);
            response.put("count", feedData.size());
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("ChatFeedController: 刷新聊天动态数据成功，共 " + feedData.size() + " 条数据");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ChatFeedController: 刷新聊天动态数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "刷新聊天动态数据失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 手动刷新缓存
     * @return 刷新结果
     */
    @PostMapping("/refresh-cache")
    public ResponseEntity<Map<String, Object>> refreshCache() {
        System.out.println("ChatFeedController: 收到手动刷新缓存请求");
        
        try {
            // 刷新所有缓存
            chatFeedService.refreshAllCache();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "缓存刷新成功");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("ChatFeedController: 缓存刷新成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ChatFeedController: 刷新缓存失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "刷新缓存失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理点赞操作
     * @param requestBody 请求体，包含userId, contentId, contentType
     * @return 操作结果
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> handleLike(@RequestBody Map<String, Object> requestBody) {
        try {
            // 参数验证
            if (!requestBody.containsKey("userId") || !requestBody.containsKey("contentId") || !requestBody.containsKey("contentType")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "缺少必要参数：userId, contentId, contentType");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long contentId = Long.valueOf(requestBody.get("contentId").toString());
            Byte contentType = Byte.valueOf(requestBody.get("contentType").toString());
            
            System.out.println("ChatFeedController: 收到点赞请求 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);
            
            // 调用服务层处理
            Map<String, Object> result = chatFeedService.handleLike(userId, contentId, contentType);
            
            System.out.println("ChatFeedController: 点赞操作完成 - 结果: " + result);
            return ResponseEntity.ok(result);
            
        } catch (NumberFormatException e) {
            System.err.println("ChatFeedController: 参数格式错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "参数格式错误");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            System.err.println("ChatFeedController: 处理点赞请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "点赞操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理点踩操作
     * @param requestBody 请求体，包含userId, contentId, contentType
     * @return 操作结果
     */
    @PostMapping("/dislike")
    public ResponseEntity<Map<String, Object>> handleDislike(@RequestBody Map<String, Object> requestBody) {
        try {
            // 参数验证
            if (!requestBody.containsKey("userId") || !requestBody.containsKey("contentId") || !requestBody.containsKey("contentType")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "缺少必要参数：userId, contentId, contentType");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long contentId = Long.valueOf(requestBody.get("contentId").toString());
            Byte contentType = Byte.valueOf(requestBody.get("contentType").toString());
            
            System.out.println("ChatFeedController: 收到点踩请求 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);
            
            // 调用服务层处理
            Map<String, Object> result = chatFeedService.handleDislike(userId, contentId, contentType);
            
            System.out.println("ChatFeedController: 点踩操作完成 - 结果: " + result);
            return ResponseEntity.ok(result);
            
        } catch (NumberFormatException e) {
            System.err.println("ChatFeedController: 参数格式错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "参数格式错误");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            System.err.println("ChatFeedController: 处理点踩请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "点踩操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理收藏操作
     * @param requestBody 请求体，包含userId, contentId, contentType
     * @return 操作结果
     */
    @PostMapping("/favorite")
    public ResponseEntity<Map<String, Object>> handleFavorite(@RequestBody Map<String, Object> requestBody) {
        try {
            // 参数验证
            if (!requestBody.containsKey("userId") || !requestBody.containsKey("contentId") || !requestBody.containsKey("contentType")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "缺少必要参数：userId, contentId, contentType");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long contentId = Long.valueOf(requestBody.get("contentId").toString());
            Byte contentType = Byte.valueOf(requestBody.get("contentType").toString());
            
            System.out.println("ChatFeedController: 收到收藏请求 - 用户ID: " + userId + ", 内容ID: " + contentId + ", 内容类型: " + contentType);
            
            // 调用服务层处理
            Map<String, Object> result = chatFeedService.handleFavorite(userId, contentId, contentType);
            
            System.out.println("ChatFeedController: 收藏操作完成 - 结果: " + result);
            return ResponseEntity.ok(result);
            
        } catch (NumberFormatException e) {
            System.err.println("ChatFeedController: 参数格式错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "参数格式错误");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            System.err.println("ChatFeedController: 处理收藏请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "收藏操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}

