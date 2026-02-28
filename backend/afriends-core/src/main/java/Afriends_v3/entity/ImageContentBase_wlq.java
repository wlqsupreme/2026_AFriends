package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 图片内容基础实体类
 * 对应表: v2_image_content_base_@niu
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_image_content_base_@niu")
public class ImageContentBase_wlq {
    @TableId(value = "image_content_id", type = IdType.INPUT)
    private Long imageContentId;
    private Long userId;
    private String contentText;
    private String coverUrl;
    private String imageUrls;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Integer viewCount;
    private Boolean isVisible;
    private Integer status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isDeleted;
    private Timestamp deletedAt;
    private String tags;
    private String featureVector;
    private Double heatScore;
}
