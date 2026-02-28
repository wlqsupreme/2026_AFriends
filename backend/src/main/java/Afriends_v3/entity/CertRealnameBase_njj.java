package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 实名认证基础实体类
 * 对应表: v3_cert_realname_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_cert_realname_base")
public class CertRealnameBase_njj {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String idType;
    private String ruleDesc;
}
