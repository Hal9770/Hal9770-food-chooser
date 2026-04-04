// ================= 用户认证相关逻辑 =================

// 1. 获取新增的 DOM 元素
const usernameInput = document.getElementById('username-input');
const passwordInput = document.getElementById('password-input');
const loginBtn = document.getElementById('login-btn');
const registerBtn = document.getElementById('register-btn');
const logoutBtn = document.getElementById('logout-btn');
const authForms = document.getElementById('auth-forms');
const userInfo = document.getElementById('user-info');
const currentUserSpan = document.getElementById('current-user');

// 【新增】页面一加载，就检查兜里有没有之前存的 Token
window.onload = function() {
    const savedToken = localStorage.getItem('jwt_token');
    if (savedToken) {
        // 如果有 Token，直接显示登录后的状态，免去重新登录
        // （注意：这里只是前端展示，真正的合法性由后端拦截器校验）
        authForms.style.display = 'none';
        userInfo.style.display = 'block';
        currentUserSpan.textContent = localStorage.getItem('jwt_username'); // 显示存的用户名
    }
};

// 2. 注册功能（这里不用改，因为注册不走 JWT）
registerBtn.addEventListener('click', function() {
    const username = usernameInput.value;
    const password = passwordInput.value;

    if (!username || !password) {
        alert('用户名和密码不能为空！');
        return;
    }

    fetch('/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, password: password })
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
        if (data.includes('成功')) {
            passwordInput.value = '';
        }
    });
});

// 3. 登录功能（【重点修改区域】）
loginBtn.addEventListener('click', function() {
    const username = usernameInput.value;
    const password = passwordInput.value;

    fetch('/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username, password: password })
    })
    // 【修改1】后端现在返回的是 JSON 对象，不是纯文本了，用 .json() 解析
    .then(res => res.json())
    .then(data => {
        // 【修改2】判断后端返回的状态码（对应 Controller 里的 result.put("code", 200)）
        if (data.code === 200) {
            alert(data.message);

            // 【核心修改】把后端发来的 Token 拿出来，存到浏览器的“本地缓存”里（揣进兜里）
            localStorage.setItem('jwt_token', data.token);
            localStorage.setItem('jwt_username', username); // 顺便把用户名也存一下

            // 切换页面状态
            authForms.style.display = 'none';
            userInfo.style.display = 'block';
            currentUserSpan.textContent = username;
        } else {
            // 登录失败，弹出后端返回的错误信息
            alert(data.message);
        }
    });
});

// 4. 退出登录（【重点修改】不能只刷新页面了）
logoutBtn.addEventListener('click', function() {
    if(confirm('确定要退出吗？')) {
        // 【核心修改】退出时，必须把兜里的 Token 丢掉！
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('jwt_username');

        // 刷新页面，由于没有 Token 了，页面会自动变回登录框
        window.location.reload();
    }
});

// ================= 原有的食物选择逻辑 =============
const button = document.getElementById('chooseBtn');
const card = document.getElementById('food-card');
const nameEl = document.getElementById('food-name');
const positionEl = document.getElementById('food-position');
const canteenSelect = document.getElementById('canteenSelect');

button.addEventListener('click', function() {
    const selectedPosition = canteenSelect.value;
    let url = '/random-food';
    if (selectedPosition) {
        url = '/random-food/by-position?position=' + encodeURIComponent(selectedPosition);
    }

    // 【核心修改】发起请求时，要把 Token 从兜里掏出来，放到请求头里（亮给后端看）
    fetch(url, {
        method: 'GET',
        headers: {
            // 从本地缓存拿出 Token，赋值给 Authorization 请求头
            'Authorization': localStorage.getItem('jwt_token') || ''
        }
    })
        .then(response => {
            // 【新增逻辑】如果后端拦截器发现 Token 不对，会返回 401 状态码
            if (response.status === 401) {
                alert('登录已过期或未登录，请重新登录！');
                localStorage.removeItem('jwt_token'); // 清除坏的 Token
                window.location.reload(); // 刷新页面，强制回到登录框
                return; // 终止后续操作
            }

            if (!response.ok) {
                throw new Error('网络响应错误');
            }
            return response.json();
        })
        .then(food => {
            // 如果上面 return 了，这里的 food 是 undefined，所以要加个判断防抖
            if (!food) return;

            nameEl.textContent = food.namev2;
            positionEl.textContent = food.positionv2;
            card.style.display = 'block';
        })
        .catch(error => {
            console.error('请求失败:', error);
            // 只有不是 401 引起的错误才弹这个窗
            if(error.message !== '网络响应错误') return;
            alert('哎呀，服务器开小差了，请稍后重试！');
        });
});
