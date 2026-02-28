package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 充值记录信息列表
 * 对应实体: Recharge_wlq
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class Recharge_list_wlq extends EntityList<Recharge_wlq> {

    // 内存存储
    private static final Map<Long, Recharge_wlq> rechargeCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalRecharge = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<Recharge_wlq> allRecharge) {
        try {
            System.out.println("开始直接加载充值记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (Recharge_wlq recharge : allRecharge) {
                System.out.println("处理充值记录数据: ID=" + recharge.getRechargeId() + ", 用户ID=" + recharge.getUserId());
                rechargeCache.put(recharge.getRechargeId(), recharge);
            }

            // 更新统计信息
            totalRecharge = rechargeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("充值记录数据直接加载完成！总数: " + totalRecharge);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载充值记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有充值记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<Recharge_wlq> mapper) {
        try {
            System.out.println("开始从数据库加载充值记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有充值记录数据
            System.out.println("正在执行数据库查询...");
            List<Recharge_wlq> allRecharge = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allRecharge.size() + " 条记录");

            // 存储到内存缓存
            for (Recharge_wlq recharge : allRecharge) {
                System.out.println("处理充值记录数据: ID=" + recharge.getRechargeId() + ", 用户ID=" + recharge.getUserId());
                rechargeCache.put(recharge.getRechargeId(), recharge);
            }

            // 更新统计信息
            totalRecharge = rechargeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("充值记录数据加载完成！总数: " + totalRecharge);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载充值记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有充值记录数据（从内存）
     */
    public static List<Recharge_wlq> getAllRecharge() {
        return new ArrayList<>(rechargeCache.values());
    }

    /**
     * 根据充值ID获取充值记录数据
     */
    public static Recharge_wlq getRechargeById(Long rechargeId) {
        return rechargeCache.get(rechargeId);
    }

    /**
     * 根据用户ID搜索充值记录数据
     */
    public static List<Recharge_wlq> searchRechargeByUserId(Long userId) {
        List<Recharge_wlq> result = new ArrayList<>();
        for (Recharge_wlq recharge : rechargeCache.values()) {
            if (recharge.getUserId() != null && recharge.getUserId().equals(userId)) {
                result.add(recharge);
            }
        }
        return result;
    }

    /**
     * 根据充值金额范围搜索充值记录数据
     */
    public static List<Recharge_wlq> searchRechargeByAmountRange(java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount) {
        List<Recharge_wlq> result = new ArrayList<>();
        for (Recharge_wlq recharge : rechargeCache.values()) {
            if (recharge.getAmount() != null) {
                if (minAmount != null && recharge.getAmount().compareTo(minAmount) < 0) {
                    continue;
                }
                if (maxAmount != null && recharge.getAmount().compareTo(maxAmount) > 0) {
                    continue;
                }
                result.add(recharge);
            }
        }
        return result;
    }

    /**
     * 根据积分范围搜索充值记录数据
     */
    public static List<Recharge_wlq> searchRechargeByPointsRange(Integer minPoints, Integer maxPoints) {
        List<Recharge_wlq> result = new ArrayList<>();
        for (Recharge_wlq recharge : rechargeCache.values()) {
            if (recharge.getPoints() != null) {
                if (minPoints != null && recharge.getPoints() < minPoints) {
                    continue;
                }
                if (maxPoints != null && recharge.getPoints() > maxPoints) {
                    continue;
                }
                result.add(recharge);
            }
        }
        return result;
    }

    /**
     * 根据账户余额范围搜索充值记录数据
     */
    public static List<Recharge_wlq> searchRechargeByMoneyBalanceRange(java.math.BigDecimal minBalance, java.math.BigDecimal maxBalance) {
        List<Recharge_wlq> result = new ArrayList<>();
        for (Recharge_wlq recharge : rechargeCache.values()) {
            if (recharge.getMoneyBalance() != null) {
                if (minBalance != null && recharge.getMoneyBalance().compareTo(minBalance) < 0) {
                    continue;
                }
                if (maxBalance != null && recharge.getMoneyBalance().compareTo(maxBalance) > 0) {
                    continue;
                }
                result.add(recharge);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecharge", totalRecharge);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        rechargeCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<Recharge_wlq> mapper) {
        loadFromDatabase(mapper);
    }
}
