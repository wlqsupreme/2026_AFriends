package Afriends_v3.controller;

import Afriends_v3.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钱包相关控制器
 * 提供充值、购买记录、钱包余额等API接口
 */
@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;
    
    @Autowired
    private Afriends_v3.mapper.UserBaseMapper userBaseMapper;
    
    // 支持的支付方式列表
    private static final String[] VALID_PAYMENT_METHODS = {"wechat", "alipay", "qq", "unionpay"};
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 钱包测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "钱包后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户充值记录
     * @param userId 用户ID，默认为1000100
     * @return 充值记录列表
     */
    @GetMapping("/recharge-records")
    public ResponseEntity<Map<String, Object>> getRechargeRecords(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== RechargeController: 收到获取充值记录请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> rechargeRecords = rechargeService.getRechargeRecords(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取充值记录成功");
            response.put("data", rechargeRecords);
            response.put("count", rechargeRecords.size());
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("RechargeController: 返回充值记录成功，共 " + rechargeRecords.size() + " 条数据，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("RechargeController: 获取充值记录失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取充值记录失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户购买记录
     * @param userId 用户ID，默认为1000100
     * @return 购买记录列表
     */
    @GetMapping("/purchase-records")
    public ResponseEntity<Map<String, Object>> getPurchaseRecords(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== RechargeController: 收到获取购买记录请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> purchaseRecords = rechargeService.getPurchaseRecords(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取购买记录成功");
            response.put("data", purchaseRecords);
            response.put("count", purchaseRecords.size());
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("RechargeController: 返回购买记录成功，共 " + purchaseRecords.size() + " 条数据，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("RechargeController: 获取购买记录失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取购买记录失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 获取用户钱包余额
     * @param userId 用户ID，默认为1000100
     * @return 钱包余额信息
     */
    @GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getWalletBalance(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("=== RechargeController: 收到获取钱包余额请求 ===");
        System.out.println("请求用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> balanceInfo = rechargeService.getWalletBalance(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取钱包余额成功");
            response.put("data", balanceInfo);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("RechargeController: 返回钱包余额成功，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            // 用户不存在等参数错误
            System.err.println("RechargeController: " + e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(404).body(errorResponse);
        } catch (Exception e) {
            System.err.println("RechargeController: 获取钱包余额失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取钱包余额失败: " + e.getMessage());
            errorResponse.put("data", new HashMap<>());
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 处理充值操作
     * @param requestBody 请求体，包含userId, amount, paymentMethod等
     * @return 充值结果
     */
    @PostMapping("/recharge")
    public ResponseEntity<Map<String, Object>> handleRecharge(@RequestBody Map<String, Object> requestBody) {
        // 参数验证
        if (requestBody == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "请求体不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        if (requestBody.get("userId") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "userId 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        if (requestBody.get("amount") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "amount 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        if (requestBody.get("paymentMethod") == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "paymentMethod 不能为空");
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Double amount = Double.valueOf(requestBody.get("amount").toString());
        String paymentMethod = requestBody.get("paymentMethod").toString();
        
        // 验证充值金额必须大于0
        if (amount <= 0) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "充值金额必须大于0");
            errorResponse.put("amount", amount);
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        // 验证用户是否存在
        try {
            Afriends_v3.entity.UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "验证用户失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(errorResponse);
        }
        
        // 验证支付方式是否有效
        boolean isValidPaymentMethod = false;
        for (String validMethod : VALID_PAYMENT_METHODS) {
            if (validMethod.equalsIgnoreCase(paymentMethod)) {
                isValidPaymentMethod = true;
                break;
            }
        }
        if (!isValidPaymentMethod) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "无效的支付方式，支持的支付方式: " + String.join(", ", VALID_PAYMENT_METHODS));
            errorResponse.put("paymentMethod", paymentMethod);
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        System.out.println("RechargeController: 收到充值请求 - 用户ID: " + userId + ", 金额: " + amount + ", 支付方式: " + paymentMethod);
        
        try {
            Map<String, Object> result = rechargeService.handleRecharge(userId, amount, paymentMethod);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("RechargeController: 处理充值请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "充值操作失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 刷新钱包数据
     * @param userId 用户ID
     * @return 刷新结果
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshWalletData(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("RechargeController: 收到刷新钱包数据请求，用户ID: " + userId);
        
        try {
            // 重新获取数据
            Map<String, Object> balanceInfo = rechargeService.getWalletBalance(userId);
            List<Map<String, Object>> rechargeRecords = rechargeService.getRechargeRecords(userId);
            List<Map<String, Object>> purchaseRecords = rechargeService.getPurchaseRecords(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "刷新钱包数据成功");
            response.put("balance", balanceInfo);
            response.put("rechargeRecords", rechargeRecords);
            response.put("purchaseRecords", purchaseRecords);
            response.put("userId", userId);
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("RechargeController: 刷新钱包数据成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("RechargeController: 刷新钱包数据失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "刷新钱包数据失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
