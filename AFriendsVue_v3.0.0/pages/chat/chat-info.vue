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
				<text class="back-arrow"></text>
			</view>
			<text class="nav-title">聊天信息</text>
		</view> -->
		
		<!-- 头像和用户名区域 -->
		<view class="profile-section" @click="goToFriendCard">
			<view class="avatar-container">
				<view class="avatar">
					<text class="avatar-text">🐱</text>
				</view>
			</view>
			<text class="username">{{friendName}}</text>
		</view>
		
		<!-- 设置选项列表 -->
		<view class="settings-list">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载设置中...</text>
			</view>

			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadChatSettings">
					<text class="retry-text">重试</text>
				</button>
			</view>

			<!-- 动态设置选项 -->
			<view v-else>
				<!-- 动态设置选项 -->
				<view v-for="setting in chatSettings" :key="setting.chatSettingId" class="setting-item" @click="handleSettingClick(setting)">
					<text class="setting-text">{{ setting.chatSettingName }}</text>
					<!-- 根据设置类型显示不同的控件 -->
					<view v-if="isToggleSetting(setting.chatSettingName)" 
						  class="toggle-switch" 
						  :class="{ 'active': getSettingStatus(setting.chatSettingName) }" 
						  @click.stop="toggleSetting(setting.chatSettingName)">
						<view class="toggle-circle"></view>
					</view>
					<text v-else class="arrow">›</text>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-nav">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAIList">
				<view class="ai-icon">AI</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-item" @click="goToProfile">
				<view class="nav-avatar">
					<view class="mini-cat-avatar">
						<view class="mini-cat-body"></view>
						<view class="mini-cat-face">
							<view class="mini-cat-eyes">
								<view class="mini-cat-eye"></view>
								<view class="mini-cat-eye"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				friendName: '念念', // 默认好友名称
				friendId: null, // 好友ID
				userId: null, // 用户ID
				chatSettings: [], // 聊天设置选项
				loading: false,
				errorMessage: '',
				// 用户设置状态
				userSettings: {
					doNotDisturb: false,
					pinChat: false,
					reminders: false,
					chatBackground: '' // 聊天背景URL
				}
			}
		},
		onLoad(options) {
			// 接收传递过来的好友名称和ID
			if (options.friendName) {
				this.friendName = decodeURIComponent(options.friendName);
			}
			if (options.friendId) {
				this.friendId = parseInt(options.friendId);
			}
			// 获取用户ID
			this.userId = uni.getStorageSync('userId') || 1;
			
			console.log('chat-info页面加载参数:', options);
			console.log('用户ID:', this.userId);
			console.log('好友ID:', this.friendId);
			console.log('好友名称:', this.friendName);
			
			// 尝试从存储中获取好友ID（如果URL参数中没有）
			if (!this.friendId) {
				this.friendId = uni.getStorageSync('currentFriendId');
				console.log('从存储中获取好友ID:', this.friendId);
			}
			
			// 检查必要参数
			if (!this.friendId) {
				console.error('好友ID不存在，无法加载设置');
				// 尝试使用默认好友ID（临时解决方案）
				this.friendId = 1000001; // 使用默认好友ID
				console.log('使用默认好友ID:', this.friendId);
				uni.showToast({
					title: '使用默认好友设置',
					icon: 'none',
					duration: 2000
				});
			}
			
			// 加载聊天设置选项
			this.loadChatSettings();
			// 加载用户设置
			this.loadUserSettings();
		},
		methods: {
			// 加载聊天设置选项
			async loadChatSettings() {
				this.loading = true;
				this.errorMessage = '';

				try {
					console.log('开始加载聊天设置选项...');
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/chat-settings-base/all',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('聊天设置API响应状态码:', response.statusCode);
					console.log('聊天设置API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						this.chatSettings = response.data.filter(setting => setting.status === 1); // 只显示启用的设置
						console.log('聊天设置加载成功:', this.chatSettings);
					} else {
						this.errorMessage = `加载聊天设置失败，状态码: ${response.statusCode}`;
						console.error('加载聊天设置失败:', response);
					}
				} catch (error) {
					this.errorMessage = '网络错误，请检查连接';
					console.error('加载聊天设置异常:', error);
				} finally {
					this.loading = false;
				}
			},

			// 根据设置类型获取设置状态
			getSettingStatus(settingName) {
				const settingMap = {
					'消息免打扰': 'doNotDisturb',
					'置顶聊天': 'pinChat',
					'提醒': 'reminders'
				};
				const key = settingMap[settingName];
				return key ? this.userSettings[key] : false;
			},

			// 切换设置状态
			toggleSetting(settingName) {
				console.log('toggleSetting被调用，设置名称:', settingName);
				const settingMap = {
					'消息免打扰': { key: 'doNotDisturb', settingId: 10000002 },
					'置顶聊天': { key: 'pinChat', settingId: 10000003 },
					'提醒': { key: 'reminders', settingId: 10000004 }
				};
				const setting = settingMap[settingName];
				if (setting) {
					this.userSettings[setting.key] = !this.userSettings[setting.key];
					console.log(`${settingName} 状态切换为:`, this.userSettings[setting.key]);
					// 保存单个设置到数据库，使用JSON数组格式
					const settingValue = this.userSettings[setting.key] ? '["1"]' : '["0"]';
					console.log('准备保存设置值:', settingValue);
					this.saveSingleSetting(setting.settingId, settingValue);
				} else {
					console.log('未找到对应的设置映射:', settingName);
				}
			},

			// 加载用户设置
			async loadUserSettings() {
				if (!this.userId || !this.friendId) {
					console.log('用户ID或好友ID不存在，使用默认设置');
					return;
				}

				try {
					console.log('开始加载用户设置，userId:', this.userId, 'friendId:', this.friendId);
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-friends-relationship/settings/${this.userId}/${this.friendId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('用户设置API响应状态码:', response.statusCode);
					console.log('用户设置API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						// 解析JSON数组格式的设置数据
						try {
							const doNotDisturbArray = JSON.parse(response.data.doNotDisturb || '["0"]');
							const pinChatArray = JSON.parse(response.data.pinChat || '["0"]');
							const remindersArray = JSON.parse(response.data.reminders || '["0"]');
							const chatBackgroundArray = JSON.parse(response.data.chatBackground || '[""]');
							
							this.userSettings.doNotDisturb = doNotDisturbArray[0] === "1";
							this.userSettings.pinChat = pinChatArray[0] === "1";
							this.userSettings.reminders = remindersArray[0] === "1";
							this.userSettings.chatBackground = chatBackgroundArray[0] || "";
							console.log('用户设置加载成功:', this.userSettings);
						} catch (e) {
							console.error('解析设置数据失败:', e);
							// 使用默认值
							this.userSettings.doNotDisturb = false;
							this.userSettings.pinChat = false;
							this.userSettings.reminders = false;
							this.userSettings.chatBackground = "";
						}
					} else {
						console.error('加载用户设置失败:', response);
					}
				} catch (error) {
					console.error('加载用户设置异常:', error);
				}
			},

			// 保存单个设置
			async saveSingleSetting(settingId, settingValue) {
				console.log('saveSingleSetting被调用，参数:', { settingId, settingValue });
				console.log('当前用户ID:', this.userId);
				console.log('当前好友ID:', this.friendId);
				
				if (!this.userId) {
					console.error('用户ID不存在，无法保存设置');
					uni.showToast({
						title: '用户信息错误，无法保存设置',
						icon: 'error',
						duration: 2000
					});
					return;
				}
				
				if (!this.friendId) {
					console.error('好友ID不存在，无法保存设置');
					uni.showToast({
						title: '好友信息不完整，无法保存设置',
						icon: 'error',
						duration: 2000
					});
					return;
				}

				try {
					const settingsData = {
						userId: this.userId,
						friendId: this.friendId,
						settingId: settingId,
						settingValue: settingValue
					};

					console.log('开始保存单个设置:', settingsData);
					const response = await uni.request({
						url: '${this.$baseUrl}/api/u-entities/user-friends-relationship/save-settings',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: settingsData
					});

					console.log('保存单个设置API响应状态码:', response.statusCode);
					console.log('保存单个设置API响应数据:', response.data);

					if (response.statusCode === 200) {
						console.log('单个设置保存成功');
						uni.showToast({
							title: '设置已保存',
							icon: 'success',
							duration: 1000
						});
					} else {
						console.error('保存单个设置失败:', response);
						uni.showToast({
							title: '保存失败',
							icon: 'error',
							duration: 1000
						});
					}
				} catch (error) {
					console.error('保存单个设置异常:', error);
					uni.showToast({
						title: '保存失败',
						icon: 'error',
						duration: 1000
					});
				}
			},

			// 判断是否为开关类型的设置
			isToggleSetting(settingName) {
				const toggleSettings = ['消息免打扰', '置顶聊天', '提醒'];
				return toggleSettings.includes(settingName);
			},

			// 处理设置项点击
			handleSettingClick(setting) {
				const settingName = setting.chatSettingName;
				
				// 如果是开关类型，不处理点击（由开关自己处理）
				if (this.isToggleSetting(settingName)) {
					return;
				}
				
				// 根据设置名称跳转到对应页面
				switch (settingName) {
					case '查找聊天记录':
						this.goToSearchHistory();
						break;
					case '设置聊天背景':
						this.goToSetBackground();
						break;
					case '清空聊天记录':
						this.goToClearHistory();
						break;
					case '举报':
						this.goToReport();
						break;
					default:
						console.log('未知设置项:', settingName);
						uni.showToast({
							title: `${settingName} 功能开发中`,
							icon: 'none'
						});
				}
			},

			// 设置聊天背景
			goToSetBackground() {
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						const tempFilePath = res.tempFilePaths[0];
						// 这里可以将图片上传到服务器，然后保存URL
						// 暂时直接使用本地路径
						this.userSettings.chatBackground = tempFilePath;
						// 保存聊天背景设置，使用JSON数组格式
						const settingValue = JSON.stringify([tempFilePath]);
						this.saveSingleSetting(10000005, settingValue);
						uni.showToast({
							title: '背景设置成功',
							icon: 'success'
						});
					},
					fail: (err) => {
						console.error('选择图片失败:', err);
						uni.showToast({
							title: '选择图片失败',
							icon: 'error'
						});
					}
				});
			},

			goBack() {
				uni.navigateBack();
			},
			goToSearchHistory() {
				// 跳转到搜索页面
				uni.navigateTo({
					url: '/pages/chat/search'
				});
			},
			goToClearHistory() {
				uni.showToast({
					title: '清空聊天记录功能',
					icon: 'none'
				});
			},
			goToReport() {
				// 跳转到举报页面
				uni.navigateTo({
					url: '/pages/report/report'
				});
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/content-feed/content-feed'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/friend-list/friend-list'
				});
			},
			goToAIList() {
				uni.navigateTo({
					url: '/pages/ai-list/ai-list'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/user-profile/user-profile'
				});
			},
			goToFriendCard() {
				// 跳转到好友卡片页面
				uni.navigateTo({
					url: `/pages/chat/friend-card?friendName=${encodeURIComponent(this.friendName)}&friendId=${this.friendId}`
				});
			}
		}
	}
</script>

<style>
	.container {
		height: 100vh;
		background-color: #fff;
		display: flex;
		flex-direction: column;
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
		color: #000;
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
		background-color: #000;
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
		color: #000;
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid #000;
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: #000;
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: #000;
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
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
		color: #000;
		font-weight: bold;
	}
	
	.nav-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 36rpx;
		font-weight: bold;
		color: #000;
	}
	
	/* 头像和用户名区域 */
	.profile-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 60rpx 0;
		background-color: #fff;
	}
	
	.avatar-container {
		margin-bottom: 30rpx;
	}
	
	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFD700, #FFA500);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(255, 215, 0, 0.3);
	}
	
	.avatar-text {
		font-size: 60rpx;
	}
	
	.username {
		font-size: 32rpx;
		color: #333;
		font-weight: 500;
	}
	
	/* 设置选项列表 */
	.settings-list {
		flex: 1;
		background-color: #fff;
	}
	
	.setting-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
		background-color: #fff;
	}
	
	.setting-text {
		font-size: 30rpx;
		color: #333;
	}
	
	.arrow {
		font-size: 36rpx;
		color: #ccc;
		font-weight: bold;
	}
	
	/* 开关样式 */
	.toggle-switch {
		width: 100rpx;
		height: 60rpx;
		background-color: #e0e0e0;
		border-radius: 30rpx;
		position: relative;
		transition: background-color 0.3s ease;
	}
	
	.toggle-switch.active {
		background-color: #20B2AA;
	}
	
	.toggle-circle {
		width: 52rpx;
		height: 52rpx;
		background-color: #fff;
		border-radius: 50%;
		position: absolute;
		top: 4rpx;
		left: 4rpx;
		transition: transform 0.3s ease;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.toggle-switch.active .toggle-circle {
		transform: translateX(40rpx);
	}
	
	/* 底部导航栏样式 */
	.bottom-nav {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
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
		background-color: #F5F5F5;
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: #FF69B4;
		font-weight: 600;
	}
	
	.ai-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #FFFFFF;
		font-size: 20rpx;
		font-weight: 600;
	}
	
	.nav-avatar {
		width: 48rpx;
		height: 48rpx;
	}
	
	.mini-cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.mini-cat-body {
		width: 40rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 20rpx;
		position: absolute;
		top: 8rpx;
		left: 4rpx;
	}
	
	.mini-cat-face {
		position: absolute;
		top: 10rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 24rpx;
		height: 20rpx;
	}
	
	.mini-cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 4rpx;
	}
	
	.mini-cat-eye {
		width: 3rpx;
		height: 3rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 32rpx;
		gap: 24rpx;
	}

	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #FF69B4;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}

	.loading-text {
		font-size: 28rpx;
		color: #666666;
	}

	/* 错误状态样式 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 32rpx;
		gap: 24rpx;
	}

	.error-icon {
		font-size: 80rpx;
		margin-bottom: 16rpx;
	}

	.error-text {
		font-size: 28rpx;
		color: #ff4757;
		text-align: center;
		margin-bottom: 16rpx;
	}

	.retry-button {
		background-color: #FF69B4;
		color: #FFFFFF;
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.retry-button:active {
		background-color: #e55a9b;
		transform: scale(0.95);
	}

	.retry-text {
		color: #FFFFFF;
		font-weight: 600;
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
		margin: 20rpx auto;
		opacity: 0.5;
	}
</style>
