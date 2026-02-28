package Afriends_v3.mapper;

import Afriends_v3.entity.INeed_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户需求信息Mapper接口
 */
@Mapper
public interface INeedMapper extends BaseMapper<INeed_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_need")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_need LIMIT 5")
    java.util.List<INeed_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_need")
    java.util.List<INeed_wlq> selectAllRecords();
}
