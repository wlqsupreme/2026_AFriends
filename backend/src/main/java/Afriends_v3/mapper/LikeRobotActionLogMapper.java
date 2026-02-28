package Afriends_v3.mapper;

import Afriends_v3.entity.LikeRobotActionLog_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 点赞机器人操作日志Mapper接口
 */
@Mapper
public interface LikeRobotActionLogMapper extends BaseMapper<LikeRobotActionLog_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_like_robot_action_log")
    int countAllRecords();

    @Select("SELECT * FROM v2_like_robot_action_log LIMIT 5")
    java.util.List<LikeRobotActionLog_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_like_robot_action_log")
    java.util.List<LikeRobotActionLog_wlq> selectAllRecords();
}
