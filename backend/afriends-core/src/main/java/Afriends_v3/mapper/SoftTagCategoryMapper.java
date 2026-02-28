package Afriends_v3.mapper;

import Afriends_v3.entity.SoftTagCategory_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 软标签分类Mapper接口
 */
@Mapper
public interface SoftTagCategoryMapper extends BaseMapper<SoftTagCategory_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_soft_tag_category")
    int countAllRecords();

    @Select("SELECT * FROM v2_soft_tag_category LIMIT 5")
    java.util.List<SoftTagCategory_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_soft_tag_category")
    java.util.List<SoftTagCategory_wlq> selectAllRecords();
}
