package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.EFMPRTEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * E、F、M、P、R、T开头实体类控制器
 * 提供所有E、F、M、P、R、T开头实体类数据的API接口
 */
@RestController
@RequestMapping("/api/efmprt-entities")
@CrossOrigin(origins = "*")
public class EFMPRTEntityController {

    @Autowired
    private EFMPRTEntityService efmprtEntityService;

    // EmojiBase 相关接口
    @PostMapping("/emoji-base/load")
    public ResponseEntity<Map<String, String>> loadEmojiBaseToMemory() {
        try {
            System.out.println("开始加载表情基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadEmojiBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("表情基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "表情基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载表情基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "表情基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/emoji-base/all")
    public ResponseEntity<List<EmojiBase_njj>> getAllEmojiBase() {
        try {
            List<EmojiBase_njj> emojis = efmprtEntityService.getAllEmojiBaseFromMemory();
            return ResponseEntity.ok(emojis);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/emoji-base/statistics")
    public ResponseEntity<Map<String, Object>> getEmojiBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getEmojiBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // FriendsProfile 相关接口
    @PostMapping("/friends-profile/load")
    public ResponseEntity<Map<String, String>> loadFriendsProfileToMemory() {
        try {
            System.out.println("开始加载好友资料数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadFriendsProfileToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("好友资料数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "好友资料数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载好友资料数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "好友资料数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/friends-profile/all")
    public ResponseEntity<List<FriendsProfile_njj>> getAllFriendsProfile() {
        try {
            List<FriendsProfile_njj> profiles = efmprtEntityService.getAllFriendsProfileFromMemory();
            return ResponseEntity.ok(profiles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/friends-profile/statistics")
    public ResponseEntity<Map<String, Object>> getFriendsProfileStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getFriendsProfileStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // MessageExtraBase 相关接口
    @PostMapping("/message-extra-base/load")
    public ResponseEntity<Map<String, String>> loadMessageExtraBaseToMemory() {
        try {
            System.out.println("开始加载消息扩展基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadMessageExtraBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("消息扩展基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "消息扩展基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载消息扩展基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "消息扩展基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/message-extra-base/all")
    public ResponseEntity<List<MessageExtraBase_njj>> getAllMessageExtraBase() {
        try {
            List<MessageExtraBase_njj> messages = efmprtEntityService.getAllMessageExtraBaseFromMemory();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/message-extra-base/statistics")
    public ResponseEntity<Map<String, Object>> getMessageExtraBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getMessageExtraBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ProductsBase 相关接口
    @PostMapping("/products-base/load")
    public ResponseEntity<Map<String, String>> loadProductsBaseToMemory() {
        try {
            System.out.println("开始加载产品基础信息数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadProductsBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("产品基础信息数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "产品基础信息数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载产品基础信息数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "产品基础信息数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/products-base/all")
    public ResponseEntity<List<ProductsBase_njj>> getAllProductsBase() {
        try {
            List<ProductsBase_njj> products = efmprtEntityService.getAllProductsBaseFromMemory();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/products-base/statistics")
    public ResponseEntity<Map<String, Object>> getProductsBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getProductsBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // PurchaseRecord 相关接口
    @PostMapping("/purchase-record/load")
    public ResponseEntity<Map<String, String>> loadPurchaseRecordToMemory() {
        try {
            System.out.println("开始加载购买记录数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadPurchaseRecordToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("购买记录数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "购买记录数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载购买记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "购买记录数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/purchase-record/all")
    public ResponseEntity<List<PurchaseRecord_njj>> getAllPurchaseRecord() {
        try {
            List<PurchaseRecord_njj> records = efmprtEntityService.getAllPurchaseRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/purchase-record/statistics")
    public ResponseEntity<Map<String, Object>> getPurchaseRecordStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getPurchaseRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ReportCategory 相关接口
    @PostMapping("/report-category/load")
    public ResponseEntity<Map<String, String>> loadReportCategoryToMemory() {
        try {
            System.out.println("开始加载举报类别数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadReportCategoryToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("举报类别数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "举报类别数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载举报类别数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "举报类别数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/report-category/all")
    public ResponseEntity<List<ReportCategory_njj>> getAllReportCategory() {
        try {
            List<ReportCategory_njj> categories = efmprtEntityService.getAllReportCategoryFromMemory();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/report-category/statistics")
    public ResponseEntity<Map<String, Object>> getReportCategoryStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getReportCategoryStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ReportRecord 相关接口
    @PostMapping("/report-record/load")
    public ResponseEntity<Map<String, String>> loadReportRecordToMemory() {
        try {
            System.out.println("开始加载举报记录数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadReportRecordToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("举报记录数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "举报记录数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载举报记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "举报记录数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/report-record/all")
    public ResponseEntity<List<ReportRecord_njj>> getAllReportRecord() {
        try {
            List<ReportRecord_njj> records = efmprtEntityService.getAllReportRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/report-record/statistics")
    public ResponseEntity<Map<String, Object>> getReportRecordStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getReportRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // TaskComment 相关接口
    @PostMapping("/task-comment/load")
    public ResponseEntity<Map<String, String>> loadTaskCommentToMemory() {
        try {
            System.out.println("开始加载任务评论数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService.loadTaskCommentToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("任务评论数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "任务评论数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载任务评论数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "任务评论数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/task-comment/all")
    public ResponseEntity<List<TaskComment_njj>> getAllTaskComment() {
        try {
            List<TaskComment_njj> comments = efmprtEntityService.getAllTaskCommentFromMemory();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/task-comment/statistics")
    public ResponseEntity<Map<String, Object>> getTaskCommentStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService.getTaskCommentStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/task-comment/save")
    public ResponseEntity<Map<String, Object>> saveTaskComment(@RequestBody Map<String, Object> requestData) {
        try {
            Map<String, Object> result = efmprtEntityService.saveTaskComment(requestData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "保存任务评论失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    @GetMapping("/task-comment/check")
    public ResponseEntity<Map<String, Object>> checkTaskComment(@RequestParam Long userId, @RequestParam Long taskId) {
        try {
            boolean exists = efmprtEntityService.checkTaskCommentExists(userId, taskId);
            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("exists", false);
            errorResult.put("error", "检查任务评论失败: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }

    // 加载所有E、F、M、P、R、T开头实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllEFMPRTEntityDataToMemory() {
        System.out.println("开始加载所有E、F、M、P、R、T开头实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "EmojiBase", "FriendsProfile", "MessageExtraBase",
                "ProductsBase", "PurchaseRecord", "ReportCategory",
                "ReportRecord", "TaskComment"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            Map<String, Object> entityResult = new HashMap<>();
            entityResult.put("entityName", entityName);
            entityResult.put("status", "loading");

            try {
                long entityStartTime = System.currentTimeMillis();

                switch (entityName) {
                    case "EmojiBase":
                        efmprtEntityService.loadEmojiBaseToMemory();
                        break;
                    case "FriendsProfile":
                        efmprtEntityService.loadFriendsProfileToMemory();
                        break;
                    case "MessageExtraBase":
                        efmprtEntityService.loadMessageExtraBaseToMemory();
                        break;
                    case "ProductsBase":
                        efmprtEntityService.loadProductsBaseToMemory();
                        break;
                    case "PurchaseRecord":
                        efmprtEntityService.loadPurchaseRecordToMemory();
                        break;
                    case "ReportCategory":
                        efmprtEntityService.loadReportCategoryToMemory();
                        break;
                    case "ReportRecord":
                        efmprtEntityService.loadReportRecordToMemory();
                        break;
                    case "TaskComment":
                        efmprtEntityService.loadTaskCommentToMemory();
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

        result.put("message", "E、F、M、P、R、T开头实体类数据加载完成");
        result.put("timestamp", String.valueOf(System.currentTimeMillis()));
        result.put("duration", totalDuration);
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("loadResults", loadResults);

        System.out.println("所有E、F、M、P、R、T开头实体类数据加载完成，成功: " + successCount +
                "，失败: " + failCount + "，总耗时: " + totalDuration + "ms");

        return ResponseEntity.ok(result);
    }
}
