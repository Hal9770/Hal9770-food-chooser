这是一个“食物选择器”项目，js通过fetch向API接口发起请求，通知后端前往数据库提取数据，转化成JSON格式字符串，最终将结果打印在HTML网页上。
效果是按下“随机选一个按钮”，页面会浮现：“今天吃:食物名称：namev2,所在位置：positionv2.2026/3/18
通过Mybatis-plus的QueryWrapper条件构造器，增加了根据食堂及其楼层范围筛选食物的功能。2026/3/20
通过Navicat工具更新丰富数据库，修改html文件且美化了网页，并且通过阿里云轻量化服务器部署上线，IP地址是：http://139.196.53.42:8080/ 2026/3/28
增加SpringSecurity依赖以及User用户登录/注册业务逻辑，并且通过JWT实现登录的无状态认证，其中JwtUtils用于制造和验证token，JwtAuthenticationFilter用于过滤请求和盖章，SecurityConfig用于放行与拦截，js文件调用localStorage.setItem将token收进本地缓存，每次请求再将token放进请求头联络过滤器。2026/4/5
启用easy-captcha依赖生成验证码图片，自定义CaptchaCache验证比对销毁验证码，登录和注册均从前端请求头获取验证码并调用CaptchaCache比对，因此可以防止恶意刷机。2026/4/9
