<template>
  <div class="profile-setup-container" :class="themeStore.themeClass">
    <!-- 状态栏占位 -->
    <div class="status-bar"></div>

    <!-- 返回按钮 -->
    <div class="back-button" @click="goBack">
      <span class="back-icon">←</span>
    </div>

    <!-- 标题区域 -->
    <div class="title-section">
      <span class="title">完善资料</span>
      <span class="subtitle">设置您的头像和昵称</span>
    </div>

    <!-- 头像选择区域 -->
    <div class="avatar-section">
      <span class="section-title">选择头像</span>
      <div class="current-avatar">
        <img
          class="selected-avatar"
          :src="backendUrl + selectedAvatar"
          alt="当前头像"
        />
      </div>
      <div class="avatar-scroll-container">
        <span class="recommend-title">推荐头像（点击选择）</span>
        <div v-if="loading" class="loading-container">
          <span class="loading-text">加载中...</span>
        </div>
        <div v-else class="avatar-scroll" style="overflow-x: auto; white-space: nowrap;">
          <div class="avatar-list" style="display: inline-flex;">
            <div
              v-for="(avatar, index) in recommendedAvatars"
              :key="index"
              class="avatar-item"
              :class="{ active: selectedAvatar === avatar }"
              @click="selectAvatar(avatar)"
              style="display: inline-block; margin-right: 10px;"
            >
              <img
			   v-if="selectedAvatar" 
                class="avatar-image"
                :src="backendUrl + avatar"
                alt="avatar"
                style="width: 60px; height: 60px; object-fit: cover; border-radius: 50%;"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 昵称输入区域 -->
    <div class="nickname-section">
      <span class="section-title">设置昵称</span>
      <div class="nickname-input-container">
        <input
          class="nickname-input"
          type="text"
          placeholder="请输入昵称"
          v-model="nickname"
          maxlength="12"
        />
        <span class="char-count">{{ nickname.length }}/12</span>
      </div>
      <div class="nickname-scroll-container">
        <span class="recommend-title">推荐昵称（点击选择）</span>
        <div v-if="loading" class="loading-container">
          <span class="loading-text">加载中...</span>
        </div>
        <div v-else class="nickname-scroll" style="overflow-y: auto; max-height: 200px;">
          <div class="nickname-list">
            <div
              v-for="(name, index) in recommendedNicknames"
              :key="index"
              class="nickname-item"
              @click="selectNickname(name)"
              style="padding: 8px 10px; cursor: pointer;"
            >
              <span class="nickname-text">{{ name }}</span>
              <span class="select-icon" style="float: right;">→</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 完成按钮 -->
    <div class="button-section">
      <button
        class="complete-btn"
        @click="completeSetup"
        :disabled="!isFormValid"
      >
        完成设置
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

import { useThemeStore } from '../../store/theme.js'

export default {
  data() {
    return {
      themeStore: useThemeStore(),
      backendUrl: "http://10.16.33.121:8888", // 您的后端服务器地址
		
      phoneNumber: "",
      selectedAvatar: "",
      nickname: "",
      recommendedAvatars: [],
      recommendedNicknames: [],
      loading: true,
    };
  },
  computed: {
    isFormValid() {
      return this.selectedAvatar && this.nickname.trim().length > 0;
    },
  },
  mounted() {
    this.themeStore.init()
    this.themeStore.applyTheme()
    console.log("mounted 触发，开始加载推荐数据");
    this.getRecommendations();
    // 如果 phoneNumber 需要从路由参数获取，可以这样：
    if (this.$route.query.phone) {
      this.phoneNumber = this.$route.query.phone;
      console.log("设置 phoneNumber:", this.phoneNumber);
    }
  },
  methods: {
    goBack() {
      console.log("点击返回按钮");
      window.history.back();
    },

    // 获取推荐头像和昵称
    getRecommendations() {
      this.loading = true;
      console.log("开始请求推荐数据，loading = true");

      axios.get("http://10.16.33.121:8888/api/user_zxc/recommendations")
        .then((res) => {
          console.log("请求成功:", res);
          this.loading = false;
          if (res.data.code === 200) {
            const data = res.data.data;
            console.log("推荐数据内容:", data);
            this.recommendedAvatars = (data.avatars || []).map(
              (avatar) => avatar.url
            );
            console.log("推荐头像列表:", this.recommendedAvatars);
            this.recommendedNicknames = data.nicknames || [];
            console.log("推荐昵称列表:", this.recommendedNicknames);
            if (this.recommendedAvatars.length > 0) {
              this.selectedAvatar = this.recommendedAvatars[0];
              console.log("默认选中头像:", this.selectedAvatar);
            }
          } else {
            console.log("获取推荐数据失败，使用默认数据");
            this.loadDefaultData();
          }
        })
        .catch((err) => {
          console.error("请求失败:", err);
          this.loading = false;
          this.loadDefaultData();
        });
    },

    // 加载默认数据
    loadDefaultData() {
      console.log("加载默认头像和昵称数据");
      this.recommendedAvatars = [
        "/static/image/avatar1.jpg",
        "/static/image/avatar2.jpg",
        "/static/image/avatar3.jpg",
        "/static/image/avatar4.jpg",
      ];
      console.log("默认头像列表:", this.recommendedAvatars);
      this.recommendedNicknames = [
        "阳光小王子",
        "温柔小姐姐",
        "快乐小精灵",
        "智慧大师",
        "勇敢战士",
        "可爱萌妹",
        "帅气小哥",
        "文艺青年",
        "运动健将",
        "音乐达人",
        "美食家",
        "旅行者",
        "摄影师",
        "设计师",
        "程序员",
        "医生",
        "老师",
        "学生",
      ];
      console.log("默认昵称列表:", this.recommendedNicknames);
      if (this.recommendedAvatars.length > 0) {
        this.selectedAvatar = this.recommendedAvatars[0];
        console.log("默认选中头像:", this.selectedAvatar);
      }
    },

    selectAvatar(avatar) {
      console.log("选择头像:", avatar);
      this.selectedAvatar = avatar;
      // alert("头像已选择");
    },

    selectNickname(name) {
      console.log("选择昵称:", name);
      this.nickname = name;
      // alert(`已选择：${name}`);
    },

    completeSetup() {
      console.log("点击完成设置，当前头像:", this.selectedAvatar, "昵称:", this.nickname);
      if (!this.isFormValid) {
        alert("请完善头像和昵称");
        return;
      }

      axios.post("http://10.16.33.121:8888/userinfo_zxc/updateAvatarAndUsername", {
        phone: this.phoneNumber,
        avatar: this.selectedAvatar,
        nickname: this.nickname,
      })
        .then((res) => {
          console.log("提交设置结果:", res);
          if (res.data.code === 200) {
            alert("设置成功");
            setTimeout(() => {
              // 使用 Vue Router 跳转到标签选择界面，并传递用户ID或手机号
              this.$router.push({
                   path: '/pages/label-zxc/label',
                query: { phone: this.phoneNumber } // 或者 userId，看你后端的接口用哪个
              });
            }, 500);
          } else {
            alert(res.data.message || "设置失败");
          }
        })
        .catch((err) => {
          console.error("设置提交失败:", err);
          alert("网络错误");
        });
    },


  },
};
</script>

<style scoped>
.profile-setup-container {
  min-height: 100vh;
  background-color: var(--color-bg);
  color: var(--color-text);
  display: flex;
  flex-direction: column;
  padding: 0 30px;
}

.status-bar {
  height: 44px;
  background-color: var(--color-bg);
}

.back-button {
  position: absolute;
  top: 60px;
  left: 30px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--color-bg-weak);
  transition: all 0.3s ease;
}

.back-button:active {
  background: var(--color-divider);
  transform: scale(0.95);
}

.back-icon {
  font-size: 20px;
  color: var(--color-text);
  font-weight: 600;
}

.title-section {
  margin-top: 120px;
  margin-bottom: 40px;
  text-align: center;
  animation: fadeInDown 0.8s ease-out;
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 12px;
  display: block;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
}

.subtitle {
  font-size: 16px;
  color: var(--color-text-secondary);
  display: block;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.avatar-section {
  margin-bottom: 40px;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 20px;
  display: block;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
}

.current-avatar {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.selected-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid var(--color-primary);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.avatar-scroll-container {
  margin-bottom: 20px;
}

.avatar-scroll {
  width: 100%;
  white-space: nowrap;
}

.avatar-list {
  display: flex;
  gap: 12px;
  padding: 0 4px;
}

.avatar-item {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 2px solid var(--color-border);
  overflow: hidden;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.avatar-item.active {
  border-color: var(--color-primary);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.16);
  transform: scale(1.1);
}

.avatar-item:active {
  transform: scale(0.95);
}

.avatar-image {
  width: 100%;
  height: 100%;
}

.nickname-section {
  margin-bottom: 40px;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.nickname-input-container {
  position: relative;
  margin-bottom: 24px;
  width: 100%;
}

.nickname-input {
  width: 100%;
  height: 56px;
  background: var(--color-card);
  padding: 16px 20px;
  border-radius: 16px;
  border: 2px solid var(--color-border);
  font-size: 16px;
  color: var(--color-text);
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
  line-height: 1.5;
  display: block;
}

.nickname-input:focus {
  border-color: var(--color-primary);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
  outline: none;
}

.nickname-input::placeholder {
  color: var(--color-text-secondary);
}

.char-count {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 14px;
  color: var(--color-text-secondary);
  pointer-events: none;
  z-index: 1;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
}

.nickname-scroll-container {
  height: 200px;
}

.recommend-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text-secondary);
  margin-bottom: 12px;
  display: block;
}

.nickname-scroll {
  height: 160px;
}

.nickname-list {
  padding: 0 4px;
}

.nickname-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  margin-bottom: 8px;
  background: var(--color-bg-weak);
  border-radius: 12px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-bottom: 1px solid var(--color-divider);
}

.nickname-item:active {
  background: var(--color-divider);
  transform: scale(0.98);
}

.nickname-text {
  font-size: 16px;
  color: var(--color-text);
  font-weight: 500;
}

.select-icon {
  font-size: 16px;
  color: var(--color-primary);
  font-weight: 600;
}

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: var(--color-bg-weak);
  border-radius: 12px;
  margin: 8px 0;
}

.loading-text {
  font-size: 16px;
  color: var(--color-text-secondary);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC",
    "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial,
    sans-serif;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.button-section {
  margin-top: auto;
  padding-bottom: 40px;
  animation: fadeInUp 0.8s ease-out 0.6s both;
}

.complete-btn {
  width: 100%;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary) 100%);
  color: var(--color-on-primary);
  border: none;
  padding: 16px 24px;
  border-radius: 16px;
  font-size: 18px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  position: relative;
  overflow: hidden;
}

.complete-btn:disabled {
  background: linear-gradient(135deg, var(--color-border) 0%, var(--color-divider) 100%);
  color: var(--color-text-secondary);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: none;
}

.complete-btn:not(:disabled)::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: left 0.5s;
}

.complete-btn:not(:disabled):active::before {
  left: 100%;
}

.complete-btn:not(:disabled):active {
  transform: translateY(2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.16);
}
</style>
