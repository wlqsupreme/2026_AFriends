/**
 * 通知轮询工具类
 * 用于定时轮询服务器获取未读通知数量
 */
class NotificationPolling {
    constructor() {
        this.pollingInterval = null;
        this.pollingIntervalTime = 30000; // 30秒
        this.isPolling = false;
        this.lastCheckTime = null;
        this.errorCount = 0;
        this.maxErrorCount = 3; // 最大连续错误次数
        this.baseUrl = 'http://localhost:8888'; // 后端API地址，建议从配置文件读取
    }
    
    /**
     * 开始轮询
     * @param {Number} userId 用户ID
     * @param {Function} callback 回调函数，参数为未读消息数量
     * @param {Number} interval 轮询间隔（毫秒），默认30秒
     */
    start(userId, callback, interval) {
        if (this.isPolling) {
            console.log('轮询已在运行中');
            return;
        }
        
        if (!userId) {
            console.error('用户ID不能为空');
            return;
        }
        
        this.isPolling = true;
        this.errorCount = 0;
        this.pollingIntervalTime = interval || this.pollingIntervalTime;
        
        // 立即执行一次
        this.fetchUnreadCount(userId, callback);
        
        // 设置定时轮询
        this.pollingInterval = setInterval(() => {
            this.fetchUnreadCount(userId, callback);
        }, this.pollingIntervalTime);
        
        console.log(`通知轮询已启动，间隔: ${this.pollingIntervalTime}ms`);
    }
    
    /**
     * 停止轮询
     */
    stop() {
        if (this.pollingInterval) {
            clearInterval(this.pollingInterval);
            this.pollingInterval = null;
            this.isPolling = false;
            this.errorCount = 0;
            console.log('通知轮询已停止');
        }
    }
    
    /**
     * 获取未读消息数量
     * @param {Number} userId 用户ID
     * @param {Function} callback 回调函数
     */
    async fetchUnreadCount(userId, callback) {
        try {
            const res = await uni.request({
                url: `${this.baseUrl}/api/notifications/unread-count?userId=${userId}`,
                method: 'GET',
                timeout: 10000 // 10秒超时
            });
            
            if (res.statusCode === 200 && res.data) {
                if (res.data.success) {
                    const unreadCount = res.data.unreadCount || 0;
                    this.errorCount = 0; // 重置错误计数
                    this.lastCheckTime = Date.now();
                    
                    // 执行回调
                    if (callback && typeof callback === 'function') {
                        callback(unreadCount);
                    }
                    
                    return unreadCount;
                } else {
                    throw new Error(res.data.message || '获取未读消息数量失败');
                }
            } else {
                throw new Error('请求失败，状态码: ' + res.statusCode);
            }
        } catch (error) {
            console.error('获取未读消息数量失败:', error);
            this.errorCount++;
            
            // 连续失败超过最大次数，停止轮询
            if (this.errorCount >= this.maxErrorCount) {
                console.error(`连续失败${this.maxErrorCount}次，停止轮询`);
                this.stop();
                
                // 提示用户
                uni.showToast({
                    title: '网络异常，通知功能已暂停',
                    icon: 'none',
                    duration: 2000
                });
            }
            
            // 即使失败也执行回调，传入0
            if (callback && typeof callback === 'function') {
                callback(0);
            }
            
            return 0;
        }
    }
    
    /**
     * 设置轮询间隔
     * @param {Number} interval 轮询间隔（毫秒）
     */
    setInterval(interval) {
        this.pollingIntervalTime = interval;
        
        // 如果正在轮询，需要重启
        if (this.isPolling) {
            const userId = this.currentUserId;
            const callback = this.currentCallback;
            this.stop();
            this.start(userId, callback, interval);
        }
    }
    
    /**
     * 检查是否正在轮询
     * @returns {Boolean}
     */
    isRunning() {
        return this.isPolling;
    }
    
    /**
     * 获取上次检查时间
     * @returns {Number|null}
     */
    getLastCheckTime() {
        return this.lastCheckTime;
    }
}

// 导出单例
export default new NotificationPolling();

