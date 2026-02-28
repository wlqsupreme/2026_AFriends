package Afriends_v3.mapper;

import Afriends_v3.entity.UserSystemMessage_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户系统消息Mapper接口
 */
@Mapper
public interface UserSystemMessageMapper extends BaseMapper<UserSystemMessage_njj> {

    /**
     * 查询所有用户系统消息（原生SQL）
     */
    @Select("SELECT * FROM v2_user_system_message")
    List<UserSystemMessage_njj> selectAllRecords();
    
    /**
     * 查询用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @Select("SELECT COUNT(*) FROM v2_user_system_message WHERE recipient_user_id = #{userId} AND is_read = 0")
    Long countUnreadByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户通知列表（分页、按时间倒序）
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 通知消息列表
     */
    @Select("SELECT * FROM v2_user_system_message WHERE recipient_user_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<UserSystemMessage_njj> getNotificationsByUserId(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 查询用户未读通知列表
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 未读通知消息列表
     */
    @Select("SELECT * FROM v2_user_system_message WHERE recipient_user_id = #{userId} AND is_read = 0 ORDER BY created_at DESC LIMIT #{limit}")
    List<UserSystemMessage_njj> getUnreadNotificationsByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID（用于验证消息归属）
     * @return 更新行数
     */
    @Update("UPDATE v2_user_system_message SET is_read = 1 WHERE message_id = #{messageId} AND recipient_user_id = #{userId}")
    int markAsRead(@Param("messageId") Long messageId, @Param("userId") Long userId);
    
    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 更新行数
     */
    @Update("UPDATE v2_user_system_message SET is_read = 1 WHERE recipient_user_id = #{userId} AND is_read = 0")
    int markAllAsRead(@Param("userId") Long userId);
}
