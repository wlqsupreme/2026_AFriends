package Afriends_v3.entity;

import Afriends_v3.mapper.ImageRecMapper_njj;

/**
 * 图片推荐算法实体类
 * 对应表结构：v2_user_image_recommendation
 * 用于图片内容推荐算法
 */
public class ImageRec_njj {
    private Long id; // 主键ID
    private Long userId; // 接收推荐的用户ID
    private Long imageContentId; // 推荐的图片内容ID
    private Float score; // 推荐得分
    private Integer rankOrder; // 推荐排序
    private java.sql.Timestamp createdAt; // 推荐时间

    // 详细得分组件（用于前端显示，不存储到数据库）
    private Float softTagScore; // 软标签得分
    private Float hardTagScore; // 硬标签得分
    private Float popularityScore; // 热度得分
    private Float timeDecayScore; // 时间衰减得分

    public ImageRec_njj() {
    }

    public ImageRec_njj(Long id, Long userId, Long imageContentId, Float score, Integer rankOrder,
            java.sql.Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.imageContentId = imageContentId;
        this.score = score;
        this.rankOrder = rankOrder;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getImageContentId() {
        return imageContentId;
    }

    public void setImageContentId(Long imageContentId) {
        this.imageContentId = imageContentId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Float getSoftTagScore() {
        return softTagScore;
    }

    public void setSoftTagScore(Float softTagScore) {
        this.softTagScore = softTagScore;
    }

    public Float getHardTagScore() {
        return hardTagScore;
    }

    public void setHardTagScore(Float hardTagScore) {
        this.hardTagScore = hardTagScore;
    }

    public Float getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Float popularityScore) {
        this.popularityScore = popularityScore;
    }

    public Float getTimeDecayScore() {
        return timeDecayScore;
    }

    public void setTimeDecayScore(Float timeDecayScore) {
        this.timeDecayScore = timeDecayScore;
    }

    @Override
    public String toString() {
        return "ImageRec{" +
                "id=" + id +
                ", userId=" + userId +
                ", imageContentId=" + imageContentId +
                ", score=" + score +
                ", rankOrder=" + rankOrder +
                ", createdAt=" + createdAt +
                '}';
    }

    // 创建测试数据
    public static ImageRec_njj createTestData() {
        ImageRec_njj recommendation = new ImageRec_njj();
        recommendation.setUserId(1L);
        recommendation.setImageContentId(1L);
        recommendation.setScore(0.85f);
        recommendation.setRankOrder(1);
        recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return recommendation;
    }

    // 批量插入测试数据
    public static void insertTestData(int n, ImageRecMapper_njj mapper) {
        for (int i = 1; i <= n; i++) {
            ImageRec_njj recommendation = new ImageRec_njj();
            recommendation.setUserId((long) i);
            recommendation.setImageContentId((long) (i + 1));
            recommendation.setScore(0.7f + (i * 0.1f)); // 0.8, 0.9, 1.0...
            recommendation.setRankOrder(i);
            recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            mapper.insertImageRec(recommendation);
        }
    }

    // 读取推荐记录
    public static ImageRec_njj readById(Long id, ImageRecMapper_njj mapper) {
        return mapper.selectImageRecById(id);
    }
}
