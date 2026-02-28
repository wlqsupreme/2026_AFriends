package Afriends_v3.mapper;

import Afriends_v3.entity.HardTagBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 标签核心元数据Mapper接口
 */
@Mapper
public interface HardTagBaseMapper extends BaseMapper<HardTagBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_hard_tag_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_hard_tag_base LIMIT 5")
    java.util.List<HardTagBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_hard_tag_base")
    java.util.List<HardTagBase_wlq> selectAllRecords();
}

