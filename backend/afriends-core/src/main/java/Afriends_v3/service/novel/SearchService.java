package Afriends_v3.service.novel;

import Afriends_v3.core.common.resp.PageRespDto;
import Afriends_v3.core.common.resp.RestResp;
import Afriends_v3.dto.req.BookSearchReqDto;
import Afriends_v3.dto.resp.BookInfoRespDto;

/**
 * 搜索 服务类
 *
 * @author xiongxiaoyang
 * @date 2022/5/23
 */
public interface SearchService {

    /**
     * 小说搜索
     *
     * @param condition 搜索条件
     * @return 搜索结果
     */
    RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition);

}
