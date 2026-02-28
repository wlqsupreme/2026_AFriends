package Afriends_v3.controller;

import Afriends_v3.entity.UserBase_wlq;
import Afriends_v3.mapper.UserBaseMapper;
import Afriends_v3.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户浏览历史控制器
 * 提供浏览历史相关的API接口
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserHistoryController {

    @Autowired
    private UserHistoryService userHistoryService;

    @Autowired
    private UserBaseMapper userBaseMapper;

    /**
     * 获取用户的浏览历史
     * GET /api/user/history?userId=1000100
     * 
     * @param userId 用户ID
     * @return 浏览历史列表
     */
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getUserHistory(
            @RequestParam(value = "userId", defaultValue = "1000100") Long userId) {
        
        System.out.println("UserHistoryController: 收到获取浏览历史请求，用户ID: " + userId);
        
        try {
            // 1. 参数校验
            if (userId == null || userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("data", new Object[0]);
                errorResponse.put("count", 0);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("data", new Object[0]);
                errorResponse.put("count", 0);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }

            // 3. 获取浏览历史
            long startTime = System.currentTimeMillis();
            List<Map<String, Object>> historyList = userHistoryService.getUserHistory(userId);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 4. 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取浏览历史成功");
            response.put("data", historyList != null ? historyList : new Object[0]);
            response.put("count", historyList != null ? historyList.size() : 0);
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());

            System.out.println("UserHistoryController: 返回浏览历史成功，共 " + (historyList != null ? historyList.size() : 0) + " 条记录，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("UserHistoryController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("userId", userId);
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            System.err.println("UserHistoryController: 获取浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取浏览历史失败，请稍后重试");
            errorResponse.put("userId", userId);
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 删除单条浏览历史
     * POST /api/user/history/delete
     * 请求体：{"userId": 1000100, "historyId": 12345}
     * 
     * @param requestBody 请求体，包含 userId 和 historyId
     * @return 操作结果
     */
    @PostMapping("/history/delete")
    public ResponseEntity<Map<String, Object>> deleteHistory(@RequestBody Map<String, Object> requestBody) {
        
        System.out.println("UserHistoryController: 收到删除浏览历史请求");
        
        try {
            // 1. 参数校验
            if (requestBody == null || requestBody.get("userId") == null || requestBody.get("historyId") == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数不完整（userId、historyId为必填项）");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Long userId;
            Long historyId;
            try {
                userId = Long.valueOf(requestBody.get("userId").toString());
                historyId = Long.valueOf(requestBody.get("historyId").toString());
            } catch (NumberFormatException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数格式错误，userId 和 historyId 必须为数字");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 验证用户ID
            if (userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 3. 验证历史记录ID
            if (historyId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "历史记录ID无效，必须大于0");
                errorResponse.put("historyId", historyId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 4. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }

            // 5. 执行删除操作
            Map<String, Object> result = userHistoryService.deleteHistory(userId, historyId);
            
            // 6. 检查服务层返回的结果
            if (Boolean.FALSE.equals(result.get("success"))) {
                // 如果是历史记录不存在，返回404；否则返回400
                if ("历史记录不存在或不属于该用户".equals(result.get("message"))) {
                    return ResponseEntity.status(404).body(result);
                }
                return ResponseEntity.badRequest().body(result);
            }

            System.out.println("UserHistoryController: 删除浏览历史成功，userId: " + userId + ", historyId: " + historyId);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            System.err.println("UserHistoryController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            System.err.println("UserHistoryController: 删除浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "删除浏览历史失败，请稍后重试");
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 清空用户的所有浏览历史
     * POST /api/user/history/clear
     * 请求体：{"userId": 1000100}
     * 
     * @param requestBody 请求体，包含 userId
     * @return 操作结果
     */
    @PostMapping("/history/clear")
    public ResponseEntity<Map<String, Object>> clearHistory(@RequestBody Map<String, Object> requestBody) {
        
        System.out.println("UserHistoryController: 收到清空浏览历史请求");
        
        try {
            // 1. 参数校验
            if (requestBody == null || requestBody.get("userId") == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数不完整（userId为必填项）");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Long userId;
            try {
                userId = Long.valueOf(requestBody.get("userId").toString());
            } catch (NumberFormatException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数格式错误，userId 必须为数字");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 验证用户ID
            if (userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 3. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户不存在");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(404).body(errorResponse);
            }

            // 4. 执行清空操作
            Map<String, Object> result = userHistoryService.clearHistory(userId);
            
            // 5. 检查服务层返回的结果
            if (Boolean.FALSE.equals(result.get("success"))) {
                return ResponseEntity.badRequest().body(result);
            }

            System.out.println("UserHistoryController: 清空浏览历史成功，userId: " + userId);
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            System.err.println("UserHistoryController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            System.err.println("UserHistoryController: 清空浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "清空浏览历史失败，请稍后重试");
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * 创建浏览历史记录（用于测试）
     * POST /api/user/history/create
     * 请求体：{"userId": 1000100, "contentId": 20000001, "contentType": 1}
     * 
     * @param requestBody 请求体
     * @return 操作结果
     */
    @PostMapping("/history/create")
    public ResponseEntity<Map<String, Object>> createHistory(@RequestBody Map<String, Object> requestBody) {
        
        System.out.println("UserHistoryController: 收到创建浏览历史记录请求");
        
        try {
            // 1. 参数校验
            if (requestBody == null || requestBody.get("userId") == null || 
                requestBody.get("contentId") == null || requestBody.get("contentType") == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数不完整（userId、contentId、contentType为必填项）");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Long userId;
            Long contentId;
            Byte contentType;
            Byte viewType = null;
            Integer durationSeconds = null;

            try {
                userId = Long.valueOf(requestBody.get("userId").toString());
                contentId = Long.valueOf(requestBody.get("contentId").toString());
                contentType = Byte.valueOf(requestBody.get("contentType").toString());
                
                // 可选参数
                if (requestBody.get("viewType") != null) {
                    viewType = Byte.valueOf(requestBody.get("viewType").toString());
                }
                if (requestBody.get("durationSeconds") != null) {
                    durationSeconds = Integer.valueOf(requestBody.get("durationSeconds").toString());
                }
            } catch (NumberFormatException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "参数格式错误，userId、contentId、contentType 必须为数字");
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 2. 验证用户ID
            if (userId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "用户ID无效，必须大于0");
                errorResponse.put("userId", userId);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 3. 验证内容类型
            if (contentType < 1 || contentType > 3) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "无效的内容类型，有效值为：1（文字动态）、2（图片动态）、3（小说）");
                errorResponse.put("contentType", contentType);
                errorResponse.put("timestamp", System.currentTimeMillis());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // 4. 调用服务层创建浏览历史
            Map<String, Object> result = userHistoryService.createHistory(userId, contentId, contentType, 
                                                                          viewType, durationSeconds);
            
            // 5. 检查服务层返回的结果
            if (Boolean.FALSE.equals(result.get("success"))) {
                if ("用户不存在".equals(result.get("message")) || "内容不存在".equals(result.get("message"))) {
                    return ResponseEntity.status(404).body(result);
                }
                return ResponseEntity.badRequest().body(result);
            }

            System.out.println("UserHistoryController: 创建浏览历史记录成功，historyId=" + result.get("historyId"));
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            System.err.println("UserHistoryController: 参数错误: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            System.err.println("UserHistoryController: 创建浏览历史记录失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "创建浏览历史记录失败，请稍后重试");
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
