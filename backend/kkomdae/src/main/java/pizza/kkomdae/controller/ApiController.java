package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import pizza.kkomdae.dto.request.LoginInfo;
import pizza.kkomdae.dto.respond.ApiResponse;
import pizza.kkomdae.service.StudentService;

@RestController
@RequestMapping("/api")
public class ApiController {// TODO : JWT 전환 예정

    private final StudentService studentService;

    public ApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginInfo loginInfo) {
        return new ApiResponse(true, "로그인 성공", studentService.login(loginInfo));
    }

    @GetMapping("/user-info")
    @Operation(summary = "첫페이지에서 유저의 정보를 조회하는 api", description = "현재 임시적으로 studentId를 받고 있음. 추후 JWT로 수정할 예정")
    public ApiResponse userInfo(@RequestParam long studentId) {
        return new ApiResponse(true, "조회 성공", studentService.getUserRentInfo(studentId));
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
