package BankSystem.demo.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Resolves the client IP address from the current HTTP request.
 * Can be used by aspects, filters, or controllers that need request context.
 */
@Component
public class ClientIpResolver {

    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String UNKNOWN_IP = "unknown";

    /**
     * Gets the client IP address from the current request.
     * Respects X-Forwarded-For header when behind a proxy/load balancer.
     *
     * @return the client IP, or "unknown" if no request is available
     */
    public String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return UNKNOWN_IP;
        }
        String xForwardedFor = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr != null ? remoteAddr : UNKNOWN_IP;
    }

    /**
     * Gets the current HttpServletRequest from the request context.
     *
     * @return the current request, or null if not in a web request context
     */
    public HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
