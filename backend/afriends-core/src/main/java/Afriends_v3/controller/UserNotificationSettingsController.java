package Afriends_v3.controller;

import Afriends_v3.entity.UserNotificationSettings_njj;
import Afriends_v3.service.UserNotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户通知设置控制器
 */
@RestController
@RequestMapping("/api/notification-settings")
@CrossOrigin(origins = "*")
public class UserNotificationSettingsController {
    
    @Autowired
    private UserNotificationSettingsService userNotificationSettingsService;
    
    /**
     * 获取用户通知设置
     * @param userId 用户ID
     * @return 用户通知设置对象
     */
    @GetMapping("/{userId}")
    public UserNotificationSettings_njj getNotificationSettings(@PathVariable Long userId) {
        return userNotificationSettingsService.getUserNotificationSettings(userId);
    }
    
    /**
     * 更新用户通知设置
     * @param settings 用户通知设置对象
     * @return 更新后的用户通知设置对象
     */
    @PostMapping("/update")
    public UserNotificationSettings_njj updateNotificationSettings(@RequestBody UserNotificationSettings_njj settings) {
        return userNotificationSettingsService.updateUserNotificationSettings(settings);
    }
}