package pizza.kkomdae.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pizza.kkomdae.service.StudentService;
import pizza.kkomdae.ssafyapi.SsafySsoService;
import pizza.kkomdae.ssafyapi.SsoAuthToken;
import pizza.kkomdae.ssafyapi.UserRequestForSso;

@RestController
@RequestMapping("/api/sso")
@Slf4j
@RequiredArgsConstructor
public class JwtAuthController {
    private final SsafySsoService ssafySsoService;

    @GetMapping("/login")
    public AuthenticationResponse callback(@RequestParam(required = false) String code, @RequestParam(required = false) String error) {

        log.info(code);
        SsoAuthToken ssoAuthToken = ssafySsoService.getSsoAuthToken(code);
        log.info(ssoAuthToken.toString());
        UserRequestForSso loginUserInfo = ssafySsoService.getLoginUserInfo(ssoAuthToken);
        log.info("{} {} {}", loginUserInfo.getLoginId(), loginUserInfo.getUserId(), loginUserInfo.getName());
        AuthenticationResponse response = ssafySsoService.checkStudentExist(loginUserInfo);

        log.info(response.getJwt());
        log.info(response.getRefreshToken());
        return response;
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refresh(@RequestBody RefreshReq refreshReq) {
        return ssafySsoService.refresh(refreshReq);
    }
}