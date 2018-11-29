package org.gjgr.pig.chivalrous.core.crypto.digest;

/**
 * 摘要算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
 *
 * @author Looly
 */
public enum DigestType {
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private String value;

    private DigestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}