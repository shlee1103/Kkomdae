package pizza.kkomdae.dto.respond;

import lombok.Getter;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Rent;

import java.time.LocalDate;

@Getter
public class UserRentTestRes {
    private final String modelCode;
    private final LocalDate dateTime;
    private boolean release;
    private final String rentPdfUrl;
    private String releasePdfUrl;
    private long onGoingTestId = 0;
    private int stage = 0;
    private int picStage = 0;

    public UserRentTestRes(Rent rent) {
        this.modelCode = rent.getDevice().getModelCode();
        this.rentPdfUrl = rent.getDevice().getLaptopTestResults().get(0).getPdfFileName();
        if (rent.getReleaseDateTime() != null) { // 반납했다면
            this.dateTime = rent.getReleaseDateTime();
            this.release = true;
            this.releasePdfUrl = rent.getDevice().getLaptopTestResults().get(1).getPdfFileName();
        } else {
            this.dateTime = rent.getRentDateTime();
            this.release = false;
        }
        if (rent.getDevice().getLaptopTestResults().get(1) != null) { // 진행 중인 테스트가 있다면 getRentsByStudentInfo order by를 device Id, laptopId로 해두어서 0번이 대여, 1번이 반납인 것을 확정
            LaptopTestResult result = rent.getDevice().getLaptopTestResults().get(1);
            this.onGoingTestId = result.getLaptopTestResultId();
            this.stage = result.getStage();
            this.picStage = result.getPhotos().size();
        }
    }
}
