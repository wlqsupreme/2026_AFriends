package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实名认证基础信息列表
 * 对应实体: CertRealnameBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CertRealnameBase_list_njj extends EntityList<CertRealnameBase_njj> {

    // 内存存储
    private static final Map<Long, CertRealnameBase_njj> certRealnameBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCertRealnameBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CertRealnameBase_njj> allCertRealnameBase) {
        try {
            System.out.println("开始直接加载实名认证基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CertRealnameBase_njj realname : allCertRealnameBase) {
                System.out.println("处理实名认证数据: ID=" + realname.getId() + ", 证件类型=" + realname.getIdType());
                certRealnameBaseCache.put(realname.getId(), realname);
            }

            // 更新统计信息
            totalCertRealnameBase = certRealnameBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("实名认证基础信息数据直接加载完成！总数: " + totalCertRealnameBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载实名认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有实名认证基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CertRealnameBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载实名认证基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有实名认证基础信息数据
            System.out.println("正在执行数据库查询...");
            List<CertRealnameBase_njj> allCertRealnameBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCertRealnameBase.size() + " 条记录");

            // 存储到内存缓存
            for (CertRealnameBase_njj realname : allCertRealnameBase) {
                System.out.println("处理实名认证数据: ID=" + realname.getId() + ", 证件类型=" + realname.getIdType());
                certRealnameBaseCache.put(realname.getId(), realname);
            }

            // 更新统计信息
            totalCertRealnameBase = certRealnameBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("实名认证基础信息数据加载完成！总数: " + totalCertRealnameBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载实名认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有实名认证基础信息数据（从内存）
     */
    public static List<CertRealnameBase_njj> getAllCertRealnameBase() {
        return new ArrayList<>(certRealnameBaseCache.values());
    }

    /**
     * 根据ID获取实名认证基础信息数据
     */
    public static CertRealnameBase_njj getCertRealnameBaseById(Long id) {
        return certRealnameBaseCache.get(id);
    }

    /**
     * 根据证件类型搜索实名认证基础信息数据
     */
    public static List<CertRealnameBase_njj> searchCertRealnameBaseByType(String idType) {
        List<CertRealnameBase_njj> result = new ArrayList<>();
        for (CertRealnameBase_njj realname : certRealnameBaseCache.values()) {
            if (realname.getIdType() != null && realname.getIdType().contains(idType)) {
                result.add(realname);
            }
        }
        return result;
    }

    /**
     * 检查缓存是否已加载
     */
    public static boolean isCacheLoaded() {
        return isCacheLoaded;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCertRealnameBase", totalCertRealnameBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        certRealnameBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CertRealnameBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}