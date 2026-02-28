package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 账户注销服务类
 * 负责处理用户账户注销时所有相关数据的逻辑删除
 */
@Service
public class AccountDeletionService {

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IDislikeCommentMapper idislikeCommentMapper;

    @Autowired
    private IHaveCommentMapper iHaveCommentMapper;

    @Autowired
    private ILikeCommentMapper iLikeCommentMapper;

    @Autowired
    private ImagePostCommentMapper imagePostCommentMapper;

    @Autowired
    private INeedCommentMapper iNeedCommentMapper;

    @Autowired
    private NovelpostCommentMapper novelpostCommentMapper;

    @Autowired
    private TextpostCommentMapper textpostCommentMapper;

    @Autowired
    private UserReviewBaseMapper userReviewBaseMapper;

    @Autowired
    private ImageContentBaseMapper imageContentBaseMapper;

    @Autowired
    private AchievementBaseMapper achievementBaseMapper;

    @Autowired
    private AchievementRecordMapper achievementRecordMapper;

    @Autowired
    private AimodelBaseInfoMapper aimodelBaseInfoMapper;

    @Autowired
    private IDislikeMapper iDislikeMapper;

    @Autowired
    private IHaveMapper iHaveMapper;

    @Autowired
    private ILikeMapper iLikeMapper;

    @Autowired
    private INeedMapper iNeedMapper;

    @Autowired
    private NovelChapterInfoMapper novelChapterInfoMapper;

    @Autowired
    private NovelContentBaseMapper novelContentBaseMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private UserBaseDynamicMapper userBaseDynamicMapper;

    @Autowired
    private UserBaseImagepostBaseMapper userBaseImagepostBaseMapper;

    @Autowired
    private UserBaseLikeActionMapper userBaseLikeActionMapper;

    @Autowired
    private UserBasePicCommentMapper userBasePicCommentMapper;

    @Autowired
    private UserBaseSystemMessageMapper userBaseSystemMessageMapper;

    @Autowired
    private UserBaseTextCommentMapper userBaseTextCommentMapper;

    @Autowired
    private UserBaseUserCollectioinMapper userBaseUserCollectioinMapper;

    @Autowired
    private UserNovelRelationMapper userNovelRelationMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 注销用户账户
     * 对所有相关实体进行逻辑删除
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteAccount(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("开始注销用户账户，用户ID: " + userId);
            long startTime = System.currentTimeMillis();
            
            Timestamp deletedAt = new Timestamp(System.currentTimeMillis());
            
            // 1. 处理UserBase和UserInfo（同步处理，因为是核心数据）
            deleteUserBaseAndInfo(userId, deletedAt);
            
            // 2. 异步处理其他实体
            deleteOtherEntitiesAsync(userId, deletedAt);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            System.out.println("用户账户注销核心操作完成，用户ID: " + userId + ", 耗时: " + duration + "ms");
            
            result.put("success", true);
            result.put("message", "账户注销已启动");
            result.put("userId", userId);
            result.put("duration", duration);
            return result;
            
        } catch (Exception e) {
            System.err.println("注销用户账户失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "账户注销启动失败: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * 异步处理除UserBase和UserInfo外的其他实体
     * 
     * @param userId 用户ID
     * @param deletedAt 删除时间戳
     */
    private void deleteOtherEntitiesAsync(Long userId, Timestamp deletedAt) {
        System.out.println("开始异步处理其他实体，用户ID: " + userId);
        
        // 异步处理使用@TableLogic注解的实体
        deleteEntitiesWithTableLogic(userId, deletedAt);
        
        // 异步处理有时间戳删除字段的实体
        deleteEntitiesWithDeletedAt(userId, deletedAt);
        
        // 异步处理没有@TableLogic但需要手动设置删除标记的实体
        deleteEntitiesWithManualFlag(userId, deletedAt);
    }

    /**
     * 同步处理UserBase和UserInfo核心数据
     */
    private void deleteUserBaseAndInfo(Long userId, Timestamp deletedAt) {
        System.out.println("处理UserBase和UserInfo核心数据，用户ID: " + userId);
        
        // 1. UserBase_wlq - 使用@TableLogic注解的deleted字段 (Integer类型)
        try {
            QueryWrapper<UserBase_wlq> userBaseWrapper = new QueryWrapper<>();
            userBaseWrapper.eq("user_id", userId);
            UserBase_wlq userBase = userBaseMapper.selectOne(userBaseWrapper);
            if (userBase != null) {
                userBaseMapper.deleteById(userId);
                System.out.println("删除UserBase记录，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBase记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 2. UserInfo_njj - 使用@TableLogic注解的deleted字段 (Integer类型)
        try {
            QueryWrapper<UserInfo_njj> userInfoWrapper = new QueryWrapper<>();
            userInfoWrapper.eq("user_id", userId);
            UserInfo_njj userInfo = userInfoMapper.selectOne(userInfoWrapper);
            if (userInfo != null) {
                userInfoMapper.deleteById(userId);
                System.out.println("删除UserInfo记录，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserInfo记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 处理使用@TableLogic注解的实体
     * 使用Mapper中自定义的方法处理逻辑删除，避免MyBatis-Plus处理特殊字符表名的问题
     */
    @Async
    public void deleteEntitiesWithTableLogic(Long userId, Timestamp deletedAt) {
        
        // 3. IDislikeComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<IDislikeComment_wlq> idislikeCommentWrapper = new QueryWrapper<>();
            idislikeCommentWrapper.eq("user_id", userId);
            List<IDislikeComment_wlq> idislikeComments = idislikeCommentMapper.selectList(idislikeCommentWrapper);
            for (IDislikeComment_wlq comment : idislikeComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                idislikeCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                idislikeCommentMapper.deleteById(comment.getIdislikeCommentId());
            }
            if (!idislikeComments.isEmpty()) {
                System.out.println("删除IDislikeComment记录 " + idislikeComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除IDislikeComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 4. IHaveComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<IHaveComment_wlq> iHaveCommentWrapper = new QueryWrapper<>();
            iHaveCommentWrapper.eq("user_id", userId);
            List<IHaveComment_wlq> iHaveComments = iHaveCommentMapper.selectList(iHaveCommentWrapper);
            for (IHaveComment_wlq comment : iHaveComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                iHaveCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                iHaveCommentMapper.deleteById(comment.getIhaveCommentId());
            }
            if (!iHaveComments.isEmpty()) {
                System.out.println("删除IHaveComment记录 " + iHaveComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除IHaveComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 5. ILikeComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<ILikeComment_wlq> iLikeCommentWrapper = new QueryWrapper<>();
            iLikeCommentWrapper.eq("user_id", userId);
            List<ILikeComment_wlq> iLikeComments = iLikeCommentMapper.selectList(iLikeCommentWrapper);
            for (ILikeComment_wlq comment : iLikeComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                iLikeCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                iLikeCommentMapper.deleteById(comment.getIlikeCommentId());
            }
            if (!iLikeComments.isEmpty()) {
                System.out.println("删除ILikeComment记录 " + iLikeComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除ILikeComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 6. ImagePostComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<ImagePostComment_wlq> imagePostCommentWrapper = new QueryWrapper<>();
            imagePostCommentWrapper.eq("user_id", userId);
            List<ImagePostComment_wlq> imagePostComments = imagePostCommentMapper.selectList(imagePostCommentWrapper);
            for (ImagePostComment_wlq comment : imagePostComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                imagePostCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                imagePostCommentMapper.deleteById(comment.getImagepostCommentId());
            }
            if (!imagePostComments.isEmpty()) {
                System.out.println("删除ImagePostComment记录 " + imagePostComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除ImagePostComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 7. INeedComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<INeedComment_wlq> iNeedCommentWrapper = new QueryWrapper<>();
            iNeedCommentWrapper.eq("user_id", userId);
            List<INeedComment_wlq> iNeedComments = iNeedCommentMapper.selectList(iNeedCommentWrapper);
            for (INeedComment_wlq comment : iNeedComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                iNeedCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                iNeedCommentMapper.deleteById(comment.getIneedCommentId());
            }
            if (!iNeedComments.isEmpty()) {
                System.out.println("删除INeedComment记录 " + iNeedComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除INeedComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 8. NovelpostComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<NovelpostComment_wlq> novelpostCommentWrapper = new QueryWrapper<>();
            novelpostCommentWrapper.eq("user_id", userId);
            List<NovelpostComment_wlq> novelpostComments = novelpostCommentMapper.selectList(novelpostCommentWrapper);
            for (NovelpostComment_wlq comment : novelpostComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                novelpostCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                novelpostCommentMapper.deleteById(comment.getNovelpostCommentId());
            }
            if (!novelpostComments.isEmpty()) {
                System.out.println("删除NovelpostComment记录 " + novelpostComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除NovelpostComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 9. TextpostComment_wlq - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            QueryWrapper<TextpostComment_wlq> textpostCommentWrapper = new QueryWrapper<>();
            textpostCommentWrapper.eq("user_id", userId);
            List<TextpostComment_wlq> textpostComments = textpostCommentMapper.selectList(textpostCommentWrapper);
            for (TextpostComment_wlq comment : textpostComments) {
                // 先设置deletedAt时间
                comment.setDeletedAt(deletedAt);
                textpostCommentMapper.updateById(comment);
                // 然后触发@TableLogic的逻辑删除
                textpostCommentMapper.deleteById(comment.getTextpostCommentId());
            }
            if (!textpostComments.isEmpty()) {
                System.out.println("删除TextpostComment记录 " + textpostComments.size() + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除TextpostComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 10. UserReviewBase_njj - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            int updateCount = userReviewBaseMapper.logicDeleteByUserId(userId, deletedAt);
            if (updateCount > 0) {
                System.out.println("删除UserReviewBase记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserReviewBase记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 11. ImageContentBase_wlq - 使用@TableLogic注解的isDeleted字段
        try {
            int updateCount = imageContentBaseMapper.logicDeleteByUserId(userId, deletedAt);
            if (updateCount > 0) {
                System.out.println("删除ImageContentBase记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除ImageContentBase记录时发生异常（表名包含特殊字符@niu）: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 12. UserBaseDynamic_njj - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            int updateCount = userBaseDynamicMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseDynamic记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseDynamic记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 13. UserBaseImagepostBase_njj - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            int updateCount = userBaseImagepostBaseMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseImagepostBase记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseImagepostBase记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 14. UserBaseLikeAction_njj - 使用@TableLogic注解的isCanceled字段 (Byte类型)
        try {
            int updateCount = userBaseLikeActionMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseLikeAction记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseLikeAction记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 15. UserBasePicComment_njj - 使用@TableLogic注解的isDeleted字段 (Integer类型)
        try {
            int updateCount = userBasePicCommentMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBasePicComment记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBasePicComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 16. UserBaseSystemMessage_njj - 使用@TableLogic注解的isDeleted字段 (Byte类型)
        try {
            int updateCount = userBaseSystemMessageMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseSystemMessage记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseSystemMessage记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 17. UserBaseTextComment_njj - 使用@TableLogic注解的isDeleted字段 (Integer类型)
        try {
            int updateCount = userBaseTextCommentMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseTextComment记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseTextComment记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 18. UserBaseUserCollectioin_njj - 使用@TableLogic注解的isDeleted字段 (Integer类型)
        try {
            int updateCount = userBaseUserCollectioinMapper.logicDeleteByUserId(userId);
            if (updateCount > 0) {
                System.out.println("删除UserBaseUserCollectioin记录 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("删除UserBaseUserCollectioin记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理有时间戳删除字段的实体
     * 需要手动设置deletedAt字段
     */
    @Async
    public void deleteEntitiesWithDeletedAt(Long userId, Timestamp deletedAt) {
        System.out.println("处理有时间戳删除字段的实体，用户ID: " + userId);
        
        // 1. AchievementBase_njj - deletedAt字段（无@TableLogic）
        // 注意：AchievementBase是系统级别的，不直接关联userId，这里仅处理用户创建的成就
        // 如果需要处理用户成就关联，需要在AchievementRecord中处理
        
        // 2. AchievementRecord_njj - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v3_achievement_record SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置AchievementRecord删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置AchievementRecord删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 3. IDislike_wlq - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v2_i_dislike SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置IDislike删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置IDislike删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 4. IHave_wlq - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v2_i_have SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置IHave删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置IHave删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 5. ILike_wlq - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v2_i_like SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置ILike删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置ILike删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 6. INeed_wlq - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v2_i_need SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置INeed删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置INeed删除时间时发生异常: "+ e.getMessage());
            e.printStackTrace();
        }
        
        // 7. NovelChapterInfo_wlq - deletedAt字段（无@TableLogic，但有isDeleted）
        // 先查找用户的所有小说
        try {
            String novelSql = "SELECT novel_id FROM v2_novelpost_base WHERE user_id = ?";
            List<Long> novelIds = jdbcTemplate.queryForList(novelSql, Long.class, userId);
            
            if (!novelIds.isEmpty()) {
                // 构建IN查询语句
                String inClause = novelIds.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(",", "(", ")"));
                
                String chapterSql = "UPDATE v2_novel_chapter_info SET deleted_at = ?, is_deleted = 1 WHERE novel_id IN " + inClause + " AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
                int updateCount = jdbcTemplate.update(chapterSql, deletedAt);
                if (updateCount > 0) {
                    System.out.println("设置NovelChapterInfo删除时间 " + updateCount + " 条，用户ID: " + userId);
                }
            }
        } catch (Exception e) {
            System.err.println("处理NovelChapterInfo记录时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 8. NovelContentBase_wlq - deletedAt字段（有isDeleted）
        try {
            String sql = "UPDATE v2_novel_content_base SET deleted_at = ?, is_deleted = 1 WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置NovelContentBase删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置NovelContentBase删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 9. NovelpostBase_wlq - deletedAt字段（有isDeleted）
        try {
            String sql = "UPDATE v2_novelpost_base SET deleted_at = ?, is_deleted = 1 WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置NovelpostBase删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置NovelpostBase删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 10. TextpostBase_wlq - deletedAt字段（无@TableLogic）
        try {
            String sql = "UPDATE v2_textpost_base SET deleted_at = ? WHERE user_id = ? AND (deleted_at IS NULL OR deleted_at = '1970-01-01 08:00:00')";
            int updateCount = jdbcTemplate.update(sql, deletedAt, userId);
            if (updateCount > 0) {
                System.out.println("设置TextpostBase删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置TextpostBase删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 处理没有@TableLogic但需要手动设置删除标记的实体
     * 主要处理布尔类型的删除标记字段
     */
    @Async
    public void deleteEntitiesWithManualFlag(Long userId, Timestamp deletedAt) {
        System.out.println("处理需要手动设置删除标记的实体，用户ID: " + userId);
        
        // 注意：大部分实体都已经通过@TableLogic或deletedAt处理了
        // 这里主要用于处理特殊情况
        
        // UserNovelRelation_njj - 只有deletedAt时间戳字段，没有isDeleted删除标识
        try {
            int updateCount = userNovelRelationMapper.setDeletedAtByUserId(userId, deletedAt);
            if (updateCount > 0) {
                System.out.println("设置UserNovelRelation删除时间 " + updateCount + " 条，用户ID: " + userId);
            }
        } catch (Exception e) {
            System.err.println("设置UserNovelRelation删除时间时发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}