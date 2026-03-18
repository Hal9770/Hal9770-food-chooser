// 1. 获取页面元素（拿到了按钮和三个填充位置）
const button = document.getElementById('chooseBtn');
const card = document.getElementById('food-card');
const nameEl = document.getElementById('food-name');
const positionEl = document.getElementById('food-position');


button.addEventListener('click', function() {
    // 2. 发起请求（注意核对这里的路径是否和后端 Controller 对应）
    fetch('/random-food')
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
