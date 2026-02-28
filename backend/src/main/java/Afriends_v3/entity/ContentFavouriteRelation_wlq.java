package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 内容收藏关系实体类
 * 对应表: v2_content_favourite_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_content_favourite_relation")
public class ContentFavouriteRelation_wlq {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long contentId;
    private String folderName;
    private Byte isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Byte contentType;
}

