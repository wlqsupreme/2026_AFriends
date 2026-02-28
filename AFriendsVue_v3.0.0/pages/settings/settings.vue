<template>
	<view class="settings-page" :class="[themeStore.themeClass, { care: isCare }]">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">设置</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 设置内容 -->
		<view class="settings-content">
			<!-- 我的账户 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">{{ $t('settings.myAccount') }}</text>
				</view>
				<view class="section-items">
					<view class="setting-item">
						<text class="item-text">{{ $t('settings.darkMode') }}</text>
						<view class="toggle-switch" :class="{ active: isDark }" @click="toggleDarkMode">
							<view class="toggle-circle"></view>
						</view>
					</view>
					<view class="setting-item" @click="goToSwitchAccount">
						<text class="item-text">{{ $t('settings.switchAccount') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToAccountSecurity">
						<text class="item-text">{{ $t('settings.accountAndSecurity') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToModeSelection">
						<text class="item-text">{{ $t('settings.modeSelection') }}</text>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
			
			<!-- 账户设置 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">{{ $t('settings.accountSettings') }}</text>
				</view>
				<view class="section-items">
					<view class="setting-item" @click="goToNotifications">
						<text class="item-text">{{ $t('settings.notifications') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToPrivacySettings">
						<text class="item-text">{{ $t('settings.privacySettings') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToLanguage">
						<text class="item-text">{{ $t('language.title') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToBlockedUsers">
						<text class="item-text">{{ $t('settings.blockedUsers') }}</text>
						<view class="arrow-right"></view>
					</view>
					<!-- <view class="setting-item" @click="goToChatSettings">
						<text class="item-text">聊天设置</text>
						<view class="arrow-right"></view>
					</view> -->
				</view>
			</view>
			
			<!-- 帮助 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">{{ $t('settings.help') }}</text>
				</view>
				<view class="section-items">
					<view class="setting-item" @click="goToTermsOfService">
						<text class="item-text">{{ $t('settings.termsOfService') }}</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToCustomerService">
						<text class="item-text">{{ $t('settings.customerService') }}</text>
						<view class="arrow-right"></view>
					</view>
					<!-- <view class="setting-item" @click="goToUsageTutorial">
						<text class="item-text">使用教程</text>
						<view class="arrow-right"></view>
					</view> -->
				</view>
			</view>
			
			<!-- 账户操作 -->
			<view class="account-actions">
				<view class="action-button logout-account" @click="logoutAccount">
					<text class="action-text">{{ $t('settings.logoutAccount') }}</text>
				</view>
				<view class="action-button sign-out" @click="signOut">
					<text class="action-text">{{ $t('settings.signOut') }}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<!-- <view class="bottom-navigation">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToChatList">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAIChat">
				<view class="ai-tab">
					<text class="ai-text">AI</text>
				</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-avatar" @click="goToProfile">
				<image class="avatar-small" src="" mode="aspectFill"></image>
			</view>
		</view> -->
	</view>
</template>

<script>
	import { useThemeStore } from '../../store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'SettingsPage',
		data() {
			return {
				themeStore: useThemeStore(),
				modeStore: null
			}
		},
		onLoad() {
			// 页面加载时保证主题已初始化并应用
			this.themeStore.init();
			this.themeStore.applyTheme();
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
		},
		computed: {
			isDark() {
				return this.themeStore.isDark;
			},
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/content-feed'
				});
			},
			goToChatList() {
				uni.navigateTo({
					url: '/pages/chat/chat-list'
				});
			},
			goToAIChat() {
				uni.navigateTo({
					url: '/pages/chat/chat'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			},
			/**
			 * 切换暗夜模式
			 * 统一由 Pinia Store 管理并应用
			 */
			toggleDarkMode() {
				this.themeStore.toggle();
				
				// 提示用户（保持原有交互）
				uni.showToast({
					title: this.isDark ? this.$t('settings.darkModeEnabled') : this.$t('settings.darkModeDisabled'),
					icon: 'none',
					duration: 1500
				});
			},
			goToSwitchAccount() {
				uni.navigateTo({
					url: '/pages/settings/switch-account'
				});
			},
			goToAccountSecurity() {
				uni.navigateTo({
					url: '/pages/settings/account-security'
				});
			},
			goToModeSelection() {
				uni.navigateTo({
					url: '/pages/settings/mode-selection'
				});
			},
			goToNotifications() {
				uni.navigateTo({
					url: '/pages/settings/notification-settings'
				});
			},
			goToPrivacySettings() {
				uni.navigateTo({
					url: '/pages/settings/privacy-settings'
				});
			},
			goToLanguage() {
				uni.navigateTo({
					url: '/pages/settings/language'
				});
			},
			goToBlockedUsers() {
				uni.navigateTo({
					url: '/pages/settings/blocked-users'
				});
			},
			goToChatSettings() {
				uni.navigateTo({
					url: '/pages/settings/chat-settings'
				});
			},
			goToTermsOfService() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			async goToCustomerService() {
        // 1. 获取当前用户ID
        const userId = uni.getStorageSync('userId');

        if (!userId) {
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          });
          return;
        }

        try {
          // 2. 请求创建客服会话
          const response = await uni.request({
            url: `${this.$baseUrl}/api/customer-service/create-session`,
            method: 'POST',
            header: {
              'Content-Type': 'application/json'
            },
            data: {
              userId: userId
            }
          });

          if (response.statusCode === 200 && response.data.success) {
            // 3. 获取到sessionId后跳转到聊天页面
            const sessionId = response.data.sessionId;
            uni.navigateTo({
              url: `/pages/chat/chat?friendName=客服&sessionId=${sessionId}`
            });
          } else {
            uni.showToast({
              title: response.data.message || '创建客服会话失败',
              icon: 'none'
            });
          }
        } catch (error) {
          uni.showToast({
            title: '网络错误，请稍后重试',
            icon: 'none'
          });
        }
      },
			goToUsageTutorial() {
				uni.navigateTo({
					url: '/pages/settings/usage-tutorial'
				});
			},
			async logoutAccount() {
				uni.showModal({
					title: this.$t('settings.confirmLogout'),
					content: this.$t('settings.logoutConfirmMessage'),
					success: async (res) => {
						if (res.confirm) {
							await this.performLogout('logoutAccount');
						}
					}
				});
			},
			async signOut() {
				uni.showModal({
					title: this.$t('settings.confirmSignOut'),
					content: this.$t('settings.signOutConfirmMessage'),
					success: async (res) => {
						if (res.confirm) {
							await this.performLogout('signOut');
						}
					}
				});
			},
			/**
			 * 执行登出操作
			 * @param {String} type - 登出类型：'logoutAccount' 或 'signOut'
			 */
			async performLogout(type) {
				try {
					// 显示加载提示
					uni.showLoading({
						title: '正在登出...',
						mask: true
					});

					// 获取 token（从本地存储）
					const token = uni.getStorageSync('token') || uni.getStorageSync('userToken');
					
					if (!token) {
						// 如果没有 token，直接清除本地数据并跳转
						console.log('未找到 token，直接清除本地数据');
						this.clearLocalData();
						uni.hideLoading();
						uni.showToast({
							title: type === 'logoutAccount' ? this.$t('settings.accountLoggedOut') : this.$t('settings.loggedOut'),
							icon: 'success',
							duration: 1500
						});
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login-replica'
							});
						}, 1500);
						return;
					}

					// 调用后端登出接口
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/logout`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							token: token
						}
					});

					uni.hideLoading();

					if (response.statusCode === 200 && response.data.success) {
						console.log('登出成功');
						// 清除本地数据
						this.clearLocalData();
						
						uni.showToast({
							title: type === 'logoutAccount' ? this.$t('settings.accountLoggedOut') : this.$t('settings.loggedOut'),
							icon: 'success',
							duration: 1500
						});
						
						// 延迟跳转到登录页面
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login-replica'
							});
						}, 1500);
					} else {
						// 即使后端登出失败，也清除本地数据并跳转
						console.warn('后端登出失败，但仍清除本地数据:', response.data.message);
						this.clearLocalData();
						
						uni.showToast({
							title: type === 'logoutAccount' ? this.$t('settings.accountLoggedOut') : this.$t('settings.loggedOut'),
							icon: 'success',
							duration: 1500
						});
						
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login-replica'
							});
						}, 1500);
					}
				} catch (error) {
					console.error('登出异常:', error);
					uni.hideLoading();
					
					// 即使发生异常，也清除本地数据并跳转
					this.clearLocalData();
					
					uni.showToast({
						title: type === 'logoutAccount' ? this.$t('settings.accountLoggedOut') : this.$t('settings.loggedOut'),
						icon: 'success',
						duration: 1500
					});
					
					setTimeout(() => {
						uni.reLaunch({
							url: '/pages/login/login-replica'
						});
					}, 1500);
				}
			},
			/**
			 * 清除本地存储的用户数据
			 */
			clearLocalData() {
				try {
					// 清除 token 相关数据
					uni.removeStorageSync('token');
					uni.removeStorageSync('userToken');
					uni.removeStorageSync('userId');
					
					// 清除用户信息缓存
					const keys = uni.getStorageInfoSync().keys;
					keys.forEach(key => {
						if (key.startsWith('user_') || key.startsWith('user_base_info_')) {
							uni.removeStorageSync(key);
						}
					});
					
					console.log('本地数据已清除');
				} catch (error) {
					console.error('清除本地数据失败:', error);
					// 如果清除失败，尝试全部清除
					try {
						uni.clearStorageSync();
					} catch (e) {
						console.error('全部清除存储失败:', e);
					}
				}
			}
		}
	}
</script>

<style>
	/* 主题类 + Fallback，确保暗夜模式在小程序/H5均生效 */
	.settings-page.theme-dark {
		background-color: var(--color-bg, #0f1115);
		color: var(--color-text, #e9edf5);
	}
	.settings-page.theme-dark .section-header {
		background-color: var(--color-bg-weak, #141722);
	}
	.settings-page.theme-dark .section-items {
		background-color: var(--color-card, #161a24);
	}
	.settings-page.theme-dark .setting-item {
		border-color: var(--color-border, #2a2f3a);
	}
	.settings-page.theme-dark .item-text {
		color: var(--color-text, #e9edf5);
	}
	.settings-page.theme-dark .toggle-switch {
		background-color: var(--switch-bg, #2d3342);
	}
	.settings-page.theme-dark .toggle-switch.active {
		background-color: var(--switch-bg-active, #7ba4ff);
	}
	.settings-page.theme-dark .toggle-circle {
		background-color: var(--color-bg, #0f1115);
	}
	.settings-page.theme-dark .section-title,
	.settings-page.theme-dark .nav-text {
		color: var(--color-text-secondary, #a7afc0);
	}

	.settings-page {
		min-height: 100vh;
		background-color: var(--color-bg);
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
		background-color: var(--color-bg);
	}
	
	.time {
		font-size: 28rpx;
		color: var(--color-text);
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
		background-color: var(--color-text);
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border);
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: var(--color-text);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 设置内容 */
	.settings-content {
		flex: 1;
		padding: 24rpx 0;
	}
	
	.settings-section {
		margin-bottom: 32rpx;
	}
	
	.section-header {
		padding: 16rpx 32rpx;
		background-color: var(--color-bg-weak);
	}
	
	.section-title {
		font-size: 26rpx;
		color: var(--color-text-secondary);
		font-weight: 500;
	}
	
	.section-items {
		background-color: var(--color-card);
	}
	
	.setting-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid var(--color-border);
	}
	
	.setting-item:last-child {
		border-bottom: none;
	}
	
	.item-text {
		font-size: 28rpx;
		color: var(--color-text);
		font-weight: 400;
	}
	
	/* 开关样式 */
	.toggle-switch {
		width: 80rpx;
		height: 44rpx;
		background-color: var(--switch-bg);
		border-radius: 22rpx;
		position: relative;
		transition: all 0.3s ease;
	}
	
	.toggle-switch.active {
		background-color: var(--switch-bg-active);
	}
	
	.toggle-circle {
		width: 40rpx;
		height: 40rpx;
		background-color: var(--color-bg);
		border-radius: 50%;
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		transition: all 0.3s ease;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}
	
	.toggle-switch.active .toggle-circle {
		left: 38rpx;
	}
	
	/* 右箭头 */
	.arrow-right {
		width: 0;
		height: 0;
		border-left: 8rpx solid var(--color-border);
		border-top: 6rpx solid transparent;
		border-bottom: 6rpx solid transparent;
	}
	
	/* 账户操作 */
	.account-actions {
		padding: 32rpx;
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}
	
	.action-button {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 16rpx;
		cursor: pointer;
	}
	
	.logout-account {
		background-color: var(--danger-bg);
	}
	
	.sign-out {
		background-color: var(--danger-bg);
	}
	
	.action-text {
		font-size: 28rpx;
		color: var(--color-on-danger);
		font-weight: 500;
	}
	
	/* 底部导航栏样式 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: var(--color-card);
		border-top: 1rpx solid var(--color-border);
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0 32rpx;
		z-index: 100;
	}
	
	.nav-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.nav-item:active {
		background-color: var(--color-bg-weak);
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: var(--color-text-secondary);
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: var(--color-primary);
		font-weight: 600;
	}
	
	.ai-tab {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, var(--color-primary), #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.ai-text {
		font-size: 20rpx;
		color: var(--color-on-primary);
		font-weight: 600;
	}
	
	.nav-avatar {
		width: 48rpx;
		height: 48rpx;
	}
	
	.avatar-small {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
	}
	
	.cat-avatar::before {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 8rpx;
		width: 8rpx;
		height: 8rpx;
		background-color: #000000;
		border-radius: 50%;
	}
	
	.cat-avatar::after {
		content: '';
		position: absolute;
		top: 8rpx;
		right: 8rpx;
		width: 8rpx;
		height: 8rpx;
		background-color: #000000;
		border-radius: 50%;
	}

	.settings-page.care .settings-content {
		padding: 40rpx 0;
		box-sizing: border-box;
	}

	.settings-page.care .section-header {
		padding: 24rpx 40rpx;
	}

	.settings-page.care .section-title {
		font-size: 34rpx;
	}

	.settings-page.care .setting-item {
		padding: 36rpx 40rpx;
		min-height: 112rpx;
		box-sizing: border-box;
	}

	.settings-page.care .item-text {
		font-size: 34rpx;
		line-height: 1.5;
	}

	.settings-page.care .toggle-switch {
		width: 110rpx;
		height: 60rpx;
		border-radius: 30rpx;
	}

	.settings-page.care .toggle-circle {
		width: 52rpx;
		height: 52rpx;
		top: 4rpx;
		left: 4rpx;
	}

	.settings-page.care .toggle-switch.active .toggle-circle {
		left: 54rpx;
	}

	.settings-page.care .arrow-right {
		border-left-width: 12rpx;
		border-top-width: 8rpx;
		border-bottom-width: 8rpx;
	}

	.settings-page.care .account-actions {
		padding: 40rpx;
		gap: 24rpx;
		box-sizing: border-box;
	}

	.settings-page.care .action-button {
		height: 112rpx;
		border-radius: 20rpx;
	}

	.settings-page.care .action-text {
		font-size: 34rpx;
	}
	

</style>


