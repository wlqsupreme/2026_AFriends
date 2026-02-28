package Afriends_v3.mapper;

import Afriends_v3.entity.SoftTagBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 软标签基础Mapper接口
 */
@Mapper
public interface SoftTagBaseMapper extends BaseMapper<SoftTagBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_soft_tag_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_soft_tag_base LIMIT 5")
    java.util.List<SoftTagBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_soft_tag_base")
    java.util.List<SoftTagBase_wlq> selectAllRecords();
}
