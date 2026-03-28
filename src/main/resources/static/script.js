// ================= 用户认证相关逻辑 =================

//// 1. 获取新增的 DOM 元素
//const usernameInput = document.getElementById('username-input');
//const passwordInput = document.getElementById('password-input');
//const loginBtn = document.getElementById('login-btn');
//const registerBtn = document.getElementById('register-btn');
//const logoutBtn = document.getElementById('logout-btn');
//const authForms = document.getElementById('auth-forms');
//const userInfo = document.getElementById('user-info');
//const currentUserSpan = document.getElementById('current-user');
//
//// 2. 注册功能
//registerBtn.addEventListener('click', function() {
//    const username = usernameInput.value;
//    const password = passwordInput.value;
//
//    if (!username || !password) {
//        alert('用户名和密码不能为空！');
//        return;
//    }
//
//    fetch('/users/register', {
//        method: 'POST',
//        headers: { 'Content-Type': 'application/json' },
//        body: JSON.stringify({ username: username, password: password })
//    })
//    .then(res => res.text()) // 后端返回的是字符串 "注册成功"
//    .then(data => {
//        alert(data); // 弹窗显示结果
//        if (data.includes('成功')) {
//            // 注册成功后自动填入用户名，方便用户直接点登录
//            passwordInput.value = '';
//        }
//    });
//});
//
//// 3. 登录功能
//loginBtn.addEventListener('click', function() {
//    const username = usernameInput.value;
//    const password = passwordInput.value;
//
//    fetch('/users/login', {
//        method: 'POST',
//        headers: { 'Content-Type': 'application/json' },
//        body: JSON.stringify({ username: username, password: password })
//    })
//    .then(res => res.text())
//    .then(data => {
//        if (data.includes('欢迎')) {
//            // 登录成功！
//            alert(data);
//
//            // 【核心】切换页面状态
//            authForms.style.display = 'none'; // 隐藏登录框
//            userInfo.style.display = 'block'; // 显示欢迎信息
//            currentUserSpan.textContent = username; // 显示用户名
//
//            // 【重要】后续如果做 JWT，这里需要存储 Token
//            // localStorage.setItem('token', '...');
//        } else {
//            alert(data); // 弹出 "用户名或密码错误"
//        }
//    });
//});
//
//// 4. 退出登录（暂时只是刷新页面）
//logoutBtn.addEventListener('click', function() {
//    if(confirm('确定要退出吗？')) {
//        window.location.reload();
//    }
//});

// ================= 原有的食物选择逻辑 =============
// 1. 获取页面元素（拿到了按钮和三个填充位置）
const button = document.getElementById('chooseBtn');
const card = document.getElementById('food-card');
const nameEl = document.getElementById('food-name');
const positionEl = document.getElementById('food-position');
const canteenSelect = document.getElementById('canteenSelect'); // 获取下拉框

button.addEventListener('click', function() {
    // 2. 获取用户选择的食堂
    const selectedPosition = canteenSelect.value;

    // 3. 动态设置请求 URL
    let url = '/random-food'; // 默认：全校随机

    if (selectedPosition) {
        // 如果用户选了具体食堂，改为调用筛选接口
        // encodeURIComponent 非常重要，防止中文乱码
        url = '/random-food/by-position?position=' + encodeURIComponent(selectedPosition);
    }
    // 2. 发起请求（注意核对这里的路径是否和后端 Controller 对应）
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('网络响应错误');
            }
            return response.json();
        })
        .then(food => {
            // 3. 核心改动：数据填充
            // 把后端传回来的 food 对象的属性，填到对应的 span 标签里
            nameEl.textContent = food.namev2;
            positionEl.textContent = food.positionv2; // 假设后端返回了这些字段

            // 4. 让隐藏的卡片显示出来
            card.style.display = 'block';
        })
        .catch(error => {
            console.error('请求失败:', error);
            alert('哎呀，服务器开小差了，请稍后重试！'); // 弹窗提示比改文字更明显
        });
});
