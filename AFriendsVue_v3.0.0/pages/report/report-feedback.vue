<!--
 * @description 举报反馈页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示举报提交成功的反馈信息
 * - 适用于举报功能完成后的反馈
 * - 提供返回功能
 * 
 * 页面设计：
 * - 粉色渐变主题，与项目整体风格一致
 * - 现代化的卡片设计
 * - 友好的用户反馈体验
 * - 渐变背景和阴影效果
-->

<template>
	<view class="report-feedback-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">{{$t('report.feedback.title')}}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 成功图标 -->
			<view class="success-icon-container">
				<view class="success-icon">
					<view class="checkmark"></view>
				</view>
			</view>
			
			<!-- 成功消息 -->
			<view class="success-message">
				<text class="message-text">{{$t('report.feedback.successMessage')}}</text>
			</view>
			
			<!-- 举报信息卡片 -->
			<view class="report-card">
				<view class="card-header">
					<view class="card-icon">
						<view class="report-icon"></view>
					</view>
					<text class="card-title">{{$t('report.feedback.reportInfo')}}</text>
				</view>
				<view class="card-content">
					<view class="info-row">
						<text class="info-label">{{$t('report.feedback.type')}}</text>
						<text class="info-value">{{ reportType }}</text>
					</view>
					<view class="info-row">
						<text class="info-label">{{$t('report.feedback.submitTime')}}</text>
						<text class="info-value">{{ submitTime }}</text>
					</view>
					<view class="info-row">
						<text class="info-label">{{$t('report.feedback.status')}}</text>
						<text class="info-value status-pending">{{$t('report.feedback.pending')}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">{{$t('report.feedback.expectedTime')}}</text>
						<text class="info-value">{{ expectedTime }}</text>
					</view>
				</view>
			</view>
			
			<!-- 温馨提示 -->
			<view class="tips-section">
				<view class="tips-header">
					<view class="tips-icon">
						<view class="lightbulb"></view>
					</view>
					<text class="tips-title">{{$t('report.feedback.tips')}}</text>
				</view>
				<view class="tips-content">
					<text class="tip-item" v-for="(tip, index) in tipItems" :key="index">• {{tip}}</text>
				</view>
			</view>
		</view>
		
		<!-- 返回按钮 -->
		<view class="return-button-section">
			<view class="return-button" @click="goBack">
				<text class="return-text">{{$t('report.feedback.return')}}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'ReportFeedbackPage',
		data() {
			return {
				reportType: this.$t('report.reason.porn'), // 从上一页传递过来的举报类型
				reportId: null, // 举报记录ID
				submitTime: this.formatDateTime(new Date()),
				expectedTime: this.formatExpectedTime(new Date()),
				tipItems: [
					this.$t('report.feedback.tipItems[0]'),
					this.$t('report.feedback.tipItems[1]'),
					this.$t('report.feedback.tipItems[2]')
				]
			}
		},
		methods: {
			goBack() {
				// 返回到首页
				uni.navigateBack({
					delta: 2
				});
			},
			formatDateTime(date) {
				const year = date.getFullYear();
				const month = (date.getMonth() + 1).toString().padStart(2, '0');
				const day = date.getDate().toString().padStart(2, '0');
				const hours = date.getHours().toString().padStart(2, '0');
				const minutes = date.getMinutes().toString().padStart(2, '0');
				return `${year}-${month}-${day} ${hours}:${minutes}`;
			},
			formatExpectedTime(date) {
				// 3个工作日后的日期
				const expectedDate = new Date(date);
				let workDays = 0;
				while (workDays < 3) {
					expectedDate.setDate(expectedDate.getDate() + 1);
					// 跳过周末
					if (expectedDate.getDay() !== 0 && expectedDate.getDay() !== 6) {
						workDays++;
					}
				}
				const year = expectedDate.getFullYear();
				const month = (expectedDate.getMonth() + 1).toString().padStart(2, '0');
				const day = expectedDate.getDate().toString().padStart(2, '0');
				return `${year}-${month}-${day}`;
			}
		},
		onLoad(options) {
			const themeStore = useThemeStore();
			themeStore.init();
			// 根据传入参数设置举报类型
			if (options.type) {
				this.reportType = decodeURIComponent(options.type);
			}
			// 接收举报记录ID
			if (options.reportId) {
				this.reportId = parseInt(options.reportId);
			}
		}
	}
</script>

<style>
	.report-feedback-page {
		min-height: 100vh;
		background: linear-gradient(135deg, #FFE4F1 0%, #FFFFFF 100%);
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background: transparent;
	}
	
	.time {
		font-size: 28rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.signal, .wifi, .battery {
		width: 24rpx;
		height: 24rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background: rgba(255, 255, 255, 0.95);
		backdrop-filter: blur(20rpx);
		border-bottom: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.8));
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 24rpx;
		background: var(--color-bg-weak, rgba(0, 0, 0, 0.05));
		transition: all 0.2s ease;
	}
	
	.back-button:active {
		background: var(--color-bg-weak-active, rgba(0, 0, 0, 0.1));
		transform: scale(0.95);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 48rpx 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
    background-color: var(--color-bg);
	}
	
	/* 成功图标 */
	.success-icon-container {
		margin-bottom: 48rpx;
	}
	
	.success-icon {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx rgba(255, 105, 180, 0.3);
		position: relative;
	}
	
	.success-icon::before {
		content: '';
		position: absolute;
		width: 140rpx;
		height: 140rpx;
		border: 2rpx solid rgba(255, 105, 180, 0.2);
		border-radius: 70rpx;
		animation: pulse 2s infinite;
	}
	
	@keyframes pulse {
		0% {
			transform: scale(1);
			opacity: 1;
		}
		100% {
			transform: scale(1.2);
			opacity: 0;
		}
	}
	
	.checkmark {
		width: 48rpx;
		height: 48rpx;
		position: relative;
	}
	
	.checkmark::before {
		content: '';
		position: absolute;
		width: 24rpx;
		height: 48rpx;
		border-right: 6rpx solid #FFFFFF;
		border-bottom: 6rpx solid #FFFFFF;
		transform: rotate(45deg);
		top: -4rpx;
		left: 8rpx;
	}
	
	/* 成功消息 */
	.success-message {
		margin-bottom: 48rpx;
		text-align: center;
		max-width: 600rpx;
	}
	
	.message-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		line-height: 1.5;
		text-align: center;
	}
	
	/* 举报信息卡片 */
	.report-card {
		width: 100%;
		background: var(--color-bg, #FFFFFF);
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
		border: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.8));
	}
	
	.card-header {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 24rpx;
		padding-bottom: 16rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.card-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.report-icon {
		width: 24rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
		position: relative;
	}
	
	.report-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 12rpx;
		height: 12rpx;
		background: #FF69B4;
		border-radius: 2rpx;
		transform: translate(-50%, -50%);
	}
	
	.card-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.card-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.info-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16rpx 0;
	}
	
	.info-label {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 400;
	}
	
	.info-value {
		font-size: 26rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.status-pending {
		color: #FF9500;
		font-weight: 600;
	}
	
	/* 温馨提示 */
	.tips-section {
		width: 100%;
		background: var(--color-bg-light, rgba(255, 255, 255, 0.8));
		border-radius: 20rpx;
		padding: 24rpx;
		border: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.6));
	}
	
	.tips-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 16rpx;
	}
	
	.tips-icon {
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.lightbulb {
		width: 16rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
	}
	
	.lightbulb::before {
		content: '';
		position: absolute;
		top: -4rpx;
		left: 50%;
		width: 8rpx;
		height: 8rpx;
		background: #FFFFFF;
		border-radius: 50%;
		transform: translateX(-50%);
	}
	
	.tips-title {
		font-size: 26rpx;
		color: #FF69B4;
		font-weight: 600;
	}
	
	.tips-content {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.tip-item {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.4;
	}
	
	/* 返回按钮 */
	.return-button-section {
		padding: 32rpx;
		display: flex;
		justify-content: center;
    background-color: var(--color-bg);
	}
	
	.return-button {
		width: 100%;
		height: 88rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(255, 105, 180, 0.3);
		transition: all 0.2s ease;
		cursor: pointer;
	}
	
	.return-button:active {
		transform: translateY(2rpx);
		box-shadow: 0 4rpx 16rpx rgba(255, 105, 180, 0.4);
	}
	
	.return-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
</style>