package Afriends_v3.mapper;

import Afriends_v3.entity.EmojiBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 表情基础信息Mapper接口
 */
@Mapper
public interface EmojiBaseMapper extends BaseMapper<EmojiBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_emoji_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_emoji_base")
    java.util.List<EmojiBase_njj> selectAllRecords();
}
