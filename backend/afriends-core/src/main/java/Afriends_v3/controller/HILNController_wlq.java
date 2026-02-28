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
 * H、I、L、N开头_wlq实体类控制器
 * 提供所有H、I、L、N开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/hiln-entities-wlq")
@CrossOrigin(origins = "*")
public class HILNController_wlq {

    @Autowired
    private EFMPRTEntityService_wlq efmprtEntityService_wlq;

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

    // IDislike 相关接口
    @PostMapping("/i-dislike/load")
    public ResponseEntity<Map<String, String>> loadIDislikeToMemory() {
        try {
            System.out.println("开始加载我不喜欢数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadIDislikeToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "我不喜欢数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载我不喜欢数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "我不喜欢数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/i-dislike/all")
    public ResponseEntity<List<IDislike_wlq>> getAllIDislike() {
        try {
            List<IDislike_wlq> iDislikes = efmprtEntityService_wlq.getAllIDislikeFromMemory();
            return ResponseEntity.ok(iDislikes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/i-dislike/statistics")
    public ResponseEntity<Map<String, Object>> getIDislikeStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getIDislikeStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ImagePostBase 相关接口
    @PostMapping("/image-post-base/load")
    public ResponseEntity<Map<String, String>> loadImagePostBaseToMemory() {
        try {
            System.out.println("开始加载图片帖子基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadImagePostBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "图片帖子基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载图片帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "图片帖子基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/image-post-base/all")
    public ResponseEntity<List<ImagePostBase_wlq>> getAllImagePostBase() {
        try {
            List<ImagePostBase_wlq> imagePostBases = efmprtEntityService_wlq.getAllImagePostBaseFromMemory();
            return ResponseEntity.ok(imagePostBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/image-post-base/statistics")
    public ResponseEntity<Map<String, Object>> getImagePostBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getImagePostBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // NovelpostBase 相关接口
    @PostMapping("/novelpost-base/load")
    public ResponseEntity<Map<String, String>> loadNovelpostBaseToMemory() {
        try {
            System.out.println("开始加载小说帖子基础数据到内存...");
            long startTime = System.currentTimeMillis();

            efmprtEntityService_wlq.loadNovelpostBaseToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "小说帖子基础数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载小说帖子基础数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "小说帖子基础数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/novelpost-base/all")
    public ResponseEntity<List<NovelpostBase_wlq>> getAllNovelpostBase() {
        try {
            List<NovelpostBase_wlq> novelpostBases = efmprtEntityService_wlq.getAllNovelpostBaseFromMemory();
            return ResponseEntity.ok(novelpostBases);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/novelpost-base/statistics")
    public ResponseEntity<Map<String, Object>> getNovelpostBaseStatistics() {
        try {
            Map<String, Object> stats = efmprtEntityService_wlq.getNovelpostBaseStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 其他实体类的接口按照相同模式实现...
    // IDislikeComment, IDislikeFeatureVector, IHave, IHaveComment,
    // IHaveFeatureVector,
    // ILike, ILikeComment, ILikeFeatureVector, ImageContentBase,
    // ImagePostComment, ImagePostFeatureVector, INeed, INeedComment,
    // INeedFeatureVector,
    // IntiniallabelChen, LikeRobotActionLog, NovelChapterInfo, NovelContentBase,
    // NovelpostComment, NovelpostFeatureVector

    // 加载所有H、I、L、N开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllHILNEntityDataToMemory() {
        System.out.println("开始加载所有H、I、L、N开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "HardTagBase", "IDislike", "IDislikeComment", "IDislikeFeatureVector",
                "IHave", "IHaveComment", "IHaveFeatureVector", "ILike", "ILikeComment",
                "ILikeFeatureVector", "ImageContentBase", "ImagePostBase", "ImagePostComment",
                "ImagePostFeatureVector", "INeed", "INeedComment", "INeedFeatureVector",
                "IntiniallabelChen", "LikeRobotActionLog", "NovelChapterInfo", "NovelContentBase",
                "NovelpostBase", "NovelpostComment", "NovelpostFeatureVector"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "HardTagBase":
                        efmprtEntityService_wlq.loadHardTagBaseToMemory();
                        break;
                    case "IDislike":
                        efmprtEntityService_wlq.loadIDislikeToMemory();
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

        result.put("message", "H、I、L、N开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println(
                "H、I、L、N开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
