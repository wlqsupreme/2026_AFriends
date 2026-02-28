<template>
	<view class="ai-store-page">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<svg class="back-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666"></path>
				</svg>
			</view>
			<view class="nav-center">
				<text class="nav-title">AI商店</text>
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
				<view class="retry-button" @click="loadAiStoreData">
					<text class="retry-text">重试</text>
				</view>
			</view>
			
			<!-- AI模型列表 -->
			<view v-else>
				<view v-for="aiModel in aiModels" :key="aiModel.modelId" class="ai-item" @click="() => goToAIDetail(aiModel)">
					<view class="ai-avatar">
						<image v-if="aiModel.modelImageUrl" class="avatar-image" :src="aiModel.modelImageUrl" mode="aspectFill"></image>
						<view v-else class="avatar-circle" :class="getAvatarClass(aiModel.modelName)">
							<text class="avatar-text">{{ getAvatarText(aiModel.modelName) }}</text>
						</view>
					</view>
					<view class="ai-info">
						<text class="ai-name">{{ aiModel.modelName || '未知AI' }}</text>
						<text class="ai-description">{{ aiModel.modelDesc || '暂无描述' }}</text>
						
						<!-- 统计信息 -->
						<view class="ai-stats">
							<view class="stat-item">
								<text class="stat-value">{{ formatRecommendCount(aiModel.recommendCount) }}</text>
								<text class="stat-label">推荐</text>
							</view>
							<view class="stat-item">
								<text class="stat-value">{{ formatPurchaseCount(aiModel.purchaseCount) }}</text>
								<text class="stat-label">购买</text>
							</view>
							<view class="stat-item">
								<text class="stat-value">{{ formatScore(aiModel.score) }}</text>
								<text class="stat-label">评分</text>
							</view>
						</view>
						
						<!-- 价格信息 -->
						<view class="ai-price">
							<text class="price-text">{{ formatPrice(aiModel.price) }}</text>
							<text class="price-label">金币</text>
						</view>
						
						<!-- 互动数据 -->
						<view class="ai-interactions">
							<view class="interaction-item">
								<text class="interaction-icon">👍</text>
								<text class="interaction-count">{{ aiModel.likeCount || 0 }}</text>
							</view>
							<view class="interaction-item">
								<text class="interaction-icon">💬</text>
								<text class="interaction-count">{{ aiModel.commentCount || 0 }}</text>
							</view>
							<view class="interaction-item">
								<text class="interaction-icon">⭐</text>
								<text class="interaction-count">{{ aiModel.collectCount || 0 }}</text>
							</view>
						</view>
						
						<view v-if="isPurchased(aiModel)" class="purchase-info">
							<text class="purchase-time">购买时间: {{ formatPurchaseTime(aiModel) }}</text>
						</view>
					</view>
					<view class="ai-status" @click.stop>
						<view v-if="isPurchased(aiModel)" class="purchased-badge">
							<text class="badge-text">已购买</text>
						</view>
						<view v-else class="buy-btn" @click="buyAiModel(aiModel)">
							<text class="buy-text">购买</text>
						</view>
					</view>
				</view>
				
				<!-- 空状态 -->
				<view v-if="aiModels.length === 0" class="empty-section">
					<view class="empty-icon">🤖</view>
					<text class="empty-text">暂无AI模型</text>
					<text class="empty-desc">敬请期待更多AI模型上线！</text>
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
	export default {
		name: 'AIStore',
		data() {
			return {
				aiModels: [], // AI模型列表
				userAiModels: [], // 用户拥有的AI模型
				loading: true, // 加载状态
				errorMessage: '' // 错误信息
			}
		},
		onLoad() {
			this.loadAiStoreData();
		},
		methods: {
			// 加载AI商店数据
			async loadAiStoreData() {
				this.loading = true;
				this.errorMessage = '';
				
				try {
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						this.errorMessage = '用户未登录';
						this.loading = false;
						return;
					}
					
					// 第一步：调用后端加载接口，确保数据已加载到内存
					console.log('开始触发AI模型数据加载到内存...');
					const loadResponse = await this.loadAimodelBaseInfoToMemory();
					
					if (loadResponse.statusCode !== 200) {
						throw new Error('AI模型数据加载失败');
					}
					console.log('AI模型数据加载成功');
					
					// 并行加载AI模型列表和用户AI模型
					const [aiModelsResponse, userAiModelsResponse] = await Promise.all([
						this.getAiModels(),
						this.getUserAiModels(userId)
					]);
					
					if (aiModelsResponse.data && aiModelsResponse.data.length > 0) {
						this.aiModels = aiModelsResponse.data;
						console.log('加载的AI模型数据:', this.aiModels);
					} else {
						this.aiModels = [];
					}
					
					if (userAiModelsResponse.data && userAiModelsResponse.data.length > 0) {
						this.userAiModels = userAiModelsResponse.data;
						console.log('用户AI模型数据:', this.userAiModels);
					} else {
						this.userAiModels = [];
					}
					
				} catch (error) {
					console.error('加载AI商店数据失败:', error);
					this.errorMessage = '加载数据失败，请重试';
				} finally {
					this.loading = false;
				}
			},
			
			// 新增：调用后端加载接口的方法
			loadAimodelBaseInfoToMemory() {
			    return new Promise((resolve, reject) => {
			        uni.request({
			            url: this.$baseUrl+'/api/a-entities/aimodel-base-info/load', // 后端加载接口路径
			            method: 'POST',
			            timeout: 30000, // 加载可能耗时，超时时间设为30秒
			            success: (res) => resolve(res),
			            fail: (err) => reject(err)
			        });
			    });
			},
			
			// 获取AI模型列表
			getAiModels() {
				return new Promise((resolve, reject) => {
					uni.request({
						url: this.$baseUrl+'/api/a-entities/aimodel-base-info/all',
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
			
			// 检查AI模型是否已购买
			isPurchased(aiModel) {
				if (!aiModel || !this.userAiModels.length) return false;
				
				// 检查用户是否拥有这个AI模型
				return this.userAiModels.some(userModel => 
					userModel.parentModelId === aiModel.modelId || 
					userModel.modelName === aiModel.modelName
				);
			},
			
			// 获取用户AI模型信息
			getUserAiModel(aiModel) {
				if (!aiModel || !this.userAiModels.length) return null;
				
				return this.userAiModels.find(userModel => 
					userModel.parentModelId === aiModel.modelId || 
					userModel.modelName === aiModel.modelName
				);
			},
			
			// 获取头像样式类
			getAvatarClass(modelName) {
				if (!modelName) return 'default-ai-avatar';
				
				const name = modelName.toLowerCase();
				if (name.includes('宠') || name.includes('pet')) return 'pet-ai-avatar';
				if (name.includes('搭') || name.includes('partner') || name.includes('生活')) return 'lifestyle-ai-avatar';
				if (name.includes('学') || name.includes('study')) return 'study-ai-avatar';
				if (name.includes('游') || name.includes('game')) return 'gaming-ai-avatar';
				if (name.includes('运') || name.includes('sport')) return 'sports-ai-avatar';
				if (name.includes('音') || name.includes('music')) return 'music-ai-avatar';
				if (name.includes('旅') || name.includes('travel')) return 'travel-ai-avatar';
				return 'default-ai-avatar';
			},
			
			// 获取头像文字
			getAvatarText(modelName) {
				if (!modelName) return '?';
				return modelName.charAt(0).toUpperCase();
			},
			
			// 格式化价格（人民币转金币，乘以100）
			formatPrice(price) {
				if (!price) return '0C';
				const goldCoins = Math.round(price * 100);
				return goldCoins + 'C';
			},
			
			// 格式化推荐数
			formatRecommendCount(count) {
				if (!count) return '0';
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 格式化购买数
			formatPurchaseCount(count) {
				if (!count) return '0';
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 格式化评分
			formatScore(score) {
				if (!score) return '0.0';
				return parseFloat(score).toFixed(1);
			},
			
			// 格式化购买时间
			formatPurchaseTime(aiModel) {
				const userModel = this.getUserAiModel(aiModel);
				if (!userModel || !userModel.createdAt) return '未知';
				
				const date = new Date(userModel.createdAt);
				return `${date.getMonth() + 1}月${date.getDate()}日`;
			},
			
			// 购买AI模型
			async buyAiModel(aiModel) {
				try {
					// 检查是否已经购买
					if (this.isPurchased(aiModel)) {
						uni.showToast({
							title: '您已购买此AI模型',
							icon: 'none'
						});
						return;
					}
					
					// 确认购买
					const confirmResult = await new Promise((resolve) => {
						uni.showModal({
							title: '确认购买',
							content: `确定要购买 ${aiModel.modelName} 吗？\n价格：${this.formatPrice(aiModel.price)}`,
							success: (res) => {
								resolve(res.confirm);
							},
							fail: () => {
								resolve(false);
							}
						});
					});
					
					if (!confirmResult) {
						return;
					}
					
					// 显示加载状态
					uni.showLoading({
						title: '购买中...'
					});
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.hideLoading();
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					// 调用购买API
					const purchaseResult = await this.purchaseAiModel(userId, aiModel);
					
					uni.hideLoading();
					
					if (purchaseResult.success) {
						uni.showToast({
							title: '购买成功！',
							icon: 'success'
						});
						
					    // 新增：刷新用户钱包余额（若前端有余额显示组件，此处更新）
					    await this.refreshUserBalance(userId);
						
						// 刷新数据
						await this.loadAiStoreData();
					} else {
						uni.showToast({
							title: purchaseResult.message || '购买失败',
							icon: 'none'
						});
					}
				} catch (error) {
					uni.hideLoading();
					console.error('购买AI模型失败:', error);
					uni.showToast({
						title: '购买失败，请重试',
						icon: 'none'
					});
				}
			},
			
			// 新增：刷新用户余额（调用RechargeService的余额接口）
			async refreshUserBalance(userId) {
			    try {
			        const { data } = await uni.request({
			            url: `${this.$baseUrl}/api/wallet/balance`,
			            method: 'GET',
			            data: { userId }
			        });
			        if (data.success) {
			            // 若前端有全局余额存储（如vuex），此处更新
			            // 示例：this.$store.commit('updateUserBalance', data.data.currentBalanceFormatted);
			            console.log('余额刷新成功：', data.data.currentBalanceFormatted);
			        }
			    } catch (error) {
			        console.error('刷新余额失败：', error);
			    }
			},
			
			// 调用购买API
			purchaseAiModel(userId, aiModel) {
				return new Promise((resolve, reject) => {
					const purchaseData = {
						userId: parseInt(userId),
						parentModelId: aiModel.modelId,
						modelName: aiModel.modelName,
						modelDesc: aiModel.modelDesc,
						modelImageUrl: aiModel.modelImageUrl || '',
						price: aiModel.price
					};
					
					console.log('发送购买请求:', purchaseData);
					
					uni.request({
						url: this.$baseUrl+'/api/u-entities/user-ai-model/purchase',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: purchaseData,
						timeout: 10000,
						success: (res) => {
							console.log('购买响应:', res);
							resolve(res.data);
						},
						fail: (err) => {
							console.error('购买请求失败:', err);
							reject(err);
						}
					});
				});
			},
			
			goBack() {
				uni.navigateBack();
			},
			openSearch() {
				uni.showToast({
					title: '搜索功能开发中',
					icon: 'none'
				});
			},
			goToAIDetail(aiModel) {
				console.log('跳转到AI详情页面:', aiModel);
				// 跳转到AI详情页面
				uni.navigateTo({
					url: `/pages/ai/pet-ai-detail?aiName=${encodeURIComponent(aiModel.modelName)}&modelId=${aiModel.modelId}`,
					success: () => {
						console.log('跳转成功');
					},
					fail: (err) => {
						console.error('跳转失败:', err);
						uni.showToast({
							title: '页面跳转失败',
							icon: 'none'
						});
					}
				});
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/content-feed'
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
	.ai-store-page {
		width: 100%;
		min-height: 100vh;
		background-color: #ffffff;
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: #ffffff;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
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
	
	.back-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: #333333;
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
	
	.ai-item {
		display: flex;
		align-items: center;
		padding: 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
		background-color: #ffffff;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.ai-item:last-child {
		border-bottom: none;
	}
	
	.ai-item:active {
		background-color: #f8f8f8;
	}
	
	.ai-avatar {
		margin-right: 24rpx;
	}
	
	.avatar-circle {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
	}
	
	.pet-ai-avatar {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	.lifestyle-ai-avatar {
		background: linear-gradient(135deg, #87CEEB, #98FB98);
	}
	
	.study-ai-avatar {
		background: linear-gradient(135deg, #DDA0DD, #E6E6FA);
	}
	
	.gaming-ai-avatar {
		background: linear-gradient(135deg, #FFA07A, #FFB6C1);
	}
	
	.sports-ai-avatar {
		background: linear-gradient(135deg, #98FB98, #87CEEB);
	}
	
	.music-ai-avatar {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}
	
	.travel-ai-avatar {
		background: linear-gradient(135deg, #20B2AA, #48D1CC);
	}
	
	.default-ai-avatar {
		background: linear-gradient(135deg, #9E9E9E, #757575);
	}
	
	.avatar-image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
	}
	
	.avatar-text {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.purchase-info {
		margin-top: 8rpx;
	}
	
	.purchase-time {
		font-size: 20rpx;
		color: #4CAF50;
	}
	
	.ai-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.ai-name {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.ai-description {
		font-size: 26rpx;
		color: #666666;
		line-height: 1.4;
	}
	
	/* 统计信息 */
	.ai-stats {
		display: flex;
		justify-content: space-between;
		margin-top: 16rpx;
		padding: 12rpx 0;
		border-top: 1rpx solid #f0f0f0;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.stat-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		flex: 1;
	}
	
	.stat-value {
		font-size: 28rpx;
		font-weight: bold;
		color: #333333;
	}
	
	.stat-label {
		font-size: 20rpx;
		color: #999999;
		margin-top: 4rpx;
	}
	
	/* 互动数据 */
	.ai-interactions {
		display: flex;
		justify-content: space-around;
		margin-top: 12rpx;
		padding: 8rpx 0;
	}
	
	.interaction-item {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.interaction-icon {
		font-size: 24rpx;
	}
	
	.interaction-count {
		font-size: 22rpx;
		color: #666666;
	}
	
	.ai-price {
		display: flex;
		align-items: center;
		gap: 8rpx;
		margin-top: 8rpx;
	}
	
	.price-text {
		font-size: 28rpx;
		color: #FF69B4;
		font-weight: bold;
	}
	
	.price-label {
		font-size: 24rpx;
		color: #999999;
	}
	
	.ai-status {
		margin-left: 24rpx;
	}
	
	.purchased-badge {
		background-color: #E8F5E8;
		border: 1rpx solid #4CAF50;
		border-radius: 20rpx;
		padding: 12rpx 20rpx;
	}
	
	.badge-text {
		font-size: 24rpx;
		color: #4CAF50;
		font-weight: 500;
	}
	
	.buy-btn {
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 20rpx;
		padding: 12rpx 20rpx;
	}
	
	.buy-text {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 500;
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
		border: 4rpx solid #f0f0f0;
		border-top: 4rpx solid #FF69B4;
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
		color: #666666;
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
		color: #666666;
		text-align: center;
		margin-bottom: 32rpx;
	}
	
	.retry-button {
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
	}
	
	.retry-text {
		font-size: 28rpx;
		color: #FFFFFF;
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
		color: #333333;
		font-weight: 600;
		margin-bottom: 16rpx;
	}
	
	.empty-desc {
		font-size: 28rpx;
		color: #666666;
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
