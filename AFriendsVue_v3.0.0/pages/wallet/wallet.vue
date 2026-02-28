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
		<!-- <view class="nav-bar">
			<view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">{{ $t('wallet.title') }}</text>
		</view> -->
		
		<!-- 钱包选项列表 -->
		<view class="wallet-options">
			<!-- 余额 -->
			<view class="option-item">
				<view class="option-left">
					<view class="option-icon balance-icon">$</view>
					<text class="option-text">{{ $t('wallet.balance') }}</text>
				</view>
				<text class="balance-amount">{{ formatBalance(balance) }}</text>
			</view>
			
			<!-- 充值 -->
			<view class="option-item" @click="goToRecharge">
				<view class="option-left">
					<view class="option-icon recharge-icon">💳</view>
					<text class="option-text">{{ $t('wallet.recharge') }}</text>
				</view>
				<text class="arrow">›</text>
			</view>
			
			<!-- 账单 -->
			<view class="option-item" @click="goToBill">
				<view class="option-left">
					<view class="option-icon bill-icon">📄</view>
					<text class="option-text">{{ $t('wallet.bill') }}</text>
				</view>
				<text class="arrow">›</text>
			</view>
			
			<!-- 客服与支持 -->
			<view class="option-item" @click="goToCustomerService">
				<view class="option-left">
					<view class="option-icon service-icon">🎧</view>
					<text class="option-text">{{ $t('wallet.customerService') }}</text>
				</view>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 底部服务条款 -->
		<view class="bottom-section">
			<text class="service-terms" @click="goToServiceTerms">{{ $t('wallet.serviceTerms') }}</text>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		data() {
			return {
				userId: 1000100, // 默认用户ID
				balance: 10000000, // 钱包余额
				loading: false,
				errorMessage: '',
				hasError: false
			}
		},
		onLoad(options) {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			
			// 接收从其他页面传递的userId参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('钱包页面接收到用户ID:', this.userId);
			}
			// 加载钱包数据
			this.loadWalletData();
		},
		methods: {
			// 加载钱包数据
			async loadWalletData() {
				try {
					console.log('=== 开始加载钱包数据 ===');
					console.log('用户ID:', this.userId);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/wallet/balance?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 钱包余额API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const balanceData = response.data.data;
						this.balance = balanceData.currentBalance;
						console.log('成功加载钱包余额:', this.balance);
						
						uni.showToast({
							title: '钱包数据加载成功',
							icon: 'success',
							duration: 1500
						});
					} else {
						console.error('加载钱包余额失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载钱包数据失败';
						uni.showToast({
							title: '加载钱包数据失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载钱包数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			goBack() {
				uni.navigateBack();
			},
			goToRecharge() {
				uni.navigateTo({
					url: `/pages/wallet/recharge?userId=${this.userId}`
				});
			},
			goToBill() {
				uni.navigateTo({
					url: `/pages/wallet/purchase-record?userId=${this.userId}`
				});
			},
			goToCustomerService() {
				uni.navigateTo({
					url: '/pages/chat/chat?friendName=客服'
				});
			},
			goToServiceTerms() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			// 格式化余额显示
			formatBalance(balance) {
				if (balance >= 10000) {
					return (balance / 10000).toFixed(1) + 'wC';
				} else if (balance >= 1000) {
					return (balance / 1000).toFixed(1) + 'kC';
				} else {
					return balance + 'C';
				}
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
		background-color: var(--color-bg, #fff);
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
		background-color: var(--color-bg, #fff);
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
	
	/* 钱包选项列表 */
	.wallet-options {
		flex: 1;
		background-color: var(--color-bg, #fff);
	}
	
	.option-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		background-color: var(--color-bg, #fff);
	}
	
	.option-item:last-child {
		border-bottom: none;
	}
	
	.option-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.option-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: var(--color-bg-light, #f0f0f0);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		color: var(--color-text-secondary, #666);
	}
	
	.option-text {
		font-size: 30rpx;
		color: var(--color-text, #333);
	}
	
	.balance-amount {
		font-size: 32rpx;
		color: var(--color-text, #333);
		font-weight: bold;
	}
	
	.arrow {
		font-size: 36rpx;
		color: var(--color-text-placeholder, #ccc);
		font-weight: bold;
	}
	
	/* 底部服务条款 */
	.bottom-section {
		padding: 40rpx;
		display: flex;
		justify-content: center;
		background-color: var(--color-bg, #fff);
	}
	
	.service-terms {
		color: var(--color-link, #007AFF);
		font-size: 28rpx;
		cursor: pointer;
	}
</style>