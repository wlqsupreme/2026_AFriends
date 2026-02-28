package Afriends_v3.service;

import Afriends_v3.entity.UserAiModel_njj;
import Afriends_v3.entity.UserAiModel_list_njj;
import Afriends_v3.mapper.UserAiModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户AI模型服务类
 * 负责管理用户AI模型数据的数据库操作和内存缓存
 */
@Service
public class UserAiModelService {

    @Autowired
    private UserAiModelMapper userAiModelMapper;

    /**
     * 加载用户AI模型数据到内存
     */
    public void loadUserAiModelToMemory() {
        try {
            System.out.println("UserAiModelService: 开始从数据库加载用户AI模型数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserAiModelService: 使用原生SQL查询用户AI模型数据...");
            var allUserAiModel = userAiModelMapper.selectAllRecords();
            System.out.println(
                    "UserAiModelService: 原生SQL查询到 " + (allUserAiModel != null ? allUserAiModel.size() : 0) + " 条记录");

            if (allUserAiModel == null || allUserAiModel.isEmpty()) {
                System.out.println("UserAiModelService: 数据库中没有用户AI模型数据，跳过加载");
                UserAiModel_list_njj.loadFromDatabaseDirectly(new ArrayList<>());
                return;
            }

            UserAiModel_list_njj.loadFromDatabaseDirectly(allUserAiModel);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserAiModelService: 用户AI模型数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserAiModelService: 加载用户AI模型数据到内存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户AI模型数据（从内存）
     */
    public List<UserAiModel_njj> getAllUserAiModelFromMemory() {
        return UserAiModel_list_njj.getAllUserAiModel();
    }

    /**
     * 根据模型ID获取用户AI模型数据
     */
    public UserAiModel_njj getUserAiModelByIdFromMemory(Long userAiId) {
        return UserAiModel_list_njj.getUserAiModelById(userAiId);
    }

    /**
     * 根据用户ID获取用户AI模型数据
     */
    public List<UserAiModel_njj> getUserAiModelByUserIdFromMemory(Long userId) {
        return UserAiModel_list_njj.getUserAiModelByUserId(userId);
    }

    /**
     * 根据父模型ID获取用户AI模型数据
     */
    public List<UserAiModel_njj> getUserAiModelByParentModelIdFromMemory(Long parentModelId) {
        return UserAiModel_list_njj.getUserAiModelByParentModelId(parentModelId);
    }

    /**
     * 获取用户AI模型统计信息（从内存）
     */
    public Map<String, Object> getUserAiModelStatisticsFromMemory() {
        return UserAiModel_list_njj.getStatistics();
    }

    /**
     * 强制刷新用户AI模型数据
     */
    public void refreshUserAiModelData() {
        loadUserAiModelToMemory();
    }

    /**
     * 购买AI模型
     */
    public Long purchaseAiModel(Long userId, Long parentModelId, String modelName, String modelDesc,
            String modelImageUrl, BigDecimal price) {
        try {
            // 生成新的userAiId
            Long userAiId = generateNextUserAiId();

            // 创建用户AI模型记录
            UserAiModel_njj userAiModel = new UserAiModel_njj();
            userAiModel.setUserAiId(userAiId);
            userAiModel.setUserId(userId);
            userAiModel.setParentModelId(parentModelId);
            userAiModel.setParentModelVersion("1.0"); // 默认版本
            userAiModel.setModelName(modelName);
            userAiModel.setCustomDesc(modelDesc);
            userAiModel.setModelImageUrl(modelImageUrl);
            userAiModel.setTone("友好"); // 默认语调
            userAiModel.setQuestionWeight("1.0"); // 默认权重
            userAiModel.setQuestionContent(""); // 默认问题内容
            userAiModel.setPower(1); // 默认能力值
            userAiModel.setLevel(1); // 默认等级
            userAiModel.setTotalExp(0); // 默认经验值
            userAiModel.setRechargeAmount(price); // 充值金额
            userAiModel.setIsVisible((byte) 1); // 可见
            userAiModel.setStatus((byte) 1); // 正常状态
            userAiModel.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            userAiModel.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userAiModel.setDeletedAt(null);
            userAiModel.setTryOutAt(null);
            userAiModel.setTryOutNum(0L);

            // 保存到数据库
            int result = userAiModelMapper.insert(userAiModel);
            if (result > 0) {
                // 添加到内存缓存
                UserAiModel_list_njj.addToCache(userAiModel);
                System.out.println("AI模型购买成功，userAiId: " + userAiId);
                return userAiId;
            } else {
                System.err.println("AI模型购买失败，数据库插入失败");
                return null;
            }
        } catch (Exception e) {
            System.err.println("购买AI模型失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成下一个userAiId
     */
    private Long generateNextUserAiId() {
        try {
            Long maxId = userAiModelMapper.selectMaxUserAiId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成userAiId失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 更新AI模型名称
     */
    public boolean updateAiModelName(Long userAiId, String newName) {
        try {
            System.out.println("UserAiModelService: 开始更新AI模型名称，userAiId: " + userAiId + ", newName: " + newName);

            // 1. 更新数据库
            int updatedRows = userAiModelMapper.updateModelName(userAiId, newName);
            if (updatedRows == 0) {
                System.out.println("UserAiModelService: 数据库中没有找到对应的AI模型，userAiId: " + userAiId);
                return false;
            }

            // 2. 更新内存缓存
            UserAiModel_list_njj.updateModelNameInCache(userAiId, newName);

            System.out.println("UserAiModelService: AI模型名称更新成功，userAiId: " + userAiId + ", newName: " + newName);
            return true;
        } catch (Exception e) {
            System.err.println("UserAiModelService: 更新AI模型名称失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新AI模型等级和经验
     */
    public boolean updateAiModelLevel(Long userAiId, Integer level, Integer totalExp) {
        try {
            System.out.println("UserAiModelService: 开始更新AI模型等级，userAiId: " + userAiId + ", level: " + level
                    + ", totalExp: " + totalExp);

            // 1. 更新数据库
            int updatedRows = userAiModelMapper.updateLevelAndExp(userAiId, level, totalExp);
            if (updatedRows == 0) {
                System.out.println("UserAiModelService: 数据库中没有找到对应的AI模型，userAiId: " + userAiId);
                return false;
            }

            // 2. 更新内存缓存
            UserAiModel_list_njj.updateLevelAndExpInCache(userAiId, level, totalExp);

            System.out.println("UserAiModelService: AI模型等级更新成功，userAiId: " + userAiId + ", level: " + level
                    + ", totalExp: " + totalExp);
            return true;
        } catch (Exception e) {
            System.err.println("UserAiModelService: 更新AI模型等级失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 试用AI模型
     */
    public Map<String, Object> tryOutAiModel(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long parentModelId = Long.valueOf(requestData.get("parentModelId").toString());

            System.out.println("UserAiModelService: 开始试用AI模型，userId: " + userId + ", parentModelId: " + parentModelId);

            // 1. 检查是否已经购买过
            List<UserAiModel_njj> existingModels = getUserAiModelByUserIdFromMemory(userId);
            boolean alreadyPurchased = existingModels.stream()
                    .anyMatch(model -> model.getParentModelId().equals(parentModelId) && model.getStatus() == 1);

            if (alreadyPurchased) {
                result.put("success", false);
                result.put("message", "您已经购买过此AI模型，无需试用");
                return result;
            }

            // 2. 检查是否已经试用过
            boolean alreadyTried = existingModels.stream()
                    .anyMatch(model -> model.getParentModelId().equals(parentModelId) && model.getTryOutAt() != null);

            if (alreadyTried) {
                result.put("success", false);
                result.put("message", "您已经试用过此AI模型");
                return result;
            }

            // 3. 获取AI模型基础信息，获取试用次数
            Long tryOutNum = getTryOutNumFromBaseInfo(parentModelId);
            if (tryOutNum == null || tryOutNum <= 0) {
                result.put("success", false);
                result.put("message", "此AI模型不支持试用");
                return result;
            }

            // 4. 创建试用记录
            UserAiModel_njj tryOutModel = new UserAiModel_njj();
            tryOutModel.setUserAiId(generateNextUserAiId());
            tryOutModel.setUserId(userId);
            tryOutModel.setParentModelId(parentModelId);
            tryOutModel.setModelName("试用-" + parentModelId);
            tryOutModel.setTryOutAt(new java.sql.Timestamp(System.currentTimeMillis()));
            tryOutModel.setTryOutNum(tryOutNum - 1); // 剩余试用次数
            tryOutModel.setStatus((byte) 2); // 试用状态
            tryOutModel.setIsVisible((byte) 1);
            tryOutModel.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            tryOutModel.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 5. 保存到数据库
            int insertResult = userAiModelMapper.insert(tryOutModel);
            if (insertResult > 0) {
                // 6. 更新内存缓存
                UserAiModel_list_njj.addToCache(tryOutModel);

                result.put("success", true);
                result.put("message", "试用成功，剩余试用次数: " + (tryOutNum - 1));
                result.put("userAiId", tryOutModel.getUserAiId());
                result.put("remainingTryOutNum", tryOutNum - 1);

                System.out.println("UserAiModelService: AI模型试用成功，userAiId: " + tryOutModel.getUserAiId() +
                        ", 剩余试用次数: " + (tryOutNum - 1));
            } else {
                result.put("success", false);
                result.put("message", "试用失败，请重试");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "试用AI模型时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从AI模型基础信息获取试用次数
     */
    private Long getTryOutNumFromBaseInfo(Long parentModelId) {
        try {
            // 从AimodelBaseInfo_list_njj中获取试用次数
            var baseInfo = Afriends_v3.entity.AimodelBaseInfo_list_njj.getAimodelBaseInfoById(parentModelId);
            if (baseInfo != null && baseInfo.getTryOutNum() != null) {
                return baseInfo.getTryOutNum();
            }
            // 如果tryOutNum为null，默认返回1次
            return 1L;
        } catch (Exception e) {
            System.err.println("获取AI模型试用次数失败: " + e.getMessage());
            return 1L; // 默认返回1次
        }
    }
}
