package Afriends_v3.util;

import Afriends_v3.entity.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 隐私设置工具类
 * 提供隐私设置检查和好友关系判断的通用方法
 */
public class PrivacySettingsUtil {

    private static final Long FRIEND_RELATION_FUNCTION_ID = 20000002L;

    /**
     * 判断两个用户是否为好友关系
     * @param userId1 用户1的ID
     * @param userId2 用户2的ID
     * @return true表示是好友，false表示不是好友
     */
    public static boolean isFriends(Long userId1, Long userId2) {
        if (userId1 == null || userId2 == null || userId1.equals(userId2)) {
            return false;
        }

        try {
            // 从缓存中获取用户1的好友关系列表
            List<UserFriendsRelationship_njj> user1Friends = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId1);

            // 检查用户2是否在用户1的好友列表中
            for (UserFriendsRelationship_njj relationship : user1Friends) {
                if (relationship.getFriendsId() != null && relationship.getFriendsId().equals(userId2)
                        && relationship.getFunctionId() != null
                        && relationship.getFunctionId().equals(FRIEND_RELATION_FUNCTION_ID)) {
                    return true;
                }
            }

            // 双向检查：检查用户1是否在用户2的好友列表中（如果需要双向验证）
            List<UserFriendsRelationship_njj> user2Friends = UserFriendsRelationship_list_njj
                    .getUserFriendsRelationshipByUserId(userId2);

            for (UserFriendsRelationship_njj relationship : user2Friends) {
                if (relationship.getFriendsId() != null && relationship.getFriendsId().equals(userId1)
                        && relationship.getFunctionId() != null
                        && relationship.getFunctionId().equals(FRIEND_RELATION_FUNCTION_ID)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("PrivacySettingsUtil: 判断好友关系失败: " + e.getMessage());
            e.printStackTrace();
            return false; // 出错时默认返回false，保证安全
        }
    }

    /**
     * 获取用户的隐私设置值
     * @param userId 用户ID
     * @param settingBase 设置基础数据
     * @param defaultValue 默认值（如果未找到设置项或未设置，返回此值）
     * @return 设置值（布尔值）
     */
    public static boolean getPrivacySetting(Long userId, SettingBase_wlq settingBase, boolean defaultValue) {
        if (userId == null || settingBase == null) {
            return defaultValue;
        }

        try {
            // 获取用户的该设置值
            List<UserSettingRelation_njj> userSettings = UserSettingRelation_list_njj
                    .getUserSettingRelationByUserId(userId);

            for (UserSettingRelation_njj relation : userSettings) {
                if (relation.getSettingId() != null && relation.getSettingId().equals(settingBase.getId())) {
                    String valueText = relation.getValueText();
                    if (valueText != null) {
                        return "true".equalsIgnoreCase(valueText.trim()) ||
                            "1".equals(valueText.trim()) ||
                            "\"true\"".equalsIgnoreCase(valueText.trim());
                    }
                    break;
                }
            }

            // 如果用户没有设置过，使用设置基础表中的默认值
            String defaultVal = settingBase.getDefaultValue();
            if (defaultVal != null) {
                // 对于"仅允许好友@"设置，即使数据库默认值是true，我们也希望新用户默认可以被任何人@
                if ("privacy.allowFriendsAt".equals(settingBase.getSettingKey())) {
                    return false; // 默认允许任何人@
                }
                return "true".equalsIgnoreCase(defaultVal.trim()) || "1".equals(defaultVal.trim());
            }

            return defaultValue;
        } catch (Exception e) {
            System.err.println("PrivacySettingsUtil: 获取隐私设置失败: " + e.getMessage());
            e.printStackTrace();
            return defaultValue; // 出错时返回默认值
        }
    }

    /**
     * 获取用户的隐私设置值
     * @param userId 用户ID
     * @param settingKey 设置键名（如privacy.allowFriendsComment）
     * @param defaultValue 默认值（如果未找到设置项或未设置，返回此值）
     * @return 设置值（布尔值）
     */
    public static boolean getPrivacySetting(Long userId, String settingKey, boolean defaultValue) {
        if (userId == null || settingKey == null) {
            return defaultValue;
        }

        try {
            // 从设置基础表中查找设置项
            SettingBase_wlq settingBase = findSettingBaseByKey(settingKey);
            if (settingBase == null) {
                System.out.println("PrivacySettingsUtil: 设置基础表中未找到设置项: " + settingKey + "，使用默认值: " + defaultValue);
                return defaultValue;
            }

            return getPrivacySetting(userId, settingBase, defaultValue);
        } catch (Exception e) {
            System.err.println("PrivacySettingsUtil: 获取隐私设置失败: " + e.getMessage());
            e.printStackTrace();
            return defaultValue; // 出错时返回默认值
        }
    }

    /**
     * 根据settingKey查找设置基础数据
     * 注意：这个方法应该只在Service层调用，不应该在工具类中直接访问数据库
     * 工具类中的实现应该只从缓存中查找，数据库访问应在Service层处理
     */
    private static SettingBase_wlq findSettingBaseByKey(String settingKey) {
        try {
            // 先尝试从内存缓存中查找
            List<SettingBase_wlq> settingBases = SettingBase_list_wlq.searchSettingBaseBySettingKey(settingKey);
            for (SettingBase_wlq sb : settingBases) {
                if (sb.getSettingKey() != null && sb.getSettingKey().equals(settingKey)) {
                    return sb;
                }
            }
            
            // 如果缓存中没有且缓存未加载，则尝试强制加载一次
            if (!SettingBase_list_wlq.isCacheLoaded()) {
                System.out.println("PrivacySettingsUtil: 检测到设置基础数据缓存未加载，可能需要初始化");
            }
            
            // 缓存中没有找到，返回null，由调用方决定是否从数据库中查找
            return null;
        } catch (Exception e) {
            System.err.println("PrivacySettingsUtil: 查找设置基础数据失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 检查是否仅允许好友评论
     * 
     * @param postAuthorId 帖子作者ID
     * @param commenterId 评论者ID
     * @return true表示允许评论，false表示不允许
     */
    public static boolean canFriendComment(Long postAuthorId, Long commenterId) {
        if (postAuthorId == null || commenterId == null) {
            return false;
        }

        // 如果是自己评论自己，允许
        if (postAuthorId.equals(commenterId)) {
            return true;
        }

        // 检查帖子作者是否开启了"仅允许好友评论"设置
        boolean onlyAllowFriendsComment = getPrivacySetting(postAuthorId, "privacy.allowFriendsComment", false);
        
        // 如果未开启"仅允许好友评论"，则所有人都可以评论
        if (!onlyAllowFriendsComment) {
            return true;
        }
        
        // 如果开启了"仅允许好友评论"，则检查是否为好友关系
        return isFriends(postAuthorId, commenterId);
    }

    /**
     * 检查是否仅允许好友@我
     * @param targetUserId 被@的用户ID
     * @param mentionerId @操作的用户ID
     * @return true表示允许，false表示不允许
     */
    public static boolean canFriendAt(Long targetUserId, Long mentionerId) {
        if (targetUserId == null || mentionerId == null) {
            return false;
        }

        // 如果是自己@自己，允许
        if (targetUserId.equals(mentionerId)) {
            return true;
        }

        // 检查被@用户是否开启了@权限（注意：这里的语义是"仅允许好友@我"）
        // true表示仅允许好友@我，false表示任何人都可以@我
        // 对于没有设置的用户，默认允许任何人@
        boolean onlyAllowFriendsAt = getPrivacySetting(targetUserId, "privacy.allowFriendsAt", false);
        
        // 如果开启了"仅允许好友@我"（值为true），则检查是否是好友
        if (onlyAllowFriendsAt) {
            return isFriends(targetUserId, mentionerId);
        }
        
        // 如果关闭了"仅允许好友@我"（值为false），则任何人都可以@自己
        return true;
    }

    /**
     * 检查收藏是否公开
     * @param collectionOwnerId 收藏所有者ID
     * @param viewerId 查看者ID
     * @return true表示可以查看，false表示不可查看
     */
    public static boolean canViewCollections(Long collectionOwnerId, Long viewerId) {
        if (collectionOwnerId == null || viewerId == null) {
            return false;
        }

        // 如果是本人查看，允许
        if (collectionOwnerId.equals(viewerId)) {
            return true;
        }

        // 检查隐私设置
        boolean publicCollections = getPrivacySetting(collectionOwnerId, "privacy.publicCollections", true);
        return publicCollections;
    }

    /**
     * 检查是否需要二次确认（一键防护）
     * @param userId 用户ID
     * @return true表示需要二次确认，false表示不需要
     */
    public static boolean needOneClickProtection(Long userId) {
        if (userId == null) {
            return false;
        }
        return getPrivacySetting(userId, "privacy.oneClickProtection", false);
    }
}