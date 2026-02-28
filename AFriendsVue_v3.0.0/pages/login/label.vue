<template>
  <div class="label-setup-container" :class="themeStore.themeClass">
    <!-- 状态栏 -->
    <div class="status-bar"></div>

    <!-- 右上角跳过 -->
    <div class="skip-button" @click="skip">跳过</div>

    <!-- 标题 -->
    <div class="title-section">
      <span class="title">选择你的初始标签</span>
      <span class="subtitle">从推荐标签中选择符合你的标签</span>
    </div>

    <!-- 标签列表 -->
    <div class="label-list">
      <div
        v-for="(label, idx) in labels"
        :key="label.id"
        class="label-item"
        :class="{ selected: selectedLabels.includes(label.id) }"
        :style="{ animationDelay: (idx * 0.06) + 's' }"
        @click="toggleLabel(label.id)"
      >
        {{ label.labelName }}
      </div>
    </div>

    <!-- 换一批按钮 -->
    <div class="refresh-section">
      <button 
        class="refresh-btn" 
        @click="loadNextBatch"
        :disabled="!hasMore"
      >
        换一批
      </button>
    </div>

    <!-- 完成按钮 -->
    <div class="button-section">
      <button
        class="complete-btn"
        @click="saveLabels"
        :disabled="selectedLabels.length === 0 || !userId"
      >
        完成
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
      labels: [],
      selectedLabels: [],
      userId: null,
      phone: null,
      currentPage: 1, // 当前页码，默认第一页
      hasMore: true,  // 是否有更多标签
      isLoading: false // 加载状态，防止重复请求
    };
  },
  mounted() {
    this.themeStore.init()
    this.themeStore.applyTheme()
    this.phone = this.$route.query.phone || "";
    if (!this.phone) {
      alert("手机号缺失，无法获取用户信息");
      return;
    }
    this.getUserIdByPhone(this.phone);
    this.loadLabels();
  },
  methods: {
    getUserIdByPhone(phone) {
      axios
        .get("http://10.16.33.121:8888/api/label_zxc/getUserIdByPhone", {
          params: { phone },
        })
        .then((res) => {
          if (res.data.code === 200) {
            this.userId = res.data.userId;
          } else {
            alert("未找到对应的用户，请先注册");
          }
        })
        .catch(() => {
          alert("获取用户信息失败，请检查网络");
        });
    },
    loadLabels() {
      if (this.isLoading) return;
      this.isLoading = true;
      // 请求随机标签接口，limit参数控制每批数量
      axios
        .get("http://10.16.33.121:8888/api/label_zxc/recommendations/random", {
          params: {
            limit: 10,
          },
        })
        .then((res) => {
          if (res.data.code === 200) {
            this.labels = res.data.data;
            // 如果返回条数小于limit，说明没更多了
            this.hasMore = res.data.data.length === 10;
            // 清空已选标签，换一批时一般清空更合理
            this.selectedLabels = [];
          }
        })
        .catch(() => {
          alert("加载标签失败，请重试");
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    loadNextBatch() {
      // 直接调用loadLabels即可换一批
      if (this.isLoading) return;
      this.loadLabels();
      // 滚动到标签区域顶部
      setTimeout(() => {
        document.querySelector(".label-list")?.scrollIntoView({
          behavior: "smooth",
        });
      }, 300);
    },
    toggleLabel(labelId) {
      if (this.selectedLabels.includes(labelId)) {
        this.selectedLabels = this.selectedLabels.filter((id) => id !== labelId);
      } else {
        this.selectedLabels.push(labelId);
      }
    },
    saveLabels() {
      if (!this.userId) {
        alert("用户ID不存在，无法保存标签");
        return;
      }
      if (!this.phone) {
        alert("手机号不存在，无法保存标签");
        return;
      }
      axios
        .post("http://10.16.33.121:8888/api/label_zxc/saveUserLabels", {
          phone: this.phone,
          labelIds: this.selectedLabels,
        })
        .then((res) => {
          if (res.data.code === 200) {
            alert("标签保存成功");
            this.$router.push("/pages/index/index");
          } else {
            alert(res.data.message || "保存标签失败");
          }
        })
        .catch(() => {
          alert("网络错误，保存失败");
        });
    },
    skip() {
      this.$router.push("/pages/index/index");
    },
  },
};
</script>

<style scoped>
.label-setup-container {
  min-height: 100vh;
  background: var(--color-bg);
  color: var(--color-text);
  padding: 0 30px;
  display: flex;
  flex-direction: column;
}
.status-bar {
  height: 44px;
}
.skip-button {
  position: absolute;
  top: 16px;
  right: 20px;
  font-size: 16px;
  color: var(--color-primary);
  cursor: pointer;
  z-index: 10;
}
.title-section {
  text-align: center;
  margin-top: 100px;
  margin-bottom: 40px;
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
.label-list {
  margin-top: 30px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
  animation: fadeInUp 0.8s ease-out 0.2s both;
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
.label-item {
  padding: 12px 24px;
  border-radius: 20px;
  background: var(--color-bg-weak);
  color: var(--color-text);
  font-size: 16px;
  font-weight: 500;
  border: 2px solid var(--color-border);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.2s;
  animation: bubbleIn 0.5s cubic-bezier(.68,-0.55,.27,1.55) both;
}
.label-item.selected {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary) 100%);
  color: var(--color-on-primary);
  border-color: var(--color-primary);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: scale(1.08);
}
@keyframes bubbleIn {
  from {
    opacity: 0;
    transform: scale(0.5) translateY(40px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

/* 换一批按钮样式 */
.refresh-section {
  margin: 30px 0;
  display: flex;
  justify-content: center;
}
.refresh-btn {
  padding: 10px 20px;
  border-radius: 20px;
  border: 2px solid var(--color-primary);
  background: transparent;
  color: var(--color-primary);
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}
.refresh-btn:hover:not(:disabled) {
  background: var(--color-bg-weak);
}
.refresh-btn:disabled {
  border-color: var(--color-border);
  color: var(--color-text-secondary);
  cursor: not-allowed;
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