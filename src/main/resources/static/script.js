// 获取页面元素
const button = document.getElementById('chooseBtn');
const result = document.getElementById('result');

// 点击事件：每次点击都向后端请求一个随机食物
button.addEventListener('click', function() {
    // 发起请求，获取随机食物
    fetch('/random-food')
        .then(response => {
            if (!response.ok) {
                throw new Error('网络响应错误');
            }
            return response.json(); // 把返回的 JSON 转成 JS 对象
        })
        .then(food => {
            // food 是一个对象，包含 id, name, canteen, category 等属性
            result.textContent = `今天吃：${food.name}`; // 显示食物名称
        })
        .catch(error => {
            console.error('请求失败:', error);
            result.textContent = '出错了，请稍后重试';
        });
});