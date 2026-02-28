package Afriends_v3.manager.cache;

import Afriends_v3.core.constant.CacheConsts;
import Afriends_v3.core.constant.DatabaseConsts;
import Afriends_v3.dto.resp.NewsInfoRespDto;
import Afriends_v3.entity.novel.NewsInfo;
import Afriends_v3.mapper.novel.NewsInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 新闻 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    private final NewsInfoMapper newsInfoMapper;

    /**
     * 最新新闻列表查询，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto> listLatestNews() {
        // 从新闻信息表中查询出最新发布的两条新闻
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
            .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        return newsInfoMapper.selectList(queryWrapper).stream().map(v -> NewsInfoRespDto.builder()
            .id(v.getId())
            .categoryId(v.getCategoryId())
            .categoryName(v.getCategoryName())
            .title(v.getTitle())
            .sourceName(v.getSourceName())
            .updateTime(v.getUpdateTime())
            .build()).toList();
    }

}
