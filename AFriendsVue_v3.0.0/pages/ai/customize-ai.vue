<template>
	<view class="customize-ai-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航栏 -->
		<!-- <view class="header">
			<view class="back-btn">
				<text class="back-arrow">&lt;</text>
			</view>
			<text class="title">自定义AI</text>
		</view> -->
		
		<!-- AI资料区域 -->
		<view class="ai-profile">
			<view class="ai-avatar-wrap">
				<image class="ai-avatar" src="" mode="aspectFill"></image>
			</view>
			<view class="ai-info">
				<text class="ai-name">相亲AI</text>
				<view class="ai-level-row">
					<text class="ai-level">Lv10</text>
					<text class="level-up-link" @click="goToLevelUp">等级提升</text>
				</view>
			</view>
		</view>
		
		<!-- 分割线 -->
		<view class="divider"></view>
		
		<!-- 声音设定区域 -->
		<!-- <view class="voice-section">
			<text class="section-title">声音设定</text>
			<view class="voice-selector">
				<text class="voice-label">声音</text>
				<view class="voice-dropdown">
					<text class="voice-value">我的声音</text>
					<view class="dropdown-arrow"></view>
				</view>
			</view>
			<view class="record-btn">
				<text class="record-text">按住录音</text>
			</view>
			<view class="playback-controls">
				<view class="play-btn"></view>
				<view class="volume-slider">
					<view class="slider-track">
						<view class="slider-fill"></view>
						<view class="slider-handle"></view>
					</view>
				</view>
			</view>
		</view> -->
		
		<!-- 分割线 -->
		<view class="divider"></view>
		
		<!-- 礼物限额区域 -->
		<view class="gift-section">
			<text class="section-title">礼物限额</text>
			<text class="section-desc">设置要给AI的额度</text>
			<view class="gift-input">
				<view class="input-field">
					<text class="placeholder">请输入</text>
				</view>
				<text class="currency">金币</text>
			</view>
		</view>
		
		<!-- 保存按钮 -->
		<view class="save-section">
			<view class="save-btn">
				<text class="save-text">保存</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'CustomizeAiPage',
		data() {
			return {
				
			}
		},
		methods: {
			goToLevelUp() {
				uni.navigateTo({
					url: '/pages/ai/level-up'
				});
			}
		}
	}
</script>

<style>
	.customize-ai-page {
		min-height: 100vh;
		background-color: #FFFFFF;
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
		background-color: #FFFFFF;
	}
	
	.time {
		font-size: 28rpx;
		color: #000000;
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
		background-color: #000000;
		border-radius: 4rpx;
	}
	
	/* 头部导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.back-btn {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		font-size: 36rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.title {
		flex: 1;
		text-align: center;
		font-size: 36rpx;
		color: #333333;
		font-weight: 600;
		margin-right: 60rpx;
	}
	
	/* AI资料区域 */
	.ai-profile {
		padding: 40rpx 32rpx;
		display: flex;
		align-items: center;
		gap: 24rpx;
	}
	
	.ai-avatar-wrap {
		width: 120rpx;
		height: 120rpx;
		border-radius: 16rpx;
		overflow: hidden;
		background-color: #F5F5F5; /* AI头像占位 */
	}
	
	.ai-avatar {
		width: 100%;
		height: 100%;
	}
	
	.ai-info {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.ai-name {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.ai-level-row {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.ai-level {
		font-size: 24rpx;
		color: #666666;
	}

	.level-up-link {
		font-size: 22rpx;
		color: #0A84FF;
		padding: 6rpx 16rpx;
		background-color: #EEF5FF;
		border-radius: 20rpx;
		border: 2rpx solid #D6E7FF;
	}
	
	/* 分割线 */
	.divider {
		height: 1rpx;
		background-color: #F0F0F0;
		margin: 0 32rpx;
	}
	
	/* 声音设定区域 */
	.voice-section {
		padding: 40rpx 32rpx;
	}
	
	.section-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
		margin-bottom: 32rpx;
	}
	
	.voice-selector {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}
	
	.voice-label {
		font-size: 28rpx;
		color: #333333;
	}
	
	.voice-dropdown {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.voice-value {
		font-size: 28rpx;
		color: #333333;
	}
	
	.dropdown-arrow {
		width: 0;
		height: 0;
		border-left: 12rpx solid transparent;
		border-right: 12rpx solid transparent;
		border-top: 12rpx solid #999999;
	}
	
	.record-btn {
		height: 120rpx;
		background-color: #F8F8F8;
		border: 2rpx solid #E0E0E0;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 32rpx;
	}
	
	.record-text {
		font-size: 28rpx;
		color: #666666;
	}
	
	.playback-controls {
		display: flex;
		align-items: center;
		gap: 24rpx;
	}
	
	.play-btn {
		width: 0;
		height: 0;
		border-left: 20rpx solid #000000;
		border-top: 16rpx solid transparent;
		border-bottom: 16rpx solid transparent;
	}
	
	.volume-slider {
		flex: 1;
	}
	
	.slider-track {
		height: 8rpx;
		background-color: #E0E0E0;
		border-radius: 4rpx;
		position: relative;
	}
	
	.slider-fill {
		height: 100%;
		width: 75%;
		background-color: #007AFF;
		border-radius: 4rpx;
	}
	
	.slider-handle {
		position: absolute;
		top: -4rpx;
		left: 75%;
		width: 16rpx;
		height: 16rpx;
		background-color: #FFFFFF;
		border: 2rpx solid #007AFF;
		border-radius: 50%;
		transform: translateX(-50%);
	}
	
	/* 礼物限额区域 */
	.gift-section {
		padding: 40rpx 32rpx;
	}
	
	.section-desc {
		font-size: 24rpx;
		color: #999999;
		margin-bottom: 24rpx;
	}
	
	.gift-input {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.input-field {
		flex: 1;
		height: 80rpx;
		background-color: #F8F8F8;
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		padding: 0 24rpx;
	}
	
	.placeholder {
		font-size: 28rpx;
		color: #999999;
	}
	
	.currency {
		font-size: 28rpx;
		color: #333333;
	}
	
	/* 保存按钮 */
	.save-section {
		padding: 40rpx 32rpx;
		margin-top: auto;
	}
	
	.save-btn {
		height: 100rpx;
		background-color: #007AFF;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.save-text {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
</style>
