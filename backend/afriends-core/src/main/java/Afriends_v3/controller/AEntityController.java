package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.AEntityService;
import Afriends_v3.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A开头实体类控制器
 * 提供所有A开头实体类数据的API接口
 */
@RestController
@RequestMapping("/api/a-entities")
@CrossOrigin(origins = "*")
public class AEntityController {

    @Autowired
    private AEntityService aEntityService;

    @Autowired
    private AchievementService achievementService;

    // AgreementBase 相关接口
    @PostMapping("/agreement-base/load")
    public ResponseEntity<Map<String, String>> loadAgreementBaseToMemory() {
        try {
            System.out.println("开始加载协议基础数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService.loadAgreementBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("协议基础数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "协议基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载协议基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "协议基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/agreement-base/all")
    public ResponseEntity<List<AgreementBase_njj>> getAllAgreementBase() {
        try {
            List<AgreementBase_njj> agreements = aEntityService.getAllAgreementBaseFromMemory();
            return ResponseEntity.ok(agreements);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/agreement-base/statistics")
    public ResponseEntity<Map<String, Object>> getAgreementBaseStatistics() {
        try {
            Map<String, Object> stats = aEntityService.getAgreementBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AimodelBaseInfo 相关接口
    @PostMapping("/aimodel-base-info/load")
    public ResponseEntity<Map<String, String>> loadAimodelBaseInfoToMemory() {
        try {
            System.out.println("开始加载AI模型基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService.loadAimodelBaseInfoToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI模型基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI模型基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI模型基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI模型基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/aimodel-base-info/all")
    public ResponseEntity<List<AimodelBaseInfo_njj>> getAllAimodelBaseInfo() {
        try {
            List<AimodelBaseInfo_njj> aimodels = aEntityService.getAllAimodelBaseInfoFromMemory();
            return ResponseEntity.ok(aimodels);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/aimodel-base-info/statistics")
    public ResponseEntity<Map<String, Object>> getAimodelBaseInfoStatistics() {
        try {
            Map<String, Object> stats = aEntityService.getAimodelBaseInfoStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/aimodel-base-info/{modelId}")
    public ResponseEntity<AimodelBaseInfo_njj> getAimodelBaseInfoById(@PathVariable Long modelId) {
        try {
            AimodelBaseInfo_njj aimodel = aEntityService.getAimodelBaseInfoByIdFromMemory(modelId);
            if (aimodel != null) {
                return ResponseEntity.ok(aimodel);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AimodelCoinLog 相关接口
    @PostMapping("/aimodel-coin-log/load")
    public ResponseEntity<Map<String, String>> loadAimodelCoinLogToMemory() {
        try {
            System.out.println("开始加载AI模型币日志数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService.loadAimodelCoinLogToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI模型币日志数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI模型币日志数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI模型币日志数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI模型币日志数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/aimodel-coin-log/all")
    public ResponseEntity<List<AimodelCoinLog_njj>> getAllAimodelCoinLog() {
        try {
            List<AimodelCoinLog_njj> coinLogs = aEntityService.getAllAimodelCoinLogFromMemory();
            return ResponseEntity.ok(coinLogs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/aimodel-coin-log/statistics")
    public ResponseEntity<Map<String, Object>> getAimodelCoinLogStatistics() {
        try {
            Map<String, Object> stats = aEntityService.getAimodelCoinLogStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AimodelLevelRule 相关接口
    @PostMapping("/aimodel-level-rule/load")
    public ResponseEntity<Map<String, String>> loadAimodelLevelRuleToMemory() {
        try {
            System.out.println("开始加载AI模型等级规则数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService.loadAimodelLevelRuleToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI模型等级规则数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI模型等级规则数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI模型等级规则数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI模型等级规则数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/aimodel-level-rule/all")
    public ResponseEntity<List<AimodelLevelRule_njj>> getAllAimodelLevelRule() {
        try {
            List<AimodelLevelRule_njj> levelRules = aEntityService.getAllAimodelLevelRuleFromMemory();
            return ResponseEntity.ok(levelRules);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/aimodel-level-rule/statistics")
    public ResponseEntity<Map<String, Object>> getAimodelLevelRuleStatistics() {
        try {
            Map<String, Object> stats = aEntityService.getAimodelLevelRuleStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AiTaskLog 相关接口
    @PostMapping("/ai-task-log/load")
    public ResponseEntity<Map<String, String>> loadAiTaskLogToMemory() {
        try {
            System.out.println("开始加载AI任务日志数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService.loadAiTaskLogToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI任务日志数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI任务日志数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI任务日志数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI任务日志数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/ai-task-log/all")
    public ResponseEntity<List<AiTaskLog_njj>> getAllAiTaskLog() {
        try {
            List<AiTaskLog_njj> taskLogs = aEntityService.getAllAiTaskLogFromMemory();
            return ResponseEntity.ok(taskLogs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-task-log/statistics")
    public ResponseEntity<Map<String, Object>> getAiTaskLogStatistics() {
        try {
            Map<String, Object> stats = aEntityService.getAiTaskLogStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-task-log/by-user-task/{userTaskId}")
    public ResponseEntity<Map<String, Object>> getAiTaskLogByUserTaskId(@PathVariable(required = true) String userTaskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("=== 接收路径参数 ===");
            System.out.println("参数值: " + userTaskId);
            System.out.println("参数类型: " + (userTaskId == null ? "null" : userTaskId.getClass().getName()));
            // 1. 校验参数非空
            if (userTaskId == null || userTaskId.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "taskId不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            // 2. 转换为 Long，处理格式错误
            Long userTaskId1 = Long.parseLong(userTaskId);
            System.out.println("AI任务ID Long: " + userTaskId1);
            // 目前两个表的ID是相同的，直接使用userTaskId作为aiTaskLogId
            // 如果将来有关联字段，可以在这里添加查询逻辑
            AiTaskLog_njj taskLog = aEntityService.getAiTaskLogByIdFromMemory(userTaskId1);
            System.out.println("AI: " + taskLog);
            if (taskLog != null) {
                // 获取用户任务关系信息，包含进度
                UserTaskRelationship_njj userTask = UserTaskRelationship_list_njj.getUserTaskRelationshipById(userTaskId1);
                
//                Map<String, Object> result = new HashMap<>();
                result.put("aiTaskLog", taskLog);
                if (userTask != null) {
                    result.put("taskPercent", userTask.getTaskPercent());
                    result.put("userTaskStatus", userTask.getStatus());
                } else {
                    result.put("taskPercent", 0);
                    result.put("userTaskStatus", 0);
                }
                
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/ai-task-log/start-task/{taskId}")
    public ResponseEntity<Map<String, Object>> startTask(@PathVariable Long taskId) {
        try {
            System.out.println("开始任务，任务ID: " + taskId);

            Map<String, Object> result = aEntityService.startTask(taskId);

            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(400).body(result);
            }
        } catch (Exception e) {
            System.err.println("开始任务失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "开始任务失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // 加载所有A开头实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllAEntityDataToMemory() {
        System.out.println("开始加载所有A开头实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "AchievementBase", "AchievementRecord", "AgreementBase",
                "AimodelBaseInfo", "AimodelCoinLog", "AimodelLevelRule", "AiTaskLog"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            Map<String, Object> entityResult = new HashMap<>();
            entityResult.put("entityName", entityName);
            entityResult.put("status", "loading");

            try {
                long entityStartTime = System.currentTimeMillis();

                switch (entityName) {
                    case "AchievementBase":
                        achievementService.loadAchievementBaseToMemory();
                        break;
                    case "AchievementRecord":
                        achievementService.loadAchievementRecordToMemory();
                        break;
                    case "AgreementBase":
                        aEntityService.loadAgreementBaseToMemory();
                        break;
                    case "AimodelBaseInfo":
                        aEntityService.loadAimodelBaseInfoToMemory();
                        break;
                    case "AimodelCoinLog":
                        aEntityService.loadAimodelCoinLogToMemory();
                        break;
                    case "AimodelLevelRule":
                        aEntityService.loadAimodelLevelRuleToMemory();
                        break;
                    case "AiTaskLog":
                        aEntityService.loadAiTaskLogToMemory();
                        break;
                }

                long entityEndTime = System.currentTimeMillis();
                long entityDuration = entityEndTime - entityStartTime;

                entityResult.put("status", "success");
                entityResult.put("duration", entityDuration);
                entityResult.put("message", entityName + " 加载成功，耗时: " + entityDuration + "ms");
                successCount++;

                System.out.println(entityName + " 加载成功，耗时: " + entityDuration + "ms");

            } catch (Exception e) {
                entityResult.put("status", "failed");
                entityResult.put("error", e.getMessage());
                entityResult.put("message", entityName + " 加载失败: " + e.getMessage());
                failCount++;

                System.err.println(entityName + " 加载失败: " + e.getMessage());
                e.printStackTrace();
            }

            loadResults.add(entityResult);
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        result.put("message", "A开头实体类数据加载完成");
        result.put("timestamp", String.valueOf(System.currentTimeMillis()));
        result.put("duration", totalDuration);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("loadResults", loadResults);

        System.out.println("所有A开头实体类数据加载完成，成功: " + successCount +
                "，失败: " + failCount + "，总耗时: " + totalDuration + "ms");

        return ResponseEntity.ok(result);
    }
}
