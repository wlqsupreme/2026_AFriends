<!-- 语言设置页面 -->
<template>
	<view class="language-page" :class="{ care: isCare }">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">语言</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 语言选项列表 -->
			<view class="language-list">
				<view class="language-item" 
					v-for="(language, index) in languages" 
					:key="index"
					:class="{ active: currentLanguage === language.code }"
					@click="selectLanguage(language.code)">
					<view class="language-info">
						<text class="language-name">{{ $t(language.nameKey) }}</text>
						<text class="language-native">{{ language.nativeName }}</text>
					</view>
					<view class="language-check" v-if="currentLanguage === language.code">
						<text class="check-icon">✓</text>
					</view>
				</view>
			</view>
			
		<!-- 说明文字 -->
		<view class="language-note">
			<text class="note-text">{{ $t('language.note') }}</text>
		</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	import { setLocale, getLocale } from '../../utils/i18n'
	import { useThemeStore } from '@/store/theme.js';
	import { useModeStore } from '@/store/mode.js';
	
	export default {
		name: 'LanguageSettings',
		data() {
			return {
				currentLanguage: 'zh-CN',
				modeStore: null,
				languages: [
					{
						code: 'zh-CN',
						nameKey: 'language.simplifiedChinese',
						nativeName: '简体中文'
					},
					{
						code: 'zh-TW',
						nameKey: 'language.traditionalChinese',
						nativeName: '繁體中文'
					},
					{
						code: 'en',
						nameKey: 'language.english',
						nativeName: 'English'
					}
				]
			}
		},
		onLoad() {
			// 加载当前语言设置
			this.loadCurrentLanguage()
					
			// 初始化主题
			const themeStore = useThemeStore();
			themeStore.init();
			themeStore.applyTheme();
			const modeStore = useModeStore();
			modeStore.init();
			this.modeStore = modeStore;
		},
		computed: {
			isCare() {
				return !!(this.modeStore && this.modeStore.isCare);
			}
		},
		methods: {
			goBack() {
				uni.navigateBack()
			},
			
			loadCurrentLanguage() {
				try {
					// 从i18n获取当前语言
					this.currentLanguage = getLocale()
				} catch (e) {
					console.error('加载语言设置失败:', e)
					this.currentLanguage = 'zh-CN'
				}
			},
			
			selectLanguage(languageCode) {
				if (this.currentLanguage === languageCode) {
					return
				}
				
				// 使用i18n翻译提示信息
				uni.showModal({
					title: this.$t('language.confirmChange'),
					content: this.$t('language.changeConfirmMessage'),
					success: (res) => {
						if (res.confirm) {
							// 切换语言
							setLocale(languageCode)
							this.currentLanguage = languageCode
							
							uni.showToast({
								title: this.$t('language.saveSuccess'),
								icon: 'success'
							})
							
							// 延迟重启应用提示
							setTimeout(() => {
								this.restartApp()
							}, 1500)
						}
					}
				})
			},
			
			restartApp() {
				// 提示用户手动重启应用
				uni.showModal({
					title: this.$t('language.changeComplete'),
					content: this.$t('language.restartMessage'),
					showCancel: false,
					success: () => {
						uni.navigateBack()
					}
				})
			}
		}
	}
</script>

<style>
	.language-page {
		width: 100%;
		min-height: 100vh;
		background-color: var(--color-bg, #f8f8f8);
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: var(--color-card, #ffffff);
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		padding: 0 16rpx;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: var(--color-text, #333333);
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 32rpx 0;
	}
	
	/* 语言列表 */
	.language-list {
		background-color: var(--color-card, #ffffff);
		margin-bottom: 32rpx;
	}
	
	.language-item {
		padding: 32rpx;
		border-bottom: 1rpx solid var(--color-border, #f0f0f0);
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.language-item:last-child {
		border-bottom: none;
	}
	
	.language-item:active {
		background-color: var(--color-bg-weak, rgba(0, 0, 0, 0.05));
	}
	
	.language-item.active {
		background-color: rgba(0, 122, 255, 0.05);
	}
	
	.language-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.language-name {
		font-size: 32rpx;
		color: var(--color-text, #333333);
		font-weight: 500;
	}
	
	.language-native {
		font-size: 28rpx;
		color: var(--color-text-secondary, #666666);
	}
	
	.language-check {
		width: 48rpx;
		height: 48rpx;
		background-color: #007aff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.check-icon {
		font-size: 24rpx;
		color: #ffffff;
		font-weight: 600;
	}
	
	/* 说明文字 */
	.language-note {
		padding: 32rpx;
		background-color: var(--color-card, #ffffff);
	}
	
	.note-text {
		font-size: 28rpx;
		color: var(--color-text-secondary, #999999);
		line-height: 1.5;
		text-align: center;
	}
	
	/* 底部手势条 */
	.home-indicator {
		height: 68rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.home-indicator::after {
		content: '';
		width: 200rpx;
		height: 8rpx;
		background-color: var(--color-text, #333333);
		border-radius: 4rpx;
	}

	.language-page.care .content-area {
		padding: 40rpx 0;
		box-sizing: border-box;
	}

	.language-page.care .language-item {
		padding: 40rpx;
		min-height: 128rpx;
		box-sizing: border-box;
	}

	.language-page.care .language-name {
		font-size: 38rpx;
		line-height: 1.5;
	}

	.language-page.care .language-native {
		font-size: 34rpx;
		line-height: 1.5;
	}

	.language-page.care .language-check {
		width: 64rpx;
		height: 64rpx;
	}

	.language-page.care .check-icon {
		font-size: 30rpx;
	}

	.language-page.care .language-note {
		padding: 40rpx;
		box-sizing: border-box;
	}

	.language-page.care .note-text {
		font-size: 32rpx;
		line-height: 1.6;
	}
</style>
