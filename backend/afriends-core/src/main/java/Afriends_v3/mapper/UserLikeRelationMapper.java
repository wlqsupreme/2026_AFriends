package Afriends_v3.mapper;

import Afriends_v3.entity.UserLikeRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户点赞关系Mapper接口
 */
@Mapper
public interface UserLikeRelationMapper extends BaseMapper<UserLikeRelation_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_like_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_like_relation")
    java.util.List<UserLikeRelation_njj> selectAllRecords();
}
