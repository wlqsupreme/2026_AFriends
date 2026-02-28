package Afriends_v3.mapper;

import Afriends_v3.entity.UserChatCall_njj;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserChatCallMapper {
    @Insert("INSERT INTO user_chat_call (id, session_id, caller_id, callee_id, call_type, call_status, start_time, end_time, duration) " +
            "VALUES (#{id}, #{sessionId}, #{callerId}, #{calleeId}, #{callType}, #{callStatus}, #{startTime}, #{endTime}, #{duration})")
    int insert(UserChatCall_njj callRecord);

    @Select("SELECT * FROM user_chat_call WHERE id = #{id}")
    UserChatCall_njj selectById(Long id);

    @Update("UPDATE user_chat_call SET call_status = #{callStatus}, end_time = #{endTime}, duration = #{duration} " +
            "WHERE id = #{id}")
    int updateById(UserChatCall_njj callRecord);

    @Select("SELECT MAX(id) FROM user_chat_call")
    Long selectMaxId();
}
