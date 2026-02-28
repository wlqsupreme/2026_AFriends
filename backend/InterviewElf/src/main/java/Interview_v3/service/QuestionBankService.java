package Interview_v3.service;

import Interview_v3.entity.QuestionBank;
import Interview_v3.entity.QuestionBankList;
import Interview_v3.mapper.QuestionBankMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 题库服务层
 */
@Service
public class QuestionBankService extends ServiceImpl<QuestionBankMapper, QuestionBank> {

    @Autowired
    private QuestionBankMapper questionBankMapper;

    /**
     * 初始化题库缓存（项目启动时调用）
     */
    public void initCache() {
        QuestionBankList.loadFromDatabase(questionBankMapper);
    }

    /**
     * 刷新题库缓存
     */
    public void refreshCache() {
        QuestionBankList.forceRefresh(questionBankMapper);
    }

    /**
     * 获取所有题库（从缓存）
     */
    public List<QuestionBank> getAllQuestionBank() {
        return QuestionBankList.getAllQuestionBank();
    }

    /**
     * 根据题库ID查询（从缓存）
     */
    public QuestionBank getQuestionBankById(Long bankId) {
        return QuestionBankList.getQuestionBankById(bankId);
    }

    /**
     * 按标签筛选题库（从缓存）
     */
    public List<QuestionBank> getQuestionBankByTag(String tag) {
        return QuestionBankList.filterQuestionBankByTag(tag);
    }

    /**
     * 按价格筛选（低于指定金额）
     */
    public List<QuestionBank> getQuestionBankByPriceLessThan(BigDecimal maxPrice) {
        return questionBankMapper.selectByPriceLessThan(maxPrice);
    }

    /**
     * 按分类ID查询
     */
    public List<QuestionBank> getQuestionBankByCategoryId(Long categoryId) {
        return questionBankMapper.selectByCategoryId(categoryId);
    }

    /**
     * 新增题库
     */
    public boolean addQuestionBank(QuestionBank questionBank) {
        boolean save = this.save(questionBank);
        if (save) {
            // 新增后刷新缓存
            refreshCache();
        }
        return save;
    }

    /**
     * 修改题库
     */
    public boolean updateQuestionBank(QuestionBank questionBank) {
        boolean update = this.updateById(questionBank);
        if (update) {
            // 修改后刷新缓存
            refreshCache();
        }
        return update;
    }

    /**
     * 删除题库（软删除）
     */
    public boolean deleteQuestionBank(Long bankId) {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setBankId(bankId);
        questionBank.setIsDeleted((byte) 1);
        boolean update = this.updateById(questionBank);
        if (update) {
            // 删除后刷新缓存
            refreshCache();
        }
        return update;
    }

    /**
     * 获取题库统计信息
     */
    public Object getStatistics() {
        return QuestionBankList.getStatistics();
    }
}