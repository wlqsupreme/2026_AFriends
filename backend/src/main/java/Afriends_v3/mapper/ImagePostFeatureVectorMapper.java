package Afriends_v3.mapper;

import Afriends_v3.entity.ImagePostFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 图片帖子特征向量Mapper接口
 */
@Mapper
public interface ImagePostFeatureVectorMapper extends BaseMapper<ImagePostFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_imagepost_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_imagepost_feature_vector LIMIT 5")
    java.util.List<ImagePostFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_imagepost_feature_vector")
    java.util.List<ImagePostFeatureVector_wlq> selectAllRecords();
}
