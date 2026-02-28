<template>
	<view class="official-certification-page">
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
			<text class="page-title">{{ $t('verification.official.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 认证选项列表 -->
		<view class="certification-list">
			<!-- 加载状态 -->
			<view class="loading-section" v-if="loading">
				<view class="loading-spinner"></view>
				<text class="loading-text">{{ $t('verification.official.loading') }}</text>
			</view>
			
			<!-- 实名认证 -->
			<view class="list-item" @click="goToRealNameCertification" v-if="!loading">
				<view class="item-content">
					<text class="item-text">{{ $t('verification.official.realName') }}</text>
					<text class="item-desc" v-if="realnameInfo">{{ realnameInfo.ruleDesc || $t('verification.official.identityVerification') }}</text>
				</view>
				<view class="arrow-icon">></view>
			</view>
			<view class="divider" v-if="!loading"></view>
			
			<!-- 学校认证 -->
			<view class="list-item" @click="goToSchoolCertification" v-if="!loading">
				<view class="item-content">
					<text class="item-text">{{ $t('verification.official.school') }}</text>
					<text class="item-desc" v-if="schoolCount > 0">{{ $t('verification.official.supportSchools', { count: schoolCount }) }}</text>
				</view>
				<view class="arrow-icon">></view>
			</view>
			<view class="divider" v-if="!loading"></view>
			
			<!-- 职业认证 -->
			<view class="list-item" @click="goToProfessionalCertification" v-if="!loading">
				<view class="item-content">
					<text class="item-text">{{ $t('verification.official.professional') }}</text>
					<text class="item-desc" v-if="jobCount > 0">{{ $t('verification.official.supportJobs', { count: jobCount }) }}</text>
				</view>
				<view class="arrow-icon">></view>
			</view>
		</view>
		
		<!-- 服务条款链接 -->
		<view class="terms-link" @click="goToTermsOfService">
			<text class="terms-text">{{ $t('verification.official.termsOfService') }}</text>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'OfficialCertificationPage',
		data() {
			return {
				loading: false,
				realnameInfo: null,
				schoolCount: 0,
				jobCount: 0
			}
		},
		onLoad() {
			this.loadCertificationData();
			
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		},
		methods: {
			// 加载认证数据
			async loadCertificationData() {
				this.loading = true;
				try {
					// 并行获取三种认证类型的数据
					await Promise.all([
						this.loadRealnameData(),
						this.loadSchoolData(),
						this.loadJobData()
					]);
				} catch (error) {
					console.error('加载认证数据失败:', error);
					uni.showToast({
						title: this.$t('verification.official.loadFailed'),
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 获取实名认证数据
			async loadRealnameData() {
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-realname-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data && response.data.length > 0) {
						this.realnameInfo = response.data[0]; // 取第一条记录
					}
				} catch (error) {
					console.error('获取实名认证数据失败:', error);
				}
			},
			
			// 获取学校认证数据
			async loadSchoolData() {
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-student-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data) {
						this.schoolCount = response.data.length;
					}
				} catch (error) {
					console.error('获取学校认证数据失败:', error);
				}
			},
			
			// 获取职业认证数据
			async loadJobData() {
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-job-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data) {
						this.jobCount = response.data.length;
					}
				} catch (error) {
					console.error('获取职业认证数据失败:', error);
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			goToRealNameCertification() {
				uni.navigateTo({
					url: '/pages/verification/real-name-verification'
				});
			},
			goToSchoolCertification() {
				uni.navigateTo({
					url: '/pages/verification/school-certification'
				});
			},
			goToProfessionalCertification() {
				uni.navigateTo({
					url: '/pages/verification/professional-certification'
				});
			},
			goToTermsOfService() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			}
		}
	}
</script>

<style>
	.official-certification-page {
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
	
	/* 头部导航 */
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
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text-secondary, #666666);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 认证选项列表 */
	.certification-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.list-item {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16rpx;
		background-color: var(--color-card, #FFFFFF);
		cursor: pointer;
	}
	
	.item-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
	
	.list-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.item-text {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		margin-bottom: 4rpx;
	}
	
	.item-desc {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 400;
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
		font-weight: 300;
	}
	
	.divider {
		height: 1rpx;
		background-color: var(--color-border, #F0F0F0);
		margin: 0 16rpx;
	}
	
	/* 服务条款链接 */
	.terms-link {
		padding: 32rpx;
		text-align: center;
	}
	
	.terms-text {
		font-size: 26rpx;
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 加载状态 */
	.loading-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 0;
	}
	
	.loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 4rpx solid var(--color-border, #F0F0F0);
		border-top: 4rpx solid #007AFF;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 20rpx;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
</style>
