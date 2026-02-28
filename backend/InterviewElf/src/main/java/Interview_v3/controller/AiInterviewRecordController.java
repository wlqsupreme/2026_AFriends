package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.AiInterviewRecord;
import Interview_v3.service.AiInterviewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI模拟面试记录接口控制器
 */
@RestController
@RequestMapping("/api/interview-record")
public class AiInterviewRecordController {

    @Autowired
    private AiInterviewRecordService interviewRecordService;

    /**
     * 初始化缓存
     */
    @GetMapping("/init-cache")
    public Result<?> initCache() {
        try {
            interviewRecordService.initCache();
            return Result.success("面试记录缓存初始化成功");
        } catch (Exception e) {
            return Result.error("缓存初始化失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有面试记录
     */
    @GetMapping("/list")
    public Result<List<AiInterviewRecord>> getAllRecord() {
        try {
            List<AiInterviewRecord> list = interviewRecordService.getAllInterviewRecord();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询面试记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询面试记录
     */
    @GetMapping("/user/{userId}")
    public Result<List<AiInterviewRecord>> getRecordByUserId(@PathVariable Long userId) {
        try {
            List<AiInterviewRecord> list = interviewRecordService.getInterviewRecordByUserId(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询用户面试记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态查询面试记录
     */
    @GetMapping("/status/{status}")
    public Result<List<AiInterviewRecord>> getRecordByStatus(@PathVariable Byte status) {
        try {
            List<AiInterviewRecord> list = interviewRecordService.getInterviewRecordByStatus(status);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询指定状态面试记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询面试记录
     */
    @GetMapping("/{interviewId}")
    public Result<AiInterviewRecord> getRecordById(@PathVariable Long interviewId) {
        try {
            AiInterviewRecord record = interviewRecordService.getInterviewRecordById(interviewId);
            if (record == null) {
                return Result.error(400, "面试记录不存在");
            }
            return Result.success(record);
        } catch (Exception e) {
            return Result.error("查询面试记录失败：" + e.getMessage());
        }
    }

    /**
     * 新增面试记录
     */
    @PostMapping("/add")
    public Result<?> addRecord(@RequestBody AiInterviewRecord record) {
        try {
            boolean success = interviewRecordService.addInterviewRecord(record);
            if (success) {
                return Result.success("新增面试记录成功");
            } else {
                return Result.error("新增面试记录失败");
            }
        } catch (Exception e) {
            return Result.error("新增面试记录异常：" + e.getMessage());
        }
    }

    /**
     * 更新面试记录
     */
    @PutMapping("/update")
    public Result<?> updateRecord(@RequestBody AiInterviewRecord record) {
        try {
            boolean success = interviewRecordService.updateInterviewRecord(record);
            if (success) {
                return Result.success("更新面试记录成功");
            } else {
                return Result.error("更新面试记录失败");
            }
        } catch (Exception e) {
            return Result.error("更新面试记录异常：" + e.getMessage());
        }
    }

    /**
     * 删除面试记录
     */
    @DeleteMapping("/delete/{interviewId}")
    public Result<?> deleteRecord(@PathVariable Long interviewId) {
        try {
            boolean success = interviewRecordService.deleteInterviewRecord(interviewId);
            if (success) {
                return Result.success("删除面试记录成功");
            } else {
                return Result.error("删除面试记录失败");
            }
        } catch (Exception e) {
            return Result.error("删除面试记录异常：" + e.getMessage());
        }
    }
}