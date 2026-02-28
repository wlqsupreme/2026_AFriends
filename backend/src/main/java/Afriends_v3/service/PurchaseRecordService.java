package Afriends_v3.service;

import Afriends_v3.entity.PurchaseRecord_njj;
import Afriends_v3.entity.PurchaseRecord_list_njj;
import Afriends_v3.entity.UserInfo_njj;
import Afriends_v3.entity.UserInfo_list_njj;
import Afriends_v3.mapper.PurchaseRecordMapper;
import Afriends_v3.mapper.UserInfoMapper;
import Afriends_v3.service.AimodelCoinLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 购买记录服务类
 * 负责管理购买记录数据的数据库操作和内存缓存
 */
@Service
public class PurchaseRecordService {

    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    @Lazy
    private AimodelCoinLogService aimodelCoinLogService;

    /**
     * 加载购买记录数据到内存
     */
    public void loadPurchaseRecordToMemory() {
        try {
            System.out.println("PurchaseRecordService: 开始从数据库加载购买记录数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("PurchaseRecordService: 使用原生SQL查询购买记录数据...");
            var allPurchaseRecord = purchaseRecordMapper.selectAllRecords();
            System.out.println(
                    "PurchaseRecordService: 原生SQL查询到 " + (allPurchaseRecord != null ? allPurchaseRecord.size() : 0)
                            + " 条记录");

            if (allPurchaseRecord == null || allPurchaseRecord.isEmpty()) {
                System.out.println("PurchaseRecordService: 数据库中没有购买记录数据，跳过加载");
                PurchaseRecord_list_njj.loadFromDatabaseDirectly(List.of());
                return;
            }

            PurchaseRecord_list_njj.loadFromDatabaseDirectly(allPurchaseRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("PurchaseRecordService: 购买记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("PurchaseRecordService: 加载购买记录数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取所有购买记录数据（从内存）
     */
    public List<PurchaseRecord_njj> getAllPurchaseRecordFromMemory() {
        return PurchaseRecord_list_njj.getAllPurchaseRecord();
    }

    /**
     * 根据记录ID获取购买记录数据（从内存）
     */
    public PurchaseRecord_njj getPurchaseRecordByIdFromMemory(Long recordId) {
        return PurchaseRecord_list_njj.getPurchaseRecordById(recordId);
    }

    /**
     * 根据用户ID获取购买记录数据（从内存）
     */
    public List<PurchaseRecord_njj> getPurchaseRecordByUserIdFromMemory(Long userId) {
        return PurchaseRecord_list_njj.getPurchaseRecordByUserId(userId);
    }

    /**
     * 根据商品类型获取购买记录数据（从内存）
     */
    public List<PurchaseRecord_njj> getPurchaseRecordByItemTypeFromMemory(String itemType) {
        return PurchaseRecord_list_njj.getPurchaseRecordByItemType(itemType);
    }

    /**
     * 获取购买记录统计信息（从内存）
     */
    public Map<String, Object> getPurchaseRecordStatisticsFromMemory() {
        return PurchaseRecord_list_njj.getStatistics();
    }

    /**
     * 强制刷新购买记录数据
     */
    public void refreshPurchaseRecordData() {
        loadPurchaseRecordToMemory();
    }

    /**
     * 保存购买记录到数据库
     */
    public boolean savePurchaseRecord(PurchaseRecord_njj purchaseRecord) {
        try {
            System.out.println("PurchaseRecordService: 开始保存购买记录到数据库...");

            // 生成recordId
            if (purchaseRecord.getRecordId() == null) {
                purchaseRecord.setRecordId(generateNextRecordId());
            }

            // 设置创建时间
            if (purchaseRecord.getCreatedAt() == null) {
                purchaseRecord.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }

            // 更新用户金币余额
            boolean goldUpdated = updateUserGold(purchaseRecord.getUserId(), purchaseRecord.getCoinsSpent());
            if (!goldUpdated) {
                System.out.println("PurchaseRecordService: 用户金币不足，购买失败");
                return false;
            }

            // 设置购买记录中的金币余额
            UserInfo_njj userInfo = UserInfo_list_njj.getUserById(purchaseRecord.getUserId());
            if (userInfo != null) {
                purchaseRecord.setCoinsBalance(userInfo.getGold() != null ? userInfo.getGold().intValue() : 0);
            }

            // 保存到数据库
            int result = purchaseRecordMapper.insert(purchaseRecord);

            if (result > 0) {
                // 添加到内存缓存
                PurchaseRecord_list_njj.addToCache(purchaseRecord);
                System.out.println("PurchaseRecordService: 购买记录保存成功，recordId: " + purchaseRecord.getRecordId());

                // 如果是礼物购买，创建AI模型金币日志
                if ("gift".equals(purchaseRecord.getItemType())) {
                    // 从购买路径中提取userAiId，格式为 "gift_page_userAiId"
                    Long userAiId = extractUserAiIdFromPurchasePath(purchaseRecord.getPurchasePath());
                    if (userAiId != null) {
                        // 获取当前AI模型金币余额
                        Long currentBalance = aimodelCoinLogService.getCurrentAiCoinBalance(purchaseRecord.getUserId(),
                                userAiId);
                        Long newBalance = currentBalance + purchaseRecord.getCoinsSpent().longValue();

                        // 创建AI模型金币日志
                        boolean coinLogCreated = aimodelCoinLogService.createAimodelCoinLog(
                                purchaseRecord.getUserId(),
                                userAiId,
                                purchaseRecord.getCoinsSpent().longValue(),
                                newBalance);

                        if (coinLogCreated) {
                            System.out.println("PurchaseRecordService: AI模型金币日志创建成功，userAiId: " + userAiId +
                                    ", amount: " + purchaseRecord.getCoinsSpent() + ", newBalance: " + newBalance);
                        } else {
                            System.out.println("PurchaseRecordService: AI模型金币日志创建失败，但购买记录已保存");
                        }
                    }
                }

                return true;
            } else {
                System.out.println("PurchaseRecordService: 购买记录保存失败");
                return false;
            }
        } catch (Exception e) {
            System.err.println("PurchaseRecordService: 保存购买记录失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量保存购买记录到数据库
     */
    public boolean savePurchaseRecords(List<PurchaseRecord_njj> purchaseRecords) {
        try {
            System.out.println("PurchaseRecordService: 开始批量保存购买记录到数据库，数量: " + purchaseRecords.size());

            boolean allSuccess = true;
            for (PurchaseRecord_njj record : purchaseRecords) {
                if (!savePurchaseRecord(record)) {
                    allSuccess = false;
                }
            }

            System.out.println("PurchaseRecordService: 批量保存购买记录完成，全部成功: " + allSuccess);
            return allSuccess;
        } catch (Exception e) {
            System.err.println("PurchaseRecordService: 批量保存购买记录失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户金币余额
     */
    private boolean updateUserGold(Long userId, Integer coinsSpent) {
        try {
            System.out.println("PurchaseRecordService: 开始更新用户金币，userId: " + userId + ", 花费: " + coinsSpent);

            // 从内存获取用户信息
            UserInfo_njj userInfo = UserInfo_list_njj.getUserById(userId);
            if (userInfo == null) {
                System.out.println("PurchaseRecordService: 用户不存在，userId: " + userId);
                return false;
            }

            // 检查金币是否足够
            Long currentGold = userInfo.getGold() != null ? userInfo.getGold() : 0L;
            if (currentGold < coinsSpent) {
                System.out.println("PurchaseRecordService: 用户金币不足，当前: " + currentGold + ", 需要: " + coinsSpent);
                return false;
            }

            // 计算新的金币余额
            Long newGold = currentGold - coinsSpent;

            // 更新数据库
            int result = userInfoMapper.updateGold(userId, newGold);
            if (result > 0) {
                // 更新内存缓存
                userInfo.setGold(newGold);
                UserInfo_list_njj.updateUserInfoInCache(userInfo);
                System.out.println("PurchaseRecordService: 用户金币更新成功，新余额: " + newGold);
                return true;
            } else {
                System.out.println("PurchaseRecordService: 用户金币更新失败");
                return false;
            }
        } catch (Exception e) {
            System.err.println("PurchaseRecordService: 更新用户金币失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从购买路径中提取userAiId
     * 格式: "gift_page_userAiId" 或 "gift_page"
     */
    private Long extractUserAiIdFromPurchasePath(String purchasePath) {
        try {
            if (purchasePath == null || !purchasePath.startsWith("gift_page")) {
                return null;
            }

            // 如果路径是 "gift_page_userAiId" 格式
            if (purchasePath.contains("_")) {
                String[] parts = purchasePath.split("_");
                if (parts.length >= 3) {
                    return Long.valueOf(parts[2]);
                }
            }

            // 如果路径只是 "gift_page"，尝试从其他地方获取userAiId
            // 这里可以根据实际业务逻辑来处理
            return null;
        } catch (Exception e) {
            System.err.println("PurchaseRecordService: 提取userAiId失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 生成下一个recordId
     */
    private Long generateNextRecordId() {
        try {
            Long maxId = purchaseRecordMapper.selectMaxRecordId();
            if (maxId == null) {
                return 10000001L; // 如果表为空，从10000001开始
            } else {
                return maxId + 1;
            }
        } catch (Exception e) {
            System.err.println("生成recordId失败: " + e.getMessage());
            return System.currentTimeMillis(); // 降级方案
        }
    }
}
