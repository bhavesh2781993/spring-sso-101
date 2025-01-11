package in.digiborn.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient springResourceServerClientRestClient() {
        return RestClient.builder()
            .baseUrl("http://localhost:9090")
            .build();
    }

}
