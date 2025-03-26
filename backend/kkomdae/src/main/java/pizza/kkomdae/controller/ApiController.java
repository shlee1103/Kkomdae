package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.dto.request.PhotoReq;
import pizza.kkomdae.dto.respond.ApiResponse;
import pizza.kkomdae.dto.respond.PhotoWithUrl;
import pizza.kkomdae.dto.respond.UserRentTestInfo;
import pizza.kkomdae.dto.respond.UserRentTestRes;
import pizza.kkomdae.s3.S3Service;
import pizza.kkomdae.security.dto.CustomUserDetails;
import pizza.kkomdae.service.PhotoService;
import pizza.kkomdae.service.StudentService;
import pizza.kkomdae.service.TestResultService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private final StudentService studentService;
    private final TestResultService testResultService;
    private final PhotoService photoService;
    private final S3Service s3Service;

    public ApiController(StudentService studentService, TestResultService testResultService, PhotoService photoService, S3Service s3Service) {
        this.studentService = studentService;
        this.testResultService = testResultService;
        this.photoService = photoService;
        this.s3Service = s3Service;
    }


    @GetMapping("/user-info")
    @Operation(summary = "첫페이지에서 유저의 정보를 조회하는 api", description = "")
    public UserRentTestInfo userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("jwt 토큰 유저 아이디 : {}", userDetails.getUserId());
        return studentService.getUserRentInfo(userDetails.getUserId());
    }

    @PostMapping("/test")
    @Operation(summary = "테스트 저장을 위한 테스트 생성", description = "테스트가 시작될 때 호출, 테스트 아이디를 반환합니다.<br>" +
            "테스트 아이디를 sharedPreference에 저장하고 사진 업로드할 때 사용 바랍니다.")
    public long initTest(@RequestParam String email) {
        return testResultService.initTest(email);
    }


    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadPhoto(
            @RequestPart("photoReq") PhotoReq photoReq,
            @RequestPart(value = "image") MultipartFile image) {
        s3Service.upload(photoReq, image);
        // TODO 로직에서 해야 할 일 : S3에 업로드, 사진 db에 저장, 피이썬에 요청
        // TODO 마지막 사진 업로드 즉 최종 업로드 이후에는 rent 를 init 하거나 update 하는 로직이 필요함
    }

    @GetMapping("photo")
    @Operation(summary = "테스트 id로 테스트의 사진을 얻는 api", description = "List<String>으로 반환")
    public List<PhotoWithUrl> getPhotos(@RequestParam long testId) {
        return testResultService.getPhotos(testId);
    }

    @Operation(summary = "PDF URL 반환", description = "testId로 pdf url을 돌려받기")
    @GetMapping("/test-pdf/{testId}")
    public String getPdfUrl(@PathVariable long testId) {
        return testResultService.getPdfUrl(testId);
    }

    @Operation(summary = "ai 사진 url update(파이썬 서버용)", description = "python용 s3 ai 이미지 업로드하고 url을 넣는 api")
    @PostMapping("ai-photo")
    public ApiResponse uploadAiPhoto(@RequestBody AiPhotoInfo aiPhotoInfo) {
        photoService.uploadAiPhoto(aiPhotoInfo);
        return new ApiResponse(true, "db에 s3 link 저장 성공");
    }
}
