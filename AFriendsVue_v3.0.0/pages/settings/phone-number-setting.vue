<template>
  <view class="phone-setting-container">
    <!-- 头部导航 -->
    <view class="header">
      <view class="back-button" @click="goBack">
        <view class="back-arrow"></view>
      </view>
      <text class="page-title">{{ pageTitle }}</text>
      <view class="placeholder"></view>
    </view>
    
    <!-- 主要内容 -->
    <view class="main-content">
      <!-- 当前手机号信息 -->
      <view class="current-phone-section" v-if="hasPhoneBound && !showPhoneForm">
        <view class="section-title">当前绑定手机号</view>
        <view class="phone-display">
          <text class="phone-number">{{ maskedPhoneNumber }}</text>
        </view>
        <button class="change-btn" @click="changePhoneNumber">
          更换手机号
        </button>
      </view>
      
      <!-- 未绑定手机号 -->
      <view class="no-phone-section" v-else-if="!hasPhoneBound && !showPhoneForm">
        <view class="section-title">您还未绑定手机号</view>
        <button class="bind-btn" @click="bindPhoneNumber">
          立即绑定
        </button>
      </view>
      
      <!-- 绑定/更换手机号表单 -->
      <view class="phone-form" v-if="showPhoneForm">
        <view class="input-group">
          <text class="input-label">新手机号</text>
          <view class="input-wrapper">
            <input 
              class="form-input" 
              type="number" 
              v-model="newPhoneNumber"
              placeholder="请输入新手机号"
              maxlength="11"
            />
            <view class="input-underline"></view>
          </view>
        </view>
        
        <button class="next-btn" @click="nextStep" :disabled="!isValidPhoneNumber">
          下一步
        </button>
        
        <view class="cancel-action" @click="cancelOperation">
          取消
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';

export default {
  name: 'PhoneNumberSetting',
  data() {
    return {
      // 用户ID
      userId: 1000100,
      // 用户当前绑定的手机号
      currentPhoneNumber: '',
      // 是否已经绑定手机号
      hasPhoneBound: false,
      // 是否显示手机号输入表单
      showPhoneForm: false,
      // 新手机号
      newPhoneNumber: '',
      // 页面标题
      pageTitle: '手机号设置'
    }
  },
  computed: {
    // 遮盖后的手机号显示
    maskedPhoneNumber() {
      if (!this.currentPhoneNumber) return '';
      return this.currentPhoneNumber.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
    },
    
    // 验证手机号格式
    isValidPhoneNumber() {
      const phoneRegex = /^1[3-9]\d{9}$/;
      return phoneRegex.test(this.newPhoneNumber);
    }
  },
  onLoad(options) {
    // 接收从其他页面传递的参数
    if (options.userId) {
      this.userId = parseInt(options.userId);
    }
    if (options.phoneNumber) {
      this.currentPhoneNumber = decodeURIComponent(options.phoneNumber);
    }
    if (options.hasPhoneBound) {
      this.hasPhoneBound = options.hasPhoneBound === 'true';
    }
    
    // 初始化主题
    const themeStore = useThemeStore();
    themeStore.init();
    themeStore.applyTheme();
    
    // 如果未传递手机号信息，则从后端获取
    if (!this.currentPhoneNumber) {
      this.fetchUserPhoneNumber();
    }
  },
  methods: {
    // 从后端获取用户手机号信息
    async fetchUserPhoneNumber() {
      try {
        console.log('开始获取用户手机号信息，用户ID:', this.userId);
        
        const response = await uni.request({
          url: `${this.$baseUrl}/api/u-entities/user-base/${this.userId}`,
          method: 'GET',
          header: {
            'Content-Type': 'application/json'
          }
        });
        
        console.log('用户基础信息API响应:', response);
        
        if (response.statusCode === 200 && response.data.success) {
          this.currentPhoneNumber = response.data.loginTelAccount || '';
          this.hasPhoneBound = response.data.hasPhoneBound || false;
          console.log('成功获取用户手机号信息，手机号:', this.currentPhoneNumber);
        } else {
          console.error('获取用户手机号信息失败:', response.data.message);
          uni.showToast({
            title: response.data.message || '获取失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('获取用户手机号信息异常:', error);
        uni.showToast({
          title: '获取失败',
          icon: 'none'
        });
      }
    },
    
    // 更换手机号
    changePhoneNumber() {
      this.showPhoneForm = true;
    },
    
    // 绑定手机号
    bindPhoneNumber() {
      this.showPhoneForm = true;
    },
    
    // 下一步，跳转到验证码页面
    nextStep() {
      if (!this.isValidPhoneNumber) {
        uni.showToast({
          title: '请输入正确的手机号',
          icon: 'none'
        });
        return;
      }
      
      // 跳转到验证码页面，传递手机号、用户ID和操作类型
      // 验证码验证后会在验证码页面调用接口更新手机号
      uni.navigateTo({
        url: `/pages/settings/phone-verification?phoneNumber=${this.newPhoneNumber}&userId=${this.userId}&operationType=${this.hasPhoneBound ? 'change' : 'bind'}`
      });
    },
    
    // 取消操作
    cancelOperation() {
      this.showPhoneForm = false;
      this.newPhoneNumber = '';
    },
    
    // 返回上一页
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style scoped>
.phone-setting-container {
  width: 100%;
  min-height: 100vh;
  background-color: var(--color-bg, #f5f5f5);
  display: flex;
  flex-direction: column;
}

/* 头部导航 */
.header {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;
  background-color: var(--color-card, #FFFFFF);
  border-bottom: 1rpx solid var(--color-border, #F0F0F0);
}

.back-button {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-arrow {
  width: 0;
  height: 0;
  border-right: 12rpx solid var(--color-text-secondary, #666666);
  border-top: 8rpx solid transparent;
  border-bottom: 8rpx solid transparent;
}

.page-title {
  font-size: 32rpx;
  color: var(--color-text, #333333);
  font-weight: 600;
}

.placeholder {
  width: 48rpx;
}

/* 主要内容 */
.main-content {
  flex: 1;
  padding: 24rpx 32rpx;
  display: flex;
  flex-direction: column;
}

.section-title {
  font-size: 28rpx;
  color: var(--color-text-secondary, #666666);
  margin-bottom: 24rpx;
}

/* 当前手机号信息 */
.current-phone-section {
  background-color: var(--color-card, #FFFFFF);
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.phone-display {
  text-align: center;
  margin-bottom: 32rpx;
}

.phone-number {
  font-size: 36rpx;
  color: var(--color-text, #333333);
  font-weight: 500;
}

.change-btn {
  width: 100%;
  height: 88rpx;
  background-color: var(--color-card, #FFFFFF);
  border: 2rpx solid #EF0056;
  border-radius: 12rpx;
  color: #EF0056;
  font-size: 32rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 未绑定手机号 */
.no-phone-section {
  background-color: var(--color-card, #FFFFFF);
  border-radius: 16rpx;
  padding: 48rpx 32rpx;
  margin-bottom: 24rpx;
  text-align: center;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.bind-btn {
  width: 100%;
  height: 88rpx;
  background-color: #EF0056;
  border: none;
  border-radius: 12rpx;
  color: #FFFFFF;
  font-size: 32rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 手机号表单 */
.phone-form {
  background-color: var(--color-card, #FFFFFF);
  border-radius: 16rpx;
  padding: 32rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.input-group {
  margin-bottom: 40rpx;
}

.input-label {
  font-size: 28rpx;
  color: var(--color-text-secondary, #666666);
  display: block;
  margin-bottom: 16rpx;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  height: 72rpx;
  padding: 0 16rpx;
  font-size: 28rpx;
  color: var(--color-text, #333333);
  background-color: transparent;
  border: none;
  outline: none;
  box-sizing: border-box;
}

.input-underline {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2rpx;
  background-color: #EF0056;
}

.next-btn {
  width: 100%;
  height: 88rpx;
  background-color: #EF0056;
  border: none;
  border-radius: 12rpx;
  color: #FFFFFF;
  font-size: 32rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24rpx;
}

.next-btn:disabled {
  background-color: #CCCCCC;
  color: #999999;
}

.cancel-action {
  text-align: center;
  font-size: 28rpx;
  color: var(--color-text-secondary, #666666);
  cursor: pointer;
}
</style>