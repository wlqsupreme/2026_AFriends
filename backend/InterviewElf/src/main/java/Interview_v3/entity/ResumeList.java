package Interview_v3.entity;

import Afriends_v3.entity.EntityList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简历列表（内存缓存）
 * 对应实体: Resume
 * 功能：从数据库读取简历数据并存储到内存中
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class ResumeList extends EntityList<Resume> {

    // 内存存储（key: resumeId，value: 简历实体）
    private static final Map<Long, Resume> resumeCache = new ConcurrentHashMap<>();

    // 缓存状态
    private static volatile boolean isCacheLoaded = false;
    private static volatile long lastUpdateTime = 0;

    // 统计信息
    private static int totalResume = 0;

    /**
     * 直接加载数据到内存（使用原生SQL查询结果）
     */
    public static void loadFromDatabaseDirectly(List<Resume> allResume) {
        try {
            System.out.println("开始直接加载简历数据到内存...");

            // 清空现有缓存
            clearCache();

            // 存储到内存缓存
            for (Resume resume : allResume) {
                System.out.println("处理简历数据: ID=" + resume.getId() + ", 名称=" + resume.getResumeName());
                resumeCache.put(resume.getId(), resume);
            }

            // 更新统计信息
            totalResume = resumeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("简历数据直接加载完成！总数: " + totalResume);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("直接加载简历数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库加载所有简历数据到内存
     */
    public static void loadFromDatabase(BaseMapper<Resume> mapper) {
        try {
            System.out.println("开始从数据库加载简历数据...");

            // 清空现有缓存
            clearCache();

            // 从数据库查询所有简历数据
            System.out.println("正在执行数据库查询...");
            List<Resume> allResume = mapper.selectList(null);
            System.out.println("数据库查询完成，查询到 " + allResume.size() + " 条记录");

            // 存储到内存缓存
            for (Resume resume : allResume) {
                System.out.println("处理简历数据: ID=" + resume.getId() + ", 名称=" + resume.getResumeName());
                resumeCache.put(resume.getId(), resume);
            }

            // 更新统计信息
            totalResume = resumeCache.size();

            // 更新缓存状态
            isCacheLoaded = true;
            lastUpdateTime = System.currentTimeMillis();

            System.out.println("简历数据加载完成！总数: " + totalResume);
            System.out.println("缓存状态: isCacheLoaded=" + isCacheLoaded + ", lastUpdateTime=" + lastUpdateTime);

        } catch (Exception e) {
            System.err.println("加载简历数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有简历（从内存）
     */
    public static List<Resume> getAllResume() {
        return new ArrayList<>(resumeCache.values());
    }

    /**
     * 根据简历ID获取简历
     */
    public static Resume getResumeById(Long resumeId) {
        return resumeCache.get(resumeId);
    }

    /**
     * 根据用户ID查询简历
     */
    public static List<Resume> getResumeByUserId(Long userId) {
        List<Resume> result = new ArrayList<>();
        for (Resume resume : resumeCache.values()) {
            if (userId.equals(resume.getUserId())) {
                result.add(resume);
            }
        }
        return result;
    }

    /**
     * 获取用户默认简历
     */
    public static Resume getDefaultResumeByUserId(Long userId) {
        for (Resume resume : resumeCache.values()) {
            if (userId.equals(resume.getUserId()) && resume.getIsDefault() == 1) {
                return resume;
            }
        }
        return null;
    }

    /**
     * 获取统计信息
     */
    public static Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalResume", totalResume);
        stats.put("cacheLoaded", isCacheLoaded);
        stats.put("lastUpdateTime", new Date(lastUpdateTime));
        return stats;
    }

    /**
     * 清空缓存
     */
    private static void clearCache() {
        resumeCache.clear();
        isCacheLoaded = false;
    }

    /**
     * 强制刷新缓存
     */
    public static void forceRefresh(BaseMapper<Resume> mapper) {
        loadFromDatabase(mapper);
    }
}