package org.example.aceditorbackend.config;

import org.example.aceditorbackend.security.CustomAuthenticationEntryPoint;
import org.example.aceditorbackend.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authBuilder.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://127.0.0.1:4200",
                "https://aceditor-536318037598.us-central1.run.app",
                "http://localhost"
        ));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                )
//                .formLogin(form -> form
//                        .successHandler(new CustomAuthenticationSuccessHandler())
//                        .failureHandler(new CustomAuthenticationFailureHandler())
//                )
//                .logout(logout -> logout
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                )
        ;

        return http.build();
    }


    @Bean
    @Profile("staging")
    public SecurityFilterChain stagingSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/users/**", "/api/files/**", "/api/folders/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                )
//                .formLogin(form -> form
//                        .successHandler(new CustomAuthenticationSuccessHandler())
//                        .failureHandler(new CustomAuthenticationFailureHandler())
//                )
//                .logout(logout -> logout
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                )
        ;

        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/users/**", "/api/files/**", "/api/folders/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                )
//                .formLogin(form -> form
//                        .successHandler(new CustomAuthenticationSuccessHandler())
//                        .failureHandler(new CustomAuthenticationFailureHandler())
//                )
//                .logout(logout -> logout
//                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
//                )
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}