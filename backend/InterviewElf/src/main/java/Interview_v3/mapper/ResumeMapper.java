package Interview_v3.mapper;

import Interview_v3.entity.Resume;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 简历Mapper接口
 * 对应实体: Resume
 * 对应表: resume
 */
@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {
    // 继承BaseMapper，自动获得MyBatis-Plus提供的CRUD基础操作

    /**
     * 统计用户的简历总数（未删除）
     */
    @Select("SELECT COUNT(*) FROM resume WHERE user_id = #{userId} AND is_deleted = 0")
    int countByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的默认简历
     */
    @Select("SELECT * FROM resume WHERE user_id = #{userId} AND is_default = 1 AND is_deleted = 0 LIMIT 1")
    Resume selectDefaultByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的所有简历（按更新时间倒序）
     */
    @Select("SELECT * FROM resume WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY update_time DESC")
    List<Resume> selectByUserId(@Param("userId") Long userId);

    /**
     * 取消用户所有默认简历（设置默认简历时用）
     */
    @Update("UPDATE resume SET is_default = 0 WHERE user_id = #{userId} AND is_deleted = 0")
    void cancelAllDefault(@Param("userId") Long userId);

    /**
     * 根据技能标签模糊查询简历
     */
    @Select("SELECT * FROM resume WHERE user_id = #{userId} AND skill LIKE CONCAT('%', #{skill}, '%') AND is_deleted = 0")
    List<Resume> selectBySkill(@Param("userId") Long userId, @Param("skill") String skill);

    /**
     * 统计用户默认简历数量（用于校验）
     */
    @Select("SELECT COUNT(*) FROM resume WHERE user_id = #{userId} AND is_default = 1 AND is_deleted = 0")
    int countDefaultByUserId(@Param("userId") Long userId);
}