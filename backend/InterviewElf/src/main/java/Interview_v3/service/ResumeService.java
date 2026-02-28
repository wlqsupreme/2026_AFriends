package Interview_v3.service;

import Interview_v3.entity.Resume;
import Interview_v3.entity.ResumeList;
import Interview_v3.mapper.ResumeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 简历服务层
 */
@Service
public class ResumeService extends ServiceImpl<ResumeMapper, Resume> {

    @Autowired
    private ResumeMapper resumeMapper;

    /**
     * 初始化简历缓存
     */
    public void initCache() {
        ResumeList.loadFromDatabase(resumeMapper);
    }

    /**
     * 刷新简历缓存
     */
    public void refreshCache() {
        ResumeList.forceRefresh(resumeMapper);
    }

    /**
     * 根据用户ID查询简历列表
     */
    public List<Resume> getResumeByUserId(Long userId) {
        return ResumeList.getResumeByUserId(userId);
    }

    /**
     * 获取用户默认简历
     */
    public Resume getDefaultResume(Long userId) {
        return ResumeList.getDefaultResumeByUserId(userId);
    }

    /**
     * 根据简历ID查询
     */
    public Resume getResumeById(Long resumeId) {
        return ResumeList.getResumeById(resumeId);
    }

    /**
     * 新增简历
     */
    public boolean addResume(Resume resume) {
        // 默认新增的简历不是默认简历
        if (resume.getIsDefault() == null) {
            resume.setIsDefault((byte) 0);
        }
        boolean save = this.save(resume);
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 设置默认简历（事务保证）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultResume(Long userId, Long resumeId) {
        try {
            // 1. 取消该用户所有默认简历
            resumeMapper.cancelAllDefault(userId);
            // 2. 设置指定简历为默认
            Resume resume = new Resume();
            resume.setId(resumeId);
            resume.setIsDefault((byte) 1);
            boolean update = this.updateById(resume);
            if (update) {
                refreshCache();
            }
            return update;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改简历
     */
    public boolean updateResume(Resume resume) {
        boolean update = this.updateById(resume);
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 删除简历（软删除）
     */
    public boolean deleteResume(Long resumeId) {
        Resume resume = new Resume();
        resume.setId(resumeId);
        resume.setIsDeleted((byte) 1);
        boolean update = this.updateById(resume);
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 统计用户简历数量
     */
    public int countResumeByUserId(Long userId) {
        return resumeMapper.countByUserId(userId);
    }

    /**
     * 获取简历统计信息
     */
    public Object getStatistics() {
        return ResumeList.getStatistics();
    }
}