package Afriends_v3.mapper;

import Afriends_v3.entity.AiChatListDetailR_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * AI聊天历史Mapper接口
 */
@Mapper
public interface AiChatListDetailRMapper extends BaseMapper<AiChatListDetailR_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_ai_chat_list_detail_r")
    int countAllRecords();

    @Select("SELECT * FROM v2_ai_chat_list_detail_r LIMIT 5")
    java.util.List<AiChatListDetailR_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_ai_chat_list_detail_r")
    java.util.List<AiChatListDetailR_wlq> selectAllRecords();

    @Select("SELECT MAX(id) FROM v2_ai_chat_list_detail_r")
    Long selectMaxId();
}
