package Afriends_v3.mapper;

import Afriends_v3.entity.NovelpostFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 小说帖子特征向量Mapper接口
 */
@Mapper
public interface NovelpostFeatureVectorMapper extends BaseMapper<NovelpostFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_novelpost_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_novelpost_feature_vector LIMIT 5")
    java.util.List<NovelpostFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_novelpost_feature_vector")
    java.util.List<NovelpostFeatureVector_wlq> selectAllRecords();
}
