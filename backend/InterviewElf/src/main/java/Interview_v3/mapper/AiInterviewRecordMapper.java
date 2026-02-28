package Interview_v3.mapper;

import Interview_v3.entity.AiInterviewRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI模拟面试记录Mapper接口
 * 对应实体: AiInterviewRecord
 * 对应表: ai_interview_record
 */
@Mapper
public interface AiInterviewRecordMapper extends BaseMapper<AiInterviewRecord> {
    // 继承BaseMapper，自动获得MyBatis-Plus提供的CRUD基础操作（增删改查、分页等）

    /**
     * 统计面试记录总条数（仅未删除的）
     */
    @Select("SELECT COUNT(*) FROM ai_interview_record WHERE is_deleted = 0")
    int countAllRecords();

    /**
     * 查询前10条面试记录（用于数据预览）
     */
    @Select("SELECT * FROM ai_interview_record WHERE is_deleted = 0 LIMIT 10")
    List<AiInterviewRecord> selectFirstTen();

    /**
     * 查询所有未删除的面试记录
     */
    @Select("SELECT * FROM ai_interview_record WHERE is_deleted = 0")
    List<AiInterviewRecord> selectAllRecords();

    /**
     * 根据用户ID查询面试记录（核心业务方法）
     * @param userId 用户ID
     * @return 该用户的所有面试记录
     */
    @Select("SELECT * FROM ai_interview_record WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY start_time DESC")
    List<AiInterviewRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据面试状态查询记录
     * @param status 面试状态：1-进行中 2-已完成 3-已中断
     * @return 对应状态的面试记录列表
     */
    @Select("SELECT * FROM ai_interview_record WHERE interview_status = #{status} AND is_deleted = 0")
    List<AiInterviewRecord> selectByStatus(@Param("status") Byte status);

    /**
     * 根据用户ID+题库ID查询面试记录（查看用户某题库的面试记录）
     * @param userId 用户ID
     * @param bankId 题库ID
     * @return 符合条件的面试记录
     */
    @Select("SELECT * FROM ai_interview_record WHERE user_id = #{userId} AND bank_id = #{bankId} AND is_deleted = 0 ORDER BY start_time DESC")
    List<AiInterviewRecord> selectByUserIdAndBankId(
            @Param("userId") Long userId,
            @Param("bankId") Long bankId
    );

    /**
     * 查询用户的最新一条面试记录
     * @param userId 用户ID
     * @return 最新面试记录
     */
    @Select("SELECT * FROM ai_interview_record WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY start_time DESC LIMIT 1")
    AiInterviewRecord selectLatestByUserId(@Param("userId") Long userId);

    /**
     * 统计用户的面试总次数
     * @param userId 用户ID
     * @return 面试总次数
     */
    @Select("SELECT COUNT(*) FROM ai_interview_record WHERE user_id = #{userId} AND is_deleted = 0")
    int countByUserId(@Param("userId") Long userId);
}