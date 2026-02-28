<!-- 添加我的方式设置页面 -->
<template>
  <view class="add-ways-page">
    <!-- 主要内容区域 -->
    <scroll-view class="content" scroll-y>
      <!-- 添加方式设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">添加方式</text>
        </view>
        <view class="section-content">
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">微信号搜索</text>
              <text class="setting-desc">允许他人通过微信号搜索到我</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('wechatSearch')">
              <view class="toggle-track" :class="{ active: settings.wechatSearch }">
                <view class="toggle-thumb" :class="{ active: settings.wechatSearch }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">手机号搜索</text>
              <text class="setting-desc">允许他人通过手机号搜索到我</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('phoneSearch')">
              <view class="toggle-track" :class="{ active: settings.phoneSearch }">
                <view class="toggle-thumb" :class="{ active: settings.phoneSearch }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">QQ号搜索</text>
              <text class="setting-desc">允许他人通过QQ号搜索到我</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('qqSearch')">
              <view class="toggle-track" :class="{ active: settings.qqSearch }">
                <view class="toggle-thumb" :class="{ active: settings.qqSearch }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">群聊添加</text>
              <text class="setting-desc">允许他人通过群聊添加我为好友</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('groupAdd')">
              <view class="toggle-track" :class="{ active: settings.groupAdd }">
                <view class="toggle-thumb" :class="{ active: settings.groupAdd }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">名片分享</text>
              <text class="setting-desc">允许他人通过名片分享添加我为好友</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('cardShare')">
              <view class="toggle-track" :class="{ active: settings.cardShare }">
                <view class="toggle-thumb" :class="{ active: settings.cardShare }"></view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 隐私说明 -->
      <view class="privacy-note">
        <text class="note-text">关闭某些添加方式可能会影响他人找到您，建议根据实际需要合理设置</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  name: 'AddWays',
  data() {
    return {
      userId: null,
      loading: false,
      settings: {
        wechatSearch: true,
        phoneSearch: true,
        qqSearch: true,
        groupAdd: true,
        cardShare: true
      }
    }
  },
  onLoad() {
    this.userId = uni.getStorageSync('userId') || 1000100
    this.loadSettings()
  },
  methods: {
    async loadSettings() {
      if (!this.userId) {
        return
      }

      this.loading = true
      try {
        const res = await uni.request({
          url: `${this.$baseUrl}/api/u-entities/privacy-settings/${this.userId}`,
          method: 'GET',
          header: {
            'Content-Type': 'application/json'
          }
        })

        if (res.statusCode === 200 && res.data && res.data.success) {
          const data = res.data.data || {}
          if (data.wechatSearch !== undefined) this.settings.wechatSearch = data.wechatSearch
          if (data.phoneSearch !== undefined) this.settings.phoneSearch = data.phoneSearch
          if (data.qqSearch !== undefined) this.settings.qqSearch = data.qqSearch
          if (data.groupAdd !== undefined) this.settings.groupAdd = data.groupAdd
          if (data.cardShare !== undefined) this.settings.cardShare = data.cardShare
        }
      } catch (e) {
        console.error('加载设置失败:', e)
      } finally {
        this.loading = false
      }
    },
    
    async toggleSetting(key) {
      if (!this.userId) {
        return
      }

      const oldValue = this.settings[key]
      const newValue = !oldValue
      this.settings[key] = newValue

      try {
        const res = await uni.request({
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
        })

        if (res.statusCode === 200 && res.data && res.data.success) {
          uni.showToast({
            title: this.$t('privacySettings.settingSaved'),
            icon: 'success'
          })
        } else {
          this.settings[key] = oldValue
          uni.showToast({
            title: (res.data && res.data.message) ? res.data.message : this.$t('privacySettings.saveFailed'),
            icon: 'none'
          })
        }
      } catch (e) {
        this.settings[key] = oldValue
        uni.showToast({
          title: this.$t('common.networkError'),
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.add-ways-page {
  min-height: 100vh;
  background-color: #FAFAFA;
}

.content {
  flex: 1;
  padding: 0;
}

.section {
  background-color: #FFFFFF;
  border-radius: 0;
  margin-bottom: 0;
  overflow: hidden;
  border-bottom: 1rpx solid #efefef;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 16rpx 32rpx;
  background-color: #FAFAFA;
}

.section-title {
  font-size: 20rpx;
  font-weight: 400;
  color: #666666;
  text-transform: uppercase;
}

.section-content {
  padding: 0;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 32rpx;
  border-bottom: 1rpx solid #efefef;
  background-color: #FFFFFF;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.setting-label {
  font-size: 32rpx;
  color: #000000;
  font-weight: 400;
}

.setting-desc {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.4;
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

.privacy-note {
  padding: 32rpx;
  background-color: #FFFFFF;
  margin-top: 20rpx;
}

.note-text {
  font-size: 28rpx;
  color: #999999;
  line-height: 1.5;
  text-align: center;
}
</style>
