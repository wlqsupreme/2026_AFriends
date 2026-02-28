package Afriends_v3.mapper;

import Afriends_v3.entity.ImagePostBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户图片内容基础Mapper接口
 */
@Mapper
public interface ImagePostBaseMapper extends BaseMapper<ImagePostBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_imagepost_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_imagepost_base LIMIT 5")
    java.util.List<ImagePostBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_imagepost_base")
    java.util.List<ImagePostBase_wlq> selectAllRecords();
}
