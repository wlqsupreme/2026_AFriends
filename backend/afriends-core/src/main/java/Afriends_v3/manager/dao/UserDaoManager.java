package Afriends_v3.manager.dao;

import Afriends_v3.core.constant.DatabaseConsts;
import Afriends_v3.entity.novel.UserInfo;
import Afriends_v3.mapper.novel.UserInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 用户模块 DAO管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/20
 */
@Component
@RequiredArgsConstructor
public class UserDaoManager {

    private final UserInfoMapper userInfoMapper;

    /**
     * 根据用户ID集合批量查询用户信息列表
     *
     * @param userIds 需要查询的用户ID集合
     * @return 满足条件的用户信息列表
     */
    public List<UserInfo> listUsers(List<Long> userIds) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), userIds);
        return userInfoMapper.selectList(queryWrapper);
    }

}
