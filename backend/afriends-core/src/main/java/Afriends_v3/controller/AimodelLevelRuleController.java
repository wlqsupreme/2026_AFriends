package Afriends_v3.controller;

import Afriends_v3.entity.AimodelLevelRule_njj;
import Afriends_v3.service.AimodelLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AI模型等级规则控制器
 */
@RestController
@RequestMapping("/api/aimodel-level-rule")
@CrossOrigin(origins = "*")
public class AimodelLevelRuleController {

    @Autowired
    private AimodelLevelRuleService aimodelLevelRuleService;

    /**
     * 加载AI模型等级规则数据到内存
     */
    @PostMapping("/load-to-memory")
    public ResponseEntity<Map<String, Object>> loadAimodelLevelRuleToMemory() {
        try {
            aimodelLevelRuleService.loadAimodelLevelRuleToMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "message", "AI模型等级规则数据加载到内存成功",
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载AI模型等级规则数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "加载AI模型等级规则数据到内存失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有AI模型等级规则
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllLevelRules() {
        try {
            List<AimodelLevelRule_njj> levelRules = aimodelLevelRuleService.getAllLevelRulesFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", levelRules,
                    "count", levelRules.size(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取所有AI模型等级规则失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取所有AI模型等级规则失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据等级获取AI模型等级规则
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<Map<String, Object>> getLevelRuleByLevel(@PathVariable Integer level) {
        try {
            AimodelLevelRule_njj levelRule = aimodelLevelRuleService.getLevelRuleByLevelFromMemory(level);
            if (levelRule != null) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "data", levelRule,
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "等级规则不存在",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            System.err.println("根据等级获取AI模型等级规则失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "根据等级获取AI模型等级规则失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取下一级等级规则
     */
    @GetMapping("/next/{currentLevel}")
    public ResponseEntity<Map<String, Object>> getNextLevelRule(@PathVariable Integer currentLevel) {
        try {
            AimodelLevelRule_njj nextLevelRule = aimodelLevelRuleService.getNextLevelRuleFromMemory(currentLevel);
            if (nextLevelRule != null) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "data", nextLevelRule,
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "下一级等级规则不存在",
                        "timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            System.err.println("获取下一级等级规则失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取下一级等级规则失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有等级规则（按等级排序）
     */
    @GetMapping("/all-ordered")
    public ResponseEntity<Map<String, Object>> getAllLevelRulesOrdered() {
        try {
            List<AimodelLevelRule_njj> levelRules = aimodelLevelRuleService.getAllLevelRulesOrderByLevelFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", levelRules,
                    "count", levelRules.size(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取所有等级规则（按等级排序）失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取所有等级规则（按等级排序）失败: " + e.getMessage(),
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
            Map<String, Object> statistics = aimodelLevelRuleService.getLevelRuleStatisticsFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "statistics", statistics,
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取AI模型等级规则统计信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取AI模型等级规则统计信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }
}
