package pizza.kkomdae.ssafyapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pizza.kkomdae.service.StudentService;

@Slf4j
@Controller
public class SsafyLoginController {
    private final SsafySsoService ssafySsoService;
    private final StudentService studentService;

    public SsafyLoginController(SsafySsoService ssafySsoService, StudentService studentService) {
        this.ssafySsoService = ssafySsoService;
        this.studentService = studentService;
    }


    @GetMapping("callback")
    public String callback(@RequestParam(required = false) String code, @RequestParam(required = false) String error) {
        if (code != null) {
            log.info(code);
            SsoAuthToken ssoAuthToken = ssafySsoService.getSsoAuthToken(code);
            log.info(ssoAuthToken.toString());
            UserRequestForSso loginUserInfo = ssafySsoService.getLoginUserInfo(ssoAuthToken);
            log.info("{} {} {}", loginUserInfo.getLoginId(), loginUserInfo.getUserId(), loginUserInfo.getName());
            studentService.checkStudentExist(loginUserInfo);
        }


        if (error != null) {
            return "redirect:/login";
        }
        return "redirect:/admin/students";
    }

    @GetMapping("/login")
    public String ssafyLogin() {
        return "index-ss.html";
    }

    @GetMapping("/token")
    public String token() {
        return "redirect:/admin/students";
    }
}
