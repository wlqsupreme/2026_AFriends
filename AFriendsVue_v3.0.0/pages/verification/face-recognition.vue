<template>
	<view class="face-recognition-page">
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
			<text class="page-title">人脸识别</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 摄像头预览区域 -->
		<view class="camera-preview">
			<view class="preview-container">
				<view class="face-frame">
					<view class="frame-corner top-left"></view>
					<view class="frame-corner top-right"></view>
					<view class="frame-corner bottom-left"></view>
					<view class="frame-corner bottom-right"></view>
				</view>
				<view class="preview-placeholder">
					<view class="camera-icon">
						<view class="camera-lens"></view>
					</view>
					<text class="preview-text">摄像头预览区域</text>
				</view>
			</view>
		</view>
		
		<!-- 识别提示 -->
		<view class="recognition-tips">
			<text class="tips-title">请将脸部放入框内</text>
			<text class="tips-desc">保持光线充足，面部清晰可见</text>
		</view>
		
		<!-- 操作按钮区域 -->
		<view class="action-buttons">
			<view class="retake-button" @click="retakePhoto">
				<text class="button-text">重新拍摄</text>
			</view>
			<view class="confirm-button" @click="confirmRecognition">
				<text class="button-text">确认识别</text>
			</view>
		</view>
		
		<!-- 进度指示器 -->
		<view class="progress-indicator">
			<view class="progress-bar">
				<view class="progress-fill" :style="{ width: progress + '%' }"></view>
			</view>
			<text class="progress-text">{{ progress }}%</text>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
		
		<!-- 识别结果弹窗 -->
		<view class="result-modal" v-if="showResult" @click="hideResult">
			<view class="modal-content" @click.stop>
				<view class="result-icon" :class="recognitionResult">
					<text v-if="recognitionResult === 'success'" class="success-icon">✓</text>
					<text v-else class="error-icon">✗</text>
				</view>
				<text class="result-title">{{ resultTitle }}</text>
				<text class="result-desc">{{ resultDesc }}</text>
				<view class="result-actions">
					<view class="action-button" @click="hideResult">
						<text class="action-text">确定</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'FaceRecognitionPage',
		data() {
			return {
				progress: 0,
				showResult: false,
				recognitionResult: 'success', // 'success' 或 'error'
				resultTitle: '识别成功',
				resultDesc: '人脸识别已完成，请继续下一步',
				certificationType: 'realName' // 认证类型：realName 表示实名认证
			}
		},
		onLoad(options) {
			// 接收传递的认证类型参数
			if (options.type) {
				this.certificationType = options.type;
			}
		},
		mounted() {
			this.startRecognition();
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			startRecognition() {
				// 模拟识别进度
				this.progress = 0;
				const timer = setInterval(() => {
					this.progress += 10;
					if (this.progress >= 100) {
						clearInterval(timer);
						this.recognitionComplete();
					}
				}, 200);
			},
			recognitionComplete() {
				// 随机模拟成功或失败
				const isSuccess = Math.random() > 0.2;
				this.recognitionResult = isSuccess ? 'success' : 'error';
				this.resultTitle = isSuccess ? '识别成功' : '识别失败';
				this.resultDesc = isSuccess ? '人脸识别已完成，请继续下一步' : '请重新进行人脸识别';
				this.showResult = true;
			},
			retakePhoto() {
				this.progress = 0;
				this.startRecognition();
			},
			confirmRecognition() {
				if (this.progress < 100) {
					uni.showToast({
						title: '识别尚未完成',
						icon: 'none'
					});
					return;
				}
				
				if (this.recognitionResult === 'success') {
					uni.showToast({
						title: '认证完成',
						icon: 'success'
					});
					
					// 延迟跳转到认证反馈页面
					setTimeout(() => {
						// 根据认证类型跳转到相应的反馈页面
						if (this.certificationType === 'realName') {
							// 实名认证成功，跳转到认证反馈页面
							uni.redirectTo({
								url: '/pages/verification/certification-feedback?type=realName'
							});
						} else {
							// 其他认证类型，返回上一页
							uni.navigateBack();
						}
					}, 1500);
				} else {
					uni.showToast({
						title: '请重新识别',
						icon: 'none'
					});
				}
			},
			hideResult() {
				this.showResult = false;
			}
		}
	}
</script>

<style>
	.face-recognition-page {
		min-height: 100vh;
		background-color: #000000;
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
		background-color: transparent;
		z-index: 10;
	}
	
	.time {
		font-size: 28rpx;
		color: #FFFFFF;
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
		background-color: #FFFFFF;
		border-radius: 4rpx;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: transparent;
		z-index: 10;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: rgba(0, 0, 0, 0.5);
		border-radius: 24rpx;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #FFFFFF;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 摄像头预览区域 */
	.camera-preview {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 0 32rpx;
		position: relative;
	}
	
	.preview-container {
		width: 100%;
		height: 600rpx;
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.face-frame {
		position: absolute;
		width: 400rpx;
		height: 400rpx;
		z-index: 5;
	}
	
	.frame-corner {
		position: absolute;
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #00FF00;
	}
	
	.top-left {
		top: 0;
		left: 0;
		border-right: none;
		border-bottom: none;
	}
	
	.top-right {
		top: 0;
		right: 0;
		border-left: none;
		border-bottom: none;
	}
	
	.bottom-left {
		bottom: 0;
		left: 0;
		border-right: none;
		border-top: none;
	}
	
	.bottom-right {
		bottom: 0;
		right: 0;
		border-left: none;
		border-top: none;
	}
	
	.preview-placeholder {
		width: 100%;
		height: 100%;
		background-color: #1A1A1A;
		border-radius: 24rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		gap: 24rpx;
	}
	
	.camera-icon {
		width: 120rpx;
		height: 120rpx;
		background-color: #333333;
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}
	
	.camera-lens {
		width: 80rpx;
		height: 80rpx;
		background-color: #666666;
		border-radius: 40rpx;
		position: relative;
	}
	
	.camera-lens::after {
		content: '';
		position: absolute;
		top: 20rpx;
		left: 20rpx;
		width: 20rpx;
		height: 20rpx;
		background-color: #999999;
		border-radius: 10rpx;
	}
	
	.preview-text {
		font-size: 26rpx;
		color: #999999;
	}
	
	/* 识别提示 */
	.recognition-tips {
		padding: 32rpx;
		text-align: center;
	}
	
	.tips-title {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
		display: block;
		margin-bottom: 8rpx;
	}
	
	.tips-desc {
		font-size: 24rpx;
		color: #CCCCCC;
		display: block;
	}
	
	/* 操作按钮区域 */
	.action-buttons {
		display: flex;
		gap: 24rpx;
		padding: 0 32rpx 32rpx;
	}
	
	.retake-button, .confirm-button {
		flex: 1;
		height: 88rpx;
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	.retake-button {
		background-color: rgba(255, 255, 255, 0.2);
		border: 2rpx solid rgba(255, 255, 255, 0.3);
	}
	
	.confirm-button {
		background: linear-gradient(135deg, #00FF00, #00CC00);
	}
	
	.retake-button:active, .confirm-button:active {
		transform: scale(0.98);
	}
	
	.button-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 进度指示器 */
	.progress-indicator {
		padding: 0 32rpx 32rpx;
		display: flex;
		align-items: center;
		gap: 24rpx;
	}
	
	.progress-bar {
		flex: 1;
		height: 8rpx;
		background-color: rgba(255, 255, 255, 0.2);
		border-radius: 4rpx;
		overflow: hidden;
	}
	
	.progress-fill {
		height: 100%;
		background: linear-gradient(90deg, #00FF00, #00CC00);
		border-radius: 4rpx;
		transition: width 0.2s ease;
	}
	
	.progress-text {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 500;
		min-width: 60rpx;
		text-align: right;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: rgba(255, 255, 255, 0.6);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 识别结果弹窗 */
	.result-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.8);
		z-index: 1000;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.modal-content {
		width: 80%;
		background-color: #FFFFFF;
		border-radius: 24rpx;
		padding: 48rpx 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 24rpx;
	}
	
	.result-icon {
		width: 120rpx;
		height: 120rpx;
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.result-icon.success {
		background-color: #00FF00;
	}
	
	.result-icon.error {
		background-color: #FF0000;
	}
	
	.success-icon, .error-icon {
		font-size: 60rpx;
		color: #FFFFFF;
		font-weight: bold;
	}
	
	.result-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.result-desc {
		font-size: 26rpx;
		color: #666666;
		text-align: center;
		line-height: 1.5;
	}
	
	.result-actions {
		width: 100%;
		margin-top: 16rpx;
	}
	
	.action-button {
		width: 100%;
		height: 80rpx;
		background-color: #007AFF;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.action-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
</style>
