<template>
	<view class="notification-list-page" :class="themeStore.themeClass">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<text class="back-icon">←</text>
			</view>
			<view class="title-section">
				<text class="page-title">通知</text>
				<text class="unread-count" v-if="unreadCount > 0">{{ unreadCount > 99 ? '99+' : unreadCount }}</text>
			</view>
			<view class="mark-all-button" @click="markAllAsRead" v-if="unreadCount > 0">
				<text class="mark-all-text">全部已读</text>
			</view>
			<view class="mark-all-placeholder" v-else></view>
		</view>
		
		<!-- 通知列表 -->
		<scroll-view 
			class="notification-scroll" 
			scroll-y 
			@scrolltolower="loadMore"
			refresher-enabled
			:refresher-triggered="refreshing"
			@refresherrefresh="onRefresh"
		>
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading && notifications.length === 0">
				<text class="loading-text">加载中...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading && notifications.length === 0">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadNotifications">重试</button>
				</view>
			</view>
			
			<!-- 通知列表 -->
			<view v-if="!hasError || notifications.length > 0">
				<view 
					class="notification-item" 
					v-for="(notification, index) in notifications" 
					:key="notification.messageId || index"
					:class="{ 'unread': notification.isRead === 0 }"
					@click="handleNotificationClick(notification)"
				>
					<view class="notification-avatar">
						<image 
							class="avatar-image" 
							:src="getSenderAvatar(notification.senderUserId)" 
							mode="aspectFill"
						></image>
						<view class="notification-type-badge" :class="getNotificationTypeClass(notification.relatedEntityType)">
							<text class="type-icon">{{ getNotificationTypeIcon(notification.relatedEntityType) }}</text>
						</view>
					</view>
					<view class="notification-content">
						<view class="notification-header">
							<text class="sender-name">{{ getSenderName(notification.senderUserId) }}</text>
							<text class="notification-time">{{ formatTime(notification.createdAt) }}</text>
						</view>
						<text class="notification-text">{{ formatNotificationContent(notification) }}</text>
						<view class="notification-footer" v-if="notification.relatedEntityId">
							<text class="related-hint">点击查看详情</text>
						</view>
					</view>
					<!-- 未读标识 -->
					<view class="unread-dot" v-if="notification.isRead === 0"></view>
				</view>
				
				<!-- 加载更多 -->
				<view class="load-more-container" v-if="hasMore && !loading">
					<text class="load-more-text">上拉加载更多</text>
				</view>
				
				<!-- 加载中 -->
				<view class="load-more-container" v-if="loading && notifications.length > 0">
					<text class="load-more-text">加载中...</text>
				</view>
				
				<!-- 没有更多 -->
				<view class="load-more-container" v-if="!hasMore && notifications.length > 0">
					<text class="load-more-text">没有更多了</text>
				</view>
				
				<!-- 空状态 -->
				<view class="empty-container" v-if="notifications.length === 0 && !loading && !hasError">
					<view class="empty-icon">📭</view>
					<text class="empty-text">暂无通知</text>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
import notificationPolling from '@/utils/notification-polling.js'
import { useThemeStore } from '../../store/theme.js'

export default {
	name: 'NotificationList',
	data() {
		return {
			themeStore: useThemeStore(),
			userId: null,
			notifications: [],
			loading: false,
			refreshing: false,
			hasError: false,
			errorMessage: '',
			page: 1,
			size: 20,
			hasMore: true,
			unreadCount: 0,
			baseUrl: '${this.$baseUrl}' // 建议从配置文件读取
		}
	},
	onLoad(options) {
		this.themeStore.init()
		this.themeStore.applyTheme()
		// 获取用户ID
		this.userId = uni.getStorageSync('userId') || options.userId;
		if (!this.userId) {
			uni.showToast({
				title: '请先登录',
				icon: 'none'
			});
			setTimeout(() => {
				uni.navigateBack();
			}, 1500);
			return;
		}
		
		// 加载通知列表
		this.loadNotifications();
		
		// 开始轮询未读数量
		notificationPolling.start(this.userId, (count) => {
			this.unreadCount = count;
		});
	},
	onShow() {
		this.themeStore.applyTheme()
	},
	onUnload() {
		// 页面卸载时停止轮询
		notificationPolling.stop();
	},
	methods: {
		/**
		 * 加载通知列表
		 */
		async loadNotifications(reset = false) {
			if (this.loading) return;
			
			if (reset) {
				this.page = 1;
				this.hasMore = true;
				this.notifications = [];
			}
			
			if (!this.hasMore && !reset) return;
			
			this.loading = true;
			this.hasError = false;
			
			try {
				const res = await uni.request({
					url: `${this.baseUrl}/api/notifications/list`,
					method: 'GET',
					data: {
						userId: this.userId,
						page: this.page,
						size: this.size
					}
				});
				
				if (res.statusCode === 200 && res.data.success) {
					const newNotifications = res.data.notifications || [];
					
					if (reset) {
						this.notifications = newNotifications;
					} else {
						this.notifications = [...this.notifications, ...newNotifications];
					}
					
					// 判断是否还有更多
					this.hasMore = newNotifications.length >= this.size;
					if (this.hasMore) {
						this.page++;
					}
					
					// 更新未读数量
					this.updateUnreadCount();
				} else {
					throw new Error(res.data.message || '加载失败');
				}
			} catch (error) {
				console.error('加载通知列表失败:', error);
				this.hasError = true;
				this.errorMessage = error.message || '加载失败，请重试';
				uni.showToast({
					title: this.errorMessage,
					icon: 'none'
				});
			} finally {
				this.loading = false;
				this.refreshing = false;
			}
		},
		
		/**
		 * 下拉刷新
		 */
		onRefresh() {
			this.refreshing = true;
			this.loadNotifications(true);
		},
		
		/**
		 * 加载更多
		 */
		loadMore() {
			if (!this.hasMore || this.loading) return;
			this.loadNotifications();
		},
		
		/**
		 * 更新未读数量
		 */
		async updateUnreadCount() {
			try {
				const res = await uni.request({
					url: `${this.baseUrl}/api/notifications/unread-count?userId=${this.userId}`,
					method: 'GET'
				});
				
				if (res.statusCode === 200 && res.data.success) {
					this.unreadCount = res.data.unreadCount || 0;
				}
			} catch (error) {
				console.error('获取未读数量失败:', error);
			}
		},
		
		/**
		 * 标记消息为已读
		 */
		async markAsRead(notification) {
			if (notification.isRead === 1) return;
			
			try {
				const res = await uni.request({
					url: `${this.baseUrl}/api/notifications/mark-read`,
					method: 'POST',
					data: {
						messageId: notification.messageId,
						userId: this.userId
					}
				});
				
				if (res.statusCode === 200 && res.data.success) {
					notification.isRead = 1;
					this.updateUnreadCount();
				}
			} catch (error) {
				console.error('标记已读失败:', error);
			}
		},
		
		/**
		 * 标记所有消息为已读
		 */
		async markAllAsRead() {
			if (this.unreadCount === 0) return;
			
			uni.showModal({
				title: '确认',
				content: '确定要标记所有通知为已读吗？',
				success: async (res) => {
					if (res.confirm) {
						try {
							const result = await uni.request({
								url: `${this.baseUrl}/api/notifications/mark-all-read?userId=${this.userId}`,
								method: 'POST'
							});
							
							if (result.statusCode === 200 && result.data.success) {
								// 更新所有通知状态
								this.notifications.forEach(n => {
									n.isRead = 1;
								});
								this.unreadCount = 0;
								
								uni.showToast({
									title: '已全部标记为已读',
									icon: 'success'
								});
							} else {
								throw new Error(result.data.message || '操作失败');
							}
						} catch (error) {
							console.error('标记全部已读失败:', error);
							uni.showToast({
								title: error.message || '操作失败',
								icon: 'none'
							});
						}
					}
				}
			});
		},
		
		/**
		 * 处理通知点击
		 */
		async handleNotificationClick(notification) {
			// 标记为已读
			await this.markAsRead(notification);
			
			// 根据关联实体类型跳转
			if (notification.relatedEntityId && notification.relatedEntityType) {
				let url = '';
				switch (notification.relatedEntityType) {
					case 'comment':
						url = `/pages/feed/post-detail?postId=${notification.relatedEntityId}`;
						break;
					case 'like':
					case 'favorite':
						url = `/pages/feed/post-detail?postId=${notification.relatedEntityId}`;
						break;
					case 'chat':
						url = `/pages/chat/chat?sessionId=${notification.relatedEntityId}`;
						break;
					default:
						// 显示通知详情
						uni.showModal({
							title: '通知详情',
							content: notification.messageContent,
							showCancel: false
						});
						return;
				}
				
				if (url) {
					uni.navigateTo({ url });
				}
			} else {
				// 显示通知详情
				uni.showModal({
					title: '通知详情',
					content: notification.messageContent,
					showCancel: false
				});
			}
		},
		
		/**
		 * 格式化通知内容
		 */
		formatNotificationContent(notification) {
			// 根据显示模式格式化内容
			const displayMode = uni.getStorageSync('notificationDisplayMode') || 2;
			
			switch (displayMode) {
				case 0: // 仅显示接收信息
					return '您收到一条新消息';
				case 1: // 仅显示用户名
					return `${this.getSenderName(notification.senderUserId)} 给您发送了消息`;
				case 2: // 完全显示
				default:
					return notification.messageContent || '您收到一条新消息';
			}
		},
		
		/**
		 * 获取发送者名称
		 */
		getSenderName(senderUserId) {
			if (!senderUserId) return '系统';
			return `用户${senderUserId}`;
		},
		
		/**
		 * 获取发送者头像
		 */
		getSenderAvatar(senderUserId) {
			if (!senderUserId) return '/static/default-avatar.png';
			return '/static/default-avatar.png';
		},
		
		/**
		 * 获取通知类型图标
		 */
		getNotificationTypeIcon(relatedEntityType) {
			const iconMap = {
				'comment': '💬',
				'like': '❤️',
				'favorite': '⭐',
				'mention': '@',
				'chat': '💌',
				'content_recommend': '📢',
				'user_recommend': '👤'
			};
			return iconMap[relatedEntityType] || '📬';
		},
		
		/**
		 * 获取通知类型样式类
		 */
		getNotificationTypeClass(relatedEntityType) {
			return `type-${relatedEntityType || 'default'}`;
		},
		
		/**
		 * 格式化时间
		 */
		formatTime(timestamp) {
			if (!timestamp) return '';
			
			const date = new Date(timestamp);
			const now = new Date();
			const diff = now - date;
			
			const minute = 60 * 1000;
			const hour = 60 * minute;
			const day = 24 * hour;
			
			if (diff < minute) {
				return '刚刚';
			} else if (diff < hour) {
				return `${Math.floor(diff / minute)}分钟前`;
			} else if (diff < day) {
				return `${Math.floor(diff / hour)}小时前`;
			} else if (diff < 7 * day) {
				return `${Math.floor(diff / day)}天前`;
			} else {
				return `${date.getMonth() + 1}-${date.getDate()}`;
			}
		},
		
		goBack() {
			uni.navigateBack();
		}
	}
}
</script>

<style>
.notification-list-page {
	min-height: 100vh;
	background-color: var(--color-bg);
	display: flex;
	flex-direction: column;
}

/* 头部导航 */
.header {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	z-index: 1000;
	height: 88rpx;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 32rpx;
	background-color: var(--color-card);
	border-bottom: 1rpx solid var(--color-border);
}

.back-button {
	width: 48rpx;
	height: 48rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.back-icon {
	font-size: 36rpx;
	color: var(--color-text);
}

.title-section {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 16rpx;
}

.page-title {
	font-size: 32rpx;
	color: var(--color-text);
	font-weight: 600;
}

.unread-count {
	font-size: 24rpx;
	color: var(--color-on-danger, #FFFFFF);
	background-color: var(--danger-bg, #FF3B30);
	border-radius: 20rpx;
	padding: 4rpx 12rpx;
	min-width: 40rpx;
	text-align: center;
}

.mark-all-button {
	padding: 8rpx 16rpx;
}

.mark-all-text {
	font-size: 26rpx;
	color: var(--color-primary);
}

.mark-all-placeholder {
	width: 100rpx;
}

/* 滚动区域 */
.notification-scroll {
	flex: 1;
	margin-top: 88rpx;
}

/* 通知项 */
.notification-item {
	display: flex;
	align-items: flex-start;
	gap: 24rpx;
	padding: 32rpx;
	background-color: var(--color-card);
	border-bottom: 1rpx solid var(--color-border);
	position: relative;
}

.notification-item.unread {
	background-color: var(--color-bg-weak);
}

.notification-avatar {
	position: relative;
	flex-shrink: 0;
}

.avatar-image {
	width: 80rpx;
	height: 80rpx;
	border-radius: 40rpx;
	background-color: var(--color-bg-weak);
}

.notification-type-badge {
	position: absolute;
	bottom: -4rpx;
	right: -4rpx;
	width: 32rpx;
	height: 32rpx;
	background-color: var(--color-card);
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.notification-list-page.theme-dark .notification-type-badge {
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.45);
}

.type-icon {
	font-size: 20rpx;
}

.notification-content {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.notification-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.sender-name {
	font-size: 28rpx;
	color: var(--color-text);
	font-weight: 600;
}

.notification-time {
	font-size: 22rpx;
	color: var(--color-text-secondary);
}

.notification-text {
	font-size: 26rpx;
	color: var(--color-text-secondary);
	line-height: 1.5;
}

.notification-footer {
	margin-top: 8rpx;
}

.related-hint {
	font-size: 22rpx;
	color: var(--color-text-secondary);
}

.unread-dot {
	position: absolute;
	top: 32rpx;
	right: 32rpx;
	width: 16rpx;
	height: 16rpx;
	background-color: var(--danger-bg, #FF3B30);
	border-radius: 50%;
}

/* 加载状态 */
.loading-container,
.error-container,
.empty-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 120rpx 40rpx;
}

.loading-text,
.error-text,
.empty-text {
	font-size: 28rpx;
	color: var(--color-text-secondary);
	text-align: center;
}

.error-icon,
.empty-icon {
	font-size: 120rpx;
	margin-bottom: 32rpx;
	opacity: 0.6;
}

.error-actions {
	margin-top: 40rpx;
}

.retry-button {
	background-color: var(--color-primary);
	color: var(--color-on-primary);
	border: none;
	border-radius: 24rpx;
	padding: 16rpx 32rpx;
	font-size: 26rpx;
}

.load-more-container {
	padding: 32rpx;
	text-align: center;
}

.load-more-text {
	font-size: 24rpx;
	color: var(--color-text-secondary);
}
</style>

