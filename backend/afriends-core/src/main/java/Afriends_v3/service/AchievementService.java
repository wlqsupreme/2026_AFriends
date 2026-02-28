package Afriends_v3.service;

import Afriends_v3.entity.AchievementBase_njj;
import Afriends_v3.entity.AchievementRecord_njj;
import Afriends_v3.entity.AchievementBase_list_njj;
import Afriends_v3.entity.AchievementRecord_list_njj;
import Afriends_v3.mapper.AchievementBaseMapper;
import Afriends_v3.mapper.AchievementRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 成就服务类
 * 负责处理成就相关的业务逻辑
 */
@Service
public class AchievementService {

    @Autowired
    private AchievementBaseMapper achievementBaseMapper;
    
    @Autowired
    private AchievementRecordMapper achievementRecordMapper;

    /**
     * 获取用户的成就数据
     * @param userId 用户ID
     * @return 成就数据列表
     */
    public List<Map<String, Object>> getUserAchievements(Long userId) {
        System.out.println("AchievementService: 开始获取用户 " + userId + " 的成就数据...");
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> achievements = new ArrayList<>();
        
        try {
            // 1. 获取所有可见的成就基础数据
            System.out.println("AchievementService: 查询所有可见的成就基础数据...");
            
            // 先查询所有数据，看看是否有数据
            List<AchievementBase_njj> allData = achievementBaseMapper.selectList(null);
            System.out.println("AchievementService: 数据库中总共有 " + allData.size() + " 条成就基础数据");
            
            // 然后查询可见的数据
            List<AchievementBase_njj> allAchievements = achievementBaseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementBase_njj>()
                    .eq("is_view", 1)
                    .orderByAsc("achievement_id")
            );
            System.out.println("AchievementService: 查询到 " + allAchievements.size() + " 条可见的成就基础数据");
            
            // 如果可见数据为空，尝试查询所有数据
            if (allAchievements.isEmpty() && !allData.isEmpty()) {
                System.out.println("AchievementService: 可见数据为空，使用所有数据");
                allAchievements = allData;
            }
            
            // 2. 获取用户的成就记录
            System.out.println("AchievementService: 查询用户 " + userId + " 的成就记录...");
            List<AchievementRecord_njj> userRecords = achievementRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementRecord_njj>()
                    .eq("user_id", userId)
            );
            System.out.println("AchievementService: 查询到 " + userRecords.size() + " 条用户成就记录");
            
            // 3. 创建用户记录映射，便于快速查找
            Map<Long, AchievementRecord_njj> userRecordMap = new HashMap<>();
            for (AchievementRecord_njj record : userRecords) {
                userRecordMap.put(record.getAchievementId(), record);
            }
            
            // 4. 整合数据
            for (AchievementBase_njj achievement : allAchievements) {
                Map<String, Object> achievementData = new HashMap<>();
                
                // 基础信息
                achievementData.put("id", achievement.getAchievementId());
                achievementData.put("name", achievement.getName());
                achievementData.put("description", achievement.getDescription());
                achievementData.put("iconUrl", achievement.getIconUrl());
                achievementData.put("condition", achievement.getCondition());
                
                // 用户记录信息
                AchievementRecord_njj userRecord = userRecordMap.get(achievement.getAchievementId());
                if (userRecord != null) {
                    // 用户已有记录
                    boolean isCompleted = userRecord.getIsCompleted() == 1;
                    achievementData.put("isCompleted", isCompleted);
                    achievementData.put("progress", userRecord.getProgress());
                    achievementData.put("completedAt", userRecord.getCompletedAt());
                    achievementData.put("createdAt", userRecord.getCreatedAt());
                    
                    // 设置成就状态：未完成(0)、完成中(1)、已完成(2)
                    if (isCompleted) {
                        achievementData.put("status", 2); // 已完成
                        achievementData.put("statusText", "已完成");
                    } else {
                        // 检查是否有进度数据来判断是否完成中
                        String progress = userRecord.getProgress();
                        if (progress != null && !progress.equals("{}") && !progress.isEmpty()) {
                            achievementData.put("status", 1); // 完成中
                            achievementData.put("statusText", "完成中");
                        } else {
                            achievementData.put("status", 0); // 未完成
                            achievementData.put("statusText", "未完成");
                        }
                    }
                } else {
                    // 用户无记录，创建默认记录
                    achievementData.put("isCompleted", false);
                    achievementData.put("progress", "{}");
                    achievementData.put("completedAt", null);
                    achievementData.put("createdAt", null);
                    achievementData.put("status", 0); // 未完成
                    achievementData.put("statusText", "未完成");
                }
                
                achievements.add(achievementData);
                System.out.println("AchievementService: 处理成就 - " + achievement.getName() + 
                    ", 完成状态: " + achievementData.get("isCompleted"));
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AchievementService: 用户成就数据获取完成！总共 " + achievements.size() + " 条数据，耗时: " + duration + "ms");
            
        } catch (Exception e) {
            System.err.println("AchievementService: 获取用户成就数据失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return achievements;
    }
    
    /**
     * 获取用户成就统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    public Map<String, Object> getUserAchievementStats(Long userId) {
        System.out.println("AchievementService: 开始获取用户 " + userId + " 的成就统计信息...");
        
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // 1. 获取所有可见的成就总数
            Long totalAchievements = achievementBaseMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementBase_njj>()
                    .eq("is_view", 1)
            );
            
            // 2. 获取用户已完成的成就数
            Long completedAchievements = achievementRecordMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementRecord_njj>()
                    .eq("user_id", userId)
                    .eq("is_completed", 1)
            );
            
            // 3. 计算完成度
            double completionRate = totalAchievements > 0 ? 
                (double) completedAchievements / totalAchievements * 100 : 0.0;
            
            stats.put("totalAchievements", totalAchievements);
            stats.put("completedAchievements", completedAchievements);
            stats.put("completionRate", Math.round(completionRate * 10.0) / 10.0); // 保留一位小数
            
            System.out.println("AchievementService: 成就统计 - 总数: " + totalAchievements + 
                ", 已完成: " + completedAchievements + ", 完成度: " + stats.get("completionRate") + "%");
            
        } catch (Exception e) {
            System.err.println("AchievementService: 获取用户成就统计信息失败: " + e.getMessage());
            e.printStackTrace();
            
            // 返回默认值
            stats.put("totalAchievements", 0);
            stats.put("completedAchievements", 0);
            stats.put("completionRate", 0.0);
        }
        
        return stats;
    }
    
    /**
     * 更新用户成就进度
     * @param userId 用户ID
     * @param achievementId 成就ID
     * @param progress 进度数据（JSON字符串）
     * @param isCompleted 是否完成
     * @return 操作结果
     */
    public Map<String, Object> updateAchievementProgress(Long userId, Long achievementId, String progress, Boolean isCompleted) {
        System.out.println("AchievementService: 更新用户 " + userId + " 的成就 " + achievementId + " 进度...");
        
        try {
            // 查询是否已有记录
            AchievementRecord_njj existingRecord = achievementRecordMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementRecord_njj>()
                    .eq("user_id", userId)
                    .eq("achievement_id", achievementId)
            );
            
            if (existingRecord != null) {
                // 更新现有记录
                existingRecord.setProgress(progress);
                existingRecord.setIsCompleted(isCompleted ? (byte) 1 : (byte) 0);
                if (isCompleted && existingRecord.getCompletedAt() == null) {
                    existingRecord.setCompletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                existingRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                achievementRecordMapper.updateById(existingRecord);
                System.out.println("AchievementService: 更新成就记录成功");
                
                return createResponse(true, "更新成就进度成功", existingRecord);
            } else {
                // 创建新记录
                AchievementRecord_njj newRecord = new AchievementRecord_njj();
                newRecord.setRecordId(generateNewRecordId());
                newRecord.setUserId(userId);
                newRecord.setAchievementId(achievementId);
                newRecord.setProgress(progress);
                newRecord.setIsCompleted(isCompleted ? (byte) 1 : (byte) 0);
                if (isCompleted) {
                    newRecord.setCompletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                }
                newRecord.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newRecord.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                
                achievementRecordMapper.insert(newRecord);
                System.out.println("AchievementService: 创建新成就记录成功，ID: " + newRecord.getRecordId());
                
                return createResponse(true, "创建成就记录成功", newRecord);
            }
            
        } catch (Exception e) {
            System.err.println("AchievementService: 更新成就进度失败: " + e.getMessage());
            e.printStackTrace();
            return createResponse(false, "更新成就进度失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 创建统一响应格式
     */
    private Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    /**
     * 生成新的记录ID
     */
    private Long generateNewRecordId() {
        try {
            // 查询最大ID
            List<AchievementRecord_njj> allRecords = achievementRecordMapper.selectList(null);
            Long maxId = 10000000L; // 起始值
            
            for (AchievementRecord_njj record : allRecords) {
                if (record.getRecordId() != null && record.getRecordId() > maxId) {
                    maxId = record.getRecordId();
                }
            }
            
            return maxId + 1;
        } catch (Exception e) {
            System.err.println("AchievementService: 生成记录ID失败，使用默认值: " + e.getMessage());
            return System.currentTimeMillis(); // 使用时间戳作为备用ID
        }
    }
    
    /**
     * 加载成就基础数据到内存
     */
    public void loadAchievementBaseToMemory() {
        try {
            System.out.println("AchievementService: 开始从数据库加载成就基础数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AchievementService: 使用原生SQL查询成就基础数据...");
            var allAchievementBase = achievementBaseMapper.selectAllRecords();
            System.out.println("AchievementService: 原生SQL查询到 " + allAchievementBase.size() + " 条记录");

            if (allAchievementBase.isEmpty()) {
                System.out.println("AchievementService: 数据库中没有成就基础数据");
                return;
            }

            AchievementBase_list_njj.loadFromDatabaseDirectly(allAchievementBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AchievementService: 成就基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AchievementService: 加载成就基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 加载成就记录数据到内存
     */
    public void loadAchievementRecordToMemory() {
        try {
            System.out.println("AchievementService: 开始从数据库加载成就记录数据...");
            long startTime = System.currentTimeMillis();

            // 使用loadFromDatabase方法
            System.out.println("AchievementService: 使用loadFromDatabase方法加载成就记录数据...");
            AchievementRecord_list_njj.loadFromDatabase(achievementRecordMapper);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AchievementService: 成就记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AchievementService: 加载成就记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 调试方法：获取所有成就基础数据
     */
    public List<Map<String, Object>> getAllAchievementBaseData() {
        System.out.println("AchievementService: 调试 - 查询所有成就基础数据...");
        List<AchievementBase_njj> allData = achievementBaseMapper.selectList(null);
        System.out.println("AchievementService: 调试 - 查询到 " + allData.size() + " 条成就基础数据");
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (AchievementBase_njj achievement : allData) {
            Map<String, Object> data = new HashMap<>();
            data.put("achievementId", achievement.getAchievementId());
            data.put("name", achievement.getName());
            data.put("description", achievement.getDescription());
            data.put("condition", achievement.getCondition());
            data.put("isView", achievement.getIsView());
            data.put("createdAt", achievement.getCreatedAt());
            result.add(data);
        }
        return result;
    }
    
    /**
     * 调试方法：获取所有成就记录数据
     */
    public List<Map<String, Object>> getAllAchievementRecordData() {
        System.out.println("AchievementService: 调试 - 查询所有成就记录数据...");
        List<AchievementRecord_njj> allData = achievementRecordMapper.selectList(null);
        System.out.println("AchievementService: 调试 - 查询到 " + allData.size() + " 条成就记录数据");
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (AchievementRecord_njj record : allData) {
            Map<String, Object> data = new HashMap<>();
            data.put("recordId", record.getRecordId());
            data.put("userId", record.getUserId());
            data.put("achievementId", record.getAchievementId());
            data.put("progress", record.getProgress());
            data.put("isCompleted", record.getIsCompleted());
            data.put("completedAt", record.getCompletedAt());
            data.put("createdAt", record.getCreatedAt());
            result.add(data);
        }
        return result;
    }
    
    /**
     * 调试方法：获取指定用户的成就记录数据
     */
    public List<Map<String, Object>> getUserAchievementRecordData(Long userId) {
        System.out.println("AchievementService: 调试 - 查询用户 " + userId + " 的成就记录数据...");
        List<AchievementRecord_njj> userData = achievementRecordMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementRecord_njj>()
                .eq("user_id", userId)
        );
        System.out.println("AchievementService: 调试 - 查询到 " + userData.size() + " 条用户成就记录数据");
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (AchievementRecord_njj record : userData) {
            Map<String, Object> data = new HashMap<>();
            data.put("recordId", record.getRecordId());
            data.put("userId", record.getUserId());
            data.put("achievementId", record.getAchievementId());
            data.put("progress", record.getProgress());
            data.put("isCompleted", record.getIsCompleted());
            data.put("completedAt", record.getCompletedAt());
            data.put("createdAt", record.getCreatedAt());
            result.add(data);
        }
        return result;
    }
    
    /**
     * 数据库连接测试方法
     */
    public Map<String, Object> testDatabaseConnection() {
        System.out.println("AchievementService: 开始数据库连接测试...");
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 测试基本连接
            System.out.println("AchievementService: 测试基本数据库连接...");
            List<AchievementBase_njj> allData = achievementBaseMapper.selectList(null);
            result.put("totalAchievementBaseCount", allData.size());
            System.out.println("AchievementService: 基本查询成功，总数据量: " + allData.size());
            
            // 2. 测试条件查询
            System.out.println("AchievementService: 测试条件查询...");
            List<AchievementBase_njj> visibleData = achievementBaseMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementBase_njj>()
                    .eq("is_view", 1)
            );
            result.put("visibleAchievementBaseCount", visibleData.size());
            System.out.println("AchievementService: 条件查询成功，可见数据量: " + visibleData.size());
            
            // 3. 测试记录表查询
            System.out.println("AchievementService: 测试成就记录表查询...");
            List<AchievementRecord_njj> recordData = achievementRecordMapper.selectList(null);
            result.put("totalAchievementRecordCount", recordData.size());
            System.out.println("AchievementService: 记录表查询成功，总数据量: " + recordData.size());
            
            // 4. 测试用户记录查询
            System.out.println("AchievementService: 测试用户记录查询...");
            List<AchievementRecord_njj> userRecordData = achievementRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AchievementRecord_njj>()
                    .eq("user_id", 1000100)
            );
            result.put("userAchievementRecordCount", userRecordData.size());
            System.out.println("AchievementService: 用户记录查询成功，数据量: " + userRecordData.size());
            
            // 5. 显示前几条数据样本
            if (!allData.isEmpty()) {
                System.out.println("AchievementService: 显示前3条成就基础数据样本:");
                for (int i = 0; i < Math.min(3, allData.size()); i++) {
                    AchievementBase_njj achievement = allData.get(i);
                    System.out.println("  - ID: " + achievement.getAchievementId() + 
                                     ", Name: " + achievement.getName() + 
                                     ", IsView: " + achievement.getIsView());
                }
            }
            
            result.put("connectionTestSuccess", true);
            result.put("message", "数据库连接测试成功");
            
        } catch (Exception e) {
            System.err.println("AchievementService: 数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
            result.put("connectionTestSuccess", false);
            result.put("error", e.getMessage());
            result.put("message", "数据库连接测试失败");
        }
        
        return result;
    }
}