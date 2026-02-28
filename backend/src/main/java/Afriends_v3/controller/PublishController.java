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
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "发布失败: " + e.getMessage());
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




