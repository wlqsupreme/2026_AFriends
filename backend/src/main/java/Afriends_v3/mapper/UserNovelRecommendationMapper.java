package Afriends_v3.mapper;

import Afriends_v3.entity.UserNovelRecommendation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户小说推荐Mapper接口
 */
@Mapper
public interface UserNovelRecommendationMapper extends BaseMapper<UserNovelRecommendation_njj> {

    /**
     * 查询所有用户小说推荐（原生SQL）
     */
    @Select("SELECT * FROM v2_user_novel_recommendation")
    List<UserNovelRecommendation_njj> selectAllRecords();
}
