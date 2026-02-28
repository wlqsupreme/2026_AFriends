package Afriends_v3.mapper;

import Afriends_v3.entity.TextpostBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 文字帖子基础Mapper接口
 */
@Mapper
public interface TextpostBaseMapper extends BaseMapper<TextpostBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_textpost_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_textpost_base LIMIT 5")
    java.util.List<TextpostBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_textpost_base")
    java.util.List<TextpostBase_wlq> selectAllRecords();
}
