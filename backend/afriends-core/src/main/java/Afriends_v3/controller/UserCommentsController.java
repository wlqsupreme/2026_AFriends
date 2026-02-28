package Afriends_v3.controller;

import Afriends_v3.service.UserCommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户评论历史控制器
 * 提供用户评论历史的API接口
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserCommentsController {

    @Autowired
    private UserCommentsService userCommentsService;

    /**
     * 获取用户的评论历史列表
     * GET /api/user/comments?userId=1000100
     *
     * @param userId 用户ID
     * @return 评论历史列表
     */
    @GetMapping("/comments")
    public ResponseEntity<Map<String, Object>> getUserComments(
            @RequestParam(value = "userId") Long userId) {
        System.out.println("UserCommentsController: 收到获取用户评论历史请求，用户ID: " + userId);

        Map<String, Object> response = new HashMap<>();
        try {
            // 1. 参数校验
            if (userId == null || userId <= 0) {
                response.put("success", false);
                response.put("message", "用户ID无效");
                response.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(response);
            }

            List<Map<String, Object>> commentsList = userCommentsService.getUserComments(userId);

            response.put("success", true);
            response.put("message", "获取评论历史成功");
            response.put("data", commentsList);
            response.put("count", commentsList.size());
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("UserCommentsController: 获取评论历史成功，用户ID: " + userId + ", 记录数: " + commentsList.size());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("UserCommentsController: 参数错误: " + e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("UserCommentsController: 获取评论历史失败: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "获取评论历史失败: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(response);
        }
    }
}
