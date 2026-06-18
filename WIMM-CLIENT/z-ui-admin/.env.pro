# 环境
NODE_ENV=production

# 接口前缀, 这个变量不要改, axios通过这个作为key ,获取实际后端地址
VITE_API_BASEPATH=pro

# 后端地址: 留空则用相对路径, 由公网 nginx 的 /wimm/api/ 反代到 NAS 后端
# (同源访问 https://19921514.xyz/wimm/api/..., 避免 https 页面调 http 的混合内容拦截与跨域)
# 注意: axios baseURL = VITE_SERVER_URL + '/api', 这里配 /wimm → /wimm/api
VITE_SERVER_URL=/wimm

# 打包路径: 必须与 nginx 部署的子路径一致(带结尾斜杠)
# 否则静态资源会 404(产物里引用 /assets/xxx, 实际需 /wimm/assets/xxx)
VITE_BASE_PATH=/wimm/

# 是否删除debugger
VITE_DROP_DEBUGGER=true

# 是否删除console.log
VITE_DROP_CONSOLE=true

# 是否sourcemap
VITE_SOURCEMAP=false

# 输出路径
VITE_OUT_DIR=wimm-admin

# 标题
VITE_APP_TITLE=钱呢
