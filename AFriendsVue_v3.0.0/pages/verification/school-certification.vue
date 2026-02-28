<template>
	<view class="school-certification-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">{{ $t('verification.school.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 提示文字 -->
    <view class="disclaimer">
      <text class="disclaimer-text">{{ $t('verification.school.disclaimer') }}</text>
    </view>

    <!-- 认证表单 -->
    <view class="verification-form">
      <!-- 学校选择区域 -->
      <view class="school-selection">
        <view class="selection-field" @click="showSchoolPicker" v-if="!loadError">
          <text class="field-label">{{ $t('verification.school.schoolName') }}</text>
          <view class="selection-content">
            <text class="selected-text" v-if="selectedSchool">{{ selectedSchool }}</text>
            <text class="placeholder-text" v-else-if="!loading">{{ $t('verification.school.selectSchool') }}</text>
            <text class="loading-text" v-else>{{ $t('verification.school.loading') }}</text>
          </view>
        </view>

        <!-- 错误提示 -->
        <view class="error-section" v-if="loadError">
          <view class="error-icon">⚠️</view>
          <text class="error-text">{{ errorMessage }}</text>
          <view class="retry-button" @click="loadSchoolData">
            <text class="retry-text">{{ $t('verification.school.retry') }}</text>
          </view>
        </view>
      </view>

      <!-- 个人信息输入区域 -->
      <view class="info-input-section">
        <!-- 姓名输入框 -->
        <view class="input-field">
          <text class="field-label">{{ $t('verification.school.name') }}</text>
          <input
            class="input-content"
            type="text"
            :placeholder="$t('verification.school.namePlaceholder')"
            v-model="name"
            maxlength="20"
          />
        </view>
        <view class="divider"></view>

        <!-- 学历选择框 -->
        <view class="selection-field" @click="openDegreePicker">
          <text class="field-label">{{ $t('verification.school.degree') }}</text>
          <view class="selection-content">
            <text class="selected-text" v-if="degree">{{ degree }}</text>
            <text class="placeholder-text" v-else>{{ $t('verification.school.degreePlaceholder') }}</text>
          </view>
        </view>
        <view class="divider"></view>

        <!-- 学号输入框 -->
        <view class="input-field">
          <text class="field-label">{{ $t('verification.school.studentId') }}</text>
          <input
            class="input-content"
            type="text"
            :placeholder="$t('verification.school.studentIdPlaceholder')"
            v-model="studentId"
            maxlength="30"
          />
        </view>
      </view>
    </view>
		
		<!-- 协议同意区域 -->
		<view class="agreement-section">
			<view class="agreement-item">
				<view class="checkbox" :class="{ checked: isAgreed }" @click="toggleAgreement">
					<text v-if="isAgreed" class="checkmark">✓</text>
				</view>
				<text class="agreement-text">{{ $t('verification.school.agreementText') }}</text>
				<text class="agreement-link" @click="goToTermsOfService">{{ $t('verification.school.agreementLink') }}</text>
			</view>
		</view>
		
		<!-- 下一步按钮 -->
		<view class="next-button-section">
			<view class="next-button" :class="{ disabled: !isFormValid }" @click="goToNextStep">
				<text class="next-text">{{ $t('verification.school.nextStep') }}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
		
		<!-- 学历选择弹窗 -->
		<view class="school-picker-modal" v-if="showDegreePicker" @click="closeDegreePicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<view class="modal-back" @click="closeDegreePicker">
						<view class="back-arrow"></view>
					</view>
					<text class="modal-title">{{ $t('verification.school.selectDegree') }}</text>
					<view class="placeholder"></view>
				</view>
				<view class="school-list">
					<view 
						class="school-item" 
						v-for="(degreeOption, index) in degreeOptions" 
						:key="index" 
						@click="selectDegree(degreeOption)"
					>
						<text class="province-name">{{ degreeOption }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 学校选择弹窗 -->
		<view class="school-picker-modal" v-if="showPicker" @click="hideSchoolPicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<view class="modal-back" @click="handleModalBack">
						<view class="back-arrow"></view>
					</view>
					<text class="modal-title">{{ currentView === 'province' ? $t('verification.school.selectProvince') : $t('verification.school.selectSchoolTitle') }}</text>
					<view class="search-icon" @click="toggleSearch" v-if="currentView === 'school'">
						<view class="magnifier"></view>
					</view>
					<view class="placeholder" v-else></view>
				</view>
				
				<!-- 搜索框 -->
				<view class="search-box" v-if="showSearch && currentView === 'school'">
					<input 
						class="search-input" 
						type="text" 
						:placeholder="$t('verification.school.searchPlaceholder')"
						v-model="searchKeyword"
						@input="handleSearch"
					/>
				</view>
				
				<!-- 省份列表 -->
				<view class="school-list" v-if="currentView === 'province'">
					<view class="school-item" v-for="(province, index) in provinces" :key="index" @click="selectProvince(province)">
						<text class="province-name">{{ province }}</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
				
				<!-- 学校列表 -->
				<view class="school-list" v-if="currentView === 'school'">
					<view 
						class="school-item" 
						v-for="(school, index) in filteredSchools" 
						:key="index" 
						@click="selectSchool(school)"
					>
						<text class="school-name">{{ school.schoolName }}</text>
					</view>
					<view class="empty-tip" v-if="filteredSchools.length === 0">
						<text class="empty-text">{{ $t('verification.school.noSchoolData') }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'SchoolCertificationPage',
		data() {
			return {
				isAgreed: false,
				showPicker: false,
				provinces: [],
				schools: [],
				selectedProvince: '',
				selectedSchool: '',
				loading: false,
				loadError: false,
				errorMessage: '',
				// 弹窗相关状态
				currentView: 'province', // 'province' 或 'school'
				currentProvinceSchools: [], // 当前选中省份的学校列表
				showSearch: false, // 是否显示搜索框
				searchKeyword: '', // 搜索关键词
				// 个人信息
				name: '', // 姓名
				degree: '', // 学历
				studentId: '', // 学号
				showDegreePicker: false, // 是否显示学历选择弹窗
				// 学历选项
				degreeOptions: ['小学', '初中', '高中', '中专', '大专', '本科', '硕士', '博士']
			}
		},
		computed: {
			// 过滤后的学校列表（支持搜索）
			filteredSchools() {
				if (!this.searchKeyword) {
					return this.currentProvinceSchools;
				}
				const keyword = this.searchKeyword.toLowerCase();
				return this.currentProvinceSchools.filter(school => {
					return school.schoolName && school.schoolName.toLowerCase().includes(keyword);
				});
			},
			// 检查表单是否有效
			isFormValid() {
				return (
					this.selectedSchool && 
					this.name && 
					this.name.length >= 2 && 
					this.degree && 
					this.studentId && 
					this.isAgreed
				);
			}
		},
		onLoad() {
			this.loadSchoolData();
				
				// 初始化主题
				const themeStore = useThemeStore();
				themeStore.init();
				themeStore.applyTheme();
		},
		methods: {
			// 加载学校数据
			async loadSchoolData() {
				this.loading = true;
				this.loadError = false;
				this.errorMessage = '';
				
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-student-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data && response.data.length > 0) {
						this.schools = response.data;
						// 从学校数据中提取省份
						const provinceSet = new Set();
						response.data.forEach(school => {
							if (school.region) {
								provinceSet.add(school.region);
							}
						});
						this.provinces = Array.from(provinceSet).sort();
						console.log('成功加载学校数据:', this.schools.length, '所学校');
					} else {
						// 数据库没有数据，显示错误信息
						this.loadError = true;
						this.errorMessage = this.$t('verification.school.noData');
						this.provinces = [];
						this.schools = [];
					}
				} catch (error) {
					console.error('获取学校数据失败:', error);
					this.loadError = true;
					this.errorMessage = this.$t('verification.school.networkFailed');
					this.provinces = [];
					this.schools = [];
					
					uni.showToast({
						title: this.$t('verification.school.dataLoadFailed'),
						icon: 'none',
						duration: 3000
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 根据省份获取学校列表
			getSchoolsByProvince(province) {
				return this.schools.filter(school => school.region === province);
			},
			
			goBack() {
				uni.navigateBack();
			},
			toggleAgreement() {
				this.isAgreed = !this.isAgreed;
			},
			showSchoolPicker() {
				// 重置状态
				this.currentView = 'province';
				this.currentProvinceSchools = [];
				this.showSearch = false;
				this.searchKeyword = '';
				this.showPicker = true;
			},
			hideSchoolPicker() {
				this.showPicker = false;
				// 重置状态
				this.currentView = 'province';
				this.currentProvinceSchools = [];
				this.showSearch = false;
				this.searchKeyword = '';
			},
			// 处理弹窗返回按钮
			handleModalBack() {
				if (this.currentView === 'school') {
					// 从学校列表返回省份列表
					this.currentView = 'province';
					this.currentProvinceSchools = [];
					this.showSearch = false;
					this.searchKeyword = '';
				} else {
					// 关闭弹窗
					this.hideSchoolPicker();
				}
			},
			// 选择省份
			selectProvince(province) {
				console.log('选择省份:', province);
				this.selectedProvince = province;
				// 获取该省份下的学校列表
				this.currentProvinceSchools = this.getSchoolsByProvince(province);
				// 切换到学校列表视图
				this.currentView = 'school';
				this.showSearch = false;
				this.searchKeyword = '';
			},
			// 选择学校
			selectSchool(school) {
				console.log('选择学校:', school);
				this.selectedSchool = school.schoolName;
				this.selectedProvince = school.region || this.selectedProvince;
				// 关闭弹窗
				this.hideSchoolPicker();
				// 显示选择成功提示
				uni.showToast({
					title: '已选择：' + school.schoolName,
					icon: 'success',
					duration: 2000
				});
			},
			// 切换搜索框显示
			toggleSearch() {
				this.showSearch = !this.showSearch;
				if (!this.showSearch) {
					this.searchKeyword = '';
				}
			},
			// 处理搜索
			handleSearch() {
				// 搜索逻辑在 computed 属性 filteredSchools 中处理
			},
			
			// 显示学历选择弹窗
			openDegreePicker() {
				this.showDegreePicker = true;
			},
			
			// 隐藏学历选择弹窗
			closeDegreePicker() {
				this.showDegreePicker = false;
			},
			
			// 选择学历
			selectDegree(degree) {
				this.degree = degree;
				this.closeDegreePicker();
			},
			
			async goToNextStep() {
				// 检查表单是否有效
				if (!this.isFormValid) {
					if (!this.selectedSchool) {
						uni.showToast({
							title: this.$t('verification.school.validSchool'),
							icon: 'none'
						});
					} else if (!this.name) {
						uni.showToast({
							title: this.$t('verification.school.validName'),
							icon: 'none'
						});
					} else if (this.name.length < 2) {
						uni.showToast({
							title: this.$t('verification.school.validNameLength'),
							icon: 'none'
						});
					} else if (!this.degree) {
						uni.showToast({
							title: this.$t('verification.school.validDegree'),
							icon: 'none'
						});
					} else if (!this.studentId) {
						uni.showToast({
							title: this.$t('verification.school.validStudentId'),
							icon: 'none'
						});
					} else if (!this.isAgreed) {
						uni.showToast({
							title: this.$t('verification.school.agreementRequired'),
							icon: 'none'
						});
					}
					return;
				}
				
				try {
					// 显示加载提示
					uni.showLoading({
						title: this.$t('verification.school.submitting')
					});
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.hideLoading();
						uni.showToast({
							title: this.$t('verification.school.loginRequired'),
							icon: 'none'
						});
						return;
					}
					
					// 构建认证信息JSON
					const certInfo = {
						name: this.name || '',
						degree: this.degree || '',
						school: this.selectedSchool,
						province: this.selectedProvince,
						studentId: this.studentId || '',
						submitTime: new Date().toISOString()
					};
					
					// 调用后端API保存认证记录
					const response = await this.saveCertRecord(userId, 'STUDENT', certInfo);
					
					uni.hideLoading();
					
					if (response.success) {
						uni.showToast({
							title: this.$t('verification.school.submitSuccess'),
							icon: 'success'
						});
						
						// 跳转到认证反馈页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/verification/certification-feedback?type=school&school=${encodeURIComponent(this.selectedSchool)}&province=${encodeURIComponent(this.selectedProvince)}`
							});
						}, 1500);
					} else {
						uni.showToast({
							title: response.message || this.$t('verification.school.submitFailed'),
							icon: 'none'
						});
					}
					
				} catch (error) {
					uni.hideLoading();
					console.error('提交认证信息失败:', error);
					uni.showToast({
						title: this.$t('verification.school.networkError'),
						icon: 'none'
					});
				}
			},
			goToTermsOfService() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			
			// 保存认证记录到后端
			async saveCertRecord(userId, certType, certInfo) {
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/u-entities/user-cert-record/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							userId: userId,
							certType: certType,
							certInfo: JSON.stringify(certInfo)
						},
						timeout: 10000
					});
					
					if (response.statusCode === 200) {
						return response.data;
					} else {
						throw new Error(`HTTP ${response.statusCode}`);
					}
				} catch (error) {
					console.error('保存认证记录失败:', error);
					throw error;
				}
			}
		}
	}
</script>

<style>
	.school-certification-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
		position: relative;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
	}
	
	.time {
		font-size: 28rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.signal, .wifi, .battery {
		width: 24rpx;
		height: 24rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
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
	
	/* 提示文字 */
	.prompt-text {
		padding: 24rpx 32rpx;
	}
	
	.prompt {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
	}

  /* 认证表单 */
  .verification-form {
    flex: 1;
    padding: 0 32rpx;
  }

  /* 说明文字 */
  .disclaimer {
    padding: 24rpx 32rpx;
    background-color: var(--color-bg-weak, #F8F8F8);
  }

  .disclaimer-text {
    font-size: 26rpx;
    color: var(--color-text-secondary, #666666);
    line-height: 1.5;
  }
	
	/* 学校选择区域 */
	.school-selection {
		padding: 0 32rpx;
	}
	
	/* 个人信息输入区域 */
	.info-input-section {
		padding: 0 32rpx;
	}
	
	.input-field {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.input-content {
		flex: 1;
		text-align: right;
		font-size: 26rpx;
		color: var(--color-text, #333333);
		padding: 0 16rpx;
	}
	
	.input-content::placeholder {
		color: #CCCCCC;
	}
	
	.selection-field {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
	}
	
	.selection-field:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.field-label {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.selection-content {
		display: flex;
		align-items: center;
	}
	
	.placeholder-text {
		font-size: 26rpx;
		color: #CCCCCC;
	}
	
	.selected-text {
		font-size: 26rpx;
		color: var(--color-text, #333333);
	}
	
	.loading-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
	}
	
	/* 错误提示样式 */
	.error-section {
		padding: 32rpx;
		background-color: #FFF2F0;
		border: 1rpx solid #FFCCC7;
		border-radius: 8rpx;
		margin: 16rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
	}
	
	.error-icon {
		font-size: 48rpx;
	}
	
	.error-text {
		font-size: 26rpx;
		color: #FF4D4F;
		text-align: center;
		line-height: 1.5;
	}
	
	.retry-button {
		padding: 16rpx 32rpx;
		background-color: #FF4D4F;
		border-radius: 8rpx;
		cursor: pointer;
	}
	
	.retry-button:active {
		background-color: #D9363E;
	}
	
	.retry-text {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 500;
	}

  /* 协议同意区域 */
  .agreement-section {
    padding: 32rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .agreement-item {
    display: flex;
    align-items: center;
    gap: 16rpx;
  }
	
	.checkbox {
		width: 32rpx;
		height: 32rpx;
		border: 2rpx solid #CCCCCC;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.checkbox.checked {
		background-color: #007AFF;
		border-color: #007AFF;
	}
	
	.checkmark {
		color: #FFFFFF;
		font-size: 20rpx;
		font-weight: bold;
	}
	
	.agreement-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.agreement-link {
		font-size: 26rpx;
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 下一步按钮 */
	.next-button-section {
		padding: 0 32rpx 32rpx;
	}
	
	.next-button {
		height: 88rpx;
		background: linear-gradient(135deg, #FF69B4, #FF1493);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	.next-button.disabled {
		background: #CCCCCC;
		cursor: not-allowed;
	}
	
	.next-button:active:not(.disabled) {
		transform: scale(0.98);
	}
	
	.next-text {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 学校选择弹窗 */
	.school-picker-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		display: flex;
		align-items: flex-end;
	}
	
	.modal-content {
		width: 100%;
		background-color: var(--color-card, #FFFFFF);
		border-radius: 24rpx 24rpx 0 0;
		max-height: 80vh;
		overflow: hidden;
	}
	
	.modal-header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.modal-back {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.modal-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.search-icon {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.magnifier {
		width: 24rpx;
		height: 24rpx;
		border: 2rpx solid var(--color-text-secondary, #666666);
		border-radius: 50%;
		position: relative;
	}
	
	.magnifier::after {
		content: '';
		position: absolute;
		bottom: -6rpx;
		right: -6rpx;
		width: 12rpx;
		height: 2rpx;
		background-color: var(--color-text-secondary, #666666);
		transform: rotate(45deg);
	}
	
	.school-list {
		max-height: 60vh;
		overflow-y: auto;
	}
	
	.school-item {
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
	}
	
	.school-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.province-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
	}
	
	/* 搜索框样式 */
	.search-box {
		padding: 16rpx 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.search-input {
		width: 100%;
		height: 64rpx;
		padding: 0 24rpx;
		background-color: var(--color-bg-weak, #F5F5F5);
		border-radius: 32rpx;
		font-size: 26rpx;
		color: var(--color-text, #333333);
	}
	
	.search-input::placeholder {
		color: #999999;
	}
	
	/* 学校名称样式 */
	.school-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
	
	/* 空数据提示 */
	.empty-tip {
		padding: 60rpx 32rpx;
		text-align: center;
	}
	
	.empty-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #999999);
	}
	
	.placeholder {
		width: 48rpx;
	}
</style>