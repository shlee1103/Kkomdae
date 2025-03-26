package pizza.kkomdae.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pizza.kkomdae.dto.request.PhotoReq;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${s3.bucketName}")
    private String bucketName;
    @Value("${s3.prefix}")
    private String prefix;

    private final AmazonS3 amazonS3;


    public void upload(PhotoReq photoReq, MultipartFile image) {
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
        }
    }

    private String generateS3Key(PhotoReq photoReq, MultipartFile image) {
        return prefix+photoReq.getTestId()+"/"+photoReq.getPhotoType()+"_"+image.getOriginalFilename();
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }
}
