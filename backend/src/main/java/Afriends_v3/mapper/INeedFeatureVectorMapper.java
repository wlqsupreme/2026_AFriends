package Afriends_v3.mapper;

import Afriends_v3.entity.INeedFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户需求特征向量Mapper接口
 */
@Mapper
public interface INeedFeatureVectorMapper extends BaseMapper<INeedFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_need_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_need_feature_vector LIMIT 5")
    java.util.List<INeedFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_need_feature_vector")
    java.util.List<INeedFeatureVector_wlq> selectAllRecords();
}
