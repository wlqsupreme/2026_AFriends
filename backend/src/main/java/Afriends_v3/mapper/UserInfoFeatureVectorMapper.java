package Afriends_v3.mapper;

import Afriends_v3.entity.UserInfoFeatureVector_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户信息特征向量Mapper接口
 */
@Mapper
public interface UserInfoFeatureVectorMapper extends BaseMapper<UserInfoFeatureVector_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_info_feature_vector")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_info_feature_vector")
    java.util.List<UserInfoFeatureVector_njj> selectAllRecords();
}
