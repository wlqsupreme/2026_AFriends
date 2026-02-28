package Afriends_v3.mapper;

import Afriends_v3.entity.TextpostComment_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 文字帖子评论Mapper接口
 */
@Mapper
public interface TextpostCommentMapper extends BaseMapper<TextpostComment_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_textpost_comment")
    int countAllRecords();

    @Select("SELECT * FROM v2_textpost_comment LIMIT 5")
    java.util.List<TextpostComment_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_textpost_comment")
    java.util.List<TextpostComment_wlq> selectAllRecords();
}
