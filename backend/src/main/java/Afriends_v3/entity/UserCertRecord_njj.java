package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 用户证书记录实体类
 * 对应表: v3_user_cert_record
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_user_cert_record")
public class UserCertRecord_njj {
    public enum CertType {
        REALNAME,
        STUDENT,
        JOB
    }

    @TableId(value = "record_id", type = IdType.INPUT)
    private Long recordId;
    private Long userId;
    private CertType certType;
    private String certInfo;
    private Byte status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
