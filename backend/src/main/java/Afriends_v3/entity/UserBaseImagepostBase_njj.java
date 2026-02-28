package Afriends_v3.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
/**
 * 用户与图片帖子的关系实体类
 * 对应表: v3_user_base_imagepost_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_base_imagepost_base")
public class UserBaseImagepostBase_njj {

    @TableId(value = "rel_id", type = IdType.INPUT)
    private Long relId;
    private Long userId;
    private Long imagepostId;
    private Byte behaviorType;
    @TableLogic
    private Byte isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
