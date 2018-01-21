package util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huiliu
 * @date 17/9/6
 */
public class Constants {
    public static final String TRACE_ID = "trace_id";
    public static final String SESSION_ID = "hk_session_id";
    public static final String USER_ID = "user_id";
    public static final String ACCOUNT_ID = "account_id";
    public static Integer XJYW = 1;
    public static Integer HTYW = 2;
    public static Integer ZXYW = 3;


    public static Map getRequestParams(HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
}
