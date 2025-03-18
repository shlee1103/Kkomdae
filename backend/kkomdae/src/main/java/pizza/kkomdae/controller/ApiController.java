package pizza.kkomdae.controller;

import org.springframework.web.bind.annotation.*;
import pizza.kkomdae.dto.request.LoginInfo;
import pizza.kkomdae.dto.respond.ApiResponse;
import pizza.kkomdae.service.StudentService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StudentService studentService;

    public ApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginInfo loginInfo) {
        return new ApiResponse(true, "로그인 성공",studentService.login(loginInfo));
    }

    @GetMapping("/user-info")
    public void userInfo(){
        studentService.getUserRentInfo();
    }

    @PostMapping("/test")
    public void initTest() {

    }

    @PostMapping("photo")
    public void uploadPhoto() {

    }

    @GetMapping("photo")
    public void getPhotos() {

    }

    @GetMapping("/test-pdf/{testId}")
    public void getPdfUrl(@PathVariable long testId) {

    }
}
