<template>
	<view class="container" :class="{ care: isCare }">
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
			<!-- <view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">{{ $t('modeSelection.title') }}</text> -->
		</view>
		
		<!-- 模式选择内容 -->
		<view class="content-area">
			<!-- 分组标题 -->
			<view class="section-header">{{ $t('modeSelection.modeSwitch') }}</view>
			
			<!-- 模式选项列表 -->
			<view class="mode-list">
				<view class="mode-item" :class="{ active: selectedMode === 'normal' }" @click="selectMode('normal')">
					<text class="mode-text">{{ $t('modeSelection.normalMode') }}</text>
				</view>
				<view class="mode-item" :class="{ active: selectedMode === 'concise' }" @click="selectMode('concise')">
					<text class="mode-text">{{ $t('modeSelection.conciseMode') }}</text>
				</view>
				<view class="mode-item" :class="{ active: selectedMode === 'minor' }" @click="selectMode('minor')">
					<text class="mode-text">{{ $t('modeSelection.minorMode') }}</text>
				</view>
				<view class="mode-item" :class="{ active: selectedMode === 'care' }" @click="selectMode('care')">
					<text class="mode-text">{{ $t('modeSelection.careMode') }}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		data() {
			return {
				selectedMode: 'normal',
				modeStore: null
			}
		},
		mounted() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
			this.selectedMode = modeStore.mode || 'normal';
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
			async selectMode(mode) {
				const oldMode = this.selectedMode;
				this.selectedMode = mode;
				const modeStore = useModeStore();
				modeStore.setMode(mode);

				const userId = uni.getStorageSync('userId');
				if (userId) {
					try {
						const enabled = mode === 'minor';
						const res = await uni.request({
							url: '${this.$baseUrl}/api/u-entities/mode-settings/minor/save',
							method: 'POST',
							header: {
								'Content-Type': 'application/json'
							},
							data: {
								userId: userId,
								enabled: enabled
							}
						});

						if (!(res.statusCode === 200 && res.data && res.data.success)) {
							this.selectedMode = oldMode;
							modeStore.setMode(oldMode);
							uni.showToast({
								title: (res.data && res.data.message) ? res.data.message : '设置保存失败',
								icon: 'none'
							});
							return;
						}
					} catch (e) {
						this.selectedMode = oldMode;
						modeStore.setMode(oldMode);
						uni.showToast({
								title: this.$t('common.networkError'),
								icon: 'none'
							});
						return;
					}
				}

				uni.showToast({
					title: this.$t('modeSelection.selected', { mode: this.getModeName(mode) }),
					icon: 'success'
				});
				
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			},
			getModeName(mode) {
				const modeNames = {
					'normal': this.$t('modeSelection.normalMode'),
					'concise': this.$t('modeSelection.conciseMode'),
					'minor': this.$t('modeSelection.minorMode'),
					'care': this.$t('modeSelection.careMode')
				};
				return modeNames[mode] || '未知模式';
			}
		}
	}
</script>

<style>
	.container {
		height: 100vh;
		background-color: var(--color-bg, #fff);
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: var(--color-card, #fff);
		z-index: 1000;
		position: relative;
	}
	
	.time {
		font-size: 32rpx;
		font-weight: 600;
		color: var(--color-text, #000);
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.signal {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.bar {
		width: 6rpx;
		background-color: var(--color-text, #000);
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
		color: var(--color-text, #000);
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid var(--color-text, #000);
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: var(--color-text, #000);
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: var(--color-text, #000);
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 1rpx 40rpx;
		background-color: var(--color-card, #fff);
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		position: relative;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text, #000);
		font-weight: bold;
	}
	
	.nav-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 36rpx;
		font-weight: bold;
		color: var(--color-text, #000);
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		background-color: var(--color-bg, #fff);
	}
	
	/* 分组标题 */
	.section-header {
		background-color: var(--color-bg-weak, #f8f8f8);
		padding: 20rpx 40rpx;
		font-size: 28rpx;
		color: var(--color-text-secondary, #666);
		font-weight: 500;
		text-align: left;
	}
	
	/* 模式选项列表 */
	.mode-list {
		background-color: var(--color-card, #fff);
	}
	
	.mode-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		background-color: var(--color-card, #fff);
		cursor: pointer;
	}
	
	.mode-item.active {
		background-color: var(--color-bg-weak, #f8f8f8);
	}
	
	.mode-item.active .mode-text {
		color: var(--color-primary, #ff69b4);
		font-weight: 600;
	}
	
	.mode-item:last-child {
		border-bottom: none;
	}
	
	.mode-text {
		font-size: 30rpx;
		color: var(--color-text, #333);
	}
	
	/* 底部手势条 */
	.home-indicator {
		width: 100rpx;
		height: 10rpx;
		background-color: var(--color-text, #000);
		border-radius: 5rpx;
		margin: 20rpx auto;
		opacity: 0.5;
	}

	.container.care .nav-bar {
		padding: 1rpx 48rpx;
	}

	.container.care .section-header {
		padding: 28rpx 48rpx;
		font-size: 36rpx;
	}

	.container.care .mode-item {
		padding: 44rpx 48rpx;
		min-height: 120rpx;
		box-sizing: border-box;
	}

	.container.care .mode-text {
		font-size: 36rpx;
		line-height: 1.5;
	}
</style>
