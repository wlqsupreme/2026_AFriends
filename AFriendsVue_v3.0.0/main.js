import App from './App'
import { createPinia, PiniaVuePlugin } from 'pinia'
import config from './config.js'
import request from './utils/request.js'
import './uni.scss'
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
Vue.use(PiniaVuePlugin)
const pinia = createPinia()

// 挂载全局配置和请求工具
Vue.prototype.$config = config
Vue.prototype.$baseUrl = config.baseUrl
Vue.prototype.$request = request

App.mpType = 'app'
const app = new Vue({
  ...App,
  pinia
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import i18n from './utils/i18n'

export function createApp() {
  const app = createSSRApp(App)
  const pinia = createPinia()
  
  // 挂载全局配置和请求工具
  app.config.globalProperties.$config = config
  app.config.globalProperties.$baseUrl = config.baseUrl
  app.config.globalProperties.$request = request
  
  // 使用i18n插件与Pinia
  app.use(i18n)
  app.use(pinia)
  return {
    app
  }
}
// #endif