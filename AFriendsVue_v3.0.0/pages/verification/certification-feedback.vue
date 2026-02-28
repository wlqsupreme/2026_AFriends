<!--
 * @description 认证反馈页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示认证提交成功的反馈信息
 * - 适用于职业认证和学校认证完成后的反馈
 * - 提供返回上一页的功能
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 现代化的卡片设计
 * - 友好的用户反馈体验
 * - 渐变背景和阴影效果
-->

<template>
	<view class="certification-feedback-page">
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
			<text class="title">{{ $t("verification.confirmCertification") }}</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 成功图标 - 只在有真实记录时显示 -->
			<view class="success-icon-container" v-if="certRecords.length > 0">
				<view class="success-icon">
					<view class="checkmark"></view>
				</view>
			</view>
			
			<!-- 成功消息 -->
			<view class="success-message">
				<text class="message-text" v-if="certRecords.length > 0">
					以下是您的认证记录
				</text>
				<text class="message-text" v-else-if="!loading">
					暂无认证记录
				</text>
			</view>
			
			
			<!-- 认证记录列表 -->
			<view class="cert-records-section" v-if="certRecords.length > 0">
				<view class="records-header">
					<view class="records-icon">
						<view class="list-icon"></view>
					</view>
					<text class="records-title">{{ $t("verification.myCertRecords") }}</text>
				</view>
				<view class="records-list">
					<view 
						v-for="record in certRecords" 
						:key="record.recordId" 
						class="record-item"
					>
						<view class="record-header">
							<text class="record-type">{{ getCertTypeText(record.certType) }}</text>
							<view 
								class="status-badge" 
								:style="{ backgroundColor: getStatusColor(record.status) }"
							>
								<text class="status-text">{{ getStatusText(record.status) }}</text>
							</view>
						</view>
						<view class="record-info">
							<view class="cert-info-list" v-if="getFormattedCertInfo(record).length > 0">
								<view 
									v-for="item in getFormattedCertInfo(record)" 
									:key="item.key"
									class="info-item"
								>
									<text class="info-label">{{ item.label }}：</text>
									<text class="info-value">{{ item.value }}</text>
								</view>
							</view>
							<text class="record-desc" v-if="getFormattedCertInfo(record).length === 0">暂无详细信息</text>
						</view>
						<view class="record-time">
							<text class="time-text">提交时间：{{ formatDateTime(record.createdAt) }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 无认证记录提示 - 只在加载完成且没有记录时显示 -->
			<view class="no-records-section" v-if="!loading && certRecords.length === 0">
				<view class="no-records-icon">
					<view class="empty-icon"></view>
				</view>
				<text class="no-records-text">{{ $t("verification.noCertRecords") }}</text>
			</view>
			
			<!-- 加载状态 -->
			<view class="loading-section" v-if="loading">
				<view class="loading-spinner"></view>
				<text class="loading-text">{{ $t("verification.loadingCertRecords") }}</text>
			</view>
			
			<!-- 温馨提示 -->
			<view class="tips-section">
				<view class="tips-header">
					<view class="tips-icon">
						<view class="lightbulb"></view>
					</view>
					<text class="tips-title">{{ $t("verification.warmTips") }}</text>
				</view>
				<view class="tips-content">
					<text class="tip-item">• {{ $t("verification.keepPhoneAvailable") }}</text>
					<text class="tip-item">• {{ $t("verification.contactCustomerService") }}</text>
					<text class="tip-item">• {{ $t("verification.getAccountBadge") }}</text>
				</view>
			</view>
		</view>
		
		<!-- 返回按钮 -->
		<view class="return-button-section">
			<view class="return-button" @click="goBack">
				<text class="return-text">{{ $t("common.back") }}</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { useThemeStore } from '@/store/theme.js';

	export default {
		name: 'CertificationFeedbackPage',
		data() {
			return {
				certificationType: '职业认证', // 或 '学校认证'
				submitTime: this.formatDateTime(new Date()),
				expectedTime: this.formatExpectedTime(new Date()),
				certRecords: [], // 认证记录列表
				loading: false, // 加载状态
				userId: null // 用户ID
			}
		},
		methods: {
			// 加载认证记录数据
			async loadCertRecords() {
				try {
					this.loading = true;
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						this.loading = false;
						return;
					}
					
					this.userId = userId;
					
					// 获取认证记录
					const response = await this.getUserCertRecords(userId);
					console.log('API响应:', response);
					console.log('响应状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					// 检查响应状态
					if (response.statusCode === 200) {
						// 后端直接返回列表，所以 response.data 就是列表
						if (Array.isArray(response.data)) {
							this.certRecords = response.data;
							console.log('成功加载认证记录，数量:', this.certRecords.length);
						} else if (response.data && Array.isArray(response.data.data)) {
							// 如果后端包装了一层，使用 response.data.data
							this.certRecords = response.data.data;
							console.log('成功加载认证记录（嵌套结构），数量:', this.certRecords.length);
						} else {
							this.certRecords = [];
							console.warn('响应数据格式不正确:', response.data);
						}
					} else {
						console.error('API返回错误状态码:', response.statusCode);
						this.certRecords = [];
						uni.showToast({
							title: '获取认证记录失败',
							icon: 'none'
						});
					}
					
				} catch (error) {
					console.error('加载认证记录失败:', error);
					this.certRecords = [];
					uni.showToast({
						title: '加载认证记录失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 获取用户认证记录
			getUserCertRecords(userId) {
				return new Promise((resolve, reject) => {
					// 确保 userId 是数字类型
					const userIdNum = typeof userId === 'string' ? parseInt(userId) : userId;
					console.log('请求用户认证记录，userId:', userIdNum);
					
					uni.request({
						url: '${this.$baseUrl}/api/u-entities/user-cert-record/user/' + userIdNum,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							console.log('请求成功，状态码:', res.statusCode);
							console.log('响应数据:', res.data);
							resolve(res);
						},
						fail: (err) => {
							console.error('请求失败:', err);
							reject(err);
						}
					});
				});
			},
			
			// 获取认证状态文本
			getStatusText(status) {
				switch (status) {
					case 0:
						return '待审核';
					case 1:
						return '已通过';
					case 2:
						return '未通过';
					default:
						return '未知状态';
				}
			},
			
			// 获取认证类型文本
			getCertTypeText(certType) {
				switch (certType) {
					case 'REALNAME':
						return '实名认证';
					case 'STUDENT':
						return '学校认证';
					case 'JOB':
						return '职业认证';
					default:
						return '未知类型';
				}
			},
			
			// 获取状态颜色
			getStatusColor(status) {
				switch (status) {
					case 0:
						return '#FF9500'; // 待审核 - 橙色
					case 1:
						return '#34C759'; // 已通过 - 绿色
					case 2:
						return '#FF3B30'; // 未通过 - 红色
					default:
						return '#8E8E93'; // 未知 - 灰色
				}
			},
			
			// 检查值是否有效（非空字符串、非null、非undefined）
			isValidValue(value) {
				return value !== null && value !== undefined && value !== '' && String(value).trim() !== '';
			},
			
			// 格式化认证信息（返回对象）
			formatCertInfo(certInfo, certType) {
				if (!certInfo) {
					console.log('formatCertInfo: certInfo 为空');
					return {};
				}
				
				try {
					// 尝试解析 JSON 字符串
					let infoObj;
					if (typeof certInfo === 'string') {
						infoObj = JSON.parse(certInfo);
					} else {
						infoObj = certInfo;
					}
					
					console.log('formatCertInfo: 解析后的 infoObj:', infoObj);
					console.log('formatCertInfo: certType:', certType, '类型:', typeof certType);
					
					// 根据认证类型过滤和格式化字段
					const formatted = {};
					
					// 统一处理 certType（可能是字符串或枚举对象）
					let certTypeStr = '';
					if (typeof certType === 'string') {
						certTypeStr = certType;
					} else if (certType && typeof certType === 'object') {
						// 处理枚举对象
						certTypeStr = certType.name || certType.toString() || String(certType);
					} else if (certType) {
						certTypeStr = String(certType);
					}
					
					console.log('formatCertInfo: 处理后的 certTypeStr:', certTypeStr);
					
					// 系统字段，需要排除
					const excludeFields = ['submitTime', 'createdAt', 'updatedAt'];
					
					// 转换为大写进行比较（更宽松的匹配）
					const certTypeUpper = certTypeStr.toUpperCase();
					
					if (certTypeUpper.includes('STUDENT')) {
						// 学校认证字段
						console.log('formatCertInfo: 识别为学校认证');
						const studentFields = ['school', 'province', 'name', 'degree', 'studentId'];
						studentFields.forEach(field => {
							if (!excludeFields.includes(field) && this.isValidValue(infoObj[field])) {
								formatted[field] = infoObj[field];
								console.log(`formatCertInfo: 添加字段 ${field} = ${infoObj[field]}`);
							}
						});
					} else if (certTypeUpper.includes('JOB')) {
						// 职业认证字段
						console.log('formatCertInfo: 识别为职业认证');
						const jobFields = ['jobName', 'jobType', 'company', 'position', 'name'];
						jobFields.forEach(field => {
							if (!excludeFields.includes(field) && this.isValidValue(infoObj[field])) {
								formatted[field] = infoObj[field];
								console.log(`formatCertInfo: 添加字段 ${field} = ${infoObj[field]}`);
							}
						});
					} else if (certTypeUpper.includes('REALNAME') || certTypeUpper.includes('REAL_NAME')) {
						// 实名认证字段
						console.log('formatCertInfo: 识别为实名认证');
						if (this.isValidValue(infoObj.name)) {
							formatted.name = infoObj.name;
							console.log(`formatCertInfo: 添加字段 name = ${infoObj.name}`);
						}
						// 支持 documentType 和 idType 两种字段名
						if (this.isValidValue(infoObj.documentType)) {
							formatted.documentType = infoObj.documentType;
							console.log(`formatCertInfo: 添加字段 documentType = ${infoObj.documentType}`);
						} else if (this.isValidValue(infoObj.idType)) {
							formatted.idType = infoObj.idType;
							console.log(`formatCertInfo: 添加字段 idType = ${infoObj.idType}`);
						}
						if (this.isValidValue(infoObj.idNumber)) {
							formatted.idNumber = this.maskIdNumber(infoObj.idNumber);
							console.log(`formatCertInfo: 添加字段 idNumber = ${formatted.idNumber}`);
						}
					} else {
						// 未知类型，显示所有有效字段（排除系统字段和空值）
						console.log('formatCertInfo: 未知类型，显示所有有效字段');
						for (const key in infoObj) {
							if (!excludeFields.includes(key) && this.isValidValue(infoObj[key])) {
								formatted[key] = infoObj[key];
								console.log(`formatCertInfo: 添加字段 ${key} = ${infoObj[key]}`);
							}
						}
					}
					
					console.log('formatCertInfo: 最终返回的 formatted:', formatted);
					return formatted;
				} catch (error) {
					console.error('解析认证信息失败:', error, certInfo);
					// 如果解析失败，返回空对象
					return {};
				}
			},
			
			// 获取格式化的认证信息列表（用于模板渲染）
			getFormattedCertInfo(record) {
				const formatted = this.formatCertInfo(record.certInfo, record.certType);
				const result = [];
				
				for (const key in formatted) {
					if (this.isValidValue(formatted[key])) {
						result.push({
							key: key,
							label: this.getInfoLabel(key, record.certType),
							value: formatted[key]
						});
					}
				}
				
				console.log('getFormattedCertInfo: 返回列表，长度:', result.length, result);
				return result;
			},
			
			// 获取信息标签文本
			getInfoLabel(key, certType) {
				const labelMap = {
					// 学校认证
					school: '学校',
					province: '省份',
					name: '姓名',
					degree: '学历',
					studentId: '学号',
					// 职业认证
					jobName: '职业',
					jobType: '职业类型',
					company: '公司',
					position: '职位',
					// 实名认证
					documentType: '证件类型',
					idType: '证件类型', // 兼容旧字段名
					idNumber: '证件号码'
				};
				
				return labelMap[key] || key;
			},
			
			// 检查是否有认证信息
			hasCertInfo(certInfo) {
				if (!certInfo) return false;
				try {
					const infoObj = typeof certInfo === 'string' ? JSON.parse(certInfo) : certInfo;
					return Object.keys(infoObj).length > 0;
				} catch {
					return false;
				}
			},
			
			// 脱敏身份证号码（只显示前后几位）
			maskIdNumber(idNumber) {
				if (!idNumber || typeof idNumber !== 'string') return idNumber;
				if (idNumber.length <= 8) return idNumber;
				const start = idNumber.substring(0, 4);
				const end = idNumber.substring(idNumber.length - 4);
				return start + '****' + end;
			},
			
			goBack() {
				uni.redirectTo({
					url: '/pages/verification/official-certification'
				});
			},
			formatDateTime(date) {
				// 处理多种日期格式：Date对象、时间戳、字符串
				let dateObj;
				if (date instanceof Date) {
					dateObj = date;
				} else if (typeof date === 'string') {
					// 处理字符串格式的时间戳或ISO格式
					dateObj = new Date(date);
				} else if (typeof date === 'number') {
					// 处理数字时间戳
					dateObj = new Date(date);
				} else {
					console.warn('无法解析日期:', date);
					return '未知时间';
				}
				
				// 检查日期是否有效
				if (isNaN(dateObj.getTime())) {
					console.warn('无效的日期:', date);
					return '无效时间';
				}
				
				const year = dateObj.getFullYear();
				const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
				const day = dateObj.getDate().toString().padStart(2, '0');
				const hours = dateObj.getHours().toString().padStart(2, '0');
				const minutes = dateObj.getMinutes().toString().padStart(2, '0');
				return `${year}-${month}-${day} ${hours}:${minutes}`;
			},
			formatExpectedTime(date) {
				// 3个工作日后的日期
				const expectedDate = new Date(date);
				let workDays = 0;
				while (workDays < 3) {
					expectedDate.setDate(expectedDate.getDate() + 1);
					// 跳过周末
					if (expectedDate.getDay() !== 0 && expectedDate.getDay() !== 6) {
						workDays++;
					}
				}
				const year = expectedDate.getFullYear();
				const month = (expectedDate.getMonth() + 1).toString().padStart(2, '0');
				const day = expectedDate.getDate().toString().padStart(2, '0');
				return `${year}-${month}-${day}`;
			}
		},
		onLoad(options) {
			// 根据传入参数设置认证类型
			if (options.type) {
				if (options.type === 'school') {
					this.certificationType = '学校认证';
				} else if (options.type === 'realName') {
					this.certificationType = '实名认证';
				} else {
					this.certificationType = '职业认证';
				}
			}
			
			// 加载认证记录
			this.loadCertRecords();
		},
		mounted() {
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
		}
	}
</script>

<style>
	.certification-feedback-page {
		min-height: 100vh;
		background: var(--certification-bg, linear-gradient(135deg, #F8FAFF 0%, #FFFFFF 100%));
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
		background: transparent;
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
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background: var(--certification-header-bg, rgba(255, 255, 255, 0.95));
		backdrop-filter: blur(20rpx);
		border-bottom: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.8));
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 24rpx;
		background: var(--color-bg-weak, rgba(0, 0, 0, 0.05));
		transition: all 0.2s ease;
	}
	
	.back-button:active {
		background: var(--color-bg-weaker, rgba(0, 0, 0, 0.1));
		transform: scale(0.95);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid var(--color-text, #000000);
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 48rpx 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
    background-color: var(--color-bg);
	}
	
	/* 成功图标 */
	.success-icon-container {
		margin-bottom: 48rpx;
	}
	
	.success-icon {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #34C759, #30D158);
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx rgba(52, 199, 89, 0.3);
		position: relative;
	}
	
	.success-icon::before {
		content: '';
		position: absolute;
		width: 140rpx;
		height: 140rpx;
		border: 2rpx solid rgba(52, 199, 89, 0.2);
		border-radius: 70rpx;
		animation: pulse 2s infinite;
	}
	
	@keyframes pulse {
		0% {
			transform: scale(1);
			opacity: 1;
		}
		100% {
			transform: scale(1.2);
			opacity: 0;
		}
	}
	
	.checkmark {
		width: 48rpx;
		height: 48rpx;
		position: relative;
	}
	
	.checkmark::before {
		content: '';
		position: absolute;
		width: 24rpx;
		height: 48rpx;
		border-right: 6rpx solid #FFFFFF;
		border-bottom: 6rpx solid #FFFFFF;
		transform: rotate(45deg);
		top: -4rpx;
		left: 8rpx;
	}
	
	/* 成功消息 */
	.success-message {
		margin-bottom: 48rpx;
		text-align: center;
		max-width: 600rpx;
	}
	
	.message-text {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		line-height: 1.5;
		text-align: center;
	}
	
	/* 认证信息卡片 */
	.certification-card {
		width: 100%;
		background: var(--color-card, #FFFFFF);
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
		border: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.8));
	}
	
	.card-header {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 24rpx;
		padding-bottom: 16rpx;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.card-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.cert-icon {
		width: 24rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
	}
	
	.cert-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 12rpx;
		height: 12rpx;
		background: #007AFF;
		border-radius: 50%;
		transform: translate(-50%, -50%);
	}
	
	.card-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.card-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.info-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16rpx 0;
		border-bottom: 1rpx solid var(--color-border, #F0F0F0);
	}
	
	.info-label {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
		font-weight: 400;
	}
	
	.info-value {
		font-size: 26rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	/* 温馨提示 */
	.tips-section {
		width: 100%;
		background: var(--certification-tips-bg, rgba(255, 255, 255, 0.8));
		border-radius: 20rpx;
		padding: 24rpx;
		border: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.6));
	}
	
	.tips-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 16rpx;
	}
	
	.tips-icon {
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FF9500, #FFB340);
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.lightbulb {
		width: 16rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
	}
	
	.lightbulb::before {
		content: '';
		position: absolute;
		top: -4rpx;
		left: 50%;
		width: 8rpx;
		height: 8rpx;
		background: #FFFFFF;
		border-radius: 50%;
		transform: translateX(-50%);
	}
	
	.tips-title {
		font-size: 26rpx;
		color: #FF9500;
		font-weight: 600;
	}
	
	.tips-content {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.tip-item {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.4;
	}
	
	/* 返回按钮 */
	.return-button-section {
		padding: 32rpx;
		display: flex;
		justify-content: center;
    background-color: var(--color-bg);
	}
	
	.return-button {
		width: 100%;
		height: 88rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.3);
		transition: all 0.2s ease;
		cursor: pointer;
	}
	
	.return-button:active {
		transform: translateY(2rpx);
		box-shadow: 0 4rpx 16rpx rgba(0, 122, 255, 0.4);
	}
	
	.return-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 认证记录列表 */
	.cert-records-section {
		width: 100%;
		margin-bottom: 32rpx;
	}
	
	.records-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 20rpx;
	}
	
	.records-icon {
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.list-icon {
		width: 16rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 2rpx;
		position: relative;
	}
	
	.list-icon::before {
		content: '';
		position: absolute;
		top: 4rpx;
		left: 0;
		width: 16rpx;
		height: 2rpx;
		background: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.list-icon::after {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 0;
		width: 12rpx;
		height: 2rpx;
		background: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.records-title {
		font-size: 28rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.records-list {
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}
	
	.record-item {
		background: var(--color-card, #FFFFFF);
		border-radius: 16rpx;
		padding: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
		border: 1rpx solid var(--color-border, rgba(240, 240, 240, 0.8));
	}
	
	.record-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12rpx;
	}
	
	.record-type {
		font-size: 26rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.status-badge {
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.status-text {
		font-size: 22rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	.record-info {
		margin-bottom: 12rpx;
	}
	
	.record-desc {
		font-size: 24rpx;
		color: var(--color-text-secondary, #666666);
		line-height: 1.4;
	}
	
	/* 认证信息列表样式 */
	.cert-info-list {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.info-item {
		display: flex;
		align-items: flex-start;
		padding: 8rpx 0;
		line-height: 1.5;
	}
	
	.info-label {
		font-size: 24rpx;
		color: var(--color-text-third, #999999);
		font-weight: 400;
		min-width: 120rpx;
		flex-shrink: 0;
	}
	
	.info-value {
		font-size: 24rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
		flex: 1;
		word-break: break-all;
	}
	
	.record-time {
		border-top: 1rpx solid var(--color-border, #F0F0F0);
		padding-top: 12rpx;
	}
	
	.time-text {
		font-size: 22rpx;
		color: var(--color-text-third, #999999);
	}
	
	/* 无认证记录提示 */
	.no-records-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 32rpx;
		margin-bottom: 32rpx;
	}
	
	.no-records-icon {
		width: 80rpx;
		height: 80rpx;
		background: #F0F0F0;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20rpx;
	}
	
	.empty-icon {
		width: 40rpx;
		height: 40rpx;
		background: #CCCCCC;
		border-radius: 20rpx;
		position: relative;
	}
	
	.empty-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 20rpx;
		height: 2rpx;
		background: #FFFFFF;
		transform: translate(-50%, -50%);
	}
	
	.no-records-text {
		font-size: 26rpx;
		color: var(--color-text-third, #999999);
	}
	
	/* 加载状态 */
	.loading-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 32rpx;
		margin-bottom: 32rpx;
	}
	
	.loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 4rpx solid var(--color-border-light, #f3f3f3);
		border-top: 4rpx solid #007AFF;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 20rpx;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 26rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: var(--color-text, #000000);
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
	
	/* 暗夜模式特殊样式 */
	@media (prefers-color-scheme: dark) {
		.certification-feedback-page {
			background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
		}
		
		.certification-card,
		.record-item {
			box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.3);
		}
		
		.header {
			background: var(--certification-header-bg-dark, rgba(30, 30, 30, 0.95));
			border-bottom: 1rpx solid var(--color-border-dark, #444444);
		}
		
		.tips-section {
			background: var(--certification-tips-bg-dark, rgba(30, 30, 30, 0.8));
			border: 1rpx solid var(--color-border-dark, #444444);
		}
		
		.loading-spinner {
			border: 4rpx solid var(--color-border-dark, #444444);
			border-top: 4rpx solid #007AFF;
		}
	}
</style>


