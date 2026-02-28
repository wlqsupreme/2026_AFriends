package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 证书工作实体类
 * 对应表: v3_cert_job_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_cert_job_base")
public class CertJobBase_njj {
    @TableId(value = "job_id", type = IdType.INPUT)
    private Long jobId;
    private Long parentId;
    private String jobType;
    private String jobName;
}
