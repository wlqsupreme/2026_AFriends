package Afriends_v3.controller;

import Afriends_v3.service.PostDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 帖子详情页面控制器
 * 提供图文和文字帖子详情数据的API接口
 */
@RestController
@RequestMapping("/api/post-detail")
@CrossOrigin(origins = "*")
public class PostDetailController {

    @Autowired
    private PostDetailService postDetailService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 帖子详情测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "帖子详情后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取帖子详情数据
     * @param postId 帖子ID
     * @param postType 帖子类型：text, image
     * @param userId 当前用户ID，默认为1000100
     * @return 帖子详情数据
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getPostDetailData(
            @RequestParam("postId") Long postId,
            @RequestParam("postType") String postType,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== PostDetailController: 收到获取帖子详情数据请求 ===");
        System.out.println("帖子ID: " + postId);
        System.out.println("帖子类型: " + postType);
        System.out.println("用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> postDetail = postDetailService.getPostDetailData(postId, postType, userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取帖子详情数据成功");
            response.put("data", postDetail);
            response.put("postId", postId);
            response.put("postType", postType);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("PostDetailController: 返回帖子详情数据成功，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("PostDetailController: 获取帖子详情数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取帖子详情数据失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("postId", postId);
            errorResponse.put("postType", postType);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 处理点赞操作
     * @param requestBody 请求体，包含postId, postType, userId
     * @return 操作结果
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> handleLike(@RequestBody Map<String, Object> requestBody) {
        Long postId = Long.valueOf(requestBody.get("postId").toString());
        String postType = requestBody.get("postType").toString();
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        
        System.out.println("PostDetailController: 收到点赞请求 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Map<String, Object> result = postDetailService.handleLike(postId, postType, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("PostDetailController: 处理点赞请求失败: " + e.getMessage());
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
     * @param requestBody 请求体，包含postId, postType, userId
     * @return 操作结果
     */
    @PostMapping("/dislike")
    public ResponseEntity<Map<String, Object>> handleDislike(@RequestBody Map<String, Object> requestBody) {
        Long postId = Long.valueOf(requestBody.get("postId").toString());
        String postType = requestBody.get("postType").toString();
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        
        System.out.println("PostDetailController: 收到点踩请求 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Map<String, Object> result = postDetailService.handleDislike(postId, postType, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("PostDetailController: 处理点踩请求失败: " + e.getMessage());
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
     * @param requestBody 请求体，包含postId, postType, userId
     * @return 操作结果
     */
    @PostMapping("/favorite")
    public ResponseEntity<Map<String, Object>> handleFavorite(@RequestBody Map<String, Object> requestBody) {
        Long postId = Long.valueOf(requestBody.get("postId").toString());
        String postType = requestBody.get("postType").toString();
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        
        System.out.println("PostDetailController: 收到收藏请求 - 帖子ID: " + postId + ", 帖子类型: " + postType + ", 用户ID: " + userId);
        
        try {
            Map<String, Object> result = postDetailService.handleFavorite(postId, postType, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("PostDetailController: 处理收藏请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "收藏操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理评论点赞操作
     * @param requestBody 请求体，包含userId, commentId
     * @return 操作结果
     */
    @PostMapping("/comment/like")
    public ResponseEntity<Map<String, Object>> handleCommentLike(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long commentId = Long.valueOf(requestBody.get("commentId").toString());
        
        System.out.println("PostDetailController: 收到评论点赞请求 - 用户ID: " + userId + ", 评论ID: " + commentId);
        
        try {
            Map<String, Object> result = postDetailService.handleCommentLike(userId, commentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("userId", userId);
            response.put("commentId", commentId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("PostDetailController: 评论点赞处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("PostDetailController: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论点赞操作失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("userId", userId);
            errorResponse.put("commentId", commentId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 提交评论
     * @param requestBody 请求体，包含postId, postType, userId, commentText, parentCommentId
     * @return 操作结果
     */
    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> submitComment(@RequestBody Map<String, Object> requestBody) {
        Long postId = Long.valueOf(requestBody.get("postId").toString());
        String postType = requestBody.get("postType").toString();
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String commentText = requestBody.get("commentText").toString();
        Long parentCommentId = requestBody.get("parentCommentId") != null ? 
            Long.valueOf(requestBody.get("parentCommentId").toString()) : null;
        
        System.out.println("PostDetailController: 收到评论请求 - 帖子ID: " + postId + ", 帖子类型: " + postType + 
            ", 用户ID: " + userId + ", 评论内容: " + commentText + ", 父评论ID: " + parentCommentId);
        
        try {
            Map<String, Object> result = postDetailService.submitComment(postId, postType, userId, commentText, parentCommentId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("PostDetailController: 处理评论请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
