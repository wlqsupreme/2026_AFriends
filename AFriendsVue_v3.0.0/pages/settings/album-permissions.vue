<template>
	<view class="album-permissions-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">相册权限</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 相册权限设置 -->
		<view class="permission-section">
			<view class="permission-header">
				<text class="permission-title">相册权限</text>
				<view class="go-to-settings" @click="goToSettings">
					<text class="settings-text">去设置</text>
					<view class="arrow-right"></view>
				</view>
			</view>
		</view>
		
		<!-- 权限选项列表 -->
		<view class="permissions-list">
			<!-- 位置信息权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许使用相册内容的位置信息</text>
					<text class="permission-description">关闭后,将不使用本地相册和已经上传的相册内容中的位置信息,如发布时无法为你推荐相册中照片和视频的拍摄地点等</text>
				</view>
				<view class="toggle-switch" :class="{ active: locationPermission }" @click="toggleLocationPermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
			
			<!-- 快捷分享权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许快捷分享相册内容到Afriends</text>
					<text class="permission-description">关闭后,你无法将系统相册中的图片和视频快捷分享给Afriends朋友</text>
				</view>
				<view class="toggle-switch" :class="{ active: sharePermission }" @click="toggleSharePermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
			
			<!-- 识别分析权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许识别并分析相册内容</text>
					<text class="permission-description">在你进入相册后,我们仅会在授权访问的相册范围内识别分析,在设备本地运行,关闭后,将不再对您手机相册中的内容进行识别,如不再提供相册搜索、人像筛选、推荐影集等功能</text>
				</view>
				<view class="toggle-switch" :class="{ active: analysisPermission }" @click="toggleAnalysisPermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'AlbumPermissionsPage',
		data() {
			return {
				locationPermission: false,
				sharePermission: false,
				analysisPermission: false
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			goToSettings() {
				// #ifndef H5
				uni.showModal({
					title: '相册权限设置',
					content: '即将跳转到系统设置页面，请在相册权限中开启相应权限。',
					showCancel: true,
					confirmText: '去设置',
					cancelText: '取消',
					success: (res) => {
						if (res.confirm) {
							// 调用系统权限设置API
							uni.openAppAuthorizeSetting({
								success: function() {
									console.log('打开系统权限设置成功');
								},
								fail: function(error) {
									console.error('打开系统权限设置失败:', error);
									uni.showToast({
										title: '请手动在系统设置中开启权限',
										icon: 'none',
										duration: 2000
									});
								}
							});
						}
					}
				});
				// #endif
				
				// #ifdef H5
				uni.showToast({
					title: '请在真机环境中测试此功能',
					icon: 'none',
					duration: 3000
				});
				// #endif
			},
			toggleLocationPermission() {
				this.locationPermission = !this.locationPermission;
				// 保存设置到后端或本地存储
				this.saveAlbumPermission('location', this.locationPermission);
			},
			toggleSharePermission() {
				this.sharePermission = !this.sharePermission;
				this.saveAlbumPermission('share', this.sharePermission);
			},
			toggleAnalysisPermission() {
				this.analysisPermission = !this.analysisPermission;
				this.saveAlbumPermission('analysis', this.analysisPermission);
			},
			/**
			 * @description 保存相册权限设置
			 * @param {String} type - 权限类型
			 * @param {Boolean} status - 权限状态
			 */
			saveAlbumPermission(type, status) {
				// 这里可以调用后端API保存设置
				console.log(`保存相册${type}权限设置:`, status);
				
				// TODO: 后端持久化暂未实现
				// 示例API调用（需要后端支持）
				/*
				uni.request({
					url: '${this.$baseUrl}/api/album-permissions/save',
					method: 'POST',
					data: {
						userId: this.userId, // 需要获取当前用户ID
						permissionType: type,
						status: status
					},
					success: (res) => {
						console.log('权限设置保存成功');
					},
					fail: (error) => {
						console.error('权限设置保存失败:', error);
					}
				});
				*/
			}
		},
		// 页面加载完成后执行
		mounted() {
			// 页面加载时检查相册相关权限
			// #ifndef H5
			uni.getSetting({
				success: (res) => {
					// 相册权限通常不需要单独授权，但可以检查相册访问权限
					// 这里的实现取决于具体平台和需求
					console.log('相册权限设置:', res.authSetting);
				},
				fail: (error) => {
					console.error('获取相册权限状态失败:', error);
				}
			});
			// #endif
			
			// #ifdef H5
			console.log('H5环境：模拟相册权限检查');
			// TODO: 后端持久化暂未实现
			// #endif
		}
	}
</script>

<style>
	.album-permissions-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: #FFFFFF;
	}
	
	.time {
		font-size: 28rpx;
		color: #000000;
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
		background-color: #000000;
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
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
		border-right: 12rpx solid #000000;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 相册权限设置 */
	.permission-section {
		padding: 24rpx 32rpx;
		background-color: #F8F8F8;
		margin: 24rpx 32rpx;
		border-radius: 16rpx;
	}
	
	.permission-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.permission-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.go-to-settings {
		display: flex;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
	}
	
	.settings-text {
		font-size: 24rpx;
		color: #666666;
	}
	
	.arrow-right {
		width: 0;
		height: 0;
		border-left: 8rpx solid #666666;
		border-top: 6rpx solid transparent;
		border-bottom: 6rpx solid transparent;
	}
	
	/* 权限选项列表 */
	.permissions-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.permission-item {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.permission-item:last-child {
		border-bottom: none;
	}
	
	.permission-content {
		flex: 1;
		margin-right: 24rpx;
	}
	
	.permission-name {
		display: block;
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
		margin-bottom: 16rpx;
		line-height: 1.4;
	}
	
	.permission-description {
		display: block;
		font-size: 24rpx;
		color: #999999;
		line-height: 1.5;
	}
	
	/* 开关样式 */
	.toggle-switch {
		width: 80rpx;
		height: 44rpx;
		background-color: #E0E0E0;
		border-radius: 22rpx;
		position: relative;
		transition: all 0.3s ease;
		flex-shrink: 0;
	}
	
	.toggle-switch.active {
		background-color: #34C759;
	}
	
	.toggle-circle {
		width: 40rpx;
		height: 40rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		transition: all 0.3s ease;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}
	
	.toggle-switch.active .toggle-circle {
		left: 38rpx;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
</style>