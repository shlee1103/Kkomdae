package pizza.kkomdae.dto.request;


import lombok.Getter;
import pizza.kkomdae.enums.PhotoType;

@Getter
public class AiPhotoInfo {
    private String s3PicUrl;
    private long testId;
    private long photoId;
    private PhotoType type;
    private String name;
}
