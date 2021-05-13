/*
 */
package model;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
public class LoggerHelper {
    
    public static String getRemoteAddr() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return getRemoteAddr(request);
    }
    
    public static String getRemoteAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    
    public static String getUserAgent() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return getUserAgent(request);
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return userAgent;
    }
    
    public static String escapeForLogging(String str) {
        str = str.replaceAll("(\\r|\\n)", " ");
        str = str.replaceAll("\\[", "(");
        str = str.replaceAll("\\]", ")");
        return str;
    }
}
