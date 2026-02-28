package Afriends_v3.mapper;

import Afriends_v3.entity.NovelChapterInfo_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 小说章节信息Mapper接口
 */
@Mapper
public interface NovelChapterInfoMapper extends BaseMapper<NovelChapterInfo_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_novel_chapter_info")
    int countAllRecords();

    @Select("SELECT * FROM v2_novel_chapter_info LIMIT 5")
    java.util.List<NovelChapterInfo_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_novel_chapter_info")
    java.util.List<NovelChapterInfo_wlq> selectAllRecords();
}
