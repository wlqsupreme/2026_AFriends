package Afriends_v3.controller;

import Afriends_v3.entity.ProductsBase_njj;
import Afriends_v3.service.ProductsBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品基础信息控制器
 */
@RestController
@RequestMapping("/api/products-base")
@CrossOrigin(origins = "*")
public class ProductsBaseController {

    @Autowired
    private ProductsBaseService productsBaseService;

    /**
     * 加载产品基础信息数据到内存
     */
    @PostMapping("/load-to-memory")
    public ResponseEntity<Map<String, Object>> loadProductsBaseToMemory() {
        try {
            productsBaseService.loadProductsBaseToMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "message", "产品基础信息数据加载到内存成功",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载产品基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "加载产品基础信息数据到内存失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有产品基础信息
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProductsBase() {
        try {
            List<ProductsBase_njj> products = productsBaseService.getAllProductsBaseFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", products,
                    "count", products.size(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取所有产品基础信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取所有产品基础信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取可用的产品基础信息（状态为1的产品）
     */
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableProducts() {
        try {
            List<ProductsBase_njj> products = productsBaseService.getAvailableProductsFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", products,
                    "count", products.size(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取可用产品基础信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取可用产品基础信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据状态获取产品基础信息
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getProductsBaseByStatus(@PathVariable Byte status) {
        try {
            List<ProductsBase_njj> products = productsBaseService.getProductsBaseByStatusFromMemory(status);
            Map<String, Object> result = Map.of(
                    "success", true,
                    "data", products,
                    "count", products.size(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("根据状态获取产品基础信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "根据状态获取产品基础信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据产品ID获取产品基础信息
     */
    @GetMapping("/{coinRechargeId}")
    public ResponseEntity<Map<String, Object>> getProductsBaseById(@PathVariable Long coinRechargeId) {
        try {
            ProductsBase_njj product = productsBaseService.getProductsBaseByIdFromMemory(coinRechargeId);
            if (product != null) {
                Map<String, Object> result = Map.of(
                        "success", true,
                        "data", product,
                        "timestamp", System.currentTimeMillis()
                );
                return ResponseEntity.ok(result);
            } else {
                Map<String, Object> result = Map.of(
                        "success", false,
                        "message", "产品不存在",
                        "timestamp", System.currentTimeMillis()
                );
                return ResponseEntity.status(404).body(result);
            }
        } catch (Exception e) {
            System.err.println("根据产品ID获取产品基础信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "根据产品ID获取产品基础信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = productsBaseService.getProductsBaseStatisticsFromMemory();
            Map<String, Object> result = Map.of(
                    "success", true,
                    "statistics", statistics,
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("获取产品基础信息统计信息失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = Map.of(
                    "success", false,
                    "message", "获取产品基础信息统计信息失败: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(500).body(result);
        }
    }
}
