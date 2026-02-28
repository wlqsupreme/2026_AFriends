<template>
  <view class="verification-container" :class="themeStore.themeClass">
    <!-- 主内容区域 -->
    <view class="main-content">
      <!-- 标题 -->
      <text class="title">请输入验证码</text>
      
      <!-- 说明文字 -->
      <text class="instruction">我们刚刚向您发送了一条验证码,请查看您的信箱</text>
      
      <!-- 验证码输入框 -->
      <view class="code-input-section">
        <view 
          v-for="(digit, index) in verificationCode" 
          :key="index"
          class="code-input-field"
          :class="{ 'active': activeIndex === index }"
          @click="focusMainInput"
        >
          <text v-if="digit" class="code-digit">{{ digit }}</text>
        </view>
        
        <!-- 隐藏的主输入框 -->
        <input
          class="main-input"
          type="number"
          v-model="mainInputValue"
          maxlength="4"
          placeholder=""
          @input="handleMainInput"
          @focus="handleMainFocus"
          @blur="handleMainBlur"
          ref="mainInput"
        />
      </view>
      
      <!-- 重新输入手机号 -->
      <view class="change-phone-section">
        <text class="change-phone-link" @click="changePhone">重新输入手机号</text>
      </view>
      
      <!-- 重新发送选项 -->
      <view class="resend-section">
        <text class="resend-text">没有收到验证码?</text>
        <text class="resend-link" @click="resendCode">重新发送</text>
      </view>
      
      <!-- 下一步按钮 -->
      <button class="login-btn" @click="handleNext" :disabled="!isCodeComplete">
        <text class="btn-text">下一步</text>
      </button>
    </view>
  </view>
</template>

<script>
import { useThemeStore } from '../../store/theme.js'

export default {
  data() {
    return {
      themeStore: useThemeStore(),
      verificationCode: ['', '', '', ''],
      mainInputValue: '',
      activeIndex: 0,
      userId: null, // 用户ID，从 reset-password 页面传递
      phone: '' // 手机号，从 reset-password 页面传递
    }
  },
  onLoad(options) {
    this.themeStore.init()
    this.themeStore.applyTheme()
    // 接收从 reset-password 页面传递的参数
    if (options.userId) {
      this.userId = parseInt(options.userId);
    }
    if (options.phone) {
      this.phone = decodeURIComponent(options.phone);
    }
  },
  onShow() {
    this.themeStore.applyTheme()
  },
  computed: {
    isCodeComplete() {
      return this.verificationCode.every(digit => digit !== '')
    }
  },
  mounted() {
    // 页面加载后自动聚焦主输入框，触发系统键盘
    this.$nextTick(() => {
      setTimeout(() => {
        this.focusMainInput()
      }, 300) // 延迟300ms确保页面完全加载
    })
  },
  methods: {
    handleMainInput(event) {
      const value = event.detail.value.replace(/\D/g, '').slice(0, 4)
      this.mainInputValue = value
      
      // 将输入的数字分配到对应的方框中
      for (let i = 0; i < 4; i++) {
        this.verificationCode[i] = value[i] || ''
      }
      
      // 设置当前活动索引
      if (value.length > 0) {
        this.activeIndex = Math.min(value.length - 1, 3)
      } else {
        this.activeIndex = 0
      }
    },
    
    handleMainFocus() {
      // 聚焦时设置活动索引
      if (this.mainInputValue.length > 0) {
        this.activeIndex = Math.min(this.mainInputValue.length - 1, 3)
      } else {
        this.activeIndex = 0
      }
    },
    
    handleMainBlur() {
      // 失焦时清除选中状态
      this.activeIndex = -1
    },
    
    focusMainInput() {
      // 点击任意方框时聚焦主输入框
      this.$nextTick(() => {
        if (this.$refs.mainInput) {
          // 使用uni-app的聚焦方法
          const input = this.$refs.mainInput
          if (input && input.focus) {
            input.focus()
          }
        }
      })
    },
    
    resendCode() {
      // 在点击发送验证码跳转页面后，或者点击重新发送后，调用后端发送验证码方法
      // TODO: 调用后端发送验证码接口
      console.log('重新发送验证码')
      
      // 显示提示信息
      uni.showToast({
        title: '验证码已重新发送',
        icon: 'success'
      })
    },
    
    handleNext() {
      if (!this.isCodeComplete) {
        uni.showToast({
          title: '请输入完整的验证码',
          icon: 'none'
        })
        return
      }
      
      const code = this.verificationCode.join('')
      console.log('验证码:', code)
      
      // 这里添加验证码验证逻辑
      // 注意：暂时不对短信验证功能做修改，直接通过验证
      uni.showToast({
        title: '验证成功',
        icon: 'success'
      })
      
      // 验证成功后跳转到重置密码界面，传递userId
      let url = '/pages/login/rest-verify-password';
      if (this.userId) {
        url += `?userId=${this.userId}`;
      }
      setTimeout(() => {
        uni.navigateTo({
          url: url
        })
      }, 1000)
    },

    goBack() {
      uni.navigateBack()
    },

    changePhone() {
      // 返回时传递userId（如果存在）
      let url = '/pages/login/reset-password';
      if (this.userId) {
        url += `?userId=${this.userId}`;
      }
      uni.redirectTo({
        url: url
      })
    }
  }
}
</script>

<style scoped>
.verification-container {
  width: 100%;
  height: 100vh;
  background-color: var(--color-bg);
  display: flex;
  flex-direction: column;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  padding: 40px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}

.title {
  font-size: 48px;
  font-weight: bold;
  color: var(--color-text);
  text-align: center;
  margin-bottom: 16px;
}

.instruction {
  font-size: 12px;
  color: var(--color-text-secondary);
  text-align: center;
  margin-bottom: 40px;
  line-height: 1.4;
  max-width: 280px;
}

/* 验证码输入框 */
.code-input-section {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
  position: relative;
}

.code-input-field {
  width: 60px;
  height: 60px;
  background-color: var(--color-bg-weak);
  border: 2px solid var(--color-border);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.code-input-field.active {
  border-color: var(--color-primary);
  background-color: var(--color-card);
}

.code-digit {
  font-size: 24px;
  font-weight: bold;
  color: var(--color-text);
}

/* 隐藏的主输入框 */
.main-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  z-index: 1000;
  background: transparent;
  border: none;
  text-align: center;
  font-size: 16px;
  border-radius: 4px;
}

/* 重新输入手机号 */
.change-phone-section {
  margin-bottom: 32px;
  text-align: center;
}

.change-phone-link {
  font-size: 12px;
  color: var(--color-primary);
  cursor: pointer;
  text-decoration: underline;
}

/* 重新发送选项 */
.resend-section {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 40px;
}

.resend-text {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.resend-link {
  font-size: 12px;
  color: var(--color-primary);
  cursor: pointer;
}

/* 下一步按钮 */
.login-btn {
  width: 280px;
  height: 48px;
  background-color: var(--color-primary);
  border: none;
  border-radius: 12px;
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
  font-size: 18px;
  font-weight: 600;
  color: var(--color-on-primary);
}

/* 响应式设计 */
@media (max-width: 375px) {
  .main-content {
    padding: 30px 24px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .instruction {
    font-size: 14px;
    max-width: 240px;
  }
  
  .code-input-field {
    width: 50px;
    height: 50px;
  }
  
  .code-digit {
    font-size: 20px;
  }
  
  .login-btn {
    width: 240px;
  }
}

@media (min-width: 414px) {
  .main-content {
    padding: 50px 40px;
  }
  
  .title {
    font-size: 32px;
  }
  
  .instruction {
    font-size: 18px;
    max-width: 320px;
  }
  
  .code-input-field {
    width: 70px;
    height: 70px;
  }
  
  .code-digit {
    font-size: 28px;
  }
  
  .login-btn {
    width: 320px;
  }
}
</style> 