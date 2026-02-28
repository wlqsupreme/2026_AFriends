package Afriends_v3.mapper;

import Afriends_v3.entity.UserChatList_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户聊天列表Mapper接口
 */
@Mapper
public interface UserChatListMapper extends BaseMapper<UserChatList_njj> {

    /**
     * 查询所有用户聊天列表（原生SQL）
     */
    @Select("SELECT * FROM v2_user_chat_list_r")
    List<UserChatList_njj> selectAllRecords();
}
