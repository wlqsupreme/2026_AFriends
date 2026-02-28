<!-- 从29号界面跳转过来 -->
<template>
	<view class="container">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
					<view class="back-button" @click="goBack">
			<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
				<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="1053"></path>
			</svg>
		</view>
			<text class="nav-title">AI理解等级</text>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 页面标题 -->
			<view class="page-header">
				<view class="header-icon">🤖</view>
				<text class="header-title">AI理解等级</text>
				<text class="header-subtitle">了解您的AI伙伴如何理解您</text>
			</view>
			
			<!-- 等级列表 -->
			<view class="levels-container">
				<!-- 第一级：合得来 -->
				<view class="level-card level-1">
					<view class="level-header">
						<view class="level-icon">🌱</view>
						<view class="level-info">
							<text class="level-title">合得来 </text>
							<text class="level-range">1-5级</text>
						</view>
						<view class="level-badge">100经验/级</view>
					</view>
					<view class="level-description">
						您的ai还不熟悉您,所以准确度可能比较低,试着和她多说说话
					</view>
				</view>
				
				<!-- 第二级：靠谱儿 -->
				<view class="level-card level-2">
					<view class="level-header">
						<view class="level-icon">🌿</view>
						<view class="level-info">
							<text class="level-title">靠谱儿 </text>
							<text class="level-range">6-10级</text>
						</view>
						<view class="level-badge">1000经验/级</view>
					</view>
					<view class="level-description">
						您的ai对您已经有所了解,但是细节上可能存在一些偏差,而且效率也不高
					</view>
				</view>
				
				<!-- 第三级：懂我者 -->
				<view class="level-card level-3">
					<view class="level-header">
						<view class="level-icon">🌳</view>
						<view class="level-info">
							<text class="level-title">懂我者 </text>
							<text class="level-range">11-20级</text>
						</view>
						<view class="level-badge">2000经验/级</view>
					</view>
					<view class="level-description">
						ai可以相对准确获得您的想法,他们的语言更加优美
					</view>
				</view>
				
				<!-- 第四级：生死交 -->
				<view class="level-card level-4">
					<view class="level-header">
						<view class="level-icon">💎</view>
						<view class="level-info">
							<text class="level-title">生死交 </text>
							<text class="level-range">21-24级</text>
						</view>
						<view class="level-badge">5000经验/级</view>
					</view>
					<view class="level-description">
						您的ai对您十分了解,可以为你找到您的心中所想
					</view>
				</view>
				
				<!-- 第五级：同行者 -->
				<view class="level-card level-5">
					<view class="level-header">
						<view class="level-icon">🔥</view>
						<view class="level-info">
							<text class="level-title">同行者 </text>
							<text class="level-range">25级</text>
						</view>
						<view class="level-badge">10000经验/级</view>
					</view>
					<view class="level-description">
						您的ai效率非常高,并且有很高的话术可以得到您想要的消息,携带的礼物可以适当减少
					</view>
				</view>
			</view>
			
			<!-- 提示信息 -->
			<view class="tip-section">
				<view class="tip-icon">💡</view>
				<text class="tip-text">多与AI聊天可以提升理解等级，提高准确度和效率</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background: rgba(255, 255, 255, 0.1);
		backdrop-filter: blur(20rpx);
		border-bottom: 1rpx solid rgba(255, 255, 255, 0.2);
		position: relative;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		background: rgba(255, 255, 255, 0.2);
		border-radius: 50%;
		transition: all 0.3s ease;
	}
	
	.back-button:active {
		background: rgba(255, 255, 255, 0.3);
		transform: scale(0.95);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: #fff;
		font-weight: bold;
	}
	
	.nav-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 36rpx;
		font-weight: bold;
		color: #fff;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 40rpx;
	}
	
	/* 页面标题 */
	.page-header {
		text-align: center;
		margin-bottom: 60rpx;
	}
	
	.header-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		animation: float 3s ease-in-out infinite;
	}
	
	.header-title {
		display: block;
		font-size: 48rpx;
		color: #fff;
		font-weight: bold;
		margin-bottom: 10rpx;
		text-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.3);
	}
	
	.header-subtitle {
		display: block;
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
		text-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.3);
	}
	
	/* 等级容器 */
	.levels-container {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
		margin-bottom: 40rpx;
	}
	
	/* 等级卡片 */
	.level-card {
		background: rgba(255, 255, 255, 0.95);
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
		transition: all 0.3s ease;
		position: relative;
		overflow: hidden;
	}
	
	.level-card::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 6rpx;
		background: linear-gradient(90deg, #667eea, #764ba2);
	}
	
	.level-card:hover {
		transform: translateY(-5rpx);
		box-shadow: 0 15rpx 35rpx rgba(0, 0, 0, 0.15);
	}
	
	/* 等级头部 */
	.level-header {
		display: flex;
		align-items: center;
		gap: 24rpx;
		margin-bottom: 20rpx;
	}
	
	.level-icon {
		font-size: 56rpx;
		width: 80rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 50%;
		box-shadow: 0 6rpx 20rpx rgba(0, 0, 0, 0.15);
		flex-shrink: 0;
	}
	
	.level-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.level-title {
		font-size: 32rpx;
		color: #333;
		font-weight: bold;
	}
	
	.level-range {
		font-size: 24rpx;
		color: #666;
	}
	
	.level-badge {
		background: linear-gradient(135deg, #667eea, #764ba2);
		color: #fff;
		font-size: 24rpx;
		font-weight: bold;
		padding: 12rpx 20rpx;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 15rpx rgba(102, 126, 234, 0.4);
	}
	
	/* 等级描述 */
	.level-description {
		font-size: 28rpx;
		color: #666;
		line-height: 1.6;
		padding-left: 104rpx;
	}
	
	/* 等级特殊样式 */
	.level-1 .level-icon {
		background: linear-gradient(135deg, #f8f9fa, #e9ecef);
		color: #6c757d;
	}
	
	.level-2 .level-icon {
		background: linear-gradient(135deg, #d4edda, #c3e6cb);
		color: #155724;
	}
	
	.level-3 .level-icon {
		background: linear-gradient(135deg, #d1ecf1, #bee5eb);
		color: #0c5460;
	}
	
	.level-4 .level-icon {
		background: linear-gradient(135deg, #e2d9f3, #d1b3f4);
		color: #6f42c1;
	}
	
	.level-5 .level-icon {
		background: linear-gradient(135deg, #f8d7da, #f5c6cb);
		color: #721c24;
	}
	
	/* 提示信息 */
	.tip-section {
		background: rgba(255, 255, 255, 0.9);
		border-radius: 20rpx;
		padding: 32rpx;
		text-align: center;
		box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
	}
	
	.tip-icon {
		font-size: 40rpx;
		margin-bottom: 16rpx;
	}
	
	.tip-text {
		font-size: 28rpx;
		color: #666;
		line-height: 1.5;
	}
	
	/* 动画 */
	@keyframes float {
		0%, 100% {
			transform: translateY(0);
		}
		50% {
			transform: translateY(-10rpx);
		}
	}
</style>




