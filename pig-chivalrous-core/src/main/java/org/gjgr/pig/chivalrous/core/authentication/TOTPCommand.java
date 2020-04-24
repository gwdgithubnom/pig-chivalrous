package org.gjgr.pig.chivalrous.core.authentication;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.codec.binary.Base32;

/**
 * @author gwd
 * @time 12-24-2019  Tuesday
 * @description: gjgrparent:
 * @target:
 * @more:
 */
public class TOTPCommand {
    private static final int SECRET_SIZE = 10;

    private static final int PASS_CODE_LENGTH = 6;

    private static final String CRYPTO = "HmacSHA1";

    private static final Random rand = new Random();

    /**
     * 共享密钥
     * String SECRET_KEY = "ga35sdia43dhqj6k3f0la";
     */
    private static final ThreadLocal<String> LOCAL_SECRET_KEY = new ThreadLocal<>();
    /**
     * 时间步长 单位:毫秒 作为口令变化的时间周期
     */
    private static final long STEP = 30000;
    /**
     * 转码位数 [1-8]
     */
    private static final int CODE_DIGITS = 8;
    /**
     * 初始化时间
     */
    private static final long INITIAL_TIME = 0;
    /**
     * 柔性时间回溯
     */
    private static final long FLEXIBILIT_TIME = 5000;
    /**
     * 数子量级
     */
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public static String generateCommonSecret() {

        // Allocating the buffer
        byte[] buffer = new byte[SECRET_SIZE];

        // Filling the buffer with random numbers.
        rand.nextBytes(buffer);

        // Getting the key and converting it to Base32
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, SECRET_SIZE);
        byte[] encodedKey = codec.encode(secretKey);
        return new String(encodedKey);
    }

    public static boolean verifyCommonSecret(String secret, long code, int interval, int window)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        //int window = WINDOW;
        long currentInterval = getCurrentInterval(interval);

        for (int i = -window; i <= window; ++i) {
            long hash = TOTP.generateTOTP(decodedKey, currentInterval + i, PASS_CODE_LENGTH, CRYPTO);

            if (hash == code) {
                return true;
            }
        }

        // The validation code is invalid.
        return false;
    }

    private static long getCurrentInterval(int interval) {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        return currentTimeSeconds / interval;
    }
}
