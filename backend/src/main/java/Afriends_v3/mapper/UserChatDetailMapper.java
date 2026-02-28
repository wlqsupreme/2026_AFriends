package Afriends_v3.mapper;

import Afriends_v3.entity.UserChatDetail_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户聊天详情Mapper接口
 */
@Mapper
public interface UserChatDetailMapper extends BaseMapper<UserChatDetail_njj> {

    /**
     * 查询所有用户聊天详情（原生SQL）
     */
    @Select("SELECT * FROM v2_user_chat_detail_r")
    List<UserChatDetail_njj> selectAllRecords();

    /**
     * 查询最大ID
     */
    @Select("SELECT MAX(id) FROM v2_user_chat_detail_r")
    Long selectMaxId();
}