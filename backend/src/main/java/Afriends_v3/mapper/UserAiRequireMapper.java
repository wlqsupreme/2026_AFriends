package Afriends_v3.mapper;

import Afriends_v3.entity.UserAiRequire_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户AI需求Mapper接口
 */
@Mapper
public interface UserAiRequireMapper extends BaseMapper<UserAiRequire_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_ai_require")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_ai_require LIMIT 5")
    java.util.List<UserAiRequire_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_user_ai_require")
    java.util.List<UserAiRequire_wlq> selectAllRecords();
}
