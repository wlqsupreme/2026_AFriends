package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 好友列表信息列表
 * 对应实体: FriendList_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class FriendList_list_wlq extends EntityList<FriendList_wlq> {

    // 内存存储
    private static final Map<Long, FriendList_wlq> friendListCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalFriendList = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<FriendList_wlq> allFriendList) {
        try {
            System.out.println("开始直接加载好友列表数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (FriendList_wlq friend : allFriendList) {
                System.out.println("处理好友数据: ID=" + friend.getId() + ", 名称=" + friend.getFriendName());
                friendListCache.put(friend.getId(), friend);
            }

            // 更新统计信息
            totalFriendList = friendListCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("好友列表数据直接加载完成！总数: " + totalFriendList);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载好友列表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有好友列表数据到内存
     */
    public static void loadFromDatabase(BaseMapper<FriendList_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载好友列表数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有好友列表数据
            System.out.println("正在执行数据库查询...");
            List<FriendList_wlq> allFriendList = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allFriendList.size() + " 条记录");

            // 存储到内存缓存
            for (FriendList_wlq friend : allFriendList) {
                System.out.println("处理好友数据: ID=" + friend.getId() + ", 名称=" + friend.getFriendName());
                friendListCache.put(friend.getId(), friend);
            }

            // 更新统计信息
            totalFriendList = friendListCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("好友列表数据加载完成！总数: " + totalFriendList);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载好友列表数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有好友列表数据（从内存）
     */
    public static List<FriendList_wlq> getAllFriendList() {
        return new ArrayList<>(friendListCache.values());
    }

    /**
     * 根据好友ID获取好友列表数据
     */
    public static FriendList_wlq getFriendListById(Long id) {
        return friendListCache.get(id);
    }

    /**
     * 根据好友名称搜索好友列表数据
     */
    public static List<FriendList_wlq> searchFriendListByName(String name) {
        List<FriendList_wlq> result = new ArrayList<>();
        for (FriendList_wlq friend : friendListCache.values()) {
            if (friend.getFriendName() != null && friend.getFriendName().contains(name)) {
                result.add(friend);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFriendList", totalFriendList);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        friendListCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<FriendList_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}

