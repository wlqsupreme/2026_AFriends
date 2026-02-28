package Afriends_v3.mapper;

import Afriends_v3.entity.CertRealnameBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 实名认证基础信息Mapper接口
 */
@Mapper
public interface CertRealnameBaseMapper extends BaseMapper<CertRealnameBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_cert_realname_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_cert_realname_base")
    java.util.List<CertRealnameBase_njj> selectAllRecords();
}
