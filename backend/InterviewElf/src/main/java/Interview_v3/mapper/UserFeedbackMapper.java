package Interview_v3.mapper;

import Interview_v3.entity.UserFeedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户反馈Mapper接口
 */
@Mapper
public interface UserFeedbackMapper extends BaseMapper<UserFeedback> {
    /**
     * 根据用户ID查询反馈
     */
    @Select("SELECT * FROM user_feedback WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<UserFeedback> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据处理状态查询反馈
     */
    @Select("SELECT * FROM user_feedback WHERE handle_status = #{status} ORDER BY create_time DESC")
    List<UserFeedback> selectByHandleStatus(@Param("status") Byte status);

    /**
     * 更新反馈处理状态
     */
    @Update("UPDATE user_feedback SET handle_status = #{status}, handle_result = #{result}, handle_time = NOW() WHERE feedback_id = #{feedbackId}")
    int updateHandleStatus(
            @Param("feedbackId") Long feedbackId,
            @Param("status") Byte status,
            @Param("result") String result
    );
}