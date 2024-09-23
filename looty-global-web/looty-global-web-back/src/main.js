import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import icon from './plugins/icon'

import App from './App'
import store from './store'
import router from './router'

const app = createApp(App)

app.use(router)
app.use(store)
app.use(ElementPlus)
app.use(icon)

app.mount('#app')


console.log("Title => ", import.meta.env.ENV_APP_TITLE)