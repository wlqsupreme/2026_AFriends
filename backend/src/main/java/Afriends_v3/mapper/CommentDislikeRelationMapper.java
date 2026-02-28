package Afriends_v3.mapper;

import Afriends_v3.entity.CommentDislikeRelation_zjx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论点踩关系Mapper接口
 */
@Mapper
public interface CommentDislikeRelationMapper extends BaseMapper<CommentDislikeRelation_zjx> {
    // 继承BaseMapper，提供基本的CRUD操作

    /**
     * 统计指定评论的有效点踩数（is_active=1）
     */
    @Select("SELECT COUNT(*) FROM v2_comment_dislike_relation WHERE comment_id = #{commentId} AND is_active = 1")
    int countActiveDislikesByCommentId(@Param("commentId") Long commentId);

    /**
     * 根据用户ID和评论ID查询点踩记录（用于判断是否已点踩）
     */
    @Select("SELECT * FROM v2_comment_dislike_relation WHERE user_id = #{userId} AND comment_id = #{commentId}")
    CommentDislikeRelation_zjx selectByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    /**
     * 检查用户是否已点踩指定评论（有效状态）
     */
    @Select("SELECT COUNT(*) FROM v2_comment_dislike_relation WHERE user_id = #{userId} AND comment_id = #{commentId} AND is_active = 1")
    int checkUserDislikedComment(@Param("userId") Long userId, @Param("commentId") Long commentId);

    /**
     * 统计所有点踩记录总数
     */
    @Select("SELECT COUNT(*) FROM v2_comment_dislike_relation")
    int countAllRecords();

    /**
     * 查询所有点踩记录
     */
    @Select("SELECT * FROM v2_comment_dislike_relation")
    List<CommentDislikeRelation_zjx> selectAllRecords();
}