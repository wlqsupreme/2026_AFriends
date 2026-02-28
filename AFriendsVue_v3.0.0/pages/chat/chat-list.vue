<template>
  <view class="chat-list" :class="[themeStore.themeClass, { concise: isConcise, care: isCare }]">
    <!-- 顶部状态栏和导航 -->
    <view class="header">
      <view class="nav-header">
        <view class="back-button" @click="goBack">
          <svg t="1756277721385" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="848" width="32" height="32">
            <path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="849"></path>
          </svg>
        </view>
        <view class="header-title">{{ $t("chatList.title") }}</view>
        <view class="placeholder"></view>
      </view>
    </view>
    
    <!-- 聊天列表内容 -->
    <scroll-view class="chat-content" scroll-y>
      <!-- 搜索框 -->
      <view class="search-container" :class="{ concise: isConcise }">
        <view class="search-box">
          <view class="search-icon">
            <svg t="1756202042594" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6084" width="24" height="24">
              <path d="M446.112323 177.545051c137.567677 0.219798 252.612525 104.59798 266.162424 241.493333 13.562828 136.895354-78.778182 261.818182-213.617777 289.008485-134.852525 27.203232-268.386263-52.156768-308.945455-183.608889s25.018182-272.252121 151.738182-325.779394A267.235556 267.235556 0 0 1 446.112323 177.545051m0-62.060607c-182.794343 0-330.989899 148.195556-330.989899 330.989899s148.195556 330.989899 330.989899 330.989899 330.989899-148.195556 330.989899-330.989899-148.195556-330.989899-330.989899-330.989899z m431.321212 793.341415a30.849293 30.849293 0 0 1-21.94101-9.102223l-157.220202-157.220202c-11.752727-12.179394-11.584646-31.534545 0.37495-43.50707 11.972525-11.972525 31.327677-12.140606 43.494141-0.37495l157.220202 157.220202a31.036768 31.036768 0 0 1 6.723232 33.810101 31.004444 31.004444 0 0 1-28.651313 19.174142z m0 0" p-id="6085" fill="#2c2c2c"></path>
            </svg>
          </view>
          <text class="search-placeholder">{{ $t("chatList.searchPlaceholder") }}</text>
        </view>
      </view>

      <!-- 加载状态 -->
      <view v-if="loading" class="loading-container">
        <view class="loading-spinner"></view>
        <text class="loading-text">{{ $t("common.loading") }}</text>
      </view>

      <!-- 错误状态 -->
      <view v-else-if="errorMessage" class="error-container">
        <view class="error-icon">⚠️</view>
        <text class="error-text">{{ errorMessage }}</text>
        <button class="retry-button" @click="loadChatList">
          <text class="retry-text">{{ $t("common.retry") }}</text>
        </button>
      </view>

      <!-- 空状态 -->
      <view v-else-if="chatList.length === 0" class="empty-container">
        <view class="empty-icon">💬</view>
        <text class="empty-text">{{ $t("chatList.noChats") }}</text>
        <text class="empty-desc">{{ $t("chatList.startChat") }}</text>
      </view>

      <!-- 聊天列表 -->
      <view v-else>
        <view class="chat-item" v-for="(chat, index) in chatList" :key="index" @click="openChat(chat)">
          <view class="avatar-container">
            <image class="avatar" :src="chat.avatar" mode="aspectFill"></image>
            <view class="status-dot" v-if="!isConcise" :class="chat.status"></view>
            <view class="unread-badge" v-if="chat.unreadCount > 0">
              {{ chat.unreadCount }}
            </view>
          </view>
          
          <view class="chat-info">
            <view class="chat-header">
              <text class="chat-name">{{ chat.name }}</text>
              <view class="upload-icon" v-if="!isConcise && chat.hasUpload">
                <text class="upload-arrow">↑</text>
              </view>
            </view>
            <text class="last-message">{{ chat.lastMessage }}</text>
          </view>
          
          <view class="chat-meta">
            <text class="timestamp" v-if="!isConcise">{{ chat.timestamp }}</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 底部导航栏 -->
    <view class="bottom-navigation">
      <view class="nav-item" @click="goToHome">
        <text class="nav-text">{{ $t("index.home") }}</text>
      </view>
      <view class="nav-item active" @click="goToChatList">
        <text class="nav-text">{{ $t("index.messages") }}</text>
      </view>
      <view class="nav-item" @click="goToAIChat">
        <view class="ai-tab">
          <text class="ai-text">AI</text>
        </view>
      </view>
      <view class="nav-item" @click="goToFriendList">
        <text class="nav-text">{{ $t("index.friends") }}</text>
      </view>
      <view class="nav-avatar" @click="goToProfile">
        <image class="avatar-small" src="" mode="aspectFill"></image>
      </view>
    </view>
    

  </view>
</template>

<script>
import { useThemeStore } from '../../store/theme.js'
import { useModeStore } from '@/store/mode.js'

export default {
  name: 'ChatList',
  data() {
    return {
      themeStore: useThemeStore(),
      modeStore: useModeStore(),
      chatList: [],
      loading: false,
      errorMessage: '',
      userId: null
    }
  },
  computed: {
    isConcise() {
      return this.modeStore && this.modeStore.isConcise
    },
    isCare() {
      return this.modeStore && this.modeStore.isCare
    }
  },
  onLoad() {
    this.themeStore.init()
    this.themeStore.applyTheme()
    this.modeStore.init()
    // 获取用户ID，这里假设从本地存储或全局状态获取
    this.userId = uni.getStorageSync('userId') || 1; // 默认用户ID为1
    this.loadChatList();
  },
  methods: {
    // 加载聊天列表
    async loadChatList() {
      if (!this.userId) {
        this.errorMessage = this.$t("chatList.userIdError");
        return;
      }

      this.loading = true;
      this.errorMessage = '';

      try {
        const response = await uni.request({
          url: `${this.$baseUrl}/api/u-entities/user-chat-list/user/${this.userId}`,
          method: 'GET',
          header: {
            'Content-Type': 'application/json'
          }
        });

        if (response.statusCode === 200 && response.data) {
          console.log('原始聊天数据:', response.data);
          // 检查第一条数据的 friendId
          if (response.data.length > 0) {
            console.log('第一条数据的 friendId:', response.data[0].friendId);
          }
          this.chatList = this.convertChatDataToDisplayFormat(response.data);
          console.log('转换后的聊天列表:', this.chatList);
        } else {
          this.errorMessage = this.$t("chatList.loadFailed");
          console.error('加载聊天列表失败:', response);
        }
      } catch (error) {
        this.errorMessage = this.$t("chatList.networkError");
        console.error('加载聊天列表异常:', error);
      } finally {
        this.loading = false;
      }
    },

    // 转换数据库数据为显示格式
    convertChatDataToDisplayFormat(chatData) {
      if (!Array.isArray(chatData)) {
        return [];
      }

      return chatData.map(chat => {
        // 处理头像URL，如果为空或无效则使用默认头像
        let avatarUrl = chat.avatarUrl;
        if (!avatarUrl || avatarUrl.trim() === '' || avatarUrl === 'null') {
          avatarUrl = '/static/default-avatar.png';
        }

        // 使用后端已经处理好的friendName（通过friend_id从user_info表查找）
        // 后端已经在getUserChatListByUserIdFromMemory方法中处理了这个逻辑
        let friendName = chat.friendName;
        if (!friendName || friendName.trim() === '' || friendName === 'null') {
          friendName = this.$t("chatList.unknownUser");
        }

        console.log('聊天记录处理:', {
          id: chat.id,
          friendId: chat.friendId,
          friendName: friendName,
          avatarUrl: avatarUrl
        });

        return {
          id: chat.id,
          name: friendName,
          lastMessage: chat.lastMessage || this.$t("chatList.noMessage"),
          avatar: avatarUrl,
          timestamp: this.formatTimestamp(chat.lastMessageTime),
          status: this.getStatusFromString(chat.status),
          unreadCount: chat.unreadCount || 0,
          hasUpload: false, // 暂时设为false
          type: 'chat',
          sessionId: chat.sessionId,
          friendId: chat.friendId
        };
      });
    },

    // 格式化时间戳
    formatTimestamp(timestamp) {
      if (!timestamp) return this.$t("chatList.justNow");
      
      const date = new Date(timestamp);
      const now = new Date();
      const diff = now - date;
      
      // 小于1分钟
      if (diff < 60000) {
        return '刚刚';
      }
      
      // 小于1小时
      if (diff < 3600000) {
        return this.$t("chatList.minutesAgo", { count: Math.floor(diff / 60000) });
      }
      
      // 小于24小时
      if (diff < 86400000) {
        return this.$t("chatList.hoursAgo", { count: Math.floor(diff / 3600000) });
      }
      
      // 超过24小时，显示具体时间
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`;
    },

    // 从字符串获取状态
    getStatusFromString(status) {
      if (!status) return 'offline';
      
      switch (status.toLowerCase()) {
        case 'online':
        case '在线':
          return 'online';
        case 'away':
        case '离开':
          return 'away';
        case 'busy':
        case '忙碌':
          return 'busy';
        default:
          return 'offline';
      }
    },

    goBack() {
      uni.navigateBack();
    },
    
    async openChat(chat) {
      console.log('点击聊天项:', chat);
      console.log('chat.friendId:', chat.friendId);
      console.log('chat.sessionId:', chat.sessionId);
      
      // 根据类型跳转不同页面
      if (chat.type === 'system') {
        // 系统消息暂时不跳转，显示提示
        uni.showToast({
          title: this.$t("chatList.systemMessageInDev"),
          icon: 'none'
        })
      } else {
        // 如果有未读消息，先清零未读消息数
        if (chat.unreadCount > 0) {
          await this.clearUnreadCount(chat.id);
        }
        
        // 跳转到聊天页面
        uni.navigateTo({
          url: `/pages/chat/chat?friendName=${encodeURIComponent(chat.name)}&friendId=${chat.friendId}&sessionId=${chat.sessionId}`
        })
      }
    },

    // 清零未读消息数
    async clearUnreadCount(chatId) {
      try {
        const response = await uni.request({
          url: `${this.$baseUrl}/api/u-entities/user-chat-list/clear-unread/${chatId}`,
          method: 'POST',
          header: {
            'Content-Type': 'application/json'
          }
        });

        if (response.statusCode === 200) {
          console.log('未读消息数已清零:', response.data);
          // 更新本地数据
          this.updateLocalUnreadCount(chatId);
          
          // 显示成功提示（可选）
          // uni.showToast({
          //   title: '已标记为已读',
          //   icon: 'success',
          //   duration: 1000
          // });
        } else {
          console.error('清零未读消息数失败:', response);
          // 即使失败也更新本地数据，避免用户困惑
          this.updateLocalUnreadCount(chatId);
        }
      } catch (error) {
        console.error('清零未读消息数异常:', error);
      }
    },

    // 更新本地未读消息数
    updateLocalUnreadCount(chatId) {
      const chat = this.chatList.find(item => item.id === chatId);
      if (chat) {
        chat.unreadCount = 0;
        console.log('本地未读消息数已更新:', chat.name, chat.unreadCount);
        
        // 触发视图更新
        this.$forceUpdate();
      }
    },
    
    goToHome() {
      uni.navigateTo({
        url: '/pages/feed/content-feed'
      })
    },
    
    goToChatList() {
      // 当前页面，无需跳转
    },
    
    goToAIChat() {
      uni.navigateTo({
        url: '/pages/ai/ai-chat'
      })
    },
    
    goToFriendList() {
      uni.navigateTo({
        url: '/pages/chat/friend-list'
      })
    },
    
    goToProfile() {
      uni.navigateTo({
        url: '/pages/feed/user-profile'
      })
    },
    

  }
}
</script>

<style lang="scss" scoped>
.chat-list {
  height: 100vh;
  background-color: var(--color-bg);
  color: var(--color-text);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.chat-list.theme-dark {
  background-color: var(--color-bg, #0f1115);
  color: var(--color-text, #e9edf5);
}

.chat-list.concise {
  .search-container {
    padding: 12rpx 24rpx;
  }
  .search-box {
    padding: 6rpx 12rpx;
    border-radius: 20rpx;
  }
  .search-placeholder {
    font-size: 22rpx;
  }
  .chat-content {
    margin-top: 0;
  }
  .chat-item {
    padding: 12rpx 24rpx;
  }
  .avatar {
    width: 88rpx;
    height: 88rpx;
    border-radius: 44rpx;
  }
  .chat-name {
    font-size: 30rpx;
  }
  .last-message {
    font-size: 24rpx;
    max-width: 360rpx;
  }
}

.chat-list.care {
  .nav-header {
    padding: 24rpx 40rpx;
    height: 104rpx;
  }

  .back-button {
    width: 76rpx;
    height: 76rpx;
  }

  .header-title {
    font-size: 38rpx;
  }

  .search-container {
    padding: 24rpx 40rpx;
  }

  .search-box {
    border-radius: 30rpx;
    padding: 14rpx 20rpx;
    max-width: 620rpx;
  }

  .search-icon {
    width: 30rpx;
    height: 30rpx;
    margin-right: 12rpx;
  }

  .search-placeholder {
    font-size: 30rpx;
  }

  .chat-content {
    padding-bottom: 140rpx;
    margin-top: 24rpx;
  }

  .chat-item {
    padding: 24rpx 40rpx;
  }

  .avatar {
    width: 140rpx;
    height: 140rpx;
    border-radius: 70rpx;
  }

  .status-dot {
    width: 24rpx;
    height: 24rpx;
    border-radius: 12rpx;
    border-width: 4rpx;
  }

  .unread-badge {
    min-width: 44rpx;
    height: 44rpx;
    border-radius: 22rpx;
    font-size: 26rpx;
    padding: 0 10rpx;
  }

  .chat-name {
    font-size: 38rpx;
  }

  .last-message {
    font-size: 32rpx;
    max-width: 520rpx;
  }

  .timestamp {
    font-size: 26rpx;
  }

  .bottom-navigation {
    height: 140rpx;
    padding: 0 40rpx;
  }

  .nav-item {
    padding: 20rpx;
    border-radius: 14rpx;
  }

  .nav-text {
    font-size: 28rpx;
  }

  .ai-tab {
    width: 56rpx;
    height: 56rpx;
  }

  .ai-text {
    font-size: 24rpx;
  }

  .nav-avatar {
    width: 56rpx;
    height: 56rpx;
  }
}

.header {
  background-color: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 32rpx;
  height: 88rpx;
}

.back-button {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.header-title {
  font-size: 32rpx;
  color: var(--color-text);
  font-weight: 600;
}

.placeholder {
  width: 60rpx;
}

/* 搜索框样式 */
.search-container {
  padding: 16rpx 32rpx;
  background-color: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
  display: flex;
  justify-content: center;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: var(--color-bg-weak);
  border-radius: 24rpx;
  padding: 8rpx 16rpx;
  width: 90%;
  max-width: 500rpx;
  position: relative;
}

.search-icon {
  width: 24rpx;
  height: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8rpx;
  flex-shrink: 0;
}

.search-icon svg {
  width: 100%;
  height: 100%;
}

.search-placeholder {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  flex: 1;
  text-align: center;
}

.chat-content {
  flex: 1;
  padding: 0;
  overflow-y: auto;
  padding-bottom: 120rpx;
  margin-top: 20rpx;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 16rpx 32rpx;
  background-color: var(--color-card);
  border-bottom: 1rpx solid var(--color-border);
  cursor: pointer;
}

.chat-item:active {
  background-color: var(--color-bg-weak);
}

.avatar-container {
  position: relative;
  margin-right: 24rpx;
}

.avatar {
  width: 110rpx; /* 55x55 */
  height: 110rpx;
  border-radius: 55rpx;
  background-color: var(--color-bg-weak);
}

.status-dot {
  position: absolute;
  bottom: 4rpx;
  right: 4rpx;
  width: 20rpx;
  height: 20rpx;
  border-radius: 10rpx;
  border: 3rpx solid var(--color-card);
}

.status-dot.online {
  background-color: #ff3b30;
}

.status-dot.away {
  background-color: #007aff;
}

.status-dot.busy {
  background-color: #ff9500;
}

.unread-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 32rpx;
  height: 32rpx;
  border-radius: 16rpx;
  background-color: #ff2d55;
  color: #FFFFFF;
  font-size: 20rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.chat-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.chat-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.chat-name {
  font-size: 34rpx; /* 17pt */
  font-weight: 600;
  color: var(--color-text);
}

.upload-icon {
  width: 32rpx;
  height: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-arrow {
  font-size: 24rpx;
  color: var(--color-text-secondary);
}

.last-message {
  font-size: 28rpx; /* 14pt */
  color: var(--color-text-secondary);
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 400rpx;
}

.chat-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8rpx;
}

.timestamp {
  font-size: 20rpx; /* 10pt */
  color: var(--color-text-secondary);
}

/* 底部导航栏样式 */
.bottom-navigation {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120rpx;
  background-color: var(--color-card);
  border-top: 1rpx solid var(--color-border);
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
  background-color: var(--color-bg-weak);
  transform: scale(0.95);
}

.nav-text {
  font-size: 24rpx;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.nav-item.active .nav-text {
  color: var(--color-primary);
  font-weight: 600;
}

.ai-tab {
  width: 48rpx;
  height: 48rpx;
  background: linear-gradient(135deg, var(--color-primary), #FF8E53);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-text {
  font-size: 20rpx;
  color: var(--color-on-primary, #FFFFFF);
  font-weight: 600;
}

.nav-avatar {
  width: 48rpx;
  height: 48rpx;
}

.avatar-small {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(135deg, #FFA500, #FF8C00);
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
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #FF69B4;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 28rpx;
  color: #666666;
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
  color: #ff4757;
  text-align: center;
  margin-bottom: 16rpx;
}

.retry-button {
  background-color: #FF69B4;
  color: #FFFFFF;
  border: none;
  border-radius: 24rpx;
  padding: 16rpx 32rpx;
  font-size: 28rpx;
  cursor: pointer;
  transition: all 0.2s ease;
}

.retry-button:active {
  background-color: #e55a9b;
  transform: scale(0.95);
}

.retry-text {
  color: #FFFFFF;
  font-weight: 600;
}

/* 空状态样式 */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 32rpx;
  gap: 24rpx;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 16rpx;
  opacity: 0.6;
}

.empty-text {
  font-size: 32rpx;
  color: #333333;
  font-weight: 600;
  margin-bottom: 8rpx;
}

.empty-desc {
  font-size: 28rpx;
  color: #666666;
  text-align: center;
}
  

</style> 