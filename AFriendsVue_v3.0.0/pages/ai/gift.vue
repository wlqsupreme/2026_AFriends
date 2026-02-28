<template>
	<view class="gift-page">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="1053"></path>
				</svg>
			</view>
			<view class="header-title">
				<text class="title-text">模型信息</text>
			</view>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- AI 概览 -->
		<view class="ai-summary">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载AI信息中...</text>
			</view>
			
			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadAiModelData">
					<text class="retry-text">重试</text>
				</button>
			</view>
			
			<!-- 正常显示 -->
			<view v-else>
				<view class="avatar-wrap" @click="modifyPhoto">
					<image v-if="isValidImageUrl(aiAvatar)" class="avatar" :src="aiAvatar" mode="aspectFill" @error="onImageError"></image>
					<view v-else class="avatar avatar-placeholder">
						<text class="avatar-text">{{ getAvatarText() }}</text>
					</view>
					<view class="edit-overlay">
						<text>点击修改照片</text>
					</view>
				</view>
				<view class="ai-info">
					<view class="name-row">
						<view class="ai-name" @click="modifyName">
							<text>{{ aiName || 'AI小助手' }}</text>
							<view class="edit-icon">✏️</view>
						</view>
						<view class="perm-pill" @click="goToPermissions">
							<view class="perm-dot">!</view>
							<text class="perm-text">权限</text>
						</view>
					</view>
					<view class="level-row">
						<text class="level-text">Lv{{ level }}</text>
						<view class="level-up-btn" @click="goLevelUp"><text class="btn-text">等级提升</text></view>
					</view>
					<view class="progress-row">
						<view class="progress">
							<view class="track"></view>
							<view class="fill" :style="{ width: progressPercent + '%' }"></view>
							<!-- <view class="thumb"></view> -->
						</view>
						<text class="progress-text">{{ currentExp }}/{{ nextExp }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 任务行 -->
		<view class="task-row" @click="goToTasks">
			<view class="task-left">
				<view class="task-icon">📋</view>
				<text class="task-text">任务</text>
			</view>
			<view class="task-right">
				<text class="task-count">{{ activeTaskCount }} 个进行中</text>
				<view class="task-arrow">></view>
			</view>
		</view>

		<!-- 礼物列表 -->
		<view class="gift-list">
			<view class="gift-header" @click="toggleGiftsList">
				<view class="header-left">
					<!-- 				<view class="back-arrow-svg" @click="goBack">
					<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
						<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="1053"></path>
					</svg>
				</view> -->
					<text class="header-title">礼物</text>
					<view class="dropdown-arrow" :class="{ 'rotated': showGiftsList }"></view>
				</view>
				<text class="header-subtitle">定价 (金币)</text>
			</view>
			
			<view class="gift-items" v-show="showGiftsList">
				<view class="gift-item" v-for="(gift, i) in gifts" :key="i">
					<view class="gift-left">
						<view class="gift-icon" :class="gift.iconClass"></view>
						<view class="gift-info">
							<text class="gift-name">{{ gift.name }}</text>
							<text class="gift-description">{{ gift.description }}</text>
						</view>
					</view>
					<view class="gift-right">
						<text class="gift-price">{{ gift.price }}</text>
						<view class="quantity-controls">
							<view class="control-btn minus" @click="decreaseGift(i)" :class="{ disabled: gift.count <= 0 }">-</view>
							<text class="quantity-display">{{ gift.count }}</text>
							<view class="control-btn plus" @click="increaseGift(i)">+</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部购物条 -->
		<view class="cart-bar">
			<text class="cart-total">已选礼物：{{ selectedGiftsCount }} 个</text>
			<view class="checkout-btn" @click="checkout"><text class="checkout-text">赠送</text></view>
		</view>

		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		name: 'GiftPage',
		data() {
			return {
				// AI模型信息
				userAiId: null,
				userId: null,
				aiName: '',
				aiAvatar: '/static/logo.png',
				level: 1,
				currentExp: 0,
				nextExp: 1000,
				power: 0,
				
				// 任务信息
				activeTaskCount: 0,
				
				// 礼物信息
				gifts: [],
				showGiftsList: true,
				
				// 等级规则信息
				levelRules: [],
				currentLevelRule: null,
				nextLevelRule: null,
				
				// 加载状态
				loading: false,
				errorMessage: ''
			}
		},
		onLoad(options) {
			console.log('gift页面加载，接收到的参数:', options);
			
			// 接收AI模型ID参数
			if (options.userAiId) {
				this.userAiId = parseInt(options.userAiId);
			}
			if (options.aiName) {
				this.aiName = decodeURIComponent(options.aiName);
			}
			
			// 如果没有传入aiName，使用userAiId作为默认名称
			if (!this.aiName && this.userAiId) {
				this.aiName = this.userAiId.toString();
			}
			
			// 获取用户ID
			this.userId = uni.getStorageSync('userId') || 1;
			
			// 加载AI模型数据、等级规则数据和礼物数据
			this.loadAiModelData();
			this.loadLevelRules();
			this.loadGiftData();
			this.loadActiveTaskCount();
		},
		
		// 页面显示时刷新数据（从level-up页面返回时会触发）
		onShow() {
			console.log('gift页面显示，刷新AI模型数据');
			// 刷新AI模型数据和等级规则数据
			this.loadAiModelData();
			this.loadLevelRules();
			this.loadActiveTaskCount();
		},
		computed: {
			progressPercent() {
				const p = Math.min(100, Math.max(0, (this.currentExp / this.nextExp) * 100));
				return Number(p.toFixed(2));
			},
			selectedGiftsCount() {
				return this.gifts.reduce((sum, g) => sum + g.count, 0);
			}
		},
		methods: {
			// 加载AI模型数据
			async loadAiModelData() {
				if (!this.userAiId) {
					console.log('没有userAiId，无法加载AI模型数据');
					return;
				}
				
				try {
					this.loading = true;
					console.log('开始加载AI模型数据，userAiId:', this.userAiId);
					
					// 首先尝试从用户AI模型接口获取
					let response = await uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/${this.userAiId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('用户AI模型数据响应:', response);
					
					// 如果用户AI模型不存在，尝试从基础模型获取
					if (response.statusCode !== 200 || !response.data) {
						console.log('用户AI模型不存在，尝试从基础模型获取');
						response = await uni.request({
							url: `${this.$baseUrl}/api/a-entities/aimodel-base-info/${this.userAiId}`,
							method: 'GET',
							header: {
								'Content-Type': 'application/json'
							}
						});
						console.log('基础AI模型数据响应:', response);
					}
					
					if (response.statusCode === 200 && response.data) {
						const modelData = response.data;
						// 只有在获取到有效数据时才更新名称，否则保持传入的名称
						if (modelData.modelName || modelData.name) {
							this.aiName = modelData.modelName || modelData.name;
						}
						
						// 验证图片URL是否有效
						const imageUrl = modelData.modelImageUrl || modelData.imageUrl;
						if (imageUrl && this.isValidImageUrl(imageUrl)) {
							this.aiAvatar = imageUrl;
						} else {
							// 如果图片URL无效，使用默认头像
							this.aiAvatar = '';
							console.log('AI模型图片URL无效，使用默认头像:', imageUrl);
						}
						
						this.level = modelData.level || 1;
						this.currentExp = modelData.totalExp || modelData.exp || 0;
						this.power = modelData.power || 0;
						
						// 更新等级规则（如果已加载）
						this.updateLevelRules();
						
						console.log('AI模型数据加载成功:', {
							aiName: this.aiName,
							level: this.level,
							currentExp: this.currentExp,
							nextExp: this.nextExp
						});
					} else {
						console.error('加载AI模型数据失败:', response);
						// 即使加载失败，也保持传入的aiName，不显示错误
						console.log('保持传入的AI名称:', this.aiName);
					}
				} catch (error) {
					console.error('加载AI模型数据异常:', error);
					this.errorMessage = '网络错误，请检查连接';
				} finally {
					this.loading = false;
				}
			},
			
			// 加载等级规则数据
			async loadLevelRules() {
				try {
					console.log('=== 开始加载等级规则数据 ===');
					
					const response = await uni.request({
						url: this.$baseUrl+'/api/aimodel-level-rule/all-ordered',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('等级规则数据响应:', response);
					console.log('响应状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						this.levelRules = response.data.data;
						console.log('等级规则数据加载成功，数量:', this.levelRules.length);
						console.log('前3条等级规则数据:', this.levelRules.slice(0, 3));
						
						// 更新当前等级规则和下一级规则
						this.updateLevelRules();
					} else {
						console.error('加载等级规则数据失败:', response);
						console.error('失败原因:', response.data ? response.data.message : '未知错误');
						// 使用默认等级规则
						this.setDefaultLevelRules();
					}
				} catch (error) {
					console.error('加载等级规则数据异常:', error);
					console.error('异常详情:', error.message);
					// 使用默认等级规则
					this.setDefaultLevelRules();
				}
			},
			
			// 更新等级规则
			updateLevelRules() {
				console.log('=== 开始更新等级规则 ===');
				console.log('当前状态:', {
					levelRulesLength: this.levelRules.length,
					currentLevel: this.level,
					currentExp: this.currentExp
				});
				
				if (this.levelRules.length === 0) {
					console.warn('等级规则数据为空，使用默认规则');
					this.setDefaultLevelRules();
					return;
				}
				
				// 查找当前等级规则
				this.currentLevelRule = this.levelRules.find(rule => rule.level === this.level);
				console.log('当前等级规则查找结果:', this.currentLevelRule);
				
				// 直接使用当前等级的expRequirement作为nextExp
				if (this.currentLevelRule) {
					this.nextExp = this.currentLevelRule.expRequirement;
					console.log('设置nextExp为当前等级所需经验:', this.nextExp);
					console.log('等级规则说明: 等级', this.level, '需要', this.nextExp, '经验才能升级');
				} else {
					// 如果没有当前等级规则，说明数据有问题
					this.nextExp = 0;
					console.error('找不到当前等级', this.level, '的规则，设置nextExp为0');
				}
				
				console.log('等级规则更新完成:', {
					currentLevel: this.level,
					currentExp: this.currentExp,
					nextExp: this.nextExp,
					currentLevelRule: this.currentLevelRule,
					nextLevelRule: this.nextLevelRule
				});
			},
			
			// 设置默认等级规则
			setDefaultLevelRules() {
				console.log('使用默认等级规则 - 数据未正确加载');
				this.levelRules = [];
				this.currentLevelRule = null;
				this.nextLevelRule = null;
				// 不设置默认值，保持为0，这样可以看到数据加载问题
				this.nextExp = 0;
				console.warn('等级规则数据未加载，nextExp设置为0，请检查数据加载逻辑');
			},
			
			// 加载礼物数据（先调用load接口，再查询礼物列表）
			async loadGiftData() {
			    try {
			        console.log('开始加载礼物数据：第一步调用 /gift-base/load 接口加载到内存');
			        
			        // 第一步：先调用后端 load 接口，将礼物数据加载到内存
			        const loadResponse = await uni.request({
			            url: this.$baseUrl + '/api/efmprt-entities-wlq/gift-base/load', // 礼物加载接口
			            method: 'POST', // 加载接口通常为POST类型，若后端是GET则改为GET
			            header: { 'Content-Type': 'application/json',
								  'Accept-Charset': 'utf-8' ,// 明确要求后端返回UTF-8编码 
								},
			            timeout: 10000 // 超时时间10秒
			        });
			        
			        console.log('礼物加载接口响应:', loadResponse);
			        
					// 关键修改：调整成功判断逻辑（后端返回的message包含"成功"即认为加载成功）
					if (loadResponse.statusCode !== 200) {
						throw new Error("礼物数据加载到内存失败：接口状态码异常");
					}
					// 优化1：先判断message是否存在，再判断是否包含"成功"（避免null报错）
					// 优化2：添加备用判断（判断duration是否存在，后端成功时必返回）
					const isLoadSuccess = loadResponse.data?.message && loadResponse.data.message.includes('成功') 
										|| loadResponse.data?.duration !== undefined;
					if (!isLoadSuccess) {
						throw new Error("礼物数据加载到内存失败：" + (loadResponse.data?.message || '未知错误'));
					}
			        
			        console.log('礼物数据加载到内存成功，开始查询礼物列表');
			        
			        // 第二步：加载成功后，调用 /gift-base/all 接口查询礼物数据
			        const listResponse = await uni.request({
			            url: this.$baseUrl + '/api/efmprt-entities-wlq/gift-base/all',
			            method: 'GET',
			            header: { 'Content-Type': 'application/json',
							      'Accept-Charset': 'utf-8'},
			            timeout: 5000
			        });
			        
			        console.log('礼物列表接口响应:', listResponse);
			        
			        if (listResponse.statusCode === 200 && listResponse.data) {
			            this.gifts = this.convertGiftDataToDisplayFormat(listResponse.data);
			            console.log('礼物数据加载成功，数量:', this.gifts.length);
			            console.log('礼物详情:', this.gifts);
			        } else {
			            console.error('查询礼物列表失败:', listResponse);
			            this.errorMessage = '加载礼物列表失败';
			        }
			    } catch (error) {
			        console.error('加载礼物数据异常:', error);
			        this.errorMessage = '礼物数据加载失败，请重试';
			        uni.showToast({ title: this.errorMessage, icon: 'none' });
			    }
			},
			
			// // 加载礼物数据
			// async loadGiftData() {
			// 	try {
			// 		console.log('开始加载礼物数据');
					
			// 		const response = await uni.request({
			// 			url: this.$baseUrl+'/api/efmprt-entities-wlq/gift-base/all',
			// 			method: 'GET',
			// 			header: {
			// 				'Content-Type': 'application/json'
			// 			}
			// 		});
					
			// 		console.log('礼物数据响应:', response);
					
			// 		if (response.statusCode === 200 && response.data) {
			// 			this.gifts = this.convertGiftDataToDisplayFormat(response.data);
			// 			console.log('礼物数据加载成功，数量:', this.gifts.length);
			// 		} else {
			// 			console.error('加载礼物数据失败:', response);
			// 			this.errorMessage = '加载礼物数据失败';
			// 		}
			// 	} catch (error) {
			// 		console.error('加载礼物数据异常:', error);
			// 		this.errorMessage = '网络错误，请检查连接';
			// 	}
			// },
			
			// 转换礼物数据为显示格式
			convertGiftDataToDisplayFormat(giftData) {
				return giftData.map((gift, index) => {
					// 解析description字段：格式为"礼物名字：描述"（使用中文冒号）
					let name = '';
					let description = '';
					
					if (gift.description && gift.description.includes('：')) {
						const parts = gift.description.split('：', 2);
						name = parts[0].trim();
						description = parts[1].trim();
					} else if (gift.description && gift.description.includes(':')) {
						// 兼容英文冒号
						const parts = gift.description.split(':', 2);
						name = parts[0].trim();
						description = parts[1].trim();
					} else {
						name = gift.description || `礼物${index + 1}`;
						description = '暂无描述';
					}
					
					// 价格转换：人民币转金币（乘以100）
					const priceInGold = Math.round((gift.price || 0) * 100);
					
					// 根据礼物名称分配图标类
					const iconClass = this.getGiftIconClass(name);
					
					return {
						id: gift.giftId,
						name: name,
						description: description,
						price: priceInGold,
						count: 0,
						iconClass: iconClass
					};
				});
			},
			
			// 根据礼物名称获取图标类
			getGiftIconClass(name) {
				const nameLower = name.toLowerCase();
				if (nameLower.includes('花') || nameLower.includes('flower')) return 'gift-flower';
				if (nameLower.includes('啤酒') || nameLower.includes('beer')) return 'gift-beer';
				if (nameLower.includes('糖') || nameLower.includes('candy') || nameLower.includes('lollipop')) return 'gift-lollipop';
				if (nameLower.includes('圈') || nameLower.includes('donut')) return 'gift-donut';
				if (nameLower.includes('巧克力') || nameLower.includes('chocolate')) return 'gift-chocolate';
				if (nameLower.includes('心') || nameLower.includes('heart')) return 'gift-heart';
				if (nameLower.includes('发箍') || nameLower.includes('hairband')) return 'gift-hairband';
				if (nameLower.includes('游戏') || nameLower.includes('game')) return 'gift-game';
				return 'gift-flower'; // 默认图标
			},
			
			goBack() { uni.navigateBack(); },
			goLevelUp() { 
				console.log('gift.vue goLevelUp 被调用，userAiId:', this.userAiId);
				uni.navigateTo({ 
					url: `/pages/ai/level-up?userAiId=${this.userAiId}` 
				}); 
			},
			goToPermissions() { uni.navigateTo({ url: '/pages/ai/permissions' }); },
			goToTasks() { uni.navigateTo({ url: '/pages/ai/tasks' }); },
			
			// 加载进行中的任务数量
			async loadActiveTaskCount() {
				try {
					console.log('开始加载进行中的任务数量，用户ID:', this.userId);
					
					const response = await uni.request({
						url: this.$baseUrl+'/api/u-entities/user-task-relationship/all',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('用户任务关系数据响应:', response);
					
					if (response.statusCode === 200 && response.data) {
						// 1. 打印所有任务的 status 原始值和类型（关键排查！）
						console.log('所有任务的 status 详情：');
						response.data.forEach((task, index) => {
							console.log(`任务${index+1}: id=${task.id}, userId=${task.userId}, status=${task.status}, status类型=${typeof task.status}`);
						});
						// 过滤出当前用户的任务，并统计进行中的任务（status = 1 表示进行中）
						const userTasks = response.data.filter(task => task.userId === this.userId);
						const activeTasks = userTasks.filter(task => task.status === "1");
						// 5. 打印进行中的任务详情（含status）
						console.log(`当前用户进行中的任务（status=1）:`, activeTasks);
						this.activeTaskCount = activeTasks.length;
						console.log('用户任务总数:', userTasks.length, '进行中的任务数量:', this.activeTaskCount);
					} else {
						console.error('加载任务数据失败');
						this.activeTaskCount = 0;
					}
				} catch (error) {
					console.error('加载任务数据异常:', error);
					this.activeTaskCount = 0;
				}
			},
			toggleGiftsList() {
				this.showGiftsList = !this.showGiftsList;
			},
			increaseGift(index) {
				this.gifts[index].count++;
			},
			decreaseGift(index) {
				if (this.gifts[index].count > 0) {
					this.gifts[index].count--;
				}
			},
			async checkout() {
				if (this.selectedGiftsCount <= 0) {
					uni.showToast({ title: '请选择礼物', icon: 'none' });
					return;
				}
				
				try {
					// 显示加载状态
					uni.showLoading({
						title: '正在赠送礼物...'
					});
					
					// 准备购买记录数据
					const purchaseRecords = [];
					let totalCoinsSpent = 0;
					
					for (const gift of this.gifts) {
						if (gift.count > 0) {
							// 为每个礼物创建购买记录
							for (let i = 0; i < gift.count; i++) {
								const purchaseRecord = {
									userId: this.userId,
									itemType: 'gift', // 商品类型：礼物
									itemId: gift.id,
									itemName: gift.name,
									coinsSpent: gift.price,
									coinsBalance: 0, // 这里可以计算用户剩余金币
									purchasePath: `gift_page_${this.userAiId}` // 购买路径，包含userAiId用于AI模型金币日志
								};
								purchaseRecords.push(purchaseRecord);
								totalCoinsSpent += gift.price;
							}
						}
					}
					
					console.log('准备保存的购买记录:', purchaseRecords);
					console.log('总花费金币:', totalCoinsSpent);
					
					// 调用后端API保存购买记录（包含金币扣除）
					const response = await uni.request({
						url: this.$baseUrl+'/api/purchase-record/save-batch',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: purchaseRecords
					});
					
					console.log('购买记录保存响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						uni.hideLoading();
						uni.showToast({ 
							title: `成功赠送 ${this.selectedGiftsCount} 个礼物！花费 ${totalCoinsSpent} 金币`, 
							icon: 'success',
							duration: 2000
						});
						
						// 重置礼物数量
						this.gifts.forEach(gift => gift.count = 0);
					} else {
						// 检查是否是金币不足的错误
						if (response.data.message && response.data.message.includes('金币不足')) {
							uni.hideLoading();
							uni.showToast({
								title: '金币不足，无法赠送礼物',
								icon: 'none',
								duration: 2000
							});
						} else {
							throw new Error(response.data.message || '赠送失败');
						}
					}
				} catch (error) {
					console.error('赠送礼物失败:', error);
					uni.hideLoading();
					uni.showToast({
						title: '赠送失败，请重试',
						icon: 'none'
					});
				}
			},
			
			modifyPhoto() {
				uni.showActionSheet({
					itemList: ['拍照', '从相册选择'],
					success: (res) => {
						if (res.tapIndex === 0) {
							this.openCamera();
						} else if (res.tapIndex === 1) {
							this.selectFromAlbum();
						}
					}
				});
			},
			
			modifyName() {
				uni.showModal({
					title: '修改AI名称',
					editable: true,
					placeholderText: '请输入新的AI名称',
					content: this.aiName,
					success: (res) => {
						if (res.confirm && res.content.trim()) {
							const newName = res.content.trim();
							this.updateAiName(newName);
						}
					}
				});
			},
			
			// 更新AI名字到数据库
			async updateAiName(newName) {
				try {
					if (!this.userAiId) {
						uni.showToast({
							title: 'AI模型ID不存在',
							icon: 'none'
						});
						return;
					}
					
					// 更新本地数据
					this.aiName = newName;
					
					// 调用后端API更新数据库
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/update-name/${this.userAiId}`,
						method: 'PUT',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							modelName: newName
						}
					});
					
					if (response.statusCode === 200) {
						uni.showToast({
							title: 'AI名字修改成功',
							icon: 'success'
						});
					} else {
						throw new Error('更新失败');
					}
				} catch (error) {
					console.error('更新AI名字失败:', error);
					uni.showToast({
						title: '修改失败，请重试',
						icon: 'none'
					});
				}
			},
			
			openCamera() {
				uni.chooseImage({
					count: 1,
					sourceType: ['camera'],
					success: (res) => {
						this.aiAvatar = res.tempFilePaths[0];
						uni.showToast({
							title: '照片修改成功',
							icon: 'success'
						});
					}
				});
			},
			
			selectFromAlbum() {
				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					success: (res) => {
						this.aiAvatar = res.tempFilePaths[0];
						uni.showToast({
							title: '照片修改成功',
							icon: 'success'
						});
					}
				});
			},
			
			// 验证图片URL是否有效
			isValidImageUrl(url) {
				if (!url || typeof url !== 'string') {
					return false;
				}
				
				// 检查是否是有效的图片URL
				const validExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.webp', '.svg'];
				const hasValidExtension = validExtensions.some(ext => 
					url.toLowerCase().includes(ext)
				);
				
				// 检查是否包含example.com等假地址
				const isFakeUrl = url.includes('example.com') || 
								 url.includes('placeholder') || 
								 url.includes('fake') ||
								 url.startsWith('http://localhost') && url.includes('static');
				
				// 检查是否是有效的HTTP/HTTPS URL
				const isValidHttpUrl = url.startsWith('http://') || url.startsWith('https://');
				
				return hasValidExtension && !isFakeUrl && isValidHttpUrl;
			},
			
			// 图片加载错误处理
			onImageError() {
				console.log('图片加载失败，切换到默认头像');
				this.aiAvatar = ''; // 清空无效的图片URL
			},
			
			// 获取头像显示文字
			getAvatarText() {
				if (!this.aiName) {
					return 'AI';
				}
				// 取AI名称的第一个字符
				return this.aiName.charAt(0).toUpperCase();
			}
		}
	}
</script>

<style>
	.gift-page { 
		min-height: 100vh; 
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		display: flex; 
		flex-direction: column; 
	}

	/* 头部 */
	.header { 
		height: 88rpx; 
		display: flex; 
		align-items: center; 
		justify-content: space-between; 
		padding: 0 32rpx; 
		border-bottom: 1rpx solid #F0F0F0; 
		background-color: #FFFFFF;
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

	/* AI 概览 */
	.ai-summary { 
		display: flex; 
		gap: 24rpx; 
		padding: 40rpx; 
		align-items: center; 
		background: rgba(255, 255, 255, 0.95);
		margin: 24rpx;
		border-radius: 20rpx;
		box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
	}
	.avatar-wrap { 
		width: 140rpx; 
		height: 140rpx; 
		border-radius: 12rpx; 
		overflow: hidden; 
		background: linear-gradient(135deg, #667eea, #764ba2);
		box-shadow: 0 6rpx 20rpx rgba(102, 126, 234, 0.3);
		position: relative;
		cursor: pointer;
	}
	.avatar { width: 100%; height: 100%; }
	
	.avatar-placeholder {
		background: linear-gradient(135deg, #667eea, #764ba2);
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.avatar-text {
		font-size: 48rpx;
		font-weight: bold;
		color: white;
	}
	
	.avatar-wrap .edit-overlay {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(0, 0, 0, 0.7);
		color: white;
		font-size: 20rpx;
		text-align: center;
		padding: 4rpx 0;
		opacity: 0;
		transition: opacity 0.3s;
	}
	
	.avatar-wrap:active .edit-overlay {
		opacity: 1;
	}
	.ai-info { flex: 1; display: flex; flex-direction: column; gap: 12rpx; }
	.name-row { display: flex; align-items: center; gap: 16rpx; }
	.ai-name { 
		font-size: 30rpx; 
		color: #333; 
		font-weight: 600; 
		display: flex; 
		align-items: center; 
		gap: 8rpx; 
		cursor: pointer;
	}
	
	.edit-icon {
		font-size: 24rpx;
		opacity: 0.7;
		transition: opacity 0.2s;
	}
	
	.ai-name:active .edit-icon {
		opacity: 1;
	}
	.perm-pill { display: flex; align-items: center; gap: 8rpx; background: #F6F7FB; border: 2rpx solid #E5E7F0; border-radius: 20rpx; padding: 6rpx 14rpx; cursor: pointer; transition: all 0.2s ease; }
	.perm-pill:active { background: #E8E9F0; transform: scale(0.95); }
	.perm-dot { width: 24rpx; height: 24rpx; border-radius: 12rpx; background: #111; color: #fff; display: flex; align-items: center; justify-content: center; font-size: 18rpx; }
	.perm-text { font-size: 22rpx; color: #333; }
	.level-row { display: flex; align-items: center; gap: 16rpx; }
	.level-text { font-size: 26rpx; color: #333; font-weight: 600; }
	.level-up-btn { padding: 8rpx 18rpx; border-radius: 24rpx; border: 2rpx solid #D6E7FF; background: #EEF5FF; }
	.level-up-btn .btn-text { color: #0A84FF; font-size: 22rpx; }
	.progress-row { display: flex; align-items: center; gap: 16rpx; }
	.progress { position: relative; height: 8rpx; flex: 1; }
	.track { position: absolute; left: 0; right: 0; top: 0; bottom: 0; background: #EAEAEA; border-radius: 4rpx; }
	.fill { position: absolute; left: 0; top: 0; bottom: 0; background: #0A84FF; border-radius: 4rpx; }
	.thumb { position: absolute; top: 50%; width: 20rpx; height: 20rpx; background: #fff; border: 2rpx solid #0A84FF; border-radius: 10rpx; transform: translate(-50%, -50%); }
	.progress-text { font-size: 22rpx; color: #666; }

	/* 任务行 */
	.task-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 32rpx 40rpx;
		background: rgba(255, 255, 255, 0.95);
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.task-row:active {
		transform: scale(0.98);
		background: rgba(255, 255, 255, 0.9);
	}
	
	.task-left {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.task-icon {
		font-size: 40rpx;
	}
	
	.task-text {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
	}
	
	.task-right {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.task-count {
		font-size: 26rpx;
		color: #666;
	}
	
	.task-arrow {
		font-size: 28rpx;
		color: #999;
		font-weight: bold;
	}

	/* 礼物列表 */
	.gift-list {
		border-top: 1rpx solid #EFEFEF;
		background: #FFFFFF;
	}
	
	.gift-header {
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background: linear-gradient(135deg, rgba(102, 126, 234, 0.7), rgba(102, 126, 234, 0.3));
		border-bottom: 1rpx solid #E8F5E8;
	}
	
	.header-left {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.back-arrow-svg {
		width: 40rpx;
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}

	.dropdown-arrow {
		width: 0;
		height: 0;
		border-left: 8rpx solid transparent;
		border-right: 8rpx solid transparent;
		border-top: 12rpx solid #666;
		transition: transform 0.3s ease;
	}

	.dropdown-arrow.rotated {
		transform: rotate(180deg);
	}
	
	.header-title {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.header-subtitle {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	.gift-items {
		padding: 0 32rpx;
		border-top: 1rpx solid #F0F0F0;
	}
	
	.gift-item {
		height: 140rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		cursor: pointer;
		transition: all 0.2s ease;
		background: #FFFFFF;
	}
	
	.gift-item:active {
		background-color: #F8F8F8;
		transform: scale(0.98);
	}
	
	.gift-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
		flex: 1;
	}
	
	.gift-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}
	
	/* 礼物图标样式 */
	.gift-flower {
		background: linear-gradient(135deg, #FF6B9D, #FF8EAB);
	}
	
	.gift-beer {
		background: linear-gradient(135deg, #FFA726, #FFB74D);
	}
	
	.gift-lollipop {
		background: linear-gradient(135deg, #AB47BC, #BA68C8);
	}
	
	.gift-donut {
		background: linear-gradient(135deg, #FF7043, #FF8A65);
	}
	
	.gift-chocolate {
		background: linear-gradient(135deg, #8D6E63, #A1887F);
	}
	
	.gift-heart {
		background: linear-gradient(135deg, #F44336, #EF5350);
	}
	
	.gift-hairband {
		background: linear-gradient(135deg, #9C27B0, #BA68C8);
	}
	
	.gift-game {
		background: linear-gradient(135deg, #3F51B5, #5C6BC0);
	}
	
	.gift-info {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
		flex: 1;
	}
	
	.gift-name {
		font-size: 30rpx;
		color: #333333;
		font-weight: 600;
		line-height: 1.2;
	}
	
	.gift-description {
		font-size: 24rpx;
		color: #666666;
		line-height: 1.4;
		max-width: 400rpx;
	}
	
	.gift-right {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex-shrink: 0;
	}
	
	.gift-price {
		font-size: 32rpx;
		color: #FF6B35;
		font-weight: 700;
		text-align: right;
		min-width: 80rpx;
	}
	
	.quantity-controls {
		display: flex;
		align-items: center;
		gap: 12rpx;
		border: 1rpx solid #E0E0E0;
		border-radius: 24rpx;
		padding: 4rpx 12rpx;
		background: #F5F5F5;
	}
	
	.control-btn {
		width: 48rpx;
		height: 48rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 36rpx;
		color: #666;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.control-btn:active {
		background: #E0E0E0;
	}
	
	.control-btn.disabled {
		color: #C0C0C0;
		cursor: not-allowed;
	}
	
	.quantity-display {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
	}

	/* 底部购物条 */
	.cart-bar { 
		height: 120rpx; 
		display: flex; 
		align-items: center; 
		gap: 20rpx; 
		padding: 0 24rpx; 
		background: #fff; 
		border-top: 1rpx solid #F0F0F0;
	}
	.cart-icon { 
		width: 48rpx; 
		height: 40rpx; 
		border: 4rpx solid #111; 
		border-top-left-radius: 6rpx; 
		border-top-right-radius: 6rpx; 
		border-bottom-left-radius: 10rpx; 
		border-bottom-right-radius: 10rpx; 
		position: relative; 
	}
	.cart-total { 
		flex: 1; 
		font-size: 28rpx; 
		color: #333; 
	}
	.checkout-btn { 
		height: 80rpx; 
		padding: 0 36rpx; 
		border-radius: 40rpx; 
		background: #FF6B6B; 
		display: flex; 
		align-items: center; 
		justify-content: center; 
		transition: all 0.2s ease;
	}
	.checkout-btn:active { 
		transform: scale(0.95); 
		background: #FF5252;
	}
	.checkout-text { 
		font-size: 28rpx; 
		color: #fff; 
		font-weight: 600; 
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

	/* 底部指示器 */
	.home-indicator { height: 8rpx; background: #000; border-radius: 4rpx; width: 120rpx; margin: 16rpx auto; }
</style>

