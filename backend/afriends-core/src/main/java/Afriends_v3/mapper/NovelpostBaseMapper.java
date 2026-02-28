package Afriends_v3.mapper;

import Afriends_v3.entity.NovelpostBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 小说帖子基础Mapper接口
 */
@Mapper
public interface NovelpostBaseMapper extends BaseMapper<NovelpostBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_novelpost_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_novelpost_base LIMIT 5")
    java.util.List<NovelpostBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_novelpost_base")
    java.util.List<NovelpostBase_wlq> selectAllRecords();
}
