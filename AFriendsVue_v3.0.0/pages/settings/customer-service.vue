<!--
 * @description 客服页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 提供客服中心的各种服务入口
 * - 包含服务分类、订单/投稿、我的服务、常见问题等模块
 * - 支持客服电话和发起新服务功能
 * 
 * 页面设计：
 * - 浅色渐变背景
 * - 现代化的卡片设计
 * - 清晰的服务分类网格
 * - 友好的用户交互体验
-->

<template>
	<view class="customer-service-page" :class="{ care: isCare }">
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
		<view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<view class="header-content">
				<text class="title">{{ $t('customerService.title') }}</text>
				<text class="subtitle">{{ $t('customerService.subtitle') }}</text>
			</view>
			<view class="mascot">
				<view class="cat-character">
					<view class="cat-face">
						<view class="cat-eye left-eye"></view>
						<view class="cat-eye right-eye"></view>
						<view class="cat-mouth"></view>
					</view>
					<view class="cat-body">
						<view class="logo-symbol">A</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 服务分类网格 -->
			<view class="service-grid">
				<view class="grid-row">
					<view class="service-item" @click="goToService('appeal')">
						<view class="service-icon appeal-icon">
							<view class="wave-line"></view>
							<view class="arrow-up"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.appeal') }}</text>
					</view>
					<view class="service-item" @click="goToService('report')">
						<view class="service-icon report-icon">
							<view class="exclamation-mark"></view>
							<view class="person-silhouette"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.report') }}</text>
					</view>
					<view class="service-item" @click="goToService('security')">
						<view class="service-icon security-icon">
							<view class="shield"></view>
							<view class="checkmark"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.security') }}</text>
					</view>
				</view>
				
				<view class="grid-row">
					<view class="service-item" @click="goToService('verification')">
						<view class="service-icon verification-icon">
							<view class="lightning"></view>
							<view class="speech-bubble"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.verification') }}</text>
					</view>
					<view class="service-item" @click="goToService('shop')">
						<view class="service-icon shop-icon">
							<view class="storefront"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.shop') }}</text>
					</view>
					<view class="service-item" @click="goToService('selling')">
						<view class="service-icon selling-icon">
							<view class="people-group"></view>
							<view class="shopping-bag"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.selling') }}</text>
					</view>
				</view>
				
				<view class="grid-row">
					<view class="service-item" @click="goToService('account-recovery')">
						<view class="service-icon recovery-icon">
							<view class="star"></view>
							<view class="person-silhouette"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.accountRecovery') }}</text>
					</view>
					<view class="service-item" @click="goToService('account-security')">
						<view class="service-icon account-security-icon">
							<view class="shield-100"></view>
							<text class="hundred">100</text>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.accountSecurity') }}</text>
					</view>
					<view class="service-item" @click="goToService('enterprise')">
						<view class="service-icon enterprise-icon">
							<view class="hexagon"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.enterprise') }}</text>
					</view>
				</view>
				
				<view class="grid-row">
					<view class="service-item" @click="goToService('billing')">
						<view class="service-icon billing-icon">
							<view class="currency-symbol">¥</view>
							<view class="document"></view>
						</view>
						<text class="service-label">{{ $t('customerService.serviceCategories.billing') }}</text>
					</view>
					<view class="service-item"></view>
					<view class="service-item"></view>
				</view>
			</view>
			
			<!-- 分页指示器 -->
			<view class="pagination-dots">
				<view class="dot active"></view>
				<view class="dot"></view>
			</view>
			
			<!-- 订单/投稿标签页 -->
			<view class="tabs-section">
				<view class="tabs-header">
					<view class="tab" :class="{ active: activeTab === 'orders' }" @click="switchTab('orders')">
						<text class="tab-text">{{ $t('customerService.tabs.orders') }}</text>
					</view>
					<view class="tab" :class="{ active: activeTab === 'submissions' }" @click="switchTab('submissions')">
						<text class="tab-text">{{ $t('customerService.tabs.submissions') }}</text>
					</view>
					<view class="view-all">
						<text class="view-all-text">{{ $t('customerService.viewAll') }}</text>
					</view>
				</view>
				
				<view class="tab-content">
					<view class="content-scroll">
						<view class="content-item">
							<view class="item-image">
								<view class="food-image">
									<view class="soup-bowl"></view>
									<view class="ingredients"></view>
								</view>
							</view>
							<view class="item-info">
								<text class="item-title">【逃离工位】单人快...</text>
								<text class="item-description">含锅底小特色瓶子饭</text>
								<text class="item-price">¥34.9</text>
							</view>
							<view class="consult-button">
								<text class="consult-text">咨询</text>
							</view>
						</view>
						
						<view class="content-item">
							<view class="item-image">
								<view class="choice-image">
									<view class="cylinder-1"></view>
									<view class="cylinder-2"></view>
								</view>
							</view>
							<view class="item-info">
								<text class="item-title">2选1</text>
								<text class="item-description">选择您喜欢的商品</text>
								<text class="item-price">¥29.9</text>
							</view>
							<view class="consult-button">
								<text class="consult-text">咨询</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 我的服务 -->
			<view class="my-services-section">
				<view class="section-header">
					<text class="section-title">{{ $t('customerService.services') }}</text>
					<view class="arrow-icon">
						<view class="arrow-right-large"></view>
					</view>
				</view>
			</view>
			
			<!-- 常见问题 -->
			<view class="common-questions-section">
				<view class="section-header">
					<text class="section-title">{{ $t('customerService.commonQuestions') }}</text>
					<view class="view-more">
						<text class="view-more-text">{{ $t('customerService.viewMore') }}</text>
					</view>
				</view>
				<view class="questions-list">
					<view class="question-item" v-for="(question, index) in questions" :key="index" @click="goToQuestion(question)">
						<text class="question-number" :class="{ active: index < 3 }">{{ index + 1 }}</text>
						<text class="question-text">{{ question }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部操作栏 -->
		<view class="bottom-action-bar">
			<view class="phone-button" @click="callCustomerService">
				<view class="phone-icon"></view>
				<text class="button-text">{{ $t('customerService.phoneNumber') }}</text>
			</view>
			<view class="new-service-button" @click="initiateNewService">
				<view class="speech-bubble-icon">
					<view class="three-dots"></view>
				</view>
				<text class="button-text">{{ $t('customerService.newService') }}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useModeStore } from '@/store/mode.js';

	export default {
		name: 'CustomerServicePage',
		data() {
			return {
				activeTab: 'orders',
				modeStore: null,
				questions: [
					'账号处罚常见问题',
					'为什么封禁账号不能注销',
					'举报相关常见问题',
					'账号被处罚了我该怎么办?',
					'账号实名相关问题'
				]
			}
		},
		onLoad() {
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
		},
		computed: {
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			switchTab(tab) {
				this.activeTab = tab;
			},
			goToService(serviceType) {
				uni.showToast({
					title: `进入${serviceType}服务`,
					icon: 'none'
				});
			},
			goToQuestion(question) {
				uni.showToast({
					title: `查看问题：${question}`,
					icon: 'none'
				});
			},
			callCustomerService() {
				uni.showModal({
					title: this.$t('customerService.phoneNumber'),
					content: '400-123-4567',
					confirmText: this.$t('common.confirm'),
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: this.$t('common.loading'),
								icon: 'none'
							});
						}
					}
				});
			},
			initiateNewService() {
				uni.showToast({
					title: '发起新服务',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.customer-service-page {
		min-height: 100vh;
		background: linear-gradient(135deg, #FFF5F5 0%, #FFFFFF 100%);
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
		color: #000000;
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
		background-color: #000000;
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background: transparent;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 24rpx;
		background: rgba(0, 0, 0, 0.05);
		transition: all 0.2s ease;
	}
	
	.back-button:active {
		background: rgba(0, 0, 0, 0.1);
		transform: scale(0.95);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #000000;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.header-content {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}
	
	.title {
		font-size: 36rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.subtitle {
		font-size: 24rpx;
		color: #999999;
		font-weight: 400;
	}
	
	.mascot {
		width: 80rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.cat-character {
		width: 60rpx;
		height: 60rpx;
		position: relative;
	}
	
	.cat-face {
		width: 40rpx;
		height: 40rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
		top: 0;
		left: 10rpx;
	}
	
	.cat-eye {
		width: 8rpx;
		height: 8rpx;
		background: #000000;
		border-radius: 50%;
		position: absolute;
		top: 12rpx;
	}
	
	.left-eye {
		left: 8rpx;
	}
	
	.right-eye {
		right: 8rpx;
	}
	
	.cat-mouth {
		width: 12rpx;
		height: 6rpx;
		border: 2rpx solid #000000;
		border-top: none;
		border-radius: 0 0 12rpx 12rpx;
		position: absolute;
		bottom: 8rpx;
		left: 14rpx;
	}
	
	.cat-body {
		width: 50rpx;
		height: 30rpx;
		background: #FF6B6B;
		border-radius: 25rpx;
		position: absolute;
		bottom: 0;
		left: 5rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.logo-symbol {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 0 32rpx;
	}
	
	/* 服务分类网格 */
	.service-grid {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}
	
	.grid-row {
		display: flex;
		justify-content: space-between;
		margin-bottom: 32rpx;
	}
	
	.grid-row:last-child {
		margin-bottom: 0;
	}
	
	.service-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		flex: 1;
		cursor: pointer;
	}
	
	.service-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}
	
	/* 处罚申诉图标 */
	.appeal-icon {
		background: linear-gradient(135deg, #8B5CF6, #A78BFA);
	}
	
	.wave-line {
		width: 40rpx;
		height: 20rpx;
		background: #FFFFFF;
		border-radius: 10rpx;
		position: relative;
	}
	
	.wave-line::before {
		content: '';
		position: absolute;
		top: -4rpx;
		left: 8rpx;
		width: 24rpx;
		height: 8rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
	}
	
	.arrow-up {
		width: 0;
		height: 0;
		border-left: 6rpx solid transparent;
		border-right: 6rpx solid transparent;
		border-bottom: 8rpx solid #FFFFFF;
		position: absolute;
		bottom: 8rpx;
		right: 8rpx;
	}
	
	/* 举报中心图标 */
	.report-icon {
		background: linear-gradient(135deg, #10B981, #34D399);
	}
	
	.exclamation-mark {
		width: 8rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
		position: relative;
	}
	
	.exclamation-mark::after {
		content: '';
		position: absolute;
		bottom: -8rpx;
		left: -2rpx;
		width: 12rpx;
		height: 12rpx;
		background: #FFFFFF;
		border-radius: 50%;
	}
	
	.person-silhouette {
		width: 20rpx;
		height: 20rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: absolute;
		bottom: 8rpx;
		right: 8rpx;
	}
	
	/* 安全中心图标 */
	.security-icon {
		background: linear-gradient(135deg, #3B82F6, #60A5FA);
	}
	
	.shield {
		width: 32rpx;
		height: 40rpx;
		background: #FFFFFF;
		border-radius: 16rpx 16rpx 8rpx 8rpx;
		position: relative;
	}
	
	.checkmark {
		width: 16rpx;
		height: 16rpx;
		border-right: 3rpx solid #3B82F6;
		border-bottom: 3rpx solid #3B82F6;
		transform: rotate(45deg);
		position: absolute;
		top: 8rpx;
		left: 8rpx;
	}
	
	/* 验证助手图标 */
	.verification-icon {
		background: linear-gradient(135deg, #F59E0B, #FBBF24);
	}
	
	.lightning {
		width: 20rpx;
		height: 24rpx;
		background: #FFFFFF;
		clip-path: polygon(50% 0%, 0% 100%, 50% 70%, 100% 100%);
	}
	
	.speech-bubble {
		width: 24rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 12rpx;
		position: absolute;
		bottom: 8rpx;
		right: 8rpx;
	}
	
	/* 抖店入驻图标 */
	.shop-icon {
		background: linear-gradient(135deg, #F59E0B, #FBBF24);
	}
	
	.storefront {
		width: 32rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
		position: relative;
	}
	
	.storefront::before {
		content: '';
		position: absolute;
		top: -8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 0;
		height: 0;
		border-left: 16rpx solid transparent;
		border-right: 16rpx solid transparent;
		border-bottom: 8rpx solid #FFFFFF;
	}
	
	/* 我要带货图标 */
	.selling-icon {
		background: linear-gradient(135deg, #F59E0B, #FBBF24);
	}
	
	.people-group {
		width: 24rpx;
		height: 16rpx;
		display: flex;
		gap: 4rpx;
	}
	
	.people-group::before,
	.people-group::after {
		content: '';
		width: 8rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
	}
	
	.shopping-bag {
		width: 20rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
		position: absolute;
		bottom: 8rpx;
		right: 8rpx;
	}
	
	/* 账号找回图标 */
	.recovery-icon {
		background: linear-gradient(135deg, #EC4899, #F472B6);
	}
	
	.star {
		width: 24rpx;
		height: 24rpx;
		background: #FFFFFF;
		clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
	}
	
	/* 账号安全图标 */
	.account-security-icon {
		background: linear-gradient(135deg, #10B981, #34D399);
		position: relative;
	}
	
	.shield-100 {
		width: 32rpx;
		height: 40rpx;
		background: #FFFFFF;
		border-radius: 16rpx 16rpx 8rpx 8rpx;
	}
	
	.hundred {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		font-size: 16rpx;
		color: #10B981;
		font-weight: 600;
	}
	
	/* 企业认证图标 */
	.enterprise-icon {
		background: linear-gradient(135deg, #8B5CF6, #A78BFA);
	}
	
	.hexagon {
		width: 32rpx;
		height: 32rpx;
		background: #FFFFFF;
		clip-path: polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%);
	}
	
	/* 月付查账图标 */
	.billing-icon {
		background: linear-gradient(135deg, #3B82F6, #60A5FA);
		position: relative;
	}
	
	.currency-symbol {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
		position: absolute;
		top: 8rpx;
		left: 8rpx;
	}
	
	.document {
		width: 24rpx;
		height: 32rpx;
		background: #FFFFFF;
		border-radius: 4rpx;
		position: absolute;
		bottom: 8rpx;
		right: 8rpx;
	}
	
	.service-label {
		font-size: 24rpx;
		color: #333333;
		font-weight: 500;
		text-align: center;
	}
	
	/* 分页指示器 */
	.pagination-dots {
		display: flex;
		justify-content: center;
		gap: 16rpx;
		margin-bottom: 24rpx;
	}
	
	.dot {
		width: 16rpx;
		height: 16rpx;
		border-radius: 50%;
		border: 2rpx solid #FF6B6B;
	}
	
	.dot.active {
		background: #FF6B6B;
	}
	
	/* 标签页 */
	.tabs-section {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}
	
	.tabs-header {
		display: flex;
		align-items: center;
		margin-bottom: 24rpx;
	}
	
	.tab {
		padding: 16rpx 32rpx;
		border-radius: 20rpx;
		margin-right: 16rpx;
		transition: all 0.2s ease;
	}
	
	.tab.active {
		background: #FF6B6B;
	}
	
	.tab-text {
		font-size: 28rpx;
		color: #999999;
		font-weight: 500;
	}
	
	.tab.active .tab-text {
		color: #FFFFFF;
	}
	
	.view-all {
		margin-left: auto;
	}
	
	.view-all-text {
		font-size: 24rpx;
		color: #999999;
	}
	
	.tab-content {
		overflow-x: auto;
	}
	
	.content-scroll {
		display: flex;
		gap: 24rpx;
	}
	
	.content-item {
		min-width: 300rpx;
		background: #F8F8F8;
		border-radius: 16rpx;
		padding: 20rpx;
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.item-image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 12rpx;
		overflow: hidden;
	}
	
	.food-image {
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
		position: relative;
	}
	
	.soup-bowl {
		width: 60rpx;
		height: 40rpx;
		background: #FFFFFF;
		border-radius: 30rpx 30rpx 0 0;
		position: absolute;
		top: 20rpx;
		left: 10rpx;
	}
	
	.ingredients {
		width: 16rpx;
		height: 16rpx;
		background: #FFD700;
		border-radius: 50%;
		position: absolute;
		top: 30rpx;
		right: 10rpx;
	}
	
	.choice-image {
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #8B4513, #A0522D);
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
	}
	
	.cylinder-1, .cylinder-2 {
		width: 20rpx;
		height: 40rpx;
		background: #FFFFFF;
		border-radius: 10rpx;
	}
	
	.item-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.item-title {
		font-size: 26rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.item-description {
		font-size: 22rpx;
		color: #666666;
	}
	
	.item-price {
		font-size: 24rpx;
		color: #FF6B6B;
		font-weight: 600;
	}
	
	.consult-button {
		background: #FF6B6B;
		padding: 12rpx 20rpx;
		border-radius: 20rpx;
	}
	
	.consult-text {
		font-size: 22rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 我的服务 */
	.my-services-section {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}
	
	.section-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.section-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.arrow-icon {
		width: 32rpx;
		height: 32rpx;
		background: #F59E0B;
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.arrow-right-large {
		width: 0;
		height: 0;
		border-left: 8rpx solid #FFFFFF;
		border-top: 6rpx solid transparent;
		border-bottom: 6rpx solid transparent;
	}
	
	/* 常见问题 */
	.common-questions-section {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	}
	
	.view-more {
		margin-left: auto;
	}
	
	.view-more-text {
		font-size: 24rpx;
		color: #999999;
	}
	
	.questions-list {
		margin-top: 16rpx;
	}
	
	.question-item {
		display: flex;
		align-items: center;
		gap: 16rpx;
		padding: 16rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.question-item:last-child {
		border-bottom: none;
	}
	
	.question-number {
		width: 32rpx;
		height: 32rpx;
		border-radius: 50%;
		background: #999999;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 20rpx;
		font-weight: 600;
	}
	
	.question-number.active {
		background: #FF6B6B;
	}
	
	.question-text {
		font-size: 26rpx;
		color: #333333;
		flex: 1;
	}
	
	/* 底部操作栏 */
	.bottom-action-bar {
		height: 120rpx;
		background: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		gap: 24rpx;
	}
	
	.phone-button {
		flex: 1;
		height: 80rpx;
		background: #FFFFFF;
		border: 2rpx solid #E0E0E0;
		border-radius: 40rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 8rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.phone-button:active {
		background: #F8F8F8;
		transform: scale(0.98);
	}
	
	.phone-icon {
		width: 24rpx;
		height: 24rpx;
		background: #000000;
		border-radius: 50%;
		position: relative;
	}
	
	.phone-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		width: 16rpx;
		height: 16rpx;
		border: 2rpx solid #FFFFFF;
		border-radius: 50%;
	}
	
	.new-service-button {
		flex: 2;
		height: 80rpx;
		background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 16rpx;
		cursor: pointer;
		transition: all 0.2s ease;
		box-shadow: 0 4rpx 16rpx rgba(255, 107, 107, 0.3);
	}
	
	.new-service-button:active {
		transform: translateY(2rpx);
		box-shadow: 0 2rpx 8rpx rgba(255, 107, 107, 0.4);
	}
	
	.speech-bubble-icon {
		width: 32rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 12rpx;
		position: relative;
	}
	
	.three-dots {
		display: flex;
		gap: 4rpx;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
	}
	
	.three-dots::before,
	.three-dots::after {
		content: '';
		width: 4rpx;
		height: 4rpx;
		background: #FF6B6B;
		border-radius: 50%;
	}
	
	.three-dots::before {
		margin-right: 4rpx;
	}
	
	.three-dots::after {
		margin-left: 4rpx;
	}
	
	.button-text {
		font-size: 24rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.new-service-button .button-text {
		color: #FFFFFF;
		font-size: 26rpx;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}

	.customer-service-page.care .header {
		height: 160rpx;
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.customer-service-page.care .back-button {
		width: 72rpx;
		height: 72rpx;
		border-radius: 36rpx;
	}

	.customer-service-page.care .back-arrow {
		border-right-width: 16rpx;
		border-top-width: 10rpx;
		border-bottom-width: 10rpx;
	}

	.customer-service-page.care .title {
		font-size: 44rpx;
	}

	.customer-service-page.care .subtitle {
		font-size: 32rpx;
	}

	.customer-service-page.care .mascot {
		width: 112rpx;
		height: 112rpx;
	}

	.customer-service-page.care .main-content {
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.customer-service-page.care .service-grid {
		padding: 40rpx;
	}

	.customer-service-page.care .service-icon {
		width: 110rpx;
		height: 110rpx;
		border-radius: 26rpx;
	}

	.customer-service-page.care .service-label {
		font-size: 32rpx;
		line-height: 1.4;
	}

	.customer-service-page.care .tabs-section {
		padding: 32rpx;
	}

	.customer-service-page.care .tab {
		padding: 20rpx 44rpx;
		border-radius: 26rpx;
	}

	.customer-service-page.care .tab-text {
		font-size: 34rpx;
	}

	.customer-service-page.care .view-all-text {
		font-size: 30rpx;
	}

	.customer-service-page.care .content-item {
		min-width: 360rpx;
		padding: 28rpx;
	}

	.customer-service-page.care .item-title {
		font-size: 32rpx;
	}

	.customer-service-page.care .item-description {
		font-size: 28rpx;
	}

	.customer-service-page.care .item-price {
		font-size: 30rpx;
	}

	.customer-service-page.care .consult-button {
		padding: 16rpx 28rpx;
		border-radius: 26rpx;
	}

	.customer-service-page.care .consult-text {
		font-size: 28rpx;
	}

	.customer-service-page.care .section-title {
		font-size: 34rpx;
	}

	.customer-service-page.care .question-item {
		padding: 24rpx 0;
	}

	.customer-service-page.care .question-number {
		width: 44rpx;
		height: 44rpx;
		font-size: 26rpx;
	}

	.customer-service-page.care .question-text {
		font-size: 32rpx;
		line-height: 1.5;
	}

	.customer-service-page.care .bottom-action-bar {
		height: 160rpx;
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.customer-service-page.care .phone-button,
	.customer-service-page.care .new-service-button {
		height: 112rpx;
		border-radius: 56rpx;
	}

	.customer-service-page.care .phone-icon {
		width: 32rpx;
		height: 32rpx;
	}

	.customer-service-page.care .speech-bubble-icon {
		width: 44rpx;
		height: 32rpx;
		border-radius: 16rpx;
	}

	.customer-service-page.care .button-text {
		font-size: 30rpx;
	}

	.customer-service-page.care .new-service-button .button-text {
		font-size: 32rpx;
	}
</style>
