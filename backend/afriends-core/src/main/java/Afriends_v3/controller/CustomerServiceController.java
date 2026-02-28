package Afriends_v3.controller;

import Afriends_v3.entity.UserChatDetail_njj;
import Afriends_v3.entity.UserChatList_njj;
import Afriends_v3.service.UserChatDetailService;
import Afriends_v3.service.UserChatListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 客服会话控制器
 * 提供客服会话创建和管理的API接口
 */
@RestController
@RequestMapping("/api/customer-service")
@CrossOrigin(origins = "*")
public class CustomerServiceController {
    
    @Autowired
	private UserChatListService userChatListService;
    
    @Autowired
    private UserChatDetailService userChatDetailService;
    
    /**
     * 创建客服会话
     */
    @PostMapping("/create-session")
    public ResponseEntity<Map<String, Object>> createCustomerServiceSession(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            
            // 首先尝试根据用户ID和客服ID(0)查找现有会话
            UserChatList_njj existingSession = userChatListService.getCustomerServiceSession(userId, 0L);
            
            // 如果存在客服会话，直接返回该会话ID
            if (existingSession != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("sessionId", existingSession.getSessionId());
                result.put("message", "使用现有会话");
                return ResponseEntity.ok(result);
            }
            
            // 创建新的客服会话
            Long sessionId = System.currentTimeMillis(); // 使用时间戳作为简单唯一ID
            
            // 创建会话记录
            UserChatList_njj chatList = new UserChatList_njj();
            chatList.setId(System.currentTimeMillis()); // 使用时间戳作为ID
            chatList.setUserId(userId); // 用户ID
            chatList.setFriendId(0L); // 客服ID设为0
            chatList.setFriendName("客服");
            chatList.setSessionId(sessionId);
            chatList.setStatus("ACTIVE"); // 进行中状态
            chatList.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            chatList.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            
            // 保存到数据库
            boolean saveResult = userChatListService.save(chatList);
            
            if (!saveResult) {
                // 如果保存失败，再次尝试查询
                existingSession = userChatListService.getCustomerServiceSession(userId, 0L);
                if (existingSession != null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("success", true);
                    result.put("sessionId", existingSession.getSessionId());
                    result.put("message", "使用现有会话");
                    return ResponseEntity.ok(result);
                } else {
                    throw new RuntimeException("无法创建客服会话");
                }
            }
            
            // 将新创建的会话添加到内存缓存中
            userChatListService.addToCache(chatList);
            
            // 添加欢迎消息
            Map<String, Object> welcomeMessageData = new HashMap<>();
            welcomeMessageData.put("sessionId", sessionId);
            welcomeMessageData.put("demandParty", "客服");
            welcomeMessageData.put("message", "您好，欢迎联系客服，有什么可以帮助您的吗？");
            welcomeMessageData.put("responseParty", String.valueOf(userId)); // 确保不为null
            welcomeMessageData.put("senderType", "客服");
            userChatDetailService.saveChatDetail(welcomeMessageData);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("sessionId", sessionId);
            result.put("message", "客服会话创建成功");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "创建客服会话失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}