package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 小说内容基础实体类
 * 对应表: v2_novel_content_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_novel_content_base")
public class NovelContentBase_wlq {
    @TableId(value = "novel_content_id", type = IdType.INPUT)
    private Long novelContentId;
    private Long userId;
    private String novelTitle;
    private String novelText;
    private String coverUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Integer viewCount;
    private Byte isVisible;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte isDeleted;
    private Timestamp deletedAt;
    private String softTags;
    private String hardTags;
    private String featureVector;
}
