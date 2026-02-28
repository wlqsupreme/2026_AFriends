package Afriends_v3.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 礼物基表实体类
 * 对应表: v2_gift_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_gift_base")
public class GiftBase_wlq {
    @TableId(value = "gift_id", type = IdType.INPUT)
    private Long giftId;
    private Long picId;
    private String description;
    private BigDecimal price;
    private String picUrl;
}

