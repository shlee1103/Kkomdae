package pizza.kkomdae.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.PhotoReq;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${s3.bucketName}")
    private String bucketName;
    @Value("${s3.prefix}")
    private String prefix;

    private final AmazonS3 amazonS3;


    public String upload(PhotoReq photoReq, MultipartFile image) {
        // S3 key 생성: prefix + testId + "/" + photoType + "_" + originalFilename
        String s3Key = generateS3Key(photoReq,image);
        ObjectMetadata metadata = createMetadata(image);

        try (InputStream inputStream = image.getInputStream()) {
            PutObjectRequest request = new PutObjectRequest(
                    bucketName,
                    s3Key,
                    inputStream,
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(request);
        }catch (IOException e){
            e.printStackTrace();
            //오류 발생 시 null 반환
            return null;
        }
        return s3Key;
    }
    // S3 Key 생성 메서드
    private String generateS3Key(PhotoReq photoReq, MultipartFile image) {
        return prefix+photoReq.getTestId()+"_"+photoReq.getPhotoType()+"_"+image.getOriginalFilename();
    }

    // 메타데이터 생성 메서드
    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    /**
     * 파일 이름을 바탕으로 presigned URL을 생성하고 반환하는 메서드
     *
     * @param fileName S3에 업로드된 파일의 key
     * @return 유효시간 내에 접근 가능한 presigned URL
     */
    public String generatePresignedUrl(String fileName) {
        Date expiration = new Date();
        long expirationTime = expiration.getTime();
        // 유효시간 5분 설정
        expirationTime += 1000*60*5;
        expiration.setTime(expirationTime);

        // GeneratePresignedUrlRequest 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, prefix + fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        // PresignedUrl 생성
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }
}
