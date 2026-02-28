package Afriends_v3.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 协议基础信息列表
 * 对应实体: AgreementBase_njj
 * 功能：从数据库读取数据并存储到内存中
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AgreementBase_list_njj extends EntityList<AgreementBase_njj> {

    // 内存存储
    private static final Map<Long, AgreementBase_njj> agreementBaseCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalAgreementBase = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询的结果）
     */
    public static void loadFromDatabaseDirectly(List<AgreementBase_njj> allAgreementBase) {
        try {
            System.out.println("开始直接加载协议基础数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AgreementBase_njj agreement : allAgreementBase) {
                System.out.println("处理协议数据: ID=" + agreement.getFileId() + ", 标题=" + agreement.getTitle());
                agreementBaseCache.put(agreement.getFileId(), agreement);
            }

            // 更新统计信息
            totalAgreementBase = agreementBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("协议基础数据直接加载完成！总数: " + totalAgreementBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载协议基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有协议基础数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AgreementBase_njj> mapper) {
        try {
            System.out.println("开始从数据库加载协议基础数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有协议基础数据
            System.out.println("正在执行数据库查询...");
            List<AgreementBase_njj> allAgreementBase = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allAgreementBase.size() + " 条记录");

            // 存储到内存缓存
            for (AgreementBase_njj agreement : allAgreementBase) {
                System.out.println("处理协议数据: ID=" + agreement.getFileId() + ", 标题=" + agreement.getTitle());
                agreementBaseCache.put(agreement.getFileId(), agreement);
            }

            // 更新统计信息
            totalAgreementBase = agreementBaseCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("协议基础数据加载完成！总数: " + totalAgreementBase);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载协议基础数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有协议基础数据（从内存）
     */
    public static List<AgreementBase_njj> getAllAgreementBase() {
        return new ArrayList<>(agreementBaseCache.values());
    }

    /**
     * 根据文件ID获取协议基础数据
     */
    public static AgreementBase_njj getAgreementBaseById(Long fileId) {
        return agreementBaseCache.get(fileId);
    }

    /**
     * 根据标题搜索协议基础数据
     */
    public static List<AgreementBase_njj> searchAgreementBaseByTitle(String title) {
        List<AgreementBase_njj> result = new ArrayList<>();
        for (AgreementBase_njj agreement : agreementBaseCache.values()) {
            if (agreement.getTitle() != null && agreement.getTitle().contains(title)) {
                result.add(agreement);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAgreementBase", totalAgreementBase);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        agreementBaseCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AgreementBase_njj> mapper) {
        loadFromDatabase(mapper);
    }
}