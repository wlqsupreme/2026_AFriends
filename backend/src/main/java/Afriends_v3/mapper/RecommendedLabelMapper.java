package Afriends_v3.mapper;

import Afriends_v3.entity.RecommendedLabel_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 推荐标签Mapper接口
 */
@Mapper
public interface RecommendedLabelMapper extends BaseMapper<RecommendedLabel_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_recommended_label")
    int countAllRecords();

    @Select("SELECT * FROM v2_recommended_label LIMIT 5")
    java.util.List<RecommendedLabel_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_recommended_label")
    java.util.List<RecommendedLabel_wlq> selectAllRecords();
}
