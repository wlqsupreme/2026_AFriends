<template>
  <view class="privacy-settings" :class="{ care: isCare }">
    <!-- 顶部导航栏 -->
    <!-- <view class="nav-bar">
      <view class="nav-left" @click="goBack">
        <text class="back-arrow">‹</text>
      </view>
      <view class="nav-title">{{ $t('privacySettings.title') }}</view>
      <view class="nav-right"></view>
    </view> -->

    <!-- 主要内容区域 -->
    <scroll-view class="content" scroll-y>
      <!-- 互动设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">{{ $t('privacySettings.interaction.title') }}</text>
        </view>
        <view class="section-content">
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.interaction.oneClickProtection') }}</text>
              <text class="setting-desc">{{ $t('privacySettings.interaction.oneClickProtectionDesc') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('oneClickProtection')">
              <view class="toggle-track" :class="{ active: settings.oneClickProtection }">
                <view class="toggle-thumb" :class="{ active: settings.oneClickProtection }"></view>
              </view>
            </view>
          </view>
          
          <!-- <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.interaction.showMyStatus') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('showMyStatus')">
              <view class="toggle-track" :class="{ active: settings.showMyStatus }">
                <view class="toggle-thumb" :class="{ active: settings.showMyStatus }"></view>
              </view>
            </view>
          </view> -->
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.interaction.allowFriendsComment') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('allowFriendsComment')">
              <view class="toggle-track" :class="{ active: settings.allowFriendsComment }">
                <view class="toggle-thumb" :class="{ active: settings.allowFriendsComment }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.interaction.allowFriendsAt') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('allowFriendsAt')">
              <view class="toggle-track" :class="{ active: settings.allowFriendsAt }">
                <view class="toggle-thumb" :class="{ active: settings.allowFriendsAt }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.interaction.publicCollections') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('publicCollections')">
              <view class="toggle-track" :class="{ active: settings.publicCollections }">
                <view class="toggle-thumb" :class="{ active: settings.publicCollections }"></view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 朋友设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">{{ $t('privacySettings.friends.title') }}</text>
        </view>
        <view class="section-content">
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.friends.friendVerification') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('friendVerification')">
              <view class="toggle-track" :class="{ active: settings.friendVerification }">
                <view class="toggle-thumb" :class="{ active: settings.friendVerification }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item" @click="goToAddWays">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.friends.addWays') }}</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.friends.recommendFriends') }}</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('recommendFriends')">
              <view class="toggle-track" :class="{ active: settings.recommendFriends }">
                <view class="toggle-thumb" :class="{ active: settings.recommendFriends }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item" @click="goToBlacklist">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.friends.blockedUsers') }}</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 权限设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">{{ $t('privacySettings.permissions.title') }}</text>
        </view>
        <view class="section-content">
          <view class="setting-item" @click="goToSystemPermissions">
            <view class="setting-info">
              <text class="setting-label">{{ $t('privacySettings.permissions.systemPermissions') }}</text>
              <text class="setting-desc">{{ $t('privacySettings.permissions.systemPermissionsDesc') }}</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';
import { useModeStore } from '@/store/mode.js';

export default {
  name: 'PrivacySettings',
  data() {
    return {
      userId: null,
      loading: false,
      settings: {
        oneClickProtection: false,
        showMyStatus: false,
        allowFriendsComment: false,
        allowFriendsAt: false,
        publicCollections: false,
        friendVerification: false,
        recommendFriends: false
      }
    }
  },
  onLoad() {
    // 获取用户ID
    this.userId = uni.getStorageSync('userId') || 1000100; // 默认值，实际应该从登录状态获取
    this.loadPrivacySettings();
    
    // 初始化主题
    const themeStore = useThemeStore();
    themeStore.init();
    themeStore.applyTheme();

    const modeStore = useModeStore();
    modeStore.init();
  },
  onShow() {
    const modeStore = useModeStore();
    modeStore.init();
  },
  computed: {
    isCare() {
      const modeStore = useModeStore();
      return !!modeStore.isCare;
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    

    
    // 加载隐私设置
    async loadPrivacySettings() {
      if (!this.userId) {
        console.log('用户ID不存在，无法加载隐私设置');
        return;
      }

      try {
        this.loading = true;
        console.log('开始加载隐私设置，userId:', this.userId);
        
        const response = await uni.request({
          url: `${this.$baseUrl}/api/u-entities/privacy-settings/${this.userId}`,
          method: 'GET',
          header: {
            'Content-Type': 'application/json'
          }
        });

        console.log('隐私设置API响应状态码:', response.statusCode);
        console.log('隐私设置API响应数据:', response.data);

        if (response.statusCode === 200 && response.data.success) {
          const settingsData = response.data.data || {};
          // 更新设置值，只更新互动设置相关的项
          if (settingsData.oneClickProtection !== undefined) {
            this.settings.oneClickProtection = settingsData.oneClickProtection;
          }
          if (settingsData.allowFriendsComment !== undefined) {
            this.settings.allowFriendsComment = settingsData.allowFriendsComment;
          }
          if (settingsData.allowFriendsAt !== undefined) {
            this.settings.allowFriendsAt = settingsData.allowFriendsAt;
          }
          if (settingsData.publicCollections !== undefined) {
            this.settings.publicCollections = settingsData.publicCollections;
          }
          if (settingsData.friendVerification !== undefined) {
            this.settings.friendVerification = settingsData.friendVerification;
          }
          if (settingsData.recommendFriends !== undefined) {
            this.settings.recommendFriends = settingsData.recommendFriends;
          }
          console.log('隐私设置加载成功:', this.settings);
        } else {
          console.error('加载隐私设置失败:', response.data.message);
          uni.showToast({
            title: this.$t('privacySettings.loadFailed'),
            icon: 'none',
            duration: 2000
          });
        }
      } catch (error) {
        console.error('加载隐私设置异常:', error);
        uni.showToast({
          title: this.$t('common.networkError'),
          icon: 'none',
          duration: 2000
        });
      } finally {
        this.loading = false;
      }
    },

    // 切换设置
    async toggleSetting(key) {
      if (!this.userId) {
        uni.showToast({
          title: this.$t('privacySettings.userIdError'),
          icon: 'error'
        });
        return;
      }

      // 先更新本地状态
      const oldValue = this.settings[key];
      this.settings[key] = !this.settings[key];
      const newValue = this.settings[key];

      try {
        console.log(`切换设置: ${key}, 新值: ${newValue}`);
        
        const response = await uni.request({
          url: '${this.$baseUrl}/api/u-entities/privacy-settings/save',
          method: 'POST',
          header: {
            'Content-Type': 'application/json'
          },
          data: {
            userId: this.userId,
            settingKey: key,
            value: newValue
          }
        });

        console.log('保存设置API响应状态码:', response.statusCode);
        console.log('保存设置API响应数据:', response.data);

        if (response.statusCode === 200 && response.data.success) {
          uni.showToast({
            title: this.$t('privacySettings.settingSaved'),
            icon: 'success',
            duration: 1000
          });
          
          // 保存成功后重新加载设置数据以确保联动设置生效
          await this.loadPrivacySettings();
        } else {
          // 保存失败，恢复原值
          this.settings[key] = oldValue;
          uni.showToast({
            title: response.data.message || this.$t('privacySettings.saveFailed'),
            icon: 'error',
            duration: 2000
          });
        }
      } catch (error) {
        // 保存失败，恢复原值
        this.settings[key] = oldValue;
        console.error('保存设置异常:', error);
        uni.showToast({
          title: this.$t('privacySettings.networkErrorRetry'),
          icon: 'error',
          duration: 2000
        });
      }
    },
    
    goToSystemPermissions() {
      // 跳转到系统权限管理页面
      uni.navigateTo({
        url: '/pages/settings/system-permissions'
      })
    },
    
    goToAddWays() {
      // 跳转到添加我的方式页面
      uni.navigateTo({
        url: '/pages/settings/add-ways'
      })
    },
    
    goToBlacklist() {
      // 跳转到黑名单用户页面
      uni.navigateTo({
        url: '/pages/settings/blocked-users'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.privacy-settings {
  min-height: 100vh;
  background-color: var(--color-bg, #FAFAFA);
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 32rpx;
  background-color: var(--color-card, #ffffff);
  border-bottom: 1rpx solid var(--color-border, #efefef);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.back-arrow {
  font-size: 48rpx;
  color: var(--color-text, #000000);
  font-weight: 300;
  line-height: 1;
}

.nav-title {
  font-size: 36rpx;
  font-weight: 600;
  color: var(--color-text, #000000);
}

.nav-right {
  width: 60rpx;
}

.content {
  flex: 1;
  padding: 0;
}

.section {
  background-color: var(--color-card, #FFFFFF);
  border-radius: 0;
  margin-bottom: 0;
  overflow: hidden;
  border-bottom: 1rpx solid var(--color-border, #efefef);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 16rpx 32rpx;
  background-color: var(--color-bg, #FAFAFA);
}

.section-title {
  font-size: 20rpx; /* 分类标题 10pt */
  font-weight: 400;
  color: var(--color-text-secondary, #666666);
  text-transform: uppercase;
}

.arrow-right {
  width: 40rpx;
  height: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.arrow-icon {
  font-size: 32rpx;
  color: var(--color-text, #000000);
  font-weight: 300;
}

.section-content {
  padding: 0;
}

.setting-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.setting-desc {
  font-size: 28rpx; /* 解释文字 14pt */
  color: var(--color-text-secondary, #666666);
  line-height: 1.4;
  font-weight: 400;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 32rpx;
  border-bottom: 1rpx solid var(--color-border, #efefef);
  background-color: var(--color-card, #FFFFFF);
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-label {
  font-size: 32rpx; /* 功能文字 16pt */
  color: var(--color-text, #000000);
  font-weight: 400;
}

.toggle-switch {
  width: 100rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-track {
  width: 100rpx;
  height: 56rpx;
  background-color: #e5e5e5;
  border-radius: 28rpx;
  position: relative;
  transition: background-color 0.3s ease;
}

.toggle-track.active {
  background-color: #007aff;
}

.toggle-thumb {
  width: 52rpx;
  height: 52rpx;
  background-color: #ffffff;
  border-radius: 26rpx;
  position: absolute;
  left: 2rpx;
  top: 2rpx;
  transition: transform 0.3s ease;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.toggle-thumb.active {
  transform: translateX(44rpx);
}

/* 响应式设计 */
@media (max-width: 750rpx) {
  .content {
    padding: 16rpx;
  }
  
  .section {
    margin-bottom: 16rpx;
  }
  
  .section-header {
    padding: 24rpx 24rpx 16rpx;
  }
  
  .section-content {
    padding: 16rpx 24rpx 24rpx;
  }
  
  .setting-item {
    padding: 20rpx 0;
  }
}

.privacy-settings.care {
  .section-header {
    padding: 24rpx 40rpx;
  }

  .section-title {
    font-size: 32rpx;
  }

  .setting-item {
    padding: 28rpx 40rpx;
    min-height: 120rpx;
    box-sizing: border-box;
  }

  .setting-label {
    font-size: 36rpx;
    line-height: 1.5;
  }

  .setting-desc {
    font-size: 32rpx;
    line-height: 1.6;
  }

  .toggle-switch {
    width: 132rpx;
    height: 76rpx;
  }

  .toggle-track {
    width: 132rpx;
    height: 72rpx;
    border-radius: 36rpx;
  }

  .toggle-thumb {
    width: 64rpx;
    height: 64rpx;
    border-radius: 32rpx;
    left: 4rpx;
    top: 4rpx;
  }

  .toggle-thumb.active {
    transform: translateX(60rpx);
  }

  .arrow-icon {
    font-size: 40rpx;
  }
}
</style>