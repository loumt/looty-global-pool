import {defineConfig} from "vite";
import path from 'node:path'
import vue from '@vitejs/plugin-vue';
import AutoImport from 'unplugin-auto-import/vite';

export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            imports:["vue", "pinia"]
        })
    ],
    resolve: {
        alias: {
            '~': path.resolve(__dirname, './'),
            '@': path.resolve(__dirname, './src')
        },
        extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    //环境变量配置文件地址
    // envDir: "",
    envPrefix: "ENV_",
    server:{
        port: 80
    }
})