package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户小说关系实体类
 * 对应表: v2_user_novel_relation
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_novel_relation")
public class UserNovelRelation_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long userId;
    private Long novelId;
    private String userText;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer favoriteCount;
    private Timestamp createAt;
    private Timestamp updateAt;
    private Timestamp deletedAt;
    @TableLogic
    private Byte isView;
}
