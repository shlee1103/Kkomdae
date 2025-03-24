package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.dto.request.LoginInfo;
import pizza.kkomdae.dto.respond.ApiResponse;
import pizza.kkomdae.service.PhotoService;
import pizza.kkomdae.service.StudentService;
import pizza.kkomdae.service.TestResultService;

@RestController
@RequestMapping("/api")
public class ApiController {// TODO : JWT 전환 예정

    private final StudentService studentService;
    private final TestResultService testResultService;
    private final PhotoService photoService;

    public ApiController(StudentService studentService, TestResultService testResultService, PhotoService photoService) {
        this.studentService = studentService;
        this.testResultService = testResultService;
        this.photoService = photoService;
    }



    @GetMapping("/user-info")
    @Operation(summary = "첫페이지에서 유저의 정보를 조회하는 api", description = "현재 임시적으로 studentId를 받고 있음. 추후 JWT Header로 수정할 예정")
    public ApiResponse userInfo(@RequestParam long studentId) {
        return new ApiResponse(true, "조회 성공", studentService.getUserRentInfo(studentId));
    }

    @PostMapping("/test")
    @Operation(summary = "테스트 저장을 위한 테스트 생성", description = "테스트가 시작될 때 호출, 테스트 아이디를 반환합니다.<br>" +
            "테스트 아이디를 sharedPreference에 저장하고 사진 업로드할 때 사용 바랍니다.")
    public ApiResponse initTest(@RequestParam String email) {
        return new ApiResponse(true, "테스트 생성 성공", testResultService.initTest(email));
    }


    @PostMapping("photo")
    @Operation(summary = "사진 업로드", description = "사진 업로드 및 Ai에 분석 요청 및 사진 단계 업데이트")
    public void uploadPhoto() {
        // TODO 로직에서 해야 할 일 : S3에 업로드, 사진 db에 저장, 피이썬에 요청
        // TODO 마지막 사진 업로드 즉 최종 업로드 이후에는 rent 를 init 하거나 update 하는 로직이 필요함
    }

    @GetMapping("photo")
    @Operation(summary = "테스트 id로 테스트의 사진을 얻는 api", description = "List<String>으로 반환")
    public ApiResponse getPhotos(@RequestParam long testId) {
        return new ApiResponse(true, "결과 사진 조회 성공", testResultService.getPhotos(testId));
    }

    @Operation(summary = "PDF URL 반환", description = "testId로 pdf url을 돌려받기")
    @GetMapping("/test-pdf/{testId}")
    public ApiResponse getPdfUrl(@PathVariable long testId) {
        return new ApiResponse(true, "PDF URL 조회 성공", testResultService.getPdfUrl(testId));
    }

    @Operation(summary = "ai 사진 url update(파이썬 서버용)", description = "python용 s3 ai 이미지 업로드하고 url을 넣는 api")
    @PostMapping("ai-photo")
    public ApiResponse uploadAiPhoto(@RequestBody AiPhotoInfo aiPhotoInfo) {
        photoService.uploadAiPhoto(aiPhotoInfo);
        return new ApiResponse(true, "db에 s3 link 저장 성공");
    }
}
