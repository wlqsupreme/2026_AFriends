package Interview_v3.service;

import Interview_v3.entity.QuestionBankCategory;
import Interview_v3.entity.QuestionBankCategoryList;
import Interview_v3.mapper.QuestionBankCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题库分类服务层（缓存优先版）
 */
@Service
public class QuestionBankCategoryService extends ServiceImpl<QuestionBankCategoryMapper, QuestionBankCategory> {

    @Autowired
    private QuestionBankCategoryMapper categoryMapper;

    /**
     * 初始化分类缓存（项目启动时调用）
     */
    public void initCache() {
        QuestionBankCategoryList.loadFromDatabase(categoryMapper);
    }

    /**
     * 刷新分类缓存
     */
    public void refreshCache() {
        QuestionBankCategoryList.forceRefresh(categoryMapper);
    }

    /**
     * 获取所有分类（优先从缓存，按排序）
     */
    public List<QuestionBankCategory> getAllCategory() {
        // 替换：从缓存获取，而非直接查数据库
        return QuestionBankCategoryList.getAllCategory();
    }

    /**
     * 根据ID查询分类（优先从缓存）
     */
    public QuestionBankCategory getCategoryById(Long categoryId) {
        // 替换：从缓存获取，而非MyBatis-Plus的getById（数据库查询）
        QuestionBankCategory category = QuestionBankCategoryList.getCategoryById(categoryId);
        // 缓存未命中时，查数据库并刷新缓存（兜底逻辑）
        if (category == null) {
            category = this.getById(categoryId);
            if (category != null) {
                refreshCache(); // 缓存补全
            }
        }
        return category;
    }

    /**
     * 根据名称查询分类（优先从缓存）
     */
    public QuestionBankCategory getCategoryByName(String name) {
        // 替换：从缓存获取，而非直接查数据库
        QuestionBankCategory category = QuestionBankCategoryList.getCategoryByName(name);
        // 缓存未命中时，查数据库并刷新缓存（兜底逻辑）
        if (category == null) {
            category = categoryMapper.selectByCategoryName(name);
            if (category != null) {
                refreshCache(); // 缓存补全
            }
        }
        return category;
    }

    /**
     * 新增分类（新增后刷新缓存）
     */
    public boolean addCategory(QuestionBankCategory category) {
        category.setIsDeleted((byte) 0);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        boolean save = this.save(category);
        // 新增成功后刷新缓存，保证缓存与数据库一致
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 修改分类（修改后刷新缓存）
     */
    public boolean updateCategory(QuestionBankCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        boolean update = this.updateById(category);
        // 修改成功后刷新缓存
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 删除分类（软删除，删除后刷新缓存）
     */
    public boolean deleteCategory(Long categoryId) {
        // 检查分类下是否有题库
        int count = categoryMapper.countBankByCategoryId(categoryId);
        if (count > 0) {
            return false; // 分类下有题库，禁止删除
        }
        QuestionBankCategory category = new QuestionBankCategory();
        category.setCategoryId(categoryId);
        category.setIsDeleted((byte) 1);
        category.setUpdateTime(LocalDateTime.now());
        boolean update = this.updateById(category);
        // 删除成功后刷新缓存
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 调整分类排序（调整后刷新缓存）
     */
    public boolean updateCategorySort(Long categoryId, Integer sort) {
        QuestionBankCategory category = new QuestionBankCategory();
        category.setCategoryId(categoryId);
        category.setSort(sort);
        category.setUpdateTime(LocalDateTime.now());
        boolean update = this.updateById(category);
        // 排序调整后刷新缓存
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 获取分类缓存统计信息
     */
    public Object getStatistics() {
        return QuestionBankCategoryList.getStatistics();
    }
}