package Afriends_v3.mapper;

import Afriends_v3.entity.UserAiRespondFeatureVector_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户AI响应特征向量Mapper接口
 */
@Mapper
public interface UserAiRespondFeatureVectorMapper extends BaseMapper<UserAiRespondFeatureVector_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_ai_respond_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_ai_respond_feature_vector LIMIT 5")
    java.util.List<UserAiRespondFeatureVector_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_user_ai_respond_feature_vector")
    java.util.List<UserAiRespondFeatureVector_wlq> selectAllRecords();
}
