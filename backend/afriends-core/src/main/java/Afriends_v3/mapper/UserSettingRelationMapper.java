package Afriends_v3.mapper;

import Afriends_v3.entity.UserSettingRelation_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户设置关系Mapper接口
 */
@Mapper
public interface UserSettingRelationMapper extends BaseMapper<UserSettingRelation_njj> {

    /**
     * 查询所有用户设置关系（原生SQL）
     */
    @Select("SELECT * FROM v2_user_setting_relation")
    List<UserSettingRelation_njj> selectAllRecords();
}
