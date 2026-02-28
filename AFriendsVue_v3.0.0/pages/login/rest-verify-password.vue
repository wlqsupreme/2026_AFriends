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
        <text class="greeting-text">修改密码</text>
        <text class="welcome-text">请您修改当前账号密码，在修改后需要重新登录</text>
      </view>
      
      <!-- 登录表单 -->
      <view class="login-form">
                 <!-- 新密码输入框 -->
         <view class="input-group">
           <text class="input-label">新密码</text>
           <view class="input-wrapper">
                           <input 
                class="form-input" 
                type="password" 
                v-model="username"
              />
             <view class="input-underline"></view>
           </view>
         </view>
        
                 <!-- 确认密码输入框 -->
         <view class="input-group">
           <text class="input-label">请再次输入新密码</text>
           <view class="input-wrapper">
                           <input 
                class="form-input" 
                type="password" 
                v-model="password"
              />
             <view class="input-underline"></view>
           </view>
         </view>
        

        
        <!-- 用户协议 -->
        <view class="terms-section">
          <text class="terms-link" @click="goToTermsOfService">服务条款</text>
        </view>
        
        <!-- 确认按钮 -->
        <button class="login-btn" @click="handleConfirm" :disabled="!isFormValid">
          <text class="btn-text">修改密码</text>
        </button>
        
        <!-- 返回登录链接 -->
        <view class="register-section">
          <text class="register-text">没有账号？</text>
          <text class="register-link" @click="goToLogin">注册账号</text>
        </view>
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
      username: '', // 新密码
      password: '', // 确认密码
      userId: null // 用户ID，从验证码页面传递
    }
  },
  onLoad(options) {
    this.themeStore.init()
    this.themeStore.applyTheme()
    // 接收从验证码页面传递的userId参数
    if (options.userId) {
      this.userId = parseInt(options.userId);
      console.log('密码修改页面接收到用户ID:', this.userId);
    }
  },
  onShow() {
    this.themeStore.applyTheme()
  },
  computed: {
    isFormValid() {
      // 检查两个密码是否都已输入且一致，且长度至少6位
      return this.username && this.password && 
             this.username === this.password && 
             this.username.length >= 6;
    }
  },
  methods: {
    async handleConfirm() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入新密码和确认密码',
          icon: 'none'
        })
        return
      }
      
      if (this.username.length < 6) {
        uni.showToast({
          title: '密码长度至少为6位',
          icon: 'none'
        })
        return
      }
      
      if (this.username !== this.password) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return
      }
      
      // 如果没有userId，提示错误
      if (!this.userId) {
        uni.showToast({
          title: '用户信息缺失，请重新操作',
          icon: 'none'
        })
        return
      }
      
      try {
        console.log('开始修改密码，用户ID:', this.userId);
        
        uni.showLoading({
          title: '修改中...'
        });
        
        // 调用后端接口修改密码
        // 注意：暂时不对密码进行加密处理，后端会处理（或暂时明文存储）
        const response = await uni.request({
          url: '${this.$baseUrl}/api/u-entities/user-base/password',
          method: 'PUT',
          header: {
            'Content-Type': 'application/json'
          },
          data: {
            userId: this.userId,
            newPassword: this.username
          }
        });
        
        uni.hideLoading();
        
        console.log('修改密码API响应:', response);
        
        if (response.statusCode === 200 && response.data.success) {
          uni.showToast({
            title: response.data.message || '密码修改成功',
            icon: 'success'
          })
          
          // 密码修改成功后跳转到账号密码登录界面
          setTimeout(() => {
            uni.navigateTo({
              url: '/pages/login/login-replica'
            })
          }, 1500)
        } else {
          uni.showToast({
            title: response.data.message || '密码修改失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading();
        console.error('修改密码异常:', error);
        uni.showToast({
          title: '密码修改失败',
          icon: 'none'
        })
      }
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
  margin-bottom: 25px;
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



.terms-section {
  text-align: center;
  margin-top: 240px;
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
  transition: background-color 0.3s ease;
}

.login-btn:disabled {
  background-color: var(--color-border);
  cursor: not-allowed;
}

.login-btn:not(:disabled):active {
  opacity: 0.9;
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