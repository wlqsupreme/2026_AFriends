/**
 * i18n全局混入
 * 为所有组件提供i18n翻译功能
 */
import { getLocale } from './i18n'

export default {
	computed: {
		/**
		 * 当前语言
		 */
		currentLocale() {
			return getLocale()
		}
	},
	methods: {
		/**
		 * 翻译方法（如果全局注入失败时的备用方案）
		 * @param {String} key 翻译键
		 * @param {Object} params 参数对象（可选）
		 * @returns {String} 翻译后的文本
		 */
		$t(key, params) {
			// 如果全局注入的 $t 方法可用，直接使用
			if (this.$i18n && this.$i18n.t) {
				return this.$i18n.t(key, params)
			}
			// 否则返回键名（开发时便于发现缺失的翻译）
			console.warn('翻译键未找到:', key)
			return key
		}
	}
}

