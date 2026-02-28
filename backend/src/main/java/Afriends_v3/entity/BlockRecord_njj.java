package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 屏蔽记录实体类
 * 对应表: v3_block_record
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_block_record")
public class BlockRecord_njj {
    @TableId(value = "record_id", type = IdType.INPUT)
    private Long recordId;
    private Long userId;
    private Long blockedUserId;
    private Timestamp blockedAt;
    private Timestamp unblockedAt;
    private Byte isBlocked;
}
