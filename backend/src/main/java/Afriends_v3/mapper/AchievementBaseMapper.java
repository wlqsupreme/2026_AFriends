package Afriends_v3.mapper;

import Afriends_v3.entity.AchievementBase_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 成就基础信息Mapper接口
 */
@Mapper
public interface AchievementBaseMapper extends BaseMapper<AchievementBase_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM v3_achievement_base")
    int countAllRecords();

    @Select("SELECT * FROM v3_achievement_base LIMIT 5")
    java.util.List<AchievementBase_njj> selectFirstFive();

    @Select("SELECT * FROM v3_achievement_base")
    java.util.List<AchievementBase_njj> selectAllRecords();
}
