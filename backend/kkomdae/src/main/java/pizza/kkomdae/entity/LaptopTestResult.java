package pizza.kkomdae.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class LaptopTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laptopTestResultId;
    @ManyToOne
    Device device;
    private boolean release;
    private boolean keyboardStatus;
    private String failedKeys; // TODO 입력 값에 따라 수정해야함
    private boolean usbStatus;
    private String failedPorts; // TODO 입력 값에 따라 수정해야함
    private boolean cameraStatus;
    private boolean chargerStatus;
    private boolean batteryReport;
    private String batteryReportUrl;
    private LocalDate date;
    private String pdfUrl;
    private int step;
    @OneToMany(mappedBy = "laptopTestResult")
    private List<Photo> photos;
    // Todo ai 결과를 어떻게 저장할 지 고민
}
