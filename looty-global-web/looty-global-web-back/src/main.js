import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

import 'element-plus/dist/index.css'
import icon from './plugins/icon'
import {formatDate} from './utils/tool'

import App from './App'
import store from './store'
import router from './router'

// 分页组件
import Pagination from '@/components/Pagination'

const app = createApp(App)

//自定义组件载入
app.component('Pagination', Pagination)

// 全局方法挂载
app.config.globalProperties.formatDate = formatDate

app.use(router)
app.use(store)
app.use(ElementPlus, {locale: zhCn})
app.use(icon)

app.mount('#app')

console.log("Title => ", import.meta.env.LY_APP_TITLE)