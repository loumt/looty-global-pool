import axios from 'axios'
import { ElNotification ,  ElMessage } from 'element-plus'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const api = axios.create({
    baseURL: import.meta.env.LY_API_BASE_URL,
    timeout: 1000
})

api.interceptors.response.use(
    response => {
    let {code, data, message} =  response.data

    if(code === 100000){
        return data
    } else {
        ElMessage({ message , type: 'error' })
    }
}, error => {
    console.dir(error)
})

export default api