package pizza.kkomdae.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.AiPhotoInfo;
import pizza.kkomdae.dto.request.PhotoReq;
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

    // 사진 업로드와 DB 저장을 위한 메서드
    @Transactional
    public void uploadPhoto(PhotoReq photoreq, MultipartFile image) {
        // S3 업로드 후 파일 이름 반환
        String s3FileName = s3Service.upload(photoreq, image);
        if (s3FileName == null) {
            throw new RuntimeException("Failed to upload photo");
        }

        //Photo entity 생성 및 필드 설정
        Photo photo = new Photo();
        // 파일명
        photo.setName(image.getOriginalFilename());
        // 타입 설정
        photo.setType(photoreq.getPhotoType());

        photoRepository.save(photo);
    }
}
