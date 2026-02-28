package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户证书记录信息列表
 * 对应实体: UserCertRecord_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class UserCertRecord_list_njj extends EntityList<UserCertRecord_njj> {

    // 内存存储
    private static final Map<Long, UserCertRecord_njj> userCertRecordCache = new ConcurrentHashMap<>();
    private static final Map<Long, List<UserCertRecord_njj>> userCertCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalUserCertRecord = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<UserCertRecord_njj> allUserCertRecord) {
        try {
            System.out.println("开始直接加载用户证书记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 检查数据是否为空
            if (allUserCertRecord == null || allUserCertRecord.isEmpty()) {
                System.out.println("用户证书记录数据为空，清空缓存并设置状态");
                totalUserCertRecord = 0;
                isCacheLoaded = true;
                lastUpdateTime = System.currentTimeMillis();
                System.out.println("用户证书记录数据直接加载完成！总数: " + totalUserCertRecord);
                return;
            }

            // 存储到内存缓存
            for (UserCertRecord_njj record : allUserCertRecord) {
                if (record != null && record.getRecordId() != null) {
                    System.out.println("处理用户证书记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId());
                    userCertRecordCache.put(record.getRecordId(), record);

                    // 按用户ID分组存储
                    userCertCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
                }
            }

            // 更新统计信息
            totalUserCertRecord = userCertRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户证书记录数据直接加载完成！总数: " + totalUserCertRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载用户证书记录数据失败: " + e.getMessage());
            e.printStackTrace();
            // 即使出错也要设置基本状态
            totalUserCertRecord = 0;
            isCacheLoaded = false;
            lastUpdateTime = 0;
        }
    }

    /**
     * 从数据库加载所有用户证书记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<UserCertRecord_njj> mapper) {
        try {
            System.out.println("开始从数据库加载用户证书记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有用户证书记录数据
            System.out.println("正在执行数据库查询...");
            List<UserCertRecord_njj> allUserCertRecord = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allUserCertRecord.size() + " 条记录");

            // 存储到内存缓存
            for (UserCertRecord_njj record : allUserCertRecord) {
                if (record != null && record.getRecordId() != null) {
                    System.out.println("处理用户证书记录数据: ID=" + record.getRecordId() + ", 用户ID=" + record.getUserId());
                    userCertRecordCache.put(record.getRecordId(), record);

                    // 按用户ID分组存储
                    userCertCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);
                }
            }

            // 更新统计信息
            totalUserCertRecord = userCertRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("用户证书记录数据加载完成！总数: " + totalUserCertRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载用户证书记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户证书记录数据（从内存）
     */
    public static List<UserCertRecord_njj> getAllUserCertRecord() {
        return new ArrayList<>(userCertRecordCache.values());
    }

    /**
     * 根据记录ID获取用户证书记录数据
     */
    public static UserCertRecord_njj getUserCertRecordById(Long recordId) {
        return userCertRecordCache.get(recordId);
    }

    /**
     * 根据用户ID获取用户证书记录数据
     */
    public static List<UserCertRecord_njj> getUserCertRecordByUserId(Long userId) {
        return userCertCache.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * 根据认证类型获取用户证书记录数据
     */
    public static List<UserCertRecord_njj> getUserCertRecordByCertType(UserCertRecord_njj.CertType certType) {
        List<UserCertRecord_njj> result = new ArrayList<>();
        for (UserCertRecord_njj record : userCertRecordCache.values()) {
            if (record.getCertType() != null && record.getCertType().equals(certType)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 根据状态获取用户证书记录数据
     */
    public static List<UserCertRecord_njj> getUserCertRecordByStatus(Byte status) {
        List<UserCertRecord_njj> result = new ArrayList<>();
        for (UserCertRecord_njj record : userCertRecordCache.values()) {
            if (record.getStatus() != null && record.getStatus().equals(status)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUserCertRecord", totalUserCertRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        userCertRecordCache.clear();
        userCertCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<UserCertRecord_njj> mapper) {
        loadFromDatabase(mapper);
    }

    /**
     * 添加新记录到缓存
     */
    public static void addToCache(UserCertRecord_njj record) {
        if (record != null && record.getRecordId() != null) {
            // 添加到主缓存
            userCertRecordCache.put(record.getRecordId(), record);

            // 添加到用户分组缓存
            userCertCache.computeIfAbsent(record.getUserId(), k -> new ArrayList<>()).add(record);

            // 更新统计信息
            totalUserCertRecord = userCertRecordCache.size();
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("新认证记录已添加到缓存: recordId=" + record.getRecordId() + ", userId=" + record.getUserId());
        }
    }
}
