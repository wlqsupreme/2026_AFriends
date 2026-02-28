package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * B和C开头实体类综合服务
 * 负责管理所有B和C开头实体类的数据库操作和内存缓存
 */
@Service
public class BCEntityService {

    @Autowired
    private BlockRecordMapper blockRecordMapper;

    @Autowired
    private CertBaseMapper certBaseMapper;

    @Autowired
    private CertJobBaseMapper certJobBaseMapper;

    @Autowired
    private CertRealnameBaseMapper certRealnameBaseMapper;

    @Autowired
    private CertStudentBaseMapper certStudentBaseMapper;

    @Autowired
    private ChatSettingsBaseMapper chatSettingsBaseMapper;

    // BlockRecord 相关方法
    public void loadBlockRecordToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载屏蔽记录数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询屏蔽记录数据...");
            var allBlockRecord = blockRecordMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allBlockRecord.size() + " 条记录");

            if (allBlockRecord.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有屏蔽记录数据");
                return;
            }

            BlockRecord_list_njj.loadFromDatabaseDirectly(allBlockRecord);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 屏蔽记录数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载屏蔽记录数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<BlockRecord_njj> getAllBlockRecordFromMemory() {
        return BlockRecord_list_njj.getAllBlockRecord();
    }

    public Map<String, Object> getBlockRecordStatisticsFromMemory() {
        return BlockRecord_list_njj.getStatistics();
    }

    /**
     * 添加拉黑记录
     */
    public BlockRecord_njj addBlockRecord(Long userId, Long blockedUserId) {
        // 检查是否已拉黑
        if (BlockRecord_list_njj.isBlocked(userId, blockedUserId)) {
            throw new RuntimeException("该用户已被拉黑");
        }
        
        BlockRecord_njj record = new BlockRecord_njj();
        record.setRecordId(System.currentTimeMillis()); // 使用时间戳作为ID
        record.setUserId(userId);
        record.setBlockedUserId(blockedUserId);
        record.setBlockedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        record.setIsBlocked((byte) 1);
        
        blockRecordMapper.insert(record);
        BlockRecord_list_njj.addBlockRecord(record);
        return record;
    }

    /**
     * 解除拉黑
     */
    public void unblockUser(Long userId, Long blockedUserId) {
        List<BlockRecord_njj> records = BlockRecord_list_njj.getBlockRecordByUserId(userId);
        for (BlockRecord_njj record : records) {
            if (record.getBlockedUserId().equals(blockedUserId) && record.getIsBlocked() == 1) {
                record.setUnblockedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                record.setIsBlocked((byte) 0);
                blockRecordMapper.updateById(record);
                BlockRecord_list_njj.removeBlockRecord(userId, blockedUserId);
                break;
            }
        }
    }

    /**
     * 获取用户拉黑列表（仅返回已拉黑的记录）
     */
    public List<BlockRecord_njj> getBlockedUsersByUserId(Long userId) {
        return BlockRecord_list_njj.getBlockRecordByUserId(userId).stream()
            .filter(record -> record.getIsBlocked() == 1)
            .collect(Collectors.toList());
    }

    // CertBase 相关方法
    public void loadCertBaseToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载证书基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询证书基础信息数据...");
            var allCertBase = certBaseMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allCertBase.size() + " 条记录");

            if (allCertBase.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有证书基础信息数据");
                return;
            }

            CertBase_list_njj.loadFromDatabaseDirectly(allCertBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 证书基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载证书基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<CertBase_njj> getAllCertBaseFromMemory() {
        return CertBase_list_njj.getAllCertBase();
    }

    public Map<String, Object> getCertBaseStatisticsFromMemory() {
        return CertBase_list_njj.getStatistics();
    }

    // CertJobBase 相关方法
    public void loadCertJobBaseToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载证书工作基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询证书工作基础信息数据...");
            var allCertJobBase = certJobBaseMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allCertJobBase.size() + " 条记录");

            if (allCertJobBase.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有证书工作基础信息数据");
                return;
            }

            CertJobBase_list_njj.loadFromDatabaseDirectly(allCertJobBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 证书工作基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载证书工作基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<CertJobBase_njj> getAllCertJobBaseFromMemory() {
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!CertJobBase_list_njj.isCacheLoaded()) {
            System.out.println("BCEntityService: 检测到职业认证缓存未加载，自动加载缓存...");
            loadCertJobBaseToMemory();
        }
        return CertJobBase_list_njj.getAllCertJobBase();
    }

    public Map<String, Object> getCertJobBaseStatisticsFromMemory() {
        return CertJobBase_list_njj.getStatistics();
    }

    // CertRealnameBase 相关方法
    public void loadCertRealnameBaseToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载实名认证基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询实名认证基础信息数据...");
            var allCertRealnameBase = certRealnameBaseMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allCertRealnameBase.size() + " 条记录");

            if (allCertRealnameBase.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有实名认证基础信息数据");
                return;
            }

            CertRealnameBase_list_njj.loadFromDatabaseDirectly(allCertRealnameBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 实名认证基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载实名认证基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<CertRealnameBase_njj> getAllCertRealnameBaseFromMemory() {
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!CertRealnameBase_list_njj.isCacheLoaded()) {
            System.out.println("BCEntityService: 检测到实名认证缓存未加载，自动加载缓存...");
            loadCertRealnameBaseToMemory();
        }
        return CertRealnameBase_list_njj.getAllCertRealnameBase();
    }

    public Map<String, Object> getCertRealnameBaseStatisticsFromMemory() {
        return CertRealnameBase_list_njj.getStatistics();
    }

    // CertStudentBase 相关方法
    public void loadCertStudentBaseToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载学生证认证基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询学生证认证基础信息数据...");
            var allCertStudentBase = certStudentBaseMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allCertStudentBase.size() + " 条记录");

            if (allCertStudentBase.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有学生证认证基础信息数据");
                return;
            }

            CertStudentBase_list_njj.loadFromDatabaseDirectly(allCertStudentBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 学生证认证基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载学生证认证基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<CertStudentBase_njj> getAllCertStudentBaseFromMemory() {
        // 检查缓存是否已加载，如果未加载则自动加载
        if (!CertStudentBase_list_njj.isCacheLoaded()) {
            System.out.println("BCEntityService: 检测到学生证认证缓存未加载，自动加载缓存...");
            loadCertStudentBaseToMemory();
        }
        return CertStudentBase_list_njj.getAllCertStudentBase();
    }

    public Map<String, Object> getCertStudentBaseStatisticsFromMemory() {
        return CertStudentBase_list_njj.getStatistics();
    }

    // ChatSettingsBase 相关方法
    public void loadChatSettingsBaseToMemory() {
        try {
            System.out.println("BCEntityService: 开始从数据库加载聊天设置基础信息数据...");
            long startTime = System.currentTimeMillis();

            // 使用原生SQL查询
            System.out.println("BCEntityService: 使用原生SQL查询聊天设置基础信息数据...");
            var allChatSettingsBase = chatSettingsBaseMapper.selectAllRecords();
            System.out.println("BCEntityService: 原生SQL查询到 " + allChatSettingsBase.size() + " 条记录");

            if (allChatSettingsBase.isEmpty()) {
                System.out.println("BCEntityService: 数据库中没有聊天设置基础信息数据");
                return;
            }

            ChatSettingsBase_list_njj.loadFromDatabaseDirectly(allChatSettingsBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("BCEntityService: 聊天设置基础信息数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载聊天设置基础信息数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ChatSettingsBase_njj> getAllChatSettingsBaseFromMemory() {
        return ChatSettingsBase_list_njj.getAllChatSettingsBase();
    }

    public Map<String, Object> getChatSettingsBaseStatisticsFromMemory() {
        return ChatSettingsBase_list_njj.getStatistics();
    }

    // 加载所有B和C开头实体类数据
    public void loadAllBCEntityDataToMemory() {
        System.out.println("BCEntityService: 开始加载所有B和C开头实体类数据...");

        try {
            loadBlockRecordToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载屏蔽记录失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadCertBaseToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载证书基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadCertJobBaseToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载证书工作基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadCertRealnameBaseToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载实名认证基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadCertStudentBaseToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载学生证认证基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadChatSettingsBaseToMemory();
        } catch (Exception e) {
            System.err.println("BCEntityService: 加载聊天设置基础信息失败，继续处理其他实体类: " + e.getMessage());
        }

        System.out.println("BCEntityService: 所有B和C开头实体类数据加载完成！");
    }
}
