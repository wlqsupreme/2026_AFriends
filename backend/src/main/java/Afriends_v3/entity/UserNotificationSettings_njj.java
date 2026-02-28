package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户通知设置实体类
 * 对应表: v3_user_notification_settings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_notification_settings")
public class UserNotificationSettings_njj {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 聊天消息通知开关
     */
    private Byte chatNotification;

    /**
     * 点赞和收藏通知开关
     */
    private Byte likeFavoriteNotification;

    /**
     * 评论通知开关
     */
    private Byte commentNotification;

    /**
     * 提及通知开关
     */
    private Byte mentionNotification;

    /**
     * 内容推荐通知开关
     */
    private Byte contentRecommendNotification;

    /**
     * 用户推荐通知开关
     */
    private Byte userRecommendNotification;

    /**
     * 通知显示模式 0-'仅显示接受信息' 1-'仅显示用户名' 2-'完全显示'
     */
    private Byte notificationDisplayMode;

    /**
     * 创建时间
     */
    private Timestamp createdAt;

    /**
     * 更新时间
     */
    private Timestamp updatedAt;
}