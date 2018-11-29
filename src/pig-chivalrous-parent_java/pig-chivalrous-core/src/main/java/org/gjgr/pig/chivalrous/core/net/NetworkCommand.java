package org.gjgr.pig.chivalrous.core.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

/**
 * @Author gwd
 * @Time 10-29-2018  Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class NetworkCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkCommand.class);

    private static final String DockerIP = "172.17.42.1";

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
}
