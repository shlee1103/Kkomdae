package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.dto.request.FlaskRequest;
import pizza.kkomdae.dto.request.LoginInfo;
import pizza.kkomdae.dto.request.PhotoReq;
import pizza.kkomdae.dto.request.SecondStageReq;
import pizza.kkomdae.dto.respond.ApiResponse;
import pizza.kkomdae.dto.respond.PhotoWithUrl;
import pizza.kkomdae.dto.respond.UserRentTestInfo;
import pizza.kkomdae.dto.respond.UserRentTestRes;
import pizza.kkomdae.dto.respond.FlaskResponse;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.s3.S3Service;
import pizza.kkomdae.security.dto.CustomUserDetails;
import pizza.kkomdae.service.FlaskService;
//import pizza.kkomdae.service.PhotoService;
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
    private final FlaskService flaskService;

    public ApiController(StudentService studentService, TestResultService testResultService, S3Service s3Service, FlaskService flaskService, PhotoService photoService) {
        this.studentService = studentService;
        this.testResultService = testResultService;
        this.s3Service = s3Service;
        this.flaskService = flaskService;
        this.photoService = photoService;
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
    public long initTest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return testResultService.initTest(userDetails.getUserId());
    }


    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse uploadPhoto(
            @RequestPart("photoReq") PhotoReq photoReq,
            @RequestPart(value = "image") MultipartFile image) {
        // s3에 원본 사진 업로드 및 db에 저장
        Photo photo = photoService.uploadPhotoSync(photoReq, image);
        // 비동기로 flask 서버에 요청
        photoService.analyzePhoto(photo.getPhotoId());
        return new ApiResponse(true, "사진 업로드 및 저장 성공");
        // TODO 로직에서 해야 할 일 : S3에 업로드, 사진 db에 저장, 피이썬에 요청
        // TODO 마지막 사진 업로드 즉 최종 업로드 이후에는 rent 를 init 하거나 update 하는 로직이 필요함
    }

    @PostMapping("/analyze-photo")
    @Operation(summary = "Flask 서버로 사진 분석 요청", description = "Flask 서버에 JSON 데이터를 전송하고 분석 결과를 반환받습니다.")
    public ApiResponse analyzePhoto(@RequestBody FlaskRequest flaskRequest) {
        FlaskResponse flaskResponse = flaskService.analyzeImage(flaskRequest);
        return new ApiResponse(true, "사진 분석 성공", flaskResponse);
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

    @Operation(summary = "qr 정보 입력", description = "2단계 qr 정보 입력 및 단계 저장")
    public void secondStage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SecondStageReq secondStageReq) {
        testResultService.secondStage(userDetails, secondStageReq);
    }

}
