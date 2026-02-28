package Afriends_v3.mapper;

import Afriends_v3.entity.NovelRec_njj;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NovelRecMapper_njj {
        // 新增小说推荐记录
        @Insert("INSERT INTO v2_user_novel_recommendation (id, user_id, novel_content_id, score, rank_order, created_at) "
                        +
                        "VALUES (#{id}, #{userId}, #{novelId}, #{score}, #{rankOrder}, #{createdAt}) " +
                        "ON DUPLICATE KEY UPDATE score = VALUES(score), rank_order = VALUES(rank_order), created_at = VALUES(created_at)")
        int insertNovelRec(NovelRec_njj novelRec);

        // 删除小说推荐记录
        @Delete("DELETE FROM v2_user_novel_recommendation WHERE id = #{id}")
        int deleteNovelRecById(Long id);

        // 修改小说推荐记录
        @Update("UPDATE v2_user_novel_recommendation SET user_id=#{userId}, novel_content_id=#{novelId}, " +
                        "score=#{score}, rank_order=#{rankOrder}, created_at=#{createdAt} WHERE id=#{id}")
        int updateNovelRec(NovelRec_njj novelRec);

        // 查询单条小说推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE id = #{id}")
        NovelRec_njj selectNovelRecById(Long id);

        // 根据用户ID查询推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} ORDER BY score DESC")
        List<NovelRec_njj> selectNovelRecsByUserId(Long userId);

        // 查询所有小说推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation ORDER BY created_at DESC")
        List<NovelRec_njj> selectAllNovelRecs();

        // 根据用户ID和小说ID查询推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} AND novel_content_id = #{novelId}")
        NovelRec_njj selectNovelRecByUserIdAndNovelId(Long userId, Long novelId);

        // 更新推荐得分
        @Update("UPDATE v2_user_novel_recommendation SET score = #{score}, rank_order = #{rankOrder} WHERE id = #{id}")
        int updateNovelRecScore(Long id, Float score, Integer rankOrder);

        // 获取用户推荐列表（按得分排序）
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} ORDER BY score DESC LIMIT #{limit}")
        List<NovelRec_njj> selectNovelRecsByScore(Long userId, Integer limit);

        // 获取用户推荐列表（按排序顺序）
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} ORDER BY rank_order ASC LIMIT #{limit}")
        List<NovelRec_njj> selectNovelRecsByRank(Long userId, Integer limit);

        // 统计用户推荐数量
        @Select("SELECT COUNT(*) FROM v2_user_novel_recommendation WHERE user_id = #{userId}")
        int countNovelRecsByUserId(Long userId);

        // 删除用户的所有推荐记录
        @Delete("DELETE FROM v2_user_novel_recommendation WHERE user_id = #{userId}")
        int deleteAllNovelRecsByUserId(Long userId);

        // 删除特定小说的推荐记录
        @Delete("DELETE FROM v2_user_novel_recommendation WHERE novel_content_id = #{novelId}")
        int deleteNovelRecsByNovelId(Long novelId);

        // 获取高分推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE score >= #{minScore} ORDER BY score DESC")
        List<NovelRec_njj> selectHighScoreNovelRecs(Float minScore);

        // 获取最新推荐记录
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{limit}")
        List<NovelRec_njj> selectLatestNovelRecs(Long userId, Integer limit);

        // 获取热门小说推荐
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} AND score >= 0.8 ORDER BY score DESC LIMIT #{limit}")
        List<NovelRec_njj> selectHotNovelRecs(Long userId, Integer limit);

        // 获取个性化推荐
        @Select("SELECT id, user_id as userId, novel_content_id as novelId, score, rank_order as rankOrder, created_at as createdAt "
                        +
                        "FROM v2_user_novel_recommendation WHERE user_id = #{userId} AND score >= #{minScore} ORDER BY score DESC LIMIT #{limit}")
        List<NovelRec_njj> selectPersonalizedNovelRecs(Long userId, Float minScore, Integer limit);

        // 检查用户是否已有该小说推荐
        @Select("SELECT COUNT(*) FROM v2_user_novel_recommendation WHERE user_id = #{userId} AND novel_content_id = #{novelId}")
        int checkUserHasNovelRec(Long userId, Long novelId);

        // 获取表中最大的ID
        @Select("SELECT MAX(id) FROM v2_user_novel_recommendation")
        Long getMaxId();
}
