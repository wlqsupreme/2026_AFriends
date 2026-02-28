import config from '@/config.js'

// 读取本地 token（登录后存储）
const getToken = () => {
  return uni.getStorageSync('token') || '';
}

export default {
  // 使用config.js中的baseUrl
  get baseURL() {
    return config.baseUrl;
  },

  // 公共请求头
  commonHeader() {
    const token = getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': token ? 'Bearer ' + token : ''
    };
  },

  // GET请求
  get(url, params = {}, timeout = 10000) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.baseURL + url,
        method: 'GET',
        data: params,
        timeout,
        header: this.commonHeader(),
        success: res => {
          if (res.statusCode === 200) {
            resolve(res);
          } else {
            uni.showToast({ 
              title: `请求失败: ${res.statusCode}`, 
              icon: 'none' 
            });
            reject(res);
          }
        },
        fail: err => {
          uni.showToast({ 
            title: '网络请求失败', 
            icon: 'none' 
          });
          reject(err);
        }
      });
    });
  },

  // POST请求
  post(url, data = {}, timeout = 10000) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.baseURL + url,
        method: 'POST',
        data,
        timeout,
        header: this.commonHeader(),
        success: res => {
          if (res.statusCode === 200) {
            resolve(res);
          } else {
            uni.showToast({ 
              title: `请求失败: ${res.statusCode}`, 
              icon: 'none' 
            });
            reject(res);
          }
        },
        fail: err => {
          uni.showToast({ 
            title: '网络请求失败', 
            icon: 'none' 
          });
          reject(err);
        }
      });
    });
  },

  // PUT请求
  put(url, data = {}, timeout = 10000) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.baseURL + url,
        method: 'PUT',
        data,
        timeout,
        header: this.commonHeader(),
        success: res => {
          if (res.statusCode === 200) {
            resolve(res);
          } else {
            uni.showToast({ 
              title: `请求失败: ${res.statusCode}`, 
              icon: 'none' 
            });
            reject(res);
          }
        },
        fail: err => {
          uni.showToast({ 
            title: '网络请求失败', 
            icon: 'none' 
          });
          reject(err);
        }
      });
    });
  },

  // DELETE请求
  delete(url, params = {}, timeout = 10000) {
    return new Promise((resolve, reject) => {
      uni.request({
        url: this.baseURL + url,
        method: 'DELETE',
        data: params,
        timeout,
        header: this.commonHeader(),
        success: res => {
          if (res.statusCode === 200) {
            resolve(res);
          } else {
            uni.showToast({ 
              title: `请求失败: ${res.statusCode}`, 
              icon: 'none' 
            });
            reject(res);
          }
        },
        fail: err => {
          uni.showToast({ 
            title: '网络请求失败', 
            icon: 'none' 
          });
          reject(err);
        }
      });
    });
  }
};
