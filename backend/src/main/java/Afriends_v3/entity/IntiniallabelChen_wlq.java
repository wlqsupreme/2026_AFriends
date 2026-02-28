package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 注册初始标签实体类
 * 对应表: v2_intiniallabel_chen
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_intiniallabel_chen")
public class IntiniallabelChen_wlq {
    @TableId(value = "v2_intiniallabel_chen_id", type = IdType.INPUT)
    private Long v2IntiniallabelChenId;
    private Long userId;
    private Long labelId;
    private Timestamp createdAt;
}