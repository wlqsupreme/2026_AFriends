package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * A开头实体类综合服务
 * 负责管理所有A开头实体类的数据库操作和内存缓存
 */
@Service
public class AEntityService {

    @Autowired
    private AgreementBaseMapper agreementBaseMapper;

    @Autowired
    private AimodelBaseInfoMapper aimodelBaseInfoMapper;

    @Autowired
    private AimodelCoinLogMapper aimodelCoinLogMapper;

    @Autowired
    private AimodelLevelRuleMapper aimodelLevelRuleMapper;

    @Autowired
    private AiTaskLogMapper aiTaskLogMapper;

    @Autowired
    private UserTaskRelationshipMapper userTaskRelationshipMapper;

    // AgreementBase 相关方法
    public void loadAgreementBaseToMemory() {
        try {
            System.out.println("AEntityService: 开始从数据库加载协议基础数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService: 使用原生SQL查询协议基础数据...");
            var allAgreementBase = agreementBaseMapper.selectAllRecords();
            System.out.println("AEntityService: 原生SQL查询到 " + allAgreementBase.size() + " 条记录");

            if (allAgreementBase.isEmpty()) {
                System.out.println("AEntityService: 数据库中没有协议基础数据");
                return;
            }

            AgreementBase_list_njj.loadFromDatabaseDirectly(allAgreementBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService: 协议基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService: 加载协议基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AgreementBase_njj> getAllAgreementBaseFromMemory() {
        return AgreementBase_list_njj.getAllAgreementBase();
    }

    public Map<String, Object> getAgreementBaseStatisticsFromMemory() {
        return AgreementBase_list_njj.getStatistics();
    }

    // AimodelBaseInfo 相关方法
    public void loadAimodelBaseInfoToMemory() {
        try {
            System.out.println("AEntityService: 开始从数据库加载AI模型基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService: 使用原生SQL查询AI模型基础信息数据...");
            var allAimodelBaseInfo = aimodelBaseInfoMapper.selectAllRecords();
            System.out.println("AEntityService: 原生SQL查询到 " + allAimodelBaseInfo.size() + " 条记录");

            if (allAimodelBaseInfo.isEmpty()) {
                System.out.println("AEntityService: 数据库中没有AI模型基础信息数据");
                return;
            }

            AimodelBaseInfo_list_njj.loadFromDatabaseDirectly(allAimodelBaseInfo);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService: AI模型基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AimodelBaseInfo_njj> getAllAimodelBaseInfoFromMemory() {
        return AimodelBaseInfo_list_njj.getAllAimodelBaseInfo();
    }

    public Map<String, Object> getAimodelBaseInfoStatisticsFromMemory() {
        return AimodelBaseInfo_list_njj.getStatistics();
    }

    public AimodelBaseInfo_njj getAimodelBaseInfoByIdFromMemory(Long modelId) {
        // 首先尝试从内存缓存获取
        AimodelBaseInfo_njj aimodel = AimodelBaseInfo_list_njj.getAimodelBaseInfoById(modelId);
        if (aimodel != null) {
            return aimodel;
        }

        // 如果内存中没有，直接从数据库查询（只查询需要的字段）
        try {
            return aimodelBaseInfoMapper.selectById(modelId);
        } catch (Exception e) {
            System.err.println("从数据库查询AI模型失败: " + e.getMessage());
            return null;
        }
    }

    // AimodelCoinLog 相关方法
    public void loadAimodelCoinLogToMemory() {
        try {
            System.out.println("AEntityService: 开始从数据库加载AI模型币日志数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService: 使用原生SQL查询AI模型币日志数据...");
            var allAimodelCoinLog = aimodelCoinLogMapper.selectAllRecords();
            System.out.println("AEntityService: 原生SQL查询到 " + allAimodelCoinLog.size() + " 条记录");

            if (allAimodelCoinLog.isEmpty()) {
                System.out.println("AEntityService: 数据库中没有AI模型币日志数据");
                return;
            }

            AimodelCoinLog_list_njj.loadFromDatabaseDirectly(allAimodelCoinLog);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService: AI模型币日志数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型币日志数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AimodelCoinLog_njj> getAllAimodelCoinLogFromMemory() {
        return AimodelCoinLog_list_njj.getAllAimodelCoinLog();
    }

    public Map<String, Object> getAimodelCoinLogStatisticsFromMemory() {
        return AimodelCoinLog_list_njj.getStatistics();
    }

    // AimodelLevelRule 相关方法
    public void loadAimodelLevelRuleToMemory() {
        try {
            System.out.println("AEntityService: 开始从数据库加载AI模型等级规则数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService: 使用原生SQL查询AI模型等级规则数据...");
            var allAimodelLevelRule = aimodelLevelRuleMapper.selectAllRecords();
            System.out.println("AEntityService: 原生SQL查询到 " + allAimodelLevelRule.size() + " 条记录");

            if (allAimodelLevelRule.isEmpty()) {
                System.out.println("AEntityService: 数据库中没有AI模型等级规则数据");
                return;
            }

            AimodelLevelRule_list_njj.loadFromDatabaseDirectly(allAimodelLevelRule);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService: AI模型等级规则数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型等级规则数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AimodelLevelRule_njj> getAllAimodelLevelRuleFromMemory() {
        return AimodelLevelRule_list_njj.getAllLevelRules();
    }

    public Map<String, Object> getAimodelLevelRuleStatisticsFromMemory() {
        return AimodelLevelRule_list_njj.getStatistics();
    }

    // AiTaskLog 相关方法
    public void loadAiTaskLogToMemory() {
        try {
            System.out.println("AEntityService: 开始从数据库加载AI任务日志数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService: 使用原生SQL查询AI任务日志数据...");
            var allAiTaskLog = aiTaskLogMapper.selectAllRecords();
            System.out.println("AEntityService: 原生SQL查询到 " + allAiTaskLog.size() + " 条记录");

            if (allAiTaskLog.isEmpty()) {
                System.out.println("AEntityService: 数据库中没有AI任务日志数据");
                return;
            }

            AiTaskLog_list_njj.loadFromDatabaseDirectly(allAiTaskLog);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService: AI任务日志数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI任务日志数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiTaskLog_njj> getAllAiTaskLogFromMemory() {
        return AiTaskLog_list_njj.getAllAiTaskLog();
    }

    public Map<String, Object> getAiTaskLogStatisticsFromMemory() {
        return AiTaskLog_list_njj.getStatistics();
    }

    public AiTaskLog_njj getAiTaskLogByIdFromMemory(Long taskId) {
        return AiTaskLog_list_njj.getAiTaskLogById(taskId);
    }

    // 加载所有A开头实体类数据
    public void loadAllAEntityDataToMemory() {
        System.out.println("AEntityService: 开始加载所有A开头实体类数据...");

        try {
            loadAgreementBaseToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService: 加载协议基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAimodelBaseInfoToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAimodelCoinLogToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型币日志失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAimodelLevelRuleToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI模型等级规则失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiTaskLogToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService: 加载AI任务日志失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("AEntityService: 所有A开头实体类数据加载完成！");
    }

    // 开始任务
    public Map<String, Object> startTask(Long taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("AEntityService: 开始任务，任务ID: " + taskId);

            // 从内存中获取任务
            AiTaskLog_njj taskLog = AiTaskLog_list_njj.getAiTaskLogById(taskId);
            if (taskLog == null) {
                result.put("success", false);
                result.put("message", "任务不存在");
                return result;
            }

            // 检查任务状态
            if (taskLog.getStatus() != 0) {
                result.put("success", false);
                result.put("message", "任务状态不允许开始，当前状态: " + taskLog.getStatus());
                return result;
            }

            // 更新任务状态为进行中 (status = 1)
            taskLog.setStatus((byte) 1);
            taskLog.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 更新数据库
            int updateResult = aiTaskLogMapper.updateById(taskLog);
            if (updateResult > 0) {
                // 更新内存缓存
                AiTaskLog_list_njj.updateAiTaskLogInCache(taskLog);

                // 同时更新 v3_user_task_relationship 表
                try {
                    // 查找对应的用户任务关系记录
                    UserTaskRelationship_njj userTask = UserTaskRelationship_list_njj
                            .getUserTaskRelationshipById(taskId);
                    if (userTask != null) {
                        // 更新状态为进行中 (status = 1)
                        userTask.setStatus((byte) 1);
                        // 设置初始进度为10%
                        userTask.setTaskPercent((byte) 10);
                        userTask.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                        // 更新数据库
                        int userTaskUpdateResult = userTaskRelationshipMapper.updateById(userTask);
                        if (userTaskUpdateResult > 0) {
                            // 更新内存缓存
                            UserTaskRelationship_list_njj.updateUserTaskRelationshipInCache(userTask);
                            System.out.println("AEntityService: 用户任务关系更新成功，任务ID: " + taskId);
                        } else {
                            System.out.println("AEntityService: 用户任务关系更新失败，任务ID: " + taskId);
                        }
                    } else {
                        System.out.println("AEntityService: 未找到对应的用户任务关系记录，任务ID: " + taskId);
                    }
                } catch (Exception e) {
                    System.err.println("AEntityService: 更新用户任务关系时发生错误: " + e.getMessage());
                    e.printStackTrace();
                }

                result.put("success", true);
                result.put("message", "任务已开始");
                result.put("taskId", taskId);
                result.put("status", 1);

                System.out.println("AEntityService: 任务开始成功，任务ID: " + taskId);
            } else {
                result.put("success", false);
                result.put("message", "更新任务状态失败");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "开始任务时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
