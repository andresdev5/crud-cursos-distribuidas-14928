package ec.edu.espe.msvc.courses.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(withDefaults());
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/public/**", "/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/courses").hasAnyRole("admin", "user")
                    .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyRole("admin", "user")
                    .requestMatchers(HttpMethod.POST, "/courses").hasRole("admin")
                    .requestMatchers(HttpMethod.PUT, "/courses/**").hasRole("admin")
                    .requestMatchers(HttpMethod.DELETE, "/courses/**").hasRole("admin")

                    .requestMatchers(HttpMethod.GET, "/enrollments").hasAnyRole("admin", "user")
                    .requestMatchers(HttpMethod.GET, "/enrollments/**").hasAnyRole("admin", "user")
                    .requestMatchers(HttpMethod.POST, "/enrollments").hasRole("admin")
                    .requestMatchers(HttpMethod.DELETE, "/enrollments").hasRole("admin")

                    .requestMatchers(HttpMethod.GET, "/enrollments/cancel/**").hasRole("admin")
                    .requestMatchers(HttpMethod.POST, "/enrollments/register-multiple").hasRole("admin")
                    .requestMatchers(HttpMethod.DELETE, "/delete-user-enrollments/**").hasRole("admin")
                    .anyRequest().authenticated()
        );
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false)
                .ignoring()
                .requestMatchers("/public/**", "/actuator/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost",
                "http://20.197.227.154"
        ));
        config.addAllowedHeader("*");
        config.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
        );
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        return JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
