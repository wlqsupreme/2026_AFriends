package Afriends_v3.controller;

import Afriends_v3.entity.*;
import com.baomidou.mybatisplus.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@RestController
@RequestMapping("/api/entities")
@CrossOrigin(origins = "*")
public class EntityController {

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllEntities() {
        try {
            Map<String, Object> result = new HashMap<>();

            // 获取所有实体类信息
            List<Map<String, Object>> entityClasses = getEntityClasses();
            List<Map<String, Object>> listClasses = getListClasses();
            
            // 获取所有_wlq实体类信息
            List<Map<String, Object>> wlqEntityClasses = getWlqEntityClasses();
            List<Map<String, Object>> wlqListClasses = getWlqListClasses();

            // 合并所有实体类
            List<Map<String, Object>> allEntityClasses = new ArrayList<>();
            allEntityClasses.addAll(entityClasses);
            allEntityClasses.addAll(wlqEntityClasses);
            
            List<Map<String, Object>> allListClasses = new ArrayList<>();
            allListClasses.addAll(listClasses);
            allListClasses.addAll(wlqListClasses);

            result.put("entityClasses", allEntityClasses);
            result.put("listClasses", allListClasses);
            result.put("totalEntityCount", allEntityClasses.size());
            result.put("totalListCount", allListClasses.size());
            result.put("njjEntityCount", entityClasses.size());
            result.put("njjListCount", listClasses.size());
            result.put("wlqEntityCount", wlqEntityClasses.size());
            result.put("wlqListCount", wlqListClasses.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // 打印详细错误信息
            Map<String, Object> error = new HashMap<>();
            error.put("error", "获取实体类信息失败: " + e.getMessage());
            error.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(error);
        }
    }

    private List<Map<String, Object>> getEntityClasses() {
        List<Map<String, Object>> entities = new ArrayList<>();

        // 动态获取所有实体类
        Class<?>[] entityClasses = {
                UserInfo_njj.class,
                AchievementBase_njj.class,
                AchievementRecord_njj.class,
                AgreementBase_njj.class,
                AimodelBaseInfo_njj.class,
                AimodelCoinLog_njj.class,
                AimodelLevelRule_njj.class,
                AiTaskLog_njj.class,
                BlockRecord_njj.class,
                CertBase_njj.class,
                CertJobBase_njj.class,
                CertRealnameBase_njj.class,
                CertStudentBase_njj.class,
                ChatSettingsBase_njj.class,
                EmojiBase_njj.class,
                FriendsProfile_njj.class,
                MessageExtraBase_njj.class,
                ProductsBase_njj.class,
                PurchaseRecord_njj.class,
                ReportCategory_njj.class,
                ReportRecord_njj.class,
                TaskComment_njj.class,
                UserAiComment_njj.class,
                UserAiModel_njj.class,
                UserBaseDynamic_njj.class,
                UserBaseImagepostBase_njj.class,
                UserBaseLikeAction_njj.class,
                UserBasePicComment_njj.class,
                UserBaseSystemMessage_njj.class,
                UserBaseTextComment_njj.class,
                UserBaseUserCollectioin_njj.class,
                UserCertRecord_njj.class,
                UserChatDetail_njj.class,
                UserChatList_njj.class,
                UserContentViewLog_njj.class,
                UserDevice_njj.class,
                UserDislikeRelation_njj.class,
                UserFriendsRelationship_njj.class,
                UserHardTagRelation_njj.class,
                UserImageRecommendation_njj.class,
                UserInfoFeatureVector_njj.class,
                UserInfoQuestion_njj.class,
                UserLikeRelation_njj.class,
                UserNovelRecommendation_njj.class,
                UserNovelRelation_njj.class,
                UserReviewBase_njj.class,
                UserSettingRelation_njj.class,
                UserSoftTagRelation_njj.class,
                UserSystemMessage_njj.class,
                UserTaskRelationship_njj.class,
                UserTextRecommendation_njj.class
        };

        for (Class<?> clazz : entityClasses) {
            Map<String, Object> entityInfo = new HashMap<>();
            entityInfo.put("className", clazz.getSimpleName());
            entityInfo.put("fullName", clazz.getName());
            entityInfo.put("packageName", clazz.getPackage().getName());

            // 获取表名注解
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                entityInfo.put("tableName", tableNameAnnotation.value());
            }

            // 获取字段信息
            List<Map<String, Object>> fields = getFieldInfo(clazz);
            entityInfo.put("fields", fields);
            entityInfo.put("fieldCount", fields.size());

            entities.add(entityInfo);
        }

        return entities;
    }

    private List<Map<String, Object>> getListClasses() {
        List<Map<String, Object>> listClasses = new ArrayList<>();

        // 动态获取所有List类
        Class<?>[] listClassArray = {
                UserInfo_list_njj.class,
                AchievementBase_list_njj.class,
                AchievementRecord_list_njj.class,
                AgreementBase_list_njj.class,
                AimodelBaseInfo_list_njj.class,
                AimodelCoinLog_list_njj.class,
                AimodelLevelRule_list_njj.class,
                AiTaskLog_list_njj.class,
                BlockRecord_list_njj.class,
                CertBase_list_njj.class,
                CertJobBase_list_njj.class,
                CertRealnameBase_list_njj.class,
                CertStudentBase_list_njj.class,
                ChatSettingsBase_list_njj.class,
                EmojiBase_list_njj.class,
                FriendsProfile_list_njj.class,
                MessageExtraBase_list_njj.class,
                ProductsBase_list_njj.class,
                PurchaseRecord_list_njj.class,
                ReportCategory_list_njj.class,
                ReportRecord_list_njj.class,
                TaskComment_list_njj.class,
                UserAiComment_list_njj.class,
                UserAiModel_list_njj.class,
                UserBaseDynamic_list_njj.class,
                UserBaseImagepostBase_list_njj.class,
                UserBaseLikeAction_list_njj.class,
                UserBasePicComment_list_njj.class,
                UserBaseSystemMessage_list_njj.class,
                UserBaseTextComment_list_njj.class,
                UserBaseUserCollectioin_list_njj.class,
                UserCertRecord_list_njj.class,
                UserChatDetail_list_njj.class,
                UserChatList_list_njj.class,
                UserContentViewLog_list_njj.class,
                UserDevice_list_njj.class,
                UserDislikeRelation_list_njj.class,
                UserFriendsRelationship_list_njj.class,
                UserHardTagRelation_list_njj.class,
                UserImageRecommendation_list_njj.class,
                UserInfoFeatureVector_list_njj.class,
                UserInfoQuestion_list_njj.class,
                UserLikeRelation_list_njj.class,
                UserNovelRecommendation_list_njj.class,
                UserNovelRelation_list_njj.class,
                UserReviewBase_list_njj.class,
                UserSettingRelation_list_njj.class,
                UserSoftTagRelation_list_njj.class,
                UserSystemMessage_list_njj.class,
                UserTaskRelationship_list_njj.class,
                UserTextRecommendation_list_njj.class
        };

        for (Class<?> clazz : listClassArray) {
            Map<String, Object> listInfo = new HashMap<>();
            listInfo.put("className", clazz.getSimpleName());
            listInfo.put("fullName", clazz.getName());
            listInfo.put("packageName", clazz.getPackage().getName());

            // 获取泛型信息
            try {
                Class<?> superClass = clazz.getSuperclass();
                if (superClass != null && superClass.getGenericSuperclass() != null) {
                    String genericInfo = superClass.getGenericSuperclass().toString();
                    listInfo.put("genericInfo", genericInfo);
                }
            } catch (Exception e) {
                listInfo.put("genericInfo", "无法获取泛型信息");
            }

            listClasses.add(listInfo);
        }

        return listClasses;
    }

    private List<Map<String, Object>> getFieldInfo(Class<?> clazz) {
        List<Map<String, Object>> fields = new ArrayList<>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            // 跳过静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Map<String, Object> fieldInfo = new HashMap<>();
            fieldInfo.put("name", field.getName());
            fieldInfo.put("type", field.getType().getSimpleName());
            fieldInfo.put("fullType", field.getType().getName());

            // 获取注解信息
            Map<String, Object> annotations = new HashMap<>();

            if (field.isAnnotationPresent(TableId.class)) {
                TableId tableId = field.getAnnotation(TableId.class);
                annotations.put("tableId", true);
                annotations.put("idType", tableId.type().toString());
            }

            if (field.isAnnotationPresent(TableField.class)) {
                TableField tableField = field.getAnnotation(TableField.class);
                annotations.put("tableField", true);
                annotations.put("exist", tableField.exist());
            }

            fieldInfo.put("annotations", annotations);
            fields.add(fieldInfo);
        }

        return fields;
    }

    private List<Map<String, Object>> getWlqEntityClasses() {
        List<Map<String, Object>> entities = new ArrayList<>();

        // 动态获取所有_wlq实体类
        Class<?>[] wlqEntityClasses = {
                ActionExp_wlq.class,
                AiChatListDetailR_wlq.class,
                AiMatches_wlq.class,
                AiTaskRequire_wlq.class,
                AiTaskRespond_wlq.class,
                CommentLikeRelation_wlq.class,
                ContentDislikeRelation_wlq.class,
                ContentFavouriteRelation_wlq.class,
                ContentFeedbackRelation_wlq.class,
                ContentLikeRelation_wlq.class,
                FriendList_wlq.class,
                GiftBase_wlq.class,
                HardTagBase_wlq.class,
                IDislike_wlq.class,
                IDislikeComment_wlq.class,
                IDislikeFeatureVector_wlq.class,
                IHave_wlq.class,
                IHaveComment_wlq.class,
                IHaveFeatureVector_wlq.class,
                ILike_wlq.class,
                ILikeComment_wlq.class,
                ILikeFeatureVector_wlq.class,
                ImageContentBase_wlq.class,
                ImagePostBase_wlq.class,
                ImagePostComment_wlq.class,
                ImagePostFeatureVector_wlq.class,
                INeed_wlq.class,
                INeedComment_wlq.class,
                INeedFeatureVector_wlq.class,
                IntiniallabelChen_wlq.class,
                LikeRobotActionLog_wlq.class,
                NovelChapterInfo_wlq.class,
                NovelContentBase_wlq.class,
                NovelpostBase_wlq.class,
                NovelpostComment_wlq.class,
                NovelpostFeatureVector_wlq.class,
                Recharge_wlq.class,
                RecommendedAvatar_wlq.class,
                RecommendedLabel_wlq.class,
                RecommendedNickname_wlq.class,
                SettingBase_wlq.class,
                SoftTagBase_wlq.class,
                SoftTagCategory_wlq.class,
                TextpostBase_wlq.class,
                TextpostComment_wlq.class,
                TextpostFeatureVector_wlq.class,
                UserAiRequire_wlq.class,
                UserAiRequireFeatureVector_wlq.class,
                UserAiRespond_wlq.class,
                UserAiRespondFeatureVector_wlq.class,
                UserBase_wlq.class
        };

        for (Class<?> clazz : wlqEntityClasses) {
            Map<String, Object> entityInfo = new HashMap<>();
            entityInfo.put("className", clazz.getSimpleName());
            entityInfo.put("fullName", clazz.getName());
            entityInfo.put("packageName", clazz.getPackage().getName());

            // 获取表名注解
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                entityInfo.put("tableName", tableNameAnnotation.value());
            }

            // 获取字段信息
            List<Map<String, Object>> fields = getFieldInfo(clazz);
            entityInfo.put("fields", fields);
            entityInfo.put("fieldCount", fields.size());

            entities.add(entityInfo);
        }

        return entities;
    }

    private List<Map<String, Object>> getWlqListClasses() {
        List<Map<String, Object>> listClasses = new ArrayList<>();

        // 动态获取所有_wlq List类
        Class<?>[] wlqListClassArray = {
                ActionExp_list_wlq.class,
                AiChatListDetailR_list_wlq.class,
                AiMatches_list_wlq.class,
                AiTaskRequire_list_wlq.class,
                AiTaskRespond_list_wlq.class,
                CommentLikeRelation_list_wlq.class,
                ContentDislikeRelation_list_wlq.class,
                ContentFavouriteRelation_list_wlq.class,
                ContentFeedbackRelation_list_wlq.class,
                ContentLikeRelation_list_wlq.class,
                FriendList_list_wlq.class,
                GiftBase_list_wlq.class,
                HardTagBase_list_wlq.class,
                IDislike_list_wlq.class,
                IDislikeComment_list_wlq.class,
                IDislikeFeatureVector_list_wlq.class,
                IHave_list_wlq.class,
                IHaveComment_list_wlq.class,
                IHaveFeatureVector_list_wlq.class,
                ILike_list_wlq.class,
                ILikeComment_list_wlq.class,
                ILikeFeatureVector_list_wlq.class,
                ImageContentBase_list_wlq.class,
                ImagePostBase_list_wlq.class,
                ImagePostComment_list_wlq.class,
                ImagePostFeatureVector_list_wlq.class,
                INeed_list_wlq.class,
                INeedComment_list_wlq.class,
                INeedFeatureVector_list_wlq.class,
                IntiniallabelChen_list_wlq.class,
                LikeRobotActionLog_list_wlq.class,
                NovelChapterInfo_list_wlq.class,
                NovelContentBase_list_wlq.class,
                NovelpostBase_list_wlq.class,
                NovelpostComment_list_wlq.class,
                NovelpostFeatureVector_list_wlq.class,
                Recharge_list_wlq.class,
                RecommendedAvatar_list_wlq.class,
                RecommendedLabel_list_wlq.class,
                RecommendedNickname_list_wlq.class,
                SettingBase_list_wlq.class,
                SoftTagBase_list_wlq.class,
                SoftTagCategory_list_wlq.class,
                TextpostBase_list_wlq.class,
                TextpostComment_list_wlq.class,
                TextpostFeatureVector_list_wlq.class,
                UserAiRequire_list_wlq.class,
                UserAiRequireFeatureVector_list_wlq.class,
                UserAiRespond_list_wlq.class,
                UserAiRespondFeatureVector_list_wlq.class,
                UserBase_list_wlq.class
        };

        for (Class<?> clazz : wlqListClassArray) {
            Map<String, Object> listInfo = new HashMap<>();
            listInfo.put("className", clazz.getSimpleName());
            listInfo.put("fullName", clazz.getName());
            listInfo.put("packageName", clazz.getPackage().getName());

            // 获取泛型信息
            try {
                Class<?> superClass = clazz.getSuperclass();
                if (superClass != null && superClass.getGenericSuperclass() != null) {
                    String genericInfo = superClass.getGenericSuperclass().toString();
                    listInfo.put("genericInfo", genericInfo);
                }
            } catch (Exception e) {
                listInfo.put("genericInfo", "无法获取泛型信息");
            }

            listClasses.add(listInfo);
        }

        return listClasses;
    }
}
