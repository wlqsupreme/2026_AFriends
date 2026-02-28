package Interview_v3.service;

import Interview_v3.entity.UserFeedback;
import Interview_v3.entity.UserFeedbackList;
import Interview_v3.mapper.UserFeedbackMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户反馈服务层（缓存优先版）
 */
@Service
public class UserFeedbackService extends ServiceImpl<UserFeedbackMapper, UserFeedback> {

    @Autowired
    private UserFeedbackMapper feedbackMapper;

    /**
     * 初始化反馈缓存（项目启动时调用）
     */
    public void initCache() {
        UserFeedbackList.loadFromDatabase(feedbackMapper);
    }

    /**
     * 刷新反馈缓存
     */
    public void refreshCache() {
        UserFeedbackList.forceRefresh(feedbackMapper);
    }

    /**
     * 根据用户ID查询反馈（优先从缓存）
     */
    public List<UserFeedback> getFeedbackByUserId(Long userId) {
        // 优先从缓存查询
        List<UserFeedback> feedbackList = UserFeedbackList.getFeedbackByUserId(userId);
        // 缓存未命中/无数据时，查数据库并刷新缓存（兜底逻辑）
        if (feedbackList == null || feedbackList.isEmpty()) {
            feedbackList = feedbackMapper.selectByUserId(userId);
            if (feedbackList != null && !feedbackList.isEmpty()) {
                refreshCache(); // 缓存补全
            }
        }
        return feedbackList;
    }

    /**
     * 根据处理状态查询反馈（优先从缓存）
     */
    public List<UserFeedback> getFeedbackByStatus(Byte status) {
        // 优先从缓存查询
        List<UserFeedback> feedbackList = UserFeedbackList.getFeedbackByHandleStatus(status);
        // 缓存未命中/无数据时，查数据库并刷新缓存（兜底逻辑）
        if (feedbackList == null || feedbackList.isEmpty()) {
            feedbackList = feedbackMapper.selectByHandleStatus(status);
            if (feedbackList != null && !feedbackList.isEmpty()) {
                refreshCache(); // 缓存补全
            }
        }
        return feedbackList;
    }

    /**
     * 根据反馈ID查询反馈（优先从缓存）
     */
    public UserFeedback getFeedbackById(Long feedbackId) {
        // 优先从缓存查询
        UserFeedback feedback = UserFeedbackList.getFeedbackById(feedbackId);
        // 缓存未命中时，查数据库并刷新缓存（兜底逻辑）
        if (feedback == null) {
            feedback = this.getById(feedbackId);
            if (feedback != null) {
                refreshCache(); // 缓存补全
            }
        }
        return feedback;
    }

    /**
     * 获取所有反馈（优先从缓存）
     */
    public List<UserFeedback> getAllFeedback() {
        // 优先从缓存查询
        return UserFeedbackList.getAllFeedback();
    }

    /**
     * 提交反馈（提交后刷新缓存）
     */
    public boolean submitFeedback(UserFeedback feedback) {
        feedback.setHandleStatus((byte) 0); // 默认未处理
        feedback.setCreateTime(LocalDateTime.now());
        boolean save = this.save(feedback);
        // 提交成功后刷新缓存，保证缓存与数据库一致
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 处理反馈（管理员操作，处理后刷新缓存）
     */
    public boolean handleFeedback(Long feedbackId, Byte status, String result) {
        int affectRows = feedbackMapper.updateHandleStatus(feedbackId, status, result);
        // 处理成功后刷新缓存
        if (affectRows > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    /**
     * 删除反馈（物理删除，删除后刷新缓存）
     */
    public boolean deleteFeedback(Long feedbackId) {
        boolean remove = this.removeById(feedbackId);
        // 删除成功后刷新缓存
        if (remove) {
            refreshCache();
        }
        return remove;
    }

    /**
     * 获取反馈缓存统计信息（缓存状态、各状态反馈数）
     */
    public Object getStatistics() {
        return UserFeedbackList.getStatistics();
    }
}