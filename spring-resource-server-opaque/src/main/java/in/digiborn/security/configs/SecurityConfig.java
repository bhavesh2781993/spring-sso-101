package in.digiborn.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2ResourceServer(resourceServerConfig ->
            resourceServerConfig
                .opaqueToken(tokenConfig -> tokenConfig
                    .introspectionUri("http://localhost:8080/oauth2/introspect")
                    .introspectionClientCredentials("client", "secret")
                    //.authenticationConverter() // To customize authentication object with provided authentication
                ));

        httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        return httpSecurity.build();
    }

}
