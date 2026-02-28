package Afriends_v3.mapper;

import Afriends_v3.entity.FriendList_wlq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 好友列表Mapper接口
 */
@Mapper
public interface FriendListMapper extends BaseMapper<FriendList_wlq> {
    // 继承BaseMapper，提供基本的CRUD操作

    @Select("SELECT COUNT(*) FROM friend_list")
    int countAllRecords();

    @Select("SELECT * FROM friend_list LIMIT 5")
    java.util.List<FriendList_wlq> selectFirstFive();

    @Select("SELECT * FROM friend_list")
    java.util.List<FriendList_wlq> selectAllRecords();
}

