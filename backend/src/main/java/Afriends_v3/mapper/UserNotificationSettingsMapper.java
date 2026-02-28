package Afriends_v3.mapper;

import Afriends_v3.entity.UserNotificationSettings_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

/**
 * 用户通知设置Mapper接口
 */
@Mapper
public interface UserNotificationSettingsMapper extends BaseMapper<UserNotificationSettings_njj> {
    /**
     * 根据用户ID获取通知设置
     * @param userId 用户ID
     * @return 用户通知设置对象
     */
    @Select("SELECT * FROM v3_user_notification_settings WHERE user_id = #{userId}")
    UserNotificationSettings_njj getByUserId(@Param("userId") Long userId);
}