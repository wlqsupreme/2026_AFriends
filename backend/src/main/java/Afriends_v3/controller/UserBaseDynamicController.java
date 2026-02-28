package Afriends_v3.controller;

import Afriends_v3.entity.UserBaseDynamic_njj;
import Afriends_v3.service.UserBaseDynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户动态基础信息控制器
 * 提供用户动态基础信息数据的API接口
 */
@RestController
@RequestMapping("/api/user-base-dynamic")
@CrossOrigin(origins = "*")
public class UserBaseDynamicController {

    @Autowired
    private UserBaseDynamicService userBaseDynamicService;

    /**
     * 加载用户动态基础信息数据到内存
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadUserBaseDynamicToMemory() {
        try {
            System.out.println("开始加载用户动态基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            userBaseDynamicService.loadUserBaseDynamicToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户动态基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户动态基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户动态基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户动态基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有用户动态基础信息数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserBaseDynamic_njj>> getAllUserBaseDynamic() {
        try {
            List<UserBaseDynamic_njj> dynamics = userBaseDynamicService.getAllUserBaseDynamicFromMemory();
            return ResponseEntity.ok(dynamics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据动态ID获取用户动态基础信息数据
     */
    @GetMapping("/{dynamicId}")
    public ResponseEntity<UserBaseDynamic_njj> getUserBaseDynamicById(@PathVariable Long dynamicId) {
        try {
            UserBaseDynamic_njj dynamic = userBaseDynamicService.getUserBaseDynamicByIdFromMemory(dynamicId);
            if (dynamic != null) {
                return ResponseEntity.ok(dynamic);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据用户ID获取用户动态基础信息数据
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserBaseDynamic_njj>> getUserBaseDynamicByUserId(@PathVariable Long userId) {
        try {
            List<UserBaseDynamic_njj> dynamics = userBaseDynamicService.getUserBaseDynamicByUserIdFromMemory(userId);
            return ResponseEntity.ok(dynamics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据内容类型获取用户动态基础信息数据
     */
    @GetMapping("/content-type/{contentType}")
    public ResponseEntity<List<UserBaseDynamic_njj>> getUserBaseDynamicByContentType(@PathVariable Byte contentType) {
        try {
            List<UserBaseDynamic_njj> dynamics = userBaseDynamicService
                    .getUserBaseDynamicByContentTypeFromMemory(contentType);
            return ResponseEntity.ok(dynamics);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取用户动态基础信息统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseDynamicStatistics() {
        try {
            Map<String, Object> stats = userBaseDynamicService.getUserBaseDynamicStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 强制刷新用户动态基础信息数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshUserBaseDynamicData() {
        try {
            System.out.println("开始刷新用户动态基础信息数据...");
            long startTime = System.currentTimeMillis();

            userBaseDynamicService.refreshUserBaseDynamicData();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户动态基础信息数据刷新完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户动态基础信息数据刷新成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("刷新用户动态基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户动态基础信息数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }
}
