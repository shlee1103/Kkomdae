package pizza.kkomdae.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pizza.kkomdae.dto.request.SecondStageReq;
import pizza.kkomdae.dto.request.TestResultReq;
import pizza.kkomdae.dto.request.ThirdStageReq;
import pizza.kkomdae.dto.respond.LaptopTestResultRes;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LaptopTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laptopTestResultId;
    @ManyToOne(fetch = FetchType.LAZY)
    Device device;
    private Boolean release;

    // 키보드 테스트 관련
    private Boolean keyboardStatus;
    @Column(length = 1000)
    private String failedKeys; // TODO 입력 값에 따라 수정해야함
    // USB 테스트 관련
    private Boolean usbStatus;
    private String failedPorts; // TODO 입력 값에 따라 수정해야함
    // 카메라 테스트 관련
    private Boolean cameraStatus;
    // 충전기 테스트 관련
    private Boolean chargerStatus;
    // 배터리 테스트 관련
    private Boolean batteryReport;
    private String batteryReportUrl;
    private String batteryReportSummary;
    
    private LocalDate date;
    private String pdfFileName;
    private Integer laptop;
    private Integer powerCable;
    private Integer adapter;
    private Integer mouse;
    private Integer bag;
    private Integer mousePad;
    private Integer stage = 1;
    private Integer picStage = 0;
    private String randomKey;
    private String description;
    private Integer sumOfDamages;

    @OneToMany(mappedBy = "laptopTestResult")
    private List<Photo> photos;
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    private Rent rent;

    public LaptopTestResult(Student student) {
        this.release = false;
        this.student = student;
    }

    public void saveSecondStage(SecondStageReq secondStageReq) {
        this.stage = 3;
        this.keyboardStatus = secondStageReq.isKeyboardStatus();
        this.failedKeys = secondStageReq.getFailedKeys();
        this.usbStatus = secondStageReq.isUsbStatus();
        this.failedPorts = secondStageReq.getFailedPorts();
        this.cameraStatus = secondStageReq.isCameraStatus();
        this.chargerStatus = secondStageReq.isChargerStatus();
        this.batteryReport = secondStageReq.isBatteryReport();
        this.batteryReportUrl = secondStageReq.getBatteryReportUrl();
    }

    public void saveThirdStage(ThirdStageReq thirdStageReq) {
        this.stage = 4;
        this.release = thirdStageReq.isRelease();
        this.date = thirdStageReq.getLocalDate();
        this.laptop = thirdStageReq.getLaptop();
        this.powerCable = thirdStageReq.getPowerCable();
        this.adapter = thirdStageReq.getAdapter();
        this.mouse = thirdStageReq.getMouse();
        this.bag = thirdStageReq.getBag();
        this.mousePad = thirdStageReq.getMousePad();
    }

    public void updateTestResult(TestResultReq testResultReq) {
        switch (testResultReq.getTestType()) {
            case "키보드" -> {
                this.keyboardStatus = testResultReq.isSuccess();
                this.failedKeys = testResultReq.isSuccess() ? null : testResultReq.getDetail();
            }
            case "카메라" -> {
                this.cameraStatus = testResultReq.isSuccess();
            }
            case "USB" -> {
                this.usbStatus = testResultReq.isSuccess();
                this.failedPorts = testResultReq.isSuccess() ? null : testResultReq.getDetail();
            }
            case "충전" -> {
                this.chargerStatus = testResultReq.isSuccess();
            }
            case "배터리" -> {
                this.batteryReport = testResultReq.isSuccess();
                this.batteryReportUrl = testResultReq.isSuccess() ? testResultReq.getDetail() : null;
                this.batteryReportSummary = testResultReq.isSuccess() ? testResultReq.getSummary() : null;
            }
            default -> throw new IllegalArgumentException("유효하지 않은 테스트 타입입니다: " + testResultReq.getTestType());
        }
    
//        // 테스트 결과가 모두 완료되었는지 확인하고 stage 업데이트
//        if (isAllTestCompleted()) {
//            this.stage = 3;  // 모든 테스트가 완료되면 다음 단계로
//        }
    }

    public boolean isAllTestCompleted() {
        return keyboardStatus != null &&
            cameraStatus != null &&
            usbStatus != null &&
            chargerStatus != null &&
            batteryReport != null;
    }
}