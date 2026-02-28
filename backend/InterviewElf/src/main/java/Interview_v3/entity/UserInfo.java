package Interview_v3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户基础信息表实体类
 * 对应表: user_info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
public class UserInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 用户ID（主键）
    private String phone; // 手机号（登录账号）
    private String password; // 加密后的密码
    private Byte status; // 账号状态：1-正常 2-禁用
    private LocalDateTime createTime; // 注册时间
    private LocalDateTime updateTime; // 更新时间
}