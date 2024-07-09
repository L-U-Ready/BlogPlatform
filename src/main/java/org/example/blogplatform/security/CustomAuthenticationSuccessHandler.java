package org.example.blogplatform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.RefreshToken;
import org.example.blogplatform.domain.Role;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.jwt.util.CookieUtil;
import org.example.blogplatform.jwt.util.JwtTokenizer;
import org.example.blogplatform.service.RefreshTokenService;
import org.example.blogplatform.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String chkAccessToken = CookieUtil.getCookieValue(request, "accessToken");

        if (chkAccessToken != null && jwtTokenizer.validateAccessToken(chkAccessToken)) {
            log.info("Access token validated successfully");
            response.sendRedirect("/Ylog/" + username);
            return;
        }

        String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");

        if (refreshToken != null && jwtTokenizer.validateRefreshToken(refreshToken)) {
            chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
            addCookies(response, chkAccessToken, refreshToken, user);
            log.info("Refresh token validated and new access token created");
        } else {
            refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
            chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);

            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setValue(refreshToken);
            refreshTokenEntity.setUserId(user.getId());

            refreshTokenService.deleteRefreshToken(refreshToken);
            refreshTokenService.addRefreshToken(refreshTokenEntity);

            addCookies(response, chkAccessToken, refreshToken, user);
            log.info("New refresh token and access token created");
        }

        response.sendRedirect("/Ylog/" + username);
    }

    private void addCookies(HttpServletResponse response, String accessToken, String refreshToken, User user) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000)); //30분 쿠키의 유지시간 단위는 초 ,  JWT의 시간단위는 밀리세컨드

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT/1000));

        Cookie username = new Cookie("username", user.getUsername());
        username.setPath("/");
        username.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));

        response.addCookie(username);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

    }
}