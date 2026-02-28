<template>
	<view class="friend-requests-page" :class="themeStore.themeClass">
		<view class="header">
			<view class="nav-header">
				<view class="back-button" @click="goBack">
					<text>{{ $t('common.back') }}</text>
				</view>
				<view class="header-title">{{ $t('friendRequests.title') }}</view>
				<view class="placeholder"></view>
			</view>
			<view class="tabs">
				<view class="tab" :class="{ active: tab === 'inbox' }" @click="tab = 'inbox'">{{ $t('friendRequests.inbox') }}</view>
				<view class="tab" :class="{ active: tab === 'outbox' }" @click="tab = 'outbox'">{{ $t('friendRequests.outbox') }}</view>
			</view>
		</view>

		<view v-if="loading" class="loading">{{ $t('common.loading') }}</view>
		<view v-else class="content">
			<view v-if="errorMessage" class="error">{{ errorMessage }}</view>
			<view v-else>
				<view v-if="currentList.length === 0" class="empty">{{ $t('friendRequests.empty') }}</view>
				<view v-else>
					<view class="item" v-for="item in currentList" :key="item.requestId">
						<view class="info">
							<image class="avatar" :src="(tab==='inbox' ? item.fromAvatarUrl : item.toAvatarUrl) || '/static/default-avatar.png'" mode="aspectFill" />
							<view class="texts">
								<text class="name">{{ tab==='inbox' ? item.fromUsername : item.toUsername }}</text>
								<text class="desc">{{ item.message || '' }}</text>
								<text class="status" v-if="tab==='outbox'">{{ item.status }}</text>
							</view>
						</view>
						<view class="actions" v-if="tab==='inbox'">
							<button class="btn" @click="accept(item)">{{ $t('friendRequests.accept') }}</button>
							<button class="btn" @click="reject(item)">{{ $t('friendRequests.reject') }}</button>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
import { useThemeStore } from '../../store/theme.js'

export default {
	name: 'FriendRequests',
	data() {
		return {
			themeStore: useThemeStore(),
			userId: null,
			tab: 'inbox',
			loading: false,
			errorMessage: '',
			inbox: [],
			outbox: []
		}
	},
	computed: {
		currentList() {
			return this.tab === 'inbox' ? this.inbox : this.outbox
		}
	},
	onLoad() {
		this.themeStore.init()
		this.themeStore.applyTheme()
		this.userId = uni.getStorageSync('userId') || 1000001
		this.loadAll()
	},
	onShow() {
		this.themeStore.applyTheme()
	},
	watch: {
		tab() {
			this.loadAll()
		}
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		async loadAll() {
			this.loading = true
			this.errorMessage = ''
			try {
				const inboxRes = await uni.request({
					url: `${this.$baseUrl}/api/u-entities/friend/requests/inbox/${this.userId}`,
					method: 'GET'
				})
				if (inboxRes.statusCode === 200 && inboxRes.data && inboxRes.data.success) {
					this.inbox = inboxRes.data.data || []
				} else {
					this.inbox = []
				}

				const outboxRes = await uni.request({
					url: `${this.$baseUrl}/api/u-entities/friend/requests/outbox/${this.userId}`,
					method: 'GET'
				})
				if (outboxRes.statusCode === 200 && outboxRes.data && outboxRes.data.success) {
					this.outbox = outboxRes.data.data || []
				} else {
					this.outbox = []
				}
			} catch (e) {
				this.errorMessage = this.$t('common.networkError')
			} finally {
				this.loading = false
			}
		},
		async accept(item) {
			try {
				const res = await uni.request({
					url: '${this.$baseUrl}/api/u-entities/friend/request/accept',
					method: 'POST',
					header: { 'Content-Type': 'application/json' },
					data: { requestId: item.requestId }
				})
				uni.showToast({ title: (res.data && res.data.message) ? res.data.message : this.$t('friendRequests.accepted'), icon: 'none' })
				this.loadAll()
			} catch (e) {
				uni.showToast({ title: this.$t('common.networkError'), icon: 'none' })
			}
		},
		async reject(item) {
			try {
				const res = await uni.request({
					url: '${this.$baseUrl}/api/u-entities/friend/request/reject',
					method: 'POST',
					header: { 'Content-Type': 'application/json' },
					data: { requestId: item.requestId }
				})
				uni.showToast({ title: (res.data && res.data.message) ? res.data.message : this.$t('friendRequests.rejected'), icon: 'none' })
				this.loadAll()
			} catch (e) {
				uni.showToast({ title: this.$t('common.networkError'), icon: 'none' })
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.friend-requests-page {
	min-height: 100vh;
	background: var(--color-bg);
	color: var(--color-text);
}
.header {
	background: var(--color-card);
	border-bottom: 1rpx solid var(--color-border);
}
.nav-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 16rpx 32rpx;
}
.header-title {
	font-size: 32rpx;
	font-weight: 600;
	color: var(--color-text);
}
.tabs {
	display: flex;
	gap: 32rpx;
	padding: 0 32rpx 16rpx;
}
.tab {
	font-size: 28rpx;
	color: var(--color-text-secondary);
	padding: 12rpx 0;
}
.tab.active {
	color: var(--color-primary);
	border-bottom: 4rpx solid var(--color-primary);
}
.content {
	padding: 16rpx 32rpx;
}
.item {
	background: var(--color-card);
	border-radius: 12rpx;
	padding: 16rpx;
	margin-bottom: 16rpx;
	border: 1rpx solid var(--color-border);
}
.info {
	display: flex;
	gap: 16rpx;
	align-items: center;
}
.avatar {
	width: 72rpx;
	height: 72rpx;
	border-radius: 36rpx;
	background: var(--color-bg-weak);
}
.texts {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 6rpx;
}
.name {
	font-size: 30rpx;
	color: var(--color-text);
}
.desc {
	font-size: 24rpx;
	color: var(--color-text-secondary);
}
.status {
	font-size: 24rpx;
	color: var(--color-text-secondary);
}
.actions {
	display: flex;
	gap: 16rpx;
	justify-content: flex-end;
	margin-top: 12rpx;
}
.btn {
	font-size: 26rpx;
	background-color: var(--color-bg-weak);
	color: var(--color-text);
	border: 1rpx solid var(--color-border);
	border-radius: 12rpx;
	padding: 0 20rpx;
	line-height: 60rpx;
}

.btn:active {
	opacity: 0.88;
	transform: scale(0.98);
}

.actions .btn:first-child {
	background-color: var(--color-primary);
	color: var(--color-on-primary);
	border: none;
}

.actions .btn:last-child {
	background-color: var(--danger-bg, #ff3b30);
	color: var(--color-on-danger, #ffffff);
	border: none;
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
