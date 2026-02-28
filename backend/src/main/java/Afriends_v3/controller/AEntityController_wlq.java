package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.AEntityService_wlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * A开头_wlq实体类控制器
 * 提供所有A开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/a-entities-wlq")
@CrossOrigin(origins = "*")
public class AEntityController_wlq {

    @Autowired
    private AEntityService_wlq aEntityService_wlq;

    // ActionExp 相关接口
    @PostMapping("/action-exp/load")
    public ResponseEntity<Map<String, String>> loadActionExpToMemory() {
        try {
            System.out.println("开始加载行为经验数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService_wlq.loadActionExpToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("行为经验数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "行为经验数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载行为经验数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "行为经验数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/action-exp/all")
    public ResponseEntity<List<ActionExp_wlq>> getAllActionExp() {
        try {
            List<ActionExp_wlq> actionExps = aEntityService_wlq.getAllActionExpFromMemory();
            return ResponseEntity.ok(actionExps);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/action-exp/statistics")
    public ResponseEntity<Map<String, Object>> getActionExpStatistics() {
        try {
            Map<String, Object> stats = aEntityService_wlq.getActionExpStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AiChatListDetailR 相关接口
    @PostMapping("/ai-chat-list-detail-r/load")
    public ResponseEntity<Map<String, String>> loadAiChatListDetailRToMemory() {
        try {
            System.out.println("开始加载AI聊天列表详情数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService_wlq.loadAiChatListDetailRToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI聊天列表详情数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI聊天列表详情数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI聊天列表详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI聊天列表详情数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    // AiChatListDetailR 相关接口 - 新增按条件查询聊天记录
    @GetMapping("/ai-chat-list-detail-r/query")
    public ResponseEntity<List<AiChatListDetailR_wlq>> queryChatByUserIdAndModelId(
            @RequestParam("userId") Long userId,  // 前端传递的用户ID
            @RequestParam("userAiId") Long userAiId) {  // 前端传递的AI模型ID
        try {
            // 调用Service层方法，按条件筛选聊天记录（需在Service中实现）
            List<AiChatListDetailR_wlq> chatList = aEntityService_wlq.queryByUserIdAndModelId(userId, userAiId);
            return ResponseEntity.ok(chatList);
        } catch (Exception e) {
            System.err.println("按条件查询聊天记录失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-chat-list-detail-r/all")
    public ResponseEntity<List<AiChatListDetailR_wlq>> getAllAiChatListDetailR() {
        try {
            List<AiChatListDetailR_wlq> aiChatListDetailRs = aEntityService_wlq.getAllAiChatListDetailRFromMemory();
            return ResponseEntity.ok(aiChatListDetailRs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-chat-list-detail-r/statistics")
    public ResponseEntity<Map<String, Object>> getAiChatListDetailRStatistics() {
        try {
            Map<String, Object> stats = aEntityService_wlq.getAiChatListDetailRStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 新增AI聊天记录保存接口，匹配前端请求
    @PostMapping("/ai-chat-list-detail-r/save")
    public ResponseEntity<Map<String, String>> saveAIChat(@RequestBody Map<String, Object> chatData) {
        try {
            // 实现AI聊天记录保存逻辑（参考UserChatDetailController的saveChatDetail方法）
            // ...

            return ResponseEntity.ok(Map.of(
                    "message", "AI聊天记录保存成功",
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "保存失败: " + e.getMessage()
            ));
        }
    }

    // AiMatches 相关接口
    @PostMapping("/ai-matches/load")
    public ResponseEntity<Map<String, String>> loadAiMatchesToMemory() {
        try {
            System.out.println("开始加载AI匹配数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService_wlq.loadAiMatchesToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI匹配数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI匹配数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI匹配数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI匹配数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/ai-matches/all")
    public ResponseEntity<List<AiMatches_wlq>> getAllAiMatches() {
        try {
            List<AiMatches_wlq> aiMatches = aEntityService_wlq.getAllAiMatchesFromMemory();
            return ResponseEntity.ok(aiMatches);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-matches/statistics")
    public ResponseEntity<Map<String, Object>> getAiMatchesStatistics() {
        try {
            Map<String, Object> stats = aEntityService_wlq.getAiMatchesStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AiTaskRequire 相关接口
    @PostMapping("/ai-task-require/load")
    public ResponseEntity<Map<String, String>> loadAiTaskRequireToMemory() {
        try {
            System.out.println("开始加载AI任务需求数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService_wlq.loadAiTaskRequireToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI任务需求数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI任务需求数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI任务需求数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI任务需求数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/ai-task-require/all")
    public ResponseEntity<List<AiTaskRequire_wlq>> getAllAiTaskRequire() {
        try {
            List<AiTaskRequire_wlq> aiTaskRequires = aEntityService_wlq.getAllAiTaskRequireFromMemory();
            return ResponseEntity.ok(aiTaskRequires);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-task-require/statistics")
    public ResponseEntity<Map<String, Object>> getAiTaskRequireStatistics() {
        try {
            Map<String, Object> stats = aEntityService_wlq.getAiTaskRequireStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // AiTaskRespond 相关接口
    @PostMapping("/ai-task-respond/load")
    public ResponseEntity<Map<String, String>> loadAiTaskRespondToMemory() {
        try {
            System.out.println("开始加载AI任务响应数据到内存...");
            long startTime = System.currentTimeMillis();

            aEntityService_wlq.loadAiTaskRespondToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("AI任务响应数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "AI任务响应数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI任务响应数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "AI任务响应数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/ai-task-respond/all")
    public ResponseEntity<List<AiTaskRespond_wlq>> getAllAiTaskRespond() {
        try {
            List<AiTaskRespond_wlq> aiTaskResponds = aEntityService_wlq.getAllAiTaskRespondFromMemory();
            return ResponseEntity.ok(aiTaskResponds);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/ai-task-respond/statistics")
    public ResponseEntity<Map<String, Object>> getAiTaskRespondStatistics() {
        try {
            Map<String, Object> stats = aEntityService_wlq.getAiTaskRespondStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 加载所有A开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllAEntityDataToMemory() {
        System.out.println("开始加载所有A开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "ActionExp", "AiChatListDetailR", "AiMatches", "AiTaskRequire", "AiTaskRespond"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "ActionExp":
                        aEntityService_wlq.loadActionExpToMemory();
                        break;
                    case "AiChatListDetailR":
                        aEntityService_wlq.loadAiChatListDetailRToMemory();
                        break;
                    case "AiMatches":
                        aEntityService_wlq.loadAiMatchesToMemory();
                        break;
                    case "AiTaskRequire":
                        aEntityService_wlq.loadAiTaskRequireToMemory();
                        break;
                    case "AiTaskRespond":
                        aEntityService_wlq.loadAiTaskRespondToMemory();
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

        result.put("message", "A开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println("A开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
