package planpad.planpadapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import planpad.planpadapp.filter.JwtAuthenticationFilter;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.service.JwtBlacklistService;
import planpad.planpadapp.service.user.KakaoService;
import planpad.planpadapp.service.user.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;
    private final KakaoService kakaoService;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 인증 정보와 세션 무효화
                            SecurityContextHolder.clearContext();

                            String userToken = request.getHeader("Authorization").replace("Bearer ", "");
                            Map<String, String> message = new HashMap<>();

                            if (jwtTokenProvider.validateToken(userToken)) {
                                Date expirationDate = jwtTokenProvider.getExpirationFromToken(userToken);
                                jwtBlacklistService.addToBlacklist(userToken, expirationDate);

                                Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(userToken));
                                String accessToken = userService.getUserById(userId).getAccessToken();
                                kakaoService.kakaoLogout(accessToken);

                                message.put("message", "로그아웃에 성공하였습니다.");
                                log.info("로그아웃에 성공하였습니다.");
                            } else {
                                message.put("message", "토큰이 만료되었거나 유효하지 않습니다.");
                                log.info("토큰이 만료되었거나 유효하지 않습니다.");
                            }

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);

                            new ObjectMapper().writeValue(response.getWriter(), message);
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, jwtBlacklistService, userService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));  // 허용할 Origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));  // 허용할 헤더
        configuration.setAllowCredentials(true);  // 쿠키와 인증정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 대해 CORS 설정

        return source;
    }
}
