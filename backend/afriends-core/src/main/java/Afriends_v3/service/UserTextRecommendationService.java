package Afriends_v3.service;

import Afriends_v3.entity.UserTextRecommendation_njj;
import Afriends_v3.entity.UserTextRecommendation_list_njj;
import Afriends_v3.mapper.UserTextRecommendationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户文本推荐服务类
 */
@Service
public class UserTextRecommendationService {

    @Autowired
    private UserTextRecommendationMapper userTextRecommendationMapper;

    /**
     * 加载用户文本推荐数据到内存
     */
    public void loadUserTextRecommendationToMemory() {
        try {
            System.out.println("UserTextRecommendationService: 开始从数据库加载用户文本推荐数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserTextRecommendationService: 使用原生SQL查询用户文本推荐数据...");
            var allUserTextRecommendation = userTextRecommendationMapper.selectAllRecords();
            System.out.println("UserTextRecommendationService: 原生SQL查询到 " + allUserTextRecommendation.size() + " 条记录");

            if (allUserTextRecommendation.isEmpty()) {
                System.out.println("UserTextRecommendationService: 数据库中没有用户文本推荐数据");
                return;
            }

            UserTextRecommendation_list_njj.loadFromDatabaseDirectly(allUserTextRecommendation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserTextRecommendationService: 用户文本推荐数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserTextRecommendationService: 加载用户文本推荐数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 从内存获取所有用户文本推荐数据
     */
    public List<UserTextRecommendation_njj> getAllUserTextRecommendationFromMemory() {
        return UserTextRecommendation_list_njj.getAllUserTextRecommendation();
    }

    /**
     * 从内存获取用户文本推荐统计信息
     */
    public Map<String, Object> getUserTextRecommendationStatisticsFromMemory() {
        return UserTextRecommendation_list_njj.getStatistics();
    }
}
