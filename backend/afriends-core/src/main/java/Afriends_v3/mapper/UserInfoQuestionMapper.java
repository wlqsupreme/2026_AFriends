package Afriends_v3.mapper;

import Afriends_v3.entity.UserInfoQuestion_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户信息问题Mapper接口
 */
@Mapper
public interface UserInfoQuestionMapper extends BaseMapper<UserInfoQuestion_njj> {

    /**
     * 查询所有用户信息问题（原生SQL）
     */
    @Select("SELECT * FROM v2_user_info_question")
    List<UserInfoQuestion_njj> selectAllRecords();
}
