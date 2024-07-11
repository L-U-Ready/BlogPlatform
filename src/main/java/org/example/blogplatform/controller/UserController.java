package org.example.blogplatform.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.blogplatform.domain.RefreshToken;
import org.example.blogplatform.domain.Role;
import org.example.blogplatform.domain.SocialLoginInfo;
import org.example.blogplatform.domain.User;
import org.example.blogplatform.dto.UserLoginResponseDto;
import org.example.blogplatform.jwt.util.CookieUtil;
import org.example.blogplatform.jwt.util.JwtTokenizer;
import org.example.blogplatform.security.dto.UserLoginDto;
import org.example.blogplatform.service.RefreshTokenService;
import org.example.blogplatform.service.SocialLoginInfoService;
import org.example.blogplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Ylog")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @GetMapping
    public String YLog() {
        return "YLogs/ylog";
    }

    @GetMapping("/loginform")
    public String showLoginForm(Model model ) {
        model.addAttribute("loginDto", new UserLoginDto());
        return "YLogs/users/loginform";
    }

    //UserLoginDto 에는 username이랑 password만 있음
//    @PostMapping("/login")
//    public String login(@ModelAttribute("loginDto")UserLoginDto loginDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, Model model) {
//        log.info("Login DTO: {}", loginDto);
//
//        if(bindingResult.hasErrors()) {
//            return "redirect:/Ylog/loginform";
//        }
//
//        // 해당 username으로 유저 정보 가져와서 아이디 비밀번호 맞는지 체크. 맞지 않다면 return
//        User user = userService.findByUsername(loginDto.getUsername());
//        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
//            return "redirect:/Ylog/loginform";
//        }
//        log.info("사용자 이름 :::" + user.getUsername());
//        log.info("사용자 이메일 :::" + user.getEmail());
//
//        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList()); // Role타입의 set에서 role의 이름만 들어있는 list가 필요함
//
//        // 액세스토큰 확인. 있을 시 해당 액세스토큰에 대한 유저 정보 반환.
//        String chkAccessToken = CookieUtil.getCookieValue(request, "accessToken");
//        log.info("사용자 엑세스 토큰 :::" + chkAccessToken);
//        if (chkAccessToken != null && jwtTokenizer.validateAccessToken(chkAccessToken)) {
////            UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, null);
//            log.info("엑세스 토큰 벨리데이트 성공");
//            log.info("리다이렉트 URL: /Ylog/" + user.getUsername());
//            return "redirect:/Ylog/" + user.getUsername();
//        } else {
//            // 없을 시 리프레시토큰 비교
//            String refreshToken = CookieUtil.getCookieValue(request, "refreshToken");
//            log.info("사용자 리프레쉬 토큰 ::: " + refreshToken);
//
//            if (refreshToken != null && jwtTokenizer.validateRefreshToken(refreshToken)) {
//                chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
////                UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, refreshToken);
//                addCookies(response, chkAccessToken, refreshToken, user);
//                log.info("리프레쉬  토큰 일치");
//                return "redirect:/Ylog/" + user.getUsername();
//            } else {
//                // 리프레시토큰이 없는 경우
//                refreshToken = jwtTokenizer.createRefreshToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
//                chkAccessToken = jwtTokenizer.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getUsername(), roles);
//
//                log.info("리프레쉬 토큰 ::: " + refreshToken);
//                log.info("체크 엑세스 토큰 :::" + chkAccessToken);
//                // 데이터베이스에 넣을 객체 생성
//                RefreshToken refreshTokenEntity = new RefreshToken();
//                refreshTokenEntity.setValue(refreshToken);
//                refreshTokenEntity.setUserId(user.getId());
//
//                // 기존에 저장되어 있던 리프레시토큰 삭제
//                refreshTokenService.deleteRefreshToken(refreshToken);
//
//                // 리프레시토큰 생성
//                refreshTokenService.addRefreshToken(refreshTokenEntity);
//
////                UserLoginResponseDto loginResponse = createLoginResponse(user, chkAccessToken, refreshToken);
//                addCookies(response, chkAccessToken, refreshToken, user);
//
//                return "redirect:/Ylog/" + user.getUsername();
//            }
//        }
//    }

    @GetMapping("/signupform")
    public String signupform(){
        return "YLogs/users/signupform";
    }

    @PostMapping("/signup")
    public String signUp(
            @ModelAttribute User user,BindingResult result,
            Model model) throws IOException {
        if(result.hasErrors()) {
            model.addAttribute("errorMessage", "다시 입력해주세요.");
            return "error";
        }

        MultipartFile profileImageFile = user.getProfileImageFile();
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String filename = UUID.randomUUID().toString() + "_" + profileImageFile.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/images/profiles/" + filename);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, profileImageFile.getBytes());
            user.setProfileImage(filename);
        } else {
            user.setProfileImage("default.png");
        }

        model.addAttribute("userRegMessage", "Welcome " + user.getUsername());
        userService.createUser(user, passwordEncoder);

        return "redirect:/Ylog";
    }

    private UserLoginResponseDto createLoginResponse(User user, String accessToken, String refreshToken) {
        return new UserLoginResponseDto(user.getUsername(), accessToken, refreshToken);
    }


}
