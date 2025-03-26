package pizza.kkomdae.dto.request;

import lombok.Getter;
import pizza.kkomdae.enums.PhotoType;
@Getter
public class PhotoReq {
    private PhotoType photoType;
    private long testId;
}
