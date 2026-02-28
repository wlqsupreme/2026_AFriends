package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 购买记录实体类
 * 对应表: v3_purchase_record
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_purchase_record")
public class PurchaseRecord_njj {
    @TableId(value = "record_id", type = IdType.INPUT)
    private Long recordId;
    private Long userId;
    private String itemType;
    private Long itemId;
    private String itemName;
    private Integer coinsSpent;
    private Integer coinsBalance;
    private String purchasePath;
    private Timestamp createdAt;
}
