package Afriends_v3.mapper;

import Afriends_v3.entity.AimodelBaseInfo_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * AI模型基础信息Mapper接口
 */
@Mapper
public interface AimodelBaseInfoMapper extends BaseMapper<AimodelBaseInfo_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_aimodel_base_info")
    int countAllRecords();

    @Select("SELECT * FROM v3_aimodel_base_info ORDER BY created_at DESC, model_id ASC")
    java.util.List<AimodelBaseInfo_njj> selectAllRecords();

    @Select("SELECT model_id, model_name, model_desc, model_image_url, tone, question_weight, question_content, power, price, purchase_count, recommend_count, score, like_count, comment_count, collect_count, dislike_count, report_count, is_visible, created_at, updated_at, deleted_at, status, version, comment_detail, temp_model_id_reind, try_out_num FROM v3_aimodel_base_info WHERE model_id = #{modelId}")
    AimodelBaseInfo_njj selectById(@Param("modelId") Long modelId);
}
