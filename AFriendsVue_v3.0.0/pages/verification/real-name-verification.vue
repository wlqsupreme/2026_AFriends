<template>
	<view class="real-name-verification-page">
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
			<text class="page-title">{{ $t('verification.realName.title') }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 说明文字 -->
		<view class="disclaimer">
			<text class="disclaimer-text">{{ $t('verification.realName.disclaimer') }}</text>
		</view>
		
		<!-- 认证表单 -->
		<view class="verification-form">
			<!-- 证件类型 -->
			<view class="form-item" @click="openDocumentTypePicker" v-if="!loadError">
				<text class="item-label">{{ $t('verification.realName.idType') }}</text>
				<view class="item-content">
					<text class="item-value" v-if="selectedDocumentType">{{ selectedDocumentType }}</text>
					<text class="placeholder-text" v-else-if="!loading">{{ $t('verification.realName.idTypePlaceholder') }}</text>
					<text class="loading-text" v-else>{{ $t('verification.realName.loading') }}</text>
				</view>
			</view>
			
			<!-- 错误提示 -->
			<view class="error-section" v-if="loadError">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="retry-button" @click="loadDocumentTypes">
					<text class="retry-text">重试</text>
				</view>
			</view>
			
			<view class="divider"></view>
			
			<!-- 真实姓名 -->
			<view class="form-item">
				<text class="item-label">{{ $t('verification.realName.realName') }}</text>
				<view class="item-content">
					<input 
						class="input-field" 
						type="text" 
						:placeholder="$t('verification.realName.realNamePlaceholder')"
						v-model="realName"
						maxlength="20"
					/>
				</view>
			</view>
			
			<view class="divider"></view>
			
			<!-- 性别 -->
			<view class="form-item" @click="openGenderPicker">
				<text class="item-label">{{ $t('verification.realName.gender') }}</text>
				<view class="item-content">
					<text class="item-value" v-if="selectedGender">{{ selectedGender }}</text>
					<text class="placeholder-text" v-else>{{ $t('verification.realName.genderPlaceholder') }}</text>
				</view>
			</view>
			
			<view class="divider"></view>
			
			<!-- 证件号码 -->
			<view class="form-item">
				<text class="item-label">{{ $t('verification.realName.idNumber') }}</text>
				<view class="item-content">
					<input 
						class="input-field" 
						type="text" 
						:placeholder="$t('verification.realName.idNumberPlaceholder')"
						v-model="documentNumber"
						maxlength="18"
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
				<text class="agreement-text">{{ $t('verification.realName.agreementText') }}</text>
				<text class="agreement-link" @click.stop="goToTermsOfService">{{ $t('verification.realName.agreementLink') }}</text>
			</view>
		</view>
		
		<!-- 进入人脸识别按钮 -->
		<view class="face-recognition-button" :class="{ disabled: !canProceed }" @click="goToFaceRecognition">
			<text class="button-text">{{ $t('verification.realName.faceRecognitionButton') }}</text>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
		
		<!-- 证件类型选择弹窗 -->
		<view class="document-type-modal" v-if="showDocumentTypePicker" @click="hideDocumentTypePicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">{{ $t('verification.realName.selectIdType') }}</text>
					<view class="close-button" @click="hideDocumentTypePicker">
						<text class="close-icon">×</text>
					</view>
				</view>
				<view class="document-type-list">
					<view 
						class="document-type-item" 
						v-for="(type, index) in documentTypes" 
						:key="index"
						@click="selectDocumentType(type)"
					>
						<text class="type-name">{{ type }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 性别选择弹窗 -->
		<view class="document-type-modal" v-if="showGenderPicker" @click="hideGenderPicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">{{ $t('verification.realName.selectGender') }}</text>
					<view class="close-button" @click="hideGenderPicker">
						<text class="close-icon">×</text>
					</view>
				</view>
				<view class="document-type-list">
					<view 
						class="document-type-item" 
						v-for="(gender, index) in genders" 
						:key="index"
						@click="selectGender(gender)"
					>
						<text class="type-name">{{ gender }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		name: 'RealNameVerificationPage',
		data() {
			return {
				selectedDocumentType: '',
				realName: '',
				selectedGender: '',
				documentNumber: '',
				isAgreed: false,
				showDocumentTypePicker: false,
				showGenderPicker: false,
				documentTypes: [],
				genders: ['男', '女'],
				loading: false,
				loadError: false,
				errorMessage: ''
			}
		},
		onLoad() {
			this.loadDocumentTypes();
			
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
		},
		computed: {
			canProceed() {
				return this.selectedDocumentType && this.realName && this.selectedGender && this.documentNumber && this.isAgreed;
			}
		},
		methods: {
			// 加载证件类型数据
			async loadDocumentTypes() {
				this.loading = true;
				this.loadError = false;
				this.errorMessage = '';
				
				try {
					const response = await uni.request({
						url: '${this.$baseUrl}/api/bc-entities/cert-realname-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data && response.data.length > 0) {
						this.documentTypes = response.data.map(item => item.idType);
						console.log('成功加载证件类型:', this.documentTypes);
					} else {
						// 数据库没有数据，显示错误信息
						this.loadError = true;
						this.errorMessage = '数据库中没有证件类型数据，请联系管理员';
						this.documentTypes = [];
					}
				} catch (error) {
					console.error('获取证件类型失败:', error);
					this.loadError = true;
					this.errorMessage = '网络连接失败，请检查网络后重试';
					this.documentTypes = [];
					
					uni.showToast({
						title: '数据加载失败',
						icon: 'none',
						duration: 3000
					});
				} finally {
					this.loading = false;
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			goToTermsOfService() {
				console.log('goToTermsOfService 被调用了');
				uni.showToast({
					title: '点击了协议链接',
					icon: 'success'
				});
				
				// 延迟一下再跳转，让用户看到提示
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/settings/terms-of-service',
						success: () => {
							console.log('跳转成功');
						},
						fail: (err) => {
							console.error('跳转失败:', err);
							uni.showToast({
								title: '跳转失败',
								icon: 'none'
							});
						}
					});
				}, 1000);
			},
			openDocumentTypePicker() {
				this.showDocumentTypePicker = true;
			},
			hideDocumentTypePicker() {
				this.showDocumentTypePicker = false;
			},
			selectDocumentType(type) {
				this.selectedDocumentType = type;
				this.hideDocumentTypePicker();
			},
			openGenderPicker() {
				this.showGenderPicker = true;
			},
			hideGenderPicker() {
				this.showGenderPicker = false;
			},
			selectGender(gender) {
				this.selectedGender = gender;
				this.hideGenderPicker();
			},
			toggleAgreement() {
				this.isAgreed = !this.isAgreed;
			},
			async goToFaceRecognition() {
				if (!this.canProceed) {
					uni.showToast({
						title: this.$t('verification.realName.completeInfo'),
						icon: 'none'
					});
					return;
				}
				
				// 验证姓名格式
				if (this.realName.length < 2) {
					uni.showToast({
						title: this.$t('verification.realName.validName'),
						icon: 'none'
					});
					return;
				}
				
				// 验证证件号码格式
				if (this.selectedDocumentType === '身份证' && this.documentNumber.length !== 18) {
					uni.showToast({
						title: this.$t('verification.realName.validIdNumber'),
						icon: 'none'
					});
					return;
				}
				
				try {
					// 显示加载提示
					uni.showLoading({
						title: this.$t('verification.realName.submitting')
					});
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.hideLoading();
						uni.showToast({
							title: this.$t('verification.realName.loginRequired'),
							icon: 'none'
						});
						return;
					}
					
					// 构建认证信息JSON
					const certInfo = {
						name: this.realName,
						gender: this.selectedGender,
						idNumber: this.documentNumber,
						documentType: this.selectedDocumentType,
						submitTime: new Date().toISOString()
					};
					
					// 调用后端API保存认证记录
					const response = await this.saveCertRecord(userId, 'REALNAME', certInfo);
					
					uni.hideLoading();
					
					if (response.success) {
						uni.showToast({
							title: this.$t('verification.realName.submitSuccess'),
							icon: 'success'
						});
						
						// 跳转到人脸识别页面
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/verification/face-recognition?type=realName'
							});
						}, 1500);
					} else {
						uni.showToast({
							title: response.message || this.$t('verification.realName.submitFailed'),
							icon: 'none'
						});
					}
					
				} catch (error) {
					uni.hideLoading();
					console.error('提交认证信息失败:', error);
					uni.showToast({
						title: this.$t('verification.realName.networkError'),
						icon: 'none'
					});
				}
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
	.real-name-verification-page {
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
	
	/* 认证表单 */
	.verification-form {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.form-item {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16rpx;
		background-color: var(--color-card, #FFFFFF);
		cursor: pointer;
	}
	
	.form-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.item-label {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.item-content {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.item-value {
		font-size: 26rpx;
		color: var(--color-text, #333333);
	}
	
	.placeholder-text {
		font-size: 26rpx;
		color: #CCCCCC;
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
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
		font-weight: 300;
	}
	
	.input-field {
		flex: 1;
		height: 60rpx;
		font-size: 26rpx;
		color: var(--color-text, #333333);
		text-align: right;
		border: none;
		outline: none;
		background: transparent;
	}
	
	.divider {
		height: 1rpx;
		background-color: var(--color-border, #F0F0F0);
		margin: 0 16rpx;
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
		cursor: pointer;
		user-select: none;
	}
	
	.agreement-link:active {
		opacity: 0.7;
	}
	
	/* 进入人脸识别按钮 */
	.face-recognition-button {
		margin: 0 32rpx 32rpx;
		height: 88rpx;
		background: linear-gradient(135deg, #FF69B4, #FF1493);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	.face-recognition-button.disabled {
		background: #CCCCCC;
		cursor: not-allowed;
	}
	
	.face-recognition-button:active:not(.disabled) {
		transform: scale(0.98);
	}
	
	.button-text {
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
	
	/* 证件类型选择弹窗 */
	.document-type-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.modal-content {
		width: 80%;
		background-color: var(--color-card, #FFFFFF);
		border-radius: 24rpx;
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
	
	.modal-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.close-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.close-icon {
		font-size: 32rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 300;
	}
	
	.document-type-list {
		max-height: 60vh;
		overflow-y: auto;
	}
	
	.document-type-item {
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
		cursor: pointer;
	}
	
	.document-type-item:active {
		background-color: var(--color-bg-weak, #F8F8F8);
	}
	
	.document-type-item:last-child {
		border-bottom: none;
	}
	
	.type-name {
		font-size: 28rpx;
		color: var(--color-text, #333333);
	}
</style>