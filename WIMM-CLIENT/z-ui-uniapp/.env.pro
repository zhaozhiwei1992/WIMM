# 环境
NODE_ENV=production

# 接口前缀, 这个变量不要改, axios通过这个作为key ,获取实际后端地址
VITE_API_BASEPATH=pro

# 小程序无"当前域名"概念, 必须用绝对地址(后端走公网 ECS nginx 反代)
# axios baseURL = VITE_SERVER_URL + '/api' → https://19921514.xyz/wimm/api
# 经 nginx location /wimm/api/ 反代到 NAS 后端 10.11.12.164:8091
VITE_SERVER_URL='https://19921514.xyz/wimm'

# 打包路径
VITE_BASE_PATH=/

# 是否删除debugger
VITE_DROP_DEBUGGER=true

# 是否删除console.log
VITE_DROP_CONSOLE=true

# 是否sourcemap
VITE_SOURCEMAP=false

# 输出路径
VITE_OUT_DIR=dist-pro

# 标题
VITE_APP_TITLE=钱呢
