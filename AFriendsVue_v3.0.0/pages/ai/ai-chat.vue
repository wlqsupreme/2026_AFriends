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
			<text class="title">{{ currentAiModel.modelName || 'AI 助手' }}</text>
			<view v-if="!isConcise" class="more-btn" @click="showMoreOptions">
				<view class="dot"></view>
				<view class="dot"></view>
				<view class="dot"></view>
			</view>
		</view>
		
		<!-- 聊天对话区域 -->
		<scroll-view class="chat-area" scroll-y="true" :scroll-top="scrollTop">
			<!-- AI模型加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载AI模型...</text>
			</view>
			
			<!-- 聊天记录加载状态 -->
			<view v-else-if="chatLoading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载聊天记录...</text>
			</view>
			
			<!-- 聊天消息列表 -->
			<template v-else-if="messages.length > 0">
				<view 
					v-for="message in messages" 
					:key="message.id"
					:class="['message', message.type]"
				>
					<image v-if="message.type === 'left' && currentAiModel.modelImageUrl" class="avatar" :src="currentAiModel.modelImageUrl" mode="aspectFill"></image>
					<view v-else-if="message.type === 'left'" class="avatar avatar-placeholder">
						<text class="avatar-text">{{ getAvatarText() }}</text>
					</view>
					<view class="message-bubble">
						<view v-if="!message.isImage" class="message-text">
							<template v-for="(item, idx) in parseMixedMessage(message.text)" :key="idx">
								<text v-if="item.type === 'text'">{{ item.value }}</text>
								<image 
									v-if="item.type === 'emoji'" 
									class="mixed-emoji" 
									:src="item.value" 
									mode="aspectFit"
								></image>
							</template>
						</view>
						<image v-if="message.isImage" class="message-image" :src="message.imageUrl" mode="widthFix"></image>
						<text v-if="message.time && !isConcise" class="message-time">{{ message.time }}</text>
					</view>
					<image v-if="message.type === 'right'" class="avatar" src="/static/logo.png" mode="aspectFill"></image>
				</view>
			</template>
			
			<!-- 无消息时显示提示 -->
			<view v-else class="no-messages">
				<text class="no-messages-text">暂无聊天记录</text>
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
					<path d="M511.5 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
				</svg>
			</view>
			
			<!-- 输入框区域 -->
			<view class="input-field" v-if="!isVoiceMode">
				<input 
					class="message-input" 
					v-model="inputMessage" 
					placeholder="输入消息..." 
					:focus="inputFocus"
					@focus="onInputFocus"
					@blur="onInputBlur"
					@confirm="sendMessage"
				/>
			</view>
			
			<!-- 语音按钮区域 -->
			<view class="voice-field" v-else @touchstart="startVoiceRecord" @touchend="endVoiceRecord">
				<text class="voice-text">按住说话</text>
			</view>
			
			<view class="action-btns">
				<view v-if="!isConcise" class="emoji-btn" @click="toggleEmoji">
					<svg class="emoji-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M512 160C317.5936 160 160 317.5936 160 512S317.5936 864 512 864 864 706.4064 864 512 706.4064 160 512 160z m0 64c159.0528 0 288 128.9472 288 288S671.0528 800 512 800 224 671.0528 224 512 352.9472 224 512 224z" fill="var(--color-text, #2c2c2c)" p-id="4603"></path>
						<path d="M683.3408 501.248H340.6592a25.6 25.6 0 0 0-25.6 26.112l0.1536 6.912a196.9408 196.9408 0 0 0 393.728-7.3984 25.6 25.6 0 0 0-25.6-25.6z m-27.8784 51.2l-0.3072 1.792A145.7664 145.7664 0 0 1 512 672.5888l-5.7088-0.1024a145.8176 145.8176 0 0 1-137.2928-117.4272l-0.4864-2.6112h286.9504z" fill="var(--color-text, #2c2c2c)" p-id="4604"></path>
						<path d="M411.1104 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="var(--color-text, #2c2c2c)" p-id="4605"></path>
						<path d="M612.8896 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="var(--color-text, #2c2c2c)" p-id="4606"></path>
					</svg>
				</view>
				<view v-if="!isConcise" class="plus-btn" @click="togglePlusMenu">
					<svg class="plus-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M511.5 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
					</svg>
				</view>
			</view>
		</view>
		

		
		<!-- 表情面板 -->
		<view v-if="!isConcise" class="emoji-panel" :class="{ 'active': showEmojiPanel }" @click="hideEmojiPanel">
			<view class="emoji-content" @click.stop @touchstart="touchStart" @touchend="touchEnd" @touchmove.stop.prevent>
				<view class="emoji-grid">
					<view class="emoji-item" v-for="(item, idx) in emojiList[currentPage]" :key="idx" @click="selectEmoji(item)">
						<image 
							v-if="item.url && typeof item.url === 'string'"
							class="emoji-image" 
							:src="item.url" 
							mode="aspectFill"
						></image>
					</view>
				</view>
				<!-- 翻页指示器 -->
				<view class="emoji-pagination">
					<view 
						v-for="(page, index) in emojiList" 
						:key="index" 
						class="page-dot" 
						:class="{ 'active': index === currentPage }"
					></view>
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
								<path d="M928 160H96c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h832c17.7 0 32-14.3 32-32V192c0-17.7-14.3-32-32-32zM338 304c35.3 0 64 28.7 64 64s-28.7 64-64 64-64-28.7-64-64 28.7-64 64-64zm513.9 436.1H172.1c-12.9 0-24.8-5.9-32.7-16.1L73.4 659.8c-7.9-10.2-7.9-23.4 0-33.6l66-84.2c7.9-10.2 19.8-16.1 32.7-16.1h679.8c12.9 0 24.8 5.9 32.7 16.1l66 84.2c7.9 10.2 7.9 23.4 0 33.6l-66 84.2c-7.9 10.2-19.8 16.1-32.7 16.1z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">图片与视频</text>
					</view>
					<view class="plus-item" @click="openCamera">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M464 336a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm72 112h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V456c0-4.4-3.6-8-8-8z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">相机</text>
					</view>
					<view class="plus-item" @click="makeCall">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">语音/视频通话</text>
					</view>
					<view class="plus-item" @click="shareLocation">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="var(--color-text, #333333)"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="var(--color-text, #333333)"></path>
							</svg>
						</view>
						<text class="plus-text">定位</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部操作菜单 -->
		<view v-if="!isConcise" class="action-sheet" :class="{ 'active': showActionSheet }" @click="hideActionSheet">
			<view class="action-content" @click.stop>
				<view class="action-item" @click="goToMyAIModels">
					<text class="action-text">我购买的AI模型</text>
				</view>
				<view class="action-item" @click="goToAIDetails">
					<text class="action-text">AI详情信息</text>
				</view>
				<view class="action-item" @click="goToAIStore">
					<text class="action-text">AI模型商城</text>
				</view>
				<view class="action-cancel" @click="hideActionSheet">
					<text class="cancel-text">取消</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'AiChat',
		data() {
			return {
				modeStore: null,
				showActionSheet: false,
				scrollTop: 0,
				inputMessage: '',
				inputFocus: false,
				isVoiceMode: false,
				showEmojiPanel: false,
				currentPage: 0,
				emojiList: [
					[
						{ emoji: '[emoji1]', url: '/static/emoji/1.png' },
						{ emoji: '[emoji2]', url: '/static/emoji/2.png' },
						{ emoji: '[emoji3]', url: '/static/emoji/3.png' },
						{ emoji: '[emoji4]', url: '/static/emoji/4.png' },
						{ emoji: '[emoji5]', url: '/static/emoji/5.png' },
						{ emoji: '[emoji6]', url: '/static/emoji/6.png' },
						{ emoji: '[emoji7]', url: '/static/emoji/7.png' },
						{ emoji: '[emoji8]', url: '/static/emoji/8.png' },
						{ emoji: '[emoji9]', url: '/static/emoji/9.png' },
						{ emoji: '[emoji10]', url: '/static/emoji/10.png' },
						{ emoji: '[emoji11]', url: '/static/emoji/11.png' },
						{ emoji: '[emoji12]', url: '/static/emoji/12.png' }
					],
					[
						{ emoji: '[emoji13]', url: '/static/emoji/13.png' },
						{ emoji: '[emoji14]', url: '/static/emoji/14.png' },
						{ emoji: '[emoji15]', url: '/static/emoji/15.png' },
						{ emoji: '[emoji16]', url: '/static/emoji/16.png' },
						{ emoji: '[emoji17]', url: '/static/emoji/17.png' },
						{ emoji: '[emoji18]', url: '/static/emoji/18.png' },
						{ emoji: '[emoji19]', url: '/static/emoji/19.png' },
						{ emoji: '[emoji20]', url: '/static/emoji/20.png' },
						{ emoji: '[emoji21]', url: '/static/emoji/21.png' },
						{ emoji: '[emoji22]', url: '/static/emoji/22.png' },
						{ emoji: '[emoji23]', url: '/static/emoji/23.png' },
						{ emoji: '[emoji24]', url: '/static/emoji/24.png' }
					],
					[
						{ emoji: '[emoji25]', url: '/static/emoji/25.png' },
						{ emoji: '[emoji26]', url: '/static/emoji/26.png' },
						{ emoji: '[emoji27]', url: '/static/emoji/27.png' },
						{ emoji: '[emoji28]', url: '/static/emoji/28.png' },
						{ emoji: '[emoji29]', url: '/static/emoji/29.png' },
						{ emoji: '[emoji30]', url: '/static/emoji/30.png' },
						{ emoji: '[emoji31]', url: '/static/emoji/31.png' },
						{ emoji: '[emoji32]', url: '/static/emoji/32.png' },
						{ emoji: '[emoji33]', url: '/static/emoji/33.png' },
						{ emoji: '[emoji34]', url: '/static/emoji/34.png' },
						{ emoji: '[emoji35]', url: '/static/emoji/35.png' },
						{ emoji: '[emoji36]', url: '/static/emoji/36.png' }
					]
				],
				emojiMap: {
					'[emoji1]': '/static/emoji/1.png',
					'[emoji2]': '/static/emoji/2.png',
					'[emoji3]': '/static/emoji/3.png',
					'[emoji4]': '/static/emoji/4.png',
					'[emoji5]': '/static/emoji/5.png',
					'[emoji6]': '/static/emoji/6.png',
					'[emoji7]': '/static/emoji/7.png',
					'[emoji8]': '/static/emoji/8.png',
					'[emoji9]': '/static/emoji/9.png',
					'[emoji10]': '/static/emoji/10.png',
					'[emoji11]': '/static/emoji/11.png',
					'[emoji12]': '/static/emoji/12.png',
					'[emoji13]': '/static/emoji/13.png',
					'[emoji14]': '/static/emoji/14.png',
					'[emoji15]': '/static/emoji/15.png',
					'[emoji16]': '/static/emoji/16.png',
					'[emoji17]': '/static/emoji/17.png',
					'[emoji18]': '/static/emoji/18.png',
					'[emoji19]': '/static/emoji/19.png',
					'[emoji20]': '/static/emoji/20.png',
					'[emoji21]': '/static/emoji/21.png',
					'[emoji22]': '/static/emoji/22.png',
					'[emoji23]': '/static/emoji/23.png',
					'[emoji24]': '/static/emoji/24.png',
					'[emoji25]': '/static/emoji/25.png',
					'[emoji26]': '/static/emoji/26.png',
					'[emoji27]': '/static/emoji/27.png',
					'[emoji28]': '/static/emoji/28.png',
					'[emoji29]': '/static/emoji/29.png',
					'[emoji30]': '/static/emoji/30.png',
					'[emoji31]': '/static/emoji/31.png',
					'[emoji32]': '/static/emoji/32.png',
					'[emoji33]': '/static/emoji/33.png',
					'[emoji34]': '/static/emoji/34.png',
					'[emoji35]': '/static/emoji/35.png',
					'[emoji36]': '/static/emoji/36.png'
				},
				showPlusPanel: false,
				currentAiModel: {},
				loading: false,
				chatLoading: false,
				messages: [],
				modelId: null,
				sessionId: null,
				userId: null,
				targetUserId: null,
				friendId: null,
				friendPhone: null,
				startX: 0
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
			
			console.log('ai-chat页面加载，接收到的参数:', options);
			
			// 获取AI模型ID参数，如果没有参数则显示错误
			if (!options.modelId) {
				console.log('没有modelId参数，显示错误');
				this.showModelNotFoundError();
				return;
			}
			
			// 保存modelId到组件数据中
			this.modelId = options.modelId;
			console.log('保存的modelId:', this.modelId);
			
			// 获取用户ID
			const userId = uni.getStorageSync('userId');
			console.log('获取到的userId:', userId);
			if (!userId) {
				console.log('没有userId，显示登录错误');
				this.showUserNotLoginError();
				return;
			}
			this.userId = userId;
			this.targetUserId = this.modelId;
			this.friendId = this.modelId;
			
			this.initSessionId();
			
			// 加载AI模型数据和聊天记录
			console.log('开始加载AI模型数据和聊天记录');
			this.loadAiModelData(options.modelId, userId);
			this.loadChatMessages(userId, options.modelId);
			
			// 页面加载完成后滚动到底部显示最新消息
			this.$nextTick(() => {
				this.scrollToBottom();
			});
		},
		methods: {
			// 加载AI模型数据
			async loadAiModelData(modelId, userId) {
				try {
					this.loading = true;
					
					// 首先尝试从用户AI模型接口获取（通过userId查询）
					const userModelResponse = await this.getUserAiModelByUserId(userId);
					if (userModelResponse && userModelResponse.data) {
						// 在用户的AI模型列表中查找匹配的模型
						const userModels = userModelResponse.data;
						const targetModel = userModels.find(model => 
							model.userAiId == modelId || model.parentModelId == modelId
						);
						if (targetModel) {
							this.currentAiModel = targetModel;
							return;
						}
					}
					
					// 如果用户模型不存在，则从基础模型接口获取
					const baseModelResponse = await this.getAimodelBaseInfoById(modelId);
					if (baseModelResponse && baseModelResponse.data) {
						this.currentAiModel = baseModelResponse.data;
						return;
					}
					
					// 如果都获取不到，显示模型不存在错误
					this.showModelNotFoundError();
					
				} catch (error) {
					console.error('加载AI模型数据失败:', error);
					this.showNetworkError();
				} finally {
					this.loading = false;
				}
			},
			
			// 获取用户AI模型数据（通过userId）
			getUserAiModelByUserId(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: '${this.$baseUrl}/api/user-ai-model/user/' + userId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 获取用户AI模型数据（通过userAiId）
			getUserAiModelById(modelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: '${this.$baseUrl}/api/user-ai-model/' + modelId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 获取AI模型基础信息
			getAimodelBaseInfoById(modelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: '${this.$baseUrl}/api/entity/aimodel-base-info/' + modelId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 加载聊天记录
			async loadChatMessages(userId, userAiId) {
				try {
					this.chatLoading = true;
					console.log('开始加载聊天记录，userId:', userId, 'userAiId:', userAiId);
					
					const response = await this.getChatMessages(userId, userAiId);
					console.log('获取聊天记录响应:', response);
					
					if (response.data && response.data.length > 0) {
						console.log('找到聊天记录，数量:', response.data.length);
						console.log('原始聊天数据:', response.data);
						// 将后端数据转换为前端消息格式
						this.messages = this.convertChatDataToMessages(response.data);
						console.log('转换后的消息:', this.messages);
					} else {
						console.log('没有找到聊天记录，显示默认欢迎消息');
						// 如果没有聊天记录，显示欢迎消息
						this.messages = [{
							id: 1,
							type: 'left',
							text: '你好！我是你的 AI 助手，有什么可以帮助你的吗？',
							time: this.getCurrentTime()
						}];
					}
				} catch (error) {
					console.error('加载聊天记录失败:', error);
					// 如果加载失败，显示默认欢迎消息
					this.messages = [{
						id: 1,
						type: 'left',
						text: '你好！我是你的 AI 助手，有什么可以帮助你的吗？',
						time: this.getCurrentTime()
					}];
				} finally {
					this.chatLoading = false;
				}
			},
			
			// 获取聊天记录
			getChatMessages(userId, userAiId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseUrl}/api/a-entities-wlq/ai-chat-list-detail-r/user/${userId}/ai/${userAiId}`,
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
			
			// 将后端聊天数据转换为前端消息格式
			convertChatDataToMessages(chatData) {
				const messages = [];
				let messageId = 1;
				
				// 按时间戳排序，最早的在前
				chatData.sort((a, b) => {
					if (a.messageTimestamp && b.messageTimestamp) {
						return new Date(a.messageTimestamp) - new Date(b.messageTimestamp);
					}
					return 0;
				});
				
				chatData.forEach(chat => {
					// 添加用户消息
					if (chat.userMessage) {
						messages.push({
							id: messageId++,
							type: 'right',
							text: chat.userMessage,
							time: this.formatTimestamp(chat.messageTimestamp)
						});
					}
					
					// 添加AI回复
					if (chat.aiResponse) {
						messages.push({
							id: messageId++,
							type: 'left',
							text: chat.aiResponse,
							time: this.formatTimestamp(chat.messageTimestamp)
						});
					}
				});
				
				return messages;
			},
			
			// 格式化时间戳
			formatTimestamp(timestamp) {
				if (!timestamp) return this.getCurrentTime();
				
				const date = new Date(timestamp);
				const now = new Date();
				const diff = now - date;
				
				// 如果是今天
				if (diff < 24 * 60 * 60 * 1000 && date.getDate() === now.getDate()) {
					const hours = date.getHours();
					const minutes = date.getMinutes();
					const timeStr = hours < 12 ? '上午' : '下午';
					const displayHours = hours < 12 ? hours : hours - 12;
					return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
				}
				
				// 如果是昨天
				if (diff < 48 * 60 * 60 * 1000 && date.getDate() === now.getDate() - 1) {
					return '昨天 ' + this.formatTime(date);
				}
				
				// 其他情况显示日期
				return `${date.getMonth() + 1}月${date.getDate()}日 ${this.formatTime(date)}`;
			},
			
			// 格式化时间
			formatTime(date) {
				const hours = date.getHours();
				const minutes = date.getMinutes();
				const timeStr = hours < 12 ? '上午' : '下午';
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},
			
			// 显示模型不存在错误
			showModelNotFoundError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '模型不存在',
					modelDesc: '抱歉，您要访问的AI模型不存在或已被删除',
					modelImageUrl: null
				};
				uni.showToast({
					title: 'AI模型不存在',
					icon: 'none',
					duration: 3000
				});
			},
			
			// 显示网络错误
			showNetworkError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '网络连接失败',
					modelDesc: '网络连接超时，请检查网络设置后重试',
					modelImageUrl: null
				};
				uni.showToast({
					title: '网络连接超时',
					icon: 'none',
					duration: 3000
				});
			},
			
			// 显示用户未登录错误
			showUserNotLoginError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '请先登录',
					modelDesc: '您需要先登录才能使用AI聊天功能',
					modelImageUrl: null
				};
				uni.showToast({
					title: '请先登录',
					icon: 'none',
					duration: 3000
				});
				
				// 延迟跳转到登录页面
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/login/login-replica'
					});
				}, 2000);
			},
			
			goBack() {
				// 尝试返回上一页，如果失败则跳转到AI列表
				try {
					uni.navigateBack({
						fail: () => {
							// 如果返回失败，跳转到AI列表页面
							uni.navigateTo({
								url: '/pages/ai/ai-list'
							});
						}
					});
				} catch (error) {
					// 如果出错，跳转到AI列表页面
					uni.navigateTo({
						url: '/pages/ai/ai-list'
					});
				}
			},
			
			showMoreOptions() {
				if (this.isConcise) return;
				this.showActionSheet = true;
			},
			
			hideActionSheet() {
				this.showActionSheet = false;
			},
			
			// 跳转到我购买的AI模型页面
			goToMyAIModels() {
				this.hideActionSheet();
				uni.navigateTo({
					url: '/pages/ai/ai-list'
				});
			},
			
			// 跳转到AI详情信息页面
			goToAIDetails() {
				this.hideActionSheet();
				// 传递AI模型ID和名称
				const aiName = this.currentAiModel.modelName || this.modelId || 'AI助手';
				uni.navigateTo({
					url: `/pages/ai/gift?userAiId=${this.modelId}&aiName=${encodeURIComponent(aiName)}`
				});
			},
			
			// 跳转到AI模型商城页面
			goToAIStore() {
				this.hideActionSheet();
				uni.navigateTo({
					url: '/pages/ai/ai-store'
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
					title: '开始录音',
					icon: 'none'
				});
			},
			
			endVoiceRecord() {
				// 结束录音
				uni.showToast({
					title: '录音完成',
					icon: 'none'
				});
			},
			
			selectImage() {
				if (this.isConcise) return;
				this.hidePlusPanel();
				
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['album'],
					success: async (res) => {
						console.log('选择图片成功:', res);
						
						if (!res.tempFilePaths || res.tempFilePaths.length === 0) {
							uni.showToast({ title: '图片路径获取失败', icon: 'error' });
							return;
						}
						const tempFilePath = String(res.tempFilePaths[0] || '');
						
						if (!tempFilePath) {
							uni.showToast({ title: '图片路径无效', icon: 'error' });
							return;
						}
						
						const tempMessage = {
							id: Date.now(),
							type: 'right',
							imageUrl: tempFilePath,
							isImage: true,
							time: this.getCurrentTime(),
							demandParty: this.userId,
							createdAt: new Date().toISOString()
						};
						this.messages.push(tempMessage);
						this.scrollToBottom();
						
						await this.uploadImage(tempFilePath);
					},
					fail: (err) => {
						console.error('选择图片失败:', err);
						uni.showToast({ title: '选择图片失败', icon: 'error' });
					}
				});
			},
			
			async uploadImage(tempFilePath) {
				try {
					if (typeof tempFilePath !== 'string' || !tempFilePath.trim()) {
						console.error('无效的图片路径:', tempFilePath);
						uni.showToast({ title: '图片路径错误', icon: 'error' });
						return;
					}
					
					if (!this.sessionId || !this.userId || !this.targetUserId) {
						console.error('参数缺失:', {
							sessionId: this.sessionId,
							userId: this.userId,
							targetUserId: this.targetUserId
						});
						uni.showToast({ title: '参数错误', icon: 'error' });
						return;
					}
					
					const uploadRes = await uni.uploadFile({
						url: `${this.$baseUrl}/api/user-chat-detail/upload/image`,
						filePath: tempFilePath,
						name: 'file',
						formData: {
							sessionId: this.sessionId,
							demandParty: this.userId,
							responseParty: this.targetUserId
						}
					});

					if (uploadRes.statusCode === 200) {
						const result = JSON.parse(uploadRes.data);
						console.log('图片上传成功:', result);
						const lastMsg = this.messages[this.messages.length - 1];
						lastMsg.imageUrl = result.fileUrl;
						lastMsg.id = result.chatId;
					} else {
						console.error('图片上传失败:', uploadRes);
						uni.showToast({ title: '图片发送失败', icon: 'error' });
					}
				} catch (error) {
					console.error('图片上传异常:', error);
					uni.showToast({ title: '图片发送失败', icon: 'error' });
				}
			},
			
			openCamera() {
				if (this.isConcise) return;
				this.hidePlusPanel();
				
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['camera'],
					success: async (res) => {
						console.log('拍摄图片成功:', res);
						const tempFilePath = res.tempFilePaths[0];
						
						const tempMessage = {
							id: Date.now(),
							type: 'right',
							imageUrl: tempFilePath,
							isImage: true,
							time: this.getCurrentTime(),
							demandParty: this.userId,
							createdAt: new Date().toISOString()
						};
						this.messages.push(tempMessage);
						this.scrollToBottom();
						
						await this.uploadImage(tempFilePath);
					},
					fail: (err) => {
						console.error('相机调用失败:', err);
						if (err.errMsg.includes('deny')) {
							uni.showToast({ title: '请授予相机权限', icon: 'none' });
						} else {
							uni.showToast({ title: '拍摄失败', icon: 'error' });
						}
					}
				});
			},
			
			makeCall() {
				if (this.isConcise) return;
				this.hidePlusPanel();
				
				const phoneNumber = this.friendPhone || '10086';
				
				uni.makePhoneCall({
					phoneNumber: phoneNumber,
					success: () => {
						console.log('调用通话成功');
					},
					fail: (err) => {
						console.error('通话调用失败:', err);
						uni.showToast({ title: '调用失败', icon: 'error' });
					}
				});
			},
			
			shareLocation() {
				if (this.isConcise) return;
				this.hidePlusPanel();
				
				uni.chooseLocation({
					success: async (res) => {
						console.log('选择位置成功:', res);
						
						const locationMessage = {
							id: Date.now(),
							type: 'right',
							isLocation: true,
							location: {
								name: res.name,
								address: res.address,
								lat: res.latitude,
								lng: res.longitude
							},
							time: this.getCurrentTime(),
							demandParty: this.userId,
							createdAt: new Date().toISOString()
						};
						this.messages.push(locationMessage);
						this.scrollToBottom();
						
						await this.saveLocationMessage(res);
					},
					fail: (err) => {
						console.error('位置选择失败:', err);
						if (err.errMsg.includes('deny')) {
							uni.showToast({ title: '请授予位置权限', icon: 'none' });
						} else {
							uni.showToast({ title: '获取位置失败', icon: 'error' });
						}
					}
				});
			},
			
			async saveLocationMessage(location) {
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-chat-detail/send/location`,
						method: 'POST',
						data: {
							sessionId: this.sessionId,
							userId: this.userId,
							friendId: this.friendId,
							locationName: location.name,
							address: location.address,
							latitude: location.latitude,
							longitude: location.longitude
						}
					});

					if (response.statusCode === 200) {
						console.log('位置消息保存成功:', response.data);
						const lastMsg = this.messages[this.messages.length - 1];
						lastMsg.id = response.data.messageId;
					} else {
						console.error('位置消息保存失败:', response);
						uni.showToast({ title: '位置发送失败', icon: 'error' });
					}
				} catch (error) {
					console.error('位置消息保存异常:', error);
					uni.showToast({ title: '位置发送失败', icon: 'error' });
				}
			},
			
			onInputFocus() {
				this.inputFocus = true;
			},
			
			onInputBlur() {
				this.inputFocus = false;
			},
			
			async sendMessage() {
				if (!this.inputMessage.trim()) {
					return;
				}
				
				const userMessage = this.inputMessage.trim();
				const userId = uni.getStorageSync('userId');
				
				if (!userId) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				// 添加用户消息到界面
				const userMsg = {
					id: Date.now(),
					type: 'right',
					text: userMessage,
					time: this.getCurrentTime()
				};
				this.messages.push(userMsg);
				
				// 清空输入框
				this.inputMessage = '';
				
				// 滚动到底部
				this.$nextTick(() => {
					this.scrollToBottom();
				});
				
				try {
					// 生成AI回复
					const aiResponse = this.generateAIResponse(userMessage);
					
					// 添加AI回复到界面
					const aiMsg = {
						id: Date.now() + 1,
						type: 'left',
						text: aiResponse,
						time: this.getCurrentTime()
					};
					this.messages.push(aiMsg);
					
					// 保存完整的对话到数据库（用户问题 + AI回答）
					await this.saveChatMessage(userId, this.modelId, userMessage, aiResponse, 'ai_chat');
					
					// 滚动到底部
					this.$nextTick(() => {
						this.scrollToBottom();
					});
					
				} catch (error) {
					console.error('发送消息失败:', error);
					uni.showToast({
						title: '发送失败，请重试',
						icon: 'none'
					});
				}
			},
			
			generateAIResponse(userMessage) {
				// 简单的 AI 回复逻辑
				const responses = [
					'我理解你的问题，让我来帮你解答。',
					'这是一个很有趣的问题！',
					'根据我的分析，我认为...',
					'让我为你提供一些建议。',
					'这个问题需要仔细考虑，我的建议是...',
					'谢谢你的提问，让我想想...',
					'这是一个很好的观点！',
					'我建议你可以尝试...'
				];
				return responses[Math.floor(Math.random() * responses.length)];
			},
			
			getCurrentTime() {
				const now = new Date();
				const hours = now.getHours();
				const minutes = now.getMinutes();
				const timeStr = hours < 12 ? '上午' : '下午';
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},
			
			scrollToBottom() {
				// 滚动到底部
				this.scrollTop = 999999;
			},
			
			// 获取头像显示文字
			getAvatarText() {
				if (!this.currentAiModel.modelName) {
					return '?';
				}
				// 取模型名称的第一个字符
				return this.currentAiModel.modelName.charAt(0);
			},
			
			// 保存聊天消息到数据库
			async saveChatMessage(userId, userAiId, userMessage, aiResponse, chatType) {
				return new Promise((resolve, reject) => {
					const chatData = {
						userId: parseInt(userId),
						userAiId: userAiId ? parseInt(userAiId) : null,
						userMessage: userMessage,
						aiResponse: aiResponse,
						chatType: chatType,
						messageTimestamp: new Date().toISOString(),
						createdAt: new Date().toISOString(),
						updatedAt: new Date().toISOString()
					};
					
					console.log('发送聊天数据:', chatData);
					
					uni.request({
						url: '${this.$baseUrl}/api/a-entities-wlq/ai-chat-list-detail-r/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: chatData,
						timeout: 10000,
						success: (res) => {
							console.log('聊天消息保存成功:', res);
							resolve(res);
						},
						fail: (err) => {
							console.error('聊天消息保存失败:', err);
							reject(err);
						}
					});
				});
			},
			
			initSessionId() {
				const historySessionId = uni.getStorageSync(`sessionId_${this.userId}_${this.modelId}`);
				if (historySessionId) {
					this.sessionId = historySessionId;
					console.log('复用历史会话ID:', this.sessionId);
					return;
				}
				this.sessionId = Number(`${this.userId}${this.modelId}${Date.now().toString().slice(-6)}`);
				console.log('生成新会话ID:', this.sessionId);
				uni.setStorageSync(`sessionId_${this.userId}_${this.modelId}`, this.sessionId);
			},
			
			selectEmoji(item) {
				this.inputMessage += item.emoji;
			},
			
			prevPage() {
				if (this.currentPage > 0) {
					this.currentPage--;
				}
			},
			
			nextPage() {
				if (this.currentPage < this.emojiList.length - 1) {
					this.currentPage++;
				}
			},
			
			touchStart(e) {
				this.startX = e.changedTouches[0].clientX;
			},
			
			touchEnd(e) {
				const endX = e.changedTouches[0].clientX;
				const diffX = endX - this.startX;
				if (diffX > 50) {
					this.prevPage();
				} else if (diffX < -50) {
					this.nextPage();
				}
			},
			
			parseMixedMessage(content) {
				if (!content) return [];
				const result = [];
				const emojiReg = /\[emoji(\d+)\]/g;
				let lastIndex = 0;

				content.replace(emojiReg, (match, num, index) => {
					if (index > lastIndex) {
						result.push({ type: 'text', value: content.slice(lastIndex, index) });
					}
					const emojiKey = `[emoji${num}]`;
					if (this.emojiMap[emojiKey]) {
						result.push({ type: 'emoji', value: this.emojiMap[emojiKey] });
					} else {
						result.push({ type: 'text', value: match });
					}
					lastIndex = index + match.length;
				});

				if (lastIndex < content.length) {
					result.push({ type: 'text', value: content.slice(lastIndex) });
				}

				return result;
			}
		}
	}
</script>

<style>
	.chat-page {
		min-height: 100vh;
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
		background-color: var(--color-bg-light, #F5F5F5);
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
	
	/* 加载状态 */
	.loading-container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		height: 400rpx;
		gap: 20rpx;
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
		color: var(--color-text-secondary, #666);
	}
	
	/* 无消息提示 */
	.no-messages {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 400rpx;
	}
	
	.no-messages-text {
		font-size: 28rpx;
		color: var(--color-text-placeholder, #999);
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
	
	.avatar-placeholder {
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: var(--color-bg-medium, #E0E0E0);
		border: 2rpx solid var(--color-border, #CCCCCC);
	}
	
	.avatar-text {
		font-size: 32rpx;
		font-weight: bold;
		color: var(--color-text-secondary, #666666);
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
	
	.message-time {
		font-size: 20rpx;
		color: var(--color-text-placeholder, #999999);
		margin-top: 8rpx;
		display: block;
	}
	
	.message.right .message-time {
		color: var(--color-white-transparent, rgba(255, 255, 255, 0.7));
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

	.chat-page.is-concise .avatar-text {
		font-size: 28rpx;
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

	.chat-page.care .avatar-text {
		font-size: 34rpx;
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

	.chat-page.care .message-time {
		font-size: 26rpx;
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

	/* 图片消息样式 */
	.message-image {
		max-width: 300rpx;
		border-radius: 16rpx;
	}

	/* 混合消息中的表情图片 */
	.mixed-emoji {
		width: 32rpx;
		height: 32rpx;
		vertical-align: middle;
		margin: 0 4rpx;
	}

	/* 表情面板高度和滚动 */
	.emoji-content {
		height: 360rpx;
		overflow: hidden;
	}

	.emoji-grid {
		height: calc(100% - 40rpx);
		overflow-y: auto;
		scrollbar-width: none;
		-webkit-overflow-scrolling: touch;
	}

	.emoji-grid::-webkit-scrollbar {
		display: none;
	}
</style>