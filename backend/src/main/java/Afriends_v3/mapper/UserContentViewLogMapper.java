package Afriends_v3.mapper;

import Afriends_v3.entity.UserContentViewLog_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户内容查看日志Mapper接口
 */
@Mapper
public interface UserContentViewLogMapper extends BaseMapper<UserContentViewLog_njj> {

    /**
     * 查询所有用户内容查看日志（原生SQL）
     */
    @Select("SELECT * FROM v2_user_content_view_log")
    List<UserContentViewLog_njj> selectAllRecords();
}
