package in.digiborn.security.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${auth.server.jwks.uri}")
    private String jwksUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2ResourceServer(resourceServerConfig ->
            resourceServerConfig.jwt(jwtConfig -> jwtConfig
                .jwkSetUri(jwksUri)
        ));

        httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        return httpSecurity.build();
    }

}
