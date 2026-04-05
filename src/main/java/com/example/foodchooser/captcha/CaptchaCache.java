package com.example.foodchooser.captcha;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CaptchaCache {
    // 使用ConcurrentHashMap保证线程安全
    private static final Map<String, CaptchaItem> CACHE = new ConcurrentHashMap<>();

    // 验证码过期时间：2分钟（毫秒）
    private static final long EXPIRE_TIME = 2 * 60 * 1000;

    /**
     * 存储验证码
     * @param key 验证码key（通常使用UUID）
     * @param code 验证码文本
     */
    public static void put(String key, String code) {
        CACHE.put(key, new CaptchaItem(code, System.currentTimeMillis()));
    }

    /**
     * 验证并移除验证码
     * @param key 验证码key
     * @param userInputCode 用户输入的验证码
     * @return 验证是否成功
     */
    public static boolean checkAndRemove(String key, String userInputCode) {
        if (key == null || userInputCode == null) {
            return false;
        }

        CaptchaItem item = CACHE.get(key);
        if (item == null) {
            return false; // 验证码不存在
        }

        // 检查是否过期
        if (System.currentTimeMillis() - item.time > EXPIRE_TIME) {
            CACHE.remove(key);
            return false;
        }

        // 验证码比对（忽略大小写）
        if (item.code.equalsIgnoreCase(userInputCode)) {
            CACHE.remove(key); // 验证成功后立即移除，防止重复使用
            return true;
        }

        return false;
    }

    /**
     * 清理过期的验证码（可选，定期清理）
     */
    public static void cleanExpired() {
        long now = System.currentTimeMillis();
        CACHE.entrySet().removeIf(entry ->
                now - entry.getValue().time > EXPIRE_TIME
        );
    }

    // 内部类，存储验证码和时间戳
    private static class CaptchaItem {
        String code;
        long time;

        CaptchaItem(String code, long time) {
            this.code = code;
            this.time = time;
        }
    }
}
