package Afriends_v3.mapper.novel;

//import io.github.xxyopen.novel.dao.entity.BookComment;
import Afriends_v3.entity.novel.BookComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 小说评论 Mapper 接口
 * </p>
 *
 * @author xiongxiaoyang
 * @date 2022/05/11
 */
@Mapper
public interface BookCommentMapper extends BaseMapper<BookComment> {

}
