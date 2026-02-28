package Afriends_v3.mapper;

import Afriends_v3.entity.ImageRec_njj;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ImageRecMapper_njj {
        // 新增图片推荐记录（处理重复键）
        @Insert("INSERT INTO v2_user_image_recommendation (id, user_id, image_content_id, score, rank_order, created_at) "
                        +
                        "VALUES (#{id}, #{userId}, #{imageContentId}, #{score}, #{rankOrder}, #{createdAt}) " +
                        "ON DUPLICATE KEY UPDATE score = VALUES(score), rank_order = VALUES(rank_order), created_at = VALUES(created_at)")
        int insertImageRec(ImageRec_njj imageRec);

        // 修改图片推荐记录
        @Update("UPDATE v2_user_image_recommendation SET user_id=#{userId}, image_content_id=#{imageContentId}, " +
                        "score=#{score}, rank_order=#{rankOrder} WHERE id=#{id}")
        int updateImageRec(ImageRec_njj imageRec);

        // 查询单条图片推荐记录
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE id = #{id}")
        ImageRec_njj selectImageRecById(Long id);

        // 根据用户ID查询图片推荐记录
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE user_id = #{userId} ORDER BY rank_order ASC, score DESC")
        List<ImageRec_njj> selectImageRecsByUserId(Long userId);

        // 根据图片内容ID查询推荐记录
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE image_content_id = #{imageContentId} ORDER BY score DESC")
        List<ImageRec_njj> selectImageRecsByImageContentId(Long imageContentId);

        // 查询所有图片推荐记录
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation ORDER BY created_at DESC")
        List<ImageRec_njj> selectAllImageRecs();

        // 更新推荐得分
        @Update("UPDATE v2_user_image_recommendation SET score = #{score} WHERE id = #{id}")
        int updateImageRecScore(Long id, Float score);

        // 更新推荐排序
        @Update("UPDATE v2_user_image_recommendation SET rank_order = #{rankOrder} WHERE id = #{id}")
        int updateImageRecRankOrder(Long id, Integer rankOrder);

        // 根据得分查询推荐记录
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE score >= #{minScore} ORDER BY score DESC")
        List<ImageRec_njj> selectImageRecsByScore(Float minScore);

        // 统计用户推荐数量
        @Select("SELECT COUNT(*) FROM v2_user_image_recommendation WHERE user_id = #{userId}")
        int countImageRecsByUserId(Long userId);

        // 获取用户最高得分推荐
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE user_id = #{userId} ORDER BY score DESC LIMIT 1")
        ImageRec_njj selectHighestScoreImageRecByUserId(Long userId);

        // 删除用户的所有图片推荐记录
        @Delete("DELETE FROM v2_user_image_recommendation WHERE user_id = #{userId}")
        int deleteAllImageRecsByUserId(Long userId);

        // 检查用户是否已有该图片的推荐记录
        @Select("SELECT COUNT(*) FROM v2_user_image_recommendation WHERE user_id = #{userId} AND image_content_id = #{imageContentId}")
        int checkUserImageRecExists(Long userId, Long imageContentId);

        // 获取用户个性化推荐（按得分和排序）
        @Select("SELECT id as id, user_id as userId, image_content_id as imageContentId, " +
                        "score as score, rank_order as rankOrder, created_at as createdAt " +
                        "FROM v2_user_image_recommendation WHERE user_id = #{userId} " +
                        "ORDER BY rank_order ASC, score DESC LIMIT #{limit}")
        List<ImageRec_njj> selectPersonalizedImageRecsByUserId(Long userId, Integer limit);

        // 获取推荐统计信息
        @Select("SELECT COUNT(*) as totalRecommendations, AVG(score) as avgScore, " +
                        "MAX(score) as maxScore, MIN(score) as minScore " +
                        "FROM v2_user_image_recommendation WHERE user_id = #{userId}")
        Object getUserImageRecStats(Long userId);

        // 获取表中最大的ID
        @Select("SELECT MAX(id) FROM v2_user_image_recommendation")
        Long getMaxId();
}
