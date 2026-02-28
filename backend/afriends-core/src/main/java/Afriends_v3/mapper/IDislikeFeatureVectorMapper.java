package Afriends_v3.mapper;

import Afriends_v3.entity.IDislikeFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我不喜欢内容特征向量Mapper接口
 */
@Mapper
public interface IDislikeFeatureVectorMapper extends BaseMapper<IDislikeFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_dislike_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_dislike_feature_vector LIMIT 5")
    java.util.List<IDislikeFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_dislike_feature_vector")
    java.util.List<IDislikeFeatureVector_wlq> selectAllRecords();
}

