package Afriends_v3.mapper;

import Afriends_v3.entity.ProductsBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 产品基础信息Mapper接口
 */
@Mapper
public interface ProductsBaseMapper extends BaseMapper<ProductsBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_products_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_products_base")
    java.util.List<ProductsBase_njj> selectAllRecords();
}
