package Afriends_v3.controller;

import Afriends_v3.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 搜索控制器
 * 用于处理所有搜索的请求
 * */
@RestController
@RequestMapping("/api/search")
public class UserSearchController {
    // 注入小说搜索服务
    @Autowired
    private UserSearchService userSearchService;

    /**
     * 小说全局搜索接口
     * 支持关键词、分页、排序、状态过滤
     */
    @GetMapping("/novels")
    public Map<String, Object> searchNovels(
            @RequestParam(required = false) String keyword, // 搜索关键词（可选）
            @RequestParam(required = false) Integer pageNum, // 页码（默认1）
            @RequestParam(required = false) Integer pageSize, // 每页条数（默认20）
            @RequestParam(required = false) Integer sortType, // 排序类型（0-最新 1-最热 2-评分最高）
            @RequestParam(required = false) Byte status) { // 小说状态（1-连载中 2-已完结）
        System.out.println("搜索关键词: " + keyword +
                ", 帖子类型: " + pageNum + ", 用户ID: " + pageSize +
                ", 评论内容: " + sortType + ", 父评论ID: " + status);
        // 直接调用服务层方法，返回处理结果
        return userSearchService.searchNovels(keyword, pageNum, pageSize, sortType, status);
    }

    /**
     * 获取热门搜索词接口
     */
    @GetMapping("/novels/hot-words")
    public Map<String, Object> getHotSearchWords(
            @RequestParam(required = false) Integer limit) { // 最多返回数量（默认10）

        List<String> hotWords = userSearchService.getHotSearchWords(limit);
        System.out.println("热门搜索词: " + hotWords);
        // 封装返回结果
        return Map.of(
                "success", true,
                "hotWords", hotWords,
                "timestamp", System.currentTimeMillis()
        );
    }

}
