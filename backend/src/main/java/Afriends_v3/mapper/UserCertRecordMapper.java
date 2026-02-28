package Afriends_v3.mapper;

import Afriends_v3.entity.UserCertRecord_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户认证记录Mapper接口
 */
@Mapper
public interface UserCertRecordMapper extends BaseMapper<UserCertRecord_njj> {

    /**
     * 查询所有用户认证记录（原生SQL）
     */
    @Select("SELECT * FROM v3_user_cert_record")
    List<UserCertRecord_njj> selectAllRecords();

    /**
     * 查询最大的record_id
     */
    @Select("SELECT MAX(record_id) FROM v3_user_cert_record")
    Long selectMaxRecordId();
}
