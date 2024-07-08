package org.example.blogplatform.jwt.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {

    /**
     * 주어진 HttpServletRequest에서 특정 쿠키의 값을 반환합니다.
     *
     * @param request HttpServletRequest 객체
     * @param cookieName 찾고자 하는 쿠키의 이름
     * @return 쿠키 값, 쿠키가 존재하지 않으면 null 반환
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
