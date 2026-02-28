<template>
	<view class="ai-page" :class="themeStore.themeClass">
		<view class="header">
			<view class="btn back" @click="openMenu"></view>
			<view class="btn settings"></view>
		</view>
		<view class="profile">
			<view class="avatar-wrap">
				<image class="avatar" src="" mode="aspectFill"></image>
			</view>
			<text class="name">相亲AI</text>
			<text class="level">lv10 经验2000/4000</text>
		</view>
		<view class="sections">
			<view class="cell" @click="goToAIImage">
				<view class="cell-icon"></view>
				<text class="cell-title">AI形象</text>
			</view>
			<view class="cell">
				<view class="cell-icon"></view>
				<text class="cell-title">任务</text>
			</view>
		</view>
		
		<!-- 左侧菜单面板 -->
		<view class="side-menu" :class="{ 'active': showSideMenu }" @click="closeSideMenu">
			<view class="menu-content" @click.stop>
				<!-- 菜单头部 -->
				<view class="menu-header">
					<view class="menu-close" @click="closeSideMenu">
						<text class="close-icon">×</text>
					</view>
				</view>
				
				<!-- 菜单选项列表 -->
				<view class="menu-list">
					<!-- 我的AI -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyAI">
							<text class="menu-text">我的AI</text>
						</view>
					</view>
					
					<!-- 我的认证、喜恶、成就 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyCertification">
							<text class="menu-text">我的认证</text>
						</view>
						<view class="menu-item" @click="goToMyPreferences">
							<text class="menu-text">我的喜恶</text>
						</view>
						<view class="menu-item" @click="goToMyAchievements">
							<text class="menu-text">我的成就</text>
						</view>
					</view>
					
					<!-- 相册、历史评论、浏览记录、草稿箱 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToAlbum">
							<text class="menu-text">相册</text>
						</view>
						<view class="menu-item" @click="goToHistoryComments">
							<text class="menu-text">互动消息</text>
						</view>
						<view class="menu-item" @click="goToBrowsingHistory">
							<text class="menu-text">浏览记录</text>
						</view>
						<view class="menu-item" @click="goToDraftBox">
							<text class="menu-text">草稿箱</text>
						</view>
					</view>
					
					<!-- 购买记录、钱包 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToPurchaseRecord">
							<text class="menu-text">购买记录</text>
						</view>
						<view class="menu-item" @click="goToWallet">
							<text class="menu-text">钱包</text>
						</view>
					</view>
					
					<!-- 编辑资料 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToEditProfile">
							<text class="menu-text">编辑资料</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';

	export default {
		name: 'AiPage',
		data() {
			return {
				themeStore: useThemeStore(),
				showSideMenu: false // 控制左侧菜单显示
			}
		},
		onLoad() {
			this.themeStore.init();
			this.themeStore.applyTheme();
		},
		methods: {
			openMenu() {
				this.showSideMenu = true;
			},
			closeSideMenu() {
				this.showSideMenu = false;
			},
			// 侧边菜单相关方法
			goToMyAI() {
				uni.navigateTo({
					url: '/pages/ai/ai'
				});
			},
			goToMyCertification() {
				uni.navigateTo({
					url: '/pages/verification/official-certification'
				});
			},
			goToMyPreferences() {
				uni.navigateTo({
					url: '/pages/feed/likes-dislikes'
				});
			},
			goToMyAchievements() {
				uni.navigateTo({
					url: '/pages/feed/achievements'
				});
			},
			goToAlbum() {
				uni.showToast({
					title: '相册功能开发中',
					icon: 'none'
				});
			},
			goToHistoryComments() {
				uni.navigateTo({
					url: '/pages/feed/interaction-messages'
				});
			},
			goToBrowsingHistory() {
				uni.showToast({
					title: '浏览记录功能开发中',
					icon: 'none'
				});
			},
			goToDraftBox() {
				uni.showToast({
					title: '草稿箱功能开发中',
					icon: 'none'
				});
			},
			goToPurchaseRecord() {
				uni.navigateTo({
					url: '/pages/wallet/purchase-record'
				});
			},
			goToShoppingCart() {
				uni.showToast({
					title: '购物车功能开发中',
					icon: 'none'
				});
			},
			goToWallet() {
				uni.navigateTo({
					url: '/pages/wallet/wallet'
				});
			},
			goToEditProfile() {
				uni.showToast({
					title: '编辑资料功能开发中',
					icon: 'none'
				});
			},
			goToAIImage() {
				console.log('点击AI形象，准备跳转');
				uni.navigateTo({
					url: '/pages/ai/gift'
				});
			}
		}
	}
</script>

<style>
	.ai-page {
		min-height: 100vh;
		background-color: var(--color-bg, #ffffff);
		color: var(--color-text, #11141f);
	}

	.header {
		height: 88rpx;
		padding: 0 32rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.btn {
		width: 48rpx;
		height: 48rpx;
		border-radius: 24rpx;
		background-color: var(--color-bg-weak, #f7f7f9);
	}

	.profile {
		padding-top: 40rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.avatar-wrap {
		width: 180rpx;
		height: 180rpx;
		border-radius: 90rpx;
		overflow: hidden;
		background-color: var(--color-bg-weak, #f7f7f9);
	}

	.avatar {
		width: 100%;
		height: 100%;
	}

	.name {
		margin-top: 24rpx;
		font-size: 36rpx;
		color: var(--color-text, #11141f);
		font-weight: 600;
	}

	.level {
		margin-top: 8rpx;
		font-size: 22rpx;
		color: var(--color-text-secondary, #666c7a);
	}

	.sections {
		margin-top: 80rpx;
		background-color: var(--color-card, #ffffff);
	}

	.cell {
		height: 110rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1px solid var(--color-border, #e5e7ec);
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.cell:active {
		background-color: var(--color-bg-weak, #f7f7f9);
	}

	.cell-icon {
		width: 44rpx;
		height: 44rpx;
		border-radius: 8rpx;
		background-color: var(--color-bg-weak, #f7f7f9);
		margin-right: 20rpx;
	}

	.cell-title {
		font-size: 30rpx;
		color: var(--color-text, #11141f);
	}
	
	/* 左侧菜单面板 */
	.side-menu {
		position: fixed;
		top: 0;
		left: -100%;
		width: 100%;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		transition: left 0.3s ease;
	}
	
	.side-menu.active {
		left: 0;
	}
	
	.menu-content {
		position: absolute;
		top: 0;
		left: 0;
		width: 600rpx;
		height: 100vh;
		background-color: var(--color-card, #ffffff);
		box-shadow: 4rpx 0 20rpx rgba(0, 0, 0, 0.1);
		display: flex;
		flex-direction: column;
	}

	.ai-page.theme-dark .menu-content {
		box-shadow: 4rpx 0 20rpx rgba(0, 0, 0, 0.6);
	}
	
	/* 菜单头部 */
	.menu-header {
		display: flex;
		justify-content: flex-end;
		padding: 40rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
	}
	
	.menu-close {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.close-icon {
		font-size: 48rpx;
		color: var(--color-text-secondary, #666c7a);
		font-weight: bold;
	}
	
	/* 菜单列表 */
	.menu-list {
		flex: 1;
		padding: 0;
		background-color: var(--color-bg-weak, #f7f7f9);
	}
	
	.menu-section {
		margin-bottom: 20rpx;
		background-color: var(--color-card, #ffffff);
	}
	
	.menu-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.menu-item:last-child {
		border-bottom: none;
	}
	
	.menu-item:active {
		background-color: var(--color-bg-weak, #f7f7f9);
	}
	
	.menu-text {
		font-size: 30rpx;
		color: var(--color-text, #11141f);
		font-weight: 500;
	}
</style>


