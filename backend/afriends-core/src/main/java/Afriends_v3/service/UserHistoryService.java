package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Afriends_v3.mapper.UserBaseMapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户浏览历史服务类
 * 负责处理浏览历史的查询、删除和清空操作
 */
@Service
public class UserHistoryService {

    @Autowired
    private UserContentViewLogMapper userContentViewLogMapper;

    @Autowired
    private TextpostBaseMapper textpostBaseMapper;

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;

    @Autowired
    private NovelpostBaseMapper novelpostBaseMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取用户的浏览历史列表
     * 
     * @param userId 用户ID
     * @return 浏览历史列表
     */
    public List<Map<String, Object>> getUserHistory(Long userId) {
        System.out.println("UserHistoryService: 开始获取用户 " + userId + " 的浏览历史");
        
        List<Map<String, Object>> historyList = new ArrayList<>();
        
        try {
            // 1. 查询用户的浏览历史记录（按时间降序）
            QueryWrapper<UserContentViewLog_njj> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .orderByDesc("created_at")
                       .orderByDesc("updated_at");
            
            List<UserContentViewLog_njj> logs = userContentViewLogMapper.selectList(queryWrapper);
            
            System.out.println("UserHistoryService: 查询到 " + logs.size() + " 条浏览历史记录");
            
            // 2. 转换为前端需要的格式
            for (UserContentViewLog_njj log : logs) {
                try {
                    Map<String, Object> historyItem = convertLogToHistoryItem(log);
                    if (historyItem != null) {
                        historyList.add(historyItem);
                    }
                } catch (Exception e) {
                    System.err.println("UserHistoryService: 转换浏览历史记录失败，historyId=" + log.getId() + ", 错误: " + e.getMessage());
                    // 继续处理下一条记录
                }
            }
            
            System.out.println("UserHistoryService: 成功转换 " + historyList.size() + " 条浏览历史记录");
            
        } catch (Exception e) {
            System.err.println("UserHistoryService: 获取浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("获取浏览历史失败: " + e.getMessage(), e);
        }
        
        return historyList;
    }

    /**
     * 将浏览日志转换为前端需要的历史记录格式
     */
    private Map<String, Object> convertLogToHistoryItem(UserContentViewLog_njj log) {
        if (log == null || log.getContentId() == null || log.getContentType() == null) {
            return null;
        }

        Map<String, Object> item = new HashMap<>();
        
        // 基本信息
        item.put("id", log.getId());
        item.put("targetId", log.getContentId());
        
        // 内容类型转换（1=文字, 2=图片, 3=小说）
        Byte contentType = log.getContentType();
        String type;
        if (contentType == 1) {
            type = "post";
        } else if (contentType == 2) {
            type = "image";
        } else if (contentType == 3) {
            type = "novel";
        } else {
            type = "post"; // 默认
        }
        item.put("type", type);

        // 获取内容详情（标题、缩略图等）
        Map<String, Object> contentInfo = getContentInfo(log.getContentId(), contentType.intValue());
        if (contentInfo != null) {
            item.put("title", contentInfo.get("title") != null ? contentInfo.get("title") : "未知内容");
            item.put("subtitle", contentInfo.get("subtitle") != null ? contentInfo.get("subtitle") : "");
            item.put("contentThumbnail", contentInfo.get("thumbnail") != null ? contentInfo.get("thumbnail") : "");
            
            // 判断是否为自己的内容
            Long contentUserId = getContentUserId(log.getContentId(), contentType.intValue());
            item.put("isOwn", contentUserId != null && contentUserId.equals(log.getUserId()));
        } else {
            item.put("title", "内容已删除");
            item.put("subtitle", "");
            item.put("contentThumbnail", "");
            item.put("isOwn", false);
        }

        // 浏览时间
        Timestamp browseTime = log.getCreateAt() != null ? log.getCreateAt() : log.getUpdateAt();
        if (browseTime != null) {
            item.put("browseDate", DATE_FORMAT.format(browseTime));
        } else {
            item.put("browseDate", DATE_FORMAT.format(new Date()));
        }

        return item;
    }

    /**
     * 获取内容信息（标题、缩略图等）
     */
    private Map<String, Object> getContentInfo(Long contentId, int contentType) {
        Map<String, Object> contentInfo = new HashMap<>();

        try {
            if (contentType == 1) {
                // 文字动态
                TextpostBase_wlq textPost = TextpostBase_list_wlq.getTextpostBaseById(contentId);
                if (textPost == null) {
                    textPost = textpostBaseMapper.selectById(contentId);
                }
                if (textPost != null) {
                    String title = textPost.getContentText();
                    if (title != null && title.length() > 50) {
                        title = title.substring(0, 50) + "...";
                    }
                    contentInfo.put("title", title != null ? title : "文字动态");
                    contentInfo.put("subtitle", "文字动态");
                    contentInfo.put("thumbnail", "");
                }

            } else if (contentType == 2) {
                // 图片动态
                ImagePostBase_wlq imagePost = ImagePostBase_list_wlq.getImagePostBaseById(contentId);
                if (imagePost == null) {
                    imagePost = imagePostBaseMapper.selectById(contentId);
                }
                if (imagePost != null) {
                    String title = imagePost.getContentText();
                    if (title != null && title.length() > 50) {
                        title = title.substring(0, 50) + "...";
                    }
                    contentInfo.put("title", title != null ? title : "图文动态");
                    contentInfo.put("subtitle", "图文动态");
                    
                    String imageUrls = imagePost.getImageUrls();
                    if (imageUrls != null && !imageUrls.isEmpty()) {
                        String[] urls = imageUrls.split(",");
                        contentInfo.put("thumbnail", urls[0].trim());
                    } else {
                        contentInfo.put("thumbnail", "");
                    }
                }

            } else if (contentType == 3) {
                // 小说
                NovelpostBase_wlq novelPost = NovelpostBase_list_wlq.getNovelpostBaseById(contentId);
                if (novelPost == null) {
                    novelPost = novelpostBaseMapper.selectById(contentId);
                }
                if (novelPost != null) {
                    contentInfo.put("title", novelPost.getNovelTitle() != null ? novelPost.getNovelTitle() : "小说");
                    String desc = novelPost.getNovelDescription();
                    if (desc != null && desc.length() > 50) {
                        desc = desc.substring(0, 50) + "...";
                    }
                    contentInfo.put("subtitle", desc != null ? desc : "小说");
                    contentInfo.put("thumbnail", novelPost.getNovelCoverUrl() != null ? novelPost.getNovelCoverUrl() : "");
                }
            }
        } catch (Exception e) {
            System.err.println("UserHistoryService: 获取内容信息失败，contentId=" + contentId + ", contentType=" + contentType + ", 错误: " + e.getMessage());
        }

        return contentInfo.isEmpty() ? null : contentInfo;
    }

    /**
     * 获取内容的用户ID（用于判断是否为自己的内容）
     */
    private Long getContentUserId(Long contentId, int contentType) {
        try {
            if (contentType == 1) {
                TextpostBase_wlq textPost = TextpostBase_list_wlq.getTextpostBaseById(contentId);
                if (textPost == null) {
                    textPost = textpostBaseMapper.selectById(contentId);
                }
                return textPost != null ? textPost.getUserId() : null;

            } else if (contentType == 2) {
                ImagePostBase_wlq imagePost = ImagePostBase_list_wlq.getImagePostBaseById(contentId);
                if (imagePost == null) {
                    imagePost = imagePostBaseMapper.selectById(contentId);
                }
                return imagePost != null ? imagePost.getUserId() : null;

            } else if (contentType == 3) {
                NovelpostBase_wlq novelPost = NovelpostBase_list_wlq.getNovelpostBaseById(contentId);
                if (novelPost == null) {
                    novelPost = novelpostBaseMapper.selectById(contentId);
                }
                return novelPost != null ? novelPost.getUserId() : null;
            }
        } catch (Exception e) {
            System.err.println("UserHistoryService: 获取内容用户ID失败，contentId=" + contentId + ", contentType=" + contentType + ", 错误: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * 删除单条浏览历史
     * 
     * @param userId 用户ID
     * @param historyId 历史记录ID
     * @return 操作结果
     */
    public Map<String, Object> deleteHistory(Long userId, Long historyId) {
        System.out.println("UserHistoryService: 开始删除浏览历史，userId=" + userId + ", historyId=" + historyId);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 查询历史记录是否存在
            UserContentViewLog_njj log = userContentViewLogMapper.selectById(historyId);
            if (log == null) {
                System.out.println("UserHistoryService: 历史记录不存在，historyId=" + historyId);
                
                // 检查该用户是否有任何浏览历史记录
                QueryWrapper<UserContentViewLog_njj> userQuery = new QueryWrapper<>();
                userQuery.eq("user_id", userId);
                List<UserContentViewLog_njj> userHistory = userContentViewLogMapper.selectList(userQuery);
                
                result.put("success", false);
                result.put("message", "历史记录不存在");
                result.put("historyId", historyId);
                result.put("userId", userId);
                result.put("userHistoryCount", userHistory.size());
                if (!userHistory.isEmpty()) {
                    // 返回用户的实际历史记录ID列表，方便调试
                    List<Long> actualHistoryIds = new ArrayList<>();
                    for (UserContentViewLog_njj h : userHistory) {
                        actualHistoryIds.add(h.getId());
                    }
                    result.put("actualHistoryIds", actualHistoryIds);
                    result.put("hint", "该用户存在浏览历史记录，但ID不匹配。请使用 GET /api/user/history?userId=" + userId + " 查询实际的记录ID");
                } else {
                    result.put("hint", "该用户没有任何浏览历史记录");
                }
                return result;
            }

            // 2. 验证历史记录是否属于该用户
            if (!log.getUserId().equals(userId)) {
                System.out.println("UserHistoryService: 历史记录不属于该用户，historyId=" + historyId + 
                                 ", 记录所属userId=" + log.getUserId() + ", 请求userId=" + userId);
                result.put("success", false);
                result.put("message", "历史记录不属于该用户");
                result.put("historyId", historyId);
                result.put("userId", userId);
                result.put("recordUserId", log.getUserId());
                return result;
            }

            // 3. 删除历史记录
            int deleteResult = userContentViewLogMapper.deleteById(historyId);
            
            if (deleteResult > 0) {
                result.put("success", true);
                result.put("message", "删除浏览历史成功");
                result.put("historyId", historyId);
                System.out.println("UserHistoryService: 删除浏览历史成功，historyId=" + historyId);
            } else {
                result.put("success", false);
                result.put("message", "删除浏览历史失败");
                result.put("historyId", historyId);
            }
            
        } catch (Exception e) {
            System.err.println("UserHistoryService: 删除浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "删除浏览历史失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 清空用户的所有浏览历史
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> clearHistory(Long userId) {
        System.out.println("UserHistoryService: 开始清空用户 " + userId + " 的所有浏览历史");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 查询该用户的所有浏览历史记录
            QueryWrapper<UserContentViewLog_njj> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            
            List<UserContentViewLog_njj> logs = userContentViewLogMapper.selectList(queryWrapper);
            int totalCount = logs.size();
            
            System.out.println("UserHistoryService: 找到 " + totalCount + " 条浏览历史记录需要删除");
            
            // 2. 批量删除
            int deletedCount = 0;
            if (!logs.isEmpty()) {
                // 获取所有ID
                List<Long> ids = new ArrayList<>();
                for (UserContentViewLog_njj log : logs) {
                    ids.add(log.getId());
                }
                
                // 批量删除
                deletedCount = userContentViewLogMapper.deleteBatchIds(ids);
            }
            
            result.put("success", true);
            result.put("message", "清空浏览历史成功");
            result.put("deletedCount", deletedCount);
            result.put("totalCount", totalCount);
            System.out.println("UserHistoryService: 清空浏览历史成功，共删除 " + deletedCount + " 条记录");
            
        } catch (Exception e) {
            System.err.println("UserHistoryService: 清空浏览历史失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "清空浏览历史失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建浏览历史记录（用于测试）
     * 
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param contentType 内容类型（1=文字, 2=图片, 3=小说）
     * @param viewType 查看类型（可选，默认为1）
     * @param durationSeconds 浏览时长（秒，可选，默认为60）
     * @return 操作结果
     */
    public Map<String, Object> createHistory(Long userId, Long contentId, Byte contentType, 
                                              Byte viewType, Integer durationSeconds) {
        System.out.println("UserHistoryService: 开始创建浏览历史记录，userId=" + userId + 
                          ", contentId=" + contentId + ", contentType=" + contentType);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 验证用户是否存在
            UserBase_wlq user = userBaseMapper.selectById(userId);
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                result.put("userId", userId);
                return result;
            }

            // 2. 验证内容是否存在
            boolean contentExists = false;
            if (contentType == 1) {
                contentExists = (textpostBaseMapper.selectById(contentId) != null);
            } else if (contentType == 2) {
                contentExists = (imagePostBaseMapper.selectById(contentId) != null);
            } else if (contentType == 3) {
                contentExists = (novelpostBaseMapper.selectById(contentId) != null);
            } else {
                result.put("success", false);
                result.put("message", "无效的内容类型，有效值为：1（文字动态）、2（图片动态）、3（小说）");
                result.put("contentType", contentType);
                return result;
            }

            if (!contentExists) {
                result.put("success", false);
                result.put("message", "内容不存在");
                result.put("contentId", contentId);
                result.put("contentType", contentType);
                return result;
            }

            // 3. 生成新的历史记录ID
            Long historyId = generateNextHistoryId();

            // 4. 创建浏览历史记录
            UserContentViewLog_njj log = new UserContentViewLog_njj();
            log.setId(historyId);
            log.setUserId(userId);
            log.setContentId(contentId);
            log.setContentType(contentType);
            log.setViewType(viewType != null ? viewType : (byte) 1); // 默认为1
            log.setDurationSeconds(durationSeconds != null ? durationSeconds : 60); // 默认60秒
            log.setIsInterested((byte) 0); // 默认未感兴趣
            log.setSource("test"); // 测试来源
            Timestamp now = new Timestamp(System.currentTimeMillis());
            log.setCreateAt(now);
            log.setUpdateAt(now);

            // 5. 保存到数据库
            int insertResult = userContentViewLogMapper.insert(log);
            
            if (insertResult > 0) {
                result.put("success", true);
                result.put("message", "创建浏览历史记录成功");
                result.put("historyId", historyId);
                result.put("data", log);
                System.out.println("UserHistoryService: 创建浏览历史记录成功，historyId=" + historyId);
            } else {
                result.put("success", false);
                result.put("message", "创建浏览历史记录失败");
            }
            
        } catch (Exception e) {
            System.err.println("UserHistoryService: 创建浏览历史记录失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "创建浏览历史记录失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 生成下一个浏览历史记录ID
     */
    private Long generateNextHistoryId() {
        try {
            // 查询当前最大ID
            QueryWrapper<UserContentViewLog_njj> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("MAX(id) as maxId");
            List<UserContentViewLog_njj> logs = userContentViewLogMapper.selectList(queryWrapper);
            
            Long maxId = 10000000L; // 默认起始值
            if (!logs.isEmpty()) {
                // 获取所有记录的ID，找出最大值
                List<UserContentViewLog_njj> allLogs = userContentViewLogMapper.selectList(null);
                for (UserContentViewLog_njj log : allLogs) {
                    if (log.getId() != null && log.getId() > maxId) {
                        maxId = log.getId();
                    }
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("UserHistoryService: 生成新的浏览历史记录ID: " + newId);
            return newId;
            
        } catch (Exception e) {
            System.err.println("UserHistoryService: 生成浏览历史记录ID失败: " + e.getMessage());
            // 使用时间戳作为备用ID
            return System.currentTimeMillis();
        }
    }
}
