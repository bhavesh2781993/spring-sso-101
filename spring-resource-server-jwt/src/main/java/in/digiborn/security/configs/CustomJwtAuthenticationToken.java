package in.digiborn.security.configs;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

/**
 * This class completely customizes the JwtAuthenticationToken and provides a way to add any key we want to add.
 */
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    private String bla;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        bla = ":)";
    }

    public String getBla() {
        return bla;
    }
}
