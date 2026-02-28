package Afriends_v3.mapper;

import Afriends_v3.entity.RecommendedNickname_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 推荐昵称Mapper接口
 */
@Mapper
public interface RecommendedNicknameMapper extends BaseMapper<RecommendedNickname_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_recommended_nickname")
    int countAllRecords();

    @Select("SELECT * FROM v2_recommended_nickname LIMIT 5")
    java.util.List<RecommendedNickname_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_recommended_nickname")
    java.util.List<RecommendedNickname_wlq> selectAllRecords();
}
