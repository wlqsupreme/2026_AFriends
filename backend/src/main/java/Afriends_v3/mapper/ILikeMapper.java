package Afriends_v3.mapper;

import Afriends_v3.entity.ILike_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我喜欢内容基础Mapper接口
 */
@Mapper
public interface ILikeMapper extends BaseMapper<ILike_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_like")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_like LIMIT 5")
    java.util.List<ILike_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_like")
    java.util.List<ILike_wlq> selectAllRecords();
}
