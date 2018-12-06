package org.gjgr.pig.chivalrous.core.system;

/**
 * 代表Java Virutal Machine Specification的信息。
 */
public class JvmSpecInfo {

    private final String JAVA_VM_SPECIFICATION_NAME = JavaSystemCommand.get("java.vm.specification.name", false);
    private final String JAVA_VM_SPECIFICATION_VERSION = JavaSystemCommand.get("java.vm.specification.version", false);
    private final String JAVA_VM_SPECIFICATION_VENDOR = JavaSystemCommand.get("java.vm.specification.vendor", false);

    /**
     * 取得当前JVM spec.的名称（取自系统属性：<code>java.vm.specification.name</code>）。
     * <p>
     * <p>
     * 例如Sun JDK 1.4.2：<code>"Java Virtual Machine Specification"</code>
     * </p>
     *
     * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
     */
    public final String getName() {
        return JAVA_VM_SPECIFICATION_NAME;
    }

    /**
     * 取得当前JVM spec.的版本（取自系统属性：<code>java.vm.specification.version</code>）。
     * <p>
     * <p>
     * 例如Sun JDK 1.4.2：<code>"1.0"</code>
     * </p>
     *
     * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
     */
    public final String getVersion() {
        return JAVA_VM_SPECIFICATION_VERSION;
    }

    /**
     * 取得当前JVM spec.的厂商（取自系统属性：<code>java.vm.specification.vendor</code>）。
     * <p>
     * <p>
     * 例如Sun JDK 1.4.2：<code>"Sun Microsystems Inc."</code>
     * </p>
     *
     * @return 属性值，如果不能取得（因为Java安全限制）或值不存在，则返回<code>null</code>。
     */
    public final String getVendor() {
        return JAVA_VM_SPECIFICATION_VENDOR;
    }

    /**
     * 将Java Virutal Machine Specification的信息转换成字符串。
     *
     * @return JVM spec.的字符串表示
     */
    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();

        JavaSystemCommand.append(builder, "JavaVM Spec. Name:    ", getName());
        JavaSystemCommand.append(builder, "JavaVM Spec. Version: ", getVersion());
        JavaSystemCommand.append(builder, "JavaVM Spec. Vendor:  ", getVendor());

        return builder.toString();
    }

}
