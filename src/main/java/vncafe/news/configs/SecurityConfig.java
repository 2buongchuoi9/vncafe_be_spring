package vncafe.news.configs;

import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import vncafe.news.security.CustomOAuth2UserService;
import vncafe.news.security.HttpCookieOAuth2AuthorizationRequestRepository;
import vncafe.news.security.OAuth2LoginSuccessHandler;
import vncafe.news.security.UserRootService;
import vncafe.news.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  final private UserRootService userRootService;
  final private JwtAuthenticationFilter jwtAuthFilter;
  final private CustomOAuth2UserService customOAuth2UserService;
  final private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  final private HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userRootService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setUseSecureCookie(false); // This line disables the Secure attribute
    return serializer;
  }

  @Bean
  public SessionRepository sessionRepository() {
    MapSessionRepository sessionRepository = new MapSessionRepository(new ConcurrentHashMap<>());
    sessionRepository.setDefaultMaxInactiveInterval(Duration.ofMinutes(30)); // 1800 seconds = 30 minutes
    return sessionRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            req -> req
                .requestMatchers("/**").permitAll())
        .formLogin(f -> f.disable())
        .httpBasic(v -> v.disable())
        .oauth2Login(o -> o
            .authorizationEndpoint(
                v -> v
                    .baseUri("/api/v1/oauth2/authorization")
                    .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository))
            .redirectionEndpoint(v -> v.baseUri("/api/v1/oauth2/callback/*"))
            .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(STATELESS));

    return http.build();
  }
}