package pizza.kkomdae.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshReq {
    private String email;
    private String refreshToken;
}
