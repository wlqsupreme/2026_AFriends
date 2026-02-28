package Afriends_v3.mapper;

import Afriends_v3.entity.CertBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 证书基础信息Mapper接口
 */
@Mapper
public interface CertBaseMapper extends BaseMapper<CertBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_cert_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_cert_base")
    java.util.List<CertBase_njj> selectAllRecords();
}
