package pizza.kkomdae.dto.respond;

import lombok.Getter;
import pizza.kkomdae.entity.Photo;
import pizza.kkomdae.enums.PhotoType;

@Getter
public class PhotoWithUrl {
    private String name;
    private PhotoType type;

    public PhotoWithUrl(Photo photo) {
        this.name = photo.getName();
        this.type = photo.getType();
    }
}
