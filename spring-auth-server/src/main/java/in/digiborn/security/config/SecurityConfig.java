package in.digiborn.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationValidator;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        // Open ID configuration
        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .authorizationEndpoint(config -> config.authenticationProviders(getAuthorizationEndpointProviders()))
//            .tokenEndpoint(config -> config.authenticationProviders())
            .oidc(Customizer.withDefaults());

        // Default entry point in case of any errors
        httpSecurity.exceptionHandling(e -> e.authenticationEntryPoint(
            new LoginUrlAuthenticationEntryPoint("/login")
        ));

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(Customizer.withDefaults())
            .authorizeHttpRequests(request -> request.anyRequest().authenticated());

        return httpSecurity.build();
    }

    /**
     * This helps with in memory user creation for poc purpose.
     * @return
     */
    /*@Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password("password")
            .authorities("read")
            .build();
        return new InMemoryUserDetailsManager(user);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * This helps with in memory client creation for poc purpose.
     * @return
     */
    /*@Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret("secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .redirectUri("https://springone.io/authorized")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .tokenSettings(TokenSettings.builder()
                // SELF_CONTAINED => NON-OPAQUE
                // REFERENCE => OPAQUE
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(1))
                .refreshTokenTimeToLive(Duration.ofMinutes(2))
                .build())
            .build();
        return new InMemoryRegisteredClientRepository(client);
    }*/

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * JSON Web Token Key Source.
     * Helps generate private key to sign the token and public key to verify the signature.
     * @return returns JWKSource
     * @throws NoSuchAlgorithmException throws exception in case no such algorithm is found.
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();

        JWKSet set = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(set);
    }

    /**
     * Method to modify claim and provide custom values.
     * @return returns OAuth2TokenCustomizer
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return context -> {
            context.getClaims().claim("test", "test");
            Collection<? extends GrantedAuthority> grantedAuthorities = context.getPrincipal().getAuthorities();
            List<String> authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            context.getClaims().claim("authorities", authorities);
        };
    }

    /**
     * Provides custom authentication provider and adds to the list of existing authentication providers.
     * @return returns List of authentication providers.
     */
    private Consumer<List<AuthenticationProvider>> getAuthorizationEndpointProviders() {
        return providers -> {
            for (AuthenticationProvider p : providers) {
                if (p instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider x) {
                    var authenticationValidator = new CustomRedirectUriValidator()
                        .andThen(OAuth2AuthorizationCodeRequestAuthenticationValidator.DEFAULT_SCOPE_VALIDATOR);
                    x.setAuthenticationValidator(authenticationValidator);
                }
            }
        };
    }
}
