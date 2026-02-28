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
        <text class="greeting-text">{{ isSwitchingAccount ? $t('switchAccount.title') : $t('login.greeting') }}</text>
        <text class="welcome-text">{{ $t('login.welcome') }}</text>
      </view>

      <!-- 登录表单 -->
      <view class="login-form">
        <!-- 用户名/手机号输入框 -->
        <view class="input-group">
          <text class="input-label">{{ $t('login.phoneOrUsername') }}</text>
          <view class="input-wrapper">
            <input
              class="form-input"
              type="text"
              v-model="username"
              :placeholder="isSwitchingAccount ? '请输入要切换的账号' : ''"
            />
            <view class="input-underline"></view>
          </view>
        </view>

        <!-- 密码输入框 -->
        <view class="input-group">
          <text class="input-label">{{ $t('login.password') }}</text>
          <view class="input-wrapper">
            <input
              class="form-input"
              type="password"
              v-model="password"
            />
            <view class="input-underline"></view>
          </view>
        </view>

        <!-- 忘记密码 -->
        <view class="forgot-password">
          <text class="option-text" @click="goToResetPassword">{{ $t('login.forgotPassword') }}</text>
        </view>

        <!-- 使用手机验证码登录 -->
        <view class="phone-login-option">
          <text class="option-text" @click="goToPhoneLogin">{{ $t('login.phoneLogin') }}</text>
        </view>

        <!-- 第三方登录 -->
        <view class="third-party-login">
          <text class="section-title">{{ $t('login.orLoginWith') }}</text>
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
          <text class="terms-link" @click="goToTermsOfService">{{ $t('login.termsOfService') }}</text>
        </view>

        <!-- 登录按钮 -->
        <button class="login-btn" @click="handleLogin">
          <text class="btn-text">{{ isSwitchingAccount ? $t('switchAccount.switchToAccount') : $t('login.login') }}</text>
        </button>

        <!-- 注册链接 -->
        <view class="register-section">
          <text class="register-text">{{ $t('login.noAccount') }}</text>
          <text class="register-link" @click="goToRegister">{{ $t('login.register') }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import accountManager from '@/utils/accountManager.js';
import { useThemeStore } from '../../store/theme.js'

export default {
  data() {
    return {
      themeStore: useThemeStore(),
      username: '',
      password: '',
      userId: null, // 存储用户ID
      isSwitchingAccount: false // 是否为切换账号模式
    }
  },
  onLoad(options) {
    this.themeStore.init()
    this.themeStore.applyTheme()
    // 检查是否为切换账号模式
    if (options && options.switchAccount === 'true') {
      this.isSwitchingAccount = true;
    }

		if (options && options.username) {
			try {
				this.username = decodeURIComponent(options.username)
			} catch (e) {
				this.username = options.username
			}
		}
  },
  onShow() {
    this.themeStore.applyTheme()
  },
  methods: {
    handleLogin() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: this.$t('login.pleaseInputUsernamePassword'),
          icon: 'none'
        })
        return
      }

      // 设置默认测试用户ID
      this.userId = 1000100;

		this.onLoginSuccess(this.userId, this.username);
		this.recordLoginDevice();

      // TODO: 这里应该调用后端登录接口
      // 暂时用注释代替实际的后端调用
      /*
      uni.request({
        url: '${this.$baseUrl}/api/user-profile/login',
        method: 'POST',
        data: {
          username: this.username,
          password: this.password
        },
        success: (res) => {
          if (res.data.success) {
            // 存储用户信息
            uni.setStorageSync('userId', res.data.userId);
            uni.setStorageSync('username', this.username);
            uni.setStorageSync('userToken', res.data.token);

            // 保存账号信息用于切换
            accountManager.saveAccount({
              username: this.username,
              id: res.data.userId,
              phone: res.data.phoneNumber
            });

            uni.showToast({
              title: this.$t('login.loginSuccess'),
              icon: 'success',
              duration: 1500
            });

            // 跳转到首页
            setTimeout(() => {
              uni.reLaunch({
                url: '/pages/feed/content-feed'
              });
            }, 1500);
          } else {
            uni.showToast({
              title: res.data.message || '登录失败',
              icon: 'none'
            });
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '网络错误',
            icon: 'none'
          });
        }
      });
      */

      // 显示登录成功提示
      uni.showToast({
        title: this.$t('login.loginSuccess'),
        icon: 'success',
        duration: 1500
      });

      // 跳转到首页
      setTimeout(() => {
        uni.reLaunch({
          url: '/pages/feed/content-feed'
        });
      }, 1500);
    },

		onLoginSuccess(userId, username, token) {
			uni.setStorageSync('userId', userId);
			uni.setStorageSync('username', username);
			if (token) {
				uni.setStorageSync('userToken', token);
			}

			accountManager.saveAccount({
				username: username,
				id: userId,
				phone: ''
			});

			try {
				if (uni && uni.$emit) {
					uni.$emit('auth-changed', { userId, username })
				}
			} catch (e) {

			}
		},

    /**
     * 记录登录设备信息
     */
    async recordLoginDevice() {
      try {
        const systemInfo = uni.getSystemInfoSync();

        // 获取设备信息
        const deviceInfo = {
          userId: this.userId,
          deviceName: systemInfo.model || '未知设备',
          deviceType: systemInfo.platform === 'ios' ? 'iOS' : (systemInfo.platform === 'android' ? 'Android' : '其他'),
          deviceModel: systemInfo.model || '',
          deviceIdentifier: `${systemInfo.platform}_${systemInfo.system}_${systemInfo.model}_${systemInfo.brand || ''}`.replace(/\s+/g, '_'),
          loginLocation: '中国', // 可以通过IP定位API获取，这里使用默认值
          loginIp: '' // 可以通过后端获取
        };

        // 调用后端API记录设备信息
        const response = await uni.request({
          url: '${this.$baseUrl}/api/u-entities/user-device/record-login',
          method: 'POST',
          header: {
            'Content-Type': 'application/json'
          },
          data: deviceInfo
        });

        if (response.statusCode === 200 && response.data.success) {
          console.log('设备信息记录成功');
        } else {
          console.warn('设备信息记录失败:', response.data.message);
        }
      } catch (error) {
        console.error('记录设备信息失败:', error);
        // 不影响登录流程，静默失败
      }
    },

    goToRegister() {
      uni.navigateTo({
        url: '/pages/login/phone-register'
      })
    },

    goToPhoneLogin() {
      uni.navigateTo({
        url: '/pages/login/phone-login'
      })
    },

    goToWechatBind() {
      uni.navigateTo({
        url: '/pages/login/login-unbound'
      })
    },

    goToResetPassword() {
      uni.navigateTo({
        url: '/pages/login/reset-password'
      })
    },

    goToTermsOfService() {
      uni.navigateTo({
        url: '/pages/settings/terms-of-service'
      })
    },

    goToWechatLogin() {
      uni.showToast({
        title: this.$t('login.redirectingToWechat'),
        icon: 'none',
        duration: 2000
      })
    },

    goToQQLogin() {
      uni.showToast({
        title: this.$t('login.redirectingToQQ'),
        icon: 'none',
        duration: 2000
      })
    },

    goToDouyinLogin() {
      uni.showToast({
        title: this.$t('login.redirectingToDouyin'),
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

.forgot-password {
  text-align: right;
  margin-bottom: 5px;
}

.phone-login-option {
  text-align: center;
  margin-bottom: 5px;
}

.option-text {
  font-size: 12px;
  color: var(--color-primary);
  cursor: pointer;
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