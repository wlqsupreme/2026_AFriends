<template>
	<view class="container">
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
			<view class="back-button" @click="goBack">
				<text class="back-arrow"></text>
			</view>
			<text class="nav-title">{{$t('report.title')}}</text>
		</view>
		
		<!-- 说明横幅 -->
		<view class="instruction-banner">
			<text class="instruction-text">{{$t('report.instruction')}}</text>
		</view>
		
		<!-- 举报原因列表 -->
		<view class="report-reasons">
			<view 
				class="reason-item" 
				:class="{ 'selected': selectedReason === index }"
				v-for="(reason, index) in reportReasons" 
				:key="index" 
				@click="selectReason(index)"
			>
				<text class="reason-text">{{reason}}</text>
				<view class="check-icon" v-if="selectedReason === index">✓</view>
			</view>
		</view>
		
		<!-- 下一步按钮 -->
		<view class="next-button" @click="goToNext">
			<text class="next-text">{{$t('report.next')}}</text>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		data() {
			return {
				reportReasons: [
					this.$t('report.reason.porn'),
					this.$t('report.reason.political'),
					this.$t('report.reason.fraud'),
					this.$t('report.reason.discrimination'),
					this.$t('report.reason.abuse'),
					this.$t('report.reason.cyberbullying'),
					this.$t('report.reason.externalLink'),
					this.$t('report.reason.illegal'),
					this.$t('report.reason.minor'),
					this.$t('report.reason.other')
				],
				selectedReason: -1,
				userId: 1000100,
				reportedUserId: null,
				contentId: null,
				contentType: null
			}
		},
		onLoad(options) {
			const themeStore = useThemeStore();
			themeStore.init();
			// 接收传递的参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			if (options.reportedUserId) {
				this.reportedUserId = parseInt(options.reportedUserId);
			}
			if (options.contentId) {
				this.contentId = parseInt(options.contentId);
			}
			if (options.contentType) {
				this.contentType = options.contentType;
			}
		},
		methods: {
			selectReason(index) {
				this.selectedReason = index;
			},
			goToNext() {
				if (this.selectedReason === -1) {
					uni.showToast({
						title: this.$t('report.selectReason'),
						icon: 'none'
					});
					return;
				}
				
				// 构建跳转参数
				const params = {
					reason: encodeURIComponent(this.reportReasons[this.selectedReason]),
					userId: this.userId
				};
				
				// 添加可选参数
				if (this.reportedUserId) {
					params.reportedUserId = this.reportedUserId;
				}
				if (this.contentId) {
					params.contentId = this.contentId;
				}
				if (this.contentType) {
					params.contentType = this.contentType;
				}
				
				// 构建查询字符串
				const queryString = Object.keys(params)
					.map(key => `${key}=${params[key]}`)
					.join('&');
				
				console.log('跳转到举报证据页面，参数:', params);
				
				// 跳转到举报证据页面，并传递所有参数
				uni.navigateTo({
					url: `/pages/report/report-evidence?${queryString}`
				});
			},
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background-color: var(--color-bg, #fff);
		padding-bottom: 260rpx; /* 给底部按钮留出空间 */
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
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
		padding: 20rpx 40rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		position: relative;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
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
	
	/* 说明横幅 */
	.instruction-banner {
		background-color: #87CEEB;
		padding: 30rpx 40rpx;
		margin: 30rpx 30rpx;
		border-radius: 20rpx;
	}
	
	.instruction-text {
		color: #fff;
		font-size: 28rpx;
		text-align: center;
		line-height: 1.5;
	}
	
	/* 举报原因列表 */
	.report-reasons {
		padding: 0 40rpx;
		margin-top: 40rpx;
	}
	
	.reason-item {
		padding: 30rpx 20rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		cursor: pointer;
		transition: all 0.3s ease;
		display: flex;
		align-items: center;
		justify-content: space-between;
		position: relative;
	}
	
	.reason-item:last-child {
		border-bottom: none;
	}
	
	.reason-item:active {
		background-color: var(--color-bg-weak, #f8f8f8);
	}
	
	.reason-text {
		font-size: 32rpx;
		color: var(--color-text, #000);
		line-height: 1.5;
		flex: 1;
	}
	
	.reason-item.selected {
		background-color: var(--color-bg-light, #f0f8ff);
		border-left: 6rpx solid #007bff;
		padding-left: 30rpx;
		transform: translateX(6rpx);
	}
	
	.reason-item.selected .reason-text {
		color: #007bff;
		font-weight: 600;
	}
	
	.check-icon {
		font-size: 36rpx;
		color: #007bff;
		font-weight: bold;
		margin-left: 20rpx;
		animation: fadeIn 0.3s ease;
	}
	
	@keyframes fadeIn {
		from {
			opacity: 0;
			transform: scale(0.8);
		}
		to {
			opacity: 1;
			transform: scale(1);
		}
	}
	
	/* 下一步按钮 */
	.next-button {
		position: fixed;
		bottom: 40rpx;
		left: 40rpx;
		right: 40rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		padding: 30rpx;
		border-radius: 20rpx;
		text-align: center;
		box-shadow: 0 10rpx 25rpx rgba(255, 105, 180, 0.3);
		cursor: pointer;
		transition: transform 0.2s ease;
	}
	
	.next-button:active {
		transform: scale(0.98);
	}
	
	.next-text {
		color: #fff;
		font-size: 36rpx;
		font-weight: bold;
	}
</style>