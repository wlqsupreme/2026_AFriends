package Afriends_v3.mapper;

import Afriends_v3.entity.ReportRecord_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户举报记录Mapper接口
 */
@Mapper
public interface ReportRecordMapper extends BaseMapper<ReportRecord_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_report_record")
    int countAllRecords();

    @Select("SELECT * FROM v3_report_record LIMIT 5")
    java.util.List<ReportRecord_wlq> selectFirstFive();

    @Select("SELECT * FROM v3_report_record")
    java.util.List<ReportRecord_wlq> selectAllRecords();
}