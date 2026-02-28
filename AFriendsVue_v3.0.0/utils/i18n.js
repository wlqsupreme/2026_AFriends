/**
 * i18n国际化配置文件
 * 支持简体中文、繁体中文、英文三种语言
 */

import { createI18n } from 'vue-i18n'
import zhCN from '../locales/zh-CN.json'
import zhTW from '../locales/zh-TW.json'
import en from '../locales/en.json'

// 从本地存储获取保存的语言设置，如果没有则使用系统语言
function getDefaultLocale() {
	try {
		const savedLanguage = uni.getStorageSync('appLanguage')
		if (savedLanguage && ['zh-CN', 'zh-TW', 'en'].includes(savedLanguage)) {
			return savedLanguage
		}
	} catch (e) {
		console.error('获取语言设置失败:', e)
	}
	
	// 获取系统语言
	const systemLanguage = uni.getSystemInfoSync().language || 'zh-CN'
	
	// 根据系统语言映射到支持的语言
	if (systemLanguage.startsWith('zh')) {
		// 可以根据需要区分简体繁体，这里默认使用简体
		return 'zh-CN'
	} else if (systemLanguage.startsWith('en')) {
		return 'en'
	}
	
	// 默认返回简体中文
	return 'zh-CN'
}

// 创建i18n实例
const i18n = createI18n({
	locale: getDefaultLocale(), // 当前语言
	fallbackLocale: 'zh-CN', // 回退语言
	messages: {
		'zh-CN': zhCN,
		'zh-TW': zhTW,
		'en': en
	},
	legacy: false, // 使用Composition API模式
	globalInjection: true, // 全局注入$t方法
	silentTranslationWarn: true, // 静默翻译警告
	silentFallbackWarn: true // 静默回退警告
})

/**
 * 切换语言
 * @param {String} locale 语言代码：'zh-CN' | 'zh-TW' | 'en'
 */
export function setLocale(locale) {
	if (!['zh-CN', 'zh-TW', 'en'].includes(locale)) {
		console.error('不支持的语言代码:', locale)
		return
	}
	
	try {
		// 保存到本地存储
		uni.setStorageSync('appLanguage', locale)
		// 更新i18n语言
		i18n.global.locale.value = locale
		console.log('语言切换成功:', locale)
	} catch (e) {
		console.error('切换语言失败:', e)
	}
}

/**
 * 获取当前语言
 * @returns {String} 当前语言代码
 */
export function getLocale() {
	return i18n.global.locale.value
}

export default i18n

