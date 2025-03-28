package pizza.kkomdae.dto.respond;

import lombok.Getter;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.enums.PhotoType;

@Getter
public class PhotoWithUrl {
    private String name;
    private int type;
    private String url;

    public PhotoWithUrl(Photo photo, String url) {
        this.name = photo.getName();
        this.type = photo.getType();
        this.url = url;
    }

}
