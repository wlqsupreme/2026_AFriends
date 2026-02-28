<!-- <template>
	<view class="search-page">
		<view class="search-header">
			<view class="back-button" @click="goBack">
				<text>返回</text>
			</view>
			<input class="search-input" v-model="keyword" placeholder="搜索内容..." />
			<view class="search-btn" @click="search">搜索</view>
		</view>
		<view class="search-results">
			<text>搜索结果将在这里显示</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			keyword: ""
		}
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		search() {
			uni.showToast({
				title: "搜索功能开发中",
				icon: "none"
			})
		}
	}
}
</script>

<style>
.search-page {
	padding: 20rpx;
}
.search-header {
	display: flex;
	align-items: center;
	gap: 20rpx;
	margin-bottom: 40rpx;
}
.back-button {
	padding: 10rpx 20rpx;
	background: #f0f0f0;
	border-radius: 10rpx;
}
.search-input {
	flex: 1;
	padding: 20rpx;
	border: 1rpx solid #ddd;
	border-radius: 10rpx;
}
.search-btn {
	padding: 20rpx 40rpx;
	background: #FF69B4;
	color: white;
	border-radius: 10rpx;
}
.search-results {
	padding: 40rpx;
	text-align: center;
	color: #666;
}
</style> -->
<template>
  <view class="search-page">
    <!-- 搜索头部 -->
    <view class="search-header">
      <view class="back-button" @click="goBack">
        <text>返回</text>
      </view>
      <input 
        class="search-input" 
        v-model="keyword" 
        placeholder="搜索小说、作者或标签..." 
        @input="handleInput"  
        @confirm="search"     
      /><!-- 回车触发搜索 -->
      <view class="search-btn" @click="search">搜索</view>
    </view>

    <!-- 搜索主体区域 -->
    <view class="search-container">
      <!-- 加载状态 -->
      <view v-if="loading" class="loading">
        <uni-loading-icon size="24"></uni-loading-icon>
        <text>搜索中...</text>
      </view>

      <!-- 搜索结果区域 -->
      <view v-else-if="showResults">
        <!-- 结果头部 -->
        <view class="results-header">
          <text>找到 {{ total }} 本相关小说</text>
          <view class="sort-container">
            <picker @change="handleSortChange" :value="sortType" :range="sortOptions">
              <view class="sort-btn">
                <text>排序：{{ sortOptions[sortType] }}</text>
                <uni-icons type="arrowdown" size="16"></uni-icons>
              </view>
            </picker>
          </view>
        </view>

        <!-- 空结果 -->
        <view v-if="total === 0" class="empty-results">
          <uni-icons type="empty" size="60" color="#ccc"></uni-icons>
          <text>没有找到相关小说</text>
        </view>

        <!-- 结果列表 -->
        <view class="results-list" v-else>
          <view 
            class="novel-item" 
            v-for="(novel, index) in novels" 
            :key="index"
            @click="goToNovelDetail(novel.novelId)"
          >
            <!-- 小说封面 -->
            <image 
              class="novel-cover" 
              :src="novel.novelCoverUrl || '/static/images/default-cover.png'" 
              mode="widthFix"
            ></image>
            
            <!-- 小说信息 -->
            <view class="novel-info">
              <view class="novel-title">
                <text v-html="highlightKeyword(novel.novelTitle)"></text>
                <view class="status-tag">{{ novel.statusText }}</view>
              </view>
              <view class="novel-author">
                <text>作者：{{ novel.authorName }}</text>
              </view>
              <view class="novel-desc">
                <text v-html="highlightKeyword(novel.novelDescription)"></text>
              </view>
              <view class="novel-stats">
                <text>评分：{{ novel.novelScore || 0 }}</text>
                <text>阅读：{{ formatCount(novel.readingCount) }}</text>
                <text>点赞：{{ formatCount(novel.likeCount) }}</text>
              </view>
              <view class="novel-tags">
                <view class="tag" v-for="(tag, tIdx) in novel.tags" :key="tIdx" v-if="tIdx < 3">
                  {{ tag }}
                </view>
              </view>
            </view>
          </view>
		  <!-- 新增：加载更多按钮 -->
		    <view 
		      v-if="hasMoreData" 
		      class="load-more-btn" 
		      @click="loadNextPage"
		    >
		      <text>{{ loadingMore ? '加载中...' : '点击加载下一页' }}</text>
		    </view>
        </view>
		
        <!-- 分页加载 -->
        <view class="pagination" v-if="total > 0">
          <uni-pagination 
            :current="pageNum" 
            :pageSize="pageSize" 
            :total="total" 
            @change="handlePageChange"
          ></uni-pagination>
        </view>
      </view>

      <!-- 初始状态/搜索建议 -->
      <view v-else>
        <!-- 热门搜索 -->
        <view class="hot-search">
          <view class="hot-title">热门搜索</view>
          <view class="hot-tags">
            <view 
              class="hot-tag" 
              v-for="(word, index) in hotWords" 
              :key="index"
              @click="searchWithHotWord(word)"
            >
              {{ word }}
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      keyword: "",          // 搜索关键词
      novels: [],           // 搜索结果列表
      total: 0,             // 总条数
      pageNum: 1,           // 当前页码
      pageSize: 10,         // 每页条数
      sortType: 0,          // 排序类型（0-最新 1-最热 2-评分）
      sortOptions: ["最新发布", "最热", "评分最高"], // 排序选项
      loading: false,       // 加载状态
      showResults: false,   // 是否显示搜索结果
      hotWords: [],          // 热门搜索词
	  loadingMore: false, // 加载下一页的状态锁
	  hasMoreData: true   // 是否有更多数据可加载
    };
  },
  onLoad() {
    // 页面加载时获取热门搜索词
    this.getHotSearchWords();
  },
  methods: {
    // 返回上一页
    goBack() {
      uni.navigateBack();
    },

    // 处理输入（实时搜索建议，可选）
    handleInput() {
      // 可在此实现输入时的搜索建议逻辑（调用后端suggest接口）
      // 简单处理：如果清空关键词，隐藏结果，显示热门搜索
      if (!this.keyword.trim()) {
        this.showResults = false;
      }
    },

    // 执行搜索
    search() {
      const keyword = this.keyword.trim();
      if (!keyword) {
        uni.showToast({ title: "请输入搜索内容", icon: "none" });
        return;
      }

      // 重置页码，显示加载状态
      this.pageNum = 1;
      this.loading = true;
      this.showResults = true;
	  this.hasMoreData = true; // 重置为有更多数据

      // 调用后端搜索接口
      uni.request({
        url: `${this.$baseUrl}/api/search/novels`, // 后端接口地址
        method: "GET",
        data: {
          keyword: keyword,
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          sortType: this.sortType,
          // 可添加status筛选：status: 1（连载）/2（完结）
        },
        success: (res) => {
			if (res.data && res.data.success) {
                this.novels = res.data.novels || [];
                this.total = res.data.total || 0;
                // 初始化判断是否有更多数据
                this.hasMoreData = this.pageSize < this.total;
            }
        },
        fail: () => {
          uni.showToast({ title: "网络错误", icon: "none" });
        },
        complete: () => {
          this.loading = false; // 无论成功失败，结束加载
        }
      });
    },
	
	// 加载下一页数据
	  loadNextPage() {
	    // 校验：如果正在加载、没有更多数据、关键词为空，则不执行
	    if (this.loadingMore || !this.hasMoreData || !this.keyword.trim()) {
	      return;
	    }
	
	    // 标记加载中状态
	    this.loadingMore = true;
	    // 页码+1（下一页）
	    const nextPage = this.pageNum + 1;
	
	    // 请求下一页数据
	    uni.request({
	      url: `${this.$baseUrl}/api/search/novels`,
	      method: "GET",
	      data: {
	        keyword: this.keyword.trim(),
	        pageNum: nextPage,    // 请求下一页
	        pageSize: this.pageSize,
	        sortType: this.sortType
	      },
	      success: (res) => {
	        if (res.data && res.data.success) {
	          const newNovels = res.data.novels || [];
	          // 将新数据合并到现有列表（而非覆盖）
	          this.novels = [...this.novels, ...newNovels];
	          // 更新当前页码
	          this.pageNum = nextPage;
	          // 判断是否还有更多数据（当前页+1的总条数 <= 总数据量）
	          this.hasMoreData = (this.pageNum * this.pageSize) < this.total;
	        } else {
	          uni.showToast({ title: "加载失败", icon: "none" });
	        }
	      },
	      fail: () => {
	        uni.showToast({ title: "网络错误", icon: "none" });
	      },
	      complete: () => {
	        // 无论成功失败，结束加载状态
	        this.loadingMore = false;
	      }
	    });
	  },

    // 获取热门搜索词
    getHotSearchWords() {
      uni.request({
        url: `${this.$baseUrl}/api/search/novels/hot-words`,
        method: "GET",
        data: { limit: 10 },
        success: (res) => {
          if (res.data && res.data.success) {
            this.hotWords = res.data.hotWords || [];
          }
        }
      });
    },

    // 用热门词搜索
    searchWithHotWord(word) {
      this.keyword = word;
      this.search();
    },

    // 处理排序变化
    handleSortChange(e) {
      this.sortType = e.detail.value;
      this.search(); // 重新搜索
    },

    // 处理分页变化
    handlePageChange(e) {
      this.pageNum = e.current;
      this.loading = true;
      // 重新调用接口加载对应页数据
      uni.request({
        url: `${this.$baseUrl}/api/search/novels`,
        method: "GET",
        data: {
          keyword: this.keyword.trim(),
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          sortType: this.sortType
        },
        success: (res) => {
          if (res.data && res.data.success) {
            this.novels = res.data.novels || [];
          }
        },
        complete: () => {
          this.loading = false;
        }
      });
    },

    // 跳转到小说详情页
    goToNovelDetail(novelId) {
      uni.navigateTo({
        url: `/pages/novel/detail?novelId=${novelId}`
      });
    },

    // 高亮关键词
    highlightKeyword(text) {
      if (!text || !this.keyword) return text;
      const reg = new RegExp(this.keyword, "gi");
      return text.replace(reg, `<span class="highlight">${this.keyword}</span>`);
    },

    // 格式化数字（万/千）
    formatCount(count) {
      if (!count) return "0";
      if (count >= 10000) {
        return (count / 10000).toFixed(1) + "万";
      } else if (count >= 1000) {
        return (count / 1000).toFixed(1) + "千";
      }
      return count.toString();
    }
  }
};
</script>

<style>
.search-page {
  padding: 20rpx;
  background-color: #f9f9f9;
  min-height: 100vh;
}

/* 搜索头部 */
.search-header {
  display: flex;
  align-items: center;
  gap: 20rpx;
  margin-bottom: 30rpx;
}
.back-button {
  padding: 10rpx 20rpx;
  background: #f0f0f0;
  border-radius: 10rpx;
  font-size: 28rpx;
}
.search-input {
  flex: 1;
  padding: 20rpx;
  background-color: #fff;
  border: 1rpx solid #ddd;
  border-radius: 30rpx;
  font-size: 28rpx;
}
.search-btn {
  padding: 20rpx 40rpx;
  background: #FF69B4;
  color: white;
  border-radius: 30rpx;
  font-size: 28rpx;
}

/* 加载状态 */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
  color: #666;
  font-size: 28rpx;
}

/* 搜索结果区域 */
.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  font-size: 26rpx;
  color: #666;
  border-bottom: 1rpx solid #eee;
  margin-bottom: 20rpx;
}
.sort-container {
  flex-shrink: 0;
}
.sort-btn {
  display: flex;
  align-items: center;
  gap: 5rpx;
  color: #FF69B4;
}

/* 空结果 */
.empty-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 200rpx 0;
  color: #999;
  font-size: 28rpx;
  gap: 20rpx;
}

/* 结果列表 */
.results-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}
.novel-item {
  display: flex;
  gap: 20rpx;
  padding: 20rpx;
  background-color: #fff;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}
.novel-cover {
  width: 160rpx;
  height: 220rpx;
  flex-shrink: 0;
  border-radius: 8rpx;
}
.novel-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  overflow: hidden;
}
.novel-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  line-height: 1.3;
}
.status-tag {
  padding: 3rpx 10rpx;
  font-size: 22rpx;
  color: #fff;
  background-color: #FF69B4;
  border-radius: 4rpx;
  flex-shrink: 0;
}
.novel-author {
  font-size: 24rpx;
  color: #666;
}
.novel-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.novel-stats {
  display: flex;
  gap: 20rpx;
  font-size: 24rpx;
  color: #999;
}
.novel-tags {
  display: flex;
  gap: 10rpx;
  flex-wrap: wrap;
}
.tag {
  padding: 5rpx 15rpx;
  background-color: #f5f5f5;
  color: #666;
  font-size: 22rpx;
  border-radius: 15rpx;
}

/* 分页 */
.pagination {
  padding: 40rpx 0;
  display: flex;
  justify-content: center;
}

/* 热门搜索 */
.hot-search {
  padding: 20rpx;
  background-color: #fff;
  border-radius: 10rpx;
}
.hot-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}
.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}
.hot-tag {
  padding: 15rpx 25rpx;
  background-color: #f5f5f5;
  color: #333;
  font-size: 26rpx;
  border-radius: 30rpx;
  white-space: nowrap;
}
.hot-tag:active {
  background-color: #eee;
}

/* 关键词高亮 */
.highlight {
  color: #FF69B4;
  font-weight: bold;
}

/* 加载更多按钮 */
.load-more-btn {
  padding: 20rpx 0;
  margin: 10rpx 0 30rpx;
  text-align: center;
  background-color: #fff;
  border-radius: 10rpx;
  color: #FF69B4;
  font-size: 28rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}
.load-more-btn:active {
  background-color: #f5f5f5;
}
</style>