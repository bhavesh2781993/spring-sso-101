package in.digiborn.security.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(name = "id")
    private Integer id;

    @NaturalId
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "secret")
    private String secret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "auth_method")
    private String authMethod;

    @Column(name = "grant_type")
    private String grantType;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

    public static Client from(RegisteredClient registeredClient) {
        Client client = new Client();
        client.setClientId(registeredClient.getClientId());
        client.setSecret(registeredClient.getClientSecret());
        String redirectUri = registeredClient.getRedirectUris()
            .stream()
            .findAny()
            .orElse(null);
        client.setRedirectUri(redirectUri);
        String scope = registeredClient.getScopes()
            .stream()
            .findAny()
            .orElse(null);
        client.setScope(scope);

        String authMethod = registeredClient.getClientAuthenticationMethods()
            .stream()
            .map(ClientAuthenticationMethod::getValue)
            .findAny()
            .orElse(null);
        client.setAuthMethod(authMethod);

        String grantType = registeredClient.getAuthorizationGrantTypes()
            .stream()
            .map(AuthorizationGrantType::getValue)
            .findAny()
            .orElse(null);
        client.setGrantType(grantType);
        return client;
    }

    public static RegisteredClient from(Client client) {
        return RegisteredClient.withId(String.valueOf(client.getId()))
            .clientId(client.getClientId())
            .clientSecret(client.getSecret())
            .scope(client.getScope())
            .redirectUri(client.getRedirectUri())
            .clientAuthenticationMethod(new ClientAuthenticationMethod(client.getAuthMethod()))
            .authorizationGrantType(new AuthorizationGrantType(client.getGrantType()))
            .build();
    }
}
