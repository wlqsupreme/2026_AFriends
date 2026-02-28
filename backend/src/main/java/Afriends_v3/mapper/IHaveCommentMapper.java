package Afriends_v3.mapper;

import Afriends_v3.entity.IHaveComment_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我拥有物品评论Mapper接口
 */
@Mapper
public interface IHaveCommentMapper extends BaseMapper<IHaveComment_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_have_comment")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_have_comment LIMIT 5")
    java.util.List<IHaveComment_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_have_comment")
    java.util.List<IHaveComment_wlq> selectAllRecords();
}
