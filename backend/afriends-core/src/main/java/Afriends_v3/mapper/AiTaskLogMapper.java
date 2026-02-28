package Afriends_v3.mapper;

import Afriends_v3.entity.AiTaskLog_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI任务日志Mapper接口
 */
@Mapper
public interface AiTaskLogMapper extends BaseMapper<AiTaskLog_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_ai_task_log")
    int countAllRecords();

    @Select("SELECT * FROM v3_ai_task_log")
    java.util.List<AiTaskLog_njj> selectAllRecords();
}
