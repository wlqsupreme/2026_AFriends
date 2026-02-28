package Afriends_v3.manager.cache;

import Afriends_v3.core.constant.CacheConsts;
import Afriends_v3.entity.novel.UserInfo;
import Afriends_v3.mapper.novel.UserInfoMapper;
import Afriends_v3.dto.UserInfoDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 用户信息 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {

    private final UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
        value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDto getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return UserInfoDto.builder()
            .id(userInfo.getId())
            .status(userInfo.getStatus()).build();
    }


}
