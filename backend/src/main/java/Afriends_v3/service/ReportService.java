package Afriends_v3.service;

import Afriends_v3.entity.ReportRecord_wlq;
import Afriends_v3.mapper.ReportRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 举报功能服务类
 * 负责处理举报相关数据的查询和操作
 */
@Service
public class ReportService {

    @Autowired
    private ReportRecordMapper reportRecordMapper;

    /**
     * 提交举报
     * @param userId 举报用户ID
     * @param reportedUserId 被举报用户ID
     * @param contentId 被举报内容ID
     * @param contentType 被举报内容类型
     * @param categoryId 举报类别ID
     * @param description 举报描述
     * @param evidenceImg 证据图片
     * @return 操作结果
     */
    public Map<String, Object> submitReport(Long userId, Long reportedUserId, Long contentId, 
            String contentType, Long categoryId, String description, String evidenceImg) {
        System.out.println("ReportService: 开始提交举报 - 用户ID: " + userId + 
            ", 被举报用户ID: " + reportedUserId + ", 内容ID: " + contentId + 
            ", 内容类型: " + contentType + ", 举报类别ID: " + categoryId);
        
        try {
            // 创建举报记录
            ReportRecord_wlq reportRecord = new ReportRecord_wlq();
            Long newId = generateNewId();
            reportRecord.setReportId(newId);
            reportRecord.setUserId(userId);
            reportRecord.setReportedUserId(reportedUserId);
            reportRecord.setContentId(contentId);
            reportRecord.setContentType(contentType);
            reportRecord.setCategoryId(categoryId);
            reportRecord.setDescription(description);
            reportRecord.setEvidenceImg(evidenceImg);
            reportRecord.setStatus((byte) 0); // 0=未处理
            reportRecord.setResult(null);
            reportRecord.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            reportRecord.setProcessedAt(null);
            
            // 插入数据库
            reportRecordMapper.insert(reportRecord);
            
            System.out.println("ReportService: 举报记录创建成功，ID: " + newId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "举报提交成功，我们将在3个工作日内处理");
            response.put("reportId", newId);
            response.put("timestamp", System.currentTimeMillis());
            
            return response;
            
        } catch (Exception e) {
            System.err.println("ReportService: 提交举报失败: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "举报提交失败: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return errorResponse;
        }
    }
    
    /**
     * 获取用户的举报记录
     * @param userId 用户ID
     * @return 举报记录列表
     */
    public List<Map<String, Object>> getReportRecords(Long userId) {
        System.out.println("ReportService: 开始获取用户举报记录 - 用户ID: " + userId);
        
        try {
            // 查询用户的举报记录
            List<ReportRecord_wlq> records = reportRecordMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ReportRecord_wlq>()
                    .eq("user_id", userId)
                    .orderByDesc("created_at")
            );
            
            List<Map<String, Object>> recordList = new ArrayList<>();
            for (ReportRecord_wlq record : records) {
                Map<String, Object> recordData = new HashMap<>();
                recordData.put("reportId", record.getReportId());
                recordData.put("userId", record.getUserId());
                recordData.put("reportedUserId", record.getReportedUserId());
                recordData.put("contentId", record.getContentId());
                recordData.put("contentType", record.getContentType());
                recordData.put("categoryId", record.getCategoryId());
                recordData.put("description", record.getDescription());
                recordData.put("evidenceImg", record.getEvidenceImg());
                recordData.put("status", record.getStatus());
                recordData.put("result", record.getResult());
                recordData.put("createdAt", record.getCreatedAt());
                recordData.put("processedAt", record.getProcessedAt());
                
                // 添加状态描述
                String statusText = getStatusText(record.getStatus());
                recordData.put("statusText", statusText);
                
                // 添加举报类别描述
                String categoryText = getCategoryText(record.getCategoryId());
                recordData.put("categoryText", categoryText);
                
                recordList.add(recordData);
            }
            
            System.out.println("ReportService: 获取到 " + recordList.size() + " 条举报记录");
            return recordList;
            
        } catch (Exception e) {
            System.err.println("ReportService: 获取举报记录失败: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 生成从10000000开始的自增ID
     * @return 新的ID
     */
    private Long generateNewId() {
        try {
            Long maxId = 9999999L; // 起始值减1
            
            // 查询举报表的最大ID，只考虑从10000000开始的ID
            List<ReportRecord_wlq> allReports = reportRecordMapper.selectList(null);
            for (ReportRecord_wlq report : allReports) {
                if (report.getReportId() != null && report.getReportId() >= 10000000L && report.getReportId() > maxId) {
                    maxId = report.getReportId();
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("ReportService: 生成新举报ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("ReportService: 生成ID失败，使用默认值: " + e.getMessage());
            return 10000000L; // 如果出错，返回默认起始值
        }
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusText(Byte status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已处理";
            default: return "未知";
        }
    }
    
    /**
     * 获取举报类别描述
     */
    private String getCategoryText(Long categoryId) {
        if (categoryId == null) return "未知";
        switch (categoryId.intValue()) {
            case 1: return "色情低俗";
            case 2: return "政治敏感";
            case 3: return "诈骗信息";
            case 4: return "种族歧视";
            case 5: return "攻击谩骂";
            case 6: return "网络暴力";
            case 7: return "站外引流";
            case 8: return "违法违规";
            case 9: return "涉未成年人";
            case 10: return "其他违规";
            default: return "未知类别";
        }
    }
}




