package Afriends_v3.service;

import Afriends_v3.entity.UserChatList_njj;
import Afriends_v3.entity.UserChatList_list_njj;
import Afriends_v3.mapper.UserChatListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 用户聊天列表服务类
 * 负责管理用户聊天列表数据的数据库操作和内存缓存
 */
@Service
public class UserChatListService {
    
    @Autowired
    private UserChatListMapper userChatListMapper;
    
    /**
     * 保存用户聊天列表数据
     */
    public boolean save(UserChatList_njj chatList) {
        try {
            int result = userChatListMapper.insert(chatList);
            return result > 0;
        } catch (Exception e) {
            System.err.println("UserChatListService: 保存用户聊天列表数据失败: " + e.getMessage());
            // 不打印完整的堆栈跟踪，避免日志过多
            return false;
        }
    }
    
    /**
     * 根据会话ID获取聊天列表数据
     */
    public UserChatList_njj getBySessionId(Long sessionId) {
        try {
            List<UserChatList_njj> chatLists = userChatListMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserChatList_njj>()
                    .eq(UserChatList_njj::getSessionId, sessionId)
            );
            return chatLists.isEmpty() ? null : chatLists.get(0);
        } catch (Exception e) {
            System.err.println("UserChatListService: 根据会话ID获取聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 更新用户聊天列表数据
     */
    public boolean update(UserChatList_njj chatList) {
        try {
            int result = userChatListMapper.updateById(chatList);
            return result > 0;
        } catch (Exception e) {
            System.err.println("UserChatListService: 更新用户聊天列表数据失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据用户ID和好友ID获取客服会话
     */
    public UserChatList_njj getCustomerServiceSession(Long userId, Long friendId) {
        try {
            // 查询该用户所有会话
            List<UserChatList_njj> userSessions = UserChatList_list_njj.getUserChatListByUserId(userId);
            for (UserChatList_njj session : userSessions) {
                // 匹配指定的好友ID
                if (session.getFriendId() != null && session.getFriendId().equals(friendId)) {
                    return session;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("UserChatListService: 根据用户ID和好友ID获取客服会话失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取用户活动的客服会话
     */
    public UserChatList_njj getActiveCustomerServiceSession(Long userId) {
        try {
            // 查询该用户所有会话
            List<UserChatList_njj> userSessions = UserChatList_list_njj.getUserChatListByUserId(userId);
            for (UserChatList_njj session : userSessions) {
                // 识别客服会话的条件：
                // 1. friendId 为 0（我们约定的客服标识）
                // 2. 状态为 "ACTIVE"（进行中）
                if (session.getFriendId() != null && session.getFriendId() == 0L 
                    && "ACTIVE".equals(session.getStatus())) {
                    return session;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("UserChatListService: 获取用户活动的客服会话失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取用户所有的客服会话（按时间倒序排列）
     */
    public List<UserChatList_njj> getCustomerServiceSessions(Long userId) {
        try {
            // 查询该用户所有会话
            List<UserChatList_njj> userSessions = UserChatList_list_njj.getUserChatListByUserId(userId);
            List<UserChatList_njj> customerSessions = new ArrayList<>();
            
            // 筛选出所有客服会话（friendId为0的会话）
            for (UserChatList_njj session : userSessions) {
                if (session.getFriendId() != null && session.getFriendId() == 0L) {
                    customerSessions.add(session);
                }
            }
            
            // 按更新时间倒序排列
            customerSessions.sort(new Comparator<UserChatList_njj>() {
                @Override
                public int compare(UserChatList_njj s1, UserChatList_njj s2) {
                    return s2.getUpdatedAt().compareTo(s1.getUpdatedAt());
                }
            });
            
            return customerSessions;
        } catch (Exception e) {
            System.err.println("UserChatListService: 获取用户客服会话列表失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * 将聊天列表添加到缓存中
     */
    public void addToCache(UserChatList_njj chatList) {
        UserChatList_list_njj.addToCache(chatList);
    }
}