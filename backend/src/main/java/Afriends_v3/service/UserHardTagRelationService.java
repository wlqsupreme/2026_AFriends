package Afriends_v3.service;

import Afriends_v3.entity.UserHardTagRelation_njj;
import Afriends_v3.entity.UserHardTagRelation_list_njj;
import Afriends_v3.mapper.UserHardTagRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户硬标签关系服务类
 */
@Service
public class UserHardTagRelationService {

    @Autowired
    private UserHardTagRelationMapper userHardTagRelationMapper;

    /**
     * 加载用户硬标签关系数据到内存
     */
    public void loadUserHardTagRelationToMemory() {
        try {
            System.out.println("UserHardTagRelationService: 开始从数据库加载用户硬标签关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserHardTagRelationService: 使用原生SQL查询用户硬标签关系数据...");
            var allUserHardTagRelation = userHardTagRelationMapper.selectAllRecords();
            System.out.println("UserHardTagRelationService: 原生SQL查询到 " + allUserHardTagRelation.size() + " 条记录");

            if (allUserHardTagRelation.isEmpty()) {
                System.out.println("UserHardTagRelationService: 数据库中没有用户硬标签关系数据");
                return;
            }

            UserHardTagRelation_list_njj.loadFromDatabaseDirectly(allUserHardTagRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserHardTagRelationService: 用户硬标签关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserHardTagRelationService: 加载用户硬标签关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 从内存获取所有用户硬标签关系数据
     */
    public List<UserHardTagRelation_njj> getAllUserHardTagRelationFromMemory() {
        return UserHardTagRelation_list_njj.getAllUserHardTagRelation();
    }

    /**
     * 从内存获取用户硬标签关系统计信息
     */
    public Map<String, Object> getUserHardTagRelationStatisticsFromMemory() {
        return UserHardTagRelation_list_njj.getStatistics();
    }
}
