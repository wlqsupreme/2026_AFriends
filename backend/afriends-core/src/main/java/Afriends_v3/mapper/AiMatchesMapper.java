package Afriends_v3.mapper;

import Afriends_v3.entity.AiMatches_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI对话匹配结果Mapper接口
 */
@Mapper
public interface AiMatchesMapper extends BaseMapper<AiMatches_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_ai_matches")
    int countAllRecords();

    @Select("SELECT * FROM v2_ai_matches LIMIT 5")
    java.util.List<AiMatches_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_ai_matches")
    java.util.List<AiMatches_wlq> selectAllRecords();
}

