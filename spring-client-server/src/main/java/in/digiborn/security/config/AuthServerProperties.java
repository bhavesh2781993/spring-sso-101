package in.digiborn.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "auth.server")
public class AuthServerProperties {

    private String clientId;
    private String clientSecret;
    private String tokenUri;

}
