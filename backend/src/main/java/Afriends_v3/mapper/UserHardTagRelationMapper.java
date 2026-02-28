package Afriends_v3.mapper;

import Afriends_v3.entity.UserHardTagRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户硬标签关系Mapper接口
 */
@Mapper
public interface UserHardTagRelationMapper extends BaseMapper<UserHardTagRelation_njj> {

    /**
     * 查询所有用户硬标签关系记录（原生SQL）
     */
    @Select("SELECT * FROM v2_user_hard_tag_relation")
    List<UserHardTagRelation_njj> selectAllRecords();
}
