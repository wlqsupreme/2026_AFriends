package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户设备实体类
 * 对应表: v3_user_device
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_device")
public class UserDevice_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private String deviceName;
    private String deviceType;
    private String deviceModel;
    private String deviceIdentifier;
    private String loginLocation;
    private Timestamp lastLoginTime;
    private String loginIp;
    private Byte isCurrent;
}
