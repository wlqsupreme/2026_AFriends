package Interview_v3.service;

import Interview_v3.entity.Collect;
import Interview_v3.entity.CollectList;
import Interview_v3.mapper.CollectMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏服务层
 */
@Service
public class CollectService extends ServiceImpl<CollectMapper, Collect> {

    @Autowired
    private CollectMapper collectMapper;

    /**
     * 初始化收藏缓存
     */
    public void initCache() {
        CollectList.loadFromDatabase(collectMapper);
    }

    /**
     * 刷新收藏缓存
     */
    public void refreshCache() {
        CollectList.forceRefresh(collectMapper);
    }

    /**
     * 检查是否已收藏
     */
    public boolean isCollected(Long userId, Byte type, Long targetId) {
        // 优先从缓存查询
        boolean cacheResult = CollectList.isCollected(userId, type, targetId);
        if (cacheResult) {
            return true;
        }
        // 缓存未命中，查数据库
        int count = collectMapper.checkCollected(userId, type, targetId);
        return count > 0;
    }

    /**
     * 收藏操作
     */
    public boolean collect(Collect collect) {
        // 1. 检查是否已收藏
        if (isCollected(collect.getUserId(), collect.getCollectType(), collect.getTargetId())) {
            return false; // 已收藏，返回失败
        }
        // 2. 补充默认值
        if (collect.getIsTop() == null) {
            collect.setIsTop((byte) 0);
        }
        collect.setCollectTime(LocalDateTime.now());
        collect.setUpdateTime(LocalDateTime.now());
        collect.setIsDeleted((byte) 0);
        // 3. 新增收藏
        boolean save = this.save(collect);
        if (save) {
            refreshCache();
        }
        return save;
    }

    /**
     * 取消收藏
     */
    public boolean cancelCollect(Long userId, Byte type, Long targetId) {
        int affectRows = collectMapper.cancelCollect(userId, type, targetId);
        if (affectRows > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    /**
     * 查询用户的收藏列表（按类型筛选）
     */
    public List<Collect> getCollectByUserIdAndType(Long userId, Byte type) {
        return CollectList.getCollectByUserIdAndType(userId, type);
    }

    /**
     * 根据收藏ID查询
     */
    public Collect getCollectById(Long collectId) {
        return CollectList.getCollectById(collectId);
    }

    /**
     * 置顶/取消置顶收藏
     */
    public boolean topCollect(Long collectId, Byte isTop) {
        Collect collect = new Collect();
        collect.setId(collectId);
        collect.setIsTop(isTop);
        collect.setUpdateTime(LocalDateTime.now());
        boolean update = this.updateById(collect);
        if (update) {
            refreshCache();
        }
        return update;
    }

    /**
     * 统计用户收藏总数
     */
    public int countCollectByUserId(Long userId) {
        return collectMapper.countByUserId(userId);
    }

    /**
     * 获取收藏统计信息
     */
    public Object getStatistics() {
        return CollectList.getStatistics();
    }
}