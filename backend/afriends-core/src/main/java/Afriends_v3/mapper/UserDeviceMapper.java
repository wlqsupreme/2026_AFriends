package Afriends_v3.mapper;

import Afriends_v3.entity.UserDevice_njj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户设备Mapper接口
 */
@Mapper
public interface UserDeviceMapper extends BaseMapper<UserDevice_njj> {

    /**
     * 查询所有用户设备（原生SQL）
     */
    @Select("SELECT * FROM v3_user_device")
    List<UserDevice_njj> selectAllRecords();

    /**
     * 根据用户ID查询设备列表
     */
    @Select("SELECT * FROM v3_user_device WHERE user_id = #{userId} ORDER BY last_login_time DESC")
    List<UserDevice_njj> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据设备标识符查询设备
     */
    @Select("SELECT * FROM v3_user_device WHERE device_identifier = #{deviceIdentifier} AND user_id = #{userId}")
    UserDevice_njj selectByDeviceIdentifier(@Param("userId") Long userId, @Param("deviceIdentifier") String deviceIdentifier);

    /**
     * 删除设备（物理删除）
     */
    @Delete("DELETE FROM v3_user_device WHERE id = #{id} AND user_id = #{userId}")
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 批量删除设备
     */
    @Delete("<script>DELETE FROM v3_user_device WHERE user_id = #{userId} AND id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    int batchDeleteByIds(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    /**
     * 将用户的其他设备标记为非当前设备
     */
    @Update("UPDATE v3_user_device SET is_current = 0 WHERE user_id = #{userId} AND id != #{deviceId}")
    int setOtherDevicesNotCurrent(@Param("userId") Long userId, @Param("deviceId") Long deviceId);

    /**
     * 将用户的所有设备标记为非当前设备（用于新设备登录时）
     */
    @Update("UPDATE v3_user_device SET is_current = 0 WHERE user_id = #{userId}")
    int setAllDevicesNotCurrent(@Param("userId") Long userId);

    /**
     * 查询最大ID（用于生成新设备ID）
     */
    @Select("SELECT MAX(id) FROM v3_user_device")
    Long selectMaxId();
}