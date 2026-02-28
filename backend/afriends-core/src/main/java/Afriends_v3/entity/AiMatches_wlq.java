package Afriends_v3.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI对话匹配结果实体类
 * 对应表: v2_ai_matches
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_ai_matches")
public class AiMatches_wlq {
    @TableId(value = "aimatches_id", type = IdType.INPUT)
    private Long aimatchesId;
    private Long requireId;
    private Long respondId;
    private Long requireUserId;
    private Long respondUserId;
    private String requireText;
    private String respondText;
    private Double similarity;
}

