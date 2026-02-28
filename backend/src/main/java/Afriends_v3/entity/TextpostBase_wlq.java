package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 文字帖子基础实体类
 * 对应表: v2_textpost_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_textpost_base")
public class TextpostBase_wlq {
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;
    private Long userId;
    private String contentText;
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
    private Timestamp deletedAt;
    private Integer dislikeCount;
    private Integer viewCount;
}
