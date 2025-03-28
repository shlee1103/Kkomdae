package pizza.kkomdae.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.PhotoReq;
import pizza.kkomdae.dto.request.SecondStageReq;
import pizza.kkomdae.dto.request.ThirdStageReq;
import pizza.kkomdae.dto.respond.*;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.s3.S3Service;
import pizza.kkomdae.security.dto.CustomUserDetails;
import pizza.kkomdae.service.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StudentService studentService;
    private final TestResultService testResultService;
    private final PhotoService photoService;
    private final S3Service s3Service;
    private final FlaskService flaskService;
    private final PdfService pdfService;


    @GetMapping("/user-info")
    @Operation(summary = "첫페이지에서 유저의 정보를 조회하는 api", description = "")
    public UserRentTestInfo userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("jwt 토큰 유저 아이디 : {}", userDetails.getUserId());
        return studentService.getUserRentInfo(userDetails.getUserId());
    }

    @PostMapping("/test")
    @Operation(summary = "테스트 저장을 위한 테스트 생성", description = "테스트가 시작될 때 호출, 테스트 아이디를 반환합니다.<br>" +
            "테스트 아이디를 sharedPreference에 저장하고 사진 업로드할 때 사용 바랍니다.<br>" +
            "반납일 경우 serialNum을 넣어주면 됩니다. 그냥 대여일 시 아무 것도 없이 그냥 전송")
    public long initTest(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(required = false) String serialNum) {
        return testResultService.initTest(userDetails.getUserId(), serialNum);
    }


    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse uploadPhoto(
            @RequestPart("photoReq") PhotoReq photoReq,
            @Parameter(required = false, description = "업로드할 이미지 파일 (선택)")
            @RequestPart(value = "image", required = false) MultipartFile image) {

        // 이미지 파일이 없고, photoType이 음수(-1 등)인 경우
        if (photoReq.getPhotoType() < 0) {
            // 테스트 결과의 stage를 2로 업데이트
            photoService.updateStageTo2(photoReq.getTestId());
            return new ApiResponse(true, "파일 없이 stage 2 업데이트 완료");
        } else {
            // 파일이 있을 경우 S3 업로드 및 DB 저장, 분석 요청 진행
            Photo photo = photoService.uploadPhotoSync(photoReq, image);
            photoService.analyzePhoto(photo.getPhotoId());
            return new ApiResponse(true, "사진 업로드 및 저장 성공");
        }
    }

    @GetMapping("photo")
    @Operation(summary = "테스트 id로 테스트의 사진을 얻는 api", description = "List<String>으로 반환")
    public ApiResponse getPhoto(@RequestParam long testId) {
        List<PhotoWithUrl> photoList = testResultService.getPhotos(testId);

        Map<String, String> photoMap = photoList.stream()
                .collect(Collectors.toMap(PhotoWithUrl::getName, PhotoWithUrl::getUrl));

        return new ApiResponse(true, "사진 url 반환 완료", photoMap);
    }

    @Operation(summary = "파일 이름으로 URL 반환", description = "파일 이름으로 url을 돌려받기")
    @GetMapping("/test-file/{file-name}")
    public UrlResponse getFileName(@PathVariable("file-name") String fileName) {
        // S3에서 URL 생성
        String url = s3Service.generatePresignedUrl(fileName);
        // UrlResponse 객체에 담아 반환
        return new UrlResponse(url);
    }

    @Operation(summary = "qr 정보 입력", description = "2단계 qr 정보 입력 및 단계 저장")
    @PostMapping("/secondStage")
    public void secondStage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SecondStageReq secondStageReq) {
        testResultService.secondStage(userDetails, secondStageReq);
    }

    @Operation(summary = "기기 정보 입력", description = "기기 모델명, 시리얼 넘버 등의 정보를 받는 api")
    @PostMapping("/thirdStage")
    public void thirdStage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ThirdStageReq thirdStageReq) {
        testResultService.thirdStage(userDetails, thirdStageReq);
    }

    @Operation(summary = "pdf 생성", description = "")
    @PostMapping("/pdf/{testId}")
    public String makePdf(@PathVariable long testId) {
        return pdfService.makePdf(testId);
    }
}
