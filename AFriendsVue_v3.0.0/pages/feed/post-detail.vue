<template>
  <view class="post-detail-page" :class="{ concise: isConcise }">
    <!-- 头部导航 -->
    <view class="header">
      <view class="back-button" @click="goBack">
        <svg t="1756246262970" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="904" width="32" height="32">
          <path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="var(--color-text, #666666)" p-id="905"></path>
        </svg>
      </view>
      <view class="header-title">{{ $t('postDetail.title') }}</view>
      <view class="more-button" @click="showMoreOptions">
        <svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
          <path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
          <path d="M464 688a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm24-112h48c4.4 0 8-3.6 8-8V296c0-4.4-3.6-8-8-8h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8z" fill="var(--color-text, #333333)"></path>
        </svg>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-scroll">
      <!-- 加载状态 -->
      <view class="loading-container" v-if="loading">
        <text class="loading-text">{{ $t('common.loading') }}</text>
      </view>
      
      <!-- 错误状态 -->
      <view class="error-container" v-if="hasError && !loading">
        <view class="error-icon">⚠️</view>
        <text class="error-text">{{ errorMessage }}</text>
        <view class="error-actions">
          <button class="retry-button" @click="retryLoad">{{ $t('common.retry') }}</button>
        </view>
      </view>
      
      <!-- 帖子内容 -->
      <view class="post-content" v-if="!hasError && !loading">
        <!-- 作者信息 -->
        <view class="author-section">
          <view class="author-info" @click="goToUserProfile">
            <image class="author-avatar" :src="postDetail.authorAvatar" mode="aspectFill"></image>
            <view class="author-details">
              <text class="author-name">{{ postDetail.authorName }}</text>
              <text class="author-time" v-if="!isConcise">{{ postDetail.publishTime }}</text>
            </view>
          </view>
          <view class="follow-button" v-if="!isOwnPost" @click="toggleFollow" :class="{ 'followed': postDetail.isFollowed }">
            <text class="follow-text">{{ postDetail.isFollowed ? $t('postDetail.followed') : $t('postDetail.follow') }}</text>
          </view>
        </view>
        
        <!-- 文字内容 -->
        <view class="text-content" v-if="postDetail.textContent">
          <text class="content-text" :class="{ 'content-text-concise': isConcise }">{{ getDisplayText(postDetail.textContent) }}</text>
        </view>
        
        <!-- 图片内容 -->
        <view class="image-content" v-if="postDetail.images && postDetail.images.length > 0">
          <view class="image-grid" :class="getImageGridClass(getDisplayImages(postDetail.images).length)">
            <image 
              v-for="(image, index) in getDisplayImages(postDetail.images)" 
              :key="index"
              class="content-image" 
              :src="image" 
              mode="aspectFill"
              @click="previewImage(postDetail.images, index)"
            ></image>
            <view class="image-more" v-if="isConcise && postDetail.images.length > 1">
              <text class="image-more-text">+{{ postDetail.images.length - 1 }}</text>
            </view>
          </view>
        </view>
        
        <!-- 位置信息 -->
        <view class="location-info" v-if="postDetail.location && !isConcise">
          <view class="location-icon">
            <svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="24" height="24">
              <path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text-secondary, #999999)"></path>
              <path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176zm0 288c-61.9 0-112-50.1-112-112s50.1-112 112-112 112 50.1 112 112-50.1 112-112 112z" fill="var(--color-text-secondary, #999999)"></path>
            </svg>
          </view>
          <text class="location-text">{{ postDetail.location }}</text>
        </view>
        
        <!-- 互动指标 -->
        <view class="engagement-metrics">
          <view class="metric-item" @click="handleLike">
            <text class="metric-icon" :class="{ 'active': postDetail.isLiked }">👍</text>
            <text class="metric-count" :class="{ 'active': postDetail.isLiked }">{{ formatCount(postDetail.likes) }}</text>
          </view>
          <view class="metric-item" v-if="!isConcise" @click="handleDislike">
            <text class="metric-icon" :class="{ 'active': postDetail.isDisliked }">👎</text>
            <text class="metric-count" :class="{ 'active': postDetail.isDisliked }">{{ formatCount(postDetail.dislikes) }}</text>
          </view>
          <view class="metric-item" v-if="!isConcise" @click="handleFavorite">
            <text class="metric-icon" :class="{ 'active': postDetail.isFavorited }">⭐</text>
            <text class="metric-count" :class="{ 'active': postDetail.isFavorited }">{{ formatCount(postDetail.favorites) }}</text>
          </view>
          <view class="metric-item" v-if="!isConcise" @click="handleShare">
            <text class="metric-icon">📤</text>
            <text class="metric-count">{{ $t('postDetail.share') }}</text>
          </view>
        </view>
      </view>
      
      <!-- 评论区域 -->
      <view class="comments-section" v-if="!hasError && !loading">
        <view class="comments-header">
          <text class="comments-title">{{ $t('postDetail.commentsTitle') }} ({{ getTotalCommentsCount() }})</text>
        </view>
        <view class="view-more-comments" v-if="isConcise && !showAllCommentsInConcise && postDetail.comments && postDetail.comments.length > 3" @click="showAllCommentsInConcise = true">
          <text class="view-more-text">{{ $t('customerService.viewMore') }}</text>
        </view>
        
        <!-- 评论列表 -->
        <view class="comments-list">
          <view 
            class="comment-item" 
            v-for="(comment, index) in displayComments"
            :key="index"
          >
            <view class="comment-avatar">
              <image class="comment-user-avatar" :src="comment.userAvatar" mode="aspectFill"></image>
            </view>
            <view class="comment-content">
              <view class="comment-user-info">
                <text class="comment-username">{{ comment.username }}</text>
                <text class="comment-time" v-if="!isConcise">{{ comment.time }}</text>
              </view>
              <text class="comment-text">{{ comment.content }}</text>
              <view class="comment-actions">
                <view class="comment-action" @click="likeComment(comment, index)">
                  <text class="action-icon" :class="{ 'active': comment.isLiked }">👍</text>
                  <text class="action-count">{{ formatCount(comment.likes) }}</text>
                </view>
                <view class="comment-action" @click="replyComment(comment)">
                  <text class="action-icon">💬</text>
                  <text class="action-count">{{ $t('postDetail.reply') }}</text>
                </view>
              </view>
              
              <!-- 回复列表 -->
              <view class="replies-list" v-if="!isConcise && comment.replies && comment.replies.length > 0">
                <view 
                  class="reply-item" 
                  v-for="(reply, replyIndex) in comment.replies" 
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
                      <text class="reply-to">{{ $t('postDetail.replyTo') }} @{{ reply.replyTo }}：</text>
                      {{ reply.content }}
                    </text>
                    <view class="reply-actions">
                      <view class="reply-action" @click="likeReply(comment, replyIndex)">
                        <text class="action-icon" :class="{ 'active': reply.isLiked }">👍</text>
                        <text class="action-count">{{ formatCount(reply.likes) }}</text>
                      </view>
                      <view class="reply-action" @click="handleReplyToReply(comment, reply)">
                        <text class="action-icon">💬</text>
                        <text class="action-count">{{ $t('postDetail.reply') }}</text>
                      </view>
                    </view>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- @用户选择面板 -->
    <view class="mention-panel" v-if="showMentionPanel">
      <view class="mention-list">
        <view 
          class="mention-item" 
          v-for="user in mentionUsers" 
          :key="user.id"
          @click="selectMentionUser(user)"
        >
          <image class="mention-avatar" :src="user.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
          <text class="mention-username">{{ user.username }}</text>
        </view>
      </view>
    </view>
    
    <!-- 底部评论输入框 -->
    <view class="comment-input-section" v-if="!hasError && !loading">
      <view class="comment-input-container">
        <input 
          class="comment-input" 
          v-model="commentText" 
          :placeholder="getInputPlaceholder()" 
          @focus="onInputFocus"
          @blur="onInputBlur"
          @input="onTextInput"
        />
        <!-- 取消回复按钮 -->
        <view class="cancel-reply-btn" v-if="replyToComment" @click="cancelReply">
          <text class="cancel-reply-text">{{ $t('common.cancel') }}</text>
        </view>
        <view class="send-button" @click="submitComment" :class="{ 'active': commentText.trim() }">
          <text class="send-text">{{ $t('postDetail.send') }}</text>
        </view>
      </view>
    </view>
    
    <!-- 更多选项弹窗 -->
    <view class="more-options-modal" v-if="showMoreModal" @click="hideMoreOptions">
      <view class="options-content" @click.stop>
        <view class="option-item" @click="reportPost">
          <text class="option-text">{{ $t('postDetail.report') }}</text>
        </view>
        <view class="option-item" @click="copyLink">
          <text class="option-text">{{ $t('postDetail.copyLink') }}</text>
        </view>
        <view class="option-item" @click="hideMoreOptions">
          <text class="option-text">{{ $t('common.cancel') }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';
import { useModeStore } from '@/store/mode.js';

	export default {
		name: 'PostDetail',
		data() {
			return {
				modeStore: useModeStore(),
				userId: 1000100, // 默认用户ID，实际应该从登录状态获取
				postDetail: {
					id: null,
					authorAvatar: '/static/avatar1.png',
					authorName: '默认用户',
					publishTime: '',
					textContent: '',
					images: [],
					location: '',
					likes: 0,
					dislikes: 0,
					favorites: 0,
					isLiked: false,
					isDisliked: false,
					isFavorited: false,
					isFollowed: false,
					comments: []
				},
				loading: true,
				hasError: false,
				errorMessage: '',
				commentText: '',
				replyToComment: null, // 回复的评论对象
				replyToReply: null,   // 回复的回复对象
				showMoreModal: false,
				isOwnPost: false,
				showMentionPanel: false,
				mentionSearchText: '',
				mentionUsers: [],
				allFriends: [], // 所有好友，用于@功能
				searchTimer: null, // 防抖定时器
				showAllCommentsInConcise: false
			}
		},
		computed: {
			isConcise() {
				return this.modeStore && this.modeStore.isConcise;
			},
			displayComments() {
				const comments = (this.postDetail && Array.isArray(this.postDetail.comments)) ? this.postDetail.comments : [];
				if (!this.isConcise) return comments;
				if (this.showAllCommentsInConcise) return comments;
				return comments.slice(0, 3);
			}
		},
		mounted() {
			const themeStore = useThemeStore();
			themeStore.init();
			this.modeStore.init();
		},
		onLoad(options) {
			console.log('页面加载参数:', options);
			this.modeStore.init();
			if (options && options.postId) {
				this.loadPostDetail(options.postId, options.postType);
			}
			const storedUserId = uni.getStorageSync('userId');
			if (storedUserId) {
				this.userId = storedUserId;
			}
			this.loadFriendsForMention();
		},
		onUnload() {
			if (this.searchTimer) {
				clearTimeout(this.searchTimer);
			}
		},
		methods: {
			getDisplayText(text) {
				if (!text) return '';
				if (!this.isConcise) return text;
				const trimmed = String(text).trim();
				return trimmed.length > 120 ? (trimmed.slice(0, 120) + '...') : trimmed;
			},
			getDisplayImages(images) {
				if (!Array.isArray(images)) return [];
				return this.isConcise ? images.slice(0, 1) : images;
			},
			async loadPostDetail(postId, postType) {
				console.log('开始加载帖子详情，postId:', postId);
				try {
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					// 调用后端API获取帖子详情数据
					const response = await uni.request({
						url: `${this.$baseUrl}/api/post-detail/data?postId=${postId}&postType=${postType}&userId=1000100`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('API响应:', response);
					
					// 检查响应状态
					if (response.statusCode === 200 && response.data.success) {
						const postData = response.data.data;
						console.log('获取到的帖子数据:', postData);
						
						// 更新页面数据
						this.showAllCommentsInConcise = false;
						this.postDetail = {
							id: postData.id,
							authorName: postData.reviewer || '未知用户',
							authorAvatar: postData.avatar || '/static/avatar1.png',
							publishTime: postData.time || '时间未知',
							textContent: postData.content || '',
							images: postData.images || [],
							location: postData.location || '',
							likes: postData.likes || 0,
							dislikes: postData.dislikes || 0,
							favorites: postData.favorites || 0,
							isLiked: postData.isLiked || false,
							isDisliked: postData.isDisliked || false,
							isFavorited: postData.isFavorited || false,
							isFollowed: postData.isFollowed || false,
							comments: postData.comments || []
						};
					} else {
						// 处理错误响应
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
						uni.showToast({
							title: this.errorMessage,
							icon: 'none'
						});
					}
				} catch (error) {
					// 处理网络错误
					console.error('加载帖子详情数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
					uni.showToast({
						title: '网络错误',
						icon: 'none'
					});
				} finally {
					// 隐藏加载状态
					this.loading = false;
				}
			},
			
			// 重新加载数据
			retryLoad() {
				if (this.postDetail.id) {
					this.loadPostDetail(this.postDetail.id);
				}
			},
			
			// 返回上一页
			goBack() {
				uni.navigateBack();
			},
			
			// 跳转到用户资料页
			goToUserProfile() {
				uni.navigateTo({
					url: `/pages/feed/user-profile?username=${encodeURIComponent(this.postDetail.authorName)}`
				});
			},
			
			// 关注/取消关注
			toggleFollow() {
				this.postDetail.isFollowed = !this.postDetail.isFollowed;
				uni.showToast({
					title: this.postDetail.isFollowed ? this.$t('postDetail.followSuccess') : this.$t('postDetail.unfollowSuccess'),
					icon: 'success'
				});
			},
			
			// 获取图片网格样式
			getImageGridClass(count) {
				if (count === 1) return 'single-image';
				if (count === 2) return 'two-images';
				if (count === 3) return 'three-images';
				return 'grid-images';
			},
			
			// 图片预览
			previewImage(images, current) {
				uni.previewImage({
					urls: images,
					current: current
				});
			},
			
			// 格式化数字
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 点赞
			handleLike() {
				// 保存当前状态用于回滚
				const wasLiked = this.postDetail.isLiked;
				const wasDisliked = this.postDetail.isDisliked;
				
				// 调用后端API处理点赞
				this.processLike(this.postDetail, wasLiked, wasDisliked);
			},
			
			// 点踩
			handleDislike() {
				// 保存当前状态用于回滚
				const wasDisliked = this.postDetail.isDisliked;
				const wasLiked = this.postDetail.isLiked;
				
				// 调用后端API处理点踩
				this.processDislike(this.postDetail, wasDisliked, wasLiked);
			},
			
			// 收藏
			handleFavorite() {
				// 保存当前状态用于回滚
				const wasFavorited = this.postDetail.isFavorited;
				
				// 调用后端API处理收藏
				this.processFavorite(this.postDetail, wasFavorited);
			},
			
			// 实际处理收藏操作
			async processFavorite(post, wasFavorited) {
				try {
					console.log('处理收藏操作:', post);
					
					// 防止重复点击
					if (post._isProcessingFavorite) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessingFavorite = true;
					
					// 确定内容类型：text=文字, image=图片
					let postType = 'text';
					if (post.images && post.images.length > 0) {
						postType = 'image';
					}
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (postType === 'image') {
						contentType = 2;
					}
					
					console.log('发送收藏请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/chat-feed/favorite',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('收藏API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const isFavorited = response.data.isActive;
						post.isFavorited = isFavorited;
						
						if (isFavorited && !wasFavorited) {
							// 新收藏
							post.favorites++;
						} else if (!isFavorited && wasFavorited) {
							// 取消收藏
							post.favorites = Math.max(0, post.favorites - 1);
						} else if (!isFavorited && !wasFavorited) {
							// 页面刷新后再次取消收藏的情况
							post.favorites = Math.max(0, post.favorites - 1);
						}
						
						// 确保状态同步
						post.isFavorited = isFavorited;
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('收藏状态更新成功:', {
							isFavorited: post.isFavorited,
							favorites: post.favorites
						});
					} else {
						console.error('收藏操作失败:', response.data);
						// 恢复之前的状态
						post.isFavorited = wasFavorited;
						if (wasFavorited) {
							post.favorites++;
						} else {
							post.favorites = Math.max(0, post.favorites - 1);
						}
						
						uni.showToast({
							title: response.data.message || '收藏失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('收藏操作异常:', error);
					// 恢复之前的状态
					post.isFavorited = wasFavorited;
					if (wasFavorited) {
						post.favorites++;
					} else {
						post.favorites = Math.max(0, post.favorites - 1);
					}
					
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessingFavorite = false;
				}
			},
			
			// 实际处理点赞操作
			async processLike(post, wasLiked, wasDisliked) {
				try {
					console.log('处理点赞操作:', post);
					
					// 防止重复点击
					if (post._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessing = true;
					
					// 确定内容类型：text=文字, image=图片
					let postType = 'text';
					if (post.images && post.images.length > 0) {
						postType = 'image';
					}
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (postType === 'image') {
						contentType = 2;
					}
					
					console.log('发送点赞请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/chat-feed/like',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点赞API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const isLiked = response.data.isActive;
						post.isLiked = isLiked;
						
						if (isLiked && !wasLiked) {
							// 新点赞
							post.likes++;
							// 如果之前点踩了，取消点踩
							if (wasDisliked) {
								post.isDisliked = false;
								post.dislikes = Math.max(0, post.dislikes - 1);
							}
						} else if (!isLiked && wasLiked) {
							// 取消点赞
							post.likes = Math.max(0, post.likes - 1);
						} else if (!isLiked && !wasLiked) {
							// 页面刷新后再次取消点赞的情况
							post.likes = Math.max(0, post.likes - 1);
						}
						
						// 确保状态同步
						post.isLiked = isLiked;
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('点赞状态更新成功:', {
							isLiked: post.isLiked,
							likes: post.likes,
							dislikes: post.dislikes
						});
					} else {
						console.error('点赞操作失败:', response.data);
						// 恢复之前的状态
						post.isLiked = wasLiked;
						if (wasLiked) {
							post.likes++;
						} else {
							post.likes = Math.max(0, post.likes - 1);
						}
						
						if (wasDisliked && !post.isDisliked) {
							post.isDisliked = true;
							post.dislikes++;
						} else if (!wasDisliked && post.isDisliked) {
							post.isDisliked = false;
							post.dislikes = Math.max(0, post.dislikes - 1);
						}
						
						uni.showToast({
							title: response.data.message || '点赞失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('点赞操作异常:', error);
					// 恢复之前的状态
					post.isLiked = wasLiked;
					if (wasLiked) {
						post.likes++;
					} else {
						post.likes = Math.max(0, post.likes - 1);
					}
					
					if (wasDisliked && !post.isDisliked) {
						post.isDisliked = true;
						post.dislikes++;
					} else if (!wasDisliked && post.isDisliked) {
						post.isDisliked = false;
						post.dislikes = Math.max(0, post.dislikes - 1);
					}
					
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessing = false;
				}
			},
			
			// 实际处理点踩操作
			async processDislike(post, wasDisliked, wasLiked) {
				try {
					console.log('处理点踩操作:', post);
					
					// 防止重复点击
					if (post._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessing = true;
					
					// 确定内容类型：text=文字, image=图片
					let postType = 'text';
					if (post.images && post.images.length > 0) {
						postType = 'image';
					}
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (postType === 'image') {
						contentType = 2;
					}
					
					console.log('发送点踩请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/chat-feed/dislike',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点踩API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const isDisliked = response.data.isActive;
						post.isDisliked = isDisliked;
						
						if (isDisliked && !wasDisliked) {
							// 新点踩
							post.dislikes++;
							// 如果之前点赞了，取消点赞
							if (wasLiked) {
								post.isLiked = false;
								post.likes = Math.max(0, post.likes - 1);
							}
						} else if (!isDisliked && wasDisliked) {
							// 取消点踩
							post.dislikes = Math.max(0, post.dislikes - 1);
						} else if (!isDisliked && !wasDisliked) {
							// 页面刷新后再次取消点踩的情况
							post.dislikes = Math.max(0, post.dislikes - 1);
						}
						
						// 确保状态同步
						post.isDisliked = isDisliked;
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('点踩状态更新成功:', {
							isDisliked: post.isDisliked,
							likes: post.likes,
							dislikes: post.dislikes
						});
					} else {
						console.error('点踩操作失败:', response.data);
						// 恢复之前的状态
						post.isDisliked = wasDisliked;
						if (wasDisliked) {
							post.dislikes++;
						} else {
							post.dislikes = Math.max(0, post.dislikes - 1);
						}
						
						if (wasLiked && !post.isLiked) {
							post.isLiked = true;
							post.likes++;
						} else if (!wasLiked && post.isLiked) {
							post.isLiked = false;
							post.likes = Math.max(0, post.likes - 1);
						}
						
						uni.showToast({
							title: response.data.message || '点踩失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('点踩操作异常:', error);
					// 恢复之前的状态
					post.isDisliked = wasDisliked;
					if (wasDisliked) {
						post.dislikes++;
					} else {
						post.dislikes = Math.max(0, post.dislikes - 1);
					}
					
					if (wasLiked && !post.isLiked) {
						post.isLiked = true;
						post.likes++;
					} else if (!wasLiked && post.isLiked) {
						post.isLiked = false;
						post.likes = Math.max(0, post.likes - 1);
					}
					
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessing = false;
				}
			},
			
			// 分享
			handleShare() {
				uni.showToast({
					title: this.$t('postDetail.sharing'),
					icon: 'none'
				});
			},
			
			// 显示更多选项
			showMoreOptions() {
				this.showMoreModal = true;
			},
			
			// 隐藏更多选项
			hideMoreOptions() {
				this.showMoreModal = false;
			},
			
			// 举报帖子
			reportPost() {
				this.hideMoreOptions();
				uni.navigateTo({
					url: '/pages/report/report'
				});
			},
			
			// 复制链接
			copyLink() {
				this.hideMoreOptions();
				uni.setClipboardData({
					data: `https://example.com/post/${this.postDetail.id}`,
					success: () => {
						uni.showToast({
							title: '链接已复制',
							icon: 'success'
						});
					}
				});
			},
			
			// 点赞评论
			likeComment(comment, index) {
				// 保存当前状态用于回滚
				const wasLiked = comment.isLiked;
				const wasLikes = comment.likes;
				
				// 调用后端API处理点赞
				this.processCommentLike(comment, wasLiked, wasLikes, index);
			},
			
			// 实际处理评论点赞操作
			async processCommentLike(comment, wasLiked, wasLikes, index) {
				try {
					console.log('处理评论点赞操作:', comment);
					
					// 防止重复点击
					if (comment._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					comment._isProcessing = true;
					
					console.log('发送评论点赞请求:', {
						userId: this.userId,
						commentId: comment.commentId
					});

					const response = await uni.request({
						url: '${this.$baseUrl}/api/post-detail/comment/like',
						method: 'POST',
						data: {
							userId: this.userId,
							commentId: comment.commentId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('评论点赞API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						const result = response.data.data;
						
						// 更新评论状态
						comment.isLiked = result.isLiked;
						comment.likes = result.likeCount;
						
						uni.showToast({
							title: result.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('评论点赞状态更新成功:', {
							isLiked: comment.isLiked,
							likes: comment.likes
						});
					} else {
						console.error('评论点赞操作失败:', response.data);
						// 恢复之前的状态
						comment.isLiked = wasLiked;
						comment.likes = wasLikes;
						
						uni.showToast({
							title: response.data.message || '点赞失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('评论点赞操作异常:', error);
					// 恢复之前的状态
					comment.isLiked = wasLiked;
					comment.likes = wasLikes;
					
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					comment._isProcessing = false;
				}
			},
			
			// 点赞回复
			likeReply(comment, replyIndex) {
				const reply = comment.replies[replyIndex];
				// 保存当前状态用于回滚
				const wasLiked = reply.isLiked;
				const wasLikes = reply.likes;
				
				// 调用后端API处理点赞
				this.processReplyLike(reply, wasLiked, wasLikes, replyIndex);
			},
			
			// 回复评论
			replyComment(comment) {
				console.log('回复评论:', comment);
				this.replyToComment = comment;
				this.replyToReply = null;
				this.commentText = '';
				
				// 聚焦到输入框
				this.$nextTick(() => {
					const input = uni.createSelectorQuery().select('.comment-input');
					if (input) {
						input.focus();
					}
				});
			},
			
			// 实际处理回复点赞操作
			async processReplyLike(reply, wasLiked, wasLikes, replyIndex) {
				try {
					console.log('处理回复点赞操作:', reply);
					
					// 防止重复点击
					if (reply._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					reply._isProcessing = true;
					
					console.log('发送回复点赞请求:', {
						userId: this.userId,
						commentId: reply.commentId
					});
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/post-detail/comment/like',
						method: 'POST',
						data: {
							userId: this.userId,
							commentId: reply.commentId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('回复点赞API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						const result = response.data.data;
						
						// 更新回复状态
						reply.isLiked = result.isLiked;
						reply.likes = result.likeCount;
						
						uni.showToast({
							title: result.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('回复点赞状态更新成功:', {
							isLiked: reply.isLiked,
							likes: reply.likes
						});
					} else {
						console.error('回复点赞操作失败:', response.data);
						// 恢复之前的状态
						reply.isLiked = wasLiked;
						reply.likes = wasLikes;
						
						uni.showToast({
							title: response.data.message || '点赞失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('回复点赞操作异常:', error);
					// 恢复之前的状态
					reply.isLiked = wasLiked;
					reply.likes = wasLikes;
					
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					reply._isProcessing = false;
				}
			},

			// 回复回复
			handleReplyToReply(comment, reply) {
				this.replyToComment = comment;
				this.replyToReply = reply;
				this.commentText = '';
				
				// 聚焦到输入框
				this.$nextTick(() => {
					const input = uni.createSelectorQuery().select('.comment-input');
					if (input) {
						input.focus();
					}
				});
			},
			
			// 输入框聚焦
			onInputFocus() {
				// 可以在这里添加键盘弹出时的处理逻辑
			},
			
			// 输入框失焦
			onInputBlur() {
				// 可以在这里添加键盘收起时的处理逻辑
			},
			
			// 加载好友列表用于@功能
			async loadFriendsForMention() {
				try {
					// 显示加载状态
					uni.showLoading({
						title: '加载中...'
					});
					
					// 调用后端API获取可提及的好友列表
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-friends-relationship/user/${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200) {
						// 处理返回的好友数据
						this.allFriends = response.data.map(user => ({
							id: user.userId,
							username: user.username,
							avatar: user.profilePicUrl || '/static/default-avatar.png'
						}));
						
						console.log('成功加载好友列表用于@功能:', this.allFriends);
					} else {
						throw new Error(response.data.message || '加载失败');
					}
				} catch (error) {
					console.error('加载好友列表失败:', error);
					
					// 使用示例数据作为降级方案
					this.allFriends = [
						{ id: 1000101, username: '好友1', avatar: '/static/avatar1.png' },
						{ id: 1000102, username: '好友2', avatar: '/static/avatar2.png' },
						{ id: 1000103, username: '好友3', avatar: '/static/avatar3.png' }
					];
				} finally {
					uni.hideLoading();
				}
			},
			
			// 处理输入框输入事件
			onTextInput(e) {
				this.commentText = e.detail.value;
				
				// 清除之前的定时器
				if (this.searchTimer) {
					clearTimeout(this.searchTimer);
				}
				
				// 检测@符号
				const atIndex = this.commentText.lastIndexOf('@');
				if (atIndex !== -1) {
					// 获取@后的文本
					this.mentionSearchText = this.commentText.substring(atIndex + 1);
					this.showMentionPanel = true;
					
					// 如果没有输入文字，显示所有好友
					if (!this.mentionSearchText) {
						this.mentionUsers = [...this.allFriends];
					} else {
						// 设置0.5秒防抖延迟后进行搜索
						this.searchTimer = setTimeout(() => {
							this.searchMentionUsers(this.mentionSearchText);
						}, 500);
					}
				} else {
					this.showMentionPanel = false;
				}
			},
			
			// 搜索可提及的用户（包括好友和非好友）
			async searchMentionUsers(keyword) {
				try {
					// 如果关键字为空，显示所有好友
					if (!keyword) {
						this.mentionUsers = [...this.allFriends];
						return;
					}
					
					// 调用后端API搜索所有用户
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-friends-relationship/user/${this.userId}/search-all?keyword=${encodeURIComponent(keyword)}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200) {
						// 处理返回的搜索结果
						this.mentionUsers = response.data.map(user => ({
							id: user.userId,
							username: user.username,
							avatar: user.profilePicUrl || '/static/default-avatar.png'
						}));
					} else {
						throw new Error(response.data.message || '搜索失败');
					}
				} catch (error) {
					console.error('搜索用户失败:', error);
					// 降级到本地过滤（仅过滤好友）
					this.mentionUsers = this.allFriends.filter(friend => 
						friend.username.toLowerCase().includes(keyword.toLowerCase())
					);
				}
			},
			
			// 选择@用户
			selectMentionUser(user) {
				// 替换@文本为完整的@用户名
				const atIndex = this.commentText.lastIndexOf('@');
				this.commentText = this.commentText.substring(0, atIndex) + '@' + user.username + ' ';
				this.showMentionPanel = false;
				this.mentionSearchText = '';
			},
			
			// 提交评论
			async submitComment() {
				if (!this.commentText.trim()) {
					return;
				}
				
				// 调用后端API提交评论或回复
				try {
					// 判断是评论还是回复
					// 评论：parentCommentId 置为空
					// 回复：parentCommentId 设置为所回复的评论的 commentId
					const parentCommentId = this.replyToComment ? this.replyToComment.commentId : null;
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/post-detail/comment',
						method: 'POST',
						data: {
							postId: this.postDetail.id,
							postType: this.postDetail.images && this.postDetail.images.length > 0 ? 'image' : 'text',
							userId: this.userId,
							commentText: this.commentText,
							parentCommentId: parentCommentId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200 && response.data.success) {
						// 成功提交后更新UI
						if (this.replyToComment) {
							// 这是回复
							const newReply = {
								commentId: response.data.commentId || Date.now(), // 如果后端返回ID则使用，否则用时间戳
								username: this.$t('postDetail.me'),
								userAvatar: '/static/avatar3.png',
								time: this.$t('postDetail.justNow'),
								content: this.commentText,
								likes: 0,
								isLiked: false,
								replyTo: this.replyToReply ? this.replyToReply.username : this.replyToComment.username
							};
							
							// 确保回复数组存在
							if (!this.replyToComment.replies) {
								this.replyToComment.replies = [];
							}
							
							// 添加到回复列表
							this.replyToComment.replies.push(newReply);
							
							uni.showToast({
								title: this.$t('postDetail.replySuccess'),
								icon: 'success'
							});
						} else {
							// 这是新评论
							const newComment = {
								commentId: response.data.commentId || Date.now(), // 如果后端返回ID则使用，否则用时间戳
								username: this.$t('postDetail.me'),
								userAvatar: '/static/avatar3.png',
								time: this.$t('postDetail.justNow'),
								content: this.commentText,
								likes: 0,
								isLiked: false,
								replies: []
							};
							
							// 添加到评论列表
							this.postDetail.comments.unshift(newComment);
							
							uni.showToast({
								title: this.$t('postDetail.commentSuccess'),
								icon: 'success'
							});
						}
						
						// 重置回复状态
						this.replyToComment = null;
						this.replyToReply = null;
						// 清空输入框
						this.commentText = '';
						// 隐藏@面板
						this.showMentionPanel = false;
					} else {
						uni.showToast({
							title: response.data.message || this.$t('postDetail.submitFailed'),
							icon: 'error'
						});
					}
				} catch (error) {
					console.error(this.$t('postDetail.submitCommentFailed'), error);
					uni.showToast({
						title: this.$t('common.networkError'),
						icon: 'error'
					});
				}
			},
			
			// 获取总评论数（包括回复）
			getTotalCommentsCount() {
				let total = this.postDetail.comments.length;
				this.postDetail.comments.forEach(comment => {
					if (comment.replies) {
						total += comment.replies.length;
					}
				});
				return total;
			},
			
			// 获取输入框placeholder
			getInputPlaceholder() {
				if (this.replyToComment) {
					if (this.replyToReply) {
						return this.$t('postDetail.replyToPlaceholder', { user: this.replyToReply.username });
					}
					return this.$t('postDetail.replyToPlaceholder', { user: this.replyToComment.username });
				}
				return this.$t('postDetail.inputPlaceholder');
			},
			
			// 取消回复
			cancelReply() {
				console.log('取消回复');
				this.commentText = '';
				this.replyToComment = null;
				this.replyToReply = null;
			}
		}
	}
</script>

<style>
  .post-detail-page {
    height: 100vh;
    background-color: var(--color-bg, #FFFFFF);
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
    position: relative;
  }

  .post-detail-page.concise .post-content {
    padding: 20rpx;
    margin-bottom: 12rpx;
  }

  .post-detail-page.concise .comments-section {
    padding: 20rpx;
  }
  
  /* 头部导航 */
  .header {
    height: 88rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 32rpx;
    border-bottom: 1rpx solid var(--color-border, #F0F0F0);
    background-color: var(--color-card, #FFFFFF);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
  }
  
  .back-button, .more-button {
    width: 48rpx;
    height: 48rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .header-title {
    font-size: 32rpx;
    color: var(--color-text, #333333);
    font-weight: 600;
  }
  
  /* 内容滚动区域 */
  .content-scroll {
    margin-top: 88rpx;
    margin-bottom: 160rpx;
    padding-bottom: 20rpx;
  }
  
  /* 加载状态样式 */
  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 80rpx 40rpx;
    background-color: var(--color-bg, #F5F5F5);
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
    background-color: var(--color-bg, #F5F5F5);
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
  
  /* 帖子内容 */
  .post-content {
    padding: 32rpx;
    border-bottom: 1rpx solid var(--color-border, #F0F0F0);
    background-color: var(--color-card, #FFFFFF);
    margin-bottom: 16rpx;
    border-radius: 16rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
  }
  
  /* 作者信息 */
  .author-section {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
  }
  
  .author-info {
    display: flex;
    align-items: center;
    gap: 16rpx;
  }
  
  .author-avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 40rpx;
    background-color: var(--color-bg-weak, #F5F5F5);
  }
  
  .author-details {
    display: flex;
    flex-direction: column;
    gap: 4rpx;
  }
  
  .author-name {
    font-size: 30rpx;
    color: var(--color-text, #333333);
    font-weight: 600;
  }
  
  .author-time {
    font-size: 24rpx;
    color: var(--color-text-secondary, #999999);
  }
  
  .follow-button {
    padding: 12rpx 24rpx;
    background-color: #FF69B4;
    border-radius: 20rpx;
    border: 1rpx solid #FF69B4;
  }
  
  .follow-button.followed {
    background-color: var(--color-card, #FFFFFF);
    border-color: #CCCCCC;
  }
  
  .follow-text {
    font-size: 26rpx;
    color: #FFFFFF;
    font-weight: 500;
  }
  
  .follow-button.followed .follow-text {
    color: var(--color-text, #666666);
  }
  
  /* 文字内容 */
  .text-content {
    margin-bottom: 24rpx;
  }
  
  .content-text {
    font-size: 30rpx;
    color: var(--color-text, #333333);
    line-height: 1.6;
  }

  .content-text-concise {
    overflow: hidden;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 4;
    line-clamp: 4;
  }
  
  /* 图片内容 */
  .image-content {
    margin-bottom: 24rpx;
  }
  
  .image-grid {
    display: flex;
    gap: 12rpx;
    flex-wrap: wrap;
    position: relative;
  }

  .image-more {
    position: absolute;
    right: 12rpx;
    bottom: 12rpx;
    padding: 6rpx 12rpx;
    border-radius: 24rpx;
    background-color: rgba(0, 0, 0, 0.45);
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .image-more-text {
    color: #ffffff;
    font-size: 28rpx;
    font-weight: 700;
  }

  .view-more-comments {
    padding: 12rpx 0;
  }

  .view-more-text {
    font-size: 26rpx;
    color: var(--color-text-secondary, #666666);
  }
  
  .single-image .image-grid {
    justify-content: flex-start;
  }
  
  .two-images .image-grid {
    justify-content: flex-start;
  }
  
  .three-images .image-grid {
    justify-content: flex-start;
  }
  
  .grid-images .image-grid {
    justify-content: flex-start;
  }
  
  .content-image {
    width: 200rpx;
    height: 150rpx;
    border-radius: 8rpx;
    background-color: var(--color-bg-weak, #F5F5F5);
    flex-shrink: 0;
    object-fit: cover;
  }
  
  /* 单张图片样式 */
  .single-image .content-image {
    width: 100%;
    height: 400rpx;
  }
  
  /* 两张图片样式 */
  .two-images .content-image {
    width: calc(50% - 6rpx);
    height: 200rpx;
  }
  
  /* 三张及以上图片样式 */
  .three-images .content-image,
  .grid-images .content-image {
    width: calc(33.33% - 8rpx);
    height: 180rpx;
  }
  
  /* 位置信息 */
  .location-info {
    display: flex;
    align-items: center;
    gap: 8rpx;
    margin-bottom: 24rpx;
  }
  
  .location-icon {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .location-text {
    font-size: 24rpx;
    color: var(--color-text-secondary, #999999);
  }
  
  /* 互动指标 */
  .engagement-metrics {
    display: flex;
    gap: 32rpx;
    padding-top: 24rpx;
    border-top: 1rpx solid var(--color-border, #F0F0F0);
  }
  
  .metric-item {
    display: flex;
    align-items: center;
    gap: 8rpx;
  }
  
  .metric-icon {
    font-size: 32rpx;
    color: var(--color-text-secondary, #666666);
    transition: all 0.2s ease;
    cursor: pointer;
  }
  
  .metric-icon:active {
    transform: scale(0.9);
  }
  
  .metric-count {
    font-size: 26rpx;
    color: var(--color-text-secondary, #666666);
  }
  
  .metric-icon.active, .metric-count.active {
    color: #FFD700;
    font-weight: 600;
  }
  
  /* 评论区域 */
  .comments-section {
    padding: 32rpx;
    background-color: var(--color-card, #FFFFFF);
    border-radius: 16rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
  }
  
  .comments-header {
    margin-bottom: 24rpx;
  }
  
  .comments-title {
    font-size: 32rpx;
    color: var(--color-text, #333333);
    font-weight: 600;
  }
  
  /* 评论列表 */
  .comments-list {
    display: flex;
    flex-direction: column;
    gap: 24rpx;
  }
  
  .comment-item {
    display: flex;
    gap: 16rpx;
  }
  
  .comment-avatar {
    flex-shrink: 0;
  }
  
  .comment-user-avatar {
    width: 64rpx;
    height: 64rpx;
    border-radius: 32rpx;
    background-color: var(--color-bg-weak, #F5F5F5);
  }
  
  .comment-content {
    flex: 1;
  }
  
  .comment-user-info {
    display: flex;
    align-items: center;
    gap: 16rpx;
    margin-bottom: 8rpx;
  }
  
  .comment-username {
    font-size: 26rpx;
    color: var(--color-text, #333333);
    font-weight: 500;
  }
  
  .comment-time {
    font-size: 22rpx;
    color: var(--color-text-secondary, #999999);
  }
  
  .comment-text {
    font-size: 28rpx;
    color: var(--color-text, #333333);
    line-height: 1.5;
    margin-bottom: 16rpx;
  }
  
  .comment-actions {
    display: flex;
    gap: 24rpx;
  }
  
  .comment-action {
    display: flex;
    align-items: center;
    gap: 8rpx;
  }
  
  .action-icon {
    font-size: 24rpx;
    color: var(--color-text-secondary, #999999);
  }
  
  .action-count {
    font-size: 22rpx;
    color: var(--color-text-secondary, #999999);
  }
  
  .action-icon.active {
    color: #FFD700;
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
    background-color: var(--color-bg-weak, #F5F5F5);
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
    color: var(--color-text-secondary, #999999);
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
  
  .reply-actions {
    display: flex;
    gap: 20rpx;
  }
  
  .reply-action {
    display: flex;
    align-items: center;
    gap: 6rpx;
  }
  
  /* 底部评论输入框 */
  .comment-input-section {
    height: auto;
    min-height: 120rpx;
    padding: 20rpx 32rpx;
    border-top: 1rpx solid var(--color-border, #F0F0F0);
    background-color: var(--color-card, #FFFFFF);
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1000;
  }
  
  /* 回复提示 - 简洁样式 */
  .reply-hint-simple {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8rpx 16rpx;
    background-color: #F0F8FF;
    border-left: 4rpx solid #FF69B4;
    margin-bottom: 8rpx;
  }
  
  .hint-text-simple {
    font-size: 22rpx;
    color: var(--color-text-secondary, #666666);
  }
  
  .cancel-reply-simple {
    width: 28rpx;
    height: 28rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #FF69B4;
    border-radius: 14rpx;
  }
  
  .cancel-text-simple {
    font-size: 18rpx;
    color: #FFFFFF;
  }
  
  .comment-input-container {
    display: flex;
    align-items: center;
    gap: 16rpx;
    height: 80rpx;
    border: 2rpx solid var(--color-border, #E0E0E0);
    border-radius: 40rpx;
    padding: 0 8rpx;
    background-color: var(--color-card, #FFFFFF);
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
    background-color: var(--color-bg-weak, #F5F5F5);
    border-radius: 20rpx;
    margin-right: 8rpx;
  }
  
  .cancel-reply-text {
    font-size: 24rpx;
    color: var(--color-text, #666666);
  }
  
  /* @用户选择面板 */
  .mention-panel {
    position: fixed;
    bottom: 120rpx;
    left: 32rpx;
    right: 32rpx;
    background-color: var(--color-card, #FFFFFF);
    border-radius: 16rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
    z-index: 1001;
    max-height: 400rpx;
    overflow-y: auto;
  }
  
  .mention-list {
    padding: 16rpx 0;
  }
  
  .mention-item {
    display: flex;
    align-items: center;
    padding: 20rpx 32rpx;
    border-bottom: 1rpx solid var(--color-border, #F0F0F0);
  }
  
  .mention-item:last-child {
    border-bottom: none;
  }
  
  .mention-avatar {
    width: 60rpx;
    height: 60rpx;
    border-radius: 30rpx;
    margin-right: 20rpx;
  }
  
  .mention-username {
    font-size: 28rpx;
    color: var(--color-text, #333333);
  }
  
  /* 更多选项弹窗 */
  .more-options-modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 9999;
    display: flex;
    align-items: flex-end;
  }
  
  .options-content {
    width: 100%;
    background-color: var(--color-card, #FFFFFF);
    border-radius: 24rpx 24rpx 0 0;
    padding: 32rpx;
  }
  
  .option-item {
    padding: 24rpx 0;
    text-align: center;
    border-bottom: 1rpx solid var(--color-border, #F0F0F0);
  }
  
  .option-item:last-child {
    border-bottom: none;
  }
  
  .option-text {
    font-size: 30rpx;
    color: var(--color-text, #333333);
  }
</style>
