package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户设置关系实体类
 * 对应表: v2_user_setting_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_setting_relation")
public class UserSettingRelation_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long settingId;
    private String valueText;
    private Timestamp createTime;
    private Timestamp updateTime;
}
