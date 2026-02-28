package Interview_v3.mapper;

import Interview_v3.entity.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 通用收藏Mapper接口
 * 对应实体: Collect
 * 对应表: collect
 */
@Mapper
public interface CollectMapper extends BaseMapper<Collect> {
    // 继承BaseMapper，自动获得MyBatis-Plus提供的CRUD基础操作

    /**
     * 检查是否已收藏（核心）
     */
    @Select("SELECT COUNT(*) FROM collect WHERE user_id = #{userId} AND collect_type = #{type} AND target_id = #{targetId} AND is_deleted = 0")
    int checkCollected(
            @Param("userId") Long userId,
            @Param("type") Byte type,
            @Param("targetId") Long targetId
    );

    /**
     * 查询用户的收藏列表（按类型筛选，按收藏时间倒序）
     */
    @Select("SELECT * FROM collect WHERE user_id = #{userId} AND collect_type = #{type} AND is_deleted = 0 ORDER BY collect_time DESC")
    List<Collect> selectByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") Byte type
    );

    /**
     * 取消收藏（软删除）
     */
    @Update("UPDATE collect SET is_deleted = 1 WHERE user_id = #{userId} AND collect_type = #{type} AND target_id = #{targetId}")
    int cancelCollect(
            @Param("userId") Long userId,
            @Param("type") Byte type,
            @Param("targetId") Long targetId
    );

    /**
     * 统计用户收藏总数
     */
    @Select("SELECT COUNT(*) FROM collect WHERE user_id = #{userId} AND is_deleted = 0")
    int countByUserId(@Param("userId") Long userId);

    /**
     * 查询用户置顶的收藏（按类型）
     */
    @Select("SELECT * FROM collect WHERE user_id = #{userId} AND collect_type = #{type} AND is_top = 1 AND is_deleted = 0 ORDER BY collect_time DESC")
    List<Collect> selectTopCollectByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") Byte type
    );

    /**
     * 批量取消收藏
     */
    @Delete("UPDATE collect SET is_deleted = 1 WHERE id IN ${ids} AND user_id = #{userId}")
    int batchCancelCollect(
            @Param("userId") Long userId,
            @Param("ids") String ids
    );
}