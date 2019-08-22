package org.gjgr.pig.chivalrous.core.crypto.asymmetric;

/**
 * 非对称算法类型<br>
 *
 * @author Looly
 */
public enum AsymmetricType {
    RSA("RSA"),
    DSA("DSA");

    private String value;

    private AsymmetricType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
