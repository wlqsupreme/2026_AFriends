package Afriends_v3.mapper;

import Afriends_v3.entity.ReportRecord_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 举报记录Mapper接口 (njj版本)
 */
@Mapper
public interface ReportRecordNjjMapper extends BaseMapper<ReportRecord_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_report_record")
    int countAllRecords();

    @Select("SELECT * FROM v3_report_record LIMIT 5")
    java.util.List<ReportRecord_njj> selectFirstFive();

    @Select("SELECT * FROM v3_report_record")
    java.util.List<ReportRecord_njj> selectAllRecords();
}
