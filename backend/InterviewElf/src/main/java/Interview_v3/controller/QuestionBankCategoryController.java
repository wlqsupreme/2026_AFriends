package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.QuestionBankCategory;
import Interview_v3.service.QuestionBankCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库分类接口控制器
 */
@RestController
@RequestMapping("/api/category")
public class QuestionBankCategoryController {

    @Autowired
    private QuestionBankCategoryService categoryService;

    /**
     * 获取所有分类
     */
    @GetMapping("/list")
    public Result<List<QuestionBankCategory>> getAllCategory() {
        try {
            List<QuestionBankCategory> list = categoryService.getAllCategory();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询分类列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询分类
     */
    @GetMapping("/{categoryId}")
    public Result<QuestionBankCategory> getCategoryById(@PathVariable Long categoryId) {
        try {
            QuestionBankCategory category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return Result.error(400, "分类不存在");
            }
            return Result.success(category);
        } catch (Exception e) {
            return Result.error("查询分类失败：" + e.getMessage());
        }
    }

    /**
     * 新增分类
     */
    @PostMapping("/add")
    public Result<?> addCategory(@RequestBody QuestionBankCategory category) {
        try {
            // 检查分类名称是否重复
            QuestionBankCategory exist = categoryService.getCategoryByName(category.getCategoryName());
            if (exist != null) {
                return Result.error(400, "分类名称已存在");
            }
            boolean success = categoryService.addCategory(category);
            if (success) {
                return Result.success("新增分类成功");
            } else {
                return Result.error("新增分类失败");
            }
        } catch (Exception e) {
            return Result.error("新增分类异常：" + e.getMessage());
        }
    }

    /**
     * 修改分类
     */
    @PutMapping("/update")
    public Result<?> updateCategory(@RequestBody QuestionBankCategory category) {
        try {
            boolean success = categoryService.updateCategory(category);
            if (success) {
                return Result.success("修改分类成功");
            } else {
                return Result.error("修改分类失败");
            }
        } catch (Exception e) {
            return Result.error("修改分类异常：" + e.getMessage());
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/delete/{categoryId}")
    public Result<?> deleteCategory(@PathVariable Long categoryId) {
        try {
            boolean success = categoryService.deleteCategory(categoryId);
            if (success) {
                return Result.success("删除分类成功");
            } else {
                return Result.error(400, "分类下存在题库，禁止删除");
            }
        } catch (Exception e) {
            return Result.error("删除分类异常：" + e.getMessage());
        }
    }

    /**
     * 调整分类排序
     */
    @PutMapping("/update-sort/{categoryId}/{sort}")
    public Result<?> updateSort(
            @PathVariable Long categoryId,
            @PathVariable Integer sort
    ) {
        try {
            boolean success = categoryService.updateCategorySort(categoryId, sort);
            if (success) {
                return Result.success("调整排序成功");
            } else {
                return Result.error("调整排序失败");
            }
        } catch (Exception e) {
            return Result.error("调整排序异常：" + e.getMessage());
        }
    }
}