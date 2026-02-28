package Afriends_v3.mapper;

import Afriends_v3.entity.AgreementBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 协议基础信息Mapper接口
 */
@Mapper
public interface AgreementBaseMapper extends BaseMapper<AgreementBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_agreement_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_agreement_base")
    java.util.List<AgreementBase_njj> selectAllRecords();
}
