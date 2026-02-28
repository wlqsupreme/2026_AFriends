package Afriends_v3.controller;

import Afriends_v3.service.LikesDislikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户喜欢讨厌页面控制器
 * 提供用户喜欢讨厌数据的API接口
 */
@RestController
@RequestMapping("/api/likes-dislikes")
@CrossOrigin(origins = "*")
public class LikesDislikesController {

    @Autowired
    private LikesDislikesService likesDislikesService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== LikesDislikesController: 测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "喜欢讨厌页面后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户的喜欢讨厌数据
     * @param userId 用户ID，默认为1000100
     * @return 喜欢讨厌数据
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getLikesDislikesData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== LikesDislikesController: 收到获取喜欢讨厌数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> data = likesDislikesService.getLikesDislikesData(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取喜欢讨厌数据成功");
            response.put("data", data);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("LikesDislikesController: 返回喜欢讨厌数据成功，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 获取喜欢讨厌数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取喜欢讨厌数据失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 添加喜欢项目
     * @param requestBody 请求体，包含userId, likeText, likeType等
     * @return 操作结果
     */
    @PostMapping("/add-like")
    public ResponseEntity<Map<String, Object>> addLike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String likeText = requestBody.get("likeText").toString();
        String likeType = requestBody.get("likeType") != null ? requestBody.get("likeType").toString() : "general";
        
        System.out.println("LikesDislikesController: 收到添加喜欢请求 - 用户ID: " + userId + ", 内容: " + likeText);
        
        try {
            Map<String, Object> result = likesDislikesService.addLike(userId, likeText, likeType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 添加喜欢失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "添加喜欢失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 添加讨厌项目
     * @param requestBody 请求体，包含userId, dislikeText, dislikeType等
     * @return 操作结果
     */
    @PostMapping("/add-dislike")
    public ResponseEntity<Map<String, Object>> addDislike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String dislikeText = requestBody.get("dislikeText").toString();
        String dislikeType = requestBody.get("dislikeType") != null ? requestBody.get("dislikeType").toString() : "general";
        
        System.out.println("LikesDislikesController: 收到添加讨厌请求 - 用户ID: " + userId + ", 内容: " + dislikeText);
        
        try {
            Map<String, Object> result = likesDislikesService.addDislike(userId, dislikeText, dislikeType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 添加讨厌失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "添加讨厌失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 删除喜欢项目
     * @param requestBody 请求体，包含userId, likeId
     * @return 操作结果
     */
    @PostMapping("/delete-like")
    public ResponseEntity<Map<String, Object>> deleteLike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long likeId = Long.valueOf(requestBody.get("likeId").toString());
        
        System.out.println("LikesDislikesController: 收到删除喜欢请求 - 用户ID: " + userId + ", 喜欢ID: " + likeId);
        
        try {
            Map<String, Object> result = likesDislikesService.deleteLike(userId, likeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 删除喜欢失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除喜欢失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 删除讨厌项目
     * @param requestBody 请求体，包含userId, dislikeId
     * @return 操作结果
     */
    @PostMapping("/delete-dislike")
    public ResponseEntity<Map<String, Object>> deleteDislike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long dislikeId = Long.valueOf(requestBody.get("dislikeId").toString());
        
        System.out.println("LikesDislikesController: 收到删除讨厌请求 - 用户ID: " + userId + ", 讨厌ID: " + dislikeId);
        
        try {
            Map<String, Object> result = likesDislikesService.deleteDislike(userId, dislikeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 删除讨厌失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除讨厌失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 编辑喜欢项目
     * @param requestBody 请求体，包含userId, likeId, likeText, likeType
     * @return 操作结果
     */
    @PostMapping("/edit-like")
    public ResponseEntity<Map<String, Object>> editLike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long likeId = Long.valueOf(requestBody.get("likeId").toString());
        String likeText = requestBody.get("likeText").toString();
        String likeType = requestBody.get("likeType") != null ? requestBody.get("likeType").toString() : "general";
        
        System.out.println("LikesDislikesController: 收到编辑喜欢请求 - 用户ID: " + userId + ", 喜欢ID: " + likeId + ", 内容: " + likeText);
        
        try {
            Map<String, Object> result = likesDislikesService.editLike(userId, likeId, likeText, likeType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 编辑喜欢失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "编辑喜欢失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 编辑讨厌项目
     * @param requestBody 请求体，包含userId, dislikeId, dislikeText, dislikeType
     * @return 操作结果
     */
    @PostMapping("/edit-dislike")
    public ResponseEntity<Map<String, Object>> editDislike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long dislikeId = Long.valueOf(requestBody.get("dislikeId").toString());
        String dislikeText = requestBody.get("dislikeText").toString();
        String dislikeType = requestBody.get("dislikeType") != null ? requestBody.get("dislikeType").toString() : "general";
        
        System.out.println("LikesDislikesController: 收到编辑讨厌请求 - 用户ID: " + userId + ", 讨厌ID: " + dislikeId + ", 内容: " + dislikeText);
        
        try {
            Map<String, Object> result = likesDislikesService.editDislike(userId, dislikeId, dislikeText, dislikeType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("LikesDislikesController: 编辑讨厌失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "编辑讨厌失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
