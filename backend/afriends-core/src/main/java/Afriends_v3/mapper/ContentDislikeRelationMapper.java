package Afriends_v3.mapper;

import Afriends_v3.entity.ContentDislikeRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 内容点踩关系Mapper接口
 */
@Mapper
public interface ContentDislikeRelationMapper extends BaseMapper<ContentDislikeRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_content_dislike_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_content_dislike_relation LIMIT 5")
    java.util.List<ContentDislikeRelation_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_content_dislike_relation")
    java.util.List<ContentDislikeRelation_wlq> selectAllRecords();
    
    @Select("SELECT * FROM v2_content_dislike_relation WHERE user_id = #{userId} AND content_id = #{contentId} AND content_type = #{contentType} LIMIT 1")
    ContentDislikeRelation_wlq selectByUserIdAndContentId(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("contentType") Byte contentType);
    
    // 查询是否存在相同 userId 和 contentId 但不同 contentType 的记录（用于处理唯一索引冲突）
    @Select("SELECT * FROM v2_content_dislike_relation WHERE user_id = #{userId} AND content_id = #{contentId} LIMIT 1")
    ContentDislikeRelation_wlq selectByUserIdAndContentIdWithoutType(@Param("userId") Long userId, @Param("contentId") Long contentId);
    
    @Select("SELECT COUNT(*) FROM v2_content_dislike_relation WHERE content_id = #{contentId} AND content_type = #{contentType} AND is_active = 1")
    int countActiveDislikesByContentId(@Param("contentId") Long contentId, @Param("contentType") Byte contentType);
}

