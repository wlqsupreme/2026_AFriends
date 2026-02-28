package Afriends_v3.mapper;

import Afriends_v3.entity.TextRec_njj;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TextRecMapper_njj {
        // 新增文字推荐记录（处理重复键）
        @Insert("INSERT INTO v2_user_text_recommendation (id, user_id, text_content_id, score, rank_order, created_at) "
                        +
                        "VALUES (#{id}, #{userId}, #{textContentId}, #{score}, #{rankOrder}, #{createdAt}) " +
                        "ON DUPLICATE KEY UPDATE score = VALUES(score), rank_order = VALUES(rank_order), created_at = VALUES(created_at)")
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        int insertTextRec(TextRec_njj textRec);

        // 删除文字推荐记录
        @Delete("DELETE FROM v2_user_text_recommendation WHERE id = #{id}")
        int deleteTextRecById(Long id);

        // 修改文字推荐记录
        @Update("UPDATE v2_user_text_recommendation SET user_id=#{userId}, text_content_id=#{textContentId}, " +
                        "score=#{score}, rank_order=#{rankOrder}, created_at=#{createdAt} WHERE id=#{id}")
        int updateTextRec(TextRec_njj textRec);

        // 查询单条文字推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE id = #{id}")
        TextRec_njj selectTextRecById(Long id);

        // 根据用户ID查询推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE user_id = #{userId} ORDER BY rank_order ASC, score DESC")
        List<TextRec_njj> selectTextRecsByUserId(Long userId);

        // 查询所有文字推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation ORDER BY created_at DESC")
        List<TextRec_njj> selectAllTextRecs();

        // 根据用户ID和内容ID查询推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE user_id = #{userId} AND text_content_id = #{textContentId}")
        TextRec_njj selectTextRecByUserIdAndContentId(Long userId, Long textContentId);

        // 更新推荐得分
        @Update("UPDATE v2_user_text_recommendation SET score = #{score}, rank_order = #{rankOrder} WHERE id = #{id}")
        int updateTextRecScore(Long id, Float score, Integer rankOrder);

        // 获取用户推荐列表（按得分排序）
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE user_id = #{userId} ORDER BY score DESC LIMIT #{limit}")
        List<TextRec_njj> selectTextRecsByScore(Long userId, Integer limit);

        // 获取用户推荐列表（按排序顺序）
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE user_id = #{userId} ORDER BY rank_order ASC LIMIT #{limit}")
        List<TextRec_njj> selectTextRecsByRank(Long userId, Integer limit);

        // 统计用户推荐数量
        @Select("SELECT COUNT(*) FROM v2_user_text_recommendation WHERE user_id = #{userId}")
        int countTextRecsByUserId(Long userId);

        // 删除用户的所有推荐记录
        @Delete("DELETE FROM v2_user_text_recommendation WHERE user_id = #{userId}")
        int deleteAllTextRecsByUserId(Long userId);

        // 删除特定内容的推荐记录
        @Delete("DELETE FROM v2_user_text_recommendation WHERE text_content_id = #{textContentId}")
        int deleteTextRecsByContentId(Long textContentId);

        // 获取高分推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE score >= #{minScore} ORDER BY score DESC")
        List<TextRec_njj> selectHighScoreTextRecs(Float minScore);

        // 获取最新推荐记录
        @Select("SELECT id, user_id as userId, text_content_id as textContentId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_text_recommendation WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
        List<TextRec_njj> selectLatestTextRecs(Long userId, Integer limit);

        // 获取表中最大的ID
        @Select("SELECT MAX(id) FROM v2_user_text_recommendation")
        Long getMaxId();
}
