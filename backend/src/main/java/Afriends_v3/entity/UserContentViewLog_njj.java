package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户内容浏览日志实体类
 * 对应表: v3_user_content_view_log
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_content_view_log")
public class UserContentViewLog_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long contentId;
    private Byte contentType;
    private Byte viewType;
    private Integer durationSeconds;
    private Byte isInterested;
    private String source;
    private Timestamp createAt;
    private Timestamp updateAt;

}
