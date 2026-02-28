package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.Resume;
import Interview_v3.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 简历管理接口控制器
 */
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    /**
     * 初始化简历缓存
     */
    @GetMapping("/init-cache")
    public Result<?> initCache() {
        try {
            resumeService.initCache();
            return Result.success("简历缓存初始化成功");
        } catch (Exception e) {
            return Result.error("缓存初始化失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户的所有简历
     */
    @GetMapping("/user/{userId}")
    public Result<List<Resume>> getResumeByUserId(@PathVariable Long userId) {
        try {
            List<Resume> list = resumeService.getResumeByUserId(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询用户简历失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户默认简历
     */
    @GetMapping("/default/{userId}")
    public Result<Resume> getDefaultResume(@PathVariable Long userId) {
        try {
            Resume resume = resumeService.getDefaultResume(userId);
            if (resume == null) {
                return Result.error(400, "用户暂无默认简历");
            }
            return Result.success(resume);
        } catch (Exception e) {
            return Result.error("查询默认简历失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询简历
     */
    @GetMapping("/{resumeId}")
    public Result<Resume> getResumeById(@PathVariable Long resumeId) {
        try {
            Resume resume = resumeService.getResumeById(resumeId);
            if (resume == null) {
                return Result.error(400, "简历不存在");
            }
            return Result.success(resume);
        } catch (Exception e) {
            return Result.error("查询简历失败：" + e.getMessage());
        }
    }

    /**
     * 新增简历
     */
    @PostMapping("/add")
    public Result<?> addResume(@RequestBody Resume resume) {
        try {
            boolean success = resumeService.addResume(resume);
            if (success) {
                return Result.success("新增简历成功");
            } else {
                return Result.error("新增简历失败");
            }
        } catch (Exception e) {
            return Result.error("新增简历异常：" + e.getMessage());
        }
    }

    /**
     * 设置默认简历
     */
    @PutMapping("/set-default/{userId}/{resumeId}")
    public Result<?> setDefaultResume(
            @PathVariable Long userId,
            @PathVariable Long resumeId
    ) {
        try {
            boolean success = resumeService.setDefaultResume(userId, resumeId);
            if (success) {
                return Result.success("设置默认简历成功");
            } else {
                return Result.error("设置默认简历失败");
            }
        } catch (Exception e) {
            return Result.error("设置默认简历异常：" + e.getMessage());
        }
    }

    /**
     * 修改简历
     */
    @PutMapping("/update")
    public Result<?> updateResume(@RequestBody Resume resume) {
        try {
            boolean success = resumeService.updateResume(resume);
            if (success) {
                return Result.success("修改简历成功");
            } else {
                return Result.error("修改简历失败");
            }
        } catch (Exception e) {
            return Result.error("修改简历异常：" + e.getMessage());
        }
    }

    /**
     * 删除简历
     */
    @DeleteMapping("/delete/{resumeId}")
    public Result<?> deleteResume(@PathVariable Long resumeId) {
        try {
            boolean success = resumeService.deleteResume(resumeId);
            if (success) {
                return Result.success("删除简历成功");
            } else {
                return Result.error("删除简历失败");
            }
        } catch (Exception e) {
            return Result.error("删除简历异常：" + e.getMessage());
        }
    }

    /**
     * 统计用户简历数量
     */
    @GetMapping("/count/{userId}")
    public Result<Integer> countResume(@PathVariable Long userId) {
        try {
            int count = resumeService.countResumeByUserId(userId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计简历数量失败：" + e.getMessage());
        }
    }
}