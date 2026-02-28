package Afriends_v3.controller;

import Afriends_v3.service.NovelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小说详情页面控制器
 * 提供小说详情数据的API接口
 */
@RestController
@RequestMapping("/api/novel-detail")
@CrossOrigin(origins = "*")
public class NovelDetailController {

    @Autowired
    private NovelDetailService novelDetailService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 小说详情测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "小说详情后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取小说详情数据
     * @param novelId 小说ID
     * @param userId 用户ID，默认为1000100
     * @return 小说详情数据
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getNovelDetail(
            @RequestParam("novelId") Long novelId,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说详情请求 ===");
        System.out.println("小说ID: " + novelId);
        System.out.println("用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> novelDetail = novelDetailService.getNovelDetail(novelId, userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说详情成功");
            response.put("data", novelDetail);
            response.put("novelId", novelId);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说详情成功，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说详情失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说详情失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("novelId", novelId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取小说章节列表
     * @param novelId 小说ID
     * @return 章节列表
     */
    @GetMapping("/chapters")
    public ResponseEntity<Map<String, Object>> getNovelChapters(
            @RequestParam("novelId") Long novelId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说章节请求 ===");
        System.out.println("小说ID: " + novelId);
        
        try {
            java.util.List<Map<String, Object>> chapters = novelDetailService.getNovelChapters(novelId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说章节成功");
            response.put("data", chapters);
            response.put("count", chapters.size());
            response.put("novelId", novelId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说章节成功，共 " + chapters.size() + " 章");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说章节失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说章节失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("novelId", novelId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取指定章节内容
     * @param chapterId 章节ID
     * @return 章节内容
     */
    @GetMapping("/chapter")
    public ResponseEntity<Map<String, Object>> getChapterContent(
            @RequestParam("chapterId") Long chapterId) {
        
        System.out.println("=== NovelDetailController: 收到获取章节内容请求 ===");
        System.out.println("章节ID: " + chapterId);
        
        try {
            Map<String, Object> chapterContent = novelDetailService.getChapterContent(chapterId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取章节内容成功");
            response.put("data", chapterContent);
            response.put("chapterId", chapterId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回章节内容成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取章节内容失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取章节内容失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("chapterId", chapterId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取小说书评列表
     * @param novelId 小说ID
     * @param userId 用户ID
     * @return 书评列表
     */
    @GetMapping("/reviews")
    public ResponseEntity<Map<String, Object>> getNovelReviews(
            @RequestParam("novelId") Long novelId,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到获取小说书评请求 ===");
        System.out.println("小说ID: " + novelId);
        System.out.println("用户ID: " + userId);
        
        try {
            List<Map<String, Object>> reviews = novelDetailService.getNovelReviews(novelId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取小说书评成功");
            response.put("data", reviews);
            response.put("count", reviews.size());
            response.put("novelId", novelId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回小说书评成功，共 " + reviews.size() + " 条");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取小说书评失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取小说书评失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("novelId", novelId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 处理评论点赞/取消点赞
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<Map<String, Object>> handleCommentLike(
            @PathVariable("commentId") Long commentId,
            @RequestParam("userId") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到评论点赞请求 ===");
        System.out.println("用户ID: " + userId);
        System.out.println("评论ID: " + commentId);
        
        try {
            Map<String, Object> result = novelDetailService.handleCommentLike(userId, commentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("userId", userId);
            response.put("commentId", commentId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 评论点赞处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理评论点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "处理评论点赞失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("userId", userId);
            errorResponse.put("commentId", commentId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 获取单条书评详情
     * @param reviewId 书评ID
     * @param userId 用户ID，默认为1000100
     * @return 书评详情数据
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<Map<String, Object>> getReviewDetail(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到获取书评详情请求 ===");
        System.out.println("书评ID: " + reviewId);
        System.out.println("用户ID: " + userId);
        
        try {
            Map<String, Object> reviewDetail = novelDetailService.getReviewDetail(reviewId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取书评详情成功");
            response.put("data", reviewDetail);
            response.put("reviewId", reviewId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 返回书评详情成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 获取书评详情失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取书评详情失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("reviewId", reviewId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理书评点赞/取消点赞
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/review/{reviewId}/like")
    public ResponseEntity<Map<String, Object>> handleReviewLike(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("userId") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到书评点赞请求 ===");
        System.out.println("书评ID: " + reviewId);
        System.out.println("用户ID: " + userId);
        
        try {
            Map<String, Object> result = novelDetailService.handleReviewLike(reviewId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("reviewId", reviewId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 书评点赞处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理书评点赞失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "处理书评点赞失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("reviewId", reviewId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理书评点踩/取消点踩
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/review/{reviewId}/dislike")
    public ResponseEntity<Map<String, Object>> handleReviewDislike(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("userId") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到书评点踩请求 ===");
        System.out.println("书评ID: " + reviewId);
        System.out.println("用户ID: " + userId);
        
        try {
            Map<String, Object> result = novelDetailService.handleReviewDislike(reviewId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("reviewId", reviewId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理书评点踩失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "处理书评点踩失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("reviewId", reviewId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 处理书评收藏/取消收藏
     * @param reviewId 书评ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/review/{reviewId}/favorite")
    public ResponseEntity<Map<String, Object>> handleReviewFavorite(
            @PathVariable("reviewId") Long reviewId,
            @RequestParam("userId") Long userId) {
        
        System.out.println("=== NovelDetailController: 收到书评收藏请求 ===");
        System.out.println("书评ID: " + reviewId);
        System.out.println("用户ID: " + userId);
        
        try {
            Map<String, Object> result = novelDetailService.handleReviewFavorite(reviewId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("reviewId", reviewId);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理书评收藏失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "处理书评收藏失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("reviewId", reviewId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 提交书评的评论（回复）
     * @param reviewId 书评ID
     * @param requestBody 请求体，包含userId, commentText
     * @return 操作结果
     */
    @PostMapping("/review/{reviewId}/comment")
    public ResponseEntity<Map<String, Object>> submitReviewComment(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody Map<String, Object> requestBody) {
        
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String commentText = requestBody.get("commentText").toString();
        
        System.out.println("NovelDetailController: 收到提交书评评论请求 - 书评ID: " + reviewId + 
            ", 用户ID: " + userId + ", 评论内容: " + commentText);
        
        try {
            // 获取书评信息以获取novelId
            Map<String, Object> reviewDetail = novelDetailService.getReviewDetail(reviewId, userId);
            Long novelId = Long.valueOf(reviewDetail.get("novelId").toString());
            
            // 提交评论，parentCommentId设置为reviewId
            Map<String, Object> result = novelDetailService.submitComment(novelId, userId, commentText, reviewId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("reviewId", reviewId);
            response.put("userId", userId);
            response.put("commentId", result.get("commentId"));
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 书评评论提交处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理书评评论提交失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论操作失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("reviewId", reviewId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 提交小说评论
     * @param requestBody 请求体，包含novelId, userId, commentText, parentCommentId
     * @return 操作结果
     */
    @PostMapping("/comment")
    public ResponseEntity<Map<String, Object>> submitComment(@RequestBody Map<String, Object> requestBody) {
        Long novelId = Long.valueOf(requestBody.get("novelId").toString());
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        String commentText = requestBody.get("commentText").toString();
        Long parentCommentId = requestBody.get("parentCommentId") != null ? 
            Long.valueOf(requestBody.get("parentCommentId").toString()) : null;
        
        System.out.println("NovelDetailController: 收到提交评论请求 - 小说ID: " + novelId + 
            ", 用户ID: " + userId + ", 评论内容: " + commentText + ", 父评论ID: " + parentCommentId);
        
        try {
            Map<String, Object> result = novelDetailService.submitComment(novelId, userId, commentText, parentCommentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.get("success"));
            response.put("message", result.get("message"));
            response.put("data", result);
            response.put("novelId", novelId);
            response.put("userId", userId);
            response.put("commentId", result.get("commentId"));
            response.put("parentCommentId", parentCommentId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("NovelDetailController: 评论提交处理完成");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("NovelDetailController: 处理评论提交失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "评论操作失败: " + e.getMessage());
            errorResponse.put("data", null);
            errorResponse.put("novelId", novelId);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
