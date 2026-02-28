package Afriends_v3.service;

import Afriends_v3.entity.UserFollowRelationship_zjx;
import Afriends_v3.entity.UserFollowRelationship_list_zjx;
import Afriends_v3.mapper.UserFollowRelationshipMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户关注服务类
 * 负责管理用户关注关系的数据库操作和内存缓存
 */
@Service
public class UserFollowService extends ServiceImpl<UserFollowRelationshipMapper, UserFollowRelationship_zjx> {

    // 在类中定义日志对象（类名替换为你的Service类名）
    private static final Logger log = LoggerFactory.getLogger(UserFollowService.class);
    /**
     * 加载所有关注关系到内存
     */
    public void loadFollowDataToMemory() {
        try {
            System.out.println("UserFollowService: 开始从数据库加载所有关注关系数据...");
            long startTime = System.currentTimeMillis();

            // 从数据库查询所有关注关系
            List<UserFollowRelationship_zjx> allFollows = this.list();
            System.out.println("UserFollowService: 数据库关注关系总数: " + allFollows.size());

            // 加载到内存缓存
            UserFollowRelationship_list_zjx.loadFromDatabaseDirectly(allFollows);

            long endTime = System.currentTimeMillis();
            System.out.println("UserFollowService: 关注关系数据加载到内存完成！总数: " + allFollows.size()
                    + "，耗时: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            System.err.println("UserFollowService: 加载关注关系数据失败: " + e.getMessage());
            e.printStackTrace();
            throw e; // 抛出异常，让控制器处理
        }
    }

    /**
     * 加载指定用户的关注关系到内存
     * @param userId 用户ID
     */
    public void loadFollowDataByUserId(Long userId) {
        try {
            System.out.println("UserFollowService: 开始加载用户[" + userId + "]的关注关系...");
            long startTime = System.currentTimeMillis();

            // 查询该用户的所有关注关系
            QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
            query.eq("user_id", userId);
            List<UserFollowRelationship_zjx> userFollows = this.list(query);

            // 加载到内存缓存
            UserFollowRelationship_list_zjx.loadFromDatabaseDirectly(userFollows);

            long endTime = System.currentTimeMillis();
            System.out.println("UserFollowService: 用户[" + userId + "]的关注关系加载完成！总数: "
                    + userFollows.size() + "，耗时: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            System.err.println("UserFollowService: 加载用户[" + userId + "]的关注关系失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 关注用户
     * @param userId 关注者ID
     * @param followedUserId 被关注者ID
     * @return 关注后的状态（true=已关注）
     */
    public boolean followUser(Long userId, Long followedUserId) {
        // 1. 检查是否已关注（有效状态：isActive=1）
        QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .eq("followed_user_id", followedUserId)
                .eq("is_active", 1)
                .eq("action","follow");
        log.info("开始执行关注逻辑：userId={}, followedUserId={}", userId, followedUserId); // 确认方法进入
        UserFollowRelationship_zjx existing = this.getOne(query);
        // 打印对象（默认调用toString()方法）
        log.info("查询到的关注记录：{}", existing);
        if (existing != null) {
            System.out.println("UserFollowService: 用户[" + userId + "]已关注[" + followedUserId + "]");
            return true; // 已关注，直接返回true
        }

        // 2. 生成新的关注关系
        UserFollowRelationship_zjx newFollow = new UserFollowRelationship_zjx();
        // 获取最大ID（实际项目建议用雪花算法）
        Long maxId = baseMapper.selectMaxId();
        newFollow.setFollowId(maxId == null ? 1 : maxId + 1);
        newFollow.setUserId(userId);
        newFollow.setFollowedUserId(followedUserId);
        newFollow.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newFollow.setUpdatedAt(newFollow.getCreatedAt());
        newFollow.setIsActive(1); // 1=有效关注
        newFollow.setAction("follow"); // 补充action字段（与接口动作一致）

        // 3. 保存到数据库
        boolean saveResult = this.save(newFollow);
        System.out.println("UserFollowService: 用户[" + newFollow + "]yao[" + saveResult + "]");
        if (saveResult) {
            // 4. 更新内存缓存
            UserFollowRelationship_list_zjx.addToCache(newFollow);
            System.out.println("UserFollowService: 用户[" + userId + "]成功关注[" + followedUserId + "]");
            return true;
        }

        System.err.println("UserFollowService: 用户[" + userId + "]关注[" + followedUserId + "]失败");
        return false;
    }

    /**
     * 取消关注用户
     * @param userId 关注者ID
     * @param followedUserId 被关注者ID
     * @return 取消后的状态（false=未关注）
     */
    public boolean unfollowUser(Long userId, Long followedUserId) {
        // 1. 查询有效关注关系
        QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .eq("followed_user_id", followedUserId)
                .eq("is_active", 1);

        UserFollowRelationship_zjx existing = this.getOne(query);
        if (existing == null) {
            System.out.println("UserFollowService: 用户[" + userId + "]未关注[" + followedUserId + "]");
            return false; // 未关注，返回false
        }

        // 2. 逻辑删除（更新为无效状态）
        existing.setIsActive(0); // 0=无效关注
        existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boolean updateResult = this.updateById(existing);

        if (updateResult) {
            // 3. 更新内存缓存
            UserFollowRelationship_list_zjx.updateInCache(existing);
            System.out.println("UserFollowService: 用户[" + userId + "]成功取消关注[" + followedUserId + "]");
            return false; // 取消后状态为未关注
        }

        System.err.println("UserFollowService: 用户[" + userId + "]取消关注[" + followedUserId + "]失败");
        return true;
    }

    /**
     * 检查是否已关注
     * @param userId 关注者ID
     * @param followedUserId 被关注者ID
     * @return true=已关注，false=未关注
     */
    public boolean isFollowing(Long userId, Long followedUserId) {
        // 优先从内存缓存查询
        if (UserFollowRelationship_list_zjx.isCacheLoaded()) {
            boolean isFollowing = UserFollowRelationship_list_zjx.isFollowing(userId, followedUserId);
            System.out.println("UserFollowService: 缓存查询 - 用户[" + userId + "]"
                    + (isFollowing ? "已关注" : "未关注") + "[" + followedUserId + "]");
            return isFollowing;
        }

        // 缓存未加载时从数据库查询
        QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .eq("followed_user_id", followedUserId)
                .eq("is_active", 1);

        boolean isFollowing = this.count(query) > 0;
        System.out.println("UserFollowService: 数据库查询 - 用户[" + userId + "]"
                + (isFollowing ? "已关注" : "未关注") + "[" + followedUserId + "]");
        return isFollowing;
    }

    /**
     * 获取用户的所有关注列表
     * @param userId 用户ID
     * @return 关注关系列表
     */
    public List<UserFollowRelationship_zjx> getUserFollows(Long userId) {
        // 从内存缓存获取
        if (UserFollowRelationship_list_zjx.isCacheLoaded()) {
            return UserFollowRelationship_list_zjx.getFollowRelationshipsByUserId(userId);
        }

        // 缓存未加载时从数据库查询
        QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
        query.eq("user_id", userId)
                .eq("is_active", 1);
        return this.list(query);
    }

    /**
     * 获取用户的所有粉丝列表
     * @param userId 用户ID
     * @return 粉丝关系列表
     */
    public List<UserFollowRelationship_zjx> getUserFans(Long userId) {
        // 从内存缓存获取
        if (UserFollowRelationship_list_zjx.isCacheLoaded()) {
            return UserFollowRelationship_list_zjx.getFollowRelationshipsByFollowedUserId(userId);
        }

        // 缓存未加载时从数据库查询
        QueryWrapper<UserFollowRelationship_zjx> query = new QueryWrapper<>();
        query.eq("followed_user_id", userId)
                .eq("is_active", 1);
        return this.list(query);
    }

    /**
     * 强制刷新内存缓存
     */
    public void forceRefreshMemory() {
        System.out.println("UserFollowService: 开始强制刷新关注关系缓存...");
        List<UserFollowRelationship_zjx> allFollows = this.list();
        UserFollowRelationship_list_zjx.loadFromDatabaseDirectly(allFollows);
        System.out.println("UserFollowService: 关注关系缓存刷新完成！总数: " + allFollows.size());
    }
}