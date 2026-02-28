<template>
	<view class="tasks-page">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="1053"></path>
				</svg>
			</view>
			<view class="header-title">
				<text class="title-text">任务</text>
			</view>
			<view class="placeholder"></view>
		</view> -->

		<!-- 任务统计 -->
		<view class="stats-section">
			<view class="stat-item">
				<text class="stat-number">{{ totalTasks }}</text>
				<text class="stat-label">总任务</text>
			</view>
			<view class="stat-item">
				<text class="stat-number">{{ activeTasks }}</text>
				<text class="stat-label">进行中</text>
			</view>
			<view class="stat-item">
				<text class="stat-number">{{ completedTasks }}</text>
				<text class="stat-label">已完成</text>
			</view>
		</view>

		<!-- 任务列表 -->
		<view class="tasks-list">
			<view class="list-header">
				<text class="header-title">任务列表</text>
				<view class="filter-tabs">
					<view 
						class="filter-tab" 
						:class="{ active: currentFilter === 'all' }"
						@click="setFilter('all')"
					>
						<text>全部</text>
					</view>
					<view 
						class="filter-tab" 
						:class="{ active: currentFilter === 'active' }"
						@click="setFilter('active')"
					>
						<text>进行中</text>
					</view>
					<view 
						class="filter-tab" 
						:class="{ active: currentFilter === 'completed' }"
						@click="setFilter('completed')"
					>
						<text>已完成</text>
					</view>
				</view>
			</view>

			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载任务中...</text>
			</view>
			
			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadTasks">
					<text class="retry-text">重试</text>
				</button>
			</view>
			
			<!-- 任务列表 -->
			<view v-else class="task-items">
				<view v-if="filteredTasks.length === 0" class="empty-container">
					<text class="empty-text">暂无任务数据</text>
				</view>
				<view 
					v-else
					class="task-item" 
					v-for="task in filteredTasks" 
					:key="task.id"
					:class="{ completed: task.status === 'completed' }"
				>
					<view class="task-header">
						<view class="task-title-row">
							<view class="task-status" :class="task.status">
								<view v-if="task.status === 'completed'" class="status-icon">✓</view>
								<view v-else-if="task.status === 'active'" class="status-icon">●</view>
								<view v-else class="status-icon">○</view>
							</view>
							<text class="task-title">{{ task.title }}</text>
							<view v-if="task.status === 'completed'" class="completed-badge">已完成</view>
						</view>
						<text class="task-time">{{ task.createTime }}</text>
					</view>
					
					<text class="task-description">{{ task.description }}</text>
					
					<view class="task-progress" v-if="task.status === 'active'">
						<view class="progress-bar">
							<view class="progress-fill" :style="{ width: task.progress + '%' }"></view>
						</view>
						<text class="progress-text">{{ task.progress }}%</text>
					</view>
					
					<view class="task-actions">
						<view v-if="task.status === 'completed'" 
							class="action-btn" 
							:class="commentedTasks.has(task.id) ? 'rated-btn' : 'rate-btn'"
							@click="commentedTasks.has(task.id) ? null : showRatingModal(task)">
							<text>{{ commentedTasks.has(task.id) ? '已评价' : '评分评价' }}</text>
						</view>
						<view v-else-if="task.status === 'terminated'" class="action-btn terminated-btn" @click="showTaskDetail(task)">
							<text>任务中止</text>
						</view>
						<view v-else class="action-btn detail-btn" @click="showTaskDetail(task)">
							<text>查看详情</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 评分评价弹窗 -->
		<view class="rating-modal" :class="{ active: showRating }" @click="hideRatingModal">
			<view class="rating-content" @click.stop>
				<view class="rating-header">
					<text class="rating-title">任务评价</text>
					<view class="close-btn" @click="hideRatingModal">×</view>
				</view>
				
				<view class="task-info">
					<text class="task-name">{{ currentTask?.title }}</text>
					<text class="task-desc">{{ currentTask?.description }}</text>
				</view>
				
				<view class="rating-section">
					<text class="rating-label">请为AI的表现评分：</text>
					<view class="star-rating">
						<view 
							class="star" 
							v-for="i in 5" 
							:key="i"
							:class="{ active: i <= rating }"
							@click="setRating(i)"
						>
							<text>★</text>
						</view>
					</view>
					<text class="rating-text">{{ ratingText }}</text>
				</view>
				
				<view class="comment-section">
					<text class="comment-label">评价内容：</text>
					<textarea 
						class="comment-input" 
						v-model="comment" 
						placeholder="请输入您的评价..."
						maxlength="200"
					></textarea>
					<text class="char-count">{{ comment.length }}/200</text>
				</view>
				
				<view class="rating-actions">
					<view class="cancel-btn" @click="hideRatingModal">
						<text>取消</text>
					</view>
					<view class="submit-btn" @click="submitRating">
						<text>提交评价</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'TasksPage',
		data() {
			return {
				currentFilter: 'all',
				showRating: false,
				currentTask: null,
				rating: 0,
				comment: '',
				tasks: [],
				loading: false,
				errorMessage: '',
				userId: null,
				commentedTasks: new Set() // 存储已评价的任务ID
			}
		},
		onLoad() {
			// 获取用户ID
			this.userId = uni.getStorageSync('userId') || 1;
			console.log('tasks页面加载，用户ID:', this.userId);
			
			// // 加载任务数据
			// this.loadTasks();
			// 第一步：先加载用户任务关系数据到内存
			this.loadUserTaskRelationshipToMemory()
				.then(() => {
					// 第二步：内存加载成功后，再加载任务列表数据
					this.loadTasks();
				})
				.catch((error) => {
					console.error('加载任务关系数据到内存失败:', error);
					this.errorMessage = '初始化任务数据失败，请重试';
					uni.showToast({
						title: '初始化失败',
						icon: 'error'
					});
					// 即使内存加载失败，仍尝试加载任务（可选，根据业务需求调整）
					this.loadTasks();
				});
		},
		computed: {
			totalTasks() {
				return this.tasks.length;
			},
			activeTasks() {
				return this.tasks.filter(task => task.status === 'active').length;
			},
			completedTasks() {
				return this.tasks.filter(task => task.status === 'completed').length;
			},
			filteredTasks() {
				if (this.currentFilter === 'all') {
					return this.tasks;
				} else if (this.currentFilter === 'active') {
					return this.tasks.filter(task => task.status === 'active');
				} else if (this.currentFilter === 'completed') {
					return this.tasks.filter(task => task.status === 'completed');
				}
				return this.tasks;
			},
			ratingText() {
				const texts = ['', '很差', '较差', '一般', '较好', '很好'];
				return texts[this.rating] || '';
			}
		},
		methods: {
			// 新增：加载用户任务关系数据到内存
			async loadUserTaskRelationshipToMemory() {
				console.log('开始加载用户任务关系数据到内存...');
				this.loading = true;
				
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-task-relationship/load`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('用户任务关系数据加载响应:', response);
					
					if (response.statusCode !== 200 || !response.data?.message.includes('成功')) {
						throw new Error('加载任务关系数据失败: ' + (response.data?.message || '未知错误'));
					}
					
					console.log('用户任务关系数据加载到内存成功');
				} catch (error) {
					console.error('加载任务关系数据异常:', error);
					throw error; // 抛出错误，让onLoad中的catch捕获
				} finally {
					// 此处不设置loading为false，留给loadTasks后续处理
				}
			},
			// 加载任务数据
			async loadTasks() {
				this.loading = true;
				this.errorMessage = '';
				
				try {
					console.log('开始加载用户任务数据，用户ID:', this.userId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-task-relationship/all`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('任务数据响应:', response);
					
					if (response.statusCode === 200 && response.data) {
						// 过滤出当前用户的任务
						const userTasks = response.data.filter(task => task.userId === this.userId);
						console.log('用户任务数据:', userTasks);
						
						// 转换为前端显示格式
						this.tasks = this.convertTasksToDisplayFormat(userTasks);
						console.log('转换后的任务数据:', this.tasks);
						
						// 检查用户是否已评价
						await this.checkUserComments();
					} else {
						throw new Error('加载任务数据失败');
					}
				} catch (error) {
					console.error('加载任务数据异常:', error);
					this.errorMessage = '加载任务数据失败，请重试';
					uni.showToast({
						title: '加载任务失败',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 转换任务数据为显示格式
			convertTasksToDisplayFormat(tasks) {
				return tasks.map(task => {
					// 根据状态映射
					let status = 'pending';
					if (task.status === 1) {
						status = 'active';
					} else if (task.status === 2) {
						status = 'completed';
					} else if (task.status === 3) {
						status = 'terminated';
					}
					
					// 格式化时间
					const createTime = this.formatTime(task.createdAt);
					const completedTime = task.endedAt ? this.formatTime(task.endedAt) : null;
					
					return {
						id: Number(task.userTaskId),
						title: `任务 ${task.userTaskId}`,
						description: `用户任务关系ID: ${task.userTaskId}，状态: ${this.getStatusText(status)}，进度: ${task.taskPercent || 0}%`,
						status: status,
						progress: task.taskPercent || 0,
						createTime: createTime,
						completedTime: completedTime
					};
				});
			},
			
			// 格式化时间
			formatTime(timestamp) {
				if (!timestamp) return '';
				const date = new Date(timestamp);
				return date.toLocaleString('zh-CN', {
					year: 'numeric',
					month: '2-digit',
					day: '2-digit',
					hour: '2-digit',
					minute: '2-digit'
				});
			},
			
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					'pending': '待开始',
					'active': '进行中',
					'completed': '已完成',
					'terminated': '已终止'
				};
				return statusMap[status] || '未知';
			},
			
			// 检查用户是否已评价
			async checkUserComments() {
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/efmprt-entities/task-comment/check?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200 && response.data.exists) {
						console.log('用户已评价'+response);
						// 如果用户已评价，将所有已完成的任务标记为已评价
						this.tasks.forEach(task => {
							console.log('用户已评价'+task);
							if (task.status === 'completed') {
								this.commentedTasks.add(task.id);
							}
						});
						console.log('用户已评价，标记所有已完成任务为已评价');
					}
				} catch (error) {
					console.error('检查用户评价状态失败:', error);
				}
			},
			
			// 检查任务是否已评价
			async checkTaskCommented(taskId) {
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/efmprt-entities/task-comment/check?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200) {
						return response.data.exists || false;
					}
					return false;
				} catch (error) {
					console.error('检查任务评价状态失败:', error);
					return false;
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			setFilter(filter) {
				this.currentFilter = filter;
			},
			showTaskDetail(task) {
				console.log('用户已评价'+task.id);
				// 关键修复2：使用Number()再次确认taskId是数字类型，避免URL传递字符串
				const taskId = Number(task.id);
				if (isNaN(taskId)) {
					uni.showToast({ title: '任务ID格式错误', icon: 'error' });
					return;
				}
				// 跳转到任务详情页面
				uni.navigateTo({
					url: `/pages/ai/task-detail?taskId=${taskId}`
				});
			},
			
			updateTaskStatus(taskId, newStatus) {
				// 更新任务状态
				const taskIndex = this.tasks.findIndex(task => task.id === taskId);
				if (taskIndex !== -1) {
					this.tasks[taskIndex].status = newStatus;
					if (newStatus === 'terminated') {
						this.tasks[taskIndex].progress = 0;
					}
				}
			},
			async showRatingModal(task) {
				// 检查是否已评价
				const isCommented = await this.checkTaskCommented(task.id);
				if (isCommented) {
					uni.showToast({
						title: '您已经评价过此任务',
						icon: 'none',
						duration: 2000
					});
					return;
				}
				
				this.currentTask = task;
				this.rating = 0;
				this.comment = '';
				this.showRating = true;
			},
			hideRatingModal() {
				this.showRating = false;
				this.currentTask = null;
			},
			setRating(score) {
				this.rating = score;
			},
			async submitRating() {
				if (this.rating === 0) {
					uni.showToast({
						title: '请选择评分',
						icon: 'none'
					});
					return;
				}
				
				// 关键修复3：验证并转换taskId为数字
				const taskId = Number(this.currentTask.id);
				if (isNaN(taskId)) {
					uni.showToast({ title: '任务ID格式错误', icon: 'error' });
					return;
				}

				try {
					console.log('提交评分:', {
						taskId: taskId,
						userId: this.userId,
						rating: this.rating,
						comment: this.comment
					});

					// 显示加载状态
					uni.showLoading({
						title: '提交中...'
					});

					const response = await uni.request({
						url: this.$baseUrl+'/api/efmprt-entities/task-comment/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							userId: this.userId,
							taskId: this.currentTask.id,
							stars: this.rating,
							commentText: this.comment || ''
						}
					});

					console.log('评分提交响应:', response);

					if (response.statusCode === 200 && response.data.success) {
						uni.showToast({
							title: '评分提交成功',
							icon: 'success'
						});
						
						// 标记当前任务为已评价
						this.commentedTasks.add(this.currentTask.id);
						
						// 重置表单
						this.rating = 0;
						this.comment = '';
						
						// 关闭弹窗
						this.hideRatingModal();
					} else {
						throw new Error(response.data.message || '提交失败');
					}
				} catch (error) {
					console.error('提交评分失败:', error);
					uni.showToast({
						title: '提交失败，请重试',
						icon: 'error'
					});
				} finally {
					uni.hideLoading();
				}
			}
		}
	}
</script>

<style>
	.tasks-page {
		min-height: 100vh;
		background: #f5f5f5;
	}

	/* 头部 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #F0F0F0;
		position: sticky;
		top: 0;
		z-index: 100;
	}

	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #F5F5F5;
		border-radius: 50%;
	}

	.back-button .icon {
		width: 32rpx;
		height: 32rpx;
	}

	.header-title {
		flex: 1;
		text-align: center;
	}

	.title-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 700;
	}

	.placeholder {
		width: 60rpx;
	}

	/* 统计区域 */
	.stats-section {
		display: flex;
		padding: 32rpx 24rpx;
		gap: 24rpx;
		background: #FFFFFF;
		margin-bottom: 24rpx;
	}

	.stat-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		padding: 24rpx 16rpx;
		background: linear-gradient(135deg, #667eea, #764ba2);
		border-radius: 16rpx;
		color: white;
	}

	.stat-number {
		font-size: 36rpx;
		font-weight: 700;
	}

	.stat-label {
		font-size: 24rpx;
		opacity: 0.9;
	}

	/* 任务列表 */
	.tasks-list {
		background: #FFFFFF;
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		overflow: hidden;
	}

	.list-header {
		padding: 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}

	.list-header .header-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
		margin-bottom: 24rpx;
		text-align: left;
	}

	.filter-tabs {
		display: flex;
		gap: 16rpx;
	}

	.filter-tab {
		padding: 12rpx 24rpx;
		border-radius: 24rpx;
		background: #F5F5F5;
		color: #666;
		font-size: 26rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.filter-tab.active {
		background: #667eea;
		color: white;
	}

	.task-items {
		padding: 0 32rpx;
	}

	.task-item {
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}

	.task-item:last-child {
		border-bottom: none;
	}

	.task-item.completed {
		opacity: 0.8;
	}

	.task-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 16rpx;
	}

	.task-title-row {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex: 1;
	}

	.task-status {
		width: 32rpx;
		height: 32rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.task-status.active {
		background: #4CAF50;
	}

	.task-status.completed {
		background: #2196F3;
	}

	.task-status.pending {
		background: #FF9800;
	}

	.status-icon {
		color: white;
		font-size: 20rpx;
		font-weight: bold;
	}

	.task-title {
		font-size: 30rpx;
		color: #333;
		font-weight: 600;
		flex: 1;
	}

	.completed-badge {
		padding: 4rpx 12rpx;
		background: #E3F2FD;
		color: #1976D2;
		border-radius: 12rpx;
		font-size: 22rpx;
	}

	.task-time {
		font-size: 24rpx;
		color: #999;
		flex-shrink: 0;
	}

	.task-description {
		font-size: 26rpx;
		color: #666;
		line-height: 1.5;
		margin-bottom: 20rpx;
	}

	.task-progress {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 20rpx;
	}

	.progress-bar {
		flex: 1;
		height: 8rpx;
		background: #E0E0E0;
		border-radius: 4rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: #4CAF50;
		border-radius: 4rpx;
		transition: width 0.3s ease;
	}

	.progress-text {
		font-size: 24rpx;
		color: #666;
		min-width: 60rpx;
	}

	.task-actions {
		display: flex;
		justify-content: flex-end;
	}

	.action-btn {
		padding: 12rpx 24rpx;
		border-radius: 20rpx;
		font-size: 24rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.rate-btn {
		background: #FF9800;
		color: white;
	}

	.rate-btn:active {
		background: #F57C00;
		transform: scale(0.95);
	}

	.rated-btn {
		background: #CCCCCC;
		color: #999999;
		cursor: not-allowed;
	}

	.rated-btn:active {
		background: #CCCCCC;
		transform: none;
	}

	.detail-btn {
		background: #E3F2FD;
		color: #1976D2;
	}

	.detail-btn:active {
		background: #BBDEFB;
		transform: scale(0.95);
	}

	.terminated-btn {
		background: #FFCDD2;
		color: #D32F2F;
	}

	.terminated-btn:active {
		background: #FFEBEE;
		transform: scale(0.95);
	}

	/* 评分弹窗 */
	.rating-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}

	.rating-modal.active {
		opacity: 1;
		visibility: visible;
	}

	.rating-content {
		width: 600rpx;
		max-height: 80vh;
		background: white;
		border-radius: 20rpx;
		padding: 40rpx;
		overflow-y: auto;
	}

	.rating-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}

	.rating-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
	}

	.close-btn {
		width: 48rpx;
		height: 48rpx;
		border-radius: 50%;
		background: #F5F5F5;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		color: #666;
		cursor: pointer;
	}

	.task-info {
		margin-bottom: 32rpx;
		padding: 24rpx;
		background: #F8F9FA;
		border-radius: 16rpx;
	}

	.task-name {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
		margin-bottom: 8rpx;
		display: block;
	}

	.task-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.4;
	}

	.rating-section {
		margin-bottom: 32rpx;
	}

	.rating-label {
		font-size: 26rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.star-rating {
		display: flex;
		gap: 16rpx;
		margin-bottom: 16rpx;
	}

	.star {
		font-size: 48rpx;
		color: #E0E0E0;
		cursor: pointer;
		transition: color 0.2s ease;
	}

	.star.active {
		color: #FFD700;
	}

	.rating-text {
		font-size: 24rpx;
		color: #666;
		text-align: center;
		display: block;
	}

	.comment-section {
		margin-bottom: 32rpx;
	}

	.comment-label {
		font-size: 26rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.comment-input {
		width: 100%;
		height: 120rpx;
		padding: 20rpx;
		border: 1rpx solid #E0E0E0;
		border-radius: 12rpx;
		font-size: 26rpx;
		resize: none;
		box-sizing: border-box;
	}

	.char-count {
		font-size: 22rpx;
		color: #999;
		text-align: right;
		margin-top: 8rpx;
		display: block;
	}

	.rating-actions {
		display: flex;
		gap: 24rpx;
	}

	.cancel-btn, .submit-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.cancel-btn {
		background: #F5F5F5;
		color: #666;
	}

	.cancel-btn:active {
		background: #E0E0E0;
	}

	.submit-btn {
		background: #4CAF50;
		color: white;
	}

	.submit-btn:active {
		background: #388E3C;
	}

	/* 加载状态样式 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 0;
		gap: 20rpx;
	}
	
	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #667eea;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 28rpx;
		color: #666;
	}
	
	/* 错误状态样式 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 0;
		gap: 20rpx;
	}
	
	.error-icon {
		font-size: 80rpx;
		color: #ff6b6b;
	}
	
	.error-text {
		font-size: 28rpx;
		color: #666;
		text-align: center;
	}
	
	.retry-button {
		padding: 16rpx 32rpx;
		background: #667eea;
		color: white;
		border: none;
		border-radius: 24rpx;
		font-size: 26rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.retry-button:active {
		transform: scale(0.95);
		background: #5a6fd8;
	}
	
	.retry-text {
		color: white;
		font-size: 26rpx;
	}
	
	/* 空状态样式 */
	.empty-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 0;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #999;
	}
</style>
