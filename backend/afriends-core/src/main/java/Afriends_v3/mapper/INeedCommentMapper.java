package Afriends_v3.mapper;

import Afriends_v3.entity.INeedComment_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户需求评论Mapper接口
 */
@Mapper
public interface INeedCommentMapper extends BaseMapper<INeedComment_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_need_comment")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_need_comment LIMIT 5")
    java.util.List<INeedComment_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_need_comment")
    java.util.List<INeedComment_wlq> selectAllRecords();
}
