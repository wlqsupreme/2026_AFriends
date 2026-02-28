package Afriends_v3.controller;

import Afriends_v3.entity.UserAiModel_njj;
import Afriends_v3.service.UserAiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户AI模型控制器
 * 提供用户AI模型数据的API接口
 */
@RestController
@RequestMapping("/api/user-ai-model")
@CrossOrigin(origins = "*")
public class UserAiModelController {

    @Autowired
    private UserAiModelService userAiModelService;

    /**
     * 加载用户AI模型数据到内存
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadUserAiModelToMemory() {
        try {
            System.out.println("开始加载用户AI模型数据到内存...");
            long startTime = System.currentTimeMillis();

            userAiModelService.loadUserAiModelToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户AI模型数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有用户AI模型数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserAiModel_njj>> getAllUserAiModel() {
        try {
            List<UserAiModel_njj> models = userAiModelService.getAllUserAiModelFromMemory();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据模型ID获取用户AI模型数据
     */
    @GetMapping("/{userAiId}")
    public ResponseEntity<UserAiModel_njj> getUserAiModelById(@PathVariable Long userAiId) {
        try {
            UserAiModel_njj model = userAiModelService.getUserAiModelByIdFromMemory(userAiId);
            if (model != null) {
                return ResponseEntity.ok(model);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据用户ID获取用户AI模型数据
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAiModel_njj>> getUserAiModelByUserId(@PathVariable Long userId) {
        try {
            List<UserAiModel_njj> models = userAiModelService.getUserAiModelByUserIdFromMemory(userId);
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据父模型ID获取用户AI模型数据
     */
    @GetMapping("/parent/{parentModelId}")
    public ResponseEntity<List<UserAiModel_njj>> getUserAiModelByParentModelId(@PathVariable Long parentModelId) {
        try {
            List<UserAiModel_njj> models = userAiModelService.getUserAiModelByParentModelIdFromMemory(parentModelId);
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取用户AI模型统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiModelStatistics() {
        try {
            Map<String, Object> stats = userAiModelService.getUserAiModelStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 强制刷新用户AI模型数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshUserAiModelData() {
        try {
            System.out.println("开始刷新用户AI模型数据...");
            long startTime = System.currentTimeMillis();

            userAiModelService.refreshUserAiModelData();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户AI模型数据刷新完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据刷新成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("刷新用户AI模型数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI模型数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 更新AI模型名称
     */
    @PutMapping("/update-name/{userAiId}")
    public ResponseEntity<Map<String, Object>> updateAiModelName(@PathVariable Long userAiId,
            @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("modelName");
            if (newName == null || newName.trim().isEmpty()) {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "AI模型名称不能为空",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(result);
            }

            boolean success = userAiModelService.updateAiModelName(userAiId, newName.trim());

            if (success) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "message", "AI模型名称更新成功",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "AI模型不存在或更新失败",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            System.err.println("更新AI模型名称失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "更新AI模型名称失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 更新AI模型等级和经验
     */
    @PutMapping("/update-level/{userAiId}")
    public ResponseEntity<Map<String, Object>> updateAiModelLevel(@PathVariable Long userAiId,
            @RequestBody Map<String, Object> request) {
        try {
            Integer level = (Integer) request.get("level");
            Integer totalExp = (Integer) request.get("totalExp");

            if (level == null || totalExp == null) {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "等级和经验值不能为空",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(result);
            }

            boolean success = userAiModelService.updateAiModelLevel(userAiId, level, totalExp);

            if (success) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "message", "AI模型等级更新成功",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "AI模型不存在或更新失败",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            System.err.println("更新AI模型等级失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "更新AI模型等级失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 试用AI模型
     */
    @PostMapping("/try-out")
    public ResponseEntity<Map<String, Object>> tryOutAiModel(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = userAiModelService.tryOutAiModel(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "试用AI模型失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}
