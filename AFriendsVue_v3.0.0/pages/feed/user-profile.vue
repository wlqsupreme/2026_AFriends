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
		
		<!-- 头部区域 -->
		<view class="header">
			<view class="blur-background">
				<view class="pizza-image"></view>
			</view>
			<view class="header-buttons">
				<view class="menu-button" @click="openMenu">
					<view class="menu-icon">
						<view class="menu-line"></view>
						<view class="menu-line"></view>
						<view class="menu-line"></view>
					</view>
				</view>
				<view class="share-button" @click="openShare">
					<text class="share-icon">📤</text>
				</view>
			</view>
		</view>
		
		<!-- 用户信息卡片 -->
		<view class="user-card">
			<view class="avatar">
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
			<view class="user-info">
				<text class="username">{{ userInfo.username || '猫又屋之主' }}</text>
				<text class="location">{{ userInfo.location || '中国-南京' }}</text>
				<view class="vip-info">
					<text class="vip-text">VIP lv{{ userInfo.level || 10 }}</text>
					<text class="exp-text">经验{{ userInfo.gold || 0 }}/{{ (userInfo.level || 10) * 400 }}</text>
				</view>
			</view>
		</view>
		
		<!-- 用户签名区域 -->
		<view class="signature-area" @click="editSignature">
			<view class="signature-input-container" v-if="!isEditing">
				<text class="signature-text">{{signatureText}}</text>
			</view>
			<view class="signature-input-container" v-else>
				<input 
					class="signature-input" 
					v-model="signatureText" 
					:maxlength="100"
					placeholder="请输入个性签名"
					@blur="saveSignature"
					@input="updateCharCount"
					focus
				/>
				<view class="char-count">{{charCount}}/100</view>
			</view>
		</view>
		
		<!-- 虚拟键盘 -->
		<view class="virtual-keyboard" v-if="isEditing && isMobile">
			<!-- 数字行 -->
			<view class="keyboard-row">
				<view class="key" v-for="num in ['1','2','3','4','5','6','7','8','9','0']" :key="num">
					<text class="key-text">{{num}}</text>
				</view>
			</view>
			<!-- 字母行1 -->
			<view class="keyboard-row">
				<view class="key" v-for="letter in ['q','w','e','r','t','y','u','i','o','p']" :key="letter">
					<text class="key-text">{{letter}}</text>
				</view>
			</view>
			<!-- 字母行2 -->
			<view class="keyboard-row">
				<view class="key shift-key">
					<text class="key-text">⇧</text>
				</view>
				<view class="key" v-for="letter in ['a','s','d','f','g','h','j','k','l']" :key="letter">
					<text class="key-text">{{letter}}</text>
				</view>
				<view class="key backspace-key">
					<text class="key-text">⌫</text>
				</view>
			</view>
			<!-- 字母行3 -->
			<view class="keyboard-row">
				<view class="key symbol-key">
					<text class="key-text">?123</text>
				</view>
				<view class="key" v-for="letter in ['z','x','c','v','b','n','m']" :key="letter">
					<text class="key-text">{{letter}}</text>
				</view>
				<view class="key comma-key">
					<text class="key-text">,</text>
				</view>
			</view>
			<!-- 底部行 -->
			<view class="keyboard-row bottom-row">
				<view class="key emoji-key">
					<text class="key-text">😊</text>
				</view>
				<view class="key space-key">
					<text class="key-text">空格</text>
				</view>
				<view class="key period-key">
					<text class="key-text">.</text>
				</view>
				<view class="key enter-key">
					<text class="key-text">完成</text>
				</view>
			</view>
		</view>
		
		<!-- 导航标签 -->
		<view class="nav-tabs">
			<view class="tab" :class="{ 'active': activeTab === 'dynamic' }" @click="switchTab('dynamic')">
				<text class="tab-text">动态</text>
				<view class="tab-underline" v-if="activeTab === 'dynamic'"></view>
			</view>
			<view class="tab" :class="{ 'active': activeTab === 'collections' }" @click="switchTab('collections')">
				<text class="tab-text">收藏</text>
				<view class="tab-underline" v-if="activeTab === 'collections'"></view>
			</view>

			<view class="search-icon">🔍</view>
		</view>
		
		<!-- 标签内容区域 -->
		<view class="content-area">
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载中...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError">
				<text class="error-icon">⚠️</text>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadUserProfileData">重试</button>
					<button class="retry-button" @click="testConnection">测试连接</button>
				</view>
			</view>
			
			<!-- 动态标签内容 -->
			<view class="tab-panel" v-if="activeTab === 'dynamic' && !loading && !hasError">
				<view class="post-item" v-for="(post, index) in personalPosts" :key="index" @click="handlePostClick(post)">
					<view class="post-header">
						<view class="post-avatar">
							<view class="small-cat-avatar">
								<view class="small-cat-body"></view>
								<view class="small-cat-face">
									<view class="small-cat-eyes">
										<view class="small-cat-eye"></view>
										<view class="small-cat-eye"></view>
									</view>
								</view>
							</view>
						</view>
						<view class="post-info">
							<text class="post-username">猫又屋之主</text>
							<text class="post-time">{{ post.timeAgo }}</text>
						</view>
					</view>
					<view class="post-content">
						<!-- 内容类型标签 -->
						<view class="content-type" v-if="post.contentType">
							<text class="type-label">{{ post.contentType }}:</text>
						</view>
						
						<!-- 文字内容 -->
						<view class="text-content" v-if="post.textContent">
							<text class="post-title">{{ post.textContent }}</text>
						</view>
						
						<!-- 小说信息 -->
						<view class="novel-info" v-if="post.novelInfo">
							<text class="post-title">{{ post.novelInfo.title }}</text>
							<text class="novel-desc">{{ post.novelInfo.description }}</text>
							<text class="novel-rating">评分: {{ post.novelInfo.rating }}</text>
						</view>
						
						<!-- 图片内容 -->
						<view class="image-content" v-if="post.images && post.images.length > 0">
							<view class="image-grid" :class="getImageGridClass(post.images.length)">
								<image 
									v-for="(image, imgIndex) in post.images" 
									:key="imgIndex"
									class="content-image" 
									:src="image" 
									mode="aspectFill"
									@click.stop="previewImage(post.images, imgIndex)"
								></image>
							</view>
						</view>
						
						<!-- 小说封面 -->
						<view class="main-image" v-if="post.novelCover">
							<image class="novel-cover" :src="post.novelCover" mode="aspectFill"></image>
							<view class="image-overlay" v-if="post.novelInfo">
								<text class="overlay-title">{{ post.novelInfo.title }}</text>
							</view>
						</view>
					</view>
					
					<!-- 互动指标 -->
					<view class="engagement-metrics">
						<view class="metric-item" @click.stop="handleLike(post)">
							<text class="metric-icon" :class="{ 'active': post.isLiked }">👍</text>
							<text class="metric-count" :class="{ 'active': post.isLiked }">{{ formatCount(post.likes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleDislike(post)">
							<text class="metric-icon" :class="{ 'active': post.isDisliked }">👎</text>
							<text class="metric-count" :class="{ 'active': post.isDisliked }">{{ formatCount(post.dislikes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleFavorite(post)">
							<text class="metric-icon" :class="{ 'active': post.isFavorited }">⭐</text>
							<text class="metric-count" :class="{ 'active': post.isFavorited }">{{ formatCount(post.favorites) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleComment(post)">
							<text class="metric-icon">💬</text>
							<text class="metric-count">{{ formatCount(post.comments) }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 收藏标签内容 -->
			<view class="tab-panel" v-if="activeTab === 'collections' && !loading && !hasError">
				<view class="post-item" v-for="(post, index) in collectedPosts" :key="index" @click="handlePostClick(post)">
					<view class="post-header">
						<view class="post-avatar">
							<view class="small-cat-avatar">
								<view class="small-cat-body"></view>
								<view class="small-cat-face">
									<view class="small-cat-eyes">
										<view class="small-cat-eye"></view>
										<view class="small-cat-eye"></view>
									</view>
								</view>
							</view>
						</view>
						<view class="post-info">
							<text class="post-username">{{ post.username }}</text>
							<text class="post-time">{{ post.timeAgo }}</text>
						</view>
					</view>
					<view class="post-content">
						<!-- 内容类型标签 -->
						<view class="content-type" v-if="post.contentType">
							<text class="type-label">{{ post.contentType }}:</text>
						</view>
						
						<!-- 文字内容 -->
						<view class="text-content" v-if="post.textContent">
							<text class="post-title">{{ post.textContent }}</text>
						</view>
						
						<!-- 小说信息 -->
						<view class="novel-info" v-if="post.novelInfo">
							<text class="post-title">{{ post.novelInfo.title }}</text>
							<text class="novel-desc">{{ post.novelInfo.description }}</text>
							<text class="novel-rating">评分: {{ post.novelInfo.rating }}</text>
						</view>
						
						<!-- 图片内容 -->
						<view class="image-content" v-if="post.images && post.images.length > 0">
							<view class="image-grid" :class="getImageGridClass(post.images.length)">
								<image 
									v-for="(image, imgIndex) in post.images" 
									:key="imgIndex"
									class="content-image" 
									:src="image" 
									mode="aspectFill"
									@click.stop="previewImage(post.images, imgIndex)"
								></image>
							</view>
						</view>
						
						<!-- 小说封面 -->
						<view class="main-image" v-if="post.novelCover">
							<image class="novel-cover" :src="post.novelCover" mode="aspectFill"></image>
							<view class="image-overlay" v-if="post.novelInfo">
								<text class="overlay-title">{{ post.novelInfo.title }}</text>
							</view>
						</view>
					</view>
					
					<!-- 互动指标 -->
					<view class="engagement-metrics">
						<view class="metric-item" @click.stop="handleLike(post)">
							<text class="metric-icon" :class="{ 'active': post.isLiked }">👍</text>
							<text class="metric-count" :class="{ 'active': post.isLiked }">{{ formatCount(post.likes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleDislike(post)">
							<text class="metric-icon" :class="{ 'active': post.isDisliked }">👎</text>
							<text class="metric-count" :class="{ 'active': post.isDisliked }">{{ formatCount(post.dislikes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleFavorite(post)">
							<text class="metric-icon" :class="{ 'active': post.isFavorited }">⭐</text>
							<text class="metric-count" :class="{ 'active': post.isFavorited }">{{ formatCount(post.favorites) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleComment(post)">
							<text class="metric-icon">💬</text>
							<text class="metric-count">{{ formatCount(post.comments) }}</text>
						</view>
					</view>
				</view>
			</view>
			

		</view>
		
		<!-- 分享弹层 -->
		<view class="share-overlay" v-if="showShare" @click="closeShare">
			<view class="share-sheet" @click.stop>
				<view class="sheet-handle"></view>
				<text class="sheet-title">分享到</text>
				<view class="share-friends">
					<scroll-view class="friends-scroll" scroll-x="true">
						<view class="friend-item" v-for="(friend, index) in shareFriends" :key="index" @click="shareToFriend(friend)">
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

				<view class="sheet-cancel" @click="closeShare"><text class="cancel-text">取消</text></view>
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
			<view class="nav-item active" @click="goToAIList">
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
		
		<!-- 左侧菜单面板 -->
		<view class="side-menu" :class="{ 'active': showSideMenu }" @click="closeSideMenu">
			<view class="menu-content" @click.stop>
				<!-- 菜单头部 -->
				<view class="menu-header">
					<view class="menu-close" @click="closeSideMenu">
						<text class="close-icon">×</text>
					</view>
				</view>
				
				<!-- 菜单选项列表 -->
				<view class="menu-list">
					<!-- 我的AI -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyAI">
							<text class="menu-text">我的AI</text>
						</view>
					</view>
					
					<!-- 我的认证、喜恶、成就 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyCertification">
							<text class="menu-text">我的认证</text>
						</view>
						<view class="menu-item" @click="goToMyPreferences">
							<text class="menu-text">我的喜恶</text>
						</view>
						<view class="menu-item" @click="goToMyAchievements">
							<text class="menu-text">我的成就</text>
						</view>
						<view class="menu-item" @click="goToMyPreferences">
							<text class="menu-text">我有</text>
						</view>
						<view class="menu-item" @click="goToMyPreferences">
							<text class="menu-text">我要</text>
						</view>
					</view>
					
					<!-- 相册、历史评论、浏览记录、草稿箱 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToAlbum">
							<text class="menu-text">相册</text>
						</view>
						<view class="menu-item" @click="goToHistoryComments">
							<text class="menu-text">历史评论</text>
						</view>
						<view class="menu-item" @click="goToBrowsingHistory">
							<text class="menu-text">浏览记录</text>
						</view>
						<view class="menu-item" @click="goToDraftBox">
							<text class="menu-text">草稿箱</text>
						</view>
					</view>
					
					<!-- 购买记录、购物车、钱包 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToPurchaseRecord">
							<text class="menu-text">购买记录</text>
						</view>
						<view class="menu-item" @click="goToShoppingCart">
							<text class="menu-text">购物车</text>
						</view>
						<view class="menu-item" @click="goToWallet">
							<text class="menu-text">钱包</text>
						</view>
					</view>
					
					<!-- 编辑资料 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToEditProfile">
							<text class="menu-text">编辑资料</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				friendName: '', // 好友名称
				isEditing: false,
				signatureText: '这个人很懒,什么都没有留下',
				charCount: 0,
				isMobile: false,
				showSideMenu: false, // 控制左侧菜单显示
				activeTab: 'dynamic', // 当前激活的标签
				showShare: false,
				userId: 1000100, // 默认用户ID，从页面参数获取
				loading: false,
				errorMessage: '',
				hasError: false,
				userInfo: {}, // 用户基本信息
				dynamicStats: {}, // 动态统计
				collectionStats: {}, // 收藏统计
				shareUsers: ['奶绿冰阔落','我想次炸鸡','匿名为某','康已奀','小菜刀御','超级好'],
				shareAppsRow1: [
					{ name: 'Chat..Group', cls: 'app-mail' },
					{ name: 'Super Group', cls: 'app-super' },
					{ name: 'Friends', cls: 'app-wechat' },
					{ name: 'Moments', cls: 'app-moment' },
					{ name: 'Alipay Frd', cls: 'app-alipay' }
				],
				shareAppsRow2: [
					{ name: 'Use t..ard Bg' },
					{ name: 'Make pic' },
					{ name: 'Contr..ovelty' },
					{ name: 'Favorite' },
					{ name: 'Promote' }
				],
				shareFriends: [
					{ name: '奶绿冰阔落', avatar: '/static/avatar-default.png' },
					{ name: '我想次炸鸡', avatar: '/static/avatar-default.png' },
					{ name: '匿名为某', avatar: '/static/avatar-default.png' },
					{ name: '康已奀', avatar: '/static/avatar-default.png' },
					{ name: '小菜刀御', avatar: '/static/avatar-default.png' },
					{ name: '超级好', avatar: '/static/avatar-default.png' },
					{ name: 'QQ好友七', avatar: '/static/avatar-default.png' }
				],
				personalPosts: [
					{
						id: 5, // 绘画帖子，使用唯一ID
						username: '猫又屋之主',
						timeAgo: '12分钟前',
						contentType: '绘画',
						textContent: '绘画练习',
						images: [],
						novelInfo: null,
						novelCover: null,
						likes: 10,
						dislikes: 2,
						favorites: 5,
						comments: 15,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					{
						id: 1, // 小说帖子，对应novel-detail.vue中的ID 1
						username: '猫又屋之主',
						timeAgo: '2小时前',
						contentType: '小说',
						textContent: null,
						images: [],
						novelInfo: {
							title: '神秘的猫又',
							description: '在一个古老的村庄里，流传着一个关于猫又的神秘传说。',
							rating: '4.5'
						},
						novelCover: '/static/novel-cover.png',
						likes: 20,
						dislikes: 5,
						favorites: 10,
						comments: 25,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					{
						id: 3, // 图片帖子，对应post-detail.vue中的ID 3
						username: '猫又屋之主',
						timeAgo: '1天前',
						contentType: '图片',
						textContent: null,
						images: ['/static/image1.jpg', '/static/image2.jpg'],
						novelInfo: null,
						novelCover: null,
						likes: 15,
						dislikes: 3,
						favorites: 8,
						comments: 10,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					}
				],
				collectedPosts: [
					{
						id: 4,
						username: '猫又屋之主',
						timeAgo: '1小时前',
						contentType: '动态',
						textContent: '今天天气真好！',
						images: [],
						novelInfo: null,
						novelCover: null,
						likes: 10,
						dislikes: 1,
						favorites: 5,
						comments: 10,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					{
						id: 2, // 对应novel-detail.vue中的ID 2
						username: '猫又屋之主',
						timeAgo: '2天前',
						contentType: '小说',
						textContent: null,
						images: [],
						novelInfo: {
							title: '猫又的奇幻之旅',
							description: '猫又发现了一个通往神秘世界的传送门。',
							rating: '4.8'
						},
						novelCover: '/static/novel-cover.png',
						likes: 25,
						dislikes: 8,
						favorites: 15,
						comments: 30,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					{
						id: 6,
						username: '猫又屋之主',
						timeAgo: '3天前',
						contentType: '图片',
						textContent: null,
						images: ['/static/image3.jpg', '/static/image4.jpg'],
						novelInfo: null,
						novelCover: null,
						likes: 18,
						dislikes: 4,
						favorites: 10,
						comments: 15,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					}
				],


			}
		},
		onLoad(options) {
			this.detectDevice();
			// 从页面参数获取用户ID
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			// 如果传入了好友名称，则设置为导航栏标题
			if (options.friendName) {
				this.friendName = decodeURIComponent(options.friendName);
				// 动态设置导航栏标题
				uni.setNavigationBarTitle({
					title: this.friendName
				});
			}
			console.log('页面加载，用户ID:', this.userId);
			// 加载数据
			this.loadUserProfileData();
			// 加载动态数据
			this.loadUserDynamicData();
		},
		methods: {
			// 加载用户个人主页数据
			async loadUserProfileData() {
				try {
					console.log('=== 开始加载用户个人主页数据 ===');
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `${this.$baseUrl}/api/user-profile/data?userId=${this.userId}`);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-profile/data?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const data = response.data.data;
						this.userInfo = data.userInfo || {};
						this.dynamicStats = data.dynamicStats || {};
						this.collectionStats = data.collectionStats || {};
						
						// 更新签名
						if (this.userInfo.bio) {
							this.signatureText = this.userInfo.bio;
						}
						
						console.log('成功加载用户个人主页数据');
						console.log('用户信息:', this.userInfo);
						console.log('动态统计:', this.dynamicStats);
						console.log('收藏统计:', this.collectionStats);
					} else {
						console.error('加载用户个人主页数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
					}
				} catch (error) {
					console.error('加载用户个人主页数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
				} finally {
					this.loading = false;
				}
			},
			
			// 加载用户动态数据
			async loadUserDynamicData() {
				try {
					console.log('=== 开始加载用户动态数据 ===');
					console.log('用户ID:', this.userId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-profile/dynamic?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 动态数据API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const data = response.data.data;
						this.personalPosts = data.posts || [];
						
						console.log('成功加载用户动态数据，共', this.personalPosts.length, '条');
					} else {
						console.error('加载用户动态数据失败:', response.data.message);
					}
				} catch (error) {
					console.error('加载用户动态数据异常:', error);
				}
			},
			
			// 加载用户收藏数据
			async loadUserCollectionsData() {
				try {
					console.log('=== 开始加载用户收藏数据 ===');
					console.log('用户ID:', this.userId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-profile/collections?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 收藏数据API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const data = response.data.data;
						this.collectedPosts = data.collectedPosts || [];
						
						console.log('成功加载用户收藏数据，共', this.collectedPosts.length, '条');
					} else {
						console.error('加载用户收藏数据失败:', response.data.message);
					}
				} catch (error) {
					console.error('加载用户收藏数据异常:', error);
				}
			},
			
			// 测试后端连接
			async testConnection() {
				try {
					console.log('=== 测试后端连接 ===');
					const response = await uni.request({
						url: this.$baseUrl+'/api/user-profile/test',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('测试连接响应:', response);
					
					if (response.statusCode === 200) {
						uni.showToast({
							title: '后端连接正常',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: '后端连接失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('测试连接异常:', error);
					uni.showToast({
						title: '连接异常',
						icon: 'error'
					});
				}
			},
			
			detectDevice() {
				// 检测是否为移动设备
				const userAgent = navigator.userAgent.toLowerCase();
				this.isMobile = /mobile|android|iphone|ipad|phone/i.test(userAgent);
			},
			editSignature() {
				this.isEditing = true;
				this.charCount = 0; // Reset char count when starting to edit
			},
			saveSignature() {
				this.isEditing = false;
				// In a real app, you would save this.signatureText to a backend or local storage
				console.log('Signature saved:', this.signatureText);
			},
			updateCharCount(event) {
				this.charCount = event.detail.value.length;
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/chat-feed'
				});
			},
			goToAIList() {
				uni.navigateTo({
					url: '/pages/ai/ai-chat'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
				});
			},
			openMenu() {
				this.showSideMenu = true;
			},
			openShare() {
				this.showShare = true;
			},
			closeShare() {
				this.showShare = false;
			},
			goToProfile() {
				// 已在个人资料页面，无需跳转
				uni.showToast({
					title: '已在个人资料页面',
					icon: 'none'
				});
			},
			closeSideMenu() {
				this.showSideMenu = false;
			},
			goToMyAI() {
				uni.navigateTo({
					url: '/pages/ai/ai'
				});
			},
			goToMyCertification() {
				uni.showToast({
					title: '我的认证功能',
					icon: 'none'
				});
			},
			goToMyPreferences() {
				uni.navigateTo({
					url: `/pages/feed/likes-dislikes?userId=${this.userId}`
				});
			},
			goToMyAchievements() {
				uni.navigateTo({
					url: '/pages/feed/achievements'
				});
			},
			goToAlbum() {
				uni.showToast({
					title: '相册功能',
					icon: 'none'
				});
			},
			goToHistoryComments() {
				uni.navigateTo({
					url: `/pages/feed/my-comments?userId=${this.userId}`
				});
			},
			goToBrowsingHistory() {
				uni.navigateTo({
				        url: `/pages/browsing-history/browsing-history?userId=${this.userId}`
				});
			},
			goToDraftBox() {
				uni.showToast({
					title: '草稿箱功能',
					icon: 'none'
				});
			},
			goToPurchaseRecord() {
				uni.navigateTo({
					url: '/pages/purchase-record/purchase-record'
				});
			},
			goToShoppingCart() {
				uni.showToast({
					title: '购物车功能',
					icon: 'none'
				});
			},
			goToWallet() {
				uni.navigateTo({
					url: '/pages/wallet/wallet'
				});
			},
			goToEditProfile() {
				uni.showToast({
					title: '编辑资料功能',
					icon: 'none'
				});
			},
			
			// 分享相关方法
			shareToWechat() {
				uni.showToast({
					title: '分享到微信',
					icon: 'none'
				});
				this.closeShare();
			},
			
			shareToMoments() {
				uni.showToast({
					title: '分享到朋友圈',
					icon: 'none'
				});
				this.closeShare();
			},
			
			shareToDouyin() {
				uni.showToast({
					title: '分享到抖音好友',
					icon: 'none'
				});
				this.closeShare();
			},
			
			shareToDouyinPost() {
				uni.showToast({
					title: '发布到抖音',
					icon: 'none'
				});
				this.closeShare();
			},
			
			shareToQQ() {
				uni.showToast({
					title: '分享到QQ',
					icon: 'none'
				});
				this.closeShare();
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
				this.closeShare();
			},
			
			shareToFriend(friend) {
				uni.showToast({
					title: `分享给${friend.name}`,
					icon: 'none'
				});
				this.closeShare();
			},
			
			handlePostClick(post) {
				// 如果是小说类型，跳转到小说详情页
				if (post.novelInfo) {
					uni.navigateTo({
						url: `/pages/feed/novel-detail?id=${post.id}&title=${encodeURIComponent(post.novelInfo.title)}&author=${encodeURIComponent(post.username)}`
					});
				} else {
					// 其他类型内容跳转到普通帖子详情页
					// 传递postId参数和isOwnPost标识，让post-detail.vue能够正确加载对应的帖子数据并控制关注按钮显示
					uni.navigateTo({
						url: `/pages/feed/post-detail?postId=${post.id}&isOwnPost=true`
					});
				}
			},
			
			// handleLike(post) {
			// 	post.isLiked = !post.isLiked;
			// 	post.likes = post.isLiked ? post.likes + 1 : post.likes - 1;
			// 	uni.showToast({
			// 		title: post.isLiked ? '点赞成功' : '取消点赞',
			// 		icon: 'none'
			// 	});
			// },
			
			// handleDislike(post) {
			// 	post.isDisliked = !post.isDisliked;
			// 	post.dislikes = post.isDisliked ? post.dislikes + 1 : post.dislikes - 1;
			// 	uni.showToast({
			// 		title: post.isDisliked ? '点踩成功' : '取消点踩',
			// 		icon: 'none'
			// 	});
			// },
			handleLike(post) {
			  // 1. 准备请求参数（与后端接口匹配）
			  const params = {
			    postId: post.id, // 帖子ID（必传）
			    postType: post.type, // 帖子类型（必传：text/image）
			    userId: 1000100 // 当前用户ID（后端默认值是1000100，实际应从登录状态获取）
			  };
			console.log('后端基础地址：', this.$baseUrl)
			  // 2. 发送POST请求到后端点赞接口
			  uni.request({
			    url: this.$baseUrl + '/api/post-detail/like', // 完整请求地址
			    method: 'POST',
			    data: params, // 请求体参数
			    header: {
			      'Content-Type': 'application/json' // 告诉后端数据格式为JSON
			    },
			    success: (res) => {
			      // 3. 处理后端响应
			      if (res.statusCode === 200 && res.data.success) {
			        // 后端处理成功：更新前端状态（使用后端返回的最新数据更可靠）
			        const result = res.data;
					console.log('点赞处理结果：', result);
			        post.isLiked = result.isLiked; // 后端返回的最新点赞状态（true/false）
			        post.likes = result.likes; // 后端返回的最新点赞数
			        // 如果点赞时需要取消点踩，同步更新点踩状态
			        if (post.isDisliked) {
			          post.isDisliked = false;
			          post.dislikes = result.dislikes || post.dislikes - 1;
			        }
			        uni.showToast({
			          title: result.isLiked ? '点赞成功' : '取消点赞',
			          icon: 'none'
			        });
			      } else {
			        // 后端处理失败：提示错误信息
			        uni.showToast({
			          title: res.data.message || '点赞失败',
			          icon: 'error'
			        });
			      }
			    },
			    fail: (err) => {
			      // 网络错误处理
			      console.error('点赞请求失败：', err);
			      uni.showToast({
			        title: '网络错误，请重试',
			        icon: 'error'
			      });
			    }
			  });
			},
			
			handleDislike(post) {
			  // 1. 准备请求参数
			  const params = {
			    postId: post.id,
			    postType: post.type,
			    userId: 1000100 // 实际应从登录状态获取
			  };
			
			  // 2. 发送POST请求到后端点踩接口
			  uni.request({
			    url: this.$baseUrl + '/api/post-detail/dislike',
			    method: 'POST',
			    data: params,
			    header: {
			      'Content-Type': 'application/json'
			    },
			    success: (res) => {
			      if (res.statusCode === 200 && res.data.success) {
			        // 后端处理成功：更新前端状态
			        const result = res.data;
			        post.isDisliked = result.isDisliked; // 后端返回的最新点踩状态
			        post.dislikes = result.dislikes; // 后端返回的最新点踩数
			        // 如果点踩时需要取消点赞，同步更新点赞状态
			        if (post.isLiked) {
			          post.isLiked = false;
			          post.likes = result.likes || post.likes - 1;
			        }
			        uni.showToast({
			          title: result.isDisliked ? '点踩成功' : '取消点踩',
			          icon: 'none'
			        });
			      } else {
			        uni.showToast({
			          title: res.data.message || '点踩失败',
			          icon: 'error'
			        });
			      }
			    },
			    fail: (err) => {
			      console.error('点踩请求失败：', err);
			      uni.showToast({
			        title: '网络错误，请重试',
			        icon: 'error'
			      });
			    }
			  });
			},
			
			// handleFavorite(post) {
			// 	post.isFavorited = !post.isFavorited;
			// 	post.favorites = post.isFavorited ? post.favorites + 1 : post.favorites - 1;
			// 	uni.showToast({
			// 		title: post.isFavorited ? '收藏成功' : '取消收藏',
			// 		icon: 'none'
			// 	});
			// },
			
			// handleComment(post) {
			// 	uni.navigateTo({
			// 		url: `/pages/comment/comment?postId=${post.id}`
			// 	});
			// },
			// 收藏/取消收藏功能
			handleFavorite(post) {
			  // 1. 准备请求参数
			  const params = {
			    postId: post.id,
			    postType: post.contentType === '小说' ? 'novel' : 'post', // 根据内容类型区分
			    userId: 1000100 // 实际应从登录状态获取
			  };
			
			  // 2. 发送POST请求到后端收藏接口
			  uni.request({
			    url: this.$baseUrl + '/api/post-detail/favorite',
			    method: 'POST',
			    data: params,
			    header: {
			      'Content-Type': 'application/json'
			    },
			    success: (res) => {
			      if (res.statusCode === 200 && res.data.success) {
			        // 后端处理成功：更新前端状态
			        const result = res.data;
			        post.isFavorited = result.isFavorited;
			        post.favorites = result.favorites;
			        
			        // 显示操作结果
			        uni.showToast({
			          title: result.isFavorited ? '收藏成功' : '取消收藏',
			          icon: 'none'
			        });
			      } else {
			        // 后端处理失败
			        uni.showToast({
			          title: res.data.message || '操作失败',
			          icon: 'error'
			        });
			      }
			    },
			    fail: (err) => {
			      console.error('收藏请求失败：', err);
			      uni.showToast({
			        title: '网络错误，请重试',
			        icon: 'error'
			      });
			    }
			  });
			},
			
			// 评论功能
			handleComment(post) {
			  // 1. 验证帖子ID有效性
			  if (!post.id) {
			    uni.showToast({
			      title: '帖子ID无效',
			      icon: 'error'
			    });
			    return;
			  }
			
			  // 2. 跳转到评论页并传递必要参数
			  uni.navigateTo({
			    url: `/pages/feed/my-comments?postId=${post.id}&postType=${post.contentType === '小说' ? 'novel' : 'post'}`,
			    success: () => {
			      console.log('成功跳转到评论页');
			    },
			    fail: (err) => {
			      console.error('跳转评论页失败：', err);
			      uni.showToast({
			        title: '打开评论失败',
			        icon: 'error'
			      });
			    }
			  });
			},
			
			formatCount(count) {
				if (count >= 1000000) {
					return (count / 1000000).toFixed(1) + 'M';
				} else if (count >= 10000) {
					return (count / 1000).toFixed(1) + 'K';
				}
				return count;
			},
			
			getImageGridClass(imageCount) {
				if (imageCount === 1) return 'single-image';
				if (imageCount === 2) return 'two-images';
				if (imageCount === 3) return 'three-images';
				if (imageCount === 4) return 'four-images';
				return 'grid-images';
			},
			
			previewImage(images, currentIndex) {
				uni.previewImage({
					urls: images,
					current: currentIndex,
					indicator: 'number'
				});
			},
			

			switchTab(tabName) {
				console.log('切换到标签:', tabName);
				this.activeTab = tabName;
				console.log('当前激活标签:', this.activeTab);
				
				// 根据标签加载对应数据
				if (tabName === 'dynamic') {
					this.loadUserDynamicData();
				} else if (tabName === 'collections') {
					this.loadUserCollectionsData();
				}
			},
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
		background-color: transparent;
		z-index: 1000;
		position: relative;
	}
	
	.time {
		font-size: 32rpx;
		font-weight: 600;
		color: #fff;
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
		background-color: #fff;
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
		color: #fff;
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid #fff;
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: #fff;
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: #fff;
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 头部区域 */
	.header {
		position: relative;
		height: 400rpx;
		overflow: hidden;
	}
	
	.blur-background {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		filter: blur(10rpx);
	}
	
	.pizza-image {
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #FF6B35, #F7931E);
		background-image: 
			radial-gradient(circle at 30% 30%, #FFD700, transparent 20%),
			radial-gradient(circle at 70% 40%, #FFD700, transparent 25%),
			radial-gradient(circle at 50% 70%, #FFD700, transparent 30%);
	}
	
	.header-buttons {
		position: absolute;
		top: 60rpx;
		left: 0;
		right: 0;
		display: flex;
		justify-content: space-between;
		padding: 0 40rpx;
		z-index: 10;
	}
	
	.menu-button, .share-button {
		width: 80rpx;
		height: 80rpx;
		background-color: rgba(255, 255, 255, 0.9);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.menu-icon {
		display: flex;
		flex-direction: column;
		gap: 6rpx;
	}
	
	.menu-line {
		width: 32rpx;
		height: 4rpx;
		background-color: #333;
		border-radius: 2rpx;
	}
	
	.share-icon {
		font-size: 40rpx;
	}
	
	/* 用户信息卡片 */
	.user-card {
		position: absolute;
		top: 300rpx;
		left: 50%;
		transform: translateX(-50%);
		background-color: #fff;
		border-radius: 20rpx;
		padding: 40rpx;
		box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
		z-index: 20;
		width: 600rpx;
		text-align: center;
	}
	
	.avatar {
		margin-bottom: 30rpx;
	}
	
	.cat-avatar {
		width: 120rpx;
		height: 120rpx;
		margin: 0 auto;
		position: relative;
	}
	
	.cat-body {
		width: 100rpx;
		height: 80rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 50rpx;
		position: absolute;
		top: 20rpx;
		left: 10rpx;
	}
	
	.cat-ears {
		position: absolute;
		top: 0;
		left: 50%;
		transform: translateX(-50%);
		display: flex;
		gap: 20rpx;
	}
	
	.ear {
		width: 20rpx;
		height: 30rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 50% 50% 0 0;
	}
	
	.cat-face {
		position: absolute;
		top: 25rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 60rpx;
		height: 50rpx;
	}
	
	.cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 10rpx;
	}
	
	.cat-eye {
		width: 8rpx;
		height: 8rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.cat-nose {
		width: 6rpx;
		height: 6rpx;
		background-color: #FF69B4;
		border-radius: 50%;
		margin: 0 auto 8rpx;
	}
	
	.cat-mouth {
		width: 20rpx;
		height: 8rpx;
		border: 2rpx solid #000;
		border-top: none;
		border-radius: 0 0 20rpx 20rpx;
		margin: 0 auto;
	}
	
	.user-info {
		display: flex;
		flex-direction: column;
		gap: 15rpx;
	}
	
	.username {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
	}
	
	.location {
		font-size: 28rpx;
		color: #666;
	}
	
	.vip-info {
		display: flex;
		justify-content: center;
		gap: 10rpx;
	}
	
	.vip-text {
		font-size: 24rpx;
		color: #FF69B4;
		font-weight: bold;
	}
	
	.exp-text {
		font-size: 24rpx;
		color: #999;
	}
	
	.signature {
		font-size: 24rpx;
		color: #999;
		font-style: italic;
	}
	
	/* 用户签名区域 */
	.signature-area {
		position: absolute;
		top: 450rpx; /* Adjust based on user-card height */
		left: 50%;
		transform: translateX(-50%);
		z-index: 20;
		width: 600rpx;
		text-align: center;
		cursor: pointer; /* Added for clickability */
	}
	
	.signature-input-container {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 10rpx;
	}
	
	.signature-text {
		font-size: 24rpx;
		color: #999;
		font-style: italic;
	}
	
	.edit-hint {
		font-size: 20rpx;
		color: #999;
		opacity: 0.7;
	}
	
	.signature-input {
		font-size: 24rpx;
		color: #333;
		padding: 10rpx 20rpx;
		border: 1rpx solid #e0e0e0;
		border-radius: 10rpx;
		background-color: #fff;
		text-align: center;
		width: 400rpx;
	}
	
	.char-count {
		font-size: 20rpx;
		color: #999;
	}
	
	/* 虚拟键盘 */
	.virtual-keyboard {
		position: fixed;
		bottom: 120rpx; /* Adjust based on bottom-nav height */
		left: 0;
		right: 0;
		background-color: #f0f0f0;
		padding: 20rpx;
		border-top: 1rpx solid #e0e0e0;
		z-index: 100;
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
	
	.keyboard-row {
		display: flex;
		justify-content: space-around;
		gap: 10rpx;
	}
	
	.key {
		width: 80rpx;
		height: 80rpx;
		background-color: #fff;
		border: 1rpx solid #ccc;
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 36rpx;
		color: #333;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.key:active {
		background-color: #e0e0e0;
	}
	
	.shift-key {
		width: 100rpx; /* Make shift key wider */
	}
	
	.backspace-key {
		width: 100rpx; /* Make backspace key wider */
	}
	
	.symbol-key {
		width: 120rpx; /* Make symbol key wider */
	}
	
	.comma-key {
		width: 100rpx; /* Make comma key wider */
	}
	
	.emoji-key {
		width: 100rpx; /* Make emoji key wider */
	}
	
	.space-key {
		width: 200rpx; /* Make space key wider */
	}
	
	.period-key {
		width: 100rpx; /* Make period key wider */
	}
	
	.enter-key {
		width: 100rpx; /* Make enter key wider */
	}
	
	.key-text {
		font-size: 36rpx;
	}
	
	/* 导航标签 */
	.nav-tabs {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx 0 20rpx;
		gap: 60rpx;
		position: relative;
		margin-top: 250rpx;
	}
	
	.tab {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
		position: relative;
		cursor: pointer;
		padding: 20rpx;
		min-width: 80rpx;
		text-align: center;
		user-select: none;
		-webkit-user-select: none;
	}
	
	.tab-text {
		font-size: 32rpx;
		color: #999;
	}
	
	.tab.active .tab-text {
		color: #FF69B4;
		font-weight: bold;
	}
	
	.tab:active {
		transform: scale(0.95);
		transition: transform 0.1s ease;
		background-color: rgba(255, 105, 180, 0.1);
		border-radius: 10rpx;
	}
	
	.tab-underline {
		width: 40rpx;
		height: 6rpx;
		background-color: #FF69B4;
		border-radius: 3rpx;
	}
	
	.search-icon {
		position: absolute;
		right: 40rpx;
		font-size: 32rpx;
		color: #999;
	}
	
	/* 标签面板 */
	.tab-panel {
		width: 100%;
	}
	
	/* 内容区域 */
	.content-area {
		width: 100%;
		padding: 24rpx;
		margin-bottom: 140rpx; /* 为底部导航栏留出空间 */
		box-sizing: border-box;
	}
	
	/* 动态内容样式 */
	.post-item {
		background-color: #fff;
		border-radius: 16rpx;
		margin-bottom: 24rpx;
		padding: 24rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
		box-sizing: border-box;
		width: 100%;
	}
	
	.post-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}
	
	.post-avatar {
		width: 60rpx;
		height: 60rpx;
		margin-right: 16rpx;
	}
	
	.small-cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.small-cat-body {
		width: 50rpx;
		height: 40rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 25rpx;
		position: absolute;
		top: 10rpx;
		left: 5rpx;
	}
	
	.small-cat-face {
		position: absolute;
		top: 12rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 30rpx;
		height: 25rpx;
	}
	
	.small-cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 5rpx;
	}
	
	.small-cat-eye {
		width: 4rpx;
		height: 4rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.post-info {
		flex: 1;
	}
	
	.post-username {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
		display: block;
		margin-bottom: 4rpx;
	}
	
	.post-time {
		font-size: 24rpx;
		color: #999;
	}
	
	.post-content {
		margin-bottom: 20rpx;
	}
	
	.content-type {
		margin-bottom: 16rpx;
	}
	
	.type-label {
		font-size: 26rpx;
		color: #666;
		background-color: #f0f0f0;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.text-content {
		margin-bottom: 16rpx;
	}
	
	.post-title {
		font-size: 30rpx;
		color: #333;
		line-height: 1.5;
		font-weight: 500;
	}
	
	.novel-info {
		margin-bottom: 16rpx;
	}
	
	.novel-desc {
		font-size: 26rpx;
		color: #666;
		margin: 8rpx 0;
		display: block;
		line-height: 1.4;
	}
	
	.novel-rating {
		font-size: 24rpx;
		color: #FF6B35;
		font-weight: 600;
	}
	
	.image-content {
		margin-bottom: 16rpx;
	}
	
	.image-grid {
		display: flex;
		gap: 12rpx;
		flex-wrap: wrap;
	}
	
	.content-image {
		border-radius: 8rpx;
		background-color: #f5f5f5;
	}
	
	.single-image .content-image {
		width: 100%;
		height: 300rpx;
	}
	
	.two-images .content-image {
		width: calc(50% - 6rpx);
		height: 200rpx;
	}
	
	.three-images .content-image {
		width: calc(33.33% - 8rpx);
		height: 150rpx;
	}
	
	.four-images .content-image {
		width: calc(50% - 6rpx);
		height: 120rpx;
	}
	
	.grid-images .content-image {
		width: calc(33.33% - 8rpx);
		height: 120rpx;
	}
	
	.main-image {
		position: relative;
		margin-bottom: 16rpx;
	}
	
	.novel-cover {
		width: 100%;
		height: 300rpx;
		border-radius: 12rpx;
		background-color: #f5f5f5;
	}
	
	.image-overlay {
		position: absolute;
		bottom: 16rpx;
		left: 16rpx;
		right: 16rpx;
		background: linear-gradient(135deg, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
		padding: 16rpx;
		border-radius: 8rpx;
	}
	
	.overlay-title {
		font-size: 28rpx;
		color: #fff;
		font-weight: 600;
	}
	
	.engagement-metrics {
		display: flex;
		justify-content: space-around;
		border-top: 1rpx solid #f0f0f0;
		padding-top: 16rpx;
	}
	
	.metric-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
		transition: background-color 0.2s ease;
	}
	
	.metric-item:active {
		background-color: #f5f5f5;
	}
	
	.metric-icon {
		font-size: 28rpx;
		color: #666;
		transition: all 0.2s ease;
	}
	
	.metric-count {
		font-size: 24rpx;
		color: #666;
		font-weight: 500;
	}
	
	.metric-icon.active,
	.metric-count.active {
		color: #FFD700;
	}
	
	.metric-icon.active {
		transform: scale(1.1);
	}
	
	/* 收藏内容样式 */
	.collections-content {
		padding: 20rpx;
		background-color: #fff;
	}
	
	.stats-row {
		display: flex;
		justify-content: space-around;
		padding: 30rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
		margin-bottom: 30rpx;
		background-color: #fafafa;
		border-radius: 15rpx;
	}
	
	.stats-item {
		font-size: 28rpx;
		color: #666;
	}
	
	.collection-category {
		margin-bottom: 40rpx;
	}
	
	.category-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx 0;
		margin-bottom: 25rpx;
		border-bottom: 1rpx solid #f5f5f5;
	}
	
	.category-title {
		font-size: 34rpx;
		color: #333;
		font-weight: 600;
	}
	
	.category-subtitle {
		font-size: 26rpx;
		color: #999;
		background-color: #f0f0f0;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.category-images {
		display: flex;
		gap: 20rpx;
		flex-wrap: wrap;
	}
	
	.image-item {
		width: 120rpx;
		height: 120rpx;
		border-radius: 12rpx;
		background-color: #f0f0f0;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		transition: transform 0.2s ease, box-shadow 0.2s ease;
	}
	
	.image-item:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.15);
	}
	
	/* 披萨图片样式 */
	.pizza-slice-1 {
		background: linear-gradient(135deg, #FF6B35, #FF8C42);
	}
	
	.pizza-slice-2 {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}
	
	/* momo图片样式 */
	.momo-1 {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	.momo-2 {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	.momo-3 {
		background: linear-gradient(135deg, #87CEEB, #98FB98);
	}
	
	.momo-4 {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
	}
	
	/* 明星图片样式 */
	.celebrity-1 {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}
	
	.celebrity-2 {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}
	
	.celebrity-3 {
		background: linear-gradient(135deg, #FF69B4, #FF1493);
	}
	
	/* 披萨图片样式 */
	.pizza-slice-3 {
		background: linear-gradient(135deg, #FF4500, #FF6347);
	}
	
	/* momo图片样式 */
	.momo-5 {
		background: linear-gradient(135deg, #DDA0DD, #D8BFD8);
	}
	
	.momo-6 {
		background: linear-gradient(135deg, #F0E68C, #BDB76B);
	}
	
	/* 美食图片样式 */
	.food-1 {
		background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
	}
	
	.food-2 {
		background: linear-gradient(135deg, #4ECDC4, #44A08D);
	}
	
	.food-3 {
		background: linear-gradient(135deg, #45B7D1, #96CEB4);
	}
	
	.food-4 {
		background: linear-gradient(135deg, #FFA07A, #FF7F50);
	}
	
	/* 旅行图片样式 */
	.travel-1 {
		background: linear-gradient(135deg, #87CEEB, #4682B4);
	}
	
	.travel-2 {
		background: linear-gradient(135deg, #98FB98, #32CD32);
	}
	
	.travel-3 {
		background: linear-gradient(135deg, #DDA0DD, #9370DB);
	}
	
	.travel-4 {
		background: linear-gradient(135deg, #F0E68C, #BDB76B);
	}
	
	.travel-5 {
		background: linear-gradient(135deg, #FFB6C1, #FF69B4);
	}
	
	/* 艺术图片样式 */
	.art-1 {
		background: linear-gradient(135deg, #FF1493, #C71585);
	}
	
	.art-2 {
		background: linear-gradient(135deg, #00CED1, #008B8B);
	}
	
	.art-3 {
		background: linear-gradient(135deg, #FFD700, #FFA500);
	}
	
	/* 赞过内容样式 */
	.liked-content {
		padding: 40rpx;
		text-align: center;
		background-color: #fff;
	}
	
	.content-placeholder {
		font-size: 28rpx;
		color: #999;
	}
	
	/* 收藏底部提示样式 */
	.collections-footer {
		text-align: center;
		padding: 40rpx 0;
		border-top: 1rpx solid #f0f0f0;
		margin-top: 40rpx;
	}
	
	.footer-text {
		font-size: 28rpx;
		color: #999;
		opacity: 0.8;
	}
	
	/* 分享弹层样式 */
	.share-overlay {
		position: fixed;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		background: rgba(0,0,0,.4);
		display: flex;
		align-items: flex-end;
		z-index: 999;
	}
	
	.share-sheet {
		width: 100%;
		background: #fff;
		border-top-left-radius: 24rpx;
		border-top-right-radius: 24rpx;
		padding: 24rpx 24rpx 16rpx;
		box-shadow: 0 -8rpx 24rpx rgba(0,0,0,.08);
	}
	
	.sheet-handle {
		width: 100rpx;
		height: 8rpx;
		background: #E6E6E6;
		border-radius: 4rpx;
		margin: 0 auto 16rpx;
	}
	
	.sheet-title {
		display: block;
		text-align: center;
		font-size: 28rpx;
		color: #333;
		margin-bottom: 12rpx;
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
	
	.share-icon-bg.wechat {
		background-color: #07C160;
	}
	
	.share-icon-bg.moments {
		background-color: #07C160;
	}
	
	.share-icon-bg.douyin {
		background-color: #000000;
	}
	
	.share-icon-bg.douyin-post {
		background-color: #000000;
	}
	
	.share-icon-bg.qq {
		background-color: #12B7F5;
	}
	
	.share-icon-img {
		width: 48rpx;
		height: 48rpx;
	}
	
	/* SVG图标样式 */
	.share-icon-bg svg {
		width: 32rpx;
		height: 32rpx;
	}
	
	.share-option-text {
		font-size: 24rpx;
		color: #333333;
	}
	
	.share-actions {
		display: flex;
		justify-content: space-around;
		padding: 20rpx 0;
		margin-bottom: 20rpx;
		border-top: 1rpx solid #F0F0F0;
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
		background-color: #F5F5F5;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}
	
	.action-text {
		font-size: 24rpx;
		color: #333333;
	}
	
	/* 好友分享列表 */
	.share-friends {
		padding: 20rpx 0;
		margin-bottom: 20rpx;
		border-top: 1rpx solid #F0F0F0;
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
		background-color: #F5F5F5;
	}
	
	.friend-avatar-img {
		width: 100%;
		height: 100%;
	}
	
	.friend-name {
		font-size: 24rpx;
		color: #333333;
		max-width: 80rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	

	
	.sheet-cancel {
		margin-top: 8rpx;
	}
	
	.cancel-text {
		display: block;
		text-align: center;
		height: 92rpx;
		line-height: 92rpx;
		border-radius: 16rpx;
		background: #F7F7F7;
		color: #333;
		font-size: 30rpx;
	}
	
	.text-content {
		margin-bottom: 16rpx;
	}
	
	.post-title {
		font-size: 30rpx;
		color: #333;
		line-height: 1.5;
		font-weight: 500;
	}
	
	.novel-info {
		margin-bottom: 16rpx;
	}
	
	.novel-desc {
		font-size: 26rpx;
		color: #666;
		margin: 8rpx 0;
		display: block;
		line-height: 1.4;
	}
	
	.novel-rating {
		font-size: 24rpx;
		color: #FF6B35;
		font-weight: 600;
	}
	
	.image-content {
		margin-bottom: 16rpx;
	}
	
	.image-grid {
		display: flex;
		gap: 12rpx;
		flex-wrap: wrap;
	}
	
	.content-image {
		border-radius: 8rpx;
		background-color: #f5f5f5;
	}
	
	.single-image .content-image {
		width: 100%;
		height: 300rpx;
	}
	
	.two-images .content-image {
		width: calc(50% - 6rpx);
		height: 200rpx;
	}
	
	.three-images .content-image {
		width: calc(33.33% - 8rpx);
		height: 150rpx;
	}
	
	.four-images .content-image {
		width: calc(50% - 6rpx);
		height: 120rpx;
	}
	
	.grid-images .content-image {
		width: calc(33.33% - 8rpx);
		height: 120rpx;
	}
	
	.main-image {
		position: relative;
		margin-bottom: 16rpx;
	}
	
	.novel-cover {
		width: 100%;
		height: 300rpx;
		border-radius: 12rpx;
		background-color: #f5f5f5;
	}
	
	.image-overlay {
		position: absolute;
		bottom: 16rpx;
		left: 16rpx;
		right: 16rpx;
		background: linear-gradient(135deg, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
		padding: 16rpx;
		border-radius: 8rpx;
	}
	
	.overlay-title {
		font-size: 28rpx;
		color: #fff;
		font-weight: 600;
	}
	
	.engagement-metrics {
		display: flex;
		justify-content: space-around;
		border-top: 1rpx solid #f0f0f0;
		padding-top: 16rpx;
	}
	
	.metric-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
		transition: background-color 0.2s ease;
	}
	
	.metric-item:active {
		background-color: #f5f5f5;
	}
	
	.metric-icon {
		font-size: 28rpx;
		color: #666;
		transition: all 0.2s ease;
	}
	
	.metric-count {
		font-size: 24rpx;
		color: #666;
		font-weight: 500;
	}
	
	.metric-icon.active,
	.metric-count.active {
		color: #FFD700;
	}
	
	.metric-icon.active {
		transform: scale(1.1);
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
	
	/* 左侧菜单面板样式 */
	.side-menu {
		position: fixed;
		top: 0;
		left: -100%;
		width: 100%;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		transition: left 0.3s ease;
	}
	
	.side-menu.active {
		left: 0;
	}
	
	.menu-content {
		position: absolute;
		top: 0;
		left: 0;
		width: 80%;
		max-width: 600rpx;
		height: 100%;
		background-color: #FFFFFF;
		overflow-y: auto;
	}
	
	.menu-header {
		display: flex;
		justify-content: flex-end;
		padding: 40rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.menu-close {
		width: 60rpx;
		height: 60rpx;
		background-color: #F5F5F5;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.close-icon {
		font-size: 32rpx;
		color: #666666;
		font-weight: 600;
	}
	
	.menu-list {
		padding: 40rpx;
	}
	
	.menu-section {
		margin-bottom: 40rpx;
	}
	
	.menu-item {
		padding: 24rpx 0;
		border-bottom: 1rpx solid #F5F5F5;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.menu-item:last-child {
		border-bottom: none;
	}
	
	.menu-item:active {
		background-color: #F5F5F5;
		padding-left: 20rpx;
	}
	
	.menu-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 40rpx;
		background-color: #fff;
		border-radius: 16rpx;
		margin: 24rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #FF69B4;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 20rpx;
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
		padding: 80rpx 40rpx;
		background-color: #fff;
		border-radius: 16rpx;
		margin: 24rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: #666;
		text-align: center;
		margin-bottom: 30rpx;
		line-height: 1.5;
	}
	
	.error-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.retry-button {
		padding: 16rpx 32rpx;
		background-color: #FF69B4;
		color: #fff;
		border: none;
		border-radius: 20rpx;
		font-size: 26rpx;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.retry-button:active {
		background-color: #FF1493;
	}
</style>
