package Afriends_v3.entity;

import Afriends_v3.mapper.TextRecMapper_njj;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 文字推荐算法实体类
 * 对应表结构：v2_user_text_recommendation
 * 用于文字内容推荐算法
 */
@TableName("v2_user_text_recommendation")
public class TextRec_njj {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 主键ID
    private Long userId; // 接收推荐的用户ID
    private Long textContentId; // 推荐的文字内容ID
    private Float score; // 推荐得分
    private Integer rankOrder; // 推荐排序
    private java.sql.Timestamp createdAt; // 推荐时间

    // 详细得分组件（用于前端显示，不存储到数据库）
    private Float softTagScore; // 软标签得分
    private Float hardTagScore; // 硬标签得分
    private Float popularityScore; // 热度得分
    private Float timeDecayScore; // 时间衰减得分

    public TextRec_njj() {
    }

    public TextRec_njj(Long id, Long userId, Long textContentId, Float score, Integer rankOrder,
            java.sql.Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.textContentId = textContentId;
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

    public Long getTextContentId() {
        return textContentId;
    }

    public void setTextContentId(Long textContentId) {
        this.textContentId = textContentId;
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
        return "TextRec{" +
                "id=" + id +
                ", userId=" + userId +
                ", textContentId=" + textContentId +
                ", score=" + score +
                ", rankOrder=" + rankOrder +
                ", createdAt=" + createdAt +
                '}';
    }

    // 创建测试数据
    public static TextRec_njj createTestData() {
        TextRec_njj recommendation = new TextRec_njj();
        // ID将由服务层生成，这里不设置
        recommendation.setUserId(1L);
        recommendation.setTextContentId(1L);
        recommendation.setScore(0.85f);
        recommendation.setRankOrder(1);
        recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return recommendation;
    }

    // 批量插入测试数据
    public static void insertTestData(int n, TextRecMapper_njj mapper) {
        // 获取当前最大ID
        Long maxId = mapper.getMaxId();
        if (maxId == null) {
            maxId = 0L;
        }

        for (int i = 1; i <= n; i++) {
            TextRec_njj recommendation = new TextRec_njj();
            maxId++;
            recommendation.setId(maxId);
            recommendation.setUserId((long) i);
            recommendation.setTextContentId((long) i);
            recommendation.setScore(0.8f + (i * 0.05f));
            recommendation.setRankOrder(i);
            recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            mapper.insertTextRec(recommendation);
        }
    }

    // 读取推荐记录
    public static TextRec_njj readById(Long id, TextRecMapper_njj mapper) {
        return mapper.selectTextRecById(id);
    }
}
