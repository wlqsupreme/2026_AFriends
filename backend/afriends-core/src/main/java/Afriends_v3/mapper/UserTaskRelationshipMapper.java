package Afriends_v3.mapper;

import Afriends_v3.entity.UserTaskRelationship_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户任务关系Mapper接口
 */
@Mapper
public interface UserTaskRelationshipMapper extends BaseMapper<UserTaskRelationship_njj> {

    /**
     * 查询所有用户任务关系（原生SQL）
     */
    @Select("SELECT * FROM v3_user_task_relationship")
    List<UserTaskRelationship_njj> selectAllRecords();
}
