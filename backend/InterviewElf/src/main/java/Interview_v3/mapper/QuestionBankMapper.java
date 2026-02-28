package Interview_v3.mapper;

import Interview_v3.entity.QuestionBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.math.BigDecimal;

/**
 * 题库信息Mapper接口
 * 对应实体: QuestionBank
 * 对应表: question_bank
 */
@Mapper
public interface QuestionBankMapper extends BaseMapper<QuestionBank> {
    // 继承BaseMapper，自动获得MyBatis-Plus提供的CRUD基础操作（增删改查、分页等）

    /**
     * 统计题库总记录数（含未删除的）
     */
    @Select("SELECT COUNT(*) FROM question_bank WHERE is_deleted = 0")
    int countAllRecords();

    /**
     * 查询前5条题库数据（用于首页推荐）
     */
    @Select("SELECT * FROM question_bank WHERE is_deleted = 0 LIMIT 5")
    List<QuestionBank> selectFirstFive();

    /**
     * 查询所有未删除的题库数据
     */
    @Select("SELECT * FROM question_bank WHERE is_deleted = 0")
    List<QuestionBank> selectAllRecords();

    /**
     * 根据标签筛选题库（自定义参数查询）
     * @param tag 题库标签（如"高频"、"算法"）
     * @return 符合标签的题库列表
     */
    @Select("SELECT * FROM question_bank WHERE is_deleted = 0 AND tag = #{tag}")
    List<QuestionBank> selectByTag(@Param("tag") String tag);

    /**
     * 查询价格低于指定金额的题库（用于价格筛选）
     * @param maxPrice 最高价格
     * @return 符合价格条件的题库列表
     */
    @Select("SELECT * FROM question_bank WHERE is_deleted = 0 AND current_price < #{maxPrice}")
    List<QuestionBank> selectByPriceLessThan(@Param("maxPrice") BigDecimal maxPrice);

    /**
     * 根据分类ID查询题库
     * @param categoryId 分类ID
     * @return 该分类下的所有题库
     */
    @Select("SELECT * FROM question_bank WHERE is_deleted = 0 AND category_id = #{categoryId}")
    List<QuestionBank> selectByCategoryId(@Param("categoryId") Long categoryId);
}