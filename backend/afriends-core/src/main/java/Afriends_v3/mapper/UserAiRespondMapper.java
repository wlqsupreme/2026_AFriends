package Afriends_v3.mapper;

import Afriends_v3.entity.UserAiRespond_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户AI响应Mapper接口
 */
@Mapper
public interface UserAiRespondMapper extends BaseMapper<UserAiRespond_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_ai_respond")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_ai_respond LIMIT 5")
    java.util.List<UserAiRespond_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_user_ai_respond")
    java.util.List<UserAiRespond_wlq> selectAllRecords();
}
