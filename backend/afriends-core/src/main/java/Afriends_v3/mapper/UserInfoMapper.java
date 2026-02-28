package Afriends_v3.mapper;

import Afriends_v3.entity.UserInfo_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户信息Mapper接口
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo_njj> {
    // 继承BaseMapper，提供基本的CRUD操作

    /**
     * 查询所有用户记录（全量查询）
     * @return 所有用户的列表
     */
    @Select("SELECT * FROM v2_user_info") // 假设表名为v2_user_info，排除已删除用户
    List<UserInfo_njj> selectAllRecords();

    @Update("UPDATE v2_user_info SET gold = #{newGold} WHERE user_id = #{userId}")
    int updateGold(Long userId, Long newGold);
}