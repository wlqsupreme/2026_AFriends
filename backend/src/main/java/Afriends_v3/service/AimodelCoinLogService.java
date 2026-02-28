package Afriends_v3.service;

import Afriends_v3.entity.AimodelCoinLog_njj;
import Afriends_v3.entity.AimodelCoinLog_list_njj;
import Afriends_v3.mapper.AimodelCoinLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * AI模型金币日志服务类
 */
@Service
public class AimodelCoinLogService {

    @Autowired
    private AimodelCoinLogMapper aimodelCoinLogMapper;

    /**
     * 加载AI模型金币日志数据到内存
     */
    public void loadAimodelCoinLogToMemory() {
        try {
            System.out.println("AimodelCoinLogService: 开始加载AI模型金币日志数据到内存...");
            AimodelCoinLog_list_njj.loadFromDatabase(aimodelCoinLogMapper);
            System.out.println("AimodelCoinLogService: AI模型金币日志数据加载完成");
        } catch (Exception e) {
            System.err.println("AimodelCoinLogService: 加载AI模型金币日志数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有AI模型金币日志（从内存）
     */
    public List<AimodelCoinLog_njj> getAllAimodelCoinLogFromMemory() {
        return AimodelCoinLog_list_njj.getAllAimodelCoinLog();
    }

    /**
     * 根据用户ID获取AI模型金币日志（从内存）
     */
    public List<AimodelCoinLog_njj> getAimodelCoinLogByUserIdFromMemory(Long userId) {
        return AimodelCoinLog_list_njj.getAimodelCoinLogByUserId(userId);
    }

    /**
     * 根据用户ID和AI模型ID获取金币日志（从内存）
     */
    public List<AimodelCoinLog_njj> getAimodelCoinLogByUserAndAiFromMemory(Long userId, Long userAiId) {
        List<AimodelCoinLog_njj> userLogs = AimodelCoinLog_list_njj.getAimodelCoinLogByUserId(userId);
        return userLogs.stream()
                .filter(log -> log.getUserAiId().equals(userAiId))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取AI模型金币日志统计信息
     */
    public Map<String, Object> getAimodelCoinLogStatisticsFromMemory() {
        return AimodelCoinLog_list_njj.getStatistics();
    }

    /**
     * 创建AI模型金币日志记录
     */
    public boolean createAimodelCoinLog(Long userId, Long userAiId, Long amount, Long totalAfter) {
        try {
            System.out.println("AimodelCoinLogService: 开始创建AI模型金币日志，userId: " + userId +
                    ", userAiId: " + userAiId + ", amount: " + amount + ", totalAfter: " + totalAfter);

            // 生成新的日志ID
            Long logId = generateNextLogId();

            // 创建金币日志记录
            AimodelCoinLog_njj coinLog = new AimodelCoinLog_njj();
            coinLog.setLogId(logId);
            coinLog.setUserId(userId);
            coinLog.setUserAiId(userAiId);
            coinLog.setAmount(amount);
            coinLog.setTotalAfter(totalAfter);
            coinLog.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            // 保存到数据库
            int result = aimodelCoinLogMapper.insert(coinLog);
            if (result > 0) {
                // 添加到内存缓存
                addToCache(coinLog);
                System.out.println("AimodelCoinLogService: AI模型金币日志创建成功，logId: " + logId);
                return true;
            } else {
                System.out.println("AimodelCoinLogService: AI模型金币日志创建失败");
                return false;
            }
        } catch (Exception e) {
            System.err.println("AimodelCoinLogService: 创建AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量创建AI模型金币日志记录
     */
    public boolean createAimodelCoinLogs(List<AimodelCoinLog_njj> coinLogs) {
        try {
            System.out.println("AimodelCoinLogService: 开始批量创建AI模型金币日志...");

            for (AimodelCoinLog_njj coinLog : coinLogs) {
                // 生成日志ID
                if (coinLog.getLogId() == null) {
                    coinLog.setLogId(generateNextLogId());
                }

                // 设置创建时间
                if (coinLog.getCreatedAt() == null) {
                    coinLog.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                }

                // 保存到数据库
                int result = aimodelCoinLogMapper.insert(coinLog);
                if (result > 0) {
                    addToCache(coinLog);
                    System.out.println("AimodelCoinLogService: AI模型金币日志创建成功，logId: " + coinLog.getLogId());
                } else {
                    System.out.println("AimodelCoinLogService: AI模型金币日志创建失败，logId: " + coinLog.getLogId());
                    return false;
                }
            }

            System.out.println("AimodelCoinLogService: 批量创建AI模型金币日志完成");
            return true;
        } catch (Exception e) {
            System.err.println("AimodelCoinLogService: 批量创建AI模型金币日志失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取用户AI模型的当前金币余额
     */
    public Long getCurrentAiCoinBalance(Long userId, Long userAiId) {
        try {
            List<AimodelCoinLog_njj> logs = getAimodelCoinLogByUserAndAiFromMemory(userId, userAiId);
            if (logs.isEmpty()) {
                return 0L; // 如果没有记录，返回0
            }

            // 获取最新的记录
            AimodelCoinLog_njj latestLog = logs.stream()
                    .max((log1, log2) -> log1.getCreatedAt().compareTo(log2.getCreatedAt()))
                    .orElse(null);

            return latestLog != null ? latestLog.getTotalAfter() : 0L;
        } catch (Exception e) {
            System.err.println("AimodelCoinLogService: 获取AI模型金币余额失败: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 添加金币日志到缓存
     */
    private void addToCache(AimodelCoinLog_njj coinLog) {
        if (coinLog != null && coinLog.getLogId() != null) {
            AimodelCoinLog_list_njj.addToCache(coinLog);
            System.out.println("AimodelCoinLogService: 金币日志已添加到缓存，logId: " + coinLog.getLogId());
        }
    }

    /**
     * 生成下一个日志ID
     */
    private Long generateNextLogId() {
        try {
            // 从数据库查询当前最大ID
            Long maxId = aimodelCoinLogMapper.selectMaxLogId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("AimodelCoinLogService: 生成日志ID失败: " + e.getMessage());
            return System.currentTimeMillis(); // 使用时间戳作为备用ID
        }
    }
}
