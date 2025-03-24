package pizza.kkomdae.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pizza.kkomdae.service.StudentService;
import pizza.kkomdae.ssafyapi.SsafySsoService;
import pizza.kkomdae.ssafyapi.SsoAuthToken;
import pizza.kkomdae.ssafyapi.UserRequestForSso;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JwtAuthController {
    private final StudentService studentService;
    private final JwtProviderForSpringSecurity jwtProvider;
    private final SsafySsoService ssafySsoService;

    @GetMapping("callback")
    public AuthenticationResponse callback(@RequestParam(required = false) String code, @RequestParam(required = false) String error) {

        log.info(code);
        SsoAuthToken ssoAuthToken = ssafySsoService.getSsoAuthToken(code);
        log.info(ssoAuthToken.toString());
        UserRequestForSso loginUserInfo = ssafySsoService.getLoginUserInfo(ssoAuthToken);
        log.info("{} {} {}", loginUserInfo.getLoginId(), loginUserInfo.getUserId(), loginUserInfo.getName());
        long studentId = studentService.checkStudentExist(loginUserInfo);
        AuthenticationResponse response = new AuthenticationResponse(jwtProvider.generateToken(studentId));
        log.info(response.getJwt());
        return response;
    }
}