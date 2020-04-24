package org.gjgr.pig.chivalrous.core.authentication;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.gjgr.pig.chivalrous.core.lang.ObjectCommand;

/**
 * Copied from @{link https://github.com/parkghost/TOTP-authentication-demo}
 *
 * <strong>NOTE: </strong> in order to use this class in CAS maven war overlays, exclude the following jar in the
 * maven-war-plugin->configuration->overlays->excludes section of local maven overlay pom.xml: WEB-INF/lib/commons-codec-1.4.jar
 * append implements code
 * <p>ClassName: TOTP</p>
 * <p>Description: TOTP = HOTP(K, T) // T is an integer
 * and represents the number of time steps between the initial counter
 * time T0 and the current Unix time
 * <p>
 * More specifically, T = (Current Unix time - T0) / X, where the
 * default floor function is used in the computation.</p>
 *
 * @since 0.5
 */
public class TOTPHolder {

    /**
     * 共享密钥
     * String SECRET_KEY = "ga35sdia43dhqj6k3f0la";
     */
    private static final ThreadLocal<String> LOCAL_SECRET_KEY = new ThreadLocal<>();
    /**
     * 时间步长 单位:毫秒 作为口令变化的时间周期
     */
    private static final long STEP = 60000;
    /**
     * 转码位数 [1-8]
     */
    private static final int CODE_DIGITS = 6;
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

    public TOTPHolder(String secretKey) {
        LOCAL_SECRET_KEY.set(secretKey);
    }

    TOTPHolder() {
        LOCAL_SECRET_KEY.set("default");
    }

    public static void main(String[] args) {
        try {

            for (int j = 0; j < 10; j++) {
                String totp = generateMyTOTP("account01", "12345");
                System.out.println(String.format("加密后: %s", totp));
                Thread.sleep(10000);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成一次性密码
     *
     * @param code 账户
     * @param pass 密码
     * @return String
     */
    public static String generateMyTOTP(String code, String pass) {
        if (ObjectCommand.isEmpty(code) || ObjectCommand.isEmpty(pass)) {
            throw new RuntimeException("target code and password should not null.");
        }
        long now = System.currentTimeMillis();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        return generateTOTP(code + pass + LOCAL_SECRET_KEY.get(), time);
    }

    /**
     * 刚性口令验证
     *
     * @param code 账户
     * @param password 密码
     * @param totp 待验证的口令
     * @return boolean
     */
    public static boolean verifyTOTPRigidity(String code, String password, String totp) {
        return generateMyTOTP(code, password).equals(totp);
    }

    /**
     * 柔性口令验证
     *
     * @param code 账户
     * @param password 密码
     * @param totp 待验证的口令
     * @return boolean
     */
    public static boolean verifyTOTPFlexibility(String code, String password, String totp) {
        long now = System.currentTimeMillis();
        String time = Long.toHexString(timeFactor(now)).toUpperCase();
        String tempTotp = generateTOTP(code + password + LOCAL_SECRET_KEY.get(), time);
        if (tempTotp.equals(totp)) {
            return true;
        }
        String time2 = Long.toHexString(timeFactor(now - FLEXIBILIT_TIME)).toUpperCase();
        String tempTotp2 = generateTOTP(code + password + LOCAL_SECRET_KEY.get(), time2);
        return tempTotp2.equals(totp);
    }

    /**
     * 获取动态因子
     *
     * @param targetTime 指定时间
     * @return long
     */
    private static long timeFactor(long targetTime) {
        return (targetTime - INITIAL_TIME) / STEP;
    }

    /**
     * 哈希加密
     *
     * @param crypto 加密算法
     * @param keyBytes 密钥数组
     * @param text 加密内容
     * @return byte[]
     */
    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "AES");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private static byte[] hexStr2Bytes(String hex) {
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        System.arraycopy(bArray, 1, ret, 0, ret.length);
        return ret;
    }

    private static String generateTOTP(String key, String time) {
        return generateTOTP(key, time, "HmacSHA1");
    }

    private static String generateTOTP256(String key, String time) {
        return generateTOTP(key, time, "HmacSHA256");
    }

    private static String generateTOTP512(String key, String time) {
        return generateTOTP(key, time, "HmacSHA512");
    }

    private static String generateTOTP(String key, String time, String crypto) {
        StringBuilder timeBuilder = new StringBuilder(time);
        while (timeBuilder.length() < 16) {
            timeBuilder.insert(0, "0");
        }
        time = timeBuilder.toString();

        byte[] msg = hexStr2Bytes(time);
        byte[] k = key.getBytes();
        byte[] hash = hmac_sha(crypto, k, msg);
        return truncate(hash);
    }

    /**
     * 截断函数
     *
     * @param target 20字节的字符串
     * @return String
     */
    private static String truncate(byte[] target) {
        StringBuilder result;
        int offset = target[target.length - 1] & 0xf;
        int binary = ((target[offset] & 0x7f) << 24)
                | ((target[offset + 1] & 0xff) << 16)
                | ((target[offset + 2] & 0xff) << 8) | (target[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[CODE_DIGITS];
        result = new StringBuilder(Integer.toString(otp));
        while (result.length() < CODE_DIGITS) {
            result.insert(0, "0");
        }
        return result.toString();
    }

}