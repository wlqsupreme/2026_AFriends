package Afriends_v3.mapper;

import Afriends_v3.entity.UserImageRecommendation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户图片推荐Mapper接口
 */
@Mapper
public interface UserImageRecommendationMapper extends BaseMapper<UserImageRecommendation_njj> {

    /**
     * 查询所有用户图片推荐（原生SQL）
     */
    @Select("SELECT * FROM v2_user_image_recommendation")
    List<UserImageRecommendation_njj> selectAllRecords();
}
