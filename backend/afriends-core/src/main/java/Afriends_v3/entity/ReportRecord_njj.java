package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 举报记录实体类
 * 对应表: v3_report_record
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_report_record")
public class ReportRecord_njj {
    @TableId(value = "report_id", type = IdType.INPUT)
    private Long reportId;
    private Long userId;
    private Long reportedUserId;
    private Long contentId;
    private String contentType;
    private Long categoryId;
    private String description;
    private String evidenceImg;
    private Byte status;
    private String result;
    private Timestamp createdAt;
    private Timestamp processedAt;
}
