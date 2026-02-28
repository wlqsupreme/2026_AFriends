package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * E、F、M、P、R、T开头实体类综合服务
 * 负责管理所有E、F、M、P、R、T开头实体类的数据库操作和内存缓存
 */
@Service
public class EFMPRTEntityService {

    @Autowired
    private EmojiBaseMapper emojiBaseMapper;

    @Autowired
    private FriendsProfileMapper friendsProfileMapper;

    @Autowired
    private MessageExtraBaseMapper messageExtraBaseMapper;

    @Autowired
    private ProductsBaseMapper productsBaseMapper;

    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private ReportCategoryMapper reportCategoryMapper;

    @Autowired
    private ReportRecordNjjMapper reportRecordMapper;

    @Autowired
    private TaskCommentMapper taskCommentMapper;

    // EmojiBase 相关方法
    public void loadEmojiBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载表情基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询表情基础信息数据...");
            var allEmojiBase = emojiBaseMapper.selectAllRecords();
            System.out.println(
                    "EFMPRTEntityService: 原生SQL查询到 " + (allEmojiBase != null ? allEmojiBase.size() : 0) + " 条记录");

            if (allEmojiBase == null || allEmojiBase.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有表情基础信息数据，跳过加载");
                // 即使没有数据也要调用loadFromDatabaseDirectly来清空缓存
                EmojiBase_list_njj.loadFromDatabaseDirectly(new ArrayList<>());
                return;
            }

            EmojiBase_list_njj.loadFromDatabaseDirectly(allEmojiBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 表情基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载表情基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            // 不抛出异常，继续处理其他实体类
            System.out.println("EFMPRTEntityService: 跳过表情基础信息数据加载，继续处理其他实体类");
        }
    }

    public List<EmojiBase_njj> getAllEmojiBaseFromMemory() {
        return EmojiBase_list_njj.getAllEmojiBase();
    }

    public Map<String, Object> getEmojiBaseStatisticsFromMemory() {
        return EmojiBase_list_njj.getStatistics();
    }

    // FriendsProfile 相关方法
    public void loadFriendsProfileToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载好友资料数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询好友资料数据...");
            var allFriendsProfile = friendsProfileMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allFriendsProfile.size() + " 条记录");

            if (allFriendsProfile.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有好友资料数据");
                return;
            }

            FriendsProfile_list_njj.loadFromDatabaseDirectly(allFriendsProfile);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 好友资料数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载好友资料数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<FriendsProfile_njj> getAllFriendsProfileFromMemory() {
        return FriendsProfile_list_njj.getAllFriendsProfile();
    }

    public Map<String, Object> getFriendsProfileStatisticsFromMemory() {
        return FriendsProfile_list_njj.getStatistics();
    }

    // MessageExtraBase 相关方法
    public void loadMessageExtraBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载消息扩展基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询消息扩展基础信息数据...");
            var allMessageExtraBase = messageExtraBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allMessageExtraBase.size() + " 条记录");

            if (allMessageExtraBase.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有消息扩展基础信息数据");
                return;
            }

            MessageExtraBase_list_njj.loadFromDatabaseDirectly(allMessageExtraBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 消息扩展基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载消息扩展基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<MessageExtraBase_njj> getAllMessageExtraBaseFromMemory() {
        return MessageExtraBase_list_njj.getAllMessageExtraBase();
    }

    public Map<String, Object> getMessageExtraBaseStatisticsFromMemory() {
        return MessageExtraBase_list_njj.getStatistics();
    }

    // ProductsBase 相关方法
    public void loadProductsBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载产品基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询产品基础信息数据...");
            var allProductsBase = productsBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allProductsBase.size() + " 条记录");

            if (allProductsBase.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有产品基础信息数据");
                return;
            }

            ProductsBase_list_njj.loadFromDatabaseDirectly(allProductsBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 产品基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载产品基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ProductsBase_njj> getAllProductsBaseFromMemory() {
        return ProductsBase_list_njj.getAllProductsBase();
    }

    public Map<String, Object> getProductsBaseStatisticsFromMemory() {
        return ProductsBase_list_njj.getStatistics();
    }

    // PurchaseRecord 相关方法
    public void loadPurchaseRecordToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载购买记录数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询购买记录数据...");
            var allPurchaseRecord = purchaseRecordMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allPurchaseRecord.size() + " 条记录");

            if (allPurchaseRecord.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有购买记录数据");
                return;
            }

            PurchaseRecord_list_njj.loadFromDatabaseDirectly(allPurchaseRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 购买记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载购买记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<PurchaseRecord_njj> getAllPurchaseRecordFromMemory() {
        return PurchaseRecord_list_njj.getAllPurchaseRecord();
    }

    public Map<String, Object> getPurchaseRecordStatisticsFromMemory() {
        return PurchaseRecord_list_njj.getStatistics();
    }

    // ReportCategory 相关方法
    public void loadReportCategoryToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载举报类别数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询举报类别数据...");
            var allReportCategory = reportCategoryMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allReportCategory.size() + " 条记录");

            if (allReportCategory.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有举报类别数据");
                return;
            }

            ReportCategory_list_njj.loadFromDatabaseDirectly(allReportCategory);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 举报类别数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载举报类别数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ReportCategory_njj> getAllReportCategoryFromMemory() {
        return ReportCategory_list_njj.getAllReportCategory();
    }

    public Map<String, Object> getReportCategoryStatisticsFromMemory() {
        return ReportCategory_list_njj.getStatistics();
    }

    // ReportRecord 相关方法
    public void loadReportRecordToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载举报记录数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询举报记录数据...");
            var allReportRecord = reportRecordMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allReportRecord.size() + " 条记录");

            if (allReportRecord.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有举报记录数据");
                return;
            }

            ReportRecord_list_njj.loadFromDatabaseDirectly(allReportRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 举报记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载举报记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ReportRecord_njj> getAllReportRecordFromMemory() {
        return ReportRecord_list_njj.getAllReportRecord();
    }

    public Map<String, Object> getReportRecordStatisticsFromMemory() {
        return ReportRecord_list_njj.getStatistics();
    }

    // TaskComment 相关方法
    public void loadTaskCommentToMemory() {
        try {
            System.out.println("EFMPRTEntityService: 开始从数据库加载任务评论数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("EFMPRTEntityService: 使用原生SQL查询任务评论数据...");
            var allTaskComment = taskCommentMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService: 原生SQL查询到 " + allTaskComment.size() + " 条记录");

            if (allTaskComment.isEmpty()) {
                System.out.println("EFMPRTEntityService: 数据库中没有任务评论数据");
                return;
            }

            TaskComment_list_njj.loadFromDatabaseDirectly(allTaskComment);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService: 任务评论数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载任务评论数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<TaskComment_njj> getAllTaskCommentFromMemory() {
        return TaskComment_list_njj.getAllTaskComment();
    }

    public Map<String, Object> getTaskCommentStatisticsFromMemory() {
        return TaskComment_list_njj.getStatistics();
    }

    /**
     * 保存任务评论
     */
    public Map<String, Object> saveTaskComment(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取请求参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            Long taskId = Long.valueOf(requestData.get("taskId").toString());
            Byte stars = Byte.valueOf(requestData.get("stars").toString());
            String commentText = requestData.get("commentText").toString();

            // 检查是否已经评价过（暂时只根据userId检查）
            int existingCount = taskCommentMapper.checkUserTaskCommentExists(userId);
            if (existingCount > 0) {
                result.put("success", false);
                result.put("message", "您已经评价过任务，不能重复评价");
                System.out.println("用户重复评价: userId=" + userId);
                return result;
            }

            // 生成新的评论ID
            Long taskCommentId = generateNextTaskCommentId();

            // 创建任务评论对象
            TaskComment_njj comment = new TaskComment_njj();
            comment.setTaskCommentId(taskCommentId);
            comment.setUserId(userId);
            comment.setStars(stars);
            comment.setCommentText(commentText);
            comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            comment.setIsVisible((byte) 1); // 默认可见

            // 保存到数据库
            int insertResult = taskCommentMapper.insert(comment);

            if (insertResult > 0) {
                // 同时更新内存缓存
                TaskComment_list_njj.addToCache(comment);

                result.put("success", true);
                result.put("message", "任务评论保存成功");
                result.put("taskCommentId", taskCommentId);
                System.out.println("任务评论保存成功: taskCommentId=" + taskCommentId +
                        ", userId=" + userId + ", stars=" + stars);
            } else {
                result.put("success", false);
                result.put("message", "任务评论保存失败");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存任务评论时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 检查任务评论是否存在
     */
    public boolean checkTaskCommentExists(Long userId, Long taskId) {
        try {
            int count = taskCommentMapper.checkUserTaskCommentExists(userId);
            return count > 0;
        } catch (Exception e) {
            System.err.println("检查任务评论存在性失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 生成下一个任务评论ID
     */
    private Long generateNextTaskCommentId() {
        try {
            // 从数据库查询最大的task_comment_id
            Long maxId = taskCommentMapper.selectMaxTaskCommentId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成任务评论ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    // 加载所有E、F、M、P、R、T开头实体类数据
    public void loadAllEFMPRTEntityDataToMemory() {
        System.out.println("EFMPRTEntityService: 开始加载所有E、F、M、P、R、T开头实体类数据...");

        try {
            loadEmojiBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载表情基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadFriendsProfileToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载好友资料失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadMessageExtraBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载消息扩展基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadProductsBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载产品基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadPurchaseRecordToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载购买记录失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadReportCategoryToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载举报类别失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadReportRecordToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载举报记录失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadTaskCommentToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService: 加载任务评论失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("EFMPRTEntityService: 所有E、F、M、P、R、T开头实体类数据加载完成！");
    }
}
