<template>
	<view class="verification-complete-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">认证完成</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-section">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载认证信息...</text>
			</view>
			
			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-section">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="retry-button" @click="retryLoad">
					<text class="retry-text">重试</text>
				</view>
			</view>
			
			<!-- 成功状态 -->
			<view v-else>
				<!-- 成功图标 -->
				<view class="success-icon">
					<view class="icon-circle" :class="getStatusClass()">
						<text class="checkmark">{{ getStatusIcon() }}</text>
					</view>
				</view>
				
				<!-- 状态标题 -->
				<view class="status-title">
					<text class="title-text">{{ getStatusTitle() }}</text>
				</view>
				
				<!-- 成功描述 -->
				<view class="success-desc">
					<text class="desc-text">{{ getStatusDesc() }}</text>
				</view>
				
				<!-- 认证信息卡片 -->
				<view class="verification-info">
					<view class="info-header">
						<text class="info-title">认证信息</text>
					</view>
					<view class="info-content">
						<view class="info-item">
							<text class="item-label">认证类型</text>
							<text class="item-value">{{ verificationType }}</text>
						</view>
						<view class="info-item">
							<text class="item-label">认证时间</text>
							<text class="item-value">{{ verificationTime }}</text>
						</view>
						<view class="info-item">
							<text class="item-label">认证状态</text>
							<view class="status-badge" :class="getStatusBadgeClass()">
								<text class="status-text">{{ verificationStatus }}</text>
							</view>
						</view>
						<!-- 认证详情 -->
						<view v-if="verificationRecord && verificationRecord.certInfo" class="info-item">
							<text class="item-label">认证详情</text>
							<text class="item-value">{{ getCertDetails() }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 下一步提示 -->
			<view class="next-steps">
				<text class="steps-title">下一步您可以：</text>
				<view class="steps-list">
					<view class="step-item">
						<view class="step-icon">1</view>
						<text class="step-text">完善个人资料</text>
					</view>
					<view class="step-item">
						<view class="step-icon">2</view>
						<text class="step-text">申请官方认证</text>
					</view>
					<view class="step-item">
						<view class="step-icon">3</view>
						<text class="step-text">开始使用平台服务</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 操作按钮 -->
		<view class="action-buttons">
			<view class="primary-button" @click="goToHome">
				<text class="button-text">返回首页</text>
			</view>
			<view class="secondary-button" @click="goToProfile">
				<text class="button-text">完善资料</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';

	export default {
		name: 'VerificationCompletePage',
		data() {
			return {
				verificationType: '实名认证',
				verificationTime: '2024年8月20日 14:30',
				verificationStatus: '已认证',
				loading: true,
				verificationRecord: null,
				errorMessage: ''
			}
		},
		onLoad(options) {
			// 从页面参数获取认证类型
			if (options.type) {
				this.verificationType = this.getCertTypeText(options.type);
			}
			// 加载认证记录
			this.loadVerificationRecord();
		},
		methods: {
			// 加载认证记录
			async loadVerificationRecord() {
				this.loading = true;
				try {
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						this.errorMessage = '用户未登录';
						this.loading = false;
						return;
					}
					
					const response = await this.getUserCertRecords(userId);
					if (response.data && response.data.length > 0) {
						// 获取最新的认证记录
						this.verificationRecord = response.data[response.data.length - 1];
						this.updateVerificationInfo();
					} else {
						this.errorMessage = '未找到认证记录';
					}
				} catch (error) {
					console.error('加载认证记录失败:', error);
					this.errorMessage = '加载认证记录失败';
				} finally {
					this.loading = false;
				}
			},
					
			mounted() {
				// 初始化主题
				const themeStore = useThemeStore();
				themeStore.init();
			},
			
			// 获取用户认证记录
			getUserCertRecords(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: '${this.$baseUrl}/api/u-entities/user-cert-record/user/' + userId,
						method: 'GET',
						timeout: 10000,
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 更新认证信息显示
			updateVerificationInfo() {
				if (this.verificationRecord) {
					// 设置认证类型
					this.verificationType = this.getCertTypeText(this.verificationRecord.certType);
					
					// 设置认证时间
					const createTime = new Date(this.verificationRecord.createdAt);
					this.verificationTime = this.formatDateTime(createTime);
					
					// 设置认证状态
					this.verificationStatus = this.getStatusText(this.verificationRecord.status);
				}
			},
			
			// 获取认证类型文本
			getCertTypeText(certType) {
				const typeMap = {
					'REALNAME': '实名认证',
					'STUDENT': '学校认证',
					'JOB': '职业认证'
				};
				return typeMap[certType] || '未知认证';
			},
			
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					0: '待审核',
					1: '已通过',
					2: '未通过'
				};
				return statusMap[status] || '未知状态';
			},
			
			// 格式化日期时间
			formatDateTime(date) {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				const hours = String(date.getHours()).padStart(2, '0');
				const minutes = String(date.getMinutes()).padStart(2, '0');
				return `${year}年${month}月${day}日 ${hours}:${minutes}`;
			},
			
			// 重试加载
			retryLoad() {
				this.loadVerificationRecord();
			},
			
			// 获取状态样式类
			getStatusClass() {
				if (!this.verificationRecord) return 'status-pending';
				const status = this.verificationRecord.status;
				if (status === 1) return 'status-success';
				if (status === 2) return 'status-error';
				return 'status-pending';
			},
			
			// 获取状态图标
			getStatusIcon() {
				if (!this.verificationRecord) return '?';
				const status = this.verificationRecord.status;
				if (status === 1) return '✓';
				if (status === 2) return '✗';
				return '⏳';
			},
			
			// 获取状态标题
			getStatusTitle() {
				if (!this.verificationRecord) return '认证处理中';
				const status = this.verificationRecord.status;
				if (status === 1) return '认证成功';
				if (status === 2) return '认证失败';
				return '认证处理中';
			},
			
			// 获取状态描述
			getStatusDesc() {
				if (!this.verificationRecord) return '您的认证申请正在处理中，请耐心等待';
				const status = this.verificationRecord.status;
				if (status === 1) return `恭喜您完成${this.verificationType}，现在可以享受更多平台服务`;
				if (status === 2) return '很抱歉，您的认证申请未通过，请检查信息后重新提交';
				return '您的认证申请正在审核中，我们会在1-3个工作日内完成审核';
			},
			
			// 获取状态徽章样式
			getStatusBadgeClass() {
				if (!this.verificationRecord) return 'badge-pending';
				const status = this.verificationRecord.status;
				if (status === 1) return 'badge-success';
				if (status === 2) return 'badge-error';
				return 'badge-pending';
			},
			
			// 获取认证详情
			getCertDetails() {
				if (!this.verificationRecord || !this.verificationRecord.certInfo) return '';
				
				try {
					const certInfo = JSON.parse(this.verificationRecord.certInfo);
					const certType = this.verificationRecord.certType;
					
					if (certType === 'REALNAME') {
						return `${certInfo.name || ''} - ${certInfo.documentType || ''}`;
					} else if (certType === 'STUDENT') {
						return `${certInfo.school || ''} - ${certInfo.degree || ''}`;
					} else if (certType === 'JOB') {
						return `${certInfo.jobType || ''} - ${certInfo.jobName || ''}`;
					}
					return '认证详情';
				} catch (error) {
					console.error('解析认证详情失败:', error);
					return '认证详情';
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/content-feed'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			}
		}
	}
</script>

<style>
	.verification-complete-page {
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
		color: var(--color-text-inverse, #FFFFFF);
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
		background-color: var(--color-text-inverse, #FFFFFF);
		border-radius: 4rpx;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: rgba(255, 255, 255, 0.2);
		border-radius: 24rpx;
		backdrop-filter: blur(10rpx);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text-inverse, #FFFFFF);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: var(--color-text-inverse, #FFFFFF);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	
	/* 状态图标 */
	.success-icon {
		margin-bottom: 32rpx;
		display: flex;
		justify-content: center;
	}
	
	.icon-circle {
		width: 160rpx;
		height: 160rpx;
		background: linear-gradient(135deg, #00FF00, #00CC00);
		border-radius: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx rgba(0, 255, 0, 0.4);
	}
	
	.checkmark {
		font-size: 80rpx;
		color: var(--color-text-inverse, #FFFFFF);
		font-weight: bold;
	}
	
	/* 状态标题 */
	.status-title {
		margin-bottom: 16rpx;
		display: flex;
		justify-content: center;
	}
	
	.title-text {
		font-size: 48rpx;
		color: var(--color-text-inverse, #FFFFFF);
		font-weight: 700;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
	}
	
	/* 状态描述 */
	.success-desc {
		margin-bottom: 48rpx;
		text-align: center;
		display: flex;
		justify-content: center;
	}
	
	.desc-text {
		font-size: 28rpx;
		color: var(--color-text-inverse-secondary, rgba(255, 255, 255, 0.9));
		line-height: 1.5;
	}
	
	/* 认证信息卡片 */
	.verification-info {
		width: 100%;
		background: var(--verification-card-bg, rgba(255, 255, 255, 0.95));
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 48rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
	}
	
	.info-header {
		margin-bottom: 24rpx;
	}
	
	.info-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.info-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.info-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 16rpx 0;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.info-item:last-child {
		border-bottom: none;
	}
	
	.item-label {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.item-value {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.status-badge {
		background: linear-gradient(135deg, #00FF00, #00CC00);
		padding: 8rpx 20rpx;
		border-radius: 20rpx;
	}
	
	.status-text {
		font-size: 24rpx;
		color: var(--color-text-inverse, #FFFFFF);
		font-weight: 600;
	}
	
	/* 下一步提示 */
	.next-steps {
		width: 100%;
		background: var(--verification-card-bg, rgba(255, 255, 255, 0.95));
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
	}
	
	.steps-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		margin-bottom: 24rpx;
		display: block;
	}
	
	.steps-list {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.step-item {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.step-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #667eea, #764ba2);
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		color: var(--color-text-inverse, #FFFFFF);
		font-size: 24rpx;
		font-weight: 600;
	}
	
	.step-text {
		font-size: 26rpx;
		color: var(--color-text, #333333);
	}
	
	/* 操作按钮 */
	.action-buttons {
		padding: 0 32rpx 32rpx;
		display: flex;
		gap: 24rpx;
	}
	
	.primary-button, .secondary-button {
		flex: 1;
		height: 88rpx;
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	.primary-button {
		background: linear-gradient(135deg, #FFFFFF, #F8F8F8);
		box-shadow: 0 4rpx 16rpx rgba(255, 255, 255, 0.3);
	}
	
	.secondary-button {
		background: rgba(255, 255, 255, 0.2);
		border: 2rpx solid rgba(255, 255, 255, 0.5);
		backdrop-filter: blur(10rpx);
	}
	
	.primary-button:active, .secondary-button:active {
		transform: scale(0.98);
	}
	
	.primary-button .button-text {
		color: var(--color-text, #333333);
	}
	
	.secondary-button .button-text {
		color: var(--color-text-inverse, #FFFFFF);
	}
	
	.button-text {
		font-size: 28rpx;
		font-weight: 600;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text-inverse-soft, rgba(255, 255, 255, 0.6));
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 加载状态 */
	.loading-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 80rpx 0;
	}
	
	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid var(--color-border-light, rgba(255, 255, 255, 0.3));
		border-top: 4rpx solid var(--color-text-inverse, #FFFFFF);
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 24rpx;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-inverse-secondary, rgba(255, 255, 255, 0.9));
	}
	
	/* 错误状态 */
	.error-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 80rpx 0;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 24rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: var(--color-text-inverse-secondary, rgba(255, 255, 255, 0.9));
		text-align: center;
		margin-bottom: 32rpx;
	}
	
	.retry-button {
		background: var(--verification-button-bg, rgba(255, 255, 255, 0.2));
		border: 2rpx solid var(--verification-button-border, rgba(255, 255, 255, 0.5));
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		backdrop-filter: blur(10rpx);
	}
	
	.retry-text {
		font-size: 28rpx;
		color: var(--color-text-inverse, #FFFFFF);
		font-weight: 600;
	}
	
	/* 状态样式 */
	.status-success {
		background: linear-gradient(135deg, #00FF00, #00CC00);
		box-shadow: 0 8rpx 32rpx rgba(0, 255, 0, 0.4);
	}
	
	.status-error {
		background: linear-gradient(135deg, #FF4444, #CC0000);
		box-shadow: 0 8rpx 32rpx rgba(255, 68, 68, 0.4);
	}
	
	.status-pending {
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		box-shadow: 0 8rpx 32rpx rgba(255, 165, 0, 0.4);
	}
	
	/* 状态徽章样式 */
	.badge-success {
		background: linear-gradient(135deg, #00FF00, #00CC00);
	}
	
	.badge-error {
		background: linear-gradient(135deg, #FF4444, #CC0000);
	}
	
	.badge-pending {
		background: linear-gradient(135deg, #FFA500, #FF8C00);
	}
	
	/* 暗夜模式特殊样式 */
	@media (prefers-color-scheme: dark) {
		.verification-info,
		.next-steps {
			background: var(--verification-card-bg-dark, rgba(30, 30, 30, 0.95));
		}
		
		.info-item {
			border-bottom: 1rpx solid var(--color-border-dark, #444444);
		}
	}
</style>
