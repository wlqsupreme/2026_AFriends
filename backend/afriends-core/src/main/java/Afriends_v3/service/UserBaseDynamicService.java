package Afriends_v3.service;

import Afriends_v3.entity.UserBaseDynamic_njj;
import Afriends_v3.entity.UserBaseDynamic_list_njj;
import Afriends_v3.mapper.UserBaseDynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 用户动态基础信息服务类
 * 负责管理用户动态基础信息数据的数据库操作和内存缓存
 */
@Service
public class UserBaseDynamicService {

    @Autowired
    private UserBaseDynamicMapper userBaseDynamicMapper;

    /**
     * 加载用户动态基础信息数据到内存
     */
    public void loadUserBaseDynamicToMemory() {
        try {
            System.out.println("UserBaseDynamicService: 开始从数据库加载用户动态基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserBaseDynamicService: 使用原生SQL查询用户动态基础信息数据...");
            var allUserBaseDynamic = userBaseDynamicMapper.selectAllRecords();
            System.out.println("UserBaseDynamicService: 原生SQL查询到 "
                    + (allUserBaseDynamic != null ? allUserBaseDynamic.size() : 0) + " 条记录");

            if (allUserBaseDynamic == null || allUserBaseDynamic.isEmpty()) {
                System.out.println("UserBaseDynamicService: 数据库中没有用户动态基础信息数据，跳过加载");
                UserBaseDynamic_list_njj.loadFromDatabaseDirectly(new ArrayList<>());
                return;
            }

            UserBaseDynamic_list_njj.loadFromDatabaseDirectly(allUserBaseDynamic);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseDynamicService: 用户动态基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseDynamicService: 加载用户动态基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户动态基础信息数据（从内存）
     */
    public List<UserBaseDynamic_njj> getAllUserBaseDynamicFromMemory() {
        return UserBaseDynamic_list_njj.getAllUserBaseDynamic();
    }

    /**
     * 根据动态ID获取用户动态基础信息数据
     */
    public UserBaseDynamic_njj getUserBaseDynamicByIdFromMemory(Long dynamicId) {
        return UserBaseDynamic_list_njj.getUserBaseDynamicById(dynamicId);
    }

    /**
     * 根据用户ID获取用户动态基础信息数据
     */
    public List<UserBaseDynamic_njj> getUserBaseDynamicByUserIdFromMemory(Long userId) {
        return UserBaseDynamic_list_njj.getUserBaseDynamicByUserId(userId);
    }

    /**
     * 根据内容类型获取用户动态基础信息数据
     */
    public List<UserBaseDynamic_njj> getUserBaseDynamicByContentTypeFromMemory(Byte contentType) {
        return UserBaseDynamic_list_njj.getUserBaseDynamicByContentType(contentType);
    }

    /**
     * 获取用户动态基础信息统计信息（从内存）
     */
    public Map<String, Object> getUserBaseDynamicStatisticsFromMemory() {
        return UserBaseDynamic_list_njj.getStatistics();
    }

    /**
     * 强制刷新用户动态基础信息数据
     */
    public void refreshUserBaseDynamicData() {
        loadUserBaseDynamicToMemory();
    }
}
