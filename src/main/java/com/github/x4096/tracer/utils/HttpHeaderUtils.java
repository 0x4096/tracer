package com.github.x4096.tracer.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 0x4096.peng@gmail.com
 * @date 2022/12/17
 */
public class HttpHeaderUtils {

    public static Map<String, String> header2Map(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();

        Enumeration<String> header = request.getHeaderNames();
        while (header.hasMoreElements()) {
            String key = header.nextElement();
            result.put(key, request.getHeader(key));
        }

        return result;
    }

}
