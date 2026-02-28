package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 我不喜欢内容基础实体类
 * 对应表: v2_i_dislike
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_i_dislike")
public class IDislike_wlq {
    @TableId(value = "dislike_id", type = IdType.INPUT)
    private Long dislikeId;
    private Long userId;
    private String dislikeText;
    private String dislikeType;
    private String imageUrls;
    private String softTags;
    private String hardTags;
    private String itemType;
    private String itemStatus;
    private Integer likeCount;
    private Integer commentCount;
    private Integer dislikeCount;
    private Double heatScore;
    private Integer viewCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer isView;
    private Timestamp deletedAt;
}

