package Afriends_v3.service;

import Afriends_v3.entity.UserNotificationSettings_njj;
import Afriends_v3.mapper.UserNotificationSettingsMapper;
import Afriends_v3.entity.UserNotificationSettings_list_njj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户通知设置服务类
 */
@Service
public class UserNotificationSettingsService {
    
    @Autowired
    private UserNotificationSettingsMapper userNotificationSettingsMapper;
    
    /**
     * 获取用户通知设置（优先从缓存获取）
     * @param userId 用户ID
     * @return 用户通知设置对象
     */
    public UserNotificationSettings_njj getUserNotificationSettings(Long userId) {
        // 先从缓存获取
        UserNotificationSettings_njj settings = UserNotificationSettings_list_njj.getByUserId(userId);
        if (settings == null) {
            // 缓存中没有则从数据库获取
            settings = userNotificationSettingsMapper.getByUserId(userId);
            if (settings != null) {
                // 放入缓存
                UserNotificationSettings_list_njj.put(settings);
            } else {
                // 如果数据库也没有，则创建默认设置并保存到数据库
                settings = createDefaultSettings(userId);
                userNotificationSettingsMapper.insert(settings);
                // 放入缓存
                UserNotificationSettings_list_njj.put(settings);
            }
        }
        return settings;
    }
    
    /**
     * 更新用户通知设置
     * @param settings 用户通知设置对象
     * @return 更新后的用户通知设置对象
     */
    public UserNotificationSettings_njj updateUserNotificationSettings(UserNotificationSettings_njj settings) {
        UserNotificationSettings_njj existing = userNotificationSettingsMapper.getByUserId(settings.getUserId());
        if (existing == null) {
            // 新增
            userNotificationSettingsMapper.insert(settings);
        } else {
            // 更新
            settings.setId(existing.getId());
            userNotificationSettingsMapper.updateById(settings);
        }
        // 更新缓存
        UserNotificationSettings_list_njj.put(settings);
        return settings;
    }
    
    /**
     * 创建默认通知设置
     * @param userId 用户ID
     * @return 默认的用户通知设置对象
     */
    private UserNotificationSettings_njj createDefaultSettings(Long userId) {
        UserNotificationSettings_njj settings = new UserNotificationSettings_njj();
        settings.setUserId(userId);
        settings.setChatNotification((byte) 1); // 默认开启聊天通知
        settings.setLikeFavoriteNotification((byte) 0);
        settings.setCommentNotification((byte) 0);
        settings.setMentionNotification((byte) 0);
        settings.setContentRecommendNotification((byte) 0);
        settings.setUserRecommendNotification((byte) 0);
        settings.setNotificationDisplayMode((byte) 2); // 默认完全显示
        return settings;
    }
    
    /**
     * 判断是否应该发送某种类型的通知
     * @param userId 用户ID
     * @param notificationType 通知类型
     * @return 是否应该发送通知
     */
    public boolean shouldSendNotification(Long userId, String notificationType) {
        UserNotificationSettings_njj settings = getUserNotificationSettings(userId);
        
        switch (notificationType.toLowerCase()) {
            case "chat":
                return settings.getChatNotification() == 1;
            case "like":
            case "favorite":
                return settings.getLikeFavoriteNotification() == 1;
            case "comment":
                return settings.getCommentNotification() == 1;
            case "mention":
                return settings.getMentionNotification() == 1;
            case "content_recommend":
                return settings.getContentRecommendNotification() == 1;
            case "user_recommend":
                return settings.getUserRecommendNotification() == 1;
            default:
                return true; // 默认发送
        }
    }
    
    /**
     * 将数字模式转换为字符串表示
     * @param mode 数字模式
     * @return 字符串模式
     */
    public String convertDisplayModeToString(Byte mode) {
        if (mode == null) return "FULL";
        
        switch (mode) {
            case 0:
                return "MINIMAL";
            case 1:
                return "MEDIUM";
            case 2:
            default:
                return "FULL";
        }
    }
    
    /**
     * 将字符串模式转换为数字表示
     * @param mode 字符串模式
     * @return 数字模式
     */
    public Byte convertStringToDisplayMode(String mode) {
        if (mode == null) return 2;
        
        switch (mode.toUpperCase()) {
            case "MINIMAL":
                return 0;
            case "MEDIUM":
                return 1;
            case "FULL":
            default:
                return 2;
        }
    }
}