package Afriends_v3.service;

import Afriends_v3.entity.UserLikeRelation_njj;
import Afriends_v3.entity.UserLikeRelation_list_njj;
import Afriends_v3.mapper.UserLikeRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户喜欢关系服务类
 */
@Service
public class UserLikeRelationService {

    @Autowired
    private UserLikeRelationMapper userLikeRelationMapper;

    /**
     * 加载用户喜欢关系数据到内存
     */
    public void loadUserLikeRelationToMemory() {
        try {
            System.out.println("UserLikeRelationService: 开始从数据库加载用户喜欢关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserLikeRelationService: 使用原生SQL查询用户喜欢关系数据...");
            var allUserLikeRelation = userLikeRelationMapper.selectAllRecords();
            System.out.println("UserLikeRelationService: 原生SQL查询到 " + allUserLikeRelation.size() + " 条记录");

            if (allUserLikeRelation.isEmpty()) {
                System.out.println("UserLikeRelationService: 数据库中没有用户喜欢关系数据");
                return;
            }

            UserLikeRelation_list_njj.loadFromDatabaseDirectly(allUserLikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserLikeRelationService: 用户喜欢关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserLikeRelationService: 加载用户喜欢关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 从内存获取所有用户喜欢关系数据
     */
    public List<UserLikeRelation_njj> getAllUserLikeRelationFromMemory() {
        return UserLikeRelation_list_njj.getAllUserLikeRelation();
    }

    /**
     * 从内存获取用户喜欢关系统计信息
     */
    public Map<String, Object> getUserLikeRelationStatisticsFromMemory() {
        return UserLikeRelation_list_njj.getStatistics();
    }
}
