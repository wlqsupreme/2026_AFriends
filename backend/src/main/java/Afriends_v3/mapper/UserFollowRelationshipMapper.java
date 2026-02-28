package Afriends_v3.mapper;

import Afriends_v3.entity.UserFollowRelationship_zjx;
import Afriends_v3.entity.UserFriendsRelationship_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户关注Mapper接口
 */
@Mapper
public interface UserFollowRelationshipMapper extends BaseMapper<UserFollowRelationship_zjx> {

    /**
     * 查询所有用户关注（原生SQL）
     */
    @Select("SELECT * FROM v3_user_follow_relationship")
    List<UserFriendsRelationship_njj> selectAllRecords();

    /**
     * 查询关注关系表中的最大ID
     * 注意：使用关注关系表的主键字段 follow_id
     */
    @Select("SELECT MAX(follow_id) FROM v3_user_follow_relationship")
    Long selectMaxId();

    /**
     * 根据用户ID（关注者ID）查询其所有关注关系
     * @param userId 关注者的用户ID
     * @return 该用户的所有关注关系列表
     */
    @Select("SELECT * FROM v3_user_follow_relationship WHERE user_id = #{userId}")
    List<UserFollowRelationship_zjx> selectByUserId(Long userId);

    /**
     * 补充：根据被关注用户ID查询所有关注者关系
     * @param followedUserId 被关注者的用户ID
     * @return 关注该用户的所有关系列表
     */
    @Select("SELECT * FROM v3_user_follow_relationship WHERE followed_user_id = #{followedUserId}")
    List<UserFollowRelationship_zjx> selectByFollowedUserId(Long followedUserId);
}