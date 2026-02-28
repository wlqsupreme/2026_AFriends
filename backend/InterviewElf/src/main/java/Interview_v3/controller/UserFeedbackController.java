package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.UserFeedback;
import Interview_v3.service.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户反馈接口控制器
 */
@RestController
@RequestMapping("/api/feedback")
public class UserFeedbackController {

    @Autowired
    private UserFeedbackService feedbackService;

    /**
     * 提交反馈
     */
    @PostMapping("/submit")
    public Result<?> submitFeedback(@RequestBody UserFeedback feedback) {
        try {
            boolean success = feedbackService.submitFeedback(feedback);
            if (success) {
                return Result.success("反馈提交成功，我们会尽快处理");
            } else {
                return Result.error("反馈提交失败");
            }
        } catch (Exception e) {
            return Result.error("提交反馈异常：" + e.getMessage());
        }
    }

    /**
     * 查询用户的反馈记录
     */
    @GetMapping("/user/{userId}")
    public Result<List<UserFeedback>> getFeedbackByUserId(@PathVariable Long userId) {
        try {
            List<UserFeedback> list = feedbackService.getFeedbackByUserId(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询反馈记录失败：" + e.getMessage());
        }
    }

    /**
     * 查询指定状态的反馈（管理员接口）
     */
    @GetMapping("/status/{status}")
    public Result<List<UserFeedback>> getFeedbackByStatus(@PathVariable Byte status) {
        try {
            List<UserFeedback> list = feedbackService.getFeedbackByStatus(status);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询反馈列表失败：" + e.getMessage());
        }
    }

    /**
     * 处理反馈（管理员接口）
     */
    @PutMapping("/handle/{feedbackId}/{status}")
    public Result<?> handleFeedback(
            @PathVariable Long feedbackId,
            @PathVariable Byte status,
            @RequestParam String result
    ) {
        try {
            boolean success = feedbackService.handleFeedback(feedbackId, status, result);
            if (success) {
                String msg = status == 2 ? "反馈处理完成" : "反馈标记为处理中";
                return Result.success(msg);
            } else {
                return Result.error("处理反馈失败");
            }
        } catch (Exception e) {
            return Result.error("处理反馈异常：" + e.getMessage());
        }
    }

    /**
     * 删除反馈（管理员接口）
     */
    @DeleteMapping("/delete/{feedbackId}")
    public Result<?> deleteFeedback(@PathVariable Long feedbackId) {
        try {
            boolean success = feedbackService.deleteFeedback(feedbackId);
            if (success) {
                return Result.success("删除反馈成功");
            } else {
                return Result.error("删除反馈失败");
            }
        } catch (Exception e) {
            return Result.error("删除反馈异常：" + e.getMessage());
        }
    }
}