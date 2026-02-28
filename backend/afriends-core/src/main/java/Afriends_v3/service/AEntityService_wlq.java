package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A开头_wlq实体类综合服务
 * 负责管理所有A开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class AEntityService_wlq {

    @Autowired
    private ActionExpMapper actionExpMapper;

    @Autowired
    private AiChatListDetailRMapper aiChatListDetailRMapper;

    @Autowired
    private AiMatchesMapper aiMatchesMapper;

    @Autowired
    private AiTaskRequireMapper aiTaskRequireMapper;

    @Autowired
    private AiTaskRespondMapper aiTaskRespondMapper;

    // AEntityService_wlq 实现类中添加逻辑（假设数据已加载到内存，从内存中筛选）
    public List<AiChatListDetailR_wlq> queryByUserIdAndModelId(Long userId, Long userAiId) {
        System.out.println("AEntityService_wlq: 查询AI聊天记录，userId=" + userId + ", userAiId=" + userAiId);
        
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!AiChatListDetailR_list_wlq.isCacheLoaded()) {
            System.out.println("AEntityService_wlq: 检测到缓存未加载，自动加载AI聊天列表详情数据...");
            try {
                loadAiChatListDetailRToMemory();
            } catch (Exception e) {
                System.err.println("AEntityService_wlq: 自动加载AI聊天列表详情数据失败: " + e.getMessage());
                // 即使加载失败，也尝试查询（可能是数据库中没有数据）
            }
        }
        
        // 从内存中获取所有聊天记录（复用已有的"查询所有"逻辑）
        List<AiChatListDetailR_wlq> allChats = getAllAiChatListDetailRFromMemory();
        if (allChats == null || allChats.isEmpty()) {
            System.out.println("AEntityService_wlq: 缓存中没有聊天记录");
            return new ArrayList<>();
        }

        System.out.println("AEntityService_wlq: 缓存中共有 " + allChats.size() + " 条聊天记录，开始筛选...");

        // 按 userId 和 modelId 筛选（需确保实体类有对应字段，如 userId、userAiId）
        List<AiChatListDetailR_wlq> filteredChats = new ArrayList<>();
        for (AiChatListDetailR_wlq chat : allChats) {
            if (chat == null) {
                continue;
            }
            
            // 防止空指针异常：调换顺序，让参数在前（参数通常不为null，来自@RequestParam）
            // 这样即使 chat.getUserId() 或 chat.getUserAiId() 为 null，也不会抛出异常
            Long chatUserId = chat.getUserId();
            Long chatUserAiId = chat.getUserAiId();
            
            // 使用 Objects.equals() 进行空安全比较，或者调换顺序让参数在前
            // 如果参数为 null 或字段为 null，则不匹配（返回 false，不抛异常）
            if (userId != null && userAiId != null &&
                Objects.equals(userId, chatUserId) && Objects.equals(userAiId, chatUserAiId)) {
                filteredChats.add(chat);
                System.out.println("AEntityService_wlq: 找到匹配的聊天记录 - id=" + chat.getId() + 
                                 ", userId=" + chatUserId + ", userAiId=" + chatUserAiId);
            }
        }
        
        System.out.println("AEntityService_wlq: 筛选完成，找到 " + filteredChats.size() + " 条匹配的聊天记录");
        return filteredChats;
    }

    // ActionExp 相关方法
    public void loadActionExpToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载行为经验数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询行为经验数据...");
            var allActionExp = actionExpMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allActionExp.size() + " 条记录");

            if (allActionExp.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有行为经验数据");
                return;
            }

            ActionExp_list_wlq.loadFromDatabaseDirectly(allActionExp);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: 行为经验数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载行为经验数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ActionExp_wlq> getAllActionExpFromMemory() {
        return ActionExp_list_wlq.getAllActionExp();
    }

    public Map<String, Object> getActionExpStatisticsFromMemory() {
        return ActionExp_list_wlq.getStatistics();
    }

    // AiChatListDetailR 相关方法
    public void loadAiChatListDetailRToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI聊天列表详情数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI聊天列表详情数据...");
            var allAiChatListDetailR = aiChatListDetailRMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiChatListDetailR.size() + " 条记录");

            if (allAiChatListDetailR.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI聊天列表详情数据");
                return;
            }

            AiChatListDetailR_list_wlq.loadFromDatabaseDirectly(allAiChatListDetailR);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI聊天列表详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI聊天列表详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiChatListDetailR_wlq> getAllAiChatListDetailRFromMemory() {
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!AiChatListDetailR_list_wlq.isCacheLoaded()) {
            System.out.println("AEntityService_wlq: 检测到缓存未加载，自动加载AI聊天列表详情数据...");
            try {
                loadAiChatListDetailRToMemory();
            } catch (Exception e) {
                System.err.println("AEntityService_wlq: 自动加载AI聊天列表详情数据失败: " + e.getMessage());
                // 即使加载失败，也尝试查询（可能是数据库中没有数据）
            }
        }
        return AiChatListDetailR_list_wlq.getAllAiChatListDetailR();
    }

    public Map<String, Object> getAiChatListDetailRStatisticsFromMemory() {
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!AiChatListDetailR_list_wlq.isCacheLoaded()) {
            System.out.println("AEntityService_wlq: 检测到缓存未加载，自动加载AI聊天列表详情数据...");
            try {
                loadAiChatListDetailRToMemory();
            } catch (Exception e) {
                System.err.println("AEntityService_wlq: 自动加载AI聊天列表详情数据失败: " + e.getMessage());
                // 即使加载失败，也尝试查询（可能是数据库中没有数据）
            }
        }
        return AiChatListDetailR_list_wlq.getStatistics();
    }

    // AiMatches 相关方法
    public void loadAiMatchesToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI匹配数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI匹配数据...");
            var allAiMatches = aiMatchesMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiMatches.size() + " 条记录");

            if (allAiMatches.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI匹配数据");
                return;
            }

            AiMatches_list_wlq.loadFromDatabaseDirectly(allAiMatches);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI匹配数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI匹配数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiMatches_wlq> getAllAiMatchesFromMemory() {
        return AiMatches_list_wlq.getAllAiMatches();
    }

    public Map<String, Object> getAiMatchesStatisticsFromMemory() {
        return AiMatches_list_wlq.getStatistics();
    }

    // AiTaskRequire 相关方法
    public void loadAiTaskRequireToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI任务需求数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI任务需求数据...");
            var allAiTaskRequire = aiTaskRequireMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiTaskRequire.size() + " 条记录");

            if (allAiTaskRequire.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI任务需求数据");
                return;
            }

            AiTaskRequire_list_wlq.loadFromDatabaseDirectly(allAiTaskRequire);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI任务需求数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务需求数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiTaskRequire_wlq> getAllAiTaskRequireFromMemory() {
        return AiTaskRequire_list_wlq.getAllAiTaskRequire();
    }

    public Map<String, Object> getAiTaskRequireStatisticsFromMemory() {
        return AiTaskRequire_list_wlq.getStatistics();
    }

    // AiTaskRespond 相关方法
    public void loadAiTaskRespondToMemory() {
        try {
            System.out.println("AEntityService_wlq: 开始从数据库加载AI任务响应数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("AEntityService_wlq: 使用原生SQL查询AI任务响应数据...");
            var allAiTaskRespond = aiTaskRespondMapper.selectAllRecords();
            System.out.println("AEntityService_wlq: 原生SQL查询到 " + allAiTaskRespond.size() + " 条记录");

            if (allAiTaskRespond.isEmpty()) {
                System.out.println("AEntityService_wlq: 数据库中没有AI任务响应数据");
                return;
            }

            AiTaskRespond_list_wlq.loadFromDatabaseDirectly(allAiTaskRespond);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("AEntityService_wlq: AI任务响应数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务响应数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<AiTaskRespond_wlq> getAllAiTaskRespondFromMemory() {
        return AiTaskRespond_list_wlq.getAllAiTaskRespond();
    }

    public Map<String, Object> getAiTaskRespondStatisticsFromMemory() {
        return AiTaskRespond_list_wlq.getStatistics();
    }

    // 加载所有A开头_wlq实体类数据
    public void loadAllAEntityDataToMemory() {
        System.out.println("AEntityService_wlq: 开始加载所有A开头_wlq实体类数据...");

        try {
            loadActionExpToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载行为经验数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiChatListDetailRToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI聊天列表详情数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiMatchesToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI匹配数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiTaskRequireToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务需求数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadAiTaskRespondToMemory();
        } catch (Exception e) {
            System.err.println("AEntityService_wlq: 加载AI任务响应数据失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("AEntityService_wlq: 所有A开头_wlq实体类数据加载完成！");
    }

    /**
     * 保存AI聊天记录到 v2_ai_chat_list_detail_r 表
     * @param chatData 前端传递的聊天数据
     * @return 保存的记录ID
     */
    public Long saveAiChatListDetailR(Map<String, Object> chatData) {
        try {
            // 1. 生成新的记录ID（如果表使用自增ID，可省略此步，由数据库自动生成）
            Long newId = generateNextAiChatDetailId();

            // 2. 创建实体类对象并设置字段
            AiChatListDetailR_wlq chatDetail = new AiChatListDetailR_wlq();
            chatDetail.setId(newId); // 设置ID（如果是自增，可注释此行）

            // 3. 从前端数据中提取字段并赋值（严格对应实体类和表结构）
            // 用户ID（必填）
            chatDetail.setUserId(parseLong(chatData.get("userId")));
            // AI会话ID（关联用户与AI的会话，必填）
            chatDetail.setUserAiId(parseLong(chatData.get("userAiId")));
            // 用户发送的消息（必填）
            chatDetail.setUserMessage(toString(chatData.get("userMessage")));
            // AI的回复消息（必填）
            chatDetail.setAiResponse(toString(chatData.get("aiResponse")));
            // 硬标签（可选，如分类标签）
            chatDetail.setHardTags(toString(chatData.get("hardTags")));
            // 软标签（可选，如情感标签）
            chatDetail.setSoftTags(toString(chatData.get("softTags")));
            // 特征向量（可选，用于AI模型的向量数据）
            chatDetail.setFeatureVector(toString(chatData.get("featureVector")));
            // 聊天类型（可选，如"text"文本、"image"图片等）
            chatDetail.setChatType(toString(chatData.get("chatType")));
            // 消息发送时间（前端传递或后端生成）
            chatDetail.setMessageTimestamp(chatData.get("messageTimestamp") != null
                    ? new Timestamp(Long.parseLong(chatData.get("messageTimestamp").toString()))
                    : new Timestamp(System.currentTimeMillis()));

            // 4. 设置创建时间和更新时间（自动生成）
            Timestamp now = new Timestamp(System.currentTimeMillis());
            chatDetail.setCreatedAt(now);
            chatDetail.setUpdatedAt(now);

            // 5. 保存到数据库
            int rows = aiChatListDetailRMapper.insert(chatDetail);
            if (rows > 0) {
                System.out.println("AI聊天记录保存成功: id=" + newId);
                // 同步更新内存缓存（如果有）
                AiChatListDetailR_list_wlq.addToCache(chatDetail); // 假设存在内存缓存类
                return newId;
            } else {
                System.err.println("AI聊天记录保存失败，数据库插入影响行数为0");
                return null;
            }
        } catch (Exception e) {
            System.err.println("保存AI聊天记录异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成下一个AI聊天记录ID（如果表使用自增ID，可删除此方法）
     */
    private Long generateNextAiChatDetailId() {
        Long maxId = aiChatListDetailRMapper.selectMaxId();
        if (maxId == null) {
            return 10000001L; // 初始ID
        } else {
            return maxId + 1;
        }
    }

    // 工具方法：安全转换为Long
    private Long parseLong(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("必填字段不能为null");
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            return Long.parseLong((String) obj);
        }
        throw new IllegalArgumentException("字段格式错误，无法转换为Long: " + obj);
    }

    // 工具方法：安全转换为String
    private String toString(Object obj) {
        if (obj == null) {
            return null; // 可选字段允许为null
        }
        return obj.toString();
    }
}
