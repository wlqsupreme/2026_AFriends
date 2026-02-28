package Afriends_v3.mapper;

import Afriends_v3.entity.UserDislikeRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户不喜欢关系Mapper接口
 */
@Mapper
public interface UserDislikeRelationMapper extends BaseMapper<UserDislikeRelation_njj> {

    /**
     * 查询所有用户不喜欢关系（原生SQL）
     */
    @Select("SELECT * FROM v2_user_dislike_relation")
    List<UserDislikeRelation_njj> selectAllRecords();
}
