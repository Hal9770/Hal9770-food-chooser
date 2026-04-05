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

// 【新增 1】获取验证码相关的 DOM 元素
const captchaImg = document.getElementById('captcha-img');       // 那张图片
const captchaInput = document.getElementById('captcha-input');   // 用户填答案的输入框
let currentCaptchaKey = ''; // 定义一个全局变量，用来存后端发来的“暗号”

// 页面一加载，就检查兜里有没有之前存的 Token
window.onload = function() {
    const savedToken = localStorage.getItem('jwt_token');
    if (savedToken) {
        authForms.style.display = 'none';
        userInfo.style.display = 'block';
        currentUserSpan.textContent = localStorage.getItem('jwt_username');
    }
    // 【新增 2】页面加载时，顺便请求第一张验证码图片
    loadCaptcha();
};

// ================= 【新增功能】验证码逻辑 =================
// 专门写一个函数，用来向后端要验证码图片
function loadCaptcha() {
    fetch('/users/captcha')
        .then(res => res.json())
        .then(data => {
            if (data.code === 200) {
                // 1. 把后端传来的图片（Base64格式）塞给 img 标签的 src
                // data.image 长这样：data:image/gif;base64,R0lGODlh...
                captchaImg.src = data.image;
                // 2. 【极其重要】把后端发来的“暗号”存到我们的全局变量里！
                currentCaptchaKey = data.key;
            } else {
                alert('获取验证码失败');
            }
        });
}

// 点击验证码图片时，重新换一张（俗称“看不清，换一张”）
captchaImg.addEventListener('click', loadCaptcha);


// 2. 注册功能（【重点修改区域】）
registerBtn.addEventListener('click', function() {
    const username = usernameInput.value;
    const password = passwordInput.value;
    // 【新增】拿到用户在输入框里填的验证码答案
    const userInputCaptcha = captchaInput.value;

    if (!username || !password) {
        alert('用户名和密码不能为空！');
        return;
    }
    // 【新增】如果没填验证码，直接拦住
    if (!userInputCaptcha) {
        alert('请输入验证码！');
        return;
    }

    fetch('/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        // 【重点修改】现在不能只传账号密码了，得把“暗号”和“答案”一起打包发给后端
        body: JSON.stringify({
            username: username,
            password: password,
            captchaKey: currentCaptchaKey, // 之前存好的暗号
            captchaCode: userInputCaptcha  // 用户填的答案
        })
    })
    // 【修改】因为后端 Controller 现在统一返回 Map（JSON格式），所以用 .json() 解析
    .then(res => res.json())
    .then(data => {
        alert(data.message); // 弹出后端返回的提示（成功或失败都有）

        // 无论注册成功还是失败（比如验证码填错了），都应该刷新一下验证码
        // 因为验证码用错一次就失效了（防重放攻击）
        loadCaptcha();
        captchaInput.value = ''; // 清空验证码输入框

        if (data.code === 200) {
            // 注册成功后的其他操作（比如清空密码等，按你之前的习惯来）
            passwordInput.value = '';
        }
    });
});


// 3. 登录功能（保持你之前写好的 JWT 逻辑不动）
loginBtn.addEventListener('click', function() {
    const username = usernameInput.value;
    const password = passwordInput.value;
    // 【新增】拿到用户填的验证码
        const userInputCaptcha = captchaInput.value;

        // 【新增】没填验证码直接拦住
        if(!userInputCaptcha) {
            alert('请输入验证码！');
            return;
        }

    fetch('/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username,
         password: password,
         captchaKey: currentCaptchaKey,
         captchaCode: userInputCaptcha })
    })
    .then(res => res.json())
    .then(data => {
        if (data.code === 200) {
            alert(data.message);
            localStorage.setItem('jwt_token', data.token);
            localStorage.setItem('jwt_username', username);
            authForms.style.display = 'none';
            userInfo.style.display = 'block';
            currentUserSpan.textContent = username;
        } else {
            alert(data.message);
            // 【新增】登录失败（密码错或验证码错），都要刷新验证码
            loadCaptcha();
            captchaInput.value = '';
        }
    });
});


// 4. 退出登录
logoutBtn.addEventListener('click', function() {
    if(confirm('确定要退出吗？')) {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('jwt_username');
        window.location.reload();
    }
});


// ================= 原有的食物选择逻辑（保持不动） =============
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

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem('jwt_token') || ''
        }
    })
        .then(response => {
            if (response.status === 401) {
                alert('登录已过期或未登录，请重新登录！');
                localStorage.removeItem('jwt_token');
                window.location.reload();
                return;
            }
            if (!response.ok) {
                throw new Error('网络响应错误');
            }
            return response.json();
        })
        .then(food => {
            if (!food) return;
            nameEl.textContent = food.namev2;
            positionEl.textContent = food.positionv2;
            card.style.display = 'block';
        })
        .catch(error => {
            console.error('请求失败:', error);
            if(error.message !== '网络响应错误') return;
            alert('哎呀，服务器开小差了，请稍后重试！');
        });
});
