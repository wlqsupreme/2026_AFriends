package Afriends_v3.service;

import Afriends_v3.entity.AimodelLevelRule_njj;
import Afriends_v3.entity.AimodelLevelRule_list_njj;
import Afriends_v3.mapper.AimodelLevelRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * AI模型等级规则服务类
 */
@Service
public class AimodelLevelRuleService {

    @Autowired
    private AimodelLevelRuleMapper aimodelLevelRuleMapper;

    /**
     * 加载AI模型等级规则数据到内存
     */
    public void loadAimodelLevelRuleToMemory() {
        try {
            System.out.println("AimodelLevelRuleService: 开始加载AI模型等级规则数据到内存...");
            AimodelLevelRule_list_njj.loadFromDatabase(aimodelLevelRuleMapper);
            System.out.println("AimodelLevelRuleService: AI模型等级规则数据加载完成");
        } catch (Exception e) {
            System.err.println("AimodelLevelRuleService: 加载AI模型等级规则数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI模型等级规则（从内存）
     */
    public List<AimodelLevelRule_njj> getAllLevelRulesFromMemory() {
        return AimodelLevelRule_list_njj.getAllLevelRules();
    }

    /**
     * 根据等级获取AI模型等级规则（从内存）
     */
    public AimodelLevelRule_njj getLevelRuleByLevelFromMemory(Integer level) {
        return AimodelLevelRule_list_njj.getLevelRuleByLevel(level);
    }

    /**
     * 获取下一级等级规则（从内存）
     */
    public AimodelLevelRule_njj getNextLevelRuleFromMemory(Integer currentLevel) {
        return AimodelLevelRule_list_njj.getNextLevelRule(currentLevel);
    }

    /**
     * 获取所有等级规则（按等级排序，从内存）
     */
    public List<AimodelLevelRule_njj> getAllLevelRulesOrderByLevelFromMemory() {
        return AimodelLevelRule_list_njj.getAllLevelRulesOrderByLevel();
    }

    /**
     * 获取AI模型等级规则统计信息
     */
    public Map<String, Object> getLevelRuleStatisticsFromMemory() {
        return AimodelLevelRule_list_njj.getStatistics();
    }

    /**
     * 强制刷新AI模型等级规则数据
     */
    public void refreshLevelRuleData() {
        loadAimodelLevelRuleToMemory();
    }
}
