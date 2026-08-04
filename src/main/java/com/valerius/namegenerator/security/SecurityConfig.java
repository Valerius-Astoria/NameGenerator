package com.valerius.namegenerator.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Application security: email/password form login backed by {@link AppUserDetailsService}.
 *
 * <p>Only the login/register pages, static assets, and the H2 console are public;
 * everything else (design, generate, history) requires an authenticated writer.</p>
 *
 * <p>Sessions are in-memory and the app is hosted on a free tier that restarts often
 * (idle spin-down, redeploys), so authentication and CSRF must survive a restart:
 * CSRF tokens live in a cookie rather than the session, and a remember-me cookie keeps
 * the writer signed in. If a CSRF check still fails (e.g. a very stale tab), the user
 * is sent back to the design page instead of a bare 403.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final String rememberMeKey;

    public SecurityConfig(
            AppUserDetailsService appUserDetailsService,
            @Value("${app.remember-me-key}") String rememberMeKey) {
        this.appUserDetailsService = appUserDetailsService;
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/error").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/design")
                        .permitAll())
                .rememberMe(remember -> remember
                        .key(rememberMeKey)
                        .alwaysRemember(true)
                        .userDetailsService(appUserDetailsService))
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?loggedOut"))
                // Cookie-based CSRF survives server restarts (session-based tokens do not).
                // The H2 console posts without CSRF tokens and renders in a frame.
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                        .ignoringRequestMatchers("/h2-console/**"))
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler((request, response, ex) ->
                                response.sendRedirect(request.getContextPath() + "/design")))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
