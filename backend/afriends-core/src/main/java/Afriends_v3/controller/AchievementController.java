package Afriends_v3.controller;

import Afriends_v3.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成就页面控制器
 * 提供成就数据的API接口
 */
@RestController
@RequestMapping("/api/achievement")
@CrossOrigin(origins = "*")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 成就测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "成就后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 调试接口 - 直接查询数据库原始数据
     */
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> debug() {
        System.out.println("=== 成就调试接口被调用 ===");
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            // 查询所有成就基础数据
            List<Map<String, Object>> allAchievements = achievementService.getAllAchievementBaseData();
            response.put("allAchievements", allAchievements);
            response.put("allAchievementsCount", allAchievements.size());
            
            // 查询所有成就记录数据
            List<Map<String, Object>> allRecords = achievementService.getAllAchievementRecordData();
            response.put("allRecords", allRecords);
            response.put("allRecordsCount", allRecords.size());
            
            // 查询用户1000100的记录
            List<Map<String, Object>> userRecords = achievementService.getUserAchievementRecordData(1000100L);
            response.put("userRecords", userRecords);
            response.put("userRecordsCount", userRecords.size());
            
            response.put("success", true);
            response.put("message", "调试数据获取成功");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("调试接口异常: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "调试数据获取失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 数据库连接测试接口
     */
    @GetMapping("/db-test")
    public ResponseEntity<Map<String, Object>> databaseTest() {
        System.out.println("=== 数据库连接测试接口被调用 ===");
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            // 测试数据库连接和基本查询
            Map<String, Object> dbTestResult = achievementService.testDatabaseConnection();
            response.putAll(dbTestResult);
            
            response.put("success", true);
            response.put("message", "数据库连接测试完成");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("数据库连接测试异常: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "数据库连接测试失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户的成就数据
     * @param userId 用户ID，默认为1000001
     * @return 成就数据列表
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getUserAchievements(
            @RequestParam(value = "userId", defaultValue = "1000001") Long userId) {
        
        System.out.println("=== AchievementController: 收到获取用户成就数据请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> achievements = achievementService.getUserAchievements(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取用户成就数据成功");
            response.put("data", achievements);
            response.put("count", achievements.size());
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("AchievementController: 返回用户成就数据成功，共 " + achievements.size() + " 条数据，耗时: " + duration + "ms");
            System.out.println("返回的响应数据: " + response);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("AchievementController: 获取用户成就数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取用户成就数据失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户成就统计信息
     * @param userId 用户ID，默认为1000001
     * @return 成就统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserAchievementStats(
            @RequestParam(value = "userId", defaultValue = "1000001") Long userId) {
        
        System.out.println("=== AchievementController: 收到获取用户成就统计信息请求 ===");
        System.out.println("请求用户ID: " + userId);
        
        try {
            Map<String, Object> stats = achievementService.getUserAchievementStats(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取用户成就统计信息成功");
            response.put("data", stats);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("AchievementController: 返回用户成就统计信息成功");
            System.out.println("统计信息: " + stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("AchievementController: 获取用户成就统计信息失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取用户成就统计信息失败: " + e.getMessage());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 更新用户成就进度
     * @param requestBody 请求体，包含userId, achievementId, progress, isCompleted
     * @return 操作结果
     */
    @PostMapping("/update-progress")
    public ResponseEntity<Map<String, Object>> updateAchievementProgress(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long achievementId = Long.valueOf(requestBody.get("achievementId").toString());
        String progress = requestBody.get("progress").toString();
        Boolean isCompleted = Boolean.valueOf(requestBody.get("isCompleted").toString());
        
        System.out.println("AchievementController: 收到更新成就进度请求 - 用户ID: " + userId + 
            ", 成就ID: " + achievementId + ", 进度: " + progress + ", 是否完成: " + isCompleted);
        
        try {
            Map<String, Object> result = achievementService.updateAchievementProgress(userId, achievementId, progress, isCompleted);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("AchievementController: 更新成就进度失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新成就进度失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 刷新用户成就数据
     * @param userId 用户ID
     * @return 刷新结果
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshUserAchievements(
            @RequestParam(value = "userId", defaultValue = "1000001") Long userId) {
        
        System.out.println("AchievementController: 收到刷新用户成就数据请求，用户ID: " + userId);
        
        try {
            // 重新获取数据
            List<Map<String, Object>> achievements = achievementService.getUserAchievements(userId);
            Map<String, Object> stats = achievementService.getUserAchievementStats(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "刷新用户成就数据成功");
            response.put("data", achievements);
            response.put("stats", stats);
            response.put("count", achievements.size());
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("AchievementController: 刷新用户成就数据成功，共 " + achievements.size() + " 条数据");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("AchievementController: 刷新用户成就数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "刷新用户成就数据失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}