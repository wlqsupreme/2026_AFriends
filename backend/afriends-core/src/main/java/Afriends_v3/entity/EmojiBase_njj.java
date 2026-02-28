package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 表情基础实体类
 * 对应表: v3_emoji_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_emoji_base")
public class EmojiBase_njj {
    @TableId(value = "emoji_id", type = IdType.INPUT)
    private Long emojiId;
    private String emojiName;
    private String emojiImageUrl;
    private Byte isVisible;
    private Long userId;
}
