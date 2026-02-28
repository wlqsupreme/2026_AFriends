package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.Collect;
import Interview_v3.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏管理接口控制器
 */
@RestController
@RequestMapping("/api/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 初始化收藏缓存
     */
    @GetMapping("/init-cache")
    public Result<?> initCache() {
        try {
            collectService.initCache();
            return Result.success("收藏缓存初始化成功");
        } catch (Exception e) {
            return Result.error("缓存初始化失败：" + e.getMessage());
        }
    }

    /**
     * 检查是否已收藏
     */
    @GetMapping("/check/{userId}/{type}/{targetId}")
    public Result<Boolean> checkCollected(
            @PathVariable Long userId,
            @PathVariable Byte type,
            @PathVariable Long targetId
    ) {
        try {
            boolean result = collectService.isCollected(userId, type, targetId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("检查收藏状态失败：" + e.getMessage());
        }
    }

    /**
     * 收藏操作
     */
    @PostMapping("/add")
    public Result<?> collect(@RequestBody Collect collect) {
        try {
            boolean success = collectService.collect(collect);
            if (success) {
                return Result.success("收藏成功");
            } else {
                return Result.error(400, "已收藏该内容，无需重复收藏");
            }
        } catch (Exception e) {
            return Result.error("收藏异常：" + e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/cancel/{userId}/{type}/{targetId}")
    public Result<?> cancelCollect(
            @PathVariable Long userId,
            @PathVariable Byte type,
            @PathVariable Long targetId
    ) {
        try {
            boolean success = collectService.cancelCollect(userId, type, targetId);
            if (success) {
                return Result.success("取消收藏成功");
            } else {
                return Result.error("取消收藏失败（未收藏该内容）");
            }
        } catch (Exception e) {
            return Result.error("取消收藏异常：" + e.getMessage());
        }
    }

    /**
     * 查询用户的收藏列表（按类型）
     */
    @GetMapping("/list/{userId}/{type}")
    public Result<List<Collect>> getCollectByType(
            @PathVariable Long userId,
            @PathVariable Byte type
    ) {
        try {
            List<Collect> list = collectService.getCollectByUserIdAndType(userId, type);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("查询收藏列表失败：" + e.getMessage());
        }
    }

    /**
     * 置顶/取消置顶收藏
     */
    @PutMapping("/top/{collectId}/{isTop}")
    public Result<?> topCollect(
            @PathVariable Long collectId,
            @PathVariable Byte isTop
    ) {
        try {
            boolean success = collectService.topCollect(collectId, isTop);
            if (success) {
                String msg = isTop == 1 ? "置顶收藏成功" : "取消置顶收藏成功";
                return Result.success(msg);
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            return Result.error("置顶操作异常：" + e.getMessage());
        }
    }

    /**
     * 统计用户收藏总数
     */
    @GetMapping("/count/{userId}")
    public Result<Integer> countCollect(@PathVariable Long userId) {
        try {
            int count = collectService.countCollectByUserId(userId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("统计收藏数量失败：" + e.getMessage());
        }
    }
}