package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户AI需求实体类
 * 对应表: v2_user_ai_require
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_ai_require")
public class UserAiRequire_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long taskId;
    private String requireText;
    private String softTags;
    private String hardTags;
    private String featureVector;
}
