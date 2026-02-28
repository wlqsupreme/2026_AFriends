package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.UserEntityService_wlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * U开头_wlq实体类控制器
 * 提供所有U开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/u-entities-wlq")
@CrossOrigin(origins = "*")
public class UserEntityController_wlq {

    @Autowired
    private UserEntityService_wlq userEntityService_wlq;

    // UserAiRequire 相关接口
    @PostMapping("/user-ai-require/load")
    public ResponseEntity<Map<String, String>> loadUserAiRequireToMemory() {
        try {
            System.out.println("开始加载用户AI需求数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService_wlq.loadUserAiRequireToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI需求数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI需求数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI需求数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-require/all")
    public ResponseEntity<List<UserAiRequire_wlq>> getAllUserAiRequire() {
        try {
            List<UserAiRequire_wlq> userAiRequires = userEntityService_wlq.getAllUserAiRequireFromMemory();
            return ResponseEntity.ok(userAiRequires);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-require/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiRequireStatistics() {
        try {
            Map<String, Object> stats = userEntityService_wlq.getUserAiRequireStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserAiRequireFeatureVector 相关接口
    @PostMapping("/user-ai-require-feature-vector/load")
    public ResponseEntity<Map<String, String>> loadUserAiRequireFeatureVectorToMemory() {
        try {
            System.out.println("开始加载用户AI需求特征向量数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService_wlq.loadUserAiRequireFeatureVectorToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI需求特征向量数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI需求特征向量数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI需求特征向量数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-require-feature-vector/all")
    public ResponseEntity<List<UserAiRequireFeatureVector_wlq>> getAllUserAiRequireFeatureVector() {
        try {
            List<UserAiRequireFeatureVector_wlq> userAiRequireFeatureVectors = userEntityService_wlq.getAllUserAiRequireFeatureVectorFromMemory();
            return ResponseEntity.ok(userAiRequireFeatureVectors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-require-feature-vector/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiRequireFeatureVectorStatistics() {
        try {
            Map<String, Object> stats = userEntityService_wlq.getUserAiRequireFeatureVectorStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserAiRespond 相关接口
    @PostMapping("/user-ai-respond/load")
    public ResponseEntity<Map<String, String>> loadUserAiRespondToMemory() {
        try {
            System.out.println("开始加载用户AI响应数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService_wlq.loadUserAiRespondToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI响应数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI响应数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI响应数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-respond/all")
    public ResponseEntity<List<UserAiRespond_wlq>> getAllUserAiRespond() {
        try {
            List<UserAiRespond_wlq> userAiResponds = userEntityService_wlq.getAllUserAiRespondFromMemory();
            return ResponseEntity.ok(userAiResponds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-respond/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiRespondStatistics() {
        try {
            Map<String, Object> stats = userEntityService_wlq.getUserAiRespondStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserAiRespondFeatureVector 相关接口
    @PostMapping("/user-ai-respond-feature-vector/load")
    public ResponseEntity<Map<String, String>> loadUserAiRespondFeatureVectorToMemory() {
        try {
            System.out.println("开始加载用户AI响应特征向量数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService_wlq.loadUserAiRespondFeatureVectorToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户AI响应特征向量数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户AI响应特征向量数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户AI响应特征向量数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-ai-respond-feature-vector/all")
    public ResponseEntity<List<UserAiRespondFeatureVector_wlq>> getAllUserAiRespondFeatureVector() {
        try {
            List<UserAiRespondFeatureVector_wlq> userAiRespondFeatureVectors = userEntityService_wlq.getAllUserAiRespondFeatureVectorFromMemory();
            return ResponseEntity.ok(userAiRespondFeatureVectors);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-ai-respond-feature-vector/statistics")
    public ResponseEntity<Map<String, Object>> getUserAiRespondFeatureVectorStatistics() {
        try {
            Map<String, Object> stats = userEntityService_wlq.getUserAiRespondFeatureVectorStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // UserBase 相关接口
    @PostMapping("/user-base/load")
    public ResponseEntity<Map<String, String>> loadUserBaseToMemory() {
        try {
            System.out.println("开始加载用户基础数据到内存...");
            long startTime = System.currentTimeMillis();

            userEntityService_wlq.loadUserBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "用户基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/user-base/all")
    public ResponseEntity<List<UserBase_wlq>> getAllUserBase() {
        try {
            List<UserBase_wlq> userBases = userEntityService_wlq.getAllUserBaseFromMemory();
            return ResponseEntity.ok(userBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/user-base/statistics")
    public ResponseEntity<Map<String, Object>> getUserBaseStatistics() {
        try {
            Map<String, Object> stats = userEntityService_wlq.getUserBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 加载所有U开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllUserEntityDataToMemory() {
        System.out.println("开始加载所有U开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "UserAiRequire", "UserAiRequireFeatureVector", "UserAiRespond",
                "UserAiRespondFeatureVector", "UserBase"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "UserAiRequire":
                        userEntityService_wlq.loadUserAiRequireToMemory();
                        break;
                    case "UserAiRequireFeatureVector":
                        userEntityService_wlq.loadUserAiRequireFeatureVectorToMemory();
                        break;
                    case "UserAiRespond":
                        userEntityService_wlq.loadUserAiRespondToMemory();
                        break;
                    case "UserAiRespondFeatureVector":
                        userEntityService_wlq.loadUserAiRespondFeatureVectorToMemory();
                        break;
                    case "UserBase":
                        userEntityService_wlq.loadUserBaseToMemory();
                        break;
                }
                loadResults.add(Map.of(
                        "entityName", entityName,
                        "status", "success",
                        "message", "加载成功"
                ));
                successCount++;
            } catch (Exception e) {
                loadResults.add(Map.of(
                        "entityName", entityName,
                        "status", "failed",
                        "message", e.getMessage()
                ));
                failCount++;
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        result.put("message", "U开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println("U开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
