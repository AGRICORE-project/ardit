package eu.agricore.indexer.util;


import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class HttpUtils {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };


    public static String getRequestIP(HttpServletRequest request) throws UnknownHostException {
        for (String header : IP_HEADERS) {

            String value = request.getHeader(header);
            if (value == null || value.isEmpty()) {
                continue;
            }
            String[] parts = value.split("\\s*,\\s*");
            return parts[0];
        }

        String requestRemote = request.getRemoteAddr();

        if ("127.0.0.1".equals(requestRemote) || "0:0:0:0:0:0:0:1".equals(requestRemote)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            requestRemote = inetAddress.getHostAddress();
        }

        return requestRemote;
    }
}