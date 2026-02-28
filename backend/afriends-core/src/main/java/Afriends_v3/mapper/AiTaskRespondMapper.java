package Afriends_v3.mapper;

import Afriends_v3.entity.AiTaskRespond_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI任务回应Mapper接口
 */
@Mapper
public interface AiTaskRespondMapper extends BaseMapper<AiTaskRespond_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_ai_task_respond")
    int countAllRecords();

    @Select("SELECT * FROM v2_ai_task_respond LIMIT 5")
    java.util.List<AiTaskRespond_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_ai_task_respond")
    java.util.List<AiTaskRespond_wlq> selectAllRecords();
}

