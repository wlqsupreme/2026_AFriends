package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 聊天设置基础实体类
 * 对应表: v3_chat_settings_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_chat_settings_base")
public class ChatSettingsBase_njj {
    @TableId(value = "chat_setting_id", type = IdType.INPUT)
    private Long chatSettingId;
    private String chatSettingName;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
