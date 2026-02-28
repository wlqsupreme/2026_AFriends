package Afriends_v3.mapper;

import Afriends_v3.entity.TaskComment_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 任务评论Mapper接口
 */
@Mapper
public interface TaskCommentMapper extends BaseMapper<TaskComment_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_task_comment")
    int countAllRecords();

    @Select("SELECT * FROM v3_task_comment")
    java.util.List<TaskComment_njj> selectAllRecords();

    @Select("SELECT MAX(CAST(task_comment_id AS UNSIGNED)) FROM v3_task_comment")
    Long selectMaxTaskCommentId();

    @Select("SELECT COUNT(*) FROM v3_task_comment WHERE user_id = #{userId}")
    int checkUserTaskCommentExists(@Param("userId") Long userId);
}
