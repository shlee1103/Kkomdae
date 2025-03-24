package pizza.kkomdae.security;

import org.springframework.stereotype.Component;

@Component
public class JwtProviderForSpringSecurity extends AbstractJwtProvider {
    public JwtProviderForSpringSecurity(JwtPropertiesForSpringSecurity jwtProperties) {
        super(jwtProperties.getSecret(), jwtProperties.getExpiration());
    }


}
