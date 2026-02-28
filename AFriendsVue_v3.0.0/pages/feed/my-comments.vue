<template>
	<view class="my-comments-page" :class="themeClass">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal">
					<view class="bar bar-1"></view>
					<view class="bar bar-2"></view>
					<view class="bar bar-3"></view>
					<view class="bar bar-4"></view>
				</view>
				<view class="wifi">📶</view>
				<view class="battery">
					<view class="battery-body">
						<view class="battery-level"></view>
					</view>
					<view class="battery-tip"></view>
				</view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">我的评论</text>
			</view>
		</view>
		
		<!-- 加载状态 -->
		<view v-if="loading" class="loading-container">
			<text class="loading-text">加载中...</text>
		</view>
		
		<!-- 错误提示 -->
		<view v-if="hasError" class="error-container">
			<text class="error-text">{{ errorMessage }}</text>
			<view class="retry-button" @click="loadComments">
				<text class="retry-text">重试</text>
			</view>
		</view>
		
		<!-- 评论列表 -->
		<view v-if="!loading && !hasError" class="comments-container">
			<!-- 空状态 -->
			<view v-if="comments.length === 0" class="empty-container">
				<text class="empty-text">暂无评论记录</text>
			</view>
			
			<!-- 评论项 -->
			<view v-for="(comment, index) in comments" :key="index" class="comment-item">
				<view class="comment-left">
					<view class="user-avatar">
						<image v-if="comment.userAvatar" :src="comment.userAvatar" class="avatar-image" mode="aspectFill"></image>
						<view v-else class="cat-avatar">
							<view class="cat-body"></view>
							<view class="cat-face">
								<view class="cat-eyes">
									<view class="cat-eye"></view>
									<view class="cat-eye"></view>
								</view>
							</view>
						</view>
					</view>
					<view class="comment-info">
						<text class="username">{{ comment.username }}</text>
						<text class="timestamp">{{ comment.timeAgo }}</text>
						<text class="comment-text">{{ comment.commentText }}</text>
						<view class="action-button">
							<text class="like-icon">👍</text>
							<text v-if="comment.likeCount > 0" class="like-count">{{ comment.likeCount }}</text>
						</view>
					</view>
				</view>
				<view class="comment-right" @click="goToContent(comment)">
					<view class="content-thumbnail">
						<image v-if="comment.contentThumbnail" :src="comment.contentThumbnail" class="thumbnail-image" mode="aspectFill"></image>
						<view v-else class="thumbnail-placeholder"></view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-navigation">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToChatList">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAIChat">
				<view class="ai-tab">
					<text class="ai-text">AI</text>
				</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-avatar" @click="goToProfile">
				<image class="avatar-small" src="" mode="aspectFill"></image>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js'
	
	export default {
		name: 'MyComments',
		data() {
			return {
				userId: null,
				comments: [],
				loading: false,
				hasError: false,
				errorMessage: ''
			}
		},
		computed: {
			themeClass() {
				const themeStore = useThemeStore()
				return themeStore.themeClass
			}
		},
		onLoad(options) {
			// 从 URL 参数获取 userId
			if (options.userId) {
				this.userId = parseInt(options.userId);
			} else {
				// 默认使用当前用户ID（可以从全局状态或本地存储获取）
				this.userId = 1000100; // 临时默认值
			}
			this.loadComments();
		},
		methods: {
			// 加载评论数据
			async loadComments() {
				if (!this.userId) {
					this.hasError = true;
					this.errorMessage = '用户ID无效';
					return;
				}
				
				try {
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					console.log('开始加载评论历史，用户ID:', this.userId);
					console.log('请求URL:', `${this.$baseUrl}/api/user/comments?userId=${this.userId}`);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user/comments?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('API响应:', response);
					
					if (response.statusCode === 200 && response.data) {
						if (response.data.success) {
							this.comments = response.data.data || [];
							console.log('加载评论历史成功，数量:', this.comments.length);
						} else {
							this.hasError = true;
							this.errorMessage = response.data.message || '获取评论历史失败';
						}
					} else {
						throw new Error('请求失败，状态码: ' + response.statusCode);
					}
				} catch (error) {
					console.error('加载评论历史失败:', error);
					this.hasError = true;
					this.errorMessage = '加载评论历史失败，请重试';
					uni.showToast({
						title: '加载失败',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			// 跳转到内容详情页
			goToContent(comment) {
				if (!comment.contentId || !comment.contentType) {
					return;
				}
				
				let url = '';
				if (comment.contentType === 1) {
					// 文字动态
					url = `/pages/feed/post-detail?postId=${comment.contentId}&postType=text`;
				} else if (comment.contentType === 2) {
					// 图片动态
					url = `/pages/feed/post-detail?postId=${comment.contentId}&postType=image`;
				} else if (comment.contentType === 3) {
					// 小说
					url = `/pages/feed/novel-detail?novelId=${comment.contentId}`;
				}
				
				if (url) {
					uni.navigateTo({
						url: url
					});
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
			goToChatList() {
				uni.navigateTo({
					url: '/pages/chat/chat-list'
				});
			},
			goToAIChat() {
				uni.navigateTo({
					url: '/pages/chat/chat'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
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
	.my-comments-page {
		width: 100%;
		min-height: 100vh;
		background-color: var(--color-bg, #ffffff);
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 88rpx;
		background-color: var(--color-bg, #ffffff);
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
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
	
	.signal {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.bar {
		width: 6rpx;
		background-color: var(--color-text, #000000);
		border-radius: 2rpx;
	}
	
	.bar-1 {
		height: 8rpx;
	}
	
	.bar-2 {
		height: 12rpx;
	}
	
	.bar-3 {
		height: 16rpx;
	}
	
	.bar-4 {
		height: 20rpx;
	}
	
	.wifi {
		font-size: 24rpx;
		color: var(--color-text, #000000);
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid var(--color-text, #000000);
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: var(--color-text, #000000);
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: var(--color-bg, #ffffff);
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		position: relative;
		z-index: 10;
	}
	
	.nav-left {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text-secondary, #999);
		font-weight: bold;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.nav-title {
		font-size: 36rpx;
		font-weight: bold;
		color: var(--color-text, #333);
	}
	
	/* 加载状态 */
	.loading-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999);
	}
	
	/* 错误提示 */
	.error-container {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 40rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999);
		margin-bottom: 40rpx;
		text-align: center;
	}
	
	.retry-button {
		padding: 20rpx 40rpx;
		background-color: var(--color-primary, #FF69B4);
		border-radius: 8rpx;
	}
	
	.retry-text {
		font-size: 28rpx;
		color: #ffffff;
	}
	
	/* 空状态 */
	.empty-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999);
	}
	
	/* 评论容器 */
	.comments-container {
		flex: 1;
		background-color: var(--color-bg-weak, #f8f8f8);
		padding: 20rpx 0;
		padding-bottom: 140rpx; /* 为底部导航栏留出空间 */
	}
	
	/* 评论项 */
	.comment-item {
		display: flex;
		align-items: flex-start;
		padding: 30rpx 40rpx;
		background-color: var(--color-card, #ffffff);
		margin-bottom: 20rpx;
	}
	
	.comment-left {
		flex: 1;
		display: flex;
		align-items: flex-start;
		gap: 20rpx;
	}
	
	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		flex-shrink: 0;
		border-radius: 50%;
		overflow: hidden;
	}
	
	.avatar-image {
		width: 100%;
		height: 100%;
		border-radius: 50%;
	}
	
	.cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.cat-body {
		width: 60rpx;
		height: 50rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 30rpx;
		position: absolute;
		top: 15rpx;
		left: 10rpx;
	}
	
	.cat-body.theme-dark {
		background: linear-gradient(135deg, #CC8400, #CC7000);
	}
	
	.cat-face {
		position: absolute;
		top: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 40rpx;
		height: 30rpx;
	}
	
	.cat-eyes {
		display: flex;
		justify-content: space-between;
		gap: 8rpx;
	}
	
	.cat-eye {
		width: 6rpx;
		height: 6rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.comment-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.username {
		font-size: 28rpx;
		color: var(--color-text, #333);
		font-weight: 600;
	}
	
	.timestamp {
		font-size: 24rpx;
		color: var(--color-text-secondary, #999);
	}
	
	.comment-text {
		font-size: 28rpx;
		color: var(--color-text, #333);
		line-height: 1.4;
		margin: 8rpx 0;
	}
	
	.action-button {
		margin-top: 8rpx;
	}
	
	.like-icon {
		font-size: 32rpx;
		color: var(--color-text-secondary, #666);
		margin-right: 8rpx;
	}
	
	.like-count {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666);
	}
	
	.comment-right {
		width: 120rpx;
		height: 120rpx;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.content-thumbnail {
		width: 100%;
		height: 100%;
		border-radius: 8rpx;
		overflow: hidden;
	}
	
	.thumbnail-image {
		width: 100%;
		height: 100%;
	}
	
	.thumbnail-placeholder {
		width: 100%;
		height: 100%;
		background-color: var(--color-bg-weak, #f0f0f0);
		border-radius: 8rpx;
		opacity: 0.7;
	}
	
	.cat-thumbnail {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	.cat-thumbnail.theme-dark {
		background: linear-gradient(135deg, #CC92A8, #CC99A2);
	}
	
	/* 底部导航栏样式 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: var(--color-card, #FFFFFF);
		border-top: 1rpx solid var(--color-border, #F0F0F0);
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0 32rpx;
		z-index: 100;
	}
	
	.nav-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.nav-item:active {
		background-color: var(--color-bg-weak, #F5F5F5);
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: var(--color-primary, #FF69B4);
		font-weight: 600;
	}
	
	.ai-tab {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, var(--color-primary, #FF69B4), #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.ai-text {
		font-size: 20rpx;
		color: var(--color-on-primary, #FFFFFF);
		font-weight: 600;
	}
	
	.nav-avatar {
		width: 48rpx;
		height: 48rpx;
	}
	
	.avatar-small {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
	}
	
	.avatar-small.theme-dark {
		background: linear-gradient(135deg, #CC8400, #CC7000);
	}
	
	.mini-cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.mini-cat-body {
		width: 40rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 20rpx;
		position: absolute;
		top: 8rpx;
		left: 4rpx;
	}
	
	.mini-cat-body.theme-dark {
		background: linear-gradient(135deg, #CC8400, #CC7000);
	}
	
	.mini-cat-face {
		position: absolute;
		top: 10rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 24rpx;
		height: 20rpx;
	}
	
	.mini-cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 4rpx;
	}
	
	.mini-cat-eye {
		width: 3rpx;
		height: 3rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 140rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 120rpx;
		height: 6rpx;
		background-color: var(--color-text, #000);
		border-radius: 3rpx;
	}
</style>
