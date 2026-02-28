<template>
	<view class="professional-certification-page">
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
			<text class="page-title">{{ $t('verification.professional.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 提示文字 -->
    <view class="disclaimer">
      <text class="disclaimer-text">{{ $t('verification.professional.disclaimer') }}</text>
    </view>

    <!-- 认证表单 -->
    <view class="verification-form">
      <!-- 职业类型选择区域 -->
      <view class="profession-selection">
        <!-- 姓名输入框 -->
        <view class="input-field">
          <text class="field-label">{{ $t('verification.professional.name') }}</text>
          <view class="input-content">
            <input
              class="text-input"
              type="text"
              :placeholder="$t('verification.professional.namePlaceholder')"
              v-model="realName"
              maxlength="20"
            />
          </view>
        </view>

        <view class="divider"></view>

        <view class="selection-field" @click="showProfessionPicker" v-if="!loadError">
          <text class="field-label">{{ $t('verification.professional.jobType') }}</text>
          <view class="selection-content">
            <text class="selected-text" v-if="selectedProfession">{{ selectedJobType }} - {{ selectedProfession }}</text>
            <text class="selected-text" v-else-if="selectedJobType">{{ selectedJobType }} - {{ $t('verification.professional.jobTypePlaceholder') }}</text>
            <text class="placeholder-text" v-else-if="!loading">{{ $t('verification.professional.jobTypePlaceholder') }}</text>
            <text class="loading-text" v-else>{{ $t('verification.professional.loading') }}</text>
          </view>
        </view>

        <view class="divider"></view>

        <!-- 公司输入框 -->
        <view class="input-field">
          <text class="field-label">{{ $t('verification.professional.company') }}</text>
          <view class="input-content">
            <input
              class="text-input"
              type="text"
              :placeholder="$t('verification.professional.companyPlaceholder')"
              v-model="company"
              maxlength="50"
            />
          </view>
        </view>

        <!-- 错误提示 -->
        <view class="error-section" v-if="loadError">
          <view class="error-icon">⚠️</view>
          <text class="error-text">{{ errorMessage }}</text>
          <view class="retry-button" @click="loadJobData">
            <text class="retry-text">{{ $t('verification.professional.retry') }}</text>
          </view>
        </view>
      </view>
    </view>
		
		<!-- 协议同意区域 -->
		<view class="agreement-section">
			<view class="agreement-item">
				<view class="checkbox" :class="{ checked: isAgreed }" @click="toggleAgreement">
					<text v-if="isAgreed" class="checkmark">✓</text>
				</view>
				<text class="agreement-text">{{ $t('verification.professional.agreementText') }}</text>
				<text class="agreement-link" @click="goToTermsOfService">{{ $t('verification.professional.agreementLink') }}</text>
			</view>
		</view>
		
		<!-- 下一步按钮 -->
		<view class="next-button-section">
			<view class="next-button" :class="{ disabled: !isFormValid }" @click="goToNextStep">
				<text class="next-text">{{ $t('verification.professional.nextStep') }}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
		
		<!-- 职业类型选择弹窗 -->
		<view class="profession-picker-modal" v-if="showPicker" @click="hideProfessionPicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<view class="modal-back" @click="hideProfessionPicker">
						<view class="back-arrow"></view>
					</view>
					<text class="modal-title">{{ $t('verification.professional.selectJobType') }}</text>
					<view class="search-icon">
						<view class="magnifier"></view>
					</view>
				</view>
				<view class="profession-list">
					<view class="profession-item" v-for="(profession, index) in jobTypes" :key="index" @click="selectProfession(profession)">
						<text class="profession-name">{{ profession }}</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 具体职业选择弹窗 -->
		<view class="job-picker-modal" v-if="showJobPicker" @click="hideJobPickerModal">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<view class="modal-back" @click="backToJobType">
						<view class="back-arrow"></view>
					</view>
					<text class="modal-title">{{ selectedJobType }} - {{ $t('verification.professional.selectJob') }}</text>
					<view class="search-icon">
						<view class="magnifier"></view>
					</view>
				</view>
				<view class="job-list">
					<view class="job-item" v-for="(job, index) in getJobsByType(selectedJobType)" :key="index" @click="selectJob(job)">
						<text class="job-name">{{ job.jobName }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'ProfessionalCertificationPage',
		data() {
			return {
				realName: '',
        company: '',
				isAgreed: false,
				showPicker: false,
				showJobPicker: false,
				professions: [],
				jobTypes: [],
				selectedJobType: '',
				selectedProfession: '',
				loading: false,
				loadError: false,
				errorMessage: '',
				currentStep: 'type' // 'type' 或 'job'
			}
		},
		computed: {
			// 检查表单是否有效
			isFormValid() {
				return (
					this.realName && 
					this.realName.length >= 2 && 
					this.selectedJobType && 
					this.selectedProfession && 
					this.company && 
					this.isAgreed
				);
			}
		},
		onLoad() {
			this.loadJobData();
			
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		},
		methods: {
			// 加载职业数据
			async loadJobData() {
				this.loading = true;
				this.loadError = false;
				this.errorMessage = '';
				
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-job-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data && response.data.length > 0) {
						// 从职业数据中提取职业类型
						const jobTypeSet = new Set();
						response.data.forEach(job => {
							if (job.jobType) {
								jobTypeSet.add(job.jobType);
							}
						});
						this.jobTypes = Array.from(jobTypeSet).sort();
						this.professions = response.data;
						console.log('成功加载职业数据:', this.professions.length, '个职业');
					} else {
						// 数据库没有数据，显示错误信息
						this.loadError = true;
						this.errorMessage = this.$t('verification.professional.noData');
						this.jobTypes = [];
						this.professions = [];
					}
				} catch (error) {
					console.error('获取职业数据失败:', error);
					this.loadError = true;
					this.errorMessage = this.$t('verification.professional.networkFailed');
					this.jobTypes = [];
					this.professions = [];
					
					uni.showToast({
						title: this.$t('verification.professional.dataLoadFailed'),
						icon: 'none',
						duration: 3000
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 根据职业类型获取具体职业列表
			getJobsByType(jobType) {
				return this.professions.filter(job => job.jobType === jobType);
			},
			
			// 显示具体职业选择弹窗
			showJobPickerModal() {
				this.showJobPicker = true;
			},
			
			// 隐藏具体职业选择弹窗
			hideJobPickerModal() {
				this.showJobPicker = false;
			},
			
			// 选择具体职业
			selectJob(job) {
				console.log('选择具体职业:', job);
				console.log('job类型:', typeof job);
				console.log('job.jobName:', job.jobName);
				
				if (job && job.jobName) {
					this.selectedProfession = job.jobName;
					this.hideJobPickerModal();
					this.currentStep = 'type';
				} else {
					console.error('职业对象格式错误:', job);
					uni.showToast({
						title: '选择失败，请重试',
						icon: 'none'
					});
				}
			},
			
			// 返回职业类型选择
			backToJobType() {
				this.showJobPicker = false;
				this.showPicker = true;
				this.currentStep = 'type';
			},
			
			goBack() {
				uni.navigateBack();
			},
			toggleAgreement() {
				this.isAgreed = !this.isAgreed;
			},
			showProfessionPicker() {
				this.showPicker = true;
			},
			hideProfessionPicker() {
				this.showPicker = false;
			},
			selectProfession(profession) {
				console.log('选择职业类型:', profession);
				this.selectedJobType = profession;
				this.hideProfessionPicker();
				// 显示具体职业选择弹窗
				this.showJobPicker = true;
				this.currentStep = 'job';
			},
			async goToNextStep() {
				// 检查表单是否有效
				if (!this.isFormValid) {
					if (!this.realName) {
						uni.showToast({
							title: this.$t('verification.professional.validName'),
							icon: 'none'
						});
					} else if (this.realName.length < 2) {
						uni.showToast({
							title: this.$t('verification.professional.validNameLength'),
							icon: 'none'
						});
					} else if (!this.selectedJobType) {
						uni.showToast({
							title: this.$t('verification.professional.validJobType'),
							icon: 'none'
						});
					} else if (!this.selectedProfession) {
						uni.showToast({
							title: this.$t('verification.professional.validJob'),
							icon: 'none'
						});
					} else if (!this.company) {
						uni.showToast({
							title: this.$t('verification.professional.validCompany'),
							icon: 'none'
						});
					} else if (!this.isAgreed) {
						uni.showToast({
							title: this.$t('verification.professional.agreementRequired'),
							icon: 'none'
						});
					}
					return;
				}
				
				try {
					// 显示加载提示
					uni.showLoading({
						title: this.$t('verification.professional.submitting')
					});
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.hideLoading();
						uni.showToast({
							title: this.$t('verification.professional.loginRequired'),
							icon: 'none'
						});
						return;
					}
					
					// 构建认证信息JSON
					const certInfo = {
						name: this.realName,
						jobType: this.selectedJobType,
						jobName: this.selectedProfession,
						company: this.company,
						position: this.selectedProfession,
						submitTime: new Date().toISOString()
					};
					
					// 调用后端API保存认证记录
					const response = await this.saveCertRecord(userId, 'JOB', certInfo);
					
					uni.hideLoading();
					
					if (response.success) {
						uni.showToast({
							title: this.$t('verification.professional.submitSuccess'),
							icon: 'success'
						});
						
						// 跳转到认证反馈页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/verification/certification-feedback?type=professional&jobType=${encodeURIComponent(this.selectedJobType)}&jobName=${encodeURIComponent(this.selectedProfession)}`
							});
						}, 1500);
					} else {
						uni.showToast({
							title: response.message || this.$t('verification.professional.submitFailed'),
							icon: 'none'
						});
					}
					
				} catch (error) {
					uni.hideLoading();
					console.error('提交认证信息失败:', error);
					uni.showToast({
						title: this.$t('verification.professional.networkError'),
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
	.professional-certification-page {
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
		color: #999999;
	}

  /* 认证表单 */
  .verification-form {
    flex: 1;
    padding: 0 32rpx;
  }
	
	/* 职业类型选择区域 */
	.profession-selection {
		padding: 0 32rpx;
	}
	
  /* 输入框样式 */
  .input-field {
    height: 120rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16rpx;
    background-color: var(--color-card, #FFFFFF);
  }
  
  .input-content {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
  
  .text-input {
    font-size: 26rpx;
    color: var(--color-text, #333333);
    text-align: right;
    border: none;
    outline: none;
    background: transparent;
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
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 职业类型选择弹窗 */
	.profession-picker-modal {
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
	
	.profession-list {
		max-height: 60vh;
		overflow-y: auto;
	}
	
	.profession-item {
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
	}
	
	.profession-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.profession-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
	}
	
	/* 具体职业选择弹窗 */
	.job-picker-modal {
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
	
	.job-list {
		max-height: 60vh;
		overflow-y: auto;
	}
	
	.job-item {
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
	}
	
	.job-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.job-item:last-child {
		border-bottom: none;
	}
	
	.job-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
  
  /* 分割线 */
  .divider {
    height: 1rpx;
    background-color: var(--color-border, #F0F0F0);
    margin: 0 16rpx;
  }
</style>