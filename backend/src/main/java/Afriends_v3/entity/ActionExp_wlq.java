package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 动作经验值实体类
 * 对应表: v2_action_exp
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_action_exp")
public class ActionExp_wlq {
    @TableId(value = "action_id", type = IdType.INPUT)
    private Integer actionId;
    private String actionName;
    private Integer expValue;
}

