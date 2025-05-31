package com.example.shopapp.configuration;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSercurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/**") // áp dụng cho tất cả các đường dẫn còn lại (ngoài /api/**)
//                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(request -> {
//                    request.requestMatchers(
//                                    "/api/v1/users/register",
//                                    "/api/v1/users/login"
//                            )
//                            .permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole(Role.USER)
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
//
//                            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(HttpMethod.GET, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
//                            .requestMatchers(HttpMethod.POST, "/api/v1/order_details/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/order_details/**").hasAnyRole(Role.ADMIN)
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/order_details/**").hasAnyRole(Role.ADMIN)
//                            .anyRequest().authenticated();
//                });
//        return http.build();
//    }


    // chatgpt
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                // bật CORS, bạn nhớ cấu hình bean corsConfigurationSource() bên ngoài
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API không giữ session
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Mở cho mọi người truy cập (không cần xác thực)
                        .requestMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll()

                        // Phân quyền chi tiết theo role
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole(Role.USER)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/order_details/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/order_details/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/order_details/**").hasRole(Role.ADMIN)

                        // Các đường dẫn còn lại bắt buộc phải xác thực
                        .anyRequest().authenticated()
                );

        return http.build();
    }


    // chatgpt dạy:
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);  // nếu dùng cookie hoặc JWT qua cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        return source;
    }
}
