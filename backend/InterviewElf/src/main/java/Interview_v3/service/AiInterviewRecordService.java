package Interview_v3.service;

import Interview_v3.entity.AiInterviewRecord;
import Interview_v3.entity.AiInterviewRecordList;
import Interview_v3.mapper.AiInterviewRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI模拟面试记录服务层
 */
@Service
public class AiInterviewRecordService extends ServiceImpl<AiInterviewRecordMapper, AiInterviewRecord> {

    @Autowired
    private AiInterviewRecordMapper interviewRecordMapper;

    /**
     * 初始化面试记录缓存
     */
    public void initCache() {
        AiInterviewRecordList.loadFromDatabase(interviewRecordMapper);
    }

    /**
     * 刷新面试记录缓存
     */
    public void refreshCache() {
        AiInterviewRecordList.forceRefresh(interviewRecordMapper);
    }

    /**
     * 获取所有面试记录（从缓存）
     */
    public List<AiInterviewRecord> getAllInterviewRecord() {
        return AiInterviewRecordList.getAllInterviewRecord();
    }

    /**
     * 根据ID查询面试记录
     */
    public AiInterviewRecord getInterviewRecordById(Long interviewId) {
        return AiInterviewRecordList.getInterviewRecordById(interviewId);
    }

    /**
     * 根据用户ID查询面试记录
     */
    public List<AiInterviewRecord> getInterviewRecordByUserId(Long userId) {
        return AiInterviewRecordList.getInterviewRecordByUserId(userId);
    }

    /**
     * 根据状态查询面试记录
     */
    public List<AiInterviewRecord> getInterviewRecordByStatus(Byte status) {
        return AiInterviewRecordList.getInterviewRecordByStatus(status);
    }

    /**
     * 新增面试记录
     */
    public boolean addInterviewRecord(AiInterviewRecord record) {
        boolean save = this.save(record);
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 更新面试记录（如状态、评分）
     */
    public boolean updateInterviewRecord(AiInterviewRecord record) {
        boolean update = this.updateById(record);
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 删除面试记录（软删除）
     */
    public boolean deleteInterviewRecord(Long interviewId) {
        AiInterviewRecord record = new AiInterviewRecord();
        record.setInterviewId(interviewId);
        record.setIsDeleted((byte) 1);
        boolean update = this.updateById(record);
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 获取统计信息
     */
    public Object getStatistics() {
        return AiInterviewRecordList.getStatistics();
    }
}