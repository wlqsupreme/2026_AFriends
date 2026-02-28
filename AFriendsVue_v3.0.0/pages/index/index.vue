<template>
  <view class="index-page" :class="themeStore.themeClass">
    <!-- 应用Logo和标题 -->
    <view class="app-header">
      <image class="app-logo" src="/static/afriends-logo.png" mode="aspectFit"></image>
      <text class="app-title">Afriends</text>
      <text class="app-subtitle">{{ $t('index.appSubtitle') }}</text>
    </view>

    <!-- 功能导航区域 -->
    <view class="navigation-grid">
      <!-- 登录入口 -->
      <view class="nav-item login-item" @click="goToLogin">
        <view class="nav-icon login-icon">
          <text class="icon-text">🔐</text>
        </view>
        <text class="nav-text">{{ $t('index.loginRegister') }}</text>
        <text class="nav-desc">{{ $t('index.startJourney') }}</text>
      </view>

      <!-- 首页 -->
      <view class="nav-item" @click="goToHome">
        <view class="nav-icon home-icon">
          <text class="icon-text">🏠</text>
        </view>
        <text class="nav-text">{{ $t('index.home') }}</text>
        <text class="nav-desc">{{ $t('index.discoverContent') }}</text>
      </view>

      <!-- 聊天列表 -->
      <view class="nav-item" @click="goToChatList">
        <view class="nav-icon chat-icon">
          <text class="icon-text">💬</text>
        </view>
        <text class="nav-text">{{ $t('index.messages') }}</text>
        <text class="nav-desc">{{ $t('index.viewChatHistory') }}</text>
      </view>

      <!-- AI聊天 -->
      <view class="nav-item" @click="goToAIChat">
        <view class="nav-icon ai-icon">
          <text class="icon-text">🤖</text>
        </view>
        <text class="nav-text">{{ $t('index.aiAssistant') }}</text>
        <text class="nav-desc">{{ $t('index.smartChat') }}</text>
      </view>

      <!-- 好友列表 -->
      <view class="nav-item" @click="goToFriendList">
        <view class="nav-icon friend-icon">
          <text class="icon-text">👥</text>
        </view>
        <text class="nav-text">{{ $t('index.friends') }}</text>
        <text class="nav-desc">{{ $t('index.manageFriends') }}</text>
      </view>

      <!-- 个人主页 -->
      <view class="nav-item" @click="goToProfile">
        <view class="nav-icon profile-icon">
          <text class="icon-text">👤</text>
        </view>
        <text class="nav-text">{{ $t('index.mine') }}</text>
        <text class="nav-desc">{{ $t('index.profileManagement') }}</text>
      </view>
    </view>

    <!-- 快速功能区域 -->
    <view class="quick-actions">
      <text class="section-title">{{ $t('index.quickActions') }}</text>
      <view class="action-buttons">
        <view class="action-btn" @click="goToSettings">
          <text class="action-icon">⚙️</text>
          <text class="action-text">{{ $t('index.settings') }}</text>
        </view>
        <view class="action-btn" @click="goToWallet">
          <text class="action-icon">💰</text>
          <text class="action-text">{{ $t('index.wallet') }}</text>
        </view>
        <view class="action-btn" @click="goToHelp">
          <text class="action-icon">❓</text>
          <text class="action-text">{{ $t('index.help') }}</text>
        </view>
      </view>
    </view>

    <!-- 底部信息 -->
    <view class="footer-info">
      <text class="version-text">{{ $t('index.version') }} 3.0.0</text>
      <text class="copyright-text">{{ $t('index.copyright') }}</text>
    </view>
  </view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js'

export default {
  name: 'IndexPage',
  data() {
    return {
			themeStore: useThemeStore()
    }
  },
	onLoad() {
		this.themeStore.init()
		this.themeStore.applyTheme()
	},
	onShow() {
		this.themeStore.applyTheme()
	},
  methods: {
    // 登录入口
    goToLogin() {
      uni.navigateTo({
        url: '/pages/login/login-replica'
      });
    },

    // 首页
    goToHome() {
      uni.navigateTo({
        url: '/pages/feed/content-feed'
      });
    },

    // 聊天列表
    goToChatList() {
      uni.navigateTo({
        url: '/pages/chat/chat-list'
      });
    },

    // AI聊天
    goToAIChat() {
      uni.navigateTo({
        url: '/pages/ai/ai-chat'
      });
    },

    // 好友列表
    goToFriendList() {
      uni.navigateTo({
        url: '/pages/chat/friend-list'
      });
    },

    // 个人主页
    goToProfile() {
      uni.navigateTo({
        url: '/pages/feed/user-profile'
      });
    },

    // 设置
    goToSettings() {
      uni.navigateTo({
        url: '/pages/settings/settings'
      });
    },

    // 钱包
    goToWallet() {
      uni.navigateTo({
        url: '/pages/wallet/wallet'
      });
    },

    // 帮助
    goToHelp() {
      uni.navigateTo({
        url: '/pages/settings/faq'
      });
    }
  }
}
</script>

<style>
.index-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 40rpx 40rpx;
}

/* 应用头部 */
.app-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.app-logo {
  width: 200rpx;
  height: 80rpx;
  margin-bottom: 20rpx;
}

.app-title {
  font-size: 48rpx;
  color: #ffffff;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
}

.app-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
  display: block;
}

/* 导航网格 */
.navigation-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30rpx;
  width: 100%;
  margin-bottom: 60rpx;
}

.nav-item {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  text-align: center;
  box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.nav-item:active {
  transform: scale(0.95);
  box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.2);
}

.nav-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20rpx;
}

.login-icon {
  background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
}

.home-icon {
  background: linear-gradient(135deg, #4ECDC4, #44A08D);
}

.chat-icon {
  background: linear-gradient(135deg, #45B7D1, #96CEB4);
}

.ai-icon {
  background: linear-gradient(135deg, #FF69B4, #FF1493);
}

.friend-icon {
  background: linear-gradient(135deg, #FFD700, #FFA500);
}

.profile-icon {
  background: linear-gradient(135deg, #9370DB, #8A2BE2);
}

.icon-text {
  font-size: 40rpx;
}

.nav-text {
  font-size: 32rpx;
  color: #333333;
  font-weight: 600;
  display: block;
  margin-bottom: 8rpx;
}

.nav-desc {
  font-size: 24rpx;
  color: #666666;
  display: block;
}

/* 登录项特殊样式 */
.login-item {
  grid-column: 1 / -1;
  background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
  color: white;
}

.login-item .nav-text,
.login-item .nav-desc {
  color: white;
}

/* 快速功能区域 */
.quick-actions {
  width: 100%;
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 32rpx;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 600;
  text-align: center;
  margin-bottom: 30rpx;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
}

.action-buttons {
  display: flex;
  justify-content: space-around;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 15rpx;
  padding: 30rpx 20rpx;
  text-align: center;
  backdrop-filter: blur(10rpx);
  border: 1rpx solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
  cursor: pointer;
}

.action-btn:active {
  transform: scale(0.95);
  background: rgba(255, 255, 255, 0.3);
}

.action-icon {
  font-size: 36rpx;
  display: block;
  margin-bottom: 10rpx;
}

.action-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
}

/* 底部信息 */
.footer-info {
  text-align: center;
  margin-top: auto;
}

.version-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
  display: block;
  margin-bottom: 10rpx;
}

.copyright-text {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.5);
  display: block;
}

/* 响应式设计 */
@media (max-width: 750rpx) {
  .navigation-grid {
    gap: 20rpx;
  }

  .nav-item {
    padding: 30rpx 20rpx;
  }

  .nav-icon {
    width: 60rpx;
    height: 60rpx;
  }

  .icon-text {
    font-size: 32rpx;
  }

  .nav-text {
    font-size: 28rpx;
  }

  .nav-desc {
    font-size: 20rpx;
  }
}

.index-page.theme-dark,
.theme-dark .index-page {
	background: linear-gradient(135deg, var(--color-bg) 0%, var(--color-bg-weak) 100%);
}

.index-page.theme-dark .app-title,
.theme-dark .index-page .app-title {
	color: var(--color-text);
	text-shadow: 0 2rpx 4rpx var(--color-border);
}

.index-page.theme-dark .app-subtitle,
.theme-dark .index-page .app-subtitle {
	color: var(--color-text-secondary);
}

.index-page.theme-dark .nav-item,
.theme-dark .index-page .nav-item {
	background: var(--color-card);
	box-shadow: 0 8rpx 25rpx var(--color-divider);
}

.index-page.theme-dark .nav-item:active,
.theme-dark .index-page .nav-item:active {
	box-shadow: 0 4rpx 15rpx var(--color-border);
}

.index-page.theme-dark .nav-text,
.theme-dark .index-page .nav-text {
	color: var(--color-text);
}

.index-page.theme-dark .nav-desc,
.theme-dark .index-page .nav-desc {
	color: var(--color-text-secondary);
}

.index-page.theme-dark .section-title,
.theme-dark .index-page .section-title {
	color: var(--color-text);
	text-shadow: 0 2rpx 4rpx var(--color-border);
}

.index-page.theme-dark .action-btn,
.theme-dark .index-page .action-btn {
	background: var(--color-card);
	border: 1rpx solid var(--color-border);
}

.index-page.theme-dark .action-btn:active,
.theme-dark .index-page .action-btn:active {
	background: var(--color-bg-weak);
}

.index-page.theme-dark .action-text,
.theme-dark .index-page .action-text {
	color: var(--color-text);
}

.index-page.theme-dark .version-text,
.theme-dark .index-page .version-text,
.index-page.theme-dark .copyright-text,
.theme-dark .index-page .copyright-text {
	color: var(--color-text-secondary);
}
</style>




