package Afriends_v3.controller;

import Afriends_v3.entity.PurchaseRecord_njj;
import Afriends_v3.service.PurchaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购买记录控制器
 * 提供购买记录数据的API接口
 */
@RestController
@RequestMapping("/api/purchase-record")
@CrossOrigin(origins = "*")
public class PurchaseRecordController {

    @Autowired
    private PurchaseRecordService purchaseRecordService;

    /**
     * 加载购买记录数据到内存
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadPurchaseRecordToMemory() {
        try {
            System.out.println("开始加载购买记录数据到内存...");
            long startTime = System.currentTimeMillis();

            purchaseRecordService.loadPurchaseRecordToMemory();

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

    /**
     * 获取所有购买记录数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<PurchaseRecord_njj>> getAllPurchaseRecord() {
        try {
            List<PurchaseRecord_njj> records = purchaseRecordService.getAllPurchaseRecordFromMemory();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据记录ID获取购买记录数据
     */
    @GetMapping("/{recordId}")
    public ResponseEntity<PurchaseRecord_njj> getPurchaseRecordById(@PathVariable Long recordId) {
        try {
            PurchaseRecord_njj record = purchaseRecordService.getPurchaseRecordByIdFromMemory(recordId);
            if (record != null) {
                return ResponseEntity.ok(record);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据用户ID获取购买记录数据
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseRecord_njj>> getPurchaseRecordByUserId(@PathVariable Long userId) {
        try {
            List<PurchaseRecord_njj> records = purchaseRecordService.getPurchaseRecordByUserIdFromMemory(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据商品类型获取购买记录数据
     */
    @GetMapping("/item-type/{itemType}")
    public ResponseEntity<List<PurchaseRecord_njj>> getPurchaseRecordByItemType(@PathVariable String itemType) {
        try {
            List<PurchaseRecord_njj> records = purchaseRecordService.getPurchaseRecordByItemTypeFromMemory(itemType);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取购买记录统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPurchaseRecordStatistics() {
        try {
            Map<String, Object> stats = purchaseRecordService.getPurchaseRecordStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 强制刷新购买记录数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshPurchaseRecordData() {
        try {
            System.out.println("开始刷新购买记录数据...");
            long startTime = System.currentTimeMillis();

            purchaseRecordService.refreshPurchaseRecordData();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("购买记录数据刷新完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "购买记录数据刷新成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("刷新购买记录数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "购买记录数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 保存购买记录
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> savePurchaseRecord(@RequestBody PurchaseRecord_njj purchaseRecord) {
        try {
            boolean success = purchaseRecordService.savePurchaseRecord(purchaseRecord);

            Map<String, Object> result = Map.of(
                    "success", success,
                    "message", success ? "购买记录保存成功" : "购买记录保存失败",
                    "recordId", purchaseRecord.getRecordId(),
                    "timestamp", System.currentTimeMillis());

            return success ? ResponseEntity.ok(result) : ResponseEntity.status(500).body(result);
        } catch (Exception e) {
            System.err.println("保存购买记录失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "保存购买记录失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 批量保存购买记录
     */
    @PostMapping("/save-batch")
    public ResponseEntity<Map<String, Object>> savePurchaseRecords(
            @RequestBody List<PurchaseRecord_njj> purchaseRecords) {
        try {
            boolean success = purchaseRecordService.savePurchaseRecords(purchaseRecords);

            Map<String, Object> result = Map.of(
                    "success", success,
                    "message", success ? "批量保存购买记录成功" : "批量保存购买记录失败",
                    "count", purchaseRecords.size(),
                    "timestamp", System.currentTimeMillis());

            return success ? ResponseEntity.ok(result) : ResponseEntity.status(500).body(result);
        } catch (Exception e) {
            System.err.println("批量保存购买记录失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "批量保存购买记录失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(result);
        }
    }
}
