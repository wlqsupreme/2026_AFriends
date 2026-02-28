<template>
	<view class="achievements-page" :class="themeStore.themeClass">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<view class="header">
			<!-- <view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">{{ $t('achievements.title') }}</text> -->
			<!-- <view class="placeholder"></view> -->
		</view>
		
		<!-- 成就统计卡片 -->
		<view class="stats-card">
			<view class="stats-content">
				<view class="stats-item">
					<text class="stats-number">{{ stats.completedAchievements }}</text>
					<text class="stats-label">{{ $t('achievements.stats.completed') }}</text>
				</view>
				<view class="stats-divider"></view>
				<view class="stats-item">
					<text class="stats-number">{{ stats.totalAchievements }}</text>
					<text class="stats-label">{{ $t('achievements.stats.total') }}</text>
				</view>
				<view class="stats-divider"></view>
				<view class="stats-item">
					<text class="stats-number">{{ stats.completionRate }}%</text>
					<text class="stats-label">{{ $t('achievements.stats.completionRate') }}</text>
				</view>
			</view>
		</view>
		
		<!-- 加载状态 -->
		<view class="loading-container" v-if="loading">
			<text class="loading-text">{{ $t('achievements.loading') }}</text>
		</view>
		
		<!-- 错误状态 -->
		<view class="error-container" v-if="hasError && !loading">
			<view class="error-icon">⚠️</view>
			<text class="error-text">{{ errorMessage }}</text>
			<view class="error-actions">
				<button class="retry-button" @click="loadAchievementData">{{ $t('achievements.retry') }}</button>
				<button class="retry-button" @click="testConnection">{{ $t('achievements.testConnection') }}</button>
			</view>
		</view>
		
		<!-- 成就列表 -->
		<view class="achievements-list" v-if="!hasError && !loading">
			<!-- 成就项目 -->
			<view 
				class="achievement-item" 
				v-for="(achievement, index) in achievements" 
				:key="achievement.id"
			>
				<view class="achievement-icon">
					<view class="icon-badge" :class="getAchievementStatusClass(achievement)">
						<text class="icon-text">{{ getAchievementIcon(achievement) }}</text>
					</view>
				</view>
				<view class="achievement-content">
					<text class="achievement-title">{{ achievement.name }}</text>
					<text class="achievement-desc">{{ achievement.description }}</text>
					<view class="achievement-meta">
						<text class="achievement-date">
							{{ achievement.completedAt ? formatTimeAgo(achievement.completedAt) : $t('achievements.status.locked') }}
						</text>
						<view class="achievement-status" :class="getAchievementStatusClass(achievement)">
							<text class="status-text">{{ getAchievementStatusText(achievement) }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 空状态 -->
			<view class="empty-state" v-if="achievements.length === 0">
				<text class="empty-icon">🏆</text>
				<text class="empty-text">{{ $t('achievements.empty') }}</text>
			</view>
		</view>
		
		<!-- 浮动操作按钮 -->
		<!-- <view class="fab-button" @click="addAchievement">
			<text class="fab-icon">+</text>
		</view> -->
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'AchievementsPage',
		data() {
			return {
				themeStore: useThemeStore(),
				userId: 1000100, // 默认用户ID，从页面参数获取
				achievements: [], // 成就数据列表
				stats: {
					totalAchievements: 0,
					completedAchievements: 0,
					completionRate: 0
				},
				loading: false,
				hasError: false,
				errorMessage: ''
			}
		},
		onLoad(options) {
			// 初始化主题
			this.themeStore.init();
			this.themeStore.applyTheme();
			
			// 接收页面参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('接收到的用户ID:', this.userId);
			}
			// 页面加载时获取数据
			this.loadAchievementData();
		},
		onShow() {
			this.themeStore.applyTheme();
		},
		methods: {
			// 加载成就数据
			async loadAchievementData() {
				try {
					console.log('=== 开始加载成就数据 ===');
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `${this.$baseUrl}/api/achievement/data?userId=${this.userId}`);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					// 并行请求成就数据和统计信息
					const [achievementsResponse, statsResponse] = await Promise.all([
						uni.request({
							url: `${this.$baseUrl}/api/achievement/data?userId=${this.userId}`,
							method: 'GET',
							header: {
								'Content-Type': 'application/json'
							}
						}),
						uni.request({
							url: `${this.$baseUrl}/api/achievement/stats?userId=${this.userId}`,
							method: 'GET',
							header: {
								'Content-Type': 'application/json'
							}
						})
					]);
					
					console.log('=== 成就数据API响应详情 ===');
					console.log('成就数据状态码:', achievementsResponse.statusCode);
					console.log('成就数据响应:', achievementsResponse.data);
					
					console.log('=== 统计信息API响应详情 ===');
					console.log('统计信息状态码:', statsResponse.statusCode);
					console.log('统计信息响应:', statsResponse.data);
					
					// 处理成就数据响应
					if (achievementsResponse.statusCode === 200 && achievementsResponse.data.success) {
						this.achievements = achievementsResponse.data.data || [];
						console.log('成功加载成就数据，共', this.achievements.length, '条');
						
						// 显示成功提示
						uni.showToast({
							title: `加载了${this.achievements.length}个成就`,
							icon: 'success',
							duration: 2000
						});
					} else {
						console.error('加载成就数据失败:', achievementsResponse.data.message);
						this.hasError = true;
						this.errorMessage = achievementsResponse.data.message || '加载成就数据失败';
					}
					
					// 处理统计信息响应
					if (statsResponse.statusCode === 200 && statsResponse.data.success) {
						this.stats = statsResponse.data.data || this.stats;
						console.log('成功加载统计信息:', this.stats);
					} else {
						console.error('加载统计信息失败:', statsResponse.data.message);
					}
					
				} catch (error) {
					console.error('加载成就数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 刷新数据
			async refreshData() {
				console.log('开始刷新成就数据');
				this.hasError = false;
				this.errorMessage = '';
				
				try {
					await this.loadAchievementData();
					uni.showToast({
						title: '刷新成功',
						icon: 'success'
					});
				} catch (error) {
					console.error('刷新数据失败:', error);
					this.hasError = true;
					this.errorMessage = '刷新数据失败，请重试';
				}
			},
			
			// 测试后端连接
			async testConnection() {
				try {
					console.log('=== 测试成就后端连接 ===');
					const response = await uni.request({
						url: '${this.$baseUrl}/api/achievement/test',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('测试连接响应:', response);
					
					if (response.statusCode === 200) {
						uni.showToast({
							title: '后端连接正常',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: '后端连接失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('测试连接异常:', error);
					uni.showToast({
						title: '连接异常',
						icon: 'error'
					});
				}
			},
			
			// 格式化时间显示
			formatTimeAgo(date) {
				if (!date) return '刚刚';
				
				const now = new Date();
				const time = new Date(date);
				const diff = now - time;
				
				const minutes = Math.floor(diff / (1000 * 60));
				const hours = Math.floor(diff / (1000 * 60 * 60));
				const days = Math.floor(diff / (1000 * 60 * 60 * 24));
				
				if (minutes < 1) return '刚刚';
				if (minutes < 60) return minutes + '分钟前';
				if (hours < 24) return hours + '小时前';
				if (days < 7) return days + '天前';
				return '一周前';
			},
			
			// 获取成就图标
			getAchievementIcon(achievement) {
				if (achievement.iconUrl) {
					return achievement.iconUrl;
				}
				// 根据成就状态返回默认图标
				if (achievement.status !== undefined) {
					switch (achievement.status) {
						case 0: return '🔒';      // 未完成
						case 1: return '⏳';      // 完成中
						case 2: return '🏆';      // 已完成
						default: return '🔒';
					}
				}
				// 兼容旧数据
				return achievement.isCompleted ? '🏆' : '🔒';
			},
			
			// 获取成就状态文本
			getAchievementStatusText(achievement) {
				if (achievement.statusText) {
					return achievement.statusText;
				}
				// 兼容旧数据
				return achievement.isCompleted ? '已完成' : '未完成';
			},
			
			// 获取成就状态类名
			getAchievementStatusClass(achievement) {
				if (achievement.status !== undefined) {
					// 新状态：0=未完成, 1=完成中, 2=已完成
					switch (achievement.status) {
						case 0: return 'locked';      // 未完成
						case 1: return 'in-progress'; // 完成中
						case 2: return 'completed';   // 已完成
						default: return 'locked';
					}
				}
				// 兼容旧数据
				return achievement.isCompleted ? 'completed' : 'locked';
			},
			
			goBack() {
				uni.navigateBack();
			},
			addAchievement() {
				uni.showToast({
					title: '添加成就功能',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.achievements-page {
		min-height: 100vh;
		background: linear-gradient(135deg, var(--color-bg) 0%, var(--color-bg-weak) 100%);
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
		color: var(--color-text);
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
		background-color: var(--color-text);
		border-radius: 4rpx;
	}
	
	/* 头部导航 */
	.header {
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		margin-bottom: 24rpx;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: var(--color-bg-weak);
		border-radius: 24rpx;
		backdrop-filter: blur(10rpx);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 36rpx;
		color: var(--color-text);
		font-weight: 700;
		text-shadow: 0 2rpx 4rpx var(--color-border);
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 成就统计卡片 */
	.stats-card {
		margin: 0 32rpx 32rpx;
		background: var(--color-card);
		border-radius: 24rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 32rpx var(--color-divider);
		backdrop-filter: blur(10rpx);
	}
	
	.stats-content {
		display: flex;
		align-items: center;
		justify-content: space-around;
	}
	
	.stats-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}
	
	.stats-number {
		font-size: 48rpx;
		color: var(--color-primary);
		font-weight: 800;
	}
	
	.stats-label {
		font-size: 24rpx;
		color: var(--color-text-secondary);
		font-weight: 500;
	}
	
	.stats-divider {
		width: 2rpx;
		height: 60rpx;
		background: linear-gradient(to bottom, transparent, var(--color-border), transparent);
	}
	
	/* 成就列表 */
	.achievements-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.achievement-item {
		display: flex;
		align-items: center;
		gap: 24rpx;
		background: var(--color-card);
		border-radius: 20rpx;
		padding: 24rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 20rpx var(--color-divider);
		backdrop-filter: blur(10rpx);
		transition: all 0.3s ease;
	}
	
	.achievement-item:active {
		transform: scale(0.98);
		box-shadow: 0 2rpx 10rpx var(--color-border);
	}
	
	.achievement-icon {
		flex-shrink: 0;
	}
	
	.icon-badge {
		width: 80rpx;
		height: 80rpx;
		background: linear-gradient(135deg, var(--danger-bg), var(--color-primary));
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 16rpx var(--color-divider);
	}
	
	.icon-badge.locked {
		background: linear-gradient(135deg, var(--color-border), var(--color-divider));
		box-shadow: 0 4rpx 16rpx var(--color-divider);
	}
	
	.icon-text {
		font-size: 32rpx;
	}
	
	.achievement-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.achievement-title {
		font-size: 28rpx;
		color: var(--color-text);
		font-weight: 600;
		line-height: 1.4;
	}
	
	.achievement-desc {
		font-size: 24rpx;
		color: var(--color-text-secondary);
		line-height: 1.4;
	}
	
	.achievement-meta {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 8rpx;
	}
	
	.achievement-date {
		font-size: 22rpx;
		color: var(--color-text-secondary);
	}
	
	.achievement-status {
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.achievement-status.completed {
		background: linear-gradient(135deg, var(--color-primary), var(--color-primary));
	}
	
	.achievement-status.in-progress {
		background: linear-gradient(135deg, var(--danger-bg), var(--danger-bg));
	}
	
	.achievement-status.locked {
		background: linear-gradient(135deg, var(--color-border), var(--color-divider));
	}
	
	.status-text {
		font-size: 20rpx;
		color: var(--color-on-primary);
		font-weight: 500;
	}
	
	.achievement-status.locked .status-text {
		color: var(--color-text-secondary);
	}
	
	/* 浮动操作按钮 */
	.fab-button {
		position: fixed;
		bottom: 120rpx;
		right: 32rpx;
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, var(--color-primary), var(--danger-bg));
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx var(--color-divider);
		transition: all 0.3s ease;
	}
	
	.fab-button:active {
		transform: scale(0.95);
		box-shadow: 0 4rpx 16rpx var(--color-border);
	}
	
	.fab-icon {
		font-size: 48rpx;
		color: var(--color-on-primary);
		font-weight: 300;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-divider);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx;
		background-color: var(--color-bg-weak);
		border-radius: 24rpx;
		margin: 0 32rpx 32rpx;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary);
	}
	
	/* 错误状态样式 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 40rpx;
		background-color: var(--color-bg-weak);
		border-radius: 24rpx;
		margin: 0 32rpx 32rpx;
		min-height: 400rpx;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: var(--danger-bg);
		text-align: center;
		margin-bottom: 40rpx;
		line-height: 1.5;
	}
	
	.error-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.retry-button {
		background-color: var(--color-primary);
		color: var(--color-on-primary);
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 26rpx;
		font-weight: 600;
	}
	
	.retry-button:active {
		opacity: 0.9;
		transform: scale(0.95);
	}
	
	/* 空状态样式 */
	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 40rpx;
		background-color: var(--color-bg-weak);
		border-radius: 24rpx;
		margin: 0 32rpx 32rpx;
		min-height: 400rpx;
	}
	
	.empty-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		opacity: 0.6;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: var(--color-text-secondary);
		text-align: center;
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.achievement-item {
			padding: 20rpx;
			margin-bottom: 16rpx;
		}
		
		.icon-badge {
			width: 70rpx;
			height: 70rpx;
		}
		
		.achievement-title {
			font-size: 26rpx;
		}
		
		.achievement-desc {
			font-size: 22rpx;
		}
	}
</style>