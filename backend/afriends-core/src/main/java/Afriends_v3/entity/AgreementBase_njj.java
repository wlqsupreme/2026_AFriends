package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 协议基础实体类
 * 对应表: v3_agreement_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_agreement_base")
public class AgreementBase_njj {
    @TableId(value = "file_id", type = IdType.INPUT)
    private Long fileId;
    private String title;
    private String content;
    private String version;
    private Timestamp createdAt;
    private Long tempFileIdReindex;
}
