package Afriends_v3.mapper;

import Afriends_v3.entity.CertStudentBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 学生证认证基础信息Mapper接口
 */
@Mapper
public interface CertStudentBaseMapper extends BaseMapper<CertStudentBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_cert_student_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_cert_student_base")
    java.util.List<CertStudentBase_njj> selectAllRecords();
}
