package Afriends_v3.mapper;

import Afriends_v3.entity.CommentLikeRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

/**
 * 评论点赞关系Mapper接口
 */
@Mapper
public interface CommentLikeRelationMapper extends BaseMapper<CommentLikeRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_comment_like_relation WHERE comment_id = #{commentId} AND is_active = 1")
    int countActiveLikesByCommentId(@Param("commentId") Long commentId);

    @Select("SELECT * FROM v2_comment_like_relation WHERE user_id = #{userId} AND comment_id = #{commentId}")
    CommentLikeRelation_wlq selectByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

    @Select("SELECT COUNT(*) FROM v2_comment_like_relation WHERE user_id = #{userId} AND comment_id = #{commentId} AND is_active = 1")
    int checkUserLikedComment(@Param("userId") Long userId, @Param("commentId") Long commentId);

    @Select("SELECT COUNT(*) FROM v2_comment_like_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_comment_like_relation")
    java.util.List<CommentLikeRelation_wlq> selectAllRecords();
}