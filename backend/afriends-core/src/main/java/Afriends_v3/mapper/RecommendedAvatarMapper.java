package Afriends_v3.mapper;

import Afriends_v3.entity.RecommendedAvatar_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 推荐头像Mapper接口
 */
@Mapper
public interface RecommendedAvatarMapper extends BaseMapper<RecommendedAvatar_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_recommended_avatar")
    int countAllRecords();

    @Select("SELECT * FROM v2_recommended_avatar LIMIT 5")
    java.util.List<RecommendedAvatar_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_recommended_avatar")
    java.util.List<RecommendedAvatar_wlq> selectAllRecords();
}
