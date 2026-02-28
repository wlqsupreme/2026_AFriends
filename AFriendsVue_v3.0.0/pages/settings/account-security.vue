<template>
	<view class="account-security-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">{{ $t('account.security.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 加载状态 -->
		<view v-if="isLoading" class="loading-container">
			<text class="loading-text">加载中...</text>
		</view>
		
		<!-- 安全设置列表 -->
		<view v-else class="security-list">
			<!-- 第一组：基础信息 -->
			<view class="list-group">
				<view class="list-item" @click="goToPhoneNumber">
					<text class="item-label">{{ $t('account.security.phoneNumber') }}</text>
					<view class="item-content">
						<text class="item-value">{{ displayPhoneNumber }}</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<view class="list-item" @click="goToPassword">
					<text class="item-label">{{ $t('account.security.password') }}</text>
					<view class="item-content">
						<text class="item-value">{{ passwordStatus }}</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第二组：第三方账号绑定 -->
			<view class="list-group">
				<view class="list-item" @click="goToThirdPartyBinding">
					<text class="item-label">{{ $t('account.security.thirdPartyBinding') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第三组：认证信息 -->
			<view class="list-group">
				<view class="list-item" @click="goToRealNameVerification">
					<text class="item-label">{{ $t('account.security.realNameVerification') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<view class="list-item" @click="goToOfficialCertification">
					<text class="item-label">{{ $t('account.security.officialCertification') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第四组：账户管理 -->
			<view class="list-group">
				<view class="list-item" @click="goToDeviceManagement">
					<text class="item-label">{{ $t('account.security.deviceManagement') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<!-- <view class="list-item" @click="goToAccountManagement">
					<text class="item-label">{{ $t('account.security.accountManagement') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view> -->
				<view class="divider"></view>
				<view class="list-item" @click="goToAccountDeletion">
					<text class="item-label">{{ $t('account.security.accountDeletion') }}</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
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
		
		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'AccountSecurityPage',
		data() {
			return {
				userId: 1000100, // 默认用户ID，实际应从登录状态获取
				phoneNumber: '', // 用户手机号
				hasPhoneBound: false, // 是否已绑定手机号
				hasPassword: false, // 是否已设置密码
				isLoading: true // 添加加载状态
			}
		},
		computed: {
			// 显示的手机号（脱敏处理）
			displayPhoneNumber() {
				if (!this.phoneNumber || this.phoneNumber === '') {
					return this.$t('account.security.notSet') || '未设置';
				}
				// 手机号脱敏显示：138****5678
				return this.phoneNumber.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
			},
			// 密码状态显示
			passwordStatus() {
				return this.hasPassword ? '已设置' : (this.$t('account.security.notSet') || '未设置');
			}
		},
		onLoad(options) {
			// 接收从其他页面传递的userId参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
			} else {
				// 从本地存储获取用户ID
				const storedUserId = uni.getStorageSync('userId');
				if (storedUserId) {
					this.userId = parseInt(storedUserId);
				}
			}
			console.log('账户安全页面 - 用户ID:', this.userId);
			
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
			
			// 页面加载时获取用户基础信息
			this.loadUserBaseInfo();
		},
		methods: {
			// 加载用户基础信息
			async loadUserBaseInfo() {
				try {
					console.log('开始加载用户基础信息，用户ID:', this.userId);
					
					// 尝试从缓存中获取用户信息
					const cacheKey = `user_base_info_${this.userId}`;
					const cachedData = uni.getStorageSync(cacheKey);
					
					if (cachedData) {
						console.log('从缓存中获取用户基础信息');
						this.phoneNumber = cachedData.loginTelAccount || '';
						this.hasPhoneBound = cachedData.hasPhoneBound || false;
						this.hasPassword = cachedData.hasPassword || false;
						this.isLoading = false;
						return;
					}
					
					// 缓存中没有数据，调用接口获取
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-base/${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('用户基础信息API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						this.phoneNumber = response.data.loginTelAccount || '';
						this.hasPhoneBound = response.data.hasPhoneBound || false;
						this.hasPassword = response.data.hasPassword || false;
						console.log('成功加载用户基础信息，手机号:', this.phoneNumber);
						
						// 将数据存入缓存
						uni.setStorageSync(cacheKey, {
							loginTelAccount: this.phoneNumber,
							hasPhoneBound: this.hasPhoneBound,
							hasPassword: this.hasPassword,
							timestamp: Date.now() // 添加时间戳，便于后续加入过期机制
						});
					} else {
						console.error('加载用户基础信息失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '加载失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('加载用户基础信息异常:', error);
					uni.showToast({
						title: '加载失败',
						icon: 'none'
					});
				} finally {
					// 无论成功还是失败，都将isLoading设为false
					this.isLoading = false;
				}
			},
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
			goToPhoneNumber() {
				// 传递用户ID和当前手机号信息
				uni.navigateTo({
					url: `/pages/settings/phone-number-setting?userId=${this.userId}&phoneNumber=${encodeURIComponent(this.phoneNumber || '')}&hasPhoneBound=${this.hasPhoneBound}`
				});
			},
			goToPassword() {
				// 传递用户ID和当前手机号信息
				uni.navigateTo({
					url: `/pages/login/reset-password?userId=${this.userId}&phoneNumber=${encodeURIComponent(this.phoneNumber || '')}`
				});
			},
			goToThirdPartyBinding() {
				uni.navigateTo({
					url: '/pages/settings/third-party-binding'
				});
			},
			goToRealNameVerification() {
				uni.navigateTo({
					url: '/pages/verification/real-name-verification'
				});
			},
			goToOfficialCertification() {
				uni.navigateTo({
					url: '/pages/verification/official-certification'
				});
			},
			goToDeviceManagement() {
				uni.navigateTo({
					url: '/pages/settings/device-management'
				});
			},
			goToAccountManagement() {
				uni.navigateTo({
					url: '/pages/settings/switch-account'
				});
			},
			goToAccountDeletion() {
				
				// 显示第一个确认对话框
				uni.showModal({
					title: '注销账户',
					content: '注销账户后，您的所有数据将被永久删除且无法恢复。确定要注销账户吗？',
					confirmText: '确定注销',
					cancelText: '取消',
					confirmColor: '#FF3B30',
					success: (res) => {

						if (res && res.confirm === true) {
							// 延迟显示第二个对话框，确保第一个对话框完全关闭
							setTimeout(() => {
								console.log('延迟后调用showSecondConfirm');
								this.showSecondConfirm();
							}, 500); // 延迟500ms
						}
					},
				});

			},
			// 显示第二个确认对话框
			showSecondConfirm() {
				
				// 直接显示第二个对话框，不使用额外的setTimeout（因为已经在第一个对话框回调中延迟了）
				uni.showModal({
					title: '最后确认',
					content: '此操作不可撤销，确定要注销账户吗？',
					confirmText: '确定注销',
					cancelText: '取消',
					confirmColor: '#FF3B30',
					success: (res) => {
						if (res && res.confirm === true) {
							
							// 使用箭头函数确保this正确绑定
							const self = this;
							if (typeof self.deleteAccount === 'function') {
								// 调用注销方法
								self.deleteAccount();
							} else {
								uni.showToast({
									title: '系统错误，请重试',
									icon: 'none'
								});
							}
						}
					},
				});

			},
			// 注销账户
			async deleteAccount() {
				
				try {
					// 检查用户ID是否存在
					if (!this.userId) {
						uni.showToast({
							title: '用户ID不存在',
							icon: 'none',
							duration: 2000
						});
						return;
					}

					// 显示加载提示
					uni.showLoading({
						title: '正在注销账户...',
						mask: true
					});
					
					// 调用注销账户接口
					const requestUrl = `${this.$baseUrl}/api/u-entities/user-base/delete-account`;
					const requestData = {
						userId: this.userId
					};

					
					const response = await uni.request({
						url: requestUrl,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: requestData
					});
					
					uni.hideLoading();
					
					if (response.statusCode === 200 && response.data.success) {
						// 注销成功
						uni.showToast({
							title: '账户注销成功',
							icon: 'success',
							duration: 2000
						});
						
						// 清除本地缓存
						uni.clearStorageSync();
						
						// 延迟跳转到登录页面
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login-replica'
							});
						}, 2000);
					} else {
						console.error('注销账户失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '注销失败',
							icon: 'none',
							duration: 2000
						});
					}
				} catch (error) {
					uni.hideLoading();
					console.error('注销账户异常:', error);
					uni.showToast({
						title: '注销失败，请稍后重试',
						icon: 'none',
						duration: 2000
					});
				}
			}
		}
	}
</script>

<style>
	.account-security-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
	}
	
	/* 加载状态样式 */
	.loading-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
	}
	
	.time {
		font-size: 28rpx;
		color: var(--color-text, #000000);
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
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
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
		border-right: 12rpx solid var(--color-text-secondary, #666666);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 安全设置列表 */
	.security-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.list-group {
		margin-bottom: 24rpx;
		background-color: var(--color-card, #FFFFFF);
		border-radius: 16rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	.list-item {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 24rpx;
		background-color: var(--color-card, #FFFFFF);
		cursor: pointer;
	}
	
	.list-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.item-label {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.item-content {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.item-value {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
		font-weight: 300;
	}
	
	.divider {
		height: 1rpx;
		background-color: var(--color-border, #F0F0F0);
		margin: 0 24rpx;
	}
	
	/* 底部导航栏样式 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: var(--color-card, #FFFFFF);
		border-top: 1rpx solid var(--color-border, #F0F0F0);
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
		background-color: var(--color-bg-weak, #F5F5F5);
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: #FF69B4;
		font-weight: 600;
	}
	
	.ai-tab {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.ai-text {
		font-size: 20rpx;
		color: #FFFFFF;
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
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 134rpx;
		height: 8rpx;
		background-color: rgba(255, 255, 255, 0.3);
		border-radius: 4rpx;
		z-index: 101;
	}
</style>
