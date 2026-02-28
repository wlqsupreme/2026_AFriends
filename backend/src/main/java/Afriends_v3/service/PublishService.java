package Afriends_v3.service;

import Afriends_v3.entity.*;
import Afriends_v3.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布内容服务类
 * 负责处理用户发布各种类型内容的逻辑
 */
@Service
public class PublishService {

    @Autowired
    private ImagePostBaseMapper imagePostBaseMapper;
    
    @Autowired
    private TextpostBaseMapper textpostBaseMapper;
    
    @Autowired
    private ILikeMapper iLikeMapper;
    
    @Autowired
    private IDislikeMapper iDislikeMapper;
    
    @Autowired
    private IHaveMapper iHaveMapper;
    
    @Autowired
    private INeedMapper iNeedMapper;
    
    /**
     * 发布内容
     * @param userId 用户ID
     * @param contentText 内容文本
     * @param columnType 专栏类型：mood(心情), ihave(我有), iwant(我要), ilike(我喜欢), ihate(我讨厌)
     * @param imageUrls 图片URLs（可选）
     * @param title 标题（可选）
     * @param description 描述（可选）
     * @return 发布结果
     */
    public Map<String, Object> publishContent(Long userId, String contentText, String columnType, 
            String imageUrls, String title, String description) {
        
        System.out.println("PublishService: 开始发布内容 - 用户ID: " + userId + 
            ", 专栏类型: " + columnType + ", 内容: " + contentText);
        
        try {
            Map<String, Object> result = new HashMap<>();
            
            switch (columnType) {
                case "mood":
                    // 心情：根据是否有图片选择v2_imagepost_base或v2_textpost_base
                    if (imageUrls != null && !imageUrls.trim().isEmpty()) {
                        result = publishImagePost(userId, contentText, imageUrls);
                    } else {
                        result = publishTextPost(userId, contentText);
                    }
                    break;
                case "ihave":
                    // 我有：v2_i_have表
                    result = publishIHave(userId, title != null ? title : contentText, description != null ? description : contentText, imageUrls);
                    break;
                case "iwant":
                    // 我要：v2_i_need表
                    result = publishINeed(userId, title != null ? title : contentText, contentText, imageUrls);
                    break;
                case "ilike":
                    // 我喜欢：v2_i_like表
                    result = publishILike(userId, contentText, imageUrls);
                    break;
                case "ihate":
                    // 我讨厌：v2_i_dislike表
                    result = publishIDislike(userId, contentText, imageUrls);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的专栏类型: " + columnType);
            }
            
            System.out.println("PublishService: 发布内容成功 - " + columnType);
            return result;
            
        } catch (Exception e) {
            System.err.println("PublishService: 发布内容失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 发布图片帖子到v2_imagepost_base表
     */
    private Map<String, Object> publishImagePost(Long userId, String contentText, String imageUrls) {
        System.out.println("PublishService: 发布图片帖子到v2_imagepost_base");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(imagePostBaseMapper, "post_id");
        
        ImagePostBase_wlq imagePost = new ImagePostBase_wlq();
        imagePost.setPostId(newId);
        imagePost.setUserId(userId);
        imagePost.setContentText(contentText);
        imagePost.setImageUrls(imageUrls);
        imagePost.setSoftTags("心情,图片");
        imagePost.setHardTags("[]");
        imagePost.setLikeCount(0);
        imagePost.setCommentCount(0);
        imagePost.setCollectCount(0);
        imagePost.setDislikeCount(0);
        imagePost.setHeatScore(0.0);
        imagePost.setViewCount(0);
        imagePost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        imagePost.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        imagePost.setIsView((byte) 1);
        imagePost.setDeleteAt(null);
        
        imagePostBaseMapper.insert(imagePost);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "图片帖子发布成功");
        result.put("postId", newId);
        result.put("postType", "image");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 发布文字帖子到v2_textpost_base表
     */
    private Map<String, Object> publishTextPost(Long userId, String contentText) {
        System.out.println("PublishService: 发布文字帖子到v2_textpost_base");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(textpostBaseMapper, "post_id");
        
        TextpostBase_wlq textPost = new TextpostBase_wlq();
        textPost.setPostId(newId);
        textPost.setUserId(userId);
        textPost.setContentText(contentText);
        textPost.setSoftTags("心情,文字");
        textPost.setHardTags("[]");
        textPost.setLikeCount(0);
        textPost.setCommentCount(0);
        textPost.setCollectCount(0);
        textPost.setDislikeCount(0);
        textPost.setHeatScore(0.0);
        textPost.setViewCount(0);
        textPost.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        textPost.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        textPost.setIsView((byte) 1);
        textPost.setDeletedAt(null);
        
        textpostBaseMapper.insert(textPost);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "文字帖子发布成功");
        result.put("postId", newId);
        result.put("postType", "text");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 发布我喜欢内容到v2_i_like表
     */
    private Map<String, Object> publishILike(Long userId, String contentText, String imageUrls) {
        System.out.println("PublishService: 发布我喜欢内容到v2_i_like");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(iLikeMapper, "like_id");
        
        ILike_wlq iLike = new ILike_wlq();
        iLike.setLikeId(newId);
        iLike.setUserId(userId);
        iLike.setLikeText(contentText);
        iLike.setLikeType("我喜欢");
        iLike.setImageUrls(imageUrls);
        iLike.setSoftTags("我喜欢");
        iLike.setHardTags("[]");
        iLike.setItemType("抽象");
        iLike.setItemStatus("可用");
        iLike.setLikeCount(0);
        iLike.setCommentCount(0);
        iLike.setDislikeCount(0);
        iLike.setHeatScore(0.0);
        iLike.setViewCount(0);
        iLike.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        iLike.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        iLike.setIsView(1);
        iLike.setDeletedAt(null);
        
        iLikeMapper.insert(iLike);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "我喜欢内容发布成功");
        result.put("postId", newId);
        result.put("postType", "ilike");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 发布我讨厌内容到v2_i_dislike表
     */
    private Map<String, Object> publishIDislike(Long userId, String contentText, String imageUrls) {
        System.out.println("PublishService: 发布我讨厌内容到v2_i_dislike");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(iDislikeMapper, "dislike_id");
        
        IDislike_wlq iDislike = new IDislike_wlq();
        iDislike.setDislikeId(newId);
        iDislike.setUserId(userId);
        iDislike.setDislikeText(contentText);
        iDislike.setDislikeType("我讨厌");
        iDislike.setImageUrls(imageUrls);
        iDislike.setSoftTags("我讨厌");
        iDislike.setHardTags("[]");
        iDislike.setItemType("抽象");
        iDislike.setItemStatus("不可用");
        iDislike.setLikeCount(0);
        iDislike.setCommentCount(0);
        iDislike.setDislikeCount(0);
        iDislike.setHeatScore(0.0);
        iDislike.setViewCount(0);
        iDislike.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        iDislike.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        iDislike.setIsView(1);
        iDislike.setDeletedAt(null);
        
        iDislikeMapper.insert(iDislike);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "我讨厌内容发布成功");
        result.put("postId", newId);
        result.put("postType", "idislike");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 发布我拥有内容到v2_i_have表
     */
    private Map<String, Object> publishIHave(Long userId, String title, String description, String imageUrls) {
        System.out.println("PublishService: 发布我拥有内容到v2_i_have");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(iHaveMapper, "have_id");
        
        IHave_wlq iHave = new IHave_wlq();
        iHave.setHaveId(newId);
        iHave.setUserId(userId);
        iHave.setTitle(title);
        iHave.setDescription(description);
        iHave.setImageUrls(imageUrls);
        iHave.setSoftTags("我拥有");
        iHave.setHardTags("[]");
        iHave.setItemType("具象");
        iHave.setItemStatus("可用");
        iHave.setPriceInfo("{}");
        iHave.setExchangeWillingness("可交换");
        iHave.setLikeCount(0);
        iHave.setCommentCount(0);
        iHave.setCollectCount(0);
        iHave.setDislikeCount(0);
        iHave.setHeatScore(0.0);
        iHave.setViewCount(0);
        iHave.setIsView((byte) 1);
        iHave.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        iHave.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        iHave.setDeletedAt(null);
        iHave.setEmbedding("[]");
        
        iHaveMapper.insert(iHave);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "我拥有内容发布成功");
        result.put("postId", newId);
        result.put("postType", "ihave");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 发布我需要内容到v2_i_need表
     */
    private Map<String, Object> publishINeed(Long userId, String title, String contentText, String imageUrls) {
        System.out.println("PublishService: 发布我需要内容到v2_i_need");
        
        // 生成新的ID（从20000000开始）
        Long newId = generateNewId(iNeedMapper, "need_id");
        
        INeed_wlq iNeed = new INeed_wlq();
        iNeed.setNeedId(newId);
        iNeed.setUserId(userId);
        iNeed.setTitle(title);
        iNeed.setContentText(contentText);
        iNeed.setContentType("ineed");
        iNeed.setImageUrls(imageUrls);
        iNeed.setSoftTags("我需要");
        iNeed.setHardTags("[]");
        iNeed.setItemType("具象");
        iNeed.setItemStatus("需要");
        iNeed.setPriceInfo("{}");
        iNeed.setExchangeWillingness("可交换");
        iNeed.setLikeCount(0);
        iNeed.setCommentCount(0);
        iNeed.setCollectCount(0);
        iNeed.setDislikeCount(0);
        iNeed.setHeatScore(0.0);
        iNeed.setViewCount(0);
        iNeed.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        iNeed.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        iNeed.setIsView((byte) 1);
        iNeed.setDeletedAt(null);
        
        iNeedMapper.insert(iNeed);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "我需要内容发布成功");
        result.put("postId", newId);
        result.put("postType", "ineed");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 生成从20000000开始的自增ID
     * @param mapper 对应的Mapper接口
     * @param idField 主键字段名
     * @return 新的ID
     */
    private Long generateNewId(Object mapper, String idField) {
        try {
            Long maxId = 19999999L; // 起始值减1
            
            if (mapper instanceof ImagePostBaseMapper) {
                List<ImagePostBase_wlq> allPosts = imagePostBaseMapper.selectList(null);
                for (ImagePostBase_wlq post : allPosts) {
                    if (post.getPostId() != null && post.getPostId() >= 20000000L && post.getPostId() > maxId) {
                        maxId = post.getPostId();
                    }
                }
            } else if (mapper instanceof TextpostBaseMapper) {
                List<TextpostBase_wlq> allPosts = textpostBaseMapper.selectList(null);
                for (TextpostBase_wlq post : allPosts) {
                    if (post.getPostId() != null && post.getPostId() >= 20000000L && post.getPostId() > maxId) {
                        maxId = post.getPostId();
                    }
                }
            } else if (mapper instanceof ILikeMapper) {
                List<ILike_wlq> allLikes = iLikeMapper.selectList(null);
                for (ILike_wlq like : allLikes) {
                    if (like.getLikeId() != null && like.getLikeId() >= 20000000L && like.getLikeId() > maxId) {
                        maxId = like.getLikeId();
                    }
                }
            } else if (mapper instanceof IDislikeMapper) {
                List<IDislike_wlq> allDislikes = iDislikeMapper.selectList(null);
                for (IDislike_wlq dislike : allDislikes) {
                    if (dislike.getDislikeId() != null && dislike.getDislikeId() >= 20000000L && dislike.getDislikeId() > maxId) {
                        maxId = dislike.getDislikeId();
                    }
                }
            } else if (mapper instanceof IHaveMapper) {
                List<IHave_wlq> allHaves = iHaveMapper.selectList(null);
                for (IHave_wlq have : allHaves) {
                    if (have.getHaveId() != null && have.getHaveId() >= 20000000L && have.getHaveId() > maxId) {
                        maxId = have.getHaveId();
                    }
                }
            } else if (mapper instanceof INeedMapper) {
                List<INeed_wlq> allNeeds = iNeedMapper.selectList(null);
                for (INeed_wlq need : allNeeds) {
                    if (need.getNeedId() != null && need.getNeedId() >= 20000000L && need.getNeedId() > maxId) {
                        maxId = need.getNeedId();
                    }
                }
            }
            
            Long newId = maxId + 1;
            System.out.println("PublishService: 生成新ID: " + newId + " (从20000000开始)");
            return newId;
            
        } catch (Exception e) {
            System.err.println("PublishService: 生成ID失败，使用默认值: " + e.getMessage());
            return 20000000L; // 如果出错，返回默认起始值
        }
    }
}




