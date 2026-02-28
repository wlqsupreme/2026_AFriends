package Afriends_v3.mapper.novel;

//import io.github.xxyopen.novel.dao.entity.UserInfo;
import Afriends_v3.entity.novel.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author xiongxiaoyang
 * @date 2022/05/11
 */
@Mapper
@Repository("novelUserInfoMapper")
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
