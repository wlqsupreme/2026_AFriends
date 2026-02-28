package Afriends_v3.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SoftTagBaseMapper_njj {

    /**
     * 根据软标签ID查询软标签信息
     */
    @Select("SELECT soft_tag_id, soft_tag_content FROM v2_soft_tag_base WHERE soft_tag_id = #{softTagId}")
    SoftTagBase getSoftTagById(Long softTagId);

    /**
     * 根据软标签ID列表查询软标签信息
     */
    @Select("<script>" +
            "SELECT soft_tag_id, soft_tag_content FROM v2_soft_tag_base WHERE soft_tag_id IN " +
            "<foreach collection='softTagIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<SoftTagBase> getSoftTagsByIds(@Param("softTagIds") List<Long> softTagIds);

    /**
     * 根据软标签内容查询软标签信息
     */
    @Select("SELECT soft_tag_id, soft_tag_content FROM v2_soft_tag_base WHERE soft_tag_content = #{softTagContent}")
    SoftTagBase getSoftTagByContent(String softTagContent);

    /**
     * 根据软标签内容模糊查询
     */
    @Select("SELECT soft_tag_id, soft_tag_content FROM v2_soft_tag_base WHERE soft_tag_content LIKE CONCAT('%', #{keyword}, '%')")
    List<SoftTagBase> searchSoftTagsByKeyword(String keyword);

    /**
     * 查询所有软标签
     */
    @Select("SELECT soft_tag_id, soft_tag_content FROM v2_soft_tag_base ORDER BY soft_tag_id")
    List<SoftTagBase> getAllSoftTags();

    /**
     * 创建软标签
     */
    @Insert("INSERT INTO v2_soft_tag_base (soft_tag_content, soft_tag_vector, category_id, view_count, like_count, collect_count, comment_count, relevance_value, created_time) "
            +
            "VALUES (#{softTagContent}, #{softTagVector}, #{categoryId}, #{viewCount}, #{likeCount}, #{collectCount}, #{commentCount}, #{relevanceValue}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "softTagId")
    int insertSoftTag(SoftTagBase softTag);

    /**
     * 更新软标签
     */
    @Update("UPDATE v2_soft_tag_base SET soft_tag_content = #{softTagContent}, soft_tag_vector = #{softTagVector}, " +
            "category_id = #{categoryId}, view_count = #{viewCount}, like_count = #{likeCount}, " +
            "collect_count = #{collectCount}, comment_count = #{commentCount}, relevance_value = #{relevanceValue} " +
            "WHERE soft_tag_id = #{softTagId}")
    int updateSoftTag(SoftTagBase softTag);

    /**
     * 删除软标签
     */
    @Delete("DELETE FROM v2_soft_tag_base WHERE soft_tag_id = #{softTagId}")
    int deleteSoftTag(Long softTagId);

    /**
     * 统计软标签数量
     */
    @Select("SELECT COUNT(*) FROM v2_soft_tag_base")
    int countSoftTags();

    /**
     * 软标签基础信息内部类
     */
    class SoftTagBase {
        private Long softTagId;
        private String softTagContent;
        private String softTagVector;
        private Long categoryId;
        private Integer viewCount;
        private Integer likeCount;
        private Integer collectCount;
        private Integer commentCount;
        private Double relevanceValue;
        private String createdTime;

        // Getters and Setters
        public Long getSoftTagId() {
            return softTagId;
        }

        public void setSoftTagId(Long softTagId) {
            this.softTagId = softTagId;
        }

        public String getSoftTagContent() {
            return softTagContent;
        }

        public void setSoftTagContent(String softTagContent) {
            this.softTagContent = softTagContent;
        }

        public String getSoftTagVector() {
            return softTagVector;
        }

        public void setSoftTagVector(String softTagVector) {
            this.softTagVector = softTagVector;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Integer getViewCount() {
            return viewCount;
        }

        public void setViewCount(Integer viewCount) {
            this.viewCount = viewCount;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(Integer collectCount) {
            this.collectCount = collectCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Double getRelevanceValue() {
            return relevanceValue;
        }

        public void setRelevanceValue(Double relevanceValue) {
            this.relevanceValue = relevanceValue;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
    }
}
