package Afriends_v3.mapper;

import Afriends_v3.entity.AchievementRecord_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 成就记录信息Mapper接口
 */
@Mapper
public interface AchievementRecordMapper extends BaseMapper<AchievementRecord_njj> {
    // 继承BaseMapper，提供基本的CRUD操作
}
