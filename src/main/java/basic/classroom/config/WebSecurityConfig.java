package basic.classroom.config;

import basic.classroom.config.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private final String[] staticResources = new String[]{"/css/**", "/js/**", "/*.ico", "/img/**"};
    private final String[] loginURL = new String[]{"/login/**", "/logout", "/api/login/**"};
    private final String[] basicURL = new String[]{"/", "/error/**", "/ending/credit"};
    private final String[] instructorURL = new String[]{"/instructor/**", "/api/members/instructor/**"};
    private final String[] studentURL = new String[]{"/student/**", "/api/members/student/**"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((httpSecurityCsrfConfigurer) -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers(staticResources).permitAll()
                                .requestMatchers(basicURL).permitAll()
                                .requestMatchers(loginURL).permitAll()
                                .requestMatchers(instructorURL).hasAuthority("INSTRUCTOR")
                                .requestMatchers(studentURL).hasAuthority("STUDENT")
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutUrl("/logout").addLogoutHandler(((request, response, authentication) -> {
                    for (Cookie cookie : request.getCookies()) {
                        String cookieName = cookie.getName();
                        Cookie deleteCookie = new Cookie(cookieName, null);
                        deleteCookie.setMaxAge(0);
                        response.addCookie(deleteCookie);
                    }
                })))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
