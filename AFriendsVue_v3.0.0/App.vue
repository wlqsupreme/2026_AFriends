<script>
	// 导入全局混入（提供i18n支持）
	import i18nMixin from './utils/i18n-mixin.js'
	// 导入通知轮询工具
	import notificationPolling from './utils/notification-polling.js'
	import { useThemeStore } from './store/theme.js'

	export default {
		mixins: [i18nMixin],
		globalData: {
			darkMode: false,
			unreadNotificationCount: 0 // 未读通知数量
		},
		onLaunch: function() {
			console.log('App Launch');
			// 初始化全局主题（暗夜模式）
			const themeStore = useThemeStore()
			themeStore.init()
			
			// 应用启动时，如果用户已登录，开始轮询通知
			//this.startNotificationPolling();
		},
		onShow: function() {
			console.log('App Show');
			// 确保切后台后返回时主题仍被应用
			const themeStore = useThemeStore()
			if (themeStore.initialized) {
				themeStore.applyTheme()
			}
			
			// 应用回到前台时，重新开始轮询
			//this.startNotificationPolling();
		},
		onHide: function() {
			console.log('App Hide');
			
			// 应用进入后台时，停止轮询以节省资源
			//notificationPolling.stop();
		},
		methods: {
			/**
			 * 开始通知轮询
			 */
			startNotificationPolling() {
				try {
					const userId = uni.getStorageSync('userId');
					if (userId) {
						// 开始轮询，回调函数更新全局未读数量
						notificationPolling.start(userId, (unreadCount) => {
							this.updateUnreadBadge(unreadCount);
						});
					} else {
						console.log('用户未登录，不启动通知轮询');
					}
				} catch (error) {
					console.error('启动通知轮询失败:', error);
				}
			},
			
			/**
			 * 更新未读消息徽章
			 * @param {Number} unreadCount 未读消息数量
			 */
			updateUnreadBadge(unreadCount) {
				this.globalData.unreadNotificationCount = unreadCount;
				
				// 通过事件总线通知各个页面更新徽章
				// 各个页面可以监听这个全局数据变化来更新自己的徽章显示
			}
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>