package pizza.kkomdae.dto.respond;

import lombok.Getter;
import pizza.kkomdae.entity.Rent;

import java.time.LocalDateTime;

@Getter
public class UserTestResultRes {
    private final String modelCode;
    private final LocalDateTime dateTime;
    private Boolean release;
    private final String rentPdfUrl;
    private String releasePdfUrl;

    public UserTestResultRes(Rent rent) {
        this.modelCode = rent.getDevice().getModelCode();
        this.rentPdfUrl = rent.getDevice().getLaptopTestResults().get(0).getPdfUrl();
        if (rent.getReleaseDateTime() != null) { // 반납했다면
            this.dateTime = rent.getReleaseDateTime();
            this.release = true;
            this.releasePdfUrl = rent.getDevice().getLaptopTestResults().get(1).getPdfUrl();
        } else {
            this.dateTime = rent.getRentDateTime();
            this.release = false;
        }
    }
}
