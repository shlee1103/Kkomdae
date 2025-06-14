package pizza.kkomdae.dto.respond;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pizza.kkomdae.entity.Laptop;
import pizza.kkomdae.entity.LaptopTestResult;
import pizza.kkomdae.entity.Rent;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Getter
public class UserRentTestRes {
    private final long rentId;
    private final String modelCode;
    private final String serialNum;
    private final LocalDate dateTime;
    private boolean release;
    private  String rentPdfName;
    private String releasePdfName;
    private long onGoingTestId = 0;
    private int stage = 0;
    private int picStage = 0;

    public UserRentTestRes(Rent rent) {
        this.rentId = rent.getRentId();
        Laptop laptop = (Laptop) rent.getDevice();
        this.modelCode = laptop.getModelCode();
        this.serialNum = laptop.getSerialNum();
        List<LaptopTestResult> laptopTestResults = rent.getLaptopTestResults();

        if (laptopTestResults.get(0).getStage() < 6) { //대여가 진행 중이라면
            LaptopTestResult result = laptopTestResults.get(0);
            this.dateTime = result.getDate();
            this.rentPdfName = result.getPdfFileName();
            this.onGoingTestId = result.getLaptopTestResultId();
            this.stage = result.getStage();
            this.picStage = result.getPicStage();
        }else{
            if (rent.getReleaseDateTime() != null) { // 반납했다면
                this.dateTime = rent.getReleaseDateTime();
                this.release = true;
                this.releasePdfName = laptopTestResults.get(1).getPdfFileName();
                this.rentPdfName = laptopTestResults.get(0).getPdfFileName();
            } else {
                this.rentPdfName = laptopTestResults.get(0).getPdfFileName();
                this.dateTime = rent.getRentDateTime();
                this.release = false;
            }
            if (laptopTestResults.size() > 1 && !rent.getDevice().isRelease()) { // 진행 중인 반납 테스트가 있다면 getRentsByStudentInfo order by를 device Id, laptopId로 해두어서 0번이 대여, 1번이 반납인 것을 확정
                LaptopTestResult result = laptopTestResults.get(1);
                this.release = false;
                this.onGoingTestId = result.getLaptopTestResultId();
                this.stage = result.getStage();
                this.picStage = result.getPhotos().size();
            }
        }
    }
}
