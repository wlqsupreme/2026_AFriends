<template>
  <view class="login-container" :class="themeStore.themeClass">
    <!-- 主内容区域 -->
    <view class="main-content">
      <!-- 应用Logo -->
      <view class="app-header">
        <image class="app-logo" src="/static/afriends-logo.png" mode="aspectFit"></image>
      </view>
      
      <!-- 问候语和欢迎文字 -->
      <view class="greeting-section">
        <text class="greeting-text">你好呀</text>
        <text class="welcome-text">欢迎来到Afriends, 请登录并开始你的交友之旅</text>
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
        
        <!-- 登录选项 -->
        <view class="login-options">
          <text class="option-text" @click="goToPasswordLogin">使用账号密码登录</text>
        </view>
        
        <!-- 第三方登录 -->
        <view class="third-party-login">
          <text class="section-title">或者你可以通过这些来登录</text>
          <view class="social-icons">
            <view class="social-icon" @click="goToWechatLogin">
              <image class="social-icon-img" src="/static/wechat-icon.png" mode="aspectFit"></image>
            </view>
            <view class="social-icon" @click="goToQQLogin">
              <image class="social-icon-img" src="/static/qq-icon.png" mode="aspectFit"></image>
            </view>
            <view class="social-icon" @click="goToDouyinLogin">
              <image class="social-icon-img" src="/static/douyin-icon.png" mode="aspectFit"></image>
            </view>
          </view>
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
        
        <!-- 主界面跳转按钮 -->
        <!-- <view class="main-page-section">
          <button class="main-page-btn" @click="goToMainPage">进入主界面</button>
        </view> -->
      </view>
    </view>
  </view>
</template>

<script>
import { useThemeStore } from '../../store/theme.js'

export default {
  data() {
    return {
      themeStore: useThemeStore(),
      phone: ''
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
    handleLogin() {
      if (!this.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      
      // 验证手机号格式
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        uni.showToast({
          title: '请输入正确的手机号',
          icon: 'none'
        })
        return
      }
      
      // 显示发送成功提示
      uni.showToast({
        title: '验证码已发送',
        icon: 'success',
        duration: 1500
      })
      
      // 延迟跳转到验证码输入界面
      setTimeout(() => {
        uni.navigateTo({
          url: '/pages/login/verification-code-login'
        })
      }, 1500)
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
    
    goToWechatBind() {
      uni.navigateTo({
        url: '/pages/login/login-unbound'
      })
    },
    
    goToMainPage() {
      uni.navigateTo({
        url: '/pages/index/index'
      })
    },

    goToTermsOfService() {
      uni.navigateTo({
        url: '/pages/settings/terms-of-service'
      })
    },

    goToWechatLogin() {
      uni.showToast({
        title: '即将跳转至微信',
        icon: 'none',
        duration: 2000
      })
    },

    goToQQLogin() {
      uni.showToast({
        title: '即将跳转至QQ',
        icon: 'none',
        duration: 2000
      })
    },

    goToDouyinLogin() {
      uni.showToast({
        title: '即将跳转至抖音',
        icon: 'none',
        duration: 2000
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
  background-color: var(--color-bg);
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
  color: var(--color-text);
  display: block;
  margin-bottom: 8px;
}

.welcome-text {
  font-size: 12px;
  color: var(--color-text-secondary);
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
  color: var(--color-text-secondary);
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
  color: var(--color-text);
  background-color: transparent;
  border: none;
  outline: none;
}

.placeholder {
  color: var(--color-text-secondary);
}

.input-underline {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background-color: var(--color-primary);
}

.form-input:focus + .input-underline {
  background-color: var(--color-primary);
}

.login-options {
  display: flex;
  justify-content: center;
  margin-bottom: 5px;
  margin-top: 75px;
}

.option-text {
  font-size: 12px;
  color: var(--color-primary);
  cursor: pointer;
}

.forgot-password {
  margin-top: 10px;
  text-align: right;
  padding-right: 16px;
}

.third-party-login {
  margin-bottom: 100px;
  text-align: center;
  padding: 0 16px;
}

.section-title {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin-bottom: 20px;
  display: block;
}

.social-icons {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.social-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.social-icon-img {
  width: 32px;
  height: 32px;
}

.terms-section {
  text-align: center;
  margin-bottom: 5px;
  line-height: 1.5;
  padding: 0 16px;
}

.terms-text {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.terms-link {
  font-size: 12px;
  color: var(--color-primary);
  cursor: pointer;
}

.login-btn {
  width: 100%;
  height: 44px;
  background-color: var(--color-primary);
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
  color: var(--color-on-primary);
}

.register-section {
  text-align: center;
  padding: 0 16px;
}

.register-text {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.register-link {
  font-size: 14px;
  color: var(--color-primary);
  cursor: pointer;
  margin-left: 4px;
}

.main-page-section {
  text-align: center;
  margin-top: 20px;
}

.main-page-btn {
  width: 200px;
  height: 44px;
  background-color: var(--color-primary);
  border: none;
  border-radius: 10px;
  color: var(--color-on-primary);
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
}

.main-page-btn:hover {
  opacity: 0.9;
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