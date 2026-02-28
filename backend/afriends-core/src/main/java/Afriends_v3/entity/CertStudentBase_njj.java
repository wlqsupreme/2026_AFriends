package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 学生证认证基础实体类
 * 对应表: v3_cert_student_base
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_cert_student_base")
public class CertStudentBase_njj {
    @TableId(value = "school_id", type = IdType.INPUT)
    private Long schoolId;
    private String region;
    private String schoolName;
}
