package pizza.kkomdae.controller;

import org.springframework.web.bind.annotation.*;
import pizza.kkomdae.dto.request.LoginInfo;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/login")
    public void login(@RequestBody LoginInfo loginInfo) {

    }

    @GetMapping("/user-info")
    public void userInfo(){

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
