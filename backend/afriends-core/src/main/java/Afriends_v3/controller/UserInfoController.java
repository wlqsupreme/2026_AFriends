package Afriends_v3.controller;

import Afriends_v3.entity.UserFollowRelationship_zjx;
import Afriends_v3.entity.UserInfo_njj;
import Afriends_v3.service.UserFollowService;
import Afriends_v3.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息控制器
 * 提供用户数据的API接口
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    // 注入关注服务（需自行实现，处理关注业务逻辑）
    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private Afriends_v3.mapper.UserBaseMapper userBaseMapper;


    /**
     * 加载数据到内存（默认100条）
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadDataToMemory() {
        try {
            System.out.println("开始加载用户数据到内存（默认100条）...");
            long startTime = System.currentTimeMillis();

            userInfoService.loadDataToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 加载指定数量的数据到内存
     */
    @PostMapping("/load/{limit}")
    public ResponseEntity<Map<String, String>> loadDataToMemoryWithLimit(@PathVariable int limit) {
        try {
            System.out.println("开始加载用户数据到内存（限制" + limit + "条）...");
            long startTime = System.currentTimeMillis();

            userInfoService.loadDataToMemory(limit);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "数据加载到内存成功，实际加载: " + limit + " 条，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration),
                    "limit", String.valueOf(limit));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有用户（从内存）
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserInfo_njj>> getAllUsers() {
        try {
            List<UserInfo_njj> users = userInfoService.getAllUsersFromMemory();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据ID获取用户（从内存）
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserInfo_njj> getUserById(@PathVariable Long id) {
        try {
            UserInfo_njj user = userInfoService.getUserByIdFromMemory(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取统计信息（从内存）
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = userInfoService.getStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 测试数据库连接
     */
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        try {
            System.out.println("开始测试数据库连接...");
            long startTime = System.currentTimeMillis();

            // 测试数据库连接
            long count = userInfoService.count();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("数据库连接测试完成，用户总数: " + count + "，耗时: " + duration + "ms");

            Map<String, Object> result = Map.of(
                    "success", true,
                    "userCount", count,
                    "duration", duration,
                    "message", "数据库连接正常");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage() != null ? e.getMessage() : "未知错误");
            result.put("message", "数据库连接失败");
            result.put("details", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 分页加载用户数据（测试用）
     */
    @GetMapping("/load-page")
    public ResponseEntity<Map<String, Object>> loadUserDataPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("开始分页加载用户数据，页码: " + page + "，大小: " + size);
            long startTime = System.currentTimeMillis();

            // 使用MyBatis-Plus的分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfo_njj> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                    page, size);

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserInfo_njj> result = userInfoService
                    .page(pageParam);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("分页查询完成，耗时: " + duration + "ms");

            Map<String, Object> response = Map.of(
                    "success", true,
                    "records", result.getRecords(),
                    "total", result.getTotal(),
                    "current", result.getCurrent(),
                    "size", result.getSize(),
                    "pages", result.getPages(),
                    "duration", duration);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("分页查询失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "message", "分页查询失败");
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 刷新内存数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshMemoryData() {
        try {
            userInfoService.forceRefreshMemory();
            Map<String, String> result = Map.of(
                    "message", "内存数据刷新成功",
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> result = Map.of(
                    "message", "内存数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }
    /**
     * 关注/取消关注接口
     * 前端调用示例：POST /api/users/follow
     * 请求体：{"userId": 1001, "followedUserId": 2001, "action": "follow"}
     */
    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> handleFollow(@RequestBody UserFollowRelationship_zjx request) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 参数校验
            if (request.getUserId() == null || request.getFollowedUserId() == null || request.getAction() == null) {
                result.put("success", false);
                result.put("message", "参数不完整（userId、followedUserId、action为必填项）");
                return ResponseEntity.badRequest().body(result);
            }

            // 2. 防止自己关注自己
            if (request.getUserId().equals(request.getFollowedUserId())) {
                result.put("success", false);
                result.put("message", "不能关注自己");
                return ResponseEntity.badRequest().body(result);
            }

            // 3. 验证被关注用户是否存在
            Afriends_v3.entity.UserBase_wlq followedUser = userBaseMapper.selectById(request.getFollowedUserId());
            if (followedUser == null) {
                result.put("success", false);
                result.put("message", "被关注用户不存在");
                result.put("followedUserId", request.getFollowedUserId());
                return ResponseEntity.status(404).body(result);
            }
            
            // 4. 验证操作者用户是否存在（可选，但建议添加）
            Afriends_v3.entity.UserBase_wlq user = userBaseMapper.selectById(request.getUserId());
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                result.put("userId", request.getUserId());
                return ResponseEntity.status(404).body(result);
            }

            // 5. 根据操作类型处理关注/取消关注
            boolean isFollowed;
            if ("follow".equals(request.getAction())) {
                // 执行关注操作
                isFollowed = userFollowService.followUser(request.getUserId(), request.getFollowedUserId());
                System.out.println("UserFollowService: 关注[" + isFollowed + "]");
                result.put("message", "关注成功");
            } else if ("unfollow".equals(request.getAction())) {
                // 执行取消关注操作
                isFollowed = userFollowService.unfollowUser(request.getUserId(), request.getFollowedUserId());
                result.put("message", "取消关注成功");
            } else {
                result.put("success", false);
                result.put("message", "无效的操作类型（仅支持follow/unfollow）");
                return ResponseEntity.badRequest().body(result);
            }
            System.out.println("用户: " + isFollowed);
            // 4. 构建返回结果
            result.put("success", true);
            result.put("isFollowed", isFollowed); // 当前关注状态（true=已关注，false=未关注）
            result.put("timestamp", new Timestamp(System.currentTimeMillis()));
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "操作失败：" + e.getMessage());
            result.put("error", e.toString());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 补充：查询用户关注状态接口（前端判断是否已关注）
     * 调用示例：GET /api/users/check-follow?userId=1001&followedUserId=2001
     */
    @GetMapping("/check-follow")
    public ResponseEntity<Map<String, Object>> checkFollowStatus(
            @RequestParam Long userId,
            @RequestParam Long followedUserId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean isFollowing = userFollowService.isFollowing(userId, followedUserId);
            result.put("success", true);
            result.put("isFollowing", isFollowing); // true=已关注，false=未关注
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}