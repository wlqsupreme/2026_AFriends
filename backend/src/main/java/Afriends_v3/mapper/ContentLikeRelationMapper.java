package Afriends_v3.mapper;

import Afriends_v3.entity.ContentLikeRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 内容点赞关系Mapper接口
 */
@Mapper
public interface ContentLikeRelationMapper extends BaseMapper<ContentLikeRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_content_like_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_content_like_relation LIMIT 5")
    java.util.List<ContentLikeRelation_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_content_like_relation")
    java.util.List<ContentLikeRelation_wlq> selectAllRecords();

    // Mapper接口
    @Select("SELECT * FROM v2_content_like_relation WHERE user_id = #{userId} AND content_id = #{contentId} AND content_type = #{contentType} FOR UPDATE")
    ContentLikeRelation_wlq selectWithLock(@Param("userId") Long userId, @Param("contentId") Long contentId, @Param("contentType") Byte contentType);
}