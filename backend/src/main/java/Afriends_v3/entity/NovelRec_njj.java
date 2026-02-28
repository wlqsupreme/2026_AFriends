package Afriends_v3.entity;

import Afriends_v3.mapper.NovelRecMapper_njj;

/**
 * 小说推荐算法实体类
 * 对应表结构：v2_user_novel_recommendation
 * 用于小说内容推荐算法
 */
public class NovelRec_njj {
    private Long id; // 主键ID
    private Long userId; // 接收推荐的用户ID
    private Long novelId; // 推荐的小说ID
    private Float score; // 推荐得分
    private Integer rankOrder; // 推荐排序
    private java.sql.Timestamp createdAt; // 推荐时间

    // 详细得分组件（用于前端显示，不存储到数据库）
    private Float softTagScore; // 软标签得分
    private Float hardTagScore; // 硬标签得分
    private Float popularityScore; // 热度得分
    private Float timeDecayScore; // 时间衰减得分

    public NovelRec_njj() {
    }

    public NovelRec_njj(Long id, Long userId, Long novelId, Float score, Integer rankOrder,
            java.sql.Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.novelId = novelId;
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

    public Long getNovelId() {
        return novelId;
    }

    public void setNovelId(Long novelId) {
        this.novelId = novelId;
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
        return "NovelRec{" +
                "id=" + id +
                ", userId=" + userId +
                ", novelId=" + novelId +
                ", score=" + score +
                ", rankOrder=" + rankOrder +
                ", createdAt=" + createdAt +
                '}';
    }

    // 创建测试数据
    public static NovelRec_njj createTestData() {
        NovelRec_njj recommendation = new NovelRec_njj();
        recommendation.setUserId(1L);
        recommendation.setNovelId(1L);
        recommendation.setScore(0.88f);
        recommendation.setRankOrder(1);
        recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return recommendation;
    }

    // 批量插入测试数据
    public static void insertTestData(int n, NovelRecMapper_njj mapper) {
        for (int i = 1; i <= n; i++) {
            NovelRec_njj recommendation = new NovelRec_njj();
            recommendation.setUserId((long) i);
            recommendation.setNovelId((long) i);
            recommendation.setScore(0.8f + (i * 0.06f));
            recommendation.setRankOrder(i);
            recommendation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            mapper.insertNovelRec(recommendation);
        }
    }

    // 读取推荐记录
    public static NovelRec_njj readById(Long id, NovelRecMapper_njj mapper) {
        return mapper.selectNovelRecById(id);
    }
}
