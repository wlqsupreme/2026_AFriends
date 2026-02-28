package Afriends_v3.mapper;

import Afriends_v3.entity.UserAiModel_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户AI模型Mapper接口
 */
@Mapper
public interface UserAiModelMapper extends BaseMapper<UserAiModel_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_user_ai_model")
    int countAllRecords();

    @Select("SELECT * FROM v3_user_ai_model")
    java.util.List<UserAiModel_njj> selectAllRecords();

    @Select("SELECT MAX(user_ai_id) FROM v3_user_ai_model")
    Long selectMaxUserAiId();

    @Update("UPDATE v3_user_ai_model SET model_name = #{newName} WHERE user_ai_id = #{userAiId}")
    int updateModelName(Long userAiId, String newName);

    @Update("UPDATE v3_user_ai_model SET level = #{level}, total_exp = #{totalExp} WHERE user_ai_id = #{userAiId}")
    int updateLevelAndExp(Long userAiId, Integer level, Integer totalExp);
}
