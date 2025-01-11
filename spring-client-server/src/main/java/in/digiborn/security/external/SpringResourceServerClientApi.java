package in.digiborn.security.external;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Objects;

import in.digiborn.security.exceptions.UnauthenticatedException;
import in.digiborn.security.exceptions.UnauthorizedException;

@Component
@RequiredArgsConstructor
public class SpringResourceServerClientApi {

    private final OAuth2AuthorizedClientManager clientManager;
    private final RestClient springResourceServerClientRestClient;

    public String demo() {
        try {
            return springResourceServerClientRestClient.get()
                .uri("/demo")
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .body(String.class);
        } catch (RestClientException ex) {
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    private String getToken() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
            .withClientRegistrationId("1")
            .principal("client")
            .build();
        OAuth2AuthorizedClient authorizedClient = clientManager.authorize(request);

        if (Objects.isNull(authorizedClient)
            || Objects.isNull(authorizedClient.getAccessToken())) {
            throw new UnauthenticatedException("Unable to get the token");
        }

        return authorizedClient.getAccessToken().getTokenValue();
    }

}
