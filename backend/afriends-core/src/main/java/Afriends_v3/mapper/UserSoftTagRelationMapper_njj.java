package Afriends_v3.mapper;

import Afriends_v3.entity.UserSoftTagRelation_njj;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserSoftTagRelationMapper_njj {

        /**
         * 根据用户ID获取软标签ID列表
         */
        @Select("SELECT soft_tag_id FROM v2_user_soft_tag_relation WHERE user_id = #{userId} AND soft_relevance_score > 0")
        List<Long> getSoftTagIdsByUserId(Long userId);

        /**
         * 根据用户ID获取软标签关系详情
         */
        @Select("SELECT soft_tag_id, soft_relevance_score FROM v2_user_soft_tag_relation WHERE user_id = #{userId} AND soft_relevance_score > 0 ORDER BY soft_relevance_score DESC")
        List<UserSoftTagRelation_njj> getSoftTagRelationsByUserId(Long userId);

        /**
         * 根据用户ID和软标签ID查询关系
         */
        @Select("SELECT * FROM v2_user_soft_tag_relation WHERE user_id = #{userId} AND soft_tag_id = #{softTagId}")
        UserSoftTagRelation_njj selectByUserIdAndSoftTagId(Long userId, Long softTagId);

        /**
         * 插入用户软标签关系
         */
        @Insert("INSERT INTO v2_user_soft_tag_relation (user_id, soft_tag_id, soft_relevance_score, last_updated, created_time) "
                        +
                        "VALUES (#{userId}, #{softTagId}, #{softRelevanceScore}, NOW(), NOW())")
        int insertUserSoftTagRelation(UserSoftTagRelation_njj relation);

        /**
         * 更新用户软标签关系
         */
        @Update("UPDATE v2_user_soft_tag_relation SET soft_relevance_score = #{softRelevanceScore}, last_updated = NOW() "
                        +
                        "WHERE user_id = #{userId} AND soft_tag_id = #{softTagId}")
        int updateUserSoftTagRelation(Long userId, Long softTagId, java.math.BigDecimal softRelevanceScore);

        /**
         * 删除用户软标签关系
         */
        @Delete("DELETE FROM v2_user_soft_tag_relation WHERE user_id = #{userId} AND soft_tag_id = #{softTagId}")
        int deleteUserSoftTagRelation(Long userId, Long softTagId);

        /**
         * 删除用户所有软标签关系
         */
        @Delete("DELETE FROM v2_user_soft_tag_relation WHERE user_id = #{userId}")
        int deleteAllUserSoftTagRelations(Long userId);

        /**
         * 统计用户软标签数量
         */
        @Select("SELECT COUNT(*) FROM v2_user_soft_tag_relation WHERE user_id = #{userId}")
        int countUserSoftTags(Long userId);
}
