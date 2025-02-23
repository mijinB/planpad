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
import planpad.planpadapp.config.security.CustomAccessDeniedHandler;
import planpad.planpadapp.config.security.UnauthorizedEntryPoint;
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

    private final UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;
    private final KakaoService kakaoService;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/login").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 인증 정보와 세션 무효화
                            SecurityContextHolder.clearContext();

                            String userToken = request.getHeader("Authorization").replace("Bearer ", "");
                            Map<String, String> message = new HashMap<>();

                            Date expirationDate = jwtTokenProvider.getExpirationFromToken(userToken);
                            jwtBlacklistService.addToBlacklist(userToken, expirationDate.getTime());

                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            message.put("message", "로그아웃에 성공하였습니다.");

                            new ObjectMapper().writeValue(response.getWriter(), message);
                        })
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, jwtBlacklistService, userService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
                );

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
