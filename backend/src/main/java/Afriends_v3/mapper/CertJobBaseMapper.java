package Afriends_v3.mapper;

import Afriends_v3.entity.CertJobBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 证书工作基础信息Mapper接口
 */
@Mapper
public interface CertJobBaseMapper extends BaseMapper<CertJobBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_cert_job_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_cert_job_base")
    java.util.List<CertJobBase_njj> selectAllRecords();
}
