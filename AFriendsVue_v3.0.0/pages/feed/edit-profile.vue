<template>
	<view class="container" :class="themeStore.themeClass">
		<!-- 导航栏 -->
		<view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">编辑资料</text>
			</view>
			<view class="nav-right">
				<text class="save-button" @click="saveProfile">保存</text>
			</view>
		</view>

		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 头像区域 -->
			<view class="profile-section">
				<view class="section-header">
					<text class="section-title">头像</text>
				</view>
				<view class="avatar-row" @click="changeAvatar">
					<view class="avatar-placeholder">
						<view class="cat-avatar">
							<view class="cat-body"></view>
							<view class="cat-ears">
								<view class="ear ear-left"></view>
								<view class="ear ear-right"></view>
							</view>
							<view class="cat-face">
								<view class="cat-eyes">
									<view class="cat-eye eye-left"></view>
									<view class="cat-eye eye-right"></view>
								</view>
								<view class="cat-nose"></view>
								<view class="cat-mouth"></view>
							</view>
						</view>
					</view>
					<text class="arrow">›</text>
				</view>
			</view>

			<!-- 基本信息 -->
			<view class="profile-section">
				<view class="section-header">
					<text class="section-title">基本信息</text>
				</view>
				
				<view class="input-row">
					<text class="label">用户名</text>
					<input class="input" v-model="profileData.username" placeholder="请输入用户名" />
				</view>
				
				<view class="input-row" @click="showGenderPicker">
					<text class="label">性别</text>
					<view class="value">{{ profileData.gender === 'MALE' ? '男' : profileData.gender === 'FEMALE' ? '女' : '未知' }}</view>
					<text class="arrow">›</text>
				</view>
				
				<view class="input-row">
					<text class="label">年龄</text>
					<input class="input" type="number" v-model="profileData.age" placeholder="请输入年龄" />
				</view>
				
				<view class="input-row">
					<text class="label">地区</text>
					<input class="input" v-model="profileData.location" placeholder="请输入所在地区" />
				</view>
			</view>

			<!-- 个人描述 -->
			<view class="profile-section">
				<view class="section-header">
					<text class="section-title">个人描述</text>
				</view>
				
				<view class="input-row">
					<text class="label">外貌</text>
					<input class="input" v-model="profileData.appearance" placeholder="请输入外貌描述" />
				</view>
				
				<view class="input-row">
					<text class="label">身份</text>
					<input class="input" v-model="profileData.identity" placeholder="请输入身份信息" />
				</view>
				
				<view class="input-row">
					<text class="label">单位</text>
					<input class="input" v-model="profileData.unit" placeholder="请输入工作单位或学校" />
				</view>
				
				<view class="input-row">
					<text class="label">个性</text>
					<input class="input" v-model="profileData.personality" placeholder="请输入个性描述" />
				</view>
				
				<view class="input-row">
					<text class="label">兴趣爱好</text>
					<input class="input" v-model="profileData.interests" placeholder="请输入兴趣爱好" />
				</view>
				
				<view class="textarea-row">
					<text class="label">个人简介</text>
					<textarea class="textarea" v-model="profileData.bio" placeholder="请输入个人简介" />
					<text class="char-count">{{ profileData.bio.length }}/200</text>
				</view>
			</view>
		</view>

		<!-- 性别选择弹窗 -->
		<view class="popup-overlay" v-if="showGenderPopup" @click="closeGenderPopup">
			<view class="popup-content" @click.stop>
				<view class="popup-header">
					<text class="popup-title">选择性别</text>
				</view>
				<view class="gender-options">
					<view class="gender-option" 
						  :class="{ active: profileData.gender === 'UNKNOWN' }" 
						  @click="selectGender('UNKNOWN')">
						<text>未知</text>
					</view>
					<view class="gender-option" 
						  :class="{ active: profileData.gender === 'MALE' }" 
						  @click="selectGender('MALE')">
						<text>男</text>
					</view>
					<view class="gender-option" 
						  :class="{ active: profileData.gender === 'FEMALE' }" 
						  @click="selectGender('FEMALE')">
						<text>女</text>
					</view>
				</view>
				<view class="popup-actions">
					<text class="cancel-button" @click="closeGenderPopup">取消</text>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-nav">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToMessages">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAI">
				<view class="ai-icon">AI</view>
			</view>
			<view class="nav-item" @click="goToFriends">
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
	import { useThemeStore } from '@/store/theme.js';

	export default {
		data() {
			return {
				themeStore: useThemeStore(),
				userId: 1000100, // 默认用户ID，实际应从登录信息获取
				profileData: {
					username: '',
					gender: 'UNKNOWN',
					age: null,
					appearance: '',
					identity: '',
					unit: '',
					personality: '',
					interests: '',
					location: '',
					bio: ''
				},
				showGenderPopup: false,
				loading: false
			}
		},
		onLoad() {
			this.themeStore.init();
			this.themeStore.applyTheme();
			this.loadUserProfile();
		},
		methods: {
			// 加载用户资料
			async loadUserProfile() {
				try {
					console.log('=== 开始加载用户资料 ===');
					console.log('用户ID:', this.userId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-profile/profile-detail?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const userInfo = response.data.data || {};
						this.profileData = {
							username: userInfo.username || '',
							gender: userInfo.gender || 'UNKNOWN',
							age: userInfo.age || null,
							appearance: userInfo.appearance || '',
							identity: userInfo.identity || '',
							unit: userInfo.unit || '',
							personality: userInfo.personality || '',
							interests: userInfo.interests || '',
							location: userInfo.location || '',
							bio: userInfo.bio || ''
						};
						console.log('成功加载用户资料:', this.profileData);
					} else {
						console.error('加载用户资料失败:', response.data.message);
						uni.showToast({
							title: '加载资料失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('加载用户资料异常:', error);
					uni.showToast({
						title: '网络连接失败',
						icon: 'none'
					});
				}
			},
			
			// 保存用户资料
			async saveProfile() {
				if (this.loading) return;
				
				this.loading = true;
				try {
					console.log('=== 开始保存用户资料 ===');
					console.log('用户ID:', this.userId);
					console.log('资料数据:', this.profileData);
					
					const requestData = {
						userId: this.userId,
						...this.profileData
					};
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/user-profile/update',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: requestData
					});
					
					console.log('=== 保存API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						uni.showToast({
							title: '保存成功',
							icon: 'success'
						});
						// 返回上一页
						setTimeout(() => {
							uni.navigateBack();
						}, 1000);
					} else {
						console.error('保存用户资料失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '保存失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('保存用户资料异常:', error);
					uni.showToast({
						title: '网络连接失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 显示性别选择弹窗
			showGenderPicker() {
				this.showGenderPopup = true;
			},
			
			// 关闭性别选择弹窗
			closeGenderPopup() {
				this.showGenderPopup = false;
			},
			
			// 选择性别
			selectGender(gender) {
				this.profileData.gender = gender;
				this.closeGenderPopup();
			},
			
			// 返回上一页
			goBack() {
				uni.navigateBack();
			},
			
			// 更改头像
			changeAvatar() {
				uni.showToast({
					title: '更改头像功能开发中',
					icon: 'none'
				});
			},
			
			// 导航方法
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/chat-feed'
				});
			},
			
			goToMessages() {
				uni.showToast({
					title: '消息功能',
					icon: 'none'
				});
			},
			
			goToAI() {
				uni.navigateTo({
					url: '/pages/ai/ai-list'
				});
			},
			
			goToFriends() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
				});
			},
			
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background-color: var(--color-bg-weak, #f7f7f9);
		color: var(--color-text, #11141f);
		display: flex;
		flex-direction: column;
	}

	.container.theme-dark {
		background-color: var(--color-bg, #0f1115);
		color: var(--color-text, #e9edf5);
	}

	.container.theme-dark .cat-body,
	.container.theme-dark .ear,
	.container.theme-dark .mini-cat-body {
		background: linear-gradient(135deg, #CC8400, #CC7000);
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: var(--color-card, #ffffff);
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
		position: relative;
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		padding: 0 16rpx;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: var(--color-bg-weak, #f7f7f9);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text-secondary, #666c7a);
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: var(--color-text, #11141f);
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
		display: flex;
		justify-content: center;
	}
	
	.save-button {
		font-size: 28rpx;
		color: var(--color-primary, #ff69b4);
		font-weight: 500;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 24rpx 0;
		margin-bottom: 120rpx; /* 为底部导航栏留出空间 */
	}
	
	.profile-section {
		background-color: var(--color-card, #ffffff);
		margin-bottom: 24rpx;
		border-radius: 16rpx;
		overflow: hidden;
	}
	
	.section-header {
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
	}
	
	.section-title {
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		font-weight: 600;
	}
	
	.input-row {
		display: flex;
		align-items: center;
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
		min-height: 88rpx;
	}
	
	.input-row:last-child {
		border-bottom: none;
	}
	
	.label {
		width: 160rpx;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		flex-shrink: 0;
	}
	
	.input {
		flex: 1;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		border: none;
		outline: none;
		background: transparent;
	}
	
	.input::placeholder {
		color: var(--color-text-secondary, #666c7a);
	}
	
	.value {
		flex: 1;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		text-align: right;
		margin-right: 16rpx;
	}
	
	.arrow {
		font-size: 36rpx;
		color: var(--color-text-secondary, #666c7a);
		font-weight: 300;
	}
	
	.textarea-row {
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
	}
	
	.textarea-row:last-child {
		border-bottom: none;
	}
	
	.textarea {
		width: 100%;
		height: 160rpx;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		border: none;
		outline: none;
		background: transparent;
		margin: 16rpx 0;
		resize: none;
	}
	
	.textarea::placeholder {
		color: var(--color-text-secondary, #666c7a);
	}
	
	.char-count {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666c7a);
		text-align: right;
		display: block;
	}
	
	.avatar-row {
		display: flex;
		align-items: center;
		padding: 24rpx 32rpx;
		min-height: 88rpx;
		cursor: pointer;
	}
	
	.avatar-placeholder {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: var(--color-bg-weak, #f7f7f9);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 16rpx;
	}
	
	.cat-avatar {
		width: 60rpx;
		height: 60rpx;
		position: relative;
	}
	
	.cat-body {
		width: 50rpx;
		height: 40rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 25rpx;
		position: absolute;
		top: 10rpx;
		left: 5rpx;
	}
	
	.cat-ears {
		position: absolute;
		top: 0;
		left: 50%;
		transform: translateX(-50%);
		display: flex;
		gap: 10rpx;
	}
	
	.ear {
		width: 10rpx;
		height: 15rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 50% 50% 0 0;
	}
	
	.cat-face {
		position: absolute;
		top: 12rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 30rpx;
		height: 25rpx;
	}
	
	.cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 5rpx;
	}
	
	.cat-eye {
		width: 4rpx;
		height: 4rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.cat-nose {
		width: 3rpx;
		height: 3rpx;
		background-color: var(--color-primary, #ff69b4);
		border-radius: 50%;
		margin: 0 auto 4rpx;
	}
	
	.cat-mouth {
		width: 10rpx;
		height: 4rpx;
		border: 1rpx solid #000;
		border-top: none;
		border-radius: 0 0 10rpx 10rpx;
		margin: 0 auto;
	}
	
	/* 弹窗样式 */
	.popup-overlay {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
	}
	
	.popup-content {
		width: 540rpx;
		background-color: var(--color-card, #ffffff);
		border-radius: 24rpx;
		overflow: hidden;
	}
	
	.popup-header {
		padding: 32rpx;
		text-align: center;
		border-bottom: 1rpx solid var(--color-border, #e5e7ec);
	}
	
	.popup-title {
		font-size: 32rpx;
		color: var(--color-text, #11141f);
		font-weight: 600;
	}
	
	.gender-options {
		padding: 32rpx;
	}
	
	.gender-option {
		padding: 24rpx;
		border-radius: 16rpx;
		margin-bottom: 16rpx;
		background-color: var(--color-bg-weak, #f7f7f9);
		text-align: center;
		font-size: 28rpx;
		color: var(--color-text, #11141f);
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.gender-option:last-child {
		margin-bottom: 0;
	}
	
	.gender-option:active,
	.gender-option.active {
		background-color: var(--color-primary, #ff69b4);
		color: var(--color-on-primary, #ffffff);
	}
	
	.popup-actions {
		padding: 0 32rpx 32rpx;
		text-align: center;
	}
	
	.cancel-button {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666c7a);
		padding: 24rpx;
		display: block;
		width: 100%;
		border-radius: 16rpx;
		background-color: var(--color-bg-weak, #f7f7f9);
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.cancel-button:active {
		background-color: var(--color-border, #e5e7ec);
	}
	
	/* 底部导航栏样式 */
	.bottom-nav {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: var(--color-card, #ffffff);
		border-top: 1rpx solid var(--color-border, #e5e7ec);
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
		background-color: var(--color-bg-weak, #f7f7f9);
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666c7a);
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: var(--color-primary, #ff69b4);
		font-weight: 600;
	}
	
	.ai-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, var(--color-primary, #ff69b4), #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		color: var(--color-on-primary, #ffffff);
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
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 134rpx;
		height: 8rpx;
		background-color: rgba(0, 0, 0, 0.12);
		border-radius: 4rpx;
		z-index: 101;
	}

	.container.theme-dark .home-indicator {
		background-color: rgba(255, 255, 255, 0.24);
	}
</style>
