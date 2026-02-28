package Afriends_v3.controller;

import Afriends_v3.mapper.UserBaseMapper;
import Afriends_v3.service.BCEntityService;
import Afriends_v3.service.UserFollowService;
import Afriends_v3.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 好友相关操作控制器
 * 提供好友管理相关功能的API接口
 */
@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendsController {

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    private BCEntityService bcEntityService;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private UserBaseMapper userBaseMapper;

    /**
     * 设置朋友圈权限
     * POST /api/friends/moments-permission
     * 请求体：{"userId": 1000100, "friendId": 1000101, "permissionType": "allow_all"}
     */
    @PostMapping("/moments-permission")
    public ResponseEntity<Map<String, Object>> setMomentsPermission(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 参数校验
            if (requestBody.get("userId") == null || requestBody.get("friendId") == null 
                    || requestBody.get("permissionType") == null) {
                result.put("success", false);
                result.put("message", "参数不完整（userId、friendId、permissionType为必填项）");
                return ResponseEntity.badRequest().body(result);
            }

            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long friendId = Long.valueOf(requestBody.get("friendId").toString());
            String permissionType = requestBody.get("permissionType").toString();

            // 2. 验证用户是否存在
            if (userBaseMapper.selectById(userId) == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                result.put("userId", userId);
                return ResponseEntity.status(404).body(result);
            }

            // 3. 验证好友是否存在
            if (userBaseMapper.selectById(friendId) == null) {
                result.put("success", false);
                result.put("message", "好友不存在");
                result.put("friendId", friendId);
                return ResponseEntity.status(404).body(result);
            }

            // 4. 验证权限类型
            Set<String> validPermissionTypes = Set.of("allow_all", "chat_only", "hide_theirs", "hide_mine");
            if (!validPermissionTypes.contains(permissionType)) {
                result.put("success", false);
                result.put("message", "无效的权限类型，支持的权限类型: " + String.join(", ", validPermissionTypes));
                result.put("permissionType", permissionType);
                return ResponseEntity.badRequest().body(result);
            }

            // 5. 根据权限类型确定 settingId 和 settingValue
            Long settingId;
            String settingValue;

            switch (permissionType) {
                case "allow_all":
                    // 允许查看朋友圈和视频号 - 设置为允许
                    settingId = 10000004L; // 提醒（或其他相关设置ID）
                    settingValue = "[\"1\"]"; // 允许
                    break;
                case "chat_only":
                    // 仅聊天 - 设置为不允许
                    settingId = 10000004L;
                    settingValue = "[\"0\"]"; // 不允许
                    break;
                case "hide_theirs":
                    // 不看他的朋友圈 - 可通过扩展实现
                    settingId = 10000004L;
                    settingValue = "[\"0\"]";
                    break;
                case "hide_mine":
                    // 不让他看我的朋友圈 - 可通过扩展实现
                    settingId = 10000004L;
                    settingValue = "[\"0\"]";
                    break;
                default:
                    result.put("success", false);
                    result.put("message", "未支持的权限类型");
                    return ResponseEntity.badRequest().body(result);
            }

            // 6. 保存权限设置（使用现有的保存好友设置接口）
            Map<String, Object> settingsData = new HashMap<>();
            settingsData.put("userId", userId);
            settingsData.put("friendId", friendId);
            settingsData.put("settingId", settingId);
            settingsData.put("settingValue", settingValue);

            Map<String, Object> saveResult = userEntityService.saveUserFriendsSettings(settingsData);
            if (Boolean.FALSE.equals(saveResult.get("success"))) {
                return ResponseEntity.badRequest().body(saveResult);
            }

            result.put("success", true);
            result.put("message", "朋友圈权限设置成功");
            result.put("permissionType", permissionType);
            return ResponseEntity.ok(result);

        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "参数格式错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            System.err.println("设置朋友圈权限失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "设置朋友圈权限失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 拉黑/取消拉黑用户
     * POST /api/friends/block
     * 请求体：{"userId": 1000100, "friendId": 1000101, "isBlocked": true}
     */
    @PostMapping("/block")
    public ResponseEntity<Map<String, Object>> blockUser(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 参数校验
            if (requestBody.get("userId") == null || requestBody.get("friendId") == null 
                    || requestBody.get("isBlocked") == null) {
                result.put("success", false);
                result.put("message", "参数不完整（userId、friendId、isBlocked为必填项）");
                return ResponseEntity.badRequest().body(result);
            }

            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long friendId = Long.valueOf(requestBody.get("friendId").toString());
            Boolean isBlocked = Boolean.valueOf(requestBody.get("isBlocked").toString());

            // 2. 验证用户是否存在
            if (userBaseMapper.selectById(userId) == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                result.put("userId", userId);
                return ResponseEntity.status(404).body(result);
            }

            // 3. 验证好友是否存在
            if (userBaseMapper.selectById(friendId) == null) {
                result.put("success", false);
                result.put("message", "好友不存在");
                result.put("friendId", friendId);
                return ResponseEntity.status(404).body(result);
            }

            // 4. 执行拉黑/取消拉黑操作
            if (isBlocked) {
                // 拉黑
                try {
                    bcEntityService.addBlockRecord(userId, friendId);
                    result.put("success", true);
                    result.put("message", "拉黑成功");
                    return ResponseEntity.ok(result);
                } catch (Exception e) {
                    // 如果已经拉黑，返回成功（幂等操作）
                    if (e.getMessage() != null && e.getMessage().contains("已拉黑")) {
                        result.put("success", true);
                        result.put("message", "该用户已在黑名单中");
                        return ResponseEntity.ok(result);
                    }
                    throw e;
                }
            } else {
                // 取消拉黑
                try {
                    bcEntityService.unblockUser(userId, friendId);
                    result.put("success", true);
                    result.put("message", "取消拉黑成功");
                    return ResponseEntity.ok(result);
                } catch (Exception e) {
                    // 如果没有拉黑记录，返回成功（幂等操作）
                    if (e.getMessage() != null && e.getMessage().contains("未拉黑")) {
                        result.put("success", true);
                        result.put("message", "该用户不在黑名单中");
                        return ResponseEntity.ok(result);
                    }
                    throw e;
                }
            }

        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "参数格式错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            System.err.println("拉黑/取消拉黑操作失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "操作失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 删除好友
     * POST /api/friends/delete
     * 请求体：{"userId": 1000100, "friendId": 1000101}
     */
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteFriend(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 参数校验
            if (requestBody.get("userId") == null || requestBody.get("friendId") == null) {
                result.put("success", false);
                result.put("message", "参数不完整（userId、friendId为必填项）");
                return ResponseEntity.badRequest().body(result);
            }

            Long userId = Long.valueOf(requestBody.get("userId").toString());
            Long friendId = Long.valueOf(requestBody.get("friendId").toString());

            // 2. 验证用户是否存在
            if (userBaseMapper.selectById(userId) == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                result.put("userId", userId);
                return ResponseEntity.status(404).body(result);
            }

            // 3. 验证好友是否存在
            if (userBaseMapper.selectById(friendId) == null) {
                result.put("success", false);
                result.put("message", "好友不存在");
                result.put("friendId", friendId);
                return ResponseEntity.status(404).body(result);
            }

            // 4. 执行取消关注操作（删除好友关系）
            boolean unfollowResult = userFollowService.unfollowUser(userId, friendId);
            if (unfollowResult == false) {
                // unfollowUser 返回 false 表示成功取消关注
                result.put("success", true);
                result.put("message", "删除好友成功");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "删除好友失败，可能不存在好友关系");
                return ResponseEntity.badRequest().body(result);
            }

        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "参数格式错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            System.err.println("删除好友失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "删除好友失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
