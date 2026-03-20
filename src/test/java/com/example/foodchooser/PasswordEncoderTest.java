package com.example.foodchooser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

// @SpringBootTest 会启动完整的 Spring 容器，这样我们才能自动注入刚才定义的 Bean
@SpringBootTest
public class PasswordEncoderTest {

    // 自动注入我们在 SecurityConfig 里定义的 PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testBcryptEncryption() {
        // 1. 定义明文密码
        String rawPassword = "123456";

        // 2. 第一次加密
        String encode1 = passwordEncoder.encode(rawPassword);

        // 3. 第二次加密（同样的明文）
        String encode2 = passwordEncoder.encode(rawPassword);

        // 4. 打印结果，直观对比
        System.out.println("第一次加密结果: " + encode1);
        System.out.println("第二次加密结果: " + encode2);

        // 5. 断言：两次加密结果应该是不一样的！
        // 这证明了 BCrypt 每次都自动生成了不同的“盐”
        assert !encode1.equals(encode2) : "两次加密结果不应相同";

        // 6. 核心验证：虽然密文不一样，但都能匹配上明文
        // matches方法内部会把明文和密文里的盐结合进行计算
        assert passwordEncoder.matches(rawPassword, encode1) : "明文应匹配第一个密文";
        assert passwordEncoder.matches(rawPassword, encode2) : "明文应匹配第二个密文";

        System.out.println("测试通过！BCrypt 加盐机制正常工作。");
    }
}

