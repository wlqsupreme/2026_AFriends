<template>
	<view class="permissions-container">
		<!-- Header -->
		<!--<view class="header">
			<view class="header-left">
				<text class="back-icon" @click="goBack">‹</text>
			</view>
			<view class="header-title">
				<text>权限管理</text>
			</view>
			<view class="header-right">
				<text class="more-icon">⋯</text>
			</view>
		</view> -->

		<!-- Content -->
		<view class="content">
			<!-- Permission Categories -->
			<view class="permission-section">
				<view class="section-title">
					<text>应用权限</text>
				</view>
				
				<view class="permission-item" @click="showPermissionDialog('位置权限')">
					<view class="permission-info">
						<view class="permission-icon">📍</view>
						<view class="permission-details">
							<text class="permission-name">位置权限</text>
							<text class="permission-desc">用于获取您的位置信息</text>
						</view>
					</view>
					<view class="permission-status">
						<text class="status-text">已授权</text>
						<text class="arrow">›</text>
					</view>
				</view>

				<view class="permission-item" @click="showPermissionDialog('相机权限')">
					<view class="permission-info">
						<view class="permission-icon">📷</view>
						<view class="permission-details">
							<text class="permission-name">相机权限</text>
							<text class="permission-desc">用于拍照和视频通话</text>
						</view>
					</view>
					<view class="permission-status">
						<text class="status-text">已授权</text>
						<text class="arrow">›</text>
					</view>
				</view>

				<view class="permission-item" @click="showPermissionDialog('麦克风权限')">
					<view class="permission-info">
						<view class="permission-icon">🎤</view>
						<view class="permission-details">
							<text class="permission-name">麦克风权限</text>
							<text class="permission-desc">用于语音通话和录音</text>
						</view>
					</view>
					<view class="permission-status">
						<text class="status-text">已授权</text>
						<text class="arrow">›</text>
					</view>
				</view>

				<view class="permission-item" @click="goToSystemSettings">
					<view class="permission-info">
						<view class="permission-icon">📱</view>
						<view class="permission-details">
							<text class="permission-name">通讯录权限</text>
							<text class="permission-desc">用于同步联系人信息</text>
						</view>
					</view>
					<view class="permission-status">
						<text class="status-text">未授权</text>
						<text class="arrow">›</text>
					</view>
				</view>

				<view class="permission-item" @click="goToSystemSettings">
					<view class="permission-info">
						<view class="permission-icon">⚙️</view>
						<view class="permission-details">
							<text class="permission-name">其他权限</text>
							<text class="permission-desc">系统相关权限设置</text>
						</view>
					</view>
					<view class="permission-status">
						<text class="status-text">系统管理</text>
						<text class="arrow">›</text>
					</view>
				</view>
			</view>
		</view>

		<!-- Permission Dialog Overlay -->
		<view v-if="showDialog" class="permission-overlay" @click="hidePermissionDialog">
			<view class="permission-dialog" @click.stop>
				<view class="dialog-header">
					<text class="dialog-title">{{ currentPermissionTitle }}</text>
					<text class="close-btn" @click="hidePermissionDialog">×</text>
				</view>
				<view class="dialog-content">
					<text class="dialog-desc">{{ getPermissionDescription(currentPermissionType) }}</text>
				</view>
				<view class="dialog-actions">
					<button class="action-btn cancel-btn" @click="hidePermissionDialog">取消</button>
					<button class="action-btn confirm-btn" @click="confirmPermission">确认</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'PermissionsPage',
	data() {
		return {
			showDialog: false,
			currentPermissionTitle: '',
			currentPermissionType: ''
		}
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		showPermissionDialog(permissionTitle) {
			this.currentPermissionTitle = permissionTitle
			this.currentPermissionType = permissionTitle
			this.showDialog = true
		},
		hidePermissionDialog() {
			this.showDialog = false
		},
		confirmPermission() {
			uni.showToast({
				title: '权限设置已更新',
				icon: 'success'
			})
			this.hidePermissionDialog()
		},
		goToSystemSettings() {
			uni.showModal({
				title: '系统设置',
				content: '即将跳转到系统设置页面',
				showCancel: true,
				confirmText: '确定',
				cancelText: '取消',
				success: (res) => {
					if (res.confirm) {
						uni.showToast({
							title: '跳转系统设置',
							icon: 'none'
						})
					}
				}
			})
		},
		getPermissionDescription(permissionType) {
			const descriptions = {
				'位置权限': '位置权限允许应用获取您的地理位置信息，用于提供基于位置的服务和功能。',
				'相机权限': '相机权限允许应用使用您的设备摄像头进行拍照、录制视频和视频通话。',
				'麦克风权限': '麦克风权限允许应用使用您的设备麦克风进行语音通话、录音和语音识别。'
			}
			return descriptions[permissionType] || '该权限用于提供相关功能和服务。'
		}
	}
}
</script>

<style scoped>
.permissions-container {
	background-color: #ffffff;
	min-height: 100vh;
}

.header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 30rpx;
	border-bottom: 1rpx solid #f0f0f0;
	background-color: #ffffff;
}

.header-left, .header-right {
	width: 60rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.back-icon, .more-icon {
	font-size: 40rpx;
	color: #333333;
}

.header-title {
	flex: 1;
	text-align: center;
}

.header-title text {
	font-size: 36rpx;
	font-weight: 600;
	color: #333333;
}

.content {
	padding: 30rpx;
}

.permission-section {
	margin-bottom: 40rpx;
}

.section-title {
	margin-bottom: 30rpx;
}

.section-title text {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.permission-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx 0;
	border-bottom: 1rpx solid #f5f5f5;
}

.permission-info {
	display: flex;
	align-items: center;
	flex: 1;
}

.permission-icon {
	font-size: 40rpx;
	margin-right: 20rpx;
}

.permission-details {
	display: flex;
	flex-direction: column;
}

.permission-name {
	font-size: 30rpx;
	color: #333333;
	margin-bottom: 8rpx;
}

.permission-desc {
	font-size: 24rpx;
	color: #999999;
}

.permission-status {
	display: flex;
	align-items: center;
}

.status-text {
	font-size: 26rpx;
	color: #666666;
	margin-right: 10rpx;
}

.arrow {
	font-size: 30rpx;
	color: #cccccc;
}

/* Permission Dialog Styles */
.permission-overlay {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 1000;
}

.permission-dialog {
	background-color: #ffffff;
	border-radius: 20rpx;
	width: 600rpx;
	max-width: 90vw;
	overflow: hidden;
}

.dialog-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 30rpx;
	border-bottom: 1rpx solid #f0f0f0;
}

.dialog-title {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.close-btn {
	font-size: 40rpx;
	color: #999999;
	width: 40rpx;
	height: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.dialog-content {
	padding: 30rpx;
}

.dialog-desc {
	font-size: 28rpx;
	color: #666666;
	line-height: 1.6;
}

.dialog-actions {
	display: flex;
	border-top: 1rpx solid #f0f0f0;
}

.action-btn {
	flex: 1;
	padding: 25rpx;
	border: none;
	background: none;
	font-size: 30rpx;
}

.cancel-btn {
	color: #999999;
	border-right: 1rpx solid #f0f0f0;
}

.confirm-btn {
	color: #007aff;
}
</style>



