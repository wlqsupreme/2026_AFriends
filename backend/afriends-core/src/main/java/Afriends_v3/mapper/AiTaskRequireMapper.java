package Afriends_v3.mapper;

import Afriends_v3.entity.AiTaskRequire_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI任务需求Mapper接口
 */
@Mapper
public interface AiTaskRequireMapper extends BaseMapper<AiTaskRequire_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_ai_task_require")
    int countAllRecords();

    @Select("SELECT * FROM v2_ai_task_require LIMIT 5")
    java.util.List<AiTaskRequire_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_ai_task_require")
    java.util.List<AiTaskRequire_wlq> selectAllRecords();
}

