<template>
	<view class="device-management-page" :class="{ care: isCare }">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">{{ $t('device.title') }}</text>
			<view class="manage-button" @click="showManageOptions">
				<text class="manage-text">{{ $t('device.manage') }}</text>
			</view>
		</view> -->
		
		<!-- 信息提示栏 -->
		<view class="info-bar">
			<text class="info-text">{{ $t('device.infoText') }}</text>
			<text class="help-link" @click="showHelp">{{ $t('device.haveQuestion') }}</text>
		</view>
		
		<!-- 加载状态 -->
		<view v-if="isLoading" class="loading-container">
			<text class="loading-text">{{ $t('common.loading') }}</text>
		</view>
		
		<!-- 设备列表 -->
		<view v-else class="device-list">
			<view v-if="devices.length === 0" class="empty-container">
				<text class="empty-text">暂无设备记录</text>
			</view>
			<view v-for="device in devices" :key="device.id" class="device-item">
				<view class="device-icon">
					<view v-if="device.type && (device.type.toLowerCase().includes('iphone') || device.type.toLowerCase().includes('android') || device.type.toLowerCase().includes('手机'))" class="icon-phone"></view>
					<view v-else-if="device.type && device.type.toLowerCase().includes('ipad')" class="icon-phone"></view>
					<view v-else class="icon-desktop"></view>
				</view>
				<view class="device-info">
					<text class="device-name">{{ device.name }}</text>
					<text class="device-type">{{ device.type }}</text>
					<text class="device-login">{{ device.lastLogin }} | {{ device.location }}</text>
				</view>
				<view v-if="device.isCurrent" class="device-status">
					<text class="status-text">{{ $t('device.thisDevice') }}</text>
				</view>
				<view v-else class="device-actions">
					<view class="action-btn" @click="removeDevice(device.id)">
						<text class="action-text">{{ $t('device.remove') }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'DeviceManagementPage',
		data() {
			return {
				userId: null, // 用户ID
				devices: [], // 设备列表
				isLoading: true, // 加载状态
				currentDeviceId: null, // 当前设备ID
				modeStore: null
			}
		},
		onLoad() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
			
			// 获取用户ID
			this.userId = uni.getStorageSync('userId') || 1000100;
			
			// 加载设备列表
			this.loadDeviceList();
		},
		computed: {
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		methods: {
			/**
			 * 加载设备列表
			 */
			async loadDeviceList() {
				try {
					this.isLoading = true;
					
					const response = await uni.request({
						url: `${this.$baseUrl}/api/u-entities/user-device/list/${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					if (response.statusCode === 200 && response.data.success) {
						this.devices = response.data.data.map(device => ({
							id: device.id,
							name: device.deviceName || device.deviceModel || '未知设备',
							type: device.deviceType || device.deviceModel || '未知',
							model: device.deviceModel || '',
							lastLogin: this.formatDateTime(device.lastLoginTime),
							location: device.loginLocation || '未知位置',
							isCurrent: device.isCurrent === 1 || device.isCurrent === '1',
							deviceIdentifier: device.deviceIdentifier
						}));
						
						// 找到当前设备ID
						const currentDevice = this.devices.find(d => d.isCurrent);
						if (currentDevice) {
							this.currentDeviceId = currentDevice.id;
						}
					} else {
						uni.showToast({
							title: response.data.message || '加载设备列表失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('加载设备列表失败:', error);
					uni.showToast({
						title: '网络错误，请稍后重试',
						icon: 'none'
					});
				} finally {
					this.isLoading = false;
				}
			},
			
			/**
			 * 格式化日期时间
			 */
			formatDateTime(timestamp) {
				if (!timestamp) return '未知时间';
				const date = new Date(timestamp);
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				const hours = String(date.getHours()).padStart(2, '0');
				const minutes = String(date.getMinutes()).padStart(2, '0');
				return `${year}-${month}-${day} ${hours}:${minutes}`;
			},
			
			goBack() {
				uni.navigateBack();
			},
			showManageOptions() {
				uni.showActionSheet({
					itemList: ['批量移除设备', '导出设备列表', '设备安全设置'],
					success: (res) => {
						switch (res.tapIndex) {
							case 0:
								this.batchRemoveDevices();
								break;
							case 1:
								this.exportDeviceList();
								break;
							case 2:
								this.deviceSecuritySettings();
								break;
						}
					}
				});
			},
			showHelp() {
				uni.showModal({
					title: this.$t('common.help'),
					content: this.$t('device.infoText'),
					showCancel: false,
					confirmText: this.$t('common.ok')
				});
			},
			
			/**
			 * 删除设备
			 */
			async removeDevice(deviceId) {
				// 不能删除当前设备
				if (deviceId === this.currentDeviceId) {
					uni.showToast({
						title: '不能删除当前设备',
						icon: 'none'
					});
					return;
				}
				
				uni.showModal({
					title: this.$t('common.confirm'),
					content: this.$t('device.remove') + '?',
					success: async (res) => {
						if (res.confirm) {
							try {
								const response = await uni.request({
									url: `${this.$baseUrl}/api/u-entities/user-device/${deviceId}?userId=${this.userId}`,
									method: 'DELETE',
									header: {
										'Content-Type': 'application/json'
									}
								});
								
								if (response.statusCode === 200 && response.data.success) {
									uni.showToast({
										title: this.$t('device.remove') + this.$t('common.success'),
										icon: 'success'
									});
									// 重新加载设备列表
									this.loadDeviceList();
								} else {
									uni.showToast({
										title: response.data.message || '删除失败',
										icon: 'none'
									});
								}
							} catch (error) {
								console.error('删除设备失败:', error);
								uni.showToast({
									title: '删除失败，请稍后重试',
									icon: 'none'
								});
							}
						}
					}
				});
			},
			
			/**
			 * 批量移除设备
			 */
			async batchRemoveDevices() {
				// 获取所有非当前设备
				const nonCurrentDevices = this.devices.filter(device => !device.isCurrent);
				
				if (nonCurrentDevices.length === 0) {
					uni.showToast({
						title: '没有可删除的设备',
						icon: 'none'
					});
					return;
				}
				
				uni.showModal({
					title: '批量移除',
					content: `确定要移除所有非本机设备吗？共${nonCurrentDevices.length}台设备`,
					success: async (res) => {
						if (res.confirm) {
							try {
								const deviceIds = nonCurrentDevices.map(d => d.id);
								
								const response = await uni.request({
									url: '${this.$baseUrl}/api/u-entities/user-device/batch-delete',
									method: 'POST',
									header: {
										'Content-Type': 'application/json'
									},
									data: {
										userId: this.userId,
										deviceIds: deviceIds
									}
								});
								
								if (response.statusCode === 200 && response.data.success) {
									uni.showToast({
										title: '批量移除完成',
										icon: 'success'
									});
									// 重新加载设备列表
									this.loadDeviceList();
								} else {
									uni.showToast({
										title: response.data.message || '批量删除失败',
										icon: 'none'
									});
								}
							} catch (error) {
								console.error('批量删除设备失败:', error);
								uni.showToast({
									title: '批量删除失败，请稍后重试',
									icon: 'none'
								});
							}
						}
					}
				});
			},
			
			exportDeviceList() {
				uni.showToast({
					title: '导出功能开发中',
					icon: 'none'
				});
			},
			deviceSecuritySettings() {
				uni.showToast({
					title: '安全设置功能开发中',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.device-management-page {
		min-height: 100vh;
		background-color: var(--color-bg, #FFFFFF);
		display: flex;
		flex-direction: column;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: var(--color-card, #FFFFFF);
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
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.manage-button {
		padding: 8rpx 16rpx;
		border-radius: 16rpx;
		background-color: var(--color-bg-weak, #F5F5F5);
	}
	
	.manage-text {
		font-size: 26rpx;
		color: var(--color-text, #000000);
		font-weight: 500;
	}
	
	/* 信息提示栏 */
	.info-bar {
		padding: 24rpx 32rpx;
		background-color: var(--color-bg-weak, #F8F8F8);
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.info-text {
		font-size: 26rpx;
		color: var(--color-text, #000000);
		line-height: 1.4;
	}
	
	.help-link {
		font-size: 26rpx;
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 加载状态 */
	.loading-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999999);
	}
	
	/* 空状态 */
	.empty-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999999);
	}
	
	/* 设备列表 */
	.device-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.device-item {
		display: flex;
		align-items: center;
		gap: 24rpx;
		padding: 32rpx 0;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.device-item:last-child {
		border-bottom: none;
	}
	
	.device-icon {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}
	
	.icon-phone {
		width: 40rpx;
		height: 40rpx;
		background-color: var(--color-text, #000000);
		border-radius: 8rpx;
		position: relative;
	}
	
	.icon-phone::before {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 16rpx;
		height: 2rpx;
		background-color: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.icon-desktop {
		width: 40rpx;
		height: 30rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		position: relative;
	}
	
	.icon-desktop::after {
		content: '';
		position: absolute;
		bottom: -8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 20rpx;
		height: 4rpx;
		background-color: var(--color-text, #000000);
		border-radius: 2rpx;
	}
	
	.device-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.device-name {
		font-size: 28rpx;
		color: var(--color-text, #000000);
		font-weight: 600;
	}
	
	.device-type {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.device-login {
		font-size: 24rpx;
		color: var(--color-text-secondary, #999999);
		line-height: 1.4;
	}
	
	.device-status {
		padding: 8rpx 16rpx;
		background-color: var(--color-bg-weak, #F0F0F0);
		border-radius: 16rpx;
	}
	
	.status-text {
		font-size: 22rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 500;
	}
	
	.device-actions {
		display: flex;
		align-items: center;
	}
	
	.action-btn {
		padding: 8rpx 16rpx;
		background-color: #F44336;
		border-radius: 16rpx;
	}
	
	.action-text {
		font-size: 22rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}

	.device-management-page.care .info-bar {
		padding: 36rpx 40rpx;
		gap: 20rpx;
		box-sizing: border-box;
	}

	.device-management-page.care .info-text {
		font-size: 32rpx;
		line-height: 1.6;
	}

	.device-management-page.care .help-link {
		font-size: 32rpx;
	}

	.device-management-page.care .loading-text,
	.device-management-page.care .empty-text {
		font-size: 32rpx;
	}

	.device-management-page.care .device-list {
		padding: 0 40rpx;
		box-sizing: border-box;
	}

	.device-management-page.care .device-item {
		padding: 44rpx 0;
		gap: 32rpx;
	}

	.device-management-page.care .device-icon {
		width: 88rpx;
		height: 88rpx;
	}

	.device-management-page.care .icon-phone {
		width: 56rpx;
		height: 56rpx;
		border-radius: 12rpx;
	}

	.device-management-page.care .icon-desktop {
		width: 56rpx;
		height: 42rpx;
		border-radius: 6rpx;
	}

	.device-management-page.care .device-name {
		font-size: 34rpx;
	}

	.device-management-page.care .device-type {
		font-size: 32rpx;
	}

	.device-management-page.care .device-login {
		font-size: 30rpx;
		line-height: 1.6;
	}

	.device-management-page.care .device-status {
		padding: 14rpx 22rpx;
		border-radius: 22rpx;
	}

	.device-management-page.care .status-text {
		font-size: 28rpx;
	}

	.device-management-page.care .action-btn {
		padding: 14rpx 22rpx;
		border-radius: 22rpx;
	}

	.device-management-page.care .action-text {
		font-size: 28rpx;
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.device-item {
			padding: 24rpx 0;
		}
		
		.device-icon {
			width: 50rpx;
			height: 50rpx;
		}
		
		.icon-phone, .icon-desktop {
			width: 35rpx;
			height: 35rpx;
		}
		
		.device-name {
			font-size: 26rpx;
		}
		
		.device-type {
			font-size: 24rpx;
		}
		
		.device-login {
			font-size: 22rpx;
		}
	}
</style>


