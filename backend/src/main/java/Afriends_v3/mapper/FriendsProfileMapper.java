package Afriends_v3.mapper;

import Afriends_v3.entity.FriendsProfile_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 好友资料Mapper接口
 */
@Mapper
public interface FriendsProfileMapper extends BaseMapper<FriendsProfile_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_friends_profile")
    int countAllRecords();

    @Select("SELECT * FROM v3_friends_profile")
    java.util.List<FriendsProfile_njj> selectAllRecords();

    @Select("SELECT MAX(friends_profile_id) FROM v3_friends_profile")
    Long selectMaxFriendsProfileId();
}
