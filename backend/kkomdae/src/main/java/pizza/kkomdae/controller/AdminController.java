package pizza.kkomdae.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pizza.kkomdae.entity.*;
import pizza.kkomdae.service.AdminService;
import pizza.kkomdae.service.DeviceService;
import pizza.kkomdae.service.StudentService;
import pizza.kkomdae.service.TestResultService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final TestResultService testResultService;
    private final StudentService studentService;
    private final AdminService adminService;
    private final DeviceService deviceService;

    // 관리자 로그인 페이지
    @GetMapping("/index.do")
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
    public String verify(@RequestParam String adminCode, RedirectAttributes redirectAttributes) {
        Admin byCode = adminService.getByCode(adminCode);
        if (byCode != null) {
            return "redirect:/admin/students";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/students")
    public String students(@RequestParam(required = false) String searchType, @RequestParam(required = false) String searchKeyword, Model model) {


        List<Student> results = studentService.findByKeyword(searchType, searchKeyword);

        model.addAttribute("reulstList", results);
        return "students";
    }

    @GetMapping("/test-results")
    public String testResults(@RequestParam long studentId, Model model) {
        List<LaptopTestResult> results = testResultService.getByStudent(studentId);
        model.addAttribute("resultList", results);
        return "test-results";
    }

    @GetMapping("/laptop")
    public String laptops(@RequestParam(required = false) String type, Model model) {
        List<Laptop>results = deviceService.getLaptops();
        model.addAttribute("laptopList", results);
        return "devices";
    }


}
