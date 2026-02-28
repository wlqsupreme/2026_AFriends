package Afriends_v3.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 充值记录实体类
 * 对应表: v2_recharge
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_recharge")
public class Recharge_wlq {
    @TableId(value = "recharge_id", type = IdType.INPUT)
    private Long rechargeId;
    private Long userId;
    private BigDecimal amount;
    private Integer points;
    private BigDecimal moneyBalance;
    private Timestamp createdAt;
}
