package Afriends_v3.service.novel.impl;

import Afriends_v3.core.common.resp.RestResp;
import Afriends_v3.dto.resp.HomeBookRespDto;
import Afriends_v3.dto.resp.HomeFriendLinkRespDto;
import Afriends_v3.manager.cache.FriendLinkCacheManager;
import Afriends_v3.manager.cache.HomeBookCacheManager;
import Afriends_v3.service.novel.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页模块 服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/13
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeBookCacheManager homeBookCacheManager;

    private final FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return RestResp.ok(homeBookCacheManager.listHomeBooks());
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return RestResp.ok(friendLinkCacheManager.listFriendLinks());
    }
}
