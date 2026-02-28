package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 用户图片内容基础实体类
 * 对应表: v2_imagepost_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_imagepost_base")
public class ImagePostBase_wlq {
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;
    private Long userId;
    private String contentText;
    private String imageUrls;
    private String softTags;
    private String hardTags;
    private String featureVector;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Double heatScore;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte isView;
    private Timestamp deleteAt;
    private Integer dislikeCount;
    private Integer viewCount;
}
