package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseTextComment_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户基础文本评论Mapper接口
 */
@Mapper
public interface UserBaseTextCommentMapper extends BaseMapper<UserBaseTextComment_njj> {

    /**
     * 查询所有用户基础文本评论记录（原生SQL）
     */
    @Select("SELECT * FROM `v2_user_base_text_comment @heng`")
    List<UserBaseTextComment_njj> selectAllRecords();
    
    /**
     * 逻辑删除用户文本评论记录
     * 由于表名含有特殊字符@和空格，需要使用反引号包围表名
     */
    @Update("UPDATE `v2_user_base_text_comment @heng` SET is_deleted = 1 WHERE user_id = #{userId} AND (is_deleted = 0 OR is_deleted IS NULL)")
    int logicDeleteByUserId(@Param("userId") Long userId);
}