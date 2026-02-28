package Afriends_v3.service;

import Afriends_v3.entity.UserInfo_njj;
import Afriends_v3.entity.UserInfo_list_njj;
import Afriends_v3.mapper.UserInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户信息服务
 * 负责管理用户数据的数据库操作和内存缓存
 */
@Service
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo_njj> {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 加载数据到内存（限制数量）
     */
    public void loadDataToMemory(int limit) {
        try {
            System.out.println("UserInfoService: 开始从数据库加载用户数据（限制" + limit + "条）...");
            long startTime = System.currentTimeMillis();

            // 先测试数据库连接
            long count = this.count();
            System.out.println("UserInfoService: 数据库用户总数: " + count);

            if (count == 0) {
                System.out.println("UserInfoService: 数据库中没有用户数据");
                return;
            }

            UserInfo_list_njj.loadFromDatabaseWithLimit(this, limit);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println(
                    "UserInfoService: 用户数据加载到内存完成！实际加载: " + Math.min(limit, count) + " 条，耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserInfoService: 加载用户数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e; // 重新抛出异常，让Controller处理
        }
    }

    /**
     * 加载数据到内存（默认限制100条）
     */
    public void loadDataToMemory() {
        loadDataToMemory(100);
    }

    /**
     * 获取所有用户（从内存）
     */
    public List<UserInfo_njj> getAllUsersFromMemory() {
        return UserInfo_list_njj.getAllUsers();
    }

    /**
     * 根据ID获取用户（从内存）
     */
    public UserInfo_njj getUserByIdFromMemory(Long userId) {
        return UserInfo_list_njj.getUserById(userId);
    }

    /**
     * 获取统计信息（从内存）
     */
    public Map<String, Object> getStatisticsFromMemory() {
        return UserInfo_list_njj.getStatistics();
    }

    /**
     * 强制刷新内存缓存
     */
    public void forceRefreshMemory() {
        UserInfo_list_njj.forceRefresh(this);
    }

    /**
     * 更新用户金币余额
     */
    public boolean updateUserGold(Long userId, Long newGoldAmount) {
        try {
            // 更新数据库中的金币
            UserInfo_njj user = new UserInfo_njj();
            user.setUserId(userId);
            user.setGold(newGoldAmount);

            com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<UserInfo_njj> updateWrapper = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
            updateWrapper.eq("user_id", userId).set("gold", newGoldAmount);

            boolean result = this.update(updateWrapper);

            if (result) {
                // 更新内存缓存中的用户信息
                UserInfo_njj cachedUser = UserInfo_list_njj.getUserById(userId);
                if (cachedUser != null) {
                    cachedUser.setGold(newGoldAmount);
                    System.out.println("用户金币已更新: userId=" + userId + ", newGold=" + newGoldAmount);
                    return true;
                }
            }

            System.err.println("更新用户金币失败: userId=" + userId + ", newGold=" + newGoldAmount);
            return false;
        } catch (Exception e) {
            System.err.println("更新用户金币异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 加载所有用户数据到内存（UserInfo_list_njj）
    public void loadUserInfoToMemory() {
        try {
            System.out.println("开始加载用户数据到内存...");
            List<UserInfo_njj> allUser = userInfoMapper.selectAllRecords(); // 从数据库查询所有用户
            UserInfo_list_njj.loadFromDatabaseDirectly(allUser); // 写入内存缓存
            System.out.println("用户数据加载完成，共 " + allUser.size() + " 条");
        } catch (Exception e) {
            System.err.println("加载用户数据到内存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 在 UserInfoService 中添加
    @PostConstruct // 服务启动后自动执行
    public void initUserInfoOnStartup() {
        this.loadUserInfoToMemory(); // 启动时加载用户数据到缓存
    }
}