package org.gjgr.pig.chivalrous.core.net;

import org.gjgr.pig.chivalrous.core.exceptions.UtilException;
import org.gjgr.pig.chivalrous.core.lang.CollectionCommand;
import org.gjgr.pig.chivalrous.core.lang.StringCommand;
import org.gjgr.pig.chivalrous.core.lang.Validator;
import org.gjgr.pig.chivalrous.core.system.HostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;

/**
 * 网络相关工具
 *
 * @author xiaoleilu
 */
public final class NetworkCommand {
    public static final String LOCAL_IP = "127.0.0.1";
    private static final Logger LOGGER = LoggerFactory.getLogger(org.gjgr.pig.chivalrous.core.net.NetworkCommand.class);

    private static final String DockerIP = "172.17.42.1";

    private NetworkCommand() {
    }

    /**
     * Only site local IPV4 address will be returned. skip IPV6 and loopback address.
     */
    public static String getLocalHostIp() {
        try {
            for (NetworkInterface iface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(iface.getInetAddresses())) {
                    LOGGER.debug("Checking ip address {}", addr);
                    String hostAddress = addr.getHostAddress();
                    // The docker virtual environment uses a virtual ip which should be skipped.
                    if (addr.isSiteLocalAddress()
                            && !addr.isLoopbackAddress()
                            && !(addr instanceof Inet6Address)
                            && !hostAddress.equals(DockerIP)) {
                        LOGGER.debug("Ok, the ip {} will be used.", addr);
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            LOGGER.error("Couldn't find the local machine ip.", e);
        }
        throw new IllegalStateException("Couldn't find the local machine ip.");
    }

    public static HostInfo localHostInfo() {
        return new HostInfo();
    }

    public static String localHostInfomation() {
        return new HostInfo().toString();
    }

    /**
     * 根据long值获取ip v4地址
     *
     * @param longIP IP的long表示形式
     * @return IP V4 地址
     */
    public static String longToIpv4(long longIP) {
        StringBuffer sb = new StringBuffer();
        // 直接右移24位
        sb.append(String.valueOf(longIP >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000FF));
        return sb.toString();
    }

    /**
     * 根据ip地址计算出long型的数据
     *
     * @param strIP IP V4 地址
     * @return long值
     */
    public static long ipv4ToLong(String strIP) {
        if (Validator.isIpv4(strIP)) {
            long[] ip = new long[4];
            // 先找到IP地址字符串中.的位置
            int position1 = strIP.indexOf(".");
            int position2 = strIP.indexOf(".", position1 + 1);
            int position3 = strIP.indexOf(".", position2 + 1);
            // 将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIP.substring(0, position1));
            ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(strIP.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }
        return 0;
    }

    /**
     * 检测本地端口可用性
     *
     * @param port 被检测的端口
     * @return 是否可用
     */
    public static boolean isUsableLocalPort(int port) {
        if (!isValidPort(port)) {
            // 给定的IP未在指定端口范围中
            return false;
        }
        try {
            new Socket(LOCAL_IP, port).close();
            // socket链接正常，说明这个端口正在使用
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 是否为有效的端口
     *
     * @param port 端口号
     * @return 是否有效
     */
    public static boolean isValidPort(int port) {
        // 有效端口是0～65535
        return port >= 0 && port <= 0xFFFF;
    }

    /**
     * 判定是否为内网IP<br>
     * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
     **/
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = NetworkCommand.ipv4ToLong(ipAddress);

        long aBegin = NetworkCommand.ipv4ToLong("10.0.0.0");
        long aEnd = NetworkCommand.ipv4ToLong("10.255.255.255");

        long bBegin = NetworkCommand.ipv4ToLong("172.16.0.0");
        long bEnd = NetworkCommand.ipv4ToLong("172.31.255.255");

        long cBegin = NetworkCommand.ipv4ToLong("192.168.0.0");
        long cEnd = NetworkCommand.ipv4ToLong("192.168.255.255");

        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals(LOCAL_IP);
        return isInnerIp;
    }

    /**
     * 获得本机的IP地址列表<br>
     * 返回的IP列表有序，按照系统设备顺序
     *
     * @return IP地址列表 {@link LinkedHashSet}
     */
    public static LinkedHashSet<String> localIpv4s() {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new UtilException(e.getMessage(), e);
        }

        if (networkInterfaces == null) {
            throw new UtilException("Get network interface error!");
        }

        final LinkedHashSet<String> ipSet = new LinkedHashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && inetAddress instanceof Inet4Address) {
                    ipSet.add(inetAddress.getHostAddress());
                }
            }
        }

        return ipSet;
    }

    /**
     * 相对URL转换为绝对URL
     *
     * @param absoluteBasePath 基准路径，绝对
     * @param relativePath     相对路径
     * @return 绝对URL
     */
    public static String toAbsoluteUrl(String absoluteBasePath, String relativePath) {
        try {
            URL absoluteUrl = new URL(absoluteBasePath);
            return new URL(absoluteUrl, relativePath).toString();
        } catch (Exception e) {
            throw new UtilException(
                    StringCommand.format("To absolute url [{}] base [{}] error!", relativePath, absoluteBasePath), e);
        }
    }

    /**
     * 隐藏掉IP地址的最后一部分为 * 代替
     *
     * @param ip IP地址
     * @return 隐藏部分后的IP
     */
    public static String hideIpPart(String ip) {
        return new StringBuffer(ip.length()).append(ip.substring(0, ip.lastIndexOf(".") + 1)).append("*").toString();
    }

    /**
     * 隐藏掉IP地址的最后一部分为 * 代替
     *
     * @param ip IP地址
     * @return 隐藏部分后的IP
     */
    public static String hideIpPart(long ip) {
        return hideIpPart(longToIpv4(ip));
    }

    /**
     * 构建InetSocketAddress<br>
     * 当host中包含端口时（用“：”隔开），使用host中的端口，否则使用默认端口<br>
     * 给定host为空时使用本地host（127.0.0.1）
     *
     * @param host        Host
     * @param defaultPort 默认端口
     * @return InetSocketAddress
     */
    public static InetSocketAddress buildInetSocketAddress(String host, int defaultPort) {
        if (StringCommand.isBlank(host)) {
            host = LOCAL_IP;
        }

        String destHost = null;
        int port = 0;
        int index = host.indexOf(":");
        if (index != -1) {
            // host:port形式
            destHost = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        } else {
            destHost = host;
            port = defaultPort;
        }

        return new InetSocketAddress(destHost, port);
    }

    /**
     * 通过域名得到IP
     *
     * @param hostName HOST
     * @return ip address or hostName if UnknownHostException
     */
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }

    /**
     * 获取本机所有网卡
     *
     * @return 所有网卡，异常返回<code>null</code>
     * @since 3.0.1
     */
    public static Collection<NetworkInterface> getNetworkInterfaces() {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return null;
        }

        return CollectionCommand.addAll(new ArrayList<NetworkInterface>(), networkInterfaces);
    }

    /**
     * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
     * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
     * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
     * <p>
     * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     *
     * @return 本机网卡IP地址，获取失败返回<code>null</code>
     * @since 3.0.1
     */
    public static InetAddress getLocalhost() {
        InetAddress candidateAddress = null;
        NetworkInterface iface;
        InetAddress inetAddr;
        try {
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
                    .hasMoreElements(); ) {
                iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    inetAddr = inetAddrs.nextElement();
                    if (false == inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (null == candidateAddress) {
                            // 非site-local地址做为候选地址返回
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            // ignore socket exception, and return null;
        }

        if (null == candidateAddress) {
            try {
                candidateAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // ignore
            }
        }

        return candidateAddress;
    }

    // ----------------------------------------------------------------------------------------- Private method start

    /**
     * 指定IP的long是否在指定范围内
     *
     * @param userIp 用户IP
     * @param begin  开始IP
     * @param end    结束IP
     * @return 是否在范围内
     */
    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
    // ----------------------------------------------------------------------------------------- Private method end
}
