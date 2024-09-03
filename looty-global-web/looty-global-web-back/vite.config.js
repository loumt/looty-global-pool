import {defineConfig} from 'vite'
import path from 'path'
import vue from '@vitejs/plugin-vue';

export default defineConfig(({}) => {
    return {
        base: '/',
        plugins: [vue()],
        resolve: {
            alias: {
                // 设置路径
                '~': path.resolve(__dirname, './'),
                // 设置别名
                '@': path.resolve(__dirname, './src')
            },
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
        },
        server:{
            port: 80
        }
    }
})