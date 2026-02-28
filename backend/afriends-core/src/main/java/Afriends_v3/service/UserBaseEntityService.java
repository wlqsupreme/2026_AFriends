package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * U开头基础实体类服务类
 */
@Service
public class UserBaseEntityService {

    @Autowired
    private UserBaseLikeActionMapper userBaseLikeActionMapper;

    @Autowired
    private UserBasePicCommentMapper userBasePicCommentMapper;

    @Autowired
    private UserBaseSystemMessageMapper userBaseSystemMessageMapper;

    @Autowired
    private UserBaseTextCommentMapper userBaseTextCommentMapper;

    @Autowired
    private UserBaseUserCollectioinMapper userBaseUserCollectioinMapper;

    // UserBaseLikeAction 相关方法
    public void loadUserBaseLikeActionToMemory() {
        try {
            System.out.println("UserBaseEntityService: 开始从数据库加载用户基础点赞行为数据...");
            long startTime = System.currentTimeMillis();

            var allUserBaseLikeAction = userBaseLikeActionMapper.selectAllRecords();
            System.out.println("UserBaseEntityService: 原生SQL查询到 " + allUserBaseLikeAction.size() + " 条记录");

            if (allUserBaseLikeAction.isEmpty()) {
                System.out.println("UserBaseEntityService: 数据库中没有用户基础点赞行为数据");
                return;
            }

            UserBaseLikeAction_list_njj.loadFromDatabaseDirectly(allUserBaseLikeAction);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseEntityService: 用户基础点赞行为数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseEntityService: 加载用户基础点赞行为数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBaseLikeAction_njj> getAllUserBaseLikeActionFromMemory() {
        return UserBaseLikeAction_list_njj.getAllUserBaseLikeAction();
    }

    public Map<String, Object> getUserBaseLikeActionStatisticsFromMemory() {
        return UserBaseLikeAction_list_njj.getStatistics();
    }

    // UserBasePicComment 相关方法
    public void loadUserBasePicCommentToMemory() {
        try {
            System.out.println("UserBaseEntityService: 开始从数据库加载用户基础图片评论数据...");
            long startTime = System.currentTimeMillis();

            var allUserBasePicComment = userBasePicCommentMapper.selectAllRecords();
            System.out.println("UserBaseEntityService: 原生SQL查询到 " + allUserBasePicComment.size() + " 条记录");

            if (allUserBasePicComment.isEmpty()) {
                System.out.println("UserBaseEntityService: 数据库中没有用户基础图片评论数据");
                return;
            }

            UserBasePicComment_list_njj.loadFromDatabaseDirectly(allUserBasePicComment);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseEntityService: 用户基础图片评论数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseEntityService: 加载用户基础图片评论数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBasePicComment_njj> getAllUserBasePicCommentFromMemory() {
        return UserBasePicComment_list_njj.getAllUserBasePicComment();
    }

    public Map<String, Object> getUserBasePicCommentStatisticsFromMemory() {
        return UserBasePicComment_list_njj.getStatistics();
    }

    // UserBaseSystemMessage 相关方法
    public void loadUserBaseSystemMessageToMemory() {
        try {
            System.out.println("UserBaseEntityService: 开始从数据库加载用户基础系统消息数据...");
            long startTime = System.currentTimeMillis();

            var allUserBaseSystemMessage = userBaseSystemMessageMapper.selectAllRecords();
            System.out.println("UserBaseEntityService: 原生SQL查询到 " + allUserBaseSystemMessage.size() + " 条记录");

            if (allUserBaseSystemMessage.isEmpty()) {
                System.out.println("UserBaseEntityService: 数据库中没有用户基础系统消息数据");
                return;
            }

            UserBaseSystemMessage_list_njj.loadFromDatabaseDirectly(allUserBaseSystemMessage);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseEntityService: 用户基础系统消息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseEntityService: 加载用户基础系统消息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBaseSystemMessage_njj> getAllUserBaseSystemMessageFromMemory() {
        return UserBaseSystemMessage_list_njj.getAllUserBaseSystemMessage();
    }

    public Map<String, Object> getUserBaseSystemMessageStatisticsFromMemory() {
        return UserBaseSystemMessage_list_njj.getStatistics();
    }

    // UserBaseTextComment 相关方法
    public void loadUserBaseTextCommentToMemory() {
        try {
            System.out.println("UserBaseEntityService: 开始从数据库加载用户基础文本评论数据...");
            long startTime = System.currentTimeMillis();

            var allUserBaseTextComment = userBaseTextCommentMapper.selectAllRecords();
            System.out.println("UserBaseEntityService: 原生SQL查询到 " + allUserBaseTextComment.size() + " 条记录");

            if (allUserBaseTextComment.isEmpty()) {
                System.out.println("UserBaseEntityService: 数据库中没有用户基础文本评论数据");
                return;
            }

            UserBaseTextComment_list_njj.loadFromDatabaseDirectly(allUserBaseTextComment);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseEntityService: 用户基础文本评论数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseEntityService: 加载用户基础文本评论数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBaseTextComment_njj> getAllUserBaseTextCommentFromMemory() {
        return UserBaseTextComment_list_njj.getAllUserBaseTextComment();
    }

    public Map<String, Object> getUserBaseTextCommentStatisticsFromMemory() {
        return UserBaseTextComment_list_njj.getStatistics();
    }

    // UserBaseUserCollectioin 相关方法
    public void loadUserBaseUserCollectioinToMemory() {
        try {
            System.out.println("UserBaseEntityService: 开始从数据库加载用户基础用户收藏数据...");
            long startTime = System.currentTimeMillis();

            var allUserBaseUserCollectioin = userBaseUserCollectioinMapper.selectAllRecords();
            System.out.println("UserBaseEntityService: 原生SQL查询到 " + allUserBaseUserCollectioin.size() + " 条记录");

            if (allUserBaseUserCollectioin.isEmpty()) {
                System.out.println("UserBaseEntityService: 数据库中没有用户基础用户收藏数据");
                return;
            }

            UserBaseUserCollectioin_list_njj.loadFromDatabaseDirectly(allUserBaseUserCollectioin);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserBaseEntityService: 用户基础用户收藏数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserBaseEntityService: 加载用户基础用户收藏数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserBaseUserCollectioin_njj> getAllUserBaseUserCollectioinFromMemory() {
        return UserBaseUserCollectioin_list_njj.getAllUserBaseUserCollectioin();
    }

    public Map<String, Object> getUserBaseUserCollectioinStatisticsFromMemory() {
        return UserBaseUserCollectioin_list_njj.getStatistics();
    }
}
