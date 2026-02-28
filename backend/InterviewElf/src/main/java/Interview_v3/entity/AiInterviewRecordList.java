package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模拟面试记录列表（内存缓存）
 * 对应实体: AiInterviewRecord
 * 功能：从数据库读取面试记录并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class AiInterviewRecordList extends EntityList<AiInterviewRecord> {

    // 内存存储（key: interviewId，value: 面试记录实体）
    private static final Map<Long, AiInterviewRecord> interviewRecordCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalInterviewRecord = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<AiInterviewRecord> allInterviewRecord) {
        try {
            System.out.println("开始直接加载AI模拟面试记录数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (AiInterviewRecord record : allInterviewRecord) {
                System.out.println("处理面试记录: ID=" + record.getInterviewId() + ", 用户ID=" + record.getUserId());
                interviewRecordCache.put(record.getInterviewId(), record);
            }

            // 更新统计信息
            totalInterviewRecord = interviewRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模拟面试记录数据直接加载完成！总数: " + totalInterviewRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载AI模拟面试记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有面试记录数据到内存
     */
    public static void loadFromDatabase(BaseMapper<AiInterviewRecord> mapper) {
        try {
            System.out.println("开始从数据库加载AI模拟面试记录数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有面试记录
            System.out.println("正在执行数据库查询...");
            List<AiInterviewRecord> allInterviewRecord = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allInterviewRecord.size() + " 条记录");

            // 存储到内存缓存
            for (AiInterviewRecord record : allInterviewRecord) {
                System.out.println("处理面试记录: ID=" + record.getInterviewId() + ", 用户ID=" + record.getUserId());
                interviewRecordCache.put(record.getInterviewId(), record);
            }

            // 更新统计信息
            totalInterviewRecord = interviewRecordCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("AI模拟面试记录数据加载完成！总数: " + totalInterviewRecord);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载AI模拟面试记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有面试记录（从内存）
     */
    public static List<AiInterviewRecord> getAllInterviewRecord() {
        return new ArrayList<>(interviewRecordCache.values());
    }

    /**
     * 根据面试ID获取记录
     */
    public static AiInterviewRecord getInterviewRecordById(Long interviewId) {
        return interviewRecordCache.get(interviewId);
    }

    /**
     * 根据用户ID查询面试记录
     */
    public static List<AiInterviewRecord> getInterviewRecordByUserId(Long userId) {
        List<AiInterviewRecord> result = new ArrayList<>();
        for (AiInterviewRecord record : interviewRecordCache.values()) {
            if (userId.equals(record.getUserId())) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 根据面试状态筛选记录
     */
    public static List<AiInterviewRecord> getInterviewRecordByStatus(Byte status) {
        List<AiInterviewRecord> result = new ArrayList<>();
        for (AiInterviewRecord record : interviewRecordCache.values()) {
            if (status.equals(record.getInterviewStatus())) {
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
        stats.put("totalInterviewRecord", totalInterviewRecord);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        interviewRecordCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<AiInterviewRecord> mapper) {
        loadFromDatabase(mapper);
    }
}