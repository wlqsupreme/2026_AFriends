package Afriends_v3.mapper;

import Afriends_v3.entity.GiftBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 礼物基表Mapper接口
 */
@Mapper
public interface GiftBaseMapper extends BaseMapper<GiftBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_gift_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_gift_base LIMIT 5")
    java.util.List<GiftBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_gift_base")
    java.util.List<GiftBase_wlq> selectAllRecords();
}

