package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 钱包相关服务类
 * 负责处理充值记录、购买记录、钱包余额等业务逻辑
 */
@Service
public class RechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;
    
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private UserEntityService userEntityService;
    
    @Autowired
    private UserBaseMapper userBaseMapper;

    /**
     * 获取用户充值记录
     * @param userId 用户ID
     * @return 充值记录列表
     */
    public List<Map<String, Object>> getRechargeRecords(Long userId) {
        System.out.println("RechargeService: 开始获取用户 " + userId + " 的充值记录...");
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> rechargeRecords = new ArrayList<>();
        
        try {
            // 检查Mapper是否注入成功
            System.out.println("=== RechargeService: 检查Mapper注入状态 ===");
            System.out.println("rechargeMapper: " + (rechargeMapper != null ? "已注入" : "未注入"));
            
            // 测试数据库连接
            System.out.println("=== 测试数据库连接 ===");
            try {
                long totalRechargeRecs = rechargeMapper.selectCount(null);
                System.out.println("数据库连接正常，充值记录表总记录数: " + totalRechargeRecs);
            } catch (Exception e) {
                System.err.println("数据库连接测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 查询用户充值记录
            System.out.println("RechargeService: 查询用户充值记录...");
            System.out.println("RechargeService: 执行SQL: SELECT * FROM v2_recharge WHERE user_id = " + userId + " ORDER BY created_at DESC");
            List<Recharge_wlq> userRechargeRecords = rechargeMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Recharge_wlq>()
                    .eq("user_id", userId)
                    .orderByDesc("created_at")
            );
            System.out.println("RechargeService: 查询到 " + userRechargeRecords.size() + " 条充值记录");
            
            // 处理充值记录数据
            for (Recharge_wlq record : userRechargeRecords) {
                try {
                    System.out.println("RechargeService: 处理充值记录 - ID: " + record.getRechargeId() + 
                        ", 用户ID: " + record.getUserId() + ", 金额: " + record.getAmount() + 
                        ", 积分: " + record.getPoints() + ", 时间: " + record.getCreatedAt());
                    
                    Map<String, Object> rechargeRecord = createRechargeRecord(record);
                    rechargeRecords.add(rechargeRecord);
                    System.out.println("RechargeService: 成功处理充值记录 - " + record.getRechargeId());
                } catch (Exception e) {
                    System.err.println("RechargeService: 处理充值记录数据失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("RechargeService: 用户充值记录获取完成！总共 " + rechargeRecords.size() + " 条数据，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("RechargeService: 获取充值记录失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rechargeRecords;
    }

    /**
     * 获取用户购买记录
     * @param userId 用户ID
     * @return 购买记录列表
     */
    public List<Map<String, Object>> getPurchaseRecords(Long userId) {
        System.out.println("RechargeService: 开始获取用户 " + userId + " 的购买记录...");
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> purchaseRecords = new ArrayList<>();
        
        try {
            // 检查Mapper是否注入成功
            System.out.println("=== RechargeService: 检查Mapper注入状态 ===");
            System.out.println("purchaseRecordMapper: " + (purchaseRecordMapper != null ? "已注入" : "未注入"));
            
            // 测试数据库连接
            System.out.println("=== 测试数据库连接 ===");
            try {
                long totalPurchaseRecs = purchaseRecordMapper.selectCount(null);
                System.out.println("数据库连接正常，购买记录表总记录数: " + totalPurchaseRecs);
            } catch (Exception e) {
                System.err.println("数据库连接测试失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 查询用户购买记录
            System.out.println("RechargeService: 查询用户购买记录...");
            System.out.println("RechargeService: 执行SQL: SELECT * FROM v3_purchase_record WHERE user_id = " + userId + " ORDER BY created_at DESC");
            List<PurchaseRecord_njj> userPurchaseRecords = purchaseRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<PurchaseRecord_njj>()
                    .eq("user_id", userId)
                    .orderByDesc("created_at")
            );
            System.out.println("RechargeService: 查询到 " + userPurchaseRecords.size() + " 条购买记录");
            
            // 处理购买记录数据
            for (PurchaseRecord_njj record : userPurchaseRecords) {
                try {
                    System.out.println("RechargeService: 处理购买记录 - ID: " + record.getRecordId() + 
                        ", 用户ID: " + record.getUserId() + ", 商品名称: " + record.getItemName() + 
                        ", 花费金币: " + record.getCoinsSpent() + ", 时间: " + record.getCreatedAt());
                    
                    Map<String, Object> purchaseRecord = createPurchaseRecord(record);
                    purchaseRecords.add(purchaseRecord);
                    System.out.println("RechargeService: 成功处理购买记录 - " + record.getRecordId());
                } catch (Exception e) {
                    System.err.println("RechargeService: 处理购买记录数据失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("RechargeService: 用户购买记录获取完成！总共 " + purchaseRecords.size() + " 条数据，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("RechargeService: 获取购买记录失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return purchaseRecords;
    }

    /**
     * 获取用户钱包余额
     * @param userId 用户ID
     * @return 钱包余额信息
     */
    public Map<String, Object> getWalletBalance(Long userId) {
        System.out.println("RechargeService: 开始获取用户 " + userId + " 的钱包余额...");
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> balanceInfo = new HashMap<>();
        
        try {
            // 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                throw new IllegalArgumentException("用户不存在");
            }
            
            // 计算总充值金额
            List<Recharge_wlq> rechargeRecords = rechargeMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Recharge_wlq>()
                    .eq("user_id", userId)
            );
            
            BigDecimal totalRechargeAmount = BigDecimal.ZERO;
            int totalPoints = 0;
            
            for (Recharge_wlq record : rechargeRecords) {
                if (record.getAmount() != null) {
                    totalRechargeAmount = totalRechargeAmount.add(record.getAmount());
                }
                if (record.getPoints() != null) {
                    totalPoints += record.getPoints();
                }
            }
            
            // 计算总消费金币
            List<PurchaseRecord_njj> purchaseRecords = purchaseRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<PurchaseRecord_njj>()
                    .eq("user_id", userId)
            );
            
            int totalCoinsSpent = 0;
            
            for (PurchaseRecord_njj record : purchaseRecords) {
                if (record.getCoinsSpent() != null) {
                    totalCoinsSpent += record.getCoinsSpent();
                }
            }
            
            // 计算当前余额（假设初始有10000000C，减去消费，加上充值获得的积分）
            int initialBalance = 10000000; // 初始余额
            int currentBalance = initialBalance - totalCoinsSpent + totalPoints;
            
            balanceInfo.put("totalRechargeAmount", totalRechargeAmount);
            balanceInfo.put("totalPoints", totalPoints);
            balanceInfo.put("totalCoinsSpent", totalCoinsSpent);
            balanceInfo.put("currentBalance", currentBalance);
            balanceInfo.put("currentBalanceFormatted", formatBalance(currentBalance));
            balanceInfo.put("rechargeCount", rechargeRecords.size());
            balanceInfo.put("purchaseCount", purchaseRecords.size());
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("RechargeService: 钱包余额计算完成！当前余额: " + currentBalance + "C，耗时: " + duration + "ms");
            
        } catch (IllegalArgumentException e) {
            // 用户不存在等参数错误，直接抛出，不返回默认值
            throw e;
        } catch (Exception e) {
            System.err.println("RechargeService: 获取钱包余额失败: " + e.getMessage());
            e.printStackTrace();
            
            // 返回默认值（仅用于其他未知异常）
            balanceInfo.put("totalRechargeAmount", BigDecimal.ZERO);
            balanceInfo.put("totalPoints", 0);
            balanceInfo.put("totalCoinsSpent", 0);
            balanceInfo.put("currentBalance", 10000000);
            balanceInfo.put("currentBalanceFormatted", "10000000C");
            balanceInfo.put("rechargeCount", 0);
            balanceInfo.put("purchaseCount", 0);
        }
        
        return balanceInfo;
    }

    /**
     * 处理充值操作
     * @param userId 用户ID
     * @param amount 充值金额
     * @param paymentMethod 支付方式
     * @return 充值结果
     */
    public Map<String, Object> handleRecharge(Long userId, Double amount, String paymentMethod) {
        System.out.println("RechargeService: 处理充值操作 - 用户ID: " + userId + ", 金额: " + amount + ", 支付方式: " + paymentMethod);
        
        try {
            boolean minorModeEnabled = userEntityService.getUserSettingBoolean(userId, "mode.minorModeEnabled", false);
            if (minorModeEnabled) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("code", "MINOR_MODE_BLOCKED");
                result.put("action", "WALLET_RECHARGE");
                result.put("message", "未成年人模式下暂不支持充值");
                result.put("timestamp", System.currentTimeMillis());
                return result;
            }

            // 计算获得的积分（1元=100积分）
            int points = (int) (amount * 100);
            
            // 获取当前余额
            Map<String, Object> currentBalance = getWalletBalance(userId);
            int currentCoins = (Integer) currentBalance.get("currentBalance");
            int newBalance = currentCoins + points;
            
            // 创建充值记录
            Recharge_wlq rechargeRecord = new Recharge_wlq();
            rechargeRecord.setRechargeId(generateRechargeId());
            rechargeRecord.setUserId(userId);
            rechargeRecord.setAmount(BigDecimal.valueOf(amount));
            rechargeRecord.setPoints(points);
            rechargeRecord.setMoneyBalance(BigDecimal.valueOf(amount));
            rechargeRecord.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            
            // 保存到数据库
            rechargeMapper.insert(rechargeRecord);
            
            System.out.println("RechargeService: 充值记录创建成功，ID: " + rechargeRecord.getRechargeId());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "充值成功");
            result.put("rechargeId", rechargeRecord.getRechargeId());
            result.put("amount", amount);
            result.put("points", points);
            result.put("newBalance", newBalance);
            result.put("newBalanceFormatted", formatBalance(newBalance));
            result.put("paymentMethod", paymentMethod);
            result.put("timestamp", System.currentTimeMillis());
            
            return result;
            
        } catch (Exception e) {
            System.err.println("RechargeService: 处理充值操作失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "充值操作失败: " + e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            return result;
        }
    }

    /**
     * 创建充值记录数据
     */
    private Map<String, Object> createRechargeRecord(Recharge_wlq record) {
        Map<String, Object> rechargeRecord = new HashMap<>();
        rechargeRecord.put("id", record.getRechargeId());
        rechargeRecord.put("userId", record.getUserId());
        rechargeRecord.put("amount", record.getAmount());
        rechargeRecord.put("points", record.getPoints());
        rechargeRecord.put("moneyBalance", record.getMoneyBalance());
        rechargeRecord.put("createdAt", record.getCreatedAt());
        rechargeRecord.put("timeAgo", formatTimeAgo(record.getCreatedAt()));
        rechargeRecord.put("amountFormatted", "+" + record.getPoints() + "C");
        rechargeRecord.put("orderNumber", "RECHARGE" + record.getRechargeId());
        
        return rechargeRecord;
    }

    /**
     * 创建购买记录数据
     */
    private Map<String, Object> createPurchaseRecord(PurchaseRecord_njj record) {
        Map<String, Object> purchaseRecord = new HashMap<>();
        purchaseRecord.put("id", record.getRecordId());
        purchaseRecord.put("userId", record.getUserId());
        purchaseRecord.put("itemType", record.getItemType());
        purchaseRecord.put("itemId", record.getItemId());
        purchaseRecord.put("itemName", record.getItemName());
        purchaseRecord.put("coinsSpent", record.getCoinsSpent());
        purchaseRecord.put("coinsBalance", record.getCoinsBalance());
        purchaseRecord.put("purchasePath", record.getPurchasePath());
        purchaseRecord.put("createdAt", record.getCreatedAt());
        purchaseRecord.put("timeAgo", formatTimeAgo(record.getCreatedAt()));
        purchaseRecord.put("amountFormatted", "-" + record.getCoinsSpent() + "C");
        purchaseRecord.put("balanceFormatted", "余额: " + record.getCoinsBalance() + "C");
        
        return purchaseRecord;
    }

    /**
     * 格式化余额显示
     */
    private String formatBalance(int balance) {
        if (balance >= 10000) {
            return (balance / 10000) + "w" + (balance % 10000 > 0 ? (balance % 10000 / 1000) + "k" : "") + "C";
        } else if (balance >= 1000) {
            return (balance / 1000) + "k" + (balance % 1000 > 0 ? (balance % 1000) + "" : "") + "C";
        } else {
            return balance + "C";
        }
    }

    /**
     * 格式化时间显示
     */
    private String formatTimeAgo(java.sql.Timestamp date) {
        if (date == null) return "刚刚";
        
        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;
        
        long minutes = diff / (1000 * 60);
        long hours = diff / (1000 * 60 * 60);
        long days = diff / (1000 * 60 * 60 * 24);
        
        if (minutes < 1) return "刚刚";
        if (minutes < 60) return minutes + "分钟前";
        if (hours < 24) return hours + "小时前";
        if (days < 7) return days + "天前";
        return "一周前";
    }

    /**
     * 生成充值记录ID
     */
    private Long generateRechargeId() {
        try {
            // 查询最大ID
            List<Recharge_wlq> allRecords = rechargeMapper.selectList(null);
            Long maxId = 1000000L; // 起始值
            
            for (Recharge_wlq record : allRecords) {
                if (record.getRechargeId() != null && record.getRechargeId() > maxId) {
                    maxId = record.getRechargeId();
                }
            }
            
            return maxId + 1;
        } catch (Exception e) {
            System.err.println("RechargeService: 生成充值ID失败，使用默认值: " + e.getMessage());
            return System.currentTimeMillis(); // 如果出错，使用时间戳
        }
    }
}
