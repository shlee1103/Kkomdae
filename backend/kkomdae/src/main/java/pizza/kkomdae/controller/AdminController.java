package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pizza.kkomdae.dto.request.DeviceCond;
import pizza.kkomdae.dto.request.StudentWithRentCond;
import pizza.kkomdae.dto.respond.DeviceWithStatus;
import pizza.kkomdae.dto.respond.LaptopTestResultWithStudent;
import pizza.kkomdae.dto.respond.StudentWithRent;
import pizza.kkomdae.entity.*;
import pizza.kkomdae.service.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Hidden
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final TestResultService testResultService;
    private final StudentService studentService;
    private final AdminService adminService;
    private final DeviceService deviceService;
    private final RentService rentService;

    // 관리자 로그인 페이지
    @GetMapping("/index")
    public String login() {
        return "index";
    }

    // pdf 정보 입력 페이지 테스트용
    @GetMapping("/pdf-page")
    public String pdfPage() {
        return "pdf-page";
    }

    // adminCode 확인로직
    @PostMapping("/verify")
    public String verify(@RequestParam String adminCode) {
        Admin byCode = adminService.getByCode(adminCode);
        //TODO 레디스 세션 추가
        if (byCode != null) {
            return "redirect:/admin/students";
        } else {
            return "redirect:/error";
        }
    }
    // 세션 삭제
    @PostMapping("/logout")
    public String logout() {
        //Todo 세션 삭제
        return "redirect:/admin/index";
    }

    // 학생목록 + 학생 별 대여 품목
    @GetMapping("/students")
    public String students(StudentWithRentCond studentWithRentCond, Model model) {
        List<StudentWithRent> results = rentService.getRentByStudent(studentWithRentCond);
        log.info("{}", results.size());
        model.addAttribute("students", results);
        return "students";
    }
    

    @GetMapping("/devices")
    public String devices(DeviceCond deviceCond, Model model) {
        List<DeviceWithStatus> results = deviceService.getDevicesWithCond(deviceCond);
        model.addAttribute("deviceList", results);
        return "devices";
    }

    @GetMapping("/test-results")
    public String testResults(HttpServletRequest request, @RequestParam(required = false) Long studentId,
                              @RequestParam(required = false) Long deviceId, @RequestParam String deviceType, Model model) {
        List<LaptopTestResultWithStudent> laptopTestResultWithStudent = testResultService.getByStudentOrDevice(studentId, deviceId, deviceType);
        String referer = request.getHeader("referer");
        model.addAttribute("referer", referer);
        model.addAttribute("laptopTestResultWithStudent", laptopTestResultWithStudent);
        model.addAttribute("studentId", studentId);
        model.addAttribute("deviceId", deviceId);
        return "test-results";
    }

    @GetMapping("/photos")
    public String photos(@RequestParam long testResultId, Model model) {
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("/test.jpg");
        photoUrls.add("/test.jpg");
        photoUrls.add("/test.jpg");
        photoUrls.add("/test.jpg");
        photoUrls.add("/test.jpg");
        log.info("urls size : {}", photoUrls.size());
        model.addAttribute("photoUrls", photoUrls);
        return "photos";
    }

    @GetMapping("/index-hj")
    public String indexHj() {
        return "index-hj";
    }

    @GetMapping("/login-hj")
    public String loginHj() {
        return "login-hj";
    }
}
