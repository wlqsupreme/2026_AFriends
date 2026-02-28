package Afriends_v3.controller;

import Afriends_v3.entity.UserChatDetail_njj;
import Afriends_v3.entity.UserChatList_njj;
import Afriends_v3.mapper.UserChatListMapper;
import Afriends_v3.service.UserEntityService;
import Afriends_v3.service.UserChatDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户聊天详情控制器
 * 提供用户聊天详情数据的API接口
 */
@RestController
@RequestMapping("/api/user-chat-detail")
@CrossOrigin(origins = "*")
public class UserChatDetailController {

    @Autowired
    private UserChatDetailService userChatDetailService;

    @Autowired
    private UserChatListMapper userChatListMapper;

    @Autowired
    private UserEntityService userEntityService;

    /**
     * 加载用户聊天详情数据到内存
     */
    @PostMapping("/load")
    public ResponseEntity<Map<String, String>> loadUserChatDetailToMemory() {
        try {
            System.out.println("开始加载用户聊天详情数据到内存...");
            long startTime = System.currentTimeMillis();

            userChatDetailService.loadUserChatDetailToMemory();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户聊天详情数据加载完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载到内存成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("加载用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据加载失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取所有用户聊天详情数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserChatDetail_njj>> getAllUserChatDetail() {
        try {
            List<UserChatDetail_njj> details = userChatDetailService.getAllUserChatDetailFromMemory();
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据ID获取用户聊天详情数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserChatDetail_njj> getUserChatDetailById(@PathVariable Long id) {
        try {
            UserChatDetail_njj detail = userChatDetailService.getUserChatDetailByIdFromMemory(id);
            if (detail != null) {
                return ResponseEntity.ok(detail);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("获取用户聊天详情失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private Map<String, Object> buildError(String action, String code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", code);
        result.put("action", action);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 根据会话ID获取用户聊天详情数据
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<UserChatDetail_njj>> getUserChatDetailBySessionId(@PathVariable Long sessionId) {
        try {
            List<UserChatDetail_njj> details = userChatDetailService.getUserChatDetailBySessionIdFromMemory(sessionId);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            System.err.println("根据会话ID获取聊天详情失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 根据发送者类型获取用户聊天详情数据
     */
    @GetMapping("/sender-type/{senderType}")
    public ResponseEntity<List<UserChatDetail_njj>> getUserChatDetailBySenderType(@PathVariable String senderType) {
        try {
            List<UserChatDetail_njj> details = userChatDetailService
                    .getUserChatDetailBySenderTypeFromMemory(senderType);
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 获取用户聊天详情统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getUserChatDetailStatistics() {
        try {
            Map<String, Object> stats = userChatDetailService.getUserChatDetailStatisticsFromMemory();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * 保存聊天详情
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveChatDetail(@RequestBody Map<String, Object> chatData) {
        try {
            Long chatId = userChatDetailService.saveChatDetail(chatData);
            if (chatId != null) {
                Map<String, String> result = Map.of(
                        "message", "聊天记录保存成功",
                        "chatId", chatId.toString(),
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.ok(result);
            } else {
                Map<String, String> result = Map.of(
                        "message", "聊天记录保存失败",
                        "timestamp", String.valueOf(System.currentTimeMillis()));
                return ResponseEntity.status(500).body(result);
            }
        } catch (Exception e) {
            System.err.println("保存聊天记录失败: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> result = Map.of(
                    "message", "保存聊天记录失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 强制刷新用户聊天详情数据
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshUserChatDetailData() {
        try {
            System.out.println("开始刷新用户聊天详情数据...");
            long startTime = System.currentTimeMillis();

            userChatDetailService.refreshUserChatDetailData();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            System.out.println("用户聊天详情数据刷新完成，耗时: " + duration + "ms");

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据刷新成功，耗时: " + duration + "ms",
                    "timestamp", String.valueOf(System.currentTimeMillis()),
                    "duration", String.valueOf(duration));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("刷新用户聊天详情数据失败: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> result = Map.of(
                    "message", "用户聊天详情数据刷新失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 上传图片（支持图片选择和相机拍摄的图片）
     * 前端需用multipart/form-data格式提交
     */
    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sessionId") BigInteger sessionId,
            @RequestParam("demandParty") String demandParty,
            @RequestParam("responseParty") String responseParty) {
        try {
            // 调用服务层保存图片并创建图片消息记录
            Map<String, Object> result = userChatDetailService.saveImageMessage(
                    file, sessionId, demandParty, responseParty);

            return ResponseEntity.ok(Map.of(
                    "message", "图片上传成功",
                    "chatId", result.get("chatId").toString(),
                    "fileUrl", result.get("fileUrl").toString(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        } catch (Exception e) {
            System.err.println("图片上传失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "图片上传失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        }
    }

    /**
     * 发送位置消息
     */
    @PostMapping("/send/location")
    public ResponseEntity<Map<String, String>> sendLocation(@RequestBody Map<String, Object> locationData) {
        try {
            // 解析位置参数
            BigInteger sessionId = new BigInteger(locationData.get("sessionId").toString());
            String demandParty = locationData.get("demandParty").toString();
            String responseParty = locationData.get("responseParty").toString();
            String name = locationData.get("name").toString();       // 位置名称
            String address = locationData.get("address").toString(); // 详细地址
            Double lat = Double.parseDouble(locationData.get("lat").toString()); // 纬度
            Double lng = Double.parseDouble(locationData.get("lng").toString()); // 经度

            // 调用服务层保存位置消息
            Long chatId = userChatDetailService.saveLocationMessage(
                    sessionId, demandParty, responseParty, name, address, lat, lng);

            return ResponseEntity.ok(Map.of(
                    "message", "位置消息发送成功",
                    "chatId", chatId.toString(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        } catch (Exception e) {
            System.err.println("位置消息发送失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "位置消息发送失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        }
    }

    /**
     * 发起通话请求（语音/视频）
     */
    @PostMapping("/call/request")
    public ResponseEntity<Map<String, String>> requestCall(@RequestBody Map<String, Object> callData) {
        try {
            Long sessionId = Long.parseLong(callData.get("sessionId").toString());
            String callerId = callData.get("callerId").toString();       // 呼叫者ID
            String calleeId = callData.get("calleeId").toString();       // 被呼叫者ID
            String callType = callData.get("callType").toString();       // voice/video

            // 调用服务层创建通话记录
            Long callId = userChatDetailService.createCallRecord(
                    sessionId, callerId, calleeId, callType);

            return ResponseEntity.ok(Map.of(
                    "message", "通话请求已发起",
                    "callId", callId.toString(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        } catch (Exception e) {
            System.err.println("通话请求失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "通话请求失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        }
    }

    /**
     * 更新通话状态（接听/拒绝/结束）
     */
    @PostMapping("/call/update-status")
    public ResponseEntity<Map<String, String>> updateCallStatus(@RequestBody Map<String, Object> statusData) {
        try {
            Long callId = Long.parseLong(statusData.get("callId").toString());
            String callStatus = statusData.get("callStatus").toString(); // accept/reject/finish
            Integer duration = statusData.get("duration") != null ?
                    Integer.parseInt(statusData.get("duration").toString()) : 0; // 通话时长(秒)

            // 调用服务层更新通话状态
            boolean success = userChatDetailService.updateCallStatus(callId, callStatus, duration);

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "message", "通话状态更新成功",
                        "callId", callId.toString(),
                        "timestamp", String.valueOf(System.currentTimeMillis())
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "未找到通话记录",
                        "callId", callId.toString()
                ));
            }
        } catch (Exception e) {
            System.err.println("更新通话状态失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "更新通话状态失败: " + e.getMessage(),
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        }
    }
}
