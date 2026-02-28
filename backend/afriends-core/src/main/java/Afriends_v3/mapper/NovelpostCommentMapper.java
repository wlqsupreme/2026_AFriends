package Afriends_v3.mapper;

import Afriends_v3.entity.NovelpostComment_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 小说评论Mapper接口
 */
@Mapper
public interface NovelpostCommentMapper extends BaseMapper<NovelpostComment_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_novelpost_comment WHERE novel_id = #{novelId} AND is_deleted = 0")
    int countByNovelId(Long novelId);

    @Select("SELECT * FROM v2_novelpost_comment WHERE novel_id = #{novelId} AND is_deleted = 0 ORDER BY created_at DESC LIMIT #{limit}")
    java.util.List<NovelpostComment_wlq> selectByNovelIdWithLimit(Long novelId, Integer limit);

    @Select("SELECT * FROM v2_novelpost_comment WHERE novel_id = #{novelId} AND parent_comment_id IS NULL AND is_deleted = 0 ORDER BY created_at DESC")
    java.util.List<NovelpostComment_wlq> selectTopLevelCommentsByNovelId(Long novelId);

    @Select("SELECT * FROM v2_novelpost_comment WHERE parent_comment_id = #{parentCommentId} AND is_deleted = 0 ORDER BY created_at ASC")
    java.util.List<NovelpostComment_wlq> selectRepliesByParentId(Long parentCommentId);
}