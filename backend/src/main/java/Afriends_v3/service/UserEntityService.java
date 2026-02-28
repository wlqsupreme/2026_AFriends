package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import Afriends_v3.mapper.PurchaseRecordMapper;
import Afriends_v3.util.PrivacySettingsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * U开头实体类服务类
 */
@Service
public class UserEntityService {

    private static final Long FRIEND_REQUEST_FUNCTION_ID = 20000001L;
    private static final Long FRIEND_RELATION_FUNCTION_ID = 20000002L;
    private static final com.fasterxml.jackson.databind.ObjectMapper OBJECT_MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();

    @Autowired
    private UserCertRecordMapper userCertRecordMapper;

    @Autowired
    private UserChatDetailMapper userChatDetailMapper;

    @Autowired
    private UserChatListMapper userChatListMapper;

    @Autowired
    private UserContentViewLogMapper userContentViewLogMapper;

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    @Autowired
    private UserDislikeRelationMapper userDislikeRelationMapper;

    @Autowired
    private UserFriendsRelationshipMapper userFriendsRelationshipMapper;

    @Autowired
    private UserImageRecommendationMapper userImageRecommendationMapper;

    @Autowired
    private UserInfoFeatureVectorMapper userInfoFeatureVectorMapper;

    @Autowired
    private UserInfoQuestionMapper userInfoQuestionMapper;

    @Autowired
    private UserNovelRecommendationMapper userNovelRecommendationMapper;

    @Autowired
    private UserNovelRelationMapper userNovelRelationMapper;

    @Autowired
    private UserReviewBaseMapper userReviewBaseMapper;

    @Autowired
    private UserSettingRelationMapper userSettingRelationMapper;

    @Autowired
    private SettingBaseMapper settingBaseMapper;

    @Autowired
    private UserSoftTagRelationMapper userSoftTagRelationMapper;

    @Autowired
    private UserSystemMessageMapper userSystemMessageMapper;

    @Autowired
    private UserTaskRelationshipMapper userTaskRelationshipMapper;

    @Autowired
    private UserAiCommentMapper userAiCommentMapper;

    @Autowired
    private UserAiModelMapper userAiModelMapper;

    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FriendsProfileMapper friendsProfileMapper;

    // UserCertRecord 相关方法
    public void loadUserCertRecordToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户认证记录数据...");
            long startTime = System.currentTimeMillis();

            var allUserCertRecord = userCertRecordMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserCertRecord.size() + " 条记录");

            if (allUserCertRecord.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户认证记录数据");
                return;
            }

            UserCertRecord_list_njj.loadFromDatabaseDirectly(allUserCertRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户认证记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户认证记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserCertRecord_njj> getAllUserCertRecordFromMemory() {
        return UserCertRecord_list_njj.getAllUserCertRecord();
    }

    public Map<String, Object> getUserCertRecordStatisticsFromMemory() {
        return UserCertRecord_list_njj.getStatistics();
    }

    public List<UserCertRecord_njj> getUserCertRecordByUserIdFromMemory(Long userId) {
        return UserCertRecord_list_njj.getUserCertRecordByUserId(userId);
    }

    public Map<String, Object> saveUserCertRecord(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取请求参数
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String certType = requestData.get("certType").toString();
            String certInfoJson = requestData.get("certInfo").toString();

            // 生成新的recordId（读取最大ID然后加1）
            Long maxRecordId = userCertRecordMapper.selectMaxRecordId();
            Long recordId = (maxRecordId != null) ? maxRecordId + 1 : 10000001L; // 如果表为空，从10000001开始

            // 创建认证记录对象
            UserCertRecord_njj record = new UserCertRecord_njj();
            record.setRecordId(recordId);
            record.setUserId(userId);
            record.setCertType(UserCertRecord_njj.CertType.valueOf(certType));
            record.setCertInfo(certInfoJson);
            record.setStatus((byte) 0); // 0表示待审核
            record.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            record.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int insertResult = userCertRecordMapper.insert(record);

            if (insertResult > 0) {
                // 同时更新内存缓存
                UserCertRecord_list_njj.addToCache(record);

                result.put("success", true);
                result.put("message", "认证记录保存成功");
                result.put("recordId", recordId);
                System.out.println("认证记录保存成功: recordId=" + recordId + ", userId=" + userId + ", certType=" + certType);
            } else {
                result.put("success", false);
                result.put("message", "认证记录保存失败");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存认证记录时发生错误: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    // UserChatDetail 相关方法
    public void loadUserChatDetailToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            var allUserChatDetail = userChatDetailMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserChatDetail.size() + " 条记录");

            if (allUserChatDetail.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户聊天详情数据");
                return;
            }

            UserChatDetail_list_njj.loadFromDatabaseDirectly(allUserChatDetail);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户聊天详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户聊天详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserChatDetail_njj> getAllUserChatDetailFromMemory() {
        return UserChatDetail_list_njj.getAllUserChatDetail();
    }

    public Map<String, Object> getUserChatDetailStatisticsFromMemory() {
        return UserChatDetail_list_njj.getStatistics();
    }

    public List<Map<String, Object>> getFriendListByUserIdFromMemory(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }

            List<UserFriendsRelationship_njj> relationships = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId);

            List<UserChatList_njj> chatLists = UserChatList_list_njj.getUserChatListByUserId(userId);
            Map<Long, UserChatList_njj> chatListByFriendId = new HashMap<>();
            if (chatLists != null) {
                for (UserChatList_njj c : chatLists) {
                    if (c != null && c.getFriendId() != null) {
                        chatListByFriendId.put(c.getFriendId(), c);
                    }
                }
            }

            List<Map<String, Object>> result = new ArrayList<>();
            if (relationships == null || relationships.isEmpty()) {
                return result;
            }

            for (UserFriendsRelationship_njj relation : relationships) {
                if (relation == null || relation.getFunctionId() == null) {
                    continue;
                }
                if (!FRIEND_RELATION_FUNCTION_ID.equals(relation.getFunctionId())) {
                    continue;
                }

                Long friendId = relation.getFriendsId();
                if (friendId == null) {
                    continue;
                }

                UserInfo_njj friendUser = UserInfo_list_njj.getUserById(friendId);
                if (friendUser == null) {
                    continue;
                }

                String friendName = friendUser.getUsername();
                if (friendName == null || friendName.trim().isEmpty()) {
                    friendName = friendUser.getRealName();
                }
                if (friendName == null || friendName.trim().isEmpty()) {
                    friendName = "";
                }

                String avatarUrl = friendUser.getProfilePicUrl();

                UserChatList_njj chatList = chatListByFriendId.get(friendId);
                Long sessionId = (chatList != null) ? chatList.getSessionId() : null;

                Map<String, Object> item = new HashMap<>();
                item.put("friendId", friendId);
                item.put("friendName", friendName);
                item.put("avatarUrl", avatarUrl);
                item.put("sessionId", sessionId);
                result.add(item);
            }

            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 获取好友列表失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取好友列表失败: " + e.getMessage());
        }
    }

    // UserChatList 相关方法
    public void loadUserChatListToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户聊天列表数据...");
            long startTime = System.currentTimeMillis();

            var allUserChatList = userChatListMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserChatList.size() + " 条记录");

            if (allUserChatList.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户聊天列表数据");
                return;
            }

            UserChatList_list_njj.loadFromDatabaseDirectly(allUserChatList);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户聊天列表数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户聊天列表数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserChatList_njj> getAllUserChatListFromMemory() {
        return UserChatList_list_njj.getAllUserChatList();
    }

    public Map<String, Object> getUserChatListStatisticsFromMemory() {
        return UserChatList_list_njj.getStatistics();
    }

    public List<UserChatList_njj> getUserChatListByUserIdFromMemory(Long userId) {
        // 1. 打印缓存整体状态（总数、是否加载）
        System.out.println("=== 缓存状态概览 ===");
        System.out.println("缓存是否加载完成：" + UserChatList_list_njj.getStatistics().get("cacheLoaded"));
        System.out.println("缓存总条数：" + UserChatList_list_njj.getStatistics().get("totalUserChatList"));
        System.out.println("最后更新时间：" + UserChatList_list_njj.getStatistics().get("lastUpdateTime"));

        List<UserChatList_njj> chatList = UserChatList_list_njj.getUserChatListByUserId(userId);
        System.out.println("获取到用户 " + userId + " 的聊天列表，共 " + chatList.size() + " 条记录");

        // 为每个聊天记录补充好友的详细信息
        for (UserChatList_njj chat : chatList) {
            System.out.println("聊天记录ID：" + chat.getId() +
                    "，好友ID：" + chat.getFriendId() +
                    "，好友名称：" + chat.getFriendName() +
                    "，会话ID：" + chat.getSessionId());
            if (chat.getFriendId() != null) {
                System.out.println("处理聊天记录: ID=" + chat.getId() + ", friendId=" + chat.getFriendId());

                UserInfo_njj friendInfo = UserInfo_list_njj.getUserById(chat.getFriendId());
                if (friendInfo != null) {
                    System.out.println("找到好友信息: friendId=" + chat.getFriendId() +
                            ", realName=" + friendInfo.getRealName() +
                            ", username=" + friendInfo.getUsername() +
                            ", profilePicUrl=" + friendInfo.getProfilePicUrl());

                    // 强制使用user_info表中的用户名，完全忽略数据库中的friend_name字段
                    String friendName = friendInfo.getRealName() != null && !friendInfo.getRealName().trim().isEmpty()
                            ? friendInfo.getRealName()
                            : friendInfo.getUsername() != null && !friendInfo.getUsername().trim().isEmpty()
                                    ? friendInfo.getUsername()
                                    : "未知用户";

                    // 强制设置好友名字，覆盖数据库中的friend_name字段
                    chat.setFriendName(friendName);
                    System.out.println("强制设置好友名字: " + friendName + " (来自user_info表)");

                    // 强制使用user_info表中的头像，覆盖数据库中的avatar_url字段
                    String avatarUrl = friendInfo.getProfilePicUrl() != null ? friendInfo.getProfilePicUrl() : "";
                    chat.setAvatarUrl(avatarUrl);
                    System.out.println("强制设置好友头像: " + avatarUrl + " (来自user_info表)");
                } else {
                    System.out.println("未找到好友信息: friendId=" + chat.getFriendId() + "，设置为未知用户");
                    // 如果找不到好友信息，设置为未知用户
                    chat.setFriendName("未知用户");
                }
            } else {
                System.out.println("聊天记录的friendId为空: ID=" + chat.getId());
                chat.setFriendName("未知用户");
            }
        }

        System.out.println("聊天列表处理完成，返回 " + chatList.size() + " 条记录");
        return chatList;
    }

    public boolean clearUnreadCount(Long chatId) {
        try {
            // 从内存缓存中获取聊天记录
            UserChatList_njj chat = UserChatList_list_njj.getUserChatListById(chatId);
            if (chat == null) {
                System.err.println("未找到聊天记录: chatId=" + chatId);
                return false;
            }

            // 更新未读消息数为0
            chat.setUnreadCount(0);

            // 更新数据库
            int result = userChatListMapper.updateById(chat);
            if (result > 0) {
                System.out.println("未读消息数已清零: chatId=" + chatId);
                return true;
            } else {
                System.err.println("更新数据库失败: chatId=" + chatId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("清零未读消息数异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // UserContentViewLog 相关方法
    public void loadUserContentViewLogToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户内容查看日志数据...");
            long startTime = System.currentTimeMillis();

            var allUserContentViewLog = userContentViewLogMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserContentViewLog.size() + " 条记录");

            if (allUserContentViewLog.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户内容查看日志数据");
                return;
            }

            UserContentViewLog_list_njj.loadFromDatabaseDirectly(allUserContentViewLog);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户内容查看日志数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户内容查看日志数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserContentViewLog_njj> getAllUserContentViewLogFromMemory() {
        return UserContentViewLog_list_njj.getAllUserContentViewLog();
    }

    public Map<String, Object> getUserContentViewLogStatisticsFromMemory() {
        return UserContentViewLog_list_njj.getStatistics();
    }

    // UserDevice 相关方法
    public void loadUserDeviceToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户设备数据...");
            long startTime = System.currentTimeMillis();

            var allUserDevice = userDeviceMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserDevice.size() + " 条记录");

            if (allUserDevice.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户设备数据");
                return;
            }

            UserDevice_list_njj.loadFromDatabaseDirectly(allUserDevice);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户设备数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户设备数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserDevice_njj> getAllUserDeviceFromMemory() {
        return UserDevice_list_njj.getAllUserDevice();
    }

    public Map<String, Object> getUserDeviceStatisticsFromMemory() {
        return UserDevice_list_njj.getStatistics();
    }

    /**
     * 根据用户ID获取设备列表
     */
    public List<UserDevice_njj> getUserDevicesByUserId(Long userId) {
        try {
            // 先从内存缓存获取
            List<UserDevice_njj> devices = UserDevice_list_njj.getUserDeviceByUserId(userId);
            if (devices != null && !devices.isEmpty()) {
                return devices;
            }
            // 如果缓存中没有，从数据库查询
            return userDeviceMapper.selectByUserId(userId);
        } catch (Exception e) {
            System.err.println("获取用户设备列表失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 删除设备
     */
    public boolean deleteUserDevice(Long userId, Long deviceId) {
        try {
            // 检查设备是否属于该用户
            UserDevice_njj device = userDeviceMapper.selectById(deviceId);
            if (device == null || !device.getUserId().equals(userId)) {
                System.err.println("删除设备失败: 设备不存在或不属于该用户");
                return false;
            }
            
            // 不能删除当前设备
            if (device.getIsCurrent() != null && device.getIsCurrent() == 1) {
                System.err.println("删除设备失败: 不能删除当前设备");
                return false;
            }
            
            // 删除设备
            int result = userDeviceMapper.deleteByIdAndUserId(deviceId, userId);
            
            // 更新内存缓存
            if (result > 0) {
                UserDevice_list_njj.removeDevice(deviceId);
                System.out.println("设备删除成功: ID=" + deviceId + ", 用户ID=" + userId);
            }
            
            return result > 0;
        } catch (Exception e) {
            System.err.println("删除设备失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量删除设备
     */
    public boolean batchDeleteUserDevices(Long userId, List<Long> deviceIds) {
        try {
            if (deviceIds == null || deviceIds.isEmpty()) {
                return false;
            }
            
            // 检查所有设备是否属于该用户，且不是当前设备
            for (Long deviceId : deviceIds) {
                UserDevice_njj device = userDeviceMapper.selectById(deviceId);
                if (device == null || !device.getUserId().equals(userId)) {
                    System.err.println("批量删除设备失败: 设备ID=" + deviceId + " 不存在或不属于该用户");
                    return false;
                }
                if (device.getIsCurrent() != null && device.getIsCurrent() == 1) {
                    System.err.println("批量删除设备失败: 设备ID=" + deviceId + " 是当前设备，不能删除");
                    return false;
                }
            }
            
            // 批量删除
            int result = userDeviceMapper.batchDeleteByIds(userId, deviceIds);
            
            // 更新内存缓存
            if (result > 0) {
                UserDevice_list_njj.removeDevices(deviceIds);
                System.out.println("批量删除设备成功: 用户ID=" + userId + ", 删除数量=" + result);
            }
            
            return result > 0;
        } catch (Exception e) {
            System.err.println("批量删除设备失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 记录或更新登录设备信息
     */
    public UserDevice_njj recordLoginDevice(Long userId, String deviceName, String deviceType, 
                                           String deviceModel, String deviceIdentifier, 
                                           String loginLocation, String loginIp) {
        try {
            // 先查询该设备是否已存在
            UserDevice_njj existingDevice = userDeviceMapper.selectByDeviceIdentifier(userId, deviceIdentifier);
            
            if (existingDevice != null) {
                // 更新现有设备信息
                existingDevice.setLastLoginTime(new java.sql.Timestamp(System.currentTimeMillis()));
                existingDevice.setLoginLocation(loginLocation);
                existingDevice.setLoginIp(loginIp);
                existingDevice.setIsCurrent((byte) 1);
                
                // 将其他设备标记为非当前设备
                userDeviceMapper.setOtherDevicesNotCurrent(userId, existingDevice.getId());
                
                // 更新数据库
                userDeviceMapper.updateById(existingDevice);
                
                // 更新内存缓存
                UserDevice_list_njj.updateDevice(existingDevice);
                
                System.out.println("设备信息更新成功: ID=" + existingDevice.getId() + ", 用户ID=" + userId);
                return existingDevice;
            } else {
                // 创建新设备记录
                // 生成设备ID
                Long maxId = userDeviceMapper.selectMaxId();
                Long newDeviceId = (maxId != null) ? maxId + 1 : 10000001L;
                
                UserDevice_njj newDevice = new UserDevice_njj();
                newDevice.setId(newDeviceId);
                newDevice.setUserId(userId);
                newDevice.setDeviceName(deviceName);
                newDevice.setDeviceType(deviceType);
                newDevice.setDeviceModel(deviceModel);
                newDevice.setDeviceIdentifier(deviceIdentifier);
                newDevice.setLoginLocation(loginLocation);
                newDevice.setLoginIp(loginIp);
                newDevice.setLastLoginTime(new java.sql.Timestamp(System.currentTimeMillis()));
                newDevice.setIsCurrent((byte) 1);
                
                // 将其他设备标记为非当前设备
                userDeviceMapper.setAllDevicesNotCurrent(userId);
                
                // 插入数据库
                userDeviceMapper.insert(newDevice);
                
                // 更新内存缓存
                UserDevice_list_njj.addDevice(newDevice);
                
                System.out.println("新设备记录成功: ID=" + newDeviceId + ", 用户ID=" + userId);
                return newDevice;
            }
        } catch (Exception e) {
            System.err.println("记录登录设备信息失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查设备是否为信任设备
     */
    public boolean isDeviceTrusted(Long userId, String deviceIdentifier) {
        try {
            UserDevice_njj device = userDeviceMapper.selectByDeviceIdentifier(userId, deviceIdentifier);
            return device != null;
        } catch (Exception e) {
            System.err.println("检查设备信任状态失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // UserDislikeRelation 相关方法
    public void loadUserDislikeRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户不喜欢关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserDislikeRelation = userDislikeRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserDislikeRelation.size() + " 条记录");

            if (allUserDislikeRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户不喜欢关系数据");
                return;
            }

            UserDislikeRelation_list_njj.loadFromDatabaseDirectly(allUserDislikeRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户不喜欢关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户不喜欢关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserDislikeRelation_njj> getAllUserDislikeRelationFromMemory() {
        return UserDislikeRelation_list_njj.getAllUserDislikeRelation();
    }

    public Map<String, Object> getUserDislikeRelationStatisticsFromMemory() {
        return UserDislikeRelation_list_njj.getStatistics();
    }

    // UserFriendsRelationship 相关方法
    public void loadUserFriendsRelationshipToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户好友关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserFriendsRelationship = userFriendsRelationshipMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserFriendsRelationship.size() + " 条记录");

            if (allUserFriendsRelationship.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户好友关系数据");
                return;
            }

            UserFriendsRelationship_list_njj.loadFromDatabaseDirectly(allUserFriendsRelationship);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户好友关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户好友关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserFriendsRelationship_njj> getAllUserFriendsRelationshipFromMemory() {
        return UserFriendsRelationship_list_njj.getAllUserFriendsRelationship();
    }

    public List<UserFriendsRelationship_njj> getUserFriendsRelationshipByUserIdFromMemory(Long userId) {
        return UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId);
    }

    public Map<String, Object> getUserFriendsRelationshipStatisticsFromMemory() {
        return UserFriendsRelationship_list_njj.getStatistics();
    }

    public String saveUserFriendsSettings(Map<String, Object> settingsData) {
        try {
            Long userId = Long.valueOf(settingsData.get("userId").toString());
            Long friendId = Long.valueOf(settingsData.get("friendId").toString());
            Long settingId = Long.valueOf(settingsData.get("settingId").toString());
            String settingValue = (String) settingsData.get("settingValue");

            // 查找现有的设置记录
            List<UserFriendsRelationship_njj> relationships = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId);
            UserFriendsRelationship_njj existingRelationship = null;

            for (UserFriendsRelationship_njj relationship : relationships) {
                if (relationship.getFriendsId().equals(friendId) && relationship.getFunctionId().equals(settingId)) {
                    existingRelationship = relationship;
                    break;
                }
            }

            if (existingRelationship != null) {
                // 更新现有记录
                existingRelationship.setFunctionSettings(settingValue);
                existingRelationship.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                userFriendsRelationshipMapper.updateById(existingRelationship);

                // 更新内存缓存
                UserFriendsRelationship_list_njj.updateInCache(existingRelationship);

                System.out.println(
                        "更新用户好友设置成功: userId=" + userId + ", friendId=" + friendId + ", settingId=" + settingId);
                return "设置保存成功";
            } else {
                // 创建新记录
                UserFriendsRelationship_njj newRelationship = new UserFriendsRelationship_njj();
                newRelationship.setUserFriendsInfoId(generateNextUserFriendsId());
                newRelationship.setUserId(userId);
                newRelationship.setFriendsId(friendId);
                newRelationship.setFunctionId(settingId); // 使用设置项ID
                newRelationship.setFunctionSettings(settingValue);
                newRelationship.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                newRelationship.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                userFriendsRelationshipMapper.insert(newRelationship);

                // 添加到内存缓存
                UserFriendsRelationship_list_njj.addToCache(newRelationship);

                System.out.println(
                        "创建用户好友设置成功: userId=" + userId + ", friendId=" + friendId + ", settingId=" + settingId);
                return "设置保存成功";
            }
        } catch (Exception e) {
            System.err.println("保存用户好友设置异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("保存设置失败: " + e.getMessage());
        }
    }

    public Map<String, Object> getUserFriendsSettings(Long userId, Long friendId) {
        try {
            List<UserFriendsRelationship_njj> relationships = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId);

            Map<String, Object> settings = new HashMap<>();
            settings.put("userId", userId);
            settings.put("friendId", friendId);

            // 初始化所有设置项为默认值
            settings.put("doNotDisturb", "[\"0\"]"); // 消息免打扰
            settings.put("pinChat", "[\"0\"]"); // 置顶聊天
            settings.put("reminders", "[\"0\"]"); // 提醒
            settings.put("chatBackground", "[\"\"]"); // 聊天背景

            // 遍历所有关系记录，找到与指定好友相关的设置
            for (UserFriendsRelationship_njj relationship : relationships) {
                if (relationship.getFriendsId().equals(friendId)) {
                    Long settingId = relationship.getFunctionId();
                    String settingValue = relationship.getFunctionSettings();

                    // 根据设置ID更新对应的设置值
                    switch (settingId.intValue()) {
                        case 10000002: // 消息免打扰
                            settings.put("doNotDisturb", settingValue);
                            break;
                        case 10000003: // 置顶聊天
                            settings.put("pinChat", settingValue);
                            break;
                        case 10000004: // 提醒
                            settings.put("reminders", settingValue);
                            break;
                        case 10000005: // 设置聊天背景
                            settings.put("chatBackground", settingValue);
                            break;
                    }
                }
            }

            return settings;
        } catch (Exception e) {
            System.err.println("获取用户好友设置异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取设置失败: " + e.getMessage());
        }
    }

    private Long generateNextUserFriendsId() {
        try {
            Long maxId = userFriendsRelationshipMapper.selectMaxId();
            return maxId != null ? maxId + 1 : 10000001L;
        } catch (Exception e) {
            System.err.println("生成用户好友关系ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    public List<UserInfo_njj> searchUsersByUsernameForAddFriend(Long userId, String keyword, String source) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }
            if (keyword == null) {
                keyword = "";
            }
            String lowerKeyword = keyword.toLowerCase();
            if (source == null || source.trim().isEmpty()) {
                source = "PHONE_SEARCH";
            }

            List<UserInfo_njj> allUsers = UserInfo_list_njj.getAllUsers();
            List<UserInfo_njj> result = new ArrayList<>();
            if (allUsers == null) {
                return result;
            }

            Set<Long> excludedUserIds = new HashSet<>();

            List<UserFriendsRelationship_njj> userRelations = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId);
            if (userRelations != null) {
                for (UserFriendsRelationship_njj r : userRelations) {
                    if (r == null || r.getFriendsId() == null || r.getFunctionId() == null) {
                        continue;
                    }
                    if (FRIEND_RELATION_FUNCTION_ID.equals(r.getFunctionId())) {
                        excludedUserIds.add(r.getFriendsId());
                    } else if (FRIEND_REQUEST_FUNCTION_ID.equals(r.getFunctionId())) {
                        Map<String, Object> parsed = parseJsonSettings(r.getFunctionSettings());
                        String status = String.valueOf(parsed.getOrDefault("status", ""));
                        if ("PENDING".equalsIgnoreCase(status)) {
                            excludedUserIds.add(r.getFriendsId());
                        }
                    }
                }
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> incomingWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            incomingWrapper.eq("friends_id", userId).eq("function_id", FRIEND_REQUEST_FUNCTION_ID);
            List<UserFriendsRelationship_njj> incoming = userFriendsRelationshipMapper.selectList(incomingWrapper);
            if (incoming != null) {
                for (UserFriendsRelationship_njj r : incoming) {
                    if (r == null || r.getUserId() == null) {
                        continue;
                    }
                    Map<String, Object> parsed = parseJsonSettings(r.getFunctionSettings());
                    String status = String.valueOf(parsed.getOrDefault("status", ""));
                    if ("PENDING".equalsIgnoreCase(status)) {
                        excludedUserIds.add(r.getUserId());
                    }
                }
            }

            for (UserInfo_njj target : allUsers) {
                if (target == null || target.getUserId() == null) {
                    continue;
                }
                if (target.getUserId().equals(userId)) {
                    continue;
                }

                if (excludedUserIds.contains(target.getUserId())) {
                    continue;
                }

                String username = target.getUsername();
                if (username == null || !username.toLowerCase().contains(lowerKeyword)) {
                    continue;
                }

                if (BlockRecord_list_njj.isBlocked(userId, target.getUserId())
                        || BlockRecord_list_njj.isBlocked(target.getUserId(), userId)) {
                    continue;
                }

                if (!isAddWayAllowed(target.getUserId(), source)) {
                    continue;
                }

                result.add(target);
            }

            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 搜索用户用于添加好友失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("搜索用户失败: " + e.getMessage());
        }
    }

    public Map<String, Object> requestAddFriend(Long fromUserId, Long toUserId, String message, String source) {
        try {
            if (fromUserId == null || toUserId == null) {
                throw new IllegalArgumentException("fromUserId/toUserId不能为空");
            }
            if (fromUserId.equals(toUserId)) {
                throw new IllegalArgumentException("不能添加自己为好友");
            }
            if (source == null || source.trim().isEmpty()) {
                source = "PHONE_SEARCH";
            }

            if (BlockRecord_list_njj.isBlocked(fromUserId, toUserId) || BlockRecord_list_njj.isBlocked(toUserId, fromUserId)) {
                throw new RuntimeException("对方已被拉黑或你已被对方拉黑");
            }

            if (!isAddWayAllowed(toUserId, source)) {
                throw new RuntimeException("对方不允许通过该方式添加");
            }

            if (isAlreadyFriend(fromUserId, toUserId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("mode", "ALREADY_FRIEND");
                result.put("message", "已经是好友");
                return result;
            }

            UserFriendsRelationship_njj pending = findPendingFriendRequest(fromUserId, toUserId);
            if (pending != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("mode", "ALREADY_REQUESTED");
                result.put("message", "已发送好友申请");
                result.put("requestId", pending.getUserFriendsInfoId());
                return result;
            }

            boolean needVerification = getUserSettingBoolean(toUserId, "privacy.friendVerification", true);
            if (!needVerification) {
                createFriendRelationBidirectional(fromUserId, toUserId);
                Map<String, Object> result = new HashMap<>();
                result.put("mode", "DIRECT_FRIEND");
                result.put("message", "已成为好友");
                return result;
            }

            Map<String, Object> settings = new HashMap<>();
            settings.put("status", "PENDING");
            settings.put("message", message == null ? "" : message);
            settings.put("source", source);
            settings.put("createdAt", System.currentTimeMillis());
            String settingsJson = OBJECT_MAPPER.writeValueAsString(settings);

            UserFriendsRelationship_njj request = new UserFriendsRelationship_njj();
            request.setUserFriendsInfoId(generateNextUserFriendsId());
            request.setUserId(fromUserId);
            request.setFriendsId(toUserId);
            request.setFunctionId(FRIEND_REQUEST_FUNCTION_ID);
            request.setFunctionSettings(settingsJson);
            request.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            request.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userFriendsRelationshipMapper.insert(request);
            UserFriendsRelationship_list_njj.addToCache(request);

            Map<String, Object> result = new HashMap<>();
            result.put("mode", "REQUEST");
            result.put("message", "好友申请已发送");
            result.put("requestId", request.getUserFriendsInfoId());
            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 发起好友申请失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("发起好友申请失败: " + e.getMessage());
        }
    }

    public String removeFriend(Long userId, Long friendId) {
        try {
            if (userId == null || friendId == null) {
                throw new IllegalArgumentException("userId/friendId不能为空");
            }
            if (userId.equals(friendId)) {
                throw new IllegalArgumentException("不能删除自己");
            }

            removeUserFriendsRelationshipRecords(userId, friendId, FRIEND_RELATION_FUNCTION_ID);
            removeUserFriendsRelationshipRecords(friendId, userId, FRIEND_RELATION_FUNCTION_ID);

            removeUserFriendsRelationshipRecords(userId, friendId, FRIEND_REQUEST_FUNCTION_ID);
            removeUserFriendsRelationshipRecords(friendId, userId, FRIEND_REQUEST_FUNCTION_ID);

            removeChatListRecords(userId, friendId);
            removeChatListRecords(friendId, userId);

            removeFriendsProfileRecords(userId, friendId);
            removeFriendsProfileRecords(friendId, userId);

            return "删除好友成功";
        } catch (Exception e) {
            System.err.println("UserEntityService: 删除好友失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("删除好友失败: " + e.getMessage());
        }
    }

    private void removeUserFriendsRelationshipRecords(Long userId, Long friendId, Long functionId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("friends_id", friendId)
                .eq("function_id", functionId);
        List<UserFriendsRelationship_njj> records = userFriendsRelationshipMapper.selectList(wrapper);
        if (records == null || records.isEmpty()) {
            return;
        }

        for (UserFriendsRelationship_njj r : records) {
            if (r == null || r.getUserFriendsInfoId() == null) {
                continue;
            }
            userFriendsRelationshipMapper.deleteById(r.getUserFriendsInfoId());
            UserFriendsRelationship_list_njj.removeFromCacheById(r.getUserFriendsInfoId());
        }
    }

    private void removeChatListRecords(Long userId, Long friendId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserChatList_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("friend_id", friendId);
        List<UserChatList_njj> records = userChatListMapper.selectList(wrapper);
        if (records == null || records.isEmpty()) {
            return;
        }

        for (UserChatList_njj r : records) {
            if (r == null || r.getId() == null) {
                continue;
            }
            userChatListMapper.deleteById(r.getId());
            UserChatList_list_njj.removeFromCacheById(r.getId());
        }
    }

    private void removeFriendsProfileRecords(Long userId, Long friendId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<FriendsProfile_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("friend_id", friendId);
        List<FriendsProfile_njj> records = friendsProfileMapper.selectList(wrapper);
        if (records == null || records.isEmpty()) {
            return;
        }

        for (FriendsProfile_njj r : records) {
            if (r == null || r.getFriendsProfileId() == null) {
                continue;
            }
            friendsProfileMapper.deleteById(r.getFriendsProfileId());
            FriendsProfile_list_njj.removeFromCacheById(r.getFriendsProfileId());
        }
    }

    public List<Map<String, Object>> getFriendRequestsInbox(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> wrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.eq("friends_id", userId).eq("function_id", FRIEND_REQUEST_FUNCTION_ID);
            List<UserFriendsRelationship_njj> records = userFriendsRelationshipMapper.selectList(wrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (UserFriendsRelationship_njj record : records) {
                Map<String, Object> parsed = parseJsonSettings(record.getFunctionSettings());
                String status = String.valueOf(parsed.getOrDefault("status", ""));
                if (!"PENDING".equalsIgnoreCase(status)) {
                    continue;
                }

                Map<String, Object> item = new HashMap<>();
                item.put("requestId", record.getUserFriendsInfoId());
                item.put("fromUserId", record.getUserId());
                item.put("toUserId", record.getFriendsId());
                item.put("status", "PENDING");
                item.put("message", parsed.getOrDefault("message", ""));
                item.put("source", parsed.getOrDefault("source", ""));
                item.put("createdAt", record.getCreatedAt());

                UserInfo_njj fromUser = UserInfo_list_njj.getUserById(record.getUserId());
                if (fromUser != null) {
                    item.put("fromUsername", fromUser.getUsername());
                    item.put("fromAvatarUrl", fromUser.getProfilePicUrl());
                }

                result.add(item);
            }

            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 获取收到的好友申请失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取好友申请失败: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getFriendRequestsOutbox(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> wrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.eq("user_id", userId).eq("function_id", FRIEND_REQUEST_FUNCTION_ID);
            List<UserFriendsRelationship_njj> records = userFriendsRelationshipMapper.selectList(wrapper);

            List<Map<String, Object>> result = new ArrayList<>();
            for (UserFriendsRelationship_njj record : records) {
                Map<String, Object> parsed = parseJsonSettings(record.getFunctionSettings());
                String status = String.valueOf(parsed.getOrDefault("status", ""));

                Map<String, Object> item = new HashMap<>();
                item.put("requestId", record.getUserFriendsInfoId());
                item.put("fromUserId", record.getUserId());
                item.put("toUserId", record.getFriendsId());
                item.put("status", status);
                item.put("message", parsed.getOrDefault("message", ""));
                item.put("source", parsed.getOrDefault("source", ""));
                item.put("createdAt", record.getCreatedAt());

                UserInfo_njj toUser = UserInfo_list_njj.getUserById(record.getFriendsId());
                if (toUser != null) {
                    item.put("toUsername", toUser.getUsername());
                    item.put("toAvatarUrl", toUser.getProfilePicUrl());
                }

                result.add(item);
            }

            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 获取发出的好友申请失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取好友申请失败: " + e.getMessage());
        }
    }

    public String acceptFriendRequest(Long requestId) {
        try {
            if (requestId == null) {
                throw new IllegalArgumentException("requestId不能为空");
            }

            UserFriendsRelationship_njj record = userFriendsRelationshipMapper.selectById(requestId);
            if (record == null || record.getFunctionId() == null || !record.getFunctionId().equals(FRIEND_REQUEST_FUNCTION_ID)) {
                throw new RuntimeException("好友申请不存在");
            }

            Map<String, Object> parsed = parseJsonSettings(record.getFunctionSettings());
            String status = String.valueOf(parsed.getOrDefault("status", ""));
            if (!"PENDING".equalsIgnoreCase(status)) {
                return "申请已处理";
            }

            parsed.put("status", "ACCEPTED");
            record.setFunctionSettings(OBJECT_MAPPER.writeValueAsString(parsed));
            record.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userFriendsRelationshipMapper.updateById(record);
            UserFriendsRelationship_list_njj.updateInCache(record);

            createFriendRelationBidirectional(record.getUserId(), record.getFriendsId());
            return "已同意好友申请";
        } catch (Exception e) {
            System.err.println("UserEntityService: 同意好友申请失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("同意好友申请失败: " + e.getMessage());
        }
    }

    public String rejectFriendRequest(Long requestId) {
        try {
            if (requestId == null) {
                throw new IllegalArgumentException("requestId不能为空");
            }

            UserFriendsRelationship_njj record = userFriendsRelationshipMapper.selectById(requestId);
            if (record == null || record.getFunctionId() == null || !record.getFunctionId().equals(FRIEND_REQUEST_FUNCTION_ID)) {
                throw new RuntimeException("好友申请不存在");
            }

            Map<String, Object> parsed = parseJsonSettings(record.getFunctionSettings());
            String status = String.valueOf(parsed.getOrDefault("status", ""));
            if (!"PENDING".equalsIgnoreCase(status)) {
                return "申请已处理";
            }

            parsed.put("status", "REJECTED");
            record.setFunctionSettings(OBJECT_MAPPER.writeValueAsString(parsed));
            record.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userFriendsRelationshipMapper.updateById(record);
            UserFriendsRelationship_list_njj.updateInCache(record);

            return "已拒绝好友申请";
        } catch (Exception e) {
            System.err.println("UserEntityService: 拒绝好友申请失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("拒绝好友申请失败: " + e.getMessage());
        }
    }

    private boolean isAddWayAllowed(Long targetUserId, String source) {
        if (targetUserId == null) {
            return false;
        }

        String normalized = source == null ? "" : source.trim().toUpperCase();
        String settingKey;
        switch (normalized) {
            case "RECOMMEND":
                settingKey = "privacy.recommendFriends";
                break;
            case "WECHAT_SEARCH":
                settingKey = "privacy.addWays.wechatSearch";
                break;
            case "QQ_SEARCH":
                settingKey = "privacy.addWays.qqSearch";
                break;
            case "GROUP_ADD":
                settingKey = "privacy.addWays.groupAdd";
                break;
            case "CARD_SHARE":
                settingKey = "privacy.addWays.cardShare";
                break;
            case "PHONE_SEARCH":
            default:
                settingKey = "privacy.addWays.phoneSearch";
                break;
        }

        return getUserSettingBoolean(targetUserId, settingKey, true);
    }

    public List<Map<String, Object>> getFriendRecommendations(Long userId, Integer limit) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }

            int finalLimit = (limit == null || limit <= 0) ? 20 : Math.min(limit, 100);

            boolean recommendEnabled = getUserSettingBoolean(userId, "privacy.recommendFriends", true);
            if (!recommendEnabled) {
                return new ArrayList<>();
            }

            Set<Long> myFriendIds = new HashSet<>();
            List<UserFriendsRelationship_njj> myRelations = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId);
            if (myRelations != null) {
                for (UserFriendsRelationship_njj r : myRelations) {
                    if (r == null || r.getFunctionId() == null || r.getFriendsId() == null) {
                        continue;
                    }
                    if (FRIEND_RELATION_FUNCTION_ID.equals(r.getFunctionId())) {
                        myFriendIds.add(r.getFriendsId());
                    }
                }
            }

            Map<Long, Integer> mutualCount = new HashMap<>();
            for (Long friendId : myFriendIds) {
                if (friendId == null) {
                    continue;
                }
                List<UserFriendsRelationship_njj> friendRelations = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(friendId);
                if (friendRelations == null) {
                    continue;
                }

                for (UserFriendsRelationship_njj rel : friendRelations) {
                    if (rel == null || rel.getFunctionId() == null || rel.getFriendsId() == null) {
                        continue;
                    }
                    if (!FRIEND_RELATION_FUNCTION_ID.equals(rel.getFunctionId())) {
                        continue;
                    }

                    Long candidateId = rel.getFriendsId();
                    if (candidateId.equals(userId)) {
                        continue;
                    }
                    if (myFriendIds.contains(candidateId)) {
                        continue;
                    }
                    mutualCount.put(candidateId, mutualCount.getOrDefault(candidateId, 0) + 1);
                }
            }

            List<Map.Entry<Long, Integer>> sorted = new ArrayList<>(mutualCount.entrySet());
            sorted.sort((a, b) -> {
                int cmp = Integer.compare(b.getValue(), a.getValue());
                if (cmp != 0) {
                    return cmp;
                }
                return Long.compare(a.getKey(), b.getKey());
            });

            List<Map<String, Object>> result = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : sorted) {
                if (result.size() >= finalLimit) {
                    break;
                }
                Long candidateId = entry.getKey();

                if (candidateId == null) {
                    continue;
                }

                if (BlockRecord_list_njj.isBlocked(userId, candidateId)
                        || BlockRecord_list_njj.isBlocked(candidateId, userId)) {
                    continue;
                }

                if (hasPendingFriendRequestBetween(userId, candidateId)) {
                    continue;
                }

                boolean candidateRecommendEnabled = getUserSettingBoolean(candidateId, "privacy.recommendFriends", true);
                if (!candidateRecommendEnabled) {
                    continue;
                }

                UserInfo_njj info = UserInfo_list_njj.getUserById(candidateId);
                if (info == null) {
                    continue;
                }

                Map<String, Object> item = new HashMap<>();
                item.put("userId", info.getUserId());
                item.put("username", info.getUsername());
                item.put("realName", info.getRealName());
                item.put("profilePicUrl", info.getProfilePicUrl());
                item.put("mutualCount", entry.getValue());
                result.add(item);
            }

            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 获取好友推荐失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取好友推荐失败: " + e.getMessage());
        }
    }

    private boolean hasPendingFriendRequestBetween(Long userId1, Long userId2) {
        if (userId1 == null || userId2 == null) {
            return false;
        }

        List<UserFriendsRelationship_njj> relations1 = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId1);
        if (relations1 != null) {
            for (UserFriendsRelationship_njj r : relations1) {
                if (r == null || r.getFunctionId() == null) {
                    continue;
                }
                if (!FRIEND_REQUEST_FUNCTION_ID.equals(r.getFunctionId())) {
                    continue;
                }
                if (r.getFriendsId() == null || !r.getFriendsId().equals(userId2)) {
                    continue;
                }

                Map<String, Object> parsed = parseJsonSettings(r.getFunctionSettings());
                String status = String.valueOf(parsed.getOrDefault("status", ""));
                if ("PENDING".equalsIgnoreCase(status)) {
                    return true;
                }
            }
        }

        List<UserFriendsRelationship_njj> relations2 = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId2);
        if (relations2 != null) {
            for (UserFriendsRelationship_njj r : relations2) {
                if (r == null || r.getFunctionId() == null) {
                    continue;
                }
                if (!FRIEND_REQUEST_FUNCTION_ID.equals(r.getFunctionId())) {
                    continue;
                }
                if (r.getFriendsId() == null || !r.getFriendsId().equals(userId1)) {
                    continue;
                }

                Map<String, Object> parsed = parseJsonSettings(r.getFunctionSettings());
                String status = String.valueOf(parsed.getOrDefault("status", ""));
                if ("PENDING".equalsIgnoreCase(status)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean getUserSettingBoolean(Long userId, String dbSettingKey, boolean defaultValue) {
        try {
            if (userId == null || dbSettingKey == null) {
                return defaultValue;
            }

            SettingBase_wlq settingBase = findSettingBaseByKey(dbSettingKey);
            if (settingBase == null) {
                settingBase = createSettingBaseIfMissing(dbSettingKey);
            }
            if (settingBase == null) {
                return defaultValue;
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserSettingRelation_njj> queryWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("setting_id", settingBase.getId());
            UserSettingRelation_njj relation = userSettingRelationMapper.selectOne(queryWrapper);
            if (relation != null && relation.getValueText() != null) {
                String valueText = relation.getValueText().trim();
                return "true".equalsIgnoreCase(valueText) || "1".equals(valueText) || "\"true\"".equalsIgnoreCase(valueText);
            }

            String defaultVal = settingBase.getDefaultValue();
            if (defaultVal != null) {
                if ("privacy.allowFriendsAt".equals(settingBase.getSettingKey())) {
                    return false;
                }
                return "true".equalsIgnoreCase(defaultVal.trim()) || "1".equals(defaultVal.trim());
            }

            return defaultValue;
        } catch (Exception e) {
            System.err.println("UserEntityService: 获取用户设置值失败: " + e.getMessage());
            return defaultValue;
        }
    }

    public String saveUserMinorModeEnabled(Long userId, Boolean enabled) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("userId不能为空");
            }

            String dbSettingKey = "mode.minorModeEnabled";

            SettingBase_wlq settingBase = findSettingBaseByKey(dbSettingKey);
            if (settingBase == null) {
                settingBase = createSettingBaseIfMissing(dbSettingKey);
            }
            if (settingBase == null) {
                throw new RuntimeException("设置基础表中未找到设置项: " + dbSettingKey);
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserSettingRelation_njj> queryWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("setting_id", settingBase.getId());
            UserSettingRelation_njj existingRelation = userSettingRelationMapper.selectOne(queryWrapper);

            String valueText = enabled != null && enabled ? "true" : "false";
            java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

            if (existingRelation != null) {
                existingRelation.setValueText(valueText);
                existingRelation.setUpdateTime(now);
                userSettingRelationMapper.updateById(existingRelation);
                UserSettingRelation_list_njj.updateInCache(existingRelation);
            } else {
                UserSettingRelation_njj newRelation = new UserSettingRelation_njj();
                Long newId = generateNextUserSettingRelationId();
                newRelation.setId(newId);
                newRelation.setUserId(userId);
                newRelation.setSettingId(settingBase.getId());
                newRelation.setValueText(valueText);
                newRelation.setCreateTime(now);
                newRelation.setUpdateTime(now);
                userSettingRelationMapper.insert(newRelation);
                UserSettingRelation_list_njj.addToCache(newRelation);
            }

            return "设置保存成功";
        } catch (Exception e) {
            System.err.println("UserEntityService: 保存未成年人模式设置失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("保存未成年人模式设置失败: " + e.getMessage());
        }
    }

    private boolean isAlreadyFriend(Long userId1, Long userId2) {
        List<UserFriendsRelationship_njj> relations = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId1);
        for (UserFriendsRelationship_njj r : relations) {
            if (r != null && userId2.equals(r.getFriendsId())
                    && r.getFunctionId() != null
                    && r.getFunctionId().equals(FRIEND_RELATION_FUNCTION_ID)) {
                return true;
            }
        }
        List<UserFriendsRelationship_njj> relations2 = UserFriendsRelationship_list_njj.getUserFriendsRelationshipByUserId(userId2);
        for (UserFriendsRelationship_njj r : relations2) {
            if (r != null && userId1.equals(r.getFriendsId())
                    && r.getFunctionId() != null
                    && r.getFunctionId().equals(FRIEND_RELATION_FUNCTION_ID)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> createOrGetChatSession(Long userId, Long friendId) {
        try {
            if (userId == null || friendId == null) {
                throw new IllegalArgumentException("userId/friendId不能为空");
            }
            if (userId.equals(friendId)) {
                throw new IllegalArgumentException("不能与自己创建会话");
            }

            if (!isAlreadyFriend(userId, friendId)) {
                throw new IllegalArgumentException("非好友关系，无法创建会话");
            }

            UserChatList_njj existing = getChatListRecord(userId, friendId);
            if (existing != null && existing.getSessionId() != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("sessionId", existing.getSessionId());
                result.put("mode", "EXISTING");
                return result;
            }

            ensureChatListBidirectional(userId, friendId);

            UserChatList_njj created = getChatListRecord(userId, friendId);
            if (created == null || created.getSessionId() == null) {
                throw new RuntimeException("会话创建失败");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", created.getSessionId());
            result.put("mode", "CREATED");
            return result;
        } catch (Exception e) {
            System.err.println("UserEntityService: 创建或获取会话失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("创建或获取会话失败: " + e.getMessage());
        }
    }

    private UserFriendsRelationship_njj findPendingFriendRequest(Long fromUserId, Long toUserId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", fromUserId).eq("friends_id", toUserId).eq("function_id", FRIEND_REQUEST_FUNCTION_ID);
        List<UserFriendsRelationship_njj> records = userFriendsRelationshipMapper.selectList(wrapper);
        for (UserFriendsRelationship_njj record : records) {
            Map<String, Object> parsed = parseJsonSettings(record.getFunctionSettings());
            String status = String.valueOf(parsed.getOrDefault("status", ""));
            if ("PENDING".equalsIgnoreCase(status)) {
                return record;
            }
        }
        return null;
    }

    private void createFriendRelationBidirectional(Long userId1, Long userId2) throws Exception {
        createFriendRelationOneWayIfAbsent(userId1, userId2);
        createFriendRelationOneWayIfAbsent(userId2, userId1);

        try {
            ensureChatListBidirectional(userId1, userId2);
        } catch (Exception e) {
            System.err.println("UserEntityService: 创建聊天列表失败: " + e.getMessage());
        }
    }

    private void ensureChatListBidirectional(Long userId1, Long userId2) {
        if (userId1 == null || userId2 == null) {
            return;
        }

        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

        UserChatList_njj u1ToU2 = getChatListRecord(userId1, userId2);
        UserChatList_njj u2ToU1 = getChatListRecord(userId2, userId1);

        Long sessionId = null;
        if (u1ToU2 != null && u1ToU2.getSessionId() != null) {
            sessionId = u1ToU2.getSessionId();
        } else if (u2ToU1 != null && u2ToU1.getSessionId() != null) {
            sessionId = u2ToU1.getSessionId();
        } else {
            sessionId = System.currentTimeMillis();
        }

        if (u1ToU2 == null) {
            UserChatList_njj record = buildChatListRecord(userId1, userId2, sessionId, now);
            userChatListMapper.insert(record);
            UserChatList_list_njj.addToCache(record);
        } else if (u1ToU2.getSessionId() == null || !u1ToU2.getSessionId().equals(sessionId)) {
            u1ToU2.setSessionId(sessionId);
            u1ToU2.setUpdatedAt(now);
            userChatListMapper.updateById(u1ToU2);

            UserChatList_list_njj.removeFromCacheById(u1ToU2.getId());
            UserChatList_list_njj.addToCache(u1ToU2);
        }

        if (u2ToU1 == null) {
            UserChatList_njj record = buildChatListRecord(userId2, userId1, sessionId, now);
            userChatListMapper.insert(record);
            UserChatList_list_njj.addToCache(record);
        } else if (u2ToU1.getSessionId() == null || !u2ToU1.getSessionId().equals(sessionId)) {
            u2ToU1.setSessionId(sessionId);
            u2ToU1.setUpdatedAt(now);
            userChatListMapper.updateById(u2ToU1);

            UserChatList_list_njj.removeFromCacheById(u2ToU1.getId());
            UserChatList_list_njj.addToCache(u2ToU1);
        }
    }

    private UserChatList_njj getChatListRecord(Long userId, Long friendId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserChatList_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("friend_id", friendId);
        return userChatListMapper.selectOne(wrapper);
    }

    private UserChatList_njj buildChatListRecord(Long userId, Long friendId, Long sessionId, java.sql.Timestamp now) {
        UserInfo_njj friendInfo = UserInfo_list_njj.getUserById(friendId);
        String friendName = friendInfo != null ?
                (friendInfo.getRealName() != null && !friendInfo.getRealName().trim().isEmpty() ? friendInfo.getRealName() :
                        (friendInfo.getUsername() != null ? friendInfo.getUsername() : ""))
                : "";
        String avatarUrl = friendInfo != null && friendInfo.getProfilePicUrl() != null ? friendInfo.getProfilePicUrl() : "";

        UserChatList_njj record = new UserChatList_njj();
        record.setId(System.currentTimeMillis() + (long) (Math.random() * 1000));
        record.setUserId(userId);
        record.setFriendId(friendId);
        record.setFriendName(friendName);
        record.setAvatarUrl(avatarUrl);
        record.setSessionId(sessionId);
        record.setLastMessage("");
        record.setLastMessageTime(now);
        record.setUnreadCount(0);
        record.setStatus("ACTIVE");
        record.setLastActive(now);
        record.setCreatedAt(now);
        record.setUpdatedAt(now);
        return record;
    }

    private void createFriendRelationOneWayIfAbsent(Long userId, Long friendId) throws Exception {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserFriendsRelationship_njj> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("friends_id", friendId).eq("function_id", FRIEND_RELATION_FUNCTION_ID);
        UserFriendsRelationship_njj exists = userFriendsRelationshipMapper.selectOne(wrapper);
        if (exists != null) {
            return;
        }

        Map<String, Object> settings = new HashMap<>();
        settings.put("since", System.currentTimeMillis());
        String settingsJson = OBJECT_MAPPER.writeValueAsString(settings);

        UserFriendsRelationship_njj relation = new UserFriendsRelationship_njj();
        relation.setUserFriendsInfoId(generateNextUserFriendsId());
        relation.setUserId(userId);
        relation.setFriendsId(friendId);
        relation.setFunctionId(FRIEND_RELATION_FUNCTION_ID);
        relation.setFunctionSettings(settingsJson);
        relation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        relation.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        userFriendsRelationshipMapper.insert(relation);
        UserFriendsRelationship_list_njj.addToCache(relation);
    }

    private Map<String, Object> parseJsonSettings(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return new HashMap<>();
            }
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("raw", json);
            return fallback;
        }
    }

    // UserImageRecommendation 相关方法
    public void loadUserImageRecommendationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户图片推荐数据...");
            long startTime = System.currentTimeMillis();

            var allUserImageRecommendation = userImageRecommendationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserImageRecommendation.size() + " 条记录");

            if (allUserImageRecommendation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户图片推荐数据");
                return;
            }

            UserImageRecommendation_list_njj.loadFromDatabaseDirectly(allUserImageRecommendation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户图片推荐数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户图片推荐数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserImageRecommendation_njj> getAllUserImageRecommendationFromMemory() {
        return UserImageRecommendation_list_njj.getAllUserImageRecommendation();
    }

    public Map<String, Object> getUserImageRecommendationStatisticsFromMemory() {
        return UserImageRecommendation_list_njj.getStatistics();
    }

    // UserInfoFeatureVector 相关方法
    public void loadUserInfoFeatureVectorToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户信息特征向量数据...");
            long startTime = System.currentTimeMillis();

            var allUserInfoFeatureVector = userInfoFeatureVectorMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserInfoFeatureVector.size() + " 条记录");

            if (allUserInfoFeatureVector.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户信息特征向量数据");
                return;
            }

            UserInfoFeatureVector_list_njj.loadFromDatabaseDirectly(allUserInfoFeatureVector);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户信息特征向量数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户信息特征向量数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserInfoFeatureVector_njj> getAllUserInfoFeatureVectorFromMemory() {
        return UserInfoFeatureVector_list_njj.getAllUserInfoFeatureVector();
    }

    public Map<String, Object> getUserInfoFeatureVectorStatisticsFromMemory() {
        return UserInfoFeatureVector_list_njj.getStatistics();
    }

    // UserInfoQuestion 相关方法
    public void loadUserInfoQuestionToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户信息问题数据...");
            long startTime = System.currentTimeMillis();

            var allUserInfoQuestion = userInfoQuestionMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserInfoQuestion.size() + " 条记录");

            if (allUserInfoQuestion.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户信息问题数据");
                return;
            }

            UserInfoQuestion_list_njj.loadFromDatabaseDirectly(allUserInfoQuestion);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户信息问题数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户信息问题数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserInfoQuestion_njj> getAllUserInfoQuestionFromMemory() {
        return UserInfoQuestion_list_njj.getAllUserInfoQuestion();
    }

    public Map<String, Object> getUserInfoQuestionStatisticsFromMemory() {
        return UserInfoQuestion_list_njj.getStatistics();
    }

    // UserNovelRecommendation 相关方法
    public void loadUserNovelRecommendationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户小说推荐数据...");
            long startTime = System.currentTimeMillis();

            var allUserNovelRecommendation = userNovelRecommendationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserNovelRecommendation.size() + " 条记录");

            if (allUserNovelRecommendation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户小说推荐数据");
                return;
            }

            UserNovelRecommendation_list_njj.loadFromDatabaseDirectly(allUserNovelRecommendation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户小说推荐数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户小说推荐数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserNovelRecommendation_njj> getAllUserNovelRecommendationFromMemory() {
        return UserNovelRecommendation_list_njj.getAllUserNovelRecommendation();
    }

    public Map<String, Object> getUserNovelRecommendationStatisticsFromMemory() {
        return UserNovelRecommendation_list_njj.getStatistics();
    }

    // UserNovelRelation 相关方法
    public void loadUserNovelRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户小说关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserNovelRelation = userNovelRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserNovelRelation.size() + " 条记录");

            if (allUserNovelRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户小说关系数据");
                return;
            }

            UserNovelRelation_list_njj.loadFromDatabaseDirectly(allUserNovelRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户小说关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户小说关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserNovelRelation_njj> getAllUserNovelRelationFromMemory() {
        return UserNovelRelation_list_njj.getAllUserNovelRelation();
    }

    public Map<String, Object> getUserNovelRelationStatisticsFromMemory() {
        return UserNovelRelation_list_njj.getStatistics();
    }

    // UserReviewBase 相关方法
    public void loadUserReviewBaseToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户评论基础信息数据...");
            long startTime = System.currentTimeMillis();

            var allUserReviewBase = userReviewBaseMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserReviewBase.size() + " 条记录");

            if (allUserReviewBase.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户评论基础信息数据");
                return;
            }

            UserReviewBase_list_njj.loadFromDatabaseDirectly(allUserReviewBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户评论基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户评论基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserReviewBase_njj> getAllUserReviewBaseFromMemory() {
        return UserReviewBase_list_njj.getAllUserReviewBase();
    }

    public Map<String, Object> getUserReviewBaseStatisticsFromMemory() {
        return UserReviewBase_list_njj.getStatistics();
    }

    // UserSettingRelation 相关方法
    public void loadUserSettingRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户设置关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserSettingRelation = userSettingRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSettingRelation.size() + " 条记录");

            if (allUserSettingRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户设置关系数据");
                return;
            }

            UserSettingRelation_list_njj.loadFromDatabaseDirectly(allUserSettingRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户设置关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户设置关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSettingRelation_njj> getAllUserSettingRelationFromMemory() {
        return UserSettingRelation_list_njj.getAllUserSettingRelation();
    }

    public Map<String, Object> getUserSettingRelationStatisticsFromMemory() {
        return UserSettingRelation_list_njj.getStatistics();
    }

    // UserSoftTagRelation 相关方法
    public void loadUserSoftTagRelationToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户软标签关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserSoftTagRelation = userSoftTagRelationMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSoftTagRelation.size() + " 条记录");

            if (allUserSoftTagRelation.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户软标签关系数据");
                return;
            }

            UserSoftTagRelation_list_njj.loadFromDatabaseDirectly(allUserSoftTagRelation);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户软标签关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户软标签关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSoftTagRelation_njj> getAllUserSoftTagRelationFromMemory() {
        return UserSoftTagRelation_list_njj.getAllUserSoftTagRelation();
    }

    public Map<String, Object> getUserSoftTagRelationStatisticsFromMemory() {
        return UserSoftTagRelation_list_njj.getStatistics();
    }

    // UserSystemMessage 相关方法
    public void loadUserSystemMessageToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户系统消息数据...");
            long startTime = System.currentTimeMillis();

            var allUserSystemMessage = userSystemMessageMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserSystemMessage.size() + " 条记录");

            if (allUserSystemMessage.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户系统消息数据");
                return;
            }

            UserSystemMessage_list_njj.loadFromDatabaseDirectly(allUserSystemMessage);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户系统消息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户系统消息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserSystemMessage_njj> getAllUserSystemMessageFromMemory() {
        return UserSystemMessage_list_njj.getAllUserSystemMessage();
    }

    public Map<String, Object> getUserSystemMessageStatisticsFromMemory() {
        return UserSystemMessage_list_njj.getStatistics();
    }

    // UserTaskRelationship 相关方法
    public void loadUserTaskRelationshipToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户任务关系数据...");
            long startTime = System.currentTimeMillis();

            var allUserTaskRelationship = userTaskRelationshipMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserTaskRelationship.size() + " 条记录");

            if (allUserTaskRelationship.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户任务关系数据");
                return;
            }

            UserTaskRelationship_list_njj.loadFromDatabaseDirectly(allUserTaskRelationship);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户任务关系数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户任务关系数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserTaskRelationship_njj> getAllUserTaskRelationshipFromMemory() {
        return UserTaskRelationship_list_njj.getAllUserTaskRelationship();
    }

    public Map<String, Object> getUserTaskRelationshipStatisticsFromMemory() {
        return UserTaskRelationship_list_njj.getStatistics();
    }

    // UserAiComment 相关方法
    public void loadUserAiCommentToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户AI评论数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiComment = userAiCommentMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserAiComment.size() + " 条记录");

            if (allUserAiComment.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户AI评论数据");
                return;
            }

            UserAiComment_list_njj.loadFromDatabaseDirectly(allUserAiComment);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户AI评论数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户AI评论数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiComment_njj> getAllUserAiCommentFromMemory() {
        return UserAiComment_list_njj.getAllUserAiComment();
    }

    public Map<String, Object> getUserAiCommentStatisticsFromMemory() {
        return UserAiComment_list_njj.getStatistics();
    }

    public List<UserAiComment_njj> getUserAiCommentByAimodelIdFromMemory(Long aimodelId) {
        return UserAiComment_list_njj.getUserAiCommentByAimodelId(aimodelId);
    }

    public Map<String, Object> saveUserAiComment(Map<String, Object> requestData) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long aimodelId = Long.valueOf(requestData.get("aimodelId").toString());
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String commentText = requestData.get("commentText").toString();
            Byte stars = Byte.valueOf(requestData.get("stars").toString());

            System.out.println("UserEntityService: 开始保存AI评价，aimodelId: " + aimodelId + ", userId: " + userId);

            // 创建评价对象
            UserAiComment_njj comment = new UserAiComment_njj();
            comment.setUserAiCommentId(generateNextUserAiCommentId());
            comment.setAimodelId(aimodelId);
            comment.setUserId(userId);
            comment.setCommentText(commentText);
            comment.setStars(stars);
            comment.setLikeCount(0L);
            comment.setIsVisible((byte) 1);
            comment.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int insertResult = userAiCommentMapper.insert(comment);
            if (insertResult > 0) {
                // 更新内存缓存
                UserAiComment_list_njj.addToCache(comment);

                result.put("success", true);
                result.put("message", "评价保存成功");
                result.put("commentId", comment.getUserAiCommentId());

                System.out.println("UserEntityService: AI评价保存成功，commentId: " + comment.getUserAiCommentId());
            } else {
                result.put("success", false);
                result.put("message", "评价保存失败，请重试");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "保存AI评价时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 生成下一个用户AI评价ID
     */
    private Long generateNextUserAiCommentId() {
        try {
            Long maxId = userAiCommentMapper.selectMaxUserAiCommentId();
            if (maxId == null) {
                return 10000001L;
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成用户AI评价ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    // UserAiModel 相关方法
    public void loadUserAiModelToMemory() {
        try {
            System.out.println("UserEntityService: 开始从数据库加载用户AI模型数据...");
            long startTime = System.currentTimeMillis();

            var allUserAiModel = userAiModelMapper.selectAllRecords();
            System.out.println("UserEntityService: 原生SQL查询到 " + allUserAiModel.size() + " 条记录");

            if (allUserAiModel.isEmpty()) {
                System.out.println("UserEntityService: 数据库中没有用户AI模型数据");
                return;
            }

            UserAiModel_list_njj.loadFromDatabaseDirectly(allUserAiModel);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserEntityService: 用户AI模型数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserEntityService: 加载用户AI模型数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<UserAiModel_njj> getAllUserAiModelFromMemory() {
        return UserAiModel_list_njj.getAllUserAiModel();
    }

    public Map<String, Object> getUserAiModelStatisticsFromMemory() {
        return UserAiModel_list_njj.getStatistics();
    }

    /**
     * 购买AI模型
     */
    public Long purchaseAiModel(Long userId, Long parentModelId, String modelName, String modelDesc,
            String modelImageUrl, BigDecimal price) {
        try {
            // 1. 检查用户是否已拥有该模型（核心：解决唯一索引冲突）
            QueryWrapper<UserAiModel_njj> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .eq("model_name", modelName)
                    .eq("deleted_at", null); // 排除已删除的记录
            UserAiModel_njj existingModel = userAiModelMapper.selectOne(queryWrapper);

            if (existingModel != null) {
                System.err.println("购买AI模型失败：用户已拥有该模型！userId=" + userId + ", modelName=" + modelName);
                // 返回特殊值（如-1），让Controller识别并返回"已购买"提示
                return -1L;
            }
            // 2. 检查用户金币余额
            UserInfo_njj user = userInfoService.getUserByIdFromMemory(userId);
            if (user == null) {
                // 缓存中无用户，尝试加载所有用户数据到内存
                System.out.println("缓存中无用户，尝试加载数据到内存...");
                userInfoService.loadUserInfoToMemory();
                // 重新查询
                user = userInfoService.getUserByIdFromMemory(userId);
                if (user == null) {
                    System.err.println("用户不存在: userId=" + userId);
                    return null;
                }
            }

            Long currentGold = user.getGold() != null ? user.getGold() : 0L;
            Long priceInCoins = Math.round(price.doubleValue() * 100); // 转换为金币

            if (currentGold < priceInCoins) {
                System.err.println(
                        "用户金币不足: userId=" + userId + ", currentGold=" + currentGold + ", required=" + priceInCoins);
                return null;
            }

            // 3. 生成新的userAiId
            Long userAiId = generateNextUserAiId();

            // 4. 创建用户AI模型记录
            UserAiModel_njj userAiModel = new UserAiModel_njj();
            userAiModel.setUserAiId(userAiId);
            userAiModel.setUserId(userId);
            userAiModel.setParentModelId(parentModelId);
            userAiModel.setParentModelVersion("1.0"); // 默认版本
            userAiModel.setModelName(modelName);
            userAiModel.setCustomDesc(modelDesc);
            userAiModel.setModelImageUrl(modelImageUrl);
            userAiModel.setTone("友好"); // 默认语调
            userAiModel.setQuestionWeight("1.0"); // 默认权重
            userAiModel.setQuestionContent(""); // 默认问题内容
            userAiModel.setPower(1); // 默认能力值
            userAiModel.setLevel(1); // 默认等级
            userAiModel.setTotalExp(0); // 默认经验值
            userAiModel.setRechargeAmount(price); // 充值金额
            userAiModel.setIsVisible((byte) 1); // 可见
            userAiModel.setStatus((byte) 1); // 正常状态
            userAiModel.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userAiModel.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            userAiModel.setDeletedAt(null);
            userAiModel.setTryOutAt(null);
            userAiModel.setTryOutNum(0L);

            // 5. 保存AI模型到数据库
            int result = userAiModelMapper.insert(userAiModel);
            if (result <= 0) {
                System.err.println("AI模型购买失败，数据库插入失败");
                return null;
            }

            // 6. 扣除用户金币
            Long newGoldAmount = currentGold - priceInCoins;
            boolean goldUpdated = userInfoService.updateUserGold(userId, newGoldAmount);
            if (!goldUpdated) {
                System.err.println("扣除用户金币失败，回滚AI模型购买");
                // 回滚：删除已插入的AI模型记录
                userAiModelMapper.deleteById(userAiId);
                return null;
            }

            // 7. 创建购买记录
            Long recordId = generateNextPurchaseRecordId();
            PurchaseRecord_njj purchaseRecord = new PurchaseRecord_njj();
            purchaseRecord.setRecordId(recordId);
            purchaseRecord.setUserId(userId);
            purchaseRecord.setItemType("AI_MODEL");
            purchaseRecord.setItemId(userAiId);
            purchaseRecord.setItemName(modelName);
            purchaseRecord.setCoinsSpent(priceInCoins.intValue());
            purchaseRecord.setCoinsBalance(newGoldAmount.intValue());
            purchaseRecord.setPurchasePath("AI_STORE");
            purchaseRecord.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            int purchaseRecordResult = purchaseRecordMapper.insert(purchaseRecord);
            if (purchaseRecordResult <= 0) {
                System.err.println("保存购买记录失败，但购买已完成");
            }

            // 8. 添加到内存缓存
            UserAiModel_list_njj.addToCache(userAiModel);

            System.out.println("AI模型购买成功: userAiId=" + userAiId + ", userId=" + userId +
                    ", price=" + priceInCoins + ", newGold=" + newGoldAmount);
            return userAiId;

        } catch (Exception e) {
            System.err.println("购买AI模型失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成下一个userAiId
     */
    private Long generateNextUserAiId() {
        try {
            Long maxId = userAiModelMapper.selectMaxUserAiId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成userAiId失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 生成下一个购买记录ID
     */
    private Long generateNextPurchaseRecordId() {
        try {
            Long maxId = purchaseRecordMapper.selectMaxRecordId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成购买记录ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 获取朋友资料
     */
    public Map<String, Object> getFriendsProfile(Long userId, Long friendId) {
        try {
            // 从内存中查找朋友资料
            List<FriendsProfile_njj> profiles = FriendsProfile_list_njj.getFriendsProfileByUserId(userId);
            for (FriendsProfile_njj profile : profiles) {
                if (profile.getFriendId().equals(friendId)) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("friendsProfile", profile.getFriendsProfile());
                    result.put("createdAt", profile.getCreatedAt());
                    return result;
                }
            }

            // 如果内存中没有，返回空对象
            Map<String, Object> result = new HashMap<>();
            result.put("friendsProfile", "");
            return result;
        } catch (Exception e) {
            System.err.println("获取朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 保存朋友资料
     */
    public Map<String, Object> saveFriendsProfile(Map<String, Object> profileData) {
        try {
            Long userId = Long.valueOf(profileData.get("userId").toString());
            Long friendId = Long.valueOf(profileData.get("friendId").toString());
            String friendsProfile = profileData.get("friendsProfile").toString();

            // 检查是否已存在记录
            List<FriendsProfile_njj> existingProfiles = FriendsProfile_list_njj.getFriendsProfileByUserId(userId);
            FriendsProfile_njj existingProfile = null;
            for (FriendsProfile_njj profile : existingProfiles) {
                if (profile.getFriendId().equals(friendId)) {
                    existingProfile = profile;
                    break;
                }
            }

            if (existingProfile != null) {
                // 更新现有记录
                existingProfile.setFriendsProfile(friendsProfile);
                existingProfile.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                // 更新数据库
                int result = friendsProfileMapper.updateById(existingProfile);
                if (result > 0) {
                    // 更新内存缓存
                    FriendsProfile_list_njj.addToCache(existingProfile);
                    System.out.println("朋友资料更新成功: userId=" + userId + ", friendId=" + friendId);
                }
            } else {
                // 创建新记录
                Long friendsProfileId = generateNextFriendsProfileId();
                FriendsProfile_njj newProfile = new FriendsProfile_njj();
                newProfile.setFriendsProfileId(friendsProfileId);
                newProfile.setUserId(userId);
                newProfile.setFriendId(friendId);
                newProfile.setFriendsProfile(friendsProfile);
                newProfile.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

                // 保存到数据库
                int result = friendsProfileMapper.insert(newProfile);
                if (result > 0) {
                    // 添加到内存缓存
                    FriendsProfile_list_njj.addToCache(newProfile);
                    System.out.println("朋友资料创建成功: userId=" + userId + ", friendId=" + friendId);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "朋友资料保存成功");
            return result;

        } catch (Exception e) {
            System.err.println("保存朋友资料失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "保存朋友资料失败: " + e.getMessage());
            return result;
        }
    }

    /**
     * 生成下一个朋友资料ID
     */
    private Long generateNextFriendsProfileId() {
        try {
            Long maxId = friendsProfileMapper.selectMaxFriendsProfileId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成朋友资料ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    // ==================== 隐私设置相关方法 ====================

    /**
     * 获取用户隐私设置
     * @param userId 用户ID
     * @return 隐私设置Map，key为设置键名（如oneClickProtection），value为布尔值
     */
    public Map<String, Boolean> getUserPrivacySettings(Long userId) {
        try {
            System.out.println("UserEntityService: 开始获取用户隐私设置，userId=" + userId);

            Map<String, Boolean> settings = new HashMap<>();

            // 定义设置键映射（前端key -> 数据库settingKey）
            Map<String, String> settingKeyMap = new HashMap<>();
            settingKeyMap.put("oneClickProtection", "privacy.oneClickProtection");
            settingKeyMap.put("allowFriendsComment", "privacy.allowFriendsComment");
            settingKeyMap.put("allowFriendsAt", "privacy.allowFriendsAt");
            settingKeyMap.put("publicCollections", "privacy.publicCollections");
            settingKeyMap.put("friendVerification", "privacy.friendVerification");
            settingKeyMap.put("recommendFriends", "privacy.recommendFriends");
            settingKeyMap.put("wechatSearch", "privacy.addWays.wechatSearch");
            settingKeyMap.put("phoneSearch", "privacy.addWays.phoneSearch");
            settingKeyMap.put("qqSearch", "privacy.addWays.qqSearch");
            settingKeyMap.put("groupAdd", "privacy.addWays.groupAdd");
            settingKeyMap.put("cardShare", "privacy.addWays.cardShare");

            // 获取用户的所有设置关系
            List<UserSettingRelation_njj> userSettings = UserSettingRelation_list_njj
                    .getUserSettingRelationByUserId(userId);

            // 遍历所有可能的隐私设置项
            for (Map.Entry<String, String> entry : settingKeyMap.entrySet()) {
                String frontendKey = entry.getKey();
                String dbSettingKey = entry.getValue();

                // 从设置基础表中查找设置项ID（先尝试从数据库查询，如果没有加载到内存则直接查询数据库）
                SettingBase_wlq settingBase = findSettingBaseByKey(dbSettingKey);

                if (settingBase == null) {
                    settingBase = createSettingBaseIfMissing(dbSettingKey);
                    if (settingBase == null) {
                        // 如果设置基础表中没有该设置项，使用默认值false
                        System.out.println("警告: 设置基础表中未找到设置项: " + dbSettingKey + "，使用默认值false");
                        settings.put(frontendKey, false);
                        continue;
                    }
                }

                // 查找用户的该设置值
                Boolean settingValue = false; // 默认值

                // 如果用户没有设置过，优先使用设置基础表中的默认值
                String defaultVal = settingBase.getDefaultValue();
                if (defaultVal != null) {
                    if ("privacy.allowFriendsAt".equals(settingBase.getSettingKey())) {
                        // 沿用PrivacySettingsUtil的语义：新用户默认允许任何人@
                        settingValue = false;
                    } else {
                        settingValue = "true".equalsIgnoreCase(defaultVal.trim()) || "1".equals(defaultVal.trim());
                    }
                }

                for (UserSettingRelation_njj relation : userSettings) {
                    if (relation.getSettingId().equals(settingBase.getId())) {
                        String valueText = relation.getValueText();
                        // 解析布尔值，支持"true"/"false"字符串
                        if (valueText != null) {
                            settingValue = "true".equalsIgnoreCase(valueText.trim()) ||
                                          "1".equals(valueText.trim()) ||
                                          "\"true\"".equalsIgnoreCase(valueText.trim());
                        }
                        break;
                    }
                }

                settings.put(frontendKey, settingValue);
            }

            System.out.println("UserEntityService: 获取用户隐私设置完成，共" + settings.size() + "项");
            return settings;

        } catch (Exception e) {
            System.err.println("UserEntityService: 获取用户隐私设置失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取隐私设置失败: " + e.getMessage());
        }
    }

    /**
     * 保存用户隐私设置（单个设置）
     * 当设置"一键防护"时，会联动设置其他相关隐私选项：
     * - 开启"一键防护"时：
     *   - 开启"仅允许好友评论"
     *   - 开启"仅允许好友@"
     *   - 关闭"公开收藏"
     * - 关闭"一键防护"时：
     *   - 关闭"仅允许好友评论"
     *   - 关闭"仅允许好友@"
     *   - 开启"公开收藏"
     * 注意：拒绝申请和分享暂未实现
     * @param userId 用户ID
     * @param settingKey 设置键名（前端key，如oneClickProtection）
     * @param value 设置值
     * @return 保存结果消息
     */
    public String saveUserPrivacySetting(Long userId, String settingKey, Boolean value) {
        try {
            System.out.println("UserEntityService: 开始保存用户隐私设置，userId=" + userId 
                    + ", settingKey=" + settingKey + ", value=" + value);

            // 定义设置键映射
            Map<String, String> settingKeyMap = new HashMap<>();
            settingKeyMap.put("oneClickProtection", "privacy.oneClickProtection");
            settingKeyMap.put("allowFriendsComment", "privacy.allowFriendsComment");
            settingKeyMap.put("allowFriendsAt", "privacy.allowFriendsAt");
            settingKeyMap.put("publicCollections", "privacy.publicCollections");

            settingKeyMap.put("friendVerification", "privacy.friendVerification");
            settingKeyMap.put("recommendFriends", "privacy.recommendFriends");

            settingKeyMap.put("wechatSearch", "privacy.addWays.wechatSearch");
            settingKeyMap.put("phoneSearch", "privacy.addWays.phoneSearch");
            settingKeyMap.put("qqSearch", "privacy.addWays.qqSearch");
            settingKeyMap.put("groupAdd", "privacy.addWays.groupAdd");
            settingKeyMap.put("cardShare", "privacy.addWays.cardShare");

            // 特殊处理"一键防护"设置，需要联动其他设置
            if ("oneClickProtection".equals(settingKey) && value != null) {
                // 一键防护开启时，联动设置其他隐私选项
                if (value) {
                    // 开启"仅允许好友评论"
                    saveSinglePrivacySetting(userId, "allowFriendsComment", true, settingKeyMap);
                    // 开启"仅允许好友@"
                    saveSinglePrivacySetting(userId, "allowFriendsAt", true, settingKeyMap);
                    // 关闭"公开收藏"
                    saveSinglePrivacySetting(userId, "publicCollections", false, settingKeyMap);
                } else {
                    // 关闭"仅允许好友评论"
                    saveSinglePrivacySetting(userId, "allowFriendsComment", false, settingKeyMap);
                    // 关闭"仅允许好友@"
                    saveSinglePrivacySetting(userId, "allowFriendsAt", false, settingKeyMap);
                    // 开启"公开收藏"
                    saveSinglePrivacySetting(userId, "publicCollections", true, settingKeyMap);
                }
            }

            // 保存主设置
            saveSinglePrivacySetting(userId, settingKey, value, settingKeyMap);
            
            return "设置保存成功";

        } catch (Exception e) {
            System.err.println("UserEntityService: 保存用户隐私设置失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("保存隐私设置失败: " + e.getMessage());
        }
    }
    
    /**
     * 保存单个用户隐私设置
     * @param userId 用户ID
     * @param settingKey 设置键名（前端key）
     * @param value 设置值
     * @param settingKeyMap 设置键映射
     */
    private void saveSinglePrivacySetting(Long userId, String settingKey, Boolean value, Map<String, String> settingKeyMap) {
        String dbSettingKey = settingKeyMap.get(settingKey);
        if (dbSettingKey == null) {
            throw new IllegalArgumentException("未知的设置键: " + settingKey);
        }

        // 从设置基础表中查找设置项
        SettingBase_wlq settingBase = findSettingBaseByKey(dbSettingKey);
        if (settingBase == null) {
            settingBase = createSettingBaseIfMissing(dbSettingKey);
            if (settingBase == null) {
                throw new RuntimeException("设置基础表中未找到设置项: " + dbSettingKey);
            }
        }

        // 查找现有的用户设置关系
        // 直接从数据库查询特定用户的特定设置，而不是从缓存中查找
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<UserSettingRelation_njj> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("setting_id", settingBase.getId());
        UserSettingRelation_njj existingRelation = userSettingRelationMapper.selectOne(queryWrapper);

        String valueText = value != null ? (value ? "true" : "false") : "false";
        java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());

        if (existingRelation != null) {
            // 更新现有记录
            existingRelation.setValueText(valueText);
            existingRelation.setUpdateTime(now);
            userSettingRelationMapper.updateById(existingRelation);

            // 更新内存缓存
            UserSettingRelation_list_njj.updateInCache(existingRelation);

            System.out.println("UserEntityService: 更新用户隐私设置成功: userId=" + userId 
                    + ", settingId=" + settingBase.getId() + ", value=" + valueText);
        } else {
            // 创建新记录
            UserSettingRelation_njj newRelation = new UserSettingRelation_njj();
            Long newId = generateNextUserSettingRelationId();
            newRelation.setId(newId);
            newRelation.setUserId(userId);
            newRelation.setSettingId(settingBase.getId());
            newRelation.setValueText(valueText);
            newRelation.setCreateTime(now);
            newRelation.setUpdateTime(now);

            userSettingRelationMapper.insert(newRelation);

            // 添加到内存缓存
            UserSettingRelation_list_njj.addToCache(newRelation);

            System.out.println("UserEntityService: 创建用户隐私设置成功: userId=" + userId 
                    + ", settingId=" + settingBase.getId() + ", value=" + valueText);
        }
    }

    /**
     * 根据设置键查找设置基础数据
     * 在Service层处理缓存失效问题，优先从缓存查找，缓存失效时从数据库查找
     * @param settingKey 设置键
     * @return 设置基础数据
     */
    private SettingBase_wlq findSettingBaseByKey(String settingKey) {
        try {
            // 先尝试从内存缓存中查找
            List<SettingBase_wlq> settingBases = SettingBase_list_wlq.searchSettingBaseBySettingKey(settingKey);
            for (SettingBase_wlq sb : settingBases) {
                if (sb.getSettingKey() != null && sb.getSettingKey().equals(settingKey)) {
                    return sb;
                }
            }
            
            // 如果缓存中没有找到，尝试从数据库中查找
            if (settingBaseMapper != null) {
                com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SettingBase_wlq> wrapper = 
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
                wrapper.eq("setting_key", settingKey);
                SettingBase_wlq dbSetting = settingBaseMapper.selectOne(wrapper);
                if (dbSetting != null) {
                    return dbSetting;
                }
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("UserEntityService: 查找设置基础数据失败: " + e.getMessage());
            return null;
        }
    }

    private SettingBase_wlq createSettingBaseIfMissing(String settingKey) {
        try {
            if (settingKey == null || settingKey.trim().isEmpty()) {
                return null;
            }

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SettingBase_wlq> wrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.eq("setting_key", settingKey);
            SettingBase_wlq existing = settingBaseMapper.selectOne(wrapper);
            if (existing != null) {
                return existing;
            }

            String defaultValue = "false";
            if ("privacy.friendVerification".equals(settingKey)
                    || "privacy.recommendFriends".equals(settingKey)
                    || "privacy.addWays.wechatSearch".equals(settingKey)
                    || "privacy.addWays.phoneSearch".equals(settingKey)
                    || "privacy.addWays.qqSearch".equals(settingKey)
                    || "privacy.addWays.groupAdd".equals(settingKey)
                    || "privacy.addWays.cardShare".equals(settingKey)) {
                defaultValue = "true";
            }

            SettingBase_wlq settingBase = new SettingBase_wlq();
            settingBase.setId(generateNextSettingBaseId());
            settingBase.setSettingKey(settingKey);
            settingBase.setSettingName(settingKey);
            settingBase.setParentId(0L);
            settingBase.setIsActive((byte) 1);
            settingBase.setIsSelectable((byte) 1);
            settingBase.setDataType("bool");
            settingBase.setAllowedValues("[\"true\",\"false\"]");
            settingBase.setDefaultValue(defaultValue);
            settingBase.setDescription(settingKey);
            settingBase.setSortOrder(0);
            java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
            settingBase.setCreateTime(now);
            settingBase.setUpdateTime(now);

            settingBaseMapper.insert(settingBase);
            SettingBase_list_wlq.forceRefresh(settingBaseMapper);
            return settingBase;
        } catch (Exception e) {
            System.err.println("UserEntityService: 自动创建SettingBase失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Long generateNextSettingBaseId() {
        try {
            List<SettingBase_wlq> all = settingBaseMapper.selectList(null);
            Long maxId = null;
            for (SettingBase_wlq sb : all) {
                if (sb != null && sb.getId() != null) {
                    if (maxId == null || sb.getId() > maxId) {
                        maxId = sb.getId();
                    }
                }
            }
            return maxId != null ? maxId + 1 : 10000001L;
        } catch (Exception e) {
            System.err.println("UserEntityService: 生成SettingBase ID失败: " + e.getMessage());
            return System.currentTimeMillis();
        }
    }

    /**
     * 生成下一个用户设置关系ID
     */
    private Long generateNextUserSettingRelationId() {
        try {
            // 查询数据库中的最大ID
            List<UserSettingRelation_njj> allRelations = userSettingRelationMapper.selectList(null);
            Long maxId = null;
            for (UserSettingRelation_njj relation : allRelations) {
                if (relation.getId() != null) {
                    if (maxId == null || relation.getId() > maxId) {
                        maxId = relation.getId();
                    }
                }
            }
            
            if (maxId == null) {
                return 30000001L; // 如果表为空，从30000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成用户设置关系ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }
}
