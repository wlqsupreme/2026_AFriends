/**
 * 暗夜模式工具函数
 * 提供全局暗夜模式的统一管理
 */

import { useThemeStore } from '../store/theme.js'

/**
 * 获取暗夜模式状态（兼容旧调用）
 * @returns {Boolean} 是否为暗夜模式
 */
export function getDarkMode() {
	const themeStore = useThemeStore()
	if (!themeStore.initialized) {
		themeStore.init()
	}
	return themeStore.isDark
}

/**
 * 设置暗夜模式状态（兼容旧调用）
 * @param {Boolean} isDark - 是否为暗夜模式
 */
export function setDarkMode(isDark) {
	const themeStore = useThemeStore()
	themeStore.setDark(isDark)
}

/**
 * 应用暗夜模式到当前页面（兼容旧调用）
 */
export function applyDarkModeToPage() {
	const themeStore = useThemeStore()
	themeStore.applyTheme()
}

/**
 * 初始化页面暗夜模式，在页面 onLoad 调用
 * @returns {Boolean} 当前暗夜模式状态
 */
export function initPageDarkMode() {
	const themeStore = useThemeStore()
	themeStore.init()
	return themeStore.isDark
}

