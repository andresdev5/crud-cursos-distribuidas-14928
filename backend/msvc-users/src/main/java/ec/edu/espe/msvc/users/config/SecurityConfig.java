package ec.edu.espe.msvc.users.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
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
    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    @Autowired
    private KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(withDefaults());
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/public/**", "/actuator/**").permitAll()
                    .anyRequest().authenticated()
        );
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

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
    Collection generateAuthoritiesFromClaim(Collection roles) {
        return (Collection) roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                Collectors.toList());
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();

                // Tokens can be configured to return roles under
                // Groups or REALM ACCESS hence have to check both
                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
                    var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                } else if (userInfo.hasClaim(GROUPS)) {
                    Collection<String> roles = (Collection<String>) userInfo.getClaim(
                            GROUPS);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
                            REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }
            return mappedAuthorities;
        };
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
