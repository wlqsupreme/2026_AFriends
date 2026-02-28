package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证书基础信息列表
 * 对应实体: CertBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CertBase_list_njj extends EntityList<CertBase_njj> {

    // 内存存储
    private static final Map<Long, CertBase_njj> certBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCertBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CertBase_njj> allCertBase) {
        try {
            System.out.println("开始直接加载证书基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CertBase_njj cert : allCertBase) {
                System.out.println("处理证书数据: ID=" + cert.getPostId() + ", 证书名称=" + cert.getCertName());
                certBaseCache.put(cert.getPostId(), cert);
            }

            // 更新统计信息
            totalCertBase = certBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("证书基础信息数据直接加载完成！总数: " + totalCertBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载证书基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有证书基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CertBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载证书基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有证书基础信息数据
            System.out.println("正在执行数据库查询...");
            List<CertBase_njj> allCertBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCertBase.size() + " 条记录");

            // 存储到内存缓存
            for (CertBase_njj cert : allCertBase) {
                System.out.println("处理证书数据: ID=" + cert.getPostId() + ", 证书名称=" + cert.getCertName());
                certBaseCache.put(cert.getPostId(), cert);
            }

            // 更新统计信息
            totalCertBase = certBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("证书基础信息数据加载完成！总数: " + totalCertBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载证书基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有证书基础信息数据（从内存）
     */
    public static List<CertBase_njj> getAllCertBase() {
        return new ArrayList<>(certBaseCache.values());
    }

    /**
     * 根据证书ID获取证书基础信息数据
     */
    public static CertBase_njj getCertBaseById(Long postId) {
        return certBaseCache.get(postId);
    }

    /**
     * 根据证书名称搜索证书基础信息数据
     */
    public static List<CertBase_njj> searchCertBaseByName(String name) {
        List<CertBase_njj> result = new ArrayList<>();
        for (CertBase_njj cert : certBaseCache.values()) {
            if (cert.getCertName() != null && cert.getCertName().contains(name)) {
                result.add(cert);
            }
        }
        return result;
    }

    /**
     * 根据父级ID获取证书基础信息数据
     */
    public static List<CertBase_njj> getCertBaseByParentId(Long parentId) {
        List<CertBase_njj> result = new ArrayList<>();
        for (CertBase_njj cert : certBaseCache.values()) {
            if (cert.getParentId() != null && cert.getParentId().equals(parentId)) {
                result.add(cert);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCertBase", totalCertBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        certBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CertBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}