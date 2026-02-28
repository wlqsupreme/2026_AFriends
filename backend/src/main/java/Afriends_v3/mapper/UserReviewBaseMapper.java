package Afriends_v3.mapper;

import Afriends_v3.entity.UserReviewBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

/**
 * 用户评论基础Mapper接口
 */
@Mapper
public interface UserReviewBaseMapper extends BaseMapper<UserReviewBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_user_review_base")
    int countAllRecords();

    @Select("SELECT * FROM v2_user_review_base")
    java.util.List<UserReviewBase_njj> selectAllRecords();
    
    /**
     * 逻辑删除用户评论记录
     * 由于表名不含特殊字符，可以正常使用MyBatis Plus的逻辑删除功能
     * 这里提供自定义方法是为了在AccountDeletionService中统一处理方式
     */
    @Update("UPDATE v2_user_review_base SET is_deleted = 1, deleted_at = #{deletedAt} WHERE (reviewer_user_id = #{userId} OR target_user_id = #{userId}) AND (is_deleted = 0 OR is_deleted IS NULL)")
    int logicDeleteByUserId(@Param("userId") Long userId, @Param("deletedAt") Timestamp deletedAt);
}