<!--
 * @description 黑名单管理页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示已拉黑的用户列表
 * - 支持解除拉黑操作
 * - 支持搜索黑名单用户
 * - 与隐私设置页面关联
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 响应式布局
 * - 用户友好的交互体验
-->

<template>
	<view class="blocked-users-page" :class="[themeStore.themeClass, { care: isCare }]">
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
			<text class="title">{{ $t('blockedUsers.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 搜索栏 -->
		<view class="search-section">
			<view class="search-input">
				<view class="search-icon">
					<view class="search-magnifier"></view>
				</view>
				<input 
					class="search-field" 
					:placeholder="$t('blockedUsers.searchPlaceholder')" 
					v-model="searchKeyword"
					@input="onSearchInput"
				/>
				<view v-if="searchKeyword" class="clear-button" @click="clearSearch">
					<view class="clear-icon"></view>
				</view>
			</view>
		</view>
		
		<!-- 统计信息 -->
		<view class="stats-section">
			<view class="stats-item">
				<text class="stats-number">{{ totalBlockedUsers }}</text>
				<text class="stats-label">{{ $t('blockedUsers.stats.blockedUsers') }}</text>
			</view>
			<view class="stats-item">
				<text class="stats-number">{{ searchResults.length }}</text>
				<text class="stats-label">{{ $t('blockedUsers.stats.searchResults') }}</text>
			</view>
		</view>
		
		<!-- 黑名单用户列表 -->
		<view class="users-list">
			<view v-if="filteredUsers.length === 0" class="empty-state">
				<view class="empty-icon">
					<view class="empty-face"></view>
				</view>
				<text class="empty-title">{{ $t('blockedUsers.empty.title') }}</text>
				<text class="empty-description">{{ $t('blockedUsers.empty.description') }}</text>
			</view>
			
			<view 
				v-for="user in filteredUsers" 
				:key="user.id" 
				class="user-item"
			>
				<view class="user-info">
					<view class="user-avatar">
						<image v-if="user.avatar" :src="user.avatar" class="avatar-image" />
						<view v-else class="avatar-placeholder">
							<text class="avatar-text">{{ user.name.charAt(0) }}</text>
						</view>
					</view>
					<view class="user-details">
						<text class="user-name">{{ user.name }}</text>
						<text class="user-id">{{ $t('blockedUsers.user.id', { id: user.id }) }}</text>
						<text class="block-date">{{ $t('blockedUsers.user.blockDate', { date: formatDate(user.blockDate) }) }}</text>
					</view>
				</view>
				<view class="user-actions">
					<view class="action-button unblock-btn" @click="unblockUser(user)">
						<text class="action-text">{{ $t('blockedUsers.actions.unblock') }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部提示 -->
		<view class="bottom-tip">
			<text class="tip-text">{{ $t('blockedUsers.tip') }}</text>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';

	export default {
		name: 'BlockedUsersPage',
		data() {
			return {
				themeStore: useThemeStore(),
				searchKeyword: '',
				blockedUsers: [],
				userId: null,
				modeStore: null
			}
		},
		onLoad() {
			this.themeStore.init();
			this.themeStore.applyTheme();
			this.userId = uni.getStorageSync('userId') || 1;
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
			this.loadBlockedUsers();
		},
		computed: {
			totalBlockedUsers() {
				return this.blockedUsers.length;
			},
			searchResults() {
				if (!this.searchKeyword.trim()) {
					return this.blockedUsers;
				}
				return this.blockedUsers.filter(user => 
					(user.name && user.name.toLowerCase().includes(this.searchKeyword.toLowerCase())) ||
					(user.id && user.id.toString().includes(this.searchKeyword))
				);
			},
			filteredUsers() {
				return this.searchResults;
			},
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		methods: {
			async loadBlockedUsers() {
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/bc-entities/block-record/user/${this.userId}`,
						method: 'GET'
					});
					if (response.statusCode === 200 && response.data) {
						// 获取用户信息（简化处理，实际应该调用用户信息接口）
						this.blockedUsers = response.data.map(record => ({
							id: record.blockedUserId,
							name: this.$t('blockedUsers.user.defaultName', { id: record.blockedUserId }), // 简化处理
							avatar: '',
							blockDate: record.blockedAt ? new Date(record.blockedAt).toISOString().split('T')[0] : '',
							blockedUserId: record.blockedUserId
						}));
					}
				} catch (error) {
					console.error('加载黑名单失败:', error);
				}
			},
			goBack() {
				uni.navigateBack();
			},
			onSearchInput() {
				// 搜索逻辑已在computed中处理
			},
			clearSearch() {
				this.searchKeyword = '';
			},
			async unblockUser(user) {
				uni.showModal({
					title: this.$t('blockedUsers.actions.confirmUnblock'),
					content: this.$t('blockedUsers.actions.confirmUnblockContent', { name: user.name }),
					success: async (res) => {
						if (res.confirm) {
							try {
								const response = await uni.request({
									url: '${this.$baseUrl}/api/bc-entities/block-record/unblock',
									method: 'POST',
									data: {
										userId: this.userId,
										blockedUserId: user.blockedUserId || user.id
									}
								});
								if (response.statusCode === 200 && response.data.success) {
									const index = this.blockedUsers.findIndex(u => (u.blockedUserId || u.id) === (user.blockedUserId || user.id));
									if (index > -1) {
										this.blockedUsers.splice(index, 1);
									}
									uni.showToast({
										title: this.$t('blockedUsers.actions.unblocked'),
										icon: 'success'
									});
								} else {
									uni.showToast({
										title: response.data.message || this.$t('blockedUsers.actions.unblockFailed'),
										icon: 'none'
									});
								}
							} catch (error) {
								uni.showToast({
									title: this.$t('blockedUsers.actions.unblockFailed'),
									icon: 'none'
								});
							}
						}
					}
				});
			},
			formatDate(dateString) {
				if (!dateString) return '';
				const date = new Date(dateString);
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
			}
		}
	}
</script>

<style>
	.blocked-users-page {
		min-height: 100vh;
		background-color: var(--color-bg, #ffffff);
		color: var(--color-text, #11141f);
		display: flex;
		flex-direction: column;
	}

	.blocked-users-page.theme-dark {
		background-color: var(--color-bg, #0f1115);
		color: var(--color-text, #e9edf5);
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: var(--color-bg, #ffffff);
	}
	
	.time {
		font-size: 28rpx;
		color: var(--color-text, #11141f);
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
		background-color: var(--color-text, #11141f);
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
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
		border-right: 12rpx solid var(--color-text, #11141f);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: var(--color-text, #11141f);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 搜索栏 */
	.search-section {
		padding: 24rpx 32rpx;
		background-color: var(--color-bg, #ffffff);
	}
	
	.search-input {
		display: flex;
		align-items: center;
		background-color: var(--color-bg-weak, #f7f7f9);
		border-radius: 16rpx;
		padding: 16rpx 20rpx;
		gap: 16rpx;
	}
	
	.search-icon {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.search-magnifier {
		width: 20rpx;
		height: 20rpx;
		border: 2rpx solid var(--color-text-secondary, #666c7a);
		border-radius: 50%;
		position: relative;
	}
	
	.search-magnifier::after {
		content: '';
		position: absolute;
		bottom: -6rpx;
		right: -6rpx;
		width: 8rpx;
		height: 2rpx;
		background-color: var(--color-text-secondary, #666c7a);
		transform: rotate(45deg);
	}
	
	.search-field {
		flex: 1;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		background: transparent;
		border: none;
		outline: none;
	}
	
	.clear-button {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.clear-icon {
		width: 16rpx;
		height: 16rpx;
		position: relative;
	}
	
	.clear-icon::before,
	.clear-icon::after {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 16rpx;
		height: 2rpx;
		background-color: var(--color-text-secondary, #666c7a);
	}
	
	.clear-icon::before {
		transform: translate(-50%, -50%) rotate(45deg);
	}
	
	.clear-icon::after {
		transform: translate(-50%, -50%) rotate(-45deg);
	}
	
	/* 统计信息 */
	.stats-section {
		display: flex;
		justify-content: center;
		padding: 0 32rpx 24rpx;
		gap: 48rpx;
	}
	
	.stats-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}
	
	.stats-number {
		font-size: 36rpx;
		color: var(--color-primary, #ff69b4);
		font-weight: 600;
	}
	
	.stats-label {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666c7a);
	}
	
	/* 用户列表 */
	.users-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 120rpx 0;
		gap: 24rpx;
	}
	
	.empty-icon {
		width: 120rpx;
		height: 120rpx;
		background-color: var(--color-bg-weak, #f7f7f9);
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.empty-face {
		width: 80rpx;
		height: 80rpx;
		background-color: var(--color-border, #e5e7ec);
		border-radius: 40rpx;
		position: relative;
	}
	
	.empty-face::before {
		content: '';
		position: absolute;
		top: 20rpx;
		left: 20rpx;
		width: 12rpx;
		height: 12rpx;
		background-color: var(--color-on-primary, #ffffff);
		border-radius: 50%;
	}
	
	.empty-face::after {
		content: '';
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 12rpx;
		height: 12rpx;
		background-color: var(--color-on-primary, #ffffff);
		border-radius: 50%;
	}
	
	.empty-title {
		font-size: 32rpx;
		color: var(--color-text, #11141f);
		font-weight: 500;
	}
	
	.empty-description {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666c7a);
	}
	
	.user-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 0;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
	}
	
	.user-item:last-child {
		border-bottom: none;
	}
	
	.user-info {
		display: flex;
		align-items: center;
		gap: 20rpx;
		flex: 1;
	}
	
	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		overflow: hidden;
	}
	
	.avatar-image {
		width: 100%;
		height: 100%;
	}
	
	.avatar-placeholder {
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.avatar-text {
		font-size: 32rpx;
		color: var(--color-on-primary, #ffffff);
		font-weight: 600;
	}
	
	.user-details {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.user-name {
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		font-weight: 500;
	}
	
	.user-id {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666c7a);
	}
	
	.block-date {
		font-size: 22rpx;
		color: var(--color-text-secondary, #666c7a);
	}
	
	.user-actions {
		display: flex;
		gap: 16rpx;
	}
	
	.action-button {
		padding: 16rpx 24rpx;
		border-radius: 20rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.unblock-btn {
		background-color: var(--color-primary, #ff69b4);
	}
	
	.unblock-btn:active {
		opacity: 0.9;
	}
	
	.action-text {
		font-size: 24rpx;
		color: var(--color-on-primary, #ffffff);
		font-weight: 500;
	}
	
	/* 底部提示 */
	.bottom-tip {
		padding: 32rpx;
		text-align: center;
	}
	
	.tip-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666c7a);
		line-height: 1.5;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #11141f);
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}

	.blocked-users-page.care .search-section {
		padding: 40rpx;
		box-sizing: border-box;
	}

	.blocked-users-page.care .search-input {
		padding: 22rpx 28rpx;
		border-radius: 20rpx;
		box-sizing: border-box;
	}

	.blocked-users-page.care .search-magnifier {
		width: 26rpx;
		height: 26rpx;
		border-width: 3rpx;
	}

	.blocked-users-page.care .search-field {
		font-size: 34rpx;
	}

	.blocked-users-page.care .clear-button {
		width: 48rpx;
		height: 48rpx;
	}

	.blocked-users-page.care .stats-section {
		padding: 0 40rpx 32rpx;
		box-sizing: border-box;
	}

	.blocked-users-page.care .stats-number {
		font-size: 46rpx;
	}

	.blocked-users-page.care .stats-label {
		font-size: 30rpx;
	}

	.blocked-users-page.care .users-list {
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.blocked-users-page.care .user-item {
		padding: 36rpx 0;
	}

	.blocked-users-page.care .user-avatar {
		width: 110rpx;
		height: 110rpx;
		border-radius: 55rpx;
	}

	.blocked-users-page.care .avatar-text {
		font-size: 38rpx;
	}

	.blocked-users-page.care .user-name {
		font-size: 34rpx;
		line-height: 1.4;
	}

	.blocked-users-page.care .user-id {
		font-size: 30rpx;
	}

	.blocked-users-page.care .block-date {
		font-size: 28rpx;
	}

	.blocked-users-page.care .action-button {
		padding: 20rpx 32rpx;
		border-radius: 28rpx;
	}

	.blocked-users-page.care .action-text {
		font-size: 30rpx;
	}

	.blocked-users-page.care .tip-text {
		font-size: 30rpx;
		line-height: 1.6;
	}
</style>