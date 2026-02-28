package Afriends_v3.mapper;

import Afriends_v3.entity.ILikeFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我喜欢内容特征向量Mapper接口
 */
@Mapper
public interface ILikeFeatureVectorMapper extends BaseMapper<ILikeFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_like_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_like_feature_vector LIMIT 5")
    java.util.List<ILikeFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_like_feature_vector")
    java.util.List<ILikeFeatureVector_wlq> selectAllRecords();
}
