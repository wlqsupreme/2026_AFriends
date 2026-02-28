package Afriends_v3.mapper;

import Afriends_v3.entity.IDislikeComment_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我不喜欢内容评论Mapper接口
 */
@Mapper
public interface IDislikeCommentMapper extends BaseMapper<IDislikeComment_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_dislike_comment")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_dislike_comment LIMIT 5")
    java.util.List<IDislikeComment_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_dislike_comment")
    java.util.List<IDislikeComment_wlq> selectAllRecords();
}

