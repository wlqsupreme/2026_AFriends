<template>
	<view class="ai-list-page" :class="themeClass">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="status-time">9:41</text>
			<view class="status-right">
				<view class="signal-bars">
					<view class="signal-bar"></view>
					<view class="signal-bar"></view>
					<view class="signal-bar"></view>
					<view class="signal-bar"></view>
				</view>
				<text class="status-wifi">📶</text>
				<text class="status-battery">🔋</text>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
					<view class="nav-left" @click="goBack">
			<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
				<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="var(--color-text-secondary, #666666)" p-id="1053"></path>
			</svg>
		</view>
			<view class="nav-center">
				<text class="nav-title">购买记录</text>
			</view>
			<view class="nav-right">
				<view class="search-icon" @click="openSearch">
					<text class="search-symbol">🔍</text>
				</view>
			</view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-section">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载AI模型...</text>
			</view>
			
			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-section">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="retry-button" @click="retryLoad">
					<text class="retry-text">重试</text>
				</view>
			</view>
			
			<!-- 空状态 -->
			<view v-else-if="userAiModels.length === 0" class="empty-section">
				<view class="empty-icon">🤖</view>
				<text class="empty-text">您还没有任何AI模型</text>
				<text class="empty-desc">快去购买或创建您的第一个AI助手吧！</text>
			</view>
			
			<!-- AI模型列表 -->
			<view v-else>
				<view v-for="letter in getSortedLetters()" :key="letter" class="category-section">
					<view class="category-header">
						<text class="category-letter">{{ letter }}</text>
					</view>
					<view v-for="model in groupedModels[letter]" :key="model.userAiId" class="ai-item" @click="selectAI(model)">
						<view class="ai-avatar">
							<image v-if="model.modelImageUrl" class="avatar-image" :src="model.modelImageUrl" mode="aspectFill"></image>
							<view v-else class="avatar-circle" :class="getAvatarClass(model.modelName)">
								<text class="avatar-text">{{ getAvatarText(model.modelName) }}</text>
							</view>
						</view>
						<view class="ai-info">
							<text class="ai-name">{{ model.modelName || '未知AI' }}</text>
							<text v-if="model.customDesc" class="ai-desc">{{ model.customDesc }}</text>
							<view class="ai-meta">
								<text class="ai-level">Lv.{{ model.level || 1 }}</text>
								<text class="ai-power">{{ model.power || 0 }} 能量</text>
							</view>
						</view>
					</view>
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
			<view class="nav-item active">
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
	import { useThemeStore } from '@/store/theme.js'
	
	export default {
		name: 'AIList',
		data() {
			return {
				userAiModels: [],
				groupedModels: {},
				loading: true,
				errorMessage: ''
			}
		},
		computed: {
			themeClass() {
				const themeStore = useThemeStore()
				return themeStore.themeClass
			}
		},
		onLoad() {
			this.loadUserAiModels();
		},
		methods: {
			// 加载用户AI模型数据
			async loadUserAiModels() {
				this.loading = true;
				this.errorMessage = '';
				
				try {
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						this.errorMessage = '用户未登录';
						this.loading = false;
						return;
					}
					
					const response = await this.getUserAiModels(userId);
					if (response.data && response.data.length > 0) {
						this.userAiModels = response.data;
						this.groupModelsByFirstLetter();
					} else {
						this.userAiModels = [];
						this.groupedModels = {};
					}
				} catch (error) {
					console.error('加载用户AI模型失败:', error);
					this.errorMessage = '加载AI模型失败，请重试';
				} finally {
					this.loading = false;
				}
			},
			
			// 获取用户AI模型
			getUserAiModels(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/user/${userId}`,
						method: 'GET',
						timeout: 10000,
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 按首字母分组AI模型
			groupModelsByFirstLetter() {
				const groups = {};
				
				this.userAiModels.forEach(model => {
					const modelName = model.modelName || '未知AI';
					const firstLetter = this.getFirstLetter(modelName);
					
					if (!groups[firstLetter]) {
						groups[firstLetter] = [];
					}
					groups[firstLetter].push(model);
				});
				
				// 对每个分组内的模型按名称排序
				Object.keys(groups).forEach(letter => {
					groups[letter].sort((a, b) => {
						const nameA = a.modelName || '';
						const nameB = b.modelName || '';
						return nameA.localeCompare(nameB, 'zh-CN');
					});
				});
				
				this.groupedModels = groups;
			},
			
			// 获取中文拼音首字母
			getFirstLetter(str) {
				if (!str) return 'Z';
				
				// 如果是英文字母开头
				if (/^[A-Za-z]/.test(str)) {
					return str.charAt(0).toUpperCase();
				}
				
				// 如果是中文字符，使用简单的拼音首字母映射
				const firstChar = str.charAt(0);
				const charCode = firstChar.charCodeAt(0);
				
				// 中文字符范围判断
				if (charCode >= 0x4e00 && charCode <= 0x9fff) {
					// 简单的拼音首字母映射（可以根据需要扩展）
					const pinyinMap = {
						'宠': 'C', '搭': 'D', '学': 'X', '游': 'Y', '运': 'Y',
						'工': 'G', '商': 'S', '医': 'Y', '教': 'J', '科': 'K',
						'艺': 'Y', '体': 'T', '文': 'W', '理': 'L', '法': 'F',
						'经': 'J', '管': 'G', '计': 'J', '机': 'J', '电': 'D',
						'通': 'T', '信': 'X', '网': 'W', '软': 'R', '硬': 'Y',
						'数': 'S', '据': 'J', '人': 'R', '工': 'G', '智': 'Z',
						'能': 'N', '机': 'J', '器': 'Q', '学': 'X', '习': 'X'
					};
					
					return pinyinMap[firstChar] || this.getPinyinFirstLetter(firstChar);
				}
				
				return 'Z'; // 其他字符归到Z组
			},
			
			// 获取拼音首字母（简化版）
			getPinyinFirstLetter(char) {
				// 这里可以使用更完整的拼音库，暂时用简单映射
				const charCode = char.charCodeAt(0);
				if (charCode >= 0x4e00 && charCode <= 0x9fff) {
					// 根据Unicode范围简单判断
					if (charCode < 0x4f00) return 'A';
					if (charCode < 0x5000) return 'B';
					if (charCode < 0x5100) return 'C';
					if (charCode < 0x5200) return 'D';
					if (charCode < 0x5300) return 'E';
					if (charCode < 0x5400) return 'F';
					if (charCode < 0x5500) return 'G';
					if (charCode < 0x5600) return 'H';
					if (charCode < 0x5700) return 'I';
					if (charCode < 0x5800) return 'J';
					if (charCode < 0x5900) return 'K';
					if (charCode < 0x5a00) return 'L';
					if (charCode < 0x5b00) return 'M';
					if (charCode < 0x5c00) return 'N';
					if (charCode < 0x5d00) return 'O';
					if (charCode < 0x5e00) return 'P';
					if (charCode < 0x5f00) return 'Q';
					if (charCode < 0x6000) return 'R';
					if (charCode < 0x6100) return 'S';
					if (charCode < 0x6200) return 'T';
					if (charCode < 0x6300) return 'U';
					if (charCode < 0x6400) return 'V';
					if (charCode < 0x6500) return 'W';
					if (charCode < 0x6600) return 'X';
					if (charCode < 0x6700) return 'Y';
					return 'Z';
				}
				return 'Z';
			},
			
			// 获取排序后的字母列表
			getSortedLetters() {
				return Object.keys(this.groupedModels).sort((a, b) => {
					// 数字和字母优先，然后是中文
					if (/^[A-Z]$/.test(a) && /^[A-Z]$/.test(b)) {
						return a.localeCompare(b);
					}
					if (/^[A-Z]$/.test(a)) return -1;
					if (/^[A-Z]$/.test(b)) return 1;
					return a.localeCompare(b, 'zh-CN');
				});
			},
			
			// 重试加载
			retryLoad() {
				this.loadUserAiModels();
			},
			goBack() {
				uni.navigateBack();
			},
			openSearch() {
				uni.showToast({
					title: '搜索功能',
					icon: 'none'
				});
			},
			selectAI(model) {
				// 跳转到AI聊天页面
				uni.navigateTo({
					url: `/pages/ai/ai-chat?modelId=${model.userAiId}&modelName=${encodeURIComponent(model.modelName || '未知AI')}`
				});
			},
			
			// 获取头像样式类
			getAvatarClass(modelName) {
				if (!modelName) return 'default-ai-avatar';
				
				const name = modelName.toLowerCase();
				if (name.includes('宠') || name.includes('pet')) return 'pet-ai-avatar';
				if (name.includes('搭') || name.includes('partner')) return 'partner-ai-avatar';
				if (name.includes('学') || name.includes('study')) return 'study-ai-avatar';
				if (name.includes('游') || name.includes('game')) return 'gaming-ai-avatar';
				if (name.includes('运') || name.includes('sport')) return 'sports-ai-avatar';
				if (name.includes('工') || name.includes('work')) return 'work-ai-avatar';
				if (name.includes('商') || name.includes('business')) return 'business-ai-avatar';
				return 'default-ai-avatar';
			},
			
			// 获取头像文字
			getAvatarText(modelName) {
				if (!modelName) return '?';
				return modelName.charAt(0).toUpperCase();
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/content-feed/content-feed'
				});
			},
			goToMessages() {
				uni.showToast({
					title: '消息功能',
					icon: 'none'
				});
			},
			goToFriends() {
				uni.navigateTo({
					url: '/pages/friend-list/friend-list'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/user-profile/user-profile'
				});
			}
		}
	}
</script>

<style>
	.ai-list-page {
		width: 100%;
		min-height: 100vh;
		background-color: var(--color-bg, #ffffff);
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 88rpx;
		background-color: var(--color-bg, #ffffff);
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
	}
	
	.status-time {
		font-size: 28rpx;
		font-weight: 600;
		color: var(--color-text, #000000);
	}
	
	.status-right {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.signal-bars {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.signal-bar {
		width: 6rpx;
		background-color: var(--color-text, #000000);
		border-radius: 2rpx;
	}
	
	.signal-bar:nth-child(1) { height: 8rpx; }
	.signal-bar:nth-child(2) { height: 12rpx; }
	.signal-bar:nth-child(3) { height: 16rpx; }
	.signal-bar:nth-child(4) { height: 20rpx; }
	
	.status-wifi, .status-battery {
		font-size: 24rpx;
		color: var(--color-text, #000000);
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: var(--color-bg, #ffffff);
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text, #333333);
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
		display: flex;
		justify-content: center;
	}
	
	.search-icon {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.search-symbol {
		font-size: 32rpx;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 32rpx 0;
	}
	
	.category-section {
		margin-bottom: 32rpx;
	}
	
	.category-header {
		padding: 0 32rpx 16rpx 32rpx;
	}
	
	.category-letter {
		font-size: 48rpx;
		color: var(--color-text-secondary, #cccccc);
		font-weight: bold;
	}
	
	.ai-item {
		display: flex;
		align-items: center;
		padding: 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		background-color: var(--color-card, #ffffff);
		cursor: pointer;
	}
	
	.ai-item:last-child {
		border-bottom: none;
	}
	
	.ai-item:active {
		background-color: var(--color-bg-weak, #f8f8f8);
	}
	
	.ai-avatar {
		margin-right: 24rpx;
	}
	
	.avatar-circle {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: var(--color-bg-weak, #f0f0f0);
	}
	
	.pet-ai-avatar {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	.pet-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #B06AB3, #A05BA0);
	}
	
	.partner-ai-avatar {
		background: linear-gradient(135deg, #87CEEB, #98FB98);
	}
	
	.partner-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #4A90A4, #5A8F6D);
	}
	
	.study-ai-avatar {
		background: linear-gradient(135deg, #DDA0DD, #E6E6FA);
	}
	
	.study-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #997A99, #A3A3B5);
	}
	
	.gaming-ai-avatar {
		background: linear-gradient(135deg, #FFA07A, #FFB6C1);
	}
	
	.gaming-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #CC7F62, #CC9299);
	}
	
	.sports-ai-avatar {
		background: linear-gradient(135deg, #98FB98, #87CEEB);
	}
	
	.sports-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #6EA86E, #5CA3B8);
	}
	
	.ai-name {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		margin-bottom: 8rpx;
	}
	
	.ai-info {
		flex: 1;
		display: flex;
		flex-direction: column;
	}
	
	.ai-desc {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		margin-bottom: 8rpx;
		line-height: 1.4;
	}
	
	.ai-meta {
		display: flex;
		gap: 16rpx;
	}
	
	.ai-level, .ai-power {
		font-size: 20rpx;
		color: var(--color-text-secondary, #999999);
		background-color: var(--color-bg-weak, #f5f5f5);
		padding: 4rpx 8rpx;
		border-radius: 8rpx;
	}
	
	.avatar-image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
	}
	
	.avatar-text {
		font-size: 32rpx;
		color: var(--color-on-primary, #FFFFFF);
		font-weight: 600;
	}
	
	.default-ai-avatar {
		background: linear-gradient(135deg, #9E9E9E, #757575);
	}
	
	.default-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #757575, #616161);
	}
	
	.work-ai-avatar {
		background: linear-gradient(135deg, #FF9800, #F57C00);
	}
	
	.work-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #BF7200, #B86000);
	}
	
	.business-ai-avatar {
		background: linear-gradient(135deg, #2196F3, #1976D2);
	}
	
	.business-ai-avatar.theme-dark {
		background: linear-gradient(135deg, #1976D2, #145EA8);
	}
	
	/* 加载状态 */
	.loading-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 120rpx 0;
	}
	
	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid var(--color-bg-weak, #f0f0f0);
		border-top: 4rpx solid var(--color-primary, #FF69B4);
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 24rpx;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	/* 错误状态 */
	.error-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 120rpx 0;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 24rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
		text-align: center;
		margin-bottom: 32rpx;
	}
	
	.retry-button {
		background: linear-gradient(135deg, var(--color-primary, #FF69B4), #FF8E53);
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
	}
	
	.retry-text {
		font-size: 28rpx;
		color: var(--color-on-primary, #FFFFFF);
		font-weight: 600;
	}
	
	/* 空状态 */
	.empty-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 120rpx 0;
	}
	
	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 32rpx;
	}
	
	.empty-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		margin-bottom: 16rpx;
	}
	
	.empty-desc {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
		text-align: center;
		line-height: 1.5;
	}
	
	/* 底部导航栏样式 */
	.bottom-nav {
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
		color: var(--color-primary, #FF69B4);
		font-weight: 600;
	}
	
	.ai-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, var(--color-primary, #FF69B4), #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		color: var(--color-on-primary, #FFFFFF);
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
		background-color: rgba(255, 255, 255, 0.3);
		border-radius: 4rpx;
		z-index: 101;
	}
</style>
