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
 * E、F、M、P、R、T开头_wlq实体类控制器
 * 提供所有E、F、M、P、R、T开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/efmprt-entities-wlq")
@CrossOrigin(origins = "*")
public class EFMPRTEntityController_wlq {

    @Autowired
    private EFMPRTEntityService_wlq efmprtEntityService_wlq;

    // FriendList 相关接口
    @PostMapping("/friend-list/load")
    public ResponseEntity<Map<String, String>> loadFriendListToMemory() {
        try {
            System.out.println("开始加载好友列表数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadFriendListToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "好友列表数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载好友列表数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "好友列表数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/friend-list/all")
    public ResponseEntity<List<FriendList_wlq>> getAllFriendList() {
        try {
            List<FriendList_wlq> friendLists = efmprtEntityService_wlq.getAllFriendListFromMemory();
            return ResponseEntity.ok(friendLists);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/friend-list/statistics")
    public ResponseEntity<Map<String, Object>> getFriendListStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getFriendListStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // GiftBase 相关接口
    @PostMapping("/gift-base/load")
    public ResponseEntity<Map<String, String>> loadGiftBaseToMemory() {
        try {
            System.out.println("开始加载礼物基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadGiftBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "礼物基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载礼物基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "礼物基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/gift-base/all")
    public ResponseEntity<List<GiftBase_wlq>> getAllGiftBase() {
        try {
            List<GiftBase_wlq> giftBases = efmprtEntityService_wlq.getAllGiftBaseFromMemory();
            return ResponseEntity.ok(giftBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/gift-base/statistics")
    public ResponseEntity<Map<String, Object>> getGiftBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getGiftBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // SoftTagBase 相关接口
    @PostMapping("/soft-tag-base/load")
    public ResponseEntity<Map<String, String>> loadSoftTagBaseToMemory() {
        try {
            System.out.println("开始加载软标签基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadSoftTagBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "软标签基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载软标签基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "软标签基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/soft-tag-base/all")
    public ResponseEntity<List<SoftTagBase_wlq>> getAllSoftTagBase() {
        try {
            List<SoftTagBase_wlq> softTagBases = efmprtEntityService_wlq.getAllSoftTagBaseFromMemory();
            return ResponseEntity.ok(softTagBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/soft-tag-base/statistics")
    public ResponseEntity<Map<String, Object>> getSoftTagBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getSoftTagBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // HardTagBase 相关接口
    @PostMapping("/hard-tag-base/load")
    public ResponseEntity<Map<String, String>> loadHardTagBaseToMemory() {
        try {
            System.out.println("开始加载硬标签基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadHardTagBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "硬标签基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载硬标签基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "硬标签基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/hard-tag-base/all")
    public ResponseEntity<List<HardTagBase_wlq>> getAllHardTagBase() {
        try {
            List<HardTagBase_wlq> hardTagBases = efmprtEntityService_wlq.getAllHardTagBaseFromMemory();
            return ResponseEntity.ok(hardTagBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/hard-tag-base/statistics")
    public ResponseEntity<Map<String, Object>> getHardTagBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getHardTagBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // TextpostBase 相关接口
    @PostMapping("/textpost-base/load")
    public ResponseEntity<Map<String, String>> loadTextpostBaseToMemory() {
        try {
            System.out.println("开始加载文字帖子基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadTextpostBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "文字帖子基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载文字帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "文字帖子基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/textpost-base/all")
    public ResponseEntity<List<TextpostBase_wlq>> getAllTextpostBase() {
        try {
            List<TextpostBase_wlq> textpostBases = efmprtEntityService_wlq.getAllTextpostBaseFromMemory();
            return ResponseEntity.ok(textpostBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/textpost-base/statistics")
    public ResponseEntity<Map<String, Object>> getTextpostBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getTextpostBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 其他实体类的接口按照相同模式实现...
    // HardTagBase, IDislike, IDislikeComment, IDislikeFeatureVector, IHave,
    // IHaveComment,
    // IHaveFeatureVector, ILike, ILikeComment, ILikeFeatureVector,
    // ImageContentBase,
    // ImagePostBase, ImagePostComment, ImagePostFeatureVector, INeed, INeedComment,
    // INeedFeatureVector, IntiniallabelChen, LikeRobotActionLog, NovelChapterInfo,
    // NovelContentBase, NovelpostBase, NovelpostComment, NovelpostFeatureVector,
    // Recharge, RecommendedAvatar, RecommendedLabel, RecommendedNickname,
    // SettingBase,
    // SoftTagBase, SoftTagCategory, TextpostBase, TextpostComment,
    // TextpostFeatureVector

    // 加载所有E、F、M、P、R、T开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllEFMPRTEntityDataToMemory() {
        System.out.println("开始加载所有E、F、M、P、R、T开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表（只包含已实现的）
        String[] entityNames = {
                "FriendList", "GiftBase", "SoftTagBase", "HardTagBase", "TextpostBase"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "FriendList":
                        efmprtEntityService_wlq.loadFriendListToMemory();
                        break;
                    case "GiftBase":
                        efmprtEntityService_wlq.loadGiftBaseToMemory();
                        break;
                    case "SoftTagBase":
                        efmprtEntityService_wlq.loadSoftTagBaseToMemory();
                        break;
                    case "HardTagBase":
                        efmprtEntityService_wlq.loadHardTagBaseToMemory();
                        break;
                    case "TextpostBase":
                        efmprtEntityService_wlq.loadTextpostBaseToMemory();
                        break;
                    // 其他实体类的加载逻辑...
                }
                loadResults.add(Map.of(
                        "entityName", entityName,
                        "status", "success",
                        "message", "加载成功"));
                successCount++;
            } catch (Exception e) {
                loadResults.add(Map.of(
                        "entityName", entityName,
                        "status", "failed",
                        "message", e.getMessage()));
                failCount++;
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        result.put("message", "E、F、M、P、R、T开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println(
                "E、F、M、P、R、T开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
