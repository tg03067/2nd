package org.example.second.security;

import lombok.RequiredArgsConstructor;
import org.example.second.security.jwt.JwtAuthenticationAccessDeniedHandler;
import org.example.second.security.jwt.JwtAuthenticationEntryPoint;
import org.example.second.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(https -> https.disable())

                .authorizeHttpRequests(auth -> auth.requestMatchers(
                        // 회원가입, 로그인 인증이 안 되어 있어도 사용가능하게 세팅
                        "/api/user/sign-up",
                        "/api/user/sign-in",
                        "/api/user/access-token",
                        // swagger 사용할 수 있게 세팅
                        "/swagger",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        // 사진
                        "/pic/**",
                        //프론트 화면 보일수 있게 세팅
                        "/",
                        "/index.html",
                        "/css/**",
                        "/static/**",
                        "/js/**",
                        "/fimg/**",
                        "/manifest.json",
                        "/favicon.ico",
                        "/logo192.png",
                        // 프론트에서 사용하는 라우터 주소
                        "/sign-in",
                        "/sign-up",
                        "/profile/*",
                        "/feed" ,

                        //actuator
                        "/actuator",
                        "/actuator/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAuthenticationAccessDeniedHandler()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
