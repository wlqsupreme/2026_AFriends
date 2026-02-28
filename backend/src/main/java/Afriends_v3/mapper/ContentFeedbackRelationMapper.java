package Afriends_v3.mapper;

import Afriends_v3.entity.ContentFeedbackRelation_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户不喜欢内容关系Mapper接口
 */
@Mapper
public interface ContentFeedbackRelationMapper extends BaseMapper<ContentFeedbackRelation_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v2_content_feedback_relation")
    int countAllRecords();

    @Select("SELECT * FROM v2_content_feedback_relation LIMIT 5")
    java.util.List<ContentFeedbackRelation_wlq> selectFirstFive();

    @Select("SELECT * FROM v2_content_feedback_relation")
    java.util.List<ContentFeedbackRelation_wlq> selectAllRecords();
}

