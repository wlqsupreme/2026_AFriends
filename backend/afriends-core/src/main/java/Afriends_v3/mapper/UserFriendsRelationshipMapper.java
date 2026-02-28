package Afriends_v3.mapper;

import Afriends_v3.entity.UserFriendsRelationship_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户好友关系Mapper接口
 */
@Mapper
public interface UserFriendsRelationshipMapper extends BaseMapper<UserFriendsRelationship_njj> {

    /**
     * 查询所有用户好友关系（原生SQL）
     */
    @Select("SELECT * FROM v3_user_friends_relationship")
    List<UserFriendsRelationship_njj> selectAllRecords();

    /**
     * 查询最大ID
     */
    @Select("SELECT MAX(user_friends_info_id) FROM v3_user_friends_relationship")
    Long selectMaxId();
}