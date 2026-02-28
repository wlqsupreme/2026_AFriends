package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户信息问答实体类
 * 对应表: v2_user_info_question
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_info_question")
public class UserInfoQuestion_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String question;
    private String softTags;
    private String hardTags;
    private String featureVector;
}
