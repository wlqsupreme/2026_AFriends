package Afriends_v3.mapper;

import Afriends_v3.entity.UserBaseDynamic_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

/**
 * 用户动态基础信息Mapper接口
 */
@Mapper
public interface UserBaseDynamicMapper extends BaseMapper<UserBaseDynamic_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM `v2_user_base_dynamic @heng`")
    int countAllRecords();

    @Select("SELECT * FROM `v2_user_base_dynamic @heng`")
    java.util.List<UserBaseDynamic_njj> selectAllRecords();
    
    /**
     * 逻辑删除用户动态记录
     * 由于表名含有特殊字符@和空格，需要使用反引号包围表名
     */
    @Update("UPDATE `v2_user_base_dynamic @heng` SET is_deleted = 1 WHERE user_id = #{userId} AND (is_deleted = 0 OR is_deleted IS NULL)")
    int logicDeleteByUserId(@Param("userId") Long userId);
}