package Afriends_v3.mapper;

import Afriends_v3.entity.UserSoftTagRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户软标签关系Mapper接口
 */
@Mapper
public interface UserSoftTagRelationMapper extends BaseMapper<UserSoftTagRelation_njj> {

    /**
     * 查询所有用户软标签关系（原生SQL）
     */
    @Select("SELECT * FROM v2_user_soft_tag_relation")
    List<UserSoftTagRelation_njj> selectAllRecords();
}
