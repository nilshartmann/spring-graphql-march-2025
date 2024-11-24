
package nh.springgraphql.graphqlservice.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger( SecurityConfig.class );

    @Bean
    @Order(1)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.NEVER))
            .authorizeHttpRequests(auth -> {
                // Alle Endpunkte auch ohne Authorisierung zulassen.
                //  "Fein-granulare" Security-Prüfung erfolgt in den GraphQL Controller-Methoden
                //  Es wäre aber denkbar, hier andere Endpunkte des Backends (z.B. /graphiql oder Actuator-Endpunkte)
                //  zu schützen
                auth.anyRequest().permitAll();
            })
            .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults()))
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtEncoder jwtEncoder(RSAKeyProvider provider) {
        JWKSet jwkSet = new JWKSet(provider.getRsaKey());
        return new NimbusJwtEncoder((jwkSelector, context) -> jwkSelector.select(jwkSet));
    }

    @Bean
    JwtDecoder jwtDecoder(RSAKeyProvider provider) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(provider.getRsaKey().toRSAPublicKey()).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            // Extract roles from the "roles" claim
            String role = jwt.getClaim("roles");
            if (role != null) {
                    authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;
        });
        return converter;
    }
}