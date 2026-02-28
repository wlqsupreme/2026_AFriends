<template>
	<view class="container">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部区域 -->
		<view class="header">
			<!-- <view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view> -->
			<view class="cartoon-character"></view>
		</view>
		
		<!-- 浮动信息卡片 -->
		<view class="floating-card" v-if="aiModelData">
			<view class="info-row">
				<text class="info-value">{{ formatRecommendCount(aiModelData.recommendCount) }}</text>
				<text class="info-label">推荐数</text>
			</view>
			<view class="info-row">
				<text class="info-value">{{ formatPurchaseCount(aiModelData.purchaseCount) }}</text>
				<text class="info-label">购买数</text>
			</view>
			<view class="info-row">
				<text class="info-value">{{ getAIPrice() }}</text>
				<text class="info-label">价格</text>
			</view>
		</view>
		
		<!-- 加载状态 -->
		<view v-if="loading" class="loading-container">
			<view class="loading-spinner"></view>
			<text class="loading-text">正在加载AI模型详情...</text>
		</view>
		
		<!-- 错误状态 -->
		<view v-else-if="errorMessage" class="error-container">
			<view class="error-icon">⚠️</view>
			<text class="error-text">{{ errorMessage }}</text>
			<view class="retry-button" @click="loadAiModelDetail">
				<text class="retry-text">重试</text>
			</view>
		</view>
		
		<!-- 主要内容区域 -->
		<view v-else class="main-content">
			<view class="product-title">{{ aiName }}</view>
			
			<!-- 产品信息和操作按钮区域 -->
			<view class="product-info-section">
				<view class="product-content">
					<view class="product-description">
						产品描述: {{ getAIDescription() }}
					</view>
					
					<!-- 图片网格 -->
					<view class="image-grid">
						<view class="image-item">
							<view class="cat-image cat-1"></view>
						</view>
						<view class="image-item">
							<view class="cat-image cat-2"></view>
						</view>
						<view class="image-item">
							<view class="cat-image cat-3"></view>
						</view>
						<view class="image-item">
							<view class="cat-image cat-4"></view>
						</view>
					</view>
				</view>
				
				<!-- 右侧操作按钮 -->
				<view class="right-action-buttons">
					<!-- 已购买状态 -->
					<view v-if="hasPurchased" class="action-item purchased-item">
						<view class="action-icon purchased-icon">
							<text class="icon-text">✅</text>
						</view>
						<text class="action-text">已购买</text>
						<text class="purchase-time-text">购买后可永久使用</text>
					</view>
					
					<!-- 未购买状态 -->
					<view v-else class="action-item" @click="buyAI">
						<view class="action-icon buy-icon">
							<text class="icon-text">🛒</text>
						</view>
						<text class="action-text">立刻购买</text>
						<text class="balance-text" v-if="userGold !== null">我的余额:{{ formatGoldBalance(userGold) }}C</text>
						<text class="balance-text balance-error" v-else>获取余额失败</text>
					</view>
					
					<view class="action-item" @click="tryAI">
						<view class="action-icon try-icon">
							<text class="icon-text">⏰</text>
						</view>
						<text class="action-text">试用模型</text>
					</view>
				</view>
			</view>
			
			<!-- 用户评价区域 -->
			<view class="reviews-section">
				<!-- 购买状态提示 -->
				<view class="purchase-status-hint" v-if="!hasPurchased">
					<text class="hint-icon">💡</text>
					<text class="hint-text">购买此AI模型后即可发表评价和回复</text>
				</view>
				
				<view class="reviews-header">
					<view class="reviews-left">
						<text class="reviews-title">用户评价 ({{ getTotalReviewsCount() }})</text>
					</view>
					<view class="review-button" @click="showReviewInput" :class="{ 'disabled': !hasPurchased }">
						<text class="review-button-text">{{ hasPurchased ? '评价' : '购买后评价' }}</text>
					</view>
				</view>
				
				<!-- 评论加载状态 -->
				<view v-if="reviewsLoading" class="reviews-loading">
					<view class="loading-spinner"></view>
					<text class="loading-text">正在加载评论...</text>
				</view>
				
				<!-- 评论错误状态 -->
				<view v-else-if="reviewsError" class="reviews-error">
					<text class="error-text">{{ reviewsError }}</text>
					<view class="retry-button" @click="loadAiReviews">
						<text class="retry-text">重试</text>
					</view>
				</view>
				
				<!-- 暂无评论状态 -->
				<view v-else-if="aiReviews.length === 0" class="reviews-empty">
					<view class="empty-icon">💬</view>
					<text class="empty-text">暂无评价</text>
					<text class="empty-desc">成为第一个评价此AI模型的用户吧！</text>
				</view>
				
				<!-- 评价列表 -->
				<view v-else class="reviews-list">
					<view 
						class="review-item" 
						v-for="(review, index) in aiReviews" 
						:key="review.id || index"
					>
						<view class="review-avatar">
							<image class="review-user-avatar" :src="review.userAvatar" mode="aspectFill"></image>
						</view>
						<view class="review-content">
							<view class="review-user-info">
								<text class="review-username">{{ review.username }}</text>
								<text class="review-time">{{ review.time }}</text>
							</view>
							<view class="review-rating">
								<text class="star" v-for="i in 5" :key="i" :class="{ 'active': i <= review.rating }">⭐</text>
							</view>
							<text class="review-text">{{ review.content }}</text>
							<!-- 移除点赞和回复功能 -->
						</view>
					</view>
				</view>
			</view>
		</view>
		

		
		<!-- 评价输入弹窗 -->
		<view class="review-modal" v-if="showReviewModal" @click="hideReviewModal">
			<view class="review-modal-content" @click.stop>
				<view class="review-modal-header">
					<text class="review-modal-title">写评价</text>
					<view class="close-button" @click="hideReviewModal">
						<text class="close-icon">×</text>
					</view>
				</view>
				
				<view class="review-rating-input">
					<text class="rating-label">评分:</text>
					<view class="stars-input">
						<text 
							class="star-input" 
							v-for="i in 5" :key="i" 
							:class="{ 'active': i <= reviewRating }"
							@click="setRating(i)"
						>⭐</text>
					</view>
				</view>
				
				<textarea 
					class="review-text-input" 
					v-model="reviewText" 
					placeholder="请输入您的评价内容..."
					maxlength="500"
				></textarea>
				
				<view class="review-modal-actions">
					<view class="cancel-button" @click="hideReviewModal">
						<text class="cancel-text">取消</text>
					</view>
					<view class="submit-button" @click="submitReview" :class="{ 'active': canSubmitReview }">
						<text class="submit-text">提交评价</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 移除回复弹窗 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				aiName: '宠物交友AI',
				modelId: null,
				aiModelData: null, // AI模型详细信息
				loading: false, // 加载状态
				errorMessage: '', // 错误信息
				// 用户购买状态
				hasPurchased: false,
				// 用户金币余额
				userGold: null,
				// 评价相关数据
				showReviewModal: false,
				reviewRating: 0,
				reviewText: '',
				// 移除回复相关数据
				// AI评价数据
				aiReviews: [],
				reviewsLoading: false, // 评论加载状态
				reviewsError: '' // 评论加载错误
			}
		},
		computed: {
			// 是否可以提交评价
			canSubmitReview() {
				return this.reviewRating > 0 && this.reviewText.trim().length > 0;
			}
		},
		onLoad(options) {
			if (options.aiName) {
				this.aiName = decodeURIComponent(options.aiName);
			}
			if (options.modelId) {
				this.modelId = options.modelId;
			}
			
			// 加载AI模型详细信息
			this.loadAiModelDetail();
			
			// 加载AI模型评论
			this.loadAiReviews();
			
			// 检查AI模型的购买状态
			this.checkPurchaseStatus();
		},
		methods: {
			// 加载AI模型详细信息
			async loadAiModelDetail() {
				if (!this.modelId) {
					console.error('模型ID不存在');
					this.errorMessage = '模型ID不存在';
					return;
				}
				
				try {
					this.loading = true;
					this.errorMessage = '';
					
					const response = await this.getAimodelBaseInfoById(this.modelId);
					if (response && response.data) {
						this.aiModelData = response.data;
						this.aiName = response.data.modelName || this.aiName;
						console.log('AI模型详情加载成功:', this.aiModelData);
					} else {
						this.errorMessage = 'AI模型不存在';
					}
				} catch (error) {
					console.error('加载AI模型详情失败:', error);
					this.errorMessage = '加载失败，请重试';
				} finally {
					this.loading = false;
				}
			},
			
			// 获取AI模型详情
			getAimodelBaseInfoById(modelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseUrl}/api/a-entities/aimodel-base-info/${modelId}`,
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
			
			// 加载AI模型评论
			async loadAiReviews() {
				if (!this.modelId) {
					console.error('模型ID不存在，无法加载评论');
					return;
				}
				
				try {
					this.reviewsLoading = true;
					this.reviewsError = '';
					
					const response = await this.getUserAiCommentsByAimodelId(this.modelId);
					if (response && response.data) {
						this.aiReviews = this.convertCommentsToReviews(response.data);
						console.log('AI模型评论加载成功:', this.aiReviews);
					} else {
						this.aiReviews = [];
					}
				} catch (error) {
					console.error('加载AI模型评论失败:', error);
					this.reviewsError = '加载评论失败，请重试';
					this.aiReviews = [];
				} finally {
					this.reviewsLoading = false;
				}
			},
			
			// 获取AI模型评论
			getUserAiCommentsByAimodelId(aimodelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-ai-comment/aimodel/${aimodelId}`,
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
			
			// 转换评论数据为前端显示格式
			convertCommentsToReviews(comments) {
				return comments.map(comment => ({
					id: comment.userAiCommentId,
					username: `用户${comment.userId}`,
					userAvatar: '/static/avatar1.png', // 默认头像
					time: this.formatCommentTime(comment.createdAt),
					rating: comment.stars || 5,
					content: comment.commentText || '暂无评价内容'
				}));
			},
			
			// 格式化评论时间
			formatCommentTime(timestamp) {
				if (!timestamp) return '未知时间';
				
				const now = new Date();
				const commentTime = new Date(timestamp);
				const diffMs = now - commentTime;
				const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
				
				if (diffDays === 0) {
					return '今天';
				} else if (diffDays === 1) {
					return '昨天';
				} else if (diffDays < 7) {
					return `${diffDays}天前`;
				} else if (diffDays < 30) {
					const weeks = Math.floor(diffDays / 7);
					return `${weeks}周前`;
				} else {
					const months = Math.floor(diffDays / 30);
					return `${months}个月前`;
				}
			},
			
			// 检查AI模型的购买状态
			async checkPurchaseStatus() {
				try {
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						console.log('用户未登录，无法检查购买状态');
						this.hasPurchased = false;
						this.userGold = null;
						return;
					}
					
					// 并行获取用户购买状态和金币余额
					const [purchaseResponse, goldResponse] = await Promise.allSettled([
						this.getUserAiModels(userId),
						this.getUserGoldBalance(userId)
					]);
					
					// 处理购买状态
					if (purchaseResponse.status === 'fulfilled' && purchaseResponse.value && purchaseResponse.value.data) {
						// 检查当前AI模型是否在用户的购买列表中
						this.hasPurchased = purchaseResponse.value.data.some(userModel => 
							userModel.parentModelId == this.modelId || 
							userModel.modelName === this.aiName
						);
						console.log(`AI模型 ${this.aiName} (ID: ${this.modelId}) 购买状态:`, this.hasPurchased);
					} else {
						this.hasPurchased = false;
						console.error('获取购买状态失败:', purchaseResponse.reason);
					}
					
					// 处理金币余额
					if (goldResponse.status === 'fulfilled' && goldResponse.value && goldResponse.value.data) {
						this.userGold = goldResponse.value.data.gold;
						console.log('用户金币余额:', this.userGold);
					} else {
						this.userGold = null;
						console.error('获取用户金币余额失败:', goldResponse.reason);
					}
				} catch (error) {
					console.error('检查购买状态失败:', error);
					this.hasPurchased = false;
					this.userGold = null;
				}
			},
			
			// 获取用户AI模型列表
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
			
			// 获取用户金币余额
			getUserGoldBalance(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseUrl}/api/users/${userId}`,
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
			
			// 保存购买状态到本地存储
			savePurchaseStatus() {
				const purchasedAIs = uni.getStorageSync('purchasedAIs') || [];
				if (!purchasedAIs.includes(this.aiName)) {
					purchasedAIs.push(this.aiName);
					uni.setStorageSync('purchasedAIs', purchasedAIs);
					console.log('购买状态已保存到本地存储');
				}
			},
			
			getAIDescription() {
				if (this.aiModelData && this.aiModelData.modelDesc) {
					return this.aiModelData.modelDesc;
				}
				return '这是一个智能交友AI，可以帮助你找到志同道合的朋友';
			},
			getAIPrice() {
				if (this.aiModelData && this.aiModelData.price) {
					const goldCoins = Math.round(this.aiModelData.price * 100);
					return goldCoins + 'C';
				}
				return '15000C';
			},
			
			// 格式化金币余额显示
			formatGoldBalance(gold) {
				if (!gold && gold !== 0) return '0';
				
				// 如果金币数量很大，使用K、M等单位
				if (gold >= 1000000) {
					return (gold / 1000000).toFixed(1) + 'M';
				} else if (gold >= 1000) {
					return (gold / 1000).toFixed(1) + 'K';
				} else {
					return gold.toString();
				}
			},
			async buyAI() {
				try {
					// 检查是否已经购买
					if (this.hasPurchased) {
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
							content: `确定要购买${this.aiName}吗？价格：${this.getAIPrice()}`,
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
					const purchaseResult = await this.purchaseAiModel(userId);
					
					uni.hideLoading();
					
					if (purchaseResult.success) {
						// 更新购买状态
						this.hasPurchased = true;
						
						uni.showToast({
							title: '购买成功！',
							icon: 'success'
						});
						
						// 延迟跳转到AI聊天页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/ai/ai-chat?modelId=${this.modelId}&modelName=${encodeURIComponent(this.aiName)}`
							});
						}, 1500);
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
			
			// 调用购买API
			purchaseAiModel(userId) {
				return new Promise((resolve, reject) => {
					const purchaseData = {
						userId: parseInt(userId),
						parentModelId: this.modelId,
						modelName: this.aiName,
						modelDesc: this.aiModelData ? this.aiModelData.modelDesc : '',
						modelImageUrl: this.aiModelData ? this.aiModelData.modelImageUrl : '',
						price: this.aiModelData ? this.aiModelData.price : 0
					};
					
					console.log('发送购买请求:', purchaseData);
					
					uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-ai-model/purchase`,
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
			async tryAI() {
				try {
					// 检查是否已经购买
					if (this.hasPurchased) {
						uni.showToast({
							title: '您已购买此AI模型，无需试用',
							icon: 'none'
						});
						return;
					}
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					// 显示加载状态
					uni.showLoading({
						title: '试用中...'
					});
					
					// 调用试用API
					const tryOutResult = await this.tryOutAiModel(userId);
					
					uni.hideLoading();
					
					if (tryOutResult.success) {
						uni.showToast({
							title: tryOutResult.message,
							icon: 'success'
						});
						
						// 延迟跳转到AI聊天页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/ai/ai-chat?modelId=${this.modelId}&modelName=${encodeURIComponent(this.aiName)}&isTryOut=true`
							});
						}, 1500);
					} else {
						uni.showToast({
							title: tryOutResult.message || '试用失败',
							icon: 'none'
						});
					}
				} catch (error) {
					uni.hideLoading();
					console.error('试用AI模型失败:', error);
					uni.showToast({
						title: '试用失败，请重试',
						icon: 'none'
					});
				}
			},
			
			// 调用试用API
			tryOutAiModel(userId) {
				return new Promise((resolve, reject) => {
					const tryOutData = {
						userId: parseInt(userId),
						parentModelId: this.modelId
					};
					
					console.log('发送试用请求:', tryOutData);
					
					uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/try-out`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: tryOutData,
						timeout: 10000,
						success: (res) => {
							console.log('试用响应:', res);
							resolve(res.data);
						},
						fail: (err) => {
							console.error('试用请求失败:', err);
							reject(err);
						}
					});
				});
			},
			goBack() {
				uni.navigateBack();
			},
			
			// 格式化数字
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
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
			
			// 获取总评价数
			getTotalReviewsCount() {
				return this.aiReviews.length;
			},
			
			// 移除点赞相关方法
			
			// 显示评价输入框
			showReviewInput() {
				if (!this.hasPurchased) {
					uni.showModal({
						title: '提示',
						content: '您需要先购买此AI模型才能评价',
						showCancel: false,
						confirmText: '知道了'
					});
					return;
				}
				
				this.reviewRating = 0;
				this.reviewText = '';
				this.showReviewModal = true;
			},
			
			// 隐藏评价输入框
			hideReviewModal() {
				this.showReviewModal = false;
				this.reviewRating = 0;
				this.reviewText = '';
			},
			
			// 设置评分
			setRating(rating) {
				this.reviewRating = rating;
			},
			
			// 提交评价
			async submitReview() {
				if (this.reviewRating === 0) {
					uni.showToast({
						title: '请选择评分',
						icon: 'none'
					});
					return;
				}
				
				if (!this.reviewText.trim()) {
					uni.showToast({
						title: '请输入评价内容',
						icon: 'none'
					});
					return;
				}
				
				try {
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					// 显示加载状态
					uni.showLoading({
						title: '提交中...'
					});
					
					// 调用保存评价API
					const saveResult = await this.saveUserAiComment(userId);
					
					uni.hideLoading();
					
					if (saveResult.success) {
						// 重新加载评价列表
						await this.loadAiReviews();
						
						uni.showToast({
							title: '评价提交成功',
							icon: 'success'
						});
						
						this.hideReviewModal();
					} else {
						uni.showToast({
							title: saveResult.message || '评价提交失败',
							icon: 'none'
						});
					}
				} catch (error) {
					uni.hideLoading();
					console.error('提交评价失败:', error);
					uni.showToast({
						title: '评价提交失败，请重试',
						icon: 'none'
					});
				}
			},
			
			// 调用保存评价API
			saveUserAiComment(userId) {
				return new Promise((resolve, reject) => {
					const commentData = {
						aimodelId: this.modelId,
						userId: parseInt(userId),
						commentText: this.reviewText,
						stars: this.reviewRating
					};
					
					console.log('发送评价请求:', commentData);
					
					uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-ai-comment/save`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: commentData,
						timeout: 10000,
						success: (res) => {
							console.log('评价响应:', res);
							resolve(res.data);
						},
						fail: (err) => {
							console.error('评价请求失败:', err);
							reject(err);
						}
					});
				});
			},
			
			// 移除回复相关方法
		}
	}
</script>

<style>
	.container {
		position: relative;
		min-height: 100vh;
		background-color: #fff;
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		z-index: 1000;
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
	
	.signal, .wifi, .battery {
		width: 40rpx;
		height: 20rpx;
		background-color: #000;
		border-radius: 4rpx;
	}
	
	/* 头部区域 */
	.header {
		position: relative;
		height: 400rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		overflow: hidden;
	}
	
	.back-button {
		position: absolute;
		top: 60rpx;
		left: 40rpx;
		width: 80rpx;
		height: 80rpx;
		background-color: rgba(255, 255, 255, 0.9);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 10;
	}
	
	.back-arrow {
		font-size: 60rpx;
		color: #FF69B4;
		font-weight: bold;
	}
	
	.cartoon-character {
		position: absolute;
		top: -20rpx;
		right: -40rpx;
		width: 600rpx;
		height: 500rpx;
		background: linear-gradient(135deg, #FFE4E1, #FFC0CB);
		border-radius: 50% 50% 50% 50% / 60% 60% 40% 40%;
		box-shadow: 0 10rpx 30rpx rgba(255, 105, 180, 0.3);
	}
	
	.cartoon-character::before {
		content: '';
		position: absolute;
		top: 120rpx;
		left: 120rpx;
		width: 40rpx;
		height: 40rpx;
		background-color: #000;
		border-radius: 50%;
		box-shadow: 200rpx 0 0 #000;
	}
	
	.cartoon-character::after {
		content: '';
		position: absolute;
		top: 200rpx;
		left: 150rpx;
		width: 120rpx;
		height: 60rpx;
		border: 8rpx solid #000;
		border-top: none;
		border-radius: 0 0 120rpx 120rpx;
	}
	
	/* 浮动信息卡片 */
	.floating-card {
		position: absolute;
		top: 320rpx;
		left: 40rpx;
		width: 300rpx;
		background-color: rgba(255, 255, 255, 0.95);
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
		z-index: 20;
	}
	
	.info-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}
	
	.info-row:last-child {
		margin-bottom: 0;
	}
	
	.info-value {
		font-size: 36rpx;
		font-weight: bold;
		color: #FF69B4;
	}
	
	.info-label {
		font-size: 28rpx;
		color: #666;
	}
	
	/* 主要内容区域 */
	.main-content {
		margin-top: 200rpx;
		padding: 0 40rpx 40rpx 40rpx;
	}
	
	.product-title {
		font-size: 48rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 30rpx;
	}
	
	/* 产品信息和操作按钮区域 */
	.product-info-section {
		display: flex;
		align-items: flex-start;
		gap: 40rpx;
		margin-bottom: 40rpx;
	}
	
	.product-content {
		flex: 1;
	}
	
	.product-description {
		font-size: 28rpx;
		color: #666;
		line-height: 1.6;
		margin-bottom: 40rpx;
	}
	
	/* 图片网格 */
	.image-grid {
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 20rpx;
		margin-bottom: 40rpx;
	}
	
	.image-item {
		aspect-ratio: 1;
	}
	
	.cat-image {
		width: 100%;
		height: 100%;
		border-radius: 20rpx;
		background-size: cover;
		background-position: center;
		box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
	}
	
	.cat-1 {
		background-color: #FFA07A;
		background-image: linear-gradient(45deg, #FFA07A, #FFB6C1);
	}
	
	.cat-2 {
		background-color: #D3D3D3;
		background-image: linear-gradient(45deg, #D3D3D3, #C0C0C0);
	}
	
	.cat-3 {
		background-color: #FFA500;
		background-image: linear-gradient(45deg, #FFA500, #FF8C00);
	}
	
	.cat-4 {
		background-color: #FF6347;
		background-image: linear-gradient(45deg, #FF6347, #FF4500);
	}
	
	/* 右侧操作按钮 */
	.right-action-buttons {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
		flex-shrink: 0;
	}
	
	.right-action-buttons .action-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 15rpx;
		padding: 20rpx;
		background-color: #fff;
		border-radius: 20rpx;
		border: 2rpx solid #f0f0f0;
		box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
		transition: all 0.3s ease;
	}
	
	.right-action-buttons .action-item:active {
		transform: scale(0.95);
		background-color: #f8f8f8;
	}
	
	.right-action-buttons .action-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 5rpx 15rpx rgba(255, 105, 180, 0.3);
	}
	
	.right-action-buttons .icon-text {
		font-size: 36rpx;
		color: #fff;
	}
	
	.right-action-buttons .action-text {
		font-size: 24rpx;
		color: #FF69B4;
		font-weight: 600;
		text-align: center;
	}
	
	.right-action-buttons .balance-text {
		font-size: 20rpx;
		color: #999;
		text-align: center;
		max-width: 150rpx;
		line-height: 1.3;
	}
	
	.right-action-buttons .balance-error {
		color: #ff4757;
		font-weight: bold;
	}
	
	/* 已购买状态样式 */
	.purchased-item {
		background-color: #E8F5E8 !important;
		border: 2rpx solid #4CAF50 !important;
	}
	
	.purchased-icon {
		background: linear-gradient(135deg, #4CAF50, #66BB6A) !important;
	}
	
	.purchase-time-text {
		font-size: 20rpx;
		color: #4CAF50;
		text-align: center;
		max-width: 150rpx;
		line-height: 1.3;
		font-weight: 500;
	}
	
	/* 购买状态提示 */
	.purchase-status-hint {
		display: flex;
		align-items: center;
		gap: 12rpx;
		padding: 20rpx;
		background-color: #FFF3E0;
		border-left: 4rpx solid #FF9800;
		border-radius: 8rpx;
		margin-bottom: 24rpx;
	}
	
	.hint-icon {
		font-size: 24rpx;
	}
	
	.hint-text {
		font-size: 24rpx;
		color: #E65100;
		line-height: 1.4;
	}
	
	/* 用户评价区域 */
	.reviews-section {
		padding: 32rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
		margin-top: 40rpx;
	}
	
	.reviews-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}
	
	.reviews-left {
		flex: 1;
	}
	
	.reviews-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.review-button {
		padding: 12rpx 24rpx;
		background-color: #FF69B4;
		border-radius: 20rpx;
		border: none;
		transition: all 0.3s ease;
	}
	
	.review-button:active {
		transform: scale(0.95);
		background-color: #FF4D94;
	}
	
	.review-button-text {
		color: #fff;
		font-size: 26rpx;
		font-weight: 500;
	}
	
	/* 评价按钮禁用状态 */
	.review-button.disabled {
		background-color: #CCCCCC;
		cursor: not-allowed;
	}
	
	.review-button.disabled:active {
		transform: none;
		background-color: #CCCCCC;
	}
	
	/* 评价列表 */
	.reviews-list {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}
	
	.review-item {
		display: flex;
		gap: 16rpx;
		padding: 24rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.review-item:last-child {
		border-bottom: none;
	}
	
	.review-avatar {
		flex-shrink: 0;
	}
	
	.review-user-avatar {
		width: 64rpx;
		height: 64rpx;
		border-radius: 32rpx;
		background-color: #F5F5F5;
	}
	
	.review-content {
		flex: 1;
	}
	
	.review-user-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 8rpx;
	}
	
	.review-username {
		font-size: 26rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.review-time {
		font-size: 22rpx;
		color: #999999;
	}
	
	.review-rating {
		margin-bottom: 12rpx;
	}
	
	.star {
		font-size: 28rpx;
		color: #DDDDDD;
		margin-right: 6rpx;
		opacity: 0.6;
	}
	
	.star.active {
		color: #FFD700;
		opacity: 1;
		text-shadow: 0 0 8rpx rgba(255, 215, 0, 0.5);
	}
	
	.review-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.5;
		margin-bottom: 16rpx;
	}
	
	.review-actions {
		display: flex;
		gap: 24rpx;
	}
	
	.review-action {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.review-action-icon {
		font-size: 24rpx;
		color: #999999;
	}
	
	.review-action-count {
		font-size: 22rpx;
		color: #999999;
	}
	
	.review-action-icon.active {
		color: #FFD700;
	}
	
	/* 回复按钮禁用状态 */
	.review-action.disabled {
		cursor: not-allowed;
		opacity: 0.6;
	}
	
	.review-action-icon.disabled {
		color: #CCCCCC;
	}
	
	.review-action-count.disabled {
		color: #CCCCCC;
	}
	
	/* 回复列表 */
	.replies-list {
		margin-top: 16rpx;
		margin-left: 80rpx;
		border-left: 2rpx solid #F0F0F0;
		padding-left: 16rpx;
	}
	
	.reply-item {
		display: flex;
		gap: 12rpx;
		margin-bottom: 16rpx;
	}
	
	.reply-avatar {
		flex-shrink: 0;
	}
	
	.reply-user-avatar {
		width: 48rpx;
		height: 48rpx;
		border-radius: 24rpx;
		background-color: #F5F5F5;
	}
	
	.reply-content {
		flex: 1;
	}
	
	.reply-user-info {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 6rpx;
	}
	
	.reply-username {
		font-size: 24rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.reply-time {
		font-size: 20rpx;
		color: #999999;
	}
	
	.reply-text {
		font-size: 26rpx;
		color: #333333;
		line-height: 1.4;
		margin-bottom: 12rpx;
	}
	
	.reply-to {
		color: #FF69B4;
		font-weight: 500;
	}
	
	.reply-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.reply-action {
		display: flex;
		align-items: center;
		gap: 6rpx;
	}
	
	/* 评价弹窗 */
	.review-modal, .reply-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.review-modal-content, .reply-modal-content {
		width: 80%;
		max-width: 600rpx;
		background-color: #fff;
		border-radius: 20rpx;
		padding: 40rpx;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.3);
	}
	
	.review-modal-header, .reply-modal-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}
	
	.review-modal-title, .reply-modal-title {
		font-size: 36rpx;
		color: #333;
		font-weight: 600;
	}
	
	.close-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 50%;
		background-color: #f5f5f5;
		cursor: pointer;
	}
	
	.close-icon {
		font-size: 40rpx;
		color: #999;
		font-weight: bold;
	}
	
	.review-rating-input {
		display: flex;
		align-items: center;
		gap: 20rpx;
		margin-bottom: 30rpx;
	}
	
	.rating-label {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
	}
	
	.stars-input {
		display: flex;
		gap: 10rpx;
	}
	
	.star-input {
		font-size: 48rpx;
		color: #DDDDDD;
		cursor: pointer;
		transition: all 0.3s ease;
		margin-right: 8rpx;
		opacity: 0.6;
	}
	
	.star-input.active {
		color: #FFD700;
		opacity: 1;
		text-shadow: 0 0 12rpx rgba(255, 215, 0, 0.6);
		transform: scale(1.1);
	}
	
	.review-text-input, .reply-text-input {
		width: 100%;
		height: 200rpx;
		padding: 20rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 10rpx;
		font-size: 28rpx;
		color: #333;
		background-color: #fff;
		resize: none;
		margin-bottom: 30rpx;
	}
	
	.reply-text-input {
		height: 150rpx;
	}
	
	.review-modal-actions, .reply-modal-actions {
		display: flex;
		gap: 20rpx;
		justify-content: flex-end;
	}
	
	.cancel-button, .submit-button {
		padding: 20rpx 40rpx;
		border-radius: 25rpx;
		font-size: 28rpx;
		font-weight: 500;
		text-align: center;
		transition: all 0.3s ease;
	}
	
	.cancel-button {
		background-color: #f5f5f5;
		color: #666;
		border: 2rpx solid #e0e0e0;
	}
	
	.submit-button {
		background-color: #ccc;
		color: #fff;
		border: none;
	}
	
	.submit-button.active {
		background-color: #FF69B4;
	}
	
	/* 加载状态 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 40rpx;
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
	
	.loading-text {
		font-size: 28rpx;
		color: #666666;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	/* 错误状态 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 40rpx;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 24rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: #666666;
		margin-bottom: 40rpx;
		text-align: center;
	}
	
	.retry-button {
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
	}
	
	.retry-text {
		color: white;
		font-size: 28rpx;
		font-weight: 500;
	}
	
	/* 评论相关样式 */
	.reviews-loading {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 0;
	}
	
	.reviews-loading .loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 3rpx solid #f0f0f0;
		border-top: 3rpx solid #FF69B4;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 16rpx;
	}
	
	.reviews-loading .loading-text {
		font-size: 24rpx;
		color: #666666;
	}
	
	.reviews-error {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 0;
	}
	
	.reviews-error .error-text {
		font-size: 24rpx;
		color: #666666;
		margin-bottom: 20rpx;
	}
	
	.reviews-error .retry-button {
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 20rpx;
		padding: 12rpx 24rpx;
	}
	
	.reviews-error .retry-text {
		color: white;
		font-size: 24rpx;
		font-weight: 500;
	}
	
	.reviews-empty {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 0;
	}
	
	.reviews-empty .empty-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		opacity: 0.5;
	}
	
	.reviews-empty .empty-text {
		font-size: 28rpx;
		color: #666666;
		margin-bottom: 8rpx;
	}
	
	.reviews-empty .empty-desc {
		font-size: 24rpx;
		color: #999999;
	}
</style>
