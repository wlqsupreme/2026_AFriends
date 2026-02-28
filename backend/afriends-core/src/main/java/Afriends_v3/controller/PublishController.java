package Afriends_v3.controller;

import Afriends_v3.service.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布内容控制器
 * 处理用户发布各种类型内容的请求
 */
@RestController
@RequestMapping("/api/publish")
@CrossOrigin(origins = "*")
public class PublishController {

    @Autowired
    private PublishService publishService;
    
    /**
     * 发布内容
     * @param requestBody 请求体，包含userId, contentText, columnType, imageUrls等
     * @return 发布结果
     */
    @PostMapping("/content")
    public ResponseEntity<Map<String, Object>> publishContent(@RequestBody Map<String, Object> requestBody) {
        // 参数验证和空值检查
        if (requestBody == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "请求体不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        // 验证必填字段
        if (requestBody.get("userId") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "userId 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        if (requestBody.get("contentText") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "contentText 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        // 验证 contentText 长度（数据库字段限制为 250 字符）
        String contentTextStr = requestBody.get("contentText").toString();
        if (contentTextStr.length() > 250) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "contentText 长度不能超过 250 个字符");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        if (requestBody.get("columnType") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "columnType 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String contentText = requestBody.get("contentText").toString();
        String columnType = requestBody.get("columnType").toString();
        String imageUrls = requestBody.get("imageUrls") != null ? requestBody.get("imageUrls").toString() : null;
        String title = requestBody.get("title") != null ? requestBody.get("title").toString() : null;
        String description = requestBody.get("description") != null ? requestBody.get("description").toString() : null;
        
        System.out.println("PublishController: 收到发布请求 - 用户ID: " + userId + 
            ", 专栏类型: " + columnType + ", 内容: " + contentText);
        
        try {
            Map<String, Object> result = publishService.publishContent(userId, contentText, columnType, imageUrls, title, description);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("PublishController: 发布内容失败: " + e.getMessage());
            e.printStackTrace();
            
            // 提供更详细的错误信息
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = e.getClass().getSimpleName();
                if (e.getCause() != null && e.getCause().getMessage() != null) {
                    errorMessage += ": " + e.getCause().getMessage();
                }
            }
            
            // 检查是否是数据库连接问题
            String finalMessage;
            if (errorMessage.contains("Connection") || errorMessage.contains("Communications") || 
                errorMessage.contains("CannotGetJdbcConnection") || errorMessage.contains("timeout") ||
                errorMessage.contains("数据库连接失败")) {
                finalMessage = "数据库连接失败，无法发布内容。请检查数据库连接配置。";
            } else {
                finalMessage = "发布失败: " + errorMessage;
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", finalMessage);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "发布服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}




