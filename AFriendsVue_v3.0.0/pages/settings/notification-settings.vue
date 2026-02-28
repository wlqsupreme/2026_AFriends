<template>
	<view class="notification-settings-page" :class="{ care: isCare }">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="status-time">9:41</text>
			<view class="status-right">
				<text class="status-signal">📶</text>
				<text class="status-wifi">📶</text>
				<text class="status-battery">🔋</text>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">{{ $t('notificationSettings.title') }}</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 聊天消息通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">{{ $t('notificationSettings.chat.title') }}</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.chat.notification') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.chatNotification" @change="toggleSetting('chatNotification', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item" @click="goToNotificationDisplay">
						<text class="setting-label">{{ $t('notificationSettings.chat.displayContent') }}</text>
						<text class="setting-arrow">›</text>
					</view>
				</view>
			</view>
			
			<!-- 互动通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">{{ $t('notificationSettings.interaction.title') }}</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.interaction.likeAndFavorite') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.likeAndFavorite" @change="toggleSetting('likeAndFavorite', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.interaction.comment') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.comment" @change="toggleSetting('comment', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.interaction.mention') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.mention" @change="toggleSetting('mention', $event)"></switch>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 推荐通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">{{ $t('notificationSettings.recommendation.title') }}</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.recommendation.content') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.contentRecommend" @change="toggleSetting('contentRecommend', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">{{ $t('notificationSettings.recommendation.user') }}</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.userRecommend" @change="toggleSetting('userRecommend', $event)"></switch>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';
import { useModeStore } from '@/store/mode.js';

export default {
    name: 'NotificationSettings',
    data() {
        return {
            userId: null,
            modeStore: null,
            notificationSettings: {
                chatNotification: true,
                likeAndFavorite: false,
                comment: false,
                mention: false,
                contentRecommend: false,
                userRecommend: false
            },
            isLoading: false
        }
    },
    onLoad() {
        // 获取用户ID（实际项目中从登录信息获取）
        this.userId = uni.getStorageSync('userId') || 1; // 示例默认值
        if (this.userId) {
            this.loadNotificationSettings();
        }
        
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
            uni.navigateBack();
        },
        goToNotificationDisplay() {
            uni.navigateTo({
                url: '/pages/settings/notification-display'
            });
        },
        async loadNotificationSettings() {
            if (!this.userId) return;
            
            this.isLoading = true;
            try {
                const res = await uni.request({
                    url: `${this.$baseUrl}/api/notification-settings/${this.userId}`,
                    method: 'GET'
                });
                
                // uni.request 直接返回响应对象，而不是 [error, response] 数组
                if (res.data) {
                    const settings = res.data;
                    this.notificationSettings = {
                        chatNotification: settings.chatNotification === 1,
                        likeAndFavorite: settings.likeFavoriteNotification === 1,
                        comment: settings.commentNotification === 1,
                        mention: settings.mentionNotification === 1,
                        contentRecommend: settings.contentRecommendNotification === 1,
                        userRecommend: settings.userRecommendNotification === 1
                    };
                }
            } catch (error) {
                console.error('加载通知设置失败:', error);
                uni.showToast({
                    title: this.$t('notificationSettings.loadFailed'),
                    icon: 'none'
                });
            } finally {
                this.isLoading = false;
            }
        },
        async toggleSetting(key, event) {
            if (!this.userId) {
                uni.showToast({
                    title: this.$t('notificationSettings.loginRequired'),
                    icon: 'none'
                });
                // 恢复开关状态
                this.notificationSettings[key] = !event.detail.value;
                return;
            }
            
            // 更新本地状态
            this.notificationSettings[key] = event.detail.value;
            
            // 构造要发送到后端的对象
            const settingsToSend = {
                userId: this.userId,
                chatNotification: this.notificationSettings.chatNotification ? 1 : 0,
                likeFavoriteNotification: this.notificationSettings.likeAndFavorite ? 1 : 0,
                commentNotification: this.notificationSettings.comment ? 1 : 0,
                mentionNotification: this.notificationSettings.mention ? 1 : 0,
                contentRecommendNotification: this.notificationSettings.contentRecommend ? 1 : 0,
                userRecommendNotification: this.notificationSettings.userRecommend ? 1 : 0,
                notificationDisplayMode: 2 // 保持默认，实际应该从服务器获取
            };
            
            try {
                const res = await uni.request({
                    url: '${this.$baseUrl}/api/notification-settings/update',
                    method: 'POST',
                    data: settingsToSend
                });
                
                // uni.request 直接返回响应对象，而不是 [error, response] 数组
                if (res.statusCode !== 200) {
                    throw new Error(this.$t('notificationSettings.requestFailed'));
                }
                
                let settingName = '';
                switch(key) {
                    case 'chatNotification':
                        settingName = this.$t('notificationSettings.chat.notification');
                        break;
                    case 'likeAndFavorite':
                        settingName = this.$t('notificationSettings.interaction.likeAndFavorite');
                        break;
                    case 'comment':
                        settingName = this.$t('notificationSettings.interaction.comment');
                        break;
                    case 'mention':
                        settingName = this.$t('notificationSettings.interaction.mention');
                        break;
                    case 'contentRecommend':
                        settingName = this.$t('notificationSettings.recommendation.content');
                        break;
                    case 'userRecommend':
                        settingName = this.$t('notificationSettings.recommendation.user');
                        break;
                }
                
                uni.showToast({
                    title: event.detail.value ? 
                          this.$t('notificationSettings.enabled', { setting: settingName }) : 
                          this.$t('notificationSettings.disabled', { setting: settingName }),
                    icon: 'none'
                });
            } catch (error) {
                console.error('保存通知设置失败:', error);
                // 恢复本地状态
                this.notificationSettings[key] = !event.detail.value;
                uni.showToast({
                    title: this.$t('notificationSettings.saveFailed'),
                    icon: 'none'
                });
            }
        }
    }
}
</script>

<style>
	.notification-settings-page {
		width: 100%;
		min-height: 100vh;
		background-color: var(--color-bg, #f8f8f8);
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 88rpx;
		background-color: var(--color-card, #ffffff);
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		font-size: 28rpx;
		font-weight: 600;
	}
	
	.status-right {
		display: flex;
		gap: 8rpx;
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
	
	.section-group {
		margin-bottom: 32rpx;
	}
	
	.section-header {
		padding: 0 32rpx 16rpx 32rpx;
	}
	
	.section-title {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999999);
		font-weight: 500;
	}
	
	.settings-list {
		background-color: var(--color-card, #ffffff);
	}
	
	.setting-item {
		padding: 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
	}
	
	.setting-item:last-child {
		border-bottom: none;
	}
	
	.setting-left {
		flex: 1;
	}
	
	.setting-label {
		font-size: 32rpx;
		color: var(--color-text, #333333);
	}
	
	.setting-right {
		display: flex;
		align-items: center;
	}
	
	.setting-arrow {
		font-size: 32rpx;
		color: #cccccc;
		font-weight: 300;
	}
	
	.setting-switch {
		transform: scale(0.8);
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

	.notification-settings-page.care .content-area {
		padding: 40rpx 0;
		box-sizing: border-box;
	}

	.notification-settings-page.care .section-header {
		padding: 0 40rpx 20rpx 40rpx;
	}

	.notification-settings-page.care .section-title {
		font-size: 34rpx;
	}

	.notification-settings-page.care .setting-item {
		padding: 40rpx;
		min-height: 120rpx;
		box-sizing: border-box;
	}

	.notification-settings-page.care .setting-label {
		font-size: 36rpx;
		line-height: 1.5;
	}

	.notification-settings-page.care .setting-arrow {
		font-size: 40rpx;
	}

	.notification-settings-page.care .setting-switch {
		transform: scale(1);
	}
</style>