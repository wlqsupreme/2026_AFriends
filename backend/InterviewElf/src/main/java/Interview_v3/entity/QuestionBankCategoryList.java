package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 题库分类列表（内存缓存）
 * 对应实体: QuestionBankCategory
 * 功能：从数据库读取分类数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class QuestionBankCategoryList extends EntityList<QuestionBankCategory> {

    // 内存存储（key: categoryId，value: 分类实体）
    private static final Map<Long, QuestionBankCategory> categoryCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalCategory = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<QuestionBankCategory> allCategory) {
        try {
            System.out.println("开始直接加载题库分类数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (QuestionBankCategory category : allCategory) {
                System.out.println("处理分类数据: ID=" + category.getCategoryId() + ", 名称=" + category.getCategoryName());
                categoryCache.put(category.getCategoryId(), category);
            }

            // 更新统计信息
            totalCategory = categoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("题库分类数据直接加载完成！总数: " + totalCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载题库分类数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有分类数据到内存
     */
    public static void loadFromDatabase(BaseMapper<QuestionBankCategory> mapper) {
        try {
            System.out.println("开始从数据库加载题库分类数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有分类数据
            System.out.println("正在执行数据库查询...");
            List<QuestionBankCategory> allCategory = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allCategory.size() + " 条分类记录");

            // 存储到内存缓存
            for (QuestionBankCategory category : allCategory) {
                System.out.println("处理分类数据: ID=" + category.getCategoryId() + ", 名称=" + category.getCategoryName());
                categoryCache.put(category.getCategoryId(), category);
            }

            // 更新统计信息
            totalCategory = categoryCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("题库分类数据加载完成！总数: " + totalCategory);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载题库分类数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有分类数据（从内存，按排序权重升序）
     */
    public static List<QuestionBankCategory> getAllCategory() {
        // 转换为列表并按sort升序排序（符合分类展示逻辑）
        List<QuestionBankCategory> categoryList = new ArrayList<>(categoryCache.values());
        categoryList.sort(Comparator.comparingInt(QuestionBankCategory::getSort));
        return categoryList;
    }

    /**
     * 根据分类ID获取分类数据
     */
    public static QuestionBankCategory getCategoryById(Long categoryId) {
        return categoryCache.get(categoryId);
    }

    /**
     * 根据分类名称查询分类
     */
    public static QuestionBankCategory getCategoryByName(String categoryName) {
        for (QuestionBankCategory category : categoryCache.values()) {
            if (categoryName != null && categoryName.equals(category.getCategoryName())) {
                return category;
            }
        }
        return null;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCategory", totalCategory);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        categoryCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<QuestionBankCategory> mapper) {
        loadFromDatabase(mapper);
    }
}