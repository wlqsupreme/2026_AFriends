package Afriends_v3.mapper;

import Afriends_v3.entity.UserBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户基础Mapper接口
 */
@Mapper
public interface UserBaseMapper extends BaseMapper<UserBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_base LIMIT 5")
    java.util.List<UserBase_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_user_base")
    java.util.List<UserBase_wlq> selectAllRecords();

    /**
     * 新增：更新用户最后活跃时间
     * 注意：需要与数据库表中的"最后活跃时间"字段名匹配（这里假设字段名为 last_active）
     */
    @Update("UPDATE v2_user_base SET last_active = #{entity.lastActive} WHERE user_id = #{entity.userId}")
    int updateLastActive(@Param("entity") UserBase_wlq userBase);

    // 新增：根据手机号查询用户（关键方法）
    @Select("SELECT * FROM v2_user_base WHERE login_tel_account = #{phone} LIMIT 1")
    UserBase_wlq selectByPhone(@Param("phone") String phone);

    // 新增：通过微信openid查询用户（原有）
    @Select("SELECT * FROM v2_user_base WHERE login_wechat_account = #{openid} LIMIT 1")
    UserBase_wlq selectByWechatAccount(String openid);

    // 新增：通过抖音OpenID查询用户
    @Select("SELECT * FROM v2_user_base WHERE bound_douyin_account = #{openid} LIMIT 1")
    UserBase_wlq selectByDouyinOpenId(@Param("openid") String openid);

    // 新增：通过QQ OpenID查询用户
    @Select("SELECT * FROM v2_user_base WHERE login_qq_account = #{openid} LIMIT 1")
    UserBase_wlq selectByQqOpenId(@Param("openid") String openid);

    // 新增：获取最大用户ID（用于生成新用户ID）
    @Select("SELECT MAX(user_id) FROM v2_user_base WHERE user_id >= 1000100")
    Long selectMaxUserId();
}