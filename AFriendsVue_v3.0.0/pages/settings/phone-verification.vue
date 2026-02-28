<template>
  <view class="verification-container">
    <!-- 主内容区域 -->
    <view class="main-content">
      <!-- 标题 -->
      <text class="title">请输入验证码</text>
      
      <!-- 说明文字 -->
      <text class="instruction">验证码已发送至</text>
      <text class="phone-number">{{ phoneNumber }}</text>
      
      <!-- 验证码输入框 -->
      <view class="code-input-section">
        <view 
          v-for="(digit, index) in verificationCodes" 
          :key="index"
          class="code-input-field"
          :class="{ 'active': currentIndex === index }"
          @click="focusMainInput"
        >
          <text v-if="digit" class="code-digit">{{ digit }}</text>
        </view>
        
        <!-- 隐藏的主输入框 -->
        <input
          class="main-input"
          type="number"
          v-model="mainInputValue"
          maxlength="6"
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
        <text class="resend-link" @click="resendCode">{{ resendText }}</text>
      </view>
      
      <!-- 确认按钮 -->
      <button class="confirm-btn" @click="handleConfirm" :disabled="!isCodeComplete">
        <text class="btn-text">确定</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  name: 'PhoneVerification',
  data() {
    return {
      // 从路由参数获取的手机号
      phoneNumber: '',
      // 用户ID
      userId: null,
      // 操作类型：bind(绑定) 或 change(更换)
      operationType: '',
      // 验证码数组
      verificationCodes: ['', '', '', '', '', ''],
      // 主输入框的值
      mainInputValue: '',
      // 当前聚焦的输入框索引
      currentIndex: 0,
      // 倒计时秒数
      countdown: 60
    }
  },
  computed: {
    // 重新发送文本
    resendText() {
      return this.countdown > 0 ? `重新发送(${this.countdown}s)` : '重新发送';
    },
    
    // 验证码是否输入完整
    isCodeComplete() {
      return this.verificationCodes.every(digit => digit !== '');
    }
  },
  onLoad(options) {
    // 获取路由参数
    this.phoneNumber = options.phoneNumber || '';
    this.operationType = options.operationType || 'bind';
    if (options.userId) {
      this.userId = parseInt(options.userId);
    }
    
    // 开始倒计时
    this.startCountdown();
    
    // 页面加载后自动聚焦主输入框，触发系统键盘
    this.$nextTick(() => {
      setTimeout(() => {
        this.focusMainInput();
      }, 300); // 延迟300ms确保页面完全加载
    });
  },
  methods: {
    handleMainInput(event) {
      const value = event.detail.value.replace(/\D/g, '').slice(0, 6);
      this.mainInputValue = value;
      
      // 将输入的数字分配到对应的方框中
      for (let i = 0; i < 6; i++) {
        this.verificationCodes[i] = value[i] || '';
      }
      
      // 设置当前活动索引
      if (value.length > 0) {
        this.currentIndex = Math.min(value.length - 1, 5);
      } else {
        this.currentIndex = 0;
      }
      
      // 检查是否输入完成
      this.checkVerificationComplete();
    },
    
    handleMainFocus() {
      // 聚焦时设置活动索引
      if (this.mainInputValue.length > 0) {
        this.currentIndex = Math.min(this.mainInputValue.length - 1, 5);
      } else {
        this.currentIndex = 0;
      }
    },
    
    handleMainBlur() {
      // 失焦时清除选中状态
      this.currentIndex = -1;
    },
    
    focusMainInput() {
      // 点击任意方框时聚焦主输入框
      this.$nextTick(() => {
        if (this.$refs.mainInput) {
          // 使用uni-app的聚焦方法
          const input = this.$refs.mainInput;
          if (input && input.focus) {
            input.focus();
          }
        }
      });
    },
    
    // 重新输入手机号
    changePhone() {
      // 返回手机号设置页面，传递userId
      let url = '/pages/settings/phone-number-setting';
      if (this.userId) {
        url += `?userId=${this.userId}`;
      }
      uni.redirectTo({
        url: url
      });
    },
    
    // 检查验证码是否输入完成
    checkVerificationComplete() {
      // 检查是否6位都已输入
      const isComplete = this.verificationCodes.every(code => code !== '');
      
      if (isComplete) {
        // 暂时不实现验证码验证逻辑，仅作为流程的一部分
        // TODO: 实际生产环境中应该调用后端接口验证验证码
        const enteredCode = this.verificationCodes.join('');
        console.log('验证码输入完成:', enteredCode);
        
        // 直接通过验证，然后调用接口更新手机号
        this.updatePhoneNumber();
      }
    },
    
    // 更新手机号（验证码验证后调用）
    async updatePhoneNumber() {
      if (!this.userId) {
        uni.showToast({
          title: '用户信息缺失',
          icon: 'none'
        });
        return;
      }
      
      try {
        console.log('开始绑定/更换手机号，用户ID:', this.userId, '新手机号:', this.phoneNumber);
        
        uni.showLoading({
          title: '处理中...'
        });
        
        const response = await uni.request({
          url: '${this.$baseUrl}/api/u-entities/user-base/phone',
          method: 'PUT',
          header: {
            'Content-Type': 'application/json'
          },
          data: {
            userId: this.userId,
            phoneNumber: this.phoneNumber,
            operationType: this.operationType
          }
        });
        
        uni.hideLoading();
        
        console.log('绑定/更换手机号API响应:', response);
        
        if (response.statusCode === 200 && response.data.success) {
          uni.showToast({
            title: response.data.message || (this.operationType === 'bind' ? '手机号绑定成功' : '手机号更换成功'),
            icon: 'success'
          });
          
          // 延迟返回上一页，让用户看到成功提示
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: response.data.message || '操作失败',
            icon: 'none'
          });
          
          // 清空输入，让用户重新输入验证码
          this.verificationCodes = ['', '', '', '', '', ''];
          this.currentIndex = 0;
          this.mainInputValue = '';
        }
      } catch (error) {
        uni.hideLoading();
        console.error('绑定/更换手机号异常:', error);
        uni.showToast({
          title: '操作失败',
          icon: 'none'
        });
        
        // 清空输入，让用户重新输入验证码
        this.verificationCodes = ['', '', '', '', '', ''];
        this.currentIndex = 0;
        this.mainInputValue = '';
      }
    },
    
    // 开始倒计时
    startCountdown() {
      const timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
        } else {
          clearInterval(timer);
        }
      }, 1000);
    },
    
    // 重新发送验证码
    resendCode() {
      if (this.countdown > 0) return;
      
      // 模拟重新发送验证码
      uni.showToast({
        title: '验证码已重新发送',
        icon: 'none'
      });
      
      // 重置倒计时
      this.countdown = 60;
      this.startCountdown();
    },
    
    // 确认按钮点击事件
    handleConfirm() {
      if (!this.isCodeComplete) {
        uni.showToast({
          title: '请输入完整的验证码',
          icon: 'none'
        });
        return;
      }
      
      // 触发验证码验证
      this.checkVerificationComplete();
    }
  }
}
</script>

<style scoped>
.verification-container {
  width: 100%;
  height: 100vh;
  background-color: #FFFFFF;
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
  font-size: 48rpx;
  font-weight: bold;
  color: #000000;
  text-align: center;
  margin-bottom: 16rpx;
}

.instruction {
  font-size: 24rpx;
  color: #000000;
  text-align: center;
  margin-bottom: 8rpx;
  line-height: 1.4;
  max-width: 280px;
}

.phone-number {
  font-size: 24rpx;
  color: #000000;
  text-align: center;
  margin-bottom: 40rpx;
  line-height: 1.4;
  max-width: 280px;
  font-weight: 500;
}

/* 验证码输入框 */
.code-input-section {
  display: flex;
  gap: 16rpx;
  margin-bottom: 32rpx;
  position: relative;
}

.code-input-field {
  width: 60rpx;
  height: 60rpx;
  background-color: #F8F8F8;
  border: 2rpx solid #E0E0E0;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.code-input-field.active {
  border-color: #EF0056;
  background-color: #FFFFFF;
}

.code-digit {
  font-size: 24rpx;
  font-weight: bold;
  color: #333333;
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
  font-size: 16rpx;
  border-radius: 4rpx;
}

/* 重新输入手机号 */
.change-phone-section {
  margin-bottom: 32rpx;
  text-align: center;
}

.change-phone-link {
  font-size: 24rpx;
  color: #0066CC;
  cursor: pointer;
  text-decoration: underline;
}

/* 重新发送选项 */
.resend-section {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 40rpx;
}

.resend-text {
  font-size: 24rpx;
  color: #000000;
}

.resend-link {
  font-size: 24rpx;
  color: #0066CC;
  cursor: pointer;
  text-decoration: underline;
}

.resend-link.disabled {
  color: #999999;
  cursor: not-allowed;
}

/* 确认按钮 */
.confirm-btn {
  width: 280px;
  height: 48px;
  background-color: #EF0056;
  border: none;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.confirm-btn:disabled {
  background-color: #CCCCCC;
  cursor: not-allowed;
}

.confirm-btn:not(:disabled):active {
  background-color: #D1004A;
}

.btn-text {
  font-size: 36rpx;
  font-weight: 600;
  color: #FFFFFF;
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
  
  .confirm-btn {
    width: 240px;
  }
  
  .change-phone-link,
  .resend-text,
  .resend-link {
    font-size: 12px;
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
  
  .confirm-btn {
    width: 320px;
  }
  
  .change-phone-link,
  .resend-text,
  .resend-link {
    font-size: 18px;
  }
}
</style>