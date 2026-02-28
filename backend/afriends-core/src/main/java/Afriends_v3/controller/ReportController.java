package Afriends_v3.controller;

import Afriends_v3.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 举报功能控制器
 * 提供举报相关数据的API接口
 */
@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;
    
    /**
     * 测试接口 - 验证后端是否正常工作
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        System.out.println("=== 举报功能测试接口被调用 ===");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "举报功能后端服务正常运行");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    /**
     * 提交举报
     * @param requestBody 请求体，包含userId, reportedUserId, contentId, contentType, categoryId, description, evidenceImg
     * @return 操作结果
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitReport(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.valueOf(requestBody.get("userId").toString());
        Long reportedUserId = requestBody.get("reportedUserId") != null ? 
            Long.valueOf(requestBody.get("reportedUserId").toString()) : null;
        Long contentId = requestBody.get("contentId") != null ? 
            Long.valueOf(requestBody.get("contentId").toString()) : null;
        String contentType = requestBody.get("contentType") != null ? 
            requestBody.get("contentType").toString() : null;
        Long categoryId = Long.valueOf(requestBody.get("categoryId").toString());
        String description = requestBody.get("description").toString();
        String evidenceImg = requestBody.get("evidenceImg") != null ? 
            requestBody.get("evidenceImg").toString() : null;
        
        System.out.println("ReportController: 收到举报请求 - 用户ID: " + userId + 
            ", 被举报用户ID: " + reportedUserId + ", 内容ID: " + contentId + 
            ", 内容类型: " + contentType + ", 举报类别ID: " + categoryId);
        
        try {
            Map<String, Object> result = reportService.submitReport(
                userId, reportedUserId, contentId, contentType, categoryId, description, evidenceImg);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("ReportController: 处理举报请求失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "举报提交失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * 获取用户的举报记录
     * @param userId 用户ID
     * @return 举报记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getReportRecords(
            @RequestParam("userId") Long userId) {
        
        System.out.println("=== ReportController: 收到获取举报记录请求 ===");
        System.out.println("用户ID: " + userId);
        System.out.println("请求时间: " + new java.util.Date());
        long startTime = System.currentTimeMillis();
        
        try {
            java.util.List<Map<String, Object>> records = reportService.getReportRecords(userId);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "获取举报记录成功");
            response.put("data", records);
            response.put("count", records.size());
            response.put("userId", userId);
            response.put("duration", duration + "ms");
            response.put("timestamp", System.currentTimeMillis());
            
            System.out.println("ReportController: 返回举报记录成功，共 " + records.size() + " 条数据，耗时: " + duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ReportController: 获取举报记录失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "获取举报记录失败: " + e.getMessage());
            errorResponse.put("data", new Object[0]);
            errorResponse.put("count", 0);
            errorResponse.put("userId", userId);
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}




