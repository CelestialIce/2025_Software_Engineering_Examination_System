import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig({
  plugins: [uni()],
  server: {
    port: 8081, // 修改为你想要的端口号，默认是 5173
    host: '0.0.0.0', // 允许外部访问
    open: true // 自动打开浏览器
  }
})
