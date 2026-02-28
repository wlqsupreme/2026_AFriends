package Afriends_v3.entity;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 小说章节信息实体类
 * 对应表: v2_novel_chapter_info
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("v2_novel_chapter_info")
public class NovelChapterInfo_wlq {
    @TableId(value = "chapter_id", type = IdType.INPUT)
    private Long chapterId;
    private Long novelId;
    private Long chapterIndex;
    private String chapterTitle;
    private String chapterContent;
    private Byte isDeleted;
    private Timestamp deletedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
