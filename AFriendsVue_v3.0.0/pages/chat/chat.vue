<template>
	<view class="chat-page" :class="{ 'is-concise': isConcise, 'care': isCare }">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部栏 -->
		<view class="header">
			<view class="back-btn" @click="goBack">
				<svg class="back-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="var(--color-text-secondary, #666666)" p-id="727"></path>
				</svg>
			</view>
			<text class="title" @click="goToFriendCard">{{ friendName }}</text>
			<view v-if="!isConcise" class="more-btn" @click="showMoreOptions">
				<view class="dot"></view>
				<view class="dot"></view>
				<view class="dot"></view>
			</view>
		</view>
		
		<!-- 聊天对话区域 -->
		<scroll-view class="chat-area" scroll-y="true" :scroll-top="scrollTop">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">{{ $t('chat.loading') }}</text>
			</view>

			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="ensureSessionAndLoadMessages">
					<text class="retry-text">{{ $t('chat.retry') }}</text>
				</button>
			</view>

			<!-- 聊天消息 -->
			<template v-else-if="messages.length > 0">
				<view 
					v-for="message in messages" 
					:key="message.id"
					:class="['message', message.type]"
				>
					<image v-if="message.type === 'left'" class="avatar" src="" mode="aspectFill" @click="goToFriendCard"></image>
					<view class="message-bubble">
						<text class="message-text">{{ message.text }}</text>
					</view>
					<image v-if="message.type === 'right'" class="avatar" src="" mode="aspectFill"></image>
				</view>
			</template>
			
			<!-- 无消息时显示提示 -->
			<view v-else class="no-messages">
				<view class="empty-icon">💬</view>
				<text class="no-messages-text">{{ $t('chat.noChatHistory') }}</text>
				<text v-if="!isConcise" class="no-messages-desc">{{ $t('chat.startChatting') }}</text>
			</view>
		</scroll-view>
		
		<!-- 消息输入栏 -->
		<view class="input-bar">
			<view class="mic-btn" @click="toggleVoiceInput">
				<!-- 语音按钮 -->
				<svg v-if="!isVoiceMode" class="mic-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M593.024 234.496l-46.165333 46.229333c0.106667 0.085333 0.149333 0.149333 0.277333 0.234667 127.552 127.658667 127.552 334.677333 0 462.336l46.122667 46.250667c153.045333-153.194667 153.045333-401.621333 0-554.816C593.173333 234.666667 593.130667 234.56 593.024 234.496z" p-id="3544"></path>
					<path d="M500.885333 327.189333c-0.042667-0.085333-0.128-0.149333-0.234667-0.213333l-46.165333 46.229333c0.042667 0.085333 0.128 0.149333 0.234667 0.256 76.48 76.565333 76.48 200.789333 0 277.397333l46.144 46.229333C603.008 595.008 603.008 429.333333 500.885333 327.189333z" p-id="3545"></path>
					<path d="M408.32 419.434667l-92.394667 92.458667 92.629333 92.714667c51.008-51.114667 51.008-133.888 0-184.938667C408.469333 419.584 408.362667 419.52 408.32 419.434667z" p-id="3546"></path>
					<path d="M512 0C229.696 0 0 229.696 0 512c0 282.282667 229.696 512 512 512 282.282667 0 512-229.717333 512-512C1024 229.696 794.282667 0 512 0zM512 981.290667C253.226667 981.290667 42.688 770.773333 42.688 512S253.226667 42.688 512 42.688 981.290667 253.226667 981.290667 512 770.773333 981.290667 512 981.290667z" p-id="3547"></path>
				</svg>
				<!-- 键盘按钮 -->
				<svg v-else class="keyboard-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M511 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
				</svg>
			</view>
			
			<!-- 输入框区域 -->
			<view class="input-field" v-if="!isVoiceMode">
				<input 
					class="message-input" 
					v-model="inputMessage" 
					:placeholder="$t('chat.inputPlaceholder')" 
					:focus="inputFocus"
					@focus="onInputFocus"
					@blur="onInputBlur"
					@confirm="sendMessage"
				/>
			</view>
			
			<!-- 语音按钮区域 -->
			<view class="voice-field" v-else @touchstart="startVoiceRecord" @touchend="endVoiceRecord">
				<text class="voice-text">{{ $t('chat.voiceModeText') }}</text>
			</view>
			
			<view class="action-btns">
				<view v-if="!isConcise" class="emoji-btn" @click="toggleEmoji">
					<svg class="emoji-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M512 160C317.5936 160 160 317.5936 160 512S317.5936 864 512 864 864 706.4064 864 512 706.4064 160 512 160z m0 64c159.0528 0 288 128.9472 288 288S671.0528 800 512 800 224 671.0528 224 512 352.9472 224 512 224z" fill="var(--color-text, #2c2c2c)" p-id="4603"></path>
						<path d="M683.3408 501.248H340.6592a25.6 25.6 0 0 0-25.6 26.112l0.1536 6.912a196.9408 196.9408 0 0 1 393.728-7.3984 25.6 25.6 0 0 0-25.6-25.6z m-27.8784 51.2l-0.3072 1.792A145.7664 145.7664 0 0 1 512 672.5888l-5.7088-0.1024a145.8176 145.8176 0 0 1-137.2928-117.4272l-0.4864-2.6112h286.9504z" fill="var(--color-text, #2c2c2c)" p-id="4604"></path>
						<path d="M411.1104 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="var(--color-text, #2c2c2c)" p-id="4605"></path>
						<path d="M612.8896 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="var(--color-text, #2c2c2c)" p-id="4606"></path>
					</svg>
				</view>
				<view v-if="!isConcise" class="plus-btn" @click="togglePlusMenu">
					<svg class="plus-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M511 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
					</svg>
				</view>
			</view>
		</view>
		

		
		<!-- 表情面板 -->
		<view v-if="!isConcise" class="emoji-panel" :class="{ 'active': showEmojiPanel }" @click="hideEmojiPanel">
			<view class="emoji-content" @click.stop>
				<view class="emoji-grid">
					<!-- 第一行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row1-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
					<!-- 第二行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row2-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
					<!-- 第三行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row3-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
				</view>
				<!-- 翻页指示器 -->
				<view class="emoji-pagination">
					<view class="page-dot active"></view>
					<view class="page-dot"></view>
					<view class="page-dot"></view>
				</view>
			</view>
		</view>

		<!-- 加号功能面板 -->
		<view v-if="!isConcise" class="plus-panel" :class="{ 'active': showPlusPanel }" @click="hidePlusPanel">
			<view class="plus-content" @click.stop>
				<view class="plus-grid">
					<view class="plus-item" @click="selectImage">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M928 160H96c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h832c17.7 0 32-14.3 32-32V192c0-17.7-14.3-32-32-32zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
							</svg>
						</view>
						<text class="plus-text">{{ $t('chat.imageVideo') }}</text>
					</view>
					<view class="plus-item" @click="openCamera">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M464 336a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm72 112h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V456c0-4.4-3.6-8-8-8z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">{{ $t('chat.camera') }}</text>
					</view>
					<view class="plus-item" @click="makeCall">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">{{ $t('chat.voiceVideoCall') }}</text>
					</view>
					<view class="plus-item" @click="shareLocation">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">{{ $t('chat.location') }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部操作菜单 -->
		<view v-if="!isConcise" class="action-sheet" :class="{ 'active': showActionSheet }" @click="hideActionSheet">
			<view class="action-content" @click.stop>
        <view class="action-item" @click="userInfo">
          <text class="action-text">{{ $t('chat.userInfo') }}</text>
        </view>
				<view class="action-item" @click="clearChat">
					<text class="action-text">{{ $t('chat.clearChatHistory') }}</text>
				</view>
				<view class="action-item" @click="reportChat">
					<text class="action-text">{{ $t('chat.report') }}</text>
				</view>
				<view class="action-item" @click="blockUser">
					<text class="action-text">{{ $t('chat.block') }}</text>
				</view>
				<view class="action-cancel" @click="hideActionSheet">
					<text class="cancel-text">{{ $t('chat.cancel') }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'ChatPage',
		data() {
			return {
				modeStore: null,
				friendName: '',
				friendId: null,
				sessionId: null,
				userId: null,
				showActionSheet: false,
				scrollTop: 0,
				inputMessage: '',
				inputFocus: false,
				isVoiceMode: false, // 语音模式标识
				showEmojiPanel: false, // 表情面板显示标识
				showPlusPanel: false, // 加号面板显示标识
				messages: [],
				loading: false,
				errorMessage: ''
			}
		},
		computed: {
			isConcise() {
				return !!(this.modeStore && this.modeStore.isConcise);
			},
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		watch: {
			isConcise(next) {
				if (next) {
					this.showEmojiPanel = false;
					this.showPlusPanel = false;
					this.showActionSheet = false;
				}
			}
		},
		onLoad(options) {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
			
			// 获取传递的参数
			if (options.friendName) {
				this.friendName = decodeURIComponent(options.friendName);
			}
			if (options.friendId) {
				this.friendId = parseInt(options.friendId);
			}
			if (options.sessionId) {
				this.sessionId = parseInt(options.sessionId);
			}
			
			// 获取当前用户ID
			this.userId = uni.getStorageSync('userId') || 1;

			if (this.friendId) {
				uni.setStorageSync('currentFriendId', this.friendId);
			}
			
			console.log('聊天页面参数:', {
				friendName: this.friendName,
				friendId: this.friendId,
				sessionId: this.sessionId,
				userId: this.userId
			});
			
			// 确保会话存在后再加载聊天记录
			this.ensureSessionAndLoadMessages();
		},
		methods: {
			async ensureSessionAndLoadMessages() {
				if (this.loading) {
					return;
				}

				this.errorMessage = '';

				if (this.sessionId) {
					await this.loadChatMessages();
					return;
				}

				if (!this.friendId) {
					this.errorMessage = this.$t('chat.sessionIdMissing');
					return;
				}

				this.loading = true;
				try {
					console.log('sessionId 缺失，开始创建或获取会话, userId:', this.userId, 'friendId:', this.friendId);
					const response = await uni.request({
						url: '${this.$baseUrl}/api/u-entities/user-chat-list/session/create-or-get',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							userId: this.userId,
							friendId: this.friendId
						}
					});

					console.log('create-or-get 会话响应:', response);
					if (response.statusCode === 200 && response.data && response.data.success && response.data.data) {
						const sessionId = response.data.data.sessionId;
						if (sessionId) {
							this.sessionId = parseInt(sessionId);
						}
					}

					if (!this.sessionId) {
						this.errorMessage = this.$t('chat.sessionIdMissing');
						return;
					}
				} catch (error) {
					console.error('创建或获取会话失败:', error);
					this.errorMessage = this.$t('chat.networkError');
					return;
				} finally {
					this.loading = false;
				}

				await this.loadChatMessages();
			},
			// 加载聊天记录
			async loadChatMessages() {
				if (!this.sessionId) {
					this.errorMessage = this.$t('chat.sessionIdMissing');
					return;
				}

				this.loading = true;
				this.errorMessage = '';

				try {
					console.log('开始加载聊天记录，sessionId:', this.sessionId);
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-chat-detail/session/${this.sessionId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('API响应状态码:', response.statusCode);
					console.log('API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						console.log('原始聊天记录数据:', response.data);
						this.messages = this.convertChatDataToMessages(response.data);
						console.log('转换后的聊天记录:', this.messages);
						
						// 滚动到底部显示最新消息
						this.$nextTick(() => {
							this.scrollToBottom();
						});
					} else {
						this.errorMessage = this.$t('chat.loadChatFailed', { code: response.statusCode });
						console.error('加载聊天记录失败:', response);
					}
				} catch (error) {
					this.errorMessage = this.$t('chat.networkError');
					console.error('加载聊天记录异常:', error);
				} finally {
					this.loading = false;
				}
			},

			// 转换数据库数据为消息格式
			convertChatDataToMessages(chatData) {
				console.log('开始转换聊天数据:', chatData);
				console.log('当前用户ID:', this.userId);
				
				if (!Array.isArray(chatData)) {
					console.log('聊天数据不是数组:', chatData);
					return [];
				}

				// 按创建时间排序（从早到晚）
				const sortedData = chatData.sort((a, b) => {
					const timeA = new Date(a.createdAt).getTime();
					const timeB = new Date(b.createdAt).getTime();
					return timeA - timeB;
				});

				const result = sortedData.map((chat, index) => {
					// 根据demand_party判断消息类型
					// 如果demand_party是当前用户ID，则消息在右侧（用户发送）
					// 否则消息在左侧（好友发送）
					const isUserMessage = chat.demandParty && chat.demandParty.toString() === this.userId.toString();
					
					console.log(`消息${index}:`, {
						id: chat.id,
						demandParty: chat.demandParty,
						message: chat.message,
						isUserMessage: isUserMessage,
						userId: this.userId
					});
					
					return {
						id: chat.id,
						type: isUserMessage ? 'right' : 'left',
						text: chat.message || '',
						time: this.formatTime(chat.createdAt),
						demandParty: chat.demandParty,
						createdAt: chat.createdAt
					};
				});
				
				console.log('转换结果:', result);
				return result;
			},

			// 格式化时间
			formatTime(timestamp) {
				if (!timestamp) return '';
				
				const date = new Date(timestamp);
				const hours = date.getHours();
				const minutes = date.getMinutes();
				const timeStr = hours < 12 ? this.$t('chat.am') : this.$t('chat.pm');
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},

			goBack() {
				// 尝试返回上一页，如果失败则跳转到聊天列表
				try {
					uni.navigateBack({
						fail: () => {
							// 如果返回失败，跳转到聊天列表页面
							uni.navigateTo({
								url: '/pages/chat/chat-list'
							});
						}
					});
				} catch (error) {
					// 如果出错，跳转到聊天列表页面
					uni.navigateTo({
						url: '/pages/chat/chat-list'
					});
				}
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
				// 当前页面，无需跳转
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
			goToAI() {
				uni.navigateTo({
					url: '/pages/ai/ai'
				});
			},
			goToFriendCard() {
				// 跳转到好友卡片页面
				console.log('goToFriendCard 被调用，当前 friendId:', this.friendId);
				console.log('当前 friendName:', this.friendName);
				
				if (!this.friendId) {
					uni.showToast({
						title: this.$t('chat.friendInfoIncomplete'),
						icon: 'error'
					});
					return;
				}
				
				uni.navigateTo({
					url: `/pages/chat/friend-card?friendName=${encodeURIComponent(this.friendName)}&friendId=${this.friendId}`
				});
			},
			showMoreOptions() {
        if (this.isConcise) return;
        this.showActionSheet = true;
			},
			hideActionSheet() {
				this.showActionSheet = false;
			},
      			userInfo() {
				uni.navigateTo({
					url: `/pages/chat/chat-info?friendName=${encodeURIComponent(this.friendName)}&friendId=${this.friendId}`
				});
			},
			clearChat() {
				// 先隐藏底部弹窗
				this.hideActionSheet();
				// 显示确认弹窗
				uni.showModal({
					title: this.$t('chat.confirmClear'),
					content: this.$t('chat.confirmClearContent'),
					success: (res) => {
						if (res.confirm) {
							// 清空聊天记录
							this.clearAllMessages();
							uni.showToast({
								title: this.$t('chat.chatHistoryCleared'),
								icon: 'success'
							});
						}
					}
				});
			},
			reportChat() {
				this.hideActionSheet();
				uni.navigateTo({
					url: '/pages/report/report'
				});
			},
			clearAllMessages() {
				// 清空消息数组
				this.messages = [];
				// 滚动到顶部
				this.scrollTop = 0;
			},
			async blockUser() {
				this.hideActionSheet();
				uni.showModal({
					title: this.$t('chat.confirmBlock'),
					content: this.$t('chat.confirmBlockContent', { name: this.friendName || this.$t('chat.theUser') }),
					success: async (res) => {
						if (res.confirm) {
							try {
								const response = await uni.request({
									url: '${this.$baseUrl}/api/bc-entities/block-record/add',
									method: 'POST',
									data: {
										userId: this.userId,
										blockedUserId: this.friendId
									}
								});
								if (response.statusCode === 200 && response.data.success) {
									uni.showToast({
										title: this.$t('chat.userBlocked'),
										icon: 'success'
									});
									setTimeout(() => {
										uni.navigateBack();
									}, 1500);
								} else {
									uni.showToast({
										title: response.data.message || this.$t('chat.blockFailed'),
										icon: 'none'
									});
								}
							} catch (error) {
								uni.showToast({
									title: this.$t('chat.blockFailed'),
									icon: 'none'
								});
							}
						}
					}
				});
			},
			toggleVoiceInput() {
				// 切换语音/键盘模式
				this.isVoiceMode = !this.isVoiceMode;
				if (this.isVoiceMode) {
					// 隐藏表情和加号面板
					this.showEmojiPanel = false;
					this.showPlusPanel = false;
				}
			},
			toggleEmoji() {
				if (this.isConcise) return;
				// 切换表情面板
				this.showEmojiPanel = !this.showEmojiPanel;
				this.showPlusPanel = false; // 隐藏加号面板
			},
			togglePlusMenu() {
				if (this.isConcise) return;
				// 切换加号菜单
				this.showPlusPanel = !this.showPlusPanel;
				this.showEmojiPanel = false; // 隐藏表情面板
			},
			hideEmojiPanel() {
				this.showEmojiPanel = false;
			},
			hidePlusPanel() {
				this.showPlusPanel = false;
			},
			startVoiceRecord() {
				// 开始录音
				uni.showToast({
					title: this.$t('chat.startRecording'),
					icon: 'none'
				});
			},
			endVoiceRecord() {
				// 结束录音
				uni.showToast({
					title: this.$t('chat.recordingComplete'),
					icon: 'none'
				});
			},
			selectImage() {
				// 选择图片
				this.hidePlusPanel();
				uni.showToast({
					title: this.$t('chat.imageSelectFeature'),
					icon: 'none'
				});
			},
			openCamera() {
				// 打开相机
				this.hidePlusPanel();
				uni.showToast({
					title: this.$t('chat.cameraFeature'),
					icon: 'none'
				});
			},
			makeCall() {
				// 拨打电话
				this.hidePlusPanel();
				uni.showToast({
					title: this.$t('chat.callFeature'),
					icon: 'none'
				});
			},
			shareLocation() {
				// 分享位置
				this.hidePlusPanel();
				uni.showToast({
					title: this.$t('chat.locationFeature'),
					icon: 'none'
				});
			},
			onInputFocus() {
				this.inputFocus = true;
			},
			onInputBlur() {
				this.inputFocus = false;
			},
			async sendMessage() {
				if (this.inputMessage.trim()) {
					const messageText = this.inputMessage.trim();
					
					// 先添加到本地显示
					const newMessage = {
						id: Date.now(), // 临时ID
						type: 'right',
						text: messageText,
						time: this.getCurrentTime(),
						demandParty: this.userId,
						createdAt: new Date().toISOString()
					};
					this.messages.push(newMessage);
					
					// 清空输入框
					this.inputMessage = '';
					
					// 滚动到底部
					this.$nextTick(() => {
						this.scrollToBottom();
					});
					
					// 保存到数据库
					await this.saveMessageToDatabase(messageText);
				}
			},

			// 保存消息到数据库
			async saveMessageToDatabase(messageText) {
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/user-chat-detail/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							sessionId: this.sessionId,
							fromUserId: this.userId,
							message: messageText,
							senderType: 'user'
						}
					});

					if (response.statusCode === 200 && response.data && response.data.success) {
						console.log('消息保存成功:', response.data);
						return;
					}

					const errorText = (response.data && response.data.message) ? response.data.message : this.$t('chat.sendMessageFailed');
					console.error('消息保存失败:', response);
					if (this.messages.length > 0) {
						const last = this.messages[this.messages.length - 1];
						if (last && last.type === 'right' && last.text === messageText) {
							this.messages.pop();
						}
					}
					uni.showToast({
						title: errorText,
						icon: 'none'
					});
				} catch (error) {
					console.error('保存消息异常:', error);
					if (this.messages.length > 0) {
						const last = this.messages[this.messages.length - 1];
						if (last && last.type === 'right' && last.text === messageText) {
							this.messages.pop();
						}
					}
					uni.showToast({
						title: this.$t('chat.sendMessageFailed'),
						icon: 'none'
					});
				}
			},
			getCurrentTime() {
				const now = new Date();
				const hours = now.getHours();
				const minutes = now.getMinutes();
				const timeStr = hours < 12 ? this.$t('chat.am') : this.$t('chat.pm');
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},
			scrollToBottom() {
				// 滚动到底部
				this.scrollTop = 999999;
			},
			goToFriendCard() {
				// 跳转到好友卡片页面
				console.log('goToFriendCard 被调用，当前 friendId:', this.friendId);
				console.log('当前 friendName:', this.friendName);
				
				if (!this.friendId) {
					uni.showToast({
						title: this.$t('chat.friendInfoIncomplete'),
						icon: 'error'
					});
					return;
				}
				
				uni.navigateTo({
					url: `/pages/chat/friend-card?friendName=${encodeURIComponent(this.friendName)}&friendId=${this.friendId}`
				});
			}
		}
	}
</script>

<style>
	.chat-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
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
	
	/* 头部栏 */
	.header {
		height: 88rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		position: relative;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.back-btn {
		position: absolute;
		left: 32rpx;
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		background-color: var(--color-bg-medium, #F0F0F0);
		border-radius: 50%;
		transition: background-color 0.2s ease;
	}
	
	.back-btn:active {
		background-color: var(--color-bg-medium, #E0E0E0);
	}
	
	.back-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.title {
		font-size: 36rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.more-btn {
		position: absolute;
		right: 32rpx;
		display: flex;
		flex-direction: column;
		gap: 4rpx;
	}
	
	.dot {
		width: 8rpx;
		height: 8rpx;
		background-color: var(--color-text, #333333);
		border-radius: 50%;
	}
	
	/* 聊天区域 */
	.chat-area {
		flex: 1;
		padding: 32rpx;
		padding-bottom: 140rpx; /* 为固定的输入栏留出空间 */
	}
	
	/* 无消息提示 */
	.no-messages {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 400rpx;
	}
	
	.no-messages-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
		margin-bottom: 8rpx;
	}

	.no-messages-desc {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
		text-align: center;
	}

	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 16rpx;
		opacity: 0.6;
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
		border: 4rpx solid var(--color-bg-light, #f3f3f3);
		border-top: 4rpx solid var(--color-teal, #20B2AA);
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
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
		padding: 80rpx 32rpx;
		gap: 24rpx;
	}

	.error-icon {
		font-size: 80rpx;
		margin-bottom: 16rpx;
	}

	.error-text {
		font-size: 28rpx;
		color: var(--color-red, #ff4757);
		text-align: center;
		margin-bottom: 16rpx;
	}

	.retry-button {
		background-color: var(--color-teal, #20B2AA);
		color: var(--color-white, #FFFFFF);
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.retry-button:active {
		background-color: var(--color-teal-dark, #1a9b94);
		transform: scale(0.95);
	}

	.retry-text {
		color: var(--color-white, #FFFFFF);
		font-weight: 600;
	}
	
	.message {
		display: flex;
		align-items: flex-start;
		margin-bottom: 32rpx;
	}
	
	.message.left {
		justify-content: flex-start;
	}
	
	.message.right {
		justify-content: flex-end;
	}
	
	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: var(--color-bg-light, #F5F5F5); /* 头像占位 */
		flex-shrink: 0;
	}
	
	.message-bubble {
		max-width: 400rpx;
		padding: 24rpx;
		border-radius: 24rpx;
		margin: 0 16rpx;
	}
	
	.message.left .message-bubble {
		background-color: var(--color-bg-medium, #F0F0F0);
	}
	
	.message.right .message-bubble {
		background-color: var(--color-teal, #20B2AA);
	}
	
	.message-text {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		line-height: 1.4;
	}
	
	.message.right .message-text {
		color: var(--color-white, #FFFFFF);
	}
	
	.timestamp {
		text-align: center;
		margin: 32rpx 0;
	}
	
	.time-text {
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999999);
		background-color: var(--color-bg-lightest, #F8F8F8);
		padding: 8rpx 24rpx;
		border-radius: 20rpx;
	}
	
	/* 输入栏 */
	.input-bar {
		height: 120rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		background-color: var(--color-bg, #FFFFFF);
		border-top: 1rpx solid var(--color-border, #F0F0F0);
		position: fixed;
		bottom: 0; /* 固定在底部 */
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	.mic-btn {
		width: 60rpx;
		height: 60rpx;
		background-color: var(--color-bg-medium, #F0F0F0);
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.mic-icon, .keyboard-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.input-field {
		flex: 1;
		height: 80rpx;
		background-color: var(--color-bg-lightest, #F8F8F8);
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		margin: 0 20rpx;
	}
	
	.voice-field {
		flex: 1;
		height: 80rpx;
		background-color: var(--color-bg-lightest, #F8F8F8);
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 20rpx;
		cursor: pointer;
	}
	
	.voice-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666);
	}
	
	.message-input {
		flex: 1;
		height: 100%;
		font-size: 28rpx;
		color: var(--color-text, #333);
		background: transparent;
		border: none;
		outline: none;
	}
	
	.action-btns {
		display: flex;
		gap: 24rpx;
		margin-left: 24rpx;
	}
	
	.emoji-btn, .plus-btn {
		width: 60rpx;
		height: 60rpx;
		background-color: var(--color-bg-medium, #F0F0F0);
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		margin-left: 20rpx;
	}
	
	.emoji-icon, .plus-icon {
		width: 32rpx;
		height: 32rpx;
	}
	

	
	/* 表情面板 */
	.emoji-panel {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: var(--color-mask-dark, rgba(0, 0, 0, 0.5));
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.emoji-panel.active {
		opacity: 1;
		visibility: visible;
	}
	
	.emoji-content {
		width: 100%;
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 20rpx 20rpx 0 0;
		padding: 32rpx;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.emoji-panel.active .emoji-content {
		transform: translateY(0);
	}
	
	.emoji-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 32rpx;
		margin-bottom: 32rpx;
	}
	
	.emoji-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		cursor: pointer;
	}
	
	.emoji-image {
		width: 80rpx;
		height: 80rpx;
		background-color: var(--color-bg-medium, #F0F0F0);
		border-radius: 16rpx;
	}
	
	.emoji-pagination {
		display: flex;
		justify-content: center;
		gap: 16rpx;
	}
	
	.page-dot {
		width: 16rpx;
		height: 16rpx;
		border-radius: 50%;
		background-color: var(--color-bg-medium, #E0E0E0);
		transition: background-color 0.3s ease;
	}
	
	.page-dot.active {
		background-color: var(--color-blue, #007AFF);
	}

	/* 加号功能面板 */
	.plus-panel {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: var(--color-mask-dark, rgba(0, 0, 0, 0.5));
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.plus-panel.active {
		opacity: 1;
		visibility: visible;
	}
	
	.plus-content {
		width: 100%;
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 20rpx 20rpx 0 0;
		padding: 32rpx;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.plus-panel.active .plus-content {
		transform: translateY(0);
	}
	
	.plus-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 32rpx;
	}
	
	.plus-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 16rpx;
		transition: background-color 0.2s ease;
	}
	
	.plus-item:active {
		background-color: var(--color-bg-lightest, #F8F8F8);
	}
	
	.plus-icon-wrapper {
		width: 80rpx;
		height: 80rpx;
		background-color: var(--color-bg-medium, #F0F0F0);
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.plus-feature-icon {
		width: 48rpx;
		height: 48rpx;
	}
	
	.plus-text {
		font-size: 24rpx;
		color: var(--color-text, #333333);
		text-align: center;
		line-height: 1.2;
	}

	/* 底部操作菜单 */
	.action-sheet {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: var(--color-mask-dark, rgba(0, 0, 0, 0.5));
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.action-sheet.active {
		opacity: 1;
		visibility: visible;
	}
	
	.action-content {
		width: 100%;
		background-color: var(--color-bg, #FFFFFF);
		border-radius: 20rpx 20rpx 0 0;
		overflow: hidden;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.action-sheet.active .action-content {
		transform: translateY(0);
	}
	
	.action-item {
		padding: 30rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		text-align: center;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.action-item:active {
		background-color: var(--color-bg-lightest, #F8F8F8);
	}
	
	.action-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.action-cancel {
		padding: 30rpx;
		text-align: center;
		cursor: pointer;
		background-color: var(--color-bg-lightest, #F8F8F8);
		margin-top: 20rpx;
	}
	
	.cancel-text {
		font-size: 32rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 500;
	}

	.chat-page.is-concise .header {
		height: 72rpx;
	}

	.chat-page.is-concise .title {
		font-size: 32rpx;
	}

	.chat-page.is-concise .back-btn {
		left: 20rpx;
		width: 52rpx;
		height: 52rpx;
	}

	.chat-page.is-concise .chat-area {
		padding: 20rpx;
		padding-bottom: 120rpx;
	}

	.chat-page.is-concise .message {
		margin-bottom: 20rpx;
	}

	.chat-page.is-concise .avatar {
		width: 64rpx;
		height: 64rpx;
		border-radius: 32rpx;
	}

	.chat-page.is-concise .message-bubble {
		padding: 18rpx;
		border-radius: 20rpx;
		margin: 0 12rpx;
	}

	.chat-page.is-concise .message-text {
		font-size: 26rpx;
	}

	.chat-page.is-concise .input-bar {
		height: 104rpx;
		padding: 0 20rpx;
	}

	.chat-page.is-concise .mic-btn {
		width: 52rpx;
		height: 52rpx;
		border-radius: 26rpx;
	}

	.chat-page.is-concise .input-field,
	.chat-page.is-concise .voice-field {
		height: 72rpx;
		border-radius: 36rpx;
		margin: 0 16rpx;
		padding: 0 24rpx;
	}

	.chat-page.is-concise .message-input {
		font-size: 26rpx;
	}

	.chat-page.care .header {
		height: 108rpx;
	}

	.chat-page.care .title {
		font-size: 42rpx;
	}

	.chat-page.care .back-btn {
		left: 24rpx;
		width: 76rpx;
		height: 76rpx;
	}

	.chat-page.care .back-icon {
		width: 40rpx;
		height: 40rpx;
	}

	.chat-page.care .chat-area {
		padding: 40rpx;
		padding-bottom: 180rpx;
		box-sizing: border-box;
	}

	.chat-page.care .message {
		margin-bottom: 40rpx;
	}

	.chat-page.care .avatar {
		width: 96rpx;
		height: 96rpx;
		border-radius: 48rpx;
	}

	.chat-page.care .message-bubble {
		max-width: 520rpx;
		padding: 32rpx;
		border-radius: 28rpx;
		margin: 0 20rpx;
		box-sizing: border-box;
	}

	.chat-page.care .message-text {
		font-size: 34rpx;
		line-height: 1.6;
	}

	.chat-page.care .input-bar {
		height: 150rpx;
		padding: 0 28rpx;
	}

	.chat-page.care .mic-btn {
		width: 76rpx;
		height: 76rpx;
		border-radius: 38rpx;
	}

	.chat-page.care .mic-icon,
	.chat-page.care .keyboard-icon {
		width: 40rpx;
		height: 40rpx;
	}

	.chat-page.care .input-field,
	.chat-page.care .voice-field {
		height: 96rpx;
		border-radius: 48rpx;
		margin: 0 20rpx;
		padding: 0 32rpx;
		box-sizing: border-box;
	}

	.chat-page.care .message-input {
		font-size: 32rpx;
	}

	.chat-page.care .voice-text {
		font-size: 32rpx;
	}

	.chat-page.care .emoji-btn,
	.chat-page.care .plus-btn {
		width: 72rpx;
		height: 72rpx;
		border-radius: 36rpx;
	}

	.chat-page.care .emoji-icon,
	.chat-page.care .plus-icon {
		width: 40rpx;
		height: 40rpx;
	}
</style>