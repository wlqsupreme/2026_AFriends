package Afriends_v3.controller;

import Afriends_v3.entity.AimodelCoinLog_njj;
import Afriends_v3.service.AimodelCoinLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI模型金币日志控制器
 */
@RestController
@RequestMapping("/api/aimodel-coin-log")
@CrossOrigin(origins = "*")
public class AimodelCoinLogController {

    @Autowired
    private AimodelCoinLogService aimodelCoinLogService;

    /**
     * 加载AI模型金币日志数据到内存
     */
    @PostMapping("/load-to-memory")
    public ResponseEntity<Map<String, Object>> loadAimodelCoinLogToMemory() {
        try {
            aimodelCoinLogService.loadAimodelCoinLogToMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "message", "AI模型金币日志数据加载到内存成功",
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI模型金币日志数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "加载AI模型金币日志数据到内存失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有AI模型金币日志
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAimodelCoinLog() {
        try {
            List<AimodelCoinLog_njj> coinLogs = aimodelCoinLogService.getAllAimodelCoinLogFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", coinLogs,
                    "count", coinLogs.size(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取所有AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取所有AI模型金币日志失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据用户ID获取AI模型金币日志
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getAimodelCoinLogByUserId(@PathVariable Long userId) {
        try {
            List<AimodelCoinLog_njj> coinLogs = aimodelCoinLogService.getAimodelCoinLogByUserIdFromMemory(userId);
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", coinLogs,
                    "count", coinLogs.size(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("根据用户ID获取AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "根据用户ID获取AI模型金币日志失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据用户ID和AI模型ID获取金币日志
     */
    @GetMapping("/user/{userId}/ai/{userAiId}")
    public ResponseEntity<Map<String, Object>> getAimodelCoinLogByUserAndAi(
            @PathVariable Long userId,
            @PathVariable Long userAiId) {
        try {
            List<AimodelCoinLog_njj> coinLogs = aimodelCoinLogService.getAimodelCoinLogByUserAndAiFromMemory(userId,
                    userAiId);
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", coinLogs,
                    "count", coinLogs.size(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("根据用户ID和AI模型ID获取金币日志失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "根据用户ID和AI模型ID获取金币日志失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取AI模型当前金币余额
     */
    @GetMapping("/balance/user/{userId}/ai/{userAiId}")
    public ResponseEntity<Map<String, Object>> getCurrentAiCoinBalance(
            @PathVariable Long userId,
            @PathVariable Long userAiId) {
        try {
            Long balance = aimodelCoinLogService.getCurrentAiCoinBalance(userId, userAiId);
            Map<String, Object> result = Map.of(
                    "success", true,
                    "userId", userId,
                    "userAiId", userAiId,
                    "balance", balance,
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取AI模型金币余额失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取AI模型金币余额失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 创建AI模型金币日志
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAimodelCoinLog(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long userAiId = Long.valueOf(request.get("userAiId").toString());
            Long amount = Long.valueOf(request.get("amount").toString());
            Long totalAfter = Long.valueOf(request.get("totalAfter").toString());

            boolean success = aimodelCoinLogService.createAimodelCoinLog(userId, userAiId, amount, totalAfter);

            if (success) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "message", "AI模型金币日志创建成功",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "AI模型金币日志创建失败",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(400).body(result);
            }
        } catch (Exception e) {
            System.err.println("创建AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "创建AI模型金币日志失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 批量创建AI模型金币日志
     */
    @PostMapping("/create-batch")
    public ResponseEntity<Map<String, Object>> createAimodelCoinLogs(@RequestBody List<AimodelCoinLog_njj> coinLogs) {
        try {
            if (coinLogs == null || coinLogs.isEmpty()) {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "金币日志列表不能为空",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(result);
            }

            boolean success = aimodelCoinLogService.createAimodelCoinLogs(coinLogs);

            if (success) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "message", "AI模型金币日志批量创建成功",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "AI模型金币日志批量创建失败",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(400).body(result);
            }
        } catch (Exception e) {
            System.err.println("批量创建AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "批量创建AI模型金币日志失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = aimodelCoinLogService.getAimodelCoinLogStatisticsFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "statistics", statistics,
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取AI模型金币日志统计信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取AI模型金币日志统计信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }
}
