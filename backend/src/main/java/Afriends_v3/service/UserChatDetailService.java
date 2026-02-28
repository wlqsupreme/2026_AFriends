package Afriends_v3.service;

import Afriends_v3.entity.UserChatCall_njj;
import Afriends_v3.entity.UserChatDetail_njj;
import Afriends_v3.entity.UserChatDetail_list_njj;
import Afriends_v3.mapper.UserChatCallMapper;
import Afriends_v3.mapper.UserChatDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

/**
 * 用户聊天详情服务类
 * 负责管理用户聊天详情数据的数据库操作和内存缓存
 */
@Service
public class UserChatDetailService {

    @Autowired
    private UserChatDetailMapper userChatDetailMapper;

    @Autowired
    private UserChatCallMapper userChatCallMapper; // 新增通话记录Mapper

    // 图片存储路径配置（建议在application.properties中配置）
    @Value("${chat.upload.image-path:/data/chat/images/}")
    private String imageStoragePath;

    // 图片访问基础URL（前端用于加载图片）
    @Value("${chat.upload.image-url:http://localhost:8888/images/}")
    private String imageBaseUrl;

    /**
     * 加载用户聊天详情数据到内存
     */
    public void loadUserChatDetailToMemory() {
        try {
            System.out.println("UserChatDetailService: 开始从数据库加载用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("UserChatDetailService: 使用原生SQL查询用户聊天详情数据...");
            var allUserChatDetail = userChatDetailMapper.selectAllRecords();
            System.out.println("UserChatDetailService: 原生SQL查询到 "
                    + (allUserChatDetail != null ? allUserChatDetail.size() : 0) + " 条记录");

            if (allUserChatDetail == null || allUserChatDetail.isEmpty()) {
                System.out.println("UserChatDetailService: 数据库中没有用户聊天详情数据，跳过加载");
                UserChatDetail_list_njj.loadFromDatabaseDirectly(new ArrayList<>());
                return;
            }

            UserChatDetail_list_njj.loadFromDatabaseDirectly(allUserChatDetail);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("UserChatDetailService: 用户聊天详情数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("UserChatDetailService: 加载用户聊天详情数据到内存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有用户聊天详情数据（从内存）
     */
    public List<UserChatDetail_njj> getAllUserChatDetailFromMemory() {
        return UserChatDetail_list_njj.getAllUserChatDetail();
    }

    /**
     * 根据ID获取用户聊天详情数据
     */
    public UserChatDetail_njj getUserChatDetailByIdFromMemory(Long id) {
        return UserChatDetail_list_njj.getUserChatDetailById(id);
    }

    /**
     * 根据会话ID获取用户聊天详情数据
     */
    public List<UserChatDetail_njj> getUserChatDetailBySessionIdFromMemory(Long sessionId) {
        return UserChatDetail_list_njj.getUserChatDetailBySessionId(BigInteger.valueOf(sessionId));
    }

    /**
     * 根据发送者类型获取用户聊天详情数据
     */
    public List<UserChatDetail_njj> getUserChatDetailBySenderTypeFromMemory(String senderType) {
        return UserChatDetail_list_njj.getUserChatDetailBySenderType(senderType);
    }

    /**
     * 获取用户聊天详情统计信息（从内存）
     */
    public Map<String, Object> getUserChatDetailStatisticsFromMemory() {
        return UserChatDetail_list_njj.getStatistics();
    }

    public Long saveChatDetail(Map<String, Object> chatData) {
        try {
            // 生成新的聊天详情ID
            Long chatId = generateNextChatDetailId();

            // 创建聊天详情对象
            UserChatDetail_njj chatDetail = new UserChatDetail_njj();
            chatDetail.setId(chatId);

            // 设置sessionId
            Object sessionIdObj = chatData.get("sessionId");
            if (sessionIdObj != null) {
                chatDetail.setSessionId(BigInteger.valueOf(Long.parseLong(sessionIdObj.toString())));
            }

            // 设置demandParty
            Object demandPartyObj = chatData.get("demandParty");
            if (demandPartyObj != null) {
                chatDetail.setDemandParty(demandPartyObj.toString());
            }

            // 设置message
            Object messageObj = chatData.get("message");
            if (messageObj != null) {
                chatDetail.setMessage(messageObj.toString());
            }

            // 设置responseParty
            Object responsePartyObj = chatData.get("responseParty");
            if (responsePartyObj != null) {
                chatDetail.setResponseParty(responsePartyObj.toString());
            } else {
                // 如果responseParty为空，设置默认值避免数据库错误
                chatDetail.setResponseParty(""); 
            }

            // 设置senderType
            Object senderTypeObj = chatData.get("senderType");
            if (senderTypeObj != null) {
                chatDetail.setSenderType(senderTypeObj.toString());
            }

            // 设置创建时间
            chatDetail.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int result = userChatDetailMapper.insert(chatDetail);
            if (result > 0) {
                // 添加到内存缓存
                UserChatDetail_list_njj.addToCache(chatDetail);
                System.out.println("聊天记录保存成功: chatId=" + chatId);
                return chatId;
            } else {
                System.err.println("聊天记录保存失败，数据库插入失败");
                return null;
            }
        } catch (Exception e) {
            System.err.println("保存聊天记录异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Long generateNextChatDetailId() {
        try {
            Long maxId = userChatDetailMapper.selectMaxId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成聊天详情ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }

    /**
     * 强制刷新用户聊天详情数据
     */
    public void refreshUserChatDetailData() {
        loadUserChatDetailToMemory();
    }
    
    /**
     * 将聊天详情添加到缓存中
     * @param chatDetail 聊天详情对象
     */
    public void addToCache(UserChatDetail_njj chatDetail) {
        UserChatDetail_list_njj.addToCache(chatDetail);
    }


    /**
     * 保存图片消息（支持图片选择和相机拍摄）
     */
    public Map<String, Object> saveImageMessage(MultipartFile file, BigInteger sessionId,
                                                String demandParty, String responseParty) throws Exception {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的图片文件为空");
        }

        // 2. 处理文件存储
        String originalFilename = file.getOriginalFilename();
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExt; // 生成唯一文件名

        // 确保存储目录存在
        File storageDir = new File(imageStoragePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // 保存文件到本地
        Path filePath = Paths.get(imageStoragePath, fileName);
        Files.write(filePath, file.getBytes());

        // 3. 构建图片访问URL
        String fileUrl = imageBaseUrl + fileName;

        // 4. 保存图片消息记录到数据库
        Long chatId = generateNextChatDetailId();
        UserChatDetail_njj chatDetail = new UserChatDetail_njj();
        chatDetail.setId(chatId);
        chatDetail.setSessionId(sessionId);
        chatDetail.setDemandParty(demandParty);
        chatDetail.setResponseParty(responseParty);
        chatDetail.setMessageType("image"); // 图片消息类型
        chatDetail.setMessage(fileUrl); // 消息内容存储图片URL
        chatDetail.setSenderType("user");
        chatDetail.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        int result = userChatDetailMapper.insert(chatDetail);
        if (result <= 0) {
            // 数据库保存失败，删除已上传的图片
            Files.deleteIfExists(filePath);
            throw new RuntimeException("图片消息数据库保存失败");
        }

        // 5. 添加到内存缓存
        UserChatDetail_list_njj.addToCache(chatDetail);

        // 6. 返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatId", chatId);
        resultMap.put("fileUrl", fileUrl);
        return resultMap;
    }

    /**
     * 保存位置消息
     */
    public Long saveLocationMessage(BigInteger sessionId, String demandParty, String responseParty,
                                    String name, String address, Double lat, Double lng) {
        try {
            // 1. 构建位置信息JSON
            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("name", name);
            locationMap.put("address", address);
            locationMap.put("lat", lat);
            locationMap.put("lng", lng);
            String locationJson = new com.alibaba.fastjson.JSONObject(locationMap).toString();

            // 2. 生成消息ID并保存
            Long chatId = generateNextChatDetailId();
            UserChatDetail_njj chatDetail = new UserChatDetail_njj();
            chatDetail.setId(chatId);
            chatDetail.setSessionId(sessionId);
            chatDetail.setDemandParty(demandParty);
            chatDetail.setResponseParty(responseParty);
            chatDetail.setMessageType("location"); // 位置消息类型
            chatDetail.setMessage(locationJson); // 存储位置JSON
            chatDetail.setSenderType("user");
            chatDetail.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            int result = userChatDetailMapper.insert(chatDetail);
            if (result > 0) {
                UserChatDetail_list_njj.addToCache(chatDetail);
                System.out.println("位置消息保存成功: chatId=" + chatId);
                return chatId;
            } else {
                System.err.println("位置消息保存失败");
                return null;
            }
        } catch (Exception e) {
            System.err.println("保存位置消息异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建通话记录（语音/视频）
     */
    public Long createCallRecord(Long sessionId, String callerId, String calleeId, String callType) {
        try {
            // 1. 生成通话记录ID
            Long callId = generateNextCallId();

            // 2. 创建通话记录对象
            UserChatCall_njj callRecord = new UserChatCall_njj();
            callRecord.setId(callId);
            callRecord.setSessionId(sessionId);
            callRecord.setCallerId(callerId);
            callRecord.setCalleeId(calleeId);
            callRecord.setCallType(callType); // voice/video
            callRecord.setCallStatus("pending"); // 初始状态：待接听
            callRecord.setStartTime(new Timestamp(System.currentTimeMillis()));

            // 3. 保存到数据库
            int result = userChatCallMapper.insert(callRecord);
            if (result > 0) {
                System.out.println("通话记录创建成功: callId=" + callId);
                return callId;
            } else {
                System.err.println("通话记录创建失败");
                return null;
            }
        } catch (Exception e) {
            System.err.println("创建通话记录异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新通话状态
     */
    public boolean updateCallStatus(Long callId, String callStatus, Integer duration) {
        try {
            // 1. 查询通话记录
            UserChatCall_njj callRecord = userChatCallMapper.selectById(callId);
            if (callRecord == null) {
                System.err.println("未找到通话记录: callId=" + callId);
                return false;
            }

            // 2. 更新状态
            callRecord.setCallStatus(callStatus);

            // 如果是结束状态，更新结束时间和时长
            if ("finish".equals(callStatus)) {
                callRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                callRecord.setDuration(duration);
            }

            // 3. 保存更新
            int result = userChatCallMapper.updateById(callRecord);
            return result > 0;
        } catch (Exception e) {
            System.err.println("更新通话状态异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成通话记录ID
     */
    private Long generateNextCallId() {
        try {
            Long maxId = userChatCallMapper.selectMaxId();
            if (maxId == null) {
                return 20000001L; // 通话ID起始值与消息ID区分开
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成通话记录ID失败: " + e.getMessage());
            return System.currentTimeMillis() + 10000000; // 确保与消息ID不冲突
        }
    }
}

