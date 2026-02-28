package Afriends_v3.mapper;

import Afriends_v3.entity.UserNovelRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户小说关系Mapper接口
 */
@Mapper
public interface UserNovelRelationMapper extends BaseMapper<UserNovelRelation_njj> {

    /**
     * 查询所有用户小说关系（原生SQL）
     */
    @Select("SELECT * FROM v2_user_novel_relation")
    List<UserNovelRelation_njj> selectAllRecords();
    
    /**
     * 设置用户小说关系的删除时间
     * 该实体只有deletedAt时间戳字段，没有isDeleted删除标识
     */
    @Update("UPDATE v2_user_novel_relation SET delete_at = #{deletedAt} WHERE user_id = #{userId} AND (delete_at IS NULL OR delete_at = '1970-01-01 08:00:00')")
    int setDeletedAtByUserId(@Param("userId") Long userId, @Param("deletedAt") Timestamp deletedAt);
}