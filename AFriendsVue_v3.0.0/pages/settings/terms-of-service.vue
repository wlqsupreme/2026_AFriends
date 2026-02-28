<!--
 * @description 服务条款页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示交友软件服务条款
 * - 包含账号注册、服务内容、隐私保护等条款
 * - 提供返回功能
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 清晰的条款结构
 * - 易于阅读的排版
-->

<template>
	<view class="terms-of-service-page">
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
			<text class="title">{{ $t('termsOfService.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 介绍部分 -->
			<view class="intro-section">
				<text class="intro-title">{{ $t('termsOfService.introTitle') }}</text>
				<text class="intro-text">{{ $t('termsOfService.introText') }}</text>
			</view>
			
			<!-- 条款内容 -->
			<view class="terms-content">
				<!-- 第一条 -->
				<view class="term-section">
					<text class="term-number">I.</text>
					<text class="term-title">{{ $t('termsOfService.account.security.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.account.security.phoneNumberLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.account.security.phoneNumber.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.account.security.passwordLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.account.security.password.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">{{ $t('termsOfService.account.security.accountSecurityLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.account.security.accountSecurity.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">{{ $t('termsOfService.account.security.accountDeletionLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.account.security.accountDeletion.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第二条 -->
				<view class="term-section">
					<text class="term-number">II.</text>
					<text class="term-title">{{ $t('termsOfService.serviceContent.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.serviceContent.contentLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.serviceContent.content.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.serviceContent.rulesLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.serviceContent.rules.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">{{ $t('termsOfService.serviceContent.publishLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.serviceContent.publish.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">{{ $t('termsOfService.serviceContent.paidLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.serviceContent.paid.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第三条 -->
				<view class="term-section">
					<text class="term-number">III.</text>
					<text class="term-title">{{ $t('termsOfService.privacy.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.privacy.collectionLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.privacy.collection.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.privacy.useLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.privacy.use.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">{{ $t('termsOfService.privacy.securityLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.privacy.security.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第四条 -->
				<view class="term-section">
					<text class="term-number">IV.</text>
					<text class="term-title">{{ $t('termsOfService.intellectualProperty.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.intellectualProperty.contentLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.intellectualProperty.content.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.intellectualProperty.userContentLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.intellectualProperty.userContent.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第五条 -->
				<view class="term-section">
					<text class="term-number">V.</text>
					<text class="term-title">{{ $t('termsOfService.liability.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.liability.limitationLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.liability.limitation.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.liability.disclaimerLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.liability.disclaimer.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第六条 -->
				<view class="term-section">
					<text class="term-number">VI.</text>
					<text class="term-title">{{ $t('termsOfService.agreement.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.agreement.changeLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.agreement.change.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.agreement.terminationLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.agreement.termination.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第七条 -->
				<view class="term-section">
					<text class="term-number">VII.</text>
					<text class="term-title">{{ $t('termsOfService.dispute.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.dispute.lawLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.dispute.law.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.dispute.resolutionLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.dispute.resolution.content') }}</text>
						</view>
					</view>
				</view>
				
				<!-- 第八条 -->
				<view class="term-section">
					<text class="term-number">VIII.</text>
					<text class="term-title">{{ $t('termsOfService.other.title') }}</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">{{ $t('termsOfService.other.entireLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.other.entire.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">{{ $t('termsOfService.other.severabilityLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.other.severability.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">{{ $t('termsOfService.other.waiverLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.other.waiver.content') }}</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">{{ $t('termsOfService.other.noticeLabel') }}</text>
							<text class="item-content">{{ $t('termsOfService.other.notice.content') }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 结尾说明 -->
			<view class="closing-section">
				<text class="closing-text">{{ $t('termsOfService.closing') }}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'TermsOfServicePage',
		methods: {
			goBack() {
				uni.navigateBack();
			}
		},
		mounted() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		}
	}
</script>

<style scoped>
	.terms-of-service-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
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
		background-color: var(--color-card, #FFFFFF);
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
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
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
		background: var(--color-bg-weak, rgba(0, 0, 0, 0.1));
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
		padding: 32rpx;
	}
	
	/* 介绍部分 */
	.intro-section {
		margin-bottom: 48rpx;
	}
	
	.intro-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		display: block;
		margin-bottom: 24rpx;
	}
	
	.intro-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.6;
		display: block;
	}
	
	/* 条款内容 */
	.terms-content {
		margin-bottom: 48rpx;
	}
	
	.term-section {
		margin-bottom: 40rpx;
	}
	
	.term-number {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		display: block;
		margin-bottom: 16rpx;
	}
	
	.term-title {
		font-size: 30rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		display: block;
		margin-bottom: 24rpx;
	}
	
	.term-items {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}
	
	.term-item {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.item-number {
		font-size: 28rpx;
		color: #FF6B6B;
		font-weight: 600;
		display: block;
	}
	
	.item-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		display: block;
	}
	
	.item-content {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.6;
		display: block;
	}
	
	/* 结尾说明 */
	.closing-section {
		padding: 32rpx 0;
		border-top: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.closing-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
		line-height: 1.6;
		text-align: center;
		display: block;
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
