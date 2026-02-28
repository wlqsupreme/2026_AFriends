package Afriends_v3.mapper;

import Afriends_v3.entity.IDislike_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我不喜欢内容基础Mapper接口
 */
@Mapper
public interface IDislikeMapper extends BaseMapper<IDislike_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_dislike")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_dislike LIMIT 5")
    java.util.List<IDislike_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_dislike")
    java.util.List<IDislike_wlq> selectAllRecords();
}

