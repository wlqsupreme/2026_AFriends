<template>
	<view class="more-reviews-page" :class="{ concise: isConcise }">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<view class="header-title">
				<text class="title-text">热门书评</text>
			</view>
			<view class="share-button" v-if="!isConcise" @click="openSharePopup">
				<text class="share-icon">📤</text>
			</view>
		</view>
		
		<!-- 小说信息卡片 -->
		<view class="novel-card">
			<view class="novel-info">
				<image class="novel-cover" :src="novelCover || '/static/novel-cover.jpg'" mode="aspectFill"></image>
				<view class="novel-details">
					<text class="novel-title">{{ novelTitle || '小说标题' }}</text>
					<text class="novel-author">{{ novelAuthor || '作者' }}</text>
					<view class="novel-stats" v-if="!isConcise">
						<text class="rating">{{ novelRating || '9.5' }}分</text>
						<text class="reader-count">{{ readerCount || '157.4万人在读' }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 评论列表 -->
		<scroll-view 
			class="reviews-scroll"
			scroll-y="true"
			refresher-enabled="true"
			:refresher-triggered="isRefreshing"
			@refresherrefresh="onRefresh"
		>
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<text class="loading-text">正在加载书评数据...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadNovelReviews">重新加载</button>
				</view>
			</view>
			
			<!-- 正常内容 -->
			<view v-if="!hasError && !loading">
			<!-- 刷新指示器 -->
			<view class="refresh-indicator" v-if="showRefreshIndicator">
				<view class="refresh-circle" :class="{ 'refreshing': isRefreshing }">
					<view class="refresh-arrow" v-if="!isRefreshing"></view>
					<view class="refresh-spinner" v-else></view>
				</view>
				<text class="refresh-text">{{ refreshText }}</text>
			</view>
			
			<!-- 评论列表区域 -->
			<view class="reviews-container">
				<view 
					class="review-item" 
					v-for="(review, reviewIndex) in displayReviews" 
					:key="reviewIndex"
					@click="goToReviewDetail(review, reviewIndex)"
				>
					<!-- 评论头部 -->
					<view class="review-header">
						<image class="reviewer-avatar" :src="review.avatar" mode="aspectFill"></image>
						<view class="reviewer-info">
							<text class="reviewer-name">{{ review.reviewer }}</text>
							<view class="review-meta" v-if="!isConcise">
								<view class="rating-stars">
									<text class="star" v-for="n in review.rating" :key="n">★</text>
									<text class="star empty" v-for="n in (5 - review.rating)" :key="n">☆</text>
								</view>
								<text class="review-time">{{ review.time }}</text>
							</view>
						</view>
					</view>
					
					<!-- 评论内容 -->
					<view class="review-content">
						<text class="review-text" :class="{ 'clamp-2': isConcise }">{{ review.content }}</text>
					</view>
					
					<!-- 评论互动 -->
					<view class="engagement-metrics">
						<view class="metric-item" @click.stop="handleLike(reviewIndex)">
							<text class="metric-icon" :class="{ 'active': review.isLiked }">👍</text>
							<text class="metric-count" :class="{ 'active': review.isLiked }">{{ formatCount(review.likes) }}</text>
						</view>
						<view class="metric-item" v-if="!isConcise" @click.stop="handleDislike(reviewIndex)">
							<text class="metric-icon" :class="{ 'active': review.isDisliked }">👎</text>
							<text class="metric-count" :class="{ 'active': review.isDisliked }">{{ formatCount(review.dislikes) }}</text>
						</view>
						<view class="metric-item" v-if="!isConcise" @click.stop="handleComment(reviewIndex)">
							<text class="metric-icon">💬</text>
							<text class="metric-count">{{ formatCount(review.comments) }}</text>
						</view>
					</view>
					
					<!-- 回复列表 -->
					<view class="replies-list" v-if="!isConcise && review.replies && review.replies.length > 0">
						<view 
							class="reply-item" 
							v-for="(reply, replyIndex) in review.replies" 
							:key="replyIndex"
						>
							<view class="reply-avatar">
								<image class="reply-user-avatar" :src="reply.userAvatar" mode="aspectFill"></image>
							</view>
							<view class="reply-content">
								<view class="reply-user-info">
									<text class="reply-username">{{ reply.username }}</text>
									<text class="reply-time">{{ reply.time }}</text>
								</view>
								<text class="reply-text">
									<text class="reply-to">回复 @{{ reply.replyTo }}：</text>
									{{ reply.content }}
								</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			</view> <!-- 结束正常内容区域 -->
		</scroll-view>
		
		<!-- 底部评论输入框 -->
		<view class="comment-input-section" v-if="!isConcise">
			<view class="comment-input-container">
				<input 
					class="comment-input" 
					v-model="commentText" 
					:placeholder="getInputPlaceholder()" 
					@focus="onInputFocus"
					@blur="onInputBlur"
				/>
				<!-- 取消回复按钮 -->
				<view class="cancel-reply-btn" v-if="replyToComment" @click="cancelReply">
					<text class="cancel-reply-text">取消</text>
				</view>
				<view class="send-button" @click="submitComment" :class="{ 'active': commentText.trim() }">
					<text class="send-text">发送</text>
				</view>
			</view>
		</view>
		
		<!-- 分享弹窗 -->
		<view class="share-popup" v-if="showSharePopup && !isConcise" @click="hideSharePopup">
			<view class="share-content" @click.stop>
				<view class="share-header">
					<text class="share-title">分享到</text>
					<view class="share-close" @click="hideSharePopup">
						<text class="close-icon">×</text>
					</view>
				</view>
				<view class="share-friends">
					<scroll-view class="friends-scroll" scroll-x="true">
						<view class="friend-item" v-for="(friend, friendIndex) in shareFriends" :key="friendIndex" @click="shareToFriend(friend)">
							<view class="friend-avatar">
								<image class="friend-avatar-img" :src="friend.avatar" mode="aspectFill"></image>
							</view>
							<text class="friend-name">{{ friend.name.length > 3 ? friend.name.substring(0, 3) + '...' : friend.name }}</text>
						</view>
					</scroll-view>
				</view>
				<view class="share-options">
					<view class="share-option" @click="shareToWechat">
						<view class="share-icon-bg wechat">
							<image class="share-icon-img" src="/static/wechat-icon.png" mode="aspectFit"></image>
						</view>
						<text class="share-option-text">微信</text>
					</view>
					<view class="share-option" @click="shareToMoments">
						<view class="share-icon-bg moments">
							<image class="share-icon-img" src="/static/wechat-icon.png" mode="aspectFit"></image>
						</view>
						<text class="share-option-text">朋友圈</text>
					</view>
					<view class="share-option" @click="shareToDouyin">
						<view class="share-icon-bg douyin">
							<image class="share-icon-img" src="/static/douyin-icon.png" mode="aspectFit"></image>
						</view>
						<text class="share-option-text">抖音好友</text>
					</view>
					<view class="share-option" @click="shareToDouyinPost">
						<view class="share-icon-bg douyin-post">
							<image class="share-icon-img" src="/static/douyin-icon.png" mode="aspectFit"></image>
						</view>
						<text class="share-option-text">发布到抖音</text>
					</view>
					<view class="share-option" @click="shareToQQ">
						<view class="share-icon-bg qq">
							<image class="share-icon-img" src="/static/qq-icon.png" mode="aspectFit"></image>
						</view>
						<text class="share-option-text">QQ</text>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'NovelMoreReviewsPage',
		data() {
			return {
				modeStore: null,
				novelId: '',
				novelTitle: '',
				novelAuthor: '',
				novelCover: '',
				novelRating: '',
				readerCount: '',
				userId: 1000100,
				isRefreshing: false,
				showRefreshIndicator: false,
				refreshText: '下拉刷新',
				pullDistance: 0,
				commentText: '',
				inputFocus: false,
				replyToComment: null,
				replyToReply: null,
				showSharePopup: false,
				loading: false,
				hasError: false,
				errorMessage: '',
				shareFriends: [
					{ name: '奶绿冰阔落', avatar: '/static/avatar-default.png' },
					{ name: '我想次炸鸡', avatar: '/static/avatar-default.png' },
					{ name: '匿名为某', avatar: '/static/avatar-default.png' },
					{ name: '康已奀', avatar: '/static/avatar-default.png' },
					{ name: '小菜刀御', avatar: '/static/avatar-default.png' },
					{ name: '超级好', avatar: '/static/avatar-default.png' },
					{ name: 'QQ好友七', avatar: '/static/avatar-default.png' }
				],

				reviews: [] // 从后端API获取的书评数据
			}
		},
		computed: {
			isConcise() {
				return this.modeStore && this.modeStore.isConcise;
			},
			displayReviews() {
				if (!Array.isArray(this.reviews)) return [];
				if (!this.isConcise) return this.reviews;
				return this.reviews.slice(0, 3);
			}
		},
		onLoad(options) {
			const themeStore = useThemeStore();
			themeStore.init();

			this.modeStore = useModeStore();
			this.modeStore.init();
			
			console.log('=== 更多书评页面加载 ===');
			console.log('接收到的参数:', options);
			
			// 接收传递的参数
			if (options.novelId) {
				this.novelId = options.novelId;
			}
			if (options.title) {
				this.novelTitle = decodeURIComponent(options.title);
			}
			if (options.author) {
				this.novelAuthor = decodeURIComponent(options.author);
			}
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			if (options.cover) {
				this.novelCover = decodeURIComponent(options.cover);
			}
			if (options.rating) {
				this.novelRating = options.rating;
			}
			if (options.readerCount) {
				this.readerCount = options.readerCount;
			}
			
			// 如果有novelId，从后端加载书评数据
			if (this.novelId) {
				this.loadNovelReviews();
			}
		},
		methods: {
			// 从后端API加载书评数据
			async loadNovelReviews() {
				try {
					console.log('=== 开始从后端加载书评数据 ===');
					console.log('小说ID:', this.novelId);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/novel-detail/reviews?novelId=${this.novelId}&userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 书评API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						this.reviews = response.data.data || [];
						console.log('成功加载书评数据，共', this.reviews.length, '条');
						
						if (this.reviews.length === 0) {
							this.hasError = true;
							this.errorMessage = '暂无书评数据';
						}
					} else {
						console.error('加载书评数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载书评数据失败';
					}
				} catch (error) {
					console.error('加载书评数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
				} finally {
					this.loading = false;
				}
			},
			// 返回功能
			goBack() {
				uni.navigateBack();
			},
			
			// 跳转到书评详情页
			goToReviewDetail(review, index) {
				// 使用书评的 commentId 作为 reviewId
				const reviewId = review.commentId || review.id;
				uni.navigateTo({
					url: `/pages/feed/review-detail?reviewId=${reviewId}`
				});
			},
			
			// 分享功能
			openSharePopup() {
				this.showSharePopup = true;
			},
			
			hideSharePopup() {
				this.showSharePopup = false;
			},
			
			// 分享相关方法
			shareToWechat() {
				uni.showToast({
					title: '分享到微信',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareToMoments() {
				uni.showToast({
					title: '分享到朋友圈',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareToDouyin() {
				uni.showToast({
					title: '分享到抖音好友',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareToDouyinPost() {
				uni.showToast({
					title: '发布到抖音',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareToQQ() {
				uni.showToast({
					title: '分享到QQ',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareAction(action) {
				const actionNames = {
					comment: '评论设置',
					gift: '送礼物',
					bookmark: '添加书签',
					search: '全文搜索',
					report: '举报与反馈'
				};
				uni.showToast({
					title: actionNames[action] || '功能开发中',
					icon: 'none'
				});
				this.hideSharePopup();
			},
			
			shareToFriend(friend) {
				uni.showToast({
					title: `分享给${friend.name}`,
					icon: 'none'
				});
				this.hideSharePopup();
			},
			

			
			// 评论点赞处理
			async handleLike(index) {
				const review = this.reviews[index];
				if (!review || !review.commentId) {
					uni.showToast({
						title: '评论ID不存在',
						icon: 'error'
					});
					return;
				}
				
				try {
					console.log('=== 开始处理书评点赞 ===');
					console.log('用户ID:', this.userId);
					console.log('评论ID:', review.commentId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/novel-detail/comment/like?userId=${this.userId}&commentId=${review.commentId}`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 书评点赞API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const result = response.data.data;
						
						// 更新书评状态
						review.isLiked = result.isLiked;
						review.likes = result.likeCount;
						
						// 如果点赞了，取消点踩状态
						if (review.isLiked && review.isDisliked) {
							review.isDisliked = false;
							review.dislikes--;
						}
						
						uni.showToast({
							title: result.message,
							icon: 'success',
							duration: 1000
						});
					} else {
						console.error('书评点赞失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '操作失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('书评点赞异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				}
			},
			
			// 评论踩处理
			handleDislike(index) {
				const review = this.reviews[index];
				if (review && typeof review === 'object') {
					review.isDisliked = !review.isDisliked;
					if (review.isDisliked) {
						review.dislikes++;
						if (review.isLiked) {
							review.isLiked = false;
							review.likes--;
						}
					} else {
						review.dislikes--;
					}
				}
			},
			
			// 评论处理
			handleComment(index) {
				// 设置回复状态，准备回复特定评论
				const review = this.reviews[index];
				if (review) {
					this.replyToComment = review;
					this.replyToReply = null;
					this.commentText = '';
					
					// 聚焦到输入框
					this.$nextTick(() => {
						const input = uni.createSelectorQuery().select('.comment-input');
						if (input) {
							input.focus();
						}
					});
				}
			},
			
			// 图片预览
			previewImage(images, current) {
				uni.previewImage({
					urls: images,
					current: current
				});
			},
			
			// 获取图片网格样式类
			getImageGridClass(count) {
				if (count === 1) return 'single-image';
				if (count === 2) return 'two-images';
				if (count === 3) return 'three-images';
				return 'grid-images';
			},
			
			// 格式化数字显示
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			

			
			// 收藏处理
			handleFavorite(post) {
				post.isFavorited = !post.isFavorited;
				if (post.isFavorited) {
					post.favorites++;
				} else {
					post.favorites--;
				}
			},
			
			// 输入框焦点处理
			onInputFocus() {
				this.inputFocus = true;
			},
			
			onInputBlur() {
				this.inputFocus = false;
			},
			
			// 提交评论
			async submitComment() {
				if (!this.commentText.trim()) {
					uni.showToast({
						title: '请输入评论内容',
						icon: 'none'
					});
					return;
				}
				
				try {
					// 判断是评论还是回复
					// 评论：parentCommentId 置为空
					// 回复：parentCommentId 设置为所回复的评论的 commentId
					const isReply = !!this.replyToComment;
					const parentCommentId = this.replyToComment ? this.replyToComment.commentId : null;
					
					// 调用后端API提交评论或回复
					const response = await uni.request({
						url: '${this.$baseUrl}/api/novel-detail/comment',
						method: 'POST',
						data: {
							novelId: this.novelId,
							userId: this.userId,
							commentText: this.commentText,
							parentCommentId: parentCommentId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200 && response.data.success) {
						// 成功提交后重新加载书评列表以获取最新数据
						await this.loadNovelReviews();
						
						// 重置回复状态
						this.replyToComment = null;
						this.replyToReply = null;
						
						uni.showToast({
							title: isReply ? '回复成功' : '评论成功',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: response.data.message || '提交失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('提交评论/回复失败:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				}
				
				// 清空输入框
				this.commentText = '';
			},
			
			// 取消回复
			cancelReply() {
				console.log('取消回复');
				this.commentText = '';
				this.replyToComment = null;
				this.replyToReply = null;
			},
			
			// 获取输入框placeholder
			getInputPlaceholder() {
				if (this.replyToComment) {
					if (this.replyToReply) {
						return `回复 @${this.replyToReply.username}...`;
					}
					return `回复 @${this.replyToComment.reviewer}...`;
				}
				return '说点什么...';
			},
			

			// 下拉刷新触发
			async onRefresh() {
				console.log('开始刷新书评数据');
				this.isRefreshing = true;
				this.refreshText = '正在刷新...';
				
				try {
					// 重新加载书评数据
					await this.loadNovelReviews();
					
					this.isRefreshing = false;
					this.refreshText = '刷新完成';
					
					// 显示刷新完成提示
					uni.showToast({
						title: '刷新成功',
						icon: 'success'
					});
					
					// 延迟隐藏刷新指示器
					setTimeout(() => {
						this.showRefreshIndicator = false;
					}, 1000);
				} catch (error) {
					console.error('刷新书评数据失败:', error);
					this.isRefreshing = false;
					this.refreshText = '刷新失败';
					
					uni.showToast({
						title: '刷新失败',
						icon: 'error'
					});
					
					setTimeout(() => {
						this.showRefreshIndicator = false;
					}, 1000);
				}
			},
			
			// 下拉过程中
			onPulling(e) {
				this.pullDistance = e.detail.dy;
				if (e.detail.dy > 50) {
					this.showRefreshIndicator = true;
					this.refreshText = '释放刷新';
				} else {
					this.refreshText = '下拉刷新';
				}
			},
			
			// 刷新器复位
			onRestore() {
				console.log('刷新器复位');
				this.showRefreshIndicator = false;
			},
			
			// 刷新器中止
			onAbort() {
				console.log('刷新器中止');
				this.showRefreshIndicator = false;
			}
		}
	}
</script>

<style>
	/* 全局样式变量定义 */
	:root {
		--color-bg: #FFFFFF;
		--color-bg-light: #F5F5F5;
		--color-text: #333333;
		--color-text-secondary: #666666;
		--color-text-placeholder: #999999;
		--color-border: #F0F0F0;
	}

	@media (prefers-color-scheme: dark) {
		:root {
			--color-bg: #000000;
			--color-bg-light: #121212;
			--color-text: #FFFFFF;
			--color-text-secondary: #CCCCCC;
			--color-text-placeholder: #999999;
			--color-border: #333333;
		}
	}

	.theme-dark {
		--color-bg: #000000;
		--color-bg-light: #121212;
		--color-text: #FFFFFF;
		--color-text-secondary: #CCCCCC;
		--color-text-placeholder: #999999;
		--color-border: #333333;
	}

	.theme-light {
		--color-bg: #FFFFFF;
		--color-bg-light: #F5F5F5;
		--color-text: #333333;
		--color-text-secondary: #666666;
		--color-text-placeholder: #999999;
		--color-border: #F0F0F0;
	}

	.more-reviews-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
	}

	.more-reviews-page.concise .novel-card {
		margin: 16rpx 24rpx;
		padding: 20rpx;
	}

	.more-reviews-page.concise .reviews-container {
		padding: 0 24rpx;
	}

	.more-reviews-page.concise .review-item {
		padding: 20rpx;
		margin-bottom: 16rpx;
	}

	.more-reviews-page.concise .reviews-scroll {
		height: calc(100vh - 88rpx - 208rpx);
		margin-bottom: 0;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: var(--color-bg, #FFFFFF);
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
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		background-color: var(--color-bg, #FFFFFF);
	}
	
	/* 小说信息卡片 */
	.novel-card {
		margin: 24rpx 32rpx;
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 16rpx;
		padding: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.novel-info {
		display: flex;
		gap: 16rpx;
	}
	
	.novel-cover {
		width: 120rpx;
		height: 160rpx;
		border-radius: 12rpx;
		background-color: var(--color-bg-light, #F5F5F5);
		flex-shrink: 0;
	}
	
	.novel-details {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}
	
	.novel-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		line-height: 1.4;
		margin-bottom: 8rpx;
	}
	
	.novel-author {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		margin-bottom: 12rpx;
	}
	
	.novel-stats {
		display: flex;
		gap: 16rpx;
	}
	
	.rating {
		font-size: 24rpx;
		color: #FF6B35;
		font-weight: 600;
	}
	
	.reader-count {
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999999);
	}
	
	/* 评论列表 */
	.reviews-scroll {
		flex: 1;
		height: calc(100vh - 88rpx - 208rpx - 120rpx);
		margin-bottom: 120rpx;
	}
	
	.reviews-container {
		padding: 0 32rpx;
	}
	
	.review-item {
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 16rpx;
		padding: 24rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.review-header {
		display: flex;
		gap: 16rpx;
		margin-bottom: 16rpx;
	}
	
	.reviewer-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 30rpx;
		background-color: var(--color-bg-light, #F5F5F5);
		flex-shrink: 0;
	}
	
	.reviewer-info {
		flex: 1;
	}
	
	.reviewer-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		margin-bottom: 8rpx;
		display: block;
	}
	
	.review-meta {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.rating-stars {
		display: flex;
		gap: 4rpx;
	}
	
	.star {
		font-size: 24rpx;
		color: #FFD700;
	}
	
	.star.empty {
		color: #E0E0E0;
	}
	
	.review-time {
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999999);
	}
	
	.review-content {
		margin-bottom: 16rpx;
	}
	
	.review-text {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		line-height: 1.6;
	}
	
	.clamp-2 {
		display: -webkit-box;
		-webkit-line-clamp: 2;
		line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}
	
	/* 回复列表 */
	.replies-list {
		margin-top: 16rpx;
		margin-left: 80rpx;
		border-left: 2rpx solid var(--color-border, #F0F0F0);
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
		background-color: var(--color-bg-light, #F5F5F5);
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
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.reply-time {
		font-size: 20rpx;
		color: var(--color-text-placeholder, #999999);
	}
	
	.reply-text {
		font-size: 26rpx;
		color: var(--color-text, #333333);
		line-height: 1.4;
		margin-bottom: 12rpx;
	}
	
	.reply-to {
		color: #FF69B4;
		font-weight: 500;
	}
	
	/* 评论互动 */
	.engagement-metrics {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		padding: 16rpx 0;
	}

	.metric-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.metric-icon {
		font-size: 32rpx;
		color: var(--color-text-secondary, #666666);
		transition: color 0.2s ease;
	}
	
	.metric-icon.active {
		color: #FFD700; /* 高亮颜色 */
		transform: scale(1.1);
	}
	
	.metric-count {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.metric-count.active {
		color: #FFD700; /* 高亮颜色 */
		font-weight: 600;
	}
	
	/* 底部评论输入框 */
	.comment-input-section {
		height: auto;
		min-height: 120rpx;
		padding: 20rpx 32rpx;
		border-top: 1rpx solid var(--color-border, #F0F0F0);
		background-color: var(--color-bg, #FFFFFF);
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	.comment-input-container {
		display: flex;
		align-items: center;
		gap: 16rpx;
		height: 80rpx;
		border: 2rpx solid var(--color-border, #E0E0E0);
		border-radius: 40rpx;
		padding: 0 8rpx;
		background-color: var(--color-bg, #FFFFFF);
	}
	
	.comment-input {
		flex: 1;
		height: 80rpx;
		padding: 0 24rpx;
		background-color: transparent;
		border: none;
		border-radius: 40rpx;
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
	
	.send-button {
		padding: 16rpx 24rpx;
		background-color: #CCCCCC;
		border-radius: 20rpx;
		transition: background-color 0.2s ease;
	}
	
	.send-button.active {
		background-color: #FF69B4;
	}
	
	.send-text {
		font-size: 26rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 取消回复按钮 */
	.cancel-reply-btn {
		padding: 12rpx 16rpx;
		background-color: var(--color-bg-light, #F5F5F5);
		border-radius: 20rpx;
		margin-right: 8rpx;
	}
	
	.cancel-reply-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.input-wrapper {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.input-user {
		flex-shrink: 0;
	}
	
	.user-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 30rpx;
		background-color: var(--color-bg-light, #F5F5F5);
	}
	
	.input-field {
		flex: 1;
	}
	
	.comment-input {
		width: 100%;
		height: 60rpx;
		background-color: var(--color-bg-light, #F5F5F5);
		border-radius: 30rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
	
	.input-actions {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex-shrink: 0;
	}
	
	.send-button {
		background-color: #FF69B4;
		padding: 12rpx 24rpx;
		border-radius: 24rpx;
	}
	
	.send-text {
		font-size: 26rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 分享弹窗 */
	.share-popup {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 2000;
		display: flex;
		align-items: flex-end;
	}
	
	.share-content {
		width: 100%;
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 24rpx 24rpx 0 0;
		padding: 32rpx;
	}
	
	.share-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}
	
	.share-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.share-close {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.close-icon {
		font-size: 40rpx;
		color: var(--color-text-placeholder, #999999);
		font-weight: bold;
	}
	
	.share-options {
		display: flex;
		justify-content: space-around;
		padding: 20rpx 0;
		margin-bottom: 20rpx;
	}
	
	.share-option {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 12rpx;
	}
	
	.share-icon-bg {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}
	
	.share-icon-img {
		width: 48rpx;
		height: 48rpx;
	}
	
	.share-icon-bg.wechat {
		background-color: #07C160;
		background-image: url('/static/wechat-icon.png');
		background-size: 32rpx 32rpx;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	.share-icon-bg.moments {
		background-color: #07C160;
		background-image: url('/static/wechat-icon.png');
		background-size: 32rpx 32rpx;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	.share-icon-bg.douyin {
		background-color: #000000;
		background-image: url('/static/douyin-icon.png');
		background-size: 32rpx 32rpx;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	.share-icon-bg.douyin-post {
		background-color: #000000;
		background-image: url('/static/douyin-icon.png');
		background-size: 32rpx 32rpx;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	.share-icon-bg.qq {
		background-color: #12B7F5;
		background-image: url('/static/qq-icon.png');
		background-size: 32rpx 32rpx;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	.share-icon-img {
		width: 40rpx;
		height: 40rpx;
	}
	
	.share-option-text {
		font-size: 24rpx;
		color: var(--color-text, #333333);
	}
	
	.share-actions {
		display: flex;
		justify-content: space-around;
		padding: 20rpx 0;
		margin-bottom: 20rpx;
		border-top: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.action-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 12rpx;
	}
	
	.action-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: var(--color-bg-light, #F5F5F5);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}
	
	.action-text {
		font-size: 24rpx;
		color: var(--color-text, #333333);
	}
	
	/* 好友分享列表 */
	.share-friends {
		padding: 20rpx 0;
		margin-bottom: 20rpx;
		border-top: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.friends-scroll {
		white-space: nowrap;
		padding: 0 20rpx;
		width: 100%;
		/* 隐藏滑动条 */
		scrollbar-width: none; /* Firefox */
		-ms-overflow-style: none; /* IE and Edge */
		/* 完全隐藏滚动条和滚动指示器 */
		overflow-x: hidden;
	}
	
	/* 隐藏Webkit浏览器的滑动条 */
	.friends-scroll::-webkit-scrollbar {
		display: none;
	}
	
	/* 隐藏滚动指示器 */
	.friends-scroll::-webkit-scrollbar-track {
		display: none;
	}
	
	.friends-scroll::-webkit-scrollbar-thumb {
		display: none;
	}
	
	.friend-item {
		display: inline-flex;
		flex-direction: column;
		align-items: center;
		margin-right: 40rpx;
		gap: 12rpx;
	}
	
	.friend-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		overflow: hidden;
		background-color: var(--color-bg-light, #F5F5F5);
	}
	
	.friend-avatar-img {
		width: 100%;
		height: 100%;
	}
	
	.friend-name {
		font-size: 24rpx;
		color: var(--color-text, #333333);
		max-width: 80rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
	/* 刷新指示器 */
	.refresh-indicator {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 40rpx 0;
		background-color: var(--color-bg, #FFFFFF);
	}
	
	.refresh-circle {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #FFD700;
		border-top: 4rpx solid transparent;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16rpx;
		position: relative;
	}
	
	.refresh-circle.refreshing {
		animation: spin 1s linear infinite;
	}
	
	.refresh-arrow {
		width: 0;
		height: 0;
		border-left: 8rpx solid transparent;
		border-right: 8rpx solid transparent;
		border-bottom: 12rpx solid #FFD700;
		transform: rotate(45deg);
	}
	
	.refresh-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 3rpx solid #FFD700;
		border-top: 3rpx solid transparent;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}
	
	.refresh-text {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 40rpx;
		background-color: var(--color-bg, #F5F5F5);
		min-height: 400rpx;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	/* 错误状态样式 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 40rpx;
		background-color: var(--color-bg-light, #F5F5F5);
		min-height: 400rpx;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: #FF6B35;
		text-align: center;
		margin-bottom: 40rpx;
		line-height: 1.5;
	}
	
	.error-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.retry-button {
		background-color: #FF69B4;
		color: #FFFFFF;
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 26rpx;
		font-weight: 600;
	}
	
	.retry-button:active {
		background-color: #FF1493;
		transform: scale(0.95);
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
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.header-title {
		flex: 1;
		text-align: center;
	}
	
	.title-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}

  .share-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

  .share-icon {
    font-size: 32rpx;
  }


</style>