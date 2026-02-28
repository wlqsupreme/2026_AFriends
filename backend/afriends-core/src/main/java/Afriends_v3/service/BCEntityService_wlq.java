package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * B和C开头_wlq实体类综合服务
 * 负责管理所有B和C开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class BCEntityService_wlq {

    @Autowired
    private CommentLikeRelationMapper commentLikeRelationMapper;

    @Autowired
    private ContentDislikeRelationMapper contentDislikeRelationMapper;

    @Autowired
    private ContentFavouriteRelationMapper contentFavouriteRelationMapper;

    @Autowired
    private ContentFeedbackRelationMapper contentFeedbackRelationMapper;

    @Autowired
    private ContentLikeRelationMapper contentLikeRelationMapper;

    // CommentLikeRelation 相关方法
    public void loadCommentLikeRelationToMemory() {
        try {
            System.out.println("BCEntityService_wlq: 开始从数据库加载评论点赞关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService_wlq: 使用原生SQL查询评论点赞关系数据...");
            var allCommentLikeRelation = commentLikeRelationMapper.selectAllRecords();
            System.out.println("BCEntityService_wlq: 原生SQL查询到 " + allCommentLikeRelation.size() + " 条记录");

            if (allCommentLikeRelation.isEmpty()) {
                System.out.println("BCEntityService_wlq: 数据库中没有评论点赞关系数据");
                return;
            }

            CommentLikeRelation_list_wlq.loadFromDatabaseDirectly(allCommentLikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService_wlq: 评论点赞关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载评论点赞关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<CommentLikeRelation_wlq> getAllCommentLikeRelationFromMemory() {
        return CommentLikeRelation_list_wlq.getAllCommentLikeRelation();
    }

    public Map<String, Object> getCommentLikeRelationStatisticsFromMemory() {
        return CommentLikeRelation_list_wlq.getStatistics();
    }

    // ContentDislikeRelation 相关方法
    public void loadContentDislikeRelationToMemory() {
        try {
            System.out.println("BCEntityService_wlq: 开始从数据库加载内容不喜欢关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService_wlq: 使用原生SQL查询内容不喜欢关系数据...");
            var allContentDislikeRelation = contentDislikeRelationMapper.selectAllRecords();
            System.out.println("BCEntityService_wlq: 原生SQL查询到 " + allContentDislikeRelation.size() + " 条记录");

            if (allContentDislikeRelation.isEmpty()) {
                System.out.println("BCEntityService_wlq: 数据库中没有内容不喜欢关系数据");
                return;
            }

            ContentDislikeRelation_list_wlq.loadFromDatabaseDirectly(allContentDislikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService_wlq: 内容不喜欢关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容不喜欢关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ContentDislikeRelation_wlq> getAllContentDislikeRelationFromMemory() {
        return ContentDislikeRelation_list_wlq.getAllContentDislikeRelation();
    }

    public Map<String, Object> getContentDislikeRelationStatisticsFromMemory() {
        return ContentDislikeRelation_list_wlq.getStatistics();
    }

    // ContentFavouriteRelation 相关方法
    public void loadContentFavouriteRelationToMemory() {
        try {
            System.out.println("BCEntityService_wlq: 开始从数据库加载内容收藏关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService_wlq: 使用原生SQL查询内容收藏关系数据...");
            var allContentFavouriteRelation = contentFavouriteRelationMapper.selectAllRecords();
            System.out.println("BCEntityService_wlq: 原生SQL查询到 " + allContentFavouriteRelation.size() + " 条记录");

            if (allContentFavouriteRelation.isEmpty()) {
                System.out.println("BCEntityService_wlq: 数据库中没有内容收藏关系数据");
                return;
            }

            ContentFavouriteRelation_list_wlq.loadFromDatabaseDirectly(allContentFavouriteRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService_wlq: 内容收藏关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容收藏关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ContentFavouriteRelation_wlq> getAllContentFavouriteRelationFromMemory() {
        return ContentFavouriteRelation_list_wlq.getAllContentFavouriteRelation();
    }

    public Map<String, Object> getContentFavouriteRelationStatisticsFromMemory() {
        return ContentFavouriteRelation_list_wlq.getStatistics();
    }

    // ContentFeedbackRelation 相关方法
    public void loadContentFeedbackRelationToMemory() {
        try {
            System.out.println("BCEntityService_wlq: 开始从数据库加载内容反馈关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService_wlq: 使用原生SQL查询内容反馈关系数据...");
            var allContentFeedbackRelation = contentFeedbackRelationMapper.selectAllRecords();
            System.out.println("BCEntityService_wlq: 原生SQL查询到 " + allContentFeedbackRelation.size() + " 条记录");

            if (allContentFeedbackRelation.isEmpty()) {
                System.out.println("BCEntityService_wlq: 数据库中没有内容反馈关系数据");
                return;
            }

            ContentFeedbackRelation_list_wlq.loadFromDatabaseDirectly(allContentFeedbackRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService_wlq: 内容反馈关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容反馈关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ContentFeedbackRelation_wlq> getAllContentFeedbackRelationFromMemory() {
        return ContentFeedbackRelation_list_wlq.getAllContentFeedbackRelation();
    }

    public Map<String, Object> getContentFeedbackRelationStatisticsFromMemory() {
        return ContentFeedbackRelation_list_wlq.getStatistics();
    }

    // ContentLikeRelation 相关方法
    public void loadContentLikeRelationToMemory() {
        try {
            System.out.println("BCEntityService_wlq: 开始从数据库加载内容点赞关系数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService_wlq: 使用原生SQL查询内容点赞关系数据...");
            var allContentLikeRelation = contentLikeRelationMapper.selectAllRecords();
            System.out.println("BCEntityService_wlq: 原生SQL查询到 " + allContentLikeRelation.size() + " 条记录");

            if (allContentLikeRelation.isEmpty()) {
                System.out.println("BCEntityService_wlq: 数据库中没有内容点赞关系数据");
                return;
            }

            ContentLikeRelation_list_wlq.loadFromDatabaseDirectly(allContentLikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService_wlq: 内容点赞关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容点赞关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ContentLikeRelation_wlq> getAllContentLikeRelationFromMemory() {
        return ContentLikeRelation_list_wlq.getAllContentLikeRelation();
    }

    public Map<String, Object> getContentLikeRelationStatisticsFromMemory() {
        return ContentLikeRelation_list_wlq.getStatistics();
    }

    // 加载所有B和C开头_wlq实体类数据
    public void loadAllBCEntityDataToMemory() {
        System.out.println("BCEntityService_wlq: 开始加载所有B和C开头_wlq实体类数据...");

        try {
            loadCommentLikeRelationToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载评论点赞关系数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadContentDislikeRelationToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容不喜欢关系数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadContentFavouriteRelationToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容收藏关系数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadContentFeedbackRelationToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容反馈关系数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadContentLikeRelationToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService_wlq: 加载内容点赞关系数据失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("BCEntityService_wlq: 所有B和C开头_wlq实体类数据加载完成！");
    }
}
