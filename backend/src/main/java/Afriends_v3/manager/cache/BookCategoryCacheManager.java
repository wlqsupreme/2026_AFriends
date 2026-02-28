package Afriends_v3.manager.cache;

import Afriends_v3.core.constant.CacheConsts;
import Afriends_v3.core.constant.DatabaseConsts;
import Afriends_v3.dto.resp.BookCategoryRespDto;
import Afriends_v3.entity.novel.BookCategory;
import Afriends_v3.mapper.novel.BookCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 小说分类 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class BookCategoryCacheManager {

    private final BookCategoryMapper bookCategoryMapper;

    /**
     * 根据作品方向查询小说分类列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.BOOK_CATEGORY_LIST_CACHE_NAME)
    public List<BookCategoryRespDto> listCategory(Integer workDirection) {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCategoryTable.COLUMN_WORK_DIRECTION, workDirection);
        return bookCategoryMapper.selectList(queryWrapper).stream().map(v ->
            BookCategoryRespDto.builder()
                .id(v.getId())
                .name(v.getName())
                .build()).toList();
    }

}
