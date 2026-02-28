package Interview_v3.mapper;

import Interview_v3.entity.QuestionBankCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题库分类Mapper接口
 */
@Mapper
public interface QuestionBankCategoryMapper extends BaseMapper<QuestionBankCategory> {
    /**
     * 查询所有未删除的分类（按排序权重升序）
     */
    @Select("SELECT * FROM question_bank_category WHERE is_deleted = 0 ORDER BY sort ASC")
    List<QuestionBankCategory> selectAllCategory();

    /**
     * 根据分类名称查询
     */
    @Select("SELECT * FROM question_bank_category WHERE category_name = #{name} AND is_deleted = 0")
    QuestionBankCategory selectByCategoryName(@Param("name") String name);

    /**
     * 统计分类下的题库数量
     */
    @Select("SELECT COUNT(*) FROM question_bank WHERE category_id = #{categoryId} AND is_deleted = 0")
    int countBankByCategoryId(@Param("categoryId") Long categoryId);
}