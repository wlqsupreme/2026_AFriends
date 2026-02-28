package Afriends_v3.service;

import Afriends_v3.entity.NovelpostBase_wlq;
import Afriends_v3.mapper.NovelpostBaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 搜索的服务类
 * 用于处理所有搜索的请求
 * */
@Service
public class UserSearchService {
    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    /**
     * 全局搜索小说（支持多条件组合）
     * @param keyword 搜索关键词（标题/描述/内容/标签）
     * @param pageNum 页码（默认1）
     * @param pageSize 每页条数（默认20）
     * @param sortType 排序类型（0-最新 1-最热 2-评分最高）
     * @param status 小说状态（1-连载中 2-已完结，null-全部）
     * @return 分页搜索结果
     */
    public Map<String, Object> searchNovels(String keyword, Integer pageNum, Integer pageSize,
                                            Integer sortType, Byte status) {
        System.out.println("NovelSearchService: 开始搜索小说 - 关键词: " + keyword +
                ", 页码: " + pageNum + ", 每页条数: " + pageSize +
                ", 排序类型: " + sortType + ", 状态: " + status);

        try {
            // 处理分页参数默认值
            pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
            pageSize = (pageSize == null || pageSize < 1 || pageSize > 50) ? 20 : pageSize;

            // 构建查询条件
            QueryWrapper<NovelpostBase_wlq> queryWrapper = new QueryWrapper<>();

            // 基础过滤：只查询可见且未删除的小说
            queryWrapper.eq("is_visible", 1)
                    .isNull("deleted_at");

            // 状态过滤（可选）
            if (status != null && (status == 1 || status == 2)) {
                queryWrapper.eq("novel_status", status);
            }

            // 关键词搜索（多字段匹配）
            if (StringUtils.hasText(keyword)) {
                String likeKeyword = "%" + keyword.trim() + "%";
                System.out.println("NovelSearchService: 相似 " + likeKeyword);
                queryWrapper.and(wrapper -> wrapper
                        .like("novel_title", likeKeyword)          // 匹配标题
                        .or().like("novel_description", likeKeyword) // 匹配描述
                        .or().like("novel_text", likeKeyword)        // 匹配正文（可根据性能调整是否保留）
                        .or().like("soft_tags", likeKeyword)         // 匹配软标签
                        .or().like("hard_tags", likeKeyword)         // 匹配硬标签
                        .or().like("tag_list", likeKeyword)          // 匹配标签列表
                        .or().like("author_name", likeKeyword)       // 匹配作者名
                );
                System.out.println("NovelSearchService: " + queryWrapper);
            }

            // 排序处理
            switch (sortType == null ? 0 : sortType) {
                case 1:  // 最热（按热度指标排序）
                    queryWrapper.orderByDesc("view_count")
                            .orderByDesc("like_count")
                            .orderByDesc("comment_count");
                    break;
                case 2:  // 评分最高
                    queryWrapper.orderByDesc("novel_score")
                            .orderByDesc("reading_count");
                    break;
                default: // 最新（默认）
                    queryWrapper.orderByDesc("created_at");
            }

            // 执行分页查询
            Page<NovelpostBase_wlq> page = new Page<>(pageNum, pageSize);
            IPage<NovelpostBase_wlq> resultPage = novelpostBaseMapper.selectPage(page, queryWrapper);

            // 处理返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("total", resultPage.getTotal());         // 总条数
            response.put("pages", resultPage.getPages());         // 总页数
            response.put("current", resultPage.getCurrent());     // 当前页
            response.put("size", resultPage.getSize());           // 每页条数
            response.put("novels", formatNovelList(resultPage.getRecords())); // 格式化后的小说列表
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("NovelSearchService: 搜索完成 - 找到 " + resultPage.getTotal() + " 本小说");
            return response;

        } catch (Exception e) {
            System.err.println("NovelSearchService: 搜索小说失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "搜索小说失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return errorResponse;
        }
    }

    /**
     * 获取热门搜索词（基于高频标签）
     * @param limit 最多返回数量
     * @return 热门搜索词列表
     */
    public List<String> getHotSearchWords(Integer limit) {
        limit = (limit == null || limit < 1 || limit > 20) ? 10 : limit;

        try {
            // 从标签中提取热门搜索词（实际项目可根据搜索历史表优化）
            List<NovelpostBase_wlq> hotNovels = novelpostBaseMapper.selectList(
                    new QueryWrapper<NovelpostBase_wlq>()
                            .eq("is_visible", 1)
                            .isNull("deleted_at")
                            .orderByDesc("view_count")
                            .last("LIMIT " + limit * 3) // 取更多数据用于提取标签
            );

            // 合并所有标签并统计频率
            Map<String, Integer> tagCount = new HashMap<>();
            for (NovelpostBase_wlq novel : hotNovels) {
                // 处理软标签
                addTagsToMap(novel.getSoftTags(), tagCount);
                // 处理硬标签
                addTagsToMap(novel.getHardTags(), tagCount);
                // 处理标签列表
                addTagsToMap(novel.getTagList(), tagCount);
            }

            // 排序并取前N个
            List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(tagCount.entrySet());
            sortedTags.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            List<String> hotWords = new ArrayList<>();
            for (int i = 0; i < Math.min(limit, sortedTags.size()); i++) {
                hotWords.add(sortedTags.get(i).getKey());
            }

            return hotWords;
        } catch (Exception e) {
            System.err.println("NovelSearchService: 获取热门搜索词失败: " + e.getMessage());
            e.printStackTrace();
            return Arrays.asList("热门小说", "最新连载", "高分推荐"); // 默认值
        }
    }

    /**
     * 将标签字符串拆分并添加到计数Map
     */
    private void addTagsToMap(String tags, Map<String, Integer> tagCount) {
        if (!StringUtils.hasText(tags)) {
            return;
        }
        // 处理多种分隔符（逗号、分号、空格等）
        String[] tagArray = tags.split("[,;\\s]+");
        for (String tag : tagArray) {
            tag = tag.trim();
            if (tag.length() > 1) { // 过滤过短标签
                tagCount.put(tag, tagCount.getOrDefault(tag, 0) + 1);
            }
        }
    }

    /**
     * 格式化小说列表（返回前端需要的字段，避免敏感信息泄露）
     */
    private List<Map<String, Object>> formatNovelList(List<NovelpostBase_wlq> novels) {
        List<Map<String, Object>> formattedList = new ArrayList<>();
        for (NovelpostBase_wlq novel : novels) {
            Map<String, Object> novelMap = new HashMap<>();
            novelMap.put("novelId", novel.getNovelId());
            novelMap.put("novelTitle", novel.getNovelTitle());
            novelMap.put("novelDescription", novel.getNovelDescription());
            novelMap.put("novelCoverUrl", novel.getNovelCoverUrl());
            novelMap.put("authorId", novel.getAuthorId());
            novelMap.put("authorName", novel.getAuthorName());
            novelMap.put("novelScore", novel.getNovelScore());
            novelMap.put("readingCount", novel.getReadingCount());
            novelMap.put("likeCount", novel.getLikeCount());
            novelMap.put("commentCount", novel.getCommentCount());
            novelMap.put("novelStatus", novel.getNovelStatus());
            novelMap.put("statusText", getNovelStatusText(novel.getNovelStatus()));
            novelMap.put("createdAt", novel.getCreatedAt());
            novelMap.put("tags", combineTags(novel)); // 合并标签

            formattedList.add(novelMap);
        }
        return formattedList;
    }

    /**
     * 合并多种标签为一个列表
     */
    private List<String> combineTags(NovelpostBase_wlq novel) {
        Set<String> tags = new LinkedHashSet<>(); // 去重且保留顺序
        addTagsToSet(novel.getSoftTags(), tags);
        addTagsToSet(novel.getHardTags(), tags);
        addTagsToSet(novel.getTagList(), tags);
        return new ArrayList<>(tags);
    }

    private void addTagsToSet(String tags, Set<String> tagSet) {
        if (!StringUtils.hasText(tags)) {
            return;
        }
        String[] tagArray = tags.split("[,;\\s]+");
        for (String tag : tagArray) {
            tag = tag.trim();
            if (!tag.isEmpty()) {
                tagSet.add(tag);
            }
        }
    }

    /**
     * 获取小说状态描述文本
     */
    private String getNovelStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0: return "未发布";
            case 1: return "连载中";
            case 2: return "已完结";
            case 3: return "暂停更新";
            default: return "未知状态";
        }
    }
}
