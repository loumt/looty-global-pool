import {defineConfig} from "vite";
import path from 'node:path'
import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';

export default defineConfig({
    server:{
        port: 80,
        proxy:{
            "/api": {
                target: "http://localhost:8888",
                changeOrigin: true
            }
        }
    },
    plugins: [
        vue(),
        AutoImport({
            imports: ["vue", "pinia"]
        })
    ],
    resolve: {
        alias: {
            '~': path.resolve(__dirname, './'),
            '@': path.resolve(__dirname, './src')
        },
        extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    css: {
        preprocessorOptions:{
            scss:{
                api: 'modern-compiler', // ["modern", "legacy",modern-compiler]
            }
        }
    },
    //环境变量配置文件地址
    // envDir: "",
    //环境变量识别前缀
    envPrefix: "LY_"
})