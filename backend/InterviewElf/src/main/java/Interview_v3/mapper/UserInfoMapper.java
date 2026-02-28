package Interview_v3.mapper;

import Interview_v3.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户基础信息Mapper接口
 * 对应实体: UserInfo
 * 对应表: user_info
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    // 继承BaseMapper，自动获得MyBatis-Plus提供的CRUD基础操作

    /**
     * 根据手机号查询用户（登录核心）
     */
    @Select("SELECT * FROM user_info WHERE phone = #{phone} AND is_deleted = 0")
    UserInfo selectByPhone(@Param("phone") String phone);

    /**
     * 检查手机号是否已注册
     */
    @Select("SELECT COUNT(*) FROM user_info WHERE phone = #{phone} AND is_deleted = 0")
    int checkPhoneExist(@Param("phone") String phone);

    /**
     * 修改密码（登录后改密）
     */
    @Update("UPDATE user_info SET password = #{newPwd}, update_time = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updatePassword(
            @Param("userId") Long userId,
            @Param("newPwd") String newPwd
    );

    /**
     * 禁用/启用账号
     */
    @Update("UPDATE user_info SET status = #{status}, update_time = NOW() WHERE id = #{userId} AND is_deleted = 0")
    int updateUserStatus(
            @Param("userId") Long userId,
            @Param("status") Byte status
    );

    /**
     * 统计正常状态的用户数
     */
    @Select("SELECT COUNT(*) FROM user_info WHERE status = 1 AND is_deleted = 0")
    int countNormalUser();

    /**
     * 根据用户ID查询密码（用于密码验证）
     */
    @Select("SELECT password FROM user_info WHERE id = #{userId} AND is_deleted = 0")
    String selectPasswordById(@Param("userId") Long userId);
}