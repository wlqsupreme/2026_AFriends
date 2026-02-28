package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * U开头_wlq实体类综合服务
 * 负责管理所有U开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class UserEntityService_wlq {

    @Autowired
    private UserAiRequireMapper userAiRequireMapper;

    @Autowired
    private UserAiRequireFeatureVectorMapper userAiRequireFeatureVectorMapper;

    @Autowired
    private UserAiRespondMapper userAiRespondMapper;

    @Autowired
    private UserAiRespondFeatureVectorMapper userAiRespondFeatureVectorMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    // UserAiRequire 相关方法
    public void loadUserAiRequireToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI需求数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRequire = userAiRequireMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRequire.size() + " 条记录");

            if (allUserAiRequire.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI需求数据");
                return;
            }

            UserAiRequire_list_wlq.loadFromDatabaseDirectly(allUserAiRequire);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI需求数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRequire_wlq> getAllUserAiRequireFromMemory() {
        return UserAiRequire_list_wlq.getAllUserAiRequire();
    }

    public Map<String, Object> getUserAiRequireStatisticsFromMemory() {
        return UserAiRequire_list_wlq.getStatistics();
    }

    // UserAiRequireFeatureVector 相关方法
    public void loadUserAiRequireFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI需求特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRequireFeatureVector = userAiRequireFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRequireFeatureVector.size() + " 条记录");

            if (allUserAiRequireFeatureVector.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI需求特征向量数据");
                return;
            }

            UserAiRequireFeatureVector_list_wlq.loadFromDatabaseDirectly(allUserAiRequireFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI需求特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRequireFeatureVector_wlq> getAllUserAiRequireFeatureVectorFromMemory() {
        return UserAiRequireFeatureVector_list_wlq.getAllUserAiRequireFeatureVector();
    }

    public Map<String, Object> getUserAiRequireFeatureVectorStatisticsFromMemory() {
        return UserAiRequireFeatureVector_list_wlq.getStatistics();
    }

    // UserAiRespond 相关方法
    public void loadUserAiRespondToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI响应数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRespond = userAiRespondMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRespond.size() + " 条记录");

            if (allUserAiRespond.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI响应数据");
                return;
            }

            UserAiRespond_list_wlq.loadFromDatabaseDirectly(allUserAiRespond);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI响应数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRespond_wlq> getAllUserAiRespondFromMemory() {
        return UserAiRespond_list_wlq.getAllUserAiRespond();
    }

    public Map<String, Object> getUserAiRespondStatisticsFromMemory() {
        return UserAiRespond_list_wlq.getStatistics();
    }

    // UserAiRespondFeatureVector 相关方法
    public void loadUserAiRespondFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户AI响应特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiRespondFeatureVector = userAiRespondFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserAiRespondFeatureVector.size() + " 条记录");

            if (allUserAiRespondFeatureVector.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户AI响应特征向量数据");
                return;
            }

            UserAiRespondFeatureVector_list_wlq.loadFromDatabaseDirectly(allUserAiRespondFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户AI响应特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiRespondFeatureVector_wlq> getAllUserAiRespondFeatureVectorFromMemory() {
        return UserAiRespondFeatureVector_list_wlq.getAllUserAiRespondFeatureVector();
    }

    public Map<String, Object> getUserAiRespondFeatureVectorStatisticsFromMemory() {
        return UserAiRespondFeatureVector_list_wlq.getStatistics();
    }

    // UserBase 相关方法
    public void loadUserBaseToMemory() {
        try {
            System.out.println("UserEntityService_wlq: 开始从数据库加载用户基础数据...");
            long startTime = System.currentTimeMillis();

            var allUserBase = userBaseMapper.selectAllRecords();
            System.out.println("UserEntityService_wlq: 原生SQL查询到 " + allUserBase.size() + " 条记录");

            if (allUserBase.isEmpty()) {
                System.out.println("UserEntityService_wlq: 数据库中没有用户基础数据");
                return;
            }

            UserBase_list_wlq.loadFromDatabaseDirectly(allUserBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService_wlq: 用户基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBase_wlq> getAllUserBaseFromMemory() {
        return UserBase_list_wlq.getAllUserBase();
    }

    public Map<String, Object> getUserBaseStatisticsFromMemory() {
        return UserBase_list_wlq.getStatistics();
    }

    // 加载所有U开头_wlq实体类数据
    public void loadAllUserEntityDataToMemory() {
        System.out.println("UserEntityService_wlq: 开始加载所有U开头_wlq实体类数据...");

        try {
            loadUserAiRequireToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRequireFeatureVectorToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI需求特征向量数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRespondToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserAiRespondFeatureVectorToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户AI响应特征向量数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadUserBaseToMemory();
        } catch (Exception e) {
            System.err.println("UserEntityService_wlq: 加载用户基础数据失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("UserEntityService_wlq: 所有U开头_wlq实体类数据加载完成！");
    }
}
