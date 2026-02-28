package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户需求信息实体类
 * 对应表: v2_i_need
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_need")
public class INeed_wlq {
    @TableId(value = "need_id", type = IdType.INPUT)
    private Long needId;
    private Long userId;
    private String title;
    private String contentText;
    private String contentType;
    private String imageUrls;
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
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte isView;
    private Timestamp deletedAt;
}
