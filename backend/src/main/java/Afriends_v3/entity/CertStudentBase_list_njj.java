package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 学生证认证基础信息列表
 * 对应实体: CertStudentBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class CertStudentBase_list_njj extends EntityList<CertStudentBase_njj> {

    // 内存存储
    private static final Map<Long, CertStudentBase_njj> certStudentBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCertStudentBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<CertStudentBase_njj> allCertStudentBase) {
        try {
            System.out.println("开始直接加载学生证认证基础信息数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (CertStudentBase_njj student : allCertStudentBase) {
                System.out.println("处理学生证认证数据: ID=" + student.getSchoolId() + ", 学校名称=" + student.getSchoolName());
                certStudentBaseCache.put(student.getSchoolId(), student);
            }

            // 更新统计信息
            totalCertStudentBase = certStudentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("学生证认证基础信息数据直接加载完成！总数: " + totalCertStudentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载学生证认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有学生证认证基础信息数据到内存
     */
    public static void loadFromDatabase(BaseMapper<CertStudentBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载学生证认证基础信息数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有学生证认证基础信息数据
            System.out.println("正在执行数据库查询...");
            List<CertStudentBase_njj> allCertStudentBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCertStudentBase.size() + " 条记录");

            // 存储到内存缓存
            for (CertStudentBase_njj student : allCertStudentBase) {
                System.out.println("处理学生证认证数据: ID=" + student.getSchoolId() + ", 学校名称=" + student.getSchoolName());
                certStudentBaseCache.put(student.getSchoolId(), student);
            }

            // 更新统计信息
            totalCertStudentBase = certStudentBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("学生证认证基础信息数据加载完成！总数: " + totalCertStudentBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载学生证认证基础信息数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有学生证认证基础信息数据（从内存）
     */
    public static List<CertStudentBase_njj> getAllCertStudentBase() {
        return new ArrayList<>(certStudentBaseCache.values());
    }

    /**
     * 根据学校ID获取学生证认证基础信息数据
     */
    public static CertStudentBase_njj getCertStudentBaseById(Long schoolId) {
        return certStudentBaseCache.get(schoolId);
    }

    /**
     * 根据学校名称搜索学生证认证基础信息数据
     */
    public static List<CertStudentBase_njj> searchCertStudentBaseByName(String schoolName) {
        List<CertStudentBase_njj> result = new ArrayList<>();
        for (CertStudentBase_njj student : certStudentBaseCache.values()) {
            if (student.getSchoolName() != null && student.getSchoolName().contains(schoolName)) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * 根据地区搜索学生证认证基础信息数据
     */
    public static List<CertStudentBase_njj> searchCertStudentBaseByRegion(String region) {
        List<CertStudentBase_njj> result = new ArrayList<>();
        for (CertStudentBase_njj student : certStudentBaseCache.values()) {
            if (student.getRegion() != null && student.getRegion().contains(region)) {
                result.add(student);
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
        stats.put("totalCertStudentBase", totalCertStudentBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        certStudentBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<CertStudentBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}