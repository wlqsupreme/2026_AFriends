package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 证书基础实体类
 * 对应表: v3_cert_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_cert_base")
public class CertBase_njj {
    @TableId(value = "post_id", type = IdType.INPUT)
    private Long postId;
    private String certName;
    private Long parentId;
    private String childName;
    private String childDesc;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
