<template>
	<view class="container">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal">
					<view class="bar bar-1"></view>
					<view class="bar bar-2"></view>
					<view class="bar bar-3"></view>
					<view class="bar bar-4"></view>
				</view>
				<view class="wifi">📶</view>
				<view class="battery">
					<view class="battery-body">
						<view class="battery-level"></view>
					</view>
					<view class="battery-tip"></view>
				</view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<view class="nav-bar">
			<view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">{{$t('report.evidence.title')}}</text>
		</view>
		
		<!-- 说明横幅 -->
		<view class="instruction-banner">
			<text class="instruction-text">{{$t('report.evidence.instruction')}}</text>
		</view>
		
		<!-- 举报理由 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">{{$t('report.evidence.reasonLabel')}}</text>
				<text class="selected-reason">{{selectedReason}}</text>
			</view>
		</view>
		
		<!-- 举报描述 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">{{$t('report.evidence.descriptionLabel')}}</text>
			</view>
			<view class="description-container">
				<textarea 
					class="description-input" 
					v-model="descriptionText" 
					:maxlength="400"
					:placeholder="$t('report.evidence.descriptionPlaceholder')"
					@input="updateCharCount"
				></textarea>
				<view class="char-count">{{charCount}}/400</view>
			</view>
		</view>
		
		<!-- 图片证据 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">{{$t('report.evidence.imageLabel')}}</text>
				<text class="upload-limit">{{$t('report.evidence.imageLimit')}}</text>
			</view>
			<view class="image-upload-container">
				<view class="upload-button" @click="uploadImage">
					<text class="plus-icon">+</text>
				</view>
				<view class="uploaded-images" v-if="uploadedImages.length > 0">
					<view class="image-item" v-for="(image, index) in uploadedImages" :key="index">
						<image :src="image" class="uploaded-image"></image>
						<view class="delete-button" @click="deleteImage(index)">×</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 提交说明 -->
		<view class="submit-info">
			<text class="info-text">{{$t('report.evidence.submitInfo')}}</text>
		</view>
		
		<!-- 提交按钮 -->
		<view class="submit-button" @click="submitReport">
			<text class="submit-text">{{$t('report.evidence.submit')}}</text>
		</view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	
	export default {
		data() {
			return {
				selectedReason: this.$t('report.reason.porn'),
				descriptionText: '',
				charCount: 0,
				uploadedImages: [],
				userId: 1000100,
				reportedUserId: null,
				contentId: null,
				contentType: null,
				categoryId: 1,
				loading: false,
				hasError: false,
				errorMessage: ''
			}
		},
		onLoad(options) {
			const themeStore = useThemeStore();
			themeStore.init();
			// 接收从举报页面传递过来的举报原因
			if (options.reason) {
				this.selectedReason = decodeURIComponent(options.reason);
			}
			// 接收其他参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			if (options.reportedUserId) {
				this.reportedUserId = parseInt(options.reportedUserId);
			}
			if (options.contentId) {
				this.contentId = parseInt(options.contentId);
			}
			if (options.contentType) {
				this.contentType = options.contentType;
			}
			// 根据举报原因设置类别ID
			this.categoryId = this.getCategoryIdByReason(this.selectedReason);
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			updateCharCount(event) {
				this.charCount = event.detail.value.length;
			},
			uploadImage() {
				// 模拟图片上传
				uni.chooseImage({
					count: 9 - this.uploadedImages.length,
					success: (res) => {
						// 这里应该上传到服务器，这里只是模拟
						this.uploadedImages = this.uploadedImages.concat(res.tempFilePaths);
					}
				});
			},
			deleteImage(index) {
				this.uploadedImages.splice(index, 1);
			},
			async submitReport() {
				if (!this.descriptionText.trim()) {
					uni.showToast({
						title: this.$t('report.evidence.fillDescription'),
						icon: 'none'
					});
					return;
				}
				
				try {
					console.log('=== 开始提交举报 ===');
					console.log('用户ID:', this.userId);
					console.log('被举报用户ID:', this.reportedUserId);
					console.log('内容ID:', this.contentId);
					console.log('内容类型:', this.contentType);
					console.log('举报类别ID:', this.categoryId);
					console.log('举报描述:', this.descriptionText);
					console.log('证据图片:', this.uploadedImages);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: '${this.$baseUrl}/api/report/submit',
						method: 'POST',
						data: {
							userId: this.userId,
							reportedUserId: this.reportedUserId,
							contentId: this.contentId,
							contentType: this.contentType,
							categoryId: this.categoryId,
							description: this.descriptionText,
							evidenceImg: JSON.stringify(this.uploadedImages)
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 举报API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						console.log('举报提交成功');
						
						uni.showToast({
							title: this.$t('common.success'),
							icon: 'success'
						});
						
						// 延迟跳转到反馈页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/report/report-feedback?type=${encodeURIComponent(this.selectedReason)}&reportId=${response.data.reportId}`
							});
						}, 1500);
					} else {
						console.error('举报提交失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || this.$t('common.failed');
						uni.showToast({
							title: response.data.message || this.$t('common.failed'),
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('举报提交异常:', error);
					this.hasError = true;
					this.errorMessage = this.$t('common.networkError');
					uni.showToast({
						title: this.$t('report.evidence.submitFailed'),
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 根据举报原因获取类别ID
			getCategoryIdByReason(reason) {
				const reasonMap = {
					[this.$t('report.reason.porn')]: 1,
					[this.$t('report.reason.political')]: 2,
					[this.$t('report.reason.fraud')]: 3,
					[this.$t('report.reason.discrimination')]: 4,
					[this.$t('report.reason.abuse')]: 5,
					[this.$t('report.reason.cyberbullying')]: 6,
					[this.$t('report.reason.externalLink')]: 7,
					[this.$t('report.reason.illegal')]: 8,
					[this.$t('report.reason.minor')]: 9,
					[this.$t('report.reason.other')]: 10
				};
				return reasonMap[reason] || 10;
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background-color: var(--color-bg-light, #f5f5f5);
		padding-bottom: 120rpx;
    background-color: var(--color-bg);
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		z-index: 1000;
		position: relative;
	}
	
	.time {
		font-size: 32rpx;
		font-weight: 600;
		color: var(--color-text, #000);
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.signal {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.bar {
		width: 6rpx;
		background-color: var(--color-text, #000);
		border-radius: 2rpx;
	}
	
	.bar-1 {
		height: 8rpx;
	}
	
	.bar-2 {
		height: 12rpx;
	}
	
	.bar-3 {
		height: 16rpx;
	}
	
	.bar-4 {
		height: 20rpx;
	}
	
	.wifi {
		font-size: 24rpx;
		color: var(--color-text, #000);
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid var(--color-text, #000);
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: var(--color-text, #000);
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: var(--color-text, #000);
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: var(--color-bg, #fff);
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		position: relative;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text, #000);
		font-weight: bold;
	}
	
	.nav-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 36rpx;
		font-weight: bold;
		color: var(--color-text, #000);
	}
	
	/* 说明横幅 */
	.instruction-banner {
		background-color: #87CEEB;
		padding: 30rpx 40rpx;
		margin: 30rpx 40rpx;
		border-radius: 20rpx;
	}
	
	.instruction-text {
		color: #fff;
		font-size: 28rpx;
		text-align: center;
		line-height: 1.5;
	}
	
	/* 内容区域 */
	.content-section {
		background-color: var(--color-bg, #fff);
		margin: 20rpx 40rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	}
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}
	
	.section-label {
		font-size: 32rpx;
		font-weight: bold;
		color: var(--color-text, #000);
	}
	
	.selected-reason {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666);
	}
	
	.upload-limit {
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999);
	}
	
	/* 举报描述 */
	.description-container {
		position: relative;
	}
	
	.description-input {
		width: 100%;
		min-height: 200rpx;
		padding: 20rpx;
		border: 1rpx solid var(--color-border, #e0e0e0);
		border-radius: 10rpx;
		font-size: 28rpx;
		line-height: 1.5;
		box-sizing: border-box;
		background-color: var(--color-bg, #fff);
		color: var(--color-text, #000);
	}
	
	.char-count {
		position: absolute;
		bottom: 20rpx;
		right: 20rpx;
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999);
	}
	
	/* 图片上传 */
	.image-upload-container {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}
	
	.upload-button {
		width: 120rpx;
		height: 120rpx;
		background-color: var(--color-bg-light, #f0f0f0);
		border: 2rpx dashed var(--color-border, #ccc);
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.plus-icon {
		font-size: 48rpx;
		color: var(--color-text-placeholder, #999);
		font-weight: bold;
	}
	
	.uploaded-images {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}
	
	.image-item {
		position: relative;
		width: 120rpx;
		height: 120rpx;
	}
	
	.uploaded-image {
		width: 100%;
		height: 100%;
		border-radius: 10rpx;
	}
	
	.delete-button {
		position: absolute;
		top: -10rpx;
		right: -10rpx;
		width: 40rpx;
		height: 40rpx;
		background-color: #ff4444;
		color: #fff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		font-weight: bold;
		cursor: pointer;
	}
	
	/* 提交说明 */
	.submit-info {
		padding: 0 40rpx;
		margin: 40rpx 0;
	}
	
	.info-text {
		font-size: 24rpx;
		color: var(--color-text-placeholder, #999);
		line-height: 1.5;
		text-align: center;
	}
	
	/* 提交按钮 */
	.submit-button {
		position: fixed;
		bottom: 40rpx;
		left: 40rpx;
		right: 40rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		padding: 30rpx;
		border-radius: 20rpx;
		text-align: center;
		box-shadow: 0 10rpx 25rpx rgba(255, 105, 180, 0.3);
		cursor: pointer;
		transition: transform 0.2s ease;
	}
	
	.submit-button:active {
		transform: scale(0.98);
	}
	
	.submit-text {
		color: #fff;
		font-size: 36rpx;
		font-weight: bold;
	}
</style>