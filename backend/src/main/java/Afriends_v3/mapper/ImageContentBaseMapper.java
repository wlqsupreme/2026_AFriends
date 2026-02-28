package Afriends_v3.mapper;

import Afriends_v3.entity.ImageContentBase_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

/**
 * 图片内容基础信息Mapper接口
 */
@Mapper
public interface ImageContentBaseMapper extends BaseMapper<ImageContentBase_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM `v2_image_content_base_@niu`")
    int countAllRecords();

    @Select("SELECT * FROM `v2_image_content_base_@niu` LIMIT 5")
    java.util.List<ImageContentBase_wlq> selectFirstFive();

    @Select("SELECT * FROM `v2_image_content_base_@niu`")
    java.util.List<ImageContentBase_wlq> selectAllRecords();
    
    /**
     * 逻辑删除图片内容记录
     * 由于表名含有特殊字符@，需要使用反引号包围表名
     */
    @Update("UPDATE `v2_image_content_base_@niu` SET is_deleted = 1, deleted_at = #{deletedAt} WHERE user_id = #{userId} AND (is_deleted = 0 OR is_deleted IS NULL OR is_deleted = false)")
    int logicDeleteByUserId(@Param("userId") Long userId, @Param("deletedAt") Timestamp deletedAt);
}