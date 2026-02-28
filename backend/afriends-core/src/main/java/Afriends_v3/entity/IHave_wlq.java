package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我拥有物品实体类
 * 对应表: v2_i_have
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_have")
public class IHave_wlq {
    @TableId(value = "have_id", type = IdType.INPUT)
    private Long haveId;
    private Long userId;
    private String title;
    private String description;
    private String imageUrls;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String softTags;
    private String hardTags;
    private String itemType;
    private String itemStatus;
    private String priceInfo;
    private String exchangeWillingness;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer dislikeCount;
    private Double heatScore;
    private Integer viewCount;
    private Byte isView;
    private Timestamp deletedAt;
    private String embedding;
}
