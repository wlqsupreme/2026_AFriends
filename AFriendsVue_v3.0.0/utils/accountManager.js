/**
 * 账号管理工具
 * 用于管理多个账号的存储和切换
 */

export default {
  /**
   * 保存账号信息
   * @param {Object} accountInfo - 账号信息
   */
  saveAccount(accountInfo) {
    try {
      let accounts = uni.getStorageSync('savedAccounts') || [];
      
      // 检查账号是否已存在
      const existingIndex = accounts.findIndex(acc => acc.username === accountInfo.username);
      if (existingIndex >= 0) {
        // 更新现有账号信息
        accounts[existingIndex] = { ...accounts[existingIndex], ...accountInfo, lastLogin: new Date().toISOString() };
      } else {
        // 添加新账号
        accounts.push({
          ...accountInfo,
          id: accountInfo.id || Date.now(), // 如果没有ID则生成一个
          lastLogin: new Date().toISOString()
        });
      }
      
      uni.setStorageSync('savedAccounts', accounts);
      return true;
    } catch (e) {
      console.error('保存账号信息失败:', e);
      return false;
    }
  },
  
  /**
   * 获取所有保存的账号
   * @returns {Array} 账号列表
   */
  getSavedAccounts() {
    try {
      return uni.getStorageSync('savedAccounts') || [];
    } catch (e) {
      console.error('获取账号信息失败:', e);
      return [];
    }
  },
  
  /**
   * 删除账号
   * @param {String} username - 用户名
   */
  removeAccount(username) {
    try {
      let accounts = uni.getStorageSync('savedAccounts') || [];
      accounts = accounts.filter(acc => acc.username !== username);
      uni.setStorageSync('savedAccounts', accounts);
      return true;
    } catch (e) {
      console.error('删除账号信息失败:', e);
      return false;
    }
  },
  
  /**
   * 清除所有账号信息
   */
  clearAllAccounts() {
    try {
      uni.removeStorageSync('savedAccounts');
      return true;
    } catch (e) {
      console.error('清除账号信息失败:', e);
      return false;
    }
  },
  
  /**
   * 获取当前账号信息
   */
  getCurrentAccount() {
    try {
      const username = uni.getStorageSync('username');
      const userId = uni.getStorageSync('userId');
      
      if (username && userId) {
        return {
          username,
          userId
        };
      }
      return null;
    } catch (e) {
      console.error('获取当前账号信息失败:', e);
      return null;
    }
  }
}