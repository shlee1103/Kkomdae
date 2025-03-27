package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.dto.request.FlaskRequest;
import pizza.kkomdae.dto.request.PhotoReq;
import pizza.kkomdae.dto.respond.FlaskResponse;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.repository.PhotoRepository;
import pizza.kkomdae.repository.laptopresult.LapTopTestResultRepository;
import pizza.kkomdae.s3.S3Service;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final LapTopTestResultRepository lapTopTestResultRepository;
    private final S3Service s3Service;
    private final FlaskService flaskService;

    // 사진 업로드와 DB 저장을 위한 메서드
    @Transactional
    public Photo uploadPhotoSync(PhotoReq photoreq, MultipartFile image) {
        // S3 업로드 후 파일 이름 반환
        String s3FileName = s3Service.upload(photoreq, image);
        if (s3FileName == null) {
            throw new RuntimeException("Failed to upload photo");
        }

        //Photo entity 생성 및 필드 설정
        Photo photo = new Photo();
        // 파일명
        String fileNameWithoutPrefix = s3FileName.replace("lkm7ln/", "");

        photo.setName(fileNameWithoutPrefix);

        // 타입 설정
        photo.setType(photoreq.getPhotoType());
        LaptopTestResult test = lapTopTestResultRepository.getReferenceById(photoreq.getTestId());
        photo.setLaptopTestResult(test);

        photoRepository.save(photo);

        return photo;
    }

    @Async
    public void analyzePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("no photo found"));

        // 2) Flask 서버 분석 요청
        // FlaskRequest 객체 생성
        // s3Key, Url 등은 상황에 맞게 photo가 가진 정보로 세팅
        FlaskRequest flaskRequest = new FlaskRequest();
        flaskRequest.setS3Key(photo.getName());

        // Flask 서버로 분석 요청
        FlaskResponse response = flaskService.analyzeImage(flaskRequest);

        // 3) AI 분석 결과 DB 업데이트
        photo.setAiName(response.getUploadName());
        photo.setDamage(response.getDamage());

        photoRepository.save(photo);
    }
}
