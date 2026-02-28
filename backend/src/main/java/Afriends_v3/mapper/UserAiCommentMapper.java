package Afriends_v3.mapper;

import Afriends_v3.entity.UserAiComment_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户AI评论Mapper接口
 */
@Mapper
public interface UserAiCommentMapper extends BaseMapper<UserAiComment_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_user_ai_comment")
    int countAllRecords();

    @Select("SELECT * FROM v3_user_ai_comment")
    java.util.List<UserAiComment_njj> selectAllRecords();

    @Select("SELECT MAX(user_ai_comment_id) FROM v3_user_ai_comment")
    Long selectMaxUserAiCommentId();
}
