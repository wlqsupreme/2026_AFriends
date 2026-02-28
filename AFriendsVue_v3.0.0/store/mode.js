import { defineStore } from 'pinia'

export const useModeStore = defineStore('mode', {
	state: () => ({
		mode: 'normal',
		initialized: false
	}),
	getters: {
		isConcise: (state) => state.mode === 'concise',
		isCare: (state) => state.mode === 'care'
	},
	actions: {
		init() {
			if (this.initialized) return
			try {
				const cached = uni.getStorageSync('appMode')
				if (cached === 'normal' || cached === 'concise' || cached === 'minor' || cached === 'care') {
					this.mode = cached
				}
			} catch (e) {
				console.error('读取模式缓存失败', e)
				this.mode = 'normal'
			}
			this.initialized = true
		},
		setMode(mode) {
			const next = (mode === 'normal' || mode === 'concise' || mode === 'minor' || mode === 'care') ? mode : 'normal'
			this.mode = next
			this.initialized = true
			try {
				uni.setStorageSync('appMode', this.mode)
			} catch (e) {
				console.error('写入模式缓存失败', e)
			}
		}
	}
})
