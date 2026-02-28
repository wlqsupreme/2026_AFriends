package Afriends_v3.service;

import Afriends_v3.entity.UserSystemMessage_njj;
import Afriends_v3.entity.UserSystemMessage_list_njj;
import Afriends_v3.mapper.UserSystemMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知服务类
 */
@Service
public class NotificationService {
    
    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;
    
    @Autowired
    private UserNotificationSettingsService userNotificationSettingsService;
    
    /**
     * 发送通知的核心方法
     * @param recipientUserId 接收用户ID
     * @param senderUserId 发送用户ID
     * @param messageType 消息类型
     * @param messageContent 消息内容
     * @param relatedEntityType 关联实体类型
     * @param relatedEntityId 关联实体ID
     * @return 是否成功发送通知
     */
    public boolean sendNotification(Long recipientUserId, Long senderUserId, 
                                  String messageType, String messageContent,
                                  String relatedEntityType, Long relatedEntityId) {
        // 检查用户是否愿意接收此类通知
        if (!shouldSendNotification(recipientUserId, messageType)) {
            System.out.println("NotificationService: 用户 " + recipientUserId + " 不接收 " + messageType + " 类型的通知");
            return false;
        }
        
        // 创建并保存通知消息
        UserSystemMessage_njj message = new UserSystemMessage_njj();
        message.setMessageId(System.currentTimeMillis()); // 使用当前时间戳作为消息ID
        message.setRecipientUserId(recipientUserId);
        message.setSenderUserId(senderUserId);
        message.setMessageContent(messageContent);
        message.setRelatedEntityType(relatedEntityType);
        message.setRelatedEntityId(relatedEntityId);
        message.setIsRead((byte) 0); // 未读
        message.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        // 保存到数据库
        userSystemMessageMapper.insert(message);
        
        // TODO: 如果将来实现了实时推送，可以在这里添加推送逻辑
        // 比如WebSocket推送或者移动推送服务
        
        return true;
    }
    
    /**
     * 判断是否应该发送通知给用户
     * @param userId 用户ID
     * @param notificationType 通知类型
     * @return 是否应该发送通知
     */
    private boolean shouldSendNotification(Long userId, String notificationType) {
        // 如果用户ID为空，不发送通知
        if (userId == null) {
            return false;
        }
        
        // 检查用户的通知设置
        return userNotificationSettingsService.shouldSendNotification(userId, notificationType);
    }
    
    /**
     * 获取用户未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public Long getUnreadCount(Long userId) {
        if (userId == null) {
            return 0L;
        }
        
        try {
            // 优先从缓存查询
            List<UserSystemMessage_njj> unreadMessages = UserSystemMessage_list_njj.getUserSystemMessageByRecipientUserId(userId);
            if (unreadMessages != null && !unreadMessages.isEmpty()) {
                long count = unreadMessages.stream()
                    .filter(msg -> msg.getIsRead() != null && msg.getIsRead() == 0)
                    .count();
                return count;
            }
            
            // 缓存中没有则从数据库查询
            Long count = userSystemMessageMapper.countUnreadByUserId(userId);
            return count != null ? count : 0L;
        } catch (Exception e) {
            System.err.println("获取未读消息数量失败: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }
    
    /**
     * 获取用户通知列表（分页）
     * @param userId 用户ID
     * @param page 页码（从1开始）
     * @param size 每页数量
     * @return 包含通知列表和分页信息的Map
     */
    public Map<String, Object> getNotificationList(Long userId, Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        
        if (userId == null) {
            result.put("notifications", List.of());
            result.put("total", 0);
            result.put("page", page != null ? page : 1);
            result.put("size", size != null ? size : 20);
            return result;
        }
        
        try {
            // 设置默认值
            if (page == null || page < 1) {
                page = 1;
            }
            if (size == null || size < 1) {
                size = 20;
            }
            
            // 计算偏移量
            int offset = (page - 1) * size;
            
            // 优先从缓存查询
            List<UserSystemMessage_njj> allMessages = UserSystemMessage_list_njj.getUserSystemMessageByRecipientUserId(userId);
            List<UserSystemMessage_njj> notifications;
            
            if (allMessages != null && !allMessages.isEmpty()) {
                // 从缓存中获取，按时间倒序排序
                notifications = allMessages.stream()
                    .sorted((a, b) -> {
                        if (a.getCreatedAt() == null || b.getCreatedAt() == null) {
                            return 0;
                        }
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    })
                    .skip(offset)
                    .limit(size)
                    .collect(Collectors.toList());
                
                result.put("notifications", notifications);
                result.put("total", allMessages.size());
            } else {
                // 从数据库查询
                notifications = userSystemMessageMapper.getNotificationsByUserId(userId, offset, size);
                
                // 查询总数（简化处理，实际应该单独查询）
                Long total = userSystemMessageMapper.countUnreadByUserId(userId);
                result.put("notifications", notifications);
                result.put("total", total != null ? total : 0);
            }
            
            result.put("page", page);
            result.put("size", size);
            
        } catch (Exception e) {
            System.err.println("获取通知列表失败: " + e.getMessage());
            e.printStackTrace();
            result.put("notifications", List.of());
            result.put("total", 0);
            result.put("page", page);
            result.put("size", size);
        }
        
        return result;
    }
    
    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @param userId 用户ID（用于验证消息归属）
     * @return 是否成功
     */
    public boolean markAsRead(Long messageId, Long userId) {
        if (messageId == null || userId == null) {
            return false;
        }
        
        try {
            int rows = userSystemMessageMapper.markAsRead(messageId, userId);
            
            // 更新缓存
            UserSystemMessage_njj message = UserSystemMessage_list_njj.getUserSystemMessageById(messageId);
            if (message != null && message.getRecipientUserId().equals(userId)) {
                message.setIsRead((byte) 1);
            }
            
            return rows > 0;
        } catch (Exception e) {
            System.err.println("标记消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 标记用户所有消息为已读
     * @param userId 用户ID
     * @return 是否成功
     */
    public boolean markAllAsRead(Long userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            int rows = userSystemMessageMapper.markAllAsRead(userId);
            
            // 更新缓存
            List<UserSystemMessage_njj> messages = UserSystemMessage_list_njj.getUserSystemMessageByRecipientUserId(userId);
            if (messages != null) {
                for (UserSystemMessage_njj message : messages) {
                    if (message.getIsRead() != null && message.getIsRead() == 0) {
                        message.setIsRead((byte) 1);
                    }
                }
            }
            
            return rows >= 0; // 即使没有未读消息也返回true
        } catch (Exception e) {
            System.err.println("标记所有消息为已读失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}