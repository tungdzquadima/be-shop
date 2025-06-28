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
                        // Cho phép truy cập không cần đăng nhập (anonymous)
                        .requestMatchers(
                                "/api/v1/users/register",
                                "/api/v1/users/login",
                                "/api/v1/products/category/**",
                                "/api/v1/products/{id}",     // lưu ý: sẽ match nếu dùng đúng định dạng trong controller
                                "/api/v1/categories/getAll",
                                "/api/v1/products/search**",
                                "/api/v1/products/search"
                        ).permitAll()

                        // Các route yêu cầu xác thực và phân quyền
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyRole(Role.ADMIN, Role.USER)
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(Role.ADMIN)
                        //.requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{id}/status").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/cancel/**").hasAnyRole(Role.USER, Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/order_details/**").hasAnyRole(Role.USER, Role.ADMIN)

                        .requestMatchers(HttpMethod.GET, "/api/v1/brands").hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/brands").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/brands/**").hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/brands/**").hasRole(Role.ADMIN)

                        // Các đường còn lại yêu cầu xác thực
                        .anyRequest().authenticated()
                );


        return http.build();
    }


    // chatgpt dạy:
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);  // nếu dùng cookie hoặc JWT qua cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        return source;
    }
}
