<template>
	<view class="third-party-binding-page" :class="{ care: isCare }">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">第三方账号绑定</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 第三方登录方式 -->
			<view class="section">
				<text class="section-title">第三方登录方式</text>
				<view class="binding-list">
					<view class="binding-item" @click="bindWechat">
						<text class="platform-name">微信</text>
						<view class="binding-status">
							<text class="status-text">未绑定</text>
							<view class="arrow-icon">></view>
						</view>
					</view>
					<view class="binding-item" @click="bindQQ">
						<text class="platform-name">QQ</text>
						<view class="binding-status">
							<view class="qq-status">
								<text class="wave-icon">〰️</text>
								<text class="qq-username">糖糖没有</text>
							</view>
							<view class="arrow-icon">></view>
						</view>
					</view>
					<view class="binding-item" @click="bindWeibo">
						<text class="platform-name">微博</text>
						<view class="binding-status">
							<text class="status-text">未绑定</text>
							<view class="arrow-icon">></view>
						</view>
					</view>
					<view class="binding-item" @click="bindApple">
						<text class="platform-name">Apple</text>
						<view class="binding-status">
							<text class="status-text">未绑定</text>
							<view class="arrow-icon">></view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 第三方内容同步平台 -->
			<view class="section">
				<text class="section-title">第三方内容同步平台</text>
				<view class="binding-list">
					<view class="binding-item" @click="viewSyncDetails">
						<text class="platform-name">今日头条 / 西瓜视频 / 懂车帝</text>
						<view class="binding-status">
							<text class="status-text">查看详情</text>
							<view class="arrow-icon">></view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 同步选项 -->
			<view class="section">
				<view class="sync-option">
					<view class="option-info">
						<text class="option-title">同步粉丝数量</text>
						<text class="option-description">开启后,抖音粉丝数量将增加(关闭粉丝数量减少)</text>
					</view>
					<switch :checked="syncFollowers" @change="toggleSyncFollowers" color="#007aff" class="sync-switch" />
				</view>
				<view class="sync-option">
					<view class="option-info">
						<text class="option-title">同步抖音头像和昵称信息</text>
					</view>
					<switch :checked="syncProfile" @change="toggleSyncProfile" color="#007aff" class="sync-switch" />
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'ThirdPartyBindingPage',
		data() {
			return {
				syncFollowers: false,
				syncProfile: false,
				modeStore: null
			}
		},
		onLoad() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
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
			bindWechat() {
				uni.showModal({
					title: '绑定微信',
					content: '确定要绑定微信账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '微信绑定功能开发中',
								icon: 'none'
							});
						}
					}
				});
			},
			bindQQ() {
				uni.showModal({
					title: '绑定QQ',
					content: '确定要绑定QQ账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: 'QQ绑定功能开发中',
								icon: 'none'
							});
						}
					}
				});
			},
			bindWeibo() {
				uni.showModal({
					title: '绑定微博',
					content: '确定要绑定微博账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '微博绑定功能开发中',
								icon: 'none'
							});
						}
					}
				});
			},
			bindApple() {
				uni.showModal({
					title: '绑定Apple',
					content: '确定要绑定Apple账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: 'Apple绑定功能开发中',
								icon: 'none'
							});
						}
					}
				});
			},
			viewSyncDetails() {
				uni.showToast({
					title: '同步详情功能开发中',
					icon: 'none'
				});
			},
			toggleSyncFollowers(e) {
				this.syncFollowers = e.detail.value;
				uni.showToast({
					title: this.syncFollowers ? '已开启粉丝同步' : '已关闭粉丝同步',
					icon: 'none'
				});
			},
			toggleSyncProfile(e) {
				this.syncProfile = e.detail.value;
				uni.showToast({
					title: this.syncProfile ? '已开启资料同步' : '已关闭资料同步',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.third-party-binding-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		position: relative;
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
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 32rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 32rpx;
	}
	
	/* 区块样式 */
	.section {
		margin-bottom: 48rpx;
	}
	
	.section-title {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
		margin-bottom: 24rpx;
		display: block;
	}
	
	/* 绑定列表 */
	.binding-list {
		background-color: var(--color-card, #FFFFFF);
		border-radius: 16rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	.binding-item {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.binding-item:last-child {
		border-bottom: none;
	}
	
	.binding-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.platform-name {
		font-size: 28rpx;
		color: var(--color-text, #000000);
		font-weight: 500;
	}
	
	.binding-status {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.status-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.qq-status {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.wave-icon {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.qq-username {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
		font-weight: 300;
	}
	
	/* 同步选项 */
	.sync-option {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 32rpx;
		background-color: var(--color-card, #FFFFFF);
		border-radius: 16rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	.option-info {
		flex: 1;
		margin-right: 24rpx;
	}
	
	.option-title {
		font-size: 28rpx;
		color: var(--color-text, #000000);
		font-weight: 500;
		display: block;
		margin-bottom: 8rpx;
	}
	
	.option-description {
		font-size: 24rpx;
		color: var(--color-text-secondary, #999999);
		line-height: 1.4;
		display: block;
	}
	
	.sync-switch {
		transform: scale(0.9);
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}

	.third-party-binding-page.care .main-content {
		padding: 40rpx;
		box-sizing: border-box;
	}

	.third-party-binding-page.care .section {
		margin-bottom: 56rpx;
	}

	.third-party-binding-page.care .section-title {
		font-size: 34rpx;
		margin-bottom: 28rpx;
	}

	.third-party-binding-page.care .binding-item {
		height: 148rpx;
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.third-party-binding-page.care .platform-name {
		font-size: 34rpx;
		line-height: 1.4;
	}

	.third-party-binding-page.care .status-text,
	.third-party-binding-page.care .qq-username {
		font-size: 32rpx;
	}

	.third-party-binding-page.care .arrow-icon {
		font-size: 36rpx;
	}

	.third-party-binding-page.care .sync-option {
		padding: 40rpx;
		border-radius: 20rpx;
		box-sizing: border-box;
	}

	.third-party-binding-page.care .option-title {
		font-size: 34rpx;
		line-height: 1.5;
	}

	.third-party-binding-page.care .option-description {
		font-size: 30rpx;
		line-height: 1.6;
	}

	.third-party-binding-page.care .sync-switch {
		transform: scale(1);
	}
</style>


