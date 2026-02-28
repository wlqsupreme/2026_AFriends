package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 小说帖子基础实体类
 * 对应表: v2_novelpost_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_novelpost_base")
public class NovelpostBase_wlq {
    @TableId(value = "novel_id", type = IdType.INPUT)
    private Long novelId;
    private Long userId;
    private String novelTitle;
    private String novelDescription;
    private String novelText;
    private String novelCoverUrl;
    private Long authorId;
    private String authorName;
    private Float novelScore;
    private Integer readingCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Integer viewCount;
    private Byte novelStatus;
    private Byte isVisible;
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String softTags;
    private String hardTags;
    private String tagList;
}
