<template>
	<view class="add-friend-page" :class="themeStore.themeClass">
		<view class="header">
			<view class="nav-header">
				<view class="back-button" @click="goBack">
					<text>{{ $t('common.back') }}</text>
				</view>
				<view class="header-title">{{ $t('addFriend.title') }}</view>
				<view class="placeholder"></view>
			</view>
			<view class="tabs">
				<view class="tab" :class="{ active: tab === 'search' }" @click="switchTab('search')">{{ $t('addFriend.tabs.search') }}</view>
				<view class="tab" :class="{ active: tab === 'recommend' }" @click="switchTab('recommend')">{{ $t('addFriend.tabs.recommend') }}</view>
			</view>
		</view>

		<view v-if="tab === 'search'" class="search-container">
			<input class="search-input" v-model="keyword" :placeholder="$t('addFriend.searchPlaceholder')" @confirm="search" />
			<button class="search-button" @click="search">{{ $t('common.search') }}</button>
		</view>

		<view v-if="loading" class="loading">{{ $t('common.loading') }}</view>
		<view v-else>
			<view v-if="errorMessage" class="error">{{ errorMessage }}</view>
			<view v-else-if="currentList.length === 0" class="empty">{{ emptyText }}</view>
			<view v-else class="list">
				<view class="user-item" v-for="user in currentList" :key="user.userId">
					<view class="user-info">
						<image class="avatar" :src="user.profilePicUrl || '/static/default-avatar.png'" mode="aspectFill" />
						<text class="username">{{ user.username }}</text>
						<text v-if="tab === 'recommend' && user.mutualCount !== undefined" class="mutual">{{ $t('addFriend.mutualFriends', { count: user.mutualCount }) }}</text>
					</view>
					<button class="add-button" @click="requestFriend(user)">{{ $t('addFriend.add') }}</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { useThemeStore } from '../../store/theme.js'

export default {
	name: 'AddFriend',
	data() {
		return {
			themeStore: useThemeStore(),
			userId: null,
			tab: 'search',
			keyword: '',
			users: [],
			recommendations: [],
			recommendDisabled: false,
			loading: false,
			errorMessage: ''
		}
	},
	computed: {
		currentList() {
			return this.tab === 'recommend' ? this.recommendations : this.users
		},
		emptyText() {
			if (this.tab === 'recommend') {
				if (this.recommendDisabled) {
					return this.$t('addFriend.recommendDisabled')
				}
				return this.$t('addFriend.recommendEmpty')
			}
			return this.$t('addFriend.empty')
		}
	},
	onLoad() {
		this.themeStore.init()
		this.themeStore.applyTheme()
		this.userId = uni.getStorageSync('userId') || 1000001
	},
	onShow() {
		this.themeStore.applyTheme()
	},
	methods: {
		switchTab(next) {
			this.tab = next
			this.errorMessage = ''
			if (next === 'recommend') {
				this.loadRecommendations()
			}
		},
		async loadRecommendations() {
			this.loading = true
			this.errorMessage = ''
			this.recommendations = []
			this.recommendDisabled = false
			try {
				const res = await uni.request({
					url: `${this.$baseUrl}/api/u-entities/friend/recommend/${this.userId}`,
					method: 'GET',
					data: {
						limit: 20
					}
				})
				if (res.statusCode === 200 && res.data && res.data.success) {
					this.recommendDisabled = !!res.data.disabled
					this.recommendations = res.data.data || []
				} else {
					this.errorMessage = (res.data && res.data.message) ? res.data.message : this.$t('addFriend.recommendFailed')
				}
			} catch (e) {
				this.errorMessage = this.$t('common.networkError')
			} finally {
				this.loading = false
			}
		},
		goBack() {
			uni.navigateBack()
		},
		async search() {
			this.loading = true
			this.errorMessage = ''
			this.users = []
			try {
				const res = await uni.request({
					url: `${this.$baseUrl}/api/u-entities/friend/search`,
					method: 'GET',
					data: {
						userId: this.userId,
						keyword: this.keyword,
						source: 'PHONE_SEARCH'
					}
				})
				if (res.statusCode === 200 && res.data && res.data.success) {
					this.users = res.data.data || []
				} else {
					this.errorMessage = (res.data && res.data.message) ? res.data.message : this.$t('addFriend.searchFailed')
				}
			} catch (e) {
				this.errorMessage = this.$t('common.networkError')
			} finally {
				this.loading = false
			}
		},
		async requestFriend(user) {
			try {
				const res = await uni.request({
					url: '${this.$baseUrl}/api/u-entities/friend/request',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data: {
						fromUserId: this.userId,
						toUserId: user.userId,
						message: '',
						source: this.tab === 'recommend' ? 'RECOMMEND' : 'PHONE_SEARCH'
					}
				})
				if (res.statusCode === 200 && res.data && res.data.success) {
					uni.showToast({
						title: res.data.message || this.$t('addFriend.requestSent'),
						icon: 'none'
					})
					if (this.tab === 'recommend') {
						this.loadRecommendations()
					}
				} else {
					uni.showToast({
						title: (res.data && res.data.message) ? res.data.message : this.$t('addFriend.requestFailed'),
						icon: 'none'
					})
				}
			} catch (e) {
				uni.showToast({
					title: this.$t('common.networkError'),
					icon: 'none'
				})
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.add-friend-page {
	min-height: 100vh;
	background-color: var(--color-bg);
	color: var(--color-text);
}
.header {
	background-color: var(--color-card);
	border-bottom: 1rpx solid var(--color-border);
}
.nav-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 16rpx 32rpx;
}

.tabs {
	display: flex;
	gap: 16rpx;
	padding: 0 32rpx 16rpx;
}

.tab {
	padding: 12rpx 20rpx;
	border-radius: 999rpx;
	background-color: var(--color-bg-weak);
	color: var(--color-text-secondary);
	font-size: 26rpx;
}

.tab.active {
	background-color: var(--color-primary);
	color: var(--color-on-primary);
}
.header-title {
	font-size: 32rpx;
	font-weight: 600;
	color: var(--color-text);
}
.search-container {
	display: flex;
	gap: 16rpx;
	padding: 24rpx 32rpx;
	background-color: var(--color-card);
	border-bottom: 1rpx solid var(--color-border);
}
.search-input {
	flex: 1;
	background: var(--color-bg-weak);
	color: var(--color-text);
	padding: 16rpx 20rpx;
	border-radius: 12rpx;
}

.search-input::placeholder {
	color: var(--color-text-secondary);
}

.search-button {
	font-size: 28rpx;
	background-color: var(--color-primary);
	color: var(--color-on-primary);
	border: none;
	border-radius: 12rpx;
	padding: 0 24rpx;
	line-height: 72rpx;
}
.list {
	padding: 16rpx 32rpx;
}
.user-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	background: var(--color-card);
	padding: 16rpx;
	border-radius: 12rpx;
	margin-bottom: 16rpx;
	border: 1rpx solid var(--color-border);
}
.user-info {
	display: flex;
	align-items: center;
	gap: 16rpx;
}

.mutual {
	font-size: 24rpx;
	color: var(--color-text-secondary);
}
.avatar {
	width: 72rpx;
	height: 72rpx;
	border-radius: 36rpx;
	background: var(--color-bg-weak);
}
.username {
	font-size: 30rpx;
	color: var(--color-text);
}
.add-button {
	font-size: 28rpx;
	background-color: var(--color-primary);
	color: var(--color-on-primary);
	border: none;
	border-radius: 12rpx;
	padding: 0 24rpx;
	line-height: 64rpx;
}
.loading, .error, .empty {
	padding: 32rpx;
	text-align: center;
	color: var(--color-text-secondary);
}

.error {
	color: var(--danger-bg, #ff3b30);
}
</style>
