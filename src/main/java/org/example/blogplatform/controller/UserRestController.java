package org.example.blogplatform.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.blogplatform.domain.RefreshToken;
import org.example.blogplatform.domain.Role;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.dto.UserLoginResponseDto;
import org.example.blogplatform.jwt.util.CookieUtil;
import org.example.blogplatform.jwt.util.JwtTokenizer;
import org.example.blogplatform.security.dto.UserLoginDto;
import org.example.blogplatform.service.RefreshTokenService;
import org.example.blogplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Ylog")
public class UserRestController {
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto loginDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        // username, password가 null이거나 정해진 형식에 맞지 않을 때
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 해당 username으로 유저 정보 가져와서 아이디 비밀번호 맞는지 체크. 맞지 않다면 return
        User user = userService.findByUsername(loginDto.getUsername());
        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList()); // Role타입의 set에서 role의 이름만 들어있는 list가 필요함

        // 액세스토큰 확인. 있을 시 해당 액세스토큰에 대한 유저 정보 반환.
        String chkAccessToken = CookieUtil.getCookieValue(request, "accessToken");
        if (chkAccessToken != null && jwtTokenizer.validateAccessToken(chkAccessToken)) {
            UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, null);
            return redirectUser(response, loginResponse, "/Ylog/" + user.getUsername());
        } else {
            // 없을 시 리프레시토큰 비교
            String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");

            if (refreshToken != null && jwtTokenizer.validateRefreshToken(refreshToken)) {
                chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
                UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, refreshToken);
                addCookies(response, chkAccessToken, refreshToken, user);
                return redirectUser(response, loginResponse, "/Ylog/" + user.getUsername());
            } else {
                // 리프레시토큰이 없는 경우
                refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
                chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);

                // 데이터베이스에 넣을 객체 생성
                RefreshToken refreshTokenEntity = new RefreshToken();
                refreshTokenEntity.setValue(refreshToken);
                refreshTokenEntity.setUserId(user.getId());

                // 기존에 저장되어 있던 리프레시토큰 삭제
                refreshTokenService.deleteRefreshToken(refreshToken);

                // 리프레시토큰 생성
                refreshTokenService.addRefreshToken(refreshTokenEntity);

                UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, refreshToken);
                addCookies(response, chkAccessToken, refreshToken, user);

                return redirectUser(response, loginResponse, "/Ylog/" + user.getUsername());
            }
        }
    }

    private UserLoginResponseDto createLoginResponse(User user, String accessToken, String refreshToken) {
        return new UserLoginResponseDto(user.getUsername(), accessToken, refreshToken);
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

    private ResponseEntity<?> redirectUser(HttpServletResponse response, UserLoginResponseDto loginResponse, String redirectUrl) {
        response.setHeader("Location", redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).body(loginResponse);
    }

}
