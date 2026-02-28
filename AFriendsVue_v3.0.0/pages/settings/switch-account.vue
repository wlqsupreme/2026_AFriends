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
			<text class="nav-title">{{ $t('switchAccount.title') }}</text>
		</view> -->
		
		<!-- 当前账号 -->
		<view class="current-account">
			<view class="account-header">
				<text class="header-text">{{ $t('switchAccount.currentAccount') }}</text>
			</view>
			<view class="account-item current">
				<view class="account-left">
					<view class="account-avatar">👩</view>
					<view class="account-info">
						<text class="account-name">{{ currentAccount.name }}</text>
						<text class="account-phone">{{ currentAccount.phone }}</text>
					</view>
				</view>
				<view class="current-badge">{{ $t('switchAccount.currentBadge') }}</view>
			</view>
		</view>
		
		<!-- 其他账号 -->
		<view class="other-accounts">
			<view class="account-header">
				<text class="header-text">{{ $t('switchAccount.otherAccounts') }}</text>
			</view>
			<view class="account-item" v-for="account in otherAccounts" :key="account.id" @click="switchToAccount(account.id)">
				<view class="account-left">
					<view class="account-avatar">{{ account.avatar }}</view>
					<view class="account-info">
						<text class="account-name">{{ account.name }}</text>
						<text class="account-phone">{{ account.phone }}</text>
					</view>
				</view>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 添加账号 -->
		<view class="add-account">
			<view class="account-item" @click="addNewAccount">
				<view class="account-left">
					<view class="account-avatar add-icon">+</view>
					<view class="account-info">
						<text class="account-name">{{ $t('switchAccount.addAccount') }}</text>
						<text class="account-phone">{{ $t('switchAccount.addAccountDesc') }}</text>
					</view>
				</view>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 底部操作按钮 -->
		<view class="bottom-actions">
			<view class="action-button logout" @click="logoutCurrent">
				<text class="action-text">{{ $t('switchAccount.logoutCurrent') }}</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import accountManager from '@/utils/accountManager.js';
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		data() {
			return {
				currentAccount: {
					name: '',
					phone: '',
					id: 'current'
				},
				otherAccounts: []
			}
		},
		onLoad() {
			this.loadAccounts();
			
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			loadAccounts() {
				// 获取当前登录的账号信息
				const currentUsername = uni.getStorageSync('username');
				if (currentUsername) {
					this.currentAccount = {
						name: currentUsername,
						phone: this.$t('switchAccount.loggedIn'),
						id: 'current'
					};
				} else {
					this.currentAccount = {
						name: this.$t('switchAccount.defaultUserName'),
						phone: this.$t('switchAccount.defaultPhone'),
						id: 'current'
					};
				}
				
				// 获取其他保存的账号
				const savedAccounts = accountManager.getSavedAccounts();
				this.otherAccounts = savedAccounts
					.filter(account => account.username !== currentUsername)
					.map(account => ({
						id: account.id,
						name: account.username,
						phone: account.phone || this.$t('switchAccount.phoneMasked'),
						avatar: this.getAvatarByName(account.username)
					}));
			},
			getAvatarByName(name) {
				// 根据用户名生成头像字符
				const avatars = ['👨', '👧', '👤', '🧑', '👩', '👴', '👵'];
				const index = name.charCodeAt(0) % avatars.length;
				return avatars[index];
			},
			switchToAccount(accountId) {
				// 查找目标账号
				const targetAccount = this.otherAccounts.find(acc => acc.id == accountId);
				if (!targetAccount) {
					uni.showToast({
						title: this.$t('switchAccount.targetNotFound'),
						icon: 'none'
					});
					return;
				}
				
				uni.showModal({
					title: this.$t('switchAccount.switchToAccount'),
					content: this.$t('switchAccount.switchToAccountConfirm', { name: targetAccount.name }),
					success: (res) => {
						if (res.confirm) {
							// 保存当前账号信息
							const currentUsername = uni.getStorageSync('username');
							if (currentUsername) {
								accountManager.saveAccount({
									username: currentUsername,
									userId: uni.getStorageSync('userId'),
									phone: this.$t('switchAccount.loggedIn')
								});
							}
							
							// TODO: 这里应该调用后端登录接口
							// 暂时用注释代替实际的后端调用
							/*
							uni.request({
								url: '${this.$baseUrl}/api/user-profile/switch-account',
								method: 'POST',
								data: {
									username: targetAccount.name,
									// password: '需要用户输入密码' // 安全考虑，不应自动填充密码
								},
								success: (res) => {
									if (res.data.success) {
										// 保存新账号信息
										uni.setStorageSync('userId', res.data.userId);
										uni.setStorageSync('username', targetAccount.name);
										
										uni.showToast({
											title: this.$t('switchAccount.switchSuccess'),
											icon: 'success'
										});
										
										// 跳转到首页
										setTimeout(() => {
											uni.redirectTo({
												url: '/pages/feed/content-feed'
											});
										}, 1500);
									} else {
										uni.showToast({
											title: res.data.message || '切换失败',
											icon: 'none'
										});
									}
								},
								fail: (err) => {
									uni.showToast({
										title: '网络错误',
										icon: 'none'
									});
								}
							});
							*/
							
							// 切换账号：跳转到登录页重新登录（带上用户名预填）
							uni.removeStorageSync('userId');
							uni.removeStorageSync('username');
							uni.removeStorageSync('userToken');
							
							uni.navigateTo({
								url: `/pages/login/login-replica?switchAccount=true&username=${encodeURIComponent(targetAccount.name)}`
							});
						}
					}
				});
			},
			addNewAccount() {
				uni.navigateTo({
					url: '/pages/login/login-replica?switchAccount=true'
				});
			},
			logoutCurrent() {
				uni.showModal({
					title: this.$t('switchAccount.logoutCurrent'),
					content: this.$t('switchAccount.logoutConfirm'),
					success: (res) => {
						if (res.confirm) {
							// 保存当前账号信息
							const currentUsername = uni.getStorageSync('username');
							if (currentUsername) {
								accountManager.saveAccount({
									username: currentUsername,
									userId: uni.getStorageSync('userId'),
									phone: this.$t('switchAccount.loggedIn')
								});
							}
							
							// 清除当前登录状态
							uni.removeStorageSync('userId');
							uni.removeStorageSync('username');
							
							uni.showToast({
								title: this.$t('switchAccount.logoutSuccess'),
								icon: 'success'
							});
							
							// 延迟跳转到登录页面
							setTimeout(() => {
								uni.redirectTo({
									url: '/pages/login/login-replica'
								});
							}, 1500);
						}
					}
				});
			}
		}
	}
</script>

<style>
	.container {
		height: 100vh;
		background-color: var(--color-bg, #f8f8f8);
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
		padding: 20rpx 40rpx;
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
		color: var(--color-text-secondary, #666);
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
	
	/* 账号分组 */
	.account-header {
		background-color: var(--color-bg, #f8f8f8);
		padding: 20rpx 40rpx;
	}
	
	.header-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666);
		font-weight: 500;
	}
	
	/* 账号项 */
	.account-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 40rpx;
		background-color: var(--color-card, #fff);
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
	}
	
	.account-item:last-child {
		border-bottom: none;
	}
	
	.account-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.account-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: var(--color-bg-weak, #f0f0f0);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		color: var(--color-text-secondary, #666);
	}
	
	.add-icon {
		background-color: #e8f5e8;
		color: #28a745;
		font-size: 40rpx;
		font-weight: bold;
	}
	
	.account-info {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.account-name {
		font-size: 32rpx;
		color: var(--color-text, #333);
		font-weight: 500;
	}
	
	.account-phone {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999);
	}
	
	.current-badge {
		background-color: #007AFF;
		color: #fff;
		font-size: 24rpx;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.arrow {
		font-size: 36rpx;
		color: #ccc;
		font-weight: bold;
	}
	
	/* 当前账号样式 */
	.current-account {
		margin-bottom: 20rpx;
	}
	
	.current-account .account-item {
		background-color: var(--color-card, #fff);
	}
	
	/* 其他账号样式 */
	.other-accounts {
		margin-bottom: 20rpx;
	}
	
	.other-accounts .account-item {
		background-color: var(--color-card, #fff);
	}
	
	/* 添加账号样式 */
	.add-account {
		margin-bottom: 20rpx;
	}
	
	.add-account .account-item {
		background-color: var(--color-card, #fff);
	}
	
	/* 底部操作按钮 */
	.bottom-actions {
		padding: 40rpx;
		background-color: var(--color-bg, #f8f8f8);
	}
	
	.action-button {
		width: 100%;
		text-align: center;
		padding: 30rpx 0;
		border-radius: 10rpx;
		background-color: var(--color-card, #fff);
		border: 2rpx solid #ff6b6b;
	}
	
	.action-text {
		font-size: 32rpx;
		color: #ff6b6b;
		font-weight: 500;
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
</style>
