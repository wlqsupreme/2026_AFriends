package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.BCEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * B和C开头实体类控制器
 * 提供所有B和C开头实体类数据的API接口
 */
@RestController
@RequestMapping("/api/bc-entities")
@CrossOrigin(origins = "*")
public class BCEntityController {

    @Autowired
    private BCEntityService bcEntityService;

    // BlockRecord 相关接口
    @PostMapping("/block-record/load")
    public ResponseEntity<Map<String, String>> loadBlockRecordToMemory() {
        try {
            System.out.println("开始加载屏蔽记录数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadBlockRecordToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("屏蔽记录数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "屏蔽记录数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载屏蔽记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "屏蔽记录数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/block-record/all")
    public ResponseEntity<List<BlockRecord_njj>> getAllBlockRecord() {
        try {
            List<BlockRecord_njj> records = bcEntityService.getAllBlockRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/block-record/statistics")
    public ResponseEntity<Map<String, Object>> getBlockRecordStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getBlockRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/block-record/add")
    public ResponseEntity<Map<String, Object>> addBlockRecord(@RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long blockedUserId = Long.valueOf(requestBody.get("blockedUserId").toString());
            BlockRecord_njj record = bcEntityService.addBlockRecord(userId, blockedUserId);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "拉黑成功");
            result.put("data", record);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @PostMapping("/block-record/unblock")
    public ResponseEntity<Map<String, Object>> unblockUser(@RequestBody Map<String, Object> requestBody) {
        try {
            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long blockedUserId = Long.valueOf(requestBody.get("blockedUserId").toString());
            bcEntityService.unblockUser(userId, blockedUserId);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "解除拉黑成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/block-record/user/{userId}")
    public ResponseEntity<List<BlockRecord_njj>> getBlockedUsersByUserId(@PathVariable Long userId) {
        try {
            List<BlockRecord_njj> records = bcEntityService.getBlockedUsersByUserId(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    // CertBase 相关接口
    @PostMapping("/cert-base/load")
    public ResponseEntity<Map<String, String>> loadCertBaseToMemory() {
        try {
            System.out.println("开始加载证书基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadCertBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("证书基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "证书基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载证书基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "证书基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/cert-base/all")
    public ResponseEntity<List<CertBase_njj>> getAllCertBase() {
        try {
            List<CertBase_njj> certs = bcEntityService.getAllCertBaseFromMemory();
            return ResponseEntity.ok(certs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/cert-base/statistics")
    public ResponseEntity<Map<String, Object>> getCertBaseStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getCertBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // CertJobBase 相关接口
    @PostMapping("/cert-job-base/load")
    public ResponseEntity<Map<String, String>> loadCertJobBaseToMemory() {
        try {
            System.out.println("开始加载证书工作基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadCertJobBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("证书工作基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "证书工作基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载证书工作基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "证书工作基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/cert-job-base/all")
    public ResponseEntity<List<CertJobBase_njj>> getAllCertJobBase() {
        try {
            List<CertJobBase_njj> jobs = bcEntityService.getAllCertJobBaseFromMemory();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/cert-job-base/statistics")
    public ResponseEntity<Map<String, Object>> getCertJobBaseStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getCertJobBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // CertRealnameBase 相关接口
    @PostMapping("/cert-realname-base/load")
    public ResponseEntity<Map<String, String>> loadCertRealnameBaseToMemory() {
        try {
            System.out.println("开始加载实名认证基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadCertRealnameBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("实名认证基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "实名认证基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载实名认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "实名认证基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/cert-realname-base/all")
    public ResponseEntity<List<CertRealnameBase_njj>> getAllCertRealnameBase() {
        try {
            List<CertRealnameBase_njj> realnames = bcEntityService.getAllCertRealnameBaseFromMemory();
            return ResponseEntity.ok(realnames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/cert-realname-base/statistics")
    public ResponseEntity<Map<String, Object>> getCertRealnameBaseStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getCertRealnameBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // CertStudentBase 相关接口
    @PostMapping("/cert-student-base/load")
    public ResponseEntity<Map<String, String>> loadCertStudentBaseToMemory() {
        try {
            System.out.println("开始加载学生证认证基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadCertStudentBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("学生证认证基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "学生证认证基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载学生证认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "学生证认证基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/cert-student-base/all")
    public ResponseEntity<List<CertStudentBase_njj>> getAllCertStudentBase() {
        try {
            List<CertStudentBase_njj> students = bcEntityService.getAllCertStudentBaseFromMemory();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/cert-student-base/statistics")
    public ResponseEntity<Map<String, Object>> getCertStudentBaseStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getCertStudentBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ChatSettingsBase 相关接口
    @PostMapping("/chat-settings-base/load")
    public ResponseEntity<Map<String, String>> loadChatSettingsBaseToMemory() {
        try {
            System.out.println("开始加载聊天设置基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService.loadChatSettingsBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("聊天设置基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "聊天设置基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载聊天设置基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "聊天设置基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/chat-settings-base/all")
    public ResponseEntity<List<ChatSettingsBase_njj>> getAllChatSettingsBase() {
        try {
            List<ChatSettingsBase_njj> settings = bcEntityService.getAllChatSettingsBaseFromMemory();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/chat-settings-base/statistics")
    public ResponseEntity<Map<String, Object>> getChatSettingsBaseStatistics() {
        try {
            Map<String, Object> stats = bcEntityService.getChatSettingsBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 加载所有B和C开头实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllBCEntityDataToMemory() {
        System.out.println("开始加载所有B和C开头实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "BlockRecord", "CertBase", "CertJobBase",
                "CertRealnameBase", "CertStudentBase", "ChatSettingsBase"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            Map<String, Object> entityResult = new HashMap<>();
            entityResult.put("entityName", entityName);
            entityResult.put("status", "loading");

            try {
                long entityStartTime = System.currentTimeMillis();

                switch (entityName) {
                    case "BlockRecord":
                        bcEntityService.loadBlockRecordToMemory();
                        break;
                    case "CertBase":
                        bcEntityService.loadCertBaseToMemory();
                        break;
                    case "CertJobBase":
                        bcEntityService.loadCertJobBaseToMemory();
                        break;
                    case "CertRealnameBase":
                        bcEntityService.loadCertRealnameBaseToMemory();
                        break;
                    case "CertStudentBase":
                        bcEntityService.loadCertStudentBaseToMemory();
                        break;
                    case "ChatSettingsBase":
                        bcEntityService.loadChatSettingsBaseToMemory();
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

        result.put("message", "B和C开头实体类数据加载完成");
        result.put("timestamp", String.valueOf(System.currentTimeMillis()));
        result.put("duration", totalDuration);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("loadResults", loadResults);

        System.out.println("所有B和C开头实体类数据加载完成，成功: " + successCount +
                "，失败: " + failCount + "，总耗时: " + totalDuration + "ms");

        return ResponseEntity.ok(result);
    }
}
