package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户AI响应实体类
 * 对应表: v2_user_ai_respond
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_ai_respond")
public class UserAiRespond_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long taskId;
    private String respondText;
    private String softTags;
    private String hardTags;
    private String featureVector;
}
