package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 题库信息列表（内存缓存）
 * 对应实体: QuestionBank
 * 功能：从数据库读取题库数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class QuestionBankList extends EntityList<QuestionBank> {

    // 内存存储（key: bankId，value: 题库实体）
    private static final Map<Long, QuestionBank> questionBankCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalQuestionBank = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<QuestionBank> allQuestionBank) {
        try {
            System.out.println("开始直接加载题库数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (QuestionBank bank : allQuestionBank) {
                System.out.println("处理题库数据: ID=" + bank.getBankId() + ", 名称=" + bank.getBankName());
                questionBankCache.put(bank.getBankId(), bank);
            }

            // 更新统计信息
            totalQuestionBank = questionBankCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("题库数据直接加载完成！总数: " + totalQuestionBank);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载题库数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有题库数据到内存
     */
    public static void loadFromDatabase(BaseMapper<QuestionBank> mapper) {
        try {
            System.out.println("开始从数据库加载题库数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有题库数据
            System.out.println("正在执行数据库查询...");
            List<QuestionBank> allQuestionBank = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allQuestionBank.size() + " 条记录");

            // 存储到内存缓存
            for (QuestionBank bank : allQuestionBank) {
                System.out.println("处理题库数据: ID=" + bank.getBankId() + ", 名称=" + bank.getBankName());
                questionBankCache.put(bank.getBankId(), bank);
            }

            // 更新统计信息
            totalQuestionBank = questionBankCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("题库数据加载完成！总数: " + totalQuestionBank);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载题库数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有题库数据（从内存）
     */
    public static List<QuestionBank> getAllQuestionBank() {
        return new ArrayList<>(questionBankCache.values());
    }

    /**
     * 根据题库ID获取题库数据
     */
    public static QuestionBank getQuestionBankById(Long bankId) {
        return questionBankCache.get(bankId);
    }

    /**
     * 根据标签筛选题库
     */
    public static List<QuestionBank> filterQuestionBankByTag(String tag) {
        List<QuestionBank> result = new ArrayList<>();
        for (QuestionBank bank : questionBankCache.values()) {
            if (tag != null && tag.equals(bank.getTag())) {
                result.add(bank);
            }
        }
        return result;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestionBank", totalQuestionBank);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        questionBankCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<QuestionBank> mapper) {
        loadFromDatabase(mapper);
    }
}