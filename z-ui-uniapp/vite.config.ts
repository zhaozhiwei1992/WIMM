import uni from "@dcloudio/vite-plugin-uni";
import AutoImport from 'unplugin-auto-import/vite';
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [uni(),
    // 自动导入配置
        AutoImport({
            imports:[
                // 预设
                'vue',
                'uni-app'                
            ]
        })
  ],
  server: {
    port: 5173,
    proxy: {
      // 选项写法
      '/api': {
        target: 'http://localhost:8090',
        changeOrigin: true,
        // 后端接口也有/api开头, 所以不要去掉了
        // rewrite: path => path.replace(/^\/api/, '')
      }
    },
    // hmr: {
    //   overlay: false
    // },
    hmr: true,
    host: '0.0.0.0'
  },
});
