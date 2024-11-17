package in.digiborn.security.configs;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * This class helps modify the JwtAuthenticationToken.
 * This can only make change to existing fields defined by AbstractAuthenticationToken.
 */
public class CustomJwtAuthenticationTokenConverter implements Converter<Jwt, CustomJwtAuthenticationToken> {

    @Override
    public CustomJwtAuthenticationToken convert(final Jwt source) {
        List<String> authorities = (List<String>) source.getClaims().get("authorities");
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

        return new CustomJwtAuthenticationToken(source, grantedAuthorities);
    }
}
