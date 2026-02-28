package Afriends_v3.mapper;

import Afriends_v3.entity.AimodelCoinLog_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI模型币日志Mapper接口
 */
@Mapper
public interface AimodelCoinLogMapper extends BaseMapper<AimodelCoinLog_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_aimodel_coin_log")
    int countAllRecords();

    @Select("SELECT * FROM v3_aimodel_coin_log")
    java.util.List<AimodelCoinLog_njj> selectAllRecords();

    @Select("SELECT MAX(log_id) FROM v3_aimodel_coin_log")
    Long selectMaxLogId();
}
