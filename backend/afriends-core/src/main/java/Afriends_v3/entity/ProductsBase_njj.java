package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 产品基础实体类
 * 对应表: v3_products_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_products_base")
public class ProductsBase_njj {
    @TableId(value = "coin_recharge_id", type = IdType.INPUT)
    private Long coinRechargeId;
    private Long coinNum;
    private Long expNum;
    private Long coinExtraNum;
    private Long expExtraNum;
    private Byte extraNum;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
