package vn.dating.app.social.utils;

import java.util.UUID;

public class RandomStringTime {

    public static String generateRandomStringByTime() {
        long currentTimeMillis = System.currentTimeMillis();
        String randomString = UUID.randomUUID().toString().replace("-", "");
        String result = currentTimeMillis + "-" + randomString;
        return result;
    }
}
