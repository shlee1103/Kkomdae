package pizza.kkomdae.dto.respond;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Rent;

import java.time.LocalDateTime;

@Getter
public class UserTestResultRes {
    private String modelCode;
    private LocalDateTime dateTime;
    private boolean release;
    private String rentPdfUrl;
    private String releasePdfUrl;

    public UserTestResultRes(Rent rent) {
        this.modelCode = rent.getDevice().getModelCode();
        this.rentPdfUrl = rent.getDevice().getLaptopTestResults().get(0).getPdfUrl();
        if (rent.getReleaseDateTime() != null) { // 반납했다면
            this.dateTime= rent.getReleaseDateTime();
            this.release = true;
            this.releasePdfUrl=rent.getDevice().getLaptopTestResults().get(1).getPdfUrl();
        }else{
            this.dateTime= rent.getRentDateTime();
            this.release=false;
        }
    }
}
