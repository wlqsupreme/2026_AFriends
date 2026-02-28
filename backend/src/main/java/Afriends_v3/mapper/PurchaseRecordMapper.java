package Afriends_v3.mapper;

import Afriends_v3.entity.PurchaseRecord_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 购买记录Mapper接口
 */
@Mapper
public interface PurchaseRecordMapper extends BaseMapper<PurchaseRecord_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_purchase_record")
    int countAllRecords();

    @Select("SELECT * FROM v3_purchase_record")
    java.util.List<PurchaseRecord_njj> selectAllRecords();

    @Select("SELECT MAX(record_id) FROM v3_purchase_record")
    Long selectMaxRecordId();
}
