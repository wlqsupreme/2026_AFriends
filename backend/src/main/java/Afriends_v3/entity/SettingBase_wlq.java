package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 设置基础实体类
 * 对应表: v2_setting_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_setting_base")
public class SettingBase_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String settingKey;
    private String settingName;
    private Long parentId;
    private Byte isActive;
    private Byte isSelectable;
    private String dataType;
    private String allowedValues;
    private String defaultValue;
    private String description;
    private Integer sortOrder;
    private Timestamp createTime;
    private Timestamp updateTime;
}
