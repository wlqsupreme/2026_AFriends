import { defineStore } from 'pinia'

/**
 * 主题/暗夜模式全局 Store
 * 负责：状态持久化、CSS 变量/类应用、原生导航与 Tab 样式同步
 */
export const useThemeStore = defineStore('theme', {
	state: () => ({
		isDark: false,
		initialized: false
	}),
	getters: {
		themeClass: (state) => (state.isDark ? 'theme-dark' : 'theme-light')
	},
	actions: {
		init() {
			if (this.initialized) return
			try {
				const cached = uni.getStorageSync('darkMode')
				this.isDark = cached === true || cached === 'true'
			} catch (e) {
				console.error('读取暗夜模式缓存失败', e)
				this.isDark = false
			}
			this.initialized = true
			this.applyTheme()
		},
		setDark(isDark) {
			this.isDark = !!isDark
			try {
				uni.setStorageSync('darkMode', this.isDark)
			} catch (e) {
				console.error('写入暗夜模式缓存失败', e)
			}
			this.applyTheme()
		},
		toggle() {
			this.setDark(!this.isDark)
		},
		applyTheme() {
			this.initialized = true
			this.applyRootClass()
			this.syncNativeChrome()
			this.emitThemeEvent()
		},
		applyRootClass() {
			if (typeof document === 'undefined') return
			const root = document.documentElement || document.body
			root.classList.remove(this.isDark ? 'theme-light' : 'theme-dark')
			root.classList.add(this.isDark ? 'theme-dark' : 'theme-light')
		},
		syncNativeChrome() {
			const navBg = this.isDark ? '#0f1115' : '#ffffff'
			const navFront = this.isDark ? '#ffffff' : '#000000'
			const tabBg = this.isDark ? '#11131a' : '#ffffff'
			try {
				if (uni.setNavigationBarColor) {
					uni.setNavigationBarColor({
						frontColor: navFront,
						backgroundColor: navBg,
						animation: { duration: 150, timingFunc: 'easeIn' }
					})
				}
				if (uni.setTabBarStyle) {
					uni.setTabBarStyle({
						color: this.isDark ? '#b8c2d6' : '#666666',
						selectedColor: this.isDark ? '#7ba4ff' : '#ff69b4',
						borderStyle: this.isDark ? 'white' : 'black',
						backgroundColor: tabBg
					})
				}
				if (uni.setBackgroundColor) {
					const pageBg = this.isDark ? '#0b0d12' : '#f8f8f8'
					uni.setBackgroundColor({
						backgroundColor: pageBg,
						backgroundColorTop: pageBg,
						backgroundColorBottom: pageBg
					})
				}
			} catch (e) {
				console.error('同步原生导航/Tab 样式失败', e)
			}
		},
		emitThemeEvent() {
			try {
				if (uni && uni.$emit) {
					uni.$emit('theme-changed', this.isDark)
				}
			} catch (e) {
				// 忽略事件派发失败
			}
		}
	}
})

