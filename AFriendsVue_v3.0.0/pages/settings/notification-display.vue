<!-- 通知显示内容设置页面 -->
<template>
	<view class="notification-display-page" :class="{ care: isCare }">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">{{ $t('notificationDisplay.title') }}</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 通知显示方式选项 -->
			<view class="display-options">
				<view class="option-item" 
					v-for="(option, index) in displayOptions" 
					:key="index"
					:class="{ active: currentDisplayMode === option.value }"
					@click="selectDisplayMode(option.value)">
					<view class="option-info">
						<text class="option-title">{{ $t(`notificationDisplay.options.${option.key}.title`) }}</text>
						<text class="option-desc">{{ $t(`notificationDisplay.options.${option.key}.description`) }}</text>
					</view>
					<view class="option-check" v-if="currentDisplayMode === option.value">
						<text class="check-icon">✓</text>
					</view>
				</view>
			</view>
			
			<!-- 说明文字 -->
			<view class="display-note">
				<text class="note-text">{{ $t('notificationDisplay.note') }}</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';
import { useModeStore } from '@/store/mode.js';

export default {
    name: 'NotificationDisplay',
    data() {
        return {
            userId: null,
            modeStore: null,
            currentDisplayMode: 2, // 数字模式: 0, 1, 2
            displayOptions: [
                {
                    value: 0,
                    key: 'minimal'
                },
                {
                    value: 1,
                    key: 'medium'
                },
                {
                    value: 2,
                    key: 'full'
                }
            ],
            isLoading: false
        }
    },
    onLoad() {
        // 获取用户ID（实际项目中从登录信息获取）
        this.userId = uni.getStorageSync('userId') || 1; // 示例默认值
        this.loadCurrentDisplayMode()
        
        // 初始化主题
        const themeStore = useThemeStore();
        themeStore.init();
        themeStore.applyTheme();

		const modeStore = useModeStore();
		modeStore.init();
		this.modeStore = modeStore;
    },
    computed: {
        isCare() {
            return !!(this.modeStore && this.modeStore.isCare);
        }
    },
    methods: {
        goBack() {
            uni.navigateBack()
        },
        
        async loadCurrentDisplayMode() {
            if (!this.userId) {
                // 尝试从本地存储加载
                try {
                    const savedMode = uni.getStorageSync('notificationDisplayMode')
                    if (savedMode !== undefined && savedMode !== '') {
                        this.currentDisplayMode = parseInt(savedMode)
                    }
                } catch (e) {
                    console.error(this.$t('common.networkError'), e)
                }
                return;
            }
            
            this.isLoading = true;
            try {
                const res = await uni.request({
                    url: `/api/notification-settings/${this.userId}`,
                    method: 'GET'
                });
                
                // uni.request 直接返回响应对象，而不是 [error, response] 数组
                if (res.data) {
                    const settings = res.data;
                    this.currentDisplayMode = settings.notificationDisplayMode !== undefined ? 
                                           settings.notificationDisplayMode : 2;
                }
            } catch (e) {
                console.error(this.$t('common.networkError'), e)
                // 回退到本地存储
                try {
                    const savedMode = uni.getStorageSync('notificationDisplayMode')
                    if (savedMode !== undefined && savedMode !== '') {
                        this.currentDisplayMode = parseInt(savedMode)
                    }
                } catch (storageError) {
                    console.error(this.$t('common.networkError'), storageError)
                }
            } finally {
                this.isLoading = false;
            }
        },
        
        async selectDisplayMode(mode) {
            if (this.currentDisplayMode === mode) {
                return
            }
            
            const oldMode = this.currentDisplayMode;
            this.currentDisplayMode = mode
            
            // 优先保存到服务器
            if (this.userId) {
                try {
                    const res = await uni.request({
                        url: `/api/notification-settings/${this.userId}`,
                        method: 'GET'
                    });
                    
                    // uni.request 直接返回响应对象，而不是 [error, response] 数组
                    if (res.statusCode !== 200) {
                        throw new Error(this.$t('notificationSettings.saveFailed'));
                    }
                    
                    let settings = res.data || { userId: this.userId };
                    settings.notificationDisplayMode = mode;
                    
                    const updateRes = await uni.request({
                        url: '/api/notification-settings/update',
                        method: 'POST',
                        data: settings
                    });
                    
                    // uni.request 直接返回响应对象，而不是 [error, response] 数组
                    if (updateRes.statusCode !== 200) {
                        throw new Error(this.$t('notificationSettings.saveFailed'));
                    }
                    
                    uni.showToast({
                        title: this.$t('notificationDisplay.saveSuccess'),
                        icon: 'success'
                    })
                } catch (e) {
                    console.error(this.$t('common.networkError'), e)
                    // 回退到本地存储
                    this.saveDisplayModeLocally(mode, oldMode)
                }
            } else {
                // 仅保存到本地
                this.saveDisplayModeLocally(mode, oldMode)
            }
        },
        
        saveDisplayModeLocally(mode, oldMode) {
            try {
                uni.setStorageSync('notificationDisplayMode', mode.toString())
                uni.showToast({
                    title: this.$t('notificationDisplay.saveSuccess'),
                    icon: 'success'
                })
            } catch (e) {
                console.error(this.$t('common.networkError'), e)
                // 恢复原来的值
                this.currentDisplayMode = oldMode;
                uni.showToast({
                    title: this.$t('common.failed'),
                    icon: 'none'
                })
            }
        }
    }
}
</script>

<style>
	.notification-display-page {
		width: 100%;
		min-height: 100vh;
		background-color: var(--color-bg, #f8f8f8);
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: var(--color-card, #ffffff);
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		padding: 0 16rpx;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text, #333333);
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 32rpx 0;
	}
	
	/* 显示选项列表 */
	.display-options {
		background-color: var(--color-card, #ffffff);
		margin-bottom: 32rpx;
	}
	
	.option-item {
		padding: 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.option-item:last-child {
		border-bottom: none;
	}
	
	.option-item:active {
		background-color: var(--color-bg-weak, rgba(0, 0, 0, 0.05));
	}
	
	.option-item.active {
		background-color: rgba(0, 122, 255, 0.05);
	}
	
	.option-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.option-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.option-desc {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.4;
	}
	
	.option-check {
		width: 48rpx;
		height: 48rpx;
		background-color: #007aff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.check-icon {
		font-size: 24rpx;
		color: #ffffff;
		font-weight: 600;
	}
	
	/* 说明文字 */
	.display-note {
		padding: 32rpx;
		background-color: var(--color-card, #ffffff);
	}
	
	.note-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999999);
		line-height: 1.5;
		text-align: center;
	}
	
	/* 底部手势条 */
	.home-indicator {
		height: 68rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.home-indicator::after {
		content: '';
		width: 200rpx;
		height: 8rpx;
		background-color: var(--color-text, #333333);
		border-radius: 4rpx;
	}

	.notification-display-page.care .content-area {
		padding: 40rpx 0;
		box-sizing: border-box;
	}

	.notification-display-page.care .option-item {
		padding: 40rpx;
		min-height: 128rpx;
		box-sizing: border-box;
	}

	.notification-display-page.care .option-title {
		font-size: 38rpx;
		line-height: 1.5;
	}

	.notification-display-page.care .option-desc {
		font-size: 32rpx;
		line-height: 1.6;
	}

	.notification-display-page.care .option-check {
		width: 64rpx;
		height: 64rpx;
	}

	.notification-display-page.care .check-icon {
		font-size: 30rpx;
	}

	.notification-display-page.care .display-note {
		padding: 40rpx;
		box-sizing: border-box;
	}

	.notification-display-page.care .note-text {
		font-size: 32rpx;
		line-height: 1.6;
	}
</style>