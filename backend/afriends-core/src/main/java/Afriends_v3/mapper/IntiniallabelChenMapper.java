package Afriends_v3.mapper;

import Afriends_v3.entity.IntiniallabelChen_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 注册初始标签Mapper接口
 */
@Mapper
public interface IntiniallabelChenMapper extends BaseMapper<IntiniallabelChen_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_intiniallabel_chen")
    int countAllRecords();

    @Select("SELECT * FROM v2_intiniallabel_chen LIMIT 5")
    java.util.List<IntiniallabelChen_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_intiniallabel_chen")
    java.util.List<IntiniallabelChen_wlq> selectAllRecords();
}