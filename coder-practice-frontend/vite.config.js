import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 5173,
    host: '0.0.0.0',

    // 🌐 代理配置（解决跨域）
    // proxy: {
    //   '/api': {
    //     target: 'http://localhost:8001', // 后端接口地址
    //     changeOrigin: true,              // 修改请求头中的 origin
    //     rewrite: (path) => path.replace(/^\/api/, '/api') // 一般保留/api前缀
    //   }
    // }
  }
})