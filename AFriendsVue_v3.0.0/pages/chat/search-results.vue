<template>
	<view class="search-results-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 页面标题 -->
		<!-- <view class="page-title">
			<text class="title-text">搜索结果</text>
		</view> -->
		
		<!-- 搜索栏区域 -->
		<view class="search-bar-area">
			<view class="search-input-field">
				<text class="search-text">意大利面</text>
			</view>
			<view class="cancel-btn">
				<text class="cancel-text">取消</text>
			</view>
		</view>
		
		<!-- 标签页导航 -->
		<view class="tab-navigation">
			<view class="tab-item active">
				<text class="tab-text">全部</text>
			</view>
			<view class="tab-item">
				<text class="tab-text">图片</text>
			</view>
			<view class="tab-item">
				<text class="tab-text">AI</text>
			</view>
			<view class="tab-item">
				<text class="tab-text">链接</text>
			</view>
			<view class="tab-item">
				<text class="tab-text">音乐与音频</text>
			</view>
		</view>
		
		<!-- 搜索结果列表 -->
		<view class="search-results-list">
			<!-- 第一个搜索结果 -->
			<view class="result-item">
				<view class="item-left">
					<image class="profile-pic" src="" mode="aspectFill"></image>
				</view>
				<view class="item-content">
					<text class="item-title">厚乳抹茶蛋糕</text>
					<view class="item-snippet">
						<text class="snippet-text">问题的。我认为</text>
						<text class="highlighted-text">意大利面</text>
						<text class="snippet-text">就应该拌四十二号混凝土</text>
					</view>
				</view>
				<view class="item-right">
					<text class="item-date">8月5日</text>
				</view>
			</view>
			
			<!-- 第二个搜索结果 -->
			<view class="result-item">
				<view class="item-left">
					<image class="profile-pic" src="" mode="aspectFill"></image>
				</view>
				<view class="item-content">
					<text class="item-title">猫又屋之主(AI)</text>
					<view class="item-snippet">
						<text class="snippet-text">就</text>
						<text class="highlighted-text">意大利面</text>
						<text class="snippet-text">就行吧</text>
					</view>
				</view>
				<view class="item-right">
					<text class="item-date">7月13日</text>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'SearchResultsPage',
		data() {
			return {
				
			}
		}
	}
</script>

<style>
	.search-results-page {
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
	
	/* 页面标题 */
	.page-title {
		padding: 24rpx 32rpx 16rpx;
		text-align: center;
	}
	
	.title-text {
		font-size: 32rpx;
		color: #000000;
		font-weight: 600;
	}
	
	/* 搜索栏区域 */
	.search-bar-area {
		padding: 24rpx 32rpx;
		display: flex;
		align-items: center;
		gap: 24rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.search-input-field {
		flex: 1;
		height: 80rpx;
		background-color: #E0E0E0;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
	}
	
	.search-text {
		font-size: 28rpx;
		color: #333333;
	}
	
	.cancel-btn {
		padding: 16rpx 0;
	}
	
	.cancel-text {
		font-size: 32rpx;
		color: #007AFF;
	}
	
	/* 标签页导航 */
	.tab-navigation {
		display: flex;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.tab-item {
		padding: 24rpx 16rpx;
		position: relative;
	}
	
	.tab-text {
		font-size: 28rpx;
		color: #999999;
	}
	
	.tab-item.active .tab-text {
		color: #333333;
		font-weight: 600;
	}
	
	.tab-item.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 40rpx;
		height: 4rpx;
		background-color: #007AFF;
		border-radius: 2rpx;
	}
	
	/* 搜索结果列表 */
	.search-results-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.result-item {
		display: flex;
		gap: 24rpx;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.item-left {
		flex-shrink: 0;
	}
	
	.profile-pic {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5; /* 头像占位 */
	}
	
	.item-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.item-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
		line-height: 1.4;
	}
	
	.item-snippet {
		display: flex;
		flex-wrap: wrap;
		gap: 4rpx;
	}
	
	.snippet-text {
		font-size: 24rpx;
		color: #666666;
		line-height: 1.4;
	}
	
	.highlighted-text {
		font-size: 24rpx;
		color: #FF69B4;
		font-weight: 600;
		line-height: 1.4;
	}
	
	.item-right {
		flex-shrink: 0;
		align-self: flex-start;
	}
	
	.item-date {
		font-size: 24rpx;
		color: #999999;
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
