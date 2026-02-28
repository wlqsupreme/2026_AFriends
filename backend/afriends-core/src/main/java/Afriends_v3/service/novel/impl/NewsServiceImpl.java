package Afriends_v3.service.novel.impl;

import Afriends_v3.core.common.resp.RestResp;
import Afriends_v3.core.constant.DatabaseConsts;
import Afriends_v3.dto.resp.NewsInfoRespDto;
import Afriends_v3.entity.novel.NewsContent;
import Afriends_v3.entity.novel.NewsInfo;
import Afriends_v3.manager.cache.NewsCacheManager;
import Afriends_v3.mapper.novel.NewsContentMapper;
import Afriends_v3.mapper.novel.NewsInfoMapper;
import Afriends_v3.service.novel.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 新闻模块 服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/14
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsCacheManager newsCacheManager;

    private final NewsInfoMapper newsInfoMapper;

    private final NewsContentMapper newsContentMapper;

    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return RestResp.ok(newsCacheManager.listLatestNews());
    }

    @Override
    public RestResp<NewsInfoRespDto> getNews(Long id) {
        NewsInfo newsInfo = newsInfoMapper.selectById(id);
        QueryWrapper<NewsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID, id)
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        NewsContent newsContent = newsContentMapper.selectOne(queryWrapper);
        return RestResp.ok(NewsInfoRespDto.builder()
            .title(newsInfo.getTitle())
            .sourceName(newsInfo.getSourceName())
            .updateTime(newsInfo.getUpdateTime())
            .content(newsContent.getContent())
            .build());
    }
}
