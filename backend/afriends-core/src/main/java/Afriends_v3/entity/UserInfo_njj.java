package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户信息实体类
 * 对应表: v2_user_info
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_user_info")
public class UserInfo_njj {
    public enum Gender {
        UNKNOWN, MALE, FEMALE
    }

    public enum UserKind {
        // 演员, 真人
        ACTOR("演员"), REAL("真人");

        private final String chineseName;

        UserKind(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getChineseName() {
            return chineseName;
        }

        public static UserKind fromChineseName(String chineseName) {
            for (UserKind kind : values()) {
                if (kind.chineseName.equals(chineseName)) {
                    return kind;
                }
            }
            return null;
        }
    }

    @TableId(value = "user_id", type = IdType.INPUT)
    private Long userId;
    private String realName;
    private Byte age;
    private Gender gender;
    private String username;
    private String profilePicUrl;
    private String appearance;
    private String identity;
    private String unit;
    private String personality;
    private String interests;
    private String location;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @TableField(typeHandler = Afriends_v3.handler.UserKindTypeHandler.class)
    private UserKind userKind;
    private Long gold;
    private Long diamond;
    private Long level;
    private String featureVector;
    private String softTags;
    private String hardTags;
    private Byte realNameVerified;
    private Byte jobVerified;
    private Byte studentVerified;
    private String bio;
    private String homepageBgUrl;
    @TableLogic
    private Integer deleted;
}
