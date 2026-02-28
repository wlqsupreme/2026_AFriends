<template>
	<view class="level-up-page">
		<!-- 头部导航（简单占位） -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">等级提升</text>
			<view class="placeholder"></view>
		</view> -->

		<!-- 加载状态 -->
		<view v-if="loading" class="loading-container">
			<text class="loading-text">加载中...</text>
		</view>
		
		<!-- 错误状态 -->
		<view v-else-if="errorMessage" class="error-container">
			<text class="error-text">{{ errorMessage }}</text>
			<view class="retry-btn" @click="loadProducts">
				<text class="retry-text">重试</text>
			</view>
		</view>
		
		<!-- 金币礼包网格 -->
		<view v-else class="packs-grid">
			<view class="pack-card" v-for="(pack, idx) in packs" :key="pack.id || idx" :class="{ active: selectedIndex === idx }" @click="selectPack(idx)">
				<text class="coin-text">{{ pack.coin }}金币</text>
				<text class="price-text">{{ pack.price }}经验</text>
			</view>
		</view>

		<!-- AI模型状态显示 -->
		<view class="ai-model-status">
			<view class="status-item">
				<text class="label">当前等级：</text>
				<text class="value">{{ currentLevel }}</text>
			</view>
			<view class="status-item">
				<text class="label">当前经验：</text>
				<text class="value">{{ currentExp }}</text>
			</view>
			<view class="status-item" v-if="getNextLevelRequirement() > 0">
				<text class="label">升级需要：</text>
				<text class="value">{{ getNextLevelRequirement() }}经验</text>
			</view>
		</view>

		<!-- 余额显示 -->
		<view class="balance">
			<text class="label">金币余额：</text>
			<text class="value">{{ coinBalance }}</text>
		</view>

		<!-- 立即充值按钮 -->
		<view class="cta-wrapper">
			<view class="cta-btn" @click="recharge">
				<text class="cta-text">立即升级</text>
			</view>
		</view>

		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		name: 'LevelUpPage',
		data() {
			return {
				packs: [],
				selectedIndex: 0,
				coinBalance: 0,
				loading: false,
				errorMessage: '',
				// AI模型信息
				userAiId: null,
				userId: null,
				currentLevel: 1,
				currentExp: 0,
				levelRules: []
			}
		},
		onLoad(options) {
			// 获取传入的AI模型ID
			if (options.userAiId) {
				this.userAiId = parseInt(options.userAiId);
			}
			this.userId = uni.getStorageSync('userId') || 1;
			
			this.loadProducts();
			this.loadUserBalance();
			this.loadAiModelData();
			this.loadLevelRules();
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			selectPack(idx) {
				this.selectedIndex = idx;
			},
			async recharge() {
				const p = this.packs[this.selectedIndex];
				if (!p) {
					uni.showToast({
						title: '请选择升级套餐',
						icon: 'error'
					});
					return;
				}
				
				// 检查用户金币是否足够
				if (this.coinBalance < p.price) {
					uni.showToast({
						title: '金币不足，无法升级',
						icon: 'error'
					});
					return;
				}
				
				// 显示确认对话框
				const confirmResult = await new Promise((resolve) => {
					uni.showModal({
						title: '确认升级',
						content: `确定要花费 ${p.price} 金币购买 ${p.coin} 经验吗？`,
						success: (res) => resolve(res.confirm),
						fail: () => resolve(false)
					});
				});
				
				if (!confirmResult) {
					return;
				}
				
				// 显示加载状态
				uni.showLoading({
					title: '升级中...'
				});
				
				try {
					// 创建购买记录
					const purchaseRecord = {
						userId: uni.getStorageSync('userId') || 1,
						itemType: 'level_up',
						itemId: p.id,
						itemName: `${p.coin}金币升级套餐`,
						coinsSpent: p.price,
						coinsBalance: 0, // 将在后端计算
						purchasePath: 'level_up_page'
					};
					
					// 调用后端API保存购买记录
					const response = await uni.request({
						url: this.$baseUrl+'/api/purchase-record/save-batch',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: [purchaseRecord]
					});
					
					if (response.statusCode === 200 && response.data.success) {
						// 购买成功，刷新用户余额
						await this.loadUserBalance();
						
						// 增加经验（获得经验值）
						const expGained = p.price; // 产品价格就是经验值
						console.log('购买成功，准备增加经验:', {
							expGained: expGained,
							currentLevel: this.currentLevel,
							currentExp: this.currentExp,
							userAiId: this.userAiId
						});
						
						// 显示调试信息
						uni.showToast({
							title: `准备增加经验：+${expGained}`,
							icon: 'none',
							duration: 2000
						});
						
						await this.addExperience(expGained);
						
						uni.showToast({
							title: '购买成功！',
							icon: 'success'
						});
						
						// 可以选择返回上一页或刷新页面
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					} else {
						throw new Error(response.data.message || '购买失败');
					}
				} catch (error) {
					console.error('升级失败:', error);
					
					// 检查是否是金币不足的错误
					if (error.message && error.message.includes('金币不足')) {
						uni.showToast({
							title: '金币不足，升级失败',
							icon: 'error'
						});
					} else {
						uni.showToast({
							title: '升级失败，请重试',
							icon: 'error'
						});
					}
				} finally {
					uni.hideLoading();
				}
			},
			
			// 加载产品数据（先调用load-to-memory接口，再查询可用产品）
			async loadProducts() {
			    this.loading = true;
			    this.errorMessage = '';
			    
			    try {
			        console.log('开始加载产品数据：第一步调用 /products-base/load-to-memory 接口加载到内存');
			        
			        // 第一步：先调用后端 load-to-memory 接口，将产品数据加载到内存
			        const loadResponse = await uni.request({
			            url: this.$baseUrl+'/api/products-base/load-to-memory',
			            method: 'POST',
			            header: {
			                'Content-Type': 'application/json',
			                'Accept-Charset': 'utf-8'
			            },
			            timeout: 10000
			        });
			        
			        console.log('产品加载到内存接口响应:', loadResponse);
			        
			        // 验证加载是否成功
			        if (loadResponse.statusCode !== 200) {
			            throw new Error("产品数据加载到内存失败：接口状态码异常");
			        }
			        const isLoadSuccess = loadResponse.data?.message && loadResponse.data.message.includes('成功') 
			                            || loadResponse.data?.duration !== undefined;
			        if (!isLoadSuccess) {
			            throw new Error("产品数据加载到内存失败：" + (loadResponse.data?.message || '未知错误'));
			        }
			        
			        console.log('产品数据加载到内存成功，开始查询可用产品');
			        
			        // 第二步：加载成功后，查询可用产品
			        const listResponse = await uni.request({
			            url: this.$baseUrl+'/api/products-base/available',
			            method: 'GET',
			            header: {
			                'Content-Type': 'application/json',
			                'Accept-Charset': 'utf-8'
			            },
			            timeout: 5000
			        });
			        
			        if (listResponse.statusCode === 200 && listResponse.data.success) {
			            this.packs = this.convertProductsToDisplayFormat(listResponse.data.data);
			            console.log('产品数据加载成功:', this.packs);
			            // 若没有可用产品，使用默认数据
			            if (!this.packs.length) {
			                this.packs = [
			                    { coin: 10, price: 100, coinExtra: 0, expExtra: 0 },
			                    { coin: 100, price: 1000, coinExtra: 0, expExtra: 0 },
			                    { coin: 500, price: 5000, coinExtra: 0, expExtra: 0 },
			                    { coin: 1000, price: 10000, coinExtra: 0, expExtra: 0 }
			                ];
			            }
			        } else {
			            throw new Error(listResponse.data.message || '加载可用产品失败');
			        }
			    } catch (error) {
			        console.error('加载产品数据失败:', error);
			        this.errorMessage = '加载产品数据失败，请重试';
			        // 使用默认数据作为备用
			        this.packs = [
			            { coin: 10, price: 100, coinExtra: 0, expExtra: 0 },
			            { coin: 100, price: 1000, coinExtra: 0, expExtra: 0 },
			            { coin: 500, price: 5000, coinExtra: 0, expExtra: 0 },
			            { coin: 1000, price: 10000, coinExtra: 0, expExtra: 0 }
			        ];
			    } finally {
			        this.loading = false;
			    }
			},
			
			// 加载用户金币余额
			async loadUserBalance() {
				try {
					const userId = uni.getStorageSync('userId') || 1;
					const response = await uni.request({
						url: `${this.$baseUrl}/api/users/${userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200 && response.data) {
						this.coinBalance = response.data.gold || 0;
						console.log('用户金币余额:', this.coinBalance);
					}
				} catch (error) {
					console.error('加载用户余额失败:', error);
					this.coinBalance = 0;
				}
			},
			
			// 转换产品数据为显示格式
			convertProductsToDisplayFormat(products) {
				return products.map(product => ({
					id: product.coinRechargeId,
					coin: product.coinNum || 0,
					price: product.expNum || 0,
					coinExtra: product.coinExtraNum || 0,
					expExtra: product.expExtraNum || 0,
					extraNum: product.extraNum || 0,
					status: product.status || 0
				}));
			},
			
			// 加载AI模型数据
			async loadAiModelData() {
				console.log('loadAiModelData 被调用，userAiId:', this.userAiId);
				if (!this.userAiId) {
					console.log('没有userAiId，无法加载AI模型数据');
					return;
				}
				
				try {
					console.log('开始加载AI模型数据，userAiId:', this.userAiId);
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/${this.userAiId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('AI模型数据响应:', response);
					
					if (response.statusCode === 200 && response.data) {
						const modelData = response.data;
						this.currentLevel = modelData.level || 1;
						this.currentExp = modelData.totalExp || 0;
						
						console.log('AI模型数据加载成功:', {
							level: this.currentLevel,
							exp: this.currentExp
						});
					} else {
						console.error('加载AI模型数据失败:', response);
					}
				} catch (error) {
					console.error('加载AI模型数据异常:', error);
				}
			},
			
			// 加载等级规则数据
			async loadLevelRules() {
				try {
					console.log('开始加载等级规则数据');
					
					const response = await uni.request({
						url: this.$baseUrl+'/api/aimodel-level-rule/all-ordered',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('等级规则数据响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						this.levelRules = response.data.data;
						console.log('等级规则数据加载成功，数量:', this.levelRules.length);
					} else {
						console.error('加载等级规则数据失败:', response);
					}
				} catch (error) {
					console.error('加载等级规则数据异常:', error);
				}
			},
			
			// 增加经验并执行升级逻辑
			async addExperience(expGained) {
				console.log('=== 开始增加经验并检查升级 ===');
				console.log('addExperience 被调用，参数:', {
					userAiId: this.userAiId,
					expGained: expGained,
					currentLevel: this.currentLevel,
					currentExp: this.currentExp,
					levelRulesLength: this.levelRules.length
				});
				
				if (!this.userAiId) {
					console.log('缺少userAiId，无法增加经验');
					uni.showToast({
						title: '缺少AI模型ID，无法增加经验',
						icon: 'error',
						duration: 3000
					});
					return;
				}
				
				if (!this.levelRules.length) {
					console.log('缺少等级规则数据，无法检查升级');
					uni.showToast({
						title: '等级规则数据未加载，无法升级',
						icon: 'error',
						duration: 3000
					});
					return;
				}
				
				// 先累加经验
				let totalExp = this.currentExp + expGained;
				let currentLevel = this.currentLevel;
				let leveledUp = false;
				let upgradeCount = 0;
				
				console.log('经验累加后:', {
					originalExp: this.currentExp,
					expGained: expGained,
					totalExp: totalExp,
					currentLevel: currentLevel
				});
				
				// 循环检查升级（横着读等级规则）
				console.log('=== 开始循环升级检查 ===');
				console.log('升级检查前状态:', {
					currentLevel: currentLevel,
					totalExp: totalExp,
					levelRulesCount: this.levelRules.length
				});
				
				while (true) {
					// 查找当前等级的规则
					const currentLevelRule = this.levelRules.find(rule => rule.level === currentLevel);
					
					console.log(`查找等级 ${currentLevel} 的规则:`, currentLevelRule);
					
					if (!currentLevelRule) {
						console.log('没有当前等级规则，停止升级检查');
						break;
					}
					
					console.log(`检查等级 ${currentLevel} 升级条件:`, {
						level: currentLevel,
						requiredExp: currentLevelRule.expRequirement,
						currentTotalExp: totalExp,
						canUpgrade: totalExp >= currentLevelRule.expRequirement,
						comparison: `${totalExp} >= ${currentLevelRule.expRequirement} = ${totalExp >= currentLevelRule.expRequirement}`
					});
					
					// 检查是否可以升级
					if (totalExp >= currentLevelRule.expRequirement) {
						// 可以升级
						const expBeforeUpgrade = totalExp;
						totalExp = totalExp - currentLevelRule.expRequirement; // 减去升级消耗的经验
						currentLevel = currentLevel + 1; // 等级加1
						leveledUp = true;
						upgradeCount++;
						
						console.log(`升级成功！等级 ${currentLevel - 1} -> ${currentLevel}，消耗经验: ${currentLevelRule.expRequirement}，剩余经验: ${totalExp}`);
					} else {
						// 经验不足，停止升级
						console.log(`经验不足，无法升级到等级 ${currentLevel + 1}，需要: ${currentLevelRule.expRequirement}，当前: ${totalExp}`);
						break;
					}
				}
				
				console.log('升级检查完成:', {
					originalLevel: this.currentLevel,
					originalExp: this.currentExp,
					finalLevel: currentLevel,
					finalExp: totalExp,
					leveledUp: leveledUp,
					upgradeCount: upgradeCount
				});
				
				// 更新AI模型等级和经验
				await this.updateAiModelLevel(currentLevel, totalExp);
				
				// 更新前端显示
				this.currentLevel = currentLevel;
				this.currentExp = totalExp;
				
				// 显示结果提示
				if (leveledUp) {
					uni.showToast({
						title: `升级成功！等级${currentLevel}，剩余经验${totalExp}`,
						icon: 'success',
						duration: 2000
					});
				} else {
					uni.showToast({
						title: `经验+${expGained}，当前等级${currentLevel}，总经验${totalExp}`,
						icon: 'success',
						duration: 2000
					});
				}
			},
			
			// 获取当前等级所需经验
			getNextLevelRequirement() {
				if (!this.levelRules.length) return 0;
				const currentLevelRule = this.levelRules.find(rule => rule.level === this.currentLevel);
				console.log('getNextLevelRequirement: 当前等级', this.currentLevel, '所需经验', currentLevelRule ? currentLevelRule.expRequirement : 0);
				return currentLevelRule ? currentLevelRule.expRequirement : 0;
			},
			
			// 更新AI模型等级和经验
			async updateAiModelLevel(newLevel, newExp) {
				try {
					const response = await uni.request({
						url: `${this.$baseUrl}/api/user-ai-model/update-level/${this.userAiId}`,
						method: 'PUT',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							level: newLevel,
							totalExp: newExp
						}
					});
					
					if (response.statusCode === 200 && response.data.success) {
						this.currentLevel = newLevel;
						this.currentExp = newExp;
						console.log('AI模型等级更新成功');
					} else {
						console.error('更新AI模型等级失败:', response);
					}
				} catch (error) {
					console.error('更新AI模型等级异常:', error);
				}
			}
		}
	}
</script>

<style>
	.level-up-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
	}

	/* 头部 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 24rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	.back-button { width: 64rpx; height: 64rpx; display: flex; align-items: center; justify-content: center; }
	.back-arrow { width: 0; height: 0; border-right: 16rpx solid var(--color-text, #333); border-top: 10rpx solid transparent; border-bottom: 10rpx solid transparent; }
	.title { font-size: 32rpx; color: var(--color-text, #333); font-weight: 600; }
	.placeholder { width: 64rpx; }

	/* 加载状态 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 32rpx;
	}
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666);
	}
	
	/* 错误状态 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 80rpx 32rpx;
		gap: 24rpx;
	}
	.error-text {
		font-size: 28rpx;
		color: var(--color-error, #ff4d4f);
		text-align: center;
	}
	.retry-btn {
		padding: 16rpx 32rpx;
		background: var(--color-primary, #FF6FB1);
		border-radius: 24rpx;
	}
	.retry-text {
		font-size: 26rpx;
		color: var(--color-white, #fff);
	}

	/* 网格 */
	.packs-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 32rpx;
		padding: 40rpx 32rpx 24rpx;
	}
	.pack-card {
		background: var(--color-card-bg, #FFFFFF);
		border: 2rpx solid var(--color-border-light, #E5E5E5);
		border-radius: 20rpx;
		box-shadow: 0 6rpx 18rpx var(--color-shadow-light, rgba(0,0,0,0.06));
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 180rpx;
		gap: 16rpx;
		transition: all .2s ease;
	}
	.pack-card:active { transform: scale(0.98); box-shadow: 0 4rpx 12rpx var(--color-shadow, rgba(0,0,0,0.12)); }
	.pack-card.active { border-color: var(--color-primary-dark, #FF4D8D); box-shadow: 0 8rpx 24rpx var(--color-primary-shadow, rgba(255,77,141,.25)); }
	.coin-text { font-size: 28rpx; color: var(--color-text, #333); font-weight: 600; }
	.price-text { font-size: 24rpx; color: var(--color-text-tertiary, #777); }

	/* AI模型状态显示 */
	.ai-model-status {
		background: var(--color-bg-light, #f8f9fa);
		border-radius: 16rpx;
		padding: 24rpx 32rpx;
		margin: 0 32rpx 24rpx;
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	.status-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	.status-item .label {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666);
	}
	.status-item .value {
		font-size: 26rpx;
		color: var(--color-text, #333);
		font-weight: 600;
	}

	/* 余额 */
	.balance {
		padding: 28rpx 32rpx 0;
		display: flex;
		align-items: baseline;
		gap: 8rpx;
		color: var(--color-text, #333);
	}
	.balance .label { font-size: 26rpx; color: var(--color-text-secondary, #666); }
	.balance .value { font-size: 30rpx; font-weight: 700; }

	/* CTA */
	.cta-wrapper { padding: 40rpx 32rpx; margin-top: auto; }
	.cta-btn {
		height: 96rpx;
		border-radius: 48rpx;
		background: linear-gradient(135deg, var(--color-primary, #FF6FB1), var(--color-primary-dark, #FF3D7E));
		display: flex; align-items: center; justify-content: center;
		box-shadow: 0 10rpx 24rpx var(--color-primary-shadow, rgba(255,61,126,.3));
	}
	.cta-text { font-size: 32rpx; color: var(--color-white, #FFF); font-weight: 700; letter-spacing: 2rpx; }

	/* 底部指示器 */
	.home-indicator { height: 8rpx; background: var(--color-text, #000); border-radius: 4rpx; width: 120rpx; margin: 16rpx auto; }
</style>



