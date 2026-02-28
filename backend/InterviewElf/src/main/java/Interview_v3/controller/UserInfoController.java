package Interview_v3.controller;

import Interview_v3.common.Result;
import Interview_v3.entity.UserInfo;
import Interview_v3.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理接口控制器（含登录/注册）
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 初始化用户缓存
     */
    @GetMapping("/init-cache")
    public Result<?> initCache() {
        try {
            userInfoService.initCache();
            return Result.success("用户缓存初始化成功");
        } catch (Exception e) {
            return Result.error("缓存初始化失败：" + e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> loginParam) {
        try {
            String phone = loginParam.get("phone");
            String password = loginParam.get("password");

            // 参数校验
            if (phone == null || password == null) {
                return Result.error(400, "手机号或密码不能为空");
            }

            UserInfo user = userInfoService.login(phone, password);
            if (user == null) {
                return Result.error(401, "手机号未注册、密码错误或账号已禁用");
            }

            // 登录成功，返回用户信息（实际项目建议返回token，此处简化）
            Map<String, Object> result = new HashMap<>();
            result.put("userId", user.getId());
            result.put("phone", user.getPhone());
            result.put("status", user.getStatus());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("登录异常：" + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserInfo userInfo) {
        try {
            // 参数校验
            if (userInfo.getPhone() == null || userInfo.getPassword() == null) {
                return Result.error(400, "手机号或密码不能为空");
            }

            boolean success = userInfoService.register(userInfo);
            if (success) {
                return Result.success("注册成功");
            } else {
                return Result.error(400, "手机号已注册");
            }
        } catch (Exception e) {
            return Result.error("注册异常：" + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/update-pwd")
    public Result<?> updatePassword(@RequestBody Map<String, Object> pwdParam) {
        try {
            Long userId = Long.valueOf(pwdParam.get("userId").toString());
            String oldPwd = pwdParam.get("oldPwd").toString();
            String newPwd = pwdParam.get("newPwd").toString();

            boolean success = userInfoService.updatePassword(userId, oldPwd, newPwd);
            if (success) {
                return Result.success("修改密码成功");
            } else {
                return Result.error(400, "旧密码错误或用户不存在");
            }
        } catch (Exception e) {
            return Result.error("修改密码异常：" + e.getMessage());
        }
    }

    /**
     * 禁用/启用账号
     */
    @PutMapping("/update-status/{userId}/{status}")
    public Result<?> updateUserStatus(
            @PathVariable Long userId,
            @PathVariable Byte status
    ) {
        try {
            boolean success = userInfoService.updateUserStatus(userId, status);
            if (success) {
                String msg = status == 1 ? "启用账号成功" : "禁用账号成功";
                return Result.success(msg);
            } else {
                return Result.error("操作失败（用户不存在）");
            }
        } catch (Exception e) {
            return Result.error("修改账号状态异常：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/{userId}")
    public Result<UserInfo> getUserInfoById(@PathVariable Long userId) {
        try {
            UserInfo user = userInfoService.getUserInfoById(userId);
            if (user == null) {
                return Result.error(400, "用户不存在");
            }
            // 脱敏：隐藏密码
            user.setPassword("******");
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("查询用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 检查账号状态
     */
    @GetMapping("/check-status/{userId}")
    public Result<Boolean> checkUserStatus(@PathVariable Long userId) {
        try {
            boolean normal = userInfoService.isUserStatusNormal(userId);
            return Result.success(normal);
        } catch (Exception e) {
            return Result.error("检查账号状态失败：" + e.getMessage());
        }
    }
}