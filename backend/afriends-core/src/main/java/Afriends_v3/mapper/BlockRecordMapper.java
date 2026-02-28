package Afriends_v3.mapper;

import Afriends_v3.entity.BlockRecord_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 屏蔽记录Mapper接口
 */
@Mapper
public interface BlockRecordMapper extends BaseMapper<BlockRecord_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_block_record")
    int countAllRecords();

    @Select("SELECT * FROM v3_block_record")
    java.util.List<BlockRecord_njj> selectAllRecords();
}
