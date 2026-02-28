<template>
	<view class="container" :class="{ 'dark-mode': isDarkMode }">
		<!-- 头部导航 -->
		<view class="header">
			<view class="header-left" @click="goBack">
				<text class="back-icon">←</text>
			</view>
			<text class="header-title">{{ $t('browsingHistory.title') }}</text>
			<view class="header-right" @click="clearAllHistory">
				<text class="clear-text">{{ $t('browsingHistory.clear') }}</text>
			</view>
		</view>
		
		<!-- 内容区域 -->
		<view class="content">
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<view class="loading-spinner"></view>
				<text class="loading-text">{{ $t('browsingHistory.loading') }}</text>
			</view>
			
			<!-- 空状态 -->
			<view class="empty-container" v-if="!loading && historyList.length === 0">
				<view class="empty-icon">📜</view>
				<text class="empty-text">{{ $t('browsingHistory.empty') }}</text>
				<text class="empty-desc">{{ $t('browsingHistory.emptyDesc') }}</text>
			</view>
			
			<!-- 浏览记录列表 -->
			<view class="history-list" v-if="!loading && historyList.length > 0">
				<!-- 日期分组 -->
				<view v-for="(group, date) in groupedHistory" :key="date">
					<view class="date-header">
						<text class="date-text">{{ formatDate(date) }}</text>
					</view>
					
					<!-- 记录项 -->
					<view class="history-item" v-for="(item, index) in group" :key="index" @click="handleItemClick(item)">
						<view class="item-left">
							<!-- 缩略图 -->
							<view class="item-thumbnail" :style="{backgroundColor: getThumbnailColor(item.type)}">
								<text class="thumbnail-icon">{{ getItemIcon(item.type) }}</text>
							</view>
						</view>
						<view class="item-middle">
							<text class="item-title">{{ item.title }}</text>
							<text class="item-subtitle">{{ item.subtitle }}</text>
						</view>
						<view class="item-right">
							<view class="delete-btn" @click.stop="deleteHistory(item.id)">
								<text class="delete-icon">×</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError">
				<text class="error-icon">⚠️</text>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadHistoryData">{{ $t('browsingHistory.retry') }}</button>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-nav">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">{{ $t('userProfile.home') }}</text>
			</view>
			<view class="nav-item">
				<text class="nav-text">{{ $t('userProfile.messages') }}</text>
			</view>
			<view class="nav-item active" @click="goToAIList">
				<view class="ai-icon">AI</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">{{ $t('userProfile.friends') }}</text>
			</view>
			<view class="nav-item" @click="goToProfile">
				<view class="nav-avatar">
					<view class="mini-cat-avatar">
						<view class="mini-cat-body"></view>
						<view class="mini-cat-face">
							<view class="mini-cat-eyes">
								<view class="mini-cat-eye"></view>
								<view class="mini-cat-eye"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
import { useThemeStore } from '@/stores/theme'

export default {
	data() {
		return {
			userId: 1000100,
			historyList: [],
			loading: false,
			hasError: false,
			errorMessage: ''
		};
	},
	computed: {
		isDarkMode() {
			const themeStore = useThemeStore()
			return themeStore.isDarkMode
		},
		groupedHistory() {
			const groups = {};
			this.historyList.forEach(item => {
				const date = item.browseDate.split(' ')[0];
				if (!groups[date]) {
					groups[date] = [];
				}
				groups[date].push(item);
			});
			const sortedGroups = {};
			Object.keys(groups).sort((a, b) => new Date(b) - new Date(a)).forEach(date => {
				sortedGroups[date] = groups[date];
			});
			return sortedGroups;
		}
	},
	onLoad(options) {
		if (options.userId) {
			this.userId = parseInt(options.userId);
		}
		this.loadHistoryData();
	},
	methods: {
		async loadHistoryData() {
			try {
				console.log('加载浏览记录，用户ID:', this.userId);
				this.loading = true;
				this.hasError = false;
				this.errorMessage = '';
				
				const response = await this.$request.get(`/api/user/history?userId=${this.userId}`);
				
				if (response.statusCode === 200 && response.data.success) {
					this.historyList = response.data.data || [];
					console.log('浏览记录加载成功，共', this.historyList.length, '条');
				} else {
					this.hasError = true;
					this.errorMessage = response.data.message || this.$t('browsingHistory.error');
				}
			} catch (error) {
				console.error('加载浏览记录异常:', error);
				this.hasError = true;
				this.errorMessage = this.$t('browsingHistory.networkError');
			} finally {
				this.loading = false;
			}
		},
		
		formatDate(dateStr) {
			const date = new Date(dateStr);
			const today = new Date();
			const yesterday = new Date(today);
			yesterday.setDate(yesterday.getDate() - 1);
			
			if (date.toDateString() === today.toDateString()) {
				return this.$t('browsingHistory.today');
			} else if (date.toDateString() === yesterday.toDateString()) {
				return this.$t('browsingHistory.yesterday');
			} else {
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
			}
		},
		
		getThumbnailColor(type) {
			const colorMap = {
				'novel': '#FF69B4',
				'post': '#4169E1',
				'image': '#FFD700',
				'painting': '#32CD32'
			};
			return colorMap[type] || '#999';
		},
		
		getItemIcon(type) {
			const iconMap = {
				'novel': '📚',
				'post': '💬',
				'image': '🖼️',
				'painting': '🎨'
			};
			return iconMap[type] || '📄';
		},
		
		handleItemClick(item) {
			console.log('点击浏览记录:', item);
			if (item.type === 'novel') {
				uni.navigateTo({
					url: `/pages/feed/novel-detail?id=${item.targetId}&title=${encodeURIComponent(item.title)}`
				});
			} else if (item.type === 'post' || item.type === 'image' || item.type === 'painting') {
				uni.navigateTo({
					url: `/pages/feed/post-detail?postId=${item.targetId}&isOwnPost=${item.isOwn}`
				});
			}
		},
		
		async deleteHistory(historyId) {
			try {
				console.log('删除浏览记录:', historyId);
				
				const response = await this.$request.post('/api/user/history/delete', {
					userId: this.userId,
					historyId: historyId
				});
				
				if (response.statusCode === 200 && response.data.success) {
					this.historyList = this.historyList.filter(item => item.id !== historyId);
					uni.showToast({
						title: this.$t('browsingHistory.deleteSuccess'),
						icon: 'success'
					});
				} else {
					uni.showToast({
						title: response.data.message || this.$t('browsingHistory.deleteFailed'),
						icon: 'error'
					});
				}
			} catch (error) {
				console.error('删除浏览记录异常:', error);
				uni.showToast({
					title: this.$t('browsingHistory.networkError'),
					icon: 'error'
				});
			}
		},
		
		clearAllHistory() {
			uni.showModal({
				title: this.$t('common.confirm'),
				content: this.$t('browsingHistory.clearConfirm'),
				confirmText: this.$t('browsingHistory.clearButton'),
				cancelText: this.$t('common.cancel'),
				success: async (res) => {
					if (res.confirm) {
						try {
							console.log('清空所有浏览记录');
							
							const response = await this.$request.post('/api/user/history/clear', {
								userId: this.userId
							});
							
							if (response.statusCode === 200 && response.data.success) {
								this.historyList = [];
								uni.showToast({
									title: this.$t('browsingHistory.clearSuccess'),
									icon: 'success'
								});
							} else {
								uni.showToast({
									title: response.data.message || this.$t('browsingHistory.clearFailed'),
									icon: 'error'
								});
							}
						} catch (error) {
							console.error('清空浏览记录异常:', error);
							uni.showToast({
								title: this.$t('browsingHistory.networkError'),
								icon: 'error'
							});
						}
					}
				}
			});
		},
		
		goBack() {
			uni.navigateBack({ delta: 1 });
		},
		
		goToHome() {
			uni.navigateTo({ url: '/pages/feed/chat-feed' });
		},
		
		goToAIList() {
			uni.navigateTo({ url: '/pages/ai/ai-chat' });
		},
		
		goToFriendList() {
			uni.navigateTo({ url: '/pages/chat/friend-list' });
		},
		
		goToProfile() {
			uni.navigateTo({ url: `/pages/feed/user-profile?userId=${this.userId}` });
		}
	}
};
</script>

<style scoped>
.container {
	position: relative;
	min-height: 100vh;
	background-color: var(--bg-color, #F5F5F5);
	transition: background-color 0.3s;
}

.dark-mode {
	--bg-color: #1a1a1a;
	--card-bg: #2a2a2a;
	--text-primary: #ffffff;
	--text-secondary: #b0b0b0;
	--border-color: #3a3a3a;
}

.container:not(.dark-mode) {
	--bg-color: #F5F5F5;
	--card-bg: #FFFFFF;
	--text-primary: #333333;
	--text-secondary: #666666;
	--border-color: #F0F0F0;
}

/* 头部导航 */
.header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 120rpx;
	background-color: var(--card-bg);
	padding: 0 32rpx;
	border-bottom: 1rpx solid var(--border-color);
}

.header-left, .header-right {
	width: 80rpx;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.back-icon {
	font-size: 36rpx;
	color: var(--text-primary);
}

.header-title {
	font-size: 36rpx;
	color: var(--text-primary);
	font-weight: 600;
}

.clear-text {
	font-size: 32rpx;
	color: #FF69B4;
	font-weight: 500;
}

/* 内容区域 */
.content {
	flex: 1;
	padding: 24rpx;
	margin-bottom: 140rpx;
}

/* 加载状态 */
.loading-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
}

.loading-spinner {
	width: 60rpx;
	height: 60rpx;
	border: 4rpx solid var(--border-color);
	border-top: 4rpx solid #FF69B4;
	border-radius: 50%;
	animation: spin 1s linear infinite;
	margin-bottom: 20rpx;
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

.loading-text {
	font-size: 28rpx;
	color: var(--text-secondary);
}

/* 空状态 */
.empty-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
	text-align: center;
}

.empty-icon {
	font-size: 120rpx;
	margin-bottom: 30rpx;
	opacity: 0.5;
}

.empty-text {
	font-size: 32rpx;
	color: var(--text-primary);
	margin-bottom: 16rpx;
}

.empty-desc {
	font-size: 28rpx;
	color: var(--text-secondary);
}

/* 浏览记录列表 */
.history-list {
	padding-bottom: 40rpx;
}

.date-header {
	padding: 20rpx 0;
	margin-top: 20rpx;
}

.date-text {
	font-size: 28rpx;
	color: var(--text-secondary);
	font-weight: 500;
}

.history-item {
	display: flex;
	align-items: center;
	background-color: var(--card-bg);
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
	box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.item-left {
	margin-right: 24rpx;
}

.item-thumbnail {
	width: 96rpx;
	height: 96rpx;
	border-radius: 12rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.thumbnail-icon {
	font-size: 48rpx;
}

.item-middle {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.item-title {
	font-size: 32rpx;
	color: var(--text-primary);
	font-weight: 500;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.item-subtitle {
	font-size: 26rpx;
	color: var(--text-secondary);
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.item-right {
	margin-left: 16rpx;
}

.delete-btn {
	width: 48rpx;
	height: 48rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: rgba(255, 0, 0, 0.1);
	border-radius: 50%;
}

.delete-icon {
	font-size: 36rpx;
	color: #FF4444;
	font-weight: bold;
}

/* 错误状态 */
.error-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
	text-align: center;
}

.error-icon {
	font-size: 120rpx;
	margin-bottom: 30rpx;
}

.error-text {
	font-size: 28rpx;
	color: var(--text-secondary);
	margin-bottom: 40rpx;
}

.retry-button {
	background-color: #FF69B4;
	color: #FFFFFF;
	border: none;
	border-radius: 48rpx;
	padding: 20rpx 60rpx;
	font-size: 28rpx;
}

/* 底部导航栏 */
.bottom-nav {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	height: 120rpx;
	background-color: var(--card-bg);
	border-top: 1rpx solid var(--border-color);
	display: flex;
	align-items: center;
	justify-content: space-around;
	padding-bottom: env(safe-area-inset-bottom);
}

.nav-item {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100%;
}

.nav-text {
	font-size: 24rpx;
	color: var(--text-secondary);
}

.nav-item.active .nav-text,
.nav-item.active .ai-icon {
	color: #FF69B4;
}

.ai-icon {
	font-size: 28rpx;
	font-weight: bold;
	color: var(--text-secondary);
}

.nav-avatar {
	width: 56rpx;
	height: 56rpx;
}

.mini-cat-avatar {
	width: 100%;
	height: 100%;
	position: relative;
}

.mini-cat-body {
	width: 100%;
	height: 100%;
	background: linear-gradient(135deg, #FFB6C1 0%, #FF69B4 100%);
	border-radius: 50%;
}

.mini-cat-face {
	position: absolute;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 80%;
	height: 80%;
}

.mini-cat-eyes {
	display: flex;
	justify-content: space-around;
	padding-top: 30%;
}

.mini-cat-eye {
	width: 8rpx;
	height: 8rpx;
	background-color: #333;
	border-radius: 50%;
}

/* 底部手势条 */
.home-indicator {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	height: env(safe-area-inset-bottom);
	background-color: var(--card-bg);
}
</style>
