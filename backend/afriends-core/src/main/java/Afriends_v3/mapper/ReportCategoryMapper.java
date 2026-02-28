package Afriends_v3.mapper;

import Afriends_v3.entity.ReportCategory_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 举报类别Mapper接口
 */
@Mapper
public interface ReportCategoryMapper extends BaseMapper<ReportCategory_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_report_category")
    int countAllRecords();

    @Select("SELECT * FROM v3_report_category")
    java.util.List<ReportCategory_njj> selectAllRecords();
}
