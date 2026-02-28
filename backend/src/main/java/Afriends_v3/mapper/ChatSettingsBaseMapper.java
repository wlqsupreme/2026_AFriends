package Afriends_v3.mapper;

import Afriends_v3.entity.ChatSettingsBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 聊天设置基础信息Mapper接口
 */
@Mapper
public interface ChatSettingsBaseMapper extends BaseMapper<ChatSettingsBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_chat_settings_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_chat_settings_base")
    java.util.List<ChatSettingsBase_njj> selectAllRecords();
}
