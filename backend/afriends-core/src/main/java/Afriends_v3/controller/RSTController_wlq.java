package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.EFMPRTEntityService_wlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * R、S、T开头_wlq实体类控制器
 * 提供所有R、S、T开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/rst-entities-wlq")
@CrossOrigin(origins = "*")
public class RSTController_wlq {

    @Autowired
    private EFMPRTEntityService_wlq efmprtEntityService_wlq;

    // Recharge 相关接口
    @PostMapping("/recharge/load")
    public ResponseEntity<Map<String, String>> loadRechargeToMemory() {
        try {
            System.out.println("开始加载充值数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadRechargeToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "充值数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载充值数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "充值数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/recharge/all")
    public ResponseEntity<List<Recharge_wlq>> getAllRecharge() {
        try {
            List<Recharge_wlq> recharges = efmprtEntityService_wlq.getAllRechargeFromMemory();
            return ResponseEntity.ok(recharges);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/recharge/statistics")
    public ResponseEntity<Map<String, Object>> getRechargeStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getRechargeStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // RecommendedAvatar 相关接口
    @PostMapping("/recommended-avatar/load")
    public ResponseEntity<Map<String, String>> loadRecommendedAvatarToMemory() {
        try {
            System.out.println("开始加载推荐头像数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadRecommendedAvatarToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "推荐头像数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载推荐头像数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "推荐头像数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/recommended-avatar/all")
    public ResponseEntity<List<RecommendedAvatar_wlq>> getAllRecommendedAvatar() {
        try {
            List<RecommendedAvatar_wlq> recommendedAvatars = efmprtEntityService_wlq.getAllRecommendedAvatarFromMemory();
            return ResponseEntity.ok(recommendedAvatars);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/recommended-avatar/statistics")
    public ResponseEntity<Map<String, Object>> getRecommendedAvatarStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getRecommendedAvatarStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 其他实体类的接口按照相同模式实现...
    // RecommendedLabel, RecommendedNickname, SettingBase, SoftTagBase, SoftTagCategory,
    // TextpostBase, TextpostComment, TextpostFeatureVector

    // 加载所有R、S、T开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllRSTEntityDataToMemory() {
        System.out.println("开始加载所有R、S、T开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "Recharge", "RecommendedAvatar", "RecommendedLabel", "RecommendedNickname",
                "SettingBase", "SoftTagBase", "SoftTagCategory", "TextpostBase",
                "TextpostComment", "TextpostFeatureVector"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "Recharge":
                        efmprtEntityService_wlq.loadRechargeToMemory();
                        break;
                    case "RecommendedAvatar":
                        efmprtEntityService_wlq.loadRecommendedAvatarToMemory();
                        break;
                    // 其他实体类的加载逻辑...
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

        result.put("message", "R、S、T开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println("R、S、T开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
