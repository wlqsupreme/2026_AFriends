package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证书工作基础信息列表
 * 对应实体: CertJobBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CertJobBase_list_njj extends EntityList<CertJobBase_njj> {

    // 内存存储
    private static final Map<Long, CertJobBase_njj> certJobBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCertJobBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CertJobBase_njj> allCertJobBase) {
        try {
            System.out.println("开始直接加载证书工作基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CertJobBase_njj job : allCertJobBase) {
                System.out.println(
                        "处理证书工作数据: ID=" + job.getJobId() + ", 工作类型=" + job.getJobType() + ", 工作名称=" + job.getJobName());
                certJobBaseCache.put(job.getJobId(), job);
            }

            // 更新统计信息
            totalCertJobBase = certJobBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("证书工作基础信息数据直接加载完成！总数: " + totalCertJobBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载证书工作基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有证书工作基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CertJobBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载证书工作基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有证书工作基础信息数据
            System.out.println("正在执行数据库查询...");
            List<CertJobBase_njj> allCertJobBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCertJobBase.size() + " 条记录");

            // 存储到内存缓存
            for (CertJobBase_njj job : allCertJobBase) {
                System.out.println(
                        "处理证书工作数据: ID=" + job.getJobId() + ", 工作类型=" + job.getJobType() + ", 工作名称=" + job.getJobName());
                certJobBaseCache.put(job.getJobId(), job);
            }

            // 更新统计信息
            totalCertJobBase = certJobBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("证书工作基础信息数据加载完成！总数: " + totalCertJobBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载证书工作基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有证书工作基础信息数据（从内存）
     */
    public static List<CertJobBase_njj> getAllCertJobBase() {
        return new ArrayList<>(certJobBaseCache.values());
    }

    /**
     * 根据工作ID获取证书工作基础信息数据
     */
    public static CertJobBase_njj getCertJobBaseById(Long jobId) {
        return certJobBaseCache.get(jobId);
    }

    /**
     * 根据工作类型搜索证书工作基础信息数据
     */
    public static List<CertJobBase_njj> searchCertJobBaseByType(String jobType) {
        List<CertJobBase_njj> result = new ArrayList<>();
        for (CertJobBase_njj job : certJobBaseCache.values()) {
            if (job.getJobType() != null && job.getJobType().contains(jobType)) {
                result.add(job);
            }
        }
        return result;
    }

    /**
     * 根据父级ID获取证书工作基础信息数据
     */
    public static List<CertJobBase_njj> getCertJobBaseByParentId(Long parentId) {
        List<CertJobBase_njj> result = new ArrayList<>();
        for (CertJobBase_njj job : certJobBaseCache.values()) {
            if (job.getParentId() != null && job.getParentId().equals(parentId)) {
                result.add(job);
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
        stats.put("totalCertJobBase", totalCertJobBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        certJobBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CertJobBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}