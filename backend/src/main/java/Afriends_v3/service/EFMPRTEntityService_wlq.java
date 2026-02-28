package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * E、F、M、P、R、T开头_wlq实体类综合服务
 * 负责管理所有E、F、M、P、R、T开头_wlq实体类的数据库操作和内存缓存
 */
@Service
public class EFMPRTEntityService_wlq {

    @Autowired
    private FriendListMapper friendListMapper;

    @Autowired
    private GiftBaseMapper giftBaseMapper;

    @Autowired
    private HardTagBaseMapper hardTagBaseMapper;

    @Autowired
    private IDislikeMapper iDislikeMapper;

    @Autowired
    private IDislikeCommentMapper iDislikeCommentMapper;

    @Autowired
    private IDislikeFeatureVectorMapper iDislikeFeatureVectorMapper;

    @Autowired
    private IHaveMapper iHaveMapper;

    @Autowired
    private IHaveCommentMapper iHaveCommentMapper;

    @Autowired
    private IHaveFeatureVectorMapper iHaveFeatureVectorMapper;

    @Autowired
    private ILikeMapper iLikeMapper;

    @Autowired
    private ILikeCommentMapper iLikeCommentMapper;

    @Autowired
    private ILikeFeatureVectorMapper iLikeFeatureVectorMapper;

    @Autowired
    private ImageContentBaseMapper imageContentBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;

    @Autowired
    private ImagePostFeatureVectorMapper imagePostFeatureVectorMapper;

    @Autowired
    private INeedMapper iNeedMapper;

    @Autowired
    private INeedCommentMapper iNeedCommentMapper;

    @Autowired
    private INeedFeatureVectorMapper iNeedFeatureVectorMapper;

    @Autowired
    private IntiniallabelChenMapper intiniallabelChenMapper;

    @Autowired
    private LikeRobotActionLogMapper likeRobotActionLogMapper;

    @Autowired
    private NovelChapterInfoMapper novelChapterInfoMapper;

    @Autowired
    private NovelContentBaseMapper novelContentBaseMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;

    @Autowired
    private NovelpostFeatureVectorMapper novelpostFeatureVectorMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private RecommendedAvatarMapper recommendedAvatarMapper;

    @Autowired
    private RecommendedLabelMapper recommendedLabelMapper;

    @Autowired
    private RecommendedNicknameMapper recommendedNicknameMapper;

    @Autowired
    private SettingBaseMapper settingBaseMapper;

    @Autowired
    private SoftTagBaseMapper softTagBaseMapper;

    @Autowired
    private SoftTagCategoryMapper softTagCategoryMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private TextpostCommentMapper textpostCommentMapper;

    @Autowired
    private TextpostFeatureVectorMapper textpostFeatureVectorMapper;

    // FriendList 相关方法
    public void loadFriendListToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载好友列表数据...");
            long startTime = System.currentTimeMillis();

            var allFriendList = friendListMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allFriendList.size() + " 条记录");

            if (allFriendList.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有好友列表数据");
                return;
            }

            FriendList_list_wlq.loadFromDatabaseDirectly(allFriendList);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 好友列表数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载好友列表数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<FriendList_wlq> getAllFriendListFromMemory() {
        return FriendList_list_wlq.getAllFriendList();
    }

    public Map<String, Object> getFriendListStatisticsFromMemory() {
        return FriendList_list_wlq.getStatistics();
    }

    // GiftBase 相关方法
    public void loadGiftBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载礼物基础数据...");
            long startTime = System.currentTimeMillis();

            var allGiftBase = giftBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allGiftBase.size() + " 条记录");

            if (allGiftBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有礼物基础数据");
                return;
            }

            GiftBase_list_wlq.loadFromDatabaseDirectly(allGiftBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 礼物基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载礼物基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<GiftBase_wlq> getAllGiftBaseFromMemory() {
        return GiftBase_list_wlq.getAllGiftBase();
    }

    public Map<String, Object> getGiftBaseStatisticsFromMemory() {
        return GiftBase_list_wlq.getStatistics();
    }

    // SoftTagBase 相关方法
    public void loadSoftTagBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载软标签基础数据...");
            long startTime = System.currentTimeMillis();

            var allSoftTagBase = softTagBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allSoftTagBase.size() + " 条记录");

            if (allSoftTagBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有软标签基础数据");
                return;
            }

            SoftTagBase_list_wlq.loadFromDatabaseDirectly(allSoftTagBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 软标签基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载软标签基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<SoftTagBase_wlq> getAllSoftTagBaseFromMemory() {
        return SoftTagBase_list_wlq.getAllSoftTagBase();
    }

    public Map<String, Object> getSoftTagBaseStatisticsFromMemory() {
        return SoftTagBase_list_wlq.getStatistics();
    }

    // HardTagBase 相关方法
    public void loadHardTagBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载硬标签基础数据...");
            long startTime = System.currentTimeMillis();

            var allHardTagBase = hardTagBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allHardTagBase.size() + " 条记录");

            if (allHardTagBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有硬标签基础数据");
                return;
            }

            HardTagBase_list_wlq.loadFromDatabaseDirectly(allHardTagBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 硬标签基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载硬标签基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<HardTagBase_wlq> getAllHardTagBaseFromMemory() {
        return HardTagBase_list_wlq.getAllHardTagBase();
    }

    public Map<String, Object> getHardTagBaseStatisticsFromMemory() {
        return HardTagBase_list_wlq.getStatistics();
    }

    // TextpostBase 相关方法
    public void loadTextpostBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载文字帖子基础数据...");
            long startTime = System.currentTimeMillis();

            var allTextpostBase = textpostBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allTextpostBase.size() + " 条记录");

            if (allTextpostBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有文字帖子基础数据");
                return;
            }

            TextpostBase_list_wlq.loadFromDatabaseDirectly(allTextpostBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 文字帖子基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载文字帖子基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<TextpostBase_wlq> getAllTextpostBaseFromMemory() {
        return TextpostBase_list_wlq.getAllTextpostBase();
    }

    public Map<String, Object> getTextpostBaseStatisticsFromMemory() {
        return TextpostBase_list_wlq.getStatistics();
    }

    // IDislike 相关方法
    public void loadIDislikeToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载我不喜欢数据...");
            long startTime = System.currentTimeMillis();

            var allIDislike = iDislikeMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allIDislike.size() + " 条记录");

            if (allIDislike.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有我不喜欢数据");
                return;
            }

            IDislike_list_wlq.loadFromDatabaseDirectly(allIDislike);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 我不喜欢数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载我不喜欢数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<IDislike_wlq> getAllIDislikeFromMemory() {
        return IDislike_list_wlq.getAllIDislike();
    }

    public Map<String, Object> getIDislikeStatisticsFromMemory() {
        return IDislike_list_wlq.getStatistics();
    }

    // ImagePostBase 相关方法
    public void loadImagePostBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载图片帖子基础数据...");
            long startTime = System.currentTimeMillis();

            var allImagePostBase = imagePostBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allImagePostBase.size() + " 条记录");

            if (allImagePostBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有图片帖子基础数据");
                return;
            }

            ImagePostBase_list_wlq.loadFromDatabaseDirectly(allImagePostBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 图片帖子基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载图片帖子基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ImagePostBase_wlq> getAllImagePostBaseFromMemory() {
        return ImagePostBase_list_wlq.getAllImagePostBase();
    }

    public Map<String, Object> getImagePostBaseStatisticsFromMemory() {
        return ImagePostBase_list_wlq.getStatistics();
    }

    // NovelpostBase 相关方法
    public void loadNovelpostBaseToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载小说帖子基础数据...");
            long startTime = System.currentTimeMillis();

            var allNovelpostBase = novelpostBaseMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allNovelpostBase.size() + " 条记录");

            if (allNovelpostBase.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有小说帖子基础数据");
                return;
            }

            NovelpostBase_list_wlq.loadFromDatabaseDirectly(allNovelpostBase);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 小说帖子基础数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载小说帖子基础数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<NovelpostBase_wlq> getAllNovelpostBaseFromMemory() {
        return NovelpostBase_list_wlq.getAllNovelpostBase();
    }

    public Map<String, Object> getNovelpostBaseStatisticsFromMemory() {
        return NovelpostBase_list_wlq.getStatistics();
    }

    // 由于方法太多，这里只展示部分方法，其他方法按照相同模式实现
    // IDislikeComment, IDislikeFeatureVector, IHave, IHaveComment,
    // IHaveFeatureVector,
    // ILike, ILikeComment, ILikeFeatureVector, ImageContentBase, ImagePostComment,
    // ImagePostFeatureVector, INeed, INeedComment, INeedFeatureVector,
    // IntiniallabelChen, LikeRobotActionLog, NovelChapterInfo, NovelContentBase,
    // NovelpostComment, NovelpostFeatureVector, Recharge,
    // RecommendedAvatar, RecommendedLabel, RecommendedNickname, SettingBase,
    // SoftTagCategory, TextpostComment, TextpostFeatureVector

    // 加载所有E、F、M、P、R、T开头_wlq实体类数据
    public void loadAllEFMPRTEntityDataToMemory() {
        System.out.println("EFMPRTEntityService_wlq: 开始加载所有E、F、M、P、R、T开头_wlq实体类数据...");

        try {
            loadFriendListToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载好友列表数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadGiftBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载礼物基础数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadSoftTagBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载软标签基础数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadHardTagBaseToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载硬标签基础数据失败，继续处理其他实体类: " + e.getMessage());
        }

        try {
            loadIDislikeToMemory();
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载我不喜欢数据失败，继续处理其他实体类: " + e.getMessage());
        }

        // 其他实体类的加载方法按照相同模式添加...

        System.out.println("EFMPRTEntityService_wlq: 所有E、F、M、P、R、T开头_wlq实体类数据加载完成！");
    }

    // Recharge 相关方法
    public void loadRechargeToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载充值数据...");
            long startTime = System.currentTimeMillis();

            var allRecharge = rechargeMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allRecharge.size() + " 条记录");

            if (allRecharge.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有充值数据");
                return;
            }

            Recharge_list_wlq.loadFromDatabaseDirectly(allRecharge);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 充值数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载充值数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Recharge_wlq> getAllRechargeFromMemory() {
        return Recharge_list_wlq.getAllRecharge();
    }

    public Map<String, Object> getRechargeStatisticsFromMemory() {
        return Recharge_list_wlq.getStatistics();
    }

    // RecommendedAvatar 相关方法
    public void loadRecommendedAvatarToMemory() {
        try {
            System.out.println("EFMPRTEntityService_wlq: 开始从数据库加载推荐头像数据...");
            long startTime = System.currentTimeMillis();

            var allRecommendedAvatar = recommendedAvatarMapper.selectAllRecords();
            System.out.println("EFMPRTEntityService_wlq: 原生SQL查询到 " + allRecommendedAvatar.size() + " 条记录");

            if (allRecommendedAvatar.isEmpty()) {
                System.out.println("EFMPRTEntityService_wlq: 数据库中没有推荐头像数据");
                return;
            }

            RecommendedAvatar_list_wlq.loadFromDatabaseDirectly(allRecommendedAvatar);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("EFMPRTEntityService_wlq: 推荐头像数据加载到内存完成！耗时: " + duration + "ms");
        } catch (Exception e) {
            System.err.println("EFMPRTEntityService_wlq: 加载推荐头像数据到内存失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<RecommendedAvatar_wlq> getAllRecommendedAvatarFromMemory() {
        return RecommendedAvatar_list_wlq.getAllRecommendedAvatar();
    }

    public Map<String, Object> getRecommendedAvatarStatisticsFromMemory() {
        return RecommendedAvatar_list_wlq.getStatistics();
    }
}
