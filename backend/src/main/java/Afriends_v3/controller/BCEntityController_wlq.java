package Afriends_v3.controller;

import Afriends_v3.entity.*;
import Afriends_v3.service.BCEntityService_wlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * B和C开头_wlq实体类控制器
 * 提供所有B和C开头_wlq实体类数据的API接口
 */
@RestController
@RequestMapping("/api/bc-entities-wlq")
@CrossOrigin(origins = "*")
public class BCEntityController_wlq {

    @Autowired
    private BCEntityService_wlq bcEntityService_wlq;

    // CommentLikeRelation 相关接口
    @PostMapping("/comment-like-relation/load")
    public ResponseEntity<Map<String, String>> loadCommentLikeRelationToMemory() {
        try {
            System.out.println("开始加载评论点赞关系数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService_wlq.loadCommentLikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "评论点赞关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载评论点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "评论点赞关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/comment-like-relation/all")
    public ResponseEntity<List<CommentLikeRelation_wlq>> getAllCommentLikeRelation() {
        try {
            List<CommentLikeRelation_wlq> commentLikeRelations = bcEntityService_wlq.getAllCommentLikeRelationFromMemory();
            return ResponseEntity.ok(commentLikeRelations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/comment-like-relation/statistics")
    public ResponseEntity<Map<String, Object>> getCommentLikeRelationStatistics() {
        try {
            Map<String, Object> stats = bcEntityService_wlq.getCommentLikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ContentDislikeRelation 相关接口
    @PostMapping("/content-dislike-relation/load")
    public ResponseEntity<Map<String, String>> loadContentDislikeRelationToMemory() {
        try {
            System.out.println("开始加载内容不喜欢关系数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService_wlq.loadContentDislikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            Map<String, String> result = Map.of(
                    "message", "内容不喜欢关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载内容不喜欢关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "内容不喜欢关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/content-dislike-relation/all")
    public ResponseEntity<List<ContentDislikeRelation_wlq>> getAllContentDislikeRelation() {
        try {
            List<ContentDislikeRelation_wlq> contentDislikeRelations = bcEntityService_wlq.getAllContentDislikeRelationFromMemory();
            return ResponseEntity.ok(contentDislikeRelations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/content-dislike-relation/statistics")
    public ResponseEntity<Map<String, Object>> getContentDislikeRelationStatistics() {
        try {
            Map<String, Object> stats = bcEntityService_wlq.getContentDislikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ContentFavouriteRelation 相关接口
    @PostMapping("/content-favourite-relation/load")
    public ResponseEntity<Map<String, String>> loadContentFavouriteRelationToMemory() {
        try {
            System.out.println("开始加载内容收藏关系数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService_wlq.loadContentFavouriteRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("内容收藏关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "内容收藏关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载内容收藏关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "内容收藏关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/content-favourite-relation/all")
    public ResponseEntity<List<ContentFavouriteRelation_wlq>> getAllContentFavouriteRelation() {
        try {
            List<ContentFavouriteRelation_wlq> contentFavouriteRelations = bcEntityService_wlq.getAllContentFavouriteRelationFromMemory();
            return ResponseEntity.ok(contentFavouriteRelations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/content-favourite-relation/statistics")
    public ResponseEntity<Map<String, Object>> getContentFavouriteRelationStatistics() {
        try {
            Map<String, Object> stats = bcEntityService_wlq.getContentFavouriteRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ContentFeedbackRelation 相关接口
    @PostMapping("/content-feedback-relation/load")
    public ResponseEntity<Map<String, String>> loadContentFeedbackRelationToMemory() {
        try {
            System.out.println("开始加载内容反馈关系数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService_wlq.loadContentFeedbackRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("内容反馈关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "内容反馈关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载内容反馈关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "内容反馈关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/content-feedback-relation/all")
    public ResponseEntity<List<ContentFeedbackRelation_wlq>> getAllContentFeedbackRelation() {
        try {
            List<ContentFeedbackRelation_wlq> contentFeedbackRelations = bcEntityService_wlq.getAllContentFeedbackRelationFromMemory();
            return ResponseEntity.ok(contentFeedbackRelations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/content-feedback-relation/statistics")
    public ResponseEntity<Map<String, Object>> getContentFeedbackRelationStatistics() {
        try {
            Map<String, Object> stats = bcEntityService_wlq.getContentFeedbackRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ContentLikeRelation 相关接口
    @PostMapping("/content-like-relation/load")
    public ResponseEntity<Map<String, String>> loadContentLikeRelationToMemory() {
        try {
            System.out.println("开始加载内容点赞关系数据到内存...");
            long startTime = System.currentTimeMillis();

            bcEntityService_wlq.loadContentLikeRelationToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("内容点赞关系数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "内容点赞关系数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载内容点赞关系数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "内容点赞关系数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/content-like-relation/all")
    public ResponseEntity<List<ContentLikeRelation_wlq>> getAllContentLikeRelation() {
        try {
            List<ContentLikeRelation_wlq> contentLikeRelations = bcEntityService_wlq.getAllContentLikeRelationFromMemory();
            return ResponseEntity.ok(contentLikeRelations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/content-like-relation/statistics")
    public ResponseEntity<Map<String, Object>> getContentLikeRelationStatistics() {
        try {
            Map<String, Object> stats = bcEntityService_wlq.getContentLikeRelationStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 加载所有B和C开头_wlq实体类数据
    @PostMapping("/load-all")
    public ResponseEntity<Map<String, Object>> loadAllBCEntityDataToMemory() {
        System.out.println("开始加载所有B和C开头_wlq实体类数据到内存...");
        long startTime = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> loadResults = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 定义要加载的实体类列表
        String[] entityNames = {
                "CommentLikeRelation", "ContentDislikeRelation", "ContentFavouriteRelation",
                "ContentFeedbackRelation", "ContentLikeRelation"
        };

        // 逐个加载每个实体类
        for (String entityName : entityNames) {
            try {
                switch (entityName) {
                    case "CommentLikeRelation":
                        bcEntityService_wlq.loadCommentLikeRelationToMemory();
                        break;
                    case "ContentDislikeRelation":
                        bcEntityService_wlq.loadContentDislikeRelationToMemory();
                        break;
                    case "ContentFavouriteRelation":
                        bcEntityService_wlq.loadContentFavouriteRelationToMemory();
                        break;
                    case "ContentFeedbackRelation":
                        bcEntityService_wlq.loadContentFeedbackRelationToMemory();
                        break;
                    case "ContentLikeRelation":
                        bcEntityService_wlq.loadContentLikeRelationToMemory();
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

        result.put("message", "B和C开头_wlq实体类数据加载完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", entityNames.length);
        result.put("duration", duration);
        result.put("loadResults", loadResults);

        System.out.println("B和C开头_wlq实体类数据加载完成！成功: " + successCount + " 个，失败: " + failCount + " 个，耗时: " + duration + "ms");

        return ResponseEntity.ok(result);
    }
}
