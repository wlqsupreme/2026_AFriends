package Afriends_v3.mapper;

import Afriends_v3.entity.MessageExtraBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 消息扩展基础信息Mapper接口
 */
@Mapper
public interface MessageExtraBaseMapper extends BaseMapper<MessageExtraBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_message_extra_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_message_extra_base")
    java.util.List<MessageExtraBase_njj> selectAllRecords();
}
