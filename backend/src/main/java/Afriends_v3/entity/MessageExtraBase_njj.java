package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 消息扩展基础实体类
 * 对应表: v3_message_extra_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_message_extra_base")
public class MessageExtraBase_njj {
    @TableId(value = "extra_message_id", type = IdType.INPUT)
    private Long extraMessageId;
    private String extraMessageName;
    private String extraMessageUrl;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
