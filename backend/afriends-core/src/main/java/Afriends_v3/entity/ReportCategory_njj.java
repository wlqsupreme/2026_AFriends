package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

/**
 * 举报类别实体类
 * 对应表: v3_report_category
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v3_report_category")
public class ReportCategory_njj {
    @TableId(value = "category_id", type = IdType.INPUT)
    private Long categoryId;
    private String categoryName;
    private String description;
    private Byte isEnabled;
    private Timestamp createdAt;
}
