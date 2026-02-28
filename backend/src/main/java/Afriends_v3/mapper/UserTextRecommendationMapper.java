package Afriends_v3.mapper;

import Afriends_v3.entity.UserTextRecommendation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户文本推荐Mapper接口
 */
@Mapper
public interface UserTextRecommendationMapper extends BaseMapper<UserTextRecommendation_njj> {

    /**
     * 查询所有用户文本推荐记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_text_recommendation")
    List<UserTextRecommendation_njj> selectAllRecords();
}
