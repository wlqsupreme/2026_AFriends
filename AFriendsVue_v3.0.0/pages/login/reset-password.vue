<template>
  <view class="login-container">
    <!-- 主内容区域 -->
    <view class="main-content">
      <!-- 应用Logo -->
      <view class="app-header">
        <image class="app-logo" src="/static/afriends-logo.png" mode="aspectFit"></image>
      </view>
      
      <!-- 问候语和欢迎文字 -->
      <view class="greeting-section">
        <text class="greeting-text">修改密码</text>
        <text class="welcome-text">请输入绑定的手机号码</text>
      </view>
      
      <!-- 登录表单 -->
      <view class="login-form">
        <!-- 手机号输入框 -->
        <view class="input-group">
          <text class="input-label">手机号</text>
          <view class="input-wrapper">
            <input 
              class="form-input" 
              type="text" 
              v-model="phone"
              maxlength="11"
            />
            <view class="input-underline"></view>
          </view>
        </view>
        
        <!-- 返回登录链接 -->
        <view class="back-login-section">
          <text class="back-login-text" @click="goToPasswordLogin">返回登录</text>
        </view>
        
        <!-- 用户协议 -->
        <view class="terms-section">
          <text class="terms-link" @click="goToTermsOfService">服务条款</text>
        </view>
        
        <!-- 登录按钮 -->
        <button class="login-btn" @click="handleLogin">
          <text class="btn-text">获取验证码</text>
        </button>
        
        <!-- 注册链接 -->
        <view class="register-section">
          <text class="register-text">没有账号?</text>
          <text class="register-link" @click="goToRegister">注册账号</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useThemeStore } from '@/store/theme.js';

export default {
  data() {
    return {
      phone: '',
      userId: null, // 用户ID，从 account-security 页面传递
      storedPhoneNumber: '' // 存储的手机号，从 account-security 页面传递
    }
  },
  onLoad(options) {
    // 接收从其他页面传递的userId参数
    if (options.userId) {
      this.userId = parseInt(options.userId);
      console.log('重置密码页面接收到用户ID:', this.userId);
    }
    // 接收从其他页面传递的phoneNumber参数
    if (options.phoneNumber) {
      this.storedPhoneNumber = decodeURIComponent(options.phoneNumber);
      console.log('重置密码页面接收到手机号:', this.storedPhoneNumber);
    }
    
    // 初始化主题
    const themeStore = useThemeStore();
    themeStore.init();
    themeStore.applyTheme();
  },
  methods: {
    handleLogin() {
      if (!this.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      
      // 检查输入的手机号与存储的手机号是否一致
      if (this.storedPhoneNumber && this.phone !== this.storedPhoneNumber) {
        uni.showToast({
          title: '请输入当前绑定的手机号',
          icon: 'none'
        })
        return
      }
      
      // 跳转到验证码输入界面，传递手机号和用户ID
      let url = `/pages/login/verification-code-resetPassword?phone=${this.phone}`;
      if (this.userId) {
        url += `&userId=${this.userId}`;
      }
      uni.navigateTo({
        url: url
      })
    },
    
    goToPasswordLogin() {
      uni.navigateTo({
        url: '/pages/login/login-replica'
      })
    },
    
    goToRegister() {
      uni.navigateTo({
        url: '/pages/login/phone-register'
      })
    },
    
    goToTermsOfService() {
      uni.navigateTo({
        url: '/pages/settings/terms-of-service'
      })
    }
    

  }
}
</script>

<style scoped>
/* 全局样式已内联 */

.login-container {
  width: 375px;
  height: 812px;
  background-color: var(--color-bg, #FFFFFF);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin: 0 auto;
}

.main-content {
  flex: 1;
  padding: 0px 32px 20px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.app-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 0px;
  margin-top: 50px;
}

.app-logo {
  width: 240px;
  height: 100px;
  margin: 0;
  padding: 0;
  display: block;
  box-sizing: border-box;
  object-fit: contain;
}

.greeting-section {
  width: 100%;
  max-width: 320px;
  margin-bottom: 45px;
  text-align: left;
  padding-left: 0px;
}

.greeting-text {
  font-size: 36px;
  font-weight: bold;
  color: var(--color-text, #333333);
  display: block;
  margin-bottom: 8px;
}

.welcome-text {
  font-size: 12px;
  color: var(--color-text-secondary, #666666);
  display: block;
  line-height: 1.5;
}

.login-form {
  width: 100%;
  max-width: 320px;
  padding-left: 16px;
  padding-right: 16px;
}

.input-group {
  margin-bottom: 5px;
}

.input-label {
  font-size: 12px;
  color: var(--color-text-secondary, #666666);
  display: block;
  margin-bottom: 0px;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  height: 38px;
  padding: 0 16px;
  font-size: 16px;
  color: var(--color-text, #333333);
  background-color: transparent;
  border: none;
  outline: none;
}

.placeholder {
  color: #999999;
}

.input-underline {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background-color: #EF0056;
}

.form-input:focus + .input-underline {
  background-color: #EF0056;
}



.back-login-section {
  display: flex;
  justify-content: center;
  margin-bottom: 238px;
  margin-top: 20px;
}

.back-login-text {
  font-size: 12px;
  color: #1286DD;
  cursor: pointer;
}

.terms-section {
  text-align: center;
  margin-bottom: 5px;
  line-height: 1.5;
  padding: 0 16px;
}

.terms-text {
  font-size: 12px;
  color: var(--color-text-secondary, #999999);
}

.terms-link {
  font-size: 12px;
  color: #1286DD;
  cursor: pointer;
}

.login-btn {
  width: 100%;
  height: 44px;
  background-color: #EF0056;
  border: none;
  border-radius: 10px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.btn-text {
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
}

.register-section {
  text-align: center;
  padding: 0 16px;
}

.register-text {
  font-size: 14px;
  color: var(--color-text-secondary, #999999);
}

.register-link {
  font-size: 14px;
  color: #1286DD;
  cursor: pointer;
  margin-left: 4px;
}

/* 响应式设计 */
@media (max-width: 375px) {
  .main-content {
    padding: 0px 24px 20px 24px;
  }
  
  .app-logo {
    width: 200px;
    height: 80px;
    box-sizing: border-box;
    object-fit: contain;
  }
  
  .greeting-section {
    max-width: 280px;
  }
  
  .login-form {
    max-width: 280px;
  }
}

@media (min-width: 414px) {
  .main-content {
    padding: 0px 40px 30px 40px;
  }
  
  .app-logo {
    width: 140px;
    height: 50px;
    box-sizing: border-box;
    object-fit: contain;
  }
  
  .greeting-section {
    max-width: 360px;
  }
  
  .login-form {
    max-width: 360px;
  }
}
</style> 