package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.QuestionBank;
import Interview_v3.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 题库接口控制器
 */
@RestController
@RequestMapping("/api/question-bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 初始化题库缓存
     */
    @GetMapping("/init-cache")
    public Result<?> initCache() {
        try {
            questionBankService.initCache();
            return Result.success("缓存初始化成功");
        } catch (Exception e) {
            return Result.error("缓存初始化失败：" + e.getMessage());
        }
    }

    /**
     * 刷新题库缓存
     */
    @GetMapping("/refresh-cache")
    public Result<?> refreshCache() {
        try {
            questionBankService.refreshCache();
            return Result.success("缓存刷新成功");
        } catch (Exception e) {
            return Result.error("缓存刷新失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有题库
     */
    @GetMapping("/list")
    public Result<List<QuestionBank>> getAllQuestionBank() {
        try {
            List<QuestionBank> list = questionBankService.getAllQuestionBank();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询题库列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询题库
     */
    @GetMapping("/{bankId}")
    public Result<QuestionBank> getQuestionBankById(@PathVariable Long bankId) {
        try {
            QuestionBank questionBank = questionBankService.getQuestionBankById(bankId);
            if (questionBank == null) {
                return Result.error(400, "题库不存在");
            }
            return Result.success(questionBank);
        } catch (Exception e) {
            return Result.error("查询题库失败：" + e.getMessage());
        }
    }

    /**
     * 按标签筛选题库
     */
    @GetMapping("/tag/{tag}")
    public Result<List<QuestionBank>> getQuestionBankByTag(@PathVariable String tag) {
        try {
            List<QuestionBank> list = questionBankService.getQuestionBankByTag(tag);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("按标签查询题库失败：" + e.getMessage());
        }
    }

    /**
     * 按价格筛选（低于指定金额）
     */
    @GetMapping("/price/{maxPrice}")
    public Result<List<QuestionBank>> getQuestionBankByPrice(@PathVariable BigDecimal maxPrice) {
        try {
            List<QuestionBank> list = questionBankService.getQuestionBankByPriceLessThan(maxPrice);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("按价格查询题库失败：" + e.getMessage());
        }
    }

    /**
     * 按分类ID查询
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<QuestionBank>> getQuestionBankByCategory(@PathVariable Long categoryId) {
        try {
            List<QuestionBank> list = questionBankService.getQuestionBankByCategoryId(categoryId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("按分类查询题库失败：" + e.getMessage());
        }
    }

    /**
     * 新增题库
     */
    @PostMapping("/add")
    public Result<?> addQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            boolean success = questionBankService.addQuestionBank(questionBank);
            if (success) {
                return Result.success("新增题库成功");
            } else {
                return Result.error("新增题库失败");
            }
        } catch (Exception e) {
            return Result.error("新增题库异常：" + e.getMessage());
        }
    }

    /**
     * 修改题库
     */
    @PutMapping("/update")
    public Result<?> updateQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            boolean success = questionBankService.updateQuestionBank(questionBank);
            if (success) {
                return Result.success("修改题库成功");
            } else {
                return Result.error("修改题库失败");
            }
        } catch (Exception e) {
            return Result.error("修改题库异常：" + e.getMessage());
        }
    }

    /**
     * 删除题库
     */
    @DeleteMapping("/delete/{bankId}")
    public Result<?> deleteQuestionBank(@PathVariable Long bankId) {
        try {
            boolean success = questionBankService.deleteQuestionBank(bankId);
            if (success) {
                return Result.success("删除题库成功");
            } else {
                return Result.error("删除题库失败");
            }
        } catch (Exception e) {
            return Result.error("删除题库异常：" + e.getMessage());
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public Result<?> getStatistics() {
        try {
            Object stats = questionBankService.getStatistics();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计信息失败：" + e.getMessage());
        }
    }
}