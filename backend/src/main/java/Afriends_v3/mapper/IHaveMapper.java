package Afriends_v3.mapper;

import Afriends_v3.entity.IHave_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 我拥有物品Mapper接口
 */
@Mapper
public interface IHaveMapper extends BaseMapper<IHave_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_i_have")
    int countAllRecords();

    @Select("SELECT * FROM v2_i_have LIMIT 5")
    java.util.List<IHave_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_i_have")
    java.util.List<IHave_wlq> selectAllRecords();
}
