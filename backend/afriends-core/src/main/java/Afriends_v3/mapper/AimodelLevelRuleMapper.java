package Afriends_v3.mapper;

import Afriends_v3.entity.AimodelLevelRule_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI模型等级规则Mapper接口
 */
@Mapper
public interface AimodelLevelRuleMapper extends BaseMapper<AimodelLevelRule_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_aimodel_level_rule")
    int countAllRecords();

    @Select("SELECT * FROM v3_aimodel_level_rule")
    java.util.List<AimodelLevelRule_njj> selectAllRecords();

    @Select("SELECT * FROM v3_aimodel_level_rule WHERE level = #{level}")
    AimodelLevelRule_njj selectByLevel(Integer level);

    @Select("SELECT * FROM v3_aimodel_level_rule ORDER BY level ASC")
    java.util.List<AimodelLevelRule_njj> selectAllOrderByLevel();
}