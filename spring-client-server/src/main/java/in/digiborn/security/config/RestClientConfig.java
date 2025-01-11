package in.digiborn.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${resource.server.baseUrl}")
    private String resourceServerBaseUrl;

    @Bean
    public RestClient springResourceServerClientRestClient() {
        return RestClient.builder()
            .baseUrl(resourceServerBaseUrl)
            .build();
    }

}
